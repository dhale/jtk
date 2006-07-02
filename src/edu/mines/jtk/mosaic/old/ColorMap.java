/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.Color;

/**
 * A color map converts a range of float values to colors.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.24
 */
public interface ColorMap {

  /**
   * Standard color maps. Each of these color maps corresponds to 
   * a pre-computed {@link edu.mines.jtk.awt.ByteIndexColorModel}.
   */
  public enum Standard {
    GRAY,
    JET,
    HUE,
    PRISM,
    RED_WHITE_BLUE,
  }

  /**
   * Gets the minimum value in the range of mapped values.
   * @return the minimum value.
   */
  public float getMinValue();

  /**
   * Gets the maximum value in the range of mapped values.
   * @return the maximum value.
   */
  public float getMaxValue();

  /**
   * Gets the color corresponding to the specified value.
   * @param value the value to be mapped to a color.
   * @return the color.
   */
  public Color getColor(float value);
}
