/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

/**
 * An SWT canvas that paints via OpenGL. To paint an SWT canvas using 
 * OpenGL, construct an instance of this class with an OpenGL painter,
 * or extend this class and implement the method {@link #glPaint()}.
 * <p>
 * Classes that extend this class may also implement the methods
 * {@link #glInit()} and {@link #glResize(int,int,int,int)}.
 * @author Dave Hale, Colorado School of Mines
 * @author Dean Witte, Transform Software
 * @version 2004.11.24
 */
public class GlSwtCanvas extends Canvas {

  /**
   * Constructs a canvas with no OpenGL painter.
   * @param parent the parent of this canvas.
   * @param style the canvas style.
   */
  public GlSwtCanvas(Composite parent, int style) {
    this(parent,style,null);
  }

  /**
   * Constructs a canvas with the specified OpenGL painter.
   * @param parent the parent of this canvas.
   * @param style the canvas style.
   * @param painter the OpenGL painter.
   */
  public GlSwtCanvas(Composite parent, int style, GlPainter painter) {
    super(parent,style);
    _painter = painter;
    _context = new GlContext(this);
    addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent pe) {
        paint(pe);
      }
    });
    addDisposeListener(new DisposeListener() {
      public void widgetDisposed(DisposeEvent de) {
        _context.dispose();
      }
    });
  }

  /**
   * Gets the OpenGL context for this canvas.
   * @return the context.
   */
  public GlContext getContext() {
    return _context;
  }

  /**
   * Initializes OpenGL state when this canvas is first painted.
   * This method is called before the methods 
   * {@link #glResize(int,int,int,int)} and {@link #glPaint()} when (1)
   * this canvas must be painted and (2) it has never been painted before.
   * <p>
   * In classes that extend this class, implementations of this method 
   * use the OpenGL context that has been locked for the current thread. 
   * This implementation does nothing.
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
   * This implementation does nothing.
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
   * This implementation does nothing.
   */
  public void glPaint() {
    if (_painter!=null)
      _painter.glPaint();
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Paints this canvas. 
   * Handles locking, unlocking, and front-back buffer swapping for
   * the OpenGL context constructed for this canvas. Calls the method
   * {@link #glPaint()} and, as necessary, the methods {@link #glInit()}
   * and {@link #glResize(int,int,int,int)}.
   * @param pe the paint event; not used in this implementation.
   */
  protected void paint(PaintEvent pe) {
    _context.lock();
    try {
      if (!_inited) {
        glInit();
        _inited = true;
      }
      Point size = getSize();
      int width = size.x;
      int height = size.y;
      if (_width!=width || _height!=height) {
        glResize(width,height,_width,_height);
        _width = width;
        _height = height;
      }
      glPaint();
      _context.swapBuffers();
    } finally {
      _context.unlock();
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private GlPainter _painter;
  private GlContext _context;
  private boolean _inited;
  private int _width,_height;
}

