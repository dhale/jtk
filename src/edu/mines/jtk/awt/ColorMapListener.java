/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.awt;

import java.util.EventListener;

/**
 * A color map listener listens for changes to a color map.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.24
 */
public interface ColorMapListener extends EventListener {

  /**
   * Called when the color map changes.
   * @param cm the color mapper.
   */
  public void colorMapChanged(ColorMap cm);
}
