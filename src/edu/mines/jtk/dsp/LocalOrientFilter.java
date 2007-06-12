/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Array;
import static edu.mines.jtk.util.MathPlus.*;

import edu.mines.jtk.util.Check;

/**
 * Local estimates of orientations of features in images.
 * Methods of this class can compute for each image sample numerous
 * parameters related to orientation. All orientation information 
 * is derived from eigenvectors and eigenvalues of the structure tensor
 * (also called the "gradient squared tensor"). This tensor is equivalent 
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
 * The angle theta = asin(u2) is the angle between the 1st eigenvector 
 * u and axis 1; -pi/2 &lt;= theta &lt;= pi/2.
 * <p>
 * The coordinate system for a 3-D image has three orthogonal axes 1, 2 
 * and 3, which correspond to the 1st, 2nd and 3rd indices of the array 
 * containing image samples. For 3-D images, the eigenvectors are unit 
 * vectors u = (u1,u2,u3), v = (v1,v2,v3), and w = (w1,w2,w3). The 1st 
 * eigenvector u is orthogonal to the best fitting plane, and the 1st 
 * component u1 of u is always non-negative. The 2nd eigenvector v is 
 * orthogonal to the best fitting line within the best fitting plane.
 * The 3rd eigenvector w is the cross product of u and v. The dip angle 
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

  public LocalOrientFilter(double sigma) {
    _sigma = sigma;
    _rgfGradient = new RecursiveGaussianFilter(1.0);
    _rgfSmoother = new RecursiveGaussianFilter(sigma);
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
   * @param el (eu-ev)/(eu+ev), a measure of linearity.
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
    _rgfGradient.apply1X(x,g1);
    _rgfGradient.applyX1(x,g2);

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
    float[][] h = (nt>3)?t[3]:new float[n2][n1];
    float[][][] gs = {g11,g22,g12};
    for (float[][] g:gs) {
      _rgfSmoother.apply0X(g,h);
      _rgfSmoother.applyX0(h,g);
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
   * @param ep (eu-ev)/(eu+ew), a measure of planarity.
   * @param el (ev-ew)/(eu+ew), a measure of linearity.
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
    _rgfGradient.apply1XX(x,g1);
    _rgfGradient.applyX1X(x,g2);
    _rgfGradient.applyXX1(x,g3);

    // Gradient products.
    float[][][] g11 = g1;
    float[][][] g22 = g2;
    float[][][] g33 = g3;
    float[][][] g12 = (nt>3)?t[3]:new float[n3][n2][n1];
    float[][][] g13 = (nt>4)?t[4]:new float[n3][n2][n1];
    float[][][] g23 = (nt>5)?t[5]:new float[n3][n2][n1];
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
    
    // Smoothed gradient products comprise the structure tensor.
    float[][][] h = (nt>6)?t[6]:new float[n3][n2][n1];
    float[][][][] gs = {g11,g22,g33,g12,g13,g23};
    for (float[][][] g:gs) {
      _rgfSmoother.apply0XX(g,h);
      _rgfSmoother.applyX0X(h,g);
      _rgfSmoother.applyXX0(g,h);
      Array.copy(h,g);
    }

    // Compute eigenvectors, eigenvalues, and outputs that depend on them.
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
            float esi = 1.0f/eui;
            if (ep!=null) ep[i3][i2][i1] = (eui-evi)*esi;
            if (el!=null) el[i3][i2][i1] = (evi-ewi)*esi;
          }
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _sigma;
  private RecursiveGaussianFilter _rgfGradient;
  private RecursiveGaussianFilter _rgfSmoother;
}
