/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import edu.mines.jtk.util.Check;

/**
 * LU decomposition (with pivoting) of a matrix A. 
 * For an m-by-n matrix A, with m&gt;=n, the LU decomposition is 
 * A(piv,:) = L*U, where L is an m-by-n unit lower triangular matrix, 
 * U is an n-by-n upper-triangular matrix, and piv is a permutation
 * vector of length m.
 * <p>
 * The LU decomposition with pivoting always exists, even for singular
 * matrices A. The primary use of LU decomposition is in the solution of
 * square systems of simultaneous linear equations. These solutions will
 * fila if the matrix A is singular.
 * <p>
 * This class was adapted from the package Jama, which was developed by 
 * Joe Hicklin, Cleve Moler, and Peter Webb of The MathWorks, Inc., and by
 * Ronald Boisvert, Bruce Miller, Roldan Pozo, and Karin Remington of the
 * National Institue of Standards and Technology.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.09.15
 */
public class DMatrixLud {

  /** 
   * Constructs an LU decomposition for the specified matrix A.
   * @param a the matrix A.
   */
  public DMatrixLud(DMatrix a) {
    int m = _m = a.getM();
    int n = _n = a.getN();
    double[][] lu = _lu = a.get();
    _piv = new int[m];
    for (int i=0; i<m; ++i)
      _piv[i] = i;
    _pivsign = 1;
    double[] lurowi;
    double[] lucolj = new double[m];

    // A left-looking, dot-product, Crout/Doolittle algorithm.
    for (int j=0; j<n; ++j) {

      // Copy the j'th column to reduce cost in inner dot-product loop.
      for (int i=0; i<m; ++i)
        lucolj[i] = lu[i][j];

      // Apply previous transformations
      for (int i=0; i<m; ++i) {
        lurowi = lu[i];

        // Dot product.
        int kmax = min(i,j);
        double s = 0.0;
        for (int k=0; k<kmax; ++k)
          s += lurowi[k]*lucolj[k];
        lurowi[j] = lucolj[i] -= s;
      }

      // Find pivot and exchange if necessary.
      int p = j;
      for (int i=j+1; i<m; ++i) {
        if (abs(lucolj[i])>abs(lucolj[p]))
          p = i;
      }
      if (p!=j) {
        for (int k=0; k<n; ++k) {
          double t = lu[p][k];
          lu[p][k] = lu[j][k];
          lu[j][k] = t;
        }
        int k = _piv[p];
        _piv[p] = _piv[j];
        _piv[j] = k;
        _pivsign = -_pivsign;
      }

      // Compute multipliers.
      if (j<m && lu[j][j]!=0.0) {
        for (int i=j+1; i<m; ++i)
          lu[i][j] /= lu[j][j];
      }
    }
  }

  /**
   * Determines whether the matrix A is non-singular.
   * @return true, if non-singular; false, otherwise.
   */
  public boolean isNonSingular() {
    for (int j=0; j<_n; ++j) {
      if (_lu[j][j]==0.0)
        return false;
    }
    return true;
  }

  /**
   * Determines whether the matrix A is singular.
   * @return true, if singular; false, otherwise.
   */
  public boolean isSingular() {
    return !isNonSingular();
  }

  /** 
   * Gets the m-by-n unit lower triangular matrix factor L.
   * @return the m-by-n factor L.
   */
  public DMatrix getL() {
    double[][] l = new double[_m][_n];
    for (int i=0; i<_m; ++i) {
      for (int j=0; j<_n; ++j) {
        if (i>j) {
          l[i][j] = _lu[i][j];
        } else if (i==j) {
          l[i][j] = 1.0;
        } else {
          l[i][j] = 0.0;
        }
      }
    }
    return new DMatrix(_m,_n,l);
  }

  /** 
   * Gets the n-by-n upper triangular matrix factor U.
   * @return the n-by-n matrix factor U.
   */
  public DMatrix getU() {
    double[][] u = new double[_n][_n];
    for (int i=0; i<_n; ++i) {
      for (int j=0; j<_n; ++j) {
        if (i<=j) {
          u[i][j] = _lu[i][j];
        } else {
          u[i][j] = 0.0;
        }
      }
    }
    return new DMatrix(_n,_n,u);
  }

  /**
   * Gets the pivot vector, an array of length m.
   * @return the pivot vector.
   */
  public int[] getPivot() {
    int[] p = new int[_m];
    for (int i=0; i<_m; ++i)
      p[i] = _piv[i];
    return p;
  }

  /**
   * Returns the solution X of the system A*X = B.
   * This solution exists only if the matrix A is non-singular.
   * @param b a matrix of right-hand-side vectors. This matrix must
   *  have the same number (m) of rows as the matrix A, but may have
   *  any number of columns.
   * @return the matrix solution X.
   * @exception IllegalStateException if A is singular.
   */
  public DMatrix solve(DMatrix b) {
    Check.argument(b.getM()==_m,"A and B have the same number of rows");
    Check.state(this.isNonSingular(),"A is non-singular");

    // Copy of right-hand side with pivoting.
    int nx = b.getN();
    DMatrix xx = b.get(_piv,0,nx-1);
    double[][] x = xx.getArray();

    // Solve L*Y = B(piv,:).
    for (int k=0; k<_n; ++k) {
      for (int i=k+1; i<_n; ++i) {
        for (int j=0; j<nx; ++j) {
          x[i][j] -= x[k][j]*_lu[i][k];
        }
      }
    }

    // Solve U*X = Y.
    for (int k=_n-1; k>=0; --k) {
      for (int j=0; j<nx; ++j) {
        x[k][j] /= _lu[k][k];
      }
      for (int i=0; i<k; ++i) {
        for (int j=0; j<nx; ++j) {
          x[i][j] -= x[k][j]*_lu[i][k];
        }
      }
    }
    return xx;
  }

  /**
   * Returns the determinant of the  matrix A.
   * @return the the determinant.
   * @exception IllegalStateException if A is not square.
   */
  public double det() {
    Check.state(_m==_n,"A is square");
    double d = _pivsign;
    for (int j=0; j<_n; ++j)
      d *= _lu[j][j];
    return d;
  }


  ///////////////////////////////////////////////////////////////////////////
  // private

  int _m,_n;
  double[][] _lu;
  int[] _piv;
  int _pivsign;
}
