/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import edu.mines.jtk.util.Complex;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Complex array processing. A complex array is an array of floats, 
 * in which each consecutive pair of floats represents the real and 
 * imaginary parts of one complex number. This means that a complex 
 * array cx contains cx.length/2 complex numbers, and cx.length is an 
 * even number.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class Cap {

  ///////////////////////////////////////////////////////////////////////////
  // copy
  public static float[] copy(float[] cx) {
    return copy(cx.length/2,cx);
  }
  public static float[][] copy(float[][] cx) {
    int n2 = cx.length;
    float[][] cy = new float[n2][];
    for (int i2=0; i2<n2; ++i2)
      cy[i2] = copy(cx[i2]);
    return cy;
  }
  public static float[][][] copy(float[][][] cx) {
    int n3 = cx.length;
    float[][][] cy = new float[n3][][];
    for (int i3=0; i3<n3; ++i3)
      cy[i3] = copy(cx[i3]);
    return cy;
  }
  public static float[] copy(int n1, float[] cx) {
    return copy(n1,cx,new float[2*n1]);
  }
  public static float[][] copy(int n1, int n2, float[][] cx) {
    return copy(n1,n2,cx,new float[n2][2*n1]);
  }
  public static float[][][] copy(int n1, int n2, int n3, float[][][] cx) {
    return copy(n1,n2,n3,cx,new float[n3][n2][2*n1]);
  }
  public static float[] copy(float[] cx, float[] cy) {
    return copy(cx.length/2,cx,cy);
  }
  public static float[][] copy(float[][] cx, float[][] cy) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2)
      copy(cx[i2],cy[i2]);
    return cy;
  }
  public static float[][][] copy(float[][][] cx, float[][][] cy) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3)
      copy(cx[i3],cy[i3]);
    return cy;
  }
  public static float[] copy(int n1, float[] cx, float[] cy) {
    int n = 2*n1;
    while (--n>=0)
      cy[n] = cx[n];
    return cy;
  }
  public static float[][] copy(int n1, int n2, float[][] cx, float[][] cy) {
    for (int i2=0; i2<n2; ++i2)
      copy(n1,cx[i2],cy[i2]);
    return cy;
  }
  public static float[][][] copy(
    int n1, int n2, int n3, float[][][] cx, float[][][] cy) {
    for (int i3=0; i3<n3; ++i3)
      copy(n1,n2,cx[i3],cy[i3]);
    return cy;
  }
  public static float[] copy(
    int n1, 
    int j1x, float[] cx, 
    int j1y, float[] cy) {
    for (int i1=0,ix=2*j1x,iy=2*j1y; i1<n1; ++i1) {
      cy[iy++] = cx[ix++];
      cy[iy++] = cx[ix++];
    }
    return cy;
  }
  public static float[][] copy(
    int n1, int n2, 
    int j1x, int j2x, float[][] cx, 
    int j1y, int j2y, float[][] cy) {
    for (int i2=0; i2<n2; ++i2)
      copy(n1,j1x,cx[i2],j1y,cy[i2]);
    return cy;
  }
  public static float[][][] copy(
    int n1, int n2, int n3,
    int j1x, int j2x, int j3x, float[][][] cx, 
    int j1y, int j2y, int j3y, float[][][] cy) {
    for (int i3=0; i3<n3; ++i3)
      copy(n1,n2,j1x,j2x,cx[i3],j1y,j2y,cy[i3]);
    return cy;
  }

  ///////////////////////////////////////////////////////////////////////////
  // zero
  public static float[] zero(int n1) {
    return new float[2*n1];
  }
  public static float[][] zero(int n1, int n2) {
    return new float[n2][2*n1];
  }
  public static float[][][] zero(int n1, int n2, int n3) {
    return new float[n3][n2][2*n1];
  }
  public static float[] zero(float[] cx) {
    return zero(cx.length/2,cx);
  }
  public static float[][] zero(float[][] cx) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2)
      zero(cx[i2]);
    return cx;
  }
  public static float[][][] zero(float[][][] cx) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3)
      zero(cx[i3]);
    return cx;
  }
  public static float[] zero(int n1, float[] cx) {
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      cx[ir] = 0.0f;
      cx[ii] = 0.0f;
    }
    return cx;
  }
  public static float[][] zero(int n1, int n2, float[][] cx) {
    for (int i2=0; i2<n2; ++i2)
      zero(n1,cx[i2]);
    return cx;
  }
  public static float[][][] zero(int n1, int n2, int n3, float[][][] cx) {
    for (int i3=0; i3<n3; ++i3)
      zero(n1,n2,cx[i3]);
    return cx;
  }

  ///////////////////////////////////////////////////////////////////////////
  // fill
  public static float[] fill(Complex ca, int n1) {
    return fill(ca,n1,new float[2*n1]);
  }
  public static float[][] fill(Complex ca, int n1, int n2) {
    return fill(ca,n1,n2,new float[n2][2*n1]);
  }
  public static float[][][] fill(Complex ca, int n1, int n2, int n3) {
    return fill(ca,n1,n2,n3,new float[n3][n2][2*n1]);
  }
  public static float[] fill(Complex ca, float[] cx) {
    return fill(ca,cx.length/2,cx);
  }
  public static float[][] fill(Complex ca, float[][] cx) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2)
      fill(ca,cx[i2]);
    return cx;
  }
  public static float[][][] fill(Complex ca, float[][][] cx) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3)
      fill(ca,cx[i3]);
    return cx;
  }
  public static float[] fill(Complex ca, int n1, float[] cx) {
    float ar = ca.r;
    float ai = ca.i;
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      cx[ir] = ar;
      cx[ii] = ai;
    }
    return cx;
  }
  public static float[][] fill(Complex ca, int n1, int n2, float[][] cx) {
    for (int i2=0; i2<n2; ++i2)
      fill(ca,n1,cx[i2]);
    return cx;
  }
  public static float[][][] fill(
    Complex ca, int n1, int n2, int n3, float[][][] cx) {
    for (int i3=0; i3<n3; ++i3)
      fill(ca,n1,n2,cx[i3]);
    return cx;
  }

  ///////////////////////////////////////////////////////////////////////////
  // ramp
  public static float[] ramp(Complex ca, Complex cb, int n1) {
    return ramp(ca,cb,n1,new float[2*n1]);
  }
  public static float[][] ramp(
    Complex ca, Complex cb1, Complex cb2, int n1, int n2) {
    return ramp(ca,cb1,cb2,n1,n2,new float[n2][2*n1]);
  }
  public static float[][][] ramp(
    Complex ca, Complex cb1, Complex cb2, Complex cb3, 
    int n1, int n2, int n3) {
    return ramp(ca,cb1,cb2,cb3,n1,n2,n3,new float[n3][n2][2*n1]);
  }
  public static float[] ramp(Complex ca, Complex cb, float[] cx) {
    return ramp(ca,cb,cx.length/2,cx);
  }
  public static float[][] ramp(
    Complex ca, Complex cb1, Complex cb2, float[][] cx) {
    int n2 = cx.length;
    Complex ca2 = new Complex(ca);
    for (int i2=0; i2<n2; ++i2,ca2.plusEquals(cb2))
      ramp(ca2,cb1,cx[i2]);
    return cx;
  }
  public static float[][][] ramp(
    Complex ca, Complex cb1, Complex cb2, Complex cb3, float[][][] cx) {
    int n3 = cx.length;
    Complex ca3 = new Complex(ca);
    for (int i3=0; i3<n3; ++i3,ca3.plusEquals(cb3))
      ramp(ca3,cb1,cb2,cx[i3]);
    return cx;
  }
  public static float[] ramp(Complex ca, Complex cb, int n1, float[] cx) {
    float ar = ca.r;
    float ai = ca.i;
    float br = cb.r;
    float bi = cb.i;
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2,ar+=br,ai+=bi) {
      cx[ir] = ar;
      cx[ii] = ai;
    }
    return cx;
  }
  public static float[][] ramp(
    Complex ca, Complex cb1, Complex cb2, int n1, int n2, float[][] cx) {
    Complex ca2 = new Complex(ca);
    for (int i2=0; i2<n2; ++i2,ca2.plusEquals(cb2))
      ramp(ca2,cb1,n1,cx[i2]);
    return cx;
  }
  public static float[][][] ramp(
    Complex ca, Complex cb1, Complex cb2, Complex cb3, 
    int n1, int n2, int n3, float[][][] cx) {
    Complex ca3 = new Complex(ca);
    for (int i3=0; i3<n3; ++i3,ca3.plusEquals(cb3))
      ramp(ca3,cb1,cb2,n1,n2,cx[i3]);
    return cx;
  }

  ///////////////////////////////////////////////////////////////////////////
  // almostEqual
  public static boolean almostEqual(
    float tolerance, float[] cx, float[] cy) {
    return almostEqual(tolerance,cx.length/2,cx,cy);
  }
  public static boolean almostEqual(
    float tolerance, float[][] cx, float[][] cy) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2) {
      if (!almostEqual(tolerance,cx[i2],cy[i2]))
        return false;
    }
    return true;
  }
  public static boolean almostEqual(
    float tolerance, float[][][] cx, float[][][] cy) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3) {
      if (!almostEqual(tolerance,cx[i3],cy[i3]))
        return false;
    }
    return true;
  }
  public static boolean almostEqual(
    float tolerance, int n1, float[] cx, float[] cy) {
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      if (!almostEqual(tolerance,cx[ir],cy[ir]) || 
          !almostEqual(tolerance,cx[ii],cy[ii])) 
        return false;
    }
    return true;
  }
  public static boolean almostEqual(
    float tolerance, int n1, int n2, float[][] cx, float[][] cy) {
    for (int i2=0; i2<n2; ++i2) {
      if (!almostEqual(tolerance,n1,cx[i2],cy[i2]))
        return false;
    }
    return true;
  }
  public static boolean almostEqual(
    float tolerance, int n1, int n2, int n3, float[][][] cx, float[][][] cy) {
    for (int i3=0; i3<n3; ++i3) {
      if (!almostEqual(tolerance,n1,n2,cx[i3],cy[i3]))
        return false;
    }
    return true;
  }
  private static boolean almostEqual(float tolerance, float ra, float rb) {
    return (ra<rb)?rb-ra<=tolerance:ra-rb<=tolerance;
  }

  ///////////////////////////////////////////////////////////////////////////
  // add, sub, mul, div
  public static float[] add(float[] cx, float[] cy) {
    return _add.apply(cx,cy);
  }
  public static float[] add(Complex ca, float[] cy) {
    return _add.apply(ca,cy);
  }
  public static float[] add(float[] cx, Complex cb) {
    return _add.apply(cx,cb);
  }
  public static float[][] add(float[][] cx, float[][] cy) {
    return _add.apply(cx,cy);
  }
  public static float[][] add(Complex ca, float[][] cy) {
    return _add.apply(ca,cy);
  }
  public static float[][] add(float[][] cx, Complex cb) {
    return _add.apply(cx,cb);
  }
  public static float[][][] add(float[][][] cx, float[][][] cy) {
    return _add.apply(cx,cy);
  }
  public static float[][][] add(Complex ca, float[][][] cy) {
    return _add.apply(ca,cy);
  }
  public static float[][][] add(float[][][] cx, Complex cb) {
    return _add.apply(cx,cb);
  }
  public static float[] add(float[] cx, float[] cy, float[] cz) {
    return _add.apply(cx,cy,cz);
  }
  public static float[] add(Complex ca, float[] cy, float[] cz) {
    return _add.apply(ca,cy,cz);
  }
  public static float[] add(float[] cx, Complex cb, float[] cz) {
    return _add.apply(cx,cb,cz);
  }
  public static float[][] add(float[][] cx, float[][] cy, float[][] cz) {
    return _add.apply(cx,cy,cz);
  }
  public static float[][] add(Complex ca, float[][] cy, float[][] cz) {
    return _add.apply(ca,cy,cz);
  }
  public static float[][] add(float[][] cx, Complex cb, float[][] cz) {
    return _add.apply(cx,cb,cz);
  }
  public static float[][][] add(
    float[][][] cx, float[][][] cy, float[][][] cz) {
    return _add.apply(cx,cy,cz);
  }
  public static float[][][] add(
    Complex ca, float[][][] cy, float[][][] cz) {
    return _add.apply(ca,cy,cz);
  }
  public static float[][][] add(
    float[][][] cx, Complex cb, float[][][] cz) {
    return _add.apply(cx,cb,cz);
  }
  public static float[] add(int n1, float[] cx, float[] cy) {
    return _add.apply(n1,cx,cy);
  }
  public static float[] add(int n1, Complex ca, float[] cy) {
    return _add.apply(n1,ca,cy);
  }
  public static float[] add(int n1, float[] cx, Complex cb) {
    return _add.apply(n1,cx,cb);
  }
  public static float[][] add(int n1, int n2, float[][] cx, float[][] cy) {
    return _add.apply(n1,n2,cx,cy);
  }
  public static float[][] add(int n1, int n2, Complex ca, float[][] cy) {
    return _add.apply(n1,n2,ca,cy);
  }
  public static float[][] add(int n1, int n2, float[][] cx, Complex cb) {
    return _add.apply(n1,n2,cx,cb);
  }
  public static float[][][] add(
    int n1, int n2, int n3, float[][][] cx, float[][][] cy) {
    return _add.apply(n1,n2,n3,cx,cy);
  }
  public static float[][][] add(
    int n1, int n2, int n3, Complex ca, float[][][] cy) {
    return _add.apply(n1,n2,n3,ca,cy);
  }
  public static float[][][] add(
    int n1, int n2, int n3, float[][][] cx, Complex cb) {
    return _add.apply(n1,n2,n3,cx,cb);
  }
  public static float[] add(int n1, float[] cx, float[] cy, float[] cz) {
    return _add.apply(n1,cx,cy,cz);
  }
  public static float[] add(int n1, Complex ca, float[] cy, float[] cz) {
    return _add.apply(n1,ca,cy,cz);
  }
  public static float[] add(int n1, float[] cx, Complex cb, float[] cz) {
    return _add.apply(n1,cx,cb,cz);
  }
  public static float[][] add(
    int n1, int n2, float[][] cx, float[][] cy, float[][] cz) {
    return _add.apply(n1,n2,cx,cy,cz);
  }
  public static float[][] add(
    int n1, int n2, Complex ca, float[][] cy, float[][] cz) {
    return _add.apply(n1,n2,ca,cy,cz);
  }
  public static float[][] add(
    int n1, int n2, float[][] cx, Complex cb, float[][] cz) {
    return _add.apply(n1,n2,cx,cb,cz);
  }
  public static float[][][] add(
    int n1, int n2, int n3, float[][][] cx, float[][][] cy, float[][][] cz) {
    return _add.apply(n1,n2,n3,cx,cy,cz);
  }
  public static float[][][] add(
    int n1, int n2, int n3, Complex ca, float[][][] cy, float[][][] cz) {
    return _add.apply(n1,n2,n3,ca,cy,cz);
  }
  public static float[][][] add(
    int n1, int n2, int n3, float[][][] cx, Complex cb, float[][][] cz) {
    return _add.apply(n1,n2,n3,cx,cb,cz);
  }
  public static float[] sub(float[] cx, float[] cy) {
    return _sub.apply(cx,cy);
  }
  public static float[] sub(Complex ca, float[] cy) {
    return _sub.apply(ca,cy);
  }
  public static float[] sub(float[] cx, Complex cb) {
    return _sub.apply(cx,cb);
  }
  public static float[][] sub(float[][] cx, float[][] cy) {
    return _sub.apply(cx,cy);
  }
  public static float[][] sub(Complex ca, float[][] cy) {
    return _sub.apply(ca,cy);
  }
  public static float[][] sub(float[][] cx, Complex cb) {
    return _sub.apply(cx,cb);
  }
  public static float[][][] sub(float[][][] cx, float[][][] cy) {
    return _sub.apply(cx,cy);
  }
  public static float[][][] sub(Complex ca, float[][][] cy) {
    return _sub.apply(ca,cy);
  }
  public static float[][][] sub(float[][][] cx, Complex cb) {
    return _sub.apply(cx,cb);
  }
  public static float[] sub(float[] cx, float[] cy, float[] cz) {
    return _sub.apply(cx,cy,cz);
  }
  public static float[] sub(Complex ca, float[] cy, float[] cz) {
    return _sub.apply(ca,cy,cz);
  }
  public static float[] sub(float[] cx, Complex cb, float[] cz) {
    return _sub.apply(cx,cb,cz);
  }
  public static float[][] sub(float[][] cx, float[][] cy, float[][] cz) {
    return _sub.apply(cx,cy,cz);
  }
  public static float[][] sub(Complex ca, float[][] cy, float[][] cz) {
    return _sub.apply(ca,cy,cz);
  }
  public static float[][] sub(float[][] cx, Complex cb, float[][] cz) {
    return _sub.apply(cx,cb,cz);
  }
  public static float[][][] sub(
    float[][][] cx, float[][][] cy, float[][][] cz) {
    return _sub.apply(cx,cy,cz);
  }
  public static float[][][] sub(
    Complex ca, float[][][] cy, float[][][] cz) {
    return _sub.apply(ca,cy,cz);
  }
  public static float[][][] sub(
    float[][][] cx, Complex cb, float[][][] cz) {
    return _sub.apply(cx,cb,cz);
  }
  public static float[] sub(int n1, float[] cx, float[] cy) {
    return _sub.apply(n1,cx,cy);
  }
  public static float[] sub(int n1, Complex ca, float[] cy) {
    return _sub.apply(n1,ca,cy);
  }
  public static float[] sub(int n1, float[] cx, Complex cb) {
    return _sub.apply(n1,cx,cb);
  }
  public static float[][] sub(int n1, int n2, float[][] cx, float[][] cy) {
    return _sub.apply(n1,n2,cx,cy);
  }
  public static float[][] sub(int n1, int n2, Complex ca, float[][] cy) {
    return _sub.apply(n1,n2,ca,cy);
  }
  public static float[][] sub(int n1, int n2, float[][] cx, Complex cb) {
    return _sub.apply(n1,n2,cx,cb);
  }
  public static float[][][] sub(
    int n1, int n2, int n3, float[][][] cx, float[][][] cy) {
    return _sub.apply(n1,n2,n3,cx,cy);
  }
  public static float[][][] sub(
    int n1, int n2, int n3, Complex ca, float[][][] cy) {
    return _sub.apply(n1,n2,n3,ca,cy);
  }
  public static float[][][] sub(
    int n1, int n2, int n3, float[][][] cx, Complex cb) {
    return _sub.apply(n1,n2,n3,cx,cb);
  }
  public static float[] sub(int n1, float[] cx, float[] cy, float[] cz) {
    return _sub.apply(n1,cx,cy,cz);
  }
  public static float[] sub(int n1, Complex ca, float[] cy, float[] cz) {
    return _sub.apply(n1,ca,cy,cz);
  }
  public static float[] sub(int n1, float[] cx, Complex cb, float[] cz) {
    return _sub.apply(n1,cx,cb,cz);
  }
  public static float[][] sub(
    int n1, int n2, float[][] cx, float[][] cy, float[][] cz) {
    return _sub.apply(n1,n2,cx,cy,cz);
  }
  public static float[][] sub(
    int n1, int n2, Complex ca, float[][] cy, float[][] cz) {
    return _sub.apply(n1,n2,ca,cy,cz);
  }
  public static float[][] sub(
    int n1, int n2, float[][] cx, Complex cb, float[][] cz) {
    return _sub.apply(n1,n2,cx,cb,cz);
  }
  public static float[][][] sub(
    int n1, int n2, int n3, float[][][] cx, float[][][] cy, float[][][] cz) {
    return _sub.apply(n1,n2,n3,cx,cy,cz);
  }
  public static float[][][] sub(
    int n1, int n2, int n3, Complex ca, float[][][] cy, float[][][] cz) {
    return _sub.apply(n1,n2,n3,ca,cy,cz);
  }
  public static float[][][] sub(
    int n1, int n2, int n3, float[][][] cx, Complex cb, float[][][] cz) {
    return _sub.apply(n1,n2,n3,cx,cb,cz);
  }

  ///////////////////////////////////////////////////////////////////////////
  // binary operations; e.g., add, sub, mul, div, ...
  private static abstract class Binary {
    float[] apply(float[] cx, float[] cy) {
      int n1 = cx.length/2;
      float[] cz = new float[2*n1];
      return apply(n1,cx,cy,cz);
    }
    float[] apply(Complex ca, float[] cy) {
      int n1 = cy.length/2;
      float[] cz = new float[2*n1];
      return apply(n1,ca,cy,cz);
    }
    float[] apply(float[] cx, Complex cb) {
      int n1 = cx.length/2;
      float[] cz = new float[2*n1];
      return apply(n1,cx,cb,cz);
    }
    float[][] apply(float[][] cx, float[][] cy) {
      int n2 = cx.length;
      float[][] cz = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        cz[i2] = apply(cx[i2],cy[i2]);
      return cz;
    }
    float[][] apply(Complex ca, float[][] cy) {
      int n2 = cy.length;
      float[][] cz = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        cz[i2] = apply(ca,cy[i2]);
      return cz;
    }
    float[][] apply(float[][] cx, Complex cb) {
      int n2 = cx.length;
      float[][] cz = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        cz[i2] = apply(cx[i2],cb);
      return cz;
    }
    float[][][] apply(float[][][] cx, float[][][] cy) {
      int n3 = cx.length;
      float[][][] cz = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        cz[i3] = apply(cx[i3],cy[i3]);
      return cz;
    }
    float[][][] apply(Complex ca, float[][][] cy) {
      int n3 = cy.length;
      float[][][] cz = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        cz[i3] = apply(ca,cy[i3]);
      return cz;
    }
    float[][][] apply(float[][][] cx, Complex cb) {
      int n3 = cx.length;
      float[][][] cz = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        cz[i3] = apply(cx[i3],cb);
      return cz;
    }
    float[] apply(float[] cx, float[] cy, float[] cz) {
      return apply(cx.length/2,cx,cy,cz);
    }
    float[] apply(Complex ca, float[] cy, float[] cz) {
      return apply(cy.length/2,ca,cy,cz);
    }
    float[] apply(float[] cx, Complex cb, float[] cz) {
      return apply(cx.length/2,cx,cb,cz);
    }
    float[][] apply(float[][] cx, float[][] cy, float[][] cz) {
      int n2 = cx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(cx[i2],cy[i2],cz[i2]);
      return cz;
    }
    float[][] apply(Complex ca, float[][] cy, float[][] cz) {
      int n2 = cy.length;
      for (int i2=0; i2<n2; ++i2)
        apply(ca,cy[i2],cz[i2]);
      return cz;
    }
    float[][] apply(float[][] cx, Complex cb, float[][] cz) {
      int n2 = cx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(cx[i2],cb,cz[i2]);
      return cz;
    }
    float[][][] apply(float[][][] cx, float[][][] cy, float[][][] cz) {
      int n3 = cx.length;
      for (int i3=0; i3<n3; ++i3)
        apply(cx[i3],cy[i3],cz[i3]);
      return cz;
    }
    float[][][] apply(Complex ca, float[][][] cy, float[][][] cz) {
      int n3 = cy.length;
      for (int i3=0; i3<n3; ++i3)
        apply(ca,cy[i3],cz[i3]);
      return cz;
    }
    float[][][] apply(float[][][] cx, Complex cb, float[][][] cz) {
      int n3 = cx.length;
      for (int i3=0; i3<n3; ++i3)
        apply(cx[i3],cb,cz[i3]);
      return cz;
    }
    float[] apply(int n1, float[] cx, float[] cy) {
      return apply(n1,cx,cy,new float[2*n1]);
    }
    float[] apply(int n1, Complex ca, float[] cy) {
      return apply(n1,ca,cy,new float[2*n1]);
    }
    float[] apply(int n1, float[] cx, Complex cb) {
      return apply(n1,cx,cb,new float[2*n1]);
    }
    float[][] apply(
      int n1, int n2, float[][] cx, float[][] cy) {
      return apply(n1,n2,cx,cy,new float[n2][2*n1]);
    }
    float[][] apply(
      int n1, int n2, Complex ca, float[][] cy) {
      return apply(n1,n2,ca,cy,new float[n2][2*n1]);
    }
    float[][] apply(
      int n1, int n2, float[][] cx, Complex cb) {
      return apply(n1,n2,cx,cb,new float[n2][2*n1]);
    }
    float[][][] apply(
      int n1, int n2, int n3, float[][][] cx, float[][][] cy) {
      return apply(n1,n2,n3,cx,cy,new float[n3][n2][2*n1]);
    }
    float[][][] apply(
      int n1, int n2, int n3, Complex ca, float[][][] cy) {
      return apply(n1,n2,n3,ca,cy,new float[n3][n2][2*n1]);
    }
    float[][][] apply(
      int n1, int n2, int n3, float[][][] cx, Complex cb) {
      return apply(n1,n2,n3,cx,cb,new float[n3][n2][2*n1]);
    }
    abstract float[] apply(int n1, float[] cx, float[] cy, float[] cz);
    abstract float[] apply(int n1, Complex ca, float[] cy, float[] cz);
    abstract float[] apply(int n1, float[] cx, Complex cb, float[] cz);
    float[][] apply(
      int n1, int n2, float[][] cx, float[][] cy, float[][] cz) {
      for (int i2=0; i2<n2; ++i2)
        apply(n1,cx[i2],cy[i2],cz[i2]);
      return cz;
    }
    float[][] apply(
      int n1, int n2, Complex ca, float[][] cy, float[][] cz) {
      for (int i2=0; i2<n2; ++i2)
        apply(n1,ca,cy[i2],cz[i2]);
      return cz;
    }
    float[][] apply(
      int n1, int n2, float[][] cx, Complex cb, float[][] cz) {
      for (int i2=0; i2<n2; ++i2)
        apply(n1,cx[i2],cb,cz[i2]);
      return cz;
    }
    float[][][] apply(
      int n1, int n2, int n3, float[][][] cx, float[][][] cy, float[][][] cz) {
      for (int i3=0; i3<n3; ++i3)
        apply(n1,n2,cx[i3],cy[i3],cz[i3]);
      return cz;
    }
    float[][][] apply(
      int n1, int n2, int n3, Complex ca, float[][][] cy, float[][][] cz) {
      for (int i3=0; i3<n3; ++i3)
        apply(n1,n2,ca,cy[i3],cz[i3]);
      return cz;
    }
    float[][][] apply(
      int n1, int n2, int n3, float[][][] cx, Complex cb, float[][][] cz) {
      for (int i3=0; i3<n3; ++i3)
        apply(n1,n2,cx[i3],cb,cz[i3]);
      return cz;
    }
  }
  private static Binary _add = new Binary() {
    float[] apply(int n1, float[] cx, float[] cy, float[] cz) {
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = cx[ir]+cy[ir];
        cz[ii] = cx[ii]+cy[ii];
      }
      return cz;
    }
    float[] apply(int n1, Complex ca, float[] cy, float[] cz) {
      float ar = ca.r;
      float ai = ca.i;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = ar+cy[ir];
        cz[ii] = ai+cy[ii];
      }
      return cz;
    }
    float[] apply(int n1, float[] cx, Complex cb, float[] cz) {
      float br = cb.r;
      float bi = cb.i;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = cx[ir]+br;
        cz[ii] = cx[ii]+bi;
      }
      return cz;
    }
  };
  private static Binary _sub = new Binary() {
    float[] apply(int n1, float[] cx, float[] cy, float[] cz) {
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = cx[ir]-cy[ir];
        cz[ii] = cx[ii]-cy[ii];
      }
      return cz;
    }
    float[] apply(int n1, Complex ca, float[] cy, float[] cz) {
      float ar = ca.r;
      float ai = ca.i;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = ar-cy[ir];
        cz[ii] = ai-cy[ii];
      }
      return cz;
    }
    float[] apply(int n1, float[] cx, Complex cb, float[] cz) {
      float br = cb.r;
      float bi = cb.i;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = cx[ir]-br;
        cz[ii] = cx[ii]-bi;
      }
      return cz;
    }
  };
}
