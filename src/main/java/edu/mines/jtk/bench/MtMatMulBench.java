/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import static java.lang.Math.min;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Benchmark single- and multi-threaded matrix multiplication.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.02
 */
public class MtMatMulBench {

  public static final int NTHREAD = 
    Runtime.getRuntime().availableProcessors();

  public static void main(String[] args) {
    int m = 1001;
    int n = 1002;
    float[][] a = randfloat(n,m);
    float[][] b = randfloat(m,n);
    float[][] c1 = zerofloat(m,m);
    float[][] c2 = zerofloat(m,m);
    float[][] c3 = zerofloat(m,m);
    float[][] c4 = zerofloat(m,m);
    float[][] c5 = zerofloat(m,m);
    Stopwatch s = new Stopwatch();
    double mflops = 2.0e-6*m*m*n;
    double maxtime = 5.0;

    System.out.println("Matrix multiply benchmark");
    System.out.println("m="+m+" n="+n+" nthread="+NTHREAD);
    System.out.println("Methods:");
    System.out.println("mul1 = single-threaded");
    System.out.println("mul2 = multi-threaded (equal chunks)");
    System.out.println("mul3 = multi-threaded (atomic-integer)");
    System.out.println("mul4 = multi-threaded (thread-pool)");
    System.out.println("mul5 = multi-threaded (fork-join)");

    for (int ntrial=0; ntrial<3; ++ntrial) {
      System.out.println();
      int nmul;

      s.restart();
      for (nmul=0; s.time()<maxtime; ++nmul)
        mul1(a,b,c1);
      s.stop();
      System.out.println("mul1: rate="+(int)(nmul*mflops/s.time())+" mflops");

      s.restart();
      for (nmul=0; s.time()<maxtime; ++nmul)
        mul2(a,b,c2);
      s.stop();
      System.out.println("mul2: rate="+(int)(nmul*mflops/s.time())+" mflops");

      s.restart();
      for (nmul=0; s.time()<maxtime; ++nmul)
        mul3(a,b,c3);
      s.stop();
      System.out.println("mul3: rate="+(int)(nmul*mflops/s.time())+" mflops");

      s.restart();
      for (nmul=0; s.time()<maxtime; ++nmul)
        mul4(a,b,c4);
      s.stop();
      System.out.println("mul4: rate="+(int)(nmul*mflops/s.time())+" mflops");

      s.restart();
      for (nmul=0; s.time()<maxtime; ++nmul)
        mul5(a,b,c5);
      s.stop();
      System.out.println("mul5: rate="+(int)(nmul*mflops/s.time())+" mflops");

      assertEquals(c1,c2);
      assertEquals(c1,c3);
      assertEquals(c1,c4);
      assertEquals(c1,c5);
    }
  }

