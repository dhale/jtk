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
import static java.lang.Math.*;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.gui.*;
import edu.mines.jtk.util.*;

/**
 * A plot panel is a panel that contains a mosaic of 2-D graphical views.
 * A plot panel may also contain a color bar and/or title. The plot panel's
 * mosaic may contain any number or rows and columns of tiles. Each tile 
 * may contain any number of tiled graphical views.
 * <p>
 * The primary purpose of this class is ease-of-use. A plot panel handles
 * much of the work of constructing a mosaic of tiled graphical views.
 * <p>
 * One consequence of ease-of-use is that some of the methods provided 
 * by this class are redundant. For example, some methods have (irow,icol) 
 * parameters that specify the row and column indices of a tile. These 
 * parameters are useful for mosaics with more than one tile. However, the 
 * most common case is a mosaic with only one tile; and, for this case, a 
 * corresponding method without (irow,icol) parameters is provided as well. 
 * The latter method simply calls the former with (irow,icol) = (0,0).
 * <p>
 * An important property of a plot panel is the orientation of its axes.
 * Tiles have axes x1 and x2. By default, the x1 axis increases toward
 * the right and the x2 axis increases toward the top of each tile in a
 * mosaic. In this default X1RIGHT_X2UP orientation, the coordinates 
 * (x1,x2) correspond to conventional (x,y) coordinates. An alternative
 * orientation is X1DOWN_X2RIGHT, which is useful when the x1 axis 
 * corresponds to, say, a depth coordinate z.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.25
 */
public class PlotPanel extends IPanel {
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
   * Constructs a new plot panel with a mosaic of one tile.
   * Uses the default orientation X1RIGHT_X2UP.
   */
  public PlotPanel() {
    this(1,1,Orientation.X1RIGHT_X2UP);
  }

  /**
   * Constructs a new plot panel with a mosaic of nrow by ncol tiles.
   * Uses the default orientation X1RIGHT_X2UP.
   * @param nrow the number of rows.
   * @param ncol the number of columns.
   */
  public PlotPanel(int nrow, int ncol) {
    this(nrow,ncol,Orientation.X1RIGHT_X2UP);
  }

  /**
   * Constructs a new plot panel with a mosaic of one tile.
   * @param orientation the plot orientation.
   */
  public PlotPanel(Orientation orientation) {
    this(1,1,orientation);
  }

  /**
   * Constructs a new plot panel with a mosaic of nrow by ncol tiles.
   * @param nrow the number of rows.
   * @param ncol the number of columns.
   * @param orientation the plot orientation.
   */
  public PlotPanel(int nrow, int ncol, Orientation orientation) {
    super();
    _orientation = orientation;
    this.setLayout(new GridBagLayout());
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
    this.add(_mosaic,gbc);
    this.setPreferredSize(new Dimension(200+300*ncol,100+300*nrow));
    //this.revalidate();
  }

  /**
   * Gets the mosaic. The mosaic contains one or more tiles.
   * @return the mosaic.
   */
  public Mosaic getMosaic() {
    return _mosaic;
  }

  /**
   * Gets the tile with specified row and column indices.
   * @param irow the row index.
   * @param icol the column index.
   * @return the tile.
   */
  public Tile getTile(int irow, int icol) {
    return _mosaic.getTile(irow,icol);
  }

  /**
   * Adds the color bar with no label. The color bar paints the color map 
   * of the most recently added pixels view. To avoid confusion, a color 
   * bar should perhaps not be added when this plot panel contains more 
   * than one pixels view.
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
      _colorBar.setFont(getFont());
      _colorBar.setForeground(getForeground());
      _colorBar.setBackground(getBackground());
      if (_colorBarFormat!=null)
        _colorBar.setFormat(_colorBarFormat);
      if (_colorBarPixelsView!=null) {
        _colorBarPixelsView.addColorMapListener(_colorBar);
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
        this.revalidate();
      }
    } else {
      _colorBar.setLabel(label);
      this.revalidate();
    }
    return _colorBar;
  }

  /**
   * Sets the format for major tic annotation of the color bar.
   * The default format is "%1.4G", which yields a minimum of 1 digit,
   * with up to 4 digits of precision. Any trailing zeros and decimal
   * point are removed from tic annotation.
   * @param format the format.
   */
  public void setColorBarFormat(String format) {
    _colorBarFormat = format;
    if (_colorBar!=null) {
      _colorBar.setFormat(format);
      this.revalidate();
    }
  }

  /**
   * Removes the color bar.
   */
  public void removeColorBar() {
    if (_colorBar!=null) {
      if (_colorBarPixelsView!=null)
        _colorBarPixelsView.removeColorMapListener(_colorBar);
      this.remove(_colorBar);
      this.revalidate();
      _colorBar = null;
    }
  }

