/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.opengl.test;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jves.opengl.*;

/**
 * Simple OpenGL program.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public class Hello {

  public static void main(String[] args) {
    GlAwtCanvas canvas = new GlAwtCanvas(new GlPainter() {
      public void glPaint() {
        if (!_inited) {
          Gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
          Gl.glMatrixMode(Gl.GL_PROJECTION);
          Gl.glLoadIdentity();
          Gl.glOrtho(0.0,1.0,0.0,1.0,-1.0,1.0);
          _inited = true;
        }
        Gl.glClear(Gl.GL_COLOR_BUFFER_BIT);
        Gl.glColor3f(1.0f,1.0f,1.0f);
        Gl.glBegin(Gl.GL_POLYGON);
          Gl.glVertex3f(0.25f,0.25f,0.00f);
          Gl.glVertex3f(0.75f,0.25f,0.00f);
          Gl.glVertex3f(0.75f,0.75f,0.00f);
          Gl.glVertex3f(0.25f,0.75f,0.00f);
        Gl.glEnd();
        Gl.glFlush();
      }
      private boolean _inited;
    });
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(800,800));
    frame.getContentPane().add(canvas,BorderLayout.CENTER);
    frame.setVisible(true);
  }
}
