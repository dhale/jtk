/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.mosaic;

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

  public void setWidthElastic(int icol, int width) {
    _we[icol] = width;
  }

  public void setWidthMinimum(int icol, int width) {
    _wm[icol] = width;
  }

  public void setHeightElastic(int irow, int height) {
    _he[irow] = height;
  }

  public void setHeightMinimum(int irow, int height) {
    _hm[irow] = height;
  }

  public Point computeSize(int wHint, int hHint, boolean changed) {
    return new Point(400,400); // TODO: implement computeSize
  }

  public void setLayout(Layout layout) {
    // Do nothing! We
  }

  public void layout(boolean changed) {
    // TODO: implement layout
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  void needsLayout() {
    doLayout();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nrow;
  private int _ncol;
  private int _axesPlacement;
  private int _borderStyle;
  private Tile[][] _tiles;
  private TileAxis[] _axesTop;
  private TileAxis[] _axesLeft;
  private TileAxis[] _axesBottom;
  private TileAxis[] _axesRight;
  private int[] _we; // ncol width elastic
  private int[] _wm; // ncol width minimum
  private int[] _he; // nrow height elastic
  private int[] _hm; // nrow height minimum

  private void onDispose() {
  }
  private void onResize() {
    doLayout();
  }

  private void doLayout() {
    Point size = getSize();
    int w = size.x;
    int h = size.y;
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
    int width = 0;
    width = max(width,widthAxesLeftMinimum());
    for (int icol=0; icol<_ncol; ++icol)
      width = max(width,widthColumnMinimum(icol));
    width = max(width,widthAxesRightMinimum());
    return width;
  }

  private int widthColumnMinimum(int icol) {
    int width = 0;
    if (_axesTop!=null)
      width = max(width,_axesTop[icol].getWidthMinimum());
    width = max(width,widthTilesMinimum(icol));
    if (_axesBottom!=null)
      width = max(width,_axesBottom[icol].getWidthMinimum());
    return width;
  }

  private int widthTilesMinimum(int icol) {
    return _wm[icol]+2*widthBorder();
  }

  private int widthAxesLeftMinimum() {
    int width = 0;
    if (_axesLeft!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        width = max(width,_axesLeft[irow].getWidthMinimum());
      width += 2*widthBorder();
    }
    return width;
  }

  private int widthAxesRightMinimum() {
    int width = 0;
    if (_axesRight!=null) {
      for (int irow=0; irow<_nrow; ++irow)
        width = max(width,_axesRight[irow].getWidthMinimum());
      width += 2*widthBorder();
    }
    return width;
  }
}
