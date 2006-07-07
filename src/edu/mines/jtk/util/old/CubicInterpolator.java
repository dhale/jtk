package com.lgc.idh.util;

import com.lgc.idh.lang.Check;
import com.lgc.idh.lang.M;

/**
 * Piecewise cubic interpolation of a function y(x) (or its derivatives).
 * <p>
 * Piecewise cubic interpolators differ in the method they use to
 * compute the slopes at the knots. The classic cubic spline computes
 * the slopes to obtain a continuous second derivative at the knots.
 * These splines often yield unacceptable wiggliness (overshoot) between 
 * the knots. A linear spline yields no overshoot, but has discontinuous 
 * first (and higher) derivatives. A monotonic spline has continuous 
 * first derivatives and yields monotonic interpolation (with no overshoot) 
 * where function values at the knots are monotonically increasing or 
 * decreasing.
 * <p>
 * For x outside the range of values specified when an interpolator was
 * constructed, the interpolator <em>extrapolates</em> using the cubic 
 * polynomial corresponding to the knot nearest to x.
 * @author Dave Hale
 * @version 1998.11.17
 */
public class CubicInterpolator {

  /**
   * Piecewise cubic interpolation with a continuous value (zero'th 
   * derivative) but discontinuous higher order derivatives. This
   * method is equivalent to (but less efficient than) simple linear 
   * interpolation.
   */
  public static final int LINEAR = 0;

  /**
   * Piecewise cubic interpolation with a continuous first derivative 
   * and discontinuous higher order derivatives. This method preserves
   * monotonicity. Where function values specified at the knots are 
   * monotonic, interpolated function values are also monotonic.
   */
  public static final int MONOTONIC = 1;

  /**
   * Piecewise cubic interpolation with a continuous second derivative 
   * and discontinuous higher order derivatives.
   */
  public static final int SPLINE = 2;

  /**
   * Constructs an interpolator.
   * @param method interpolation method: LINEAR, MONOTONIC, or SPLINE.
   * @param n number of x and y(x) values specified.
   * @param x array[n] of values at which y(x) are specified.
   *  These values must be monotonically increasing or decreasing, 
   *  with no equal values. (In other words, the array must be 
   *  monotonic-definite.)
   * @param y array[n] of function values y(x).
   */
  public CubicInterpolator(int method, int n, float[] x, float[] y) {
    Check.argument(Array.isMonotonicDefinite(x));
    Check.argument(
      method==LINEAR || method==MONOTONIC || method==SPLINE,
      "method not valid");
    _xd = new float[n];
    _yd = new float[n][4];
    for (int i=0; i<n; ++i) {
      _xd[i] = x[i];
      _yd[i][0] = y[i];
    }
    if (method==LINEAR) {
      initLinear(n,_xd,_yd);
    } else if (method==MONOTONIC) {
      initMonotonic(n,_xd,_yd);
    } else if (method==SPLINE) {
      initSpline(n,_xd,_yd);
    } else {
      assert false;
    }
  }

  /**
   * Interpolate a function value y(x).
   * @param x value at which to interpolate.
   * @return interpolated function value y(x).
   */
  public final float interpolate(float x) {
    int index = Array.binarySearch(_xd,x,_index);
    if (index<0) index = 0;
    _index = index;
    float[] yd = _yd[index];
    float delx = x-_xd[index];
    return yd[0]+delx*(yd[1]+delx*(yd[2]*FLT_O2+delx*(yd[3]*FLT_O6)));
  }

  /**
   * Interpolate the first derivative y'(x).
   * @param x value at which to interpolate.
   * @return interpolated first derivative y'(x).
   */
  public final float interpolate1(float x) {
    int index = Array.binarySearch(_xd,x,_index);
    if (index<0) index = 0;
    _index = index;
    float[] yd = _yd[index];
    float delx = x-_xd[index];
    return yd[1]+delx*(yd[2]+delx*(yd[3]*FLT_O2));
  }

