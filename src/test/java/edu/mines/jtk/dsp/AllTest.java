/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests all classes in package edu.mines.jtk.dsp.
 * @author Dave Hale
 * @version 2005.03.24
 */
public class AllTest extends TestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(EigenTest.class);
    suite.addTestSuite(EigenTensors2Test.class);
    suite.addTestSuite(EigenTensors3Test.class);
    suite.addTestSuite(FftTest.class);
    suite.addTestSuite(FftComplexTest.class);
    suite.addTestSuite(FftRealTest.class);
    suite.addTestSuite(HilbertTransformFilterTest.class);
    suite.addTestSuite(HistogramTest.class);
    suite.addTestSuite(LocalCausalFilterTest.class);
    suite.addTestSuite(LocalOrientFilterTest.class);
    suite.addTestSuite(Real1Test.class);
    suite.addTestSuite(Recursive2ndOrderFilterTest.class);
    suite.addTestSuite(RecursiveExponentialFilterTest.class);
    suite.addTestSuite(RecursiveGaussianFilterTest.class);
    suite.addTestSuite(SamplingTest.class);
    suite.addTestSuite(SincInterpolatorTest.class);
    suite.addTestSuite(SymmetricTridiagonalFilterTest.class);

    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
