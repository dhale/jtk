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

  public static final int GL_COLOR_BUFFER_BIT                    =0x00004000;
  public static final int GL_POLYGON                             =0x0009;
  public static final int GL_PROJECTION                          =0x1701;

  public static native void glBegin(int mode);
  public static native void glClear(int mask);
  public static native void glClearColor(
    float red, float green, float blue, float alpha);
  public static native void glColor3f(float red, float green, float blue);
  public static void glColor(float red, float green, float blue) {
    glColor3f(red,green,blue);
  }
  public static native void glEnd();
  public static native void glFlush();
  public static native void glLoadIdentity();
  public static native void glMatrixMode(int mode);
  public static native void glOrtho(
    double left, double right, 
    double bottom, double top, 
    double zNear, double zFar);
  public static native void glVertex3f(float x, float y, float z);
  public static void glVertex(float x, float y, float z) {
    glVertex3f(x,y,z);
  }
  public static native void glViewport(int x, int y, int width, int height);

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

  static {
    System.loadLibrary("edu_mines_jves_opengl");
  }
}
