/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.IndexColorModel;
import java.util.EnumSet;
import java.util.Set;
import static java.lang.Math.*;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.awt.ColorMapped;
import edu.mines.jtk.awt.ColorMapListener;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Check;

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
 * @version 2009.06.19
 */
public class PlotPanel extends IPanel {
  private static final long serialVersionUID = 1L;

  /**
   * Placement of labeled axes in mosaic. The default axes placement
   * is two axes placed depending on the plot orientation.
   */
  public enum AxesPlacement {
    LEFT_TOP,
    LEFT_BOTTOM,
    NONE
  }

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

  public PlotPanel(int nrow, int ncol, Orientation orientation) {
    this(nrow,ncol,orientation,axesPlacement(orientation));
  }

  private static AxesPlacement axesPlacement(Orientation orientation) {
    AxesPlacement axesPlacement;
    if (orientation==Orientation.X1DOWN_X2RIGHT) {
      axesPlacement = AxesPlacement.LEFT_TOP;
    } else {
      axesPlacement = AxesPlacement.LEFT_BOTTOM;
    }
    return axesPlacement;
  }

  /**
   * Constructs a new plot panel with a mosaic of nrow by ncol tiles.
   * @param nrow the number of rows.
   * @param ncol the number of columns.
   * @param orientation the plot orientation.
   * @param axesPlacement the placement of axes.
   */
  public PlotPanel(
    int nrow, int ncol, 
    Orientation orientation, 
    AxesPlacement axesPlacement)
  {
    super();
    _orientation = orientation;
    _axesPlacement = axesPlacement;
    setLayout(new GridBagLayout());
    Set<Mosaic.AxesPlacement> axesPlacementSet;
    if (axesPlacement==AxesPlacement.LEFT_TOP) {
      axesPlacementSet = EnumSet.of(
        Mosaic.AxesPlacement.LEFT,
        Mosaic.AxesPlacement.TOP
      );
    } else if (axesPlacement==AxesPlacement.LEFT_BOTTOM) {
      axesPlacementSet = EnumSet.of(
        Mosaic.AxesPlacement.LEFT,
        Mosaic.AxesPlacement.BOTTOM
      );
    } else {
      axesPlacementSet = EnumSet.noneOf(Mosaic.AxesPlacement.class);
    }
    _mosaic = new Mosaic(nrow,ncol,axesPlacementSet);
    _colorMapHandler = new ColorMapHandler();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 100;
    gbc.weighty = 100;
    gbc.fill = GridBagConstraints.BOTH;
    add(_mosaic,gbc);
    setPreferredSize(new Dimension(200+300*ncol,100+300*nrow));
    revalidate();
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
   * bar should perhaps not be added when this plot panel contains multiple 
   * pixels views with different color maps..
   * @return the color bar.
   */
  public ColorBar addColorBar() {
    return addColorBar(null,null);
  }

  /**
   * Adds the color bar with specified label.
   * @param label the label; null, if none.
   * @return the color bar.
   */
  public ColorBar addColorBar(String label) {
    return addColorBar(null,label);
  }

  /**
   * Adds a color bar with a specified color mapped object and no label.
   * @param cm the specified color mapped.
   * @return the color bar.
   */
  public ColorBar addColorBar(ColorMapped cm) {
    return addColorBar(cm,null);
  }

  /**
   * Adds a color bar with a specified color mapped object and label.  
   * If the specified color mapped object is null, then this plot panel
   * will try to find the best color map to display in the color bar.
   * @param cm the color mapped object.
   * @param label the label.
   * @return the color bar.
   */
  public ColorBar addColorBar(ColorMapped cm, String label) {
    if (cm!=null) {
      _autoColorMapped = false;
      _colorMapped = cm;
    } else {
      _colorMapped = findBestColorMapped();
    }
    if (_colorBar==null) {
      _colorBar = new ColorBar(label);
      _colorBar.setFont(getFont());
      _colorBar.setForeground(getForeground());
      _colorBar.setBackground(getBackground());
      if (_colorBarFormat!=null)
        _colorBar.setFormat(_colorBarFormat);
      if (_colorBarWidthMinimum!=0)
        _colorBar.setWidthMinimum(_colorBarWidthMinimum);
      if (_colorMapped!=null)
        _colorMapped.getColorMap().addListener(_colorBar);
      add(_colorBar,makeColorBarConstraints());
    } else {
      _colorBar.setLabel(label);
    }
    revalidate();
    return _colorBar;
  }

  /**
   * Sets a minimum width (in pixels) for a color bar.
   * This method is useful when attempting to construct multiple plot 
   * panels with the same layout. In this scenario, set this minimum
   * equal to the width of the widest color bar. Then all color bars
   * will have the same width. Those widths might otherwise vary as tic 
   * and axes labels vary for the different panels.
   * @param widthMinimum the minimum width.
   */
  public void setColorBarWidthMinimum(int widthMinimum) {
    _colorBarWidthMinimum = widthMinimum;
    if (_colorBar!=null) {
      _colorBar.setWidthMinimum(widthMinimum);
      revalidate();
    }
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
      revalidate();
    }
  }

