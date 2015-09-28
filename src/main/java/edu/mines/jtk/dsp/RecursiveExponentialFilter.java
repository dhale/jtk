/****************************************************************************
Copyright 2011, Colorado School of Mines and others.
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

import edu.mines.jtk.util.Check;
import edu.mines.jtk.util.Parallel;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Recursive symmetric exponential smoothing filter. Except perhaps near
 * the edges of input and output arrays, the impulse response of this 
 * two-sided filter is symmetric and decays exponentially from its peak 
 * value at zero lag. Specifically, the impulse response has the form
 * h[n] = a^abs(n)*(1-a)/(1+a), where a is a parameter in the range
 * [0:1) derived from a specified half-width sigma.
 * <p>
 * Like the Gaussian filter, the impulse response of the exponential
 * filter is nowhere zero. The half-width sigma for the exponential 
 * filter is here defined so that, for low frequencies, the frequency 
 * response of the exponential filter approximates that for a Gaussian 
 * filter with the same specified half-width sigma. Specifically, the
 * value, slope and curvature of the frequency responses will be the
 * same for exponential and Gaussian filters if the same half-widths
 * are specified.
 * <p>
 * This smoothing filter is faster than a recursive Gaussian filter. 
 * This filter also provides a variety of boundary conditions that
 * can be used to control the filtering of samples near the edges
 * of arrays. For most (but not all) of these boundary conditions, 
 * this filter is symmetric and positive-definite (SPD). This means, 
 * for example, that it can be used as a preconditioner in 
 * conjugate-gradient solutions of SPD systems of equations.
 * <p>
 * Multidimensional filters are applied as a cascade of one-dimensional
 * filters applied for each dimension of multidimensional arrays. In 
 * contrast to the Gaussian filter, this cascade for the exponential 
 * filter does not have isotropic impulse or frequency responses.
 * <p>
 * All smoothing can be performed in place, so that input and output 
 * arrays can be the same array.
 *
 * @author Dave Hale &amp; Simon Luo, Colorado School of Mines
 * @version 2011.10.01
 */
public class RecursiveExponentialFilter {

  /**
   * Boundary condition used at edges of either input or output samples.
   * All except the input-zero-slope condition yield a filter that is
   * symmetric positive-definite (SPD).
   * <p>
   * The default boundary condition is output-zero-slope.
   */
  public enum Edges {
    /**
     * Extrapolate input samples beyond edges with zero values.
     * A filter with this boundary condition is SPD.
     */
    INPUT_ZERO_VALUE,
    /**
     * Extrapolate input samples beyond edges with values at the edges.
     * Extrapolated values will be constant, so that the slope is zero. 
     * <em>A filter with this boundary condition is not SPD.</em>
     */
    INPUT_ZERO_SLOPE,
    /**
     * Constrain output values beyond edges to be zero.
     * Output samples near the edges will have nearly zero value.
     * A filter with this boundary condition is SPD.
     */
    OUTPUT_ZERO_VALUE,
    /**
     * Constrain output values beyond edges to be constant.
     * Output samples near the edges will have nearly zero slope.
     * A filter with this boundary condition (the default) is SPD.
     */
    OUTPUT_ZERO_SLOPE
  }

  /**
   * Constructs a filter with specified half-width.
   * The same half-width is used when applying the filter for all 
   * dimensions of multidimensional arrays.
   * @param sigma filter half-width.
   */
  public RecursiveExponentialFilter(double sigma) {
    this(sigma,sigma,sigma);
  }

  /**
   * Constructs a filter with specified half-widths.
   * @param sigma1 filter half-width for the 1st dimension.
   * @param sigma23 filter half-width for 2nd and 3rd dimensions. 
   */
  public RecursiveExponentialFilter(double sigma1, double sigma23) {
    this(sigma1,sigma23,sigma23);
  }

