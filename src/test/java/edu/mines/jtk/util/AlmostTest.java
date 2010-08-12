/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Wrap another class Almost for junit testing.
   (junit.jar must be in CLASSPATH)
*/
public class AlmostTest extends TestCase {

  /** Run some test code.
      Any "public void test*()" method starting with "test" will be used
  */
  public void testEverything () {
    Almost a = new Almost();

    // should obviously succeed.  No precision involved
    assert (a.between(1., 0., 2.));
    assert (a.between(-1., 0., -2.));
    assert (a.between(-1., -0.5, -2.));
    assert (a.outside(1., 0., 2.) ==0);
    assert (a.outside(1., 0.5, 2.) ==0);
    assert (a.outside(-1., 0., -2.) ==0);
    assert (a.outside(-1., -0.5, -2.) ==0);
    assert (a.cmp(1., 0.) > 0);
    assert (a.cmp(0., 1.) < 0);
    assert (a.cmp(1., 1.) == 0);
    assert (a.cmp(0., 0.) == 0);
    assert (a.equal(3.,3.));
    assert (a.equal(0.,0.));
    assert (a.zero(0.));

    // Succeed if precision handled correctly
    assert (a.zero(a.getMinValue()/2.));
    assert (!a.zero(a.getMinValue()*2.));
    assert (1. != 1.+a.getEpsilon());
    assert (1. != 1.-a.getEpsilon());
    assert (0. != a.getMinValue());
    assert (a.equal(1., 1.+a.getEpsilon()/2.));
    assert (!a.equal(1., 1.+a.getEpsilon()*2.1));
    assert (a.equal(1., 1.000000000001));
    assert (a.getMinValue()/2.>0.);
    assert (a.equal(0., a.getMinValue()/2.));
    assert (a.between(1., 1.000000000001, 2.));
    assert (a.between(-1., -1.000000000001, -2.));
    assert (a.outside(1., 1.000000000001, 2.) ==0);
    assert (a.cmp(1., 1.000000000001) ==0);
  }

  /**
     test the hash code algorithm
   */
  public void testHashCode () {
    Almost a = new Almost(0.001,0.000001);
    assert (a.hashCodeOf(0.00000001,100) == 0);
    assert (a.hashCodeOf(0.99999999,100) == 1);
    assert (a.hashCodeOf(1.00000001,100) == 1);
    assert (a.hashCodeOf(123456789L,100) == 123456789L);
    assert (a.hashCodeOf(3.1415,4) ==
           a.hashCodeOf(3.1415926,4));
    assert (a.hashCodeOf(3.1415,5) !=
           a.hashCodeOf(3.1415926,5));
    assert (a.hashCodeOf(-3.1415,4) ==
           a.hashCodeOf(-3.1415926,4));
    assert (a.hashCodeOf(-3.1415,5) !=
           a.hashCodeOf(-3.1415926,5));
    assert (a.hashCodeOf(314.15,4) ==
           a.hashCodeOf(314.15926,4));
    assert (a.hashCodeOf(314.15,5) !=
           a.hashCodeOf(314.15926,5));
    assert (a.hashCodeOf(-314.15,4) ==
           a.hashCodeOf(-314.15926,4));
    assert (a.hashCodeOf(-314.15,5) !=
           a.hashCodeOf(-314.15926,5));
    assert (a.hashCodeOf(0.0031415,4) ==
           a.hashCodeOf(0.0031415926,4));
    assert (a.hashCodeOf(0.0031415,5) !=
           a.hashCodeOf(0.0031415926,5));

    // specify precision differently
    a = new Almost(0.0001);
    assert (a.equal(0.0031415,0.0031415926));
    assert (a.hashCodeOf(0.0031415) ==
           a.hashCodeOf(0.0031415926));

    a = new Almost(0.00001);
    assert (!a.equal(0.0031415,0.0031415926));
    assert (a.hashCodeOf(0.0031415) !=
           a.hashCodeOf(0.0031415926));

    a = new Almost(4);
    assert (a.equal(0.0031415,0.0031415926));
    assert (a.hashCodeOf(0.0031415) ==
           a.hashCodeOf(0.0031415926));

    a = new Almost(5);
    assert (!a.equal(0.0031415,0.0031415926));
    assert (a.hashCodeOf(0.0031415) !=
           a.hashCodeOf(0.0031415926));

  }

  /**
     test handling of nans
   */
  public void testNaNs() {
    try { Almost.FLOAT.equal(3,Float.NaN); assert false;}
    catch (IllegalArgumentException e) {}
    try { Almost.FLOAT.equal(0,Float.NaN); assert false;}
    catch (IllegalArgumentException e) {}
    try { Almost.FLOAT.equal(3,Double.NaN); assert false;}
    catch (IllegalArgumentException e) {}
    try { Almost.FLOAT.equal(0,Double.NaN); assert false;}
    catch (IllegalArgumentException e) {}
  }

  /**
     test Object methods
   */
  public void testAlmostObjectMethod() {
    Almost af1= new Almost(10*MathPlus.FLT_EPSILON, 100*Float.MIN_VALUE);
    Almost af2 = new Almost(10*MathPlus.FLT_EPSILON);
    Almost af3 = new Almost();
    Almost ad = new Almost(10*MathPlus.DBL_EPSILON, 100*Double.MIN_VALUE);
    assert af1.equals(af2): af1+" "+af2;
    assert af1.equals(af3): af1+" "+af3;
    assert af2.equals(af3): af2+" "+af3;
    assert af1.hashCode() == af2.hashCode(): af1+" "+af2;
    assert af1.hashCode() == af3.hashCode(): af1+" "+af3;
    assert af2.hashCode() == af3.hashCode(): af2+" "+af3;
    assert af1.toString().equals(af2.toString()):
      af1.toString()+" "+af2.toString();
    assert af1.toString().equals(af3.toString()):
      af1.toString()+" "+af3.toString();
    assert af2.toString().equals(af3.toString()):
      af2.toString()+" "+af3.toString();
    for (Almost af: new Almost[] {af1, af2, af3}) {
      assert !af.equals(ad);
      assert af.hashCode() != ad.hashCode();
      assert !af.toString().equals(ad.toString());
    }
  }

  // OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL

  /* Initialize objects used by all test methods */
  @Override protected void setUp() throws Exception {super.setUp();}

  /* Destruction of stuff used by all tests: rarely necessary */
  @Override protected void tearDown() throws Exception {super.tearDown();}

  // NO NEED TO CHANGE THE FOLLOWING

  /** Standard constructor calls TestCase(name) constructor
 * @param name */
  public AlmostTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods
 * @return new Test */
  public static junit.framework.Test suite() {
    return new TestSuite(AlmostTest.class);
  }

  /** Run all tests with text gui if this class main is invoked
 * @param args command line */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }

}

