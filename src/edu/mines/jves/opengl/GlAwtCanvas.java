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
 * An AWT canvas that paints via OpenGL. To paint an AWT canvas using 
 * OpenGL, extend this class and implement the method {@link #glPaint()}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public abstract class GlAwtCanvas extends Canvas {

  /**
   * Constructs a canvas.
   */
  public GlAwtCanvas() {
    _context = new GlContext(this);
  }

  /**
   * Gets the OpenGL context for this canvas.
   * @return the context.
   */
  public GlContext getContext() {
    return _context;
  }

  /**
   * Paints this canvas via the current OpenGL context.
   */
  public abstract void glPaint();

  /**
   * Paints this canvas. Overrides the base-class implementation.
   * This implementation (1) locks the OpenGL context, (2) calls 
   * {@link #glPaint()}, (3) swaps the front and back buffers, and 
   * finally (4) unlocks the OpenGL context.
   * @param g the graphics; not used in this implementation.
   */
  public void paint(Graphics g) {
    _context.lock();
    try {
      glPaint();
      _context.swapBuffers();
    } finally {
      _context.unlock();
    }
  }

  /**
   * Updates this canvas. Overrides the base-class implementation.
   * This implementation simply calls {@link #paint(Graphics)}.
   */
  public void update(Graphics g) {
    paint(g);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private GlContext _context;
}
