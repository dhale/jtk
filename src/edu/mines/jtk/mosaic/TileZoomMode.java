/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A mode for zooming tiles and tile axes.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.29
 */
public class TileZoomMode extends Mode {

  /**
   * Constructs a tile zoom mode with specified mosaic, name, and icon.
   * @param mosaic the mosaic.
   * @param name the name.
   * @param icon the icon.
   */
  public TileZoomMode(Mosaic mosaic, String name, Icon icon) {
    super(mosaic.getModeManager(),name,icon);
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected
  
  protected void setActive(Component component, boolean active) {
    if ((component instanceof Tile) || (component instanceof TileAxis)) {
      if (active) {
        component.addMouseListener(_ml);
      } else {
        component.removeMouseListener(_ml);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Tile _tile; // non-null while zooming a tile
  private TileAxis _axis; // non-null while zooming an axis
  private int _xbegin; // x coordinate where zoom began
  private int _ybegin; // y coordinate where zoom began

  private MouseListener _ml = new MouseAdapter() {;
    public void mousePressed(MouseEvent e) {
      if (e.getSource() instanceof Tile) {
        beginZoom((Tile)e.getSource(),e);
      } else if (e.getSource() instanceof TileAxis) {
        beginZoom((TileAxis)e.getSource(),e);
      }
    }
    public void mouseReleased(MouseEvent e) {
      if (e.getSource() instanceof Tile) {
        endZoom((Tile)e.getSource(),e);
      } else if (e.getSource() instanceof TileAxis) {
        endZoom((TileAxis)e.getSource(),e);
      }
    }
  };

  private MouseMotionListener _mml = new MouseMotionAdapter() {
    public void mouseDragged(MouseEvent e) {
      if (e.getSource() instanceof Tile) {
        duringZoom((Tile)e.getSource(),e);
      } else if (e.getSource() instanceof TileAxis) {
        duringZoom((TileAxis)e.getSource(),e);
      }
    }
  };

  private void beginZoom(Tile tile, MouseEvent e) {
    tile.addMouseMotionListener(_mml);
    _tile = tile;
    _axis = null;
    _xbegin = e.getX();
    _ybegin = e.getY();
  }

  private void duringZoom(Tile tile, MouseEvent e) {
  }

  private void endZoom(Tile tile, MouseEvent e) {
    _tile = null;
    _axis = null;
    tile.removeMouseMotionListener(_mml);
  }

  private void beginZoom(TileAxis axis, MouseEvent e) {
    axis.addMouseMotionListener(_mml);
    _axis = axis;
    _tile = null;
    _xbegin = e.getX();
    _ybegin = e.getY();
  }

  private void duringZoom(TileAxis axis, MouseEvent e) {
  }

  private void endZoom(TileAxis axis, MouseEvent e) {
    _axis = null;
    _tile = null;
    axis.removeMouseMotionListener(_mml);
  }

  private void drawZoom(Tile tile, int x, int y) {
    if (tile==null)
      return;

    // If this is tile in which zoom began, ...
    if (tile==_tile) {

      // Draw zoom rectangle in this tile.
      // TODO: implement draw zoom rectangle

      // Draw zoom lines in other tiles and axes in this tile's row and column.
      Mosaic mosaic = tile.getMosaic();
      int jrow = tile.getRowIndex();
      int jcol = tile.getColumnIndex();
      int nrow = mosaic.countRows();
      int ncol = mosaic.countColumns();
      for (int irow=0; irow<nrow; ++irow) {
        if (irow!=jrow)
          drawZoom(mosaic.getTile(irow,jcol),x,y);
      }
      for (int icol=0; icol<ncol; ++icol) {
        if (icol!=jcol)
          drawZoom(mosaic.getTile(jrow,icol),x,y);
      }
      drawZoom(mosaic.getTileAxisTop(jcol),x,y);
      drawZoom(mosaic.getTileAxisLeft(jrow),x,y);
      drawZoom(mosaic.getTileAxisBottom(jcol),x,y);
      drawZoom(mosaic.getTileAxisRight(jrow),x,y);
    }
    
    // Else, if this is not the tile in which zoom began, ...
    else {

      // Draw zoom lines in other tile.
      // TODO: implement draw zoom lines
    }
  }

  private void drawZoom(TileAxis axis, int x, int y) {
    if (axis==null)
      return;

    // If this is the axis in which zoom began, ...
    if (axis==_axis) {

      // Draw zoom lines in this axis.
      // TODO: implement draw zoom lines

      // Draw zoom lines in other tiles and axes in this axis's row or column.
      Mosaic mosaic = axis.getMosaic();
      int index = axis.getIndex();
      if (axis.isHorizontal()) {
        int jcol = axis.getIndex();
        int nrow = mosaic.countRows();
        for (int irow=0; irow<nrow; ++irow)
          drawZoom(mosaic.getTile(irow,jcol),x,y);
        if (axis.isTop()) {
          drawZoom(mosaic.getTileAxisBottom(jcol),x,y);
        } else {
          drawZoom(mosaic.getTileAxisTop(jcol),x,y);
        }
      } else {
        int jrow = axis.getIndex();
        int ncol = mosaic.countColumns();
        for (int icol=0; icol<ncol; ++icol)
          drawZoom(mosaic.getTile(jrow,icol),x,y);
        if (axis.isLeft()) {
          drawZoom(mosaic.getTileAxisRight(jrow),x,y);
        } else {
          drawZoom(mosaic.getTileAxisLeft(jrow),x,y);
        }
      }
    }
    
    // Else, if this is not the axis in which zoom began, ...
    else {

      // Draw zoom lines in other axis.
      // TODO: implement draw zoom lines
    }
  }
}

