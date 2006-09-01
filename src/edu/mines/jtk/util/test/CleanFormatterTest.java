package edu.mines.jtk.util.test;

import junit.framework.*;
import java.util.logging.*;
import edu.mines.jtk.util.CleanHandler;
import edu.mines.jtk.util.CleanFormatter;

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
      assert s[i].endsWith(messages[i]+"\n");
      logger.fine("|"+s[i]+"|");
    }
    assert s[0].equals("one\n"): s[0];
    assert s[1].equals("WARNING: two\n") : s[1];
    assert s[2].matches("^\\*\\*\\*\\* SEVERE WARNING \\*\\*\\*\\* "+
                        "\\(Class.method \\d+-\\d+ #.*\\)\n"+
                        "SEVERE: three\n$") :s[2];
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

  /** Standard constructor calls TestCase(name) constructor */
  public CleanFormatterTest(String name) {super (name);}

  /** This automatically generates a suite of all "test" methods */
  public static junit.framework.Test suite() {
    try {assert false; throw new IllegalStateException("need -ea");}
    catch (AssertionError e) {}
    return new TestSuite(CleanFormatterTest.class);
  }

  /** Run all tests with text gui if this class main is invoked */
  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
