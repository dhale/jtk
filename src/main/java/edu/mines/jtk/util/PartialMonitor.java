/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.util;

/**
 * Wrap an existing Monitor with a partial range.
 * Note that only makes sense to call initReport()
 * with the first wrapper used.
 *
 * @author W.S. Harlan
 */
public class PartialMonitor implements Monitor {
    private Monitor _wrapped = null;
    private double _begin = 0.0;
    private double _end = 1.0;

    /**
     * An existing Monitor will be wrapped for progress in a limited range.
     *
     * @param wrapped The wrapped monitor.
     * @param begin   The first value to be updated to the wrapped monitor,
     *                corresponding to a 0 reported to this monitor.
     * @param end     The last value to be updated to the wrapped monitor
     *                corresponding to a 1 reported to this monitor.
     */
    public PartialMonitor(final Monitor wrapped, final double begin, final double end) {
        _wrapped = wrapped;
        _begin = begin;
        _end = end;
    }

    @Override
    public void report(final double fraction) {
        if (_wrapped == null) {
            return;
        }
        _wrapped.report(fraction * (_end - _begin) + _begin);
    }

    @Override
    public boolean isCanceled() {
        return _wrapped.isCanceled();
    }

    @Override
    public void initReport(final double initFraction) {
        _wrapped.initReport(initFraction * (_end - _begin) + _begin);
    }
}

