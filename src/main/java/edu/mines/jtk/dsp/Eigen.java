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
import edu.mines.jtk.util.Check;

/**
 * Special-purpose eigensolvers for digital signal processing.
 * Methods of this class solve small eigen-problems efficiently.
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.09.04
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
      //a01 = 0.0f;
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
      double tiny = 0.1*sqrt(DBL_EPSILON); // avoid overflow in r*r below
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
      //a01 = 0.0;
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
  public static void solveSymmetric33(double[][] a, double[][] v, double[] d) {
    solveSymmetric33Jacobi(a,v,d); // slow but more accurate
  }

  /**
   * Computes eigenvalues and eigenvectors for a symmetric 3x3 matrix A.
   * If the eigenvectors are placed in columns in a matrix V, and the 
   * eigenvalues are placed in corresponding columns of a diagonal 
   * matrix D, then AV = VD.
   * <p>
   * This method is typically faster but not as accurate when eigenvalues
   * differ by more than a few orders of magnitude.
   * @param a the symmetric matrix A.
   * @param v the array of eigenvectors v[0], v[1], and v[2].
   * @param d the array of eigenvalues d[0], d[1], and d[2].
   */
  public static void solveSymmetric33Fast(
    double[][] a, double[][] v, double[] d) 
  {
    solveSymmetric33Hybrid(a,v,d);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  /**
   * Sorts eigenvalues d and eigenvectors v in descending order.
   */
  private static void sortDescending33(double[][] v, double[] d) {
    for (int i=0; i<3; ++i) {
      for (int j=i; j>0 && d[j-1]<d[j]; --j) {
        double dj = d[j];
        d[j] = d[j-1];
        d[j-1] = dj;
        double[] vj = v[j];
        v[j] = v[j-1];
        v[j-1] = vj;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // Kopp's hybrid method. (See Kopp, J., 2008, Efficient numerical
  // diagonalization of hermitian 3x3 matrices: International Journal
  // of Modern Physics, C 19, 523-548.)

  private static final double ONE_THIRD = 1.0/3.0;
  private static final double ONE_OVER_SQRT3 = 1.0/sqrt(3.0);

  /**
   * Computes eigenvalues of a symmetric 3x3 matrix using Cardano's
   * analytical method.
   */
  private static void getEigenvaluesSymmetric33(double[][] a, double[] d) {
    double a00 = a[0][0],
           a01 = a[0][1], a11 = a[1][1],
           a02 = a[0][2], a12 = a[1][2], a22 = a[2][2];
    double de = a01*a12;
    double dd = a01*a01;
    double ee = a12*a12;
    double ff = a02*a02;
    double c2 = a00+a11+a22;
    double c1 = (a00*a11+a00*a22+a11*a22)-(dd+ee+ff);
    double c0 = a22*dd+a00*ee+a11*ff-a00*a11*a22-2.0*a02*de;
    double p = c2*c2-3.0*c1;
    double q = c2*(p-1.5*c1)-13.5*c0; // 13.5 = 27/2
    double t = 27.0*(0.25*c1*c1*(p-c1)+c0*(q+6.75*c0)); // 6.75 = 27/4
    double phi = ONE_THIRD*atan2(sqrt(abs(t)),q);
    double sqrtp = sqrt(abs(p));
    double c = sqrtp*cos(phi);
    double s = ONE_OVER_SQRT3*sqrtp*sin(phi);
    double dt = ONE_THIRD*(c2-c);
    d[0] = dt+c;
    d[1] = dt+s;
    d[2] = dt-s;
  }

  /**
   * Implementation of Kopp's hybrid method for real symmetric 3x3 matrices.
   * Computes eigenvalues and eigenvectors using Cardano's analytical method
   * for the eigenvalues and, typically, an analytical vector cross-product
   * algorithm for the eigenvectors. When necessary for accuracy, this method
   * uses a slower but more accurate QL algorithm.
   */
  private static void solveSymmetric33Hybrid(
    double[][] a, double[][] v, double[] d) 
  {
    getEigenvaluesSymmetric33(a,d);
    double a00 = a[0][0],
           a01 = a[0][1], a11 = a[1][1],
           a02 = a[0][2], a12 = a[1][2]; // a22 = a[2][2]; unused here
    double d0 = d[0], d1 = d[1], d2 = d[2];
    double n0 = a00*a00+a01*a01+a02*a02;
    double n1 = a01*a01+a11*a11+a12*a12;
    double t = abs(d0);
    double u = abs(d1);
    if (u>t) t = u;
    u = abs(d2);
    if (u>t) t = u;
    if (t<1.0) {
      u = t;
    } else {
      u = sqrt(t);
    }
    double error = 256.0*DBL_EPSILON*(n0+u)*(n1+u);
    double v10 = a01*a12-a02*a11;
    double v11 = a02*a01-a12*a00;
    double v12 = a01*a01;

    // Compute 1st eigenvector via v0 = (A-d0)*e1 x (A-d0)*e2.
    double v00 = v10+a02*d0;
    double v01 = v11+a12*d0;
    double v02 = (a00-d0)*(a11-d0)-v12;
    double v0s = v00*v00+v01*v01+v02*v02;

    // If vectors are nearly linearly dependent, or if large cancellation
    // may have occured in the calculation of A-d0, fall back to the QL 
    // algorithm. This case should be rare.
    if (v0s<=error) {
      solveSymmetric33Ql(a,v,d);
      return;
    } else {
      v0s = sqrt(1.0/v0s);
      v00 *= v0s;
      v01 *= v0s;
      v02 *= v0s;
    }

    // Compute 2nd eigenvector via v1 = (A-d1)*e1 x (A-d1)*e2.
    v10 = v10+a02*d1;
    v11 = v11+a12*d1;
    v12 = (a00-d1)*(a11-d1)-v12;
    double v1s = v10*v10+v11*v11+v12*v12;

    // Same check as above but now for 2nd eigenvector.
    if (v1s<=error) {
      solveSymmetric33Ql(a,v,d);
      return;
    } else {
      v1s = sqrt(1.0/v1s);
      v10 *= v1s;
      v11 *= v1s;
      v12 *= v1s;
    }

    // Compute 3rd eigenvector via v2 = v0 x v1
    double v20 = v01*v12-v02*v11;
    double v21 = v02*v10-v00*v12;
    double v22 = v00*v11-v01*v10;

    // Return eigenvectors.
    v[0][0] = v00;  v[0][1] = v01;  v[0][2] = v02;
    v[1][0] = v10;  v[1][1] = v11;  v[1][2] = v12;
    v[2][0] = v20;  v[2][1] = v21;  v[2][2] = v22;
  }

  /**
   * Kopp's solver for eigenvalues and eigenvectors via QL decomposition.
   */
  private static void solveSymmetric33Ql(
    double[][] a, double[][] v, double[] d) 
  {
    // Reduce A to tri-diagonal form.
    double[] e = new double[3];
    reduceSymmetric33(a,v,d,e);

    // Loop over off-diagonal elements e[0] and e[1].
    for (int l=0; l<2; ++l) {

      // While not converged and number of iterations not too large.
      for (int niter=0; niter<=100; ++niter) {
        if (niter==100) {
          System.out.println("A ="); dump(a);
          System.out.println("V ="); dump(v);
          System.out.println("d ="); dump(d);
        }
        Check.state(niter<100,"number of QL iterations is less than 100");

        // If off-diagonal element e[l] is insignificant, then converged.
        int m;
        for (m=l; m<2; ++m) {
          double g = abs(d[m])+abs(d[m+1]);
          if (abs(e[m])+g==g)
            break;
        }
        if (m==l)
          break;

        // Compute Householder transformation.
        double g = (d[l+1]-d[l])/(e[l]+e[l]);
        double r = sqrt(g*g+1.0);
        if (g>0.0) {
          g = d[m]-d[l]+e[l]/(g+r);
        } else {
          g = d[m]-d[l]+e[l]/(g-r);
        }
        double s = 1.0;
        double c = 1.0;
        double p = 0.0;
        for (int i=m-1; i>=l; --i) {
          double f = s*e[i];
          double b = c*e[i];
          if (abs(f)>abs(g)) {
            c = g/f;
            r = sqrt(c*c+1.0);
            e[i+1] = f*r;
            s = 1.0/r;
            c *= s;
          } else {
            s = f/g;
            r = sqrt(s*s+1.0);
            e[i+1] = g*r;
            c = 1.0/r;
            s *= c; 
          }
          g = d[i+1]-p;
          r = (d[i]-g)*s+2.0*c*b;
          p = s*r;
          d[i+1] = g+p;
          g = c*r-b;

          // Update eigenvectors.
          for (int k=0; k<3; ++k) {
            double t = v[i+1][k];
            v[i+1][k] = s*v[i][k]+c*t;
            v[i  ][k] = c*v[i][k]-s*t;
          }
        }
        d[l] -= p;
        e[l] = g;
        e[m] = 0.0;
      }
    }
    sortDescending33(v,d);
  }

  /**
   * Kopp's tridiagonal reduction for real symmetric 3x3 matrices.
   * Diagonal is {d[0],d[1],d[2]} and super-diagonal is {e[0],e[1]}.
   * Householder transformations are stored in the matrix v.
   */
  private static void reduceSymmetric33(
    double[][] a, double[][] v, double[] d, double[] e) 
  {
    double a00 = a[0][0],
           a01 = a[0][1], a11 = a[1][1],
           a02 = a[0][2], a12 = a[1][2], a22 = a[2][2];
    double v11 = 1.0;
    double v12 = 0.0;
    double v21 = 0.0;
    double v22 = 1.0;
    double h = a01*a01+a02*a02;
    double g = (a01>0.0)?-sqrt(h):sqrt(h);
    double e0 = g;
    double f = g*a01;
    double u1 = a01-g;
    double u2 = a02;
    double omega = h-f;
    double d0,d1,d2,e1,s,q1,q2;
    if (omega>0.0) {
      omega = 1.0/omega;
      s = 0.0;
      f = a11*u1+a12*u2;
      q1 = omega*f;
      s += u1*f;
      f = a12*u1+a22*u2;
      q2 = omega*f;
      s += u2*f;
      s *= 0.5*omega*omega;
      q1 -= s*u1;
      q2 -= s*u2;
      d0 = a00;
      d1 = a11-2.0*q1*u1;
      d2 = a22-2.0*q2*u2;
      f = omega*u1;
      v11 -= f*u1;
      v12 -= f*u2;
      f = omega*u2;
      v21 -= f*u1;
      v22 -= f*u2;
      e1 = a12-q1*u2-u1*q2;
    } else {
      d0 = a00;
      d1 = a11;
      d2 = a22;
      e1 = a12;
    }
    d[0] = d0;
    d[1] = d1;
    d[2] = d2;
    e[0] = e0;
    e[1] = e1;
    v[0][0] = 1.0;  v[0][1] = 0.0;  v[0][2] = 0.0;
    v[1][0] = 0.0;  v[1][1] = v11;  v[1][2] = v12;
    v[2][0] = 0.0;  v[2][1] = v21;  v[2][2] = v22;
  }

  ///////////////////////////////////////////////////////////////////////////
  // Old iterative Jacobi method for symmetric 3x3 matrices. For random 
  // matrices, this Jacobi solver is about 6 times slower than the 
  // hybrid method.

  /**
   * Old iterative Jacobi solver. Slower than the current solver.
   */
  private static void solveSymmetric33Jacobi(
    double[][] a, double[][] v, double[] d) 
  {

    // Copy matrix to local variables.
    double a00 = a[0][0],
           a01 = a[0][1],  a11 = a[1][1],
           a02 = a[0][2],  a12 = a[1][2],  a22 = a[2][2];

    // Initial eigenvectors. 
    double v00 = 1.0,  v01 = 0.0,  v02 = 0.0,
           v10 = 0.0,  v11 = 1.0,  v12 = 0.0,
           v20 = 0.0,  v21 = 0.0,  v22 = 1.0;

    // Tiny constant to avoid overflow of r*r (in computation of t) below.
    double tiny = 0.1*sqrt(DBL_EPSILON);
    
    // Absolute values of off-diagonal elements.
    double aa01 = abs(a01);
    double aa02 = abs(a02);
    double aa12 = abs(a12);

    // Apply Jacobi rotations until all off-diagonal elements are zero.
    // Count rotations, just in case this does not converge.
    for (int nrot=0; aa01+aa02+aa12>0.0; ++nrot) {
      Check.state(nrot<100,"number of Jacobi rotations is less than 100");
      double c,r,s,t,u,vpr,vqr,apr,aqr;

      // If a01 is the largest off-diagonal element, ...
      if (aa01>=aa02 && aa01>=aa12) {
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
        a01 = 0.0;
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
          r = 0.5*u/a02;
          t = (r>=0.0)?1.0/(r+sqrt(1.0+r*r)):1.0/(r-sqrt(1.0+r*r));
        }
        c = 1.0/sqrt(1.0+t*t);
        s = t*c;
        u = s/(1.0+c);
        r = t*a02;
        a00 -= r;
        a22 += r;
        a02 = 0.0;
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
          r = 0.5*u/a12;
          t = (r>=0.0)?1.0/(r+sqrt(1.0+r*r)):1.0/(r-sqrt(1.0+r*r));
        }
        c = 1.0/sqrt(1.0+t*t);
        s = t*c;
        u = s/(1.0+c);
        r = t*a12;
        a11 -= r;
        a22 += r;
        a12 = 0.0;
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
    sortDescending33(v,d);
  }
}
