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

  private Tile _tile; // non-null while zooming a tile
  private TileAxis _axis; // non-null while zooming an axis
  private int _xbegin; // x coordinate where zoom began
  private int _ybegin; // y coordinate where zoom began
  private int _xlast; // x coordinate where mouse was last
  private int _ylast; // y coordinate where mouse was last
  private Graphics _graphics; // graphics used to draw zoom rectangle

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
    _tile = tile;
    _axis = null;
    _xbegin = e.getX();
    _ybegin = e.getY();
    drawZoom(tile,_xbegin,_ybegin);
    tile.addMouseMotionListener(_mml);
  }

  private void duringZoom(Tile tile, MouseEvent e) {
    drawZoom(tile,_xlast,_ylast);
    drawZoom(tile,e.getX(),e.getY());
  }

  private void endZoom(Tile tile, MouseEvent e) {
    drawZoom(tile,_xlast,_ylast);
    tile.removeMouseMotionListener(_mml);
    _tile = null;
    _axis = null;
  }

  private void beginZoom(TileAxis axis, MouseEvent e) {
    _axis = axis;
    _tile = null;
    _xbegin = e.getX();
    _ybegin = e.getY();
    axis.addMouseMotionListener(_mml);
  }

  private void duringZoom(TileAxis axis, MouseEvent e) {
  }

  private void endZoom(TileAxis axis, MouseEvent e) {
    axis.removeMouseMotionListener(_mml);
    _axis = null;
    _tile = null;
  }

  private void drawZoom(Tile tile, int x, int y) {
    if (tile==null)
      return;

    // If this is tile in which zoom began, ...
    if (tile==_tile) {

      // Draw zoom rectangle in this tile.
      drawRect(tile,x,y);

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

  private void drawRect(JComponent c, int x, int y) {
    int x1 = _xbegin;
    int y1 = _ybegin;
    int x2 = x;
    int y2 = y;
    int xmin = min(x1,x2);
    int xmax = max(x1,x2);
    int ymin = min(y1,y2);
    int ymax = max(y1,y2);
    Graphics g = c.getGraphics();
    g.setXORMode(c.getBackground());
    g.setColor(Color.RED);
    g.drawRect(xmin,ymin,xmax-xmin,ymax-ymin);
    g.dispose();
    _xlast = x;
    _ylast = y;
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

