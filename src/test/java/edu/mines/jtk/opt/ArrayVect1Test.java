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

/** Wrap edu.mines.jtk.opt.ArrayVect1 for junit testing.
   (junit.jar must be in CLASSPATH)
*/
public class ArrayVect1Test extends TestCase {

  /** Run unit tests.
      @throws Exception any test failure
   */
  public void testAll() throws Exception {
    double[] a = new double[31];
    for (int i=0; i<a.length; ++i) {a[i] = i;}
    Vect v = new ArrayVect1(a, 3.);
    VectUtil.test(v);

    // test inverse covariance
    for (int i=0; i<a.length; ++i) {a[i] = 1;}
    v = new ArrayVect1(a, 3.);
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
   * @param name name of junit test  */
  public ArrayVect1Test(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods
   * @return junit test */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(ArrayVect1Test.class);
  }

  /** Run all tests with text gui if this class main is invoked
   * @param args command-line arguments */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
