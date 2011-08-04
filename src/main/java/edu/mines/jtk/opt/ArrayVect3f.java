/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Implement a Vect as a three-dimensional array of floats.
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

public class ArrayVect3f implements Vect {

    /**
     * wrapped data
     */
    protected transient float[][][] _data = null;

    /**
     * variance for all samples
     */
    protected transient double _variance = 1.0;

    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
    private static final long serialVersionUID = 1L; // try never to change
    private static final int VERSION = 1; // compatible change in serialization

    /**
     * Wrap an array as a Vect.
     *
     * @param data     This will be assigned to the public data.
     * @param variance This variance will be used to divide data in
     *                 multiplyInverseCovariance.
     */
    public ArrayVect3f(final float[][][] data, final double variance) {
        init(data, variance);
    }

    /**
     * Get the value of the variance passed to the constructor.
     *
     * @return This variance will be used to divide data in
     *         multiplyInverseCovariance.
     */
    public double getVariance() {
        return _variance;
    }

    /**
     * To be used with init()
     */
    protected ArrayVect3f() {
    }

    /**
     * Wrap an array as a Vect.
     *
     * @param data     This will be assigned to the public data.
     * @param variance This variance will be used to divide data in
     *                 multiplyInverseCovariance.
     */
    protected final void init(final float[][][] data, final double variance) {
        _data = data;
        _variance = variance;
    }

    /**
     * Get the embedded data.
     *
     * @return Same array as passed to constructore.
     */
    public float[][][] getData() {
        return _data;
    }

    /**
     * Return the size of the embedded array
     *
     * @return size of embedded array
     */
    public int getSize() {
        return _data.length * _data[0].length * _data[0][0].length;
    }

    // Vect interface
    @Override
    public void add(final double scaleThis, final double scaleOther, final VectConst other) {
        final float s1 = (float) scaleThis;
        final float s2 = (float) scaleOther;
        final ArrayVect3f rhs = (ArrayVect3f) other;
        for (int i = 0; i < _data.length; ++i) {
            for (int j = 0; j < _data[0].length; ++j) {
                for (int k = 0; k < _data[0][0].length; ++k) {
                    _data[i][j][k] = s1 * _data[i][j][k] + s2 * rhs._data[i][j][k];
                }
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
    public ArrayVect3f clone() {
        try {
            final float[][][] newData = new float[_data.length][_data[0].length][];
            for (int i = 0; i < newData.length; ++i) {
                for (int j = 0; j < newData[0].length; ++j) {
                    newData[i][j] = _data[i][j].clone();
                }
            }
            final ArrayVect3f result = (ArrayVect3f) super.clone();
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
        double result = 0.0;
        final ArrayVect3f rhs = (ArrayVect3f) other;
        for (int i = 0; i < _data.length; ++i) {
            for (int j = 0; j < _data[0].length; ++j) {
                for (int k = 0; k < _data[0][0].length; ++k) {
                    result += (double) _data[i][j][k] * rhs._data[i][j][k];
                }
            }
        }
        return result;
    }

    private void writeObject(final ObjectOutputStream out)
            throws IOException {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", _data);
        map.put("variance", _variance);
        map.put("VERSION", VERSION);
        out.writeObject(map);
    }

    private void readObject(final ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        @SuppressWarnings("unchecked") final Map<String, Object> map =
                (Map<String, Object>) in.readObject();

        _data = (float[][][]) map.get("data");
        _variance = (Double) map.get("variance");

        final int version = (Integer) map.get("VERSION");
        if (version != VERSION) {
            Logger.getLogger(getClass().getName()).warning
                    ("Need to convert data from version " + version + " to " + VERSION);
        }
    }
}
