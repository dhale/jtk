/****************************************************************************
Copyright 2011, Colorado School of Mines and others.
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
package edu.mines.jtk.mosaic;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class ProjectorTest extends TestCase {
  private static final double eps = 1.0e-10;

  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ProjectorTest.class);
    TestResult result = junit.textui.TestRunner.run(suite);

    // Check result and exit with nonzero status if any failed.
    if (!result.wasSuccessful())
      fail("Tests failed.");
  }

  public void testMergeA () {
    Projector pa = new Projector(0, 1);
    Projector pb = new Projector(0, 1);
    pa.merge(pb);

    Projector expected = new Projector(0,1);
    assertVeryClose(expected,pa);
  }
  public void testMergeB () {
    Projector pa = new Projector(0, 1);
    Projector pb = new Projector(1, 0);
    pa.merge(pb);

    Projector expected = new Projector(0,1);
    assertVeryClose(expected,pa);
  }
  public void testMergeC () {
    Projector pa = new Projector(1, 0);
    Projector pb = new Projector(0, 1);
    pa.merge(pb);

    Projector expected = new Projector(1,0);
    assertVeryClose(expected,pa);
  }
  public void testMergeD () {
    Projector pa = new Projector(1, 0);
    Projector pb = new Projector(1, 0);
    pa.merge(pb);

    Projector expected = new Projector(1,0);
    assertVeryClose(expected,pa);
  }
  public void testMergeE () {
    Projector pa = new Projector(10,  0);
    Projector pb = new Projector( 1, 11);
    pa.merge(pb);

    Projector expected = new Projector(11,0);
    assertVeryClose(expected, pa);
  }
  public void testMergeF () {
    Projector pa = new Projector(10,  5);
    Projector pb = new Projector( 1, 11);
    pa.merge(pb);

    Projector expected = new Projector(11,1);
    assertVeryClose(expected, pa);
  }
  public void testMergeG () {
    Projector pa = new Projector( 1, 11);
    Projector pb = new Projector(10,  0);
    pa.merge(pb);

    Projector expected = new Projector(0,11);
    assertVeryClose(expected, pa);
  }
  public void testMergeH () {
    Projector pa = new Projector( 1.5, 1.4);
    Projector pb = new Projector( 1, 2);
    pa.merge(pb);

    Projector expected = new Projector(2,1);
    assertVeryClose(expected, pa);
  }

  public void testMerge1 () {
    Projector pa = new Projector(10, 20, 0.1, 0.8);
    Projector pb = new Projector(10, 20, 0.0, 1.0);
    pa.merge(pb);

    Projector expected = new Projector(10, 20, 0.1, 0.8);
    assertVeryClose(expected,pa);
  }
  public void testMerge1r () {
    Projector pa = new Projector(10, 20, 0.0, 1.0);
    Projector pb = new Projector(10, 20, 0.1, 0.8);
    pa.merge(pb);

    Projector expected = new Projector(10, 20, 0.1, 0.8);
    assertVeryClose(expected,pa);
  }
  public void testMerge2 () {
    Projector pa = new Projector(10, 20, 0.1, 0.8);
    Projector pb = new Projector(20, 10, 0.0, 1.0);
    pa.merge(pb);

    Projector expected = new Projector(10, 20, 0.1, 0.8);
    assertVeryClose(expected,pa);
  }
  public void testMerge2r () {
    Projector pa = new Projector(20, 10, 0.0, 1.0);
    Projector pb = new Projector(10, 20, 0.1, 0.8);
    pa.merge(pb);

    Projector expected = new Projector(20, 10, 0.2, 0.9);
    assertVeryClose(expected,pa);
  }
  public void testMerge3 () {
    Projector pa = new Projector(10, 20, 0.0, 1.0);
    Projector pb = new Projector(20, 10, 0.1, 0.8);
    pa.merge(pb);

    Projector expected = new Projector(10, 20, 0.2, 0.9);
    assertVeryClose(expected,pa);
  }
  public void testMerge3r () {
    Projector pa = new Projector(20, 10, 0.1, 0.8);
    Projector pb = new Projector(10, 20, 0.0, 1.0);
    pa.merge(pb);

    Projector expected = new Projector(20, 10, 0.1, 0.8);
    assertVeryClose(expected,pa);
  }

  public void testAsserting () {
    try { assert false; fail("Assertions not enabled!"); }
    catch (AssertionError ex) { } // Good.
  }

  private static void assertVeryClose (Projector expected, Projector actual) {
    boolean success = true;
    success &= Math.abs(expected.u0()-actual.u0()) <= eps;
    success &= Math.abs(expected.u1()-actual.u1()) <= eps;
    success &= Math.abs(expected.v0()-actual.v0()) <= eps;
    success &= Math.abs(expected.v1()-actual.v1()) <= eps;
    if (!success)
      fail("Expected: <"+expected+"> but was:<"+actual+">");
  }
  
  private static void assertVeryClose (double expected, double actual) {
	    boolean success = true;
	    success &= Math.abs(expected-actual) <= eps;
	    if (!success)
	      fail("Expected: <"+expected+"> but was:<"+actual+">");
	  }
  
  
  // The same set of tests as above, except this time for a LOG scale projector
  // with the addition of a functional test of log projection
  
  public void testProjectionLog () {
	  Projector p = new Projector(0.1, 100, 0.0, 1.0, Scale.LOG);
	  assertVeryClose(0.1, p.v(p.u(0.1)));
	  assertVeryClose(2, p.v(p.u(2)));
	  assertVeryClose(56.7785, p.v(p.u(56.7785)));
	  assertVeryClose(0.0, p.u(p.v(0.0)));
	  assertVeryClose(0.25, p.u(p.v(0.25)));
	  assertVeryClose(0.6173, p.u(p.v(0.6173)));
  }
  
  public void testMergeALog () {
    Projector pa = new Projector(0, 1, Scale.LOG);
    Projector pb = new Projector(0, 1, Scale.LOG);
    pa.merge(pb);
    Projector expected = new Projector(0,1, Scale.LOG);
    assertVeryClose(expected,pa);
  }
  public void testMergeBLog () {
    Projector pa = new Projector(0, 1, Scale.LOG);
    Projector pb = new Projector(1, 0, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(0,1, Scale.LOG);
    assertVeryClose(expected,pa);
  }
  public void testMergeCLog () {
    Projector pa = new Projector(1, 0, Scale.LOG);
    Projector pb = new Projector(0, 1, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(1,0, Scale.LOG);
    assertVeryClose(expected,pa);
  }
  public void testMergeDLog () {
    Projector pa = new Projector(1, 0, Scale.LOG);
    Projector pb = new Projector(1, 0, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(1,0, Scale.LOG);
    assertVeryClose(expected,pa);
  }
  public void testMergeELog () {
    Projector pa = new Projector(10,  0, Scale.LOG);
    Projector pb = new Projector( 1, 11, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(11,0, Scale.LOG);
    assertVeryClose(expected, pa);
  }
  public void testMergeFLog () {
    Projector pa = new Projector(10,  5, Scale.LOG);
    Projector pb = new Projector( 1, 11, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(11,1, Scale.LOG);
    assertVeryClose(expected, pa);
  }
  public void testMergeGLog () {
    Projector pa = new Projector( 1, 11, Scale.LOG);
    Projector pb = new Projector(10,  0, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(0,11, Scale.LOG);
    assertVeryClose(expected, pa);
  }
  public void testMergeHLog () {
    Projector pa = new Projector( 1.5, 1.4, Scale.LOG);
    Projector pb = new Projector( 1, 2, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(2,1, Scale.LOG);
    assertVeryClose(expected, pa);
  }

  public void testMerge1Log () {
    Projector pa = new Projector(10, 20, 0.1, 0.8, Scale.LOG);
    Projector pb = new Projector(10, 20, 0.0, 1.0, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(10, 20, 0.1, 0.8, Scale.LOG);
    assertVeryClose(expected,pa);
  }
  public void testMerge1rLog () {
    Projector pa = new Projector(10, 20, 0.0, 1.0, Scale.LOG);
    Projector pb = new Projector(10, 20, 0.1, 0.8, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(10, 20, 0.1, 0.8, Scale.LOG);
    assertVeryClose(expected,pa);
  }
  public void testMerge2Log () {
    Projector pa = new Projector(10, 20, 0.1, 0.8, Scale.LOG);
    Projector pb = new Projector(20, 10, 0.0, 1.0, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(10, 20, 0.1, 0.8, Scale.LOG);
    assertVeryClose(expected,pa);
  }
  public void testMerge2rLog () {
    Projector pa = new Projector(20, 10, 0.0, 1.0, Scale.LOG);
    Projector pb = new Projector(10, 20, 0.1, 0.8, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(20, 10, 0.2, 0.9, Scale.LOG);
    assertVeryClose(expected,pa);
  }
  public void testMerge3Log () {
    Projector pa = new Projector(10, 20, 0.0, 1.0, Scale.LOG);
    Projector pb = new Projector(20, 10, 0.1, 0.8, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(10, 20, 0.2, 0.9, Scale.LOG);
    assertVeryClose(expected,pa);
  }
  public void testMerge3rLog () {
    Projector pa = new Projector(20, 10, 0.1, 0.8, Scale.LOG);
    Projector pb = new Projector(10, 20, 0.0, 1.0, Scale.LOG);
    pa.merge(pb);

    Projector expected = new Projector(20, 10, 0.1, 0.8, Scale.LOG);
    assertVeryClose(expected,pa);
  }
}
