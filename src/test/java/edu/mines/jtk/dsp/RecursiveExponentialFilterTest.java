/****************************************************************************
Copyright (c) 2011, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.RecursiveExponentialFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2011.08.11
 */
public class RecursiveExponentialFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(RecursiveExponentialFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testInPlace() {
    int n1 = 101;
    int n2 = 102;
    int n3 = 103;
    float[] x1 = randfloat(n1);
    float[] y1 = new float[n1];
    float[] z1 = new float[n1];
    float[][] x2 = randfloat(n1,n2);
    float[][] y2 = new float[n2][n1];
    float[][] z2 = new float[n2][n1];
    float[][][] x3 = randfloat(n1,n2,n3);
    float[][][] y3 = new float[n3][n2][n1];
    float[][][] z3 = new float[n3][n2][n1];
    RecursiveExponentialFilter.Edges[] edgesAll = {
      RecursiveExponentialFilter.Edges.INPUT_ZERO_VALUE,
      RecursiveExponentialFilter.Edges.OUTPUT_ZERO_VALUE,
      RecursiveExponentialFilter.Edges.INPUT_ZERO_SLOPE,
      RecursiveExponentialFilter.Edges.OUTPUT_ZERO_SLOPE
    };
    RecursiveExponentialFilter ref = new RecursiveExponentialFilter(40.0);
    for (RecursiveExponentialFilter.Edges edges:edgesAll) {
      ref.setEdges(edges);
      copy(x1,z1); ref.apply(x1,y1); ref.apply(z1,z1); assertEqual(y1,z1);
      copy(x2,z2); ref.apply(x2,y2); ref.apply(z2,z2); assertEqual(y2,z2);
      copy(x3,z3); ref.apply(x3,y3); ref.apply(z3,z3); assertEqual(y3,z3);
    }
  }

  // Compare low-frequency response with that for Gaussian.
  public void testFrequencyResponse() {
    int n = 501;
    double sigma = 4.0;
    float[] x = new float[n]; x[n/2] = 1.0f;
    float[] ye = new float[n];
    float[] yg = new float[n];
    RecursiveExponentialFilter ref = new RecursiveExponentialFilter(sigma);
    RecursiveGaussianFilter rgf = new RecursiveGaussianFilter(sigma);
    ref.apply(x,ye);
    rgf.apply0(x,yg);
    Fft fft = new Fft(n);
    fft.setCenter(true);
    Sampling sf = fft.getFrequencySampling1();
    int i0 = sf.indexOfNearest(0.0);
    float[] ae = cabs(fft.applyForward(ye));
    float[] ag = cabs(fft.applyForward(yg));
    float e0 = ae[i0];
    float e1 = (ae[i0+1]-ae[i0-1])/2.0f;
    float e2 = ae[i0+1]-2.0f*ae[i0]+ae[i0-1];
    float g0 = ag[i0];
    float g1 = (ag[i0+1]-ag[i0-1])/2.0f;
    float g2 = ag[i0+1]-2.0f*ag[i0]+ag[i0-1];
    assertEquals(e0,g0,0.0001);
    assertEquals(e1,g1,0.0001);
    assertEquals(e2,g2,0.01*abs(g2));
  }

  // Check for symmetric positive-definite, or not.
  public void testSpd() {
    RecursiveExponentialFilter.Edges[] edgesAll = {
      RecursiveExponentialFilter.Edges.INPUT_ZERO_VALUE,
      RecursiveExponentialFilter.Edges.OUTPUT_ZERO_VALUE,
      RecursiveExponentialFilter.Edges.INPUT_ZERO_SLOPE,
      RecursiveExponentialFilter.Edges.OUTPUT_ZERO_SLOPE
    };
    for (RecursiveExponentialFilter.Edges edges:edgesAll) {
      testSpd1(edges);
      testSpd2(edges);
      testSpd3(edges);
    }
  }
  private static void testSpd1(RecursiveExponentialFilter.Edges edges) {
    int n1 = 201;
    float[] x = sub(randfloat(n1),0.5f);
    float[] y = sub(randfloat(n1),0.5f);
    float[] ax = new float[n1];
    float[] ay = new float[n1];
    RecursiveExponentialFilter ref = new RecursiveExponentialFilter(n1/5.0);
    ref.setEdges(edges);
    ref.apply(x,ax);
    ref.apply(y,ay);
    float yax = sum(mul(y,ax));
    float xay = sum(mul(x,ay));
    float xax = sum(mul(x,ax));
    float yay = sum(mul(y,ay));
    float tol = 100.0f*FLT_EPSILON;
    if (edges==RecursiveExponentialFilter.Edges.INPUT_ZERO_SLOPE) {
      float err = abs(xay-yax);
      assertTrue(err>tol);
    } else {
      assertTrue(xax>0.0f);
      assertTrue(yay>0.0f);
      assertEquals(xay,yax,tol);
    }
  }
  private static void testSpd2(RecursiveExponentialFilter.Edges edges) {
    int n1 = 201;
    int n2 = 203;
    float[][] x = sub(randfloat(n1,n2),0.5f);
    float[][] y = sub(randfloat(n1,n2),0.5f);
    float[][] ax = new float[n2][n1];
    float[][] ay = new float[n2][n1];
    RecursiveExponentialFilter ref = new RecursiveExponentialFilter(n1/5.0);
    ref.setEdges(edges);
    ref.apply(x,ax);
    ref.apply(y,ay);
    float yax = sum(mul(y,ax));
    float xay = sum(mul(x,ay));
    float xax = sum(mul(x,ax));
    float yay = sum(mul(y,ay));
    float tol = 100.0f*FLT_EPSILON;
    if (edges==RecursiveExponentialFilter.Edges.INPUT_ZERO_SLOPE) {
      float err = abs(xay-yax);
      assertTrue(err>tol);
    } else {
      assertTrue(xax>0.0f);
      assertTrue(yay>0.0f);
      assertEquals(xay,yax,tol);
    }
  }
  private static void testSpd3(RecursiveExponentialFilter.Edges edges) {
    int n1 = 101;
    int n2 = 103;
    int n3 = 105;
    float[][][] x = sub(randfloat(n1,n2,n3),0.5f);
    float[][][] y = sub(randfloat(n1,n2,n3),0.5f);
    float[][][] ax = new float[n3][n2][n1];
    float[][][] ay = new float[n3][n2][n1];
    RecursiveExponentialFilter ref = new RecursiveExponentialFilter(n1/5.0);
    ref.setEdges(edges);
    ref.apply(x,ax);
    ref.apply(y,ay);
    float yax = sum(mul(y,ax));
    float xay = sum(mul(x,ay));
    float xax = sum(mul(x,ax));
    float yay = sum(mul(y,ay));
    float tol = 100.0f*FLT_EPSILON;
    if (edges==RecursiveExponentialFilter.Edges.INPUT_ZERO_SLOPE) {
      float err = abs(xay-yax);
      assertTrue(err>tol);
    } else {
      assertTrue(xax>0.0f);
      assertTrue(yay>0.0f);
      assertEquals(xay,yax,tol);
    }
  }

  private void assertEqual(float[] e, float[] a) {
    int n = e.length;
    for (int i=0; i<n; ++i)
      assertEquals(e[i],a[i],0.0);
  }
  private void assertEqual(float[][] e, float[][] a) {
    int n = e.length;
    for (int i=0; i<n; ++i)
      assertEqual(e[i],a[i]);
  }
  private void assertEqual(float[][][] e, float[][][] a) {
    int n = e.length;
    for (int i=0; i<n; ++i)
      assertEqual(e[i],a[i]);
  }

  /*
  public static void test1() {
    int n1 = 201;
    float[] xi = makeImpulses(n1);
    float[] xs = makeSteps(n1);
    float[][] xAll = {xi,xs};
    RecursiveExponentialFilter.Edges[] edgesAll = {
      RecursiveExponentialFilter.Edges.INPUT_ZERO_VALUE,
      RecursiveExponentialFilter.Edges.OUTPUT_ZERO_VALUE,
      RecursiveExponentialFilter.Edges.INPUT_ZERO_SLOPE,
      RecursiveExponentialFilter.Edges.OUTPUT_ZERO_SLOPE
    };
    for (float[] x:xAll) {
      for (Edges edges:edgesAll) {
        test1ForEdges(edges,x);
      }
    }
  }
  private static float[] makeImpulses(int n1) {
    float[] x = new float[n1];
    x[0] = x[n1/2] = x[n1-1] = 1.0f;
    return x;
  }
  private static float[] makeSteps(int n1) {
    float[] x = fillfloat(1.0f,n1);
    for (int i1=0; i1<n1/2; ++i1)
      x[i1] = -1.0f;
    return x;
  }
  private static void test1ForEdges(Edges edges, float[] x) {
    RecursiveExponentialFilter ref = new RecursiveExponentialFilter(10.0);
    ref.setEdges(edges);
    float[] y = copy(x);
    ref.apply(y,y);
    System.out.println("x constant, y min="+min(y)+" max="+max(y));
    plot("y for "+edges,y);
  }

  public static void test2() {
    int n1 = 203;
    int n2 = 201;
    float[][] x = new float[n2][n1];
    float[][] y = new float[n2][n1];
    x[   0][   0] = 1.0f;
    x[   0][n1-1] = 1.0f;
    x[n2-1][   0] = 1.0f;
    x[n2-1][n1-1] = 1.0f;
    x[n2/2][n1/2] = 1.0f;
    RecursiveExponentialFilter ref = new RecursiveExponentialFilter(30.0);
    ref.setEdges(RecursiveExponentialFilter.Edges.INPUT_ZERO_VALUE);
    copy(x,y);
    ref.apply(y,y);
    plot("y",y);
    fill(1.0f,x);
    for (int i2=0; i2<n2; ++i2) {
      float s2 = (i2<n2/2)?-1.0f:1.0f;
      for (int i1=0; i1<n1; ++i1) {
        float s1 = (i1<n1/2)?-1.0f:1.0f;
          x[i2][i1] *= s1*s2;
      }
    }
    ref.setEdges(RecursiveExponentialFilter.Edges.OUTPUT_ZERO_SLOPE);
    ref.apply(x,y);
    System.out.println("x constant, y min="+min(y)+" max="+max(y));
    plot("y",y);
  }

  public static void test3() {
    int n1 = 203;
    int n2 = 201;
    int n3 = 199;
    float[][][] x = new float[n3][n2][n1];
    float[][][] y = new float[n3][n2][n1];
    x[   0][   0][   0] = 1.0f;
    x[   0][   0][n1-1] = 1.0f;
    x[   0][n2-1][   0] = 1.0f;
    x[   0][n2-1][n1-1] = 1.0f;
    x[n3-1][   0][   0] = 1.0f;
    x[n3-1][   0][n1-1] = 1.0f;
    x[n3-1][n2-1][   0] = 1.0f;
    x[n3-1][n2-1][n1-1] = 1.0f;
    x[n3/2][n2/2][n1/2] = 1.0f;
    RecursiveExponentialFilter ref = new RecursiveExponentialFilter(40.0);
    ref.setEdges(RecursiveExponentialFilter.Edges.INPUT_ZERO_VALUE);
    copy(x,y);
    ref.apply(y,y);
    plot("y",copy(y));
    fill(1.0f,x);
    for (int i3=0; i3<n3; ++i3) {
      float s3 = (i3<n3/2)?-1.0f:1.0f;
      for (int i2=0; i2<n2; ++i2) {
        float s2 = (i2<n2/2)?-1.0f:1.0f;
        for (int i1=0; i1<n1; ++i1) {
          float s1 = (i1<n1/2)?-1.0f:1.0f;
            x[i3][i2][i1] *= s1*s2*s3;
        }
      }
    }
    ref.setEdges(RecursiveExponentialFilter.Edges.OUTPUT_ZERO_SLOPE);
    ref.apply(x,y);
    System.out.println("x constant, y min="+min(y)+" max="+max(y));
    plot("y",y);
  }

  public static void plot(String title, float[] x) {
    SimplePlot sp = SimplePlot.asPoints(x);
    sp.setTitle(title);
  }

  public static void plot(String title, float[][] x) {
    SimplePlot sp = new SimplePlot();
    PixelsView pv = sp.addPixels(x);
    pv.setInterpolation(PixelsView.Interpolation.NEAREST);
    pv.setColorModel(ColorMap.JET);
    sp.addColorBar();
    sp.setTitle(title);
  }

  public static void plot(String title, float[][][] x) {
    SimpleFrame sf = SimpleFrame.asImagePanels(x);
    sf.setSize(800,800);
  }
  */
}
