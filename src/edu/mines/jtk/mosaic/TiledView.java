/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;

/**
 * A tiled view in a tile. To paint something in a tile, classes extend 
 * and use the methods of this abstract base class.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.28
 */
public abstract class TiledView {

  /**
   * Paints this tiled view. This method is implemented by classes that 
   * extend this abstract base class. Implementations may modify the
   * specified graphics context freely. Such modifications will not affect
   * the paintings of other tiled views in the same tile or mosaic. 
   * <p>
   * Tiled views should <em>not</em> replace (set) entirely the transform 
   * in the specified graphics context. This transform may already have
   * been set by the tile or its mosaic. Therefore, tiled views should 
   * modify this transform only by specifying <em>additional</em> scaling,
   * translation, etc.
   * @param g2d the graphics context in which to paint.
   */
  public abstract void paint(Graphics2D g2d);

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
    return (_tile!=null)?_tile.getHorizontalProjector():null;
  }

  /**
   * Gets the vertical projector of this tiled view.
   * The returned vertical projector is a reference to that for the tile 
   * that contains this tiled view or null, if this tiled view is not in 
   * a tile.
   * @return the vertical projector; null, if none.
   */
  public Projector getVerticalProjector() {
    return (_tile!=null)?_tile.getVerticalProjector():null;
  }

  /**
   * Gets the transcaler of this tiled view.
   * The returned transcaler is a reference to that for the tile that
   * contains this tiled view or null, if this tiled view is not in a 
   * tile.
   * @return the transcaler; null, if none.
   */
  public Transcaler getTranscaler() {
    return (_tile!=null)?_tile.getTranscaler():null;
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
      _bhp = (bhp!=null)?new Projector(bhp):null;
      _bvp = (bvp!=null)?new Projector(bvp):null;
      if (_tile!=null)
        _tile.alignProjectors();
    }
  }
  
  /**
   * Gets the best horizontal projector for this tiled view.
   * @return the best horizontal projector; by reference, not by copy.
   */
  protected Projector getBestHorizontalProjector() {
    return _bhp;
  }
  
  /**
   * Gets the best vertical projector for this tiled view.
   * @return the best vertical projector; by reference, not by copy.
   */
  protected Projector getBestVerticalProjector() {
    return _bvp;
  }

  /**
   * Requests a repaint of the tile that contains this tiled view.
   */
  protected void repaint() {
    if (_tile!=null)
      _tile.repaint();
  }

  /**
   * Gets the line width for the specified graphics context.
   * @param g2d the graphics context.
   * @return the line width.
   */
  protected float getLineWidth(Graphics2D g2d) {
    float lineWidth = 1.0f;
    Stroke stroke = g2d.getStroke();
    if (stroke instanceof BasicStroke) {
      BasicStroke bs = (BasicStroke)stroke;
      lineWidth = bs.getLineWidth();
    }
    return lineWidth;
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  /**
   * Called by the tile when this tiled view is added or removed.
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

