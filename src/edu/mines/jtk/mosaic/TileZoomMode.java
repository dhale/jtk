/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.*;
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
   * Constructs a tile zoom mode for the specified mosaic.
   * @param mosaic the mosaic.
   */
  public TileZoomMode(Mosaic mosaic) {
    super(mosaic.getModeManager());
    setName("Zoom");
    setIcon(loadIcon(TileZoomMode.class,"resources/Zoom24.gif"));
    setMnemonicKey(KeyEvent.VK_Z);
    setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_Z,0));
    setShortDescription("Zoom in tile or axis");
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

  private Tile _tile; // tile in which zooming began; null, if in axis
  private TileAxis _axis; // axis in which zooming began; null, if in tile
  private int _xbegin; // x coordinate where zoom began
  private int _ybegin; // y coordinate where zoom began
  private int _xdraw; // x coordinate to which zoom rect was last drawn
  private int _ydraw; // y coordinate to which zoom rect was last drawn

  private MouseListener _ml = new MouseAdapter() {;
    public void mousePressed(MouseEvent e) {
      beginZoom(e);
    }
    public void mouseReleased(MouseEvent e) {
      endZoom(e);
    }
  };

  private MouseMotionListener _mml = new MouseMotionAdapter() {
    public void mouseDragged(MouseEvent e) {
      duringZoom(e);
    }
  };

  private void beginZoom(MouseEvent e) {
    _xbegin = e.getX();
    _ybegin = e.getY();
    Object source = e.getSource();
    if (source instanceof Tile) {
      Tile tile = _tile = (Tile)source;
      drawZoom(tile,_xbegin,_ybegin,true,true);
      tile.addMouseMotionListener(_mml);
    } else if (source instanceof TileAxis) {
      TileAxis axis = _axis = (TileAxis)source;
      drawZoom(axis,_xbegin,_ybegin,_axis.isHorizontal(),_axis.isVertical());
      axis.addMouseMotionListener(_mml);
    }
  }

  private void duringZoom(MouseEvent e) {
    int xdraw = e.getX();
    int ydraw = e.getY();
    if (_tile!=null) {
      drawZoom(_tile,_xdraw,_ydraw,true,true);
      drawZoom(_tile, xdraw, ydraw,true,true);
    } else if (_axis!=null) {
      drawZoom(_axis,_xdraw,_ydraw,_axis.isHorizontal(),_axis.isVertical());
      drawZoom(_axis, xdraw, ydraw,_axis.isHorizontal(),_axis.isVertical());
    }
  }

  private void endZoom(MouseEvent e) {
    if (_tile!=null) {
      drawZoom(_tile,_xdraw,_ydraw,true,true);
      _tile.removeMouseMotionListener(_mml);
      _tile = null;
    } else if (_axis!=null) {
      drawZoom(_axis,_xdraw,_ydraw,_axis.isHorizontal(),_axis.isVertical());
      _axis.removeMouseMotionListener(_mml);
      _axis = null;
    }
  }

  private void drawZoom(Tile tile, int x, int y, boolean bx, boolean by) {
    if (tile==null)
      return;

    // If this is tile in which zoom began, ...
    if (tile==_tile) {

      // Draw zoom in this tile.
      drawRect(tile,x,y,bx,by);

      // Draw zoom in other tiles and axes in this tile's row and column.
      Mosaic mosaic = tile.getMosaic();
      int jrow = tile.getRowIndex();
      int jcol = tile.getColumnIndex();
      int nrow = mosaic.countRows();
      int ncol = mosaic.countColumns();
      for (int irow=0; irow<nrow; ++irow) {
        if (irow!=jrow)
          drawZoom(mosaic.getTile(irow,jcol),x,y,true,false);
      }
      for (int icol=0; icol<ncol; ++icol) {
        if (icol!=jcol)
          drawZoom(mosaic.getTile(jrow,icol),x,y,false,true);
      }
      drawZoom(mosaic.getTileAxisTop(jcol),x,y,true,false);
      drawZoom(mosaic.getTileAxisLeft(jrow),x,y,false,true);
      drawZoom(mosaic.getTileAxisBottom(jcol),x,y,true,false);
      drawZoom(mosaic.getTileAxisRight(jrow),x,y,false,true);
    }
    
    // Else, if this is not the tile in which zoom began, ...
    else {

      // Draw zoom in other tile.
      drawRect(tile,x,y,bx,by);
    }
  }

  private void drawZoom(TileAxis axis, int x, int y, boolean bx, boolean by) {
    if (axis==null)
      return;

    // If this is the axis in which zoom began, ...
    if (axis==_axis) {

      // Draw zoom in this axis.
      drawRect(axis,x,y,bx,by);

      // Draw zoom in other tiles and axes in this axis's row or column.
      Mosaic mosaic = axis.getMosaic();
      int index = axis.getIndex();
      if (axis.isHorizontal()) {
        int jcol = axis.getIndex();
        int nrow = mosaic.countRows();
        for (int irow=0; irow<nrow; ++irow)
          drawZoom(mosaic.getTile(irow,jcol),x,y,true,false);
        if (axis.isTop()) {
          drawZoom(mosaic.getTileAxisBottom(jcol),x,y,true,false);
        } else {
          drawZoom(mosaic.getTileAxisTop(jcol),x,y,true,false);
        }
      } else {
        int jrow = axis.getIndex();
        int ncol = mosaic.countColumns();
        for (int icol=0; icol<ncol; ++icol)
          drawZoom(mosaic.getTile(jrow,icol),x,y,false,true);
        if (axis.isLeft()) {
          drawZoom(mosaic.getTileAxisRight(jrow),x,y,false,true);
        } else {
          drawZoom(mosaic.getTileAxisLeft(jrow),x,y,false,true);
        }
      }
    }
    
    // Else, if this is not the axis in which zoom began, ...
    else {

      // Draw zoom in other axis.
      drawRect(axis,x,y,bx,by);
    }
  }

  private void drawRect(JComponent c, int x, int y, boolean bx, boolean by) {
    _xdraw = x;
    _ydraw = y;
    int xmin = bx?min(_xbegin,_xdraw):-1;
    int xmax = bx?max(_xbegin,_xdraw):c.getWidth();
    int ymin = by?min(_ybegin,_ydraw):-1;
    int ymax = by?max(_ybegin,_ydraw):c.getHeight();
    Graphics g = c.getGraphics();
    g.setColor(Color.RED);
    g.setXORMode(c.getBackground());
    g.drawRect(xmin,ymin,xmax-xmin,ymax-ymin);
    g.dispose();
  }
}

