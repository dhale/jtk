/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.font.*;
import java.util.*;
import javax.swing.*;
import edu.mines.jtk.dsp.Sampling;
import static java.lang.Math.*;

/**
 * A plot panel consists of a mosaic and optional color bar and/or title.
 * The mosaic may contain any number or rows and columns of tiles.
 * Each tile may paint any number of tiled views.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.24
 */
public class PlotPanel extends IPanel {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new plot panel with a mosaic of one tile.
   */
  public PlotPanel() {
    this(1,1);
  }

  /**
   * Constructs a new plot panel with a mosaic of nrow by ncol tiles.
   * @param nrow the number of rows.
   * @param ncol the number of columns.
   */
  public PlotPanel(int nrow, int ncol) {
    super();
    Set<Mosaic.AxesPlacement> axesPlacement = EnumSet.of(
      Mosaic.AxesPlacement.LEFT,
      Mosaic.AxesPlacement.BOTTOM
    );
    _mosaic = new Mosaic(nrow,ncol,axesPlacement);
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 100;
    gbc.weighty = 100;
    gbc.fill = GridBagConstraints.BOTH;
    this.add(_mosaic,gbc);
  }

  /**
   * Adds the color bar. The color bar displays the color map of the most 
   * recently added pixels view. To avoid confusion, a color bar should
   * not be added when more than one pixels view is painted.
   */
  public void addColorBar() {
    if (_colorBar==null) {
      _colorBar = new ColorBar();
      if (_pixelsView!=null) {
        _pixelsView.addColorMapListener(_colorBar);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.EAST;
        int top = _mosaic.getHeightAxesTop();
        int left = 25;
        int bottom = _mosaic.getHeightAxesBottom();
        int right = 0;
        gbc.insets = new Insets(top,left,bottom,right);
        this.add(_colorBar,gbc);
      }
    }
  }

  /**
   * Removes the color bar.
   */
  public void removeColorBar() {
    if (_colorBar!=null) {
      this.remove(_colorBar);
      _colorBar = null;
    }
  }

  /**
   * Sets the plot title.
   * @param title the title.
   */
  public void setTitle(String title) {
    if (_title==null) {
      _title = new Title(title);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.fill = GridBagConstraints.BOTH;
      int top = 0;
      int left = _mosaic.getWidthAxesLeft();
      int bottom = 0;
      int right = _mosaic.getWidthAxesRight();
      gbc.insets = new Insets(top,left,bottom,right);
      this.add(_title,gbc);
    }
    _title.set(title);
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   */
  public PixelsView addPixels(float[][] f) {
    return addPixels(0,0,f);
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   */
  public PixelsView addPixels(int irow, int icol, float[][] f) {
    if (_colorBar!=null && _pixelsView!=null)
      _pixelsView.removeColorMapListener(_colorBar);
    _pixelsView = new PixelsView(f);
    if (_colorBar!=null)
      _pixelsView.addColorMapListener(_colorBar);
    _mosaic.getTile(irow,icol).addTiledView(_pixelsView);
    return _pixelsView;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private PixelsView _pixelsView;
  private ColorBar _colorBar;
  private Title _title;

  /**
   * Internal class for plot title.
   */
  private static class Title extends IPanel {
    String text;

    Title(String text) {
      this.text = text;
      Font font = getFont();
      font = font.deriveFont(1.5f*font.getSize());
      setFont(font);
    }

    void set(String text) {
      this.text = text;
      repaint();
    }

    public void paintToRect(Graphics2D g2d, int x, int y, int w, int h) {
      g2d = createGraphics(g2d,x,y,w,h);
      g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

      Font font = g2d.getFont();
      FontMetrics fm = g2d.getFontMetrics();
      FontRenderContext frc = g2d.getFontRenderContext();
      LineMetrics lm = font.getLineMetrics(text,frc);
      int fh = round(lm.getHeight());
      int fa = round(lm.getAscent());
      int fd = round(lm.getDescent());

      int wt = fm.stringWidth(text);
      int xt = max(0,min(w-wt,(w-wt)/2));
      int yt = h-1-2*fd;
      g2d.drawString(text,xt,yt);
      
      g2d.dispose();
    }

    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      paintToRect((Graphics2D)g,0,0,getWidth(),getHeight());
    }

    public Dimension getMinimumSize() {
      if (isMinimumSizeSet()) {
        return super.getMinimumSize();
      } else {
        Font font = getFont();
        FontMetrics fm = getFontMetrics(font);
        int fh = fm.getHeight();
        int fd = fm.getDescent();
        int wt = fm.stringWidth(text);
        return new Dimension(wt,fd+fh);
      }
    }
    public Dimension getPreferredSize() {
      if (isPreferredSizeSet()) {
        return super.getPreferredSize();
      } else {
        return getMinimumSize();
      }
    }
  }
}
