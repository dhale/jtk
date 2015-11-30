/****************************************************************************
Copyright 2003, Landmark Graphics and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