  /**
   * Adds the plot title. Equivalent to {@link #setTitle(String)}.
   * The title font is 1.5 times larger than the font of this panel.
   * @param title the title; null, if none.
   */
  public void addTitle(String title) {
    setTitle(title);
  }

  /**
   * Sets the plot title. Equivalent to {@link #addTitle(String)}.
   * @param title the title; null, for no title.
   */
  public void setTitle(String title) {
    if (title!=null) {
      if (_title==null) {
        _title = new Title(title);
        Font font = getFont();
        font = font.deriveFont(1.5f*font.getSize2D());
        _title.setFont(getFont());
        _title.setForeground(getForeground());
        _title.setBackground(getBackground());
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
        this.revalidate();
      } else {
        _title.set(title);
      }
    } else if (_title!=null) {
      this.remove(_title);
      this.revalidate();
      _title = null;
    }
  }

  /**
   * Removes the plot title. Equivalent to calling the method
   * {@link #setTitle(String)} with a null title.
   */
  public void removeTitle() {
    setTitle(null);
  }

  /**
   * Sets limits for the horizontal axis.
   * By default, limits are computed automatically by tiled graphical views.
   * This method can be used to override those default limits.
   * @param hmin the minimum value.
   * @param hmax the maximum value.
   */
  public void setHLimits(double hmin, double hmax) {
    setHLimits(0,hmin,hmax);
  }

  /**
   * Sets limits for the vertical axis.
   * By default, limits are computed automatically by tiled graphical views.
   * This method can be used to override those default limits.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   */
  public void setVLimits(double vmin, double vmax) {
    setVLimits(0,vmin,vmax);
  }

  /**
   * Sets limits for the horizontal axis in the specified column.
   * By default, limits are computed automatically by tiled graphical views.
   * This method can be used to override those default limits.
   * @param icol the column index.
   * @param hmin the minimum value.
   * @param hmax the maximum value.
   */
  public void setHLimits(int icol, double hmin, double hmax) {
    Check.argument(hmin<hmax,"hmin<hmax");
    setBestHorizontalProjector(icol,new Projector(hmin,hmax));
  }

  /**
   * Sets limits for the vertical axis in the specified row.
   * By default, limits are computed automatically by tiled graphical views.
   * This method can be used to override those default limits.
   * @param irow the row index.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   */
  public void setVLimits(int irow, double vmin, double vmax) {
    Check.argument(vmin<vmax,"vmin<vmax");
    if (_orientation==Orientation.X1RIGHT_X2UP) {
      setBestVerticalProjector(irow,new Projector(vmax,vmin));
    } else {
      setBestVerticalProjector(irow,new Projector(vmin,vmax));
    }
  }

  /**
   * Sets default limits for the horizontal axis. This method may be used 
   * to restore default limits after they have been set explicitly.
   */
  public void setHLimitsDefault() {
    setHLimitsDefault(0);
  }

  /**
   * Sets default limits for the vertical axis. This method may be used 
   * to restore default limits after they have been set explicitly.
   */
  public void setVLimitsDefault() {
    setVLimitsDefault(0);
  }

  /**
   * Sets default limits for the horizontal axis in the specified column.
   * This method may be used to restore default limits after they have 
   * been set explicitly.
   * @param icol the column index.
   */
  public void setHLimitsDefault(int icol) {
    setBestHorizontalProjector(icol,null);
  }

