/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.logging.Logger;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.Almost;

/** Unit tests for edu.mines.jtk.opt.ArrayVect1f.
*/
public class ArrayVect1fTest extends TestCase {
  private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt.test");

  /** Run test code.
   * @throws Exception any test failure */
  public void testAll () throws Exception {
    { // check Vect properties
      float[] a = new float[31];
      for (int i=0; i<a.length; ++i) {a[i] = i;}
      Vect v = new ArrayVect1f(a, 0, 3.);
      VectUtil.test(v);
      v = new ArrayVect1f(a, 10, 3.);
      VectUtil.test(v);

      // test inverse covariance
      for (int i=0; i<a.length; ++i) {a[i] = 1;}
      v = new ArrayVect1f(a, 0, 3.);
      Vect w = v.clone();
      w.multiplyInverseCovariance();
      assert Almost.FLOAT.equal(1./3., v.dot(w));
      assert Almost.FLOAT.equal(1./3., v.magnitude());
    }

    { // test size of serialization
      float[] data = new float[501];
      Arrays.fill(data, 1.f);
      int minimumSize = data.length*4;
      int externalSize = 10*minimumSize;
      int writeObjectSize = 10*minimumSize;
      LOG.fine("minimum size = "+ minimumSize);
      ArrayVect1f v = new ArrayVect1f(data, 0, 1.);
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(0);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(v);
        oos.close();
        byte[] result = baos.toByteArray();
        externalSize = result.length;
        LOG.fine("externalizable size = "+externalSize);
      }
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(0);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(v);
        oos.close();
        byte[] result = baos.toByteArray();
        writeObjectSize = result.length;
        LOG.fine("writeObject size = "+writeObjectSize);
      }
      assert externalSize <= minimumSize + 308 :
        externalSize+" <= "+(minimumSize + 308 );
      assert writeObjectSize <= minimumSize + 308 :
        writeObjectSize +"<="+ (minimumSize + 308);
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
  public ArrayVect1fTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods.
      @return A suite of all junit tests as a Test.
   */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(ArrayVect1fTest.class);
  }

  /** Run all tests with text gui if this class main is invoked
      @param args Command-line arguments.
   */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
