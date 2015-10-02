/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
