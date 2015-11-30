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
package edu.mines.jtk.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.util.Parameter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 02/21/2000, 08/24/2006.
 */
public class ParameterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ParameterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testParameter() {
    Parameter par = new Parameter("fo<o","Hello");
    assertTrue(par.getString().equals("Hello"));
    par.setString("true");
    assertTrue(par.getBoolean());
    par.setString("3141");
    assertTrue(par.getInt()==3141);
    par.setString("3141.0");
    assertTrue(par.getFloat()==3141.0f);
    par.setString("3.141");
    assertTrue(par.getDouble()==3.141);
    double[] empty = new double[0];
    par.setDoubles(empty);
    assertTrue(par.getType()==Parameter.DOUBLE);
    par.setFloats(null);
    assertTrue(par.getType()==Parameter.FLOAT);
    float[] fvalues = {1.2f,3.4f};
    par.setFloats(fvalues);
    fvalues = par.getFloats();
    assertTrue(fvalues[0]==1.2f);
    assertTrue(fvalues[1]==3.4f);
    par.setUnits("km/s");
    assertTrue(par.getUnits().equals("km/s"));
    boolean[] bvalues = {true,false};
    par.setBooleans(bvalues);
    bvalues = par.getBooleans();
    assertTrue(bvalues[0]);
    assertTrue(!bvalues[1]);
    par.setUnits(null);
    assertTrue(par.getUnits()==null);
  }
}
