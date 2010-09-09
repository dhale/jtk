/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.awt;

/**
 * A color mapped object has a colormap.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.16
 */
public interface ColorMapped {

  /**
   * Gets the color map.
   * @return the color map.
   */
  public ColorMap getColorMap();
}
