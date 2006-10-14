/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;
import edu.mines.jtk.util.Array;
import edu.mines.jtk.dsp.MinimumPhaseFilter;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.dsp.MinimumPhaseFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.11.22
 */
public class MinimumPhaseFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(MinimumPhaseFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1Random() {
    int[] lag1 = {0,1,2};
    float[] a = {2.0f,1.8f,0.81f};
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,a);
    int n = 100;
    float[] x,y,z;

    x = Array.randfloat(n);
    y = Array.zerofloat(n);
    z = Array.zerofloat(n);
    mpf.apply(x,y);
    mpf.applyInverse(y,z);
    assertEqual(x,z);
    mpf.applyTranspose(x,y);
    mpf.applyInverseTranspose(y,z);
    assertEqual(x,z);

    float d1,d2;
    x = Array.randfloat(n);
    y = Array.randfloat(n);
    z = Array.zerofloat(n);
    mpf.apply(x,z);
    d1 = dot(z,y);
    mpf.applyTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,n*100.0*FLT_EPSILON);
    mpf.applyInverse(x,z);
    d1 = dot(z,y);
    mpf.applyInverseTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,n*100.0*FLT_EPSILON);
  }

  public void test2Random() {
    int[] lag1 = {
       0, 1, 2, 3, 4,
      -4,-3,-2,-1, 0
    };
    int[] lag2 = {
       0, 0, 0, 0, 0,
       1, 1, 1, 1, 1
    };
    float[] a = { 
       1.79548454f, -0.64490664f, -0.03850411f, -0.01793403f, -0.00708972f,
      -0.02290331f, -0.04141619f, -0.08457147f, -0.20031442f, -0.55659920f
    };

    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,lag2,a);
    int n1 = 19;
    int n2 = 21;
    float[][] x,y,z;

    x = Array.randfloat(n1,n2);
    y = Array.zerofloat(n1,n2);
    z = Array.zerofloat(n1,n2);
    mpf.apply(x,y);
    mpf.applyInverse(y,z);
    assertEqual(x,z);
    mpf.applyTranspose(x,y);
    mpf.applyInverseTranspose(y,z);
    assertEqual(x,z);

    float d1,d2;
    x = Array.randfloat(n1,n2);
    y = Array.randfloat(n1,n2);
    z = Array.zerofloat(n1,n2);
    mpf.apply(x,z);
    d1 = dot(z,y);
    mpf.applyTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,n1*n2*100.0*FLT_EPSILON);
    mpf.applyInverse(x,z);
    d1 = dot(z,y);
    mpf.applyInverseTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,n1*n2*100.0*FLT_EPSILON);
  }

  public void testFactorFomel() {
    float[] r = {24.0f,242.0f,867.0f,1334.0f,867.0f,242.0f,24.0f};
    int n = r.length;
    int[] lag1 = {0,1,2,3};
    MinimumPhaseFilter mpf = MinimumPhaseFilter.factor(r,lag1);
    int nlag = lag1.length;
    float[] x = new float[nlag];
    float[] a = new float[nlag];
    x[0] = 1.0f;
    mpf.apply(x,a);
    assertEquals(24.0f,a[0],10*FLT_EPSILON);
    assertEquals(26.0f,a[1],10*FLT_EPSILON);
    assertEquals( 9.0f,a[2],10*FLT_EPSILON);
    assertEquals( 1.0f,a[3],10*FLT_EPSILON);
  }

  public void xtestFactorLaplacian() {
    float[][] r = {
      { 0.000f,-0.999f, 0.000f},
      {-0.999f, 4.000f,-0.999f},
      { 0.000f,-0.999f, 0.000f}
    };
    int n = r.length;
    int[] lag1 = {
                   0, 1, 2, 3, 4,
      -4,-3,-2,-1, 0
    };
    int[] lag2 = {
                   0, 0, 0, 0, 0,
       1, 1, 1, 1, 1
    };
    MinimumPhaseFilter.factor(r,lag1,lag2);
  }

  private static float dot(float[] x, float[] y) {
    int n = x.length;
    float sum = 0.0f;
    for (int i=0; i<n; ++i)
      sum += x[i]*y[i];
    return sum;
  }

  private static float dot(float[][] x, float[][] y) {
    int n1 = x[0].length;
    int n2 = x.length;
    float sum = 0.0f;
    for (int i2=0; i2<n2; ++i2)
      for (int i1=0; i1<n1; ++i1)
        sum += x[i2][i1]*y[i2][i1];
    return sum;
  }

  private static float dot(float[][][] x, float[][][] y) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    float sum = 0.0f;
    for (int i3=0; i3<n3; ++i3)
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          sum += x[i3][i2][i1]*y[i3][i2][i1];
    return sum;
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
