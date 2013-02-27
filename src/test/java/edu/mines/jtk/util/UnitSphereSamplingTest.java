/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import static java.lang.Math.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.util.UnitSphereSampling}.
 * @author Dave Hale, Colorado School of Mines
 * @version 08/27/2008.
 */
public class UnitSphereSamplingTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(UnitSphereSamplingTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testSymmetry() {
    testSymmetry(8);
    testSymmetry(16);
  }

  public void testInterpolation() {
    testInterpolation(16,0.0001f);
  }
  
  public void testTriangle() {
    testTriangle(8);
    testTriangle(16);
  }

  public void testTriangle1() {
    // This used to fail due to rounding errors when computing r and s.
    float[] p = {-0.82179755f,0.56977963f,0.0f};
    UnitSphereSampling uss = new UnitSphereSampling(16);
    int i = uss.getIndex(p);
    int[] abc = uss.getTriangle(p);
    int ia = abc[0], ib = abc[1], ic = abc[2];
    assertTrue(i==ia || i==ib || i==ic);
  }
  
  public void testWeights() {
    testWeights(8);
    testWeights(16);
  }
  
  public void testMaxError() {
    testMaxError(16,1.0f);
  }

  private static void testSymmetry(int nbits) {
    UnitSphereSampling uss = new UnitSphereSampling(nbits);
    int mi = uss.getMaxIndex();
    for (int i=1,j=-i; i<=mi; ++i,j=-i) {
      float[] p = uss.getPoint(i);
      float[] q = uss.getPoint(j);
      assertEquals(p[0],-q[0],0.0);
      assertEquals(p[1],-q[1],0.0);
      assertEquals(p[2],-q[2],0.0);
    }
    int npoint = 10000;
    for (int ipoint=0; ipoint<npoint; ++ipoint) {
      float[] p = randomPoint();
      float[] q = {-p[0],-p[1],-p[2]};
      int i = uss.getIndex(p);
      int j = uss.getIndex(q);
      if (p[2]==0.0f) {
        assertEquals(i+j,mi+1);
      } else {
        assertEquals(-i,j);
      }
    }
  }

  private static void testInterpolation(int nbits, float error) {
    UnitSphereSampling uss = new UnitSphereSampling(nbits);
    int mi = uss.getMaxIndex();

    // Tabulate function values for sampled points.
    int nf = 1+2*mi;
    float[] fi = new float[nf];
    for (int i=1; i<=mi; ++i) {
      float[] p = uss.getPoint( i);
      float[] q = uss.getPoint(-i);
      fi[i   ] = func(p[0],p[1],p[2]);
      fi[nf-i] = func(q[0],q[1],q[2]);
    }

    // Interpolate and track errors.
    float emax = 0.0f;
    int npoint = 10000;
    for (int ipoint=0; ipoint<npoint; ++ipoint) {
      float[] p = randomPoint();
      int[] iabc = uss.getTriangle(p);
      float[] wabc = uss.getWeights(p,iabc);
      int   ia = iabc[0], ib = iabc[1], ic = iabc[2];
      float wa = wabc[0], wb = wabc[1], wc = wabc[2];
      if (ia<0) {
        ia = nf+ia;
        ib = nf+ib;
        ic = nf+ic;
      }
      float fa = fi[ia], fb = fi[ib], fc = fi[ic];
      float f = func(p[0],p[1],p[2]);
      float g = wa*fa+wb*fb+wc*fc;
      float e = abs(g-f);
      if (e>emax) {
        emax = e;
      }
    }
    assertTrue(emax<error);
  }
  private static float func(float x, float y, float z) {
    return 0.1f*(9.0f*x*x*x-2.0f*x*x*y+3.0f*x*y*y-4.0f*y*y*y+2.0f*z*z*z-x*y*z);
  }

  private static void testTriangle(int nbits) {
    UnitSphereSampling uss = new UnitSphereSampling(nbits);
    int npoint = 100000;
    for (int ipoint=0; ipoint<npoint; ++ipoint) {
      float[] p = randomPoint();
      int i = uss.getIndex(p);
      int[] abc = uss.getTriangle(p);
      int ia = abc[0], ib = abc[1], ic = abc[2];
      /*
      float[] q = uss.getPoint(i);
      float[] qa = uss.getPoint(ia);
      float[] qb = uss.getPoint(ib);
      float[] qc = uss.getPoint(ic);
      float d = distanceOnSphere(p,q);
      float da = distanceOnSphere(p,qa);
      float db = distanceOnSphere(p,qb);
      float dc = distanceOnSphere(p,qc);
       */
      assertTrue(i==ia || i==ib || i==ic);
    }
  }

  private static void testWeights(int nbits) {
    UnitSphereSampling uss = new UnitSphereSampling(nbits);
    int npoint = 10;
    for (int ipoint=0; ipoint<npoint; ++ipoint) {
      float[] p = randomPoint();
      int[] iabc = uss.getTriangle(p);
      /*
      int ia = iabc[0], ib = iabc[1], ic = iabc[2];
      int i = uss.getIndex(p);
      float[] q = uss.getPoint(i);
      float[] qa = uss.getPoint(ia);
      float[] qb = uss.getPoint(ib);
      float[] qc = uss.getPoint(ic);
      */
      float[] wabc = uss.getWeights(p,iabc);
      float wa = wabc[0], wb = wabc[1], wc = wabc[2];
      assertEquals(1.0,wa+wb+wc,0.00001);
      // TODO: more assertions?
      /*
      trace("wa="+wa+" wb="+wb+" wc="+wc);
      ArrayMath.dump(p);
      ArrayMath.dump(qa);
      ArrayMath.dump(qb);
      ArrayMath.dump(qc);
      */
    }
  }

  private static void testMaxError(int nbits, float error) {
    UnitSphereSampling uss = new UnitSphereSampling(nbits);
    int npoint = 100000;
    float dmax = 0.0f;
    //float[] pmax = null;
    //float[] qmax = null;
    for (int ipoint=0; ipoint<npoint; ++ipoint) {
      float[] p = randomPoint();
      int i = uss.getIndex(p);
      float[] q = uss.getPoint(i);
      float d = distanceOnSphere(p,q);
      if (d>dmax) {
        dmax = d;
        //pmax = p;
        //qmax = q;
      }
    }
    /*
    float dmaxDegrees = (float)(dmax*180.0/PI);
    trace("npoint="+npoint+" dmax="+dmax+" degrees="+dmaxDegrees);
    trace("pmax=");
    ArrayMath.dump(pmax);
    trace("qmax=");
    ArrayMath.dump(qmax);
    */
    assertTrue(dmax<error);
  }

  private static java.util.Random _random = new java.util.Random();
  private static float[] randomPoint() {
    float x = -1.0f+2.0f*_random.nextFloat();
    float y = -1.0f+2.0f*_random.nextFloat();
    float z = -1.0f+2.0f*_random.nextFloat();
    float f = _random.nextFloat();
    if (f<0.1f)            x = 0.0f;
    if (0.1f<=f && f<0.2f) y = 0.0f;
    if (0.2f<=f && f<0.3f) z = 0.0f;
    float s = 1.0f/(float)sqrt(x*x+y*y+z*z);
    return new float[]{x*s,y*s,z*s};
  }

  private static float distanceOnSphere(float[] p, float[] q) {
    double x = p[0]+q[0];
    double y = p[1]+q[1];
    double z = p[2]+q[2];
    double d = x*x+y*y+z*z;
    if (d==0.0) {
      d = PI;
    } else if (d==4.0) {
      d = 0.0;
    } else {
      d = 2.0*atan(sqrt((4.0-d)/d));
    }
    return (float)d;
    //return (float)acos(p[0]*q[0]+p[1]*q[1]+p[2]*q[2]);
  }

  /*
  private static final boolean TRACE = true;
  private static void trace(String s) {
    if (TRACE)
      System.out.println(s);
  }
  */
}
