/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import static edu.mines.jtk.util.ArrayMath.*;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.util.ArrayMath}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.06.23
 */
public class ArrayMathTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ArrayMathTest.class);
    junit.textui.TestRunner.run(suite);
  }

  // Adapted from Bentley, J.L., and McIlroy, M.D., 1993, Engineering a sort
  // function, Software -- Practice and Experience, v. 23(11), p. 1249-1265.
  private static final int SAWTOOTH=0,RAND=1,STAGGER=2,PLATEAU=3,SHUFFLE=4;
  private static final int COPY=0,REV=1,REVHALF1=2,REVHALF2=3,SORT=4,DITHER=5;
  public void testSort() {
    Random r = new Random(314159);
    int[] ntest = {100,1023,1024,1025};
    for (int n:ntest) {
      float[] x = new float[n];
      for (int m = 1; m<2*n; m*=2) {
        for (int dist=0; dist<5; ++dist) {
          for (int i=0,j=0,k=1; i<n; ++i) {
            int ix = 0;
            switch(dist) {
              case SAWTOOTH: ix = i%m; break;
              case RAND:     ix = r.nextInt()%m; break;
              case STAGGER:  ix = (i*m+i)%n; break;
              case PLATEAU:  ix = min(i,m); break;
              case SHUFFLE:  ix = r.nextInt()%m!=0?(j+=2):(k+=2); break;
            }
            x[i] = (float)ix;
          }
          for (int order=0; order<6; ++order) {
            float[] y = null;
            float[] z;
            switch(order) {
            case COPY:
              y = copy(x);
              break;
            case REV:
              y = reverse(x);
              break;
            case REVHALF1:
              y = copy(x);
              z = reverse(copy(n/2,x));
              copy(n/2,0,z,0,y);
              break;
            case REVHALF2:
              y = copy(x);
              z = reverse(copy(n/2,n/2,x));
              copy(n/2,0,z,n/2,y);
              break;
            case SORT:
              y = copy(x);
              java.util.Arrays.sort(y);
              break;
            case DITHER:
              y = copy(x);
              for (int i=0; i<n; ++i)
                y[i] += (float)(i%5);
              break;
            }
            sortAndCheck(y);
          }
        }
      }
    }
  }
  private void sortAndCheck(float[] x) {
    int n = x.length;
    float[] x1 = copy(x);
    for (int k=0; k<n; k+=n/4) {
      quickPartialSort(k,x1);
      for (int i=0; i<k; ++i)
        assertTrue(x1[i]<=x1[k]);
      for (int i=k; i<n; ++i)
        assertTrue(x1[k]<=x1[i]);
    }
    float[] x2 = copy(x);
    quickSort(x2);
    for (int i=1; i<n; ++i)
      assertTrue(x2[i-1]<=x2[i]);
    int[] i1 = rampint(0,1,n);
    for (int k=0; k<n; k+=n/4) {
      quickPartialIndexSort(k,x,i1);
      for (int j=0; j<k; ++j)
        assertTrue(x[i1[j]]<=x[i1[k]]);
      for (int j=k+1; j<n; ++j)
        assertTrue(x[i1[k]]<=x[i1[j]]);
    }
    int[] i2 = rampint(0,1,n);
    quickIndexSort(x,i2);
    for (int j=1; j<n; ++j)
      assertTrue(x[i2[j-1]]<=x[i2[j]]);
  }

  public void testFloat1() {
    int n1 = 8;
    int n2 = 6;
    int n3 = 4;
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

    b1 = copy(n1/2,0,2,a1);
    b2 = copy(n1/2,n2/2,0,0,2,2,a2);
    b3 = copy(n1/2,n2/2,n3/2,0,0,0,2,2,2,a3);
    assertEqual(b1,rampfloat(0,2,n1/2));
    assertEqual(b2,rampfloat(0,2,20,n1/2,n2/2));
    assertEqual(b3,rampfloat(0,2,20,200,n1/2,n2/2,n3/2));

    b1 = copy(a1);
    b2 = copy(a2);
    b3 = copy(a3);
    copy(n1-1,1,a1,1,b1);
    copy(n1-2,n2-1,2,1,a2,2,1,b2);
    copy(n1-3,n2-2,n3-1,3,2,1,a3,3,2,1,b3);
    assertEqual(b1,rampfloat(0,1,n1));
    assertEqual(b2,rampfloat(0,1,10,n1,n2));
    assertEqual(b3,rampfloat(0,1,10,100,n1,n2,n3));

    b1 = reverse(reverse(a1));
    assertEqual(b1,a1);

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
    float[][][] rx;

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

  /*
  private void assertAlmostEqual(float[] rx, float[] ry) {
    float tolerance = 100.0f*FLT_EPSILON;
    assertTrue(equal(tolerance,rx,ry));
  }

  private void assertAlmostEqual(float[][] rx, float[][] ry) {
    float tolerance = 100.0f*FLT_EPSILON;
    assertTrue(equal(tolerance,rx,ry));
  }
  */

  private void assertAlmostEqual(float[][][] rx, float[][][] ry) {
    float tolerance = 100.0f*FLT_EPSILON;
    assertTrue(equal(tolerance,rx,ry));
  }

  public void testCfloat1() {
    int n1 = 8;
    int n2 = 6;
    int n3 = 4;
    Cfloat c0 = new Cfloat(0.0f,0.0f);
    Cfloat c1 = new Cfloat(1.0f,0.0f);
    Cfloat c2 = new Cfloat(2.0f,0.0f);
    Cfloat c10 = new Cfloat(10.0f,0.0f);
    Cfloat c12 = new Cfloat(12.0f,0.0f);
    Cfloat c20 = new Cfloat(20.0f,0.0f);
    Cfloat c100 = new Cfloat(100.0f,0.0f);
    Cfloat c123 = new Cfloat(123.0f,0.0f);
    Cfloat c200 = new Cfloat(200.0f,0.0f);
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

    b1 = ccopy(n1/2,0,2,a1);
    b2 = ccopy(n1/2,n2/2,0,0,2,2,a2);
    b3 = ccopy(n1/2,n2/2,n3/2,0,0,0,2,2,2,a3);
    assertEqual(b1,crampfloat(c0,c2,n1/2));
    assertEqual(b2,crampfloat(c0,c2,c20,n1/2,n2/2));
    assertEqual(b3,crampfloat(c0,c2,c20,c200,n1/2,n2/2,n3/2));

    b1 = ccopy(a1);
    b2 = ccopy(a2);
    b3 = ccopy(a3);
    ccopy(n1-1,1,a1,1,b1);
    ccopy(n1-2,n2-1,2,1,a2,2,1,b2);
    ccopy(n1-3,n2-2,n3-1,3,2,1,a3,3,2,1,b3);
    assertEqual(b1,crampfloat(c0,c1,n1));
    assertEqual(b2,crampfloat(c0,c1,c10,n1,n2));
    assertEqual(b3,crampfloat(c0,c1,c10,c100,n1,n2,n3));

    b1 = creverse(creverse(a1));
    assertEqual(b1,a1);

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

  public void testMath() {
    assertEquals(0.0f,sin(FLT_PI));
    assertEquals(0.0d,sin(DBL_PI));

    assertEquals(1.0f,cos(2.0f*FLT_PI));
    assertEquals(1.0d,cos(2.0d*DBL_PI));

    assertEquals(1.0f,tan(FLT_PI/4.0f));
    assertEquals(1.0d,tan(DBL_PI/4.0d));

    assertEquals(FLT_PI/2.0f,asin(1.0f));
    assertEquals(DBL_PI/2.0d,asin(1.0d));

    assertEquals(FLT_PI/2.0f,acos(0.0f));
    assertEquals(DBL_PI/2.0d,acos(0.0d));

    assertEquals(FLT_PI/4.0f,atan(1.0f));
    assertEquals(DBL_PI/4.0d,atan(1.0d));

    assertEquals(FLT_PI/2.0f,atan2(1.0f,0.0f));
    assertEquals(DBL_PI/2.0d,atan2(1.0d,0.0d));

    assertEquals(-3.0f*FLT_PI/4.0f,atan2(-1.0f,-1.0f));
    assertEquals(-3.0d*DBL_PI/4.0d,atan2(-1.0d,-1.0d));

    assertEquals(FLT_PI,toRadians(180.0f));
    assertEquals(DBL_PI,toRadians(180.0d));

    assertEquals(180.0f,toDegrees(FLT_PI));
    assertEquals(180.0d,toDegrees(DBL_PI));

    assertEquals(1.0f,log(exp(1.0f)));
    assertEquals(1.0d,log(exp(1.0d)));

    assertEquals(3.0f,sqrt(pow(3.0f,2.0f)));
    assertEquals(3.0d,sqrt(pow(3.0d,2.0d)));

    assertEquals(tanh(1.0f),sinh(1.0f)/cosh(1.0f));
    assertEquals(tanh(1.0d),sinh(1.0d)/cosh(1.0d));

    assertEquals(4.0f,ceil(FLT_PI));
    assertEquals(4.0d,ceil(DBL_PI));
    assertEquals(-3.0f,ceil(-FLT_PI));
    assertEquals(-3.0d,ceil(-DBL_PI));

    assertEquals(3.0f,floor(FLT_PI));
    assertEquals(3.0d,floor(DBL_PI));
    assertEquals(-4.0f,floor(-FLT_PI));
    assertEquals(-4.0d,floor(-DBL_PI));

    assertEquals(3.0f,rint(FLT_PI));
    assertEquals(3.0d,rint(DBL_PI));
    assertEquals(-3.0f,rint(-FLT_PI));
    assertEquals(-3.0d,rint(-DBL_PI));

    assertEquals(3,round(FLT_PI));
    assertEquals(3,round(DBL_PI));
    assertEquals(-3,round(-FLT_PI));
    assertEquals(-3,round(-DBL_PI));

    assertEquals(3,round(FLT_E));
    assertEquals(3,round(DBL_E));
    assertEquals(-3,round(-FLT_E));
    assertEquals(-3,round(-DBL_E));

    assertEquals(1.0f,signum(FLT_PI));
    assertEquals(1.0d,signum(DBL_PI));
    assertEquals(-1.0f,signum(-FLT_PI));
    assertEquals(-1.0d,signum(-DBL_PI));
    assertEquals(0.0f,signum(0.0f));
    assertEquals(0.0d,signum(0.0d));

    assertEquals(2,abs(2));
    assertEquals(2L,abs(2L));
    assertEquals(2.0f,abs(2.0f));
    assertEquals(2.0d,abs(2.0d));
    assertEquals(2,abs(-2));
    assertEquals(2L,abs(-2L));
    assertEquals(2.0f,abs(-2.0f));
    assertEquals(2.0d,abs(-2.0d));
    assertEquals("abs(float) changed sign of 0",
                 0, Float.floatToIntBits(abs(0.0f)));
    assertEquals("abs(double) changed sign of 0",
                 0, Double.doubleToLongBits(abs(0.0d)));

    assertEquals(4,max(1,3,4,2));
    assertEquals(4L,max(1L,3L,4L,2L));
    assertEquals(4.0f,max(1.0f,3.0f,4.0f,2.0f));
    assertEquals(4.0d,max(1.0d,3.0d,4.0d,2.0d));

    assertEquals(1,min(3,1,4,2));
    assertEquals(1L,min(3L,1L,4L,2L));
    assertEquals(1.0f,min(3.0f,1.0f,4.0f,2.0f));
    assertEquals(1.0d,min(3.0d,1.0d,4.0d,2.0d));
  }

  private void assertEquals(float expected, float actual) {
    float small = 1.0e-6f*max(abs(expected),abs(actual),1.0f);
    assertEquals(expected,actual,small);
  }

  private void assertEquals(double expected, double actual) {
    double small = 1.0e-12f*max(abs(expected),abs(actual),1.0d);
    assertEquals(expected,actual,small);
  }
}
