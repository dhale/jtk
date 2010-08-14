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

/** Unit tests for edu.mines.jtk.opt.VectArray.
*/
public class VectArrayTest extends TestCase {

  /** Run test code. */
  public void testAll () {
    Random random = new Random(32525);
    VectArray vm = new VectArray(5);
    for (int index=0; index<5; ++index) {
      double[] a = new double[7*index];
      for (int i=0; i<a.length; ++i) {a[i] = random.nextDouble();}
      Vect v = new ArrayVect1(a, 2.);
      vm.put(index, v);
      assert vm.containsKey(index);
    }
    assert !vm.containsKey(99);
    VectUtil.test(vm);
    int[] keys = vm.getKeys();
    assert keys.length == 5 : "keys.length = "+keys.length;
    for (int i=0; i<5; ++i) { assert keys[i] == i;}
    for (int index=0; index<5; ++index) {
      ArrayVect1 value = (ArrayVect1) vm.get(index);
      assert (value != null) : "index="+index;
      assert (value.getData() != null) : "index="+index;
      assert (value.getSize() == 7*index) : "index="+index;
      assert (value.getData().length == 7*index) : "index="+index;
    }
    // test inverse covariance
    vm = new VectArray(5);
    for (int index=0; index<5; ++index) {
      double[] a = new double[7*index+1];
      for (int i=0; i<a.length; ++i) {a[i] = 1;}
      Vect v = new ArrayVect1(a, 1.);
      vm.put(index, v);
    }
    Vect wm = vm.clone();
    wm.multiplyInverseCovariance();
    assert Almost.FLOAT.equal(1., wm.dot(vm)) : wm.dot(vm);
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
  public VectArrayTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods.
      @return A suite of all junit tests as a Test.
   */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(VectArrayTest.class);
  }

  /** Run all tests with text gui if this class main is invoked 
      @param args Command-line arguments.
   */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
