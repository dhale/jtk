/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A mosaic of tiles.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.11
 */
public class Mosaic extends Composite {

  public static final int AXES_NONE = 0;
  public static final int AXES_TOP = 1;
  public static final int AXES_LEFT = 2;
  public static final int AXES_BOTTOM = 4;
  public static final int AXES_RIGHT = 8;

  public static final int BORDER_NONE = SWT.NONE;
  public static final int BORDER_FLAT = SWT.FLAT;
  public static final int BORDER_SHADOW_IN = SWT.SHADOW_IN;

  /**
   * Constructs a mosaic with the specified number of rows and columns.
   * @param parent the composite parent.
   * @param style the style.
   * @param axesPlacement the locations of axes.
   * @param borderStyle the style of borders around tiles and axes.
   * @param nrow the number of rows.
   * @param ncol the number of columns.
   */
  public Mosaic(Composite parent, int style, 
    int nrow, int ncol,
    int axesPlacement, int borderStyle)
  {
    super(parent,style);
    _nrow = nrow;
    _ncol = ncol;
    _axesPlacement = axesPlacement;
    _borderStyle = borderStyle;
    _tiles = new Tile[nrow][ncol];
    for (int irow=0; irow<nrow; ++irow) {
      for (int icol=0; icol<ncol; ++icol) {
        _tiles[irow][icol] = new Tile(this,irow,icol);
      }
    }
    if ((axesPlacement&AXES_TOP)!=0) {
      _axesTop = new TileAxis[ncol];
      for (int icol=0; icol<ncol; ++icol) {
        _axesTop[icol] = new TileAxis(this,TileAxis.TOP,icol);
      }
    }
    if ((axesPlacement&AXES_LEFT)!=0) {
      _axesLeft = new TileAxis[nrow];
      for (int irow=0; irow<nrow; ++irow) {
        _axesLeft[irow] = new TileAxis(this,TileAxis.LEFT,irow);
      }
    }
    if ((axesPlacement&AXES_BOTTOM)!=0) {
      _axesBottom = new TileAxis[ncol];
      for (int icol=0; icol<ncol; ++icol) {
        _axesBottom[icol] = new TileAxis(this,TileAxis.BOTTOM,icol);
      }
    }
    if ((axesPlacement&AXES_RIGHT)!=0) {
      _axesRight = new TileAxis[nrow];
      for (int irow=0; irow<nrow; ++irow) {
        _axesRight[irow] = new TileAxis(this,TileAxis.RIGHT,irow);
      }
    }
    _we = new int[ncol];
    _wm = new int[ncol];
    for (int icol=0; icol<ncol; ++icol) {
      _we[icol] = 100;
      _wm[icol] = 100;
    }
    _he = new int[nrow];
    _hm = new int[nrow];
    for (int irow=0; irow<nrow; ++irow) {
      _he[irow] = 100;
      _hm[irow] = 100;
    }
    addListener(SWT.Dispose, new Listener() {
      public void handleEvent(Event e) {
        onDispose();
      }
    });
    addListener(SWT.Resize, new Listener() {
      public void handleEvent(Event e) {
        onResize();
      }
    });
    addListener(SWT.Paint, new Listener() {
      public void handleEvent(Event e) {
        onPaint(new PaintEvent(e));
      }
    });
  }

  public Tile getTile(int irow, int icol) {
    return _tiles[irow][icol];
  }

  public TileAxis getTileAxisTop(int icol) {
    return (_axesTop!=null)?_axesTop[icol]:null;
  }

  public TileAxis getTileAxisLeft(int irow) {
    return (_axesLeft!=null)?_axesLeft[irow]:null;
  }

  public TileAxis getTileAxisBottom(int icol) {
    return (_axesBottom!=null)?_axesBottom[icol]:null;
  }

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

  public Point computeSize(int wHint, int hHint, boolean changed) {
    return new Point(widthMinimum(),heightMinimum());
  }

  public void setLayout(Layout layout) {
    // Do nothing!
  }

