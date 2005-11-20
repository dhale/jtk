/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Check;
import edu.mines.jtk.util.Cdouble;

/**
 * Recursive 2nd-order filter. This filter solves a linear, 2nd-order, 
 * constant-coefficient difference equation of the form
 * <pre>
 * y[i] = b0*x[i]+b1*x[i-1]+b2*x[i-2]-a1*y[i-1]-a2*y[i-2],
 * </pre>
 * for i = 0, 1, 2, ..., n-1, where x[i] = y[i] = 0 for i&lt;0.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.19
 */
public class Recursive2ndOrderFilter {

  /**
   * Constructs a recursive 2nd-order filter with specified coefficients.
   * If some of the coefficients are zero, the filter may be of only 1st
   * or even 0th order.
   * @param b0 a filter coefficient.
   * @param b1 a filter coefficient.
   * @param b2 a filter coefficient.
   * @param a1 a filter coefficient.
   * @param a2 a filter coefficient.
   */
  public Recursive2ndOrderFilter(
    float b0, float b1, float b2, float a1, float a2) 
  {
    _b0 = b0;
    _b1 = b1;
    _b2 = b2;
    _a1 = a1;
    _a2 = a2;
  }

  /**
   * Constructs a recursive 2nd-order filter from pole, zero, and gain.
   * This filter is actually a 1st-order filter, because it has only
   * one (real) pole and zero.
   * @param pole the pole.
   * @param zero the zero.
   * @param gain the filter gain.
   */
  public Recursive2ndOrderFilter(double pole, double zero, double gain) {
    _b0 = (float)(gain);
    _b1 = (float)(-gain*zero);
    _a1 = (float)(-pole);
  }

  /**
   * Constructs a recursive 2nd-order filter from poles, zeros, and gain.
   * The poles must be real or conjugate pairs; likewise for the zeros.
   * @param pole1 the 1st pole.
   * @param pole2 the 2nd pole.
   * @param zero1 the 1st zero.
   * @param zero2 the 2nd zero.
   * @param gain the filter gain.
   */
  public Recursive2ndOrderFilter(
    Cdouble pole1, Cdouble pole2,
    Cdouble zero1, Cdouble zero2,
    double gain)
  {
    Check.argument(pole1.i==0.0     &&  pole2.i==0.0 ||
                   pole2.r==pole1.r && -pole2.i==pole1.i,
                   "poles are real or conjugate pair");
    Check.argument(zero1.i==0.0     &&  zero2.i==0.0 ||
                   zero2.r==zero1.r && -zero2.i==zero1.i,
                   "zeros are real or conjugate pair");
    _b0 = (float)(gain);
    _b1 = (float)(-(zero1.r+zero2.r)*gain);
    _b2 = (float)((zero1.times(zero2)).r*gain);
    _a1 = (float)(-(pole1.r+pole2.r));
    _a2 = (float)((pole1.times(pole2)).r);
  }

