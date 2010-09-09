/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import static edu.mines.jtk.lapack.Blas.LOWER;
import static edu.mines.jtk.lapack.Lapack.*;
import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * Eigenvalue and eigenvector decomposition of a square matrix A.
 * <p>
 * If A is symmetric, then A = V*D*V' where the matrix of eigenvalues D
 * is diagonal and the matrix of eigenvectors V is orthogonal (V*V' = I).
 * <p>
 * If A is not symmetric, then the eigenvalue matrix D is block diagonal
 * with real eigenvalues in 1-by-1 blocks and any complex eigenvalues
 * lambda + i*mu in 2-by-2 block [lambda, mu; -mu, lambda]. The columns
 * of V represent the eigenvectors in the sense that A*V = V*D. The matrix
 * V may be badly conditioned or even singular, so the validity of the
 * equation A = V*D*inverse(V) depends on the condition number of V.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.16
 */
public class DMatrixEvd {

  /** 
   * Constructs an eigenvalue decomposition for the specified square matrix.
   * @param a the square matrix
   */
  public DMatrixEvd(DMatrix a) {
    Check.argument(a.isSquare(),"A is square");
    _n = a.getN();
    _v = new double[_n*_n];
    _d = new double[_n];
    _e = new double[_n];
    double[] aa = a.getPackedColumns();
    if (a.isSymmetric()) {
      int[] m = new int[1]; // not used (m[0] will equal _n)
      int[] isuppz = new int[2*_n]; // not used
      double[] work = new double[1];
      int[] iwork = new int[1];
      int info = dsyevr(JOB_V,RANGE_A,LOWER,
        _n,aa,_n,0.0,0.0,0,0,0.0,m,_d,_v,_n,isuppz,work,-1,iwork,-1);
      if (info>0)
        throw new RuntimeException("internal error in LAPACK dsyevr");
      int lwork = (int)work[0];
      work = new double[lwork];
      int liwork = iwork[0];
      iwork = new int[liwork];
      info = dsyevr(JOB_V,RANGE_A,LOWER,
        _n,aa,_n,0.0,0.0,0,0,0.0,m,_d,_v,_n,isuppz,work,lwork,iwork,liwork);
      if (info>0)
        throw new RuntimeException("internal error in LAPACK dsyevr");
    } else {
      double[] work = new double[1];
      dgeev(JOB_N,JOB_V,_n,aa,_n,_d,_e,_v,_n,_v,_n,work,-1);
      int lwork = (int)work[0];
      work = new double[lwork];
      int info = dgeev(JOB_N,JOB_V,_n,aa,_n,_d,_e,_v,_n,_v,_n,work,lwork);
      if (info>0)
        throw new RuntimeException("LAPACK dgeev failed to converge");
    }
  }

  /** 
   * Gets the matrix of eigenvectors V.
   * @return the matrix V.
   */
  public DMatrix getV() {
    return new DMatrix(_n,_n,_v);
  }

  /** 
   * Gets the block diagonal matrix of eigenvalues D.
   * @return the matrix D.
   */
  public DMatrix getD() {
    double[] d = new double[_n*_n];
    for (int i=0; i<_n; ++i) {
      for (int j=0; j<_n; ++j) {
        d[i+j*_n] = 0.0;
      }
      d[i+i*_n] = _d[i];
      if (_e[i]>0.0) {
        d[i+(i+1)*_n] = _e[i];
      } else if (_e[i]<0.0) {
        d[i+(i-1)*_n] = _e[i];
      }
    }
    return new DMatrix(_n,_n,d);
  }

  /** 
   * Gets the real parts of the eigenvalues.
   * @return array of real parts = real(diag(D)).
   */
  public double[] getRealEigenvalues() {
    return copy(_d);
  }

  /** 
   * Gets the imaginary parts of the eigenvalues
   * @return array of imaginary parts = imag(diag(D))
   */
  public double[] getImagEigenvalues() {
    return copy(_e);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n; // row and column dimensions for square matrix V
  private double[] _v; // eigenvectors V
  private double[] _d; // eigenvalues (real parts)
  private double[] _e; // eigenvalues (imag parts)
}
