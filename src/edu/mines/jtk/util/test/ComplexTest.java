/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.util.test;

import junit.framework.*;
import edu.mines.jves.util.Complex;
import static edu.mines.jves.util.Complex.*;
import static edu.mines.jves.util.M.*;

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

    Complex a = new Complex(FLT_PI,FLT_E);
    Complex b = new Complex(FLT_E,FLT_PI);

    assertEquals(a,sub(add(a,b),b));
    assertEquals(a,div(mul(a,b),b));

    assertEquals(a,conj(conj(a)));

    assertEquals(a,polar(abs(a),arg(a)));

    assertEquals(a,exp(log(a)));

    assertEquals(a,pow(sqrt(a),2.0f));

    assertEquals(pow(a,b),exp(b.times(log(a))));

    assertEquals(pow(a,b),exp(b.times(log(a))));

    assertEquals(sin(I.times(a)),
                 I.times(sinh(a)));

    assertEquals(cos(I.times(a)),cosh(a));

    assertEquals(tan(I.times(a)),
                 I.times(tanh(a)));
  }

  private void assertEquals(float expected, float actual) {
    float small = 1.0e-6f*max(abs(expected),abs(actual),1.0f);
    assertEquals(expected,actual,small);
  }

  private void assertEquals(Complex expected, Complex actual) {
    assertEquals(expected.r,actual.r);
    assertEquals(expected.i,actual.i);
  }
}
