/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

/**
 * A recursive filter. An IIR (infinite impulse response) filter.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.19
 */
public abstract class RecursiveFilter {

  /**
   * Applies this filter in the forward direction. 
   * <p>
   * The input and output arrays may be the same. The length of the 
   * input array must not be less than the length of the output array.
   * @param x the input array.
   * @param y the output array.
   */
  public abstract void applyForward(float[] x, float[] y);

  /**
   * Applies this filter in the forward direction. 
   * Same as {@link #applyForward(float[],float[])}.
   */
  public void apply(float[] x, float[] y) {
    applyForward(x,y);
  }
}
