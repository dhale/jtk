/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.awt;

import java.awt.Color;
import java.awt.image.IndexColorModel;

import javax.swing.event.EventListenerList;

import edu.mines.jtk.util.Check;

/**
 * Transforms floats to colors for an index color model.
 * Two steps
 * <ol><li>
 * Map float to byte using clips/percentiles
 * </li><li>
 * Map byte to color using index color model.
 * </li></ol>
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.08.07
 */
public class FloatIndexColorMap extends ColorMap {

  /**
   * Constructs a color map for specified values and index color model.
   * The integers 0 and 255 must be valid pixels for the color model.
   * @param fbm float byte map.
   * @param icm the index color model.
   */
  public FloatIndexColorMap(FloatByteMap fbm, IndexColorModel icm) {
    super(0.0,1.0,icm);
    _fbm = fbm;
    _icm = icm;
  }

  /**
   * Gets the color index corresponding to the specified value.
   * @param f the value to be mapped to index.
   * @return the index in the range [0,255].
   */
  public int getIndex(float f) {
    return _fbm.map(f);
  }

  /**
   * Gets the color (in standard ARGB format) for the specified value.
   * @param f the value to be mapped to a color.
   * @return the pixel.
   */
  public int getARGB(float f) {
    return _icm.getRGB(getIndex(f));
  }

  /**
   * Sets the min-max range of values mapped to colors. Values outside this 
   * range are clipped. The default range is the min and max clips in the 
   * mapping from floats to bytes.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   */
  public void setValueRange(double vmin, double vmax) {
    if (vmin!=getMinValue() || vmax!=getMaxValue()) {
      _fbm.setClips(vmin,vmax);
      super.setValueRange(vmin,vmax);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private FloatByteMap _fbm;
  private IndexColorModel _icm;
}
