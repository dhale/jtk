/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.MathPlus.*;

import edu.mines.jtk.util.Check;

/**
 * Special-purpose eigensolvers for digital signal processing.
 * Methods of this class solve small eigen-problems efficiently.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.31
 */
public class Eigen {

  /**
   * Computes eigenvalues and eigenvectors for a symmetric 2x2 matrix A.
   * If the eigenvectors are placed in columns in a matrix V, and the 
   * eigenvalues are placed in corresponding columns of a diagonal 
   * matrix D, then AV = VD.
   * @param a the symmetric matrix A.
   * @param v the array of eigenvectors v[0] and v[1].
   * @param d the array of eigenvalues d[0] and d[1].
   */
  public static void solveSymmetric22(float[][] a, float[][] v, float[] d) {

    // Copy matrix to local variables.
    float a00 = a[0][0];
    float a01 = a[0][1],  a11 = a[1][1];

    // Initial eigenvectors. 
    float v00 = 1.0f,     v01 = 0.0f;
    float v10 = 0.0f,     v11 = 1.0f;

    // If off-diagonal element is non-zero, zero it with a Jacobi rotation.
    if (a01!=0.0f) {
      float tiny = 0.1f*sqrt(FLT_EPSILON); // avoid overflow in r*r below
      float c,r,s,t,u,vpr,vqr;
      u = a11-a00;
      if (abs(a01)<tiny*abs(u)) {
        t = a01/u;
      } else {
        r = 0.5f*u/a01;
        t = (r>=0.0f)?1.0f/(r+sqrt(1.0f+r*r)):1.0f/(r-sqrt(1.0f+r*r));
      }
      c = 1.0f/sqrt(1.0f+t*t);
      s = t*c;
      u = s/(1.0f+c);
      r = t*a01;
      a00 -= r;
      a11 += r;
      a01 = 0.0f;
      vpr = v00;
      vqr = v10;
      v00 = vpr-s*(vqr+vpr*u);
      v10 = vqr+s*(vpr-vqr*u);
      vpr = v01;
      vqr = v11;
      v01 = vpr-s*(vqr+vpr*u);
      v11 = vqr+s*(vpr-vqr*u);
    }

    // Copy eigenvalues and eigenvectors to output arrays.
    d[0] = a00;
    d[1] = a11;
    v[0][0] = v00;  v[0][1] = v01;
    v[1][0] = v10;  v[1][1] = v11;

    // Sort eigenvalues (and eigenvectors) in descending order.
    if (d[0]<d[1]) {
      float dt = d[1];
      d[1] = d[0];
      d[0] = dt;
      float[] vt = v[1];
      v[1] = v[0];
      v[0] = vt;
    }
  }

  /**
   * Computes eigenvalues and eigenvectors for a symmetric 2x2 matrix A.
   * If the eigenvectors are placed in columns in a matrix V, and the 
   * eigenvalues are placed in corresponding columns of a diagonal 
   * matrix D, then AV = VD.
   * @param a the symmetric matrix A.
   * @param v the array of eigenvectors v[0] and v[1].
   * @param d the array of eigenvalues d[0] and d[1].
   */
  public static void solveSymmetric22(double[][] a, double[][] v, double[] d) {

    // Copy matrix to local variables.
    double a00 = a[0][0];
    double a01 = a[0][1],  a11 = a[1][1];

    // Initial eigenvectors. 
    double v00 = 1.0,     v01 = 0.0;
    double v10 = 0.0,     v11 = 1.0;

    // If off-diagonal element is non-zero, zero it with a Jacobi rotation.
    if (a01!=0.0) {
      double tiny = 0.1f*sqrt(DBL_EPSILON); // avoid overflow in r*r below
      double c,r,s,t,u,vpr,vqr;
      u = a11-a00;
      if (abs(a01)<tiny*abs(u)) {
        t = a01/u;
      } else {
        r = 0.5*u/a01;
        t = (r>=0.0)?1.0/(r+sqrt(1.0+r*r)):1.0/(r-sqrt(1.0+r*r));
      }
      c = 1.0/sqrt(1.0+t*t);
      s = t*c;
      u = s/(1.0+c);
      r = t*a01;
      a00 -= r;
      a11 += r;
      a01 = 0.0f;
      vpr = v00;
      vqr = v10;
      v00 = vpr-s*(vqr+vpr*u);
      v10 = vqr+s*(vpr-vqr*u);
      vpr = v01;
      vqr = v11;
      v01 = vpr-s*(vqr+vpr*u);
      v11 = vqr+s*(vpr-vqr*u);
    }

    // Copy eigenvalues and eigenvectors to output arrays.
    d[0] = a00;
    d[1] = a11;
    v[0][0] = v00;  v[0][1] = v01;
    v[1][0] = v10;  v[1][1] = v11;

    // Sort eigenvalues (and eigenvectors) in descending order.
    if (d[0]<d[1]) {
      double dt = d[1];
      d[1] = d[0];
      d[0] = dt;
      double[] vt = v[1];
      v[1] = v[0];
      v[0] = vt;
    }
  }

