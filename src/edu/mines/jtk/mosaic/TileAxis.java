/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import javax.swing.*;

/**
 * A tile axis in a mosaic.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.27
 */
public class TileAxis extends JPanel {

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
    if (_mosaic!=null)
      _mosaic.validate();
  }

  public void setLabel(String label) {
    _label = label;
    _mosaic.validate();
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  TileAxis(Mosaic mosaic, int placement, int index) {
    _mosaic = mosaic;
    _placement = placement;
    _index = index;
    mosaic.add(this);
  }

  /**
   * Gets the width minimum for this axis. This width does not include 
   * any border that will be drawn by the mosaic.
   */
  int getWidthMinimum() {
    Graphics g = getGraphics();
    FontMetrics fm = g.getFontMetrics();
    int width = 0;
    if (isVertical()) {
      width = countTicChars()*fm.charWidth('3')+fm.getHeight();
      if (_label!=null)
        width += fm.getHeight();
    } else {
      width = 20;
      if (_label!=null)
        width = Math.max(width,fm.stringWidth(_label));
    }
    g.dispose();
    return width;
  }

  /**
   * Gets the height minimum for this axis. This height does not include 
   * any border that will be drawn by the mosaic.
   */
  int getHeightMinimum() {
    Graphics g = getGraphics();
    FontMetrics fm = g.getFontMetrics();
    int height = 0;
    if (isHorizontal()) {
      height = 3*fm.getHeight();
      if (_label!=null)
        height += fm.getHeight();
    } else {
      height = 20;
      if (_label!=null)
        height = Math.max(height,fm.stringWidth(_label));
    }
    g.dispose();
    return height;
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int width = getWidth();
    int height = getHeight();
    g.setColor(Color.RED);
    g.fillRect(0,0,width,height);
    FontMetrics fm = g.getFontMetrics();
    int sw = fm.stringWidth("Axis");
    int sh = fm.getAscent();
    int x = width/2-sw/2;
    int y = height/2+sh/2;
    g.setColor(Color.BLACK);
    g.drawString("Axis",x,y);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private int _placement;
  private int _index;
  private String _label;

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
