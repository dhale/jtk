/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests all classes in package edu.mines.jtk.util.
 * @author Dave Hale
 * @version 2006.08.31
 */
public class AllTest extends TestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(AlmostTest.class);
    suite.addTestSuite(ArgsParserTest.class);
    suite.addTestSuite(ArrayMathTest.class);
    suite.addTestSuite(ArrayQueueTest.class);
    suite.addTestSuite(AxisTicsTest.class);
    suite.addTestSuite(CfloatTest.class);
    suite.addTestSuite(CleanFormatterTest.class);
    suite.addTestSuite(ClipsTest.class);
    suite.addTestSuite(LocalizeTest.class);
    suite.addTestSuite(MathPlusTest.class);
    suite.addTestSuite(ParameterTest.class);
    suite.addTestSuite(ParameterSetTest.class);
    suite.addTestSuite(QuantilerTest.class);
    suite.addTestSuite(SimpleFloat3Test.class);
    suite.addTestSuite(StopwatchTest.class);
    suite.addTestSuite(UnitSphereSamplingTest.class);
    suite.addTestSuite(UnitsTest.class);

    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
