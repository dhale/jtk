/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util.test;

import junit.framework.*;

/**
 * Tests all classes in package edu.mines.jtk.util.
 * @author Dave Hale
 * @version 2004.11.02
 */
public class Suite extends TestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(ArgsParserTest.class);
    suite.addTestSuite(ArrayTest.class);
    suite.addTestSuite(AxisTicsTest.class);
    suite.addTestSuite(BrentMinFinderTest.class);
    suite.addTestSuite(BrentZeroFinderTest.class);
    suite.addTestSuite(CfloatTest.class);
    suite.addTestSuite(MathPlusTest.class);
    suite.addTestSuite(StopwatchTest.class);

    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
