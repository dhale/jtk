package edu.mines.jves.util.test;

import junit.framework.*;

/**
 * Tests all classes package edu.mines.jves.util.
 * @author Dave Hale
 * @version 2004.11.02
 */
public class Suite extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(StopwatchTest.class);
    junit.textui.TestRunner.run(suite);
  }
}
