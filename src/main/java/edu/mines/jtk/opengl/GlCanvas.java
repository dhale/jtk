/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl;

import java.awt.*;

/**
 * A canvas that paints via OpenGL. To paint a canvas using OpenGL, 
 * construct an instance of this class with an OpenGL painter,
 * or extend this class and implement the method {@link #glPaint()}.
 * <p>
 * Classes that extend this class may also implement the methods
 * {@link #glInit()} and {@link #glResize(int,int,int,int)}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.24
 */
public class GlCanvas extends Canvas {
  private static final long serialVersionUID = 1L;
    
 /**
   * Constructs a canvas with no OpenGL painter.
   */
  public GlCanvas() {
    this(null);
  }

  /**
   * Constructs a canvas with the specified OpenGL painter.
   * @param painter the OpenGL painter.
   */
  public GlCanvas(GlPainter painter) {
    _painter = painter;
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
   * Enables or disables automatic repainting. If enabled, then,
   * immediately after this canvas paints itself, it automatically
   * requests that it be painted again. By default, automatic
   * repainting is disabled.
   * @param autoRepaint true, for automatic repainting; false, otherwise.
   */
  public void setAutoRepaint(boolean autoRepaint) {
    _autoRepaint = autoRepaint;
  }

  /**
   * Dispose this context.
   */
  public synchronized void dispose() {
    _context.dispose();
  }

  /**
   * Initializes OpenGL state when this canvas is first painted.
   * This method is called before the methods 
   * {@link #glResize(int,int,int,int)} and {@link #glPaint()} when (1)
   * this canvas must be painted and (2) it has never been painted before.
   * <p>
   * In classes that extend this class, implementations of this method 
   * use the OpenGL context that has been locked for the current thread. 
   * This implementation simply delegates to the painter, if not null.
   */
  public void glInit() {
    if (_painter!=null)
      _painter.glInit();
  }

  /**
   * Modifies OpenGL state when this canvas has been resized.
   * This method is called before the method {@link #glPaint()} when
   * (1) this canvas must be painted and (2) its width or height have 
   * changed since it was last painted or it has never been painted.
   * <p>
   * In classes that extend this class, implementations of this method 
   * use the OpenGL context that has been locked for the current thread. 
   * This implementation simply delegates to the painter, if not null.
   * @param width the current width.
   * @param height the current height.
   * @param widthBefore the width before resizing.
   * @param heightBefore the height before resizing.
   */
  public void glResize(
    int width, int height, 
    int widthBefore, int heightBefore)
  {
    if (_painter!=null)
      _painter.glResize(width,height,widthBefore,heightBefore);
  }

  /**
   * Paints this canvas via OpenGL.
   * <p>
   * In classes that extend this class, implementations of this method 
   * use the OpenGL context that has been locked for the current thread. 
   * This implementation simply delegates to the painter, if not null.
   */
  public void glPaint() {
    if (_painter!=null)
      _painter.glPaint();
  }

  /**
   * Paints this canvas. Overrides the base-class implementation.
   * Handles locking, unlocking, and front-back buffer swapping for
   * the OpenGL context constructed for this canvas. Calls the method
   * {@link #glPaint()} and, as necessary, the methods {@link #glInit()}
   * and {@link #glResize(int,int,int,int)}.
   * @param g the graphics.
   */
  public void paint(Graphics g) {
    _context.lock();
    try {
      if (!_inited) {
        glInit();
        _inited = true;
      }
      int width = getWidth();
      int height = getHeight();
      if (_width!=width || _height!=height) {
        glResize(width,height,_width,_height);
        _width = width;
        _height = height;
      }
      glPaint();
      _context.swapBuffers();
      if (_autoRepaint)
        repaint();
    } finally {
      _context.unlock();
    }
  }

  /**
   * Updates this canvas. Overrides the base-class implementation.
   * This implementation simply calls {@link #paint(Graphics)}.
   * @param g the graphics.
   */
  public void update(Graphics g) {
    paint(g);
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void finalize() throws Throwable {
    try {
      dispose();
    } finally {
      super.finalize();
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private GlPainter _painter;
  private GlContext _context;
  private boolean _inited;
  private int _width,_height;
  private boolean _autoRepaint;
}
