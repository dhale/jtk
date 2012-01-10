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
}
