/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la;

import edu.mines.jtk.util.Check;
import edu.mines.jtk.util.Array;
import static java.lang.Math.*;

/**
 * QR decomposition of an m-by-n matrix A. If m&gt;=n, then the QR
 * decomposition is A = Q*R, where Q is an m-by-n orthogonal matrix,
 * and R is an n-by-n upper-triangular matrix.
 * <p>
 * The QR decomposition always exists, even if the matrix A does not
 * have full rank. Therefore, the constructor never fails. However, the 
 * primary use of the QR decomposition is for least-squares solutions 
 * of non-square systems of linear equations, and such solutions are 
 * feasible only if A has full rank.
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
   * Constructs an eigenvalue decomposition for the specified square matrix.
   * @param a the square matrix
   */
  public DMatrixQrd(DMatrix a) {
    int m = _m = a.getM();
    int n = _n = a.getN();
    double[][] qr = _qr = a.get();
    double[] rdiag = _rdiag = new double[_n];

    // Main loop.
    for (int k=0; k<n; ++k) {

      // Compute 2-norm of k-th column without under/overflow.
      double nrm = 0;
      for (int i=k; i<_m; ++i)
        nrm = hypot(nrm,_qr[i][k]);

      if (nrm!=0.0) {

        // Form k-th Householder vector.
        if (_qr[k][k]<0.0)
          nrm = -nrm;
        for (int i=k; i<_m; ++i)
          _qr[i][k] /= nrm;
        _qr[k][k] += 1.0;

        // Apply transformation to remaining columns.
        for (int j=k+1; j<_n; ++j) {
          double s = 0.0; 
          for (int i=k; i<_m; ++i)
            s += _qr[i][k]*_qr[i][j];
          s = -s/_qr[k][k];
          for (int i=k; i<_m; ++i)
            _qr[i][j] += s*_qr[i][k];
        }
      }
      _rdiag[k] = -nrm;
    }
  }

  /**
   * Determines whether the matrix A = Q*R has full rank?
   * @return true, if full rank; false, otherwise.
   */
  public boolean hasFullRank() {
    for (int j=0; j<_n; ++j) {
      if (_rdiag[j]==0.0)
        return false;
    }
    return true;
  }

  /** 
   * Gets the Householder vectors. These vectors define the reflections, the 
   * Householder transformations, used to construct this QR decomposition.
   * @return the lower trapezoidal matrix whose columns are the vectors.
  */
  public DMatrix getH() {
    DMatrix x = new DMatrix(_m,_n);
    double[][] h = x.getArray();
    for (int i=0; i<_m; ++i) {
      for (int j=0; j<_n; ++j) {
        h[i][j] = (i>=j)?_qr[i][j]:0.0;
      }
    }
    return x;
   }

  /** 
   * Gets the upper triangular matrix factor R.
   * @return the matrix factor R.
   */
  public DMatrix getR() {
    DMatrix x = new DMatrix(_n,_n);
    double[][] r = x.getArray();
    for (int i=0; i<_n; ++i) {
      for (int j=0; j<_n; ++j) {
        if (i<j) {
          r[i][j] = _qr[i][j];
        } else if (i==j) {
          r[i][j] = _rdiag[i];
        } else {
          r[i][j] = 0.0;
        }
      }
    }
    return x;
  }

  /** 
   * Gets the matrix factor Q.
   * @return the matrix factor Q.
   */
  public DMatrix getQ() {
    DMatrix x = new DMatrix(_m,_n);
    double[][] q = x.getArray();
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
    return x;
  }

  /**
   * Returns the least-squares solution X of the system A*X = B.
   * This solution is possible only if the matrix A has full rank.
   * @param b a matrix of right-hand-side vectors. This matrix must
   *  have the same number (m) of rows as the matrix A, and may have
   *  any number of columns.
   * @return the matrix X that minimizes the two-norm of A*X-B.
   * @exception IllegalStateException if this matrix is rank-deficient.
   */
  public DMatrix solve(DMatrix b) {
    Check.argument(b.getM()==_m,"A and B have the same number of rows");
    Check.state(this.hasFullRank(),"this QR decomposition has full rank");
      
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
    return new DMatrix(_n,nx,x);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  int _m,_n;
  double[][] _qr;
  double[] _rdiag;
}
