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
 * A tile in a mosaic.
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
   * Returns the number of tiled views in this this.
   * @return the number of tiled views.
   */
  public int countTiledViews() {
    return _tvs.size();
  }

  /**
   * Gets the view rectangle for this tile.
   * @return the view rectangle.
   */
  public DRectangle getViewRectangle() {
    return _vr.clone();
  }

  /**
   * Sets the view rectangle for this tile. The view rectangle represents
   * the subset of normalized coordinate space that is displayed in this 
   * tile. Setting the view rectangle may cause the tile to zoom or scroll.
   * <p>
   * If this tile is in a mosaic, calling this method will cause the mosaic 
   * to set the view rectangles of any other tiles in the same row or column 
   * accordingly.
   * @param vr the view rectangle.
   */
  public void setViewRectangle(DRectangle vr) {
    if (_mosaic!=null) {
      _mosaic.setViewRectangleInternal(this,vr);
    } else {
      setViewRectangleInternal(vr);
    }
  }

  // We override this method so that we can update our transcaler.
  // We assume that this is the *only* way that our size changes.
  // Also, we assume that a repaint is already pending.
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x,y,width,height);
    _ts.setMapping(0,width-1,0,height-1);
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
    for (TiledView tv : _tvs) {
      tv.setProjectors(_hp,_vp);
    }
    repaint();
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
   * Called by this tile's mosaic.
   */
  void setViewRectangleInternal(DRectangle vr) {
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
