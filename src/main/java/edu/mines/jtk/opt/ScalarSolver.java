/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;
import edu.mines.jtk.util.Monitor;

import java.util.Arrays;

/**
 * Search a single variable for a value that minimizes a function
 *
 * @author W.S. Harlan
 */
public class ScalarSolver {
    // private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
    private static final double GOLD = 0.5 * (Math.sqrt(5.0) - 1.0); // 0.618034
    private static final Almost s_almost = new Almost();

    private Function _function = null;

    /**
     * Implement a function of one variable to be minimized
     */
    public interface Function {
        /**
         * Return a single value as a function of the argument
         *
         * @param scalar Argument to be optimized within a known range.
         * @return value to be minimized
         */
        double function(double scalar);
    }

    /**
     * Constructor
     *
     * @param function Objective function to be minimized.
     */
    public ScalarSolver(final Function function) {
        _function = function;
    }

    /**
     * Minimize a function of scalar and return the optimum value.
     *
     * @param scalarMin        The minimum value allowed for the argument scalar.
     * @param scalarMax        The maximum value allowed for the argument scalar.
     * @param okError          The unknown error in scalar should be less than this
     *                         fraction of the range: dscalar/(scalarMax-scalarMin) &lt;= okError,
     *                         where dscalar is the error bound on the returned value of scalar.
     * @param okFraction       The error in scalar should be less than
     *                         this fraction of the minimum possible range for scalar:
     *                         dscalar &lt;= okFraction*(scalar-scalarMin),
     *                         where dscalar is the error bound on the returned value of scalar.
     * @param numberIterations The maximum number of iterations if greater
     *                         than 6.  The optimization performs at least 6 iterations --
     *                         the minimum necessary for a genuinely parabolic function.
     *                         I recommend at least 20.
     * @param monitor          For reporting progress.  Ignored if null.
     * @return The optimum value minimizing the function.
     */
    public double solve(final double scalarMin, final double scalarMax,
                        final double okError, final double okFraction,
                        final int numberIterations, Monitor monitor) {
        if (monitor == null) {
            monitor = Monitor.NULL_MONITOR;
        }
        monitor.report(0.0);

        int nter = numberIterations;
        if (nter < 6) {
            nter = 6;
        }
        final double[] xs = {0.0, 0.25, 0.75, 1.0}; // Assume bias toward endpoints
        final double[] fs = new double[4];
        for (int i = 0; i < fs.length; ++i) {
            fs[i] = function(xs[i], scalarMin, scalarMax);
        }
        int iter = 4;

        double xmin;
        double error = 1.0;
        double previousError = 1.0;
        while (true) {
            monitor.report(((double) iter) / nter);
            // Find new minimum point
            final int imin = sort(xs, fs);
            xmin = xs[imin];

            // update error estimate
            final double previousPreviousError = previousError;
            previousError = error;
            if (imin == 0) {
                error = xs[1] - xs[0];
            } else if (imin == 3) {
                error = xs[3] - xs[2];
            } else if (imin == 1 || imin == 2) {
                error = xs[imin + 1] - xs[imin - 1];
            } else {
                assert (false) : ("impossible imin=" + imin);
            }
            final double fraction = Almost.FLOAT.divide(error, xmin, 0.0);
            /*
            LOG.fine("iter="+iter);
            LOG.fine("xs[0]="+xs[0]+" xs[1]="+xs[1]+" xs[2]="+xs[2]+" xs[3]="+xs[3]);
            LOG.fine("fs[0]="+fs[0]+" fs[1]="+fs[1]+" fs[2]="+fs[2]+" fs[3]="+fs[3]);
            LOG.fine("imin="+imin+" xmin="+xmin+" fraction="+fraction);
            LOG.fine(" error="+error+ " previousError="+previousError
                   +" previousPreviousError="+previousPreviousError);
            */

            // Is it time to stop and use the current answer?
            if (iter >= nter || (error < okError && fraction < okFraction) || monitor.isCanceled()) {
                // LOG.fine("DONE");
                break;
            }

            // Find next point to evaluate function
            double xnew = Float.MAX_VALUE;
            if (imin == 0) { // Move fast toward left endpoint
                assert (xs[imin] == 0.0) : ("Left endpoint should be zero, not " + xs[imin]);
                xnew = xs[1] * 0.1;
                // LOG.fine("Move rapidly to left");
            } else if (imin == 3) { // Move fast toward right endpoint
                assert (xs[imin] == 1.0) : ("Right endpoint should be one, not " + xs[imin]);
                xnew = 1.0 - 0.1 * (1.0 - xs[2]);
                // LOG.fine("Move rapidly to right");
            } else if (imin == 1 || imin == 2) { // Have bounded minimum
                boolean goodConvergence = false;
                if (error < previousPreviousError * 0.501) { // converging linearly?
                    // Try parabolic method for hyperlinear convergence.
                    // LOG.fine("good convergence so far");
                    try {
                        xnew = minParabola(xs[imin - 1], fs[imin - 1],
                                xs[imin], fs[imin],
                                xs[imin + 1], fs[imin + 1]);
                        // LOG.fine("Good parabola ");
                        goodConvergence = true;
                    } catch (BadParabolaException e) {
                        // LOG.fine("Bad parabola: "+e.getLocalizedMessage());
                        goodConvergence = false;
                    }
                }
                if (!goodConvergence) {
                    /*
                    LOG.fine("Use GOLDEN section: xs[imin-1]="+xs[imin-1]
                          +" xs[imin-1]="+xs[imin-1]
                          +" xs[imin]="+xs[imin]
                          +" xs[imin+1]="+xs[imin+1]);
                    */
                    // Converging badly.  Resort to golden section.
                    if (xs[imin] - xs[imin - 1] >= xs[imin + 1] - xs[imin]) {
                        // Left side is larger.
                        // LOG.fine("left side is larger");
                        xnew = xs[imin - 1] + GOLD * (xs[imin] - xs[imin - 1]);
                    } else { // Right side is larger
                        // LOG.fine("right side is larger");
                        xnew = xs[imin + 1] - GOLD * (xs[imin + 1] - xs[imin]);
                    }
                }
            } else {
                assert (false) : ("Impossible imin=" + imin);
            }
            // LOG.fine("xnew="+xnew);
            assert (xnew != Float.MAX_VALUE) : ("bad xnew");

            // evaluate function at new point
            double fnew = Float.MAX_VALUE;
            // don't repeat a calculation
            for (int i = 0; i < xs.length; ++i) {
                if (Almost.FLOAT.equal(xnew, xs[i])) {
                    // LOG.fine("Recycle xs["+i+"]="+xs[i]+" fs["+i+"]="+xs[i]);
                    fnew = fs[i];
                }
            }
            if (fnew == Float.MAX_VALUE) { // call expensive function
                fnew = function(xnew, scalarMin, scalarMax);
            }
            // LOG.fine("fnew="+fnew);

            // save on top of discarded value before resorting
            if (imin < 2) {
                xs[3] = xnew;
                fs[3] = fnew;
                // LOG.fine("updated xs[3]="+xs[3]+ " fs[3]="+fs[3]);
            } else {
                xs[0] = xnew;
                fs[0] = fnew;
                // LOG.fine("updated xs[0]="+xs[0]+ " fs[0]="+fs[0]);
            }

            // completed an iteration
            ++iter;
        }

        assert (xmin >= 0.0 && xmin <= 1.0) : ("Impossible xmin=" + xmin);

        final double result = scalarMin + xmin * (scalarMax - scalarMin);
        // LOG.fine("result="+result);
        monitor.report(1.0);
        return result;
    }

