/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.util;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.mines.jtk.util.Localize.timeWords;

/**
 * Report progress to default Logger
 *
 * @author W.S. Harlan
 */
public class LogMonitor implements Monitor {
    private Logger _log = null;
    private String _prefix = "";
    private volatile long _startTime = 0;
    private volatile long _lastTime = 0;
    private volatile long _currentTime = 0;
    private volatile double _initFraction = 0;
    private volatile double _lastFraction = 0;
    private volatile boolean _canceled = false;
    private Thread _thread = null;

    /**
     * Line separator
     */
    private static final String NL = System.getProperty("line.separator");

    /**
     * Print no more than this often, in milliseconds
     */
    private static final long SHORTEST_INTERVAL = 10000L;
    /**
     * Wait no longer than this for first report, after minor progress, in ms
     */
    private static final long LONGEST_FIRST_INTERVAL = 60 * 1000L;
    /**
     * Print at least this often, in ms
     */
    private static final long LONGEST_INTERVAL = 15L * 60 * 1000L;

    private static final Logger LOG
            = Logger.getLogger(LogMonitor.class.getName(),
            LogMonitor.class.getName());
    private final boolean _debug = LOG.isLoggable(Level.FINE);

    /**
     * Progress will be reported to this Logger.
     *
     * @param prefix Prefix this string to every report.
     * @param logger Send to this Logger.  If null,
     *               then check arguments but do nothing.
     */
    public LogMonitor(final String prefix, final Logger logger) {
        _log = logger;
        if (prefix != null) {
            _prefix = prefix;
        }
    }

    // Monitor
    @Override
    public void initReport(final double initFraction) {
        if (_initFraction != 0 || initFraction < _initFraction) {
            throw new IllegalStateException
                    ("initReport is being called twice, or with a bad value: " +
                            "new value of " + initFraction + " cannot replace previous value of " +
                            _initFraction);

        }
        _initFraction = Math.min(0.99999, initFraction); // avoid divides by zero
        report(initFraction); // start the clock
    }

    // Monitor
    @Override
    public void report(double fraction) {
        // convert internally to fraction from 0 to 1
        fraction = (fraction - _initFraction) / (1.0 - _initFraction);
        synchronized (this) {
            if (fraction < _lastFraction - 0.0001) {
                final IllegalStateException ex = new IllegalStateException
                        ("Progress cannot decrease from " + _lastFraction + " to " + fraction);
                if (_debug) { // Only make this fatal in test environment.
                    throw ex;
                }
                LOG.log(Level.WARNING, "", ex);
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
                final boolean significantProgress =
                        (fraction > _lastFraction + 0.02) ||
                                (fraction > _lastFraction + 0.01 && _lastFraction <= 0.02);
                final boolean firstProgress =
                        _lastFraction == 0 && fraction > _lastFraction + 0.001;
                if (significantProgress || firstProgress) {
                    // expensive query of time
                    _currentTime = System.currentTimeMillis();
                    final long interval =
                            (significantProgress) ? SHORTEST_INTERVAL : LONGEST_FIRST_INTERVAL;
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

    @Override
    public boolean isCanceled() {
        return _canceled;
    }

    /**
     * Interrupt any further work.  Causes isCancelled() to return true.
     */
    public void cancel() {
        _canceled = true;
    }

    /**
     * Let user know about progress.
     * Update time stamps to limit frequency of next update.
     *
     * @param fraction    Current fraction of total progress, between 0 and 1.
     * @param currentTime Current time in milliseconds.
     */
    private void print(final double fraction, final long currentTime) {
        synchronized (this) {
            if (_log != null) {
                _log.info(_prefix + getProgressReport
                        (_startTime, currentTime, fraction, _initFraction));
            }
            _lastFraction = fraction;
            _lastTime = currentTime;
        }
    }

    /**
     * Get a user-viewable String describing the progress
     * and completion time.
     *
     * @param startTime    Time in milliseconds when work began.
     * @param currentTime  Current time in milliseconds.
     * @param fraction     Current fraction of total progress, between 0 and 1.
     * @param initFraction Initial fraction of total progress, between 0 and 1,
     *                     when work started.
     * @return Progress report as a string.
     */
    @SuppressWarnings("deprecation")
    public static String getProgressReport
    (final long startTime, final long currentTime, final double fraction, final double initFraction) {

        String progress = "";
        final long secSoFar = ((currentTime - startTime) / 1000);
        if (secSoFar > 0) {
            progress = timeWords(secSoFar) + " ${so_far}";
            final long secRemaining = (fraction > 0)
                    ? (long) ((1.0 / fraction - 1.0) * secSoFar)
                    : 0;
            if (secRemaining > 0) {
                final String remaining = timeWords(secRemaining) + " ${remaining}";
                if (progress.length() > 0) {
                    progress = remaining + ", " + progress;
                } else {
                    progress = remaining;
                }
            }
            final long total = secSoFar + secRemaining;
            if (progress.length() > 0) {
                progress = NL + "  " + progress + ", " + timeWords(total) + " ${total}";
            }
            if (fraction >= 1.0) {
                progress = NL + "  ${Finished_in} " + timeWords(total) + " ${total}";
            }
        }
        // Use full fraction for user's benefit
        final int percent =
                (int) (100.0 * (initFraction + fraction * (1.0 - initFraction)) + 0.49);
        String message = " ${progress}: " + percent +
                "% ${complete_at} " + (new Date()) + progress;
        message = Localize.filter(message, LogMonitor.class);
        return message;
    }

    /**
     * Update current time at least one in a while
     */
    private class UpdateTimeThread extends Thread {
        /**
         * Constructor.
         */
        private UpdateTimeThread() {
            super("LogMonitor.UpdateTimeThread " + new Date());
        }

        @Override
        public void run() {
            try {
                while (_lastFraction < 0.9999) {
                    Thread.sleep(LONGEST_INTERVAL / 4);
                    _currentTime = System.currentTimeMillis();
                    if (_currentTime > _lastTime + 2 * LONGEST_INTERVAL) {
                        // No real progress since last update, too long ago.
                        print(_lastFraction, _currentTime);
                    }
                }
            } catch (InterruptedException e) {
                // finished and return
            }
        }
    }

    /**
     * run tests
     *
     * @param argv command line
     * @throws Exception test failures
     */
    public static void main(final String[] argv) throws Exception {
        CleanHandler.setDefaultHandler();

        final Monitor monitor = new LogMonitor("${Test}", LOG);
        monitor.report(0);
        final int pause = 25;
        Thread.sleep(pause);
        monitor.report(0);
        final int n = 25;
        for (int i = 0; i < n; ++i) {
            monitor.report(i / (n - 1.0));
            Thread.sleep(pause);
        }
    }
}

