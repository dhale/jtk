/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.opengl.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.events.*;

import edu.mines.jves.opengl.*;

/**
 * Simple OpenGL program.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public class HelloSwt {

  private static class MyCanvas extends GlSwtCanvas {
    public MyCanvas(Composite parent, int style) {
      super(parent, style);
    }
    public void glPaint() {
      Point size = getSize();
      if (_width!=size.x || _height!=size.y) {
        _width = size.x;
        _height = size.y;
        Gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        Gl.glViewport(0,0,_width,_height);
        Gl.glMatrixMode(Gl.GL_PROJECTION);
        Gl.glLoadIdentity();
        Gl.glOrtho(0.0,1.0,0.0,1.0,-1.0,1.0);
      }
      Gl.glClear(Gl.GL_COLOR_BUFFER_BIT);
      Gl.glBlendColor(1.0f,1.0f,1.0f,1.0f); // OpenGL 1.2 test
      Gl.glColor3f(1.0f,1.0f,1.0f);
      Gl.glBegin(Gl.GL_POLYGON);
        Gl.glVertex3f(0.25f,0.25f,0.00f);
        Gl.glVertex3f(0.75f,0.25f,0.00f);
        Gl.glVertex3f(0.75f,0.75f,0.00f);
        Gl.glVertex3f(0.25f,0.75f,0.00f);
      Gl.glEnd();
      Gl.glFlush();
    }
    private int _width = -1;
    private int _height = -1;
  }
  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    shell.setSize(800,800);
    MyCanvas canvas = new MyCanvas(shell,SWT.NONE);
    canvas.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
    canvas.setForeground(display.getSystemColor(SWT.COLOR_RED));
    shell.layout();
    shell.open();
    while (!shell.isDisposed())
    {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
    System.exit(0);
  }
}

