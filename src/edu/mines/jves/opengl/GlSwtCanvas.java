/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.opengl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * An SWT canvas that paints via OpenGL. To paint an SWT canvas using 
 * OpenGL, extend this class and implement the method {@link #glPaint()}.
 * <p>
 * Classes that extend this class may also implement the methods
 * {@link #glInit()} and {@link #glResize(int,int,int,int)}.
 * @author Dave Hale, Colorado School of Mines
 * @author Dean Witte, Transform Software
 * @version 2004.11.24
 */
public class GlSwtCanvas extends Canvas {

  /**
   * Constructs a canvas.
   * @param parent The parent component of this canvas; passed to the 
   *  superclass constructor.
   * @param style The style flags; passed to the superclass constructor.
   */
  public GlSwtCanvas(Composite parent, int style) {
    super(parent,style);
    _context = new GlContext(this);
    addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent pe) {
        paint(pe);
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
   * {@link #glResize(int,int,int,int)} and {@link #glPaint()} when 
   * (1) this this canvas must be painted and (2) it has never been 
   * painted before.
   * <p>
   * In classes that extend this class, implementations of this method 
   * use the OpenGL context that has been locked for the current thread. 
   * This implementation does nothing.
   */
  public void glInit() {
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
   * @param widthOld the width before resizing.
   * @param heightOld the height before resizing.
   * @param widthNew the width after resizing.
   * @param heightNew the height after resizing.
   */
  public void glResize(
    int widthOld, int heightOld, 
    int widthNew, int heightNew) 
  {
  }

  /**
   * Paints this canvas via OpenGL.
   * <p>
   * In classes that extend this abstract class, implementations of this 
   * method use the OpenGL context that has been locked for the current 
   * thread. This implementation does nothing.
   */
  public void glPaint() {
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
        glResize(_width,_height,width,height);
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

  private GlContext _context;
  private boolean _inited;
  private int _width,_height;
}

