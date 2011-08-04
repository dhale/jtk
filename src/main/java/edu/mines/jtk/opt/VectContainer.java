/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/

package edu.mines.jtk.opt;

/**
 * Treat an indexed collection of Vects as a new Vect.
 * Defined as an interface to allow both in-memory and cached
 * implementations.
 *
 * @author W.S. Harlan
 */
public interface VectContainer extends Vect {

    /**
     * Save a vect for the specified index.
     *
     * @param index A unique integer for the requested vect.
     *              Integers need not be consecutive.  Use any valid integer.
     * @param vect  Vect for specified index.
     */
    void put(int index, Vect vect);

    /**
     * Get a vect for the specified index.
     *
     * @param index A unique integer for the requested vect.
     *              Integers need not be consecutive.
     * @return Vect associated with this index, or null if none.
     */
    Vect get(int index);

    /**
     * Returns the number of unique indices in this container.
     *
     * @return number of unique indices
     */
    int size();

    /**
     * Returns true if this container has a vect for the specified index.
     *
     * @param index Check for a Vect with this index.
     * @return True if index has been assigned to a vect.
     */
    boolean containsKey(int index);

    /**
     * Return a set of all indices that have been assigned to a value.
     *
     * @return all indices that have been assigned Vect.
     *         Return in the order of preferred access.
     */
    int[] getKeys();

    // Override Object return type
    @Override
    VectContainer clone();
}
