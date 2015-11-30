/****************************************************************************
Copyright 2007, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Parallel;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Local estimates of orientations of features in images.
 * Methods of this class can compute for each image sample numerous
 * parameters related to orientation. All orientation information 
 * is derived from eigenvectors and eigenvalues of the structure tensor
 * (also called the "gradient-squared tensor"). This tensor is equivalent 
 * to a matrix of 2nd partial derivatives of an autocorrelation evaluated 
 * at zero lag. In other words, orientation is here determined by the 
 * (2-D) ellipse or (3-D) ellipsoid that best fits the peak of the 
 * autocorrelation of image samples in a local window.
 * <p>
 * The coordinate system for a 2-D image has two orthogonal axes 1 and 2, 
 * which correspond to the 1st and 2nd indices of the array containing 
 * image samples. For 2-D images, the eigenvectors are the unit vectors 
 * u = (u1,u2) and v = (v1,v2). The 1st eigenvector u is perpendicular 
 * to the best fitting line, and the 1st component u1 of u is always 
 * non-negative. The 2nd eigenvector v is perpendicular to u such that 
 * the cross product u1*v2-u2*v1 = 1; that is, v1 = -u2 and v2 = u1. 
 * The angle theta = asin(u2) is the angle measured counter-clockwise 
 * between the 1st eigenvector u and axis 1; -pi/2 &lt;= theta &lt;= pi/2.
 * <p>
 * The coordinate system for a 3-D image has three orthogonal axes 1, 2 
 * and 3, which correspond to the 1st, 2nd and 3rd indices of the array 
 * containing image samples. For 3-D images, the eigenvectors are unit 
 * vectors u = (u1,u2,u3), v = (v1,v2,v3), and w = (w1,w2,w3). The 1st 
 * eigenvector u is orthogonal to the best fitting plane, and the 1st 
 * component u1 of u is always non-negative. The 2nd eigenvector v is 
 * orthogonal to the best fitting line within the best fitting plane.
 * The 3rd eigenvector w is orthogonal to both u and v and is aligned
 * with the direction in which the images changes least. The dip angle 
 * theta = acos(u1) is the angle between the 1st eigenvector u and axis 1; 
 * 0 &lt;= theta &lt;= pi/2. The azimuthal angle phi = atan2(u3,u2)
 * is well-defined for only non-zero theta; -pi &lt;= phi &lt;= pi.
 * <p>
 * The local linearity or planarity of features is determined by the
 * eigenvalues. For 2-D images with eigenvalues eu and ev (corresponding 
 * to the eigenvectors u and v), linearity is (eu-ev)/eu. For 3-D
 * images with eigenvalues eu, ev, and ew, planarity is (eu-ev)/eu
 * and linearity is (ev-ew)/eu. Both linearity and planarity are
 * in the range [0,1].
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.02.12
 */
public class LocalOrientFilter {

  /**
   * Constructs a filter with an isotropic Gaussian window.
   * @param sigma half-width of window; same for all dimensions.
   */
  public LocalOrientFilter(double sigma) {
    this(sigma,sigma,sigma);
  }

  
  /**
   * Constructs a filter with a possibly anisotropic Gaussian window.
   * @param sigma1 half-width of window in 1st dimension.
   * @param sigma2 half-width of window in 2nd and higher dimensions.
   */
  public LocalOrientFilter(double sigma1, double sigma2) {
    this(sigma1,sigma2,sigma2);
  }

  /**
   * Constructs a filter with a possibly anisotropic Gaussian window.
   * @param sigma1 half-width of window in 1st dimension.
   * @param sigma2 half-width of window in 2nd dimension.
   * @param sigma3 half-width of window in 3rd and higher dimensions.
   */
  public LocalOrientFilter(double sigma1, double sigma2, double sigma3) {
    _rgfSmoother1 = (sigma1>=1.0)?new RecursiveGaussianFilter(sigma1):null;
    if (sigma2==sigma1) {
      _rgfSmoother2 = _rgfSmoother1;
    } else {
      _rgfSmoother2 = (sigma2>=1.0)?new RecursiveGaussianFilter(sigma2):null;
    }
    if (sigma3==sigma2) {
      _rgfSmoother3 = _rgfSmoother2;
    } else {
      _rgfSmoother3 = (sigma3>=1.0)?new RecursiveGaussianFilter(sigma3):null;
    }
    setGradientSmoothing(1.0);
  }

