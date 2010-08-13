/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import java.nio.*;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * Simple OpenGL program to display a lit sphere using glDrawArrays.
 * @author Jeff Emanuel, Transform Software
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.08
 */
public class SphereTest {

  private static GlCanvas canvas = new GlCanvas() {

    /**
     * Contains vertex data.
     */
    private FloatBuffer _buffer;

    /**
     * Computes the vertices of the triangles for rendering a sphere and 
     * stores them in _buffer.  The vertices are repeated as necessary so
     * that the buffer can be used by rendering with glDrawArrays.
     */
    private void computeVertices() {
      final int nphi = 50;
      final int ntheta = 100;

      final float dphi = (float)Math.PI/nphi;
      final float dtheta = 2.0f*(float)Math.PI/ntheta;

      float[] vertices = new float[2*3*3*ntheta+(nphi-2)*3*6*ntheta];

      // Precompute trigonometry.
      float[] phiSin = new float[nphi];
      float[] phiCos = new float[nphi];
      for (int i=0; i<nphi; ++i) {
        float phi = i*dphi;
        phiSin[i] = (float)Math.sin(phi);
        phiCos[i] = (float)Math.cos(phi);
      }
      float[] thetaSin = new float[ntheta];
      float[] thetaCos = new float[ntheta];
      for (int i=0; i<ntheta; ++i) {
        float theta = i*dtheta;
        thetaSin[i] = (float)Math.sin(theta);
        thetaCos[i] = (float)Math.cos(theta);
      }

      // Fan at north pole.
      int index = 0;
      for (int i=0; i<ntheta; ++i) {
        // Pole.
        vertices[index++] = 0.0f;  // x
        vertices[index++] = 0.0f;  // y
        vertices[index++] = 1.0f;  // z

        vertices[index++] = thetaCos[i]*phiSin[1];  // x
        vertices[index++] = thetaSin[i]*phiSin[1];  // y
        vertices[index++] = phiCos[1];              // z

        int j=(i+1)%ntheta;
        vertices[index++] = thetaCos[j]*phiSin[1];
        vertices[index++] = thetaSin[j]*phiSin[1];
        vertices[index++] = phiCos[1];
      }

      // Latitude strips.
      for (int ip=1; ip<nphi-1; ++ip) {
        for (int it=0; it<ntheta; ++it) {
          int jt = (it+1)%ntheta;
          vertices[index++] = thetaCos[it]*phiSin[ip];
          vertices[index++] = thetaSin[it]*phiSin[ip];
          vertices[index++] = phiCos[ip];

          vertices[index++] = thetaCos[it]*phiSin[ip+1];
          vertices[index++] = thetaSin[it]*phiSin[ip+1];
          vertices[index++] = phiCos[ip+1];

          vertices[index++] = thetaCos[jt]*phiSin[ip];
          vertices[index++] = thetaSin[jt]*phiSin[ip];
          vertices[index++] = phiCos[ip];

          vertices[index] = vertices[index-3];
          ++index;
          vertices[index] = vertices[index-3];
          ++index;
          vertices[index] = vertices[index-3];
          ++index;

          vertices[index] = vertices[index-9];
          ++index;
          vertices[index] = vertices[index-9];
          ++index;
          vertices[index] = vertices[index-9];
          ++index;

          vertices[index++] = thetaCos[jt]*phiSin[ip+1];
          vertices[index++] = thetaSin[jt]*phiSin[ip+1];
          vertices[index++] = phiCos[ip+1];
        }
      }

      // Fan at south pole.
      for (int i=0; i<ntheta; ++i) {
        vertices[index++] = thetaCos[i]*phiSin[nphi-1];
        vertices[index++] = thetaSin[i]*phiSin[nphi-1];
        vertices[index++] = phiCos[nphi-1];

        // Pole.
        vertices[index++] = 0.0f;
        vertices[index++] = 0.0f;
        vertices[index++] = 1.0f;

        int j=(i+1)%ntheta;
        vertices[index++] = thetaCos[j]*phiSin[nphi-1];
        vertices[index++] = thetaSin[j]*phiSin[nphi-1];
        vertices[index++] = phiCos[nphi-1];
      }

      _buffer = ByteBuffer.allocateDirect(vertices.length*4)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
      _buffer.put(vertices);
      _buffer.rewind();
    }

    public void glInit() {
      computeVertices();

      float[] mat_specular = {1.0f,1.0f,1.0f,1.0f};
      float[] mat_shininess = {50.0f};
      float[] light_position = {1.0f,-3.0f,1.0f,0.0f};
      float[] white_light = {1.0f,1.0f,1.0f,1.0f};
      float[] lmodel_ambient = {0.1f,0.1f,0.1f,1.0f};

      glClearColor(0.0f,0.0f,0.0f,0.0f);
      glShadeModel(GL_SMOOTH);
      glMaterialfv(GL_FRONT,GL_SPECULAR,mat_specular,0);
      glMaterialfv(GL_FRONT,GL_SHININESS,mat_shininess,0);
      glLightfv(GL_LIGHT0,GL_POSITION,light_position,0);
      glLightfv(GL_LIGHT0,GL_DIFFUSE,white_light,0);
      glLightfv(GL_LIGHT0,GL_SPECULAR,white_light,0);
      glLightModelfv(GL_LIGHT_MODEL_AMBIENT,lmodel_ambient,0);
      glLightModeli(GL_LIGHT_MODEL_TWO_SIDE,GL_FALSE);
    }

    public void glResize(int x, int y, int width, int height) {
      glViewport(0,0,width,height);
      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glOrtho(-1.2,1.2,-1.2,1.2,-2.0,2.0);
      glRotatef(-90.0f,1.0f,0.0f,0.0f);
    }

    public void glPaint() {
      glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
      glEnable(GL_LIGHTING);
      glEnable(GL_LIGHT0);
      glEnable(GL_DEPTH_TEST);
      glEnableClientState(GL_NORMAL_ARRAY);
      glEnableClientState(GL_VERTEX_ARRAY);
      glVertexPointer(3,GL_FLOAT,0,_buffer);
      glNormalPointer(GL_FLOAT,0,_buffer);
      glColor3f(1.0f,1.0f,1.0f);
      glDrawArrays(GL_TRIANGLES,0,_buffer.capacity()/3);
      glFlush();
    }
  };

  public static void main(String[] args) {
    TestSimple.run(args,canvas);
  }
}
