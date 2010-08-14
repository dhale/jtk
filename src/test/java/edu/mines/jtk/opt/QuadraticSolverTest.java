/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import java.util.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.Almost;

/** Wrap edu.mines.jtk.opt.QuadraticSolver for junit testing.
   (junit.jar must be in CLASSPATH)
*/
public class QuadraticSolverTest extends TestCase {
  private static final String NL = System.getProperty("line.separator");
  /** Junit test of QuadraticSolver
   */
  public void testQS() {
    /*
      Minimize  0.5 x'Hx + b'x = 0, where H = |2 4 | and b = (2 1)'
                                              |4 11|
      solution is x = (-3 1)'
    */
    Quadratic q = new Quadratic() {
        public void multiplyHessian(Vect x) {
          double[] data = ((ArrayVect1)x).getData();
          double[] newData = new double[data.length];
          newData[0] = 2.*data[0] + 4.*data[1];
          newData[1] = 4.*data[0] + 11.*data[1];
          data[0] = newData[0];
          data[1] = newData[1];
        }
        public void inverseHessian(Vect x) {}
        public Vect getB() {
          return new TestVect(new double[] {2.,1.}, 1.);
        }
      };
    QuadraticSolver qs = new QuadraticSolver(q);

    { // not enough iterations
      ArrayVect1 result = (ArrayVect1) qs.solve(1, null);
      assert !Almost.FLOAT.equal(-3., result.getData()[0]): "result="+result;
      assert !Almost.FLOAT.equal(1., result.getData()[1]): "result="+result;
      result.dispose();
    }
    { // just barely enough iterations
      ArrayVect1 result = (ArrayVect1) qs.solve(2, null);
      assert Almost.FLOAT.equal(-3., result.getData()[0]): "result="+result;
      assert Almost.FLOAT.equal(1., result.getData()[1]): "result="+result;
      result.dispose();
    }
    { // Does not blow up with too many iterations
      ArrayVect1 result = (ArrayVect1) qs.solve(20, null);
      assert Almost.FLOAT.equal(-3., result.getData()[0]): "result="+result;
      assert Almost.FLOAT.equal(1., result.getData()[1]): "result="+result;
      result.dispose();
    }
    assert TestVect.undisposed.size() == 0 : TestVect.getTraces();
    assert TestVect.max <= 5:
      "max number of model vectors ("+TestVect.max+") should be less than 5";

  }

  private static class TestVect extends ArrayVect1 {
    private static final long serialVersionUID = 1L;
    /** Visible only for tests */
    public static int max = 0;
    /** Visible only for tests. */
    public static Map<Object,String> undisposed =
      Collections.synchronizedMap(new HashMap<Object,String>());

    /** Constructor
       @param data
       @param variance
     */
    public TestVect(double[] data, double variance) {
      super (data,variance);
      remember(this);
    }
    @Override
        public TestVect clone() {
      TestVect result = (TestVect) super.clone();
      remember(result);
      return result;
    }
    private void remember(Object tv) { // remember where allocated
      synchronized (undisposed) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        new Exception("This vector was never disposed").printStackTrace(pw);
        pw.flush();
        undisposed.put(tv, sw.toString());
        max = Math.max(max, undisposed.size());
      }
    }
    @Override
        public void dispose() {
      synchronized (undisposed) {
        super.dispose();
        undisposed.remove(this);
      }
    }
    /** @return traces for debugging
     */
    public static String getTraces() {
      StringBuilder sb = new StringBuilder();
      for (String s : undisposed.values()) {
        sb.append(s);
        sb.append(NL);
      }
      return sb.toString();
    }
  }

  // OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL

  /* Initialize objects used by all test methods */
  @Override protected void setUp() throws Exception { super.setUp();}

  /* Destruction of stuff used by all tests: rarely necessary */
  @Override protected void tearDown() throws Exception { super.tearDown();}

  // NO NEED TO CHANGE THE FOLLOWING

  /** Standard constructor calls TestCase(name) constructor
      @param name Name of junit Test.
   */
  public QuadraticSolverTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods.
      @return A suite of all junit tests as a Test.
   */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(QuadraticSolverTest.class);
  }

  /** Run all tests with text gui if this class main is invoked
      @param args Command-line arguments.
   */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
