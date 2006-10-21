/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import static edu.mines.jtk.util.Localize.timeWords;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Report progress to default Logger
    @author W.S. Harlan
 */
public class LogMonitor implements Monitor {
  private Logger _log = null;
  private String _prefix = "";
  private volatile long _startTime = 0;
  private volatile long _lastTime = 0;
  private volatile long _currentTime = 0;
  private volatile double _initFraction = 0;
  private volatile double _lastFraction = 0;
  private Thread _thread = null;

  /** Line separator */
  private static final String NL = System.getProperty("line.separator");

  /** Print no more than this often, in milliseconds */
  private static final long SHORTEST_INTERVAL = 10000L;
  /** Wait no longer than this for first report, after minor progress, in ms */
  private static final long LONGEST_FIRST_INTERVAL = 60*1000L;
  /** Print at least this often, in ms */
  private static final long LONGEST_INTERVAL = 15*60*1000L;

  private static final Logger LOG
    = Logger.getLogger(LogMonitor.class.getName(),
                       LogMonitor.class.getName());
  private boolean _debug = LOG.isLoggable(Level.FINE);

  /** Progress will be reported to this Logger.
      @param prefix Prefix this string to every report.
      @param logger Send to this Logger.  If null,
      then check arguments but do nothing.
  */
  public LogMonitor(String prefix, Logger logger) {
    _log = logger;
    if (prefix != null)
      _prefix = prefix;
  }

  // Monitor
  public void initReport(double initFraction) {
    if (_initFraction != 0 || initFraction < _initFraction) {
      throw new IllegalStateException
        ("initReport is being called twice, or with a bad value: "+
         "new value of "+initFraction+" cannot replace previous value of "+
         _initFraction);

    }
    _initFraction = Math.min(0.99999, initFraction); // avoid divides by zero
    report(initFraction); // start the clock
  }

  // Monitor
  public void report(double fraction) {
    // convert internally to fraction from 0 to 1
    fraction = (fraction - _initFraction)/(1. - _initFraction);
    synchronized (this) {
      if (fraction < _lastFraction-0.0001) {
        IllegalStateException ex = new IllegalStateException
          ("Progress cannot decrease from "+_lastFraction+" to "+fraction);
        if (_debug) { // Only make this fatal in test environment.
          throw ex;
        }
        ex.printStackTrace();
        _lastFraction = fraction;
        return;
      }

      // just starting
      if (_startTime == 0) {
        _currentTime = System.currentTimeMillis();
        _startTime = _currentTime;
        if (fraction < 1) {
          _thread = new UpdateTimeThread();
          _thread.setDaemon(true);
          _thread.start();
        }
        print(fraction, _currentTime);
        return;
      }

      // finished
      if (fraction >= 1) {
        // just finished
        if (fraction > _lastFraction) {
          _currentTime = System.currentTimeMillis();
          print(fraction, _currentTime);
        }
        if (_thread != null) {
          _thread.interrupt();
          _thread = null;
        }
        return;
      }

      // decide whether to print or not
      boolean print = false;
      if (_currentTime > _lastTime + LONGEST_INTERVAL) {
        _currentTime = System.currentTimeMillis();
        // Too long since last update
        print = true;
      } else {
        boolean significantProgress =
          (fraction > _lastFraction + 0.02) ||
          (fraction > _lastFraction+ 0.01 && _lastFraction <= 0.02);
        boolean firstProgress =
          _lastFraction == 0 && fraction > _lastFraction+ 0.001;
        if (significantProgress || firstProgress) {
          // expensive query of time
          _currentTime = System.currentTimeMillis();
          long interval =
            (significantProgress) ? SHORTEST_INTERVAL: LONGEST_FIRST_INTERVAL;
          if (_currentTime >= _lastTime + interval) {
            print = true;
          }
        }
      }

      // decided to print
      if (print) {
        print(fraction, _currentTime);
      }
    }
  }

  /** Let user know about progress.
      Update time stamps to limit frequency of next update.
      @param fraction Current fraction of total progress, between 0 and 1.
      @param currentTime Current time in milliseconds.
  */
  private void print(double fraction, long currentTime) {
    synchronized (this) {
      if (_log != null) {
        _log.info(_prefix+getProgressReport
                  (_startTime, currentTime, fraction, _initFraction));
      }
      _lastFraction = fraction;
      _lastTime = currentTime;
    }
  }

  /** Get a user-viewable String describing the progress
      and completion time.
      @param startTime Time in milliseconds when work began.
      @param currentTime Current time in milliseconds.
      @param fraction Current fraction of total progress, between 0 and 1.
      @param initFraction Initial fraction of total progress, between 0 and 1,
      when work started.
      @return Progress report as a string.
  */
  public static String getProgressReport
    (long startTime, long currentTime, double fraction, double initFraction) {

    String progress = "";
    long secSoFar = ((currentTime - startTime)/1000);
    if (secSoFar > 0) {
      progress = timeWords(secSoFar)+" ${so_far}";
      long secRemaining = (fraction > 0)
        ? (long) ((1./fraction -1.)*secSoFar)
        : 0;
      if (secRemaining > 0) {
        String remaining = timeWords(secRemaining) + " ${remaining}";
        if (progress.length() > 0) {
          progress = remaining+", "+progress;
        } else {
          progress = remaining;
        }
      }
      long total = secSoFar + secRemaining;
      if (progress.length() > 0) {
        progress = NL+"  "+progress+", "+timeWords(total)+" ${total}";
      }
      if (fraction >= 1.) {
        progress = NL+"  ${Finished_in} "+timeWords(total)+" ${total}";
      }
    }
    // Use full fraction for user's benefit
    int percent =
      (int)(100.*(initFraction + fraction*(1.-initFraction))+0.49);
    String message = " ${progress}: "+ percent+
      "% ${complete_at} "+(new Date())+progress;
    message = Localize.filter(message, LogMonitor.class);
    return message;
  }

  /** Update current time at least one in a while */
  private class UpdateTimeThread extends Thread {
    /** Constructor. */
    public UpdateTimeThread() {
      super ("LogMonitor.UpdateTimeThread "+new Date());
    }
    @Override public void run() {
      try {
        while (_lastFraction < 0.9999) {
          Thread.sleep(LONGEST_INTERVAL/4);
          _currentTime = System.currentTimeMillis();
          if (_currentTime > _lastTime + 2*LONGEST_INTERVAL) {
            // No real progress since last update, too long ago.
            print(_lastFraction, _currentTime);
          }
        }
      } catch (InterruptedException e) {
        return;
      }
    }
  }

  /** run tests
      @param argv command line
      @throws Exception test failures
  */
  public static void main(String[] argv) throws Exception {
    CleanHandler.setDefaultHandler();

    Monitor monitor = new LogMonitor("${Test}",LOG);
    int n=25;
    int pause = 25;
    monitor.report(0);
    Thread.sleep(pause);
    monitor.report(0);
    for (int i=0; i<n; ++i) {
      monitor.report(i/(n-1.));
      Thread.sleep(pause);
    }
  }
}

