/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A tile in a mosaic.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.11
 */
public class Tile extends Canvas {

  public int getRowIndex() {
    return _irow;
  }

  public int getColumnIndex() {
    return _icol;
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  Tile(Mosaic mosaic, int irow, int icol) {
    super(mosaic,SWT.NONE);
    _mosaic = mosaic;
    _irow = irow;
    _icol = icol;
    addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent pe) {
        doPaint(pe);
      }
    });
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private int _irow,_icol;

  private void doPaint(PaintEvent pe) {
    GC gc = pe.gc;
    Point size = getSize();
    int w = size.x;
    int h = size.y;
    gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
    gc.fillRectangle(0,0,w,h);
    Point extent = gc.stringExtent("Axis");
    int x = w/2-extent.x/2;
    int y = h/2-extent.y/2;
    gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
    gc.drawString("Tile",x,y);
  }
}
