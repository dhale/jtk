/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;

/**
 * Tests all classes in package edu.mines.jtk.dsp.
 * @author Dave Hale
 * @version 2005.03.24
 */
public class Suite extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(FftComplexTest.class);
    suite.addTestSuite(FftRealTest.class);
    suite.addTestSuite(Real1Test.class);
    suite.addTestSuite(SamplingTest.class);

    junit.textui.TestRunner.run(suite);
  }
}
