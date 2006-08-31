package edu.mines.jtk.opt.test;

import static edu.mines.jtk.opt.StringUtil.NL;
import static edu.mines.jtk.opt.StringUtil.getLongTimeStamp;
import static edu.mines.jtk.opt.StringUtil.getTimeStamp;
import static edu.mines.jtk.opt.StringUtil.parseLongTimeStamp;
import static edu.mines.jtk.opt.StringUtil.parseTimeStamp;
import static edu.mines.jtk.opt.StringUtil.prependToLines;
import static edu.mines.jtk.opt.StringUtil.timeWords;

import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Wrap edu.mines.jtk.opt.StringUtil for junit testing.
   (junit.jar must be in CLASSPATH)
*/
public class StringUtilTest extends TestCase {

  /** Junit test code
     @throws Exception any test failure
   */
  public void testAll() throws Exception {
    String lines = prependToLines("a","bbb"+NL+"ccc");
    assert (lines.equals("abbb"+NL+"accc"));
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

    {
      String stamp = getLongTimeStamp();
      Date date = parseLongTimeStamp(stamp);
      assert getLongTimeStamp(date).equals(getLongTimeStamp(date.getTime()));
      assert date.equals(parseLongTimeStamp(getLongTimeStamp(date)));
    }
    {
      String stamp = getTimeStamp();
      Date date = parseTimeStamp(stamp);
      assert getTimeStamp(date).equals(getTimeStamp(date.getTime()));
      assert date.equals(parseTimeStamp(getTimeStamp(date)));
    }
  }

  // OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL OPTIONAL

  /* Initialize objects used by all test methods */
  @Override protected void setUp() throws Exception { super.setUp();}

  /* Destruction of stuff used by all tests: rarely necessary */
  @Override protected void tearDown() throws Exception { super.tearDown();}

  // NO NEED TO CHANGE THE FOLLOWING

  /** Standard constructor calls TestCase(name) constructor */
  public StringUtilTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(StringUtilTest.class);
  }

  /** Run all tests with text gui if this class main is invoked */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
