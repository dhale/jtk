/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

/**
 * An interface for 3D tensors used in anisotropic 3D image processing. 
 * Each tensor is a symmetric positive-semidefinite 3-by-3 matrix:
 * <pre><code>
 *     |a11 a12 a13|
 * A = |a12 a22 a23|
 *     |a13 a23 a33|
 * </code></pre>
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.07.28
 */
public interface Tensors3 {

  /**
   * Gets tensor elements for specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param i3 index for 3rd dimension.
   * @param a array {a11,a12,a13,a22,a23,a33} of tensor elements.
   */
  public void getTensor(int i1, int i2, int i3, float[] a);
}
