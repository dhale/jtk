/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import org.netlib.blas.BLAS;
import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * A double-precision matrix.
 * Elements of an m-by-n matrix are stored contiguously in an array a[m*n], 
 * such that array element a[i+j*m] corresponds to the i'th row and the 
 * j'th column of the m-by-n matrix. For example, a 3-by-3 matrix is stored 
 * as:
 * <pre><code>
 *   a[0] a[3] a[6]
 *   a[1] a[4] a[7]
 *   a[2] a[5] a[8]
 * </code></pre>
 * This is the column-major order required by LAPACK.
 * <p>
 * Decompositions of a matrix A facilate the solutions of various problems. 
 * For example, the QR decomposition A = Q*R of a rectangular matrix A
 * (where Q is orthogonal and R is upper right triangular) is useful in 
 * least-square approximations. Decompositions currently provided by this 
 * class include
 * <ul>
 * <li>LU decomposition of rectangular matrices</li>
 * <li>QR decomposition of rectangular matrices</li>
 * <li>singular value decomposition of rectangular matrices</li>
 * <li>eigenvalue and eigenvector decomposition of square matrices</li>
 * <li>Cholesky decomposition of symmetric positive-definite matrices</li>
 * </ul>
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.12
 */
public class DMatrix {

  /**
   * Constructs an m-by-n matrix of zeros.
   * @param m the number of rows.
   * @param n the number of columns.
   */
  public DMatrix(int m, int n) {
    _m = m;
    _n = n;
    _a = new double[m*n];
  }

  /**
   * Constructs an m-by-n matrix filled with the specified value.
   * @param m the number of rows.
   * @param n the number of columns.
   * @param v the value.
   */
  public DMatrix(int m, int n, double v) {
    this(m,n);
    fill(v,_a);
  }

  /**
   * Constructs a matrix from the specified array. Does not copy array
   * elements into a new array. Rather, this matrix simply references
   * the specified array. 
   * <p>
   * That array contains packed columns of this matrix. In other words,
   * array element a[i+j*m] corresponds to the i'th row and j'th column
   * of this matrix.
   * @param m the number of rows.
   * @param n the number of columns.
   * @param a the array.
   */
  public DMatrix(int m, int n, double[] a) {
    Check.argument(m*n<=a.length,"m*n <= a.length");
    _m = m;
    _n = n;
    _a = a;
  }

  /**
   * Constructs a matrix from the specified array. Copies array elements
   * a[i][j] to the i'th row and j'th column of this matrix. In other
   * words, the specified array contains matrix elements ordered by rows.
   * <p>
   * The specified array must be regular. That is, each row much contain
   * the same number of columns, and each column must contain the same
   * number of rows.
   * @param a the array.
   */
  public DMatrix(double[][] a) {
    Check.argument(isRegular(a),"array a is regular");
    _m = a.length;
    _n = a[0].length;
    _a = new double[_m*_n];
    set(a);
  }

  /**
   * Constructs a copy of the specified matrix.
   * @param a the matrix.
   */
  public DMatrix(DMatrix a) {
    this(a._m,a._n, copy(a._a));
  }

  /**
   * Gets the number of rows in this matrix.
   * @return the number of rows.
   */
  public int getM() {
    return _m;
  }

  /**
   * Gets the number of rows in this matrix.
   * @return the number of rows.
   */
  public int getRowCount() {
    return _m;
  }

  /**
   * Gets the number of columns in this matrix.
   * @return the number of columns.
   */
  public int getN() {
    return _n;
  }

  /**
   * Gets the number of columns in this matrix.
   * @return the number of columns.
   */
  public int getColumnCount() {
    return _n;
  }

  /**
   * Gets the array in which matrix elements are stored.
   * @return the array; by reference, not by copy.
   */
  public double[] getArray() {
    return _a;
  }

  /**
   * Determines whether this matrix is square.
   * @return true, if square; false, otherwise.
   */
  public boolean isSquare() {
    return _m==_n;
  }

