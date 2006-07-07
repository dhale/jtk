package com.lgc.idh.util.test;

import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.lgc.idh.lang.M;
import com.lgc.idh.util.CubicInterpolator;

/**
 * Tests {@link com.lgc.idh.util.CubicInterpolator}.
 * @author Dave Hale
 * @version 2000.02.21
 */
public class CubicInterpolatorTest extends TestCase {

  public void testMonotonic() {
    float[] x = {1.0f, 2.0f, 3.0f, 4.0f};
    float[] y = {0.0f, 0.0f, 1.0f, 1.0f};
    int n = x.length;
    
    int linear = CubicInterpolator.LINEAR;
    int monoto = CubicInterpolator.MONOTONIC;
    int spline = CubicInterpolator.SPLINE;

    CubicInterpolator cl = new CubicInterpolator(linear,n,x,y);
    CubicInterpolator cm = new CubicInterpolator(monoto,n,x,y);
    CubicInterpolator cs = new CubicInterpolator(spline,n,x,y);

    int nx = 101;
    float fx = x[0];
    float dx = (x[n-1]-fx)/(float)(nx-1);
    float ylLast = 0.0f;
    float ymLast = 0.0f;
    float ysLast = 0.0f;
    boolean splineNotMonotonic = false;
    for (int ix=0; ix<nx; ++ix) {
      float xi = fx+(float)ix*dx;
      float yl = cl.interpolate(xi);
      assertTrue(yl>=ylLast);
      ylLast = yl;
      float ym = cm.interpolate(xi);
      assertTrue(ym>=ymLast);
      ymLast = ym;
      float ys = cs.interpolate(xi);
      if (ys<ysLast) splineNotMonotonic = true;
      ysLast = ys;
    }
    assertTrue(splineNotMonotonic);
  }

  public void testSpline() {
    int nknot = 11;
    float dknot = 1.0f/(nknot-1);
    float fknot = 0.0f;

    float[] xknot = new float[nknot];
    float[] yknot = new float[nknot];
    for (int i=0; i<nknot; ++i) {
      float x = fknot+(float)i*dknot;
      float y = 1.0f+x+x*x;
      xknot[i] = x;
      yknot[i] = y;
    }
    
    int spline = CubicInterpolator.SPLINE;

    CubicInterpolator cs = new CubicInterpolator(spline,nknot,xknot,yknot);

    int nx = 1001;
    float xmin = -0.2f;
    float xmax = 1.2f;

    Random r = new Random();
    for (int i=0; i<nx; ++i) {
      float x = xmin+(xmax-xmin)*r.nextFloat();
      float yc0 = 1.0f+x+x*x;
      float yc1 = 1.0f+2.0f*x;
      float yc2 = 2.0f;
      float ys0 = cs.interpolate(x);
      float ys1 = cs.interpolate1(x);
      float ys2 = cs.interpolate2(x);
      assertEqual(yc0,ys0);
      assertEqual(yc1,ys1);
      assertEqual(yc2,ys2);
    }
  }

  private static void assertEqual(float x, float y) {
    assertTrue(x+" = "+y,almostEqual(x,y));
  }
  
  private static boolean almostEqual(float x, float y) {
    float ax = M.abs(x);
    float ay = M.abs(y);
    return M.abs(x-y)<0.001f*M.max(ax,ay);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Setup.
  
  public CubicInterpolatorTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(CubicInterpolatorTest.class);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
