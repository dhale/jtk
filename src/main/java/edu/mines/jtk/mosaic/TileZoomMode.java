/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.*;
import javax.swing.*;

import edu.mines.jtk.awt.Mode;
import edu.mines.jtk.awt.ModeManager;

/**
 * A mode for zooming tiles and tile axes.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.29
 */
public class TileZoomMode extends Mode {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a tile zoom mode with specified manager.
   * @param modeManager the mode manager for this mode.
   */
  public TileZoomMode(ModeManager modeManager) {
    super(modeManager);
    setName("Zoom");
    setIcon(loadIcon(TileZoomMode.class,"ZoomIn16.gif"));
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

  private MouseListener _ml = new MouseAdapter() {
    public void mousePressed(MouseEvent e) {
      if (e.isControlDown() && e.isAltDown()) {
        Object source = e.getSource();
        if (source instanceof Tile) {
          Tile tile = (Tile)source;
          Container frame = tile.getTopLevelAncestor();
          Projector hp = tile.getHorizontalProjector();
          Projector vp = tile.getVerticalProjector();
          DRectangle r = tile.getViewRectangle();
          double ffsize = frame.getFont().getSize();
          double fwidth = frame.getWidth();
          double fheight = frame.getHeight();
          double fratio = fwidth/fheight; 
          double pwidth = tile.getWidth();
          double pheight = tile.getHeight();
          double pratio = pwidth/pheight; 
          double vwidth = abs(hp.v(r.x)-hp.v(r.x+r.width));
          double vheight = abs(vp.v(r.y)-vp.v(r.y+r.height));
          double vratio = vwidth/vheight; 
          System.out.printf("Tile: frame font size  = %1d%n",(int)ffsize);
          System.out.printf("      frame width  = %1d%n",(int)fwidth);
          System.out.printf("      frame height = %1d%n",(int)fheight);
          System.out.printf("      frame ratio  = %1.4g%n",fratio);
          System.out.printf("      pixel width  = %1d%n",(int)pwidth);
          System.out.printf("      pixel height = %1d%n",(int)pheight);
          System.out.printf("      pixel ratio  = %1.4g%n",pratio);
          System.out.printf("      value width  = %1.4g%n",vwidth);
          System.out.printf("      value height = %1.4g%n",vheight);
          System.out.printf("      value ratio  = %1.4g%n",vratio);
        }
      } else {
        beginZoom(e);
      }
    }
    public void mouseReleased(MouseEvent e) {
      endZoom();
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

  private void endZoom() {

    // Tile for which to set the view rectangle, and flags for x and y zoom.
    Tile tile = null;
    boolean zx = false;
    boolean zy = false;

    // If zoom began in a tile, ...
    if (_tile!=null) {
      tile = _tile;
      zx = true;
      zy = true;
      drawZoom(_tile,_xdraw,_ydraw,zx,zy);
      _tile.removeMouseMotionListener(_mml);
      _tile = null;
    } 

    // Else, if zoom began in an axis, ...
    else if (_axis!=null) {
      tile = _axis.getTile();
      zx = _axis.isHorizontal();
      zy = _axis.isVertical();
      drawZoom(_axis,_xdraw,_ydraw,zx,zy);
      _axis.removeMouseMotionListener(_mml);
      _axis = null;
    }

    // If we have a tile for which we must set the view rectangle, ...
    if (tile!=null && (zx || zy)) {
      int xmin = min(_xbegin,_xdraw);
      int xmax = max(_xbegin,_xdraw);
      int ymin = min(_ybegin,_ydraw);
      int ymax = max(_ybegin,_ydraw);
      Transcaler ts = tile.getTranscaler();
      DRectangle vr = tile.getViewRectangle();

      // If zooming or unzooming x, ...
      if (zx) {
        vr.x = (xmin<xmax)?ts.x(xmin):0.0;
        vr.width = (xmin<xmax)?ts.x(xmax)-vr.x:1.0;
      }

      // If zooming or unzooming y, ...
      if (zy) {
        vr.y = (ymin<ymax)?ts.y(ymin):0.0;
        vr.height = (ymin<ymax)?ts.y(ymax)-vr.y:1.0;
      }

      // Arbitrarily limit zoom to a factor of 10000.
      double tiny = 0.0001;
      if (vr.width<tiny) {
        vr.x -= (tiny-vr.width)/2;
        vr.width = tiny;
      }
      if (vr.height<tiny) {
        vr.y -= (tiny-vr.height)/2;
        vr.height = tiny;
      }
      vr.x = min(1.0-vr.width,vr.x);
      vr.y = min(1.0-vr.height,vr.y);

      // Set view rectangle of one tile, and mosaic will set the others.
      tile.setViewRectangle(vr);
    }
  }

  private void drawZoom(Tile tile, int x, int y, boolean bx, boolean by) {
    if (tile==null)
      return;

    // If this is tile in which zoom began, ...
    if (tile==_tile) {

      // Clip zoom to tile bounds.
      x = max(0,min(tile.getWidth()-1,x));
      y = max(0,min(tile.getHeight()-1,y));

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

      // Clip zoom to axis bounds.
      x = max(0,min(axis.getWidth()-1,x));
      y = max(0,min(axis.getHeight()-1,y));

      // Draw zoom in this axis.
      drawRect(axis,x,y,bx,by);

      // Draw zoom in other tiles and axes in this axis's row or column.
      Mosaic mosaic = axis.getMosaic();
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

