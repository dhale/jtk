/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

/**
 * Wrappers for LAPACK (Linear Algebra PACKage).
 * For internal use only.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.12
 */
class Lapack {

  ///////////////////////////////////////////////////////////////////////////
  // LU decomposition

  static native int dgetrf(
    int m, int n, double[] a, int lda, int[] ipiv);

  static native int dgetrs(
    int trans, int n, int nrhs, 
    double[] a, int lda, int[] ipiv, 
    double[] b, int ldb);

  ///////////////////////////////////////////////////////////////////////////
  // Cholesky decomposition

  static native int dpotrf(
    int uplo, int n, double[] a, int lda);

  static native int dpotrs(
    int uplo, int n, int nrhs, 
    double[] a, int lda,
    double[] b, int ldb);

  ///////////////////////////////////////////////////////////////////////////
  // QR decomposition

  static native int dgeqrf(
    int m, int n, double[] a, int lda, 
    double[] tau, double[] work, int lwork);

  static native int dorgqr(
    int m, int n, int k, double[] a, int lda, 
    double[] tau, double[] work, int lwork);

  static native int dormqr(
    int side, int trans, int m, int n, int k, 
    double[] a, int lda, double[] tau, double[] c, int ldc, 
    double[] work, int lwork);

  static native int dtrtrs(
    int uplo, int trans, int diag, 
    int n, int nrhs, double[] a, int lda, double[] b, int ldb);

  ///////////////////////////////////////////////////////////////////////////
  // Singular value decomposition

  static final int JOB_A = 201;
  static final int JOB_S = 202;
  static final int JOB_O = 203;
  static final int JOB_N = 204;

  static native int dgesvd(
    int jobu, int jobvt, int m, int n, double[] a, int lda, 
    double[] s, double[] u, int ldu, double[] vt, int ldvt, 
    double[] work, int lwork);

  ///////////////////////////////////////////////////////////////////////////
  // Eigenvalue decomposition

  static final int JOB_V = 205;
  static final int RANGE_A = 301;
  static final int RANGE_V = 302;
  static final int RANGE_I = 303;

  static native int dsyevr(
    int jobz, int range, int uplo, 
    int n, double[] a, int lda, double vl, double vu, int il, int iu, 
    double abstol, int[] m, double[] w, double[] z, int ldz, int[] isuppz, 
    double[] work, int lwork, int[] iwork, int liwork);

  static native int dgeev(
    int jobvl, int jobvr, 
    int n, double[] a, int lda, double[] wr, double[] wi, 
    double[] vl, int ldvl, double[] vr, int ldvr, 
    double[] work, int lwork);

  static {
    System.loadLibrary("edu_mines_jtk_lapack");
  }
}
