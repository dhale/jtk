/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import java.util.Random;
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
    float[] cy = new float[2*n1];
    copy(n1,cx,cy);
    return cy;
  }
  public static float[][] copy(int n1, int n2, float[][] cx) {
    float[][] cy = new float[n2][2*n1];
    copy(n1,n2,cx,cy);
    return cy;
  }
  public static float[][][] copy(int n1, int n2, int n3, float[][][] cx) {
    float[][][] cy = new float[n3][n2][2*n1];
    copy(n1,n2,n3,cx,cy);
    return cy;
  }
  public static void copy(float[] cx, float[] cy) {
    int n1 = cx.length/2;
    copy(n1,cx,cy);
  }
  public static void copy(float[][] cx, float[][] cy) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2)
      copy(cx[i2],cy[i2]);
  }
  public static void copy(float[][][] cx, float[][][] cy) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3)
      copy(cx[i3],cy[i3]);
  }
  public static void copy(int n1, float[] cx, float[] cy) {
    int n = 2*n1;
    while (--n>=0)
      cy[n] = cx[n];
  }
  public static void copy(int n1, int n2, float[][] cx, float[][] cy) {
    for (int i2=0; i2<n2; ++i2)
      copy(n1,cx[i2],cy[i2]);
  }
  public static void copy(
    int n1, int n2, int n3, float[][][] cx, float[][][] cy) {
    for (int i3=0; i3<n3; ++i3)
      copy(n1,n2,cx[i3],cy[i3]);
  }
  public static void copy(
    int n1, 
    int j1x, float[] cx, 
    int j1y, float[] cy) {
    for (int i1=0,ix=2*j1x,iy=2*j1y; i1<n1; ++i1) {
      cy[iy++] = cx[ix++];
      cy[iy++] = cx[ix++];
    }
  }
  public static void copy(
    int n1, int n2, 
    int j1x, int j2x, float[][] cx, 
    int j1y, int j2y, float[][] cy) {
    for (int i2=0; i2<n2; ++i2)
      copy(n1,j1x,cx[i2],j1y,cy[i2]);
  }
  public static void copy(
    int n1, int n2, int n3,
    int j1x, int j2x, int j3x, float[][][] cx, 
    int j1y, int j2y, int j3y, float[][][] cy) {
    for (int i3=0; i3<n3; ++i3)
      copy(n1,n2,j1x,j2x,cx[i3],j1y,j2y,cy[i3]);
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
  public static void zero(float[] cx) {
    int n = cx.length;
    while (--n>=0)
      cx[n] = 0.0f;
  }
  public static void zero(float[][] cx) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2)
      zero(cx[i2]);
  }
  public static void zero(float[][][] cx) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3)
      zero(cx[i3]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // fill
  public static float[] fill(Complex ca, int n1) {
    float[] cx = new float[2*n1];
    fill(ca,cx);
    return cx;
  }
  public static float[][] fill(Complex ca, int n1, int n2) {
    float[][] cx = new float[n2][2*n1];
    fill(ca,cx);
    return cx;
  }
  public static float[][][] fill(Complex ca, int n1, int n2, int n3) {
    float[][][] cx = new float[n3][n2][2*n1];
    fill(ca,cx);
    return cx;
  }
  public static void fill(Complex ca, float[] cx) {
    int n1 = cx.length/2;
    float ar = ca.r;
    float ai = ca.i;
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      cx[ir] = ar;
      cx[ii] = ai;
    }
  }
  public static void fill(Complex ca, float[][] cx) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2)
      fill(ca,cx[i2]);
  }
  public static void fill(Complex ca, float[][][] cx) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3)
      fill(ca,cx[i3]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // ramp
  public static float[] ramp(Complex ca, Complex cb, int n1) {
    float[] cx = new float[2*n1];
    ramp(ca,cb,cx);
    return cx;
  }
  public static float[][] ramp(
    Complex ca, Complex cb1, Complex cb2, int n1, int n2) {
    float[][] cx = new float[n2][2*n1];
    ramp(ca,cb1,cb2,cx);
    return cx;
  }
  public static float[][][] ramp(
    Complex ca, Complex cb1, Complex cb2, Complex cb3, 
    int n1, int n2, int n3) {
    float[][][] cx = new float[n3][n2][2*n1];
    ramp(ca,cb1,cb2,cb3,cx);
    return cx;
  }
  public static void ramp(Complex ca, Complex cb, float[] cx) {
    int n1 = cx.length/2;
    float ar = ca.r;
    float ai = ca.i;
    float br = cb.r;
    float bi = cb.i;
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2,ar+=br,ai+=bi) {
      cx[ir] = ar;
      cx[ii] = ai;
    }
  }
  public static void ramp(
    Complex ca, Complex cb1, Complex cb2, float[][] cx) {
    int n2 = cx.length;
    Complex ca2 = new Complex(ca);
    for (int i2=0; i2<n2; ++i2,ca2.plusEquals(cb2))
      ramp(ca2,cb1,cx[i2]);
  }
  public static void ramp(
    Complex ca, Complex cb1, Complex cb2, Complex cb3, float[][][] cx) {
    int n3 = cx.length;
    Complex ca3 = new Complex(ca);
    for (int i3=0; i3<n3; ++i3,ca3.plusEquals(cb3))
      ramp(ca3,cb1,cb2,cx[i3]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // rand
  public static float[] rand(int n1) {
    return rand(_random,n1);
  }
  public static float[][] rand(int n1, int n2) {
    return rand(_random,n1,n2);
  }
  public static float[][][] rand(int n1, int n2, int n3) {
    return rand(_random,n1,n2,n3);
  }
  public static void rand(float[] cx) {
    rand(_random,cx);
  }
  public static void rand(float[][] cx) {
    rand(_random,cx);
  }
  public static void rand(float[][][] cx) {
    rand(_random,cx);
  }
  public static float[] rand(Random random, int n1) {
    float[] cx = new float[n1];
    rand(random,cx);
    return cx;
  }
  public static float[][] rand(Random random, int n1, int n2) {
    float[][] cx = new float[n2][n1];
    rand(random,cx);
    return cx;
  }
  public static float[][][] rand(Random random, int n1, int n2, int n3) {
    float[][][] cx = new float[n3][n2][n1];
    rand(random,cx);
    return cx;
  }
  public static void rand(Random random, float[] cx) {
    int n1 = cx.length/2;
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      cx[ir] = random.nextFloat();
      cx[ii] = random.nextFloat();
    }
  }
  public static void rand(Random random, float[][] cx) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2)
      rand(random,cx[i2]);
  }
  public static void rand(Random random, float[][][] cx) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3)
      rand(random,cx[i3]);
  }
  private static Random _random = new Random();

  ///////////////////////////////////////////////////////////////////////////
  // equal
  public static boolean equal(float[] cx, float[] cy) {
    int n1 = cx.length/2;
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      if (cx[ir]!=cy[ir] || cx[ii]!=cy[ii]) 
        return false;
    }
    return true;
  }
  public static boolean equal(float[][] cx, float[][] cy) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2) {
      if (!equal(cx[i2],cy[i2]))
        return false;
    }
    return true;
  }
  public static boolean equal(float[][][] cx, float[][][] cy) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3) {
      if (!equal(cx[i3],cy[i3]))
        return false;
    }
    return true;
  }
  public static boolean equal(float tolerance, float[] cx, float[] cy) {
    int n1 = cx.length/2;
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      if (!equal(tolerance,cx[ir],cy[ir]) || 
          !equal(tolerance,cx[ii],cy[ii])) 
        return false;
    }
    return true;
  }
  public static boolean equal(float tolerance, float[][] cx, float[][] cy) {
    int n2 = cx.length;
    for (int i2=0; i2<n2; ++i2) {
      if (!equal(tolerance,cx[i2],cy[i2]))
        return false;
    }
    return true;
  }
  public static boolean equal(float tolerance, float[][][] cx, float[][][] cy) {
    int n3 = cx.length;
    for (int i3=0; i3<n3; ++i3) {
      if (!equal(tolerance,cx[i3],cy[i3]))
        return false;
    }
    return true;
  }
  private static boolean equal(float tolerance, float ra, float rb) {
    return (ra<rb)?rb-ra<=tolerance:ra-rb<=tolerance;
  }

  ///////////////////////////////////////////////////////////////////////////
  // complex-to-complex
  public static float[] neg(float[] cx) {
    return _neg.apply(cx);
  }
  public static float[][] neg(float[][] cx) {
    return _neg.apply(cx);
  }
  public static float[][][] neg(float[][][] cx) {
    return _neg.apply(cx);
  }
  public static void neg(float[] cx, float[] cy) {
    _neg.apply(cx,cy);
  }
  public static void neg(float[][] cx, float[][] cy) {
    _neg.apply(cx,cy);
  }
  public static void neg(float[][][] cx, float[][][] cy) {
    _neg.apply(cx,cy);
  }
  public static float[] conj(float[] cx) {
    return _conj.apply(cx);
  }
  public static float[][] conj(float[][] cx) {
    return _conj.apply(cx);
  }
  public static float[][][] conj(float[][][] cx) {
    return _conj.apply(cx);
  }
  public static void conj(float[] cx, float[] cy) {
    _conj.apply(cx,cy);
  }
  public static void conj(float[][] cx, float[][] cy) {
    _conj.apply(cx,cy);
  }
  public static void conj(float[][][] cx, float[][][] cy) {
    _conj.apply(cx,cy);
  }
  public static float[] exp(float[] cx) {
    return _exp.apply(cx);
  }
  public static float[][] exp(float[][] cx) {
    return _exp.apply(cx);
  }
  public static float[][][] exp(float[][][] cx) {
    return _exp.apply(cx);
  }
  public static void exp(float[] cx, float[] cy) {
    _exp.apply(cx,cy);
  }
  public static void exp(float[][] cx, float[][] cy) {
    _exp.apply(cx,cy);
  }
  public static void exp(float[][][] cx, float[][][] cy) {
    _exp.apply(cx,cy);
  }
  private static abstract class ComplexToComplex {
    float[] apply(float[] cx) {
      int n1 = cx.length/2;
      float[] cy = new float[2*n1];
      apply(cx,cy);
      return cy;
    }
    float[][] apply(float[][] cx) {
      int n2 = cx.length;
      float[][] cy = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        cy[i2] = apply(cx[i2]);
      return cy;
    }
    float[][][] apply(float[][][] cx) {
      int n3 = cx.length;
      float[][][] cy = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        cy[i3] = apply(cx[i3]);
      return cy;
    }
    abstract void apply(float[] cx, float[] cy);
    void apply(float[][] cx, float[][] cy) {
      int n2 = cx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(cx[i2],cy[i2]);
    }
    void apply(float[][][] cx, float[][][] cy) {
      int n3 = cx.length;
      for (int i3=0; i3<n3; ++i3)
        apply(cx[i3],cy[i3]);
    }
  }
  private static ComplexToComplex _neg = new ComplexToComplex() {
    void apply(float[] cx, float[] cy) {
      int n1 = cx.length;
      for (int i1=0; i1<n1; ++i1)
        cy[i1] = -cx[i1];
    }
  };
  private static ComplexToComplex _conj = new ComplexToComplex() {
    void apply(float[] cx, float[] cy) {
      int n1 = cx.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cy[ir] =  cx[ir];
        cy[ii] = -cx[ii];
      }
    }
  };
  private static ComplexToComplex _exp = new ComplexToComplex() {
    void apply(float[] cx, float[] cy) {
      Complex ct = new Complex();
      int n1 = cx.length/2;
      for (int i1=0,ir=0,ii=1; i1<n1; ++i1,ir+=2,ii+=2) {
        ct.r = cx[ir];
        ct.i = cx[ii];
        Complex ce = Complex.exp(ct);
        cy[ir] = ce.r;
        cy[ii] = ce.i;
      }
    }
  };

  ///////////////////////////////////////////////////////////////////////////
  // complex-to-real
  public static float[] real(float[] cx) {
    return _real.apply(cx);
  }
  public static float[][] real(float[][] cx) {
    return _real.apply(cx);
  }
  public static float[][][] real(float[][][] cx) {
    return _real.apply(cx);
  }
  public static void real(float[] cx, float[] cy) {
    _real.apply(cx,cy);
  }
  public static void real(float[][] cx, float[][] cy) {
    _real.apply(cx,cy);
  }
  public static void real(float[][][] cx, float[][][] cy) {
    _real.apply(cx,cy);
  }
  public static float[] imag(float[] cx) {
    return _imag.apply(cx);
  }
  public static float[][] imag(float[][] cx) {
    return _imag.apply(cx);
  }
  public static float[][][] imag(float[][][] cx) {
    return _imag.apply(cx);
  }
  public static void imag(float[] cx, float[] cy) {
    _imag.apply(cx,cy);
  }
  public static void imag(float[][] cx, float[][] cy) {
    _imag.apply(cx,cy);
  }
  public static void imag(float[][][] cx, float[][][] cy) {
    _imag.apply(cx,cy);
  }
  public static float[] abs(float[] cx) {
    return _abs.apply(cx);
  }
  public static float[][] abs(float[][] cx) {
    return _abs.apply(cx);
  }
  public static float[][][] abs(float[][][] cx) {
    return _abs.apply(cx);
  }
  public static void abs(float[] cx, float[] cy) {
    _abs.apply(cx,cy);
  }
  public static void abs(float[][] cx, float[][] cy) {
    _abs.apply(cx,cy);
  }
  public static void abs(float[][][] cx, float[][][] cy) {
    _abs.apply(cx,cy);
  }
  public static float[] arg(float[] cx) {
    return _arg.apply(cx);
  }
  public static float[][] arg(float[][] cx) {
    return _arg.apply(cx);
  }
  public static float[][][] arg(float[][][] cx) {
    return _arg.apply(cx);
  }
  public static void arg(float[] cx, float[] cy) {
    _arg.apply(cx,cy);
  }
  public static void arg(float[][] cx, float[][] cy) {
    _arg.apply(cx,cy);
  }
  public static void arg(float[][][] cx, float[][][] cy) {
    _arg.apply(cx,cy);
  }
  public static float[] norm(float[] cx) {
    return _norm.apply(cx);
  }
  public static float[][] norm(float[][] cx) {
    return _norm.apply(cx);
  }
  public static float[][][] norm(float[][][] cx) {
    return _norm.apply(cx);
  }
  public static void norm(float[] cx, float[] cy) {
    _norm.apply(cx,cy);
  }
  public static void norm(float[][] cx, float[][] cy) {
    _norm.apply(cx,cy);
  }
  public static void norm(float[][][] cx, float[][][] cy) {
    _norm.apply(cx,cy);
  }
  private static abstract class ComplexToReal {
    float[] apply(float[] cx) {
      int n1 = cx.length/2;
      float[] cy = new float[n1];
      apply(cx,cy);
      return cy;
    }
    float[][] apply(float[][] cx) {
      int n2 = cx.length;
      float[][] cy = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        cy[i2] = apply(cx[i2]);
      return cy;
    }
    float[][][] apply(float[][][] cx) {
      int n3 = cx.length;
      float[][][] cy = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        cy[i3] = apply(cx[i3]);
      return cy;
    }
    abstract void apply(float[] cx, float[] ry);
    void apply(float[][] cx, float[][] ry) {
      int n2 = cx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(cx[i2],ry[i2]);
    }
    void apply(float[][][] cx, float[][][] ry) {
      int n3 = cx.length;
      for (int i3=0; i3<n3; ++i3)
        apply(cx[i3],ry[i3]);
    }
  }
  private static ComplexToReal _real = new ComplexToReal() {
    void apply(float[] cx, float[] ry) {
      int n1 = cx.length/2;
      for (int i1=0,ir=0; i1<n1; ++i1,ir+=2)
        ry[i1] = cx[ir];
    }
  };
  private static ComplexToReal _imag = new ComplexToReal() {
    void apply(float[] cx, float[] ry) {
      int n1 = cx.length/2;
      for (int i1=0,ii=1; i1<n1; ++i1,ii+=2)
        ry[i1] = cx[ii];
    }
  };
  private static ComplexToReal _abs = new ComplexToReal() {
    void apply(float[] cx, float[] ry) {
      Complex ct = new Complex();
      int n1 = cx.length/2;
      for (int i1=0,ir=0,ii=1; i1<n1; ++i1,ir+=2,ii+=2) {
        ct.r = cx[ir];
        ct.i = cx[ii];
        ry[i1] = Complex.abs(ct);
      }
    }
  };
  private static ComplexToReal _arg = new ComplexToReal() {
    void apply(float[] cx, float[] ry) {
      Complex ct = new Complex();
      int n1 = cx.length/2;
      for (int i1=0,ir=0,ii=1; i1<n1; ++i1,ir+=2,ii+=2) {
        ct.r = cx[ir];
        ct.i = cx[ii];
        ry[i1] = Complex.arg(ct);
      }
    }
  };
  private static ComplexToReal _norm = new ComplexToReal() {
    void apply(float[] cx, float[] ry) {
      int n1 = cx.length/2;
      for (int i1=0,ir=0,ii=1; i1<n1; ++i1,ir+=2,ii+=2) {
        float cr = cx[ir];
        float ci = cx[ii];
        ry[i1] = cr*cr+ci*ci;
      }
    }
  };

  ///////////////////////////////////////////////////////////////////////////
  // real-to-complex
  public static float[] complex(float[] rx, float[] ry) {
    return _complex.apply(rx,ry);
  }
  public static float[][] complex(float[][] rx, float[][] ry) {
    return _complex.apply(rx,ry);
  }
  public static float[][][] complex(float[][][] rx, float[][][] ry) {
    return _complex.apply(rx,ry);
  }
  public static void complex(float[] rx, float[] ry, float[] cz) {
    _complex.apply(rx,ry,cz);
  }
  public static void complex(float[][] rx, float[][] ry, float[][] cz) {
    _complex.apply(rx,ry,cz);
  }
  public static void complex(float[][][] rx, float[][][] ry, float[][][] cz) {
    _complex.apply(rx,ry,cz);
  }
  public static float[] polar(float[] rx, float[] ry) {
    return _polar.apply(rx,ry);
  }
  public static float[][] polar(float[][] rx, float[][] ry) {
    return _polar.apply(rx,ry);
  }
  public static float[][][] polar(float[][][] rx, float[][][] ry) {
    return _polar.apply(rx,ry);
  }
  public static void polar(float[] rx, float[] ry, float[] cz) {
    _polar.apply(rx,ry,cz);
  }
  public static void polar(float[][] rx, float[][] ry, float[][] cz) {
    _polar.apply(rx,ry,cz);
  }
  public static void polar(float[][][] rx, float[][][] ry, float[][][] cz) {
    _complex.apply(rx,ry,cz);
  }
  private static abstract class RealToComplex {
    float[] apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      float[] cz = new float[2*n1];
      apply(rx,ry,cz);
      return cz;
    }
    float[][] apply(float[][] rx, float[][] ry) {
      int n2 = rx.length;
      float[][] cz = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        cz[i2] = apply(rx[i2],ry[i2]);
      return cz;
    }
    float[][][] apply(float[][][] rx, float[][][] ry) {
      int n3 = rx.length;
      float[][][] cz = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        cz[i3] = apply(rx[i3],ry[i3]);
      return cz;
    }
    abstract void apply(float[] rx, float[] ry, float[] cz);
    void apply(float[][] rx, float[][] ry, float[][] cz) {
      int n2 = rx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(rx[i2],ry[i2],cz[i2]);
    }
    void apply(float[][][] rx, float[][][] ry, float[][][] cz) {
      int n3 = cz.length;
      for (int i3=0; i3<n3; ++i3)
        apply(rx[i3],ry[i3],cz[i3]);
    }
  }
  private static RealToComplex _complex = new RealToComplex() {
    void apply(float[] rx, float[] ry, float[] cz) {
      int n1 = rx.length;
      for (int i1=0,ir=0,ii=1; i1<n1; ++i1,ir+=2,ii+=2) {
        cz[ir] = rx[i1];
        cz[ii] = ry[i1];
      }
    }
  };
  private static RealToComplex _polar = new RealToComplex() {
    void apply(float[] rx, float[] ry, float[] cz) {
      int n1 = rx.length;
      for (int i1=0,ir=0,ii=1; i1<n1; ++i1,ir+=2,ii+=2) {
        float r = rx[i1];
        float a = ry[i1];
        cz[ir] = r*cos(a);
        cz[ii] = r*sin(a);
      }
    }
  };

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
  public static void add(float[] cx, float[] cy, float[] cz) {
    _add.apply(cx,cy,cz);
  }
  public static void add(Complex ca, float[] cy, float[] cz) {
    _add.apply(ca,cy,cz);
  }
  public static void add(float[] cx, Complex cb, float[] cz) {
    _add.apply(cx,cb,cz);
  }
  public static void add(float[][] cx, float[][] cy, float[][] cz) {
    _add.apply(cx,cy,cz);
  }
  public static void add(Complex ca, float[][] cy, float[][] cz) {
    _add.apply(ca,cy,cz);
  }
  public static void add(float[][] cx, Complex cb, float[][] cz) {
    _add.apply(cx,cb,cz);
  }
  public static void add(float[][][] cx, float[][][] cy, float[][][] cz) {
    _add.apply(cx,cy,cz);
  }
  public static void add(Complex ca, float[][][] cy, float[][][] cz) {
    _add.apply(ca,cy,cz);
  }
  public static void add(float[][][] cx, Complex cb, float[][][] cz) {
    _add.apply(cx,cb,cz);
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
  public static void sub(float[] cx, float[] cy, float[] cz) {
    _sub.apply(cx,cy,cz);
  }
  public static void sub(Complex ca, float[] cy, float[] cz) {
    _sub.apply(ca,cy,cz);
  }
  public static void sub(float[] cx, Complex cb, float[] cz) {
    _sub.apply(cx,cb,cz);
  }
  public static void sub(float[][] cx, float[][] cy, float[][] cz) {
    _sub.apply(cx,cy,cz);
  }
  public static void sub(Complex ca, float[][] cy, float[][] cz) {
    _sub.apply(ca,cy,cz);
  }
  public static void sub(float[][] cx, Complex cb, float[][] cz) {
    _sub.apply(cx,cb,cz);
  }
  public static void sub(float[][][] cx, float[][][] cy, float[][][] cz) {
    _sub.apply(cx,cy,cz);
  }
  public static void sub(Complex ca, float[][][] cy, float[][][] cz) {
    _sub.apply(ca,cy,cz);
  }
  public static void sub(float[][][] cx, Complex cb, float[][][] cz) {
    _sub.apply(cx,cb,cz);
  }
  public static float[] mul(float[] cx, float[] cy) {
    return _mul.apply(cx,cy);
  }
  public static float[] mul(Complex ca, float[] cy) {
    return _mul.apply(ca,cy);
  }
  public static float[] mul(float[] cx, Complex cb) {
    return _mul.apply(cx,cb);
  }
  public static float[][] mul(float[][] cx, float[][] cy) {
    return _mul.apply(cx,cy);
  }
  public static float[][] mul(Complex ca, float[][] cy) {
    return _mul.apply(ca,cy);
  }
  public static float[][] mul(float[][] cx, Complex cb) {
    return _mul.apply(cx,cb);
  }
  public static float[][][] mul(float[][][] cx, float[][][] cy) {
    return _mul.apply(cx,cy);
  }
  public static float[][][] mul(Complex ca, float[][][] cy) {
    return _mul.apply(ca,cy);
  }
  public static float[][][] mul(float[][][] cx, Complex cb) {
    return _mul.apply(cx,cb);
  }
  public static void mul(float[] cx, float[] cy, float[] cz) {
    _mul.apply(cx,cy,cz);
  }
  public static void mul(Complex ca, float[] cy, float[] cz) {
    _mul.apply(ca,cy,cz);
  }
  public static void mul(float[] cx, Complex cb, float[] cz) {
    _mul.apply(cx,cb,cz);
  }
  public static void mul(float[][] cx, float[][] cy, float[][] cz) {
    _mul.apply(cx,cy,cz);
  }
  public static void mul(Complex ca, float[][] cy, float[][] cz) {
    _mul.apply(ca,cy,cz);
  }
  public static void mul(float[][] cx, Complex cb, float[][] cz) {
    _mul.apply(cx,cb,cz);
  }
  public static void mul(float[][][] cx, float[][][] cy, float[][][] cz) {
    _mul.apply(cx,cy,cz);
  }
  public static void mul(Complex ca, float[][][] cy, float[][][] cz) {
    _mul.apply(ca,cy,cz);
  }
  public static void mul(float[][][] cx, Complex cb, float[][][] cz) {
    _mul.apply(cx,cb,cz);
  }
  public static float[] div(float[] cx, float[] cy) {
    return _div.apply(cx,cy);
  }
  public static float[] div(Complex ca, float[] cy) {
    return _div.apply(ca,cy);
  }
  public static float[] div(float[] cx, Complex cb) {
    return _div.apply(cx,cb);
  }
  public static float[][] div(float[][] cx, float[][] cy) {
    return _div.apply(cx,cy);
  }
  public static float[][] div(Complex ca, float[][] cy) {
    return _div.apply(ca,cy);
  }
  public static float[][] div(float[][] cx, Complex cb) {
    return _div.apply(cx,cb);
  }
  public static float[][][] div(float[][][] cx, float[][][] cy) {
    return _div.apply(cx,cy);
  }
  public static float[][][] div(Complex ca, float[][][] cy) {
    return _div.apply(ca,cy);
  }
  public static float[][][] div(float[][][] cx, Complex cb) {
    return _div.apply(cx,cb);
  }
  public static void div(float[] cx, float[] cy, float[] cz) {
    _div.apply(cx,cy,cz);
  }
  public static void div(Complex ca, float[] cy, float[] cz) {
    _div.apply(ca,cy,cz);
  }
  public static void div(float[] cx, Complex cb, float[] cz) {
    _div.apply(cx,cb,cz);
  }
  public static void div(float[][] cx, float[][] cy, float[][] cz) {
    _div.apply(cx,cy,cz);
  }
  public static void div(Complex ca, float[][] cy, float[][] cz) {
    _div.apply(ca,cy,cz);
  }
  public static void div(float[][] cx, Complex cb, float[][] cz) {
    _div.apply(cx,cb,cz);
  }
  public static void div(float[][][] cx, float[][][] cy, float[][][] cz) {
    _div.apply(cx,cy,cz);
  }
  public static void div(Complex ca, float[][][] cy, float[][][] cz) {
    _div.apply(ca,cy,cz);
  }
  public static void div(float[][][] cx, Complex cb, float[][][] cz) {
    _div.apply(cx,cb,cz);
  }
  private static abstract class Binary {
    float[] apply(float[] cx, float[] cy) {
      int n1 = cx.length/2;
      float[] cz = new float[2*n1];
      apply(cx,cy,cz);
      return cz;
    }
    float[] apply(Complex ca, float[] cy) {
      int n1 = cy.length/2;
      float[] cz = new float[2*n1];
      apply(ca,cy,cz);
      return cz;
    }
    float[] apply(float[] cx, Complex cb) {
      int n1 = cx.length/2;
      float[] cz = new float[2*n1];
      apply(cx,cb,cz);
      return cz;
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
    abstract void apply(float[] cx, float[] cy, float[] cz);
    abstract void apply(Complex ca, float[] cy, float[] cz);
    abstract void apply(float[] cx, Complex cb, float[] cz);
    void apply(float[][] cx, float[][] cy, float[][] cz) {
      int n2 = cx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(cx[i2],cy[i2],cz[i2]);
    }
    void apply(Complex ca, float[][] cy, float[][] cz) {
      int n2 = cy.length;
      for (int i2=0; i2<n2; ++i2)
        apply(ca,cy[i2],cz[i2]);
    }
    void apply(float[][] cx, Complex cb, float[][] cz) {
      int n2 = cx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(cx[i2],cb,cz[i2]);
    }
    void apply(float[][][] cx, float[][][] cy, float[][][] cz) {
      int n3 = cx.length;
      for (int i3=0; i3<n3; ++i3)
        apply(cx[i3],cy[i3],cz[i3]);
    }
    void apply(Complex ca, float[][][] cy, float[][][] cz) {
      int n3 = cy.length;
      for (int i3=0; i3<n3; ++i3)
        apply(ca,cy[i3],cz[i3]);
    }
    void apply(float[][][] cx, Complex cb, float[][][] cz) {
      int n3 = cx.length;
      for (int i3=0; i3<n3; ++i3)
        apply(cx[i3],cb,cz[i3]);
    }
  }
  private static Binary _add = new Binary() {
    void apply(float[] cx, float[] cy, float[] cz) {
      int n1 = cx.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = cx[ir]+cy[ir];
        cz[ii] = cx[ii]+cy[ii];
      }
    }
    void apply(Complex ca, float[] cy, float[] cz) {
      int n1 = cy.length/2;
      float ar = ca.r;
      float ai = ca.i;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = ar+cy[ir];
        cz[ii] = ai+cy[ii];
      }
    }
    void apply(float[] cx, Complex cb, float[] cz) {
      int n1 = cx.length/2;
      float br = cb.r;
      float bi = cb.i;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = cx[ir]+br;
        cz[ii] = cx[ii]+bi;
      }
    }
  };
  private static Binary _sub = new Binary() {
    void apply(float[] cx, float[] cy, float[] cz) {
      int n1 = cx.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = cx[ir]-cy[ir];
        cz[ii] = cx[ii]-cy[ii];
      }
    }
    void apply(Complex ca, float[] cy, float[] cz) {
      float ar = ca.r;
      float ai = ca.i;
      int n1 = cy.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = ar-cy[ir];
        cz[ii] = ai-cy[ii];
      }
    }
    void apply(float[] cx, Complex cb, float[] cz) {
      float br = cb.r;
      float bi = cb.i;
      int n1 = cx.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        cz[ir] = cx[ir]-br;
        cz[ii] = cx[ii]-bi;
      }
    }
  };
  private static Binary _mul = new Binary() {
    void apply(float[] cx, float[] cy, float[] cz) {
      int n1 = cx.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        float xr = cx[ir];
        float xi = cx[ii];
        float yr = cy[ir];
        float yi = cy[ii];
        cz[ir] = xr*yr-xi*yi;
        cz[ii] = xr*yi+xi*yr;
      }
    }
    void apply(Complex ca, float[] cy, float[] cz) {
      float ar = ca.r;
      float ai = ca.i;
      int n1 = cy.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        float yr = cy[ir];
        float yi = cy[ii];
        cz[ir] = ar*yr-ai*yi;
        cz[ii] = ar*yi+ai*yr;
      }
    }
    void apply(float[] cx, Complex cb, float[] cz) {
      float br = cb.r;
      float bi = cb.i;
      int n1 = cx.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        float xr = cx[ir];
        float xi = cx[ii];
        cz[ir] = xr*br-xi*bi;
        cz[ii] = xr*bi+xi*br;
      }
    }
  };
  private static Binary _div = new Binary() {
    void apply(float[] cx, float[] cy, float[] cz) {
      int n1 = cx.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        float xr = cx[ir];
        float xi = cx[ii];
        float yr = cy[ir];
        float yi = cy[ii];
        float yd = yr*yr+yi*yi;
        cz[ir] = (xr*yr+xi*yi)/yd;
        cz[ii] = (xi*yr-xr*yi)/yd;
      }
    }
    void apply(Complex ca, float[] cy, float[] cz) {
      float ar = ca.r;
      float ai = ca.i;
      int n1 = cy.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        float yr = cy[ir];
        float yi = cy[ii];
        float yd = yr*yr+yi*yi;
        cz[ir] = (ar*yr+ai*yi)/yd;
        cz[ii] = (ai*yr-ar*yi)/yd;
      }
    }
    void apply(float[] cx, Complex cb, float[] cz) {
      float br = cb.r;
      float bi = cb.i;
      float bd = br*br+bi*bi;
      int n1 = cx.length/2;
      for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
        float xr = cx[ir];
        float xi = cx[ii];
        cz[ir] = (xr*br+xi*bi)/bd;
        cz[ii] = (xi*br-xr*bi)/bd;
      }
    }
  };
}