  /**
   * Sets default limits for the vertical axis in the specified column.
   * This method may be used to restore default limits after they have 
   * been set explicitly.
   * @param irow the row index.
   */
  public void setVLimitsDefault(int irow) {
    setBestVerticalProjector(irow,null);
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
    adjustColorBar();
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
   * Sets the format for the horizontal axis.
   * @param format the format.
   */
  public void setHFormat(String format) {
    setHFormat(0,format);
  }

  /**
   * Sets the format for the vertical axis.
   * @param format the format.
   */
  public void setVFormat(String format) {
    setVFormat(0,format);
  }

  /**
   * Sets the format for the horizontal axis in the specified column.
   * @param icol the column index.
   * @param format the format.
   */
  public void setHFormat(int icol, String format) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      _mosaic.getTileAxisTop(icol).setFormat(format);
    } else {
      _mosaic.getTileAxisBottom(icol).setFormat(format);
    }
  }

  /**
   * Sets the format for the vertical axis in the specified row.
   * @param irow the row index.
   * @param format the format.
   */
  public void setVFormat(int irow, String format) {
    _mosaic.getTileAxisLeft(irow).setFormat(format);
  }

  /**
   * Adds a grid view.
   * @return the grid view.
   */
  public GridView addGrid() {
    return addGrid(0,0);
  }

  /**
   * Adds a grid view with specified parameters string. 
   * For the format of the parameters string, see 
   * {@link edu.mines.jtk.mosaic.GridView#setParameters(String)}.
   * @param parameters the parameters string.
   * @return the grid view.
   */
  public GridView addGrid(String parameters) {
    return addGrid(0,0,parameters);
  }

  /**
   * Adds a grid view.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @return the grid view.
   */
  public GridView addGrid(int irow, int icol) {
    GridView gv = new GridView();
    return addGridView(irow,icol,gv);
  }

  /**
   * Adds a grid view with specified parameters string.
   * For the format of the parameters string, see 
   * {@link edu.mines.jtk.mosaic.GridView#setParameters(String)}.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param parameters the parameters string.
   * @return the grid view.
   */
  public GridView addGrid(int irow, int icol, String parameters) {
    GridView gv = new GridView(parameters);
    return addGridView(irow,icol,gv);
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   * @return the pixels view.
   */
  public PixelsView addPixels(float[][] f) {
    return addPixels(0,0,f);
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   * @return the pixels view.
   */
  public PixelsView addPixels(Sampling s1, Sampling s2, float[][] f) {
    return addPixels(0,0,s1,s2,f);
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param f array[n2][n1] of sampled function values f(x1,x2), 
   *  where n1 = f[0].length and n2 = f.length.
   * @return the pixels view.
   */
  public PixelsView addPixels(int irow, int icol, float[][] f) {
    PixelsView pv = new PixelsView(f);
    return addPixelsView(irow,icol,pv);
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[n2][n1] of sampled function values f(x1,x2), 
   *  where n1 = f[0].length and n2 = f.length.
   * @return the pixels view.
   */
  public PixelsView addPixels(
    int irow, int icol, Sampling s1, Sampling s2, float[][] f) {
    PixelsView pv = new PixelsView(s1,s2,f);
    return addPixelsView(irow,icol,pv);
  }

  /**
   * Adds a points view of the arrays x1 and x2 of point (x1,x2) coordinates.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   * @return the points view.
   */
  public PointsView addPoints(float[] x1, float[] x2) {
    return addPoints(0,0,x1,x2);
  }

  /**
   * Adds a points view of (x1,x2) with specified x2 coordinates.
   * The corresponding coordinates x1 are assumed to be 0, 1, 2, ....
   * @param x2 array of x2 coordinates.
   */
  public PointsView addPoints(float[] x2) {
    return addPoints(0,0,x2);
  }

  /**
   * Adds a view of points (x1,x2) for a sampled function x2(x1).
   * @param s1 the sampling of x1 coordinates.
   * @param x2 array of x2 coordinates.
   */
  public PointsView addPoints(Sampling s1, float[] x2) {
    return addPoints(0,0,s1,x2);
  }

  /**
   * Adds a points view of the arrays x1 and x2 of point (x1,x2) coordinates.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   * @return the points view.
   */
  public PointsView addPoints(int irow, int icol, float[] x1, float[] x2) {
    PointsView pv = new PointsView(x1,x2);
    return addPointsView(irow,icol,pv);
  }

  /**
   * Adds a points view of (x1,x2) with specified x2 coordinates.
   * The corresponding coordinates x1 are assumed to be 0, 1, 2, ....
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param x2 array of x2 coordinates.
   */
  public PointsView addPoints(int irow, int icol, float[] x2) {
    PointsView pv = new PointsView(x2);
    return addPointsView(irow,icol,pv);
  }

  /**
   * Adds a view of points (x1,x2) for a sampled function x2(x1).
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param s1 the sampling of x1 coordinates.
   * @param x2 array of x2 coordinates.
   */
  public PointsView addPoints(int irow, int icol, Sampling s1, float[] x2) {
    PointsView pv = new PointsView(s1,x2);
    return addPointsView(irow,icol,pv);
  }

  /**
   * Adds a sequence view with specified values f(x).
   * Uses default sampling of x = 0, 1, 2, ....
   * @param f array of sampled function values f(x).
   */
  public SequenceView addSequence(float[] f) {
    return addSequence(0,0,f);
  }

  /**
   * Adds a sequence view with specified sampling and values f(x).
   * @param sx the sampling of the variable x.
   * @param f array of sampled function values f(x).
   */
  public SequenceView addSequence(Sampling sx, float[] f) {
    return addSequence(0,0,sx,f);
  }

  /**
   * Adds a sequence view with specified values f(x).
   * Uses default sampling of x = 0, 1, 2, ....
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param f array of sampled function values f(x).
   */
  public SequenceView addSequence(int irow, int icol, float[] f) {
    SequenceView sv = new SequenceView(f);
    return addSequenceView(irow,icol,sv);
  }

  /**
   * Adds a sequence view with specified sampling and values f(x).
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param sx the sampling of the variable x.
   * @param f array of sampled function values f(x).
   */
  public SequenceView addSequence(int irow, int icol, Sampling sx, float[] f) {
    SequenceView sv = new SequenceView(sx,f);
    return addSequenceView(irow,icol,sv);
  }

  /**
   * Removes the specified tiled view from this plot panel.
   * @param tv the tiled view.
   * @return true, if this panel contained the specified tiled view; 
   *  false, otherwise.
   */
  public boolean remove(TiledView tv) {
    int nrow = _mosaic.countRows();
    int ncol = _mosaic.countColumns();
    for (int irow=0; irow<nrow; ++irow) {
      for (int icol=0; icol<ncol; ++icol) {
        if (getTile(irow,icol).removeTiledView(tv))
          return true;
      }
    }
    return false;
  }

  /**
   * Sets the font in all components of this panel.
   * Sets the title font to be 1.5 times larger than the specified font.
   * @param font the font.
   */
  public void setFont(Font font) {
    super.setFont(font);
    if (_mosaic!=null)
      _mosaic.setFont(font);
    if (_colorBar!=null)
      _colorBar.setFont(font);
    if (_title!=null)
      _title.setFont(font.deriveFont(1.5f*font.getSize2D()));
    adjustColorBar();
    revalidate();
  }

  /**
   * Sets the foreground color in all components of this panel.
   * @param color the foreground color.
   */
  public void setForeground(Color color) {
    super.setForeground(color);
    if (_mosaic!=null)
      _mosaic.setForeground(color);
    if (_colorBar!=null)
      _colorBar.setForeground(color);
    if (_title!=null)
      _title.setForeground(color);
  }

  /**
   * Sets the background color in all components of this panel.
   * @param color the background color.
   */
  public void setBackground(Color color) {
    super.setBackground(color);
    if (_mosaic!=null)
      _mosaic.setBackground(color);
    if (_colorBar!=null)
      _colorBar.setBackground(color);
    if (_title!=null)
      _title.setBackground(color);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private ColorBar _colorBar;
  private String _colorBarFormat;
  private String _colorBarLabel;
  private PixelsView _colorBarPixelsView;
  private Title _title;
  private Orientation _orientation;

  /**
   * Internal class for plot title.
   */
  private class Title extends IPanel {
    String text;

    Title(String text) {
      this.text = text;
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

  private GridView addGridView(int irow, int icol, GridView gv) {
    _mosaic.getTile(irow,icol).addTiledView(gv);
    return gv;
  }

  private PixelsView addPixelsView(int irow, int icol, PixelsView pv) {
    if (_colorBar!=null && _colorBarPixelsView!=null)
      _colorBarPixelsView.removeColorMapListener(_colorBar);
    _colorBarPixelsView = pv;
    if (_orientation==Orientation.X1RIGHT_X2UP) {
      _colorBarPixelsView.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
    } else if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      _colorBarPixelsView.setOrientation(PixelsView.Orientation.X1DOWN_X2RIGHT);
    }
    if (_colorBar!=null)
      _colorBarPixelsView.addColorMapListener(_colorBar);
    _mosaic.getTile(irow,icol).addTiledView(_colorBarPixelsView);
    return _colorBarPixelsView;
  }

  private PointsView addPointsView(int irow, int icol, PointsView pv) {
    if (_orientation==Orientation.X1RIGHT_X2UP) {
      pv.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
    } else if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      pv.setOrientation(PointsView.Orientation.X1DOWN_X2RIGHT);
    }
    _mosaic.getTile(irow,icol).addTiledView(pv);
    return pv;
  }

  private SequenceView addSequenceView(int irow, int icol, SequenceView sv) {
    _mosaic.getTile(irow,icol).addTiledView(sv);
    return sv;
  }

  private void setBestHorizontalProjector(int icol, Projector p) {
    int nrow = getMosaic().countRows();
    for (int irow=0; irow<nrow; ++irow)
      getTile(irow,icol).setBestHorizontalProjector(p);
  }

  private void setBestVerticalProjector(int irow, Projector p) {
    int ncol = getMosaic().countColumns();
    for (int icol=0; icol<ncol; ++icol)
      getTile(irow,icol).setBestVerticalProjector(p);
  }

  /**
   * Called when the color bar in this panel may need resizing.
   * This implementation simply removes and adds any existing color bar.
   */
  private void adjustColorBar() {
    if (_colorBar!=null) {
      removeColorBar();
      addColorBar(_colorBarLabel);
    }
  }
}