  /**
   * Constructs a filter with specified half-widths.
   * @param sigma1 filter half-width for the 1st dimension.
   * @param sigma2 filter half-width for the 2nd dimension.
   * @param sigma3 filter half-width for the 3rd dimension.
   */
  public RecursiveExponentialFilter(
    double sigma1, double sigma2, double sigma3)
  {
    Check.argument(sigma1>=0.0,"sigma is non-negative");
    Check.argument(sigma2>=0.0,"sigma is non-negative");
    Check.argument(sigma3>=0.0,"sigma is non-negative");
    _sigma1 = (float)sigma1;
    _sigma2 = (float)sigma2;
    _sigma3 = (float)sigma3;
    _a1 = aFromSigma(sigma1);
    _a2 = aFromSigma(sigma2);
    _a3 = aFromSigma(sigma3);
  }

  /**
   * Sets the boundary condition used for samples beyond edges.
   * @param edges the boundary condition.
   */
  public void setEdges(Edges edges) {
    _ei = edges==Edges.INPUT_ZERO_VALUE || edges==Edges.INPUT_ZERO_SLOPE;
    _zs = edges==Edges.INPUT_ZERO_SLOPE || edges==Edges.OUTPUT_ZERO_SLOPE;
  }

  /**
   * Applies this filter.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[] x, float[] y) {
    apply1(x,y);
  }

  /**
   * Applies this filter along all array dimensions.
   * Input and output arrays can be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][] x, float[][] y) {
    apply2(x,y);
    apply1(y,y);
  }

  /**
   * Applies this filter along all array dimensions.
   * Input and output arrays can be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][][] x, float[][][] y) {
    apply3(x,y);
    apply2(y,y);
    apply1(y,y);
  }

  /**
   * Applies this filter along the 1st (only) array dimension.
   * Input and output arrays can be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void apply1(float[] x, float[] y) {
    smooth1(_ei,_zs,_a1,x,y);
  }

  /**
   * Applies this filter along the 1st array dimension.
   * Input and output arrays can be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void apply1(float[][] x, float[][] y) {
    int n2 = x.length;
    for (int i2=0; i2<n2; ++i2) {
      float[] x2 = x[i2];
      float[] y2 = y[i2];
      smooth1(_ei,_zs,_a1,x2,y2);
    }
  }

  /**
   * Applies this filter along the 2nd array dimension.
   * Input and output arrays can be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void apply2(float[][] x, float[][] y) {
    smooth2(_ei,_zs,_a2,x,y);
  }

  /**
   * Applies this filter along the 1st array dimension.
   * Input and output arrays can be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void apply1(float[][][] x, float[][][] y) {
    final float[][][] xx = x;
    final float[][][] yy = y;
    final int n2 = x[0].length;
    final int n3 = x.length;
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        for (int i2=0; i2<n2; ++i2) {
          float[] x32 = xx[i3][i2];
          float[] y32 = yy[i3][i2];
          smooth1(_ei,_zs,_a1,x32,y32);
        }
      }
    });
  }

  /**
   * Applies this filter along the 2nd array dimension.
   * Input and output arrays can be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void apply2(float[][][] x, float[][][] y) {
    final float[][][] xx = x;
    final float[][][] yy = y;
    final int n3 = x.length;
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        float[][] x3 = xx[i3];
        float[][] y3 = yy[i3];
        smooth2(_ei,_zs,_a2,x3,y3);
      }
    });
  }

  /**
   * Applies this filter along the 3rd array dimension.
   * Input and output arrays can be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void apply3(float[][][] x, float[][][] y) {
    final float[][][] xx = x;
    final float[][][] yy = y;
    final int n2 = x[0].length;
    final int n3 = x.length;
    Parallel.loop(n2,new Parallel.LoopInt() {
      public void compute(int i2) {
        float[][] x2 = new float[n3][];
        float[][] y2 = new float[n3][];
        for (int i3=0; i3<n3; ++i3) {
          x2[i3] = xx[i3][i2];
          y2[i3] = yy[i3][i2];
        }
        smooth2(_ei,_zs,_a3,x2,y2);
      }
    });
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private float _sigma1,_a1; // half-width and parameter a for 1st dimension
  private float _sigma2,_a2; // half-width and parameter a for 2nd dimension
  private float _sigma3,_a3; // half-width and parameter a for 3rd dimension
  private boolean _ei = false; // true, iff b.c. specified for input edges
  private boolean _zs = true; // true, iff zero slope boundary conditions

  private static float aFromSigma(double sigma) {
    if (sigma<=0.0f)
      return 0.0f;
    double ss = sigma*sigma;
    return (float)((1.0+ss-sqrt(1.0+2.0*ss))/ss);
  }

  // Smooth a 1D array.
  private static void smooth1(
    boolean ei, boolean zs, float a, float[] x, float[] y) 
  {
    if (a==0.0f) {
      copy(x,y);
    } else if (ei) {
      smooth1Ei(zs,a,x,y);
    } else {
      smooth1Eo(zs,a,x,y);
    }
  }

  // Smooth along 2nd dimension of a 2D array.
  private static void smooth2(
    boolean ei, boolean zs, float a, float[][] x, float[][] y) 
  {
    if (a==0.0f) {
      copy(x,y);
    } else if (ei) {
      smooth2Ei(zs,a,x,y);
    } else {
      smooth2Eo(zs,a,x,y);
    }
  }

  // Smooth a 1D array for input boundary conditions.
  private static void smooth1Ei(
    boolean zs, float a, float[] x, float[] y)
  {
    int n1 = x.length;
    float b = 1.0f-a;
    float sx = zs?1.0f:b;
    float sy = a;
    float yi = y[0] = sx*x[0];
    for (int i1=1; i1<n1-1; ++i1)
      y[i1] = yi = a*yi+b*x[i1];
    sx /= 1.0f+a;
    sy /= 1.0f+a;
    y[n1-1] = yi = sy*yi+sx*x[n1-1];
    for (int i1=n1-2; i1>=0; --i1)
      y[i1] = yi = a*yi+b*y[i1];
  }

  // Smooth along 2nd dimension of a 2D array for input boundary conditions.
  private static void smooth2Ei(
    boolean zs, float a, float[][] x, float[][] y)
  {
    int n1 = x[0].length;
    int n2 = x.length;
    float b = 1.0f-a;
    float sx = zs?1.0f:b;
    float sy = a;
    for (int i1=0; i1<n1; ++i1)
      y[0][i1] = sx*x[0][i1];
    for (int i2=1; i2<n2-1; ++i2)
      for (int i1=0; i1<n1; ++i1)
        y[i2][i1] = a*y[i2-1][i1]+b*x[i2][i1];
    sx /= 1.0f+a;
    sy /= 1.0f+a;
    for (int i1=0; i1<n1; ++i1)
      y[n2-1][i1] = sy*y[n2-2][i1]+sx*x[n2-1][i1];
    for (int i2=n2-2; i2>=0; --i2)
      for (int i1=0; i1<n1; ++i1)
        y[i2][i1] = a*y[i2+1][i1]+b*y[i2][i1];
  }

  // Smooth a 1D array for output boundary conditions.
  // Adapted from Algorithm 4.1 in Boisvert, R.F., Algorithms for
  // special tridiagonal systems: SIAM J. Sci. Stat. Comput., v. 12,
  // no. 2, pp. 423-442.
  private static void smooth1Eo(
    boolean zs, float a, float[] x, float[] y)
  {
    int n1 = x.length;
    float aa = a*a;
    float ss = zs?1.0f-a:1.0f;
    float gg = zs?aa-a:aa;
    float c = (1.0f-aa-ss)/ss;
    float d = 1.0f/(1.0f-aa+gg*(1.0f+c*pow(aa,n1-1)));
    float e = (1.0f-a)*(1.0f-a)*FLT_EPSILON/4.0f;

    // copy scaled input to output
    mul((1.0f-a)*(1.0f-a),x,y);

    // reversed triangular factorization
    int k1 = min((int)ceil(log(e)/log(a)),2*n1-2); // 1 <= k1 <= 2*n1-2
    float ynm1 = 0.0f;
    int m1 = k1-n1+1; // 2-n1 <= m1 <= n1-1
    for (int i1=m1; i1>0; --i1)
      ynm1 = a*ynm1+y[i1];
    ynm1 *= c;
    if (n1-k1<1)
      ynm1 = a*ynm1+(1.0f+c)*y[0];
    m1 = max(n1-k1,1); // 1 <= m1 <= n1-1
    for (int i1=m1; i1<n1; ++i1)
      ynm1 = a*ynm1+y[i1];
    ynm1 *= d;

    // reverse substitution
    y[n1-1] -= gg*ynm1;
    for (int i1=n1-2; i1>=0; --i1)
      y[i1] += a*y[i1+1];
    y[0] /= ss;

    // forward substitution
    for (int i1=1; i1<n1-1; ++i1)
      y[i1] += a*y[i1-1];
    y[n1-1] = ynm1;
  }

  // Smooth along 2nd dimension of a 2D array for output boundary conditions.
  private static void smooth2Eo(
    boolean zs, float a, float[][] x, float[][] y)
  {
    int n1 = x[0].length;
    int n2 = x.length;
    float aa = a*a;
    float ss = zs?1.0f-a:1.0f;
    float gg = zs?aa-a:aa;
    float c = (1.0f-aa-ss)/ss;
    float d = 1.0f/(1.0f-aa+gg*(1.0f+c*pow(aa,n1-1)));
    float e = (1.0f-a)*(1.0f-a)*FLT_EPSILON/4.0f;

    // copy scaled input to output
    mul((1.0f-a)*(1.0f-a),x,y);

    // reversed triangular factorization
    int k2 = min((int)ceil(log(e)/log(a)),2*n2-2);
    float[] ynm1 = new float[n1];
    int m2 = k2-n2+1;
    for (int i2=m2; i2>0; --i2) {
      float[] yi = y[i2];
      for (int i1=0; i1<n1; ++i1)
        ynm1[i1] = a*ynm1[i1]+yi[i1];
    }
    for (int i1=0; i1<n1; ++i1)
      ynm1[i1] *= c;
    if (n2-k2<1) {
      for (int i1=0; i1<n1; ++i1)
        ynm1[i1] = a*ynm1[i1]+(1.0f+c)*y[0][i1];
    }
    m2 = max(n2-k2,1);
    for (int i2=m2; i2<n2; ++i2) {
      float[] yi = y[i2];
      for (int i1=0; i1<n1; ++i1)
        ynm1[i1] = a*ynm1[i1]+yi[i1];
    }
    for (int i1=0; i1<n1; ++i1)
      ynm1[i1] *= d;

    // reverse substitution
    for (int i1=0; i1<n1; ++i1)
      y[n2-1][i1] -= gg*ynm1[i1];
    for (int i2=n2-2; i2>=0; --i2) {
      float[] yi = y[i2];
      float[] yp = y[i2+1];
      for (int i1=0; i1<n1; ++i1)
        yi[i1] += a*yp[i1];
    }
    float oss = 1.0f/ss;
    for (int i1=0; i1<n1; ++i1)
      y[0][i1] *= oss;

    // forward substitution
    for (int i2=1; i2<n2-1; ++i2) {
      float[] yi = y[i2];
      float[] ym = y[i2-1];
      for (int i1=0; i1<n1; ++i1)
        yi[i1] += a*ym[i1];
    }
    for (int i1=0; i1<n1; ++i1)
      y[n2-1][i1] = ynm1[i1];
  }
}
