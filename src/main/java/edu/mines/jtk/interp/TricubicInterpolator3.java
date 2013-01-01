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
 * Piecewise tricubic polynomial interpolation of a function y(x1,x2,x3).
 * The interpolated function has continuous first derivatives dy/d1, dy/d2,
 * dy/d3 and cross-derivatives ddy/d1d2, ddy/d1d3, ddy/d2d3 and dddy/d1d2d3.
 * First derivatives are computed using methods that may be specified for each
 * dimension. Cross-derivatives are computed by averaging derivatives of first
 * derivatives.
 * <p>
 * The function y(x1,x2,x3) is specified by samples on a regular grid, which
 * need not be uniform. The regular grid is specified by one-dimensional
 * arrays of monotonically increasing coordinates x1, x2 and x3, such that
 * gridded x1 are identical for all gridded x2 and x3, gridded x2 are
 * identical for all gridded x1 and x2, and gridded x3 are identical for all
 * gridded x1 and x2.
 * <p>
 * Extrapolation (that is, interpolation outside the specified grid of
 * (x1,x2,x3) coordinates), is performed using cubic polynomials for the
 * nearest grid samples. Extrapolated values can be well outside the [min,max]
 * range of interpolated values, and should typically be avoided.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.12.27
 */
public class TricubicInterpolator3 {

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
   * @param x3 array[n3] of x3 coordinates; monotonically increasing.
   * @param y array[n3][n2][n1] of sampled values y(x1,x2,x3).
   */
  public TricubicInterpolator3(
    float[] x1, float[] x2, float[] x3, float[][][] y) 
  {
    this(Method.MONOTONIC,Method.MONOTONIC,Method.MONOTONIC,x1,x2,x3,y);
  }

  /**
   * Constructs an interpolator for specified methods and values.
   * @param method1 method used to compute dy/d1.
   * @param method2 method used to compute dy/d2.
   * @param method3 method used to compute dy/d3.
   * @param x1 array[n1] of x1 coordinates; monotonically increasing.
   * @param x2 array[n2] of x2 coordinates; monotonically increasing.
   * @param x3 array[n3] of x3 coordinates; monotonically increasing.
   * @param y array[n3][n2][n1] of sampled values y(x1,x2,x3).
   */
  public TricubicInterpolator3(
    Method method1, Method method2, Method method3,
    float[] x1, float[] x2, float[] x3, float[][][] y) 
  {
    this(method1,method2,method3,x1.length,x2.length,x3.length,x1,x2,x3,y);
  }

  /**
   * Constructs an interpolator for specified methods and values.
   * @param method1 method used to compute dy/d1.
   * @param method2 method used to compute dy/d2.
   * @param method3 method used to compute dy/d3.
   * @param n1 number of x1 coordinates.
   * @param n2 number of x2 coordinates.
   * @param n3 number of x3 coordinates.
   * @param x1 array[n1] of x1 coordinates; monotonically increasing.
   * @param x2 array[n2] of x2 coordinates; monotonically increasing.
   * @param x3 array[n3] of x3 coordinates; monotonically increasing.
   * @param y array[n3][n2][n1] of sampled values y(x1,x2,x3).
   */
  public TricubicInterpolator3(
    Method method1, Method method2, Method method3,
    int n1, int n2, int n3, 
    float[] x1, float[] x2, float[] x3, float[][][] y) 
  {
    Check.argument(isMonotonic(x1), "array x1 is monotonic");
    Check.argument(isMonotonic(x2), "array x2 is monotonic");
    Check.argument(isMonotonic(x3), "array x3 is monotonic");
    _n1 = n1;
    _n2 = n2;
    _n3 = n3;
    _x1 = copy(n1,x1);
    _x2 = copy(n2,x2);
    _x3 = copy(n3,x3);
    _a = makeCoefficients(method1,method2,method3,n1,n2,n3,x1,x2,x3,y);
  }

  /**
   * Returns interpolated value y.
   * Same as {@link #interpolate000(float,float,float)}.
   * @param x1 coordinate x1.
   * @param x2 coordinate x2.
   * @param x3 coordinate x3.
   * @return interpolated y(x1,x2,x3).
   */
  public float interpolate(float x1, float x2, float x3) {
    return interpolate000(x1,x2,x3);
  }

  /**
   * Returns interpolated value y.
   * @param x1 coordinate x1.
   * @param x2 coordinate x2.
   * @param x3 coordinate x3.
   * @return interpolated y(x1,x2,x3).
   */
  public float interpolate000(float x1, float x2, float x3) {
    return interpolate000(x1,x2,x3,_ks);
  }

