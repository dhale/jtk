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
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.27
 */
public class Tile extends JPanel {

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

  /**
   * Adds the specified tiled view to this tile. If the tiled view is
   * already in this tile, it is first removed, before adding it again.
   * @param tv the tiled view.
   */
  public void addTiledView(TiledView tv) {
    _tvs.remove(tv);
    _tvs.add(tv);
    tv.setTile(this);
    alignProjectors();
  }

  /**
   * Removes the specified tiled view from this tile. If the tiled view
   * is not in this tile, this method does nothing.
   * @param tv the tiled view.
   */
  public void removeTiledView(TiledView tv) {
    if (_tvs.remove(tv)) {
      tv.setTile(null);
      alignProjectors();
    }
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
    return _vr.clone();
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

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int width = getWidth();
    int height = getHeight();
    g.setColor(Color.GREEN);
    g.fillRect(0,0,width,height);
    FontMetrics fm = g.getFontMetrics();
    int sw = fm.stringWidth("Tile");
    int sh = fm.getAscent();
    int x = width/2-sw/2;
    int y = height/2+sh/2;
    g.setColor(Color.BLACK);
    g.drawString("Tile",x,y);
    for (TiledView tv : _tvs) {
      Graphics2D g2d = (Graphics2D)g.create();
      tv.paint(g2d);
    }
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
    _hp = hp;
    _vp = vp;
    repaint();
  }

  /**
   * Called during alignment of this tile by its mosaic.
   */
  void setHorizontalProjector(Projector hp) {
    _hp = hp;
    repaint();
  }

  /**
   * Called during alignment of this tile by its mosaic.
   */
  void setVerticalProjector(Projector vp) {
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
    _vr = vr.clone();
    _vr.x = max(0.0,min(1.0,_vr.x));
    _vr.y = max(0.0,min(1.0,_vr.y));
    _vr.width = max(0.0,min(1.0-_vr.x,_vr.width));
    _vr.height = max(0.0,min(1.0-_vr.y,_vr.height));
    _ts.setMapping(_vr.x,_vr.y,_vr.x+_vr.width,_vr.y+_vr.height);
    repaint();
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
  private DRectangle _vr = new DRectangle(0.0,0.0,1.0,1.0);

  private void updateBestProjectors() {
    Projector bhp = null;
    Projector bvp = null;
    int ntv = _tvs.size();
    if (ntv>0) {
      TiledView tv = _tvs.get(ntv-1);
      bhp = tv.getBestHorizontalProjector();
      bvp = tv.getBestVerticalProjector();
      for (int itv=ntv-2; itv>=0; --itv) {
        tv = _tvs.get(itv);
        bhp.merge(tv.getBestHorizontalProjector());
        bvp.merge(tv.getBestVerticalProjector());
      }
    }
  }
}
