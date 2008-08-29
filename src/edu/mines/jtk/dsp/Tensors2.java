/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

/**
 * An interface for 2D tensors used in anisotropic 2D image processing. 
 * Each tensor is a symmetric positive-semidefinite 2-by-2 matrix:
 * <pre><code>
 * A = |a11 a12|
 *     |a12 a22|
 * </code></pre>
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.07.28
 */
public interface Tensors2 {

  /**
   * Gets tensor elements for specified indices.
   * @param i1 index for 1st dimension.
   * @param i2 index for 2nd dimension.
   * @param a array {a11,a12,a22} of tensor elements.
   */
  public void getTensor(int i1, int i2, float[] a);
}
