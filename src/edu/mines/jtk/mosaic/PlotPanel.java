/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import edu.mines.jtk.dsp.Sampling;

/**
 * A plot panel consists of a mosaic and optional color bar and title.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.24
 */
public class PlotPanel extends IPanel {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new plot panel.
   */
  public PlotPanel() {
    super();
    Set<Mosaic.AxesPlacement> axesPlacement = EnumSet.of(
      Mosaic.AxesPlacement.LEFT,
      Mosaic.AxesPlacement.BOTTOM
    );
    _mosaic = new Mosaic(1,1,axesPlacement);
    _tile = _mosaic.getTile(0,0);
    this.setLayout(new BorderLayout());
    this.add(_mosaic,BorderLayout.CENTER);
  }

  /**
   * Adds a color bar.
   */
  public void addColorBar() {
    if (_colorBar==null) {
      _colorBar = new ColorBar();
      if (_pixelsView!=null) {
        _pixelsView.addColorMapListener(_colorBar);
        this.add(_colorBar,BorderLayout.EAST);
      }
    }
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   */
  public void addPixels(float[][] f) {
    _pixelsView = new PixelsView(f);
    if (_colorBar!=null)
      _pixelsView.addColorMapListener(_colorBar);
    _tile.addTiledView(_pixelsView);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private Tile _tile;
  private PixelsView _pixelsView;
  private ColorBar _colorBar;
}
