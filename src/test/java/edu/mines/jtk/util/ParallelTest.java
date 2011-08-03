/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;
import static edu.mines.jtk.util.Parallel.*;

/**
 * Tests {@link edu.mines.jtk.util.Parallel}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.11.23
 */
public class ParallelTest extends TestCase {
  public static void main(String[] args) {
    if (args.length>0 && args[0].equals("bench")) {
      boolean parallel = (args.length>1 && args[1].equals("serial")) ?
        false : true;
      bench(parallel);
    }
    TestSuite suite = new TestSuite(ParallelTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testRandom() {
    for (int ntest=0; ntest<1000; ++ntest) {
      oneRandomTest();
    }
  }
  private void oneRandomTest() {
    Random r = new Random();
    int n = 100+r.nextInt(100);
    int begin = r.nextInt(n);
    int end = begin+1+r.nextInt(n-begin);
    int step = 1+r.nextInt(6);
    int chunk = 1+r.nextInt(4);
    float[] a = randfloat(n);
    float[] bs = zerofloat(n);
    float[] bp = zerofloat(n);
    sqrS(begin,end,step,a,bs);
    sqrP(begin,end,step,chunk,a,bp);
    assertEquals(bs,bp,0.0f);
    float ss = sumS(begin,end,step,a);
    float sp = sumP(begin,end,step,chunk,a);
    assertEquals(ss,sp,0.0001f*max(ss,sp));
  }
  private void sqrS(int begin, int end, int step, float[] a, float[] b) {
    for (int i=begin; i<end; i+=step)
      b[i] = a[i]*a[i];
  }
  private void sqrP(int begin, int end, int step, int chunk, 
    final float[] a, final float[] b) 
  {
    loop(begin,end,step,chunk,new LoopInt() {
      public void compute(int i) {
        b[i] = a[i]*a[i];
      }
    });
  }
  private float sumS(int begin, int end, int step, float[] a) {
    float s = 0.0f;
    for (int i=begin; i<end; i+=step)
      s += a[i];
    return s;
  }
  private float sumP(int begin, int end, int step, int chunk, 
    final float[] a) 
  {
    //trace("begin="+begin+" end="+end+" step="+step+" chunk="+chunk);
    return reduce(begin,end,step,chunk,new ReduceInt<Float>() {
      public Float compute(int i) {
        return a[i];
      }
      public Float combine(Float s1, Float s2) {
        return s1+s2;
      }
    });
  }

  public void testUnsafe() {
    final Unsafe<Worker> nts = new Unsafe<Worker>();
    loop(20,new LoopInt() {
      public void compute(int i) {
        Worker w = nts.get();
        if (w==null) nts.set(w=new Worker());
        w.work();
      }
    });
    /* this simpler version will fail with multithreading
    final Worker w = new Worker();
    loop(20,new LoopInt() {
      public void compute(int i) {
        w.work();
      }
    });
    */
  }
  private static class Worker { // a simple class that is not thread safe
    public void work() { // should fail if entered from multiple threads
      assertTrue(!_working);
      _working = true;
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      _working = false;
    }
    private boolean _working;
  }


  private static void assertEquals(float[] e, float[] a, float t) {
    int n = e.length;
    for (int i=0; i<n; ++i)
      assertEquals(e[i],a[i],t);
  }

  private static void trace(String s) {
    System.out.println(s);
  }

  ///////////////////////////////////////////////////////////////////////////
  // benchmark

  // Squares of elements of 2D and 3D arrays
  private static void benchArraySqr() {
    int n1 = 501;
    int n2 = 502;
    int n3 = 503;
    System.out.println("Array sqr: n1="+n1+" n2="+n2+" n3="+n3);
    int niter,rate;
    double maxtime = 5.0;
    double mflop2 = 1.0e-6*n1*n2;
    double mflop3 = 1.0e-6*n1*n2*n3;
    Stopwatch sw = new Stopwatch();
    float[][][] a = sub(randfloat(n1,n2,n3),0.5f);
    float[][][] bs = copy(a);
    float[][][] bp = copy(a);
    for (int ntest=0; ntest<3; ++ntest) {
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        sqrS(a[0],bs[0]);
      sw.stop();
      rate = (int)((niter*mflop2)/sw.time());
      System.out.println("2D S: rate = "+rate);
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        sqrP(a[0],bp[0]);
      sw.stop();
      rate = (int)((niter*mflop2)/sw.time());
      System.out.println("2D P: rate = "+rate);
      //System.out.println("    :  err = "+emax(bp[0],bs[0]));
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        sqrS(a,bs);
      sw.stop();
      rate = (int)((niter*mflop3)/sw.time());
      System.out.println("3D S: rate = "+rate);
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        sqrP(a,bp);
      sw.stop();
      rate = (int)((niter*mflop3)/sw.time());
      System.out.println("3D P: rate = "+rate);
      //System.out.println("    :  err = "+emax(bp,bs));
    }
  }
  private static void sqr(float[] a, float[] b) {
    int n = a.length;
    for (int i=0; i<n; ++i)
      b[i] = a[i]*a[i];
  }
  private static void sqrS(float[][] a, float[][] b) {
    int n = a.length;
    for (int i=0; i<n; ++i)
      sqr(a[i],b[i]);
  }
  private static void sqrS(float[][][] a, float[][][] b) {
    int n = a.length;
    for (int i=0; i<n; ++i)
      sqrS(a[i],b[i]);
  }
  private static void sqrP(final float[][] a, final float[][] b) {
    int n = a.length;
    int chunk = max(1,10000/a[0].length);
    loop(0,n,1,chunk,new LoopInt() {
      public void compute(int i) {
        sqr(a[i],b[i]);
      }
    });
  }
  private static void sqrP(final float[][][] a, final float[][][] b) {
    int n = a.length;
    loop(n,new LoopInt() {
      public void compute(int i) {
        sqrP(a[i],b[i]); // nested parallelism
      }
    });
  }

  // Sum of elements of 2D and 3D arrays
  private static void benchArraySum() {
    int n1 = 501;
    int n2 = 502;
    int n3 = 503;
    System.out.println("Array sum: n1="+n1+" n2="+n2+" n3="+n3);
    int niter,rate;
    double maxtime = 5.0;
    double mflop2 = 1.0e-6*n1*n2;
    double mflop3 = 1.0e-6*n1*n2*n3;
    Stopwatch sw = new Stopwatch();
    float[][][] a = sub(randfloat(n1,n2,n3),0.5f);
    for (int ntest=0; ntest<3; ++ntest) {
      float ss = 0.0f;
      float sp = 0.0f;
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        ss = sumS(a[0]);
      sw.stop();
      rate = (int)((niter*mflop2)/sw.time());
      System.out.println("2D S: rate = "+rate);
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        sp = sumP(a[0]);
      sw.stop();
      rate = (int)((niter*mflop2)/sw.time());
      System.out.println("2D P: rate = "+rate);
      //System.out.println("    : sum = "+ss+" err = "+abs(sp-ss));
      ss = 0.0f;
      sp = 0.0f;
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        ss = sumS(a);
      sw.stop();
      rate = (int)((niter*mflop3)/sw.time());
      System.out.println("3D S: rate = "+rate);
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        sp = sumP(a);
      sw.stop();
      rate = (int)((niter*mflop3)/sw.time());
      System.out.println("3D P: rate = "+rate);
      //System.out.println("    : sum = "+ss+" err = "+abs(sp-ss));
    }
  }
  private static float sum(float[] a) {
    int n = a.length;
    float s = 0.0f;
    for (int i=0; i<n; ++i)
      s += a[i];
    return s;
  }
  private static float sumS(float[][] a) {
    int n = a.length;
    float s = 0.0f;
    for (int i=0; i<n; ++i)
      s += sum(a[i]);
    return s;
  }
  private static float sumS(float[][][] a) {
    int n = a.length;
    float s = 0.0f;
    for (int i=0; i<n; ++i)
      s += sumS(a[i]);
    return s;
  }
  private static float sumP(final float[][] a) {
    int n = a.length;
    int chunk = max(1,10000/a[0].length);
    return reduce(0,n,1,chunk,new ReduceInt<Float>() {
      public Float compute(int i) {
        return sum(a[i]);
      }
      public Float combine(Float s1, Float s2) {
        return s1+s2;
      }
    });
  }
  private static float sumP(final float[][][] a) {
    int n = a.length;
    return reduce(n,new ReduceInt<Float>() {
      public Float compute(int i) {
        return sumP(a[i]); // nested parallelism
      }
      public Float combine(Float s1, Float s2) {
        return s1+s2;
      }
    });
  }

  // Matrix multiply
  private static void benchMatrixMultiply() {
    int m = 1001;
    int n = 1002;
    System.out.println("Matrix multiply for m="+m+" n="+n);
    float[][] a = randfloat(n,m);
    float[][] b = randfloat(m,n);
    float[][] cs = zerofloat(m,m);
    float[][] cp = zerofloat(m,m);
    int niter,rate;
    double maxtime = 5.0;
    double mflop = 2.0e-6*m*m*n;
    Stopwatch sw = new Stopwatch();
    for (int ntest=0; ntest<3; ++ntest) {
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter) {
        matrixMultiplySerial(a,b,cs);
      }
      sw.stop();
      rate = (int)((niter*mflop)/sw.time());
      System.out.println("S: rate = "+rate+" mflops");
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter) {
        matrixMultiplyParallel(a,b,cp);
      }
      sw.stop();
      rate = (int)((niter*mflop)/sw.time());
      System.out.println("P: rate = "+rate+" mflops");
    }
  }
  private static void matrixMultiplySerial(
    float[][] a, 
    float[][] b, 
    float[][] c) 
  {
    int nj = c[0].length;
    for (int j=0; j<nj; ++j)
      computeColumn(j,a,b,c);
  }
  private static void matrixMultiplyParallel(
    final float[][] a, 
    final float[][] b, 
    final float[][] c) 
  {
    int nj = c[0].length;
    loop(nj,new LoopInt() {
      public void compute(int j) {
        computeColumn(j,a,b,c);
      }
    });
  }
  private static void computeColumn(
    final int j, final float[][] a, final float[][] b, final float[][] c) 
  {
    final int ni = c.length;
    final int nk = b.length;
    final float[] bj = new float[nk];
    for (int k=0; k<nk; ++k)
      bj[k] = b[k][j];
    for (int i=0; i<ni; ++i) {
      float[] ai = a[i];
      float cij = 0.0f;
      int mk = nk%4;
      for (int k=0; k<mk; ++k)
        cij += ai[k]*bj[k];
      for (int k=mk; k<nk; k+=4) {
        cij += ai[k  ]*bj[k  ];
        cij += ai[k+1]*bj[k+1];
        cij += ai[k+2]*bj[k+2];
        cij += ai[k+3]*bj[k+3];
      }
      c[i][j] = cij;
    }
  }

