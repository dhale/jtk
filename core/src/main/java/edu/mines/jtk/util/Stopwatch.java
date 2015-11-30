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
