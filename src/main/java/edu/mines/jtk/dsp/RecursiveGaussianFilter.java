/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Recursive implementation of a Gaussian filter and derivatives. Filters 
 * include the 0th, 1st, and 2nd derivatives. The impulse response of the 
 * 0th-derivative smoothing filter is infinitely long, and is approximately 
 * h[n] = 1.0/(sqrt(2*PI)*sigma)*exp(-0.5*(n*n)/(sigma*sigma)). Here,
 * sigma denotes the standard width of the Gaussian.
 * <p>
 * For large filter widths sigma, this recursive implementation can be
 * much more efficient than convolution with a truncated Gaussian.
 * Specifically, if the Gaussian is truncated for |n| &gt; 4*sigma, then
 * this recursive implementation requires 2/sigma of the multiplications 
 * required by convolution. In other words, for sigma &gt; 2, this
 * recursive implementation should be more efficient than convolution.
 * <p>
 * For any application of this filter, input and output arrays may be the 
 * same array. When the filter cannot be applied in-place, intermediate
 * arrays are constructed internally.
 * <p>
 * This filter implements two different methods for approximating 
 * with difference equations a Gaussian filter and its derivatives.
 * <p>
 * The first method is that of Deriche, R., 1993, Recursively implementing 
 * the Gaussian and its derivatives: INRIA Research Report, number 1893. 
 * Deriche's method is used for small widths sigma, for which it is most 
 * accurate. 
 * <p>
 * The second method is that of van Vliet, L.J., Young, I.T., and Verbeek, 
 * P.W., 1998, Recursive Gaussian derivative filters, Proceedings of the 
 * 14th International Conference on Pattern Recognition, IEEE Computer 
 * Society Press. The parallel implementation used here yields zero-phase 
 * impulse responses without the end effects caused by the serial (cascade) 
 * poles-only implementation recommended by van Vliet, et al. This 
 * second method is used for large widths sigma.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.02.12
 */
public class RecursiveGaussianFilter {

  /**
   * The method used to design the Gaussian filter.
   */
  public enum Method {
    DERICHE,
    VAN_VLIET
  }

  /**
   * Construct a Gaussian filter with specified width and design method.
   * @param sigma the width; must not be less than 1.
   * @param method the method used to design the filter.
   */
  public RecursiveGaussianFilter(double sigma, Method method) {
    Check.argument(sigma>=1.0,"sigma>=1.0");
    _filter = (method==Method.DERICHE)?
      new DericheFilter(sigma) :
      new VanVlietFilter(sigma);
  }

  /**
   * Construct a Gaussian filter with specified width.
   * @param sigma the width; must not be less than 1.
   */
  public RecursiveGaussianFilter(double sigma) {
    Check.argument(sigma>=1.0,"sigma>=1.0");
    _filter = (sigma<32.0) ? 
      new DericheFilter(sigma) :
      new VanVlietFilter(sigma);
  }

