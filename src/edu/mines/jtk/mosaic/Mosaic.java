/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * A mosaic of tiles and tile axes. A mosaic manages the geometry (layout)
 * and the world and normalized coordinate systems of its tiles.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.27
 */
public class Mosaic extends JPanel {

  public static final int AXES_NONE = 0;
  public static final int AXES_TOP = 1;
  public static final int AXES_LEFT = 2;
  public static final int AXES_BOTTOM = 4;
  public static final int AXES_RIGHT = 8;

  public static final int BORDER_FLAT = 0;
  public static final int BORDER_SHADOW = 1;

  /**
   * Constructs a mosaic with the specified number of rows and columns.
   * @param axesPlacement the locations of axes.
   * @param borderStyle the style of borders around tiles and axes.
   * @param nrow the number of rows.
   * @param ncol the number of columns.
   */
  public Mosaic(int nrow, int ncol, int axesPlacement, int borderStyle) {
    _nrow = nrow;
    _ncol = ncol;
    _axesPlacement = axesPlacement;
    _borderStyle = borderStyle;

    // Tiles.
    _tiles = new Tile[nrow][ncol];
    _tileList = new ArrayList<Tile>();
    for (int irow=0; irow<nrow; ++irow) {
      for (int icol=0; icol<ncol; ++icol) {
        Tile tile = _tiles[irow][icol] = new Tile(this,irow,icol);
        _tileList.add(tile);
      }
    }

    // Tile axes.
    _axisList = new ArrayList<TileAxis>();
    if ((axesPlacement&AXES_TOP)!=0) {
      _axesTop = new TileAxis[ncol];
      for (int icol=0; icol<ncol; ++icol) {
        TileAxis axis = _axesTop[icol] = 
          new TileAxis(this,TileAxis.TOP,icol);
        _axisList.add(axis);
      }
    }
    if ((axesPlacement&AXES_LEFT)!=0) {
      _axesLeft = new TileAxis[nrow];
      for (int irow=0; irow<nrow; ++irow) {
        TileAxis axis = _axesLeft[irow] = 
          new TileAxis(this,TileAxis.LEFT,irow);
        _axisList.add(axis);
      }
    }
    if ((axesPlacement&AXES_BOTTOM)!=0) {
      _axesBottom = new TileAxis[ncol];
      for (int icol=0; icol<ncol; ++icol) {
        TileAxis axis = _axesBottom[icol] = 
          new TileAxis(this,TileAxis.BOTTOM,icol);
        _axisList.add(axis);
      }
    }
    if ((axesPlacement&AXES_RIGHT)!=0) {
      _axesRight = new TileAxis[nrow];
      for (int irow=0; irow<nrow; ++irow) {
        TileAxis axis = _axesRight[irow] = 
          new TileAxis(this,TileAxis.RIGHT,irow);
        _axisList.add(axis);
      }
    }

    // Width elastic and minimum for each column.
    _we = new int[ncol];
    _wm = new int[ncol];
    for (int icol=0; icol<ncol; ++icol) {
      _we[icol] = 100;
      _wm[icol] = 100;
    }

    // Height elastic and minimum for each row.
    _he = new int[nrow];
    _hm = new int[nrow];
    for (int irow=0; irow<nrow; ++irow) {
      _he[irow] = 100;
      _hm[irow] = 100;
    }

    // Borders for tiles and axes.
    if (_borderStyle==BORDER_FLAT) {
      _borderTile = BorderFactory.createLineBorder(getForeground());
      _borderAxis = null;
    } else if (_borderStyle==BORDER_SHADOW) {
      _borderTile = BorderFactory.createLoweredBevelBorder();
      _borderAxis = _borderTile;
    }

    // Mode manager.
    _modeManager = new ModeManager();
    for (Tile tile : _tileList)
      _modeManager.add(tile);
    for (TileAxis axis : _axisList)
      _modeManager.add(axis);
  }

  /**
   * Returns the number of rows of tiles in this mosaic.
   * @return the number of rows.
   */
  public int countRows() {
    return _nrow;
  }

