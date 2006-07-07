package com.lgc.idh.util.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.lgc.idh.lang.M;
import com.lgc.idh.util.BrentZeroFinder;

/**
 * Tests {@link com.lgc.idh.util.BrentZeroFinder}.
 * @author Dave Hale
 * @version 2001.07.10
 */
public class BrentZeroFinderTest extends TestCase {

  // Test functions with results published by Forsythe et al.
  abstract class BrentTestFunc implements BrentZeroFinder.Function {
    void findZero(double a, double b) {
      _count = 0;
      BrentZeroFinder zeroFinder = new BrentZeroFinder(this);
      double xzero = zeroFinder.findZero(a,b,M.DBL_EPSILON);
      double yzero = eval(xzero);
      checkRoot(xzero);
      checkFunc(yzero);
      checkCount(_count);
    }
    public double eval(double x) {
      ++_count;
      return evaluate(x);
    }
    abstract double evaluate(double x);
    abstract void checkRoot(double x);
    abstract void checkFunc(double y);
    abstract void checkCount(int count);
    private int _count;
  }
  class ZeroFunc1 extends BrentTestFunc {
    double evaluate(double x) {
      return (M.pow(x,2)-2.0)*x-5.0;
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
    double evaluate(double x) {
      return M.cos(x)-x;
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
    double evaluate(double x) {
      return M.sin(x)-x;
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

  public void testZeroFinder() {
    ZeroFunc1 f1 = new ZeroFunc1();
    f1.findZero(2.0,3.0);
    ZeroFunc2 f2 = new ZeroFunc2();
    f2.findZero(-1.0,3.0);
    ZeroFunc3 f3 = new ZeroFunc3();
    f3.findZero(-1.0,3.0);
  }

  private static void assertEqual(double x, double y) {
    assertTrue(x+" = "+y,almostEqual(x,y));
  }
  
  private static boolean almostEqual(double x, double y) {
    double ax = M.abs(x);
    double ay = M.abs(y);
    return M.abs(x-y)<=0.0001*M.max(ax,ay);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Setup.
  
  public BrentZeroFinderTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(BrentZeroFinderTest.class);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
