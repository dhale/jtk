/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.Almost;

/** Unit tests for edu.mines.jtk.opt.ArrayVect2f.
*/
public class ArrayVect2fTest extends TestCase {

  /** Run test code. */
  public void testAll () {
    {
      float[][] a = new float[31][21];
      for (int i=0; i<a.length; ++i) {
        for (int j=0; j<a[i].length; ++j) {
          a[i][j] = i+2.5f*j;
        }
      }
      Vect v = new ArrayVect2f(a, 2.);
      VectUtil.test(v);

      // test inverse covariance
      for (int i=0; i<a.length; ++i) {
        for (int j=0; j<a[i].length; ++j) {
          a[i][j] = 1;
        }
      }
      v = new ArrayVect2f(a, 3.);
      Vect w = v.clone();
      w.multiplyInverseCovariance();
      assert Almost.FLOAT.equal(1./3., v.dot(w));
      assert Almost.FLOAT.equal(1./3., v.magnitude());
    }

    {
      Random random = new Random(352);
      float[][] a = new float[201][];
      boolean oneWasShort = false;
      boolean oneWasLong = false;
      for (int i=0; i<a.length; ++i) {
        a[i] = new float[random.nextInt(11)];
        if (a[i].length ==0) oneWasShort = true;
        for (int j=0; j<a[i].length; ++j) {
          oneWasLong = true;
          a[i][j] = 5*random.nextFloat()-2;
        }
      }
      assert oneWasShort;
      assert oneWasLong;
      Vect v = new ArrayVect2f(a, 2.5);
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
  public ArrayVect2fTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods.
      @return A suite of all junit tests as a Test.
   */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(ArrayVect2fTest.class);
  }

  /** Run all tests with text gui if this class main is invoked 
      @param args Command-line arguments.
   */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
