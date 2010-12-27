/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import static java.lang.Math.min;

import org.netlib.lapack.LAPACK;
import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * LU decomposition of a matrix A.
 * For an m-by-n matrix A, the LU decomposition is A = P*L*U or A(p,:) = 
 * L*U, where P is an m-by-m row permutation matrix, p is a corresponding 
 * array of m row permutation indices, L is an m-by-min(m,n) lower 
 * triangular or trapezoidal matrix with unit diagonal elements, and 
 * U is a min(m,n)-by-n upper triangular or trapezoidal matrix.
 * <p>
 * The LU decomposition with pivoting never fails, even if the matrix 
 * A is singular. However, the primary use of LU decomposition is in the
 * solution of square systems of linear equations, which will fail if A 
 * is singular (or not square).
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.12
 */
public class DMatrixLud {

  /**
   * Constructs an LU decomposition of the specified matrix A.
   * @param a the matrix.
   */
  public DMatrixLud(DMatrix a) {
    _m = a.getM();
    _n = a.getN();
    _lu = a.getPackedColumns();
    _npiv = min(_m,_n);
    _ipiv = new int[_npiv];
    LapackInfo li = new LapackInfo();
    _lapack.dgetrf(_m,_n,_lu,_m,_ipiv,li);
    int info = li.get("dgetrf");
    _p = new int[_m];
    for (int i=0; i<_m; ++i)
      _p[i] = i;
    _det = 1.0;
    for (int i=0; i<_m; ++i) {
      if (i<_npiv) {
        int j = _ipiv[i]-1;
        _det *= _lu[i+i*_m];
        if (j!=i) {
          int pi = _p[i];
          _p[i] = _p[j];
          _p[j] = pi;
          _det = -_det;
        }
      }
    }
    _singular = info>0;
  }

  /**
   * Determines whether the matrix A is singular. If singular, then this 
   * decomposition cannot be used to solve systems of linear equations.
   * @return true, if singular; false, otherwise.
   */
  public boolean isSingular() {
    return _singular;
  }

  /**
   * Gets the lower triangular (or lower trapezoidal) factor L.
   * The matrix L has dimensions m-by-min(m,n) and unit diagonal elements.
   * @return the factor L.
   */
  public DMatrix getL() {
    int m = _m;
    int n = min(_m,_n);
    double[] l = new double[m*n];
    for (int j=0; j<n; ++j) {
      l[j+j*m] = 1.0;
      for (int i=j+1; i<m; ++i) {
        l[i+j*m] = _lu[i+j*_m];
      }
    }
    return new DMatrix(m,n,l);
  }

  /**
   * Gets the upper triangular (or upper trapezoidal) factor U.
   * The matrix U has dimensions min(m,n)-by-n.
   * @return the factor L.
   */
  public DMatrix getU() {
    int m = min(_m,_n);
    int n = _n;
    double[] u = new double[m*n];
    for (int j=0; j<n; ++j) {
      int imax = min(m-1,j);
      for (int i=0; i<=imax; ++i) {
        u[i+j*m] = _lu[i+j*_m];
      }
    }
    return new DMatrix(m,n,u);
  }

  /**
   * Gets the row permutation matrix P. 
   * The matrix P has dimensions m-by-m.
   * @return the permutation matrix P.
   */
  public DMatrix getP() {
    int m = _m;
    int n = _m;
    double[] p = new double[m*n];
    for (int i=0; i<m; ++i) {
      p[_p[i]+i*m] = 1.0;
    }
    return new DMatrix(m,n,p);
  }

  /**
   * Gets the array of row permutation (pivot) indices p. In this 
   * decomposition of the m-by-n matrix A, row i was interchanged 
   * with row p[i], for i = 0, 1, 2, ..., m-1.
   * @return the pivot indices p.
   */
  public int[] getPivotIndices() {
    return copy(_p);
  }

  /**
   * Returns the determinant of the square matrix A.
   * The determinant exists only for square matrices A.
   * @return the determinant.
   */
  public double det() {
    Check.argument(_m==_n,"A is square");
    return _det;
  }

  /**
   * Returns the solution X of the linear system A*X = B.
   * The matrix A must be square and non singular.
   * Also, the matrices A and B must have the same number of rows.
   * @param b the right-hand-side matrix B.
   * @return the solution matrix X.
   */
  public DMatrix solve(DMatrix b) {
    Check.argument(_m==_n,"A is square");
    Check.argument(_m==b.getM(),"A and B have same number of rows");
    Check.state(!_singular,"A is not singular");
    int n = _n;
    int nrhs = b.getN();
    double[] aa = _lu;
    int lda = _m;
    int[] ipiv = _ipiv;
    double[] ba = b.getPackedColumns();
    int ldb = _m;
    LapackInfo li = new LapackInfo();
    _lapack.dgetrs("N",n,nrhs,aa,lda,ipiv,ba,ldb,li);
    li.check("dgetrs");
    return new DMatrix(_m,nrhs,ba);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final LAPACK _lapack = LAPACK.getInstance();

  private int _m; // number of rows
  private int _n; // number of columns
  private double[] _lu; // factors L and U
  private int _npiv; // _ipiv.length = min(_m,_n)
  private int[] _ipiv; // one-based pivot indices returned by dgetrf
  private int[] _p; // zero-based pivot indices
  private double _det; // determinant
  private boolean _singular; // true, if A is singular
}