  /**
   * Returns the number of columns of tiles in this mosaic.
   * @return the number of columns.
   */
  public int countColumns() {
    return _ncol;
  }

  /**
   * Gets the tile with specified row and column indices.
   * @param irow the row index.
   * @param icol the column index.
   * @return the tile.
   */
  public Tile getTile(int irow, int icol) {
    return _tiles[irow][icol];
  }

  /**
   * Gets the top tile axis with specified column index.
   * @param icol the column index.
   * @return the axis; null, if none.
   */
  public TileAxis getTileAxisTop(int icol) {
    return (_axesTop!=null)?_axesTop[icol]:null;
  }

  /**
   * Gets the left tile axis with specified row index.
   * @param irow the row index.
   * @return the axis; null, if none.
   */
  public TileAxis getTileAxisLeft(int irow) {
    return (_axesLeft!=null)?_axesLeft[irow]:null;
  }

  /**
   * Gets the bottom tile axis with specified column index.
   * @param icol the column index.
   * @return the axis; null, if none.
   */
  public TileAxis getTileAxisBottom(int icol) {
    return (_axesBottom!=null)?_axesBottom[icol]:null;
  }

  /**
   * Gets the right tile axis with specified row index.
   * @param irow the row index.
   * @return the axis; null, if none.
   */
  public TileAxis getTileAxisRight(int irow) {
    return (_axesRight!=null)?_axesRight[irow]:null;
  }

  /**
   * Sets the width minimum for the specified column. All tiles in the 
   * specified column will have width not less than the specified minimum. 
   * Width minimums are used to compute the preferred width of this mosaic.
   * The default width minimum is 100.
   * @param icol the column index.
   * @param widthMinimum the width minimum.
   */
  public void setWidthMinimum(int icol, int widthMinimum) {
    _wm[icol] = widthMinimum;
  }

  /**
   * Sets the width elastic for the specified column. If extra width is 
   * available in this mosaic, it is allocated to the specified column 
   * of tiles in proportion to the specified width elastic. 
   * For fixed-width columns, the width elastic should be zero.
   * The default width elastic is 100.
   * @param icol the column index.
   * @param widthElastic the width elastic.
   */
  public void setWidthElastic(int icol, int widthElastic) {
    _we[icol] = widthElastic;
  }

  /**
   * Sets the height minimum for the specified row. All tiles in the 
   * specified row will have height not less than the specified minimum. 
   * Height minimums are used to compute the preferred height of this mosaic.
   * The default height minimum is 100.
   * @param irow the row index.
   * @param heightMinimum the height minimum.
   */
  public void setHeightMinimum(int irow, int heightMinimum) {
    _hm[irow] = heightMinimum;
  }

  /**
   * Sets the height elastic for the specified row. If extra height is 
   * available in this mosaic, it is allocated to the specified row 
   * of tiles in proportion to the specified height elastic. 
   * For fixed-height rows, the height elastic should be zero.
   * The default height elastic is 100.
   * @param irow the row index.
   * @param heightElastic the height elastic.
   */
  public void setHeightElastic(int irow, int heightElastic) {
    _he[irow] = heightElastic;
  }

  /**
   * Gets the mode manager for this mosaic.
   * @return the mode manager.
   */
  public ModeManager getModeManager() {
    return _modeManager;
  }

  // Override base class implementation.
  public void setFont(Font font) {
    super.setFont(font);
    if (_axisList!=null) {
      for (TileAxis axis : _axisList)
        axis.setFont(font);
    }
  }

  // Override base class implementation.
  public void setForeground(Color color) {
    super.setForeground(color);
    if (_tileList!=null) {
      for (Tile tile : _tileList)
        tile.setForeground(color);
      for (TileAxis axis : _axisList)
        axis.setForeground(color);
    }
  }

  // Override base class implementation.
  public void setBackground(Color color) {
    super.setBackground(color);
    if (_tileList!=null) {
      for (Tile tile : _tileList)
        tile.setBackground(color);
      for (TileAxis axis : _axisList)
        axis.setBackground(color);
    }
  }

