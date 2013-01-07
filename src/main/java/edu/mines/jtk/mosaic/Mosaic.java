/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.*;

import edu.mines.jtk.awt.ModeManager;

/**
 * A mosaic of tiles and tile axes. A mosaic lays out its tiles in a matrix,
 * with a specified number of rows and columns. It manages the world and
 * normalized coordinate systems of those tiles, so that tiles zoom and
 * scroll consistently. 
 * <p>
 * For example, when the the view rectangle (in normalized coordinates)
 * of a tile is set, perhaps while zooming or scrolling, then that tile's 
 * mosaic changes the view rectangles of any other tiles in the same row 
 * or column accordingly, so that they all zoom and scroll together.
 * <p>
 * A mosaic can also manage axes at the top, left, bottom, and/or right
 * sides of its matrix of tiles. These axes annotate the adjacent tiles,
 * and mosaic ensures that they too zoom and scroll consistent with any
 * changes to the view rectangles of those tiles.
 * <p>
 * A mosaic also manages a horizontal scrollbar for each column and a
 * vertical scrollbar for each row. The mosaic shows scrollbars for only
 * those dimensions of view rectangles that are zoomed. In other words,
 * scrollbars are visible and consume space only when they are needed.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.27
 * @version 2005.12.23
 */
public class Mosaic extends IPanel {
  private static final long serialVersionUID = 1L;

  /**
   * Placement of axes.
   */
  public enum AxesPlacement {
    TOP, LEFT, BOTTOM, RIGHT
  }

