/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl.test;

import edu.mines.jtk.opengl.*;
import static edu.mines.jtk.opengl.Gl.*;

/**
 * Simple OpenGL program.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.02
 */
public class Hello {
  private static GlPainter painter = new GlPainter() {
    public void glInit() {
      glClearColor(0.0f,0.0f,0.0f,0.0f);
    }
    public void glResize(int width, int height, int widthOld, int heightOld) {
      glViewport(0,0,width,height);
      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glOrtho(0.0,1.0,0.0,1.0,-1.0,1.0);
    }
    public void glPaint() {
      glClear(GL_COLOR_BUFFER_BIT);
      glBlendColor(1.0f,1.0f,1.0f,1.0f); // something from OpenGL 1.2
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
    TestSimple.run(args,painter);
  }
}
