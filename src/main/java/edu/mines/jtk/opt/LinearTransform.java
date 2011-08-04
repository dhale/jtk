/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.opt;

/**
 * Define methods applying a linear transform and its transpose
 *
 * @author W.S. Harlan
 */
public interface LinearTransform {
    /**
     * Apply the linear transform data = F model
     * Zero the current data, and do not add.
     *
     * @param data  Output after linear transform
     * @param model Input for linear transform
     */
    void forward(Vect data, VectConst model);

    /**
     * Apply the transpose of a linear transform model = F' data
     * Add to existing data.
     *
     * @param data  Input for transpose.
     * @param model Output after linear transform.
     */
    void addTranspose(VectConst data, Vect model);

    /**
     * To speed convergence multiple a model by an approximate inverse
     * Hessian.  An empty implementation is equivalent to an identity
     * and is also okay.
     * The Hessian is equivalent to multiplying once by the
     * forward operation and then by the transpose.  Your approximate
     * inverse can greatly speed convergence by trying to diagonalize
     * this Hessian, or at least balancing the diagonal.
     *
     * @param model The model to be multiplied.
     */
    void inverseHessian(Vect model);

    /**
     * Apply any robust trimming of outliers, or
     * scale all errors for an approximate L1 norm when squared.
     * This method should do nothing if you want a standard
     * least-squares solution.
     * Do not change the overall variance of the errors more than necessary.
     *
     * @param dataError This is the original data minus the modeled data.
     */
    void adjustRobustErrors(Vect dataError);
}