  /**
   * Determines whether this matrix is symmetric (and square).
   * @return true, if symmetric (and square); false, otherwise.
   */
  public boolean isSymmetric() {
    if (!isSquare())
      return false;
    for (int i=0; i<_n; ++i)
      for (int j=i+1; j<_n; ++j)
        if (_a[i+j*_m]!=_a[j+i*_m])
          return false;
    return true;
  }

  /**
   * Gets all elements of this matrix into a new array.
   * @return the array.
   */
  public double[][] get() {
    double[][] a = new double[_m][_n];
    get(a);
    return a;
  }

  /**
   * Gets all elements of this matrix into the specified array.
   * @param a the array.
   */
  public void get(double[][] a) {
    for (int i=0; i<_m; ++i) {
      double[] ai = a[i];
      for (int j=0; j<_n; ++j) {
        ai[j] = _a[i+j*_m];
      }
    }
  }

  /**
   * Gets a matrix element.
   * @param i the row index.
   * @param j the column index.
   * @return the element.
   */
  public double get(int i, int j) {
    return _a[i+j*_m];
  }

  /**
   * Gets the specified submatrix a[i0:i1][j0:j1] of this matrix.
   * @param i0 the index of first row.
   * @param i1 the index of last row.
   * @param j0 the index of first column.
   * @param j1 the index of last column.
   */
  public DMatrix get(int i0, int i1, int j0, int j1) {
    checkI(i0,i1);
    checkJ(j0,j1);
    int m = i1-i0+1;
    int n = j1-j0+1;
    double[] b = new double[m*n];
    for (int j=0; j<n; ++j)
      for (int i=0; i<m; ++i)
        b[i+j*m] = _a[i+i0+(j+j0)*_m];
    return new DMatrix(m,n,b);
  }

