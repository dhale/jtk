/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests all classes in package edu.mines.jtk.opt.
 * @author W.S. Harlan
 */
public class AllTest extends TestSuite {

  /** Get all tests in this package.
      @return A suite of all junit tests as a Test.
   */
  public static Test suite() {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(ArrayVect1fsTest.class);
    suite.addTestSuite(ArrayVect1fTest.class);
    suite.addTestSuite(ArrayVect1Test.class);
    suite.addTestSuite(ArrayVect2fTest.class);
    suite.addTestSuite(ArrayVect2Test.class);
    suite.addTestSuite(ArrayVect3fTest.class);
    suite.addTestSuite(BrentMinFinderTest.class);
    suite.addTestSuite(BrentZeroFinderTest.class);
    suite.addTestSuite(CoordinateTransformTest.class);
    suite.addTestSuite(GaussNewtonSolverTest.class);
    suite.addTestSuite(LineSearchTest.class);
    suite.addTestSuite(QuadraticSolverTest.class);
    suite.addTestSuite(ScalarSolverTest.class);
    suite.addTestSuite(ScalarVectTest.class);
    suite.addTestSuite(VectArrayTest.class);
    suite.addTestSuite(VectMapTest.class);
    return suite;
  }

  /** Run all tests with text gui if this class main is invoked
      @param args Command-line arguments.
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
