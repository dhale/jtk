/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.Almost;

/** Unit tests for edu.mines.jtk.opt.ArrayVect2.
*/
public class ArrayVect2Test extends TestCase {

  /** Run test code. */
  public void testAll () {
    double[][] a = new double[31][21];
    for (int i=0; i<a.length; ++i) {
      for (int j=0; j<a[i].length; ++j) {
        a[i][j] = i+2.4*j;
      }
    }
    Vect v = new ArrayVect2(a, 2.);
    VectUtil.test(v);

    // test inverse covariance
    for (int i=0; i<a.length; ++i) {
      for (int j=0; j<a[i].length; ++j) {
        a[i][j] = 1;
      }
    }
    v = new ArrayVect2(a, 3.);
    Vect w = v.clone();
    w.multiplyInverseCovariance();
    assert Almost.FLOAT.equal(1./3., v.dot(w));
    assert Almost.FLOAT.equal(1./3., v.magnitude());
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
  public ArrayVect2Test(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods.
      @return A suite of all junit tests as a Test.
   */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(ArrayVect2Test.class);
  }

  /** Run all tests with text gui if this class main is invoked 
      @param args Command-line arguments.
   */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
