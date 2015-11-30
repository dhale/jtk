/****************************************************************************
Copyright 2008, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.ArrayMath.*;

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
   * Constructs tensors from the specified tensors.
   * @param t the tensors from which to copy eigenvectors and eigenvalues.
   */
  public EigenTensors2(EigenTensors2 t) {
    this(t._u1,t._u2,t._au,t._av);
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
   * Gets eigenvalues for all tensors.
   * @param au array of eigenvalues au.
   * @param av array of eigenvalues av.
   */
  public void getEigenvalues(float[][] au, float[][] av) {
    copy(_au,au);
    copy(_av,av);
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
    fill(au,_au);
    fill(av,_av);
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
   * Sets eigenvalues for all tensors.
   * @param au array of eigenvalues au.
   * @param av array of eigenvalues av.
   */
  public void setEigenvalues(float[][] au, float[][] av) {
    copy(au,_au);
    copy(av,_av);
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
   * @param u {u1,u2} of eigenvector components.
   */
  public void setEigenvectorU(int i1, int i2, float[] u) {
    setEigenvectorU(i1,i2,u[0],u[1]);
  }

  /**
   * Scales eigenvalues of these tensors by specified factors.
   * @param s array of scale factors.
   */
  public void scale(float[][] s) {
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        float si = s[i2][i1];
        _au[i2][i1] *= si;
        _av[i2][i1] *= si;
      }
    }
  }

  /**
   * Inverts these tensors by inverting their eigenvalues.
   * Takes no care to avoid division by zero eigenvalues.
   */
  public void invert() {
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        _au[i2][i1] = 1.0f/_au[i2][i1];
        _av[i2][i1] = 1.0f/_av[i2][i1];
      }
    }
  }

  /**
   * Inverts these tensors, assumed to be structure tensors.
   * After inversion, all eigenvalues are in the range (0,1].
   * Specifically, after inversion, 0 &lt; au &lt;= av &lt;= 1.
   * <p>
   * Before inversion, tensors are assumed to be structure tensors, 
   * for which eigenvalues au are not less than their corresponding 
   * eigenvalues av. (Any eigenvalues au for which this condition is 
   * not satisfied are set equal to the corresponding eigenvalue av.) 
   * Structure tensors can, for example, be computed using 
   * {@link LocalOrientFilter}.
   * <p>
   * Then, if any eigenvalues are equal to zero, this method adds a 
   * small fraction of the largest eigenvalue au to all eigenvalues.
   * If am is the minimum of the eigenvalues av after this perturbation,
   * then the parameter p0 is used to compute a0 = pow(am/av,p0) and
   * the parameter p1 is used to compute a1 = pow(av/au,p1). Inverted 
   * eigenvalues are then au = a0*a1 and av = a0. 
   * <p>
   * In this way, p0 emphasizes overall amplitude and p1 emphasizes 
   * linearity. For amplitude-independent tensors with all eigenvalues
   * av equal to one, set p0 = 0.0. To enhance linearity, set p1 &gt; 1.0. 
   * To simply invert (and normalize) these tensors, set p0 = p1 = 1.0.
   * @param p0 power for amplitude.
   * @param p1 power for linearity.
   */
  public void invertStructure(double p0, double p1) {
    float amax = 0.0f;
    float amin = FLT_MAX;
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        float aui = _au[i2][i1];
        float avi = _av[i2][i1];
        if (avi<0.0f) avi = 0.0f;
        if (aui< avi) aui = avi;
        if (avi<amin) amin = avi;
        if (aui>amax) amax = aui;
        _au[i2][i1] = aui;
        _av[i2][i1] = avi;
      }
    }
    //float aeps = (amin==0.0f)?max(FLT_MIN*100.0f,FLT_EPSILON*amax):0.0f;
    float aeps = max(FLT_MIN*100.0f,FLT_EPSILON*amax);
    amin += aeps;
    amax += aeps;
    float fp0 = (float)p0;
    float fp1 = (float)p1;
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        float aui = _au[i2][i1]+aeps;
        float avi = _av[i2][i1]+aeps;
        float a0i = pow(amin/avi,fp0);
        float a1i = pow( avi/aui,fp1);
        _au[i2][i1] = a0i*a1i;
        _av[i2][i1] = a0i;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n1,_n2;
  private float[][] _au;
  private float[][] _av;
  private float[][] _u1;
  private float[][] _u2;
}
