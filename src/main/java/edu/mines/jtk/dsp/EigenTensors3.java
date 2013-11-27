/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import java.io.*;

import edu.mines.jtk.io.ArrayInputStream;
import edu.mines.jtk.io.ArrayOutputStream;
import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.UnitSphereSampling;


/**
 * An array of eigen-decompositions of tensors for 3D image processing.
 * Each tensor is a symmetric positive-semidefinite 3-by-3 matrix:
 * <pre><code>
 *     |a11 a12 a13|
 * A = |a12 a22 a23|
 *     |a13 a23 a33|
 * </code></pre>
 * Such tensors can be used to parameterize anisotropic image processing.
 * <p>
 * The eigen-decomposition of the matrix A is
 * <pre><code>
 * A = au*u*u' + av*v*v' + aw*w*w' 
 *   = (au-av)*u*u' + (aw-av)*w*w' + av*I
 * </code></pre>
 * where u, v, and w are orthogonal unit eigenvectors of A. (The notation 
 * u' denotes the transpose of u.) The outer products of eigenvectors are
 * scaled by the non-negative eigenvalues au, av, and aw. The second
 * equation exploits the identity u*u' + v*v' + w*w' = I, and makes
 * apparent the redundancy of the vector v.
 * <p>
 * Only the 1st and 2nd components of the eigenvectors u and w are stored. 
 * Except for a sign, the 3rd components may be computed from the 1st and 
 * 2nd. Because the tensors are independent of the choice of sign, the 
 * eigenvectors u and w are stored with an implied non-negative 3rd 
 * component.
 * <p>
 * Storage may be further reduced by compression, whereby eigenvalues
 * and eigenvectors are quantized. Quantization errors for eigenvalues
 * (au,av,aw) are less than 0.001*(au+av+aw). Quantization errors for 
 * eigenvectors are less than one degree of arc on the unit sphere.
 * Memory required to store each tensor is 12 bytes if compressed, and
 * 28 bytes if not compressed.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.06.07
 */
