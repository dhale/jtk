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
 * Implements a Vect by wrapping a single double
 */
public class ScalarVect implements Vect {
    /**
     * wrapped data
     */
    protected transient double _value = 0;
    /**
     * variance for value
     */
    protected transient double _variance = 1.0;
    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
    private static final long serialVersionUID = 1L; // try never to change
    private static final int VERSION = 1; // compatible change in serialization

    /**
     * Specify the initial value
     *
     * @param value    The initial value of the wrapped scalar
     * @param variance The method multiplyInverseCovariance()
     *                 will divide the scalar by this number.
     *                 Pass a value of 1 if you do not care.
     */
    public ScalarVect(final double value, final double variance) {
        init(value, variance);
    }

    /**
     * To be used with init()
     */
    protected ScalarVect() {
        init(0.0, 1.0);
    }

    /**
     * Initialize the Vect.
     *
     * @param value    The initial value of the wrapped scalar .
     * @param variance The method multiplyInverseCovariance()
     *                 will divide the scalar by this number.
     *                 Pass a value of 1 if you do not care.
     */
    public final void init(final double value, final double variance) {
        _value = value;
        _variance = variance;
    }

    /**
     * Get the value of the scalar.
     *
     * @return The wrapped scalar.
     */
    public double get() {
        return _value;
    }

    /**
     * Set the value of the scalar.
     *
     * @param value The new value of the wrapped scalar.
     */
    public void set(final double value) {
        _value = value;
    }

    // VectConst, Cloneable
    @Override
    public ScalarVect clone() {
        try {
            return (ScalarVect) super.clone();
        } catch (CloneNotSupportedException ex) {
            final IllegalStateException e = new IllegalStateException(ex.getMessage());
            e.initCause(ex);
            throw e;
        }
    }

    // VectConst
    @Override
    public double dot(final VectConst other) {
        final ScalarVect rhs = (ScalarVect) other;
        return _value * rhs._value;
    }

    // Object
    @Override
    public String toString() {
        return "ScalarVect<" + _value + ">";
    }

    // Vect
    @Override
    public void dispose() {
        _value = Double.NaN;
        _variance = Double.NaN;
    }

    // Vect
    @Override
    public void multiplyInverseCovariance() {
        _value = Almost.FLOAT.divide(_value, _variance, 0.0);
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
        final ScalarVect rhs = (ScalarVect) other;
        _value = scaleThis * _value + scaleOther * rhs._value;
    }

    // Vect
    @Override
    public void project(final double scaleThis, final double scaleOther, final VectConst other) {
        add(scaleThis, scaleOther, other);
    }

    // VectConst
    @Override
    public double magnitude() {
        return Almost.FLOAT.divide(dot(this), _variance, 0.0);
    }

    private void writeObject(final ObjectOutputStream out)
            throws IOException {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("value", _value);
        map.put("variance", _variance);
        map.put("VERSION", VERSION);
        out.writeObject(map);
    }

    private void readObject(final ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        @SuppressWarnings("unchecked") final Map<String, Object> map =
                (Map<String, Object>) in.readObject();

        _value = (Double) map.get("value");
        _variance = (Double) map.get("variance");

        final int version = (Integer) map.get("VERSION");
        if (version != VERSION) {
            Logger.getLogger(getClass().getName()).warning
                    ("Need to convert data from version " + version + " to " + VERSION);
        }
    }
}

