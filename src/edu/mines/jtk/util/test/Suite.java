/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util.test;

import junit.framework.*;

/**
 * Tests all classes package edu.mines.jtk.util.
 * @author Dave Hale
 * @version 2004.11.02
 */
public class Suite extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(AxisTicsTest.class);
    suite.addTestSuite(ComplexTest.class);
    suite.addTestSuite(MTest.class);
    suite.addTestSuite(StopwatchTest.class);

    junit.textui.TestRunner.run(suite);
  }
}
