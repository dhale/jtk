/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.dsp.Eigen;
import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Stopwatch;

/**
 * Tests {@link edu.mines.jtk.dsp.Eigen}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.31
 */
public class EigenTest extends TestCase {
  public static void main(String[] args) {
    if (args.length>0 && args[0].equals("bench")) {
      benchSymmetric33();
    } else {
      TestSuite suite = new TestSuite(EigenTest.class);
      junit.textui.TestRunner.run(suite);
    }
  }

  public void testSymmetric22() {
    int nrand = 100;
    for (int irand=0; irand<nrand; ++irand) {
      float[][] a = Array.randfloat(2,2);
      a = Array.add(a,Array.transpose(a));
      float[][] v = new float[2][2];
      float[] d = new float[2];
      Eigen.solveSymmetric22(a,v,d);
      check(a,v,d);
    }
  }

  public void testSymmetric33() {
    float[][] v = new float[3][3];
    float[] d = new float[3];
    int nrand = 100;
    for (int irand=0; irand<nrand; ++irand) {
      float[][] a = Array.randfloat(3,3);
      a = Array.add(a,Array.transpose(a));
      Eigen.solveSymmetric33(a,v,d);
      check(a,v,d);
    }
  }

  public void testSymmetric33Special() {
    float[][] v = new float[3][3];
    float[] d = new float[3];
    float[][][] as = {A100,A110,A111};
    for (float[][] a:as) {
      Eigen.solveSymmetric33(a,v,d);
      check(a,v,d);
    }
  }

  private void check(float[][] a, float[][] v, float[] d) {
    int n = a.length;
    for (int k=0; k<n; ++k) {
      assertTrue(k==0 || d[k-1]>=d[k]);
      for (int i=0; i<n; ++i) {
        float av = 0.0f;
        for (int j=0; j<n; ++j) {
          av += a[i][j]*v[k][j];
        }
        float vd = v[k][i]*d[k];
        assertEquals(av,vd,0.001);
      }
    }
  }

  private static final float[][] A100 = {
    {1.0f,0.0f,0.0f},
    {0.0f,0.0f,0.0f},
    {0.0f,0.0f,0.0f}
  };
  private static final float[][] A110 = {
    {1.0f,0.0f,0.0f},
    {0.0f,1.0f,0.0f},
    {0.0f,0.0f,0.0f}
  };
  private static final float[][] A111 = {
    {1.0f,0.0f,0.0f},
    {0.0f,1.0f,0.0f},
    {0.0f,0.0f,1.0f}
  };
  private static final float[][] AA = {
    {-1.08876e-13f,  1.87872e-17f,  1.29275e-16f},
    { 1.87872e-17f, -7.65274e-15f, -1.13984e-14f},
    { 1.29275e-16f, -1.13984e-14f, -2.53222e-14f}
  };

  private static void benchSymmetric33() {
    int nrand = 1000000;
    float[][][] a = new float[nrand][][];
    for (int irand=0; irand<nrand; ++irand) {
      a[irand] = Array.randfloat(3,3);
      a[irand] = Array.add(a[irand],Array.transpose(a[irand]));
    }
    float[][] v = new float[3][3];
    float[] d = new float[3];
    Stopwatch s = new Stopwatch();
    int nloop,rate;
    double maxtime = 2.0;
    s.reset();
    s.start();
    for (nloop=0; s.time()<maxtime; ++nloop) {
      for (int irand=0; irand<nrand; ++irand) {
        Eigen.solveSymmetric33(a[irand],v,d);
      }
    }
    s.stop();
    rate = (int)((double)nloop*(double)nrand/s.time());
    System.out.println("Number of 3x3 eigen-decompositions per second");
    System.out.println("new: rate="+rate);
    s.reset();
    s.start();
    for (nloop=0; s.time()<maxtime; ++nloop) {
      for (int irand=0; irand<nrand; ++irand) {
        Eigen.solveSymmetric33Jacobi(a[irand],v,d);
      }
    }
    s.stop();
    rate = (int)((double)nloop*(double)nrand/s.time());
    System.out.println("old: rate="+rate);
  }
}
