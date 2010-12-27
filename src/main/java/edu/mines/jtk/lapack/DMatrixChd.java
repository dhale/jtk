/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import org.netlib.lapack.LAPACK;
import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * Cholesky decomposition of a symmetric positive-definite matrix A.
 * For a symmetric positive-definite matrix a, the Cholesky decomposition
 * is A = L*L', where L is a lower triangular matrix.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.12
 */
public class DMatrixChd {

  /**
   * Constructs a Cholesky decomposition of the specified matrix A.
   * The matrix A must be symmetric. For efficiency, this condition
   * is assumed and not checked. That is, only the lower triangular
   * part of A is used to perform the decomposition.
   * @param a the matrix.
   */
  public DMatrixChd(DMatrix a) {
    Check.argument(a.isSquare(),"A is square");
    _n = a.getN();
    _l = a.getPackedColumns();

    // Zero elements above lower triangle.
    for (int j=0; j<_n; ++j) {
      for (int i=0; i<j; ++i) {
        _l[i+j*_n] = 0.0;
      }
    }

    // Decompose.
    LapackInfo li = new LapackInfo();
    _lapack.dpotrf("L",_n,_l,_n,li);
    int info = li.get("dpotrf");

    _pd = info==0;

    _det = 1.0;
    for (int i=0; i<_n; ++i)
      _det *= _l[i+i*_n];
    _det = _det*_det;
  }

  /**
   * Determines whether the matrix A is positive definite. (The matrix
   * A was assumed to be symmetric when this decomposition was constructed.)
   * If not symmetric and positive-definite, then this decomposition cannot
   * be used to solve systems of linear equations.
   * @return true, if positive-definite; false, otherwise.
   */
  public boolean isPositiveDefinite() {
    return _pd;
  }

  /**
   * Gets the lower triangular factor L.
   * @return the factor L.
   */
  public DMatrix getL() {
    return new DMatrix(_n,_n,copy(_l));
  }

  /**
   * Returns the determinant of the matrix A.
   * @return the determinant.
   */
  public double det() {
    return _det;
  }

  /**
   * Returns the solution X of the linear system A*X = B.
   * The matrix A must be symmetric and positive-definite.
   * Also, the matrices A and B must have the same number of rows.
   * @param b the right-hand-side matrix B.
   * @return the solution matrix X.
   */
  public DMatrix solve(DMatrix b) {
    Check.argument(_n==b.getM(),"A and B have same number of rows");
    Check.state(_pd,"A is positive-definite");
    int n = _n;
    int nrhs = b.getN();
    double[] aa = _l;
    int lda = _n;
    double[] ba = b.getPackedColumns();
    int ldb = _n;
    LapackInfo li = new LapackInfo();
    _lapack.dpotrs("L",n,nrhs,aa,lda,ba,ldb,li);
    li.check("dpotrs");
    return new DMatrix(_n,nrhs,ba);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final LAPACK _lapack = LAPACK.getInstance();

  private int _n; // number of rows equals number of columns
  private double[] _l; // factor L
  private double _det; // determinant
  private boolean _pd; // true, if A is positive-definite
}
