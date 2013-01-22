/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests all classes in package edu.mines.jtk.interp.
 * @author Dave Hale
 * @version 2009.07.23
 */
public class AllTest extends TestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(BicubicInterpolator2Test.class);
    suite.addTestSuite(BilinearInterpolator2Test.class);
    suite.addTestSuite(CubicInterpolatorTest.class);
    suite.addTestSuite(Gridder2Test.class);
    suite.addTestSuite(LasserreVolumeTest.class);
    suite.addTestSuite(SibsonInterpolator2Test.class);
    suite.addTestSuite(SibsonInterpolator3Test.class);
    suite.addTestSuite(TricubicInterpolator3Test.class);
    suite.addTestSuite(TrilinearInterpolator3Test.class);

    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
