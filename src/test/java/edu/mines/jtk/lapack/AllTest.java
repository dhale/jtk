/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests all classes in package edu.mines.jtk.lapack.
 * @author Dave Hale
 * @version 2005.12.12
 */
public class AllTest extends TestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(DMatrixTest.class);
    suite.addTestSuite(DMatrixChdTest.class);
    suite.addTestSuite(DMatrixEvdTest.class);
    suite.addTestSuite(DMatrixLudTest.class);
    suite.addTestSuite(DMatrixQrdTest.class);
    suite.addTestSuite(DMatrixSvdTest.class);

    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