  /**
   * Removes the color bar.
   */
  public void removeColorBar() {
    if (_colorBar!=null) {
      remove(_colorBar);
      revalidate();
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
        font.deriveFont(1.5f*font.getSize2D());
        _title.setFont(getFont());
        _title.setForeground(getForeground());
        _title.setBackground(getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets.top = 0;
        gbc.insets.bottom = 0;
        gbc.insets.left = 0;
        gbc.insets.right = 0;
        //gbc.insets.left = _mosaic.getWidthAxesLeft();
        //gbc.insets.right = _mosaic.getWidthAxesRight();
        add(_title,gbc);
        revalidate();
      } else {
        _title.set(title);
      }
    } else if (_title!=null) {
      remove(_title);
      revalidate();
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
   * Sets limits for the both horizontal and vertical axes.
   * By default, limits are computed automatically by tiled graphical views.
   * This method can be used to override those default limits.
   * @param hmin the minimum value.
   * @param vmin the minimum value.
   * @param hmax the maximum value.
   * @param vmax the maximum value.
   */
  public void setLimits(double hmin, double vmin, double hmax, double vmax) {
    setHLimits(hmin,hmax);
    setVLimits(vmin,vmax);
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
   * Sets default limits for horizontal and vertical axes. This method may
   * be used to restore default limits after they have been set explicitly.
   */
  public void setLimitsDefault() {
    setHLimitsDefault();
    setVLimitsDefault();
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
    if (_axesPlacement==AxesPlacement.LEFT_TOP) {
      _mosaic.getTileAxisTop(icol).setLabel(label);
      adjustColorBar();
    } else if (_axesPlacement==AxesPlacement.LEFT_BOTTOM) {
      _mosaic.getTileAxisBottom(icol).setLabel(label);
      adjustColorBar();
    }
  }

  /**
   * Sets the label for the vertical axis in the specified row.
   * @param irow the row index.
   * @param label the label.
   */
  public void setVLabel(int irow, String label) {
    if (_axesPlacement!=AxesPlacement.NONE) {
      _mosaic.getTileAxisLeft(irow).setLabel(label);
    }
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
    if (_axesPlacement==AxesPlacement.LEFT_TOP) {
      _mosaic.getTileAxisTop(icol).setFormat(format);
    } else if (_axesPlacement==AxesPlacement.LEFT_BOTTOM) {
      _mosaic.getTileAxisBottom(icol).setFormat(format);
    }
  }

  /**
   * Sets the format for the vertical axis in the specified row.
   * @param irow the row index.
   * @param format the format.
   */
  public void setVFormat(int irow, String format) {
    if (_axesPlacement!=AxesPlacement.NONE) {
      _mosaic.getTileAxisLeft(irow).setFormat(format);
    }
  }

  /**
   * Sets tic label rotation for the vertical axis in the specifie row.
   * If true, tic labels in the vertical axis are rotated 90 degrees 
   * counter-clockwise. The default is false, not rotated.
   * @param irow the row index.
   * @param rotated true, if rotated; false, otherwise.
   */
  public void setVRotated(int irow, boolean rotated) {
    if (_axesPlacement!=AxesPlacement.NONE) {
      _mosaic.getTileAxisLeft(irow).setVerticalAxisRotated(rotated);
    }
  }

  /**
   * Sets the tic interval for the horizontal axis.
   * @param interval the major labeled tic interval.
   */
  public void setHInterval(double interval) {
    setHInterval(0,interval);
  }

  /**
   * Sets the tic interval for the vertical axis.
   * @param interval the major labeled tic interval.
   */
  public void setVInterval(double interval) {
    setVInterval(0,interval);
  }

  /**
   * Sets the tic interval for the horizontal axis in the specified column.
   * @param icol the column index.
   * @param interval the major labeled tic interval.
   */
  public void setHInterval(int icol, double interval) {
    if (_axesPlacement==AxesPlacement.LEFT_TOP) {
      _mosaic.getTileAxisTop(icol).setInterval(interval);
    } else if (_axesPlacement==AxesPlacement.LEFT_BOTTOM) {
      _mosaic.getTileAxisBottom(icol).setInterval(interval);
    }
  }

  /**
   * Sets the tic interval for the vertical axis in the specified column.
   * @param irow the row index.
   * @param interval the major labeled tic interval.
   */
  public void setVInterval(int irow, double interval) {
    if (_axesPlacement!=AxesPlacement.NONE) {
      _mosaic.getTileAxisLeft(irow).setInterval(interval);
    }
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
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[nc][n2][n1] of sampled function values f(x1,x2), 
   *  where n1 = f[0][0].length, n2 = f[0].length, and nc is the 
   *  number of components.
   * @return the pixels view.
   */
  public PixelsView addPixels(float[][][] f) {
    return addPixels(0,0,f);
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[nc][n2][n1] of sampled function values f(x1,x2), 
   *  where n1 = f[0][0].length, n2 = f[0].length, and nc is the 
   *  number of components.
   * @return the pixels view.
   */
  public PixelsView addPixels(Sampling s1, Sampling s2, float[][][] f) {
    return addPixels(0,0,s1,s2,f);
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param f array[nc][n2][n1] of sampled function values f(x1,x2), 
   *  where n1 = f[0][0].length, n2 = f[0].length, and nc is the 
   *  number of components.
   * @return the pixels view.
   */
  public PixelsView addPixels(int irow, int icol, float[][][] f) {
    PixelsView pv = new PixelsView(f);
    return addPixelsView(irow,icol,pv);
  }

  /**
   * Adds a pixels view of the specified sampled function f(x1,x2).
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[nc][n2][n1] of sampled function values f(x1,x2), 
   *  where n1 = f[0][0].length, n2 = f[0].length, and nc is the 
   *  number of components.
   * @return the pixels view.
   */
  public PixelsView addPixels(
    int irow, int icol, Sampling s1, Sampling s2, float[][][] f) {
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
   * Adds a points view of arrays x1, x2 and x3 of point (x1,x2,x3) coordinates.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   * @param x3 array of x3 coordinates.
   * @return the points view.
   */
  public PointsView addPoints(float[] x1, float[] x2, float[] x3) {
    return addPoints(0,0,x1,x2,x3);
  }

  /**
   * Adds a points view of (x1,x2) with specified x2 coordinates.
   * The corresponding coordinates x1 are assumed to be 0, 1, 2, ....
   * @param x2 array of x2 coordinates.
   * @return the points view.
   */
  public PointsView addPoints(float[] x2) {
    return addPoints(0,0,x2);
  }

  /**
   * Adds a view of points (x1,x2) for a sampled function x2(x1).
   * @param s1 the sampling of x1 coordinates.
   * @param x2 array of x2 coordinates.
   * @return the points view.
   */
  public PointsView addPoints(Sampling s1, float[] x2) {
    return addPoints(0,0,s1,x2);
  }

  /**
   * Adds a view of arrays of (x1,x2) coordinates for multiple plot segments.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * @param x1 array of arrays of x1 coordinates.
   * @param x2 array of arrays of x2 coordinates.
   */
  public PointsView addPoints(float[][] x1, float[][] x2) {
    return addPoints(0,0,x1,x2);
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
   * Adds a points view of arrays x1, x2 and x3 of point (x1,x2,x3) coordinates.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   * @param x3 array of x3 coordinates.
   * @return the points view.
   */
  public PointsView addPoints(
    int irow, int icol, float[] x1, float[] x2, float[] x3) 
  {
    PointsView pv = new PointsView(x1,x2,x3);
    return addPointsView(irow,icol,pv);
  }

  /**
   * Adds a points view of (x1,x2) with specified x2 coordinates.
   * The corresponding coordinates x1 are assumed to be 0, 1, 2, ....
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param x2 array of x2 coordinates.
   * @return the points view.
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
   * @return the points view.
   */
  public PointsView addPoints(int irow, int icol, Sampling s1, float[] x2) {
    PointsView pv = new PointsView(s1,x2);
    return addPointsView(irow,icol,pv);
  }

  /**
   * Adds a view of arrays of (x1,x2) coordinates for multiple plot segments.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param x1 array of arrays of x1 coordinates.
   * @param x2 array of arrays of x2 coordinates.
   */
  public PointsView addPoints(int irow, int icol, float[][] x1, float[][] x2) {
    PointsView pv = new PointsView(x1,x2);
    return addPointsView(irow,icol,pv);
  }

  /**
   * Adds a contours view with the function f(x1,x2).
   * Function f(x1,x2) assumed to have uniform sampling.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where
   * n1 = f[0].length and n2 = f.length.
   */
  public ContoursView addContours(float[][] f) {
    return addContours(0,0,f);
  }

  /**
   * Adds a contours view of the specified sampled function f(x1,x2).
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   * @return the contours view.
   */
  public ContoursView addContours(Sampling s1, Sampling s2, float[][] f) {
    return addContours(0,0,s1,s2,f);
  }

  /** 
   * Adds a contours view with the function f(x1,x2).
   * Function f(x1,x2) assumed to have uniform sampling.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param f array[n2][n1] of sampline function values f(x1,x2), where
   * n1 = f[0].length and n2 = f.length.
   */
  public ContoursView addContours(int irow, int icol, float[][] f) {
    ContoursView cv = new ContoursView(f);
    return addContoursView(irow,icol,cv);
  }

  /**
   * Adds a contours view of the specified sampled function f(x1,x2).
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[n2][n1] of sampled function values f(x1,x2), 
   *  where n1 = f[0].length and n2 = f.length.
   * @return the contours view.
   */
  public ContoursView addContours(
    int irow, int icol, Sampling s1, Sampling s2, float[][] f) {
    ContoursView cv = new ContoursView(s1,s2,f);
    return addContoursView(irow,icol,cv);
  }

  /**
   * Adds a sequence view with specified values f(x).
   * Uses default sampling of x = 0, 1, 2, ....
   * @param f array of sampled function values f(x).
   * @return the sequence view.
   */
  public SequenceView addSequence(float[] f) {
    return addSequence(0,0,f);
  }

  /**
   * Adds a sequence view with specified sampling and values f(x).
   * @param sx the sampling of the variable x.
   * @param f array of sampled function values f(x).
   * @return the sequence view.
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
   * @return the sequence view.
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
   * @return the sequence view.
   */
  public SequenceView addSequence(int irow, int icol, Sampling sx, float[] f) {
    SequenceView sv = new SequenceView(sx,f);
    return addSequenceView(irow,icol,sv);
  }

  /**
   * Adds the specified tiled view to this plot panel. If the tiled view 
   * is already in this panel, it is first removed, before adding it again.
   * @param tv the tiled view.
   * @return true, if this panel did not already contain the specified
   *  tiled view; false, otherwise.
   */
  public boolean addTiledView(TiledView tv) {
    return addTiledView(0,0,tv);
  }

  /**
   * Adds the specified tiled view to this plot panel. If the tiled view 
   * is already in the specified tile, it is first removed, before adding 
   * it again.
   * @param irow the tile row index.
   * @param icol the tile column index.
   * @param tv the tiled view.
   * @return true, if the tile did not already contain the specified
   *  tiled view; false, otherwise.
   */
  public boolean addTiledView(int irow, int icol, TiledView tv) {
    if (tv instanceof ColorMapped) {
      ColorMapped cm = (ColorMapped)tv;
      cm.getColorMap().addListener(_colorMapHandler);
    }
    return getTile(irow,icol).addTiledView(tv);
  }

  /**
   * Removes the specified tiled view from this plot panel.
   * @param tv the tiled view.
   * @return true, if this panel contained the specified tiled view; 
   *  false, otherwise.
   */
  public boolean remove(TiledView tv) {
    if (tv instanceof ColorMapped) {
      ColorMapped cm = (ColorMapped)tv;
      cm.getColorMap().removeListener(_colorMapHandler);
    }
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
  private int _colorBarWidthMinimum = 0;
  private Title _title;
  private Orientation _orientation;
  private AxesPlacement _axesPlacement;
  private ColorMapped _colorMapped;
  private boolean _autoColorMapped = true;
  private ColorMapHandler _colorMapHandler;

  /**
   * Internal class for plot title.
   */
  private class Title extends IPanel {
    private static final long serialVersionUID = 1L;
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
      //int fh = round(lm.getHeight());
      //int fa = round(lm.getAscent());
      int fd = round(lm.getDescent());
      int wt = fm.stringWidth(text);
      int xt = max(0,(w-wt)/2);
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
        return new Dimension(wt+4*fh,fd+fh);
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
    addTiledView(irow,icol,gv);
    return gv;
  }

  private PixelsView addPixelsView(int irow, int icol, PixelsView pv) {
    pv.getColorMap().addListener(_colorMapHandler);
    if (_orientation==Orientation.X1RIGHT_X2UP) {
      pv.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
    } else if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      pv.setOrientation(PixelsView.Orientation.X1DOWN_X2RIGHT);
    }
    addTiledView(irow,icol,pv);
    return pv;
  }

  private PointsView addPointsView(int irow, int icol, PointsView pv) {
    if (_orientation==Orientation.X1RIGHT_X2UP) {
      pv.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
    } else if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      pv.setOrientation(PointsView.Orientation.X1DOWN_X2RIGHT);
    }
    addTiledView(irow,icol,pv);
    return pv;
  }

  private ContoursView addContoursView(int irow, int icol, ContoursView cv) {
    if (_orientation==Orientation.X1RIGHT_X2UP) {
      cv.setOrientation(ContoursView.Orientation.X1RIGHT_X2UP);
    } else if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      cv.setOrientation(ContoursView.Orientation.X1DOWN_X2RIGHT);
    }
    addTiledView(irow,icol,cv);
    return cv;
  }

  private SequenceView addSequenceView(int irow, int icol, SequenceView sv) {
    addTiledView(irow,icol,sv);
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
   * This method is necessary because we want the colorbar height to equal
   * that of the tiles in the mosaic, but not including any tile axes.
   */
  private void adjustColorBar() {
    if (_colorBar!=null) {
      GridBagLayout gbl = (GridBagLayout)getLayout();
      gbl.setConstraints(_colorBar,makeColorBarConstraints());
      revalidate();
      //remove(_colorBar);
      //add(_colorBar,makeColorBarConstraints());
    }
  }

  private GridBagConstraints makeColorBarConstraints() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0;
    gbc.weighty = 100;
    gbc.fill = GridBagConstraints.VERTICAL;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets.top = _mosaic.getHeightAxesTop();
    gbc.insets.left = 10;
    gbc.insets.bottom = _mosaic.getHeightAxesBottom();
    gbc.insets.right = 0;
    return gbc;
  }

  /**
   * Searches for the best color mapped through each tiled view.
   * The panels are searched top to bottom, right to left.
   */
  private ColorMapped findBestColorMapped() {
    if (_autoColorMapped) {
      int rows = _mosaic.countRows();
      int cols = _mosaic.countColumns();
      ColorMapped cmBest = null;
      ColorMapped cmSolid = null;
      for (int ncol=cols-1; ncol>=0; --ncol) {
        for (int nrow=0; nrow<rows; ++nrow) {
          Tile t = getTile(nrow,ncol);
          int ntv = t.countTiledViews();
          for (int itv=ntv-1; itv>=0 && cmBest==null; --itv) {
            TiledView tv = t.getTiledView(itv);
            if (tv instanceof ColorMapped) {
              ColorMapped cm = (ColorMapped)tv;
              if (isMultiColor(cm)) {
                cmBest = cm;
              } else if (cmSolid==null) {
                cmSolid = cm;
              }
            }
          }
        }
      }
      if (cmBest==null)
        cmBest = cmSolid;
      return cmBest;
    } else {
      return _colorMapped;
    }
  }

  /**
   * Determines if a specified color map has more than one color.
   * Note that we ignore any variation in alpha.
   */
  private static boolean isMultiColor(ColorMapped cm) {
    ColorMap cmap = cm.getColorMap();
    IndexColorModel icm = cmap.getColorModel();
    int n = icm.getMapSize();
    int rgb = icm.getRGB(0)&0x00ffffff;
    for (int i=1; i<n; ++i) 
      if (rgb!=(icm.getRGB(i)&0x00ffffff))
        return true;
    return false;
  }

  /**
   * Ensures that the colorbar (if any) reflects the correct color map.
   */
  private void updateColorMapped() {
    ColorMapped cm = findBestColorMapped();
    if (cm!=_colorMapped && _colorBar!=null) {
      if (_colorMapped!=null)
        _colorMapped.getColorMap().removeListener(_colorBar);
      _colorMapped = cm;
      _colorMapped.getColorMap().addListener(_colorBar);
      revalidate();
    }
  }
  private class ColorMapHandler implements ColorMapListener {

    /**
     * Called whenever the color map within PlotPanel is changed.
     * @param cm the color map.
     */
    public void colorMapChanged(ColorMap cm) {
      updateColorMapped();
    }
  }
}