    private final double[] _doubleTemp = new double[4]; // reused after profiling

    /**
     * Reorganize samples
     *
     * @param xs Sorted by increasing values
     * @param fs Sorted the same way as xs
     * @return sample for which fs is minimized
     */
    private int sort(final double[] xs, final double[] fs) {
        assert xs.length == 4;
        final int[] sortedX = (new IndexSorter(xs)).getSortedIndices();
        System.arraycopy(xs, 0, _doubleTemp, 0, 4);
        for (int i = 0; i < xs.length; ++i) {
            xs[i] = _doubleTemp[sortedX[i]];
        }
        System.arraycopy(fs, 0, _doubleTemp, 0, 4);
        for (int i = 0; i < xs.length; ++i) {
            fs[i] = _doubleTemp[sortedX[i]];
        }
        int imin = 0;
        for (int i = 1; i < fs.length; ++i) {
            if (fs[i] < fs[imin]) {
                imin = i;
            }
        }
        return imin;
    }

    /**
     * @param x         A fraction of the distance between scalarMin and scalarMax
     * @param scalarMin Minimum scalar
     * @param scalarMax Maximum scalar
     * @return optimum fraction of distance between minimum and maximum scalars
     */
    private double function(final double x, final double scalarMin, final double scalarMax) {
        return function(scalarMin + x * (scalarMax - scalarMin));
    }

    /**
     * Evaluate function
     *
     * @param scalar Argument for embedded function
     * @return Value of function
     */
    private double function(final double scalar) {
        return _function.function(scalar);
    }

