/****************************************************************************
Copyright 2016, Colorado School of Mines.
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

/**
 * An implementation of the AxisScalable interface for use
 * with TiledView classes displaying a 1D function.
 * <p>
 *  *** currently implemented for PointsView only ***
 * <p>
 * This implementation stores a reference to its parent
 * AxisScalable object and keeps track of its axis scale
 * state information. Setting an axis scale will change the
 * local state values as well as changing the scale value of
 * the associated Projector. 
 * @author Eric Addison
 * @version 2016.1.25
 */
public class AxisScalable1D implements AxisScalable {

  /**
   * Constructs and AxisScalable1D object with a parent AxisScalable
   * as, horizontal scale hscale, and vertical scale vscale.
   * @param as the parent AxisScalable
   * @param hscale the horizontal scaling
   * @param vscale the vertical scaling 
   */
  public AxisScalable1D(AxisScalable as, AxisScale hscale, AxisScale vscale) {
    _as = as;
    _hscale = hscale;
    _vscale = vscale;
  }
  
  /**
   * Sets the horizontal axis scaling.
   * @param s the new scale
   * @return the parent AxisScalable object _as
   */
  public AxisScalable setHScale(AxisScale s) {
    if(s != _hscale){
      Projector hp = ((TiledView)_as).getHorizontalProjector();
      Tile tile = ((TiledView)_as).getTile();
      if(hp != null)
        hp.setScale(s);
      if( tile != null)
        tile.setHScale(s);
    }
    _hscale = s;  
    return _as;
  }

  /**
   * Sets the vertical axis scaling.
   * @param s the new scale
   * @return the parent AxisScalable object _as
   */
  public AxisScalable setVScale(AxisScale s) {
  if(s != _vscale){
    Projector vp = ((TiledView)_as).getVerticalProjector();
    Tile tile = ((TiledView)_as).getTile();
    if(vp != null)
      vp.setScale(s);
    if( tile != null)
      tile.setVScale(s);
  }
  _vscale = s; 
    return _as;
  }

  /**
   * Gets the current horizontal axis scaling.
   * @return the horizontal scale
   */
  public AxisScale getHScale() {
    return _hscale;
  }

  /**
   * Gets the current vertical axis scaling.
   * @return the vertical scale
   */
  public AxisScale getVScale() {
    return _vscale;
  }
  
  ///////////////////////////////////////////////////////////////////////////
  // private
  
  private AxisScalable _as;
  private AxisScale _hscale, _vscale;
  


}