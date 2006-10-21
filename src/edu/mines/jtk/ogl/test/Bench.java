/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl.test;

import static edu.mines.jtk.ogl.Gl.*;

import javax.media.opengl.*;
import javax.swing.JFrame;

import edu.mines.jtk.util.Stopwatch;

/**
 * Benchmarking to determine overhead of getting GL object for every call.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.07
 */
public class Bench extends JFrame implements GLEventListener {
  private static final long serialVersionUID = 1L;

  public Bench() {
    super("Bench");
    GLCapabilities caps = new GLCapabilities();
    caps.setDoubleBuffered(true);
    System.out.println(caps);
    GLCanvas canvas = new GLCanvas(caps);
    canvas.addGLEventListener(this);
    this.add(canvas);
  }

  public void init(GLAutoDrawable drawable) {
    glClearColor(0.0f,0.0f,0.0f,0.0f);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0.0,1.0,0.0,1.0,-1.0,1.0);
    System.out.println("OpenGL version="+glGetString(GL_VERSION));
    System.out.println("OpenGL vendor="+glGetString(GL_VENDOR));
  }

  public void display(GLAutoDrawable drawable) {
    Stopwatch s = new Stopwatch();
    int nloop;

    for (int ntrial=0; ntrial<3; ++ntrial) {
      s.restart();
      glClear(GL_COLOR_BUFFER_BIT);
      for (nloop=0; s.time()<1.0; ++nloop) {
        for (int ipoly=0; ipoly<100; ++ipoly) {
          glColor3f(1.0f,1.0f,1.0f);
          glBegin(GL_POLYGON);
            glVertex3f(0.25f,0.25f,0.00f);
            glVertex3f(0.75f,0.25f,0.00f);
            glVertex3f(0.75f,0.75f,0.00f);
            glVertex3f(0.25f,0.75f,0.00f);
          glEnd();
        }
      }
      //glFinish();
      s.stop();
      System.out.println("new rate="+nloop+" loops per second");

      s.restart();
      GL gl = GLContext.getCurrent().getGL();
      gl.glClear(GL.GL_COLOR_BUFFER_BIT);
      for (nloop=0; s.time()<1.0; ++nloop) {
        for (int ipoly=0; ipoly<100; ++ipoly) {
          gl.glColor3f(1.0f,1.0f,1.0f);
          gl.glBegin(GL.GL_POLYGON);
            gl.glVertex3f(0.25f,0.25f,0.00f);
            gl.glVertex3f(0.75f,0.25f,0.00f);
            gl.glVertex3f(0.75f,0.75f,0.00f);
            gl.glVertex3f(0.25f,0.75f,0.00f);
          gl.glEnd();
        }
      }
      //gl.glFinish();
      s.stop();
      System.out.println("old rate="+nloop+" loops per second");
    }
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
  }

  public void displayChanged(
    GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
  {
  }

  public static void main(String[] args) {
    Bench frame = new Bench();
    frame.setSize(512,512);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
