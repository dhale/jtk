package edu.mines.jtk.opt.test;

import java.util.Arrays;
import junit.framework.*;

import edu.mines.jtk.util.Array;
import edu.mines.jtk.opt.Almost;
import edu.mines.jtk.opt.CoordinateTransform;

/** Wrap edu.mines.jtk.opt.CoordinateTransform for junit testing.
   (junit.jar must be in CLASSPATH)
*/
public class CoordinateTransformTest extends TestCase {

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
    double[][] inCopy = Array.copy(in);
    double[][] outCopy = Array.copy(out);

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

  /** Standard constructor calls TestCase(name) constructor */
  public CoordinateTransformTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(CoordinateTransformTest.class);
  }

  /** Run all tests with text gui if this class main is invoked */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
