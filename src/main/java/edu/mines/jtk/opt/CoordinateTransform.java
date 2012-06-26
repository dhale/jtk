/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.opt;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Find a best linear combination of input
 * coordinates to fit output coordinates.
 * Finds a_io (a with subscripts i and o)
 * to best approximate the linear transform.
 * <pre>
 * out_o = sum_i ( a_io * in_i )
 * </pre>
 * where in_i are input coordinates,<br>
 * out_o are output coordinates,<br>
 * i is the index of each input dimension,<br>
 * and o is the index of each output dimension.<br>
 * <p/>
 * <p> The optimum coefficients minimize this least
 * squares error:
 * <pre>
 * sum_oj [ sum_i ( a_io * in_ij ) - out_oj ]^2
 * </pre>
 * where in_ij is an input array of different coordinates,<br>
 * out_oj is an output array of corresponding coordinates,<br>
 * and j is the index of pairs of coordinates to be fit
 * in a least-squares sense.
 * <p/>
 * <p> Normal equations (indexed by k) are solved independently for each o:
 * <pre>
 * sum_ij ( in_kj * in_ij * a_io ) = sum_j ( in_kj * out_oj )
 * </pre>
 * The Hessian is <code> H = sum_j ( in_kj * in_ij ) </code>
 * and the gradient <code> b = - sum_j ( in_kj * out_oj ) </code>
 * <p/>
 * <p> The solution is undamped and may not behave as you
 * want for degenerate solutions.
 * <p/>
 * <p> If the linear transform needs a translation shift,
 * then include a constant as one of the input coordinates.
 *
 * @author W.S. Harlan
 */
public class CoordinateTransform {
    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");

    private int _nout = 0;
    private int _nin = 0;
    private final double[][] _hessian;
    private final double[][] _b;
    private double[][] _a;
    private double[] _in0 = null;
    private double[] _out0 = null;
    private double[] _inScr = null;
    private double[] _outScr = null;

    /**
     * Constructor sets number of input and output coordinates.
     *
     * @param dimensionOut Number of output coordinates.
     * @param dimensionIn  Number of input coordinates.
     */
    public CoordinateTransform(final int dimensionOut, final int dimensionIn) {
        _nout = dimensionOut;
        _nin = dimensionIn;
        _hessian = new double[_nin][_nin];
        _b = new double[_nout][_nin];
        _inScr = new double[_nin];
        _outScr = new double[_nout];
    }

    /**
     * Add an observation of a set of input and output coordinates
     * You should add enough of these to determine (or overdetermine)
     * a unique linear mapping.
     * To allow translation, include a constant 1 as an input coordinate.
     *
     * @param out A set of observed output coordinates
     *            with an unknown linear relationship to input coordinates.
     * @param in  A set of observed input coordinates
     *            that should be linearly combined to calculate each of the
     *            output coordinates.
     *            To allow translation, include a constant 1.
     */
    public void add(final double[] out, final double[] in) {
        _a = null; // must redo any previous solution

        if (in.length != _nin) {
            throw new IllegalArgumentException("in must have dimension " + _nin);
        }

        if (out.length != _nout) {
            throw new IllegalArgumentException("out must have dimension " + _nout);
        }

        if (_in0 == null) {
            _in0 = in.clone();
        }
        if (_out0 == null) {
            _out0 = out.clone();
        }

        for (int i = 0; i < _nin; ++i) {
            _inScr[i] = in[i] - _in0[i];
        }
        for (int i = 0; i < _nout; ++i) {
            _outScr[i] = out[i] - _out0[i];
        }

        for (int k = 0; k < _nin; ++k) {
            for (int i = 0; i < _nin; ++i) {
                _hessian[k][i] += _inScr[k] * _inScr[i];
            }
            for (int o = 0; o < _nout; ++o) {
                _b[o][k] -= _outScr[o] * _inScr[k];
            }
        }
    }

    /**
     * For a given set of input coordinates,
     * return the linearly predicted output coordinates.
     *
     * @param in A set of input coordinates
     * @return A computed set of output coordinates.
     */
    public double[] get(double[] in) {
        for (int i = 0; i < in.length; ++i) {
            _inScr[i] = in[i] - _in0[i];
        }
        in = null;

        if (_a == null) {
            _a = new double[_nout][_nin];
            for (int o = 0; o < _nout; ++o) {
                final LinearQuadratic lq = new LinearQuadratic(o);
                final QuadraticSolver qs = new QuadraticSolver(lq);
                final ArrayVect1 solution = (ArrayVect1) qs.solve(_nin + 4, null);
                final double[] data = solution.getData();
                System.arraycopy(data, 0, _a[o], 0, _nin);
                solution.dispose();
            }
        }
        final double[] result = new double[_nout];
        for (int o = 0; o < _nout; ++o) {
            for (int i = 0; i < _nin; ++i) {
                result[o] += _a[o][i] * _inScr[i];
            }
        }
        for (int i = 0; i < result.length; ++i) {
            result[i] = result[i] + _out0[i];
        }
        return result;
    }

    // describes normal equations.
    private class LinearQuadratic implements Quadratic {
        private int _o = -1;

        /**
         * Constructor for normal equations
         *
         * @param o Index of output dimension
         */
        private LinearQuadratic(final int o) {
            _o = o;
        }

        @Override
        public void multiplyHessian(final Vect x) {
            final ArrayVect1 m = (ArrayVect1) x;
            final double[] data = m.getData();
            final double[] oldData = data.clone();
            Arrays.fill(data, 0.0);
            for (int i = 0; i < data.length; ++i) {
                for (int j = 0; j < data.length; ++j) {
                    data[i] += _hessian[i][j] * oldData[j];
                }
            }
        }

        @Override
        public Vect getB() {
            return new ArrayVect1(_b[_o].clone(), 1.0);
        }

        @Override
        public void inverseHessian(final Vect x) {
        } // not necessary
    }

}