  /**
   * Sets half-width of Gaussian derivative filter used to compute gradients.
   * Typically, this half-width should not exceed one-fourth that of the
   * the corresponding Gaussian window used to compute local averages of 
   * gradient products.
   * The default half-width for Gaussian derivatives is 1.0.
   * @param sigma half-width of derivatives; same for all dimensions.
   */
  public void setGradientSmoothing(double sigma) {
    setGradientSmoothing(sigma,sigma,sigma);
  }

  /**
   * Sets half-widths of Gaussian derivative filters used to compute gradients.
   * Typically, these half-widths should not exceed one-fourth those of the
   * the corresponding Gaussian windows used to compute local averages of 
   * gradient-squared tensors.
   * The default half-widths for Gaussian derivatives is 1.0.
   * @param sigma1 half-width of derivative in 1st dimension.
   * @param sigma2 half-width of derivatives in 2nd and higher dimensions.
   */
  public void setGradientSmoothing(double sigma1, double sigma2) {
    setGradientSmoothing(sigma1,sigma2,sigma2);
  }

  /**
   * Sets half-widths of Gaussian derivative filters used to compute gradients.
   * Typically, these half-widths should not exceed one-fourth those of the
   * the corresponding Gaussian windows used to compute local averages of 
   * gradient-squared tensors.
   * The default half-widths for Gaussian derivatives is 1.0.
   * @param sigma1 half-width of derivative in 1st dimension.
   * @param sigma2 half-width of derivative in 2nd dimension.
   * @param sigma3 half-width of derivatives in 3rd and higher dimensions.
   */
  public void setGradientSmoothing(
    double sigma1, double sigma2, double sigma3) 
  {
    _rgfGradient1 = new RecursiveGaussianFilter(sigma1);
    if (sigma2==sigma1) {
      _rgfGradient2 = _rgfGradient1;
    } else {
      _rgfGradient2 = new RecursiveGaussianFilter(sigma2);
    }
    if (sigma3==sigma2) {
      _rgfGradient3 = _rgfGradient2;
    } else {
      _rgfGradient3 = new RecursiveGaussianFilter(sigma3);
    }
  }
  
  /**
   * Applies this filter to estimate orientation angles.
   * @param x input array for 2-D image.
   * @param theta orientation angle; -pi &lt;= theta &lt;= pi
   */
  public void applyForTheta(float[][] x, float[][] theta) {
    apply(x,
      theta,
      null,null,
      null,null,
      null,null,
      null);
  }

  /**
   * Applies this filter to estimate normal vectors (1st eigenvectors).
   * @param x input array for 2-D image.
   * @param u1 1st component of normal vector.
   * @param u2 2nd component of normal vector.
   */
  public void applyForNormal(float[][] x, float[][] u1, float[][] u2) {
    apply(x,
      null,
      u1,u2,
      null,null,
      null,null,
      null);
  }

  /**
   * Applies this filter to estimate normal vectors and linearities.
   * @param x input array for 2-D image.
   * @param u1 1st component of normal vector.
   * @param u2 2nd component of normal vector.
   * @param el linearity in range [0,1].
   */
  public void applyForNormalLinear(float[][] x, 
    float[][] u1, float[][] u2, float[][] el) 
  {
    apply(x,
      null,
      u1,u2,
      null,null,
      null,null,
      el);
  }

  /**
   * Applies this filter to estimate 2-D structure tensors.
   * @param x input array for 2-D image.
   * @return structure tensors.
   */
  public EigenTensors2 applyForTensors(float[][] x) {
    int n1 = x[0].length;
    int n2 = x.length;
    float[][] u1 = new float[n2][n1];
    float[][] u2 = new float[n2][n1];
    float[][] eu = new float[n2][n1];
    float[][] ev = new float[n2][n1];
    apply(x,
      null,
      u1,u2,
      null,null,
      eu,ev,
      null);
    return new EigenTensors2(u1,u2,eu,ev);
  }

