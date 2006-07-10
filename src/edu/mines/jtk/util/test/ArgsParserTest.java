package edu.mines.jtk.util.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.ArgsParser;

/**
 * Tests {@link edu.mines.jtk.util.ArgsParser}.
 * @author Dave Hale
 * @version 2001.02.05
 */
public class ArgsParserTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ArgsParserTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testArgsParser() {
    String[][] args = {
      {"-a3.14","-b","foo"},
      {"-a","3.14","-b","foo"},
      {"--alpha","3.14","--beta","foo"},
      {"--a=3.14","--b","foo"},
    };
    for (int iarg=0; iarg<args.length; ++iarg) {
      float a = 0.0f;
      boolean b = false;
      String c = null;
      try {
        String shortOpts = "ha:b";
        String[] longOpts = {"help","alpha=","beta"};
        ArgsParser ap = new ArgsParser(args[iarg],shortOpts,longOpts);
        String[] opts = ap.getOptions();
        String[] vals = ap.getValues();
        for (int i=0; i<opts.length; ++i) {
          String opt = opts[i];
          String val = vals[i];
          if (opt.equals("-h") || opt.equals("--help")) {
            assertTrue(false);
          } else if (opt.equals("-a") || opt.equals("--alpha")) {
            a = ap.toFloat(val);
          } else if (opt.equals("-b") || opt.equals("--beta")) {
            b = true;
          }
        }
        String[] otherArgs = ap.getOtherArgs();
        assertTrue(otherArgs.length==1);
        assertTrue(otherArgs[0].equals("foo"));
        assertTrue(a==3.14f);
        assertTrue(b==true);
      } catch (ArgsParser.OptionException e) {
        assertTrue("no exceptions: e="+e.getMessage(),false);
      }
    }
  }
}
