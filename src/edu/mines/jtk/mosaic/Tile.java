/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A tile in a mosaic.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.11
 */
public class Tile extends Canvas {

  /**
   * Gets the mosaic that contains this tile.
   * @return the mosaic.
   */
  public Mosaic getMosaic() {
    return _mosaic;
  }

  /**
   * Gets the row index of this tile.
   * @return the row index.
   */
  public int getRowIndex() {
    return _irow;
  }

  /**
   * Gets the column index of this tile.
   * @return the column index.
   */
  public int getColumnIndex() {
    return _icol;
  }

  /**
   * Gets the horizontal projector for this tile.
   * @return the horizontal projector.
   */
  public Projector getHorizontalProjector() {
    return _hp;
  }

  /**
   * Gets the vertical projector for this tile.
   * @return the vertical projector.
   */
  public Projector getVerticalProjector() {
    return _vp;
  }

  /**
   * Gets the transcaler for this tile.
   * @return the transcaler.
   */
  public Transcaler getTranscaler() {
    return _ts;
  }

  public int countTiledViews() {
    return _tvs.size();
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  Tile(Mosaic mosaic, int irow, int icol) {
    super(mosaic,SWT.NONE);
    _mosaic = mosaic;
    _irow = irow;
    _icol = icol;
    addListener(SWT.Dispose, new Listener() {
      public void handleEvent(Event e) {
        onDispose(e);
      }
    });
    addListener(SWT.Resize, new Listener() {
      public void handleEvent(Event e) {
        onResize(e);
      }
    });
    addListener(SWT.Paint, new Listener() {
      public void handleEvent(Event e) {
        onPaint(e);
      }
    });
  }
  
  /**
   * Called during alignment of this tile by its mosaic.
   */
  Projector getBestHorizontalProjector() {
    return _bhp;
  }
  
  /**
   * Called during alignment of this tile by its mosaic.
   */
  Projector getBestVerticalProjector() {
    return _bvp;
  }

  /**
   * Called during alignment of this tile by its mosaic.
   * Or, if the mosaic is null, the tile calls this method,
   * when one of its tiled views requests alignment.
   */
  void setProjectors(Projector hp, Projector vp) {
    _hp = hp;
    _vp = vp;
    for (TiledView tv : _tvs) {
      tv.setProjectors(_hp,_vp);
    }
    redraw();
  }

  /**
   * Called by a tiled view when it requires alignment.
   */
  void alignTiledView(TiledView tv) {
    updateBestProjectors();
    if (_mosaic!=null) {
      _mosaic.alignTile(this);
    } else {
      setProjectors(_bhp,_bvp);
    }
  }

  /**
   * Paints this tile. Typically, this method is called by our paint 
   * listener, in response to a paint event. However, it may also be 
   * called by our mosaic, if exporting to an image or other drawable.
   * @param gc the GC with which to paint.
   * @param width the width of the drawable, in pixels.
   * @param height the height of the drawable, in pixels.
   */
  void paint(GC gc, int width, int height) {
    gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
    gc.fillRectangle(0,0,width,height);
    Point extent = gc.stringExtent("Tile");
    int x = width/2-extent.x/2;
    int y = height/2-extent.y/2;
    gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
    gc.drawString("Tile",x,y);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private int _irow;
  private int _icol;
  private ArrayList<TiledView> _tvs = new ArrayList<TiledView>();
  private Projector _hp = new Projector(0.0,1.0,0.0,1.0);
  private Projector _vp = new Projector(0.0,1.0,0.0,1.0);
  private Projector _bhp = null;
  private Projector _bvp = null;
  private Transcaler _ts = new Transcaler();

  private void onDispose(Event e) {
  }

  private void onResize(Event e) {
    Point size = getSize();
    int w = size.x;
    int h = size.y;
    _ts.setMapping(0,w-1,0,h-1);
  }

  private void onPaint(Event e) {
    PaintEvent pe = new PaintEvent(e);
    Point size = getSize();
    paint(pe.gc,size.x,size.y);
  }

  private void updateBestProjectors() {
    Projector bhp = null;
    Projector bvp = null;
    int ntv = _tvs.size();
    if (ntv>0) {
      TiledView tv = _tvs.get(0);
      bhp = tv.getBestHorizontalProjector();
      bvp = tv.getBestVerticalProjector();
      for (int itv=1; itv<ntv; ++itv) {
        tv = _tvs.get(itv);
        bhp.merge(tv.getBestHorizontalProjector());
        bvp.merge(tv.getBestVerticalProjector());
      }
    }
  }
}
