/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

/**
 * Wrappers for BLAS (Basic Linear Algebra Subroutines).
 * For internal use only.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.11
 */
class Blas {

  static final int ROW_MAJOR = 101; // for cblas only, so do not use!
  static final int COL_MAJOR = 102; // for cblas only, so do not use!

  static final int NO_TRANS = 111;
  static final int TRANS = 112;
  static final int CONJ_TRANS = 113;

  static final int UPPER = 121;
  static final int LOWER = 122;

  static final int NON_UNIT = 131;
  static final int UNIT = 132;

  static final int LEFT = 141;
  static final int RIGHT = 142;

  static native void dgemm(
    int order, int transa, int transb, int m, int n, int k,
    double alpha, double[] a, int lda, double[] b, int ldb,
    double beta, double[] c, int ldc);

  static native void dtrsm(
    int order, int side, int uplo, int trans, int diag, int m, int n, 
    double alpha, double[] a, int lda, double[] b, int ldb);

  static {
    System.loadLibrary("edu_mines_jtk_lapack");
  }
}
