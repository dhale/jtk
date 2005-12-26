/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.font.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.gui.*;
import static java.lang.Math.*;

/**
 * A plot frame contains a mosaic and optional color bar and/or title.
 * The plot frame's mosaic may contain any number or rows and columns of 
 * tiles. Each tile may contain any number of tiled views.
 * <p>
 * Tiles have axes x1 and x2. By default, the x1 axis increases toward
 * the right and the x2 axis increases toward the top of each tile in a
 * mosaic. In this default X1RIGHT_X2UP orientation, the coordinates 
 * (x1,x2) correspond to conventional (x,y) coordinates. An alternative
 * orientation is X1DOWN_X2RIGHT, which is useful when the x1 axis 
 * corresponds to, say, a depth coordinate z.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.25
 */
public class PlotFrame extends JFrame {
  private static final long serialVersionUID = 1L;

  /**
   * Orientation of axes x1 and x2. For example, the default orientation 
   * X1RIGHT_X2UP corresponds to x1 increasing horizontally from left to 
   * right, and x2 increasing vertically from bottom to top.
   */
  public enum Orientation {
    X1RIGHT_X2UP,
    X1DOWN_X2RIGHT
  }

  /**
   * Constructs a new plot frame with a mosaic of one tile.
   */
  public PlotFrame() {
    this(1,1,Orientation.X1RIGHT_X2UP);
  }

  /**
   * Constructs a new plot frame with a mosaic of nrow by ncol tiles.
   * @param nrow the number of rows.
   * @param ncol the number of columns.
   */
  public PlotFrame(int nrow, int ncol) {
    this(nrow,ncol,Orientation.X1RIGHT_X2UP);
  }

  /**
   * Constructs a new plot frame with a mosaic of one tile.
   * @param orientation the plot orientation.
   */
  public PlotFrame(Orientation orientation) {
    this(1,1,orientation);
  }

  /**
   * Constructs a new plot frame with a mosaic of nrow by ncol tiles.
   * @param nrow the number of rows.
   * @param ncol the number of columns.
   * @param orientation the plot orientation.
   */
  public PlotFrame(int nrow, int ncol, Orientation orientation) {
    super();
    _orientation = orientation;
    _plot = new IPanel();
    _plot.setLayout(new GridBagLayout());
    Set<Mosaic.AxesPlacement> axesPlacement;
    if (orientation==Orientation.X1DOWN_X2RIGHT) {
      axesPlacement = EnumSet.of(
        Mosaic.AxesPlacement.LEFT,
        Mosaic.AxesPlacement.TOP
      );
    } else {
      axesPlacement = EnumSet.of(
        Mosaic.AxesPlacement.LEFT,
        Mosaic.AxesPlacement.BOTTOM
      );
    }
    _mosaic = new Mosaic(nrow,ncol,axesPlacement);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 100;
    gbc.weighty = 100;
    gbc.fill = GridBagConstraints.BOTH;
    _plot.add(_mosaic,gbc);
    _plot.revalidate();
    this.setLayout(new BorderLayout());
    this.add(_plot,BorderLayout.CENTER);
    this.setSize(200+300*ncol,100+300*nrow);
    this.setVisible(true);
    ModeManager modeManager = _mosaic.getModeManager();
    TileZoomMode zoomMode = new TileZoomMode(modeManager);
    zoomMode.setActive(true);
  }

  /**
   * Adds the color bar with no label. The color bar paints the color map 
   * of the most recently added pixels view. To avoid confusion, a color 
   * bar should not be added when this plot frame contains more than one 
   * pixels view.
   */
  public ColorBar addColorBar() {
    return addColorBar(null);
  }