  /**
   * Applies this filter in the forward direction. 
   * <p>
   * Input and output arrays may be the same array, but must have equal
   * lengths.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyForward(float[] x, float[] y) {
    checkArrays(x,y);
    int n = y.length;

    // Special case b1 = b2 = a2 = 0.
    if (_b1==0.0f && _b2==0.0f && _a2==0.0f) {
      float yim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float xi = x[i];
        float yi = _b0*xi-_a1*yim1;
        y[i] = yi;
        yim1 = yi;
      }
    }

    // Special case b2 = a2 = 0.
    else if (_b2==0.0f && _a2==0.0f) {
      float yim1 = 0.0f;
      float xim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xim1-_a1*yim1;
        y[i] = yi;
        yim1 = yi;
        xim1 = xi;
      }
    }

    // Special case b2 = 0.
    else if (_b2==0.0f) {
      float yim2 = 0.0f;
      float yim1 = 0.0f;
      float xim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xim1-_a1*yim1-_a2*yim2;
        y[i] = yi;
        yim2 = yim1;
        yim1 = yi;
        xim1 = xi;
      }
    }

    // Special case b0 = 0.
    else if (_b0==0.0f) {
      float yim2 = 0.0f;
      float yim1 = 0.0f;
      float xim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float yi = _b1*xim1-_a1*yim1-_a2*yim2;
        y[i] = yi;
        yim2 = yim1;
        yim1 = yi;
        xim1 = x[i];
      }
    }

    // General case.
    else { 
      float yim2 = 0.0f;
      float yim1 = 0.0f;
      float xim2 = 0.0f;
      float xim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xim1+_b2*xim2-_a1*yim1-_a2*yim2;
        y[i] = yi;
        yim2 = yim1;
        yim1 = yi;
        xim2 = xim1;
        xim1 = xi;
      }
    }
  }

  /**
   * Applies this filter in the reverse direction. 
   * <p>
   * Input and output arrays may be the same array, but must have equal
   * lengths.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyReverse(float[] x, float[] y) {
    checkArrays(x,y);
    int n = y.length;

    // Special case b1 = b2 = a2 = 0.
    if (_b1==0.0f && _b2==0.0f && _a2==0.0f) {
      float yip1 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float xi = x[i];
        float yi = _b0*xi-_a1*yip1;
        y[i] = yi;
        yip1 = yi;
      }
    }

    // Special case b2 = a2 = 0.
    else if (_b2==0.0f && _a2==0.0f) {
      float xip1 = 0.0f;
      float yip1 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xip1-_a1*yip1;
        y[i] = yi;
        yip1 = yi;
        xip1 = xi;
      }
    }

    // Special case b2 = 0.
    else if (_b2==0.0f) {
      float xip1 = 0.0f;
      float yip1 = 0.0f;
      float yip2 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xip1-_a1*yip1-_a2*yip2;
        y[i] = yi;
        yip2 = yip1;
        yip1 = yi;
        xip1 = xi;
      }
    }

    // Special case b0 = 0.
    else if (_b0==0.0f) {
      float xip1 = 0.0f;
      float xip2 = 0.0f;
      float yip1 = 0.0f;
      float yip2 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float yi = _b1*xip1+_b2*xip2-_a1*yip1-_a2*yip2;
        y[i] = yi;
        yip2 = yip1;
        yip1 = yi;
        xip2 = xip1;
        xip1 = x[i];
      }
    }

    // General case.
    else { 
      float xip1 = 0.0f;
      float xip2 = 0.0f;
      float yip1 = 0.0f;
      float yip2 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xip1+_b2*xip2-_a1*yip1-_a2*yip2;
        y[i] = yi;
        yip2 = yip1;
        yip1 = yi;
        xip2 = xip1;
        xip1 = xi;
      }
    }
  }

  /**
   * Applies this filter in the forward direction, accumulating the output. 
   * This method filters the input, and adds the result to the output; it
   * is most useful when implementing parallel forms of recursive filters.
   * <p>
   * Input and output arrays may be the same array, but must have equal
   * lengths.
   * @param x the input array.
   * @param y the output array.
   */
  public void accumulateForward(float[] x, float[] y) {
    checkArrays(x,y);
    int n = y.length;

    // Special case b1 = b2 = a2 = 0.
    if (_b1==0.0f && _b2==0.0f && _a2==0.0f) {
      float yim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float xi = x[i];
        float yi = _b0*xi-_a1*yim1;
        y[i] += yi;
        yim1 = yi;
      }
    }

