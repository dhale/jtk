/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.Iterator;

import edu.mines.jtk.util.Check;

/**
 * A tile in a mosaic contains a list of tiled views. A tile coordinates
 * changes to its tiled views and its view rectangle with other tiles
 * in its mosaic.
 * <p>
 * Each tile has a horizontal projector and a vertical projector. These 
 * map world coordinates to and from normalized coordinates. The mosaic 
 * aligns its tiles such that all tiles in the same column share the same 
 * horizontal projector, and all tiles in the same row share the same 
 * vertical projector.
 * <p>
 * A tile's view rectangle represents a subset of a unit square; i.e., the
 * view rectangle is in normalized coordinates. Setting the view rectangle 
 * of a tile causes the view rectangle to be set accordingly in all tiles 
 * in the same column and row of the mosaic. To zoom or scroll a tile, 
 * change its view rectangle.
 * <p>
 * A tile's transcaler maps normalized coordinates to and from device 
 * coordinates. When unzoomed (by default), the tile's transcaler maps
 * normalized coordinates (0.0,0.0) to device coordinates (0,0) and 
 * normalized coordinates (1.0,1.0) to device coordinates (width-1,height-1),
 * where width and height represent the size of the tile. The transcaler
 * changes when either its size or its view rectangle is changed.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.27
 * @version 2005.12.23
 */
public class Tile extends IPanel {
  private static final long serialVersionUID = 1L;

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
   * Sets the best horizontal projector for this tile. If null, this
   * tile will compute its best horizontal projector by merging those
   * of its tiled views. If not null, the specified projector is best.
   * In either case, this tile's horizontal projector may be adjusted by 
   * it's mosaic during alignment with other tiles in the same column.
   * @param bhp the best horizontal projector.
   */
  public void setBestHorizontalProjector(Projector bhp) {
    _shp = bhp;
    alignProjectors();
  }

