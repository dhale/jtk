/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import java.util.ArrayList;

import edu.mines.jtk.util.Stopwatch;

/**
 * Benchmark ArrayList of primitives.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.02.17
 */
public class ArrayListBench {

  private static final int INITIAL_CAPACITY = 8;

  public static class FloatList {
    public int n;
    public float[] a = new float[INITIAL_CAPACITY];
    public void add(float f) {
      if (n==a.length) {
        float[] t = new float[2*a.length];
        System.arraycopy(a,0,t,0,n);
        a = t;
      }
      a[n++] = f;
    }
    public float[] trim() {
      float[] t = new float[n];
      System.arraycopy(a,0,t,0,n);
      return t;
    }
  }

  public static void main(String[] args) {
    double maxtime = 2;
    int n = 10000;
    double rate;
    for (int niter=0; niter<5; ++niter) {
      rate = benchArrayList(maxtime,n);
      System.out.println("ArrayList<Float> rate="+rate);
      rate = benchFloatList(maxtime,n);
      System.out.println("FloatList        rate="+rate);
    }
  }

  interface ListMaker {
    public void makeList(int n);
  }

  static double benchList(double maxtime, int n, ListMaker lm) {
    Stopwatch sw = new Stopwatch();
    sw.start();
    int niter;
    for (niter=0; sw.time()<maxtime; ++niter)
      lm.makeList(n);
    sw.stop();
    return (double)n*(double)niter/sw.time()*1.0e-6;
  }

  static double benchArrayList(double maxtime, int n) {
    return benchList(maxtime,n,new ListMaker() {
      public void makeList(int n) {
        ArrayList<Float> list = new ArrayList<Float>(INITIAL_CAPACITY);
        float f = 0.0f;
        for (int i=0; i<n; ++i) {
          list.add(f);
          f += 1.0f;
        }
      }
    });
  }

  static double benchFloatList(double maxtime, int n) {
    return benchList(maxtime,n,new ListMaker() {
      public void makeList(int n) {
        FloatList list = new FloatList();
        float f = 0.0f;
        for (int i=0; i<n; ++i) {
          list.add(f);
          f += 1.0f;
        }
      }
    });
  }
}
