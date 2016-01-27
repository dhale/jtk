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

import java.util.Iterator;

/**
 * An implementation of the AxisScalable interface for use
 * with the Tile class.
 * <p>
 * This implementation stores a reference to its Tile object,
 * and when setting an axis scale with a definite value of
 * Scale (not Scale.AUTO), will also set the Scale of the other
 * Tiles in the corresponding Mosaic row or column, if applicable.
 * <p>
 * AxisScalableTile also stores the axis scale state information
 * associated with the Tile object stored in _tile. 
 * @author Eric Addison
 * @version 2016.1.25
 */
public class AxisScalableTile implements AxisScalable {

  /**
   * Constructs an AxisScalableTile with a reference to the Tile
   * tile, with initial scales hscale and vscale.
   * @param tile the Tile to work on
   * @param hscale the initial horizontal axis scale
   * @param vscale the initial vertical axis scale
   */
  public AxisScalableTile(Tile tile, AxisScale hscale, AxisScale vscale) {
    _tile = tile;
    _hscale = hscale;
    _vscale = vscale;
  }

  /**
   * Sets the horizontal axis scaling.
   * @param s the new scale
   * @return the stored AxisScalable object (Tile)
   */
  public AxisScalable setHScale(AxisScale s) {
    if (_hscale != s) {
      _hscale = s;

      // set all TiledViews in this tile to new scale
      Iterator<TiledView> itr = _tile.getTiledViews();
      while (itr.hasNext()) {
        TiledView tv = itr.next();
        if (tv instanceof AxisScalable)
          ((AxisScalable) tv).setHScale(_hscale);
      }

      // set other tiles in this row to new scale
      Mosaic mos = _tile.getMosaic();
      for (int jrow = 0; jrow < mos.countRows(); ++jrow) {
        Tile t = mos.getTile(jrow, _tile.getColumnIndex());
        t.setHScale(_hscale);
      }

    }

    // update axistics for this tile
    if (_tile.getTileAxisTop() != null) {
      _tile.getTileAxisTop().updateAxisTics();
      _tile.getTileAxisTop().repaint();
    }
    if (_tile.getTileAxisBottom() != null) {
      _tile.getTileAxisBottom().updateAxisTics();
      _tile.getTileAxisBottom().repaint();
    }
    _tile.repaint();
    return _tile;
  }

  /**
   * Sets the vertical axis scaling.
   * @param s the new scale
   * @return the stored AxisScalable object (Tile)
   */
  public AxisScalable setVScale(AxisScale s) {
    if (_vscale != s) {
      _vscale = s;
      // set all TiledViews in this tile to new scale
      Iterator<TiledView> itr = _tile.getTiledViews();
      while (itr.hasNext()) {
        TiledView tv = itr.next();
        if (tv instanceof AxisScalable)
          ((AxisScalable) tv).setVScale(_vscale);
      }

      // set other tiles in this row to new scale
      Mosaic mos = _tile.getMosaic();
      for (int jcol = 0; jcol < mos.countColumns(); ++jcol) {
        Tile t = mos.getTile(_tile.getRowIndex(), jcol);
        t.setVScale(_vscale);
      }

    }

    // update axistics for this tile
    if (_tile.getTileAxisLeft() != null) {
      _tile.getTileAxisLeft().updateAxisTics();
      _tile.getTileAxisLeft().repaint();
    }
    if (_tile.getTileAxisRight() != null) {
      _tile.getTileAxisRight().updateAxisTics();
      _tile.getTileAxisRight().repaint();
    }
    _tile.repaint();
    return _tile;
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

  private Tile _tile;
  private AxisScale _hscale, _vscale;
  
}
