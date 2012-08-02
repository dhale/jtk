/****************************************************************************
  Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
  This program and accompanying materials are made available under the terms of
  the Common Public License - v1.0, which accompanies this distribution, and is
  available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import static edu.mines.jtk.util.Localize.timeWords;

/** Wrap edu.mines.jtk.util.Localize for junit testing.
  (junit.jar must be in CLASSPATH)
 */
public class LocalizeTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(LocalizeTest.class.getName());

    /**
     * Run some test code. Any "public void test*()" method starting with "test" will be used
     *
     */
    public static void testLocalize() {
        { // This is the normal way to call it. Gets LocalizeTest.properties, unless we are in France.
            final Localize dfault = new Localize(LocalizeTest.class);
            final String sDefault = dfault.format("msg1", 3.14, 42);
            assert "A number 3.14000 here, and another #42".equals(sDefault) : sDefault;
        }
        { // for testing, we specify a specific locale.  Gets LocalizeTest_fr.properties.
            final Localize fr = new Localize(LocalizeTest.class, null, Locale.FRENCH);
            final int i = 42;
            final String sFr = fr.format("msg1", 3.14, i);
            assert "Un nombre 3,14000 ici, et un autre #42".equals(sFr) ||
                   "Un nombre 3.14000 ici, et un autre #42".equals(sFr) : sFr;
        }
        { // for testing, we specify an alternate file LocalizeTestAlt[_*].properties
            final Localize alt = new Localize(LocalizeTest.class, "LocalizeTestAlt");
            final String s = alt.format("msg1", 3.14, 42);
            assert "A custom file with number 3.14000, and another #42".equals(s) : s;
        }
        { // Specifying format instead of key for format.
            final Localize alt = new Localize(LocalizeTest.class, "LocalizeTestAlt");
            final String s = alt.format("No property just a format with number %g.", 3.14);
            assert "No property just a format with number 3.14000.".equals(s) : s;
        }
        { // Specifying non-existent resource file.
            final Localize alt = new Localize(LocalizeTest.class, "DoesNotExist");
            final String s = alt.format("A number %g.", 3.14);
            assert "A number 3.14000.".equals(s) : s;
        }
        { // Specifying non-existent resource file.
            final Localize alt = new Localize(LocalizeTest.class);
            final String s = alt.format("Ignored number.", 3.14);
            assert "Ignored number.".equals(s);
        }
    }

    /**
     * tests.
     */
    public static void testLocale() {
        assert !Locale.getDefault().equals(Locale.FRANCE)
                : "The testLocalize unit test does not work in France";
    }

    /**
     * tests.
     */
    public static void testLocalizeThrowable() {
        final IOException ioException = new IOException("ioe");
        assert "ioe".equals(Localize.getMessage(ioException));
        {
            final IllegalArgumentException e = new IllegalArgumentException(ioException);
            assert "ioe".equals(Localize.getMessage(e));
        }
        {
            final String better = "Bad argument: " + ioException.getLocalizedMessage();
            final IllegalArgumentException e = new IllegalArgumentException(better, ioException);
            assert better.equals(Localize.getMessage(e)): Localize.getMessage(e);
        }
        {
            final IllegalArgumentException e = new IllegalArgumentException(null,ioException);
            assert "ioe".equals(Localize.getMessage(e));
        }
        {
            final IllegalArgumentException e = new IllegalArgumentException("foo",ioException);
            assert "foo".equals(Localize.getMessage(e));
        }
        {
            final IllegalArgumentException e = new IllegalArgumentException("foo",null);
            assert "foo".equals(Localize.getMessage(e));
        }
        {
            final IllegalArgumentException e = new IllegalArgumentException();
            assert "java.lang.IllegalArgumentException".equals(Localize.getMessage(e)) :Localize.getMessage(e);
        }
        {
            final IllegalArgumentException e = new IllegalArgumentException(null,null);
            assert "java.lang.IllegalArgumentException".equals(Localize.getMessage(e)) :Localize.getMessage(e);
        }
    }

    /** Junit test code
      @throws Exception any test failure
     */
    public static void testLocalizeOld() throws Exception {
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
