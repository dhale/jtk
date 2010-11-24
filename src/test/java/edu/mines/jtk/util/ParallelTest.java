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
    if (args.length>0 && args[0].equals("bench"))
      bench();
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

  public static void bench() {
  }
}