  /**
   * Returns interpolated partial derivative dy/d1.
   * @param x1 coordinate x1.
   * @param x2 coordinate x2.
   * @param x3 coordinate x3.
   * @return interpolated dy/d1.
   */
  public float interpolate100(float x1, float x2, float x3) {
    return interpolate100(x1,x2,x3,_ks);
  }

  /**
   * Returns interpolated partial derivative dy/d2.
   * @param x1 coordinate x1.
   * @param x2 coordinate x2.
   * @param x3 coordinate x3.
   * @return interpolated dy/d2.
   */
  public float interpolate010(float x1, float x2, float x3) {
    return interpolate010(x1,x2,x3,_ks);
  }

  /**
   * Returns interpolated partial derivative dy/d3.
   * @param x1 coordinate x1.
   * @param x2 coordinate x2.
   * @param x3 coordinate x3.
   * @return interpolated dy/d3.
   */
  public float interpolate001(float x1, float x2, float x3) {
    return interpolate001(x1,x2,x3,_ks);
  }

  /**
   * Returns an array of interpolated values y.
   * Same as {@link #interpolate000(Sampling,Sampling,Sampling)}.
   * @param s1 sampling of coordinate x1.
   * @param s2 sampling of coordinate x2.
   * @param s3 sampling of coordinate x3.
   * @return interpolated y(x1,x2,x3).
   */
  public float[][][] interpolate(Sampling s1, Sampling s2, Sampling s3) {
    return interpolate000(s1,s2,s3);
  }

