/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.opt;

/**
 * Implement a vector supporting linear vector-space methods
 * Test your implementation with VectUtil.test().
 *
 * @author W.S. Harlan
 */
public interface Vect extends VectConst {
    /**
     * Add a scaled version of another vector to a scaled version of this
     * vector.
     * [If this==other, then the result should be the same as scaling
     * this by (scaleThis+scaleOther)]
     *
     * @param scaleThis  Multiply this vector by this scalar before adding.
     * @param scaleOther Multiply the other vector by this scalar before adding.
     * @param other      The other vector to be multiplied.
     */
    void add(double scaleThis, double scaleOther, VectConst other);

    /**
     * Project another vector onto the space of this vector,
     * then scale, and add to a scaled version of this vector.
     * (Useful for perturbing one vector with a constrained subspace.)
     * This method should give the same result as add(),
     * if the other Vect is an instance of the same class as this Vect.
     * This operation need not be supported for any any types
     * other than this Vect.
     *
     * @param other      The other vector to be projected, scaled, and added.
     * @param scaleThis  Multiply this vector by this scalar before adding.
     * @param scaleOther Multiply the other vector by this scalar before adding.
     */
    void project(double scaleThis, double scaleOther, VectConst other);

    /**
     * Optionally free any resources held by this object.
     * Will not be used again.  This method can safely do nothing.
     */
    void dispose();

    /**
     * Optionally multiply a vector by the inverse covariance matrix.
     * Also called preconditioning.
     * A method that does nothing is equivalent to an identity.
     * [vect.magnitude() should return the same value as
     * vect.dot(((Vect)vect.clone()).multiplyInverseCovariance());
     * This should enhance components that should be discouraged
     * in the model and suppress components that are preferred.
     * This operation slows convergence.  This filter must be linear.
     * For inversions, you should at least implement a scaling operation that
     * correctly weights errors in the data versus the magnitude of the model.]
     */
    void multiplyInverseCovariance();

    /**
     * Optionally apply a hard constraint (such as an inequality)
     * to the current vector.  This is used only by a non-linear optimization.
     * This method can safely do nothing.
     */
    void constrain();

    /**
     * Apply a linear filter that enhances components that should
     * be optimized first, and suppresses components of lesser importance.
     * Also called post-conditioning.
     * This filter prefilters all perturbations of the model and
     * speeds convergence on components of most importance in the model.
     * The same result can be accomplished by Transform.inverseHessian(),
     * but this location may be more convenient.  Use this location
     * if the conditioning is independent of the Transform and dependent
     * on the implementation of the model.
     * This method can safely do nothing.
     */
    void postCondition();

    // Cloneable, but override return type
    @Override
    Vect clone();
}

