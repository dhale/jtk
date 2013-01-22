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
 * Piecewise trilinear polynomial interpolation of a function y(x1,x2,x3).
 * <p>
 * The function y(x1,x2,x3) is specified by samples on a regular grid, which
 * need not be uniform. The regular grid is specified by one-dimensional
 * arrays of monotonically increasing coordinates x1, x2 and x3, such that
 * gridded x1 are identical for all gridded x2 and x3, gridded x2 are
 * identical for all gridded x1 and x2, and gridded x3 are identical for all
 * gridded x1 and x2.
 * <p>
 * Extrapolation (that is, interpolation outside the specified grid of
 * (x1,x2,x3) coordinates), is performed using linear polynomials for the
 * nearest grid samples. Extrapolated values can be well outside the [min,max]
 * range of interpolated values, and should typically be avoided.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2013.01.22
 */
public class TrilinearInterpolator3 {

  /**
   * Constructs an interpolator for specified values y(x1,x2).
   * @param x1 array[n1] of x1 coordinates; monotonically increasing.
   * @param x2 array[n2] of x2 coordinates; monotonically increasing.
   * @param x3 array[n3] of x3 coordinates; monotonically increasing.
   * @param y array[n3][n2][n1] of sampled values y(x1,x2,x3).
   */
  public TrilinearInterpolator3(
    float[] x1, float[] x2, float[] x3, float[][][] y) 
  {
    this(x1.length,x2.length,x3.length,x1,x2,x3,y);
  }

  /**
   * Constructs an interpolator for specified values.
   * @param n1 number of x1 coordinates.
   * @param n2 number of x2 coordinates.
   * @param n3 number of x3 coordinates.
   * @param x1 array[n1] of x1 coordinates; monotonically increasing.
   * @param x2 array[n2] of x2 coordinates; monotonically increasing.
   * @param x3 array[n3] of x3 coordinates; monotonically increasing.
   * @param y array[n3][n2][n1] of sampled values y(x1,x2,x3).
   */
  public TrilinearInterpolator3(
    int n1, int n2, int n3, 
    float[] x1, float[] x2, float[] x3, float[][][] y) 
  {
    Check.argument(isMonotonic(x1), "array x1 is monotonic");
    Check.argument(isMonotonic(x2), "array x2 is monotonic");
    Check.argument(isMonotonic(x3), "array x3 is monotonic");
    _x1 = copy(n1,x1);
    _x2 = copy(n2,x2);
    _x3 = copy(n3,x3);
    makeCoefficients(x1,x2,x3,y);
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

  private float[] _x1; // array of x1 coordinates in regular grid
  private float[] _x2; // array of x2 coordinates in regular grid
  private float[] _x3; // array of x3 coordinates in regular grid
  private float[][][] _a000,_a100,_a010,_a001, // interpolation
                      _a110,_a101,_a011,_a111; // coefficients
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
    float d1 = x1-_x1[k1];
    float d2 = x2-_x2[k2];
    float d3 = x3-_x3[k3];
    return _a000[k3][k2][k1]+
       d1*(_a100[k3][k2][k1]+d3*(_a101[k3][k2][k1])) +
       d2*(_a010[k3][k2][k1]+d1*(_a110[k3][k2][k1])) +
       d3*(_a001[k3][k2][k1]+d2*(_a011[k3][k2][k1]+d1*(_a111[k3][k2][k1])));
  }
  private float interpolate100(
    float x1, float x2, float x3, int k1, int k2, int k3) 
  {
    float d2 = x2-_x2[k2];
    float d3 = x3-_x3[k3];
    return _a100[k3][k2][k1]+d2*_a110[k3][k2][k1] +
       d3*(_a101[k3][k2][k1]+d2*_a111[k3][k2][k1]);
  }
  private float interpolate010(
    float x1, float x2, float x3, int k1, int k2, int k3) 
  {
    float d1 = x1-_x1[k1];
    float d3 = x3-_x3[k3];
    return _a010[k3][k2][k1]+d1*_a110[k3][k2][k1] +
       d3*(_a011[k3][k2][k1]+d1*_a111[k3][k2][k1]);
  }
  private float interpolate001(
    float x1, float x2, float x3, int k1, int k2, int k3) 
  {
    float d1 = x1-_x1[k1];
    float d2 = x2-_x2[k2];
    return _a001[k3][k2][k1]+d1*_a101[k3][k2][k1] +
       d2*(_a011[k3][k2][k1]+d1*_a111[k3][k2][k1]);
  }

  private void makeCoefficients(
    float[] x1, float[] x2, float[] x3, float[][][] y) 
  {
    int n1 = x1.length;
    int n2 = x2.length;
    int n3 = x3.length;
    _a000 = new float[n3-1][n2-1][n1-1];
    _a100 = new float[n3-1][n2-1][n1-1];
    _a010 = new float[n3-1][n2-1][n1-1];
    _a001 = new float[n3-1][n2-1][n1-1];
    _a110 = new float[n3-1][n2-1][n1-1];
    _a101 = new float[n3-1][n2-1][n1-1];
    _a011 = new float[n3-1][n2-1][n1-1];
    _a111 = new float[n3-1][n2-1][n1-1];
    for (int k3=0; k3<n3-1; ++k3) {
      float d3 = x3[k3+1]-x3[k3];
      for (int k2=0; k2<n2-1; ++k2) {
        float d2 = x2[k2+1]-x2[k2];
        for (int k1=0; k1<n1-1; ++k1) {
          float d1 = x1[k1+1]-x1[k1];
          float y000 = y[k3  ][k2  ][k1  ];
          float y100 = y[k3  ][k2  ][k1+1];
          float y010 = y[k3  ][k2+1][k1  ];
          float y001 = y[k3+1][k2  ][k1  ];
          float y110 = y[k3  ][k2+1][k1+1];
          float y101 = y[k3+1][k2  ][k1+1];
          float y011 = y[k3+1][k2+1][k1  ];
          float y111 = y[k3+1][k2+1][k1+1];
          _a000[k3][k2][k1] = y000;
          _a100[k3][k2][k1] = (y100-y000)/d1;
          _a010[k3][k2][k1] = (y010-y000)/d2;
          _a001[k3][k2][k1] = (y001-y000)/d3;
          _a110[k3][k2][k1] = (y000-y100+y110-y010)/(d1*d2);
          _a101[k3][k2][k1] = (y000-y100+y101-y001)/(d1*d3);
          _a011[k3][k2][k1] = (y000-y010+y011-y001)/(d2*d3);
          _a111[k3][k2][k1] = (y111-y000+y100-y011+y010-y101+y001-y110) /
                              (d1*d2*d3);
        }
      }
    }
  }
}
