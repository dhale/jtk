/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.Recursive2ndOrderFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.11.22
 */
public class Recursive2ndOrderFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(Recursive2ndOrderFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1() {
    test1(2.00f, 0.00f, 0.00f, 0.90f, 0.00f);
    test1(2.00f, 4.00f, 0.00f, 0.90f, 0.00f);
    test1(2.00f, 4.00f, 0.00f, 1.80f, 0.81f);
    test1(0.00f, 4.00f, 2.00f, 1.80f, 0.81f);
    test1(2.00f, 4.00f, 2.00f, 1.80f, 0.81f);
  }

  public void test1(float b0, float b1, float b2, float a1, float a2) {
    int n = 100;
    float[] x,y1,y2;
    x = randfloat(n);
    Recursive2ndOrderFilter rf = new Recursive2ndOrderFilter(b0,b1,b2,a1,a2);

    y1 = copy(x);
    rf.applyForward(y1,y1);
    y2 = reverse(x);
    rf.applyReverse(y2,y2);
    y2 = reverse(y2);
    assertEqual(y1,y2);

    rf.accumulateForward(y1,y1);
    y2 = reverse(y2);
    rf.accumulateReverse(y2,y2);
    y2 = reverse(y2);
    assertEqual(y1,y2);
  }

  public void test2() {
    test2(2.00f, 0.00f, 0.00f, 0.90f, 0.00f);
    test2(2.00f, 4.00f, 0.00f, 0.90f, 0.00f);
    test2(2.00f, 4.00f, 0.00f, 1.80f, 0.81f);
    test2(0.00f, 4.00f, 2.00f, 1.80f, 0.81f);
    test2(2.00f, 4.00f, 2.00f, 1.80f, 0.81f);
  }

  public void test2(float b0, float b1, float b2, float a1, float a2) {
    int n = 20;
    float[][] x,y1,y2;
    x = randfloat(n,n);
    Recursive2ndOrderFilter rf = new Recursive2ndOrderFilter(b0,b1,b2,a1,a2);

    y1 = copy(x);
    rf.apply1Forward(y1,y1);
    rf.accumulate1Forward(y1,y1);

    y2 = transpose(x);
    rf.apply2Forward(y2,y2);
    rf.accumulate2Forward(y2,y2);
    y2 = transpose(y2);
    assertEqual(y1,y2);

    y1 = copy(x);
    rf.apply1Reverse(y1,y1);
    rf.accumulate1Reverse(y1,y1);

    y2 = transpose(x);
    rf.apply2Reverse(y2,y2);
    rf.accumulate2Reverse(y2,y2);
    y2 = transpose(y2);
    assertEqual(y1,y2);
  }

  public void test3() {
    test3(2.00f, 0.00f, 0.00f, 0.90f, 0.00f);
    test3(2.00f, 4.00f, 0.00f, 0.90f, 0.00f);
    test3(2.00f, 4.00f, 0.00f, 1.80f, 0.81f);
    test3(0.00f, 4.00f, 2.00f, 1.80f, 0.81f);
    test3(2.00f, 4.00f, 2.00f, 1.80f, 0.81f);
  }

  public void test3(float b0, float b1, float b2, float a1, float a2) {
    int n = 20;
    float[][][] x,y1,y2;
    x = randfloat(n,n,n);
    Recursive2ndOrderFilter rf = new Recursive2ndOrderFilter(b0,b1,b2,a1,a2);

    y1 = copy(x);
    rf.apply1Forward(y1,y1);
    rf.accumulate1Forward(y1,y1);

    y2 = transpose12(x);
    rf.apply2Forward(y2,y2);
    rf.accumulate2Forward(y2,y2);
    y2 = transpose12(y2);
    assertEqual(y1,y2);

    y2 = transpose13(x);
    rf.apply3Forward(y2,y2);
    rf.accumulate3Forward(y2,y2);
    y2 = transpose13(y2);
    assertEqual(y1,y2);

    y1 = copy(x);
    rf.apply1Reverse(y1,y1);
    rf.accumulate1Reverse(y1,y1);

    y2 = transpose12(x);
    rf.apply2Reverse(y2,y2);
    rf.accumulate2Reverse(y2,y2);
    y2 = transpose12(y2);
    assertEqual(y1,y2);

    y2 = transpose13(x);
    rf.apply3Reverse(y2,y2);
    rf.accumulate3Reverse(y2,y2);
    y2 = transpose13(y2);
    assertEqual(y1,y2);
  }

  private void assertEqual(float[] re, float[] ra) {
    int n = re.length;
    float tolerance = (float)(n)*FLT_EPSILON;
    for (int i=0; i<n; ++i)
      assertEquals(re[i],ra[i],tolerance);
  }

  private void assertEqual(float[][] re, float[][] ra) {
    int n2 = re.length;
    int n1 = re[0].length;
    float tolerance = (float)(n1*n2)*FLT_EPSILON;
    for (int i2=0; i2<n2; ++i2)
      for (int i1=0; i1<n1; ++i1)
        assertEquals(re[i2][i1],ra[i2][i1],tolerance);
  }

  private void assertEqual(float[][][] re, float[][][] ra) {
    int n3 = re.length;
    int n2 = re[0].length;
    int n1 = re[0][0].length;
    float tolerance = (float)(n1*n2*n3)*FLT_EPSILON;
    for (int i3=0; i3<n3; ++i3)
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          assertEquals(re[i3][i2][i1],ra[i3][i2][i1],tolerance);
  }

  private static float[][][] transpose12(float[][][] x) {
    int n3 = x.length;
    int n2 = x[0].length;
    int n1 = x[0][0].length;
    float[][][] y = new float[n3][n1][n2];
    for (int i3=0; i3<n3; ++i3) {
      float[][] y3 = y[i3];
      for (int i2=0; i2<n2; ++i2) {
        float[] x32 = x[i3][i2];
        for (int i1=0; i1<n1; ++i1) {
          y3[i1][i2] = x32[i1];
        }
      }
    }
    return y;
  }

  private static float[][][] transpose13(float[][][] x) {
    int n3 = x.length;
    int n2 = x[0].length;
    int n1 = x[0][0].length;
    float[][][] y = new float[n1][n2][n3];
    for (int i2=0; i2<n2; ++i2) {
      for (int i3=0; i3<n3; ++i3) {
        float[] x32 = x[i3][i2];
        for (int i1=0; i1<n1; ++i1) {
          y[i1][i2][i3] = x32[i1];
        }
      }
    }
    return y;
  }

  /*
  private static float[][][] transpose23(float[][][] x) {
    int n3 = x.length;
    int n2 = x[0].length;
    int n1 = x[0][0].length;
    float[][][] y = new float[n2][n3][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i3=0; i3<n3; ++i3) {
        float[] x32 = x[i3][i2];
        float[] y23 = y[i2][i3];
        for (int i1=0; i1<n1; ++i1) {
          y23[i1] = x32[i1];
        }
      }
    }
    return y;
  }
  */
}
