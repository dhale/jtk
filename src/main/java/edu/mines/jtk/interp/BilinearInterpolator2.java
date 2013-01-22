/****************************************************************************
Copyright (c) 2013, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Piecewise bilinear polynomial interpolation of a function y(x1,x2).
 * <p>
 * The function y(x1,x2) is specified by samples on a regular grid, which need
 * not be uniform. The regular grid is specified by one-dimensional arrays of
 * monotonically increasing coordinates x1 and x2, such that gridded x1 are
 * identical for all gridded x2, and gridded x2 are identical for all gridded
 * x1.
 * <p>
 * Extrapolation (that is, interpolation outside the specified grid of (x1,x2)
 * coordinates), is performed using linear polynomials for the nearest grid
 * samples. Extrapolated values can be well outside the [min,max] range of
 * interpolated values, and should typically be avoided.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2013.01.22
 */
public class BilinearInterpolator2 {

  /**
   * Constructs an interpolator for specified values.
   * @param x1 array[n1] of x1 coordinates; monotonically increasing.
   * @param x2 array[n2] of x2 coordinates; monotonically increasing.
   * @param y array[n2][n1] of sampled values y(x1,x2).
   */
  public BilinearInterpolator2(float[] x1, float[] x2, float[][] y) {
    this(x1.length,x2.length,x1,x2,y);
  }

  /**
   * Constructs an interpolator for specified values.
   * @param n1 number of x1 coordinates.
   * @param n2 number of x2 coordinates.
   * @param x1 array[n1] of x1 coordinates; monotonically increasing.
   * @param x2 array[n2] of x2 coordinates; monotonically increasing.
   * @param y array[n2][n1] of sampled values y(x1,x2).
   */
  public BilinearInterpolator2(
    int n1, int n2, float[] x1, float[] x2, float[][] y) 
  {
    Check.argument(isMonotonic(x1), "array x1 is monotonic");
    Check.argument(isMonotonic(x2), "array x2 is monotonic");
    _x1 = copy(n1,x1);
    _x2 = copy(n2,x2);
    makeCoefficients(_x1,_x2,y);
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

  private float[] _x1; // array of x1 coordinates in regular grid
  private float[] _x2; // array of x2 coordinates in regular grid
  private float[][] _a00,_a10,_a01,_a11; // interpolation coefficients
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
    float d1 = x1-_x1[k1];
    float d2 = x2-_x2[k2];
    return _a00[k2][k1]+d1*_a10[k2][k1] +
       d2*(_a01[k2][k1]+d1*_a11[k2][k1]);
  }
  private float interpolate10(float x1, float x2, int k1, int k2) {
    float d2 = x2-_x2[k2];
    return _a10[k2][k1]+d2*_a11[k2][k1];
  }
  private float interpolate01(float x1, float x2, int k1, int k2) {
    float d1 = x1-_x1[k1];
    return _a01[k2][k1]+d1*_a11[k2][k1];
  }

  private void makeCoefficients(float[] x1, float[] x2, float[][] y) {
    int n1 = x1.length;
    int n2 = x2.length;
    _a00 = new float[n2-1][n1-1];
    _a10 = new float[n2-1][n1-1];
    _a01 = new float[n2-1][n1-1];
    _a11 = new float[n2-1][n1-1];
    for (int k2=0; k2<n2-1; ++k2) {
      float d2 = x2[k2+1]-x2[k2];
      for (int k1=0; k1<n1-1; ++k1) {
        float d1 = x1[k1+1]-x1[k1];
        float y00 = y[k2  ][k1  ];
        float y10 = y[k2  ][k1+1];
        float y01 = y[k2+1][k1  ];
        float y11 = y[k2+1][k1+1];
        _a00[k2][k1] = y00;
        _a10[k2][k1] = (y10-y00)/d1;
        _a01[k2][k1] = (y01-y00)/d2;
        _a11[k2][k1] = (y00-y10+y11-y01)/(d1*d2);
      }
    }
  }
}
