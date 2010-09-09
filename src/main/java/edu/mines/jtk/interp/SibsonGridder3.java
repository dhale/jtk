/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.Sampling;

/**
 * Gridding by Sibson interpolation of scattered samples of f(x1,x2,x3).
 * This class exists only to implement the interface {@link Gridder3}.
 * It otherwise adds no significant functionality to its base class 
 * {@link SibsonInterpolator3}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.22
 */
public class SibsonGridder3 extends SibsonInterpolator3 implements Gridder3 {

  /**
   * Constructs a gridder with specified known (scattered) samples.
   * @param f array of known sample values f(x1,x2,x3).
   * @param x1 array of known sample x1 coordinates.
   * @param x2 array of known sample x2 coordinates.
   * @param x3 array of known sample x3 coordinates.
   */
  public SibsonGridder3(float[] f, float[] x1, float[] x2, float[] x3) {
    super(f,x1,x2,x3);
  }

  /**
   * Sets the smoothness of the Sibson interpolant. 
   * Two values for smoothness are possible.
   * <p>
   * If false (the default), the interpolant is C1 everywhere except 
   * at the known sample points, where it is C0, with a discontinuous 
   * derivative. For this default, interpolated values are guaranteed 
   * to be within the range of known sample values. 
   * <p>
   * If true, the interpolant is smoother, C1 everywhere, but interpolated
   * values may be outside the range of known sample values.
   * @param smooth true, for C1 everywhere; false, for C0 at known samples.
   * @see SibsonInterpolator3#setGradientPower(double)
   */
  public void setSmooth(boolean smooth) {
    if (smooth) {
      setGradientPower(1.0);
    } else {
      setGradientPower(0.0);
    }
  }

  /**
   * Sets the known (scattered) samples to be interpolated.
   * @param f array of sample values f(x1,x2,x3).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public void setScattered(float[] f, float[] x1, float[] x2, float[] x3) {
    super.setSamples(f,x1,x2,x3);
  }

  /**
   * Computes gridded sample values from the known sample values.
   * Before interpolating, this method sets the bounds to be consistent 
   * with the first and last values of the specified samplings, so that
   * interpolated values will never be null.
   * @param s1 sampling of x1.
   * @param s2 sampling of x2.
   * @param s3 sampling of x3.
   * @return array of gridded sample values.
   */
  public float[][][] grid(Sampling s1, Sampling s2, Sampling s3) {
    super.setBounds(s1,s2,s3);
    return super.interpolate(s1,s2,s3);
  }
}
