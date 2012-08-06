/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * Prints Opengl version and vendor and draws a white square.
 * Useful for determining which OpenGL driver you are using.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.02
 */
public class HelloTest {
  private static GlCanvas canvas = new GlCanvas() {
    public void glInit() {
      glClearColor(0.0f,0.0f,0.0f,0.0f);
      System.out.println("OpenGL version="+glGetString(GL_VERSION));
      System.out.println("OpenGL vendor="+glGetString(GL_VENDOR));
      int[] param = new int[1];
      glGetIntegerv(GL_MAX_ELEMENTS_VERTICES,param,0);
      System.out.println("GL_MAX_ELEMENTS_VERTICES="+param[0]);
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
    if (args.length==0) {
      TestSimple.run(canvas,false);
    } else {
      TestSimple.run(canvas,false,args[0]);
    }
  }
}
