/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Array;

/**
 * An array of eigen-decompositions of tensors for 2D image processing.
 * Each tensor is a symmetric positive-semidefinite 2-by-2 matrix:
 * <pre><code>
 * A = |a11 a12|
 *     |a12 a22|
 * </code></pre>
 * Such tensors can be used to parameterize anisotropic image processing.
 * <p>
 * The eigen-decomposition of the matrix A is
 * <pre><code>
 * A = au*u*u' + av*v*v'
 *   = (au-av)*u*u' + av*I
 * </code></pre>
 * where u and v are orthogonal unit eigenvectors of A. (The notation u' 
 * denotes the transpose of u.) The outer products of eigenvectors are
 * scaled by the non-negative eigenvalues au and av. The second equation 
 * exploits the identity u*u' + v*v' = I, and makes apparent the redundancy 
 * of the vector v.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.06.09
 */
public class EigenTensors2 implements Tensors2 {

  /**
   * Constructs tensors for specified array dimensions. All eigenvalues 
   * and eigenvectors are not set and are initially zero.
   * @param n1 number of tensors in 1st dimension.
   * @param n2 number of tensors in 2nd dimension.
   */
  public EigenTensors2(int n1, int n2) {
    _n1 = n1;
    _n2 = n2;
    _au = new float[n2][n1];
    _av = new float[n2][n1];
    _u1 = new float[n2][n1];
    _u2 = new float[n2][n1];
  }

  /**
   * Constructs tensors for specified array dimensions and eigenvalues.
   * @param u1 array of 1st components of u.
   * @param u2 array of 2nd components of u.
   * @param au array of 1D eigenvalues.
   * @param av array of 2D eigenvalues.
   * @param compressed true, for compressed tensors; false, otherwise.
   */
  public EigenTensors2(
    float[][] u1, float[][] u2,
    float[][] au, float[][] av)
  {
    this(u1[0].length,u1.length);
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        float aui = au[i2][i1];
        float avi = av[i2][i1];
        float u1i = u1[i2][i1];
        float u2i = u2[i2][i1];
        setEigenvalues(i1,i2,aui,avi);
        setEigenvectorU(i1,i2,u1i,u2i);
      }
    }
  }

  /**
   * Gets the number of tensors in the 1st dimension.
   * @return the number of tensors in the 1st dimension.
   */
  public int getN1() {
    return _n1;
  }

  /**
   * Gets the number of tensors in the 2nd dimension.
   * @return the number of tensors in the 2nd dimension.
   */
  public int getN2() {
    return _n2;
  }

  /**
   * Gets tensor elements for specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param a array {a11,a12,a22} of tensor elements.
   */
  public void getTensor(int i1, int i2, float[] a) {
    float au = _au[i2][i1];
    float av = _av[i2][i1];
    float u1 = _u1[i2][i1];
    float u2 = _u2[i2][i1];
    au -= av;
    a[0] = au*u1*u1+av; // a11
    a[1] = au*u1*u2   ; // a12
    a[2] = au*u2*u2+av; // a22
  }

  /**
   * Gets tensor elements for specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @return a array {a11,a12,a22} of tensor elements.
   */
  public float[] getTensor(int i1, int i2) {
    float[] a = new float[3];
    getTensor(i1,i2,a);
    return a;
  }


  /**
   * Gets eigenvalues for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param a array {au,av} of eigenvalues.
   */
  public void getEigenvalues(int i1, int i2, float[] a) {
    a[0] = _au[i2][i1];
    a[1] = _av[i2][i1];
  }

  /**
   * Gets eigenvalues for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @return array {au,av} of eigenvalues.
   */
  public float[] getEigenvalues(int i1, int i2) {
    float[] a = new float[2];
    getEigenvalues(i1,i2,a);
    return a;
  }

  /**
   * Gets the eigenvector u for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param u array {u1,u2} of eigenvector components.
   */
  public void getEigenvectorU(int i1, int i2, float[] u) {
    u[0] = _u1[i2][i1];
    u[1] = _u2[i2][i1];
  }

  /**
   * Gets the eigenvector u for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @return array {u1,u2} of eigenvector components.
   */
  public float[] getEigenvectorU(int i1, int i2) {
    float[] u = new float[2];
    getEigenvectorU(i1,i2,u);
    return u;
  }

  /**
   * Gets the eigenvector v for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param v array {v1,v2} of eigenvector components.
   */
  public void getEigenvectorV(int i1, int i2, float[] v) {
    v[0] =  _u2[i2][i1];
    v[1] = -_u1[i2][i1];
  }

  /**
   * Gets the eigenvector v for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @return array {v1,v2} of eigenvector components.
   */
  public float[] getEigenvectorV(int i1, int i2) {
    float[] v = new float[2];
    getEigenvectorV(i1,i2,v);
    return v;
  }

  /**
   * Sets tensor elements for specified indices.
   * This method first computes an eigen-decomposition of the specified
   * tensor, and then stores the computed eigenvectors and eigenvalues.
   * The eigenvalues are ordered such that au &gt;= av &gt;= 0.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param a array {a11,a12,a22} of tensor elements.
   */
  public void setTensor(int i1, int i2, float[] a) {
    setTensor(i1,i2,a[0],a[1],a[2]);
  }

  /**
   * Sets tensor elements for specified indices.
   * This method first computes an eigen-decomposition of the specified
   * tensor, and then stores the computed eigenvectors and eigenvalues.
   * The eigenvalues are ordered such that au &gt;= av &gt;= 0.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param a11 tensor element a11.
   * @param a12 tensor element a12.
   * @param a22 tensor element a22.
   */
  public void setTensor(int i1, int i2, float a11, float a12, float a22) {
    float[][] aa = {
      {a11,a12},
      {a12,a22}
    };
    float[][] vv = new float[2][2];
    float[] ev = new float[2];
    Eigen.solveSymmetric22(aa,vv,ev);
    float[] u = vv[0];
    float au = ev[0]; if (au<0.0f) au = 0.0f;
    float av = ev[1]; if (av<0.0f) av = 0.0f;
    setEigenvectorU(i1,i2,u);
    setEigenvalues(i1,i2,au,av);
  }

  /**
   * Sets eigenvalues for all tensors.
   * @param au eigenvalue au.
   * @param av eigenvalue av.
   */
  public void setEigenvalues(float au, float av) {
    Array.fill(au,_au);
    Array.fill(av,_av);
  }

  /**
   * Sets eigenvalues for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param au eigenvalue au.
   * @param av eigenvalue av.
   */
  public void setEigenvalues(int i1, int i2, float au, float av) {
    _au[i2][i1] = au;
    _av[i2][i1] = av;
  }

  /**
   * Sets eigenvalues for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param a array {au,av} of eigenvalues.
   */
  public void setEigenvalues(int i1, int i2, float[] a) {
    setEigenvalues(i1,i2,a[0],a[1]);
  }

  /**
   * Sets the eigenvector u for the tensor with specified indices.
   * The specified vector is assumed to have length one.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param u1 1st component of u.
   * @param u2 2nd component of u.
   */
  public void setEigenvectorU(int i1, int i2, float u1, float u2) {
    _u1[i2][i1] = u1;
    _u2[i2][i1] = u2;
  }

  /**
   * Sets the eigenvector u for the tensor with specified indices.
   * The specified vector is assumed to have length one.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param array {u1,u2} of eigenvector components.
   */
  public void setEigenvectorU(int i1, int i2, float[] u) {
    setEigenvectorU(i1,i2,u[0],u[1]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n1,_n2;
  private float[][] _au;
  private float[][] _av;
  private float[][] _u1;
  private float[][] _u2;
}