  public void layout(boolean changed) {
    doLayout();
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  void needsLayout() {
    doLayout();
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
  private int[] _wm; // array[ncol] of width minimums
  private int[] _we; // array[ncol] of width elastics
  private int[] _hm; // array[nrow] of height minimums
  private int[] _he; // array[nrow] of height elastics

  private void onDispose() {
  }

  private void onResize() {
    doLayout();
  }

  private void onPaint(PaintEvent pe) {
    GC gc = pe.gc;
    //gc.setBackground(getSystemColor(SWT.COLOR_WHITE));
    //gc.fillRectangle(getBounds());
    //int wb = widthBorder();
    //gc.setLineWidth(widthBorder());

    // Tiles.
    for (int irow=0; irow<_nrow; ++irow) {
      for (int icol=0; icol<_ncol; ++icol) {
        Rectangle bounds = _tiles[irow][icol].getBounds();
        paintBorderShadowIn(gc,bounds);
      }
    }

    // Axes.
    if (_axesTop!=null) {
      for (int icol=0; icol<_ncol; ++icol)
        paintBorderShadowIn(gc,_axesTop[icol].getBounds());
    }
    if (_axesLeft!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        paintBorderShadowIn(gc,_axesLeft[irow].getBounds());
    }
    if (_axesBottom!=null) {
      for (int icol=0; icol<_ncol; ++icol)
        paintBorderShadowIn(gc,_axesBottom[icol].getBounds());
    }
    if (_axesRight!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        paintBorderShadowIn(gc,_axesRight[irow].getBounds());
    }
  }

  private void paintBorderShadowIn(GC gc, Rectangle r) {
    int x = r.x;
    int y = r.y;
    int w = r.width;
    int h = r.height;
    gc.setForeground(getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
    gc.drawLine(x-2,y-2,x+w+1,y-2);
    gc.drawLine(x-2,y-2,x-2,y+h+1);
    gc.drawLine(x-1,y-1,x+w,y-1);
    gc.drawLine(x-1,y-1,x-1,y+h);
    gc.setForeground(getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
    gc.drawLine(x,y+h,x+w,y+h);
    gc.drawLine(x+w,y,x+w,y+h);
    gc.drawLine(x-1,y+h+1,x+w+1,y+h+1);
    gc.drawLine(x+w+1,y-1,x+w+1,y+h+1);
  }

  private Color getSystemColor(int id) {
    return getDisplay().getSystemColor(id);
  }

  private void doLayout() {
    Point size = getSize();

    // Extra width and height to fill; zero, if no extra space.
    int w = size.x;
    int h = size.y;
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

    // Width of borders around tiles and axes.
    int wb = widthBorder();

    // Axes top.
    if (_axesTop!=null) {
      int haxis = heightMinimumAxesTop()-wb-wb;
      int xaxis = widthMinimumAxesLeft()+wb;
      int yaxis = wb;
      for (int icol=0; icol<_ncol; ++icol) {
        int waxis = wcol[icol];
        _axesTop[icol].setBounds(xaxis,yaxis,waxis,haxis);
        xaxis += waxis+wb+wb;
      }
    }

    // Axes left.
    if (_axesLeft!=null) {
      int waxis = widthMinimumAxesLeft()-wb-wb;
      int xaxis = wb;
      int yaxis = heightMinimumAxesTop()+wb;
      for (int irow=0; irow<_nrow; ++irow) {
        int haxis = hrow[irow];
        _axesLeft[irow].setBounds(xaxis,yaxis,waxis,haxis);
        yaxis += haxis+wb+wb;
      }
    }

    // Tiles.
    int xtile0 = wb;
    int ytile0 = wb;
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
        xtile += wtile+wb+wb;
      }
      ytile += htile+wb+wb;
    }

    // Axes bottom.
    if (_axesBottom!=null) {
      int haxis = heightMinimumAxesBottom()-wb-wb;
      int xaxis = widthMinimumAxesLeft()+wb;
      int yaxis = ytile;
      for (int icol=0; icol<_ncol; ++icol) {
        int waxis = wcol[icol];
        _axesBottom[icol].setBounds(xaxis,yaxis,waxis,haxis);
        xaxis += waxis+wb+wb;
      }
    }

    // Axes right.
    if (_axesRight!=null) {
      int waxis = widthMinimumAxesRight()-wb-wb;
      int xaxis = xtile;
      int yaxis = heightMinimumAxesTop()+wb;
      for (int irow=0; irow<_nrow; ++irow) {
        int haxis = hrow[irow];
        _axesRight[irow].setBounds(xaxis,yaxis,waxis,haxis);
        yaxis += haxis+wb+wb;
      }
    }
  }

  private int widthBorder() {
    if (_borderStyle==BORDER_FLAT) {
      return 1;
    } else if (_borderStyle==BORDER_SHADOW_IN) {
      return 2;
    } else {
      return 0;
    }
  }

  private int widthMinimum() {
    int width = widthMinimumAxesLeft();
    for (int icol=0; icol<_ncol; ++icol)
      width += widthMinimumColumn(icol);
    width += widthMinimumAxesRight();
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
    return _wm[icol]+2*widthBorder();
  }

  private int widthMinimumAxesLeft() {
    int width = 0;
    if (_axesLeft!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        width = max(width,_axesLeft[irow].getWidthMinimum());
      width += 2*widthBorder();
    }
    return width;
  }

  private int widthMinimumAxesRight() {
    int width = 0;
    if (_axesRight!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        width = max(width,_axesRight[irow].getWidthMinimum());
      width += 2*widthBorder();
    }
    return width;
  }

  private int heightMinimum() {
    int height = heightMinimumAxesTop();
    for (int irow=0; irow<_nrow; ++irow)
      height += heightMinimumRow(irow);
    height += heightMinimumAxesBottom();
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
    return _hm[irow]+2*widthBorder();
  }

  private int heightMinimumAxesTop() {
    int height = 0;
    if (_axesTop!=null) {
      for (int icol=0; icol<_ncol; ++icol)
        height = max(height,_axesTop[icol].getHeightMinimum());
      height += 2*widthBorder();
    }
    return height;
  }

  private int heightMinimumAxesBottom() {
    int height = 0;
    if (_axesBottom!=null) {
      for (int icol=0; icol<_ncol; ++icol)
        height = max(height,_axesBottom[icol].getHeightMinimum());
      height += 2*widthBorder();
    }
    return height;
  }
}
