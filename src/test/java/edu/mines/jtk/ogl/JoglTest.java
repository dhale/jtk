/****************************************************************************
Copyright (c) 2012, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import java.awt.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import javax.swing.*;

/**
 * Prints OpenGL version and vendor and draws a white square.
 * Useful for determining which OpenGL driver you are using.
 * This version uses only JOGL, nothing from the Mines JTK.
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.08.18
 */
public class JoglTest {
  private static class Guts implements GLEventListener {
    private String what;
    Guts(String what) {
      this.what = what;
    }
    public void init(GLAutoDrawable drawable) {
      System.out.println(what+": init");
      GL2 gl = drawable.getGL().getGL2();
      gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
    }
    public void reshape(
      GLAutoDrawable drawable, 
      int x, int y, int width, int height) 
    {
      System.out.println(what+": reshape");
      GL2 gl = drawable.getGL().getGL2();
      gl.glViewport(0,0,width,height);
      gl.glMatrixMode(GL2.GL_PROJECTION);
      gl.glLoadIdentity();
      gl.glOrtho(0.0,1.0,0.0,1.0,-1.0,1.0);
    }
    public void display(GLAutoDrawable drawable) {
      System.out.println(what+": display");
      GL2 gl = drawable.getGL().getGL2();
      gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
      gl.glColor3f(1.0f,1.0f,0.0f);
      gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(0.25f,0.25f,0.00f);
        gl.glVertex3f(0.75f,0.25f,0.00f);
        gl.glVertex3f(0.75f,0.75f,0.00f);
        gl.glVertex3f(0.25f,0.75f,0.00f);
      gl.glEnd();
      gl.glFlush();
    }
    public void dispose(GLAutoDrawable drawable) {
      System.out.println(what+": dispose");
    }
  }
  private static void makeFrame(Component component, String title) {
    JFrame frame = new JFrame(title);
    frame.setSize(500,500);
    frame.setLocationRelativeTo(null);
    frame.getContentPane().add(component,BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  public static void main(String[] args) {
    System.out.println("main");
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        System.out.println("main:run");
        GLCanvas canvas = new GLCanvas();
        GLJPanel jpanel = new GLJPanel();
        canvas.addGLEventListener(new Guts("GLCanvas"));
        jpanel.addGLEventListener(new Guts("GLJPanel"));
        makeFrame(canvas,"JoglTest: GLCanvas");
        makeFrame(jpanel,"JoglTest: GLJPanel");
      }
    });
  }
}
