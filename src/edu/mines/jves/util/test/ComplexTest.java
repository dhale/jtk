/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.util.test;

import junit.framework.*;
import edu.mines.jves.util.M;
import edu.mines.jves.util.Complex;

/**
 * Tests {@link edu.mines.jves.util.Complex}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.04
 */
public class ComplexTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ComplexTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test() {

    Complex a = new Complex(M.FLT_PI,M.FLT_E);
    Complex b = new Complex(M.FLT_E,M.FLT_PI);

    assertEquals(a,Complex.sub(Complex.add(a,b),b));
    assertEquals(a,Complex.div(Complex.mul(a,b),b));

    assertEquals(a,Complex.conj(Complex.conj(a)));

    assertEquals(a,Complex.polar(Complex.abs(a),Complex.arg(a)));

    assertEquals(a,Complex.exp(Complex.log(a)));

    assertEquals(a,Complex.pow(Complex.sqrt(a),2.0f));

    assertEquals(Complex.pow(a,b),Complex.exp(b.times(Complex.log(a))));

    assertEquals(Complex.pow(a,b),Complex.exp(b.times(Complex.log(a))));

    assertEquals(Complex.sin(Complex.I.times(a)),
                 Complex.I.times(Complex.sinh(a)));

    assertEquals(Complex.cos(Complex.I.times(a)),Complex.cosh(a));

    assertEquals(Complex.tan(Complex.I.times(a)),
                 Complex.I.times(Complex.tanh(a)));
  }

  private void assertEquals(float expected, float actual) {
    float small = 1.0e-6f*M.max(M.abs(expected),M.abs(actual),1.0f);
    assertEquals(expected,actual,small);
  }

  private void assertEquals(Complex expected, Complex actual) {
    assertEquals(expected.r,actual.r);
    assertEquals(expected.i,actual.i);
  }
}
