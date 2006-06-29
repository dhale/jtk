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
 * Simple OpenGL texture mapping program.
 * @author Jeff Emanuel, Transform Software
 * @version 2004.12.03
 */
public class Texture {
  private static GlPainter painter = new GlPainter() {
    /**
     * The texture data.
     */
    private byte[] _checkImage = null;
    /**
     * The OpenGL name of the texture data.
     */
    private int[] _texName = new int[1];
    private void makeCheckImage() {
      int n = 64;
      _checkImage = new byte[n*n*4];
      for (int i=0; i<n; ++i) {
        for (int j=0; j<n; ++j) {
          int index = (n*i+j)*4;
          int c = ((((i&0x8)==0)^((j&0x8))==0)?1:0)*255;
          byte b = (byte)c;
          _checkImage[index] = b;
          _checkImage[index+1] = b;
          _checkImage[index+2] = b;
          _checkImage[index+3] = (byte)255;
        }
      }
    }
    public void glInit() {
      makeCheckImage();
      glClearColor(0.0f,0.0f,0.0f,0.0f);
      glPixelStorei(GL_UNPACK_ALIGNMENT,1);
      glGenTextures(1,_texName);
      glBindTexture(GL_TEXTURE_2D,_texName[0]);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
      glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,64,64,0,GL_RGBA,
        GL_UNSIGNED_BYTE,_checkImage);
    }
    public void glResize(int width, int height, int widthOld, int heightOld) {
      glViewport(0,0,width,height);
      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glOrtho(0.0,1.0,0.0,1.0,-1.0,1.0);
    }
    public void glPaint() {
      glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
      glEnable(GL_TEXTURE_2D);
      glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_REPLACE);
      glBindTexture(GL_TEXTURE_2D,_texName[0]);
      glBegin(GL_POLYGON);
        glTexCoord2f(0.0f,0.0f); glVertex3f(0.25f,0.25f,0.00f);
        glTexCoord2f(1.0f,0.0f); glVertex3f(0.75f,0.25f,0.00f);
        glTexCoord2f(1.0f,1.0f); glVertex3f(0.75f,0.75f,0.00f);
        glTexCoord2f(0.0f,1.0f); glVertex3f(0.25f,0.75f,0.00f);
      glEnd();
      glFlush();
      glDisable(GL_TEXTURE_2D);
    }
  };
  public static void main(String[] args) {
    TestSimple.run(args,painter);
  }
}
