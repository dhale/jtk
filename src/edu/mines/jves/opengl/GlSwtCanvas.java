/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.opengl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

/**
 * An SWT canvas that paints via OpenGL. To paint an SWT canvas using 
 * OpenGL, extend this class and implement the method {@link #glPaint()}.
 * @author Dave Hale, Colorado School of Mines
 * @author Dean Witte, Transform Software
 * @version 2004.11.24
 */
public abstract class GlSwtCanvas extends Canvas {

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
      public void paintControl(PaintEvent e) {
        e.gc.drawText("Hello world",50,50);
        paint(e);
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
   * Paints this canvas via the current OpenGL context.
   */
  public abstract void glPaint();

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Paints this canvas.  This implementation (1) locks the OpenGL context,
   * (2) calls {@link #glPaint()}, (3) swaps the front and back buffers, and 
   * finally (4) unlocks the OpenGL context.
   * @param pe the paint event; not used in this implementation.
   */
  protected void paint(PaintEvent pe) {
    _context.lock();
    try {
      glPaint();
      _context.swapBuffers();
    } finally {
      _context.unlock();
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private GlContext _context;
}

