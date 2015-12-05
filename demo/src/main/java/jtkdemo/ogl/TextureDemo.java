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
package jtkdemo.ogl;

import java.nio.ByteBuffer;

import static edu.mines.jtk.ogl.Gl.*;
import edu.mines.jtk.ogl.*;

/**
 * Simple OpenGL texture mapping program.
 * @author Jeff Emanuel, Transform Software
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.08
 */
public class TextureDemo {
  private static GlCanvas canvas = new GlCanvas() {
    /**
     * The texture data.
     */
    private final int N = 64;
    private final ByteBuffer _checkImage = ByteBuffer.allocateDirect(N*N*4);
    /**
     * The OpenGL name of the texture data.
     */
    private int[] _texName = new int[1];
    private void makeCheckImage() {
      for (int i=0; i<N; ++i) {
        for (int j=0; j<N; ++j) {
          int index = (N*i+j)*4;
          int c = ((((i&0x8)==0)^((j&0x8))==0)?1:0)*255;
          byte b = (byte)c;
          _checkImage.put(index  ,b);
          _checkImage.put(index+1,b);
          _checkImage.put(index+2,b);
          _checkImage.put(index+3,(byte)255);
        }
      }
    }
    public void glInit() {
      makeCheckImage();
      glClearColor(0.0f,0.0f,0.0f,0.0f);
      glPixelStorei(GL_UNPACK_ALIGNMENT,1);
      glGenTextures(1,_texName,0);
      glBindTexture(GL_TEXTURE_2D,_texName[0]);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
      glTexImage2D(
        GL_TEXTURE_2D,0,GL_RGBA,N,N,0,GL_RGBA,GL_UNSIGNED_BYTE,_checkImage);
    }
    public void glResize(int x, int y, int width, int height) {
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
      glDisable(GL_TEXTURE_2D);
      glFlush();
    }
  };
  public static void main(String[] args) {
    DemoSimple.run(args,canvas);
  }
}