  // Override base class implementation.
  public Dimension getMinimumSize() {
    if (isMinimumSizeSet()) {
      return super.getMinimumSize();
    } else {
      return new Dimension(widthMinimum(),heightMinimum());
    }
  }

  // Override base class implementation; ignore any layout manager.
  public void doLayout() {

    // Extra width and height to fill; zero, if no extra space.
    int w = getWidth();
    int h = getHeight();
    int wm = widthMinimum();
    int hm = heightMinimum();
    int wfill = max(0,w-wm);
    int hfill = max(0,h-hm);

    // Sums of width elastics and height elastics.
    int wesum = 0;
    for (int icol=0; icol<_ncol; ++icol)
      wesum += _we[icol];
    int hesum = 0;
    for (int irow=0; irow<_nrow; ++irow)
      hesum += _he[irow];

    // Avoid divide by zero.
    wesum = max(1,wesum);
    hesum = max(1,hesum);

    // Widths of columns, not including borders.
    int[] wcol = new int[_ncol];
    for (int icol=0,wleft=wfill; icol<_ncol; ++icol) {
      int wpad = (icol<_ncol-1)?wfill*_we[icol]/wesum:wleft;
      wcol[icol] = _wm[icol]+wpad;
      wleft -= wpad;
    }

    // Heights of rows, not including borders.
    int[] hrow = new int[_nrow];
    for (int irow=0,hleft=hfill; irow<_nrow; ++irow) {
      int hpad = (irow<_nrow-1)?hfill*_he[irow]/hesum:hleft;
      hrow[irow] = _hm[irow]+hpad;
      hleft -= hpad;
    }

    // Width of borders around axes and tiles.
    int wab = widthAxesBorder();
    int wtb = widthTileBorder();

    // Width of spacing between adjacent tiles.
    int wts = widthTileSpacing();

    // Axes top.
    if (_axesTop!=null) {
      int haxis = heightMinimumAxesTop()-wab-wab;
      int xaxis = widthMinimumAxesLeft()+wtb;
      int yaxis = wab;
      for (int icol=0; icol<_ncol; ++icol) {
        int waxis = wcol[icol];
        _axesTop[icol].setBounds(xaxis,yaxis,waxis,haxis);
        xaxis += waxis+wtb+wts+wtb;
      }
    }

    // Axes left.
    if (_axesLeft!=null) {
      int waxis = widthMinimumAxesLeft()-wab-wab;
      int xaxis = wab;
      int yaxis = heightMinimumAxesTop()+wtb;
      for (int irow=0; irow<_nrow; ++irow) {
        int haxis = hrow[irow];
        _axesLeft[irow].setBounds(xaxis,yaxis,waxis,haxis);
        yaxis += haxis+wtb+wts+wtb;
      }
    }

    // Tiles.
    int xtile0 = wtb;
    int ytile0 = wtb;
    if (_axesLeft!=null)
      xtile0 += widthMinimumAxesLeft();
    if (_axesTop!=null)
      ytile0 += heightMinimumAxesTop();
    int xtile = xtile0;
    int ytile = ytile0;
    for (int irow=0; irow<_nrow; ++irow) {
      int htile = hrow[irow];
      xtile = xtile0;
      for (int icol=0; icol<_ncol; ++icol) {
        int wtile = wcol[icol];
        _tiles[irow][icol].setBounds(xtile,ytile,wtile,htile);
        xtile += wtile+wtb+wts+wtb;
      }
      ytile += htile+wtb+wts+wtb;
    }

    // Bottom-right corner of tiles, including the last tile border.
    xtile -= wts+wtb;
    ytile -= wts+wtb;

    // Axes bottom.
    if (_axesBottom!=null) {
      int haxis = heightMinimumAxesBottom()-wab-wab;
      int xaxis = widthMinimumAxesLeft()+wtb;
      int yaxis = ytile+wab;
      for (int icol=0; icol<_ncol; ++icol) {
        int waxis = wcol[icol];
        _axesBottom[icol].setBounds(xaxis,yaxis,waxis,haxis);
        xaxis += waxis+wtb+wts+wtb;
      }
    }

    // Axes right.
    if (_axesRight!=null) {
      int waxis = widthMinimumAxesRight()-wab-wab;
      int xaxis = xtile+wab;
      int yaxis = heightMinimumAxesTop()+wtb;
      for (int irow=0; irow<_nrow; ++irow) {
        int haxis = hrow[irow];
        _axesRight[irow].setBounds(xaxis,yaxis,waxis,haxis);
        yaxis += haxis+wtb+wts+wtb;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Tiles.
    for (int irow=0; irow<_nrow; ++irow) {
      for (int icol=0; icol<_ncol; ++icol) {
        paintBorder(g,_tiles[irow][icol]);
      }
    }

    // Axes.
    if (_axesTop!=null) {
      for (int icol=0; icol<_ncol; ++icol)
        paintBorder(g,_axesTop[icol]);
    }
    if (_axesLeft!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        paintBorder(g,_axesLeft[irow]);
    }
    if (_axesBottom!=null) {
      for (int icol=0; icol<_ncol; ++icol)
        paintBorder(g,_axesBottom[icol]);
    }
    if (_axesRight!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        paintBorder(g,_axesRight[irow]);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  void alignProjectors(Tile tile) {
    int jrow = tile.getRowIndex();
    int jcol = tile.getColumnIndex();
    Projector bhp = tile.getBestHorizontalProjector().clone();
    for (int irow=0; irow<_nrow; ++irow) {
      if (irow!=jrow)
        bhp.merge(_tiles[irow][jcol].getBestHorizontalProjector());
    }
    Projector bvp = tile.getBestVerticalProjector().clone();
    for (int icol=0; icol<_ncol; ++icol) {
      if (icol!=jcol)
        bvp.merge(_tiles[jrow][icol].getBestVerticalProjector());
    }
    tile.setProjectors(bhp,bvp);
    for (int irow=0; irow<_nrow; ++irow) {
      if (irow!=jrow)
        _tiles[irow][jcol].setHorizontalProjector(bhp);
    }
    for (int icol=0; icol<_ncol; ++icol) {
      if (icol!=jcol)
        _tiles[jrow][icol].setVerticalProjector(bvp);
    }
  }

  void needsLayout() {
    validate();
  }

  void setViewRect(Tile tile, DRectangle vr) {
    double x = max(0.0,min(1.0,vr.x));
    double y = max(0.0,min(1.0,vr.y));
    double w = max(0.0,min(1.0-vr.x,vr.width));
    double h = max(0.0,min(1.0-vr.y,vr.height));
    DRectangle tr = new DRectangle(x,y,w,h);
    tile.setViewRect(tr);
    int jrow = tile.getRowIndex();
    int jcol = tile.getColumnIndex();
    for (int irow=0; irow<_nrow; ++irow) {
      if (irow!=jrow) {
        Tile ti = _tiles[irow][jcol];
        DRectangle dr = ti.getViewRectangle();
        dr.x = tr.x;
        dr.width = tr.width;
        ti.setViewRect(dr);
      }
    }
    for (int icol=0; icol<_ncol; ++icol) {
      if (icol!=jcol) {
        Tile ti = _tiles[jrow][icol];
        DRectangle dr = ti.getViewRectangle();
        dr.y = tr.y;
        dr.height = tr.height;
        ti.setViewRect(dr);
      }
    }
    repaintAxis(_axesTop,jcol);
    repaintAxis(_axesBottom,jcol);
    repaintAxis(_axesLeft,jrow);
    repaintAxis(_axesRight,jrow);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nrow; // number of rows
  private int _ncol; // number of columns
  private int _axesPlacement; // bits for top, left, bottom, right axes
  private int _borderStyle; // border style
  private Tile[][] _tiles; // array[nrow][ncol] of tiles
  private TileAxis[] _axesTop; // array[ncol] of top axes; null, if none
  private TileAxis[] _axesLeft; // array[nrow] of left axes; null, if none
  private TileAxis[] _axesBottom; // array[ncol] of bottom axes; null, if none
  private TileAxis[] _axesRight; // array[nrow] of right axes; null, if none
  private ArrayList<Tile> _tileList; // simple list of all tiles
  private ArrayList<TileAxis> _axisList; // simple list of all axes
  private int[] _wm; // array[ncol] of width minimums
  private int[] _we; // array[ncol] of width elastics
  private int[] _hm; // array[nrow] of height minimums
  private int[] _he; // array[nrow] of height elastics
  private Border _borderTile; // border for all tiles
  private Border _borderAxis; // border for all axes
  private ModeManager _modeManager; // mode manager

  private void repaintAxis(TileAxis[] axes, int index) {
    if (axes!=null)
      axes[index].repaint();
  }

  private void paintBorder(Graphics g, Tile tile) {
    paintBorder(g,tile,_borderTile);
  }

  private void paintBorder(Graphics g, TileAxis axis) {
    paintBorder(g,axis,_borderAxis);
  }

  private void paintBorder(Graphics g, JPanel panel, Border border) {
    if (panel!=null && border!=null) {
      Insets i = border.getBorderInsets(this);
      int x = panel.getX()-i.left;
      int y = panel.getY()-i.top;
      int width = i.left+panel.getWidth()+i.right;
      int height = i.top+panel.getHeight()+i.bottom;
      border.paintBorder(this,g,x,y,width,height);
    }
  }

  private int widthAxesBorder() {
    return (_borderAxis!=null)?_borderAxis.getBorderInsets(this).left:0;
  }

  private int widthTileBorder() {
    return (_borderTile!=null)?_borderTile.getBorderInsets(this).left:0;
  }

  private int widthTileSpacing() {
    return 2;
  }

  private int widthMinimum() {
    int width = widthMinimumAxesLeft();
    for (int icol=0; icol<_ncol; ++icol)
      width += widthMinimumColumn(icol);
    width += widthMinimumAxesRight();
    width += (_ncol-1)*widthTileSpacing();
    return width;
  }

  private int widthMinimumColumn(int icol) {
    int width = 0;
    if (_axesTop!=null)
      width = max(width,_axesTop[icol].getWidthMinimum());
    width = max(width,widthMinimumTiles(icol));
    if (_axesBottom!=null)
      width = max(width,_axesBottom[icol].getWidthMinimum());
    return width;
  }

  private int widthMinimumTiles(int icol) {
    int width = widthTileBorder();
    width += _wm[icol];
    width += widthTileBorder();
    return width;
  }

  private int widthMinimumAxesLeft() {
    int width = 0;
    if (_axesLeft!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        width = max(width,_axesLeft[irow].getWidthMinimum());
      width += 2*widthAxesBorder();
    }
    return width;
  }

  private int widthMinimumAxesRight() {
    int width = 0;
    if (_axesRight!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        width = max(width,_axesRight[irow].getWidthMinimum());
      width += 2*widthAxesBorder();
    }
    return width;
  }

  private int heightMinimum() {
    int height = heightMinimumAxesTop();
    for (int irow=0; irow<_nrow; ++irow)
      height += heightMinimumRow(irow);
    height += heightMinimumAxesBottom();
    height += (_nrow-1)*widthTileSpacing();
    return height;
  }

  private int heightMinimumRow(int irow) {
    int height = 0;
    if (_axesLeft!=null)
      height = max(height,_axesLeft[irow].getHeightMinimum());
    height = max(height,heightMinimumTiles(irow));
    if (_axesRight!=null)
      height = max(height,_axesRight[irow].getHeightMinimum());
    return height;
  }

  private int heightMinimumTiles(int irow) {
    int height = widthTileBorder();
    height += _hm[irow];
    height += widthTileBorder();
    return height;
  }

  private int heightMinimumAxesTop() {
    int height = 0;
    if (_axesTop!=null) {
      for (int icol=0; icol<_ncol; ++icol)
        height = max(height,_axesTop[icol].getHeightMinimum());
      height += 2*widthAxesBorder();
    }
    return height;
  }

  private int heightMinimumAxesBottom() {
    int height = 0;
    if (_axesBottom!=null) {
      for (int icol=0; icol<_ncol; ++icol)
        height = max(height,_axesBottom[icol].getHeightMinimum());
      height += 2*widthAxesBorder();
    }
    return height;
  }
}
