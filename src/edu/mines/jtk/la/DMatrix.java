/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la;

import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Check;
import static java.lang.Math.*;

/**
 * A double-precision matrix.
 * Matrix elements are stored in an array of arrays of doubles a[m][n], 
 * such that array element a[i][j] corresponds to the i'th row and the
 * j'th column of the m-by-n matrix.
 * <p>
 * This class was adapted from the package Jama, which was developed by 
 * Joe Hicklin, Cleve Moler, and Peter Webb of The MathWorks, Inc., and by
 * Ronald Boisvert, Bruce Miller, Roldan Pozo, and Karin Remington of the
 * National Institue of Standards and Technology.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.01
 */
public class DMatrix {

  /**
   * The number of rows.
   */
  public int m;

  /**
   * The number of columns.
   */
  public int n;

  /**
   * The m-by-n array of matrix elements. The array element a[i][j] 
   * corresponds to the i'th row and the j'th column of the matrix.
   */
  public double[][] a;

  /**
   * Constructs an m-by-n matrix of zeros.
   * @param m the number of rows.
   * @param n the number of columns.
   */
  public DMatrix(int m, int n) {
    this.m = m;
    this.n = n;
    this.a = new double[m][n];
  }

  /**
   * Constructs an m-by-n matrix filled with the specified value.
   * @param m the number of rows.
   * @param n the number of columns.
   * @param v the value.
   */
  public DMatrix(int m, int n, double v) {
    this(m,n);
    Array.fill(v,a);
  }

  /**
   * Constructs a matrix from the specified array. Does not copy array
   * elements into a new array. Rather, the new matrix simply references 
   * the specified array.
   * @param a the array.
   */
  public DMatrix(double[][] a) {
    this(a,false);
  }

  /**
   * Constructs a matrix from the specified array, with optional copying.
   * @param a the array.
   * @param copy true, to copy elements into a new array;
   *  false, to simply reference the the specified array.
   */
  public DMatrix(double[][] a, boolean copy) {
    Check.argument(Array.isRegular(a),"array a is regular");
    this.m = a.length;
    this.n = a[0].length;
    this.a = (copy)?Array.copy(a):a;
  }

  /**
   * Constructs a matrix quickly without checking arguments. Does not
   * copy array elements into a new array. Rather, the new matrix simply 
   * references the specified array.
   * @param m the number of rows.
   * @param n the number of columns.
   * @param a the array.
   */
  public DMatrix(int m, int n, double[][] a) {
    this.m = m;
    this.n = n;
    this.a = a;
  }

  /**
   * Returns the elements of this matrix packed by columns.
   * @return the packed columns.
   */
  public double[] packColumns() {
    double[] c = new double[m*n];
    for (int i=0; i<m; ++i)
      for (int j=0; j<n; ++j)
        c[i+j*m] = a[i][j];
    return c;
  }

  /**
   * Returns the elements of this matrix packed by rows.
   * @return the packed rows.
   */
  public double[] packRows() {
    double[] r = new double[m*n];
    for (int i=0; i<m; ++i)
      for (int j=0; j<n; ++j)
        r[i*n+j] = a[i][j];
    return r;
  }

  /**
   * Gets a matrix element.
   * @param i the row index.
   * @param j the column index.
   * @return the element.
   */
  public double get(int i, int j) {
    return a[i][j];
  }

  /**
   * Gets the specified submatrix a[i0:i1][j0:j1] of this matrix.
   * @param i0 the index of first row.
   * @param i1 the index of last row.
   * @param j0 the index of first column.
   * @param j1 the index of last column.
   */
  public DMatrix get(int i0, int i1, int j0, int j1) {
    int m = i1-i0+1;
    int n = j1-j0+1;
    DMatrix x = new DMatrix(m,n);
    Array.copy(n,m,j0,i0,a,0,0,x.a);
    return x;
  }

  /**
   * Gets a matrix from the specified rows and columns of this matrix.
   * @param r the array of row indices; null, for all rows.
   * @param c the array of column indices; null, for all columns.
   */
  public DMatrix get(int[] r, int[] c) {
    if (r==null && c==null) {
      return new DMatrix(m,n,a);
    } else {
      int m = (r!=null)?r.length:this.m;
      int n = (c!=null)?c.length:this.n;
      double[][] b = new double[m][n];
      if (r==null) {
        for (int i=0; i<m; ++i)
          for (int j=0; j<n; ++j)
            b[i][j] = a[i][c[j]];
      } else if (c==null) {
        for (int i=0; i<m; ++i)
          for (int j=0; j<n; ++j)
            b[i][j] = a[r[i]][j];
      } else {
        for (int i=0; i<m; ++i)
          for (int j=0; j<n; ++j)
            b[i][j] = a[r[i]][c[j]];
      }
      return new DMatrix(m,n,b);
    }
  }