  private static float emax(float[] a, float[] b) {
    int n = a.length;
    float emax = 0.0f;
    for (int i=0; i<n; ++i)
      emax = max(emax,abs(b[i]-a[i]));
    return emax;
  }
  private static float emax(float[][] a, float[][] b) {
    int n = a.length;
    float emax = 0.0f;
    for (int i=0; i<n; ++i)
      emax = emax(a[i],b[i]);
    return emax;
  }
  private static float emax(float[][][] a, float[][][] b) {
    int n = a.length;
    float emax = 0.0f;
    for (int i=0; i<n; ++i)
      emax = emax(a[i],b[i]);
    return emax;
  }

  private static void bench(boolean parallel) {
    Parallel.setParallel(parallel);
    benchArraySqr();
    benchArraySum();
    benchMatrixMultiply();
  }
}

/*
2010.11.26
banjer: 2 x 2.66 GHz Intel Core i7, 8 GB 1067 MHz DDR3 RAM
banjer: j edu.mines.jtk.util.ParallelTest bench
Array sqr: n1=501 n2=502 n3=503
2D S: rate = 1663
2D P: rate = 1709
3D S: rate = 561
3D P: rate = 777
2D S: rate = 1669
2D P: rate = 1752
3D S: rate = 563
3D P: rate = 776
2D S: rate = 1602
2D P: rate = 1531
3D S: rate = 549
3D P: rate = 773
Array sum: n1=501 n2=502 n3=503
2D S: rate = 1095
2D P: rate = 1737
3D S: rate = 712
3D P: rate = 1487
2D S: rate = 1086
2D P: rate = 1831
3D S: rate = 725
3D P: rate = 1459
2D S: rate = 1089
2D P: rate = 1832
3D S: rate = 739
3D P: rate = 1528
Matrix multiply for m=1001 n=1002
S: rate = 1706 mflops
P: rate = 4622 mflops
S: rate = 1704 mflops
P: rate = 4647 mflops
S: rate = 1680 mflops
P: rate = 4735 mflops

2010.11.26
brooks: j edu.mines.jtk.util.ParallelTest bench
Array sqr: n1=501 n2=502 n3=503
2D S: rate = 1700
2D P: rate = 1560
3D S: rate = 722
3D P: rate = 2558
2D S: rate = 1700
2D P: rate = 1606
3D S: rate = 710
3D P: rate = 2395
2D S: rate = 1681
2D P: rate = 1606
3D S: rate = 711
3D P: rate = 2582
Array sum: n1=501 n2=502 n3=503
2D S: rate = 1120
2D P: rate = 1450
3D S: rate = 822
3D P: rate = 5593
2D S: rate = 1115
2D P: rate = 1758
3D S: rate = 691
3D P: rate = 5956
2D S: rate = 1115
2D P: rate = 1723
3D S: rate = 696
3D P: rate = 6052
Matrix multiply for m=1001 n=1002
S: rate = 2151 mflops
P: rate = 31102 mflops
S: rate = 2058 mflops
P: rate = 31003 mflops
S: rate = 2124 mflops
P: rate = 30858 mflops
*/
