/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
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
