/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la;

/**
 * A tridiagonal matrix is a square matrix specified by three diagonals.
 * All elements except for those on the diagonal, lower sub-diagonal, and
 * upper super-diagonal of the matrix are equal to zero. The diagonals are 
 * represented by three arrays a, b, and c of matrix elements. Here is an 
 * example of a tridiagonal system of n = 4 equations:
 * <pre><code>
 *  |b[0]    c[0]     0       0  | |u[0]|     |r[0]|
 *  |a[1]    b[1]    c[1]     0  | |u[1]|  =  |r[1]|
 *  | 0      a[2]    b[2]    c[2]| |u[2]|     |r[2]|
 *  | 0       0      a[3]    b[3]| |u[3]|     |r[3]|
 * </code></pre>
 * The values a[0] and c[n-1] are ignored.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.12.12
 */
public class TridiagonalFMatrix {

  /**
   * Constructs a tridiagonal matrix with the specified number of rows.
   * All matrix elements are initially zero.
   * @param n the number of rows (and columns) in the matrix.
   */
  public TridiagonalFMatrix(int n) {
    this(n, new float[n], new float[n], new float[n]);
  }

  /**
   * Constructs a new tridiagonal matrix with specified elements.
   * The arrays a, b, and c are passed by reference, not by copy.
   * @param n the number of rows (and columns) in the matrix.
   * @param a array of lower sub-diagonal elements; a[0] is ignored.
   * @param b array of diagonal elements.
   * @param c array of upper super-diagonal elements; c[n-1] is ignored.
   */
  public TridiagonalFMatrix(int n, float[] a, float[] b, float[] c) {
    _n = n;
    _a = a;
    _b = b;
    _c = c;
  }

  /**
   * Returns the number of rows and columns in this matrix.
   * @return the number of rows and columns.
   */
  public int n() {
    return _n;
  }

  /**
   * Returns the array a of lower sub-diagonal elements.
   * @return the array a; by reference, not by copy.
   */
  public float[] a() {
    return _a;
  }

  /**
   * Returns the array b of diagonal elements.
   * @return the array b; by reference, not by copy.
   */
  public float[] b() {
    return _b;
  }

  /**
   * Returns the array c of upper sub-diagonal elements.
   * @return the array c; by reference, not by copy.
   */
  public float[] c() {
    return _c;
  }

  /**
   * Solves this tridiagonal system for specified right-hand-side.
   * Uses Gaussian elimination without pivoting, and assumes that this 
   * matrix is non-singular.
   * @param r input array containing the right-hand-side column vector.
   * @param u output array containing the left-hand-side vector of unknowns.
   */
  public void solve(float[] r, float[] u) {
    if (_w==null)
      _w = new float[_n];
    float t = _b[0];
    u[0] = r[0]/t;
    for (int j=1; j<_n; ++j) {
      _w[j] = _c[j-1]/t;
      t = _b[j]-_a[j]*_w[j];
      u[j] = (r[j]-_a[j]*u[j-1])/t;
    }
    for (int j=_n-1; j>0; --j)
      u[j-1] -= _w[j]*u[j];
  }

  /**
   * Multiplies this matrix by the specified column vector.
   * @param x input array containing the column vector.
   * @return array containing the matrix-vector product.
   */
  public float[] times(float[] x) {
    int n = x.length;
    float[] y = new float[n];
    times(x,y);
    return y;
  }

  /**
   * Multiplies this matrix by the specified column vector.
   * @param x input array containing the column vector.
   * @param y output array containing the matrix-vector product.
   */
  public void times(float[] x, float[] y) {
    int n = x.length;
    int nm1 = n-1;
    float xim1;
    float xip1 = 0.0f;
    float xi = x[0];
    y[0] = _b[0]*xi;
    if (n>1) {
      xip1 = x[1];
      y[0] += _c[0]*xip1;
      y[n-1] = _a[n-1]*x[n-2]+_b[n-1]*x[n-1];
    }
    for (int i=1; i<nm1; ++i) {
      xim1 = xi;
      xi = xip1;
      xip1 = x[i+1];
      y[i] = _a[i]*xim1+_b[i]*xi+_c[i]*xip1;
    }
  }

  private int _n; // number of rows and columns
  private float[] _a,_b,_c; // the three diagonals
  private float[] _w; // work array
}