  /**
   * Computes eigenvalues and eigenvectors for a symmetric 3x3 matrix A.
   * If the eigenvectors are placed in columns in a matrix V, and the 
   * eigenvalues are placed in corresponding columns of a diagonal 
   * matrix D, then AV = VD.
   * @param a the symmetric matrix A.
   * @param v the array of eigenvectors v[0], v[1], and v[2].
   * @param d the array of eigenvalues d[0], d[1], and d[2].
   */
  public static void solveSymmetric33(float[][] a, float[][] v, float[] d) {

    // Copy matrix to local variables.
    float a00 = a[0][0];
    float a01 = a[0][1],  a11 = a[1][1];
    float a02 = a[0][2],  a12 = a[1][2],  a22 = a[2][2];

    // Initial eigenvectors. 
    float v00 = 1.0f,     v01 = 0.0f,     v02 = 0.0f;
    float v10 = 0.0f,     v11 = 1.0f,     v12 = 0.0f;
    float v20 = 0.0f,     v21 = 0.0f,     v22 = 1.0f;

    // Tiny constant to avoid overflow of r*r (in computation of t) below.
    float tiny = 0.1f*sqrt(FLT_EPSILON);
    
    // Absolute values of off-diagonal elements.
    float aa01 = abs(a01);
    float aa02 = abs(a02);
    float aa12 = abs(a12);

    // Apply Jacobi rotations until all off-diagonal elements are zero.
    // Count rotations, just in case this does not converge.
    for (int nrot=0; aa01+aa02+aa12>0.0f; ++nrot) {
      Check.state(nrot<100,"number of Jacobi rotations is less than 100");
      float c,r,s,t,u,vpr,vqr,apr,aqr;

      // If a01 is the largest off-diagonal element, ...
      if (aa01>=aa02 && aa01>=aa12) {
        u = a11-a00;
        if (abs(a01)<tiny*abs(u)) {
          t = a01/u;
        } else {
          r = 0.5f*u/a01;
          t = (r>=0.0f)?1.0f/(r+sqrt(1.0f+r*r)):1.0f/(r-sqrt(1.0f+r*r));
        }
        c = 1.0f/sqrt(1.0f+t*t);
        s = t*c;
        u = s/(1.0f+c);
        r = t*a01;
        a00 -= r;
        a11 += r;
        a01 = 0.0f;
        apr = a02;
        aqr = a12;
        a02 = apr-s*(aqr+apr*u);
        a12 = aqr+s*(apr-aqr*u);
        vpr = v00;
        vqr = v10;
        v00 = vpr-s*(vqr+vpr*u);
        v10 = vqr+s*(vpr-vqr*u);
        vpr = v01;
        vqr = v11;
        v01 = vpr-s*(vqr+vpr*u);
        v11 = vqr+s*(vpr-vqr*u);
        vpr = v02;
        vqr = v12;
        v02 = vpr-s*(vqr+vpr*u);
        v12 = vqr+s*(vpr-vqr*u);
      } 
      
      // Else if a02 is the largest off-diagonal element, ...
      else if (aa02>=aa01 && aa02>=aa12) {
        u = a22-a00;
        if (abs(a02)<tiny*abs(u)) {
          t = a02/u;
        } else {
          r = 0.5f*u/a02;
          t = (r>=0.0f)?1.0f/(r+sqrt(1.0f+r*r)):1.0f/(r-sqrt(1.0f+r*r));
        }
        c = 1.0f/sqrt(1.0f+t*t);
        s = t*c;
        u = s/(1.0f+c);
        r = t*a02;
        a00 -= r;
        a22 += r;
        a02 = 0.0f;
        apr = a01;
        aqr = a12;
        a01 = apr-s*(aqr+apr*u);
        a12 = aqr+s*(apr-aqr*u);
        vpr = v00;
        vqr = v20;
        v00 = vpr-s*(vqr+vpr*u);
        v20 = vqr+s*(vpr-vqr*u);
        vpr = v01;
        vqr = v21;
        v01 = vpr-s*(vqr+vpr*u);
        v21 = vqr+s*(vpr-vqr*u);
        vpr = v02;
        vqr = v22;
        v02 = vpr-s*(vqr+vpr*u);
        v22 = vqr+s*(vpr-vqr*u);
      } 

      // Else if a12 is the largest off-diagonal element, ...
      else {
        u = a22-a11;
        if (abs(a12)<tiny*abs(u)) {
          t = a12/u;
        } else {
          r = 0.5f*u/a12;
          t = (r>=0.0f)?1.0f/(r+sqrt(1.0f+r*r)):1.0f/(r-sqrt(1.0f+r*r));
        }
        c = 1.0f/sqrt(1.0f+t*t);
        s = t*c;
        u = s/(1.0f+c);
        r = t*a12;
        a11 -= r;
        a22 += r;
        a12 = 0.0f;
        apr = a01;
        aqr = a02;
        a01 = apr-s*(aqr+apr*u);
        a02 = aqr+s*(apr-aqr*u);
        vpr = v10;
        vqr = v20;
        v10 = vpr-s*(vqr+vpr*u);
        v20 = vqr+s*(vpr-vqr*u);
        vpr = v11;
        vqr = v21;
        v11 = vpr-s*(vqr+vpr*u);
        v21 = vqr+s*(vpr-vqr*u);
        vpr = v12;
        vqr = v22;
        v12 = vpr-s*(vqr+vpr*u);
        v22 = vqr+s*(vpr-vqr*u);
      }

      // Update absolute values of all off-diagonal elements.
      aa01 = abs(a01);
      aa02 = abs(a02);
      aa12 = abs(a12);
    }

    // Copy eigenvalues and eigenvectors to output arrays.
    d[0] = a00;
    d[1] = a11;
    d[2] = a22;
    v[0][0] = v00;  v[0][1] = v01;  v[0][2] = v02;
    v[1][0] = v10;  v[1][1] = v11;  v[1][2] = v12;
    v[2][0] = v20;  v[2][1] = v21;  v[2][2] = v22;

    // Sort eigenvalues (and eigenvectors) in descending order.
    for (int i=0; i<3; ++i) {
      for (int j=i; j>0 && d[j-1]<d[j]; --j) {
        float dj = d[j];
        d[j] = d[j-1];
        d[j-1] = dj;
        float[] vj = v[j];
        v[j] = v[j-1];
        v[j-1] = vj;
      }
    }
  }

