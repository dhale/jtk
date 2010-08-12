/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.util.logging.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Wrap edu.mines.jtk.util.CleanFormatter for junit testing.
   (junit.jar must be in CLASSPATH)
*/
public class CleanFormatterTest extends TestCase {

  /** Line separator */
  private static final String NL = System.getProperty("line.separator");

  /** Unit tests */
  public void testFormatter() {
    CleanHandler.setDefaultHandler();
    Logger logger = Logger.getLogger("edu.mines.jtk.util.CleanFormatter");
    CleanFormatter cf = new CleanFormatter();
    String[] messages = new String[] {"one", "two", "three"};
    Level[] levels = new Level[] {Level.INFO, Level.WARNING, Level.SEVERE};
    String[] s = new String[3];
    for (int i=0; i<messages.length; ++i) {
      LogRecord lr = new LogRecord(levels[i], messages[i]);
      lr.setSourceClassName("Class");
      lr.setSourceMethodName("method");
      s[i] = cf.format(lr);
      assertTrue(s[i].endsWith(messages[i]+NL));
      logger.fine("|"+s[i]+"|");
    }
    assert s[0].equals("one"+NL): s[0];
    assert s[1].equals("WARNING: two"+NL) : s[1];
    assert s[2].matches("^\\*\\*\\*\\* SEVERE WARNING \\*\\*\\*\\* "+
                        "\\(Class.method \\d+-\\d+ #.*\\)"+NL+
                        "SEVERE: three"+NL+"$") :s[2];
  }

  /** Test prependToLines method */
  public void testPrepend() {
    String lines = CleanFormatter.prependToLines("a","bbb"+NL+"ccc");
    assert (lines.equals("abbb"+NL+"accc"));
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
  public CleanFormatterTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods.
      @return A suite of all junit tests as a Test.
   */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(CleanFormatterTest.class);
  }

  /** Run all tests with text gui if this class main is invoked
      @param args Command-line arguments.
   */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
