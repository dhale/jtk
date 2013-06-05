/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import java.awt.*;
import java.io.File;
import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import com.jogamp.opengl.util.awt.Screenshot;

/**
 * A canvas that paints via OpenGL. To paint a canvas using OpenGL, 
 * extend this class and implement the method {@link #glPaint()}.
 * Classes that extend this class may also implement the methods
 * {@link #glInit()} and {@link #glResize(int,int,int,int)}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.08
 */
public class GlCanvas extends GLCanvas implements GLEventListener {
//public class GlCanvas extends GLJPanel implements GLEventListener {
// If GLCanvas does not work (e.g., in JOGL2 RC9 on Mac OS X 10.8),
// we may have more luck extending GLJPanel instead of GLCanvas.
// GLJPanel now does not work correctly with ImagePanelGroup.

  private static final long serialVersionUID = 1L;
    
  /**
   * Constructs a canvas with default capabilities.
   */
  public GlCanvas() {
    this.addGLEventListener(this);
  }

  /**
   * Constructs a canvas with specified capabilities.
   */
  public GlCanvas(GLCapabilities capabilities) {
    super(capabilities);
    this.addGLEventListener(this);
  }

  /*
   * Constructs a canvas with specified capabilities.
   */
  /* Not supported if this class extends GLJPanel,
   * which we may need to do of GLCanvas does not work.
  public GlCanvas(
    GLCapabilities capabilities,
    GLCapabilitiesChooser chooser,
    GLContext shareWith,
    GraphicsDevice device)
  {
    super(capabilities,chooser,shareWith,device);
    this.addGLEventListener(this);
  }
  */

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
   * Runs the specified runnable with a current OpenGL context.
   * The specified runnable may safely call OpenGL functions within
   * its method run.
   * @param runnable the object to run.
   */
  public void runWithContext(Runnable runnable) {
    _runnable = runnable;
    try {
      display();
    } finally {
      _runnable = null;
    }
  }

  /**
   * Paints this canvas to an image in a file with specified name.
   * Uses the file suffix to determine the format of the image.
   * @param fileName name of the file to contain the image.
   */
  public void paintToFile(String fileName) {
    _fileName = fileName;
    Graphics g = getGraphics();
    paint(g);
    g.dispose();
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
   * @param x the x pixel coordinate of top-left corner.
   * @param y the y pixel coordinate of top-left corner.
   * @param width the width in pixels.
   * @param height the height in pixels.
   */
  public void glResize(int x, int y, int width, int height) {
  }

  /**
   * Paints this canvas via OpenGL.
   * <p>
   * In classes that extend this class, implementations of this method 
   * use the OpenGL context that has been locked for the current thread. 
   * This implementation does nothing.
   */
  public void glPaint() {
  }

  public void init(GLAutoDrawable drawable) {
    glInit();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    glResize(x,y,w,h);
  }

  public void display(GLAutoDrawable drawable) {
    if (_runnable!=null) {
      _runnable.run();
    } else {
      glPaint();
      if (_fileName!=null) {
        try {
          File file = new File(_fileName);
          int w = getWidth();
          int h = getHeight();
          Screenshot.writeToFile(file,w,h);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        _fileName = null;
      }
      if (_autoRepaint)
        repaint();
    }
  }

  public void dispose(GLAutoDrawable drawable) {
  }

  public void displayChanged(
    GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
  {
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private boolean _autoRepaint;
  private Runnable _runnable;
  private String _fileName;
}
