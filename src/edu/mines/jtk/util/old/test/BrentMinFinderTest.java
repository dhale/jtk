
package com.lgc.idh.util.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.lgc.idh.lang.M;
import com.lgc.idh.util.BrentMinFinder;

/**
 * Tests {@link com.lgc.idh.util.BrentMinFinder}.
 * @author Dave Hale
 * @version 2003.07.15
 */
public class BrentMinFinderTest extends TestCase {

  // Test functions with results published by Forsythe et al.
  abstract class BrentTestFunc implements BrentMinFinder.Function {
    void findMin(double a, double b) {
      _count = 0;
      BrentMinFinder minFinder = new BrentMinFinder(this);
      double xmin = minFinder.findMin(a,b,M.DBL_EPSILON);
      double ymin = eval(xmin);
      checkMin(xmin);
      checkFunc(ymin);
      checkCount(_count);
    }
    public double eval(double x) {
      ++_count;
      return evaluate(x);
    }
    abstract double evaluate(double x);
    abstract void checkMin(double x);
    abstract void checkFunc(double y);
    abstract void checkCount(int count);
    private int _count;
  }
  class MinFunc1 extends BrentTestFunc {
    double evaluate(double x) {
      return (M.pow(x,2)-2.0)*x-5.0;
    }
    void checkMin(double x) {
      assertEqual(x,8.164965811e-01);
    }
    void checkFunc(double y) {
      assertEqual(y,-6.0887e+00);
    }
    void checkCount(int count) {
      assertEqual(count,12);
    }
  }
  class MinFunc2 extends BrentTestFunc {
    double evaluate(double x) {
      return M.pow((M.pow(x,2)-2.0)*x-5.0,2);
    }
    void checkMin(double x) {
      assertEqual(x,2.094551483e+00);
    }
    void checkFunc(double y) {
      assertEqual(y,2.7186e-16);
    }
    void checkCount(int count) {
      assertEqual(count,14);
    }
  }
  class MinFunc3 extends BrentTestFunc {
    double evaluate(double x) {
      return M.pow(M.cos(x)-x,2)-2;
    }
    void checkMin(double x) {
      assertEqual(x,7.390851269e-01);
    }
    void checkFunc(double y) {
      assertEqual(y,-2.0);
    }
    void checkCount(int count) {
      assertEqual(count,14);
    }
  }
  class MinFunc4 extends BrentTestFunc {
    double evaluate(double x) {
      return M.pow(M.sin(x)-x,2)+1;
    }
    void checkMin(double x) {
      assertEqual(x,-3.125827630e-04);
    }
    void checkFunc(double y) {
      assertEqual(y,1.0);
    }
    void checkCount(int count) {
      assertEqual(count,48);
    }
  }

  public void testMinFinder() {
    MinFunc1 f1 = new MinFunc1();
    f1.findMin(0.0,1.0);
    MinFunc2 f2 = new MinFunc2();
    f2.findMin(2.0,3.0);
    MinFunc3 f3 = new MinFunc3();
    f3.findMin(-1.0,3.0);
    MinFunc4 f4 = new MinFunc4();
    f4.findMin(-1.0,3.0);
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
  
  public BrentMinFinderTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(BrentMinFinderTest.class);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