public class EigenTensors3 implements Tensors3,Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs tensors for specified array dimensions. All eigenvalues 
   * and eigenvectors u and w are not set and are initially zero.
   * @param n1 number of tensors in 1st dimension.
   * @param n2 number of tensors in 2nd dimension.
   * @param n3 number of tensors in 3rd dimension.
   * @param compressed true, for compressed tensors; false, otherwise.
   */
  public EigenTensors3(int n1, int n2, int n3, boolean compressed) {
    _n1 = n1;
    _n2 = n2;
    _n3 = n3;
    _compressed = compressed;
    if (compressed) {
      _bu = new short[n3][n2][n1];
      _bw = new short[n3][n2][n1];
      _iu = new short[n3][n2][n1];
      _iw = new short[n3][n2][n1];
      if (_uss==null) 
        _uss = new UnitSphereSampling(16);
    } else {
      _au = new float[n3][n2][n1];
      _aw = new float[n3][n2][n1];
      _u1 = new float[n3][n2][n1];
      _u2 = new float[n3][n2][n1];
      _w1 = new float[n3][n2][n1];
      _w2 = new float[n3][n2][n1];
    }
    _as = new float[n3][n2][n1];
  }

  /**
   * Constructs tensors for specified array dimensions and eigenvalues.
   * The 3rd components of eigenvectors u and w are computed from the 1st 
   * and 2nd components and are assumed to be non-negative.
   * @param u1 array of 1st components of u.
   * @param u2 array of 2nd components of u.
   * @param w1 array of 1st components of w.
   * @param w2 array of 2nd components of w.
   * @param au array of eigenvalues au.
   * @param av array of eigenvalues av.
   * @param aw array of eigenvalues aw.
   * @param compressed true, for compressed tensors; false, otherwise.
   */
  public EigenTensors3(
    float[][][] u1, float[][][] u2,
    float[][][] w1, float[][][] w2,
    float[][][] au, float[][][] av, float[][][] aw,
    boolean compressed)
  {
    this(u1[0][0].length,u1[0].length,u1.length,compressed);
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          float aui = au[i3][i2][i1];
          float avi = av[i3][i2][i1];
          float awi = aw[i3][i2][i1];
          float u1i = u1[i3][i2][i1];
          float u2i = u2[i3][i2][i1];
          float u3i = c3(u1i,u2i);
          float w1i = w1[i3][i2][i1];
          float w2i = w2[i3][i2][i1];
          float w3i = c3(w1i,w2i);
          setEigenvalues(i1,i2,i3,aui,avi,awi);
          setEigenvectorU(i1,i2,i3,u1i,u2i,u3i);
          setEigenvectorW(i1,i2,i3,w1i,w2i,w3i);
        }
      }
    }
  }

  /**
   * Constructs tensors from the specified tensors.
   * @param t the tensors from which to copy eigenvectors and eigenvalues.
   */
  public EigenTensors3(EigenTensors3 t) {
    this(t._n1,t._n2,t._n3,t._compressed);
    float[] a = new float[3];
    float[] u = new float[3];
    float[] w = new float[3];
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          t.getEigenvalues(i1,i2,i3,a);
          t.getEigenvectorU(i1,i2,i3,u);
          t.getEigenvectorW(i1,i2,i3,w);
          setEigenvalues(i1,i2,i3,a);
          setEigenvectorU(i1,i2,i3,u);
          setEigenvectorW(i1,i2,i3,w);
        }
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
   * Gets the number of tensors in the 3rd dimension.
   * @return the number of tensors in the 3rd dimension.
   */
  public int getN3() {
    return _n3;
  }

  /**
   * Gets tensor elements for specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param a array {a11,a12,a13,a22,a23,a33} of tensor elements.
   */
  public void getTensor(int i1, int i2, int i3, float[] a) {
    float asum = _as[i3][i2][i1];
    float au,av,aw,u1,u2,u3,w1,w2,w3;
    if (_compressed) {
      float ascale = asum*AS_GET;
      au = ascale*_bu[i3][i2][i1];
      aw = ascale*_bw[i3][i2][i1];
      float[] u = _uss.getPoint(_iu[i3][i2][i1]);
      u1 = u[0]; u2 = u[1]; u3 = u[2];
      float[] w = _uss.getPoint(_iw[i3][i2][i1]);
      w1 = w[0]; w2 = w[1]; w3 = w[2];
    } else {
      au = _au[i3][i2][i1];
      aw = _aw[i3][i2][i1];
      u1 = _u1[i3][i2][i1];
      u2 = _u2[i3][i2][i1];
      u3 = c3(u1,u2);
      w1 = _w1[i3][i2][i1];
      w2 = _w2[i3][i2][i1];
      w3 = c3(w1,w2);
    }
    av = asum-au-aw;
    au -= av;
    aw -= av;
    a[0] = au*u1*u1+aw*w1*w1+av; // a11
    a[1] = au*u1*u2+aw*w1*w2   ; // a12
    a[2] = au*u1*u3+aw*w1*w3   ; // a13
    a[3] = au*u2*u2+aw*w2*w2+av; // a22
    a[4] = au*u2*u3+aw*w2*w3   ; // a23
    a[5] = au*u3*u3+aw*w3*w3+av; // a33
  }

  /**
   * Gets tensor elements for specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @return a array {a11,a12,a13,a22,a23,a33} of tensor elements.
   */
  public float[] getTensor(int i1, int i2, int i3) {
    float[] a = new float[6];
    getTensor(i1,i2,i3,a);
    return a;
  }


  /**
   * Gets eigenvalues for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param a array {au,av,aw} of eigenvalues.
   */
  public void getEigenvalues(int i1, int i2, int i3, float[] a) {
    float asum = _as[i3][i2][i1];
    float au,aw;
    if (_compressed) {
      float ascale = asum*AS_GET;
      au = ascale*_bu[i3][i2][i1];
      aw = ascale*_bw[i3][i2][i1];
    } else {
      au = _au[i3][i2][i1];
      aw = _aw[i3][i2][i1];
    }
    a[0] = au; 
    a[1] = asum-au-aw;
    a[2] = aw; 
  }

  /**
   * Gets eigenvalues for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @return array {au,av,aw} of eigenvalues.
   */
  public float[] getEigenvalues(int i1, int i2, int i3) {
    float[] a = new float[3];
    getEigenvalues(i1,i2,i3,a);
    return a;
  }

  /**
   * Gets eigenvalues for all tensors.
   * @param au array of eigenvalues au.
   * @param av array of eigenvalues av.
   * @param aw array of eigenvalues aw.
   */
  public void getEigenvalues(float[][][] au, float[][][] av, float[][][] aw) {
    float[] auvw = new float[3];
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          getEigenvalues(i1,i2,i3,auvw);
          au[i3][i2][i1] = auvw[0];
          av[i3][i2][i1] = auvw[1];
          aw[i3][i2][i1] = auvw[2];
        }
      }
    }
  }

  /**
   * Gets the eigenvector u for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param u array {u1,u2,u3} of eigenvector components.
   */
  public void getEigenvectorU(int i1, int i2, int i3, float[] u) {
    if (_compressed) {
      float[] ui = _uss.getPoint(_iu[i3][i2][i1]);
      u[0] = ui[0];
      u[1] = ui[1];
      u[2] = ui[2];
    } else {
      u[0] = _u1[i3][i2][i1];
      u[1] = _u2[i3][i2][i1];
      u[2] = c3(u[0],u[1]);
    }
  }

  /**
   * Gets the eigenvector u for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @return array {u1,u2,u3} of eigenvector components.
   */
  public float[] getEigenvectorU(int i1, int i2, int i3) {
    float[] u = new float[3];
    getEigenvectorU(i1,i2,i3,u);
    return u;
  }

  /**
   * Gets the eigenvector v for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param v array {v1,v2,v3} of eigenvector components.
   */
  public void getEigenvectorV(int i1, int i2, int i3, float[] v) {
    float[] u = getEigenvectorU(i1,i2,i3);
    float[] w = getEigenvectorW(i1,i2,i3);
    v[0] = w[1]*u[2]-w[2]*u[1]; // v = w cross u
    v[1] = w[2]*u[0]-w[0]*u[2];
    v[2] = w[0]*u[1]-w[1]*u[0];
  }

  /**
   * Gets the eigenvector v for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @return array {v1,v2,v3} of eigenvector components.
   */
  public float[] getEigenvectorV(int i1, int i2, int i3) {
    float[] v = new float[3];
    getEigenvectorV(i1,i2,i3,v);
    return v;
  }

  /**
   * Gets the eigenvector w for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param w array {w1,w2,w3} of eigenvector components.
   */
  public void getEigenvectorW(int i1, int i2, int i3, float[] w) {
    if (_compressed) {
      float[] wi = _uss.getPoint(_iw[i3][i2][i1]);
      w[0] = wi[0];
      w[1] = wi[1];
      w[2] = wi[2];
    } else {
      w[0] = _w1[i3][i2][i1];
      w[1] = _w2[i3][i2][i1];
      w[2] = c3(w[0],w[1]);
    }
  }

  /**
   * Gets the eigenvector w for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @return array {w1,w2,w3} of eigenvector components.
   */
  public float[] getEigenvectorW(int i1, int i2, int i3) {
    float[] w = new float[3];
    getEigenvectorW(i1,i2,i3,w);
    return w;
  }

  /**
   * Sets tensor elements for specified indices.
   * This method first computes an eigen-decomposition of the specified
   * tensor, and then stores the computed eigenvectors and eigenvalues.
   * The eigenvalues are ordered such that au &gt;= av &gt;= aw &gt;= 0.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param a array {a11,a12,a13,a22,a23,a33} of tensor elements.
   */
  public void setTensor(int i1, int i2, int i3, float[] a) {
    setTensor(i1,i2,i3,a[0],a[1],a[2],a[3],a[4],a[5]);
  }

  /**
   * Sets tensor elements for specified indices.
   * This method first computes an eigen-decomposition of the specified
   * tensor, and then stores the computed eigenvectors and eigenvalues.
   * The eigenvalues are ordered such that au &gt;= av &gt;= aw &gt;= 0.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param a11 tensor element a11.
   * @param a12 tensor element a12.
   * @param a13 tensor element a13.
   * @param a22 tensor element a22.
   * @param a23 tensor element a23.
   * @param a33 tensor element a33.
   */
  public void setTensor(
    int i1, int i2, int i3, 
    float a11, float a12, float a13, float a22, float a23, float a33)
  {
    double[][] aa = {
      {a11,a12,a13},
      {a12,a22,a23},
      {a13,a23,a33}
    };
    double[][] vv = new double[3][3];
    double[] ev = new double[3];
    //Eigen.solveSymmetric33Fast(aa,vv,ev); // is fast method accurate enough?
    Eigen.solveSymmetric33(aa,vv,ev); // slow but accurate!
    double[] u = vv[0];
    double[] w = vv[2];
    float u1 = (float)u[0];
    float u2 = (float)u[1];
    float u3 = (float)u[2];
    float w1 = (float)w[0];
    float w2 = (float)w[1];
    float w3 = (float)w[2];
    float au = (float)ev[0]; if (au<0.0f) au = 0.0f;
    float av = (float)ev[1]; if (av<0.0f) av = 0.0f;
    float aw = (float)ev[2]; if (aw<0.0f) aw = 0.0f;
    setEigenvectorU(i1,i2,i3,u1,u2,u3);
    setEigenvectorW(i1,i2,i3,w1,w2,w3);
    setEigenvalues(i1,i2,i3,au,av,aw);
  }

  /**
   * Sets eigenvalues for all tensors.
   * @param au eigenvalue au.
   * @param av eigenvalue av.
   * @param aw eigenvalue aw.
   */
  public void setEigenvalues(float au, float av, float aw) {
    float as = au+av+aw;
    if (_compressed) {
      float ascale = (as>0.0f)?AS_SET/as:0.0f;
      short bu = (short)(au*ascale+0.5f);
      short bw = (short)(aw*ascale+0.5f);
      fill(bu,_bu);
      fill(bw,_bw);
    } else {
      fill(au,_au);
      fill(aw,_aw);
    }
    fill(as,_as);
  }

  /**
   * Sets eigenvalues for all tensors.
   * @param au array of eigenvalues au.
   * @param av array of eigenvalues av.
   * @param aw array of eigenvalues aw.
   */
  public void setEigenvalues(float[][][] au, float[][][] av, float[][][] aw) {
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          float aui = au[i3][i2][i1];
          float avi = av[i3][i2][i1];
          float awi = aw[i3][i2][i1];
          setEigenvalues(i1,i2,i3,aui,avi,awi);
        }
      }
    }
  }

  /**
   * Sets eigenvalues for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param au eigenvalue au.
   * @param av eigenvalue av.
   * @param aw eigenvalue aw.
   */
  public void setEigenvalues(
    int i1, int i2, int i3, float au, float av, float aw)
  {
    float asum = au+av+aw;
    if (_compressed) {
      float ascale = (asum>0.0f)?AS_SET/asum:0.0f;
      _bu[i3][i2][i1] = (short)(au*ascale+0.5f);
      _bw[i3][i2][i1] = (short)(aw*ascale+0.5f);
    } else {
      _au[i3][i2][i1] = au;
      _aw[i3][i2][i1] = aw;
    }
    _as[i3][i2][i1] = asum;
  }

  /**
   * Sets eigenvalues for the tensor with specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param a array {au,av,aw} of eigenvalues.
   */
  public void setEigenvalues(int i1, int i2, int i3, float[] a) {
    setEigenvalues(i1,i2,i3,a[0],a[1],a[2]);
  }

  /**
   * Sets the eigenvector u for the tensor with specified indices.
   * The specified vector is assumed to have length one. If the 3rd 
   * component is negative, this method stores the negative of the 
   * specified vector, so that the 3rd component is positive.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param u1 1st component of u.
   * @param u2 2nd component of u.
   * @param u3 3nd component of u.
   */
  public void setEigenvectorU(
    int i1, int i2, int i3, float u1, float u2, float u3)
  {
    if (u3<0.0f) {
      u1 = -u1;
      u2 = -u2;
      u3 = -u3;
    }
    if (_compressed) {
      _iu[i3][i2][i1] = (short)_uss.getIndex(u1,u2,u3);
    } else {
      _u1[i3][i2][i1] = u1;
      _u2[i3][i2][i1] = u2;
    }
  }

  /**
   * Sets the eigenvector u for the tensor with specified indices.
   * The specified vector is assumed to have length one. If the 3rd 
   * component is negative, this method stores the negative of the 
   * specified vector, so that the 3rd component is positive.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param u {u1,u2,u3} of eigenvector components.
   */
  public void setEigenvectorU(int i1, int i2, int i3, float[] u) {
    setEigenvectorU(i1,i2,i3,u[0],u[1],u[2]);
  }

  /**
   * Sets the eigenvector w for the tensor with specified indices.
   * The specified vector is assumed to have length one. If the 3rd 
   * component is negative, this method stores the negative of the 
   * specified vector, so that the 3rd component is positive.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param w1 1st component of w.
   * @param w2 2nd component of w.
   * @param w3 3nd component of w.
   */
  public void setEigenvectorW(
    int i1, int i2, int i3, float w1, float w2, float w3)
  {
    if (w3<0.0f) {
      w1 = -w1;
      w2 = -w2;
      w3 = -w3;
    }
    if (_compressed) {
      _iw[i3][i2][i1] = (short)_uss.getIndex(w1,w2,w3);
    } else {
      _w1[i3][i2][i1] = w1;
      _w2[i3][i2][i1] = w2;
    }
  }

  /**
   * Sets the eigenvector w for the tensor with specified indices.
   * The specified vector is assumed to have length one. If the 3rd 
   * component is negative, this method stores the negative of the 
   * specified vector, so that the 3rd component is positive.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param w {w1,w2,w3} of eigenvector components.
   */
  public void setEigenvectorW(int i1, int i2, int i3, float[] w) {
    setEigenvectorW(i1,i2,i3,w[0],w[1],w[2]);
  }

  /**
   * Scales eigenvalues of these tensors by specified factors.
   * @param s array of scale factors.
   */
  public void scale(float[][][] s) {
    float[] a = new float[3];
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          float si = s[i3][i2][i1];
          getEigenvalues(i1,i2,i3,a);
          a[0] *= si;
          a[1] *= si;
          a[2] *= si;
          setEigenvalues(i1,i2,i3,a);
        }
      }
    }
  }

  /**
   * Inverts these tensors by inverting their eigenvalues.
   * Takes no care to avoid division by zero eigenvalues.
   */
  public void invert() {
    float[] a = new float[3];
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          getEigenvalues(i1,i2,i3,a);
          a[0] = 1.0f/a[0];
          a[1] = 1.0f/a[1];
          a[2] = 1.0f/a[2];
          setEigenvalues(i1,i2,i3,a);
        }
      }
    }
  }

  /**
   * Inverts these tensors, assumed to be structure tensors.
   * After inversion, all eigenvalues are in the range (0,1].
   * Specifically, after inversion, 0 &lt; au &lt;= av &lt;= aw &lt;= 1.
   * <p>
   * Before inversion, tensors are assumed to be structure tensors, 
   * for which eigenvalues au are not less than their corresponding 
   * eigenvalues av which are not less than their corresponding aw. 
   * (Any eigenvalues au for which this condition is not satisfied 
   * are set equal to the corresponding eigenvalue av; likewise for 
   * av and aw.) Structure tensors can, for example, be computed using 
   * {@link LocalOrientFilter}.
   * <p>
   * Then, if any eigenvalues are equal to zero, this method adds a 
   * small fraction of the largest eigenvalue au to all eigenvalues.
   * If am is the minimum of the eigenvalues aw after this perturbation,
   * then the parameter p0 is used to compute a0 = pow(am/aw,p0), the 
   * parameter p1 is used to compute a1 = pow(aw/av,p1), and the parameter
   * p2 is used to compute a2 = pow(av/au,p2). Inverted eigenvalues are 
   * then au = a0*a1*a2, av = a0*a1 and aw = a0. 
   * <p>
   * In this way, p0 emphasizes overall amplitude, p1 emphasizes 
   * linearity and p2 emphasizes planarity. For amplitude-independent 
   * tensors with all eigenvalues av equal to one, set p0 = 0.0. To 
   * enhance linearity, set p1 &gt; 1.0. To enhance planarity, set
   * p2 &gt; 1.0. To simply invert (and normalize) these tensors, set 
   * p0 = p1 = p2 = 1.0.
   * @param p0 power for amplitude.
   * @param p1 power for linearity.
   * @param p2 power for planarity.
   */
  public void invertStructure(double p0, double p1, double p2) {
    float[] a = new float[3];
    float amax = 0.0f;
    float amin = FLT_MAX;
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          getEigenvalues(i1,i2,i3,a);
          float aui = a[0], avi = a[1], awi = a[2];
          if (awi<0.0f) awi = 0.0f;
          if (avi< awi) avi = awi;
          if (aui< avi) aui = avi;
          if (awi<amin) amin = awi;
          if (aui>amax) amax = aui;
          setEigenvalues(i1,i2,i3,aui,avi,awi);
        }
      }
    }
    float aeps = max(FLT_MIN*100.0f,FLT_EPSILON*amax);
    amin += aeps;
    amax += aeps;
    float fp0 = (float)p0;
    float fp1 = (float)p1;
    float fp2 = (float)p2;
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          getEigenvalues(i1,i2,i3,a);
          float aui = a[0], avi = a[1], awi = a[2];
          aui += aeps;
          avi += aeps;
          awi += aeps;
          float a0i = pow(amin/awi,fp0);
          float a1i = pow( awi/avi,fp1);
          float a2i = pow( avi/aui,fp2);
          aui = a0i*a1i*a2i;
          avi = a0i*a1i;
          awi = a0i;
          setEigenvalues(i1,i2,i3,aui,avi,awi);
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final float AS_SET = (float)Short.MAX_VALUE;
  private static final float AS_GET = 1.0f/AS_SET;
  private static UnitSphereSampling _uss; // for compressing unit vectors

  private boolean _compressed; // true if tensors compressed
  private int _n1,_n2,_n3; // array dimensions
  private float[][][] _as; // sum a1+a2+a3
  private short[][][] _bu; // au compressed
  private short[][][] _bw; // aw compressed
  private short[][][] _iu; // (u1,u2,u3) compressed
  private short[][][] _iw; // (w1,w2,w3) compressed
  private float[][][] _au; // au not compressed
  private float[][][] _aw; // aw not compressed
  private float[][][] _u1; // u1 not compressed
  private float[][][] _u2; // u2 not compressed
  private float[][][] _w1; // w1 not compressed
  private float[][][] _w2; // w2 not compressed

  private static float c3(float c1, float c2) {
    float c3s = 1.0f-c1*c1-c2*c2;
    return (c3s>0.0f)?(float)Math.sqrt(c3s):0.0f;
  }

  private void readObject(ObjectInputStream ois)
    throws IOException, ClassNotFoundException 
  {
    int format = ois.readInt();
    if (format==1) {
      boolean compressed = _compressed = ois.readBoolean();
      int n1 = _n1 = ois.readInt();
      int n2 = _n2 = ois.readInt();
      int n3 = _n3 = ois.readInt();
      @SuppressWarnings("resource")
      ArrayInputStream ais = new ArrayInputStream(ois);
      if (compressed) {
        _bu = new short[n3][n2][n1];
        _bw = new short[n3][n2][n1];
        _iu = new short[n3][n2][n1];
        _iw = new short[n3][n2][n1];
        ais.readShorts(_bu);
        ais.readShorts(_bw);
        ais.readShorts(_iu);
        ais.readShorts(_iw);
        if (_uss==null)
          _uss = new UnitSphereSampling(16);
      } else {
        _au = new float[n3][n2][n1];
        _aw = new float[n3][n2][n1];
        _u1 = new float[n3][n2][n1];
        _u2 = new float[n3][n2][n1];
        _w1 = new float[n3][n2][n1];
        _w2 = new float[n3][n2][n1];
        ais.readFloats(_au);
        ais.readFloats(_aw);
        ais.readFloats(_u1);
        ais.readFloats(_u2);
        ais.readFloats(_w1);
        ais.readFloats(_w2);
      }
      _as = new float[n3][n2][n1];
      ais.readFloats(_as);
    }

  //else if (format==2) {
  //  ...
  //}

    else {
      throw new InvalidClassException("invalid format");
    }
  }

  private void writeObject(ObjectOutputStream oos)
    throws IOException 
  {
    oos.writeInt(1); // format
    oos.writeBoolean(_compressed);
    oos.writeInt(_n1);
    oos.writeInt(_n2);
    oos.writeInt(_n3);
    @SuppressWarnings("resource")
    ArrayOutputStream aos = new ArrayOutputStream(oos);
    if (_compressed) {
      aos.writeShorts(_bu);
      aos.writeShorts(_bw);
      aos.writeShorts(_iu);
      aos.writeShorts(_iw);
    } else {
      aos.writeFloats(_au);
      aos.writeFloats(_aw);
      aos.writeFloats(_u1);
      aos.writeFloats(_u2);
      aos.writeFloats(_w1);
      aos.writeFloats(_w2);
    }
    aos.writeFloats(_as);
    aos.flush();
  }
}