  /**
   * Applies the 0th-derivative filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply0(float[] x, float[] y) {
    _filter.applyN(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply1(float[] x, float[] y) {
    _filter.applyN(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply2(float[] x, float[] y) {
    _filter.applyN(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply0X(float[][] x, float[][] y) {
    _filter.applyNX(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply1X(float[][] x, float[][] y) {
    _filter.applyNX(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply2X(float[][] x, float[][] y) {
    _filter.applyNX(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX0(float[][] x, float[][] y) {
    _filter.applyXN(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX1(float[][] x, float[][] y) {
    _filter.applyXN(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX2(float[][] x, float[][] y) {
    _filter.applyXN(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply0XX(float[][][] x, float[][][] y) {
    _filter.applyNXX(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply1XX(float[][][] x, float[][][] y) {
    _filter.applyNXX(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply2XX(float[][][] x, float[][][] y) {
    _filter.applyNXX(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX0X(float[][][] x, float[][][] y) {
    _filter.applyXNX(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX1X(float[][][] x, float[][][] y) {
    _filter.applyXNX(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX2X(float[][][] x, float[][][] y) {
    _filter.applyXNX(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 3rd dimension.
   * Applies no filter along the 1st or 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyXX0(float[][][] x, float[][][] y) {
    _filter.applyXXN(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 3rd dimension.
   * Applies no filter along the 1st or 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyXX1(float[][][] x, float[][][] y) {
    _filter.applyXXN(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 3rd dimension.
   * Applies no filter along the 1st or 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyXX2(float[][][] x, float[][][] y) {
    _filter.applyXXN(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st and 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply00(float[][] x, float[][] y) {
    _filter.applyXN(0,x,y);
    _filter.applyNX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st dimension
   * and the 0th-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply10(float[][] x, float[][] y) {
    _filter.applyXN(0,x,y);
    _filter.applyNX(1,y,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st dimension
   * and the 1st-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply01(float[][] x, float[][] y) {
    _filter.applyXN(1,x,y);
    _filter.applyNX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st and 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply11(float[][] x, float[][] y) {
    _filter.applyXN(1,x,y);
    _filter.applyNX(1,y,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 1st dimension
   * and the 0th-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply20(float[][] x, float[][] y) {
    _filter.applyXN(0,x,y);
    _filter.applyNX(2,y,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st dimension
   * and the 2nd-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply02(float[][] x, float[][] y) {
    _filter.applyXN(2,x,y);
    _filter.applyNX(0,y,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st, 2nd and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply000(float[][][] x, float[][][] y) {
    _filter.applyXXN(0,x,y);
    _filter.applyXNX(0,y,y);
    _filter.applyNXX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st dimension
   * and the 0th-derivative filter along the 2nd and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply100(float[][][] x, float[][][] y) {
    _filter.applyXXN(0,x,y);
    _filter.applyXNX(0,y,y);
    _filter.applyNXX(1,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 2nd dimension
   * and the 0th-derivative filter along the 1st and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply010(float[][][] x, float[][][] y) {
    _filter.applyXXN(0,x,y);
    _filter.applyXNX(1,y,y);
    _filter.applyNXX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 3rd dimension
   * and the 0th-derivative filter along the 1st and 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply001(float[][][] x, float[][][] y) {
    _filter.applyXXN(1,x,y);
    _filter.applyXNX(0,y,y);
    _filter.applyNXX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st and 2nd dimensions
   * and the 0th-derivative filter along the 3rd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply110(float[][][] x, float[][][] y) {
    _filter.applyXXN(0,x,y);
    _filter.applyXNX(1,y,y);
    _filter.applyNXX(1,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st and 3rd dimensions
   * and the 0th-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply101(float[][][] x, float[][][] y) {
    _filter.applyXXN(1,x,y);
    _filter.applyXNX(0,y,y);
    _filter.applyNXX(1,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 2nd and 3rd dimensions
   * and the 0th-derivative filter along the 1st dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply011(float[][][] x, float[][][] y) {
    _filter.applyXXN(1,x,y);
    _filter.applyXNX(1,y,y);
    _filter.applyNXX(0,y,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 1st dimension
   * and the 0th-derivative filter along the 2nd and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply200(float[][][] x, float[][][] y) {
    _filter.applyXXN(0,x,y);
    _filter.applyXNX(0,y,y);
    _filter.applyNXX(2,y,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 2nd dimension
   * and the 0th-derivative filter along the 1st and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply020(float[][][] x, float[][][] y) {
    _filter.applyXXN(0,x,y);
    _filter.applyXNX(2,y,y);
    _filter.applyNXX(0,y,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 3rd dimension
   * and the 0th-derivative filter along the 1st and 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply002(float[][][] x, float[][][] y) {
    _filter.applyXXN(2,x,y);
    _filter.applyXNX(0,y,y);
    _filter.applyNXX(0,y,y);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Filter _filter;

  private static void checkArrays(float[] x, float[] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
  }

  private static void checkArrays(float[][] x, float[][] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
    Check.argument(x[0].length==y[0].length,"x[0].length==y[0].length");
    Check.argument(isRegular(x),"x is regular");
    Check.argument(isRegular(y),"y is regular");
  }

  private static void checkArrays(float[][][] x, float[][][] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
    Check.argument(x[0].length==y[0].length,"x[0].length==y[0].length");
    Check.argument(x[0][0].length==y[0][0].length,
      "x[0][0].length==y[0][0].length");
    Check.argument(isRegular(x),"x is regular");
    Check.argument(isRegular(y),"y is regular");
  }

  private static boolean sameArrays(float[] x, float[] y) {
    return x==y;
  }

  private static boolean sameArrays(float[][] x, float[][] y) {
    if (x==y) {
      return true;
    } else {
      int n2 = x.length;
      for (int i2=0; i2<n2; ++i2)
        if (x[i2]==y[i2])
          return true;
    }
    return false;
  }

  ///////////////////////////////////////////////////////////////////////////
  private static abstract class Filter {

    abstract void applyN(int nd, float[] x, float[] y);

    abstract void applyXN(int nd, float[][] x, float[][] y);

    void applyNX(int nd, float[][] x, float[][] y) {
      int m2 = y.length;
      for (int i2=0; i2<m2; ++i2)
        applyN(nd,x[i2],y[i2]);
    }

    void applyNXX(final int nd, final float[][][] x, final float[][][] y) {
      final int m3 = y.length;
      Parallel.loop(m3,new Parallel.LoopInt() {
        public void compute(int i3) {
          applyNX(nd,x[i3],y[i3]);
        }
      });
    }

    void applyXNX(final int nd, final float[][][] x, final float[][][] y) {
      final int m3 = y.length;
      Parallel.loop(m3,new Parallel.LoopInt() {
        public void compute(int i3) {
          applyXN(nd,x[i3],y[i3]);
        }
      });
    }

    void applyXXN(final int nd, final float[][][] x, final float[][][] y) {
      checkArrays(x,y);
      final int m3 = y.length;
      final int m2 = y[0].length;
      final float[][][] tx = new float[m2][m3][];
      final float[][][] ty = new float[m2][m3][];
      for (int i3=0; i3<m3; ++i3) {
        for (int i2=0; i2<m2; ++i2) {
          tx[i2][i3] = x[i3][i2];
          ty[i2][i3] = y[i3][i2];
        }
      }
      Parallel.loop(m2,new Parallel.LoopInt() {
        public void compute(int i2) {
          applyXN(nd,tx[i2],ty[i2]);
        }
      });
    }
    /* Should be equivalent to the parallel version above.
    void applyXXN(int nd, float[][][] x, float[][][] y) {
      checkArrays(x,y);
      int m3 = y.length;
      int m2 = y[0].length;
      float[][] x2 = new float[m3][];
      float[][] y2 = new float[m3][];
      for (int i2=0; i2<m2; ++i2) {
        for (int i3=0; i3<m3; ++i3) {
          x2[i3] = x[i3][i2];
          y2[i3] = y[i3][i2];
        }
        applyXN(nd,x2,y2);
      }
    }
    */
  }

  ///////////////////////////////////////////////////////////////////////////
  private static class DericheFilter extends Filter {

    DericheFilter(double sigma) {
      makeND(sigma);
    }

    void applyN(int nd, float[] x, float[] y) {
      checkArrays(x,y);
      if (sameArrays(x,y))
        x = copy(x);
      int m = y.length;
      float n0 = _n0[nd],  n1 = _n1[nd],  n2 = _n2[nd],  n3 = _n3[nd];
      float d1 = _d1[nd],  d2 = _d2[nd],  d3 = _d3[nd],  d4 = _d4[nd];
      float yim4 = 0.0f,  yim3 = 0.0f,  yim2 = 0.0f,  yim1 = 0.0f;
      float               xim3 = 0.0f,  xim2 = 0.0f,  xim1 = 0.0f;
      for (int i=0; i<m; ++i) {
        float xi = x[i];
        float yi = n0*xi+n1*xim1+n2*xim2+n3*xim3 -
                         d1*yim1-d2*yim2-d3*yim3-d4*yim4;
        y[i] = yi;
        yim4 = yim3;  yim3 = yim2;  yim2 = yim1;  yim1 = yi;
                      xim3 = xim2;  xim2 = xim1;  xim1 = xi;
      }
      n1 = n1-d1*n0;
      n2 = n2-d2*n0;
      n3 = n3-d3*n0;
      float n4 = -d4*n0;
      if (nd%2!=0) {
        n1 = -n1;  n2 = -n2;  n3 = -n3;  n4 = -n4;
      }
      float yip4 = 0.0f,  yip3 = 0.0f,  yip2 = 0.0f,  yip1 = 0.0f;
      float xip4 = 0.0f,  xip3 = 0.0f,  xip2 = 0.0f,  xip1 = 0.0f;
      for (int i=m-1; i>=0; --i) {
        float xi = x[i];
        float yi = n1*xip1+n2*xip2+n3*xip3+n4*xip4 -
                   d1*yip1-d2*yip2-d3*yip3-d4*yip4;
        y[i] += yi;
        yip4 = yip3;  yip3 = yip2;  yip2 = yip1;  yip1 = yi;
        xip4 = xip3;  xip3 = xip2;  xip2 = xip1;  xip1 = xi;
      }
    }

    void applyXN(int nd, float[][] x, float[][] y) {
      checkArrays(x,y);
      if (sameArrays(x,y))
        x = copy(x);
      int m2 = y.length;
      int m1 = y[0].length;
      float n0 = _n0[nd],  n1 = _n1[nd],  n2 = _n2[nd],  n3 = _n3[nd];
      float d1 = _d1[nd],  d2 = _d2[nd],  d3 = _d3[nd],  d4 = _d4[nd];
      float[] yim4 = new float[m1];
      float[] yim3 = new float[m1];
      float[] yim2 = new float[m1];
      float[] yim1 = new float[m1];
      float[] xim4 = new float[m1];
      float[] xim3 = new float[m1];
      float[] xim2 = new float[m1];
      float[] xim1 = new float[m1];
      float[] yi = new float[m1];
      float[] xi = new float[m1];
      for (int i2=0; i2<m2; ++i2) {
        float[] x2 = x[i2];
        float[] y2 = y[i2];
        for (int i1=0; i1<m1; ++i1) {
          xi[i1] = x2[i1];
          yi[i1] = n0*xi[i1]+n1*xim1[i1]+n2*xim2[i1]+n3*xim3[i1]
                            -d1*yim1[i1]-d2*yim2[i1]-d3*yim3[i1]-d4*yim4[i1];
          y2[i1] = yi[i1];
        }
        float[] yt = yim4;
        yim4 = yim3;
        yim3 = yim2;
        yim2 = yim1;
        yim1 = yi;
        yi = yt;
        float[] xt = xim3;
        xim3 = xim2;
        xim2 = xim1;
        xim1 = xi;
        xi = xt;
      }
      n1 = n1-d1*n0;
      n2 = n2-d2*n0;
      n3 = n3-d3*n0;
      float n4 = -d4*n0;
      if (nd%2!=0) {
        n1 = -n1;  n2 = -n2;  n3 = -n3;  n4 = -n4;
      }
      float[] yip4 = yim4;
      float[] yip3 = yim3;
      float[] yip2 = yim2;
      float[] yip1 = yim1;
      float[] xip4 = xim4;
      float[] xip3 = xim3;
      float[] xip2 = xim2;
      float[] xip1 = xim1;
      for (int i1=0; i1<m1; ++i1) {
        yip4[i1] = 0.0f;
        yip3[i1] = 0.0f;
        yip2[i1] = 0.0f;
        yip1[i1] = 0.0f;
        xip4[i1] = 0.0f;
        xip3[i1] = 0.0f;
        xip2[i1] = 0.0f;
        xip1[i1] = 0.0f;
      }
      for (int i2=m2-1; i2>=0; --i2) {
        float[] x2 = x[i2];
        float[] y2 = y[i2];
        for (int i1=0; i1<m1; ++i1) {
          xi[i1] = x2[i1];
          yi[i1] = n1*xip1[i1]+n2*xip2[i1]+n3*xip3[i1]+n4*xip4[i1] -
                   d1*yip1[i1]-d2*yip2[i1]-d3*yip3[i1]-d4*yip4[i1];
          y2[i1] += yi[i1];
        }
        float[] yt = yip4;
        yip4 = yip3;
        yip3 = yip2;
        yip2 = yip1;
        yip1 = yi;
        yi = yt;
        float[] xt = xip4;
        xip4 = xip3;
        xip3 = xip2;
        xip2 = xip1;
        xip1 = xi;
        xi = xt;
      }
    }

    // Coefficients computed using Deriche's method. These coefficients
    // were computed for sigma = 100 and 0 <= x <= 10*sigma = 1000,
    // using the Mathematica function FindFit. The coefficients have
    // roughly 10 digits of precision.
    // 0th derivative.
    private static double a00 =  1.6797292232361107; 
    private static double a10 =  3.7348298269103580;
    private static double b00 =  1.7831906544515104;
    private static double b10 =  1.7228297663338028;
    private static double c00 = -0.6802783501806897;
    private static double c10 = -0.2598300478959625;
    private static double w00 =  0.6318113174569493; 
    private static double w10 =  1.9969276832487770;
    // 1st derivative.
    private static double a01 =  0.6494024008440620;
    private static double a11 =  0.9557370760729773;
    private static double b01 =  1.5159726670750566;
    private static double b11 =  1.5267608734791140;
    private static double c01 = -0.6472105276644291; 
    private static double c11 = -4.5306923044570760;
    private static double w01 =  2.0718953658782650;
    private static double w11 =  0.6719055957689513;
    // 2nd derivative.
    private static double a02 =  0.3224570510072559;
    private static double a12 = -1.7382843963561239;
    private static double b02 =  1.3138054926516880;
    private static double b12 =  1.2402181393295362;
    private static double c02 = -1.3312275593739595;
    private static double c12 =  3.6607035671974897;
    private static double w02 =  2.1656041357418863;
    private static double w12 =  0.7479888745408682;
    //
    private static double[] a0 = {a00,a01,a02};
    private static double[] a1 = {a10,a11,a12};
    private static double[] b0 = {b00,b01,b02};
    private static double[] b1 = {b10,b11,b12};
    private static double[] c0 = {c00,c01,c02};
    private static double[] c1 = {c10,c11,c12};
    private static double[] w0 = {w00,w01,w02};
    private static double[] w1 = {w10,w11,w12};

    /*
    // Deriche's published coefficients.
    private static double[] a0 = {  1.6800, -0.6472, -1.3310};
    private static double[] a1 = {  3.7350, -4.5310,  3.6610};
    private static double[] b0 = {  1.7830,  1.5270,  1.2400};
    private static double[] b1 = {  1.7230,  1.5160,  1.3140};
    private static double[] c0 = { -0.6803,  0.6494,  0.3225};
    private static double[] c1 = { -0.2598,  0.9557, -1.7380};
    private static double[] w0 = {  0.6318,  0.6719,  0.7480};
    private static double[] w1 = {  1.9970,  2.0720,  2.1660};
    */
    private float[] _n0,_n1,_n2,_n3; // numerator coefficients
    private float[] _d1,_d2,_d3,_d4; // denominator coefficients

    /**
     * Makes Deriche's numerator and denominator coefficients.
     */
    private void makeND(double sigma) {
      _n0 = new float[3];
      _n1 = new float[3];
      _n2 = new float[3];
      _n3 = new float[3];
      _d1 = new float[3];
      _d2 = new float[3];
      _d3 = new float[3];
      _d4 = new float[3];

      // For 0th, 1st, and 2nd derivatives, ...
      for (int i=0; i<3; ++i) {
        double n0 = (i%2==0)?a0[i]+c0[i]:0.0;
        double n1 = exp(-b1[i]/sigma) * (
                      c1[i]*sin(w1[i]/sigma) -
                      (c0[i]+2.0*a0[i])*cos(w1[i]/sigma)) +
                    exp(-b0[i]/sigma) * (
                      a1[i]*sin(w0[i]/sigma) -
                      (2.0*c0[i]+a0[i])*cos(w0[i]/sigma));
        double n2 = 2.0*exp(-(b0[i]+b1[i])/sigma) * (
                      (a0[i]+c0[i])*cos(w1[i]/sigma)*cos(w0[i]/sigma) -
                      a1[i]*cos(w1[i]/sigma)*sin(w0[i]/sigma) -
                      c1[i]*cos(w0[i]/sigma)*sin(w1[i]/sigma)) +
                    c0[i]*exp(-2.0*b0[i]/sigma) +
                    a0[i]*exp(-2.0*b1[i]/sigma);
        double n3 = exp(-(b1[i]+2.0*b0[i])/sigma) * (
                      c1[i]*sin(w1[i]/sigma) -
                      c0[i]*cos(w1[i]/sigma)) +
                    exp(-(b0[i]+2.0*b1[i])/sigma) * (
                      a1[i]*sin(w0[i]/sigma) -
                      a0[i]*cos(w0[i]/sigma));
        double d1 = -2.0*exp(-b0[i]/sigma)*cos(w0[i]/sigma) -
                     2.0*exp(-b1[i]/sigma)*cos(w1[i]/sigma);
        double d2 = 4.0*exp(-(b0[i]+b1[i])/sigma) *
                      cos(w0[i]/sigma)*cos(w1[i]/sigma) +
                    exp(-2.0*b0[i]/sigma) +
                    exp(-2.0*b1[i]/sigma);
        double d3 = -2.0*exp(-(b0[i]+2.0*b1[i])/sigma)*cos(w0[i]/sigma) -
                     2.0*exp(-(b1[i]+2.0*b0[i])/sigma)*cos(w1[i]/sigma);
        double d4 = exp(-2.0*(b0[i]+b1[i])/sigma);
        _n0[i] = (float)n0;
        _n1[i] = (float)n1;
        _n2[i] = (float)n2;
        _n3[i] = (float)n3;
        _d1[i] = (float)d1;
        _d2[i] = (float)d2;
        _d3[i] = (float)d3;
        _d4[i] = (float)d4;
      }
      scaleN(sigma);
    }

    /**
     * Scales numerator filter coefficients to normalize the filters.
     * For example, the sum of the 0th-derivative filter coefficients
     * should be 1.0. The scale factors are computed from finite-length
     * approximations to the impulse responses of the three filters.
     */
    private void scaleN(double sigma) {
      int n = 1+2*(int)(10.0*sigma);
      float[] x = new float[n];
      float[] y0 = new float[n];
      float[] y1 = new float[n];
      float[] y2 = new float[n];
      int m = (n-1)/2;
      x[m] = 1.0f;
      applyN(0,x,y0);
      applyN(1,x,y1);
      applyN(2,x,y2);
      double[] s = new double[3];
      for (int i=0,j=n-1; i<j; ++i,--j) {
        double t = i-m;
        s[0] += y0[j]+y0[i];
        s[1] += sin(t/sigma)*(y1[j]-y1[i]);
        s[2] += cos(t*sqrt(2.0)/sigma)*(y2[j]+y2[i]);
      }
      s[0] += y0[m];
      s[2] += y2[m];
      s[1] *= sigma*exp(0.5);
      s[2] *= -(sigma*sigma)/2.0*exp(1.0);
      for (int i=0; i<3; ++i) {
        _n0[i] /= s[i];
        _n1[i] /= s[i];
        _n2[i] /= s[i];
        _n3[i] /= s[i];
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  private static class VanVlietFilter extends Filter {

    VanVlietFilter(double sigma) {
      makeG(sigma);
    }

    void applyN(int nd, float[] x, float[] y) {
      checkArrays(x,y);
      if (sameArrays(x,y))
        x = copy(x);
      _g[nd][0][0].applyForward(x,y);
      _g[nd][0][1].accumulateReverse(x,y);
      _g[nd][1][0].accumulateForward(x,y);
      _g[nd][1][1].accumulateReverse(x,y);
    }

    void applyXN(int nd, float[][] x, float[][] y) {
      checkArrays(x,y);
      if (sameArrays(x,y))
        x = copy(x);
      _g[nd][0][0].apply2Forward(x,y);
      _g[nd][0][1].accumulate2Reverse(x,y);
      _g[nd][1][0].accumulate2Forward(x,y);
      _g[nd][1][1].accumulate2Reverse(x,y);
    }

    // Poles (inverses) for 4th-order filters published by van Vliet, et al.
    private static Cdouble[][] POLES = {
      {new Cdouble( 1.12075, 1.27788),
       new Cdouble( 1.12075,-1.27788),
       new Cdouble( 1.76952, 0.46611),
       new Cdouble( 1.76952,-0.46611)},
      {new Cdouble( 1.04185, 1.24034),
       new Cdouble( 1.04185,-1.24034),
       new Cdouble( 1.69747, 0.44790),
       new Cdouble( 1.69747,-0.44790)},
      {new Cdouble( 0.94570, 1.21064),
       new Cdouble( 0.94570,-1.21064),
       new Cdouble( 1.60161, 0.42647),
       new Cdouble( 1.60161,-0.42647)}
    };

    private Recursive2ndOrderFilter[][][] _g;

    private void makeG(double sigma) {
      _g = new Recursive2ndOrderFilter[3][2][2];

      // Loop over filters for 0th, 1st, and 2nd derivatives.
      for (int nd=0; nd<3; ++nd) {

        // Adjust the poles for the scale factor q.
        Cdouble[] poles = adjustPoles(sigma,POLES[nd]);

        // Filter gain.
        double gain = computeGain(poles);
        double gg = gain*gain;

        // Residues.
        Cdouble d0 = new Cdouble(poles[0]);
        Cdouble d1 = new Cdouble(poles[2]);
        Cdouble e0 = d0.inv();
        Cdouble e1 = d1.inv();
        Cdouble g0 = gr(nd,d0,poles,gg);
        Cdouble g1 = gr(nd,d1,poles,gg);

        // Coefficients for 2nd-order recursive filters.
        double a10 = -2.0*d0.r;
        double a11 = -2.0*d1.r;
        double a20 = d0.norm();
        double a21 = d1.norm();
        double b00,b01,b10,b11,b20,b21;

        // 0th- and 2nd-derivative filters are symmetric.
        if (nd==0 || nd==2) {
          b10 = g0.i/e0.i;
          b11 = g1.i/e1.i;
          b00 = g0.r-b10*e0.r;
          b01 = g1.r-b11*e1.r;
          b20 = 0.0;
          b21 = 0.0;
          _g[nd][0][0] = makeFilter(b00,b10,b20,a10,a20);
          _g[nd][1][0] = makeFilter(b01,b11,b21,a11,a21);
          b20 -= b00*a20;
          b21 -= b01*a21;
          b10 -= b00*a10;
          b11 -= b01*a11;
          b00 = 0.0;
          b01 = 0.0;
          _g[nd][0][1] = makeFilter(b00,b10,b20,a10,a20);
          _g[nd][1][1] = makeFilter(b01,b11,b21,a11,a21);

        // 1st-derivative filter is anti-symmetric.
        } else if (nd==1) {
          b20 = g0.i/e0.i;
          b21 = g1.i/e1.i;
          b10 = g0.r-b20*e0.r;
          b11 = g1.r-b21*e1.r;
          b00 = 0.0;
          b01 = 0.0;
          _g[nd][0][0] = makeFilter(b00,b10,b20,a10,a20);
          _g[nd][1][0] = makeFilter(b01,b11,b21,a11,a21);
          b20 = -b20;
          b21 = -b21;
          b10 = -b10;
          b11 = -b11;
          b00 = 0.0;
          b01 = 0.0;
          _g[nd][0][1] = makeFilter(b00,b10,b20,a10,a20);
          _g[nd][1][1] = makeFilter(b01,b11,b21,a11,a21);
        }
      }
    }
    Recursive2ndOrderFilter makeFilter(
      double b0, double b1, double b2, double a1, double a2)
    {
      return new Recursive2ndOrderFilter(
        (float)b0,(float)b1,(float)b2,(float)a1,(float)a2);
    }

    /**
     * Evaluates residue of G(z) for the n'th derivative and j'th pole.
     */
    private Cdouble gr(int nd, Cdouble polej, Cdouble[] poles, double gain) {
      Cdouble pj = polej;
      Cdouble qj = pj.inv();
      Cdouble c1 = new Cdouble(1.0,0.0);
      Cdouble gz = new Cdouble(c1);
      if (nd==1) {
        gz.timesEquals(c1.minus(qj));
        gz.timesEquals(c1.plus(pj));
        gz.timesEquals(pj);
        gz.timesEquals(0.5);
      } else if (nd==2) {
        gz.timesEquals(c1.minus(qj));
        gz.timesEquals(c1.minus(pj));
        gz.timesEquals(-1.0);
      }
      Cdouble gp = new Cdouble(c1);
      int np = poles.length;
      for (int ip=0; ip<np; ++ip) {
        Cdouble pi = poles[ip];
        if (!pi.equals(pj) && !pi.equals(pj.conj()))
          gp.timesEquals(c1.minus(pi.times(qj)));
        gp.timesEquals(c1.minus(pi.times(pj)));
      }
      return gz.over(gp).times(gain);
    }

    private static Cdouble[] adjustPoles(double sigma, Cdouble[] poles) {

      // Simple search for scale factor q that yields the desired sigma.
      double q = sigma;
      double s = computeSigma(q,poles);
      for (int iter=0; abs(sigma-s)>sigma*1.0e-8; ++iter) {
        //System.out.println("sigma="+sigma+" s="+s+" q="+q);
        Check.state(iter<100,"number of iterations less than 100");
        s = computeSigma(q,poles);
        q *= sigma/s;
      }

      // Adjust poles.
      int npole = poles.length;
      Cdouble[] apoles = new Cdouble[npole];
      for (int ipole=0; ipole<npole; ++ipole) {
        Cdouble pi = poles[ipole];
        double a = pow(pi.abs(),2.0/q);
        double t = atan2(pi.i,pi.r)*2.0/q;
        apoles[ipole] = Cdouble.polar(a,t).inv();
      }
      return apoles;
    }

    private static double computeGain(Cdouble[] poles) {
      int npole = poles.length;
      Cdouble c1 = new Cdouble(1.0,0.0);
      Cdouble cg = new Cdouble(c1);
      for (int ipole=0; ipole<npole; ++ipole) {
        cg.timesEquals(c1.minus(poles[ipole]));
      }
      return cg.r;
    }

    private static double computeSigma(double sigma, Cdouble[] poles) {
      int npole = poles.length;
      double q = sigma/2.0;
      Cdouble c1 = new Cdouble(1.0);
      Cdouble cs = new Cdouble();
      for (int ipole=0; ipole<npole; ++ipole) {
        Cdouble pi = poles[ipole];
        double a = pow(pi.abs(),-1.0/q);
        double t = atan2(pi.i,pi.r)/q;
        Cdouble b = Cdouble.polar(a,t);
        Cdouble c = c1.minus(b);
        Cdouble d = c.times(c);
        cs.plusEquals(b.times(2.0).over(d));
      }
      return sqrt(cs.r);
    }
  }
}
