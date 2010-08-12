/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.Sampling;

/**
 * Gridding by interpolation with radial basis functions.
 * This class exists only to implement the interface {@link Gridder2}.
 * It otherwise adds no significant functionality to its base class 
 * {@link RadialInterpolator2}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.01.16
 */
public class RadialGridder2 extends RadialInterpolator2 implements Gridder2 {

  /**
   * Constructs a gridder with specified known (scattered) samples.
   * @param basis the radial basis function.
   * @param f array of known sample values f(x1,x2).
   * @param x1 array of known sample x1 coordinates.
   * @param x2 array of known sample x2 coordinates.
   */
  public RadialGridder2(
    RadialInterpolator2.Basis basis, 
    float[] f, float[] x1, float[] x2) 
  {
    super(basis,f,x1,x2);
  }

  /**
   * Sets the known (scattered) samples to be interpolated.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public void setScattered(float[] f, float[] x1, float[] x2) {
    super.setSamples(f,x1,x2);
  }

  /**
   * Computes gridded sample values from the known sample values.
   * Before interpolating, this method sets the bounds to be consistent 
   * with the first and last values of the specified samplings, so that
   * interpolated values will never be null.
   * @param s1 sampling of x1.
   * @param s2 sampling of x2.
   * @return array of gridded sample values.
   */
  public float[][] grid(Sampling s1, Sampling s2) {
    return super.interpolate(s1,s2);
  }
}