  /**
   * Computes eigenvalues and eigenvectors for a symmetric 3x3 matrix A.
   * If the eigenvectors are placed in columns in a matrix V, and the 
   * eigenvalues are placed in corresponding columns of a diagonal 
   * matrix D, then AV = VD.
   * @param a the symmetric matrix A.
   * @param v the array of eigenvectors v[0], v[1], and v[2].
   * @param d the array of eigenvalues d[0], d[1], and d[2].
   */
  public static void solveSymmetric33New(float[][] a, float[][] v, float[] d) {

    // Copy matrix to local variables.
    double a00 = a[0][0];
    double a01 = a[0][1],  a11 = a[1][1];
    double a02 = a[0][2],  a12 = a[1][2],  a22 = a[2][2];

    // Principle invariants.
    double p1 = a00+a11+a22;
    double p2 = a00*a11-a01*a01 +
                a00*a22-a02*a02 +
                a11*a22-a12*a12;
    double p3 = a00*(a11*a22-a12*a12) +
                a01*(a02*a12-a01*a22) +
                a02*(a01*a12-a02*a11);

    // Eigenvalues.
    double p1o3 = p1*ONE_THIRD;
    double p2o3 = p2*ONE_THIRD;
    double p1o3s = p1o3*p1o3;
    double w = p1o3s-p2o3;
    while (w<=0.0)
      w += p1o3s*DBL_EPSILON;
    double r = sqrt(w);
    double s = p1o3*p1o3s-p1*p2*ONE_SIXTH+p3*ONE_HALF;
    double t = acos((s/w)*(1.0/r))*ONE_THIRD;
    double d0 = p1o3+2.0*r*cos(t);
    double d1 = p1o3-2.0*r*cos(t+PIO3);
    double d2 = p1-d0-d1;

    // Eigenvectors. 
    double a0 = a00-d0, b0 = a11-d0, c0 = a22-d0;
    double v00 = (a01*a12-b0*a02)*(a02*a12-c0*a01);
    double v01 = (a02*a12-c0*a01)*(a02*a01-a0*a12);
    double v02 = (a01*a12-b0*a02)*(a02*a01-a0*a12);
    double v0s = 1.0/sqrt(v00*v00+v01*v01+v02*v02);
    v00 *= v0s;
    v01 *= v0s;
    v02 *= v0s;
    double a1 = a00-d1, b1 = a11-d1, c1 = a22-d1;
    double v10 = (a01*a12-b1*a02)*(a02*a12-c1*a01);
    double v11 = (a02*a12-c1*a01)*(a02*a01-a1*a12);
    double v12 = (a01*a12-b1*a02)*(a02*a01-a1*a12);
    double v1s = 1.0/sqrt(v10*v10+v11*v11+v12*v12);
    v10 *= v1s;
    v11 *= v1s;
    v12 *= v1s;
    double v20 = v01*v12-v11*v02;
    double v21 = v10*v02-v00*v12;
    double v22 = v00*v11-v10*v01;

    // Output.
    v[0][0] = (float)v00;  v[0][1] = (float)v01;  v[0][2] = (float)v02;
    v[1][0] = (float)v10;  v[1][1] = (float)v11;  v[1][2] = (float)v12;
    v[2][0] = (float)v20;  v[2][1] = (float)v21;  v[2][2] = (float)v22;
    d[0] = (float)d0;
    d[1] = (float)d1;
    d[2] = (float)d2;
  }
  private static final double ONE_HALF = 1.0/2.0;
  private static final double ONE_THIRD = 1.0/3.0;
  private static final double ONE_SIXTH = 1.0/6.0;
  private static final double PIO3 = PI/3.0;
}
