/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.opt;

/**
 * Define a second-order quadratic operation on a Vector
 * 0.5 x'Hx + b'x
 * where H is a positive semidefinite quadratic and b is a
 * linear gradient.
 *
 * @author W.S. Harlan
 */
public interface Quadratic {
    /**
     * Multiply vector by the quadratic Hessian H.
     * Perform the operation in-place.
     *
     * @param x Vector to be multiplied and modified.
     */
    void multiplyHessian(Vect x);

    /**
     * Multiply vector by an approximate inverse of the Hessian.
     * Perform the operation in-place.  This method is
     * useful to speed convergence.
     * An empty implementation is equivalent to an identity.
     *
     * @param x Vector to be multiplied and modified.
     */
    void inverseHessian(Vect x);

    /**
     * Get the linear gradient term b of the quadratic expression.
     * The recipient receives a unique copy that must be disposed.
     *
     * @return The vector b where the quadratic is x'Hx + b'x
     */
    Vect getB();

}
