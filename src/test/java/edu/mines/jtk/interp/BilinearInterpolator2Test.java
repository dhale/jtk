/****************************************************************************
Copyright (c) 2013, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.dsp.Sampling;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.interp.BilinearInterpolator2}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2013.01.22
 */
public class BilinearInterpolator2Test extends TestCase {
  
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(BilinearInterpolator2Test.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testSingleValues() {
    float[][][] xy = sampleTestFunction(11,12);
    float[] x1 = xy[0][0];
    float[] x2 = xy[0][1];
    float[][] y = xy[1];
    float x1min = min(x1), x1max = max(x1), x1del = x1max-x1min;
    float x2min = min(x2), x2max = max(x2), x2del = x2max-x2min;
    x1min -= 0.2f*x1del; x1max += 0.2f*x1del;
    x2min -= 0.2f*x2del; x2max += 0.2f*x2del;
    BilinearInterpolator2 bi = makeInterpolator(x1,x2,y);
    int n = 101;
    Random r = new Random(5);
    for (int i=0; i<n; ++i) {
      float x1i = x1min+(x1max-x1min)*r.nextFloat();
      float x2i = x2min+(x2max-x2min)*r.nextFloat();
      float y00 = bi.interpolate00(x1i,x2i);
      float y10 = bi.interpolate10(x1i,x2i);
      float y01 = bi.interpolate01(x1i,x2i);
      assertEqual(testFunction00(x1i,x2i),y00);
      assertEqual(testFunction10(x1i,x2i),y10);
      assertEqual(testFunction01(x1i,x2i),y01);
    }
  }

  public void testArrayValues() {
    float[][][] xy = sampleTestFunction(11,13);
    float[] x1 = xy[0][0];
    float[] x2 = xy[0][1];
    float[][] y = xy[1];
    float x1min = min(x1), x1max = max(x1);
    float x2min = min(x2), x2max = max(x2);
    BilinearInterpolator2 bi = makeInterpolator(x1,x2,y);
    int n1i = 101;
    int n2i = 102;
    float d1i = (x1max-x1min)/(n1i-1);
    float d2i = (x2max-x2min)/(n2i-1);
    float f1i = x1min;
    float f2i = x2min;
    float[] x1i = rampfloat(f1i,d1i,n1i);
    float[] x2i = rampfloat(f2i,d2i,n2i);
    float[][] yi = bi.interpolate(x1i,x2i);
    for (int i2i=0; i2i<n2i; ++i2i)
      for (int i1i=0; i1i<n1i; ++i1i)
        assertEqual(testFunction00(x1i[i1i],x2i[i2i]),yi[i2i][i1i]);
  }

  public void testSampleValues() {
    float[][][] xy = sampleTestFunction(11,13);
    float[] x1 = xy[0][0];
    float[] x2 = xy[0][1];
    float[][] y = xy[1];
    float x1min = min(x1), x1max = max(x1);
    float x2min = min(x2), x2max = max(x2);
    BilinearInterpolator2 bi = makeInterpolator(x1,x2,y);
    int n1i = 101;
    int n2i = 102;
    float d1i = (x1max-x1min)/(n1i-1);
    float d2i = (x2max-x2min)/(n2i-1);
    float f1i = x1min;
    float f2i = x2min;
    Sampling s1i = new Sampling(n1i,d1i,f1i);
    Sampling s2i = new Sampling(n2i,d2i,f2i);
    float[][] yi = bi.interpolate(s1i,s2i);
    for (int i2i=0; i2i<n2i; ++i2i) {
      float x2i = (float)s2i.getValue(i2i);
      for (int i1i=0; i1i<n1i; ++i1i) {
        float x1i = (float)s1i.getValue(i1i);
        assertEqual(testFunction00(x1i,x2i),yi[i2i][i1i]);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static BilinearInterpolator2 makeInterpolator(
    float[] x1, float[] x2, float[][] y)
  {
    return new BilinearInterpolator2(x1,x2,y);
  }

  private static float[][][] sampleTestFunction(int n1, int n2) {
    Random r = new Random(3);
    float[] x1 = mul(2.0f,randfloat(r,n1));
    float[] x2 = mul(2.0f,randfloat(r,n2));
    quickSort(x1);
    quickSort(x2);
    float[][] y = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2)
      for (int i1=0; i1<n1; ++i1)
        y[i2][i1] = testFunction00(x1[i1],x2[i2]);
    return new float[][][]{new float[][]{x1,x2},y};
  }
  private static float testFunction00(float x1, float x2) {
    return (1.1f+1.3f*x1)*(1.2f+1.4f*x2);
  }
  private static float testFunction10(float x1, float x2) {
    return 1.3f*(1.2f+1.4f*x2);
  }
  private static float testFunction01(float x1, float x2) {
    return 1.4f*(1.1f+1.3f*x1);
  }

  private static void assertEqual(float x, float y) {
    assertTrue(x+" = "+y,almostEqual(x,y));
  }
  
  private static boolean almostEqual(float x, float y) {
    float ax = abs(x);
    float ay = abs(y);
    return abs(x-y)<=0.001f*max(ax,ay);
  }

}
