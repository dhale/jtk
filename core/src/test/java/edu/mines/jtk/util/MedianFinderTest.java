/****************************************************************************
Copyright 2010, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.util;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.util.MedianFinder}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.10.10
 */
public class MedianFinderTest extends TestCase {
  public static void main(String[] args) {
    if (args.length>0 && args[0].equals("bench"))
      bench();
    TestSuite suite = new TestSuite(MedianFinderTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testUnweighted() {
    Random r = new Random();
    int ntest = 100;
    for (int itest=0; itest<ntest; ++itest) {
      int n = 1+(r.nextBoolean()?r.nextInt(100):r.nextInt(10));
      float[] f = randfloat(n);
      float[] w = fillfloat(1.0f,n);
      if (r.nextBoolean()) {
        for (int i=n/4; i<3*n/4; ++i)
          f[i] = f[0];
      }
      MedianFinder mf = new MedianFinder(n);
      float ew = mf.findMedian(w,f);
      float eu = mf.findMedian(f);
      assertTrue(ew==eu);
    }
  }

  public void testWeighted() {
    Random r = new Random();
    int ntest = 100;
    for (int itest=0; itest<ntest; ++itest) {
      int n = 1+(r.nextBoolean()?r.nextInt(100):r.nextInt(10));
      float[] w = randfloat(n);
      float[] f = randfloat(n);

      // Weighted median using a slow complete sort.
      int[] k = rampint(0,1,n);
      quickIndexSort(f,k);
      float wsum = 0.0f;
      float wtotal = sum(w);
      int i;
      for (i=0; i<n && wsum<0.5f*wtotal; ++i)
        wsum += w[k[i]];
      float qslow = f[k[i-1]];

      // Weighted median using fast median finder.
      MedianFinder mf = new MedianFinder(n);
      float qfast = mf.findMedian(w,f);

      assertTrue(qslow==qfast);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // benchmark

  public static void bench() {
    double maxtime = 2.0;
    Stopwatch sw = new Stopwatch();
    for (int n=10; n<10000; n*=10) {
      System.out.println("n="+n);
      MedianFinder mf = new MedianFinder(n);
      int nq,rate;
      for (int ntrial=0; ntrial<3; ++ntrial) {
        float[] w = fillfloat(1.0f,n);
        float[] f = randfloat(n);

        float q1 = 0.0f;
        sw.restart();
        for (nq=0; sw.time()<maxtime; ++nq)
          q1 = mf.findMedian(w,f);
        sw.stop();
        rate = (int)(1.0e-6*nq*n/sw.time());
        System.out.println("  weighted: median="+q1+" rate="+rate);

        float q2 = 0.0f;
        sw.restart();
        for (nq=0; sw.time()<maxtime; ++nq)
          q2 = mf.findMedian(f);
        sw.stop();
        rate = (int)(1.0e-6*nq*n/sw.time());
        System.out.println("unweighted: median="+q2+" rate="+rate);

        float q3 = 0.0f;
        sw.restart();
        for (nq=0; sw.time()<maxtime; ++nq)
          q3 = sum(f)/n;
        sw.stop();
        rate = (int)(1.0e-6*nq*n/sw.time());
        System.out.println("   average: mean="+q3+" rate="+rate);
      }
    }
  }
}