  /**
   * Interpolate the second derivative y''(x).
   * @param x value at which to interpolate.
   * @return interpolated second derivative y''(x).
   */
  public final float interpolate2(float x) {
    int index = Array.binarySearch(_xd,x,_index);
    if (index<0) index = 0;
    _index = index;
    float[] yd = _yd[index];
    float delx = x-_xd[index];
    return yd[2]+delx*yd[3];
  }

  /**
   * Interpolate the third derivative y'''(x).
   * @param x value at which to interpolate.
   * @return interpolated third derivative y'''(x).
   */
  public final float interpolate3(float x) {
    int index = Array.binarySearch(_xd,x,_index);
    if (index<0) index = 0;
    _index = index;
    float[] yd = _yd[index];
    return yd[3];
  }

  /**
   * Interpolate an array of function values y(x).
   * @param n number of values to interpolate.
   * @param x array of values at which to interpolate.
   * @param y array of interpolated function values.
   */
  public void interpolate(int n, float[] x, float[] y) {
    for (int i=0; i<n; ++i) y[i] = interpolate(x[i]);
  }

  /**
   * Interpolate an array of first derivatives y'(x).
   * @param n number of derivatives to interpolate.
   * @param x array of values at which to interpolate.
   * @param y array of interpolated first derivatives y'(x).
   */
  public void interpolate1(int n, float[] x, float[] y) {
    for (int i=0; i<n; ++i) y[i] = interpolate1(x[i]);
  }

  /**
   * Interpolate an array of second derivatives y''(x).
   * @param n number of derivatives to interpolate.
   * @param x array of values at which to interpolate.
   * @param y array of interpolated second derivatives y''(x).
   */
  public void interpolate2(int n, float[] x, float[] y) {
    for (int i=0; i<n; ++i) y[i] = interpolate2(x[i]);
  }

