/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * A tiled view in a tile.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.17
 */
public abstract class TiledView {

  /**
   * Gets the tile that contains this tiled view.
   * @return the tile.
   */
  public Tile getTile() {
    return _tile;
  }

  /**
   * Paints this tiled view.
   * @param gc the graphics context.
   */
  public abstract void paint(GC gc);

  /**
   * Gets the horizontal projector of this tiled view.
   * @return the horizontal projector.
   */
  public Projector getHorizontalProjector() {
    return _hp;
  }

  /**
   * Gets the vertical projector of this tiled view.
   * @return the vertical projector.
   */
  public Projector getVerticalProjector() {
    return _vp;
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
        _tile.alignTiledView(this);
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
   * Called during alignment of this tiled view by its tile.
   */
  void setProjectors(Projector hp, Projector vp) {
    _hp = hp;
    _vp = vp;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Tile _tile;
  private Projector _hp = new Projector(0.0,1.0,0.0,1.0);
  private Projector _vp = new Projector(0.0,1.0,0.0,1.0);
  private Projector _bhp = null;
  private Projector _bvp = null;

  private boolean equal(Projector a, Projector b) {
    return (a==null)?b==null:a.equals(b);
  }
}

