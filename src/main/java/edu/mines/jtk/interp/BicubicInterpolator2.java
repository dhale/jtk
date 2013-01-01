/****************************************************************************
Copyright (c) 2012, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Piecewise bicubic polynomial interpolation of a function y(x1,x2).
 * The interpolated function has continuous first derivatives dy/d1, dy/d2 and
 * cross-derivatives ddy/d1d2. First derivatives are computed using methods
 * that may be specified for each dimension. Cross-derivatives are computed by
 * averaging derivatives of first derivatives.
 * <p>
 * The function y(x1,x2) is specified by samples on a regular grid, which need
 * not be uniform. The regular grid is specified by one-dimensional arrays of
 * monotonically increasing coordinates x1 and x2, such that gridded x1 are
 * identical for all gridded x2, and gridded x2 are identical for all gridded
 * x1.
 * <p>
 * Extrapolation (that is, interpolation outside the specified grid of (x1,x2)
 * coordinates), is performed using cubic polynomials for the nearest grid
 * samples. Extrapolated values can be well outside the [min,max] range of
 * interpolated values, and should typically be avoided.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.12.27
 */
public class BicubicInterpolator2 {

  /**
   * Method used to compute first derivatives.
   */
  public enum Method {
    /**
     * Uses monotonicity-preserving cubic interpolation.
     * This is the default method.
     */
    MONOTONIC,
    /**
     * Uses cubic spline interpolation.
     */
    SPLINE
  }

  /**
   * Constructs an interpolator for specified values y(x1,x2).
   * @param x1 array[n1] of x1 coordinates; monotonically increasing.
   * @param x2 array[n2] of x2 coordinates; monotonically increasing.
   * @param y array[n2][n1] of sampled values y(x1,x2).
   */
  public BicubicInterpolator2(float[] x1, float[] x2, float[][] y) {
    this(Method.MONOTONIC,Method.MONOTONIC,x1,x2,y);
  }

  /**
   * Constructs an interpolator for specified methods and values.
   * @param method1 method used to compute dy/d1.
   * @param method2 method used to compute dy/d2.
   * @param x1 array[n1] of x1 coordinates; monotonically increasing.
   * @param x2 array[n2] of x2 coordinates; monotonically increasing.
   * @param y array[n2][n1] of sampled values y(x1,x2).
   */
  public BicubicInterpolator2(
    Method method1, Method method2, 
    float[] x1, float[] x2, float[][] y) 
  {
    this(method1,method2,x1.length,x2.length,x1,x2,y);
  }

  /**
   * Constructs an interpolator for specified methods and values.
   * @param method1 method used to compute dy/d1.
   * @param method2 method used to compute dy/d2.
   * @param n1 number of x1 coordinates.
   * @param n2 number of x2 coordinates.
   * @param x1 array[n1] of x1 coordinates; monotonically increasing.
   * @param x2 array[n2] of x2 coordinates; monotonically increasing.
   * @param y array[n2][n1] of sampled values y(x1,x2).
   */
  public BicubicInterpolator2(
    Method method1, Method method2,
    int n1, int n2, float[] x1, float[] x2, float[][] y) 
  {
    Check.argument(isMonotonic(x1), "array x1 is monotonic");
    Check.argument(isMonotonic(x2), "array x2 is monotonic");
    _n1 = n1;
    _n2 = n2;
    _x1 = copy(n1,x1);
    _x2 = copy(n2,x2);
    _a = makeCoefficients(method1,method2,n1,n2,x1,x2,y);
  }

  /**
   * Returns interpolated value y.
   * Same as {@link #interpolate00(float,float)}.
   * @param x1 coordinate x1.
   * @param x2 coordinate x2.
   * @return interpolated y(x1,x2).
   */
  public float interpolate(float x1, float x2) {
    return interpolate00(x1,x2);
  }

  /**
   * Returns interpolated value y.
   * @param x1 coordinate x1.
   * @param x2 coordinate x2.
   * @return interpolated y(x1,x2).
   */
  public float interpolate00(float x1, float x2) {
    return interpolate00(x1,x2,_ks);
  }

  /**
   * Returns interpolated partial derivative dy/d1.
   * @param x1 coordinate x1.
   * @param x2 coordinate x2.
   * @return interpolated dy/d1.
   */
  public float interpolate10(float x1, float x2) {
    return interpolate10(x1,x2,_ks);
  }

  /**
   * Returns interpolated partial derivative dy/d2.
   * @param x1 coordinate x1.
   * @param x2 coordinate x2.
   * @return interpolated dy/d2.
   */
  public float interpolate01(float x1, float x2) {
    return interpolate01(x1,x2,_ks);
  }

