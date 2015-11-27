/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
      System.out.println("JOGL version: "+ 
          Package.getPackage("com.jogamp.opengl").
          getImplementationVersion());
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