  /**
   * Adds the color bar with specified label.
   * @param label the label; null, if none.
   */
  public ColorBar addColorBar(String label) {
    _colorBarLabel = label;
    if (_colorBar==null) {
      _colorBar = new ColorBar(label);
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
        _plot.add(_colorBar,gbc);
        _plot.revalidate();
      }
    } else {
      _colorBar.setLabel(label);
    }
    return _colorBar;
  }

  /**
   * Removes the color bar.
   */
  public void removeColorBar() {
    if (_colorBar!=null) {
      _plot.remove(_colorBar);
      _plot.revalidate();
      _colorBar = null;
    }
  }

  /**
   * Adds the plot title.
   * @param title the title; null, if none.
   */
  public void addTitle(String title) {
    setTitle(title);
  }

  /**
   * Sets the plot title.
   * @param title the title; null, if none.
   */
  public void setTitle(String title) {
    if (title!=null) {
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
        _plot.add(_title,gbc);
        _plot.revalidate();
      } else {
        _title.set(title);
      }
    } else if (_title!=null) {
      _plot.remove(_title);
      _plot.revalidate();
    }
  }

  /**
   * Removes the plot title.
   */
  public void removeTitle() {
    setTitle(null);
  }

  /**
   * Sets the label for the horizontal axis.
   * @param label the label.
   */
  public void setHLabel(String label) {
    setHLabel(0,label);
  }

  /**
   * Sets the label for the vertical axis.
   * @param label the label.
   */
  public void setVLabel(String label) {
    setVLabel(0,label);
  }

  /**
   * Sets the label for the horizontal axis in the specified column.
   * @param icol the column index.
   * @param label the label.
   */
  public void setHLabel(int icol, String label) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      _mosaic.getTileAxisTop(icol).setLabel(label);
    } else {
      _mosaic.getTileAxisBottom(icol).setLabel(label);
    }
    if (_colorBar!=null) {
      removeColorBar();
      addColorBar(_colorBarLabel);
    }
  }

  /**
   * Sets the label for the vertical axis in the specified row.
   * @param irow the row index.
   * @param label the label.
   */
  public void setVLabel(int irow, String label) {
    _mosaic.getTileAxisLeft(irow).setLabel(label);
  }

  /**
   * Sets the label for the X1 axis.
   * @param label the label.
   */
  public void setX1Label(String label) {
    setX1Label(0,label);
  }

  /**
   * Sets the label for the X2 axis.
   * @param label the label.
   */
  public void setX2Label(String label) {
    setX2Label(0,label);
  }

  /**
   * Sets the label for X1 axis with specified index.
   * @param index the row or column index.
   * @param label the label.
   */
  public void setX1Label(int index, String label) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setVLabel(index,label);
    } else {
      setHLabel(index,label);
    }
  }

  /**
   * Sets the label for X1 axis with specified index.
   * @param index the row or column index.
   * @param label the label.
   */
  public void setX2Label(int index, String label) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setHLabel(index,label);
    } else {
      setVLabel(index,label);
    }
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
    if (_orientation==Orientation.X1RIGHT_X2UP) {
      _pixelsView.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
    } else if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      _pixelsView.setOrientation(PixelsView.Orientation.X1DOWN_X2RIGHT);
    }
    if (_colorBar!=null)
      _pixelsView.addColorMapListener(_colorBar);
    _mosaic.getTile(irow,icol).addTiledView(_pixelsView);
    return _pixelsView;
  }

  /**
   * Paints this panel to a PNG image with specified resolution and width.
   * The image height is computed so that the image has the same aspect 
   * ratio as this panel.
   * @param dpi the image resolution, in dots per inch.
   * @param win the image width, in inches.
   * @param fileName the name of the file to contain the PNG image.  
   */
  public void paintToPng(double dpi, double win, String fileName) 
    throws IOException 
  {
    _plot.paintToPng(dpi,win,fileName);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private PixelsView _pixelsView;
  private ColorBar _colorBar;
  private String _colorBarLabel;
  private Title _title;
  private IPanel _plot;
  private Orientation _orientation;

  /**
   * Internal class for plot title.
   */
  private class Title extends IPanel {
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
