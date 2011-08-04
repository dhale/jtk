/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements convenience methods for Vect.
 *
 * @author W.S. Harlan
 */
public class VectUtil {
    private static final Logger LOG
            = Logger.getLogger(VectUtil.class.getName());

    private VectUtil() {
    }

    /**
     * Scale a vector by a scalar constant.
     *
     * @param v      Vector to scale.
     * @param scalar Factor to scale the vector.
     */
    public static void scale(final Vect v, final double scalar) {
        v.add(scalar, 0.0, v);
    }

    /**
     * Set the magnitude of this vector to zero, so that this.dot(this) == 0.
     *
     * @param v Vector to zero
     */
    public static void zero(final Vect v) {
        scale(v, 0.0);
    }

    /**
     * Copy the state of one vector onto another.
     *
     * @param to   Vector whose state should be initialized
     *             with the state of from.
     * @param from Vector whose state should be copied.
     */
    public static void copy(final Vect to, final VectConst from) {
        to.add(0.0, 1.0, from);
    }

    /**
     * Clone a vector and initialized to zero, so that
     * out.dot(out) == 0.
     *
     * @param v Vect to clone
     * @return A cloned copy of the vector set to zero magnitude.
     */
    public static Vect cloneZero(final VectConst v) {
        final Vect result = v.clone();
        zero(result);
        return result;
    }

    static final Almost ALMOST_DOT = new Almost(0.000015);

    /**
     * See if two vectors are the same.  Useful for test code.
     *
     * @param v1 First vector
     * @param v2 Second vector
     * @return true if vectors appear to be the same, within
     *         floating precision.
     */
    public static boolean areSame(final VectConst v1, final VectConst v2) {
        final double aa = v1.dot(v1);
        final double ab = v1.dot(v2);
        final double bb = v2.dot(v2);
        // LOG.info("aa="+aa+" ab="+ab+" bb="+bb);
        return
                ALMOST_DOT.equal(aa, bb) &&
                        ALMOST_DOT.equal(aa, ab) &&
                        ALMOST_DOT.equal(ab, bb);
    }

    /**
     * Exercise all methods of Vect.
     *
     * @param vect An instance of a Vect to test.
     *             Should be initialized to random non-zero values.
     *             A vector of zero magnitude will fail.
     */
    public static void test(final VectConst vect) {
        final double originalDot = vect.dot(vect);
        ass(!Almost.FLOAT.zero(originalDot), "cannot test a zero vector");

        Vect t = VectUtil.cloneZero(vect);
        ass(Almost.FLOAT.zero(t.dot(t)), "cloneZero() did not work");

        VectUtil.copy(t, vect);
        double check = t.dot(vect) / vect.dot(vect);
        ass(Almost.FLOAT.equal(check, 1.0), "not 1. check=" + check);

        VectUtil.scale(t, 0.5);
        check = t.dot(vect) / vect.dot(vect);
        ass(Almost.FLOAT.equal(check, 0.5), "not 0.5 check=" + check);

        t.add(1.0, 1.0, vect);
        check = t.dot(vect) / vect.dot(vect);
        ass(Almost.FLOAT.equal(check, 1.5), "not 1.5 check=" + check);

        t.add(2.0, -5.0, vect);
        check = t.dot(vect) / vect.dot(vect);
        ass(Almost.FLOAT.equal(check, -2.0), "not -2, check=" + check);

        t.project(0.0, 1.0, vect);
        t.project(1.75, -0.75, vect);
        ass(VectUtil.areSame(t, vect), "project failed");

        t.dispose();
        ass(Almost.FLOAT.equal(originalDot, vect.dot(vect)),
                "exercise of clone damaged original");

        t = vect.clone();
        t.multiplyInverseCovariance();
        final double mag1 = vect.dot(t);
        t.dispose();
        final double mag2 = vect.magnitude();
        ass(Almost.FLOAT.equal(mag1, mag2),
                "magnitude() inconsistent with "
                        + "multiplyInverseCovariance() and dot(): " +
                        mag1 + "!=" + mag2);
        ass(mag1 > 0, "inverse covariance gave zero magnitude");
        ass(mag2 > 0, "magnitude was zero when dot product was not zero");

        // simple test of constrain
        t = vect.clone();
        t.constrain();
        final double mag3 = t.magnitude();
        ass(mag3 > 0, "constrain() gave zero magnitude");
        t.dispose();

        // make sure postCondition can be called
        t = vect.clone();
        t.postCondition();
        t.dispose();

        // some will override toString method
        final String vs = vect.toString();
        assert vs != null && vs.length() > 0;

        // test serialization
        byte[] data = null;
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            t = vect.clone();
            oos.writeObject(t);
            oos.flush();
            oos.close();
            t.dispose();
            t = null;
            data = baos.toByteArray();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "", e);
            ass(false, "writing serialization failed " + e.getMessage());
        }
        try {
            final ByteArrayInputStream bais = new ByteArrayInputStream(data);
            final ObjectInputStream ois = new ObjectInputStream(bais);
            t = (Vect) (ois.readObject());
            ass(VectUtil.areSame(t, vect),
                    "Serialization did not preserve Vect "
                            + t.dot(t) + "==" + t.dot(vect) + "==" + vect.dot(vect));
            // check these are not sharing anything
            VectUtil.scale(t, 0.5);
            final double tt = t.dot(t);
            final double tv = t.dot(vect);
            final double vv = vect.dot(vect);
            ass(tt > 0, "Scaling set serialized vect to zero magnitude");
            ass(Almost.FLOAT.equal(tt * 2, tv),
                    "Serialized vector does not have independent magnitude tt=" + tt +
                            " tv=" + tv);
            ass(Almost.FLOAT.equal(tv * 2, vv),
                    "serialized vector does not have independent magnitude tv=" + tv +
                            " vv=" + vv);
            t.dispose();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "", e);
            ass(false, "reading serialization failed " + e.getMessage());
        } catch (ClassNotFoundException e) {
            LOG.log(Level.SEVERE, "", e);
            ass(false, "Can't find class just written " + e.getMessage());
        }
    }

    /**
     * Return the number of significant digits in the dot product
     * when calculated with and without the transpose.
     *
     * @param data      Nonzero sample data
     * @param model     A nonzero sample model.
     * @param transform The transform to test.
     * @return number of digits in precision.
     */
    public static int getTransposePrecision(final VectConst data, final VectConst model,
                                            final LinearTransform transform) {
        return getTransposePrecision(data, model,
                new LinearTransformWrapper(transform));
    }

    /**
     * Return the number of significant digits in the dot product
     * when calculated with and without the transpose.
     *
     * @param data      Nonzero sample data
     * @param model     A nonzero sample model.
     * @param transform The transform to test.
     * @return number of digits in precision.
     */
    public static int getTransposePrecision(final VectConst data, final VectConst model,
                                            final Transform transform) {
        VectUtil.test(data);
        VectUtil.test(model);
        final boolean dampOnlyPerturbation = true; // results in a bigger b
        final TransformQuadratic tq = new TransformQuadratic
                (data, model, null, transform, dampOnlyPerturbation);
        int precision = 200;
        precision = Math.min(precision, tq.getTransposePrecision());
        return precision;
    }

    // Assertion that cannot be disabled.
    private static void ass(final boolean condition, final String requirement) {
        if (!condition) {
            throw new IllegalStateException(requirement);
        }
    }
}

