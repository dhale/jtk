/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;

/**
 * A difference filter, with a transpose, inverse, and inverse-transpose.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.10.10
 */
public class DifferenceFilter {

  /**
   * Constructs a difference filter.
   */
  public DifferenceFilter() {
    this(1.0);
  }

  /**
   * Constructs a difference filter with specified weight.
   * @param alpha the weight applied to the previous sample.
   */
  public DifferenceFilter(double alpha) {
    _alpha = (float)alpha;
  }

  /**
   * Applies this difference filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply(float[] x, float[] y) {
    int n = y.length;
    y[0] = x[0];
    for (int i=1; i<n; ++i)
      y[i] = x[i]-_alpha*x[i-1];
  }

  /**
   * Applies the transpose of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyTranspose(float[] x, float[] y) {
    int n = y.length;
    y[n-1] = x[n-1];
    for (int i=n-2; i>=0; --i)
      y[i] = x[i]-_alpha*x[i+1];
  }

  /**
   * Applies the inverse of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyInverse(float[] x, float[] y) {
    int n = y.length;
    y[0] = x[0];
    for (int i=1; i<n; ++i)
      y[i] = x[i]+_alpha*y[i-1];
  }

  /**
   * Applies the inverse transpose of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyInverseTranspose(float[] x, float[] y) {
    int n = y.length;
    y[n-1] = x[n-1];
    for (int i=n-2; i>=0; --i)
      y[i] = x[i]+_alpha*y[i+1];
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private float _alpha;
}
