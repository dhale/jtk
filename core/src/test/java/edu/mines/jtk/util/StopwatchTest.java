/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
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
 * Tests {@link edu.mines.jtk.util.Stopwatch}.
 * @author Dave Hale and Dean Witte
 * @version 2004.11.02
 */
public class StopwatchTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(StopwatchTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test() {
    double sleepTime = 0.1;  // time to sleep in seconds
    double smallTime = 0.01; // time errors less than this are ignored
    Stopwatch sw = new Stopwatch();

    // Checks inside here must be ok at least once, or this test fails.
    boolean ok = true;
    for (int itrial=0; itrial<100; ++itrial) {
      sw.reset();
      ok = true;

      // Start and check time (while running).
      sw.start();
      sleep(sleepTime);
      ok = ok && sleepTime-smallTime<sw.time();
      ok = ok && sw.time()<sleepTime+smallTime;

      // Stop and check time.
      sw.stop();
      ok = ok && sleepTime-smallTime<sw.time();
      ok = ok && sw.time()<sleepTime+smallTime;

      // Wait, start, stop, and check accumulated time.
      sleep(sleepTime);
      sw.start();
      sleep(sleepTime);
      sw.stop();
      ok = ok && 2.0*(sleepTime-smallTime)<sw.time();
      ok = ok && sw.time()<2.0*(sleepTime+smallTime);

      // Restart, stop, and check time.
      sw.restart();
      sleep(sleepTime);
      sw.stop();
      ok = ok && sleepTime-smallTime<sw.time();
      ok = ok && sw.time()<sleepTime+smallTime;

      // If all checks ok, then done.
      if (ok)
        break;
    }
    assertTrue(ok);
  }

  private static void sleep(double time) {
    try {
      Thread.sleep((long)(time*1000.0));
    } catch (InterruptedException ie) {
      assertTrue("no exception",false);
    }
  }
}
