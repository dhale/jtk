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
 * Vector operations that do not change the state of the vector
 *
 * @author W.S. Harlan
 */
public interface VectConst extends Cloneable, java.io.Serializable {
    /**
     * Return the Cartesian dot product of this vector with another
     * vector (not including any inverse covariance).
     * [Feel free to normalize by the number of elements in the array,
     * if the inverse convariance is defined consistently.]
     *
     * @param other The vector to be dotted.
     * @return The dot product.
     */
    double dot(VectConst other);

    /**
     * This is the dot product of the vector with
     * itself premultiplied by the inverse covariance.
     * If the inverse covariance is an identity, then
     * the result is just the dot product with itself.
     * Equivalently,
     * <pre>
     * Vect vect = (Vect) this.clone();
     * vect.multiplyInverseCovariance();
     * return this.dot(vect);
     * </pre>
     * But you can usually avoid the clone.
     *
     * @return magnitude of vector.
     */
    double magnitude();

    // Cloneable: You can clone a mutable version of a VectConst
    Vect clone();
}

