/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.opengl;

import java.awt.Canvas;
import java.awt.Graphics;

/**
 * An AWT canvas that paints via OpenGL.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public class GlAwtCanvas extends Canvas {

  /**
   * Constructs a canvas with the specified painter.
   * @param painter the OpenGL painter.
   */
  public GlAwtCanvas(GlPainter painter) {
    _painter = painter;
  }

  /**
   * Gets the OpenGL context for this canvas.
   * @return the context; null, if canvas has never been painted.
   */
  public GlContext getContext() {
    return _context;
  }

  public void paint(Graphics g) {
    if (_context==null)
      _context = new GlContext(this);
    _context.lock();
    try {
      _painter.glPaint();
      _context.swapBuffers();
    } finally {
      _context.unlock();
    }
  }

  public void update(Graphics g) {
    paint(g);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private GlContext _context;
  private GlPainter _painter;
}
