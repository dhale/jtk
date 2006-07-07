package com.lgc.idh.util.test;

import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.lgc.idh.util.Array;
import com.lgc.idh.util.Quantiler;
import com.lgc.idh.util.Stopwatch;

/**
 * Tests {@link com.lgc.idh.util.Quantiler}.
 * @author Dave Hale
 * @version 2000.10.01
 */
public class QuantilerTest extends TestCase {

  public void xtestSimple() {
    int m = 101;
    int n = 100001;
    Random random = new Random();
    Quantiler q = new Quantiler(0.5f);
    for (int i=0; i<n; ++i) {
      int x;
      x = i;
      //x = n-1-i;
      //x = (i%2==0)?(n-i-1)/2:(n+i)/2;
      //x = (i%2==0)?i/2:n-(i+1)/2;
      //x = (i%2==0)?n-1-i/2:(i-1)/2;
      x = random.nextInt(m);
      //System.out.println("i="+i+" x="+x+" q="+q.estimate());
      q.update((float)x);
    }
    System.out.println("estimate="+q.estimate());
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

    Random r = new Random();
    r.setSeed(314159);
    int n = 10000;
    float[] f = new float[n];
    for (int i=0; i<n; ++i)
      f[i] = r.nextFloat();

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
  // Benchmark.

  public static void bench() {
    Stopwatch sw = new Stopwatch();

    for (int iter=0; iter<10; ++iter) {
      Random r = new Random();
      int n = 1000001;
      float[] f1 = new float[n];
      for (int i=0; i<n; ++i)
        f1[i] = r.nextFloat();

      sw.restart();
      Quantiler quantiler = new Quantiler(0.5f);
      quantiler.update(f1);
      float q1 = quantiler.estimate();
      sw.stop();
      System.out.println("Quantiler median="+q1+" in "+sw.time()+" seconds.");

      sw.restart();
      float[] f2 = Array.copy(f1);
      Array.quickPartialSort(n/2,f2);
      float q2 = f2[n/2];
      sw.stop();
      System.out.println("quickPart median="+q2+" in "+sw.time()+" seconds.");

      try {
        Thread.sleep(2000);
      } catch (Exception e) {
      }
      System.out.println();
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // Setup.
  
  public QuantilerTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(QuantilerTest.class);
  }

  public static void main(String[] args) {
    if (args.length>0 && args[0].equals("bench"))
      bench();
    junit.textui.TestRunner.run(suite());
  }
}
