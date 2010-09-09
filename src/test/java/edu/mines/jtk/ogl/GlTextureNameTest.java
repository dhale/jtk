/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import java.nio.ByteBuffer;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * Tests GlTextureName. This test repeatedly constructs texture names but
 * does not explicitly dispose them. Instead, texture names are disposed
 * when they are finalized during garbage collection.
 * <p>
 * When possible, we should dispose texture names explicitly, and not
 * depend on finalization. However, depending entirely on explicit calls 
 * to dispose is unreliable, particularly for long-lived objects like
 * texture names. For example, the object that normally calls dispose may
 * itself have been garbage collected. 
 * <p>
 * This test creates a worst case scenario, in which we never dispose 
 * texture names explicitly, and rely entirely on finalization. While
 * running this test, monitor the use of memory by the JVM.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.08
 */
public class GlTextureNameTest {
  private static GlCanvas canvas = new GlCanvas() {

    private static final int SIZE = 256;
    private ByteBuffer _checkImage = null;
    private GlTextureName _textureName;

    private void makeCheckImage() {
      int n = SIZE;
      _checkImage = ByteBuffer.allocateDirect(n*n*4);
      for (int i=0,k=0; i<n; ++i) {
        for (int j=0; j<n; ++j) {
          int c = ((((i&16)==0)^((j&16))==0)?1:0)*255;
          byte b = (byte)c;
          _checkImage.put(k++,b);
          _checkImage.put(k++,b);
          _checkImage.put(k++,b);
          _checkImage.put(k++,(byte)255);
        }
      }
    }
    private void makeTexture() {
      _textureName = new GlTextureName();
      glBindTexture(GL_TEXTURE_2D,_textureName.name());
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
      glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,SIZE,SIZE,0,GL_RGBA,
        GL_UNSIGNED_BYTE,_checkImage);
    }
    public void glInit() {
      glClearColor(0.0f,0.0f,0.0f,0.0f);
      glPixelStorei(GL_UNPACK_ALIGNMENT,1);
      makeCheckImage();
      makeTexture();
    }
    public void glResize(int x, int y, int width, int height) {
      glViewport(0,0,width,height);
      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glOrtho(0.0,1.0,0.0,1.0,-1.0,1.0);
      glMatrixMode(GL_MODELVIEW);
    }
    public void glPaint() {
      glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
      glEnable(GL_TEXTURE_2D);
      glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_REPLACE);
      glBindTexture(GL_TEXTURE_2D,_textureName.name());
      glBegin(GL_POLYGON);
        glTexCoord2f(0.0f,0.0f); glVertex3f(0.25f,0.25f,0.00f);
        glTexCoord2f(1.0f,0.0f); glVertex3f(0.75f,0.25f,0.00f);
        glTexCoord2f(1.0f,1.0f); glVertex3f(0.75f,0.75f,0.00f);
        glTexCoord2f(0.0f,1.0f); glVertex3f(0.25f,0.75f,0.00f);
      glEnd();
      glFlush();
      glDisable(GL_TEXTURE_2D);
      makeTexture();
    }
  };
  public static void main(String[] args) {
    TestSimple.run(args,canvas,true);
  }
}
