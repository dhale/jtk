/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.opengl;

/**
 * OpenGL standard constants and methods. Where possible, standard OpenGL
 * function names and argument types are preserved. For convenience, some
 * methods are overloaded; e.g., glVertex2f(float,float) can be written
 * simply as glVertex(float,float).
 * <p>
 * All methods in this class are static. These methods should be called
 * only while an OpenGL context ({@link edu.mines.jves.opengl.GlContext})
 * is locked for the calling thread.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public class Gl {

  public static native void glFlush();

  ///////////////////////////////////////////////////////////////////////////
  // OpenGL 1.2

  public static void glBlendColor(
    float red, float green, float blue, float alpha) 
  {
    nglBlendColor(getContext().glBlendColor,
      red,green,blue,alpha);
  }
  private static native void nglBlendColor(long glBlendColor,
    float red, float green, float blue, float alpha);


  ///////////////////////////////////////////////////////////////////////////
  // package

  static GlContext getContext() {
    return (GlContext)_context.get();
  }

  static void setContext(GlContext context) {
    _context.set(context);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static ThreadLocal _context = new ThreadLocal();
  private Gl() {
  }
}