  /**
   * Applies this filter for the specified image and outputs. All
   * outputs are optional and are computed for only non-null arrays.
   * @param x input array for 2-D image
   * @param theta orientation angle = asin(u2); -pi &lt;= theta &lt;= pi
   * @param u1 1st component of 1st eigenvector.
   * @param u2 2nd component of 1st eigenvector.
   * @param v1 1st component of 2nd eigenvector.
   * @param v2 2nd component of 2nd eigenvector.
   * @param eu largest eigenvalue corresponding to the eigenvector u.
   * @param ev smallest eigenvalue corresponding to the eigenvector v.
   * @param el (eu-ev)/eu, a measure of linearity.
   */
  public void apply(float[][] x,
    float[][] theta,
    float[][] u1, float[][] u2, 
    float[][] v1, float[][] v2,
    float[][] eu, float[][] ev, 
    float[][] el)
  {
    // Where possible, use output arrays for workspace.
    float[][][] t = new float[8][][];
    int nt = 0;
    if (theta!=null) t[nt++] = theta;
    if (u1!=null) t[nt++] = u1;
    if (u2!=null) t[nt++] = u2;
    if (v1!=null) t[nt++] = v1;
    if (v2!=null) t[nt++] = v2;
    if (eu!=null) t[nt++] = eu;
    if (ev!=null) t[nt++] = ev;
    if (el!=null) t[nt++] = el;

    // Gradient.
    int n1 = x[0].length;
    int n2 = x.length;
    float[][] g1 = (nt>0)?t[0]:new float[n2][n1];
    float[][] g2 = (nt>1)?t[1]:new float[n2][n1];
    _rgfGradient1.apply10(x,g1);
    _rgfGradient2.apply01(x,g2);

    // Gradient products.
    float[][] g11 = g1;
    float[][] g22 = g2;
    float[][] g12 = (nt>2)?t[2]:new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        float g1i = g1[i2][i1];
        float g2i = g2[i2][i1];
        g11[i2][i1] = g1i*g1i;
        g22[i2][i1] = g2i*g2i;
        g12[i2][i1] = g1i*g2i;
      }
    }
    
    // Smoothed gradient products comprise the structure tensor.
    if (_rgfSmoother1!=null || _rgfSmoother2!=null) {
      float[][] h = (nt>3)?t[3]:new float[n2][n1];
      float[][][] gs = {g11,g22,g12};
      for (float[][] g:gs) {
        if (_rgfSmoother1!=null) {
          _rgfSmoother1.apply0X(g,h);
        } else {
          copy(g,h);
        }
        if (_rgfSmoother2!=null) {
          _rgfSmoother2.applyX0(h,g);
        } else {
          copy(h,g);
        }
      }
    }

    // Compute eigenvectors, eigenvalues, and outputs that depend on them.
    float[][] a = new float[2][2];
    float[][] z = new float[2][2];
    float[] e = new float[2];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        a[0][0] = g11[i2][i1];
        a[0][1] = g12[i2][i1];
        a[1][0] = g12[i2][i1];
        a[1][1] = g22[i2][i1];
        Eigen.solveSymmetric22(a,z,e);
        float u1i = z[0][0];
        float u2i = z[0][1];
        if (u1i<0.0f) {
          u1i = -u1i;
          u2i = -u2i;
        }
        float v1i = -u2i;
        float v2i = u1i;
        float eui = e[0];
        float evi = e[1];
        if (evi<0.0f) evi = 0.0f;
        if (eui<evi) eui = evi;
        if (theta!=null) theta[i2][i1] = asin(u2i);
        if (u1!=null) u1[i2][i1] = u1i;
        if (u2!=null) u2[i2][i1] = u2i;
        if (v1!=null) v1[i2][i1] = v1i;
        if (v2!=null) v2[i2][i1] = v2i;
        if (eu!=null) eu[i2][i1] = eui;
        if (ev!=null) ev[i2][i1] = evi;
        if (el!=null) el[i2][i1] = (eui-evi)/eui;
      }
    }
  }

  /**
   * Applies this filter to estimate orientation angles.
   * @param x input array for 3-D image.
   * @param theta orientation dip angle; 0 &lt;= theta &lt;= pi/2.
   * @param phi orientation azimuthal angle; -pi &lt;= phi &lt;= pi.
   */
  public void applyForThetaPhi(float[][][] x, 
    float[][][] theta, float[][][] phi) 
  {
    apply(x,
      theta,phi,
      null,null,null,
      null,null,null,
      null,null,null,
      null,null,null,
      null,null);
  }

  /**
   * Applies this filter to estimate normal vectors (1st eigenvectors).
   * @param x input array for 3-D image.
   * @param u1 1st component of normal vector.
   * @param u2 2nd component of normal vector.
   * @param u3 3rd component of normal vector.
   */
  public void applyForNormal(float[][][] x, 
    float[][][] u1, float[][][] u2, float[][][] u3) 
  {
    apply(x,
      null,null,
      u1,u2,u3,
      null,null,null,
      null,null,null,
      null,null,null,
      null,null);
  }

  /**
   * Applies this filter to estimate normal vectors and planarities.
   * Normal vectors are 1st eigenvectors corresponding to largest eigenvalues.
   * @param x input array for 3-D image.
   * @param u1 1st component of normal vector.
   * @param u2 2nd component of normal vector.
   * @param u3 3rd component of normal vector.
   * @param ep planarity in range [0,1].
   */
  public void applyForNormalPlanar(float[][][] x, 
    float[][][] u1, float[][][] u2, float[][][] u3, float[][][] ep) 
  {
    apply(x,
      null,null,
      u1,u2,u3,
      null,null,null,
      null,null,null,
      null,null,null,
      ep,null);
  }

  /**
   * Applies this filter to estimate inline vectors (3rd eigenvectors).
   * @param x input array for 3-D image.
   * @param w1 1st component of inline vector.
   * @param w2 2nd component of inline vector.
   * @param w3 3rd component of inline vector.
   */
  public void applyForInline(float[][][] x, 
    float[][][] w1, float[][][] w2, float[][][] w3)
  {
    apply(x,
      null,null,
      null,null,null,
      null,null,null,
      w1,w2,w3,
      null,null,null,
      null,null);
  }

  /**
   * Applies this filter to estimate inline vectors and linearities.
   * Inline vectors are 3rd eigenvectors corresponding to smallest eigenvalues.
   * @param x input array for 3-D image.
   * @param w1 1st component of inline vector.
   * @param w2 2nd component of inline vector.
   * @param w3 3rd component of inline vector.
   * @param el linearity in range [0,1].
   */
  public void applyForInlineLinear(float[][][] x, 
    float[][][] w1, float[][][] w2, float[][][] w3,
    float[][][] el) 
  {
    apply(x,
      null,null,
      null,null,null,
      null,null,null,
      w1,w2,w3,
      null,null,null,
      null,el);
  }

  /**
   * Applies this filter to estimate compressed 3-D structure tensors.
   * @param x input array for 3-D image.
   * @return structure tensors.
   */
  public EigenTensors3 applyForTensors(float[][][] x) {
    return applyForTensors(x,true);
  }

  /**
   * Applies this filter to estimate 3-D structure tensors.
   * @param x input array for 3-D image.
   * @param compressed true, for compressed tensors; false, otherwise.
   * @return structure tensors.
   */
  public EigenTensors3 applyForTensors(float[][][] x, boolean compressed) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    float[][][] u2 = new float[n3][n2][n1];
    float[][][] u3 = new float[n3][n2][n1];
    float[][][] w1 = new float[n3][n2][n1];
    float[][][] w2 = new float[n3][n2][n1];
    float[][][] eu = new float[n3][n2][n1];
    float[][][] ev = new float[n3][n2][n1];
    float[][][] ew = new float[n3][n2][n1];
    apply(x,
      null,null,
      null,u2,u3,
      null,null,null,
      w1,w2,null,
      eu,ev,ew,
      null,null);

    // Compute u1 such that u3 > 0.
    float[][][] u1 = u3;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float u2i = u2[i3][i2][i1];
          float u3i = u3[i3][i2][i1];
          float u1s = 1.0f-u2i*u2i-u3i*u3i;
          float u1i = (u1s>0.0f)?sqrt(u1s):0.0f;
          if (u3i<0.0f) {
            u1i = -u1i;
            u2i = -u2i;
          }
          u1[i3][i2][i1] = u1i;
          u2[i3][i2][i1] = u2i;
        }
      }
    }
    return new EigenTensors3(u1,u2,w1,w2,eu,ev,ew,compressed);
  }

  /**
   * Applies this filter for the specified image and outputs. All
   * outputs are optional and are computed for only non-null arrays.
   * @param x input array for 3-D image.
   * @param theta orientation dip angle; 0 &lt;= theta &lt;= pi/2.
   * @param phi orientation azimuthal angle; -pi &lt;= phi &lt;= pi.
   * @param u1 1st component of 1st eigenvector.
   * @param u2 2nd component of 1st eigenvector.
   * @param u3 3rd component of 1st eigenvector.
   * @param v1 1st component of 2nd eigenvector.
   * @param v2 2nd component of 2nd eigenvector.
   * @param v3 3rd component of 2nd eigenvector.
   * @param w1 1st component of 3rd eigenvector.
   * @param w2 2nd component of 3rd eigenvector.
   * @param w3 3rd component of 3rd eigenvector.
   * @param eu largest eigenvalue corresponding to the eigenvector u.
   * @param ev middle eigenvalue corresponding to the eigenvector v.
   * @param ew smallest eigenvalue corresponding to the eigenvector w.
   * @param ep (eu-ev)/eu, a measure of planarity.
   * @param el (ev-ew)/eu, a measure of linearity.
   */
  public void apply(float[][][] x,
    float[][][] theta, float[][][] phi,
    float[][][] u1, float[][][] u2, float[][][] u3, 
    float[][][] v1, float[][][] v2, float[][][] v3, 
    float[][][] w1, float[][][] w2, float[][][] w3, 
    float[][][] eu, float[][][] ev, float[][][] ew, 
    float[][][] ep, float[][][] el)
  {
    // Where possible, use output arrays for workspace.
    float[][][][] t = new float[16][][][];
    int nt = 0;
    if (theta!=null) t[nt++] = theta;
    if (phi!=null) t[nt++] = phi;
    if (u1!=null) t[nt++] = u1;
    if (u2!=null) t[nt++] = u2;
    if (u3!=null) t[nt++] = u3;
    if (v1!=null) t[nt++] = v1;
    if (v2!=null) t[nt++] = v2;
    if (v3!=null) t[nt++] = v3;
    if (w1!=null) t[nt++] = w1;
    if (w2!=null) t[nt++] = w2;
    if (w3!=null) t[nt++] = w3;
    if (eu!=null) t[nt++] = eu;
    if (ev!=null) t[nt++] = ev;
    if (ew!=null) t[nt++] = ew;
    if (ep!=null) t[nt++] = ep;
    if (el!=null) t[nt++] = el;

    // Gradient.
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    float[][][] g1 = (nt>0)?t[0]:new float[n3][n2][n1];
    float[][][] g2 = (nt>1)?t[1]:new float[n3][n2][n1];
    float[][][] g3 = (nt>2)?t[2]:new float[n3][n2][n1];
    _rgfGradient1.apply100(x,g1);
    _rgfGradient2.apply010(x,g2);
    _rgfGradient3.apply001(x,g3);

    // Gradient products.
    float[][][] g11 = g1;
    float[][][] g22 = g2;
    float[][][] g33 = g3;
    float[][][] g12 = (nt>3)?t[3]:new float[n3][n2][n1];
    float[][][] g13 = (nt>4)?t[4]:new float[n3][n2][n1];
    float[][][] g23 = (nt>5)?t[5]:new float[n3][n2][n1];
    computeGradientProducts(g1,g2,g3,g11,g12,g13,g22,g23,g33);
    /*
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float g1i = g1[i3][i2][i1];
          float g2i = g2[i3][i2][i1];
          float g3i = g3[i3][i2][i1];
          g11[i3][i2][i1] = g1i*g1i;
          g22[i3][i2][i1] = g2i*g2i;
          g33[i3][i2][i1] = g3i*g3i;
          g12[i3][i2][i1] = g1i*g2i;
          g13[i3][i2][i1] = g1i*g3i;
          g23[i3][i2][i1] = g2i*g3i;
        }
      }
    }
    */
    
    // Smoothed gradient products comprise the structure tensor.
    if (_rgfSmoother1!=null || _rgfSmoother2!=null || _rgfSmoother3!=null) {
      float[][][] h = (nt>6)?t[6]:new float[n3][n2][n1];
      float[][][][] gs = {g11,g22,g33,g12,g13,g23};
      for (float[][][] g:gs) {
        if (_rgfSmoother1!=null) {
          _rgfSmoother1.apply0XX(g,h);
        } else {
          copy(g,h);
        }
        if (_rgfSmoother2!=null) {
          _rgfSmoother2.applyX0X(h,g);
        } else {
          copy(h,g);
        }
        if (_rgfSmoother3!=null) {
          _rgfSmoother3.applyXX0(g,h);
          copy(h,g);
        }
      }
    }

    // Compute eigenvectors, eigenvalues, and outputs that depend on them.
    solveEigenproblems(g11,g12,g13,g22,g23,g33,
      theta,phi,u1,u2,u3,v1,v2,v3,w1,w2,w3,eu,ev,ew,ep,el);
    /*
    float[][] a = new float[3][3];
    float[][] z = new float[3][3];
    float[] e = new float[3];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          a[0][0] = g11[i3][i2][i1];
          a[0][1] = g12[i3][i2][i1];
          a[0][2] = g13[i3][i2][i1];
          a[1][0] = g12[i3][i2][i1];
          a[1][1] = g22[i3][i2][i1];
          a[1][2] = g23[i3][i2][i1];
          a[2][0] = g13[i3][i2][i1];
          a[2][1] = g23[i3][i2][i1];
          a[2][2] = g33[i3][i2][i1];
          Eigen.solveSymmetric33(a,z,e);
          float u1i = z[0][0];
          float u2i = z[0][1];
          float u3i = z[0][2];
          float v1i = z[1][0];
          float v2i = z[1][1];
          float v3i = z[1][2];
          if (u1i<0.0f) {
            u1i = -u1i;
            u2i = -u2i;
            u3i = -u3i;
          }
          if (v2i<0.0f) {
            v1i = -v1i;
            v2i = -v2i;
            v3i = -v3i;
          }
          float w1i = u2i*v3i-u3i*v2i;
          float w2i = u3i*v1i-u1i*v3i;
          float w3i = u1i*v2i-u2i*v1i;
          float eui = e[0];
          float evi = e[1];
          float ewi = e[2];
          if (ewi<0.0f) ewi = 0.0f;
          if (evi<ewi) evi = ewi;
          if (eui<evi) eui = evi;
          if (theta!=null) theta[i3][i2][i1] = acos(u1i);
          if (phi!=null) phi[i3][i2][i1] = atan2(u3i,u2i);
          if (u1!=null) u1[i3][i2][i1] = u1i;
          if (u2!=null) u2[i3][i2][i1] = u2i;
          if (u3!=null) u3[i3][i2][i1] = u3i;
          if (v1!=null) v1[i3][i2][i1] = v1i;
          if (v2!=null) v2[i3][i2][i1] = v2i;
          if (v3!=null) v3[i3][i2][i1] = v3i;
          if (w1!=null) w1[i3][i2][i1] = w1i;
          if (w2!=null) w2[i3][i2][i1] = w2i;
          if (w3!=null) w3[i3][i2][i1] = w3i;
          if (eu!=null) eu[i3][i2][i1] = eui;
          if (ev!=null) ev[i3][i2][i1] = evi;
          if (ew!=null) ew[i3][i2][i1] = ewi;
          if (ep!=null || el!=null) {
            float esi = (eui>0.0f)?1.0f/eui:1.0f;
            if (ep!=null) ep[i3][i2][i1] = (eui-evi)*esi;
            if (el!=null) el[i3][i2][i1] = (evi-ewi)*esi;
          }
        }
      }
    }
    */
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private RecursiveGaussianFilter _rgfGradient1;
  private RecursiveGaussianFilter _rgfGradient2;
  private RecursiveGaussianFilter _rgfGradient3;
  private RecursiveGaussianFilter _rgfSmoother1;
  private RecursiveGaussianFilter _rgfSmoother2;
  private RecursiveGaussianFilter _rgfSmoother3;

  private void computeGradientProducts(
    final float[][][] g1, final float[][][] g2, final float[][][] g3,
    final float[][][] g11, final float[][][] g12, final float[][][] g13,
    final float[][][] g22, final float[][][] g23, final float[][][] g33)
  {
    final int n1 = g1[0][0].length;
    final int n2 = g1[0].length;
    final int n3 = g1.length;
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        for (int i2=0; i2<n2; ++i2) {
          float[] g1i = g1[i3][i2];
          float[] g2i = g2[i3][i2];
          float[] g3i = g3[i3][i2];
          float[] g11i = g11[i3][i2];
          float[] g12i = g12[i3][i2];
          float[] g13i = g13[i3][i2];
          float[] g22i = g22[i3][i2];
          float[] g23i = g23[i3][i2];
          float[] g33i = g33[i3][i2];
          for (int i1=0; i1<n1; ++i1) {
            float g1ii = g1i[i1];
            float g2ii = g2i[i1];
            float g3ii = g3i[i1];
            g11i[i1] = g1ii*g1ii;
            g22i[i1] = g2ii*g2ii;
            g33i[i1] = g3ii*g3ii;
            g12i[i1] = g1ii*g2ii;
            g13i[i1] = g1ii*g3ii;
            g23i[i1] = g2ii*g3ii;
          }
        }
      }
    });
  }

  private void solveEigenproblems(
    final float[][][] g11, final float[][][] g12, final float[][][] g13,
    final float[][][] g22, final float[][][] g23, final float[][][] g33,
    final float[][][] theta, final float[][][] phi,
    final float[][][] u1, final float[][][] u2, final float[][][] u3, 
    final float[][][] v1, final float[][][] v2, final float[][][] v3, 
    final float[][][] w1, final float[][][] w2, final float[][][] w3, 
    final float[][][] eu, final float[][][] ev, final float[][][] ew, 
    final float[][][] ep, final float[][][] el)
  {
    final int n1 = g11[0][0].length;
    final int n2 = g11[0].length;
    final int n3 = g11.length;
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        double[][] a = new double[3][3];
        double[][] z = new double[3][3];
        double[] e = new double[3];
        for (int i2=0; i2<n2; ++i2) {
          for (int i1=0; i1<n1; ++i1) {
            a[0][0] = g11[i3][i2][i1];
            a[0][1] = g12[i3][i2][i1];
            a[0][2] = g13[i3][i2][i1];
            a[1][0] = g12[i3][i2][i1];
            a[1][1] = g22[i3][i2][i1];
            a[1][2] = g23[i3][i2][i1];
            a[2][0] = g13[i3][i2][i1];
            a[2][1] = g23[i3][i2][i1];
            a[2][2] = g33[i3][i2][i1];
            Eigen.solveSymmetric33(a,z,e);
            float u1i = (float)z[0][0];
            float u2i = (float)z[0][1];
            float u3i = (float)z[0][2];
            float v1i = (float)z[1][0];
            float v2i = (float)z[1][1];
            float v3i = (float)z[1][2];
            float w1i = (float)z[2][0];
            float w2i = (float)z[2][1];
            float w3i = (float)z[2][2];
            if (u1i<0.0f) {
              u1i = -u1i;
              u2i = -u2i;
              u3i = -u3i;
            }
            if (v2i<0.0f) {
              v1i = -v1i;
              v2i = -v2i;
              v3i = -v3i;
            }
            if (w3i<0.0f) {
              w1i = -w1i;
              w2i = -w2i;
              w3i = -w3i;
            }
            float eui = (float)e[0];
            float evi = (float)e[1];
            float ewi = (float)e[2];
            if (ewi<0.0f) ewi = 0.0f;
            if (evi<ewi) evi = ewi;
            if (eui<evi) eui = evi;
            if (theta!=null) theta[i3][i2][i1] = acos(u1i);
            if (phi!=null) phi[i3][i2][i1] = atan2(u3i,u2i);
            if (u1!=null) u1[i3][i2][i1] = u1i;
            if (u2!=null) u2[i3][i2][i1] = u2i;
            if (u3!=null) u3[i3][i2][i1] = u3i;
            if (v1!=null) v1[i3][i2][i1] = v1i;
            if (v2!=null) v2[i3][i2][i1] = v2i;
            if (v3!=null) v3[i3][i2][i1] = v3i;
            if (w1!=null) w1[i3][i2][i1] = w1i;
            if (w2!=null) w2[i3][i2][i1] = w2i;
            if (w3!=null) w3[i3][i2][i1] = w3i;
            if (eu!=null) eu[i3][i2][i1] = eui;
            if (ev!=null) ev[i3][i2][i1] = evi;
            if (ew!=null) ew[i3][i2][i1] = ewi;
            if (ep!=null || el!=null) {
              float esi = (eui>0.0f)?1.0f/eui:1.0f;
              if (ep!=null) ep[i3][i2][i1] = (eui-evi)*esi;
              if (el!=null) el[i3][i2][i1] = (evi-ewi)*esi;
            }
          }
        }
      }
    });
  }
}
