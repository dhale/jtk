package edu.mines.jves.util;

/**
 * A timer that works like a stopwatch. A stopwatch can be started and
 * stopped. A stopwatch is either running or not running. Stopwatch time
 * can be read in either state, and that time can be reset to zero.
 * <p>
 * Stopwatch time is elapsed (wall-clock) time, not process or CPU time. 
 * Stopwatch time resolution is one millisecond. In other words, a 
 * stopwatch cannot measure a time difference less than one millisecond.
 * @author Dave Hale and Dean Witte
 * @version 2004.11.02
 */
public class Stopwatch {

  /**
   * Starts this stopwatch. If the stopwatch is running, does nothing.
   */
  public void start() {
    if (!_running) {
      _running = true;
      _start = System.currentTimeMillis();
    }
  }

  /**
   * Stops this stopwatch. If the stopwatch is not running, does nothing.
   */
  public void stop() {
    if (_running) {
      _time += System.currentTimeMillis()-_start;
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
      return (double)(0.001*(_time+System.currentTimeMillis()-_start));
    } else {
      return (double)(0.001*_time);
    }
  }

  private boolean _running;
  private long _start;
  private long _time;
}