  /**
   * Gets a matrix from specified one row and columns of this matrix.
   * @param i the row index.
   * @param c the array of column indices; null, for all columns.
   */
  public DMatrix get(int i, int[] c) {
    return get(i,i,c);
  }

  /**
   * Gets a matrix from specified rows and one column of this matrix.
   * @param r the array of row indices; null, for all rows.
   * @param j the column index.
   */
  public DMatrix get(int[] r, int j) {
    return get(r,j,j);
  }

  /**
   * Gets a matrix from specified rows and columns of this matrix.
   * @param i0 the index of the first row.
   * @param i1 the index of the last row.
   * @param c the array of column indices; null, for all columns.
   */
  public DMatrix get(int i0, int i1, int[] c) {
    if (c==null) {
      return get(i0,i1,0,n-1);
    } else {
      int n = c.length;
      double[][] b = new double[m][n];
      for (int i=i0; i<=i1; ++i1) {
        for (int j=0; j<n; ++j) {
          b[i][j] = a[i][c[j]];
        }
      }
      return new DMatrix(m,n,b);
    }
  }

  /**
   * Gets a matrix from specified rows and columns of this matrix.
   * @param r the array of row indices; null, for all rows.
   * @param j0 the index of the first column.
   * @param j1 the index of the last column.
   */
  public DMatrix get(int[] r, int j0, int j1) {
    if (r==null) {
      return get(0,m-1,j0,j1);
    } else {
      int m = r.length;
      double[][] b = new double[m][n];
      for (int i=0; i<m; ++i) {
        for (int j=j0; j<=j1; ++j) {
          b[i][j] = a[r[i]][j];
        }
      }
      return new DMatrix(m,n,b);
    }
  }

  /**
   * Sets a matrix element.
   * @param i the row index.
   * @param j the column index.
   * @param v the element value.
   */
  public void set(int i, int j, double v) {
    a[i][j] = v;
  }

  /**
   * Sets the specified submatrix a[i0:i1][j0:j1] of this matrix.
   * @param i0 the index of first row.
   * @param i1 the index of last row.
   * @param j0 the index of first column.
   * @param j1 the index of last column.
   * @param x the matrix from which to copy elements.
   */
  public void set(int i0, int i1, int j0, int j1, DMatrix x) {
    int m = i1-i0+1;
    int n = j1-j0+1;
    Array.copy(n,m,0,0,x.a,j0,i0,a);
  }

  /**
   * Sets the specified rows and columns of this matrix.
   * @param r the array of row indices; null, for all rows.
   * @param c the array of column indices; null, for all columns.
   * @param x the matrix from which to copy elements.
   */
  public void set(int[] r, int[] c, DMatrix x) {
    if (r==null && c==null) {
      Array.copy(x.a,a);
    } else {
      int m = (r!=null)?r.length:this.m;
      int n = (c!=null)?c.length:this.n;
      double[][] b = x.a;
      if (r==null) {
        for (int i=0; i<m; ++i)
          for (int j=0; j<n; ++j)
            a[i][c[j]] = b[i][j];
      } else if (c==null) {
        for (int i=0; i<m; ++i)
          for (int j=0; j<n; ++j)
            a[r[i]][j] = b[i][j];
      } else {
        for (int i=0; i<m; ++i)
          for (int j=0; j<n; ++j)
            a[r[i]][c[j]] = b[i][j];
      }
    }
  }

  /**
   * Sets the specified one row and columns of this matrix.
   * @param i the row index.
   * @param c the array of column indices; null, for all columns.
   * @param x the matrix from which to copy elements.
   */
  public void set(int i, int[] c, DMatrix x) {
    set(i,i,c,x);
  }

  /**
   * Sets the specified rows and one column of this matrix.
   * @param r the array of row indices; null, for all rows.
   * @param j the column index.
   * @param x the matrix from which to copy elements.
   */
  public void set(int[] r, int j, DMatrix x) {
    set(r,j,j,x);
  }

