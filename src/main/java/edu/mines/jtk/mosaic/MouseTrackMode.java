/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.mines.jtk.awt.Mode;
import edu.mines.jtk.awt.ModeManager;

/**
 * A mode for tracking the mouse location. When this mode is active,
 * then mouse movement within any tile is highlighted in any tile axes
 * in that tile's row and column. This mode is not exclusive.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.01
 */
public class MouseTrackMode extends Mode {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a mouse track mode with specified manager.
   * @param modeManager the mode manager for this mode.
   */
  public MouseTrackMode(ModeManager modeManager) {
    super(modeManager);
    setName("Track");
    setIcon(loadIcon(MouseTrackMode.class,"Track24.gif"));
    setMnemonicKey(KeyEvent.VK_Z);
    setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_T,0));
    setShortDescription("Track mouse in tile");
  }

  /**
   * Returns false, to indicate that mouse track mode is not exclusive.
   * @return false.
   */
  public boolean isExclusive() {
    return false;
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected
  
  protected void setActive(Component component, boolean active) {
    if ((component instanceof Tile)) {
      if (active) {
        component.addMouseListener(_ml);
      } else {
        component.removeMouseListener(_ml);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Tile _tile; // tile in which tracking; null, if not tracking.
  private int _xmouse; // x coordinate where mouse last tracked
  private int _ymouse; // y coordinate where mouse last tracked

  private MouseListener _ml = new MouseAdapter() {
    public void mouseEntered(MouseEvent e) {
      beginTracking(e);
    }
    public void mouseExited(MouseEvent e) {
      endTracking();
    }
  };

  private MouseMotionListener _mml = new MouseMotionAdapter() {
    public void mouseDragged(MouseEvent e) {
      duringTracking(e);
    }
    public void mouseMoved(MouseEvent e) {
      duringTracking(e);
    }
  };

  private void beginTracking(MouseEvent e) {
    _xmouse = e.getX();
    _ymouse = e.getY();
    _tile = (Tile)e.getSource();
    beginTracking(_tile.getTileAxisTop(),_xmouse,_ymouse);
    beginTracking(_tile.getTileAxisLeft(),_xmouse,_ymouse);
    beginTracking(_tile.getTileAxisBottom(),_xmouse,_ymouse);
    beginTracking(_tile.getTileAxisRight(),_xmouse,_ymouse);
    fireTrack();
    _tile.addMouseMotionListener(_mml);
  }
  private void beginTracking(TileAxis ta, int x, int y) {
    if (ta!=null)
      ta.beginTracking(x,y);
  }

  private void duringTracking(MouseEvent e) {
    _xmouse = e.getX();
    _ymouse = e.getY();
    _tile = (Tile)e.getSource();
    duringTracking(_tile.getTileAxisTop(),_xmouse,_ymouse);
    duringTracking(_tile.getTileAxisLeft(),_xmouse,_ymouse);
    duringTracking(_tile.getTileAxisBottom(),_xmouse,_ymouse);
    duringTracking(_tile.getTileAxisRight(),_xmouse,_ymouse);
    fireTrack();
  }
  private void duringTracking(TileAxis ta, int x, int y) {
    if (ta!=null)
      ta.duringTracking(x,y);
  }

  private void endTracking() {
    _tile.removeMouseMotionListener(_mml);
    endTracking(_tile.getTileAxisTop());
    endTracking(_tile.getTileAxisLeft());
    endTracking(_tile.getTileAxisBottom());
    endTracking(_tile.getTileAxisRight());
    fireTrack();
    _tile = null;
  }
  private void endTracking(TileAxis ta) {
    if (ta!=null)
      ta.endTracking();
  }

  // Someday this might fire mouse track events to any listeners.
  // Currently, this method does nothing.
  private void fireTrack() {
    /*
    if (_tile==null) {
    } else {
      Projector hp = _tile.getHorizontalProjector();
      Projector vp = _tile.getVerticalProjector();
      Transcaler ts = _tile.getTranscaler();
      double ux = ts.x(_xmouse);
      double uy = ts.y(_ymouse);
      double vx = hp.v(ux);
      double vy = vp.v(uy);
    }
    */
  }
}

