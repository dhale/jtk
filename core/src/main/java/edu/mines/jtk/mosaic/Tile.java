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
   * Sets limits for the both horizontal and vertical axes.
   * By default, limits are computed automatically by tiled graphical views.
   * This method can be used to override those default limits.
   * @param hmin the minimum value.
   * @param vmin the minimum value.
   * @param hmax the maximum value.
   * @param vmax the maximum value.
   */
  public void setLimits(double hmin, double vmin, double hmax, double vmax) {
    setHLimits(hmin,hmax);
    setVLimits(vmin,vmax);
  }
  
  /**
   * Sets limits for the horizontal axis.
   * By default, limits are computed automatically by tiled graphical views.
   * This method can be used to override those default limits.
   * @param hmin the minimum value.
   * @param hmax the maximum value.
   */
  public void setHLimits(double hmin, double hmax) {
    Check.argument(hmin<hmax,"hmin<hmax");
    if(_bhp!=null &&_bhp.isLog())
      Check.argument(hmin>0,"hmin>0 for LOG scales");
    _shp = new Projector(hmin,hmax);
    alignProjectors();
  }
  
  /**
   * Sets limits for the vertical axis.
   * By default, limits are computed automatically by tiled graphical views.
   * This method can be used to override those default limits.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   */
  public void setVLimits(double vmin, double vmax) {
    Check.argument(vmin<vmax,"vmin<vmax");
    if(_bvp!=null && _bvp.isLog())
      Check.argument(vmin>0,"vmin>0 for LOG scales");
    if(_bvp.v0()<_bvp.v1())
      _svp = new Projector(vmin,vmax);
    else
      _svp = new Projector(vmax,vmin);
    alignProjectors();
  }
  
  /**
   * Sets default limits for horizontal and vertical axes. This method may
   * be used to restore default limits after they have been set explicitly.
   */
  public void setLimitsDefault() {
    _svp=null;
    _shp=null;
    alignProjectors();
  }
  
  /**
   * Sets default limits for the horizontal axis. This method may be used 
   * to restore default limits after they have been set explicitly.
   */
  public void setHLimitsDefault() {
    _shp=null;
    alignProjectors();
  }
  
  /**
   * Sets default limits for the vertical axis. This method may be used 
   * to restore default limits after they have been set explicitly.
   */
  public void setVLimitsDefault() {
    _svp=null;
    alignProjectors();
  }
  
  /**
   * Sets the best horizontal projector for this tile. If null, this
   * tile will compute its best horizontal projector by merging those
   * of its tiled views. If not null, the specified projector is best.
   * In either case, this tile's horizontal projector may be adjusted by 
   * it's mosaic during alignment with other tiles in the same column.
   * @param bhp the best horizontal projector.
   */
  @Deprecated
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
  @Deprecated
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

  /**
   * Gets the Horizontal axis scaling.
   * @return the Scale; null, if none.
   */
  public AxisScale getHScale() {
    return (_bhp!=null)?_bhp.getScale():AxisScale.LINEAR;
  }

  /**
   * Gets the Vertical axis scaling.
   * @return the Scale; null, if none.
   */
  public AxisScale getVScale() {
    return (_bvp!=null)?_bvp.getScale():AxisScale.LINEAR;
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
    alignProjectors(_hp.getScale(),_vp.getScale());
  }
  
  
  void alignProjectors(AxisScale hscale, AxisScale vscale) {
    updateBestProjectors(hscale,vscale);
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

  /**
   * Called when we might new realignment.
   */
  
  private void updateBestProjectors(AxisScale hscale, AxisScale vscale) {
    Projector bhp = null;
    Projector bvp = null;
    int ntv = _tvs.size();
    if (_shp==null) {
      int itv = ntv-1;
      for (; bhp==null && itv>=0; --itv) {
        TiledView tv = _tvs.get(itv);
        bhp = new Projector(tv.getBestHorizontalProjector());
      }
      for (; itv>=0; --itv) {
        TiledView tv = _tvs.get(itv);
        bhp.merge(tv.getBestHorizontalProjector());
      }
    } else
      _shp.setScale(hscale);
    
    if (_svp==null) {
      int itv = ntv-1;
      for (; bvp==null && itv>=0; --itv) {
        TiledView tv = _tvs.get(itv);
        bvp = new Projector(tv.getBestVerticalProjector());
      }
      for (; itv>=0; --itv) {
        TiledView tv = _tvs.get(itv);
        bvp.merge(tv.getBestVerticalProjector());
      }
    } else
      _svp.setScale(vscale);
    
    _bhp = (_shp!=null)?_shp:bhp;
    _bvp = (_svp!=null)?_svp:bvp;
    
    // reset scales to linear if disagreement
    boolean[] checkScales = checkViewScales(hscale,vscale);
    if(!checkScales[0])
      _bhp.setScale(AxisScale.LINEAR);
    if(!checkScales[1])
      _bvp.setScale(AxisScale.LINEAR);
  }
  
  // check to see if all view scales match the specified hscale and vscale
  private boolean[] checkViewScales(AxisScale hscale, AxisScale vscale) {
    boolean[] compat = new boolean[]{true,true};
    for(TiledView tv : _tvs){
      compat[0] = (tv.getHScale()==hscale && compat[0])?true:false;
      compat[1] = (tv.getVScale()==vscale && compat[1])?true:false; 
    }
    return compat;
  }
}
