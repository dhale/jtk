/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.DifferenceFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.10.15
 */
public class DifferenceFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(DifferenceFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1Impulse() {
    float[] e = {0.0f,0.0f,0.0f,-1.0f,2.0f,-1.0f,0.0f,0.0f,0.0f};
    int n = e.length;
    float[] x = new float[n];
    float[] y = new float[n];
    float[] z = new float[n];
    x[(n-1)/2] = 1.0f;
    DifferenceFilter df = new DifferenceFilter();
    df.apply(x,y);
    df.applyInverse(y,z);
    assertEqual(x,z,10.0f*FLT_EPSILON);
    df.applyTranspose(x,y);
    df.applyInverseTranspose(y,z);
    assertEqual(x,z,10.0f*FLT_EPSILON);
    df.apply(x,y);
    df.applyTranspose(y,z);
    assertEqual(e,z,0.02f);
  }

  public void test2Impulse() {
    float[][] e = {
      { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
      { 0.0f, 0.0f, 0.0f, 0.0f,-1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
      { 0.0f, 0.0f, 0.0f,-1.0f, 4.0f,-1.0f, 0.0f, 0.0f, 0.0f},
      { 0.0f, 0.0f, 0.0f, 0.0f,-1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
      { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}
    };
    int n1 = e[0].length;
    int n2 = e.length;
    float[][] x = new float[n2][n1];
    float[][] y = new float[n2][n1];
    float[][] z = new float[n2][n1];
    x[(n2-1)/2][(n1-1)/2] = 1.0f;
    DifferenceFilter df = new DifferenceFilter();
    df.apply(x,y);
    df.applyInverse(y,z);
    assertEqual(x,z,10.0f*FLT_EPSILON);
    df.applyTranspose(x,y);
    df.applyInverseTranspose(y,z);
    assertEqual(x,z,10.0f*FLT_EPSILON);
    df.apply(x,y);
    df.applyTranspose(y,z);
    assertEqual(e,z,0.04f);
  }

  public void test3Impulse() {
    float[][][] e = {
      {
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}
      },{
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f,-1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}
      },{
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f,-1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f,-1.0f, 6.0f,-1.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f,-1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}
      },{
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f,-1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}
      },{
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}
      }
    };
    int n1 = e[0][0].length;
    int n2 = e[0].length;
    int n3 = e.length;
    float[][][] x = new float[n3][n2][n1];
    float[][][] y = new float[n3][n2][n1];
    float[][][] z = new float[n3][n2][n1];
    x[(n3-1)/2][(n2-1)/2][(n1-1)/2] = 1.0f;
    DifferenceFilter df = new DifferenceFilter();
    df.apply(x,y);
    df.applyInverse(y,z);
    assertEqual(x,z,10.0f*FLT_EPSILON);
    df.applyTranspose(x,y);
    df.applyInverseTranspose(y,z);
    assertEqual(x,z,10.0f*FLT_EPSILON);
    df.apply(x,y);
    df.applyTranspose(y,z);
    assertEqual(e,z,0.06f);
  }

  public void test1Random() {
    DifferenceFilter df = new DifferenceFilter();
    int n = 100;
    float[] x,y,z;

    x = rands(n);
    y = zeros(n);
    z = zeros(n);
    df.apply(x,y);
    df.applyInverse(y,z);
    assertEqual(x,z);
    df.applyTranspose(x,y);
    df.applyInverseTranspose(y,z);
    assertEqual(x,z);

    float d1,d2;
    float tiny = n*10.0f*FLT_EPSILON;
    x = rands(n);
    y = rands(n);
    z = zeros(n);
    df.apply(x,z);
    d1 = dot(z,y);
    df.applyTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
    df.applyInverse(x,z);
    d1 = dot(z,y);
    df.applyInverseTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
  }

  public void test2Random() {
    DifferenceFilter df = new DifferenceFilter();
    int n1 = 19;
    int n2 = 21;
    float[][] x,y,z;

    x = rands(n1,n2);
    y = zeros(n1,n2);
    z = zeros(n1,n2);
    df.apply(x,y);
    df.applyInverse(y,z);
    assertEqual(x,z);
    df.applyTranspose(x,y);
    df.applyInverseTranspose(y,z);
    assertEqual(x,z);

    float d1,d2;
    float tiny = n1*n2*10.0f*FLT_EPSILON;
    x = rands(n1,n2);
    y = rands(n1,n2);
    z = zeros(n1,n2);
    df.apply(x,z);
    d1 = dot(z,y);
    df.applyTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
    df.applyInverse(x,z);
    d1 = dot(z,y);
    df.applyInverseTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
  }

  public void test3Random() {
    DifferenceFilter df = new DifferenceFilter();
    int n1 = 11;
    int n2 = 13;
    int n3 = 12;
    float[][][] x,y,z;

    x = rands(n1,n2,n3);
    y = zeros(n1,n2,n3);
    z = zeros(n1,n2,n3);
    df.apply(x,y);
    df.applyInverse(y,z);
    assertEqual(x,z);
    df.applyTranspose(x,y);
    df.applyInverseTranspose(y,z);
    assertEqual(x,z);

    float d1,d2;
    float tiny = n1*n2*n3*10.0f*FLT_EPSILON;
    x = rands(n1,n2,n3);
    y = rands(n1,n2,n3);
    z = zeros(n1,n2,n3);
    df.apply(x,z);
    d1 = dot(z,y);
    df.applyTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
    df.applyInverse(x,z);
    d1 = dot(z,y);
    df.applyInverseTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
  }

  private static float[] rands(int n1) {
    return sub(randfloat(n1),0.5f);
  }
  private static float[][] rands(int n1, int n2) {
    return sub(randfloat(n1,n2),0.5f);
  }
  private static float[][][] rands(int n1, int n2, int n3) {
    return sub(randfloat(n1,n2,n3),0.5f);
  }

  private static float[] zeros(int n1) {
    return zerofloat(n1);
  }
  private static float[][] zeros(int n1, int n2) {
    return zerofloat(n1,n2);
  }
  private static float[][][] zeros(int n1, int n2, int n3) {
    return zerofloat(n1,n2,n3);
  }

  private static float dot(float[] x, float[] y) {
    return sum(mul(x,y));
  }
  private static float dot(float[][] x, float[][] y) {
    return sum(mul(x,y));
  }
  private static float dot(float[][][] x, float[][][] y) {
    return sum(mul(x,y));
  }

  private static void assertEqual(float[] re, float[] ra) {
    int n1 = re.length;
    float tol= (float)(n1)*FLT_EPSILON;
    assertEqual(re,ra,tol);
  }
  private static void assertEqual(float[][] re, float[][] ra) {
    int n2 = re.length;
    int n1 = re[0].length;
    float tol= (float)(n1*n2)*FLT_EPSILON;
    assertEqual(re,ra,tol);
  }
  private static void assertEqual(float[][][] re, float[][][] ra) {
    int n3 = re.length;
    int n2 = re[0].length;
    int n1 = re[0][0].length;
    float tol= (float)(n1*n2*n3)*FLT_EPSILON;
    assertEqual(re,ra,tol);
  }

  private static void assertEqual(float[] re, float[] ra, float tol) {
    int n = re.length;
    for (int i=0; i<n; ++i)
      assertEquals(re[i],ra[i],tol);
  }
  private static void assertEqual(float[][] re, float[][] ra, float tol) {
    int n2 = re.length;
    int n1 = re[0].length;
    for (int i2=0; i2<n2; ++i2)
      for (int i1=0; i1<n1; ++i1)
        assertEquals(re[i2][i1],ra[i2][i1],tol);
  }
  private static void assertEqual(float[][][] re, float[][][] ra, float tol) {
    int n3 = re.length;
    int n2 = re[0].length;
    int n1 = re[0][0].length;
    for (int i3=0; i3<n3; ++i3)
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          assertEquals(re[i3][i2][i1],ra[i3][i2][i1],tol);
  }
}
