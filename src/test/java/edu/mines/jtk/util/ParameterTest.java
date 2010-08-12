/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
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
