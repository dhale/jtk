/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util.test;

import junit.framework.*;
import edu.mines.jtk.util.Stopwatch;

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
    double sleepTime = 0.1;
    double extraTime = 0.5;
    Stopwatch sw = new Stopwatch();

    // Start and check time (while running).
    sw.start();
    sleep(sleepTime);
    assertTrue(sleepTime<=sw.time() && sw.time()<sleepTime+extraTime);

    // Stop and check time.
    sw.stop();
    assertTrue(sleepTime<=sw.time() && sw.time()<sleepTime+extraTime);

    // Wait, start, stop, and check accumulated time.
    sleep(sleepTime);
    sw.start();
    sleep(sleepTime);
    sw.stop();
    assertTrue(2.0*sleepTime<=sw.time() && sw.time()<2.0*(sleepTime+extraTime));

    // Restart, stop, and check time.
    sw.restart();
    sleep(sleepTime);
    sw.stop();
    assertTrue(sleepTime<=sw.time() && sw.time()<sleepTime+extraTime);
  }

  private static void sleep(double time) {
    try {
      Thread.sleep((long)(time*1000.0));
    } catch (InterruptedException ie) {
      assertTrue("no exception",false);
    }
  }
}
