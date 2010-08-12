/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * A timer that works like a stopwatch. A stopwatch can be started and
 * stopped. A stopwatch is either running or not running. Stopwatch time
 * can be read in either state, and that time can be reset to zero.
 * <p>
 * Stopwatch time is elapsed (wall-clock) time, not process or CPU time. 
 * Stopwatch time resolution is one nanosecond. In other words, a 
 * stopwatch cannot measure a time difference less than one nanosecond.
 * On the other hand, the granularity of the stopwatch depends on the
 * underlying operating system and may be larger.
 * @see System#nanoTime()
 * @author Dave Hale, Colorado School of Mines
 * @author Dean Witte, Transform Software
 * @version 2004.11.02
 */
public class Stopwatch {

  /**
   * Starts this stopwatch. If the stopwatch is running, does nothing.
   */
  public void start() {
    if (!_running) {
      _running = true;
      _start = System.nanoTime();
    }
  }

  /**
   * Stops this stopwatch. If the stopwatch is not running, does nothing.
   */
  public void stop() {
    if (_running) {
      _time += System.nanoTime()-_start;
      _running = false;
    }
  }

  /**
   * Stops this stopwatch and resets the time to zero.
   */
  public void reset() {
    stop();
    _time = 0;
  }

  /**
   * Resets and starts this stopwatch.
   */
  public void restart() {
    reset();
    start();
  }

  /**
   * Returns the stopwatch time, in seconds. Does not start or stop.
   * @return the time, in seconds.
   */
  public double time() {
    if (_running) {
      return 1.0e-9*(_time+System.nanoTime()-_start);
    } else {
      return 1.0e-9*_time;
    }
  }

  private boolean _running;
  private long _start;
  private long _time;
}
