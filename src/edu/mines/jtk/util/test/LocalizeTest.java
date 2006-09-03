/****************************************************************************
Copyright (c) 2004, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util.test;

import static edu.mines.jtk.util.Localize.timeWords;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Wrap edu.mines.jtk.util.Localize for junit testing.
   (junit.jar must be in CLASSPATH)
*/
public class LocalizeTest extends TestCase {

  /** Junit test code
     @throws Exception any test failure
   */
  public void testAll() throws Exception {
    {
      long seconds =(29L + 60*(9));
      String words = timeWords(seconds);
      assert (words.equals("9 minutes 29 seconds")) : words;
    }
    {
      long seconds =(29L + 60*(10));
      String words = timeWords(seconds);
      assert (words.equals("10 minutes")) : words;
    }
    {
      long seconds =(30L + 60*(10));
      String words = timeWords(seconds);
      assert (words.equals("11 minutes")) : words;
    }
    {
      long seconds =(29L + 60*(29 + 60*(9)));
      String words = timeWords(seconds);
      assert (words.equals("9 hours 29 minutes")) : words;
    }
    {
      long seconds =(30L + 60*(30 + 60*(9)));
      String words = timeWords(seconds);
      assert (words.equals("9 hours 31 minutes")) : words;
    }
    {
      long seconds =(30L + 60*(30 + 60*(10)));
      String words = timeWords(seconds);
      assert (words.equals("11 hours")) : words;
    }
    {
      long seconds =(30L + 60*(30 + 60*(11 +24*9)));
      String words = timeWords(seconds);
      assert (words.equals("9 days 12 hours")) : words;
    }
    {
      long seconds =(30L + 60*(30 + 60*(11 +24*10)));
      String words = timeWords(seconds);
      assert (words.equals("10 days")) : words;
    }
    {
      long seconds =(0L + 60*(0 + 60*(12 +24*10)));
      String words = timeWords(seconds);
      assert (words.equals("11 days")) : words;
    }
    {
      // 2 hours.
      long seconds = 3600L * 2;
      String words = timeWords(seconds);
      assert (words.equals("2 hours")) : words;
    }
    {
      // 1 second less than 2 hours.
      long seconds = 3600L * 2 - 1;
      String words = timeWords(seconds);
      assert (words.equals("2 hours")) : words;
    }
    {
      // 2 days.
      long seconds = 3600L * 24 * 2;
      String words = timeWords(seconds);
      assert (words.equals("2 days")) : words;
    }
    {
      // 1 second less than 2 days.
      long seconds = 3600L * 24 * 2 - 1;
      String words = timeWords(seconds);
      assert (words.equals("2 days")) : words;
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
  public LocalizeTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods.
      @return A suite of all junit tests as a Test.
   */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(LocalizeTest.class);
  }

  /** Run all tests with text gui if this class main is invoked
      @param args Command-line arguments.
   */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
