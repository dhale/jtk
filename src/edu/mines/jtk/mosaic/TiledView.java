/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;

/**
 * A tiled view in a tile.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.28
 */
public abstract class TiledView {

  /**
   * Paints this tiled view.
   * @param g the graphics.
   */
  public abstract void paint(Graphics g);

  /**
   * Gets the tile that contains this tiled view.
   * @return the tile; null, if none.
   */
  public Tile getTile() {
    return _tile;
  }

  /**
   * Gets the horizontal projector of this tiled view.
   * The returned horizontal projector is a reference to that for the tile 
   * that contains this tiled view or null, if this tiled view is not in a 
   * tile.
   * @return the horizontal projector; null, if none.
   */
  public Projector getHorizontalProjector() {
    return _tile.getHorizontalProjector();
  }

  /**
   * Gets the vertical projector of this tiled view.
   * The returned vertical projector is a reference to that for the tile 
   * that contains this tiled view or null, if this tiled view is not in 
   * a tile.
   * @return the vertical projector; null, if none.
   */
  public Projector getVerticalProjector() {
    return _tile.getVerticalProjector();
  }

  /**
   * Gets the transcaler of this tiled view.
   * The returned transcaler is a reference to that for the tile that
   * contains this tiled view or null, if this tiled view is not in a 
   * tile.
   * @return the transcaler; null, if none.
   */
  public Transcaler getTranscaler() {
    return _tile.getTranscaler();
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Sets the best projectors for this tiled view. Classes that extend
   * this class call this method when their best projectors change.
   * If this tiled view is in a tile, such a change may cause the tile 
   * to realign this view.
   */
  protected void setBestProjectors(Projector bhp, Projector bvp) {
    if (!equal(_bhp,bhp) || !equal(_bvp,bvp)) {
      _bhp = bhp.clone();
      _bvp = bvp.clone();
      if (_tile!=null) {
        _tile.alignProjectors();
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // package
  
  /**
   * Called during alignment of this tiled view by its tile.
   */
  Projector getBestHorizontalProjector() {
    return _bhp;
  }
  
  /**
   * Called during alignment of this tiled view by its tile.
   */
  Projector getBestVerticalProjector() {
    return _bvp;
  }

  /**
   * Called by the tile when this tiled view is added/removed to/from it.
   */
  void setTile(Tile tile) {
    _tile = tile;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Tile _tile;
  private Projector _bhp = null;
  private Projector _bvp = null;

  private boolean equal(Projector a, Projector b) {
    return (a==null)?b==null:a.equals(b);
  }
}

