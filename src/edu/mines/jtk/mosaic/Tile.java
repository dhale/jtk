/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;

/**
 * A tile in a mosaic.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.11
 */
public class Tile extends Canvas {

  public int getRowIndex() {
    return _irow;
  }

  public int getColumn() {
    return _icol;
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  Tile(Mosaic mosaic, int irow, int icol) {
    super(mosaic,SWT.NONE);
    _mosaic = mosaic;
    _irow = irow;
    _icol = icol;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private int _irow,_icol;
}
