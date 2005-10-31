/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

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
public class Recursive2ndOrderFilter extends RecursiveFilter {

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
   * The input and output arrays may be the same. The length of the 
   * input array must not be less than the length of the output array.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyForward(float[] x, float[] y) {
    Check.argument(x.length>=y.length,"x.length>=y.length");
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

  public void applyReverse(float[] x, float[] y) {
    int n = y.length;
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

  public void applyForwardReverse(float[] x, float[] y) {
    applyForward(x,y);
    int n = y.length;
    float s = 1.0f/((1.0f-_a2)*((1.0f+_a2)*(1.0f+_a2)-_a1*_a1));
    float m10 = s*((1.0f+_a2)*_b1-_a1*(_b0+_b2));
    float m11 = s*((1.0f-_a1*_a1+_a2)*_b0-_a2*((1.0f+_a2)*_b2-_a1*_b1));
    float m20 = s*((1.0f+_a2)*_b0-_a1*_b1+_a1*_a1*_b2-_a2*(1.0f+_a2)*_b2);
    float m21 = s*(_a2*(_a1*(_b0+_b2)-(1.0f+_a2)*_b1));
    float uip1 = _b1*x[n-1]+_b2*x[n-2]-_a1*y[n-1]-_a2*y[n-2];
    float uip2 = _b2*x[n-1]-_a1*uip1-_a2*y[n-1];
    float yip1 = m10*uip2+m11*uip1;
    float yip2 = m20*uip2+m21*uip1;
    for (int i=n-1; i>=0; --i) {
      float ui = y[i];
      float yi = _b0*ui+_b1*uip1+_b2*uip2-_a1*yip1-_a2*yip2;
      y[i] = yi;
      yip2 = yip1;
      yip1 = yi;
      uip2 = uip1;
      uip1 = ui;
    }
  }

  /**
   * Applies this filter in the forward direction, accumulating the output. 
   * This method filters the input, and adds the result to the output; it
   * is most useful when implementing parallel forms of recursive filters.
   * <p>
   * The input and output arrays may be the same. The length of the 
   * input array must not be less than the length of the output array.
   * @param x the input array.
   * @param y the output array.
   */
  public void accumulateForward(float[] x, float[] y) {
    Check.argument(x.length>=y.length,"x.length>=y.length");
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

  ///////////////////////////////////////////////////////////////////////////
  // private

  float _b0,_b1,_b2,_a1,_a2; // filter coefficients
}
