/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.util.test;

import junit.framework.*;
import edu.mines.jves.util.M;

/**
 * Tests {@link edu.mines.jves.util.M}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.04
 */
public class MTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(MTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test() {
    assertEquals(0.0f,M.sin(M.FLT_PI));
    assertEquals(0.0d,M.sin(M.DBL_PI));

    assertEquals(1.0f,M.cos(2.0f*M.FLT_PI));
    assertEquals(1.0d,M.cos(2.0d*M.DBL_PI));

    assertEquals(1.0f,M.tan(M.FLT_PI/4.0f));
    assertEquals(1.0d,M.tan(M.DBL_PI/4.0d));

    assertEquals(M.FLT_PI/2.0f,M.asin(1.0f));
    assertEquals(M.DBL_PI/2.0d,M.asin(1.0d));

    assertEquals(M.FLT_PI/2.0f,M.acos(0.0f));
    assertEquals(M.DBL_PI/2.0d,M.acos(0.0d));

    assertEquals(M.FLT_PI/4.0f,M.atan(1.0f));
    assertEquals(M.DBL_PI/4.0d,M.atan(1.0d));

    assertEquals(M.FLT_PI/2.0f,M.atan2(1.0f,0.0f));
    assertEquals(M.DBL_PI/2.0d,M.atan2(1.0d,0.0d));

    assertEquals(-3.0f*M.FLT_PI/4.0f,M.atan2(-1.0f,-1.0f));
    assertEquals(-3.0d*M.DBL_PI/4.0d,M.atan2(-1.0d,-1.0d));

    assertEquals(M.FLT_PI,M.toRadians(180.0f));
    assertEquals(M.DBL_PI,M.toRadians(180.0d));

    assertEquals(180.0f,M.toDegrees(M.FLT_PI));
    assertEquals(180.0d,M.toDegrees(M.DBL_PI));

    assertEquals(1.0f,M.log(M.exp(1.0f)));
    assertEquals(1.0d,M.log(M.exp(1.0d)));

    assertEquals(3.0f,M.sqrt(M.pow(3.0f,2.0f)));
    assertEquals(3.0d,M.sqrt(M.pow(3.0d,2.0d)));

    assertEquals(M.tanh(1.0f),M.sinh(1.0f)/M.cosh(1.0f));
    assertEquals(M.tanh(1.0d),M.sinh(1.0d)/M.cosh(1.0d));

    assertEquals(4.0f,M.ceil(M.FLT_PI));
    assertEquals(4.0d,M.ceil(M.DBL_PI));
    assertEquals(-3.0f,M.ceil(-M.FLT_PI));
    assertEquals(-3.0d,M.ceil(-M.DBL_PI));

    assertEquals(3.0f,M.floor(M.FLT_PI));
    assertEquals(3.0d,M.floor(M.DBL_PI));
    assertEquals(-4.0f,M.floor(-M.FLT_PI));
    assertEquals(-4.0d,M.floor(-M.DBL_PI));

    assertEquals(3.0f,M.rint(M.FLT_PI));
    assertEquals(3.0d,M.rint(M.DBL_PI));
    assertEquals(-3.0f,M.rint(-M.FLT_PI));
    assertEquals(-3.0d,M.rint(-M.DBL_PI));

    assertEquals(3,M.round(M.FLT_PI));
    assertEquals(3,M.round(M.DBL_PI));
    assertEquals(-3,M.round(-M.FLT_PI));
    assertEquals(-3,M.round(-M.DBL_PI));

    assertEquals(3,M.round(M.FLT_E));
    assertEquals(3,M.round(M.DBL_E));
    assertEquals(-3,M.round(-M.FLT_E));
    assertEquals(-3,M.round(-M.DBL_E));

    assertEquals(1.0f,M.signum(M.FLT_PI));
    assertEquals(1.0d,M.signum(M.DBL_PI));
    assertEquals(-1.0f,M.signum(-M.FLT_PI));
    assertEquals(-1.0d,M.signum(-M.DBL_PI));
    assertEquals(0.0f,M.signum(0.0f));
    assertEquals(0.0d,M.signum(0.0d));

    assertEquals(2,M.abs(2));
    assertEquals(2L,M.abs(2L));
    assertEquals(2.0f,M.abs(2.0f));
    assertEquals(2.0d,M.abs(2.0d));
    assertEquals(2,M.abs(-2));
    assertEquals(2L,M.abs(-2L));
    assertEquals(2.0f,M.abs(-2.0f));
    assertEquals(2.0d,M.abs(-2.0d));

    assertEquals(4,M.max(1,3,4,2));
    assertEquals(4L,M.max(1L,3L,4L,2L));
    assertEquals(4.0f,M.max(1.0f,3.0f,4.0f,2.0f));
    assertEquals(4.0d,M.max(1.0d,3.0d,4.0d,2.0d));

    assertEquals(1,M.min(3,1,4,2));
    assertEquals(1L,M.min(3L,1L,4L,2L));
    assertEquals(1.0f,M.min(3.0f,1.0f,4.0f,2.0f));
    assertEquals(1.0d,M.min(3.0d,1.0d,4.0d,2.0d));
  }

  private void assertEquals(float expected, float actual) {
    float small = 1.0e-6f*M.max(M.abs(expected),M.abs(actual),1.0f);
    assertEquals(expected,actual,small);
  }

  private void assertEquals(double expected, double actual) {
    double small = 1.0e-12f*M.max(M.abs(expected),M.abs(actual),1.0d);
    assertEquals(expected,actual,small);
  }
}
