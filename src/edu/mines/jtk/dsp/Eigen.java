/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Special-purpose eigensolvers.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.31
 */
public class Eigen {

  /**
   * Computes eigenvalues and eigenvectors for a symmetric 3x3 matrix A.
   * If the eigenvectors are placed in columns in a matrix V, and the 
   * eigenvalues are placed in corresponding columns of a diagonal 
   * matrix D, then AV = VD.
   * @param a the symmetric matrix A.
   * @param v the array of eigenvectors v[0], v[1], and v[2].
   * @param d the array of eigenvalues d[0], d[1], and d[2].
   */
  public static void solve33(float[][] a, float[][] v, float[] d) {
    float a00 = a[0][0];
    float a01 = a[0][1],  a11 = a[1][1];
    float a02 = a[0][2],  a12 = a[1][2],  a22 = a[2][2];
    float  d0 = a00,       d1 = a11,       d2 = a22;
    float v00 = 1.0f,     v01 = 0.0f,     v02 = 0.0f;
    float v10 = 0.0f,     v11 = 1.0f,     v12 = 0.0f;
    float v20 = 0.0f,     v21 = 0.0f,     v22 = 1.0f;
    float g,h,hh,c,s,t,tau;
    
    float aa01 = abs(a01);
    float aa02 = abs(a02);
    float aa12 = abs(a12);
    for (int nrot=0; aa01+aa02+aa12>0.0f; ++nrot) {
      Check.state(nrot<50,"number of Jacobi rotations is less than 50");

      if (aa01>=aa02 && aa01>=aa12) {
        g = 100.0f*aa01;
        h = d1-d0;
        hh = abs(h);
        if (hh+g==hh) {
          t = a01/h;
        } else {
          float theta = 0.5f*h/a01;
          float den = abs(theta)+sqrt(1.0f+theta*theta);
          t = (theta>=0.0f)?1.0f/den:-1.0f/den;
        }
        c = 1.0f/sqrt(1.0f+t*t);
        s = t*c;
        tau = s/(1.0f+c);
        h = t*a01;
        d0 -= h;
        d1 += h;
        a01 = 0.0f;
        g = a02;
        h = a12;
        a02 = g-s*(h+g*tau);
        a12 = h+s*(g-h*tau);
        g = v00;
        h = v10;
        v00 = g-s*(h+g*tau);
        v10 = h+s*(g-h*tau);
        g = v01;
        h = v11;
        v01 = g-s*(h+g*tau);
        v11 = h+s*(g-h*tau);
        g = v02;
        h = v12;
        v02 = g-s*(h+g*tau);
        v12 = h+s*(g-h*tau);
      } else if (aa02>=aa01 && aa02>=aa12) {
        g = 100.0f*aa02;
        h = d2-d0;
        hh = abs(h);
        if (hh+g==hh) {
          t = a02/h;
        } else {
          float theta = 0.5f*h/a02;
          float den = abs(theta)+sqrt(1.0f+theta*theta);
          t = (theta>=0.0f)?1.0f/den:-1.0f/den;
        }
        c = 1.0f/sqrt(1.0f+t*t);
        s = t*c;
        tau = s/(1.0f+c);
        h = t*a02;
        d0 -= h;
        d2 += h;
        a02 = 0.0f;
        g = a01;
        h = a12;
        a01 = g-s*(h+g*tau);
        a12 = h+s*(g-h*tau);
        g = v00;
        h = v20;
        v00 = g-s*(h+g*tau);
        v20 = h+s*(g-h*tau);
        g = v01;
        h = v21;
        v01 = g-s*(h+g*tau);
        v21 = h+s*(g-h*tau);
        g = v02;
        h = v22;
        v02 = g-s*(h+g*tau);
        v22 = h+s*(g-h*tau);
      } else {
        g = 100.0f*aa12;
        h = d2-d1;
        hh = abs(h);
        if (hh+g==hh) {
          t = a12/h;
        } else {
          float theta = 0.5f*h/a12;
          float den = abs(theta)+sqrt(1.0f+theta*theta);
          t = (theta>=0.0f)?1.0f/den:-1.0f/den;
        }
        c = 1.0f/sqrt(1.0f+t*t);
        s = t*c;
        tau = s/(1.0f+c);
        h = t*a12;
        d1 -= h;
        d2 += h;
        a12 = 0.0f;
        g = a01;
        h = a02;
        a01 = g-s*(h+g*tau);
        a02 = h+s*(g-h*tau);
        g = v10;
        h = v20;
        v10 = g-s*(h+g*tau);
        v20 = h+s*(g-h*tau);
        g = v11;
        h = v21;
        v11 = g-s*(h+g*tau);
        v21 = h+s*(g-h*tau);
        g = v12;
        h = v22;
        v12 = g-s*(h+g*tau);
        v22 = h+s*(g-h*tau);
      }
      aa01 = abs(a01);
      aa02 = abs(a02);
      aa12 = abs(a12);
    }
    v[0][0] = v00;  v[0][1] = v01;  v[0][2] = v02;
    v[1][0] = v10;  v[1][1] = v11;  v[1][2] = v12;
    v[2][0] = v20;  v[2][1] = v21;  v[2][2] = v22;
    d[0] = d0;
    d[1] = d1;
    d[2] = d2;
  }
}
