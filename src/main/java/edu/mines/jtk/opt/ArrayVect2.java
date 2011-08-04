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
 * Implement a Vect as a two dimensional array of doubles.
 * The embedded data are exposed by a getData method.  For all practical
 * purposes this member is public, except that this class must always
 * point to the same array.  The implementation as an array
 * is the point of this class, to avoid duplicate implementations
 * elsewhere.  Multiple inheritance is prohibited and
 * prevents the mixin pattern, but you can share the wrapped array
 * as a private member of your own class,
 * and easily delegate all implemented methods.
 *
 * @author W.S.Harlan
 */

public class ArrayVect2 implements Vect {
    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
    private static final long serialVersionUID = 1L;

    /**
     * wrapped data
     */
    private double[][] _data = null;

    /**
     * variance
     */
    private double _variance = 1.0;

    /**
     * Wrap an array as a Vect.
     *
     * @param data     This will be assigned to the public data.
     * @param variance This variance will be used to divide data in
     *                 multiplyInverseCovariance.
     */
    public ArrayVect2(final double[][] data, final double variance) {
        init(data, variance);
    }

    /**
     * To be used with init()
     */
    protected ArrayVect2() {
    }

    /**
     * Wrap an array as a Vect.
     *
     * @param data     This will be assigned to the public data.
     * @param variance This variance will be used to divide data in
     *                 multiplyInverseCovariance.
     */
    protected void init(final double[][] data, final double variance) {
        _data = data;
        _variance = variance;
    }

    /**
     * Get the embedded data.
     *
     * @return Same array as passed to constructore.
     */
    public double[][] getData() {
        return _data;
    }

    /**
     * Return the size of the embedded array
     *
     * @return size of embedded array
     */
    public int getSize() {
        return _data.length * _data[0].length;
    }

    // Vect interface
    @Override
    public void add(final double scaleThis, final double scaleOther, final VectConst other) {
        final ArrayVect2 o = (ArrayVect2) other;
        for (int i = 0; i < _data.length && _data.length > 0; ++i) {
            for (int j = 0; j < _data[0].length; ++j) {
                _data[i][j] = scaleThis * _data[i][j] + scaleOther * o._data[i][j];
            }
        }
    }

    // Vect interface
    @Override
    public void project(final double scaleThis, final double scaleOther, final VectConst other) {
        add(scaleThis, scaleOther, other);
    }

    // Vect interface
    @Override
    public void dispose() {
        _data = null;
    }

    // Vect interface
    @Override
    public void multiplyInverseCovariance() {
        final double scale = Almost.FLOAT.divide(1.0, getSize() * _variance, 0.0);
        VectUtil.scale(this, scale);
    }

    // VectConst interface
    @Override
    public double magnitude() {
        return Almost.FLOAT.divide(dot(this), getSize() * _variance, 0.0);
    }

    // Vect interface
    @Override
    public void constrain() {
    }

    // Vect interface
    @Override
    public void postCondition() {
    }

    // VectConst interface
    @Override
    public ArrayVect2 clone() {
        try {
            final double[][] newData = new double[_data.length][];
            for (int i = 0; i < newData.length; ++i) {
                newData[i] = _data[i].clone();
            }
            final ArrayVect2 result = (ArrayVect2) super.clone();
            result.init(newData, _variance);
            return result;
        } catch (CloneNotSupportedException ex) {
            final IllegalStateException e = new IllegalStateException(ex.getMessage());
            e.initCause(ex);
            throw e;
        }
    }

    // VectConst interface
    @Override
    public double dot(final VectConst other) {
        final ArrayVect2 rhs = (ArrayVect2) other;
        double result = 0.0;
        for (int i = 0; i < _data.length; ++i) {
            for (int j = 0; j < _data[0].length; ++j) {
                result += _data[i][j] * rhs._data[i][j];
            }
        }
        return result;
    }
}
