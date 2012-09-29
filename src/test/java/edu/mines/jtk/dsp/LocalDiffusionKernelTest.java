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
    if (args.length>0 && args[0].equals("bench")) {
      boolean parallel = (args.length>1 && args[1].equals("serial")) ?
        false : true;
      bench(parallel);
    }
    TestSuite suite = new TestSuite(LocalDiffusionKernelTest.class);
    junit.textui.TestRunner.run(suite);
  }

  ///////////////////////////////////////////////////////////////////////////
  // test

  public void testD21() {
    LocalDiffusionKernel ldk = 
      new LocalDiffusionKernel(LocalDiffusionKernel.Stencil.D21);
    testSpd2(ldk);
    testSpd3(ldk);
    /*
    float[][] s = fillfloat(1.0f,5,5);
    float[][] x = zerofloat(5,5);
    float[][] y = zerofloat(5,5);
    //s[1][1] = 2.0f; s[1][2] = 2.0f; s[1][3] = 2.0f;
    //s[2][1] = 2.0f; s[2][2] = 2.0f; s[2][3] = 2.0f;
    //s[3][1] = 2.0f; s[3][2] = 2.0f; s[3][3] = 2.0f;
    s[2][2] = 2.0f;
    x[0][0] = 1.0f;
    x[0][4] = 1.0f;
    x[4][0] = 1.0f;
    x[4][4] = 1.0f;
    x[2][2] = 1.0f;
    ldk.apply(1.0f,s,x,y);
    dump(x);
    dump(y);
    */
  }
  public void testD22() {
    LocalDiffusionKernel ldk = 
      new LocalDiffusionKernel(LocalDiffusionKernel.Stencil.D22);
    testSpd2(ldk);
    testSpd3(ldk);
    testSpd2RandomTensors(ldk);
  }

  private static void testSpd2(LocalDiffusionKernel ldk) {
    int n1 = 5;
    int n2 = 6;
    for (int iter=0; iter<10; ++iter) {
      float[][] s = randfloat(n1,n2);
      float[][] x = sub(randfloat(n1,n2),0.5f);
      float[][] y = sub(randfloat(n1,n2),0.5f);
      float[][] dx = zerofloat(n1,n2);
      float[][] dy = zerofloat(n1,n2);
      ldk.apply(1.0f,s,x,dx);
      ldk.apply(1.0f,s,y,dy);
      float xdx = dot(x,dx);
      float ydy = dot(y,dy);
      float ydx = dot(y,dx);
      float xdy = dot(x,dy);
      assertTrue(xdx>=0.0f);
      assertTrue(ydy>=0.0f);
      assertEquals(xdy,ydx,0.0001);
    }
  }
  private static void testSpd2RandomTensors(LocalDiffusionKernel ldk) {
    int n1 = 5;
    int n2 = 6;
    for (int iter=0; iter<10; ++iter) {
      float[][] s = randfloat(n1,n2);
      float[][] x = sub(randfloat(n1,n2),0.5f);
      float[][] y = sub(randfloat(n1,n2),0.5f);
      float[][] dx = zerofloat(n1,n2);
      float[][] dy = zerofloat(n1,n2);
      Tensors2 t = new RandomTensors2(n1,n2);
      ldk.apply(t,1.0f,s,x,dx);
      ldk.apply(t,1.0f,s,y,dy);
      float xdx = dot(x,dx);
      float ydy = dot(y,dy);
      float ydx = dot(y,dx);
      float xdy = dot(x,dy);
      assertTrue(xdx>=0.0f);
      assertTrue(ydy>=0.0f);
      assertEquals(xdy,ydx,0.0001);
    }
  }
  private static void testSpd3(LocalDiffusionKernel ldk) {
    int n1 = 5;
    int n2 = 6;
    int n3 = 7;
    for (int iter=0; iter<10; ++iter) {
      float[][][] s = randfloat(n1,n2,n3);
      float[][][] x = sub(randfloat(n1,n2,n3),0.5f);
      float[][][] y = sub(randfloat(n1,n2,n3),0.5f);
      float[][][] dx = zerofloat(n1,n2,n3);
      float[][][] dy = zerofloat(n1,n2,n3);
      ldk.apply(null,1.0f,s,x,dx);
      ldk.apply(null,1.0f,s,y,dy);
      float xdx = dot(x,dx);
      float ydy = dot(y,dy);
      float ydx = dot(y,dx);
      float xdy = dot(x,dy);
      assertTrue(xdx>=0.0f);
      assertTrue(ydy>=0.0f);
      assertEquals(xdy,ydx,0.0001);
    }
  }
  private static float dot(float[][] x, float[][] y) {
    return sum(mul(x,y));
  }
  private static float dot(float[][][] x, float[][][] y) {
    return sum(mul(x,y));
  }

  private static class RandomTensors2 extends EigenTensors2 {
    RandomTensors2(int n1, int n2) {
      super(n1,n2);
      Random r = new Random();
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float a = 2.0f*FLT_PI*r.nextFloat();
          float u1 = cos(a);
          float u2 = sin(a);
          float du = 0.01f+0.09f*r.nextFloat();
          float dv = 0.01f+0.99f*r.nextFloat();
          setEigenvectorU(i1,i2,u1,u2);
          setEigenvalues(i1,i2,du,dv);
        }
      }
    }
  }

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
      int rate = (int)(1.0e-6*niter*nsample/sw.time());
      System.out.println("rate = "+rate+"  sum = "+sum);
    }
  }
}
