/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;

import java.util.logging.Logger;

/**
 * Implements a Vect by wrapping an array of doubles.
 * The embedded data are exposed by a getData method.  For all practical
 * purposes this member is public, except that this class must always
 * point to the same array.  The implementation as an array
 * is the point of this class, to avoid duplicate implementations
 * elsewhere.  Multiple inheritance is prohibited and
 * prevents the mixin pattern, but you can share the wrapped array
 * as a private member of your own class,
 * and easily delegate all implemented methods.
 *
 * @author W.S. Harlan
 */
public class ArrayVect1 implements Vect {
    /**
     * wrapped data
     */
    private double[] _data = null;
    /**
     * variance
     */
    private double _variance = 1.0;
    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
    private static final long serialVersionUID = 1L;

    /**
     * Construct from an array of data.
     *
     * @param data     This is the data that will be manipulated.
     * @param variance The method multiplyInverseCovariance()
     *                 will divide all samples by this number.  Pass a value
     *                 of 1 if you do not care.
     */
    public ArrayVect1(final double[] data, final double variance) {
        init(data, variance);
    }

    /**
     * To be used with init()
     */
    protected ArrayVect1() {
    }

    /**
     * Construct from an array of data.
     *
     * @param data     This is the data that will be manipulated.
     * @param variance The method multiplyInverseCovariance()
     *                 will divide all samples by this number.  Pass a value
     *                 of 1 if you do not care.
     */
    protected final void init(final double[] data, final double variance) {
        _data = data;
        _variance = variance;
    }

    /**
     * Return the size of the embedded array
     *
     * @return size of the embedded array
     */
    public int getSize() {
        return _data.length;
    }

    /**
     * Get the embedded data
     *
     * @return Same array as passed to constructore.
     */
    public double[] getData() {
        return _data;
    }

    // Cloneable
    @Override
    public ArrayVect1 clone() {
        try {
            final ArrayVect1 result = (ArrayVect1) super.clone();
            result._data = result._data.clone();
            return result;
        } catch (CloneNotSupportedException ex) {
            final IllegalStateException e = new IllegalStateException(ex.getMessage());
            e.initCause(ex);
            throw e;
        }
    }

    // VectConst
    @Override
    public double dot(final VectConst other) {
        double result = 0;
        final ArrayVect1 rhs = (ArrayVect1) other;
        for (int i = 0; i < _data.length; ++i) {
            result += _data[i] * rhs._data[i];
        }
        return result;
    }

    // Object
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < _data.length; ++i) {
            sb.append(String.valueOf(_data[i]));
            if (i < _data.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    // Vect
    @Override
    public void dispose() {
        _data = null;
    }

    // Vect
    @Override
    public void multiplyInverseCovariance() {
        final double scale = Almost.FLOAT.divide(1.0, getSize() * _variance, 0.0);
        VectUtil.scale(this, scale);
    }

    // Vect
    @Override
    public void constrain() {
    }

    // Vect
    @Override
    public void postCondition() {
    }

    // Vect
    @Override
    public void add(final double scaleThis, final double scaleOther, final VectConst other) {
        final ArrayVect1 rhs = (ArrayVect1) other;
        for (int i = 0; i < _data.length; ++i) {
            _data[i] = scaleThis * _data[i] + scaleOther * rhs._data[i];
        }
    }

    // Vect
    @Override
    public void project(final double scaleThis, final double scaleOther, final VectConst other) {
        add(scaleThis, scaleOther, other);
    }

    // VectConst
    @Override
    public double magnitude() {
        return Almost.FLOAT.divide(dot(this), getSize() * _variance, 0.0);
    }
}

