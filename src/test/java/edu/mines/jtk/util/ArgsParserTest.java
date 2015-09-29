/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
package edu.mines.jtk.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.util.ArgsParser}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2001.02.05, 2006.07.12
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
    for (String[] arg:args) {
      float a = 0.0f;
      boolean b = false;
      try {
        String shortOpts = "ha:b";
        String[] longOpts = {"help","alpha=","beta"};
        ArgsParser ap = new ArgsParser(arg,shortOpts,longOpts);
        String[] opts = ap.getOptions();
        String[] vals = ap.getValues();
        for (int i=0; i<opts.length; ++i) {
          String opt = opts[i];
          String val = vals[i];
          if (opt.equals("-h") || opt.equals("--help")) {
            assertTrue(false);
          } else if (opt.equals("-a") || opt.equals("--alpha")) {
            a = ArgsParser.toFloat(val);
          } else if (opt.equals("-b") || opt.equals("--beta")) {
            b = true;
          }
        }
        String[] otherArgs = ap.getOtherArgs();
        assertTrue(otherArgs.length==1);
        assertTrue(otherArgs[0].equals("foo"));
        assertTrue(a==3.14f);
        assertTrue(b);
      } catch (ArgsParser.OptionException e) {
        assertTrue("no exceptions: e="+e.getMessage(),false);
      }
    }
  }
}