  /**
   * Sets the specified rows and columns of this matrix.
   * @param i0 the index of the first row.
   * @param i1 the index of the last row.
   * @param c the array of column indices; null, for all columns.
   * @param x the matrix from which to copy elements.
   */
  public void set(int i0, int i1, int[] c, DMatrix x) {
    if (c==null) {
      set(i0,i1,0,n-1,x);
    } else {
      int n = c.length;
      double[][] b = x.a;
      for (int i=i0; i<=i1; ++i1) {
        for (int j=0; j<n; ++j) {
          a[i][c[j]] = b[i][j];
        }
      }
    }
  }

  /**
   * Sets the specified rows and columns of this matrix.
   * @param r the array of row indices; null, for all rows.
   * @param j0 the index of the first column.
   * @param j1 the index of the last column.
   * @param x the matrix from which to copy elements.
   */
  public void set(int[] r, int j0, int j1, DMatrix x) {
    if (r==null) {
      set(0,m-1,j0,j1,x);
    } else {
      int m = r.length;
      double[][] b = x.a;
      for (int i=0; i<m; ++i) {
        for (int j=j0; j<=j1; ++j) {
          a[r[i]][j] = b[i][j];
        }
      }
    }
  }

  /**
   * Returns the transpose of this matrix.
   * @return the transpose.
   */
  public DMatrix transpose() {
    DMatrix x = new DMatrix(n,m);
    double[][] b = x.a;
    for (int i=0; i<m; ++i) {
      for (int j=0; j<n; ++j) {
        b[j][i] = a[i][j];
      }
    }
    return x;
  }

  /**
   * Returns the one-norm (maximum column sum) of this matrix.
   * @return the one-norm.
   */
  public double norm1() {
    double f = 0.0;
    for (int j=0; j<n; ++j) {
      double s = 0.0;
      for (int i=0; i<m; ++i)
        s += abs(a[i][j]);
      f = max(f,s);
    }
    return f;
  }

  /**
   * Returns the two-norm (maximum singular value) of this matrix.
   * @return the two-norm.
   */
  public double norm2() {
    return 1.0; // TODO: use the SVD.
  }

  /**
   * Returns the infinity-norm (maximum row sum) of this matrix.
   * @return the infinity-norm.
   */
  public double normI() {
    double f = 0.0;
    for (int i=0; i<m; ++i) {
      double s = 0.0;
      for (int j=0; j<n; ++j)
        s += abs(a[i][j]);
      f = max(f,s);
    }
    return f;
  }

  /**
   * Returns the Frobenius norm (sqrt of sum of squares) of this matrix.
   * @return the Frobenius norm.
   */
  public double normF() {
    double f = 0.0;
    for (int i=0; i<m; ++i) {
      for (int j=0; j<n; ++j) {
        f = hypot(f,a[i][j]);
      }
    }
    return f;
  }

  /**
   * Returns C = -A, where A is this matrix.
   * @return C = -A.
   */
  public DMatrix negate() {
    DMatrix c = new DMatrix(m,n);
    Array.neg(a,c.a);
    return c;
  }

  /**
   * Returns C = A + B, where A is this matrix.
   * @param b the matrix B.
   * @return C = A + B.
   */
  public DMatrix plus(DMatrix b) {
    DMatrix c = new DMatrix(m,n);
    Array.add(a,b.a,c.a);
    return c;
  }

  /**
   * Returns A = A + B, where A is this matrix.
   * @param b the matrix B.
   * @return A = A + B.
   */
  public DMatrix plusEquals(DMatrix b) {
    Array.add(a,b.a,a);
    return this;
  }

  /**
   * Returns C = A - B, where A is this matrix.
   * @param b the matrix B.
   * @return C = A - B.
   */
  public DMatrix minus(DMatrix b) {
    DMatrix c = new DMatrix(m,n);
    Array.sub(a,b.a,c.a);
    return c;
  }

  /**
   * Returns A = A - B, where A is this matrix.
   * @param b the matrix B.
   * @return A = A - B.
   */
  public DMatrix minusEquals(DMatrix b) {
    Array.sub(a,b.a,a);
    return this;
  }

