/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util.test;

import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.CubicInterpolator;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.util.CubicInterpolator}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2000.02.21, 2006.07.12
 */
public class CubicInterpolatorTest extends TestCase {
  
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public void testMonotonic() {
    float[] x = {1.0f, 2.0f, 3.0f, 4.0f};
    float[] y = {0.0f, 0.0f, 1.0f, 1.0f};
    int n = x.length;
    
    CubicInterpolator.Method linear = CubicInterpolator.Method.LINEAR;
    CubicInterpolator.Method monoto = CubicInterpolator.Method.MONOTONIC;
    CubicInterpolator.Method spline = CubicInterpolator.Method.SPLINE;

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
    
    CubicInterpolator.Method spline = CubicInterpolator.Method.SPLINE;

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
    float ax = abs(x);
    float ay = abs(y);
    return abs(x-y)<0.001f*max(ax,ay);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Setup.
  
  public CubicInterpolatorTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(CubicInterpolatorTest.class);
  }
}
