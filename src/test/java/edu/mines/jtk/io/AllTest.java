/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.io;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests all classes in package edu.mines.jtk.io.
 * @author Dave Hale
 * @version 2006.01.14
 */
public class AllTest extends TestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(ArrayFileTest.class);

    return suite;
  }

  /**
   * Self test.  Runs the test suite.
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
