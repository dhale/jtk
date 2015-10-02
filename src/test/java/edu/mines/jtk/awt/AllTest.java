/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
package edu.mines.jtk.awt;
 
import junit.framework.Test;
import junit.framework.TestSuite;
 
/**
 * Tests all classes in package edu.mines.jtk.awt.
 * @author Chris Engelsma
 * @version 2015.09.25
 */
public class AllTest extends TestSuite {
 
  public static Test suite() {
    TestSuite suite = new TestSuite();
 
    suite.addTestSuite(ColorMapTest.class);
 
    return suite;
  }
 
  /**
   * Self test.  Runs the test suite.
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
