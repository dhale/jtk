/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl;

/**
 * An OpenGL painter. Paints via an OpenGL context locked for the current 
 * thread. A painter draws on an OpenGL <em>target</em>, which may be an 
 * AWT canvas, an SWT canvas, an AWT image, and so on. The painter is
 * therefore independent of the type of OpenGL target on which it paints.
 * For example, the same painter may be used to paint either an SWT or AWT
 * canvas.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.02
 */
public interface GlPainter {

  /**
   * Initializes OpenGL state when the target is first painted.
   * This method is called before the methods 
   * {@link #glResize(int,int,int,int)} and {@link #glPaint()} when (1) 
   * the target must be painted and (2) it has never been painted before.
   * <p>
   * Implementations of this method use the OpenGL context that has been 
   * locked for the current thread. 
   */
  public void glInit();

  /**
   * Modifies OpenGL state when this canvas has been resized.
   * This method is called before the method {@link #glPaint()} when
   * (1) the target must be painted and (2) its width or height have 
   * changed since it was last painted or it has never been painted.
   * <p>
   * Implementations of this method use the OpenGL context that has been 
   * locked for the current thread. 
   * @param width the current width.
   * @param height the current height.
   * @param widthBefore the width before resizing.
   * @param heightBefore the height before resizing.
   */
  public void glResize(
    int width, int height, 
    int widthBefore, int heightBefore);

  /**
   * Paints the target via OpenGL.
   * <p>
   * Implementations of this method use the OpenGL context that has been 
   * locked for the current thread.
   */
  public void glPaint();
}
