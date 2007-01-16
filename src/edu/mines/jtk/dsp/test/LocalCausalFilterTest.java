/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import static edu.mines.jtk.util.MathPlus.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.dsp.LocalCausalFilter;
import edu.mines.jtk.util.Array;

/**
 * Tests {@link edu.mines.jtk.dsp.LocalCausalFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.15
 */
public class LocalCausalFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(LocalCausalFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1Random() {
    int[] lag1 = {0,1,2};
    final float[] ar = {  1.00f, -1.80f,  0.81f}; // (1-0.9z)(1-0.9z)
    final float[] as = {  1.00f, -1.60f,  0.64f}; // (1-0.8z)(1-0.8z)
    LocalCausalFilter lcf = new LocalCausalFilter(lag1);
    LocalCausalFilter.A1 a1 = new LocalCausalFilter.A1() {
      public void get(int i1, float[] a) {
        if (i1%2==0) {
          a[0] = ar[0];  a[1] = ar[1];  a[2] = ar[2];
        } else {
          a[0] = as[0];  a[1] = as[1];  a[2] = as[2];
        }
      }
    };
    int n = 100;
    float tiny = n*10.0f*FLT_EPSILON;

    { // y'Ax == x'A'y
      float[] x = randfloat(n);
      float[] y = randfloat(n);
      float[] ax = zerofloat(n);
      float[] ay = zerofloat(n);
      lcf.apply(a1,x,ax);
      lcf.applyTranspose(a1,y,ay);
      float dyx = dot(y,ax);
      float dxy = dot(x,ay);
      assertEquals(dyx,dxy,tiny);
    }

    { // y'Bx == x'B'y (for B = inv(A))
      float[] x = randfloat(n);
      float[] y = randfloat(n);
      float[] bx = zerofloat(n);
      float[] by = zerofloat(n);
      lcf.applyInverse(a1,x,bx);
      lcf.applyInverseTranspose(a1,y,by);
      float dyx = dot(y,bx);
      float dxy = dot(x,by);
      assertEquals(dyx,dxy,tiny);
    }

    { // x == BAx (for B = inv(A))
      float[] x = randfloat(n);
      float[] y = Array.copy(x);
      lcf.apply(a1,y,y); // in-place
      lcf.applyInverse(a1,y,y); // in-place
      assertEqual(x,y);
    }

    { // x == A'B'x (for B = inv(A))
      float[] x = randfloat(n);
      float[] y = zerofloat(n);
      lcf.applyInverseTranspose(a1,x,y); // *not* in-place
      lcf.applyTranspose(a1,y,y); // in-place
      assertEqual(x,y);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static float[] randfloat(int n1) {
    return Array.sub(Array.randfloat(n1),0.5f);
  }
  private static float[][] randfloat(int n1, int n2) {
    return Array.sub(Array.randfloat(n1,n2),0.5f);
  }
  private static float[][][] randfloat(int n1, int n2, int n3) {
    return Array.sub(Array.randfloat(n1,n2,n3),0.5f);
  }

  private static float[] zerofloat(int n1) {
    return Array.zerofloat(n1);
  }
  private static float[][] zerofloat(int n1, int n2) {
    return Array.zerofloat(n1,n2);
  }
  private static float[][][] zerofloat(int n1, int n2, int n3) {
    return Array.zerofloat(n1,n2,n3);
  }

  private static float dot(float[] x, float[] y) {
    return Array.sum(Array.mul(x,y));
  }
  private static float dot(float[][] x, float[][] y) {
    return Array.sum(Array.mul(x,y));
  }
  private static float dot(float[][][] x, float[][][] y) {
    return Array.sum(Array.mul(x,y));
  }

  private static void assertEqual(float[] re, float[] ra) {
    int n = re.length;
    float tolerance = (float)(n)*FLT_EPSILON;
    for (int i=0; i<n; ++i)
      assertEquals(re[i],ra[i],tolerance);
  }

  private static void assertEqual(float[][] re, float[][] ra) {
    int n2 = re.length;
    int n1 = re[0].length;
    float tolerance = (float)(n1*n2)*FLT_EPSILON;
    for (int i2=0; i2<n2; ++i2)
      for (int i1=0; i1<n1; ++i1)
        assertEquals(re[i2][i1],ra[i2][i1],tolerance);
  }

  private static void assertEqual(float[][][] re, float[][][] ra) {
    int n3 = re.length;
    int n2 = re[0].length;
    int n1 = re[0][0].length;
    float tolerance = (float)(n1*n2*n3)*FLT_EPSILON;
    for (int i3=0; i3<n3; ++i3)
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          assertEquals(re[i3][i2][i1],ra[i3][i2][i1],tolerance);
  }
}
