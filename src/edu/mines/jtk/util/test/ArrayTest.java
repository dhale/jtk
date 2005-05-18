/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util.test;

import junit.framework.*;
import edu.mines.jtk.util.Cfloat;
import edu.mines.jtk.util.Cdouble;
import static edu.mines.jtk.util.Array.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.util.Array}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.17
 */
public class ArrayTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ArrayTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testFloat1() {
    int n1 = 5;
    int n2 = 4;
    int n3 = 3;
    float[] a1 = rampfloat(0,1,n1);
    float[][] a2 = rampfloat(0,1,10,n1,n2);
    float[][][] a3 = rampfloat(0,1,10,100,n1,n2,n3);
    float[] b1;
    float[][] b2;
    float[][][] b3;

    b1 = copy(a1);
    b2 = copy(a2);
    b3 = copy(a3);
    assertEqual(b1,a1);
    assertEqual(b2,a2);
    assertEqual(b3,a3);

    copy(a1,b1);
    copy(a2,b2);
    copy(a3,b3);
    assertEqual(b1,a1);
    assertEqual(b2,a2);
    assertEqual(b3,a3);

    b1 = copy(n1-1,a1);
    b2 = copy(n1-1,n2-1,a2);
    b3 = copy(n1-1,n2-1,n3-1,a3);
    assertEqual(b1,rampfloat(0,1,n1-1));
    assertEqual(b2,rampfloat(0,1,10,n1-1,n2-1));
    assertEqual(b3,rampfloat(0,1,10,100,n1-1,n2-1,n3-1));

    copy(n1-1,a1,b1);
    copy(n1-1,n2-1,a2,b2);
    copy(n1-1,n2-1,n3-1,a3,b3);
    assertEqual(b1,rampfloat(0,1,n1-1));
    assertEqual(b2,rampfloat(0,1,10,n1-1,n2-1));
    assertEqual(b3,rampfloat(0,1,10,100,n1-1,n2-1,n3-1));

    b1 = copy(n1-1,1,a1);
    b2 = copy(n1-2,n2-1,2,1,a2);
    b3 = copy(n1-3,n2-2,n3-1,3,2,1,a3);
    assertEqual(b1,rampfloat(1,1,n1-1));
    assertEqual(b2,rampfloat(12,1,10,n1-1,n2-1));
    assertEqual(b3,rampfloat(123,1,10,100,n1-1,n2-1,n3-1));
    
    copy(n1-1,1,a1,0,b1);
    copy(n1-2,n2-1,2,1,a2,0,0,b2);
    copy(n1-3,n2-2,n3-1,3,2,1,a3,0,0,0,b3);
    assertEqual(b1,rampfloat(1,1,n1-1));
    assertEqual(b2,rampfloat(12,1,10,n1-1,n2-1));
    assertEqual(b3,rampfloat(123,1,10,100,n1-1,n2-1,n3-1));

    b1 = copy(a1);
    b2 = copy(a2);
    b3 = copy(a3);
    copy(n1-1,1,a1,1,b1);
    copy(n1-2,n2-1,2,1,a2,2,1,b2);
    copy(n1-3,n2-2,n3-1,3,2,1,a3,3,2,1,b3);
    assertEqual(b1,rampfloat(0,1,n1));
    assertEqual(b2,rampfloat(0,1,10,n1,n2));
    assertEqual(b3,rampfloat(0,1,10,100,n1,n2,n3));

    b2 = reshape(n1,n2,flatten(a2));
    b3 = reshape(n1,n2,n3,flatten(a3));
    assertEqual(a2,b2);
    assertEqual(a3,b3);