  /**
   * Interpolate an array of third derivatives y'''(x).
   * @param n number of derivatives to interpolate.
   * @param x array of values at which to interpolate.
   * @param y array of interpolated third derivatives y'''(x).
   */
  public void interpolate3(int n, float[] x, float[] y) {
    for (int i=0; i<n; ++i) y[i] = interpolate3(x[i]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Private.

  private static final float FLT_O2 = 1.0f/2.0f;
  private static final float FLT_O6 = 1.0f/6.0f;

  private int _index; // index from most recent interpolation.
  private float[] _xd; // array[n] of x.
  private float[][] _yd; // array[n*4] of y, y', y'', and y'''.

  /**
   * Computes cubic interpolation coefficients for linear interpolation.
   */
  private void initLinear(int n, float[] x, float[][] y) {

    // If n=1, then use constant interpolation.
    if (n==1) {
      y[0][1] = 0.0f;
      y[0][2] = 0.0f;
      y[0][3] = 0.0f;
      return;
    }

    // Compute slopes.
    for (int i=0; i<n-1; ++i) {
      y[i][1] = (y[i+1][0]-y[i][0])/(x[i+1]-x[i]);
      y[i][2] = y[i][3] = 0.0f;
    }
    y[n-1][1] = y[n-2][1];
    y[n-1][2] = y[n-1][3] = 0.0f;
  }

  /**
   * Computes cubic interpolation coefficients via the Fritsch-Carlson 
   * method, which preserves monotonicity.
   * <p>
   * The Fritsch-Carlson method yields continuous 1st derivatives, but 2nd
   * and 3rd derivatives are discontinuous.  The method will yield a
   * monotonic interpolant for monotonic data.  1st derivatives are set to
   * zero wherever first divided differences change sign.
   * <p>
   * For more information, see Fritsch, F. N., and Carlson, R. E., 1980, 
   * Monotone piecewise cubic interpolation:  SIAM J. Numer. Anal., v. 17,
   * n. 2, p. 238-246.
   * <p>
   * Also, see the book by Kahaner, D., Moler, C., and Nash, S., 1989, 
   * Numerical Methods and Software, Prentice Hall.  This function was 
   * derived from SUBROUTINE PCHEZ contained on the diskette that comes 
   * with the book.
   */
  private void initMonotonic(int n, float[] x, float[][] y) {
    int i;
    float h1,h2,del1,del2,dmin,dmax,hsum,hsum3,w1,w2,drat1,drat2,divdf3;

    // If n=1, then use constant interpolation.
    if (n==1) {
      y[0][1] = 0.0f;
      y[0][2] = 0.0f;
      y[0][3] = 0.0f;
      return;

    // Else, if n=2, then use linear interpolation.
    } else if (n==2) {
      y[0][1] = y[1][1] = (y[1][0]-y[0][0])/(x[1]-x[0]);
      y[0][2] = y[1][2] = 0.0f;
      y[0][3] = y[1][3] = 0.0f;
      return;
    }

    // Set left end derivative via shape-preserving 3-point formula.
    h1 = x[1]-x[0];
    h2 = x[2]-x[1];
    hsum = h1+h2;
    del1 = (y[1][0]-y[0][0])/h1;
    del2 = (y[2][0]-y[1][0])/h2;
    w1 = (h1+hsum)/hsum;
    w2 = -h1/hsum;
    y[0][1] = w1*del1+w2*del2;
    if (y[0][1]*del1<=0.0f)
      y[0][1] = 0.0f;
    else if (del1*del2<0.0f) {
      dmax = 3.0f*del1;
      if (M.abs(y[0][1])>M.abs(dmax)) y[0][1] = dmax;
    }

    // Loop over interior points.
    for (i=1; i<n-1; ++i) {

      // Compute intervals and slopes.
      h1 = x[i]-x[i-1];
      h2 = x[i+1]-x[i];
      hsum = h1+h2;
      del1 = (y[i][0]-y[i-1][0])/h1;
      del2 = (y[i+1][0]-y[i][0])/h2;

      // If not strictly monotonic, zero derivative.
      if (del1*del2<=0.0f) {
        y[i][1] = 0.0f;
      
      // Else, if strictly monotonic, use Butland's formula:
      //      3*(h1+h2)*del1*del2 
      // -------------------------------
      // ((2*h1+h2)*del1+(h1+2*h2)*del2)
      // computed as follows to avoid roundoff error
      } else {
        hsum3 = hsum+hsum+hsum;
        w1 = (hsum+h1)/hsum3;
        w2 = (hsum+h2)/hsum3;
        dmin = M.min(M.abs(del1),M.abs(del2));
        dmax = M.max(M.abs(del1),M.abs(del2));
        drat1 = del1/dmax;
        drat2 = del2/dmax;
        y[i][1] = dmin/(w1*drat1+w2*drat2);
      }
    }

    // Set right end derivative via shape-preserving 3-point formula.
    w1 = -h2/hsum;
    w2 = (h2+hsum)/hsum;
    y[n-1][1] = w1*del1+w2*del2;
    if (y[n-1][1]*del2<=0.0f)
      y[n-1][1] = 0.0f;
    else if (del1*del2<0.0f) {
      dmax = 3.0f*del2;
      if (M.abs(y[n-1][1])>M.abs(dmax)) y[n-1][1] = dmax;
    }

    // Compute 2nd and 3rd derivatives of cubic polynomials.
    for (i=0; i<n-1; ++i) {
      h2 = x[i+1]-x[i];
      del2 = (y[i+1][0]-y[i][0])/h2;
      divdf3 = y[i][1]+y[i+1][1]-2.0f*del2;
      y[i][2] = 2.0f*(del2-y[i][1]-divdf3)/h2;
      y[i][3] = (divdf3/h2)*(6.0f/h2);
    }
    y[n-1][2] = y[n-2][2]+(x[n-1]-x[n-2])*y[n-2][3];
    y[n-1][3] = y[n-2][3];
  }

  /**
   * Computes cubic spline interpolation coefficients for interpolation 
   * with continuous second derivatives.
   */
  private void initSpline(int n, float[] x, float[][] y) {
    int i;
    float h1,h2,del1,del2,dmax,hsum,w1,w2,divdf3,sleft,sright,alpha,t;
    float[] work;

    // If n=1, then use constant interpolation.
    if (n==1) {
      y[0][1] = 0.0f;
      y[0][2] = 0.0f;
      y[0][3] = 0.0f;
      return;

    // Else, if n=2, then use linear interpolation.
    } else if (n==2) {
      y[0][1] = y[1][1] = (y[1][0]-y[0][0])/(x[1]-x[0]);
      y[0][2] = y[1][2] = 0.0f;
      y[0][3] = y[1][3] = 0.0f;
      return;
    }
    
    // Set left end derivative via shape-preserving 3-point formula.
    h1 = x[1]-x[0];
    h2 = x[2]-x[1];
    hsum = h1+h2;
    del1 = (y[1][0]-y[0][0])/h1;
    del2 = (y[2][0]-y[1][0])/h2;
    w1 = (h1+hsum)/hsum;
    w2 = -h1/hsum;
    sleft = w1*del1+w2*del2;
    if (sleft*del1<=0.0f)
      sleft = 0.0f;
    else if (del1*del2<0.0f) {
      dmax = 3.0f*del1;
      if (M.abs(sleft)>M.abs(dmax)) sleft = dmax;
    }

    // Set right end derivative via shape-preserving 3-point formula.
    h1 = x[n-2]-x[n-3];
    h2 = x[n-1]-x[n-2];
    hsum = h1+h2;
    del1 = (y[n-2][0]-y[n-3][0])/h1;
    del2 = (y[n-1][0]-y[n-2][0])/h2;
    w1 = -h2/hsum;
    w2 = (h2+hsum)/hsum;
    sright = w1*del1+w2*del2;
    if (sright*del2<=0.0f)
      sright = 0.0f;
    else if (del1*del2<0.0f) {
      dmax = 3.0f*del2;
      if (M.abs(sright)>M.abs(dmax)) sright = dmax;
    }
    
    // Compute tridiagonal system coefficients and right-hand-side.
    work = new float[n];
    work[0] = 1.0f;
    y[0][2] = 2.0f*sleft;
    for (i=1; i<n-1; ++i) {
      h1 = x[i]-x[i-1];
      h2 = x[i+1]-x[i];
      del1 = (y[i][0]-y[i-1][0])/h1;
      del2 = (y[i+1][0]-y[i][0])/h2;
      alpha = h2/(h1+h2);
      work[i] = alpha;
      y[i][2] = 3.0f*(alpha*del1+(1.0f-alpha)*del2);
    }
    work[n-1] = 0.0f;
    y[n-1][2] = 2.0f*sright;
    
    // Solve tridiagonal system for slopes.
    t = 2.0f;
    y[0][1] = y[0][2]/t;
    for (i=1; i<n; ++i) {
      y[i][3] = (1.0f-work[i-1])/t;
      t = 2.0f-work[i]*y[i][3];
      y[i][1] = (y[i][2]-work[i]*y[i-1][1])/t;
    }
    for (i=n-2; i>=0; i--) y[i][1] -= y[i+1][3]*y[i+1][1];

    // Compute 2nd and 3rd derivatives of cubic polynomials.
    for (i=0; i<n-1; ++i) {
      h2 = x[i+1]-x[i];
      del2 = (y[i+1][0]-y[i][0])/h2;
      divdf3 = y[i][1]+y[i+1][1]-2.0f*del2;
      y[i][2] = 2.0f*(del2-y[i][1]-divdf3)/h2;
      y[i][3] = (divdf3/h2)*(6.0f/h2);
    }
    y[n-1][2] = y[n-2][2]+(x[n-1]-x[n-2])*y[n-2][3];
    y[n-1][3] = y[n-2][3];
  }
}
