/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import static edu.mines.jtk.util.MathPlus.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.util.MathPlus}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.04
 */
public class MathPlusTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(MathPlusTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test() {
    assertEquals(0.0f,sin(FLT_PI));
    assertEquals(0.0d,sin(DBL_PI));

    assertEquals(1.0f,cos(2.0f*FLT_PI));
    assertEquals(1.0d,cos(2.0d*DBL_PI));

    assertEquals(1.0f,tan(FLT_PI/4.0f));
    assertEquals(1.0d,tan(DBL_PI/4.0d));

    assertEquals(FLT_PI/2.0f,asin(1.0f));
    assertEquals(DBL_PI/2.0d,asin(1.0d));

    assertEquals(FLT_PI/2.0f,acos(0.0f));
    assertEquals(DBL_PI/2.0d,acos(0.0d));

    assertEquals(FLT_PI/4.0f,atan(1.0f));
    assertEquals(DBL_PI/4.0d,atan(1.0d));

    assertEquals(FLT_PI/2.0f,atan2(1.0f,0.0f));
    assertEquals(DBL_PI/2.0d,atan2(1.0d,0.0d));

    assertEquals(-3.0f*FLT_PI/4.0f,atan2(-1.0f,-1.0f));
    assertEquals(-3.0d*DBL_PI/4.0d,atan2(-1.0d,-1.0d));

    assertEquals(FLT_PI,toRadians(180.0f));
    assertEquals(DBL_PI,toRadians(180.0d));

    assertEquals(180.0f,toDegrees(FLT_PI));
    assertEquals(180.0d,toDegrees(DBL_PI));

    assertEquals(1.0f,log(exp(1.0f)));
    assertEquals(1.0d,log(exp(1.0d)));

    assertEquals(3.0f,sqrt(pow(3.0f,2.0f)));
    assertEquals(3.0d,sqrt(pow(3.0d,2.0d)));

    assertEquals(tanh(1.0f),sinh(1.0f)/cosh(1.0f));
    assertEquals(tanh(1.0d),sinh(1.0d)/cosh(1.0d));

    assertEquals(4.0f,ceil(FLT_PI));
    assertEquals(4.0d,ceil(DBL_PI));
    assertEquals(-3.0f,ceil(-FLT_PI));
    assertEquals(-3.0d,ceil(-DBL_PI));

    assertEquals(3.0f,floor(FLT_PI));
    assertEquals(3.0d,floor(DBL_PI));
    assertEquals(-4.0f,floor(-FLT_PI));
    assertEquals(-4.0d,floor(-DBL_PI));

    assertEquals(3.0f,rint(FLT_PI));
    assertEquals(3.0d,rint(DBL_PI));
    assertEquals(-3.0f,rint(-FLT_PI));
    assertEquals(-3.0d,rint(-DBL_PI));

    assertEquals(3,round(FLT_PI));
    assertEquals(3,round(DBL_PI));
    assertEquals(-3,round(-FLT_PI));
    assertEquals(-3,round(-DBL_PI));

    assertEquals(3,round(FLT_E));
    assertEquals(3,round(DBL_E));
    assertEquals(-3,round(-FLT_E));
    assertEquals(-3,round(-DBL_E));

    assertEquals(1.0f,signum(FLT_PI));
    assertEquals(1.0d,signum(DBL_PI));
    assertEquals(-1.0f,signum(-FLT_PI));
    assertEquals(-1.0d,signum(-DBL_PI));
    assertEquals(0.0f,signum(0.0f));
    assertEquals(0.0d,signum(0.0d));

    assertEquals(2,abs(2));
    assertEquals(2L,abs(2L));
    assertEquals(2.0f,abs(2.0f));
    assertEquals(2.0d,abs(2.0d));
    assertEquals(2,abs(-2));
    assertEquals(2L,abs(-2L));
    assertEquals(2.0f,abs(-2.0f));
    assertEquals(2.0d,abs(-2.0d));
    assertEquals("abs(float) changed sign of 0",
                 0, Float.floatToIntBits(abs(0.0f)));
    assertEquals("abs(double) changed sign of 0",
                 0, Double.doubleToLongBits(abs(0.0d)));

    assertEquals(4,max(1,3,4,2));
    assertEquals(4L,max(1L,3L,4L,2L));
    assertEquals(4.0f,max(1.0f,3.0f,4.0f,2.0f));
    assertEquals(4.0d,max(1.0d,3.0d,4.0d,2.0d));

    assertEquals(1,min(3,1,4,2));
    assertEquals(1L,min(3L,1L,4L,2L));
    assertEquals(1.0f,min(3.0f,1.0f,4.0f,2.0f));
    assertEquals(1.0d,min(3.0d,1.0d,4.0d,2.0d));
  }

  private void assertEquals(float expected, float actual) {
    float small = 1.0e-6f*max(abs(expected),abs(actual),1.0f);
    assertEquals(expected,actual,small);
  }

  private void assertEquals(double expected, double actual) {
    double small = 1.0e-12f*max(abs(expected),abs(actual),1.0d);
    assertEquals(expected,actual,small);
  }
}
