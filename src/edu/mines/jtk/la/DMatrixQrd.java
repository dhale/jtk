/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la;

import static java.lang.Math.hypot;

import edu.mines.jtk.util.Check;

/**
 * QR decomposition of a matrix A. 
 * For an m-by-n matrix A, with m&gt;=n, the QR decomposition is A = Q*R, 
 * where Q is an m-by-n orthogonal matrix, and R is an n-by-n upper-triangular 
 * matrix.
 * <p>
 * The QR decomposition is constructed even if the matrix A is rank
 * deficient. However, the primary use of the QR decomposition is for 
 * least-squares solutions of non-square systems of linear equations, 
 * and such solutions are feasible only if the matrix A is of full rank.
 * <p>
 * This class was adapted from the package Jama, which was developed by 
 * Joe Hicklin, Cleve Moler, and Peter Webb of The MathWorks, Inc., and by
 * Ronald Boisvert, Bruce Miller, Roldan Pozo, and Karin Remington of the
 * National Institue of Standards and Technology.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.01
 */
public class DMatrixQrd {

  /** 
   * Constructs an QR decomposition for the specified matrix A.
   * The matrix A must not have more columns than rows. 
   * If A is m-by-n, then, m&gt;=n is required.
   * @param a the matrix A.
   */
  public DMatrixQrd(DMatrix a) {
    Check.argument(a.getM()>=a.getN(),"m >= n");
    int m = _m = a.getM();
    int n = _n = a.getN();
    _qr = a.get();
    _rdiag = new double[_n];

    // Main loop.
    for (int k=0; k<n; ++k) {

      // Compute 2-norm of k-th column without under/overflow.
      double nrm = 0;
      for (int i=k; i<m; ++i)
        nrm = hypot(nrm,_qr[i][k]);

      if (nrm!=0.0) {

        // Form k-th Householder vector.
        if (_qr[k][k]<0.0)
          nrm = -nrm;
        for (int i=k; i<m; ++i)
          _qr[i][k] /= nrm;
        _qr[k][k] += 1.0;

        // Apply transformation to remaining columns.
        for (int j=k+1; j<n; ++j) {
          double s = 0.0; 
          for (int i=k; i<m; ++i)
            s += _qr[i][k]*_qr[i][j];
          s = -s/_qr[k][k];
          for (int i=k; i<m; ++i)
            _qr[i][j] += s*_qr[i][k];
        }
      }
      _rdiag[k] = -nrm;
    }
  }

  /**
   * Determines whether the matrix A = Q*R is of full rank.
   * @return true, if full rank; false, otherwise.
   */
  public boolean isFullRank() {
    for (int j=0; j<_n; ++j) {
      if (_rdiag[j]==0.0)
        return false;
    }
    return true;
  }

  /** 
   * Gets the m-by-n matrix factor Q.
   * @return the m-by-n matrix factor Q.
   */
  public DMatrix getQ() {
    double[][] q = new double[_m][_n];
    for (int k=_n-1; k>=0; --k) {
      for (int i=0; i<_m; ++i) {
        q[i][k] = 0.0;
      }
      q[k][k] = 1.0;
      for (int j=k; j<_n; ++j) {
        if (_qr[k][k]!=0.0) {
          double s = 0.0;
          for (int i=k; i<_m; ++i) {
            s += _qr[i][k]*q[i][j];
          }
          s = -s/_qr[k][k];
          for (int i=k; i<_m; ++i) {
            q[i][j] += s*_qr[i][k];
          }
        }
      }
    }
    return new DMatrix(_m,_n,q);
  }

  /** 
   * Gets the n-by-n upper triangular matrix factor R.
   * @return the n-by-n matrix factor R.
   */
  public DMatrix getR() {
    double[][] r = new double[_n][_n];
    for (int i=0; i<_n; ++i) {
      r[i][i] = _rdiag[i];
      for (int j=i+1; j<_n; ++j) {
        r[i][j] = _qr[i][j];
      }
    }
    return new DMatrix(_n,_n,r);
  }

  /**
   * Returns the least-squares solution X of the system A*X = B.
   * This solution exists only if the matrix A is of full rank.
   * @param b a matrix of right-hand-side vectors. This matrix must
   *  have the same number (m) of rows as the matrix A, but may have
   *  any number of columns.
   * @return the matrix X that minimizes the two-norm of A*X-B.
   * @exception IllegalStateException if A is rank-deficient.
   */
  public DMatrix solve(DMatrix b) {
    Check.argument(b.getM()==_m,"A and B have the same number of rows");
    Check.state(this.isFullRank(),"A is of full rank");
      
    // Copy the right hand side.
    int nx = b.getN();
    double[][] x = b.get();

    // Compute Y = transpose(Q)*B.
    for (int k=0; k<_n; ++k) {
      for (int j=0; j<nx; ++j) {
        double s = 0.0; 
        for (int i=k; i<_m; ++i) {
          s += _qr[i][k]*x[i][j];
        }
        s = -s/_qr[k][k];
        for (int i=k; i<_m; ++i) {
          x[i][j] += s*_qr[i][k];
        }
      }
    }

    // Solve R*X = Y.
    for (int k=_n-1; k>=0; --k) {
      for (int j=0; j<nx; ++j)
        x[k][j] /= _rdiag[k];
      for (int i=0; i<k; ++i) {
        for (int j=0; j<nx; ++j) {
          x[i][j] -= x[k][j]*_qr[i][k];
        }
      }
    }
    return new DMatrix(_m,nx,x).get(0,_n-1,null);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  int _m,_n;
  double[][] _qr;
  double[] _rdiag;
}
