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
 * Tests {@link edu.mines.jtk.dsp.LocalDiffusionKernel}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.11.25
 */
public class LocalDiffusionKernelTest extends TestCase {
  public static void main(String[] args) {
    if (args.length>0 && args[0].equals("bench"))
      bench();
    TestSuite suite = new TestSuite(LocalDiffusionKernelTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test() {
    // TODO: implement tests
  }

  ///////////////////////////////////////////////////////////////////////////
  // benchmark

  private static class IdentityTensors3 implements Tensors3 {
    public void getTensor(int i1, int i2, int i3, float[] a) {
      a[0] = 1.0f;
      a[1] = 0.0f;
      a[2] = 0.0f;
      a[3] = 1.0f;
      a[4] = 0.0f;
      a[5] = 1.0f;
    }
  }

  private static void bench() {
    bench3();
  }

  private static void bench3() {
    int n1 = 501;
    int n2 = 502;
    int n3 = 503;
    Random r = new Random(314159);
    float[][][] x = randfloat(r,n1,n2,n3);
    float[][][] y = randfloat(r,n1,n2,n3);
    float[][][] s = randfloat(r,n1,n2,n3);
    IdentityTensors3 d = new IdentityTensors3();
    LocalDiffusionKernel ldf = new LocalDiffusionKernel();
    int niter;
    double maxtime = 5.0;
    double nsample = (double)n1*(double)n2*(double)n3;
    Stopwatch sw = new Stopwatch();
    for (int itest=0; itest<3; ++itest) {
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        ldf.apply(d,0.5f,s,x,y);
      sw.stop();
      float sum = sum(y);
      double rate = 1.0e-6*niter*nsample/sw.time();
      System.out.println("rate = "+rate+"  sum = "+sum);
    }
  }
}
