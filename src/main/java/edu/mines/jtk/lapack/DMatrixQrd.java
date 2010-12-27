/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import org.netlib.blas.BLAS;
import org.netlib.lapack.LAPACK;
import static edu.mines.jtk.util.ArrayMath.*;
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
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.14
 */
public class DMatrixQrd {

  /** 
   * Constructs a QR decomposition for the specified matrix A.
   * @param a the m-by-n matrix A with m&gt;=n.
   */
  public DMatrixQrd(DMatrix a) {
    Check.argument(a.getM()>=a.getN(),"m >= n");
    _m = a.getM();
    _n = a.getN();
    _k = min(_m,_n); // same as n, but might not be if we allow m<n
    _qr = a.getPackedColumns();
    _tau = new double[_k];
    _work = new double[1];
    LapackInfo li = new LapackInfo();
    _lapack.dgeqrf(_m,_n,_qr,_m,_tau,_work,-1,li);
    li.check("dgeqrf");
    _lwork = (int)_work[0];
    _work = new double[_lwork];
    _lapack.dgeqrf(_m,_n,_qr,_m,_tau,_work,_lwork,li);
    li.check("dgeqrf");
  }

  /**
   * Determines whether the matrix A = Q*R is of full rank.
   * @return true, if full rank; false, otherwise.
   */
  public boolean isFullRank() {
    for (int j=0; j<_n; ++j) {
      if (_qr[j+j*_m]==0.0)
        return false;
    }
    return true;
  }

  /** 
   * Gets the m-by-n matrix factor Q.
   * @return the m-by-n matrix factor Q.
   */
  public DMatrix getQ() {
    double[] q = copy(_qr);
    LapackInfo li = new LapackInfo();
    _lapack.dorgqr(_m,_n,_k,q,_m,_tau,_work,_lwork,li);
    li.check("dorgqr");
    return new DMatrix(_m,_n,q);
  }

  /** 
   * Gets the upper triangular n-by-n matrix factor R.
   * @return the n-by-n matrix factor R.
   */
  public DMatrix getR() {
    double[] r = new double[_n*_n];
    for (int j=0; j<_n; ++j)
      for (int i=0; i<=j; ++i)
        r[i+j*_n] = _qr[i+j*_m];
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

    // Compute C = Q'*B. Q' is n-by-m, B is m-by-nrhs, and C is m-by-nrhs.
    // The extra n-m rows in C are necessary here because C overwrites B.
    int nrhs = b.getN();
    DMatrix c = new DMatrix(b);
    double[] ca = c.getArray();
    double[] work = new double[1];
    LapackInfo li = new LapackInfo();
    _lapack.dormqr("L","T",_m,nrhs,_k,_qr,_m,_tau,ca,_m,work,-1,li);
    li.check("dormqr");
    int lwork = (int)work[0];
    work = new double[lwork];
    _lapack.dormqr("L","T",_m,nrhs,_k,_qr,_m,_tau,ca,_m,work,lwork,li);
    li.check("dormqr");

    // Solve R*X = C.  R is n-by-n, X is n-by-nrhs, and C is m-by-nrhs.
    _blas.dtrsm("L","U","N","N",_n,nrhs,1.0,_qr,_m,ca,_m);

    // Discard the extra n-m rows in X.
    DMatrix x = c.get(0,_n-1,null);
    return x;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final BLAS _blas = BLAS.getInstance();
  private static final LAPACK _lapack = LAPACK.getInstance();

  int _m; // number of rows
  int _n; // number of columns
  int _k; // min(m,n) = number of elementary reflectors
  double[] _qr; // m-by-n matrix that represents the decomposition
  double[] _tau; // array of k scale factors of the elementary reflectors
  double[] _work; // work array
  int _lwork; // size of work array
}
