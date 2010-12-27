/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import org.netlib.lapack.LAPACK;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Singular value decomposition of a matrix A.
 * For an m-by-n matrix A, let mn = min(m,n). Then the singular value
 * decomposition is A = U*S*V', where U is an m-by-mn orthogonal matrix,
 * S is an mn-by-mn diagonal matrix of singular values, and V' is an 
 * mn-by-n orthogonal matrix. The columns of U are the left singular
 * vectors and the rows of V' (V transpose) are the right singular
 * vectors.
 * <p>
 * The singular values s[k] = S(k,k) are in decreasing order, such
 * that s[0] &gt;= s[1] &gt;= ... &gt;= s[mn-1].
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.15
 */
public class DMatrixSvd {

  /** 
   * Constructs a singular value decomposition for the specified matrix A.
   * @param a the matrix A.
   */
  public DMatrixSvd(DMatrix a) {
    double[] aa = a.getPackedColumns();
    _m = a.getM();
    _n = a.getN();
    _mn = min(_m,_n);
    _s = new double[_mn];
    _u = new double[_m*_mn];
    _vt = new double[_mn*_n];
    double[] work = new double[1];
    LapackInfo li = new LapackInfo();
    _lapack.dgesvd("S","S",_m,_n,aa,_m,_s,_u,_m,_vt,_mn,work,-1,li);
    li.check("dgesvd");
    int lwork = (int)work[0];
    work = new double[lwork];
    _lapack.dgesvd("S","S",_m,_n,aa,_m,_s,_u,_m,_vt,_mn,work,lwork,li);
    li.check("dgesvd");
  }

  /**
   * Gets the matrix U of left singular vectors.
   * @return the matrix U.
   */
  public DMatrix getU() {
    return new DMatrix(_m,_mn,copy(_u));
  }

  /**
   * Gets the diagonal matrix S of singular values.
   * @return the matrix S.
   */
  public DMatrix getS() {
    return DMatrix.diagonal(_s);
  }

  /**
   * Gets the array s of singular values.
   * @return the array s.
   */
  public double[] getSingularValues() {
    return copy(_s);
  }

  /**
   * Gets the matrix V of right singular vectors.
   * The right singular vectors are in the columns of V.
   * @return the matrix V.
   */
  public DMatrix getV() {
    return new DMatrix(_mn,_n,_vt).transpose();
  }

  /**
   * Gets the matrix V' (V transposed) of right singular vectors.
   * The right singular vectors are in the rows of V'.
   * @return the matrix V'.
   */
  public DMatrix getVTranspose() {
    return new DMatrix(_mn,_n,copy(_vt));
  }

  /**
   * Returns the two-norm of the matrix A.
   * @return the two-norm.
   */
  public double norm2() {
    return _s[0];
  }

  /**
   * Returns the condition number of the matrix A.
   * @return the condition number.
   */
  public double cond() {
    return _s[0]/_s[_mn-1];
  }

  /**
   * Returns the effective numerical rank of the matrix A.
   * The effective numerical rank is the number of significant
   * singular values.
   * @return the rank. 
   */
  public int rank() {
    double eps = ulp(1.0);
    double tol = max(_m,_n)*_s[0]*eps;
    int r = 0;
    for (int i=0; i<_mn; ++i) {
      if (_s[i]>tol)
        ++r;
    }
    return r;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final LAPACK _lapack = LAPACK.getInstance();

  int _m; // number of rows
  int _n; // number of columns
  int _mn; // min(_m,_n)
  double[] _s; // array of singular values
  double[] _u; // left singular vectors (in columns)
  double[] _vt; // right singular vectors (in rows)
}