  /**
   * Sets the best vertical projector for this tile. If null, this
   * tile will compute its best vertical projector by merging those
   * of its tiled views. If not null, the specified projector is best.
   * In either case, this tile's vertical projector may be adjusted by 
   * it's mosaic during alignment with other tiles in the same row.
   * @param bvp the best vertical projector.
   */
  public void setBestVerticalProjector(Projector bvp) {
    _svp = bvp;
    alignProjectors();
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

  /**
   * Transforms a pixel x coordinate to a horizontal world coordinate.
   * @param x the pixel x coordinate.
   * @return the horizontal world coordinate.
   */
  public double pixelToWorldHorizontal(int x) {
    return _hp.v(_ts.x(x));
  }

  /**
   * Transforms a pixel y coordinate to a vertical world coordinate.
   * @param y the pixel y coordinate.
   * @return the vertical world coordinate.
   */
  public double pixelToWorldVertical(int y) {
    return _vp.v(_ts.y(y));
  }

  /**
   * Adds the specified tiled view to this tile. If the tiled view is
   * already in this tile, it is first removed, before adding it again.
   * @param tv the tiled view.
   * @return true, if this tile did not already contain the specified
   *  tiled view; false, otherwise.
   */
  public boolean addTiledView(TiledView tv) {
    boolean removed = _tvs.remove(tv);
    _tvs.add(tv);
    tv.setTile(this);
    alignProjectors();
    return !removed;
  }

  /**
   * Removes the specified tiled view from this tile. If the tiled view
   * is not in this tile, this method does nothing.
   * @param tv the tiled view.
   * @return true, if this tile contained the specified tiled view;
   *  false, otherwise.
   */
  public boolean removeTiledView(TiledView tv) {
    if (_tvs.remove(tv)) {
      tv.setTile(null);
      alignProjectors();
      return true;
    }
    return false;
  }

  /**
   * Returns the number of tiled views in this this.
   * @return the number of tiled views.
   */
  public int countTiledViews() {
    return _tvs.size();
  }

  /**
   * Gets the tiled view with specified index.
   * @param index the index.
   * @return the tiled view.
   */
  public TiledView getTiledView(int index) {
    return _tvs.get(index);
  }

  /**
   * Gets an iterator for the tiled views in this tile.
   * @return the iterator.
   */
  public Iterator<TiledView> getTiledViews() {
    return _tvs.iterator();
  }

  /**
   * Gets the view rectangle for this tile. The view rectangle represents
   * the subset of normalized coordinate space that is displayed in this 
   * tile.
   * @return the view rectangle.
   */
  public DRectangle getViewRectangle() {
    return new DRectangle(_vr);
  }

  /**
   * Sets the view rectangle for this tile. The view rectangle represents
   * the subset of normalized coordinate space that is displayed in this 
   * tile. Setting the view rectangle may zoom or scroll this tile.
   * @param vr the view rectangle.
   */
  public void setViewRectangle(DRectangle vr) {
    _mosaic.setViewRect(this,vr);
  }

  // We override this method so that we can update our transcaler. We assume 
  // that this is the *only* way that our size changes. Also, we assume that 
  // a repaint is already pending, so we need not request one here.
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x,y,width,height);
    _ts.setMapping(width,height);
  }

  /**
   * Gets the top tile axis for this tile.
   * @return the axis; null, if none.
   */
  public TileAxis getTileAxisTop() {
    return _mosaic.getTileAxisTop(_icol);
  }

  /**
   * Gets the left tile axis for this tile.
   * @return the axis; null, if none.
   */
  public TileAxis getTileAxisLeft() {
    return _mosaic.getTileAxisLeft(_irow);
  }

  /**
   * Gets the bottom tile axis for this tile.
   * @return the axis; null, if none.
   */
  public TileAxis getTileAxisBottom() {
    return _mosaic.getTileAxisBottom(_icol);
  }

  /**
   * Gets the right tile axis for this tile.
   * @return the axis; null, if none.
   */
  public TileAxis getTileAxisRight() {
    return _mosaic.getTileAxisRight(_irow);
  }

  public void paintToRect(Graphics2D g2d, int x, int y, int w, int h) {
    g2d = createGraphics(g2d,x,y,w,h);

    // Save transcaler for this panel.
    Transcaler tsPanel = _ts;

    // Set transcaler for the graphics rectangle.
    _ts = getTranscaler(w,h);

    // Paint tiled views.
    for (TiledView tv : _tvs) {
      Graphics2D gtv = (Graphics2D)g2d.create();
      tv.paint(gtv);
      gtv.dispose();
    }

    // Restore transcaler for this panel.
    _ts = tsPanel;

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

  Tile(Mosaic mosaic, int irow, int icol) {
    _mosaic = mosaic;
    _irow = irow;
    _icol = icol;
    mosaic.add(this);
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
    Check.argument(hp!=null,"horizontal projector not null");
    Check.argument(vp!=null,"vertical projector not null");
    _hp = hp;
    _vp = vp;
    repaint();
  }

  /**
   * Called during alignment of this tile by its mosaic.
   */
  void setHorizontalProjector(Projector hp) {
    Check.argument(hp!=null,"horizontal projector not null");
    _hp = hp;
    repaint();
  }

  /**
   * Called during alignment of this tile by its mosaic.
   */
  void setVerticalProjector(Projector vp) {
    Check.argument(vp!=null,"vertical projector not null");
    _vp = vp;
    repaint();
  }

  /**
   * Called by this tile or by a tiled view when this tile needs alignment.
   */
  void alignProjectors() {
    updateBestProjectors();
    _mosaic.alignProjectors(this);
  }

  /**
   * Called by this tile's mosaic.
   */
  void setViewRect(DRectangle vr) {
    if (_vr.x!=vr.x || 
        _vr.y!=vr.y || 
        _vr.width!=vr.width || 
        _vr.height!=vr.height) {
      _vr = new DRectangle(vr);
      _vr.x = max(0.0,min(1.0,_vr.x));
      _vr.y = max(0.0,min(1.0,_vr.y));
      _vr.width = max(0.0,min(1.0-_vr.x,_vr.width));
      _vr.height = max(0.0,min(1.0-_vr.y,_vr.height));
      _ts.setMapping(_vr.x,_vr.y,_vr.x+_vr.width,_vr.y+_vr.height);
      repaint();
    }
  }

  /**
   * Called by tile axes adjacent to this tile.
   * Also used internally, for consistency.
   */
  Transcaler getTranscaler(int w, int h) {
    return new Transcaler(
      _vr.x,_vr.y,_vr.x+_vr.width,_vr.y+_vr.height,0,0,w-1,h-1);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private int _irow;
  private int _icol;
  private ArrayList<TiledView> _tvs = new ArrayList<TiledView>();
  private Projector _hp = new Projector(0.0,1.0,0.0,1.0);
  private Projector _vp = new Projector(0.0,1.0,0.0,1.0);
  private Projector _bhp; // best horizontal projector; computed or specified
  private Projector _bvp; // best vertical projector; computed or specified
  private Projector _shp; // specified best horizontal projector; or null
  private Projector _svp; // specified best vertical projector; or null
  private Transcaler _ts = new Transcaler();
  private DRectangle _vr = new DRectangle(0.0,0.0,1.0,1.0);

  private void updateBestProjectors() {
    Projector bhp = null;
    Projector bvp = null;
    int ntv = _tvs.size();
    if (_shp==null) {
      int itv = ntv-1;
      for (; bhp==null && itv>=0; --itv) {
        TiledView tv = _tvs.get(itv);
        bhp = tv.getBestHorizontalProjector();
      }
      for (; itv>=0; --itv) {
        TiledView tv = _tvs.get(itv);
        bhp.merge(tv.getBestHorizontalProjector());
      }
    }
    if (_svp==null) {
      int itv = ntv-1;
      for (; bvp==null && itv>=0; --itv) {
        TiledView tv = _tvs.get(itv);
        bvp = tv.getBestVerticalProjector();
      }
      for (; itv>=0; --itv) {
        TiledView tv = _tvs.get(itv);
        bvp.merge(tv.getBestVerticalProjector());
      }
    }
    _bhp = (_shp!=null)?_shp:bhp;
    _bvp = (_svp!=null)?_svp:bvp;
  }
}
