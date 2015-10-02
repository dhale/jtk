/****************************************************************************
Copyright 2003, Landmark Graphics and others.
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
package edu.mines.jtk.opt;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Unit tests for edu.mines.jtk.opt.ScalarVect.
*/
public class ScalarVectTest extends TestCase {

  /** Run test code. */
  public void testAll () {
    {
      Vect v = new ScalarVect(.266, 3.);
      VectUtil.test(v);
    }
    {
      Vect v = new ScalarVect(-666.266, 8.);
      VectUtil.test(v);
    }
  }

  // OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL

  /* Initialize objects used by all test methods */
  @Override protected void setUp() throws Exception { super.setUp();}

  /* Destruction of stuff used by all tests: rarely necessary */
  @Override protected void tearDown() throws Exception { super.tearDown();}

  // NO NEED TO CHANGE THE FOLLOWING

  /** Standard constructor calls TestCase(name) constructor 
      @param name Name of junit Test.
   */
  public ScalarVectTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods.
      @return A suite of all junit tests as a Test.
   */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(ScalarVectTest.class);
  }

  /** Run all tests with text gui if this class main is invoked 
      @param args Command-line arguments.
   */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