  /**
   * Returns an array of interpolated values y.
   * @param s1 sampling of coordinate x1.
   * @param s2 sampling of coordinate x2.
   * @param s3 sampling of coordinate x3.
   * @return interpolated y(x1,x2,x3).
   */
  public float[][][] interpolate000(Sampling s1, Sampling s2, Sampling s3) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s3.getCount();
    float[][][] y = new float[n3][n2][n1];
    interpolate000(s1,s2,s3,y);
    return y;
  }

  /**
   * Computes an array of interpolated values y.
   * Same as {@link #interpolate000(Sampling,Sampling,Sampling,float[][][])}.
   * @param s1 sampling of coordinate x1.
   * @param s2 sampling of coordinate x2.
   * @param s3 sampling of coordinate x3.
   * @param y output array of interpolated y(x1,x2,x3).
   */
  public void interpolate(
    Sampling s1, Sampling s2, Sampling s3, float[][][] y) 
  {
    interpolate000(s1,s2,s3,y);
  }

  /**
   * Computes an array of interpolated values y.
   * @param s1 sampling of coordinate x1.
   * @param s2 sampling of coordinate x2.
   * @param s3 sampling of coordinate x3.
   * @param y output array of interpolated y(x1,x2,x3).
   */
  public void interpolate000(
    Sampling s1, Sampling s2, Sampling s3, float[][][] y) 
  {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s3.getCount();
    int[] k1 = makeIndices(s1,_x1);
    int[] k2 = makeIndices(s2,_x2);
    int[] k3 = makeIndices(s3,_x3);
    for (int i3=0; i3<n3; ++i3) {
      float x3 = (float)s3.getValue(i3);
      for (int i2=0; i2<n2; ++i2) {
        float x2 = (float)s2.getValue(i2);
        for (int i1=0; i1<n1; ++i1) {
          float x1 = (float)s1.getValue(i1);
          y[i3][i2][i1] = interpolate000(x1,x2,x3,k1[i1],k2[i2],k3[i3]);
        }
      }
    }
  }

  /**
   * Returns an array of interpolated values y.
   * Same as {@link #interpolate000(float[],float[],float[])}.
   * @param x1 array[n1] of coordinates x1.
   * @param x2 array[n2] of coordinates x2.
   * @param x3 array[n3] of coordinates x3.
   * @return array[n3][n2][n1] of interpolated y(x1,x2,x3).
   */
  public float[][][] interpolate(float[] x1, float[] x2, float[] x3) {
    return interpolate000(x1,x2,x3);
  }

  /**
   * Returns an array of interpolated values y.
   * @param x1 array[n1] of coordinates x1.
   * @param x2 array[n2] of coordinates x2.
   * @param x3 array[n3] of coordinates x3.
   * @return array[n3][n2][n1] of interpolated y(x1,x2,x3).
   */
  public float[][][] interpolate000(float[] x1, float[] x2, float[] x3) {
    int n1 = x1.length;
    int n2 = x2.length;
    int n3 = x3.length;
    float[][][] y = new float[n3][n2][n1];
    interpolate000(x1,x2,x3,y);
    return y;
  }

  /**
   * Computes an array of interpolated values y.
   * Same as {@link #interpolate000(float[],float[],float[],float[][][])}.
   * @param x1 array[n1] of coordinates x1.
   * @param x2 array[n2] of coordinates x2.
   * @param x3 array[n3] of coordinates x3.
   * @param y output array[n3][n2][n1] of interpolated y(x1,x2,x3).
   */
  public void interpolate(
    float[] x1, float[] x2, float[] x3, float[][][] y) 
  {
    interpolate000(x1,x2,x3,y);
  }

  /**
   * Computes an array of interpolated values y.
   * @param x1 array[n1] of coordinates x1.
   * @param x2 array[n2] of coordinates x2.
   * @param x3 array[n3] of coordinates x3.
   * @param y output array[n3][n2][n1] of interpolated y(x1,x2,x3).
   */
  public void interpolate000(
    float[] x1, float[] x2, float[] x3, float[][][] y) 
  {
    int n1 = x1.length;
    int n2 = x2.length;
    int n3 = x3.length;
    int[] k1 = makeIndices(x1,_x1);
    int[] k2 = makeIndices(x2,_x2);
    int[] k3 = makeIndices(x3,_x3);
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          y[i3][i2][i1] = interpolate000(
            x1[i1],x2[i2],x3[i3],
            k1[i1],k2[i2],k3[i3]);
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void trace(String s) {
    System.out.println(s);
  }

  private int _n1,_n2,_n3; // number of samples
  private float[] _x1; // array of x1 coordinates in regular grid
  private float[] _x2; // array of x2 coordinates in regular grid
  private float[] _x3; // array of x3 coordinates in regular grid
  private float[][][][][][] _a; // array[n3-1][n2-1][n1-1][4][4][4] of coeff
  private int[] _ks = {0,0,0}; // coefficients used in previous interpolation

  private static int index(float x, float[] xs, int i) {
    i = binarySearch(xs,x,i);
    if (i<0) 
      i = (i<-1)?-2-i:0;
    if (i>=xs.length-1)
      i = xs.length-2;
    return i;
  }

  private void updateIndices(float x1, float x2, float x3, int[] ks) {
    ks[0] = index(x1,_x1,ks[0]); 
    ks[1] = index(x2,_x2,ks[1]); 
    ks[2] = index(x3,_x3,ks[2]); 
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

  private float interpolate000(float x1, float x2, float x3, int[] ks) {
    updateIndices(x1,x2,x3,ks);
    return interpolate000(x1,x2,x3,ks[0],ks[1],ks[2]);
  }
  private float interpolate100(float x1, float x2, float x3, int[] ks) {
    updateIndices(x1,x2,x3,ks);
    return interpolate100(x1,x2,x3,ks[0],ks[1],ks[2]);
  }
  private float interpolate010(float x1, float x2, float x3, int[] ks) {
    updateIndices(x1,x2,x3,ks);
    return interpolate010(x1,x2,x3,ks[0],ks[1],ks[2]);
  }
  private float interpolate001(float x1, float x2, float x3, int[] ks) {
    updateIndices(x1,x2,x3,ks);
    return interpolate001(x1,x2,x3,ks[0],ks[1],ks[2]);
  }

  private float interpolate000(
    float x1, float x2, float x3, int k1, int k2, int k3) 
  {
    return eval000(_a[k3][k2][k1],x1-_x1[k1],x2-_x2[k2],x3-_x3[k3]);
  }
  private float interpolate100(
    float x1, float x2, float x3, int k1, int k2, int k3) 
  {
    return eval100(_a[k3][k2][k1],x1-_x1[k1],x2-_x2[k2],x3-_x3[k3]);
  }
  private float interpolate010(
    float x1, float x2, float x3, int k1, int k2, int k3) 
  {
    return eval010(_a[k3][k2][k1],x1-_x1[k1],x2-_x2[k2],x3-_x3[k3]);
  }
  private float interpolate001(
    float x1, float x2, float x3, int k1, int k2, int k3) 
  {
    return eval001(_a[k3][k2][k1],x1-_x1[k1],x2-_x2[k2],x3-_x3[k3]);
  }

  private static float eval000(float[][][] a, float d1, float d2, float d3) {
    float sum3 = 0.0f;
    for (int m3=3; m3>=0; --m3) {
      float sum2 = 0.0f;
      for (int m2=3; m2>=0; --m2) {
        float sum1 = 0.0f;
        for (int m1=3; m1>=0; --m1) {
          sum1 = a[m3][m2][m1]+d1*sum1;
        }
        sum2 = sum1+d2*sum2;
      }
      sum3 = sum2+d3*sum3;
    }
    return sum3;
  }
  private static float eval100(float[][][] a, float d1, float d2, float d3) {
    float sum3 = 0.0f;
    for (int m3=3; m3>=0; --m3) {
      float sum2 = 0.0f;
      for (int m2=3; m2>=0; --m2) {
        float sum1 = 0.0f;
        for (int m1=3; m1>=1; --m1) {
          sum1 = m1*a[m3][m2][m1]+d1*sum1;
        }
        sum2 = sum1+d2*sum2;
      }
      sum3 = sum2+d3*sum3;
    }
    return sum3;
  }
  private static float eval010(float[][][] a, float d1, float d2, float d3) {
    float sum3 = 0.0f;
    for (int m3=3; m3>=0; --m3) {
      float sum2 = 0.0f;
      for (int m2=3; m2>=1; --m2) {
        float sum1 = 0.0f;
        for (int m1=3; m1>=0; --m1) {
          sum1 = m2*a[m3][m2][m1]+d1*sum1;
        }
        sum2 = sum1+d2*sum2;
      }
      sum3 = sum2+d3*sum3;
    }
    return sum3;
  }
  private static float eval001(float[][][] a, float d1, float d2, float d3) {
    float sum3 = 0.0f;
    for (int m3=3; m3>=1; --m3) {
      float sum2 = 0.0f;
      for (int m2=3; m2>=0; --m2) {
        float sum1 = 0.0f;
        for (int m1=3; m1>=0; --m1) {
          sum1 = m3*a[m3][m2][m1]+d1*sum1;
        }
        sum2 = sum1+d2*sum2;
      }
      sum3 = sum2+d3*sum3;
    }
    return sum3;
  }

  // From org.apache.commons.math3.analysis.interpolation.
  // TricubicSplineInterpolatingFunction.java
  private static final float[][] AINV = {
    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {-3,3,0,0,0,0,0,0,-2,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {2,-2,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,-1,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {-3,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,-3,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,
     -1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {9,-9,-9,9,0,0,0,0,6,3,-6,-3,0,0,0,0,6,-6,3,-3,0,0,0,0,0,0,0,0,0,0,0,0,
     4,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {-6,6,6,-6,0,0,0,0,-3,-3,3,3,0,0,0,0,-4,4,-2,2,0,0,0,0,0,0,0,0,0,0,0,0,
     -2,-2,-1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {2,0,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,2,0,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {-6,6,6,-6,0,0,0,0,-4,-2,4,2,0,0,0,0,-3,3,-3,3,0,0,0,0,0,0,0,0,0,0,0,0,
     -2,-1,-2,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {4,-4,-4,4,0,0,0,0,2,2,-2,-2,0,0,0,0,2,-2,2,-2,0,0,0,0,0,0,0,0,0,0,0,0,
     1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-3,3,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,-2,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,-2,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,-3,3,0,0,0,0,0,0,-2,-1,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,2,-2,0,0,0,0,0,0,1,1,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-3,0,3,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,-1,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,-3,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,-1,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,-9,-9,9,0,0,0,0,0,0,
     0,0,0,0,0,0,6,3,-6,-3,0,0,0,0,6,-6,3,-3,0,0,0,0,4,2,2,1,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-6,6,6,-6,0,0,0,0,0,0,
     0,0,0,0,0,0,-3,-3,3,3,0,0,0,0,-4,4,-2,2,0,0,0,0,-2,-2,-1,-1,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,-2,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,2,0,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-6,6,6,-6,0,0,0,0,0,0,
     0,0,0,0,0,0,-4,-2,4,2,0,0,0,0,-3,3,-3,3,0,0,0,0,-2,-1,-2,-1,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,-4,-4,4,0,0,0,0,0,0,
     0,0,0,0,0,0,2,2,-2,-2,0,0,0,0,2,-2,2,-2,0,0,0,0,1,1,1,1,0,0,0,0},
    {-3,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,0,0,-1,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,-3,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,-2,0,0,0,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {9,-9,0,0,-9,9,0,0,6,3,0,0,-6,-3,0,0,0,0,0,0,0,0,0,0,6,-6,0,0,3,-3,0,0,
     0,0,0,0,0,0,0,0,4,2,0,0,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {-6,6,0,0,6,-6,0,0,-3,-3,0,0,3,3,0,0,0,0,0,0,0,0,0,0,-4,4,0,0,-2,2,0,0,
     0,0,0,0,0,0,0,0,-2,-2,0,0,-1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-3,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,0,0,-1,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-3,0,0,
     0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,0,0,-1,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,-9,0,0,-9,9,0,0,0,0,0,0,0,0,0,0,6,3,
     0,0,-6,-3,0,0,0,0,0,0,0,0,0,0,6,-6,0,0,3,-3,0,0,4,2,0,0,2,1,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-6,6,0,0,6,-6,0,0,0,0,0,0,0,0,0,0,-3,-3,
     0,0,3,3,0,0,0,0,0,0,0,0,0,0,-4,4,0,0,-2,2,0,0,-2,-2,0,0,-1,-1,0,0},
    {9,0,-9,0,-9,0,9,0,0,0,0,0,0,0,0,0,6,0,3,0,-6,0,-3,0,6,0,-6,0,3,0,-3,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,2,0,2,0,1,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,9,0,-9,0,-9,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,3,
     0,-6,0,-3,0,6,0,-6,0,3,0,-3,0,0,0,0,0,0,0,0,0,4,0,2,0,2,0,1,0},
    {-27,27,27,-27,27,-27,-27,27,-18,-9,18,9,18,9,-18,-9,-18,18,-9,9,18,-18,
     9,-9,-18,18,18,-18,-9,9,9,-9,-12,-6,-6,-3,12,6,6,3,-12,-6,12,6,-6,-3,6,
     3,-12,12,-6,6,-6,6,-3,3,-8,-4,-4,-2,-4,-2,-2,-1},
    {18,-18,-18,18,-18,18,18,-18,9,9,-9,-9,-9,-9,9,9,12,-12,6,-6,-12,12,-6,
     6,12,-12,-12,12,6,-6,-6,6,6,6,3,3,-6,-6,-3,-3,6,6,-6,-6,3,3,-3,-3,8,-8,
     4,-4,4,-4,2,-2,4,4,2,2,2,2,1,1},
    {-6,0,6,0,6,0,-6,0,0,0,0,0,0,0,0,0,-3,0,-3,0,3,0,3,0,-4,0,4,0,-2,0,2,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,-2,0,-1,0,-1,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,-6,0,6,0,6,0,-6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-3,0,
     -3,0,3,0,3,0,-4,0,4,0,-2,0,2,0,0,0,0,0,0,0,0,0,-2,0,-2,0,-1,0,-1,0},
    {18,-18,-18,18,-18,18,18,-18,12,6,-12,-6,-12,-6,12,6,9,-9,9,-9,-9,9,-9,
     9,12,-12,-12,12,6,-6,-6,6,6,3,6,3,-6,-3,-6,-3,8,4,-8,-4,4,2,-4,-2,6,-6,
     6,-6,3,-3,3,-3,4,2,4,2,2,1,2,1},
    {-12,12,12,-12,12,-12,-12,12,-6,-6,6,6,6,6,-6,-6,-6,6,-6,6,6,-6,6,-6,-8,
     8,8,-8,-4,4,4,-4,-3,-3,-3,-3,3,3,3,3,-4,-4,4,4,-2,-2,2,2,-4,4,-4,4,-2,
     2,-2,2,-2,-2,-2,-2,-1,-1,-1,-1},
    {2,0,0,0,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,2,0,0,0,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {-6,6,0,0,6,-6,0,0,-4,-2,0,0,4,2,0,0,0,0,0,0,0,0,0,0,-3,3,0,0,-3,3,0,0,
     0,0,0,0,0,0,0,0,-2,-1,0,0,-2,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {4,-4,0,0,-4,4,0,0,2,2,0,0,-2,-2,0,0,0,0,0,0,0,0,0,0,2,-2,0,0,2,-2,0,0,
     0,0,0,0,0,0,0,0,1,1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,
     0,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-6,6,0,0,6,-6,0,0,0,0,0,0,0,0,0,0,-4,-2,
     0,0,4,2,0,0,0,0,0,0,0,0,0,0,-3,3,0,0,-3,3,0,0,-2,-1,0,0,-2,-1,0,0},
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,-4,0,0,-4,4,0,0,0,0,0,0,0,0,0,0,2,2,
     0,0,-2,-2,0,0,0,0,0,0,0,0,0,0,2,-2,0,0,2,-2,0,0,1,1,0,0,1,1,0,0},
    {-6,0,6,0,6,0,-6,0,0,0,0,0,0,0,0,0,-4,0,-2,0,4,0,2,0,-3,0,3,0,-3,0,3,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,-1,0,-2,0,-1,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,-6,0,6,0,6,0,-6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-4,0,
     -2,0,4,0,2,0,-3,0,3,0,-3,0,3,0,0,0,0,0,0,0,0,0,-2,0,-1,0,-2,0,-1,0},
    {18,-18,-18,18,-18,18,18,-18,12,6,-12,-6,-12,-6,12,6,12,-12,6,-6,-12,12,
     -6,6,9,-9,-9,9,9,-9,-9,9,8,4,4,2,-8,-4,-4,-2,6,3,-6,-3,6,3,-6,-3,6,-6,3,
     -3,6,-6,3,-3,4,2,2,1,4,2,2,1},
    {-12,12,12,-12,12,-12,-12,12,-6,-6,6,6,6,6,-6,-6,-8,8,-4,4,8,-8,4,-4,-6,
     6,6,-6,-6,6,6,-6,-4,-4,-2,-2,4,4,2,2,-3,-3,3,3,-3,-3,3,3,-4,4,-2,2,-4,4,
     -2,2,-2,-2,-1,-1,-2,-2,-1,-1},
    {4,0,-4,0,-4,0,4,0,0,0,0,0,0,0,0,0,2,0,2,0,-2,0,-2,0,2,0,-2,0,2,0,-2,0,0,
     0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,4,0,-4,0,-4,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,
     2,0,-2,0,-2,0,2,0,-2,0,2,0,-2,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0},
    {-12,12,12,-12,12,-12,-12,12,-8,-4,8,4,8,4,-8,-4,-6,6,-6,6,6,-6,6,-6,-6,
     6,6,-6,-6,6,6,-6,-4,-2,-4,-2,4,2,4,2,-4,-2,4,2,-4,-2,4,2,-3,3,-3,3,-3,3,
     -3,3,-2,-1,-2,-1,-2,-1,-2,-1},
    {8,-8,-8,8,-8,8,8,-8,4,4,-4,-4,-4,-4,4,4,4,-4,4,-4,-4,4,-4,4,4,-4,-4,4,4,
     -4,-4,4,2,2,2,2,-2,-2,-2,-2,2,2,-2,-2,2,2,-2,-2,2,-2,2,-2,2,-2,2,-2,1,1,
     1,1,1,1,1,1}
  };
  private static float[][][] getA(float[] dxs, float[][] yds) {
    float d1 = dxs[1];
    float d2 = dxs[2];
    float d3 = dxs[3];
    float[][][] a = new float[4][4][4];
    for (int m3=0,i=0; m3<4; ++m3) { // for all powers of x3, ...
      for (int m2=0; m2<4; ++m2) { // for all powers of x2, ...
        for (int m1=0; m1<4; ++m1,++i) { // for all powers of x1, ...
          float am = 0.0f;
          for (int jd=0,j=0; jd<8; ++jd) { // for all eight derivatives, ...
            for (int jp=0; jp<8; ++jp,++j) { // for all eight points, ...
              if (AINV[i][j]!=0)
                am += AINV[i][j]*yds[jd][jp]*dxs[jd];
            }
          }
          am /= pow(d1,m1)*pow(d2,m2)*pow(d3,m3);
          a[m3][m2][m1] = am;
        }
      }
    }
    return a;
  }

  /**
   * Makes arrays {y000,y100,y010,y100,y110,y101,y011,y111} of derivatives.
   */
  private static float[][][][] makeDerivatives(
    Method method1, Method method2, Method method3,
    int n1, int n2, int n3, float[] x1, float[] x2, float[] x3, float[][][] y)
  {
    CubicInterpolator.Method cim1 = CubicInterpolator.Method.MONOTONIC;
    CubicInterpolator.Method cim2 = CubicInterpolator.Method.MONOTONIC;
    CubicInterpolator.Method cim3 = CubicInterpolator.Method.MONOTONIC;
    if (method1==Method.SPLINE) cim1 = CubicInterpolator.Method.SPLINE;
    if (method2==Method.SPLINE) cim2 = CubicInterpolator.Method.SPLINE;
    if (method3==Method.SPLINE) cim3 = CubicInterpolator.Method.SPLINE;
    float[][][] y000 = y;

    // Partial derivatives dy/d1.
    float[][][] y100 = new float[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        float[] ys = new float[n1];
        for (int i1=0; i1<n1; ++i1)
          ys[i1] = y[i3][i2][i1];
        CubicInterpolator ci = new CubicInterpolator(cim1,n1,x1,ys);
        for (int i1=0; i1<n1; ++i1)
          y100[i3][i2][i1] = ci.interpolate1(x1[i1]);
      }
    }

    // Partial derivatives dy/d2.
    float[][][] y010 = new float[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      for (int i1=0; i1<n1; ++i1) {
        float[] ys = new float[n2];
        for (int i2=0; i2<n2; ++i2)
          ys[i2] = y[i3][i2][i1];
        CubicInterpolator ci = new CubicInterpolator(cim2,n2,x2,ys);
        for (int i2=0; i2<n2; ++i2)
          y010[i3][i2][i1] = ci.interpolate1(x2[i2]);
      }
    }

    // Partial derivatives dy/d3.
    float[][][] y001 = new float[n3][n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        float[] ys = new float[n3];
        for (int i3=0; i3<n3; ++i3)
          ys[i3] = y[i3][i2][i1];
        CubicInterpolator ci = new CubicInterpolator(cim3,n3,x3,ys);
        for (int i3=0; i3<n3; ++i3)
          y001[i3][i2][i1] = ci.interpolate1(x3[i3]);
      }
    }

    // Partial derivatives ddy/d1d2.
    float[][][] y110 = new float[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        float[] ys = new float[n1];
        for (int i1=0; i1<n1; ++i1)
          ys[i1] = y010[i3][i2][i1]; // dy/d2
        CubicInterpolator ci = new CubicInterpolator(cim1,n1,x1,ys);
        for (int i1=0; i1<n1; ++i1)
          y110[i3][i2][i1] += 0.5f*ci.interpolate1(x1[i1]);
      }
      for (int i1=0; i1<n1; ++i1) {
        float[] ys = new float[n2];
        for (int i2=0; i2<n2; ++i2)
          ys[i2] = y100[i3][i2][i1]; // dy/d1
        CubicInterpolator ci = new CubicInterpolator(cim2,n2,x2,ys);
        for (int i2=0; i2<n2; ++i2)
          y110[i3][i2][i1] += 0.5f*ci.interpolate1(x2[i2]);
      }
    }

    // Partial derivatives ddy/d1d3.
    float[][][] y101 = new float[n3][n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i3=0; i3<n3; ++i3) {
        float[] ys = new float[n1];
        for (int i1=0; i1<n1; ++i1)
          ys[i1] = y001[i3][i2][i1]; // dy/d3
        CubicInterpolator ci = new CubicInterpolator(cim1,n1,x1,ys);
        for (int i1=0; i1<n1; ++i1)
          y101[i3][i2][i1] += 0.5f*ci.interpolate1(x1[i1]);
      }
      for (int i1=0; i1<n1; ++i1) {
        float[] ys = new float[n3];
        for (int i3=0; i3<n3; ++i3)
          ys[i3] = y100[i3][i2][i1]; // dy/d1
        CubicInterpolator ci = new CubicInterpolator(cim3,n3,x3,ys);
        for (int i3=0; i3<n3; ++i3)
          y101[i3][i2][i1] += 0.5f*ci.interpolate1(x3[i3]);
      }
    }

    // Partial derivatives ddy/d2d3.
    float[][][] y011 = new float[n3][n2][n1];
    for (int i1=0; i1<n1; ++i1) {
      for (int i3=0; i3<n3; ++i3) {
        float[] ys = new float[n2];
        for (int i2=0; i2<n2; ++i2)
          ys[i2] = y001[i3][i2][i1]; // dy/d3
        CubicInterpolator ci = new CubicInterpolator(cim2,n2,x2,ys);
        for (int i2=0; i2<n2; ++i2)
          y011[i3][i2][i1] += 0.5f*ci.interpolate1(x2[i2]);
      }
      for (int i2=0; i2<n2; ++i2) {
        float[] ys = new float[n3];
        for (int i3=0; i3<n3; ++i3)
          ys[i3] = y010[i3][i2][i1]; // dy/d2
        CubicInterpolator ci = new CubicInterpolator(cim3,n3,x3,ys);
        for (int i3=0; i3<n3; ++i3)
          y011[i3][i2][i1] += 0.5f*ci.interpolate1(x3[i3]);
      }
    }

    // Partial derivatives dddy/d1d2d3.
    float[][][] y111 = new float[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        float[] ys = new float[n1];
        for (int i1=0; i1<n1; ++i1)
          ys[i1] = y011[i3][i2][i1]; // ddy/d2d3
        CubicInterpolator ci = new CubicInterpolator(cim1,n1,x1,ys);
        for (int i1=0; i1<n1; ++i1)
          y111[i3][i2][i1] += 0.3333333f*ci.interpolate1(x1[i1]);
      }
    }
    for (int i3=0; i3<n3; ++i3) {
      for (int i1=0; i1<n1; ++i1) {
        float[] ys = new float[n2];
        for (int i2=0; i2<n2; ++i2)
          ys[i2] = y101[i3][i2][i1]; // ddy/d1d3
        CubicInterpolator ci = new CubicInterpolator(cim2,n2,x2,ys);
        for (int i2=0; i2<n2; ++i2)
          y111[i3][i2][i1] += 0.3333333f*ci.interpolate1(x2[i2]);
      }
    }
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        float[] ys = new float[n3];
        for (int i3=0; i3<n3; ++i3)
          ys[i3] = y110[i3][i2][i1]; // ddy/d1d2
        CubicInterpolator ci = new CubicInterpolator(cim3,n3,x3,ys);
        for (int i3=0; i3<n3; ++i3)
          y111[i3][i2][i1] += 0.3333333f*ci.interpolate1(x3[i3]);
      }
    }

    return new float[][][][]{y000,y100,y010,y001,y110,y101,y011,y111};
  }

  /**
   * Makes interpolation coefficients to match specified derivatives.
   */
  private static float[][][][][][] makeCoefficients(
    Method method1, Method method2, Method method3,
    int n1, int n2, int n3, float[] x1, float[] x2, float[] x3, float[][][] y)
  {
    float[][][][] yd = makeDerivatives(
      method1,method2,method3,n1,n2,n3,x1,x2,x3,y);
    float[][][] y000 = yd[0];
    float[][][] y100 = yd[1];
    float[][][] y010 = yd[2];
    float[][][] y001 = yd[3];
    float[][][] y110 = yd[4];
    float[][][] y101 = yd[5];
    float[][][] y011 = yd[6];
    float[][][] y111 = yd[7];
    float[][][][][][] a = new float[n3-1][n2-1][n1-1][4][4][4];
    for (int i3=0,j3=1; i3<n3-1; ++i3,++j3) {
      float dx3 = x3[j3]-x3[i3];
      for (int i2=0,j2=1; i2<n2-1; ++i2,++j2) {
        float dx2 = x2[j2]-x2[i2];
        for (int i1=0,j1=1; i1<n1-1; ++i1,++j1) {
          float dx1 = x1[j1]-x1[i1];
          float[] dxs = {1.0f,dx1,dx2,dx3,dx1*dx2,dx1*dx3,dx2*dx3,dx1*dx2*dx3};
          float[][] yds = {
            {y000[i3][i2][i1],y000[i3][i2][j1],
             y000[i3][j2][i1],y000[i3][j2][j1],
             y000[j3][i2][i1],y000[j3][i2][j1],
             y000[j3][j2][i1],y000[j3][j2][j1]},
            {y100[i3][i2][i1],y100[i3][i2][j1],
             y100[i3][j2][i1],y100[i3][j2][j1],
             y100[j3][i2][i1],y100[j3][i2][j1],
             y100[j3][j2][i1],y100[j3][j2][j1]},
            {y010[i3][i2][i1],y010[i3][i2][j1],
             y010[i3][j2][i1],y010[i3][j2][j1],
             y010[j3][i2][i1],y010[j3][i2][j1],
             y010[j3][j2][i1],y010[j3][j2][j1]},
            {y001[i3][i2][i1],y001[i3][i2][j1],
             y001[i3][j2][i1],y001[i3][j2][j1],
             y001[j3][i2][i1],y001[j3][i2][j1],
             y001[j3][j2][i1],y001[j3][j2][j1]},
            {y110[i3][i2][i1],y110[i3][i2][j1],
             y110[i3][j2][i1],y110[i3][j2][j1],
             y110[j3][i2][i1],y110[j3][i2][j1],
             y110[j3][j2][i1],y110[j3][j2][j1]},
            {y101[i3][i2][i1],y101[i3][i2][j1],
             y101[i3][j2][i1],y101[i3][j2][j1],
             y101[j3][i2][i1],y101[j3][i2][j1],
             y101[j3][j2][i1],y101[j3][j2][j1]},
            {y011[i3][i2][i1],y011[i3][i2][j1],
             y011[i3][j2][i1],y011[i3][j2][j1],
             y011[j3][i2][i1],y011[j3][i2][j1],
             y011[j3][j2][i1],y011[j3][j2][j1]},
            {y111[i3][i2][i1],y111[i3][i2][j1],
             y111[i3][j2][i1],y111[i3][j2][j1],
             y111[j3][i2][i1],y111[j3][i2][j1],
             y111[j3][j2][i1],y111[j3][j2][j1]},
          };
          a[i3][i2][i1] = getA(dxs,yds);
        }
      }
    }
    return a;
  }
}