  /**
   * Returns an array of interpolated values y.
   * Same as {@link #interpolate00(Sampling,Sampling)}.
   * @param s1 sampling of coordinate x1.
   * @param s2 sampling of coordinate x2.
   * @return interpolated y(x1,x2).
   */
  public float[][] interpolate(Sampling s1, Sampling s2) {
    return interpolate00(s1,s2);
  }

  /**
   * Returns an array of interpolated values y.
   * @param s1 sampling of coordinate x1.
   * @param s2 sampling of coordinate x2.
   * @return interpolated y(x1,x2).
   */
  public float[][] interpolate00(Sampling s1, Sampling s2) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    float[][] y = new float[n2][n1];
    interpolate00(s1,s2,y);
    return y;
  }

  /**
   * Computes an array of interpolated values y.
   * Same as {@link #interpolate00(Sampling,Sampling,float[][])}.
   * @param s1 sampling of coordinate x1.
   * @param s2 sampling of coordinate x2.
   * @param y output array of interpolated y(x1,x2).
   */
  public void interpolate(Sampling s1, Sampling s2, float[][] y) {
    interpolate00(s1,s2,y);
  }

  /**
   * Computes an array of interpolated values y.
   * @param s1 sampling of coordinate x1.
   * @param s2 sampling of coordinate x2.
   * @param y output array of interpolated y(x1,x2).
   */
  public void interpolate00(Sampling s1, Sampling s2, float[][] y) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int[] k1 = makeIndices(s1,_x1);
    int[] k2 = makeIndices(s2,_x2);
    for (int i2=0; i2<n2; ++i2) {
      float x2 = (float)s2.getValue(i2);
      for (int i1=0; i1<n1; ++i1) {
        float x1 = (float)s1.getValue(i1);
        y[i2][i1] = interpolate00(x1,x2,k1[i1],k2[i2]);
      }
    }
  }

  /**
   * Returns an array of interpolated values y.
   * Same as {@link #interpolate00(float[],float[])}.
   * @param x1 array[n1] of coordinates x1.
   * @param x2 array[n2] of coordinates x2.
   * @return array[n2][n1] of interpolated y(x1,x2).
   */
  public float[][] interpolate(float[] x1, float[] x2) {
    return interpolate00(x1,x2);
  }

  /**
   * Returns an array of interpolated values y.
   * @param x1 array[n1] of coordinates x1.
   * @param x2 array[n2] of coordinates x2.
   * @return array[n2][n1] of interpolated y(x1,x2).
   */
  public float[][] interpolate00(float[] x1, float[] x2) {
    int n1 = x1.length;
    int n2 = x2.length;
    float[][] y = new float[n2][n1];
    interpolate00(x1,x2,y);
    return y;
  }

  /**
   * Computes an array of interpolated values y.
   * Same as {@link #interpolate00(float[],float[],float[][])}.
   * @param x1 array[n1] of coordinates x1.
   * @param x2 array[n2] of coordinates x2.
   * @param y output array[n2][n1] of interpolated y(x1,x2).
   */
  public void interpolate(float[] x1, float[] x2, float[][] y) {
    interpolate00(x1,x2,y);
  }

  /**
   * Computes an array of interpolated values y.
   * @param x1 array[n1] of coordinates x1.
   * @param x2 array[n2] of coordinates x2.
   * @param y output array[n2][n1] of interpolated y(x1,x2).
   */
  public void interpolate00(float[] x1, float[] x2, float[][] y) {
    int n1 = x1.length;
    int n2 = x2.length;
    int[] k1 = makeIndices(x1,_x1);
    int[] k2 = makeIndices(x2,_x2);
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        y[i2][i1] = interpolate00(x1[i1],x2[i2],k1[i1],k2[i2]);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void trace(String s) {
    System.out.println(s);
  }

  private int _n1,_n2; // number of samples
  private float[] _x1; // array of x1 coordinates in regular grid
  private float[] _x2; // array of x2 coordinates in regular grid
  private float[][][][] _a; // array[n2-1][n1-1][4][4] of coefficients
  private int[] _ks = {0,0}; // coefficients used in previous interpolation

  private static int index(float x, float[] xs, int i) {
    i = binarySearch(xs,x,i);
    if (i<0) 
      i = (i<-1)?-2-i:0;
    if (i>=xs.length-1)
      i = xs.length-2;
    return i;
  }

  private void updateIndices(float x1, float x2, int[] ks) {
    ks[0] = index(x1,_x1,ks[0]); 
    ks[1] = index(x2,_x2,ks[1]); 
  }

  private static int[] makeIndices(float[] xi, float[] xs) {
    int n = xi.length;
    int[] ki = new int[n];
    ki[0] = index(xi[0],xs,0);
    for (int i=1; i<n; ++i)
      ki[i] = index(xi[i],xs,ki[i-1]);
    return ki;
  }
  private static int[] makeIndices(Sampling si, float[] xs) {
    int n = si.getCount();
    int[] ki = new int[n];
    ki[0] = index((float)si.getValue(0),xs,0);
    for (int i=1; i<n; ++i)
      ki[i] = index((float)si.getValue(i),xs,ki[i-1]);
    return ki;
  }

  private float interpolate00(float x1, float x2, int[] ks) {
    updateIndices(x1,x2,ks);
    return interpolate00(x1,x2,ks[0],ks[1]);
  }
  private float interpolate10(float x1, float x2, int[] ks) {
    updateIndices(x1,x2,ks);
    return interpolate10(x1,x2,ks[0],ks[1]);
  }
  private float interpolate01(float x1, float x2, int[] ks) {
    updateIndices(x1,x2,ks);
    return interpolate01(x1,x2,ks[0],ks[1]);
  }

  private float interpolate00(float x1, float x2, int k1, int k2) {
    return eval00(_a[k2][k1],x1-_x1[k1],x2-_x2[k2]);
  }
  private float interpolate10(float x1, float x2, int k1, int k2) {
    return eval10(_a[k2][k1],x1-_x1[k1],x2-_x2[k2]);
  }
  private float interpolate01(float x1, float x2, int k1, int k2) {
    return eval01(_a[k2][k1],x1-_x1[k1],x2-_x2[k2]);
  }

  private static float eval00(float[][] a, float d1, float d2) {
    return a[0][0]+d1*(a[0][1]+d1*(a[0][2]+d1*a[0][3])) +
       d2*(a[1][0]+d1*(a[1][1]+d1*(a[1][2]+d1*a[1][3])) +
       d2*(a[2][0]+d1*(a[2][1]+d1*(a[2][2]+d1*a[2][3])) +
       d2*(a[3][0]+d1*(a[3][1]+d1*(a[3][2]+d1*a[3][3])))));
  }
  private static float eval10(float[][] a, float d1, float d2) {
    return      a[0][1]+d2*(a[1][1]+d2*(a[2][1]+d2*a[3][1])) +
      d1*(2.0f*(a[0][2]+d2*(a[1][2]+d2*(a[2][2]+d2*a[3][2]))) +
      d1*(3.0f*(a[0][3]+d2*(a[1][3]+d2*(a[2][3]+d2*a[3][3])))));
  }
  private static float eval01(float[][] a, float d1, float d2) {
    return      a[1][0]+d1*(a[1][1]+d1*(a[1][2]+d1*a[1][3])) +
      d2*(2.0f*(a[2][0]+d1*(a[2][1]+d1*(a[2][2]+d1*a[2][3]))) +
      d2*(3.0f*(a[3][0]+d1*(a[3][1]+d1*(a[3][2]+d1*a[3][3])))));
  }

  // See http://en.wikipedia.org/wiki/Bicubic_interpolation
  private static final float[][] AINV = {
    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
    {-3,3,0,0,-2,-1,0,0,0,0,0,0,0,0,0,0},
    {2,-2,0,0,1,1,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},
    {0,0,0,0,0,0,0,0,-3,3,0,0,-2,-1,0,0},
    {0,0,0,0,0,0,0,0,2,-2,0,0,1,1,0,0},
    {-3,0,3,0,0,0,0,0,-2,0,-1,0,0,0,0,0},
    {0,0,0,0,-3,0,3,0,0,0,0,0,-2,0,-1,0},
    {9,-9,-9,9,6,3,-6,-3,6,-6,3,-3,4,2,2,1},
    {-6,6,6,-6,-3,-3,3,3,-4,4,-2,2,-2,-2,-1,-1},
    {2,0,-2,0,0,0,0,0,1,0,1,0,0,0,0,0},
    {0,0,0,0,2,0,-2,0,0,0,0,0,1,0,1,0},
    {-6,6,6,-6,-4,-2,4,2,-3,3,-3,3,-2,-1,-2,-1},
    {4,-4,-4,4,2,2,-2,-2,2,-2,2,-2,1,1,1,1}
  };
  private static float[][] getA(float[] dxs, float[][] yds) {
    float d1 = dxs[1];
    float d2 = dxs[2];
    float[][] a = new float[4][4];
    for (int m2=0,i=0; m2<4; ++m2) { // for all powers of x2, ...
      for (int m1=0; m1<4; ++m1,++i) { // for all powers of x1, ...
        float am = 0.0f;
        for (int jd=0,j=0; jd<4; ++jd) { // for all four derivatives, ...
          for (int jp=0; jp<4; ++jp,++j) { // for all four points, ...
            if (AINV[i][j]!=0)
              am += AINV[i][j]*yds[jd][jp]*dxs[jd];
          }
        }
        am /= pow(d1,m1)*pow(d2,m2);
        a[m2][m1] = am;
      }
    }
    return a;
  }

  /**
   * Makes arrays {y00,y10,y10,y11} of derivatives.
   * The derivatives are y00 = y; y10 = dy/d1; y10 = dy/d2; y11 = ddy/d1d2.
   */
  private static float[][][] makeDerivatives(
    Method method1, Method method2,
    int n1, int n2, float[] x1, float[] x2, float[][] y)
  {
    CubicInterpolator.Method cim1 = CubicInterpolator.Method.MONOTONIC;
    CubicInterpolator.Method cim2 = CubicInterpolator.Method.MONOTONIC;
    if (method1==Method.SPLINE) cim1 = CubicInterpolator.Method.SPLINE;
    if (method2==Method.SPLINE) cim2 = CubicInterpolator.Method.SPLINE;
    float[][] y00 = y;

    // Partial derivatives dy/d1.
    float[][] y10 = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      float[] ys = new float[n1];
      for (int i1=0; i1<n1; ++i1)
        ys[i1] = y[i2][i1];
      CubicInterpolator ci = new CubicInterpolator(cim1,n1,x1,ys);
      for (int i1=0; i1<n1; ++i1)
        y10[i2][i1] = ci.interpolate1(x1[i1]);
    }

    // Partial derivatives dy/d2.
    float[][] y01 = new float[n2][n1];
    for (int i1=0; i1<n1; ++i1) {
      float[] ys = new float[n2];
      for (int i2=0; i2<n2; ++i2)
        ys[i2] = y[i2][i1];
      CubicInterpolator ci = new CubicInterpolator(cim2,n2,x2,ys);
      for (int i2=0; i2<n2; ++i2)
        y01[i2][i1] = ci.interpolate1(x2[i2]);
    }

    // Partial derivatives ddy/d1d2 (averages of 1st derivatives).
    float[][] y11 = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      float[] ys = new float[n1];
      for (int i1=0; i1<n1; ++i1)
        ys[i1] = y01[i2][i1]; // dy/d2
      CubicInterpolator ci = new CubicInterpolator(cim1,n1,x1,ys);
      for (int i1=0; i1<n1; ++i1)
        y11[i2][i1] += 0.5f*ci.interpolate1(x1[i1]);
    }
    for (int i1=0; i1<n1; ++i1) {
      float[] ys = new float[n2];
      for (int i2=0; i2<n2; ++i2)
        ys[i2] = y10[i2][i1]; // dy/d1
      CubicInterpolator ci = new CubicInterpolator(cim2,n2,x2,ys);
      for (int i2=0; i2<n2; ++i2)
        y11[i2][i1] += 0.5f*ci.interpolate1(x2[i2]);
    }

    return new float[][][]{y00,y10,y01,y11};
  }

  /**
   * Makes interpolation coefficients to match specified derivatives.
   */
  private static float[][][][] makeCoefficients(
    Method method1, Method method2,
    int n1, int n2, float[] x1, float[] x2, float[][] y)
  {
    float[][][] yd = makeDerivatives(method1,method2,n1,n2,x1,x2,y);
    float[][] y00 = yd[0];
    float[][] y10 = yd[1];
    float[][] y01 = yd[2];
    float[][] y11 = yd[3];
    float[][][][] a = new float[n2-1][n1-1][4][4];
    for (int i2=0,j2=1; i2<n2-1; ++i2,++j2) {
      float dx2 = x2[j2]-x2[i2];
      for (int i1=0,j1=1; i1<n1-1; ++i1,++j1) {
        float dx1 = x1[j1]-x1[i1];
        float[] dxs = {1.0f,dx1,dx2,dx1*dx2};
        float[][] yds = {
          {y00[i2][i1],y00[i2][j1],y00[j2][i1],y00[j2][j1]},
          {y10[i2][i1],y10[i2][j1],y10[j2][i1],y10[j2][j1]},
          {y01[i2][i1],y01[i2][j1],y01[j2][i1],y01[j2][j1]},
          {y11[i2][i1],y11[i2][j1],y11[j2][i1],y11[j2][j1]}
        };
        a[i2][i1] = getA(dxs,yds);
      }
    }
    return a;
  }
}