  /**
   * Constructs a mosaic with the specified number of rows and columns.
   * @param nrow the number of rows.
   * @param ncol the number of columns.
   * @param axesPlacement the placement of axes.
   */
  public Mosaic(int nrow, int ncol, Set<AxesPlacement> axesPlacement) {
    _nrow = nrow;
    _ncol = ncol;

    // Tiles.
    _tiles = new Tile[nrow][ncol];
    _tileList = new ArrayList<Tile>();
    for (int irow=0; irow<nrow; ++irow) {
      for (int icol=0; icol<ncol; ++icol) {
        Tile tile = _tiles[irow][icol] = new Tile(this,irow,icol);
        _tileList.add(tile);
        add(tile);
      }
    }

    // Tile axes.
    _axisList = new ArrayList<TileAxis>();
    if (axesPlacement.contains(AxesPlacement.TOP)) {
      _axesTop = new TileAxis[ncol];
      for (int icol=0; icol<ncol; ++icol) {
        TileAxis axis = _axesTop[icol] = 
          new TileAxis(this,TileAxis.Placement.TOP,icol);
        _axisList.add(axis);
        add(axis);
      }
    }
    if (axesPlacement.contains(AxesPlacement.LEFT)) {
      _axesLeft = new TileAxis[nrow];
      for (int irow=0; irow<nrow; ++irow) {
        TileAxis axis = _axesLeft[irow] = 
          new TileAxis(this,TileAxis.Placement.LEFT,irow);
        _axisList.add(axis);
        add(axis);
      }
    }
    if (axesPlacement.contains(AxesPlacement.BOTTOM)) {
      _axesBottom = new TileAxis[ncol];
      for (int icol=0; icol<ncol; ++icol) {
        TileAxis axis = _axesBottom[icol] = 
          new TileAxis(this,TileAxis.Placement.BOTTOM,icol);
        _axisList.add(axis);
        add(axis);
      }
    }
    if (axesPlacement.contains(AxesPlacement.RIGHT)) {
      _axesRight = new TileAxis[nrow];
      for (int irow=0; irow<nrow; ++irow) {
        TileAxis axis = _axesRight[irow] = 
          new TileAxis(this,TileAxis.Placement.RIGHT,irow);
        _axisList.add(axis);
        add(axis);
      }
    }

    // Scroll bars, initially not visible.
    _vsb = new VScrollBar[_nrow];
    _hsb = new HScrollBar[_ncol];
    for (int irow=0; irow<_nrow; ++irow) {
      VScrollBar vsb = _vsb[irow] = new VScrollBar(irow);
      add(vsb);
    }
    for (int icol=0; icol<_ncol; ++icol) {
      HScrollBar hsb = _hsb[icol] = new HScrollBar(icol);
      add(hsb);
    }

    // Mouse wheel listeners for scrolling.
    for (int irow=0; irow<nrow; ++irow) {
      for (int icol=0; icol<ncol; ++icol) {
        Tile tile = _tiles[irow][icol];
        tile.addMouseWheelListener(new MouseWheelListener() {
          public void mouseWheelMoved(MouseWheelEvent event) {
            double u = event.getWheelRotation();
            //double u = event.getPreciseWheelRotation(); // JDK 1.7 only
            Tile tile = (Tile)event.getSource();
            DRectangle vr = tile.getViewRectangle();
            if (event.isShiftDown() && vr.width<1.0) { // horizontal
              HScrollBar hsb = _hsb[tile.getColumnIndex()];
              vr.x += u*hsb.getUnitIncrement(1)*SCROLL_SCL;
              vr.x = max(0.0,min(1.0-vr.width,vr.x));
              tile.setViewRectangle(vr);
            } else if (!event.isShiftDown() && vr.height<1.0) { // vertical
              VScrollBar vsb = _vsb[tile.getRowIndex()];
              vr.y += u*vsb.getUnitIncrement(1)*SCROLL_SCL;
              vr.y = max(0.0,min(1.0-vr.height,vr.y));
              tile.setViewRectangle(vr);
            }
          }
        });
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

    // Mode manager.
    _modeManager = new ModeManager();
    for (Tile tile : _tileList)
      _modeManager.add(tile);
    for (TileAxis axis : _axisList)
      _modeManager.add(axis);
  }

  /**
   * Sets the mode manager for this mosaic. By default, a mosaic
   * constructs a unique mode manager when it is constructed. This 
   * default may be overridden, for example, when two or more mosaics 
   * must share modes.
   * @param modeManager the mode manager.
   */
  public void setModeManager(ModeManager modeManager) {
    if (_modeManager!=null) {
      for (Tile tile : _tileList)
        _modeManager.remove(tile);
      for (TileAxis axis : _axisList)
        _modeManager.remove(axis);
    }
    _modeManager = modeManager;
    if (_modeManager!=null) {
      for (Tile tile : _tileList)
        _modeManager.add(tile);
      for (TileAxis axis : _axisList)
        _modeManager.add(axis);
    }
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
    revalidate();
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
    revalidate();
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
    revalidate();
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
    revalidate();
  }

  /**
   * Sets the width in pixels of spacing between adjacent tiles.
   * @param wts the width of the inter-tile spacing.
   */
  public void setWidthTileSpacing(int wts) {
    _wts = wts;
    revalidate();
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
    if (_tileList!=null) {
      for (Tile tile : _tileList)
        tile.setFont(font);
      for (TileAxis axis : _axisList)
        axis.setFont(font);
    }
    revalidate();
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

  // Override base class implementation.
  public Dimension getPreferredSize() {
    if (isPreferredSizeSet()) {
      return super.getPreferredSize();
    } else {
      return getMinimumSize();
    }
  }

  // Override base class implementation; ignore any layout manager.
  public void doLayout() {

    // Ensure scroll bars are visible or invisible, as necessary.
    updateScrollBars();

    // Extra width and height to fill; zero, if no extra space.
    int w = getWidth();
    int h = getHeight();
    int wf = widthFixed();
    int hf = heightFixed();
    int wfill = max(0,w-wf);
    int hfill = max(0,h-hf);

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
      int we = (icol<_ncol-1)?wfill*_we[icol]/wesum:wleft;
      if (_we[icol]==0)
        we = 0;
      wcol[icol] = max(_wm[icol],we);
      wleft -= wcol[icol];
    }

    // Heights of rows, not including borders.
    int[] hrow = new int[_nrow];
    for (int irow=0,hleft=hfill; irow<_nrow; ++irow) {
      int he = (irow<_nrow-1)?hfill*_he[irow]/hesum:hleft;
      if (_he[irow]==0)
        he = 0;
      hrow[irow] = max(_hm[irow],he);
      hleft -= hrow[irow];
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

    // Horizontal scroll bars.
    int hhsb = heightHScrollBars();
    if (hhsb>0) {
      int xhsb = widthMinimumAxesLeft()+wtb;
      int yhsb = ytile;
      if (_axesBottom!=null)
        yhsb += heightMinimumAxesBottom()-wab;
      for (int icol=0; icol<_ncol; ++icol) {
        int whsb = wcol[icol];
        _hsb[icol].setBounds(xhsb,yhsb,whsb,hhsb);
        xhsb += whsb+wtb+wts+wtb;
      }
    }

    // Vertical scroll bars.
    int wvsb = widthVScrollBars();
    if (wvsb>0) {
      int xvsb = xtile;
      if (_axesRight!=null)
        xvsb += widthMinimumAxesRight()-wab;
      int yvsb = heightMinimumAxesTop()+wtb;
      for (int irow=0; irow<_nrow; ++irow) {
        int hvsb = hrow[irow];
        _vsb[irow].setBounds(xvsb,yvsb,wvsb,hvsb);
        yvsb += hvsb+wtb+wts+wtb;
      }
    }
  }

  public void paintToRect(Graphics2D g2d, int x, int y, int w, int h) {
    g2d = createGraphics(g2d,x,y,w,h);

    // Scale factors for width and height.
    double ws = (double)w/(double)getWidth();
    double hs = (double)h/(double)getHeight();

    // Insets and strokes for tile and axes borders.
    float lineWidth = getLineWidth(g2d);
    float wtb = lineWidth*(float)widthTileBorder();
    float wab = lineWidth*(float)widthAxesBorder();
    int itb = 1+(int)(wtb/2.0f);
    int iab = 1+(int)(wab/2.0f);
    BasicStroke stb = new BasicStroke(wtb);
    BasicStroke sab = new BasicStroke(wab);

    // Draw tile and tile axis children.
    int nc = getComponentCount();
    for (int ic=0; ic<nc; ++ic) {
      Component c = getComponent(ic);
      int xc = c.getX();
      int yc = c.getY();
      int wc = c.getWidth();
      int hc = c.getHeight();
      xc = (int)round(xc*ws);
      yc = (int)round(yc*hs);
      wc = (int)round(wc*ws);
      hc = (int)round(hc*hs);
      if (c instanceof IPanel) {
        IPanel ip = (IPanel)c;
        ip.paintToRect(g2d,xc,yc,wc,hc);
        if (wtb>0.0f && ip instanceof Tile) {
          Tile tile = (Tile)ip;
          if (tile.countTiledViews()>0) {
            g2d.setStroke(stb);
            g2d.drawRect(xc-itb,yc-itb,wc+itb+itb-1,hc+itb+itb-1);
          }
        } else if (wab>0.0f && ip instanceof TileAxis) {
          g2d.setStroke(sab);
          g2d.drawRect(xc-iab,yc-iab,wc+iab+iab-1,hc+iab+iab-1);
        }
      }
    }
    g2d.dispose();
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    paintToRect((Graphics2D)g,0,0,getWidth(),getHeight());
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  void alignProjectors(Tile tile) {
    int jrow = tile.getRowIndex();
    int jcol = tile.getColumnIndex();
    Projector bhp = tile.getBestHorizontalProjector();
    if (bhp!=null) {
      bhp = new Projector(bhp);
      for (int irow=0; irow<_nrow; ++irow) {
        if (irow!=jrow)
          bhp.merge(_tiles[irow][jcol].getBestHorizontalProjector());
      }
    }
    Projector bvp = tile.getBestVerticalProjector();
    if (bvp!=null) {
      bvp = new Projector(bvp);
      for (int icol=0; icol<_ncol; ++icol) {
        if (icol!=jcol)
          bvp.merge(_tiles[jrow][icol].getBestVerticalProjector());
      }
    }
    if (bhp!=null && bvp!=null) {
      tile.setProjectors(bhp,bvp);
    } else if (bhp!=null) {
      tile.setHorizontalProjector(bhp);
    } else if (bvp!=null) {
      tile.setVerticalProjector(bvp);
    }
    bhp = tile.getHorizontalProjector();
    bvp = tile.getVerticalProjector();
    for (int irow=0; irow<_nrow; ++irow) {
      if (irow!=jrow)
        _tiles[irow][jcol].setHorizontalProjector(bhp);
    }
    for (int icol=0; icol<_ncol; ++icol) {
      if (icol!=jcol)
        _tiles[jrow][icol].setVerticalProjector(bvp);
    }
    repaintAxis(_axesTop,jcol);
    repaintAxis(_axesBottom,jcol);
    repaintAxis(_axesLeft,jrow);
    repaintAxis(_axesRight,jrow);
  }

  void setViewRect(Tile tile, DRectangle vr) {
    int wvsb = widthVScrollBars();
    int hhsb = heightHScrollBars();
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
    _hsb[jcol].update();
    _vsb[jrow].update();
    if (wvsb!=widthVScrollBars() || hhsb!=heightHScrollBars())
      revalidate();
  }

  int getHeightAxesTop() {
    return heightMinimumAxesTop();
  }

  int getHeightAxesBottom() {
    return heightMinimumAxesBottom();
  }

  int getWidthAxesLeft() {
    return widthMinimumAxesLeft();
  }

  int getWidthAxesRight() {
    return widthMinimumAxesRight();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nrow; // number of rows
  private int _ncol; // number of columns
  private Tile[][] _tiles; // array[nrow][ncol] of tiles
  private TileAxis[] _axesTop; // array[ncol] of top axes; null, if none
  private TileAxis[] _axesLeft; // array[nrow] of left axes; null, if none
  private TileAxis[] _axesBottom; // array[ncol] of bottom axes; null, if none
  private TileAxis[] _axesRight; // array[nrow] of right axes; null, if none
  private ArrayList<Tile> _tileList; // simple list of all tiles
  private ArrayList<TileAxis> _axisList; // simple list of all axes
  private HScrollBar[] _hsb; // horizontal scroll bars
  private VScrollBar[] _vsb; // vertical scroll bars
  private int _wts = 2; // width of tile spacing, in pixels
  private int[] _wm; // array[ncol] of width minimums
  private int[] _we; // array[ncol] of width elastics
  private int[] _hm; // array[nrow] of height minimums
  private int[] _he; // array[nrow] of height elastics
  private ModeManager _modeManager; // mode manager

  private void repaintAxis(TileAxis[] axes, int index) {
    if (axes!=null)
      repaintAxis(axes[index]);
  }
  private void repaintAxis(TileAxis axis) {
    axis.repaint();
    axis.updateAxisTics();
  }

  private int widthAxesBorder() {
    return 0; // currently hardwired
  }

  private int widthTileBorder() {
    return 1; // currently hardwired
  }

  private int widthTileSpacing() {
    return _wts;
  }

  private int widthFixed() {
    int width = widthMinimumAxesLeft();
    width += (_ncol-1)*widthTileSpacing();
    width += 2*_ncol*widthTileBorder();
    width += widthMinimumAxesRight();
    width += widthVScrollBars();
    return width;
  }

  private int widthMinimum() {
    int width = widthMinimumAxesLeft();
    for (int icol=0; icol<_ncol; ++icol)
      width += widthMinimumColumn(icol);
    width += widthMinimumAxesRight();
    width += (_ncol-1)*widthTileSpacing();
    width += widthMinimumVScrollBars();
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

  private int widthMinimumVScrollBars() {
    //return _vsb[0].getMinimumSize().width;
    return 0;
  }

  private int widthVScrollBars() {
    for (int irow=0; irow<_nrow; ++irow) {
      if (_vsb[irow].isVisible())
        return _vsb[irow].getMinimumSize().width;
    }
    return 0;
  }

  private int heightFixed() {
    int height = heightMinimumAxesTop();
    height += (_nrow-1)*widthTileSpacing();
    height += 2*_nrow*widthTileBorder();
    height += heightMinimumAxesBottom();
    height += heightHScrollBars();
    return height;
  }

  private int heightMinimum() {
    int height = heightMinimumAxesTop();
    for (int irow=0; irow<_nrow; ++irow)
      height += heightMinimumRow(irow);
    height += heightMinimumAxesBottom();
    height += (_nrow-1)*widthTileSpacing();
    height += heightMinimumHScrollBars();
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

  private int heightMinimumHScrollBars() {
    //return _hsb[0].getMinimumSize().height;
    return 0;
  }

  private int heightHScrollBars() {
    for (int icol=0; icol<_ncol; ++icol) {
      if (_hsb[icol].isVisible())
        return _hsb[icol].getMinimumSize().height;
    }
    return 0;
  }

  private static final int SCROLL_MAX = 1000000000;
  private static final double SCROLL_SCL = 1.0/SCROLL_MAX;
  //private static final int HORIZONTAL = Adjustable.HORIZONTAL;
  //private static final int VERTICAL = Adjustable.VERTICAL;
  private class TileScrollBar extends JScrollBar {
    private static final long serialVersionUID = 1L;
    Tile tile;
    TileScrollBar(int orientation, final Tile tile) {
      super(orientation,0,SCROLL_MAX,0,SCROLL_MAX);
      setVisible(false);
      this.tile = tile;
      addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent ae) {
          if (_settingInternal)
            return;
          DRectangle vr = tile.getViewRectangle();
          if (getOrientation()==HORIZONTAL) {
            vr.x = getV();
            vr.width = getE();
          } else {
            vr.y = getV();
            vr.height = getE();
          }
          tile.setViewRectangle(vr);
        }
      });
    }
    void setV(double v) {
      _settingInternal = true;
      setValue((int)(v*SCROLL_MAX+0.5));
      _settingInternal = false;
    }
    void setE(double e) {
      _settingInternal = true;
      setVisibleAmount((int)(e*SCROLL_MAX+0.5));
      setVisible(getVisibleAmount()<SCROLL_MAX);
      setUnitIncrement((int)(0.05*e*SCROLL_MAX+0.5));
      setBlockIncrement((int)(0.50*e*SCROLL_MAX+0.5));
      _settingInternal = false;
    }
    double getV() {
      return SCROLL_SCL*getValue();
    }
    double getE() {
      return SCROLL_SCL*getVisibleAmount();
    }
    void update() {
      DRectangle vr = tile.getViewRectangle();
      if (getOrientation()==HORIZONTAL) {
        setV(vr.x);
        setE(vr.width);
      } else {
        setV(vr.y);
        setE(vr.height);
      }
    }
    private boolean _settingInternal;
  }
  private class HScrollBar extends TileScrollBar {
    private static final long serialVersionUID = 1L;
    int icol;
    HScrollBar(int icol) {
      super(HORIZONTAL,_tiles[0][icol]);
      this.icol = icol;
    }
  }
  private class VScrollBar extends TileScrollBar {
    private static final long serialVersionUID = 1L;
    int irow;
    VScrollBar(int irow) {
      super(VERTICAL,_tiles[irow][0]);
      this.irow = irow;
    }
  }

  private void updateScrollBars() {
    for (int icol=0; icol<_ncol; ++icol)
      _hsb[icol].update();
    for (int irow=0; irow<_nrow; ++irow)
      _vsb[irow].update();
  }
}
