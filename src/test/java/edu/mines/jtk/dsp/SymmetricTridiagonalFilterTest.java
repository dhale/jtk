/****************************************************************************
Copyright (c) 2013, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.SymmetricTridiagonalFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2013.07.23
 */
public class SymmetricTridiagonalFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(SymmetricTridiagonalFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1Simple() {
    int n = 5;
    double af,ai,al,b;
    //af = al = 0.50; // zero-value
    af = al = 0.75; // zero-slope
    ai = 0.50;
    b  = 0.25;
    SymmetricTridiagonalFilter f = new SymmetricTridiagonalFilter(af,ai,al,b);
    float[] x = zerofloat(n);
    float[] y = zerofloat(n);
    float[] z = zerofloat(n);
    fill(1.0f,x);
    //x[n/2] = 1.0f;
    f.apply(x,y);
    f.applyInverse(y,z);
    //dump(x); dump(y); dump(z);
    assertEqual(x,z);
  }

  public void test2Simple() {
    int n1 = 5;
    int n2 = 4;
    double af,ai,al,b;
    //af = al = 0.50; // zero-value
    af = al = 0.75; // zero-slope
    ai = 0.50;
    b  = 0.25;
    SymmetricTridiagonalFilter f = new SymmetricTridiagonalFilter(af,ai,al,b);
    float[][] x = zerofloat(n1,n2);
    float[][] y = zerofloat(n1,n2);
    float[][] z = zerofloat(n1,n2);
    fill(1.0f,x);
    //x[n2/2][n1/2] = 1.0f;
    f.apply1(x,y);
    f.apply2(y,y);
    f.applyInverse1(y,z);
    f.applyInverse2(z,z);
    //dump(x); dump(y); dump(z);
    assertEqual(x,z);
  }

  public void test3Simple() {
    int n1 = 11;
    int n2 = 12;
    int n3 = 13;
    float[][][] r = randfloat(n1,n2,n3);
    float[][][] x = copy(r);
    float[][][] y = copy(r);
    SymmetricTridiagonalFilter stf = 
      new SymmetricTridiagonalFilter(2.6,2.5,2.7,1.2);
    stf.apply1(x,x);
    stf.apply2(x,x);
    stf.apply3(x,x);
    stf.apply1(y,y);
    y = transpose12(y);
    stf.apply1(y,y);
    y = transpose12(y);
    y = transpose23(y);
    stf.apply2(y,y);
    y = transpose23(y);
    assertEqual(x,y);
  }

  public void test1Random() {
    java.util.Random r = new java.util.Random();
    int ntest = 1000;
    for (int itest=0; itest<ntest; ++itest) {
      SymmetricTridiagonalFilter stf = makeRandomFilter();
      boolean inplace = r.nextBoolean(); // apply in-place?
      int n = 2+r.nextInt(10);
      float[] t = randfloat(r,n);
      float[] x = copy(t);
      float[] y = inplace?x:zerofloat(n);
      float[] z = inplace?x:zerofloat(n);
      stf.apply(x,y);
      stf.applyInverse(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
    }
  }

  public void test2Random() {
    java.util.Random r = new java.util.Random();
    int ntest = 1000;
    for (int itest=0; itest<ntest; ++itest) {
      SymmetricTridiagonalFilter stf = makeRandomFilter();
      boolean inplace = r.nextBoolean(); // apply in-place?
      int n1 = 2+r.nextInt(11);
      int n2 = 2+r.nextInt(12);
      float[][] t = randfloat(r,n1,n2);
      float[][] x = copy(t);
      float[][] y = inplace?x:zerofloat(n1,n2);
      float[][] z = inplace?x:zerofloat(n1,n2);
      stf.apply1(x,y);
      stf.applyInverse1(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
      stf.apply2(x,y);
      stf.applyInverse2(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
    }
  }

  public void test3Random() {
    java.util.Random r = new java.util.Random();
    int ntest = 1000;
    for (int itest=0; itest<ntest; ++itest) {
      SymmetricTridiagonalFilter stf = makeRandomFilter();
      boolean inplace = r.nextBoolean(); // apply in-place?
      int n1 = 2+r.nextInt(11);
      int n2 = 2+r.nextInt(12);
      int n3 = 2+r.nextInt(13);
      float[][][] t = randfloat(r,n1,n2,n3);
      float[][][] x = copy(t);
      float[][][] y = inplace?x:zerofloat(n1,n2,n3);
      float[][][] z = inplace?x:zerofloat(n1,n2,n3);
      stf.apply1(x,y);
      stf.applyInverse1(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
      stf.apply2(x,y);
      stf.applyInverse2(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
      stf.apply3(x,y);
      stf.applyInverse3(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
    }
  }

  private static SymmetricTridiagonalFilter makeRandomFilter() {
    java.util.Random r = new java.util.Random();
    float af,ai,al,b;
    boolean aeq2b = r.nextBoolean(); // |a| = |2b|?
    boolean abneg = r.nextBoolean(); // sgn(a) = sgn(b)?
    boolean afzs = r.nextBoolean(); // af for zero-slope?
    boolean alzs = r.nextBoolean(); // al for zero-slope?
    if (aeq2b && afzs==true && alzs==true) {
      if (r.nextBoolean()) {
        afzs = false;
      } else {
        alzs = false;
      }
    }
    b = r.nextFloat();
    ai = 2.0f*b;
    if (!aeq2b) ai += max(0.001,r.nextFloat())*b;
    if (abneg) ai = -ai;
    af = ai;
    al = ai;
    if (afzs) af = ai+b;
    if (alzs) al = ai+b;
    return new SymmetricTridiagonalFilter(af,ai,al,b);
  }

  private static float[][][] transpose12(float[][][] x) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    float[][][] y = new float[n3][n1][n2];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          y[i3][i1][i2] = x[i3][i2][i1];
        }
      }
    }
    return y;
  }

  private static float[][][] transpose23(float[][][] x) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    float[][][] y = new float[n2][n3][n1];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          y[i2][i3][i1] = x[i3][i2][i1];
        }
      }
    }
    return y;
  }

  private static void assertEqual(float[] e, float[] a) {
    float tol = 0.001f*max(abs(e));
    assertEqual(e,a,tol);
  }

  private static void assertEqual(float[][] e, float[][] a) {
    float tol = 0.001f*max(abs(e));
    assertEqual(e,a,tol);
  }

  private static void assertEqual(float[][][] e, float[][][] a) {
    float tol = 0.001f*max(abs(e));
    assertEqual(e,a,tol);
  }

  private static void assertEqual(float[] e, float[] a, float tol) {
    int n = e.length;
    for (int i=0; i<n; ++i) {
      assertEquals(e[i],a[i],tol);
    }
  }

  private static void assertEqual(float[][] e, float[][] a, float tol) {
    int n = e.length;
    for (int i=0; i<n; ++i)
      assertEqual(e[i],a[i],tol);
  }

  private static void assertEqual(float[][][] e, float[][][] a, float tol) {
    int n = e.length;
    for (int i=0; i<n; ++i)
      assertEqual(e[i],a[i],tol);
  }
}