  /**
   * Returns C = A .* B, where A is this matrix.
   * The symbol .* denotes element-by-element multiplication.
   * @param b the matrix B.
   * @return C = A .* B.
   */
  public DMatrix arrayTimes(DMatrix b) {
    DMatrix c = new DMatrix(m,n);
    Array.mul(a,b.a,c.a);
    return c;
  }

  /**
   * Returns A = A .* B, where A is this matrix.
   * The symbol .* denotes element-by-element multiplication.
   * @param b the matrix B.
   * @return A = A .* B.
   */
  public DMatrix arrayTimesEquals(DMatrix b) {
    Array.mul(a,b.a,a);
    return this;
  }

  /**
   * Returns C = A ./ B, where A is this matrix.
   * The symbol ./ denotes element-by-element right division.
   * @param b the matrix B.
   * @return C = A ./ B.
   */
  public DMatrix arrayRightDivide(DMatrix b) {
    DMatrix c = new DMatrix(m,n);
    Array.div(a,b.a,c.a);
    return c;
  }

  /**
   * Returns A = A ./ B, where A is this matrix.
   * The symbol ./ denotes element-by-element right division.
   * @param b the matrix B.
   * @return A = A ./ B.
   */
  public DMatrix arrayRightDivideEquals(DMatrix b) {
    Array.div(a,b.a,a);
    return this;
  }

  /**
   * Returns C = A .\ B, where A is this matrix.
   * The symbol .\ denotes element-by-element left division.
   * @param b the matrix B.
   * @return C = A .\ B.
   */
  public DMatrix arrayLeftDivide(DMatrix b) {
    DMatrix c = new DMatrix(m,n);
    Array.div(b.a,a,c.a);
    return c;
  }

  /**
   * Returns A = A .\ B, where A is this matrix.
   * The symbol .\ denotes element-by-element left division.
   * @param b the matrix B.
   * @return A = A .\ B.
   */
  public DMatrix arrayLeftDivideEquals(DMatrix b) {
    Array.div(b.a,a,a);
    return this;
  }

  /**
   * Returns C = A * s, where A is this matrix, and s is a scalar.
   * @param s the scalar s.
   * @return C = A * s.
   */
  public DMatrix times(double s) {
    DMatrix c = new DMatrix(m,n);
    Array.mul(a,s,c.a);
    return c;
  }

  /**
   * Returns A = A * s, where A is this matrix, and s is a scalar.
   * @param s the scalar s.
   * @return A = A * s.
   */
  public DMatrix timesEquals(double s) {
    Array.mul(a,s,a);
    return this;
  }

  /**
   * Returns C = A * B, where A is this matrix. The number of columns in 
   * this matrix A must equal the number of rows in the specified matrix B.
   * @param b the matrix B.
   * @return C = A * B.
   */
  public DMatrix times(DMatrix b) {
    Check.argument(n==b.m,"number of columns in A equals number of rows in B");
    DMatrix c = new DMatrix(m,b.n);
    double[][] aa = a;
    double[][] ba = b.a;
    double[][] ca = c.a;
    double[] bj = new double[n];
    for (int j=0; j<b.n; ++j) {
      for (int k=0; k<n; ++k)
        bj[k] = ba[k][j];
      for (int i=0; i<m; ++i) {
        double[] ai = aa[i];
        double s = 0.0;
        for (int k=0; k<n; ++k)
          s += ai[k]*bj[k];
        ca[i][j] = s;
      }
    }
    return c;
  }

  /**
   * Returns the trace (sum of diagonal elements) of this matrix.
   * @return the trace.
   */
  public double trace() {
    int mn = min(m,n);
    double t = 0.0;
    for (int i=0; i<mn; ++i)
      t += a[i][i];
    return t;
  }

  /**
   * Returns a new matrix with random elements. The distribution of the
   * random numbers is uniform in the interval [0,1).
   * @param m the number of rows.
   * @param n the number of columns.
   * @return the random matrix.
   */
  public static DMatrix random(int m, int n) {
    DMatrix x = new DMatrix(m,n);
    Array.rand(x.a);
    return x;
  }

  /**
   * Returns a new identity matrix.
   * @param m the number of rows.
   * @param n the number of columns.
   * @return the identity matrix.
   */
  public static DMatrix identity(int m, int n) {
    DMatrix x = new DMatrix(m,n);
    double[][] xa = x.a;
    int mn = min(m,n);
    for (int i=0; i<mn; ++i)
      xa[i][i] = 1.0;
    return x;
  }
}
