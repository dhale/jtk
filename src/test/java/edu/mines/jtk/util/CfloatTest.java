/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.Cfloat.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.util.Cfloat}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.04
 */
public class CfloatTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(CfloatTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test() {

    Cfloat a = new Cfloat(FLT_PI,FLT_E);
    Cfloat b = new Cfloat(FLT_E,FLT_PI);

    assertEquals(a,sub(add(a,b),b));
    assertEquals(a,div(mul(a,b),b));

    assertEquals(a,conj(conj(a)));

    assertEquals(a,polar(abs(a),arg(a)));

    assertEquals(a,exp(log(a)));

    assertEquals(a,pow(sqrt(a),2.0f));

    assertEquals(pow(a,b),exp(b.times(log(a))));

    assertEquals(pow(a,b),exp(b.times(log(a))));

    assertEquals(sin(FLT_I.times(a)),
                 FLT_I.times(sinh(a)));

    assertEquals(cos(FLT_I.times(a)),cosh(a));

    assertEquals(tan(FLT_I.times(a)),
                 FLT_I.times(tanh(a)));
  }

  private void assertEquals(float expected, float actual) {
    float small = 1.0e-6f*max(abs(expected),abs(actual),1.0f);
    assertEquals(expected,actual,small);
  }

  private void assertEquals(Cfloat expected, Cfloat actual) {
    assertEquals(expected.r,actual.r);
    assertEquals(expected.i,actual.i);
  }
}
