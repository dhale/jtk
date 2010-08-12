/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.util.Quantiler}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2000.10.01, 2006.07.13
 */
public class QuantilerTest extends TestCase {
  public static void main(String[] args) {
    if (args.length>0 && args[0].equals("bench"))
      bench();
    TestSuite suite = new TestSuite(QuantilerTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testLinear() {

    int n = 101;
    float[] f = new float[n];
    for (int i=0; i<n; ++i)
      f[i] = (float)i/(float)(n-1);

    for (int i=0; i<n; ++i) {
      float q = f[i];
      Quantiler quantiler = new Quantiler(q);
      int nupdate = 20;
      for (int iupdate=0; iupdate<nupdate; ++iupdate) {
        quantiler.update(f);
      }
      float e = quantiler.estimate();
      float elo = q-0.01f;
      float ehi = q+0.01f;
      assertTrue(elo<=e);
      assertTrue(e<=ehi);
    }
  }

  public void testRandom() {
    int n = 10000;
    float[] f = randfloat(new Random(314159),n);
    int ntest = 101;
    for (int itest=0; itest<ntest; ++itest) {
      float q = (float)itest/(float)(ntest-1);
      Quantiler quantiler = new Quantiler(q);
      quantiler.update(f);
      float e = quantiler.estimate();
      float elo = q-0.01f;
      float ehi = q+0.01f;
      assertTrue(elo<=e);
      assertTrue(e<=ehi);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // benchmark

  public static void bench() {
    Stopwatch sw = new Stopwatch();
    int n = 1000001; // odd, so median equals one array element
    double maxtime = 2.0;
    int nq,rate;

    for (int ntrial=0; ntrial<3; ++ntrial) {
      float[] f1 = randfloat(n);

      float q1 = 0.0f;
      sw.restart();
      for (nq=0; sw.time()<maxtime; ++nq) {
        Quantiler quantiler = new Quantiler(0.5f);
        quantiler.update(f1);
        q1 = quantiler.estimate();
      }
      sw.stop();
      rate = (int)(1.0e-6*nq*n/sw.time());
      System.out.println("Quantiler:        median="+q1+" rate="+rate);

      float q2 = 0.0f;
      sw.restart();
      for (nq=0; sw.time()<maxtime; ++nq) {
        float[] f2 = copy(f1);
        quickPartialSort(n/2,f2);
        q2 = f2[n/2];
      }
      sw.stop();
      rate = (int)(1.0e-6*nq*n/sw.time());
      System.out.println("quickPartialSort: median="+q2+" rate="+rate);
    }
  }
}
