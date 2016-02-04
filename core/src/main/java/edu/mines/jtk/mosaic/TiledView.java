/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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

  /**
   * Gets the scale of the best horizontal projector.
   * @return the scale of the best horizontal projector.
   */  
  public AxisScale getHScale(){
    return (_bhp!=null)?_bhp.getScale():AxisScale.LINEAR;
  }

  /**
   * Gets the scale of the best vertical projector.
   * @return the scale of the best horizontal projector.
   */  
  public AxisScale getVScale(){
    return (_bvp!=null)?_bvp.getScale():AxisScale.LINEAR;
  }

  /**
   * Sets the scale both best projectors.
   * The method must be overridden by a subclass
   * to implement non-linear scaling.
   * @param hscale the new horizontal scale
   * @param vscale the new vertical scale
   * @param align whether to align Tile Projectors after setting scale
   * @return this TiledView
   */  
  public TiledView setScales(AxisScale hscale, AxisScale vscale, boolean align){
    return this;
  }
  
  /**
   * Convenience method to set both axis scales separately and
   * align tile projectors
   * @param hscale the new horizontal scale
   * @param vscale the new vertical scale
   * @return this TiledView
   */  
  public TiledView setScales(AxisScale hscale, AxisScale vscale){
    return  setScales(hscale,vscale,true);
  }
  
  /**
   * Convenience method to set both scales the same
   * @param scale the new scale
   * @return this TiledView
   */  
  public TiledView setScales(AxisScale scale){
    return setScales(scale,scale,true);
  }  
  
  /**
   * Convenience method to set the scale of the 
   * best horizontal projector.
   * @param scale the new scale 
   * @return this TiledView
   */  
  public TiledView setHScale(AxisScale scale){
    return setScales(scale,_bvp.getScale(),true);
  }

  /**
   * Convenience method to set the scale of the 
   * best vertical projector.
   * @param scale the new scale
   * @return this TiledView
   */  
  public TiledView setVScale(AxisScale scale){
    return setScales(_bhp.getScale(),scale,true);
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
    setBestProjectors(bhp,bvp,true);
  }
  
  /**
   * Sets the best projectors for this tiled view. Classes that extend
   * this class call this method when their best projectors change.
   * If this tiled view is in a tile, Tile will be realigned if parameter
   * align is set to true.
   */
  protected void setBestProjectors(Projector bhp, Projector bvp, boolean align) {
    if (!equal(_bhp,bhp) || !equal(_bvp,bvp)) {
      _bhp = (bhp!=null)?new Projector(bhp):null;
      _bvp = (bvp!=null)?new Projector(bvp):null;
      if (_tile!=null && align)
        _tile.alignProjectors(_bhp.getScale(),_bvp.getScale());
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

