package edu.mines.jtk.util.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.Parameter;

/**
 * Tests {@link edu.mines.jtk.util.Parameter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 02/21/2000, 08/24/2006.
 */
public class ParameterTest extends TestCase {

  public void testParameter() {
    Parameter par = new Parameter("fo<o","Hello");
    assertTrue(par.getString().equals("Hello"));
    par.setString("true");
    assertTrue(par.getBoolean()==true);
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
    fvalues = null;
    fvalues = par.getFloats();
    assertTrue(fvalues[0]==1.2f);
    assertTrue(fvalues[1]==3.4f);
    par.setUnits("km/s");
    assertTrue(par.getUnits().equals("km/s"));
    boolean[] bvalues = {true,false};
    par.setBooleans(bvalues);
    bvalues = null;
    bvalues = par.getBooleans();
    assertTrue(bvalues[0]==true);
    assertTrue(bvalues[1]==false);
    par.setUnits(null);
    assertTrue(par.getUnits()==null);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Setup.
  
  public ParameterTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(ParameterTest.class);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
