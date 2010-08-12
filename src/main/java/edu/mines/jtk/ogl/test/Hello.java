/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl.test;

import static edu.mines.jtk.ogl.Gl.*;
import edu.mines.jtk.ogl.GlCanvas;

/**
 * Prints Opengl version and vendor and draws a white square.
 * Useful for determining which OpenGL driver you are using.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.02
 */
public class Hello {
  private static GlCanvas canvas = new GlCanvas() {
    public void glInit() {
      glClearColor(0.0f,0.0f,0.0f,0.0f);
      System.out.println("OpenGL version="+glGetString(GL_VERSION));
      System.out.println("OpenGL vendor="+glGetString(GL_VENDOR));
    }
    public void glResize(int x, int y, int width, int height) {
      glViewport(0,0,width,height);
      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glOrtho(0.0,1.0,0.0,1.0,-1.0,1.0);
    }
    public void glPaint() {
      glClear(GL_COLOR_BUFFER_BIT);
      glColor3f(1.0f,1.0f,1.0f);
      glBegin(GL_POLYGON);
        glVertex3f(0.25f,0.25f,0.00f);
        glVertex3f(0.75f,0.25f,0.00f);
        glVertex3f(0.75f,0.75f,0.00f);
        glVertex3f(0.25f,0.75f,0.00f);
      glEnd();
      glFlush();
    }
  };
  public static void main(String[] args) {
    TestSimple.run(args,canvas);
  }
}