    /**
     * Fit a parabola to three points and find the minimum point.
     * where f(x1) = f1; f(x2) = f2; f(x3) = f3;
     * and where x1 &lt; x2 &lt; x3; f(x2) &lt; f(x1); f(x2) &lt; f(x3).
     *
     * @param x1 A value of the argument to the parabolic function
     * @param f1 The value of the function f(x1) at x1
     * @param x2 A value of the argument to the parabolic function
     * @param f2 The value of the function f(x2) at x2
     * @param x3 A value of the argument to the parabolic function
     * @param f3 The value of the function f(x3) at x3
     * @return Value of x that minimizes function f(x)
     * @throws BadParabolaException     If the arguments
     *                                  describe a parabola that cannot be minimized in the range
     *                                  x1 &lt; x &lt;x3,
     *                                  or if the following strict inequalities
     *                                  are not true: x1 &lt; x2 &lt; x3; f(x2) &lt; f(x1), f(x2) &lt; f(x3),
     *                                  or if the x2 is too close to one of the endpoints
     *                                  to describe a parabola accurately.
     * @throws IllegalArgumentException If input values lead to degenerate
     *                                  solutions.
     */
    private static double minParabola(final double x1, final double f1,
                                      final double x2, final double f2,
                                      final double x3, final double f3)
            throws BadParabolaException, IllegalArgumentException {
        if (!Almost.FLOAT.le(x1, x2) || !Almost.FLOAT.le(x2, x3)) {
            throw new BadParabolaException
                    ("Violates x1 <= x2 <= x3: x1=" + x1 + " x2=" + x2 + " x3=" + x3);
        }
        if (Almost.FLOAT.equal(x1, x2)) {
            // LOG.fine("May have already converged x1="+x1+" x2="+x2+" x3="+x3);
            final double result = x2 + 0.1 * (x3 - x2);
            // LOG.fine("Decimate other interval with "+result);
            return result;
        }
        if (Almost.FLOAT.equal(x2, x3)) {
            // LOG.fine("May have already converged x1="+x1+" x2="+x2+" x3="+x3);
            final double result = x1 + 0.9 * (x2 - x1);
            // LOG.fine("Decimate other interval with "+result);
            return result;
        }
        if (!Almost.FLOAT.le(f2, f1) || !Almost.FLOAT.le(f2, f3)) {
            throw new BadParabolaException
                    ("Violates f(x2) <= f(x1), f(x2) <= f(x3)" +
                            ": f1=" + f1 + " f2=" + f2 + " f3=" + f3);
        }
        double xm = Almost.FLOAT.divide((x2 - x1), (x3 - x1), 0.0);
        if (xm < 0.001 || xm > 0.999) {
            throw new BadParabolaException
                    ("Parabola is badly sampled x1=" + x1 + " x2=" + x2 + " x3=" + x3);
        }
        final double a = Almost.FLOAT.divide(((f3 - f1) * xm - (f2 - f1)), (xm - xm * xm), 0.0);
        final double b = f3 - f1 - a;
        if (Almost.FLOAT.ge(a * b, 0.0) || 0.5 * Math.abs(b) > Math.abs(a)) {
            throw new BadParabolaException
                    ("Poor numerical conditioning a=" + a + " b=" + b);
        }
        xm = Almost.FLOAT.divide(-0.5 * b, a, -1.0);
        if (xm < 0.0 || xm > 1.0) {
            throw new BadParabolaException
                    ("Poor numerical conditioning a=" + a + " b=" + b + " xm=" + xm);
        }
        return xm * (x3 - x1) + x1;
    }

    private static class BadParabolaException extends Exception {
        private static final long serialVersionUID = 1L;

        /**
         * Available points do not describe a valid parabola
         *
         * @param message Error message
         */
        private BadParabolaException(final String message) {
            super(message);
        }
    }

    /**
     * Get indices that order an array of values.
     *
     * @author W.S. Harlan
     */
    private class IndexSorter {
        private double[] _values = null;
        private int _length = 0;

        /**
         * The array of values that determine the sort.
         *
         * @param values Array of values to be sorted.
         *               These are held without modification or cloning.
         */
        private IndexSorter(final double[] values) {
            _values = values;
            _length = values.length;
        }

        /**
         * Get an array of indices such that
         * values[index[i]] >= values[index[j]] if i >= j;
         *
         * @return indices that address original array of values
         *         in increasing order.
         */
        public int[] getSortedIndices() {
            final MyComparable[] c = new MyComparable[_length];
            for (int i = 0; i < c.length; ++i) {
                c[i] = new MyComparable(i);
            }

            Arrays.sort(c);
            final int[] result = new int[c.length];
            for (int i = 0; i < result.length; ++i) {
                result[i] = c[i].index;
            }
            return result;
        }

        /**
         * For sorting with Arrays.sort
         */
        private class MyComparable implements Comparable<MyComparable> {
            /**
             * Array index
             */
            private int index = 0;

            /**
             * Constructor.
             *
             * @param index index into array.
             */
            private MyComparable(final int index) {
                this.index = index;
            }

            @Override
            public int compareTo(final MyComparable o) {
                return s_almost.cmp(_values[index], _values[o.index]);
            }
        }
    }
}
