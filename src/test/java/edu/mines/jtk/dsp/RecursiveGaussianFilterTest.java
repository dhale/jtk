/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Random;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.RecursiveGaussianFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.11.25
 */
public class RecursiveGaussianFilterTest extends TestCase {
  public static void main(String[] args) {
    if (args.length>0 && args[0].equals("bench")) {
      boolean parallel = (args.length>1 && args[1].equals("serial")) ?
        false : true;
      bench(parallel);
    }
    TestSuite suite = new TestSuite(RecursiveGaussianFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1() {
    float sigma = 10.0f;
    int n = 1+2*(int)(sigma*4.0f);
    int k = (n-1)/2;
    float[] x = zerofloat(n);
    float[] y = zerofloat(n);
    x[k] = 1.0f;
    float tolerance = 0.01f*gaussian(sigma,0.0f);
    RecursiveGaussianFilter rf = new RecursiveGaussianFilter(sigma);
    rf.apply0(x,y);
    for (int i=0; i<n; ++i) {
      float gi = gaussian(sigma,i-k);
      assertEquals(gi,y[i],tolerance);
    }
  }

  public void test2() {
    float sigma = 10.0f;
    int n1 = 1+2*(int)(sigma*4.0f);
    int n2 = n1+2;
    int k1 = (n1-1)/2;
    int k2 = (n2-1)/2;
    float[][] x = zerofloat(n1,n2);
    float[][] y = zerofloat(n1,n2);
    x[k2][k1] = 1.0f;
    float tolerance = 0.01f*gaussian(sigma,0.0f,0.0f);
    RecursiveGaussianFilter rf = new RecursiveGaussianFilter(sigma);
    rf.apply00(x,y);
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        float gi = gaussian(sigma,i1-k1,i2-k2);
        assertEquals(gi,y[i2][i1],tolerance);
      }
    }
  }

  public void test3() {
    float sigma = 10.0f;
    int n1 = 1+2*(int)(sigma*4.0f);
    int n2 = n1+2;
    int n3 = n2+2;
    int k1 = (n1-1)/2;
    int k2 = (n2-1)/2;
    int k3 = (n3-1)/2;
    float[][][] x = zerofloat(n1,n2,n3);
    float[][][] y = zerofloat(n1,n2,n3);
    x[k3][k2][k1] = 1.0f;
    float tolerance = 0.01f*gaussian(sigma,0.0f,0.0f);
    RecursiveGaussianFilter rf = new RecursiveGaussianFilter(sigma);
    rf.apply000(x,y);
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float gi = gaussian(sigma,i1-k1,i2-k2,i3-k3);
          assertEquals(gi,y[i3][i2][i1],tolerance);
        }
      }
    }
  }

  private static float gaussian(float s, float x) {
    float xx = x*x;
    float ss = s*s;
    return exp(-0.5f*xx/ss)/sqrt(2.0f*FLT_PI*ss);
  }
  private static float gaussian(float s, float x1, float x2) {
    return gaussian(s,x1)*gaussian(s,x2);
  }
  private static float gaussian(float s, float x1, float x2, float x3) {
    return gaussian(s,x1,x2)*gaussian(s,x3);
  }

  ///////////////////////////////////////////////////////////////////////////
  // benchmark

  private static void bench(boolean parallel) {
    Parallel.setParallel(parallel);
    bench3();
  }

  private static void bench3() {
    int n1 = 501;
    int n2 = 502;
    int n3 = 503;
    Random r = new Random(314159);
    float[][][] x = randfloat(r,n1,n2,n3);
    float[][][] y = randfloat(r,n1,n2,n3);
    RecursiveGaussianFilter rf = new RecursiveGaussianFilter(3.0f);
    int niter;
    double maxtime = 5.0;
    double nsample = (double)n1*(double)n2*(double)n3;
    Stopwatch sw = new Stopwatch();
    for (int itest=0; itest<3; ++itest) {
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        rf.apply000(x,y);
      sw.stop();
      float sum = sum(y);
      int rate = (int)(1.0e-6*niter*nsample/sw.time());
      System.out.println("rate = "+rate+"  sum = "+sum);
    }
  }
}
