/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests all classes in package edu.mines.jtk.opt.
 * @author W.S. Harlan
 * @version 2005.03.24
 */
public class Suite extends TestSuite {

  /** Get all tests in this package.
      @return A suite of all junit tests as a Test.
   */
  public static Test suite() {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(AlmostTest.class);
    suite.addTestSuite(ArrayVect1Test.class);
    suite.addTestSuite(CoordinateTransformTest.class);
    suite.addTestSuite(GaussNewtonSolverTest.class);
    suite.addTestSuite(QuadraticSolverTest.class);
    suite.addTestSuite(ScalarSolverTest.class);

    return suite;
  }

  /** Run all tests with text gui if this class main is invoked
      @param args Command-line arguments.
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
