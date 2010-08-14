/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.util.BrentZeroFinder}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2001.07.10, 2006.07.12
 */
public class BrentZeroFinderTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(BrentZeroFinderTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testForsythe() {
    ZeroFunc1 f1 = new ZeroFunc1();
    f1.findZero(2.0,3.0);
    ZeroFunc2 f2 = new ZeroFunc2();
    f2.findZero(-1.0,3.0);
    ZeroFunc3 f3 = new ZeroFunc3();
    f3.findZero(-1.0,3.0);
  }

  // Test functions with results published by Forsythe et al.
  abstract class BrentTestFunc implements BrentZeroFinder.Function {
    void findZero(double a, double b) {
      _count = 0;
      BrentZeroFinder zeroFinder = new BrentZeroFinder(this);
      double xzero = zeroFinder.findZero(a,b,DBL_EPSILON);
      double yzero = evaluate(xzero);
      checkRoot(xzero);
      checkFunc(yzero);
      checkCount(_count);
    }
    public double evaluate(double x) {
      ++_count;
      return eval(x);
    }
    abstract double eval(double x);
    abstract void checkRoot(double x);
    abstract void checkFunc(double y);
    abstract void checkCount(int count);
    private int _count;
  }
  class ZeroFunc1 extends BrentTestFunc {
    double eval(double x) {
      return (pow(x,2.0)-2.0)*x-5.0;
    }
    void checkRoot(double x) {
      assertEqual(x,2.094551482e+00);
    }
    void checkFunc(double y) {
      assertEqual(y,-1.7764e-15);
    }
    void checkCount(int count) {
      assertEqual(count,11);
    }
  }
  class ZeroFunc2 extends BrentTestFunc {
    double eval(double x) {
      return cos(x)-x;
    }
    void checkRoot(double x) {
      assertEqual(x,7.390851332e-01);
    }
    void checkFunc(double y) {
      assertEqual(y,0.0);
    }
    void checkCount(int count) {
      assertEqual(count,11);
    }
  }
  class ZeroFunc3 extends BrentTestFunc {
    double eval(double x) {
      return sin(x)-x;
    }
    void checkRoot(double x) {
      assertEqual(x,-1.643737357e-08);
    }
    void checkFunc(double y) {
      assertEqual(y,0.0);
    }
    void checkCount(int count) {
      assertEqual(count,58);
    }
  }

  private static void assertEqual(double x, double y) {
    assertTrue(x+" = "+y,almostEqual(x,y));
  }
  
  private static boolean almostEqual(double x, double y) {
    double ax = abs(x);
    double ay = abs(y);
    return abs(x-y)<=0.0001*max(ax,ay);
  }
}