    b2 = transpose(transpose(a2));
    assertEqual(a2,b2);
  }

  public void testFloat2() {
    int n1 = 3;
    int n2 = 4;
    int n3 = 5;
    float r0 = 0.0f;
    float ra = 2.0f;
    float rb1 = 1.0f;
    float rb2 = 2.0f;
    float rb3 = 4.0f;
    float[][][] rx,ry,rz;

    assertEqual(zerofloat(n1,n2,n3),fillfloat(r0,n1,n2,n3));

    rx = rampfloat(ra,rb1,rb2,rb3,n1,n2,n3);
    assertEqual(rx,sub(add(rx,rx),rx));
    assertEqual(rx,sub(add(rx,ra),ra));
    assertEqual(fillfloat(ra,n1,n2,n3),sub(add(ra,rx),rx));

    rx = rampfloat(ra,rb1,rb2,rb3,n1,n2,n3);
    assertEqual(rx,div(mul(rx,rx),rx));
    assertEqual(rx,div(mul(rx,ra),ra));
    assertEqual(fillfloat(ra,n1,n2,n3),div(mul(ra,rx),rx));

    rx = rampfloat(ra,rb1,rb2,rb3,n1,n2,n3);
    assertEqual(rx,log(exp(rx)));

    rx = rampfloat(ra,rb1,rb2,rb3,n1,n2,n3);
    assertAlmostEqual(rx,mul(sqrt(rx),sqrt(rx)));

    rx = rampfloat(ra,rb1,rb2,rb3,n1,n2,n3);
    assertAlmostEqual(rx,pow(sqrt(rx),2.0f));

    rx = rampfloat(ra,rb1,rb2,rb3,n1,n2,n3);
    int[] imax = {-1,-1,-1};
    float rmax = max(rx,imax);
    assertTrue(rmax==rx[n3-1][n2-1][n1-1]);
    assertEquals(n1-1,imax[0]);
    assertEquals(n2-1,imax[1]);
    assertEquals(n3-1,imax[2]);

    rx = rampfloat(ra,rb1,rb2,rb3,n1,n2,n3);
    int[] imin = {-1,-1,-1};
    float rmin = min(rx,imin);
    assertTrue(rmin==rx[0][0][0]);
    assertEquals(0,imin[0]);
    assertEquals(0,imin[1]);
    assertEquals(0,imin[2]);
  }

  private void assertEqual(float[] rx, float[] ry) {
    assertTrue(equal(rx,ry));
  }

  private void assertEqual(float[][] rx, float[][] ry) {
    assertTrue(equal(rx,ry));
  }

  private void assertEqual(float[][][] rx, float[][][] ry) {
    assertTrue(equal(rx,ry));
  }

  private void assertAlmostEqual(float[] rx, float[] ry) {
    float tolerance = 100.0f*FLT_EPSILON;
    assertTrue(equal(tolerance,rx,ry));
  }

  private void assertAlmostEqual(float[][] rx, float[][] ry) {
    float tolerance = 100.0f*FLT_EPSILON;
    assertTrue(equal(tolerance,rx,ry));
  }

  private void assertAlmostEqual(float[][][] rx, float[][][] ry) {
    float tolerance = 100.0f*FLT_EPSILON;
    assertTrue(equal(tolerance,rx,ry));
  }

  public void testCfloat1() {
    int n1 = 5;
    int n2 = 4;
    int n3 = 3;
    Cfloat c0 = new Cfloat(0.0f,0.0f);
    Cfloat c1 = new Cfloat(1.0f,0.0f);
    Cfloat c10 = new Cfloat(10.0f,0.0f);
    Cfloat c12 = new Cfloat(12.0f,0.0f);
    Cfloat c100 = new Cfloat(100.0f,0.0f);
    Cfloat c123 = new Cfloat(123.0f,0.0f);
    float[] a1 = crampfloat(c0,c1,n1);
    float[][] a2 = crampfloat(c0,c1,c10,n1,n2);
    float[][][] a3 = crampfloat(c0,c1,c10,c100,n1,n2,n3);
    float[] b1;
    float[][] b2;
    float[][][] b3;

    b1 = ccopy(a1);
    b2 = ccopy(a2);
    b3 = ccopy(a3);
    assertEqual(b1,a1);
    assertEqual(b2,a2);
    assertEqual(b3,a3);

    ccopy(a1,b1);
    ccopy(a2,b2);
    ccopy(a3,b3);
    assertEqual(b1,a1);
    assertEqual(b2,a2);
    assertEqual(b3,a3);

    b1 = ccopy(n1-1,a1);
    b2 = ccopy(n1-1,n2-1,a2);
    b3 = ccopy(n1-1,n2-1,n3-1,a3);
    assertEqual(b1,crampfloat(c0,c1,n1-1));
    assertEqual(b2,crampfloat(c0,c1,c10,n1-1,n2-1));
    assertEqual(b3,crampfloat(c0,c1,c10,c100,n1-1,n2-1,n3-1));

    ccopy(n1-1,a1,b1);
    ccopy(n1-1,n2-1,a2,b2);
    ccopy(n1-1,n2-1,n3-1,a3,b3);
    assertEqual(b1,crampfloat(c0,c1,n1-1));
    assertEqual(b2,crampfloat(c0,c1,c10,n1-1,n2-1));
    assertEqual(b3,crampfloat(c0,c1,c10,c100,n1-1,n2-1,n3-1));

    b1 = ccopy(n1-1,1,a1);
    b2 = ccopy(n1-2,n2-1,2,1,a2);
    b3 = ccopy(n1-3,n2-2,n3-1,3,2,1,a3);
    assertEqual(b1,crampfloat(c1,c1,n1-1));
    assertEqual(b2,crampfloat(c12,c1,c10,n1-1,n2-1));
    assertEqual(b3,crampfloat(c123,c1,c10,c100,n1-1,n2-1,n3-1));
    
    ccopy(n1-1,1,a1,0,b1);
    ccopy(n1-2,n2-1,2,1,a2,0,0,b2);
    ccopy(n1-3,n2-2,n3-1,3,2,1,a3,0,0,0,b3);
    assertEqual(b1,crampfloat(c1,c1,n1-1));
    assertEqual(b2,crampfloat(c12,c1,c10,n1-1,n2-1));
    assertEqual(b3,crampfloat(c123,c1,c10,c100,n1-1,n2-1,n3-1));

    b1 = ccopy(a1);
    b2 = ccopy(a2);
    b3 = ccopy(a3);
    ccopy(n1-1,1,a1,1,b1);
    ccopy(n1-2,n2-1,2,1,a2,2,1,b2);
    ccopy(n1-3,n2-2,n3-1,3,2,1,a3,3,2,1,b3);
    assertEqual(b1,crampfloat(c0,c1,n1));
    assertEqual(b2,crampfloat(c0,c1,c10,n1,n2));
    assertEqual(b3,crampfloat(c0,c1,c10,c100,n1,n2,n3));

    b2 = creshape(n1,n2,cflatten(a2));
    b3 = creshape(n1,n2,n3,cflatten(a3));
    assertEqual(a2,b2);
    assertEqual(a3,b3);

    b2 = ctranspose(ctranspose(a2));
    assertEqual(a2,b2);
  }

  public void testCfloat2() {
    int n1 = 3;
    int n2 = 4;
    int n3 = 5;
    Cfloat c0 = new Cfloat();
    Cfloat ca = new Cfloat(1.0f,2.0f);
    Cfloat cb1 = new Cfloat(2.0f,3.0f);
    Cfloat cb2 = new Cfloat(3.0f,4.0f);
    Cfloat cb3 = new Cfloat(4.0f,5.0f);
    float[][][] cx,cy,cz;

    assertEqual(czerofloat(n1,n2,n3),cfillfloat(c0,n1,n2,n3));

    cx = crampfloat(ca,cb1,cb2,cb3,n1,n2,n3);
    assertEqual(cx,csub(cadd(cx,cx),cx));
    assertEqual(cx,csub(cadd(cx,ca),ca));
    assertEqual(cfillfloat(ca,n1,n2,n3),csub(cadd(ca,cx),cx));

    cx = crampfloat(ca,cb1,cb2,cb3,n1,n2,n3);
    assertEqual(cx,cdiv(cmul(cx,cx),cx));
    assertEqual(cx,cdiv(cmul(cx,ca),ca));
    assertEqual(cfillfloat(ca,n1,n2,n3),cdiv(cmul(ca,cx),cx));

    cx = crampfloat(ca,cb1,cb2,cb3,n1,n2,n3);
    assertEqual(cnorm(cx),cabs(cmul(cx,cconj(cx))));

    float[][][] rr = fillfloat(1.0f,n1,n2,n3);
    float[][][] ra = rampfloat(0.0f,1.0f,1.0f,1.0f,n1,n2,n3);
    cx = polar(rr,ra);
    float[][][] rx = cos(ra);
    float[][][] ry = sin(ra);
    cy = cmplx(rx,ry);
    assertEqual(cx,cy);
    Cfloat ci = new Cfloat(0.0f,1.0f);
    float[][][] ciw = crampfloat(c0,ci,ci,ci,n1,n2,n3);
    cz = cexp(ciw);
    assertEqual(cx,cz);
  }

  public void testMonotonic() {
    double[] a = {};
    assertTrue(isMonotonic(a));
    assertTrue(isIncreasing(a));
    assertTrue(isDecreasing(a));

    double[] a0 = {0};
    assertTrue(isMonotonic(a0));
    assertTrue(isIncreasing(a0));
    assertTrue(isDecreasing(a0));

    double[] a01 = {0,1};
    assertTrue(isMonotonic(a01));
    assertTrue(isIncreasing(a01));
    assertTrue(!isDecreasing(a01));

    double[] a10 = {1,0};
    assertTrue(isMonotonic(a10));
    assertTrue(!isIncreasing(a10));
    assertTrue(isDecreasing(a10));

    double[] a101 = {1,0,1};
    assertTrue(!isMonotonic(a101));
    assertTrue(!isIncreasing(a101));
    assertTrue(!isDecreasing(a101));

    double[] a010 = {0,1,0};
    assertTrue(!isMonotonic(a010));
    assertTrue(!isIncreasing(a010));
    assertTrue(!isDecreasing(a010));
  }

  public void testBinarySearch() {
    double[] a = {};
    checkSearch(a,1);

    double[] a0 = {2};
    checkSearch(a0,1);
    checkSearch(a0,2);
    checkSearch(a0,3);

    double[] a13 = {1,3};
    checkSearch(a13,0);
    checkSearch(a13,1);
    checkSearch(a13,2);
    checkSearch(a13,3);
    checkSearch(a13,4);

    double[] a31 = {3,1};
    checkSearch(a31,0);
    checkSearch(a31,1);
    checkSearch(a31,2);
    checkSearch(a31,3);
    checkSearch(a31,4);

    double[] a135 = {1,3,5};
    checkSearch(a135,0);
    checkSearch(a135,1);
    checkSearch(a135,2);
    checkSearch(a135,3);
    checkSearch(a135,4);
    checkSearch(a135,5);
    checkSearch(a135,6);

    double[] a531 = {5,3,1};
    checkSearch(a531,0);
    checkSearch(a531,1);
    checkSearch(a531,2);
    checkSearch(a531,3);
    checkSearch(a531,4);
    checkSearch(a531,5);
    checkSearch(a531,6);
  }
  private void checkSearch(double[] a, double x) {
    int n = a.length;
    int i = binarySearch(a,x);
    validateSearch(a,x,i);
    for (int is=-2; is<n+2; ++is) {
      i = binarySearch(a,x,is);
      validateSearch(a,x,i);
    }
  }
  private void validateSearch(double[] a, double x, int i) {
    int n = a.length;
    if (i>=0) {
      assertTrue(a[i]==x);
    } else {
      i = -(i+1);
      if (n==0) {
        assertTrue(i==0);
      } else if (n<2 || a[0]<a[n-1]) {
        if (i==0) {
          assertTrue(x<a[i]);
        } else if (i==n) {
          assertTrue(a[i-1]<x);
        } else {
          assertTrue(a[i-1]<x && x<a[i]);
        }
      } else {
        if (i==0) {
          assertTrue(x>a[i]);
        } else if (i==n) {
          assertTrue(a[i-1]>x);
        } else {
          assertTrue(a[i-1]>x && x>a[i]);
        }
      }
    }
  }
}
