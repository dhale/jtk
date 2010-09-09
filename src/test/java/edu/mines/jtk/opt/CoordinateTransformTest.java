/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import java.util.Arrays;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.Almost;
import static edu.mines.jtk.util.ArrayMath.copy;

/** Wrap edu.mines.jtk.opt.CoordinateTransform for junit testing.
   (junit.jar must be in CLASSPATH)
*/
public class CoordinateTransformTest extends TestCase {

  /** Test CoordinateTransform class
     @throws Exception
   */
  public void testAll() throws Exception {
    double[][] in = new double[][] {
      {1.,1.},
      {2.,2.},
      {3.,4.},
    };
    double[][] out = new double[][] {
      {2.},
      {4.},
      {7.},
    };
    double[][] inCopy = copy(in);
    double[][] outCopy = copy(out);

    CoordinateTransform ls = new CoordinateTransform(1, 2);
    for (int j=0; j<out.length; ++j) {
      ls.add(out[j], in[j]);
    }
    Almost almost = new Almost();
    double a, b;

    a = 1.; b = 1.;
    assert almost.equal(a+b, ls.get(new double[]{a,b})[0]):
      a+"+"+b+"!="+ls.get(new double[]{a,b})[0] ;

    a = 2.; b = 2.;
    assert almost.equal(a+b, ls.get(new double[]{a,b})[0]) :
      a+"+"+b+"!="+ls.get(new double[]{a,b})[0];

    a = 3.; b = 4.;
    assert almost.equal(a+b, ls.get(new double[]{a,b})[0]) :
      a+"+"+b+"!="+ls.get(new double[]{a,b})[0];

    a = 1.; b = 3.;
    assert almost.equal(a+b, ls.get(new double[]{a,b})[0]) :
      a+"+"+b+"!="+ls.get(new double[]{a,b})[0];

    a = 3.; b = 7;
    assert almost.equal(a+b, ls.get(new double[]{a,b})[0]) :
      a+"+"+b+"!="+ls.get(new double[]{a,b})[0];

    assert Arrays.deepEquals(in,inCopy) :
      Arrays.deepToString(in)+" "+Arrays.deepToString(inCopy);
    assert Arrays.deepEquals(out,outCopy):
      Arrays.deepToString(out)+" "+Arrays.deepToString(outCopy);
  }

  // OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL

  /* Initialize objects used by all test methods */
  @Override protected void setUp() throws Exception { super.setUp();}

  /* Destruction of stuff used by all tests: rarely necessary */
  @Override protected void tearDown() throws Exception { super.tearDown();}

  // NO NEED TO CHANGE THE FOLLOWING

  /** Standard constructor calls TestCase(name) constructor
   * @param name name of junit test */
  public CoordinateTransformTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods
   * @return junit Test */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(CoordinateTransformTest.class);
  }

  /** Run all tests with text gui if this class main is invoked
   * @param args command-line arguments (ignored) */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