  /**
   * Computes j'th column of matrix multiplication C = A*B.
   * The work array bj is used to cache the j'th column of the matrix b.
   * This version allocates a new work array bj internally for every call.
   * Loop unrolling improves performance.
   */
  private static void computeColumn(
    int j, float[][] a, float[][] b, float[][] c) 
  {
    int ni = c.length;
    int nk = b.length;
    float[] bj = new float[nk];
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

  /**
   * Single-threaded method.
   */
  private static void mul1(float[][] a, float[][] b, float[][] c) {
    checkDimensions(a,b,c);
    int nj = c[0].length;
    for (int j=0; j<nj; ++j)
      computeColumn(j,a,b,c);
  }

  /**
   * Multi-threaded equal-chunks version.
   */
  private static void mul2(
    final float[][] a, final float[][] b, final float[][] c) 
  {
    checkDimensions(a,b,c);
    int nj = c[0].length;
    int mj = 1+nj/NTHREAD;
    Thread[] threads = new Thread[NTHREAD];
    for (int ithread=0; ithread<NTHREAD; ++ithread) {
      final int jfirst = ithread*mj;
      final int jlast = min(jfirst+mj,nj);
      threads[ithread] = new Thread(new Runnable() {
        public void run() {
          for (int j=jfirst; j<jlast; ++j)
            computeColumn(j,a,b,c);
        }
      });
    }
    startAndJoin(threads);
  }

  /**
   * Multi-threaded atomic-integer version.
   */
  private static void mul3(
    final float[][] a, final float[][] b, final float[][] c) 
  {
    checkDimensions(a,b,c);
    final int nj = c[0].length;
    final AtomicInteger aj = new AtomicInteger();
    Thread[] threads = new Thread[NTHREAD];
    for (int ithread=0; ithread<threads.length; ++ithread) {
      threads[ithread] = new Thread(new Runnable() {
        public void run() {
          for (int j=aj.getAndIncrement(); j<nj; j=aj.getAndIncrement())
            computeColumn(j,a,b,c);
        }
      });
    }
    startAndJoin(threads);
  }

  /**
   * Multi-threaded thread-pool version.
   */
  private static void mul4(
    final float[][] a, final float[][] b, final float[][] c) 
  {
    checkDimensions(a,b,c);
    final int nj = c[0].length;
    ExecutorService es = Executors.newFixedThreadPool(NTHREAD);
    CompletionService<Void> cs = new ExecutorCompletionService<Void>(es);
    for (int j=0; j<nj; ++j) {
      final int jj = j;
      cs.submit(new Runnable() {
        public void run() {
          computeColumn(jj,a,b,c);
        }
      },null);
    }
    try {
      for (int j=0; j<nj; ++j)
        cs.take();
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
    es.shutdown();
  }

  /**
   * Multi-threaded fork-join version.
   */
  private static void mul5(
    final float[][] a, final float[][] b, final float[][] c) 
  {
    checkDimensions(a,b,c);
    final int nj = c[0].length;
    Parallel.loop(nj,new Parallel.LoopInt() {
      public void compute(int j) {
        computeColumn(j,a,b,c);
      }
    });
  }

  /**
   * Single-threaded blocked version. Currently unused.
   */
  /*
  private static void mulb(float[][] a, float[][] b, float[][] c) {
    checkDimensions(a,b,c);
    int ni = c.length;
    int nj = c[0].length;
    int nk = b.length;
    int mi = 16;
    int mj = 16;
    int mk = 16;
    float[] bjj = new float[nk];
    for (int i=0; i<ni; i+=mi) {
      int nii = min(i+mi,ni);
      for (int j=0; j<nj; j+=mj) {
        int njj = min(j+mj,nj);
        for (int ii=i; ii<nii; ++ii)
          for (int jj=j; jj<njj; ++jj)
            c[ii][jj] = 0.0f;
        for (int k=0; k<nk; k+=mk) {
          int nkk = min(k+mk,nk);
          for (int jj=j; jj<njj; ++jj) {
            for (int kk=k; kk<nkk; ++kk)
              bjj[kk] = b[kk][jj];
            for (int ii=i; ii<nii; ++ii) {
              float[] aii = a[ii];
              float cij = c[ii][jj];
              for (int kk=k; kk<nkk; ++kk)
                cij += aii[kk]*bjj[kk];
              c[ii][jj] = cij;
            }
          }
        }
      }
    }
  }
  */

  private static void startAndJoin(Thread[] threads) {
    for (Thread thread:threads)
      thread.start();
    try {
      for (Thread thread:threads)
        thread.join();
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
  }

  private static void assertEquals(float[][] a, float[][] b) {
    Check.state(a.length==b.length,"same dimensions");
    Check.state(a[0].length==b[0].length,"same dimensions");
    int m = a[0].length;
    int n = a.length;
    for (int i=0; i<m; ++i) {
      for (int j=0; j<n; ++j) {
        Check.state(a[i][j]==b[i][j],"same elements");
      }
    }
  }

  private static void checkDimensions(float[][] a, float[][] b, float[][] c) {
    int ma = a.length;
    int na = a[0].length;
    int mb = b.length;
    int nb = b[0].length;
    int mc = c.length;
    int nc = c[0].length;
    Check.argument(na==mb,"number of columns in A = number of rows in B");
    Check.argument(ma==mc,"number of rows in A = number of rows in C");
    Check.argument(nb==nc,"number of columns in B = number of columns in C");
  }
}