    // Special case b2 = a2 = 0.
    else if (_b2==0.0f && _a2==0.0f) {
      float yim1 = 0.0f;
      float xim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xim1-_a1*yim1;
        y[i] += yi;
        yim1 = yi;
        xim1 = xi;
      }
    }

    // Special case b2 = 0.
    else if (_b2==0.0f) {
      float yim2 = 0.0f;
      float yim1 = 0.0f;
      float xim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xim1-_a1*yim1-_a2*yim2;
        y[i] += yi;
        yim2 = yim1;
        yim1 = yi;
        xim1 = xi;
      }
    }

    // Special case b0 = 0.
    else if (_b0==0.0f) {
      float yim2 = 0.0f;
      float yim1 = 0.0f;
      float xim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float yi = _b1*xim1-_a1*yim1-_a2*yim2;
        y[i] += yi;
        yim2 = yim1;
        yim1 = yi;
        xim1 = x[i];
      }
    }

    // General case.
    else { 
      float yim2 = 0.0f;
      float yim1 = 0.0f;
      float xim2 = 0.0f;
      float xim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xim1+_b2*xim2-_a1*yim1-_a2*yim2;
        y[i] += yi;
        yim2 = yim1;
        yim1 = yi;
        xim2 = xim1;
        xim1 = xi;
      }
    }
  }

  /**
   * Applies this filter in the reverse direction, accumulating the output. 
   * This method filters the input, and adds the result to the output; it
   * is most useful when implementing parallel forms of recursive filters.
   * <p>
   * Input and output arrays may be the same array, but must have equal
   * lengths.
   * @param x the input array.
   * @param y the output array.
   */
  public void accumulateReverse(float[] x, float[] y) {
    checkArrays(x,y);
    int n = y.length;

    // Special case b1 = b2 = a2 = 0.
    if (_b1==0.0f && _b2==0.0f && _a2==0.0f) {
      float yip1 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float xi = x[i];
        float yi = _b0*xi-_a1*yip1;
        y[i] += yi;
        yip1 = yi;
      }
    }

    // Special case b2 = a2 = 0.
    else if (_b2==0.0f && _a2==0.0f) {
      float xip1 = 0.0f;
      float yip1 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xip1-_a1*yip1;
        y[i] += yi;
        yip1 = yi;
        xip1 = xi;
      }
    }

    // Special case b2 = 0.
    else if (_b2==0.0f) {
      float xip1 = 0.0f;
      float yip1 = 0.0f;
      float yip2 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xip1-_a1*yip1-_a2*yip2;
        y[i] += yi;
        yip2 = yip1;
        yip1 = yi;
        xip1 = xi;
      }
    }

    // Special case b0 = 0.
    else if (_b0==0.0f) {
      float xip1 = 0.0f;
      float xip2 = 0.0f;
      float yip1 = 0.0f;
      float yip2 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float yi = _b1*xip1+_b2*xip2-_a1*yip1-_a2*yip2;
        y[i] += yi;
        yip2 = yip1;
        yip1 = yi;
        xip2 = xip1;
        xip1 = x[i];
      }
    }

    // General case.
    else { 
      float xip1 = 0.0f;
      float xip2 = 0.0f;
      float yip1 = 0.0f;
      float yip2 = 0.0f;
      for (int i=n-1; i>=0; --i) {
        float xi = x[i];
        float yi = _b0*xi+_b1*xip1+_b2*xip2-_a1*yip1-_a2*yip2;
        y[i] += yi;
        yip2 = yip1;
        yip1 = yi;
        xip2 = xip1;
        xip1 = xi;
      }
    }
  }

  /**
   * Applies this filter in 1st dimension in the forward direction. 
   * <p>
   * Input and output arrays may be the same array, but must be
   * regular and have equal lengths.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1Forward(float[][] x, float[][] y) {
    checkArrays(x,y);
    int n2 = y.length;
    int n1 = y[0].length;
    for (int i2=0; i2<n2; ++i2) {
      applyForward(x[i2],y[i2]);
    }
  }

  /**
   * Applies this filter in 2nd dimension in the forward direction. 
   * <p>
   * Input and output arrays may be the same array, but must be
   * regular and have equal lengths.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2Forward(float[][] x, float[][] y) {
    checkArrays(x,y);
    int n2 = y.length;
    int n1 = y[0].length;

    // Special case b1 = b2 = a2 = 0.
    if (_b1==0.0f && _b2==0.0f && _a2==0.0f) {
      float[] yim1 = new float[n1];
      for (int i2=0; i2<n2; ++i2) {
        float[] xi = x[i2];
        float[] yi = y[i2];
        for (int i1=0; i1<n1; ++i1) {
          yi[i1] = _b0*xi[i1]-
                              _a1*yim1[i1];
        }
        yim1 = yi;
      }
    }

    // Special case b2 = a2 = 0.
    else if (_b2==0.0f && _a2==0.0f) {
      float[] yim1 = new float[n1];
      float[] xim1 = new float[n1];
      float[] xi = new float[n1];
      for (int i2=0; i2<n2; ++i2) {
        float[] x2 = x[i2];
        float[] yi = y[i2];
        for (int i1=0; i1<n1; ++i1) {
          xi[i1] = x2[i1];
          yi[i1] = _b0*xi[i1]+_b1*xim1[i1]-
                              _a1*yim1[i1];
        }
        yim1 = yi;
        float[] xt = xim1;
        xim1 = xi;
        xi = xt;
      }
    }

    // Special case b2 = 0.
    else if (_b2==0.0f) {
      float[] yim2 = new float[n1];
      float[] yim1 = new float[n1];
      float[] xim1 = new float[n1];
      float[] xi = new float[n1];
      for (int i2=0; i2<n2; ++i2) {
        float[] x2 = x[i2];
        float[] yi = y[i2];
        for (int i1=0; i1<n1; ++i1) {
          xi[i1] = x2[i1];
          yi[i1] = _b0*xi[i1]+_b1*xim1[i1]-
                              _a1*yim1[i1]-_a2*yim2[i1];
        }
        yim2 = yim1;
        yim1 = yi;
        float[] xt = xim1;
        xim1 = xi;
        xi = xt;
      }
    }

    // Special case b0 = 0.
    else if (_b0==0.0f) {
      float[] yim2 = new float[n1];
      float[] yim1 = new float[n1];
      float[] xim2 = new float[n1];
      float[] xim1 = new float[n1];
      float[] xi = new float[n1];
      for (int i2=0; i2<n2; ++i2) {
        float[] x2 = x[i2];
        float[] yi = y[i2];
        for (int i1=0; i1<n1; ++i1) {
          xi[i1] = x2[i1];
          yi[i1] = _b1*xim1[i1]+_b2*xim2[i1]-
                   _a1*yim1[i1]-_a2*yim2[i1];
        }
        yim2 = yim1;
        yim1 = yi;
        float[] xt = xim2;
        xim2 = xim1;
        xim1 = xi;
        xi = xt;
      }
    }

    // General case.
    else {
      float[] yim2 = new float[n1];
      float[] yim1 = new float[n1];
      float[] xim2 = new float[n1];
      float[] xim1 = new float[n1];
      float[] xi = new float[n1];
      for (int i2=0; i2<n2; ++i2) {
        float[] x2 = x[i2];
        float[] yi = y[i2];
        for (int i1=0; i1<n1; ++i1) {
          xi[i1] = x2[i1];
          yi[i1] = _b0*xi[i1]+_b1*xim1[i1]+_b2*xim2[i1]-
                              _a1*yim1[i1]-_a2*yim2[i1];
        }
        yim2 = yim1;
        yim1 = yi;
        float[] xt = xim2;
        xim2 = xim1;
        xim1 = xi;
        xi = xt;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private float _b0,_b1,_b2,_a1,_a2; // filter coefficients

  private static void checkArrays(float[] x, float[] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
  }

  private static void checkArrays(float[][] x, float[][] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
    Check.argument(x[0].length==y[0].length,"x[0].length==y[0].length");
    Check.argument(Array.isRegular(x),"x is regular");
    Check.argument(Array.isRegular(y),"y is regular");
  }

  private static void checkArrays(float[][][] x, float[][][] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
    Check.argument(x[0].length==y[0].length,"x[0].length==y[0].length");
    Check.argument(x[0][0].length==y[0][0].length,
      "x[0][0].length==y[0][0].length");
    Check.argument(Array.isRegular(x),"x is regular");
    Check.argument(Array.isRegular(y),"y is regular");
  }
}
