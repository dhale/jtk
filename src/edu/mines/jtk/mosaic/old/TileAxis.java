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
 * A tile axis in a mosaic.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.12
 */
public class TileAxis extends Canvas {

  public static final int TOP = 1;
  public static final int LEFT = 2;
  public static final int BOTTOM = 3;
  public static final int RIGHT = 4;

  public int getPlacement() {
    return _placement;
  }

  public int getIndex() {
    return _index;
  }

  public void setFont(Font font) {
    super.setFont(font);
    _mosaic.needsLayout();
  }

  public void setLabel(String label) {
    _label = label;
    _mosaic.needsLayout();
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  TileAxis(Mosaic mosaic, int placement, int index) {
    super(mosaic,SWT.NONE);
    _mosaic = mosaic;
    _placement = placement;
    _index = index;
    addListener(SWT.Paint, new Listener() {
      public void handleEvent(Event e) {
        onPaint(e);
      }
    });
  }

  /**
   * Gets the width minimum for this axis. This width does not include 
   * any border that will be drawn by the mosaic.
   */
  int getWidthMinimum() {
    GC gc = new GC(this);
    try {
      FontMetrics fm = gc.getFontMetrics();
      int width = 0;
      if (isVertical()) {
        width = countTicChars()*fm.getAverageCharWidth()+fm.getHeight();
        if (_label!=null)
          width += gc.stringExtent(_label).y;
      } else {
        width = 20;
        if (_label!=null)
          width = Math.max(width,gc.stringExtent(_label).x);
      }
      return width;
    } finally {
      gc.dispose();
    }
  }

  /**
   * Gets the height minimum for this axis. This height does not include 
   * any border that will be drawn by the mosaic.
   */
  int getHeightMinimum() {
    GC gc = new GC(this);
    try {
      FontMetrics fm = gc.getFontMetrics();
      int height = 0;
      if (isHorizontal()) {
        height = 3*fm.getHeight();
        if (_label!=null)
          height += gc.stringExtent(_label).y;
      } else {
        height = 20;
        if (_label!=null)
          height = Math.max(height,gc.stringExtent(_label).x);
      }
      return height;
    } finally {
      gc.dispose();
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  /**
   * Paints this axis. Typically, this method is called by our paint 
   * listener, in response to a paint event. However, it may also be 
   * called by our mosaic, if exporting to an image or other drawable.
   * @param gc the GC with which to paint.
   * @param width the width of the drawable, in pixels.
   * @param height the height of the drawable, in pixels.
   */
  void paint(GC gc, int width, int height) {
    gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_RED));
    gc.fillRectangle(0,0,width,height);
    Point extent = gc.stringExtent("Axis");
    int x = width/2-extent.x/2;
    int y = height/2-extent.y/2;
    gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
    gc.drawString("Axis",x,y);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private int _placement;
  private int _index;
  private String _label;

  private void onPaint(Event e) {
    PaintEvent pe = new PaintEvent(e);
    Point size = getSize();
    paint(pe.gc,size.x,size.y);
  }

  private boolean isHorizontal() {
    return _placement==TOP || _placement==BOTTOM;
  }

  private boolean isVertical() {
    return _placement==LEFT || _placement==RIGHT;
  }

  private int countTicChars() {
    return 8;
  }
}