  /**
   * Gets a new matrix from the specified rows and columns of this matrix.
   * @param r the array of row indices; null, for all rows.
   * @param c the array of column indices; null, for all columns.
   */
  public DMatrix get(int[] r, int[] c) {
    if (r==null && c==null) {
      return new DMatrix(_m,_n, copy(_a));
    } else {
      int m = (r!=null)?r.length:_m;
      int n = (c!=null)?c.length:_n;
      double[] b = new double[m*n];
      if (r==null) {
        for (int j=0; j<n; ++j)
          for (int i=0; i<m; ++i)
            b[i+j*m] = _a[i+c[j]*_m];
      } else if (c==null) {
        for (int j=0; j<n; ++j)
          for (int i=0; i<m; ++i)
            b[i+j*m] = _a[r[i]+j*_m];
      } else {
        for (int j=0; j<n; ++j)
          for (int i=0; i<m; ++i)
            b[i+j*m] = _a[r[i]+c[j]*_m];
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
    checkI(i0,i1);
    if (c==null) {
      return get(i0,i1,0,_n-1);
    } else {
      int m = i1-i0+1;
      int n = c.length;
      double[] b = new double[m*n];
      for (int j=0; j<n; ++j) {
        for (int i=i0; i<=i1; ++i) {
          b[i-i0+j*m] = _a[i+c[j]*_m];
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
    checkJ(j0,j1);
    if (r==null) {
      return get(0,_m-1,j0,j1);
    } else {
      int m = r.length;
      int n = j1-j0+1;
      double[] b = new double[m*n];
      for (int j=j0; j<=j1; ++j) {
        for (int i=0; i<m; ++i) {
          b[i+(j-j0)*m] = _a[r[i]+j*_m];
        }
      }
      return new DMatrix(m,n,b);
    }
  }

  /**
   * Gets the elements of this matrix packed by columns.
   * @return the array of matrix elements packed by columns.
   */
  public double[] getPackedColumns() {
    return copy(_a);
  }

  /**
   *Gets the elements of this matrix packed by rows.
   * @return the array of matrix elements packed by rows.
   */
  public double[] getPackedRows() {
    double[] r = new double[_m*_n];
    for (int j=0; j<_n; ++j)
      for (int i=0; i<_m; ++i)
        r[i*_n+j] = _a[i+j*_m];
    return r;
  }

  /**
   * Sets all elements of this matrix from the specified array.
   * Copies each array element into this matrix.
   * @param a the array.
   */
  public void set(double[][] a) {
    for (int i=0; i<_m; ++i) {
      double[] ai = a[i];
      for (int j=0; j<_n; ++j) {
        _a[i+j*_m] = ai[j];
      }
    }
  }

  /**
   * Sets a matrix element.
   * @param i the row index.
   * @param j the column index.
   * @param v the element value.
   */
  public void set(int i, int j, double v) {
    _a[i+j*_m] = v;
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
    checkI(i0,i1);
    checkJ(j0,j1);
    int m = i1-i0+1;
    int n = j1-j0+1;
    Check.argument(m==x._m,"i1-i0+1 equals number of rows in x");
    Check.argument(n==x._n,"j1-j0+1 equals number of columns in x");
    double[] b = x._a;
    for (int i=0; i<m; ++i)
      for (int j=0; j<n; ++j)
        _a[i+i0+(j+j0)*_m] = b[i+j*m];
  }

  /**
   * Sets the specified rows and columns of this matrix.
   * @param r the array of row indices; null, for all rows.
   * @param c the array of column indices; null, for all columns.
   * @param x the matrix from which to copy elements.
   */
  public void set(int[] r, int[] c, DMatrix x) {
    if (r==null) {
      Check.argument(_m==x._m,"number of rows equal in this and x");
    } else {
      Check.argument(r.length==x._m,"r.length equals number of rows in x");
    }
    if (c==null) {
      Check.argument(_n==x._n,"number of columns equal in this and x");
    } else {
      Check.argument(c.length==x._n,"c.length equals number of columns in x");
    }
    if (r==null && c==null) {
      copy(x._a,_a);
    } else {
      int m = (r!=null)?r.length:_m;
      int n = (c!=null)?c.length:_n;
      double[] b = x._a;
      if (r==null) {
        for (int j=0; j<n; ++j)
          for (int i=0; i<m; ++i)
            _a[i+c[j]*_m] = b[i+j*m];
      } else if (c==null) {
        for (int j=0; j<n; ++j)
          for (int i=0; i<m; ++i)
            _a[r[i]+j*_m] = b[i+j*m];
      } else {
        for (int j=0; j<n; ++j)
          for (int i=0; i<m; ++i)
            _a[r[i]+c[j]*_m] = b[i+j*m];
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
    checkI(i0,i1);
    Check.argument(i1-i0+1==x._m,"i1-i0+1 equals number of rows in x");
    if (c==null) {
      set(i0,i1,0,_n-1,x);
    } else {
      int m = i1-i0+1;
      int n = c.length;
      double[] b = x._a;
      for (int j=0; j<n; ++j) {
        for (int i=i0; i<=i1; ++i) {
          _a[i+c[j]*_m] = b[i-i0+j*m];
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
    checkJ(j0,j1);
    Check.argument(j1-j0+1==x._n,"j1-j0+1 equals number of columns in x");
    if (r==null) {
      set(0,_m-1,j0,j1,x);
    } else {
      int m = r.length;
      double[] b = x._a;
      for (int j=j0; j<=j1; ++j) {
        for (int i=0; i<m; ++i) {
          _a[r[i]+j*_m] = b[i+(j-j0)*m];
        }
      }
    }
  }

  /**
   * Sets the elements of this matrix from an array packed by columns.
   * @param c the array of matrix elements packed by columns.
   */
  public void setPackedColumns(double[] c) {
    copy(_m*_n,c,_a);
  }

  /**
   * Sets the elements of this matrix from an array packed by rows.
   * @param r the array of matrix elements packed by rows.
   */
  public void setPackedRows(double[] r) {
    for (int j=0; j<_n; ++j)
      for (int i=0; i<_m; ++i)
        _a[i+j*_m] = r[i*_n+j];
  }

  /**
   * Returns the transpose of this matrix.
   * @return the transpose.
   */
  public DMatrix transpose() {
    DMatrix x = new DMatrix(_n,_m);
    double[] b = x._a;
    for (int j=0; j<_n; ++j) {
      for (int i=0; i<_m; ++i) {
        b[j+i*_n] = _a[i+j*_m];
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
    for (int j=0; j<_n; ++j) {
      double s = 0.0;
      for (int i=0; i<_m; ++i)
        s += abs(_a[i+j*_m]);
      f = max(f,s);
    }
    return f;
  }

  /**
   * Returns the two-norm (maximum singular value) of this matrix.
   * @return the two-norm.
   * @see edu.mines.jtk.lapack.DMatrixSvd
   */
  public double norm2() {
    return new DMatrixSvd(this).norm2();
  }

  /**
   * Returns the infinity-norm (maximum row sum) of this matrix.
   * @return the infinity-norm.
   */
  public double normI() {
    double f = 0.0;
    for (int i=0; i<_m; ++i) {
      double s = 0.0;
      for (int j=0; j<_n; ++j)
        s += abs(_a[i+j*_m]);
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
    for (int j=0; j<_n; ++j) {
      for (int i=0; i<_m; ++i) {
        f = hypot(f,_a[i+j*_m]);
      }
    }
    return f;
  }

  /**
   * Returns the determinant of this matrix.
   * @return the determinant.
   */
  public double det() {
    return new DMatrixLud(this).det();
  }

  /**
   * Returns the condition number of this matrix.
   * @return the condition number.
   * @see edu.mines.jtk.lapack.DMatrixSvd
   */
  public double cond() {
    return new DMatrixSvd(this).cond();
  }

  /**
   * Returns the effective numerical rank of this matrix.
   * @return the rank.
   * @see edu.mines.jtk.lapack.DMatrixSvd
   */
  public double rank() {
    return new DMatrixSvd(this).rank();
  }

  /**
   * Returns the trace (sum of diagonal elements) of this matrix.
   * @return the trace.
   */
  public double trace() {
    int mn = min(_m,_n);
    double t = 0.0;
    for (int i=0; i<mn; ++i)
      t += _a[i+i*_m];
    return t;
  }

  /**
   * Returns the LU decomposition of this matrix.
   * @return the LU decomposition.
   */
  public DMatrixLud lud() {
    return new DMatrixLud(this);
  }

  /**
   * Returns the QR decomposition of this matrix.
   * @return the QR decomposition.
   */
  public DMatrixQrd qrd() {
    return new DMatrixQrd(this);
  }

  /**
   * Returns the Cholesky decomposition of this matrix.
   * @return the Cholesky decomposition.
   */
  public DMatrixChd chd() {
    return new DMatrixChd(this);
  }

  /**
   * Returns the eigenvalue and eigenvector decomposition of this matrix.
   * @return the eigenvalue and eigenvector decomposition.
   */
  public DMatrixEvd evd() {
    return new DMatrixEvd(this);
  }

  /**
   * Returns the singular value decomposition of this matrix.
   * @return the singular value decomposition.
   */
  public DMatrixSvd svd() {
    return new DMatrixSvd(this);
  }

  /**
   * Solves the system A*X = B. Requires m&gt;=n for this m-by-n matrix A.
   * If m&gt;n, then the returned solution X is a least-squares solution.
   * @param b the matrix B of right-hand-side column vectors.
   * @return the matrix X of solution vectors.
   */
  public DMatrix solve(DMatrix b) {
    Check.state(_m>=_n,"number of rows is not less than number of columns");
    if (_m==_n) {
      return new DMatrixLud(this).solve(b);
    } else {
      return new DMatrixQrd(this).solve(b);
    }
  }

  /**
   * Returns the inverse of this matrix.
   * If m&gt;n for this m-by-n matrix A, then returns the pseudo-inverse.
   */
  public DMatrix inverse() {
    return solve(identity(_m,_m));
  }

  /**
   * Returns C = -A, where A is this matrix.
   * @return C = -A.
   */
  public DMatrix negate() {
    DMatrix c = new DMatrix(_m,_n);
    neg(_a,c._a);
    return c;
  }

  /**
   * Returns C = A + B, where A is this matrix.
   * @param b the matrix B.
   * @return C = A + B.
   */
  public DMatrix plus(DMatrix b) {
    DMatrix c = new DMatrix(_m,_n);
    add(_a,b._a,c._a);
    return c;
  }

  /**
   * Returns A = A + B, where A is this matrix.
   * @param b the matrix B.
   * @return A = A + B.
   */
  public DMatrix plusEquals(DMatrix b) {
    add(_a,b._a,_a);
    return this;
  }

  /**
   * Returns C = A - B, where A is this matrix.
   * @param b the matrix B.
   * @return C = A - B.
   */
  public DMatrix minus(DMatrix b) {
    DMatrix c = new DMatrix(_m,_n);
    sub(_a,b._a,c._a);
    return c;
  }

  /**
   * Returns A = A - B, where A is this matrix.
   * @param b the matrix B.
   * @return A = A - B.
   */
  public DMatrix minusEquals(DMatrix b) {
    sub(_a,b._a,_a);
    return this;
  }

  /**
   * Returns C = A .* B, where A is this matrix.
   * The symbol .* denotes element-by-element multiplication.
   * @param b the matrix B.
   * @return C = A .* B.
   */
  public DMatrix arrayTimes(DMatrix b) {
    DMatrix c = new DMatrix(_m,_n);
    mul(_a,b._a,c._a);
    return c;
  }

  /**
   * Returns A = A .* B, where A is this matrix.
   * The symbol .* denotes element-by-element multiplication.
   * @param b the matrix B.
   * @return A = A .* B.
   */
  public DMatrix arrayTimesEquals(DMatrix b) {
    mul(_a,b._a,_a);
    return this;
  }

  /**
   * Returns C = A ./ B, where A is this matrix.
   * The symbol ./ denotes element-by-element right division.
   * @param b the matrix B.
   * @return C = A ./ B.
   */
  public DMatrix arrayRightDivide(DMatrix b) {
    DMatrix c = new DMatrix(_m,_n);
    div(_a,b._a,c._a);
    return c;
  }

  /**
   * Returns A = A ./ B, where A is this matrix.
   * The symbol ./ denotes element-by-element right division.
   * @param b the matrix B.
   * @return A = A ./ B.
   */
  public DMatrix arrayRightDivideEquals(DMatrix b) {
    div(_a,b._a,_a);
    return this;
  }

  /**
   * Returns C = A .\ B, where A is this matrix.
   * The symbol .\ denotes element-by-element left division.
   * @param b the matrix B.
   * @return C = A .\ B.
   */
  public DMatrix arrayLeftDivide(DMatrix b) {
    DMatrix c = new DMatrix(_m,_n);
    div(b._a,_a,c._a);
    return c;
  }

  /**
   * Returns A = A .\ B, where A is this matrix.
   * The symbol .\ denotes element-by-element left division.
   * @param b the matrix B.
   * @return A = A .\ B.
   */
  public DMatrix arrayLeftDivideEquals(DMatrix b) {
    div(b._a,_a,_a);
    return this;
  }

  /**
   * Returns C = A * s, where A is this matrix, and s is a scalar.
   * @param s the scalar s.
   * @return C = A * s.
   */
  public DMatrix times(double s) {
    DMatrix c = new DMatrix(_m,_n);
    mul(_a,s,c._a);
    return c;
  }

  /**
   * Returns A = A * s, where A is this matrix, and s is a scalar.
   * @param s the scalar s.
   * @return A = A * s.
   */
  public DMatrix timesEquals(double s) {
    mul(_a,s,_a);
    return this;
  }

  /**
   * Returns C = A * B, where A is this matrix. The number of columns in 
   * this matrix A must equal the number of rows in the specified matrix B.
   * @param b the matrix B.
   * @return C = A * B.
   */
  public DMatrix times(DMatrix b) {
    Check.argument(_n==b._m,
      "number of columns in A equals number of rows in B");
    DMatrix c = new DMatrix(_m,b._n);
    _blas.dgemm("N","N",_m,b._n,_n,1.0,_a,_m,b._a,b._m,1.0,c._a,c._m);
    return c;
  }

  /**
   * Returns C = A * B', where A is this matrix and B' is B transposed. 
   * The number of columns in this matrix A must equal the number of 
   * columns in the specified matrix B.
   * @param b the matrix B.
   * @return C = A * B'.
   */
  public DMatrix timesTranspose(DMatrix b) {
    Check.argument(_n==b._n,
      "number of columns in A equals number of columns in B");
    DMatrix c = new DMatrix(_m,b._m);
    _blas.dgemm("N","T",_m,b._n,_n,1.0,_a,_m,b._a,b._m,1.0,c._a,c._m);
    return c;
  }

  /**
   * Returns C = A' * B, where A' is this matrix transposed. 
   * The number of rows in this matrix A must equal the number of 
   * rows in the specified matrix B.
   * @param b the matrix B.
   * @return C = A' * B.
   */
  public DMatrix transposeTimes(DMatrix b) {
    Check.argument(_m==b._m,
      "number of rows in A equals number of rows in B");
    DMatrix c = new DMatrix(_n,b._n);
    _blas.dgemm("T","N",_n,b._n,_m,1.0,_a,_m,b._a,b._m,1.0,c._a,c._m);
    return c;
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
    rand(x._a);
    return x;
  }

  /**
   * Returns a new square matrix with random elements. The distribution 
   * of the random numbers is uniform in the interval [0,1).
   * @param n the number of rows and columns.
   * @return the random matrix.
   */
  public static DMatrix random(int n) {
    return DMatrix.random(n,n);
  }

  /**
   * Returns a new identity matrix.
   * @param m the number of rows.
   * @param n the number of columns.
   * @return the identity matrix.
   */
  public static DMatrix identity(int m, int n) {
    DMatrix x = new DMatrix(m,n);
    double[] xa = x._a;
    int mn = min(m,n);
    for (int i=0; i<mn; ++i)
      xa[i+i*m] = 1.0;
    return x;
  }

  /**
   * Returns a new square identity matrix.
   * @param n the number of rows and columns.
   * @return the identity matrix.
   */
  public static DMatrix identity(int n) {
    return DMatrix.identity(n,n);
  }

  /**
   * Returns a new diagonal matrix with specified elements.
   * @param d array of diagonal elements d[k] = A(k,k).
   */
  public static DMatrix diagonal(double[] d) {
    int n = d.length;
    double[] a = new double[n*n];
    for (int i=0; i<n; ++i)
      a[i+i*n] = d[i];
    return new DMatrix(n,n,a);
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    DMatrix that = (DMatrix)obj;
    if (this._m!=that._m ||  this._n!=that._n)
      return false;
    double[] a = this._a;
    double[] b = that._a;
    int mn = _m*_n;
    for  (int i=0; i<mn; ++i) {
      if (a[i]!=b[i])
        return false;
    }
    return true;
  }

  public int hashCode() {
    int h = _m^_n;
    int mn = _m*_n;
    for  (int i=0; i<mn; ++i) {
      long bits = Double.doubleToLongBits(_a[i]);
      h ^= (int)(bits^(bits>>>32)); 
    }
    return h;
  }

  public String toString() {
    String ls = System.getProperty("line.separator");
    StringBuilder sb = new StringBuilder();
    String[][] s = format(_m,_n,_a);
    int max = maxlen(s);
    String format = "%"+max+"s";
    sb.append("[[");
    int ncol = 77/(max+2);
    if (ncol>=5)
      ncol = (ncol/5)*5;
    for (int i=0; i<_m; ++i) {
      int nrow = 1+(_n-1)/ncol;
      if (i>0)
        sb.append(" [");
      for (int irow=0,j=0; irow<nrow; ++irow) {
        for (int icol=0; icol<ncol && j<_n; ++icol,++j) {
          sb.append(String.format(format,s[i][j]));
          if (j<_n-1)
            sb.append(", ");
        }
        if (j<_n) {
          sb.append(ls);
          sb.append("  ");
        } else { 
          if (i<_m-1) {
            sb.append("],");
            sb.append(ls);
          } else {
            sb.append("]]");
            sb.append(ls);
          }
        }
      }
    }
    return sb.toString();
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final BLAS _blas = BLAS.getInstance();

  private int _m; // number of rows
  private int _n; // number of columns
  private double[] _a; // array[_m*_n] of matrix elements

  private void checkI(int i) {
    if (i<0 || i>=_m)
      Check.argument(0<=i && i<_m,"row index i="+i+" is in bounds");
  }

  private void checkJ(int j) {
    if (j<0 || j>=_n)
      Check.argument(0<=j && j<_n,"column index j="+j+" is in bounds");
  }

  private void checkI(int i0, int i1) {
    checkI(i0);  checkI(i1);  Check.argument(i0<=i1,"i0<=i1");
  }

  private void checkJ(int j0, int j1) {
    checkJ(j0);  checkJ(j1);  Check.argument(j0<=j1,"j0<=j1");
  }

  // Used in implementation of toString() above.
  private static String[][] format(int m, int n, double[] d) {
    int pg = 6;
    String fg = "% ."+pg+"g";
    int pemax = -1;
    int pfmax = -1;
    for (int i=0; i<m; ++i) {
      for (int j=0; j<n; ++j) {
        String s = String.format(fg,d[i+j*m]);
        s = clean(s);
        int ls = s.length();
        if (s.contains("e")) {
          int pe = (ls>7)?ls-7:0;
          if (pemax<pe)
            pemax = pe;
        } else {
          int ip = s.indexOf('.');
          int pf = (ip>=0)?ls-1-ip:0;
          if (pfmax<pf)
            pfmax = pf;
        }
      }
    }
    String[][] s = new String[m][n];
    String f;
    if (pemax>=0) {
      if (pfmax>pg-1)
        pfmax = pg-1;
      int pe = (pemax>pfmax)?pemax:pfmax;
      f = "% ."+pe+"e";
    } else {
      int pf = pfmax;
      f = "% ."+pf+"f";
    }
    for (int i=0; i<m; ++i) {
      for (int j=0; j<n; ++j) {
        s[i][j] = String.format(f,d[i+j*m]);
      }
    }
    return s;
  }
  private static String clean(String s) {
    int len = s.length();
    int iend = s.indexOf('e');
    if (iend<0)
      iend = s.indexOf('E');
    if (iend<0)
      iend = len;
    int ibeg = iend;
    if (s.indexOf('.')>0) {
      while (ibeg>0 && s.charAt(ibeg-1)=='0')
        --ibeg;
      if (ibeg>0 && s.charAt(ibeg-1)=='.')
        --ibeg;
    }
    if (ibeg<iend) {
      String sb = s.substring(0,ibeg);
      s = (iend<len)?sb+s.substring(iend,len):sb;
    }
    return s;
  }
  private static int maxlen(String[][] s) {
    int max = 0;
    int m = s.length;
    int n = s[0].length;
    for (int i=0; i<m; ++i) {
      for (int j=0; j<n; ++j) {
        int len = s[i][j].length();
        if (max<len) 
          max = len;
      }
    }
    return max;
  }
}
