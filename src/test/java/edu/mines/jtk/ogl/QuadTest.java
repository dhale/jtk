/****************************************************************************
Copyright (c) 2012, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import java.nio.FloatBuffer;
import java.util.Random;

import edu.mines.jtk.util.Direct;
import edu.mines.jtk.util.Stopwatch;
import static edu.mines.jtk.ogl.Gl.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Draw lots of quads with OpenGL.
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.08.07
 */
public class QuadTest {
  private static GlCanvas canvas = new GlCanvas() {
    public void glInit() {
      makeQuads(65536);
      glClearColor(0.0f,0.0f,0.0f,0.0f);
      glEnable(GL_DEPTH_TEST);
      _stopwatch.start();
    }
    public void glResize(int x, int y, int width, int height) {
      glViewport(0,0,width,height);
      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glFrustum(-0.03,0.03,-0.03,0.03,0.1,100.0);
      setModelView(_zoom);
    }
    private void setModelView(double zoom) {
      _zoom = zoom;
      glMatrixMode(GL_MODELVIEW);
      glLoadIdentity();
      glTranslated(0.0,0.0,-4.0/_zoom);
      glRotated(-80.0,1.0,0.0,0.0);
    }
    public void glPaint() {
      glClear(GL_DEPTH_BUFFER_BIT|GL_COLOR_BUFFER_BIT);
      glEnableClientState(GL_VERTEX_ARRAY);
      glEnableClientState(GL_NORMAL_ARRAY);
      glEnableClientState(GL_COLOR_ARRAY);
      glVertexPointer(3,GL_FLOAT,0,_vb);
      glNormalPointer(GL_FLOAT,0,_nb);
      glColorPointer(3,GL_FLOAT,0,_cb);
      glDrawArrays(GL_QUADS,0,4*_nq);
      glDisableClientState(GL_NORMAL_ARRAY);
      glDisableClientState(GL_COLOR_ARRAY);
      glDisableClientState(GL_VERTEX_ARRAY);
      glRotated(1.0,0.0,0.0,1.0);
      glFlush();
      ++_npaint;
      if (_stopwatch.time()>2.0) {
        _stopwatch.stop();
        float fps = (float)(_npaint/_stopwatch.time());
        int qps = (int)(_nq*fps);
        System.out.println("frames/second = "+fps+", quads/second = "+qps);
        _npaint = 0;
        if (_zoom<16.0) {
          setModelView(2.0*_zoom);
        } else {
          setModelView(1.0/16.0);
        }
        if (fps<29.0f) {
          makeQuads(_nq/2);
        } else if (fps>59.0f) {
          makeQuads(_nq*2);
        }
        _stopwatch.restart();
      }
    }

    /////////////////////////////////////////////////////////////////////////
    // private

    private int _nq;
    private FloatBuffer _vb,_nb,_cb;
    private Stopwatch _stopwatch = new Stopwatch();
    private int _npaint;
    private double _zoom = 1;

    private void makeQuads(int nq) {
      System.out.println("quads/frame = "+nq);
      _nq = nq;
      int nv = 4*nq;
      int nn = 4*nq;
      int nc = 4*nq;
      Random random = new Random();
      _vb = Direct.newFloatBuffer(3*nv);
      _nb = Direct.newFloatBuffer(3*nn);
      _cb = Direct.newFloatBuffer(3*nc);
      float e = 0.01f;
      for (int iq=0,iv=0,in=0,ic=0; iq<nq; ++iq) {
        float radius = 1.0f+0.1f*random.nextFloat();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        float p = FLT_PI*random.nextFloat()*2.0f;
        float t = FLT_PI*random.nextFloat();
        float u = sin(p)*cos(t);
        float v = sin(p)*sin(t);
        float w = cos(p);
        _vb.put(iv++,radius*cos(p-e)*cos(t-e));
        _vb.put(iv++,radius*cos(p-e)*sin(t-e));
        _vb.put(iv++,radius*sin(p-e));
        _vb.put(iv++,radius*cos(p+e)*cos(t-e));
        _vb.put(iv++,radius*cos(p+e)*sin(t-e));
        _vb.put(iv++,radius*sin(p+e));
        _vb.put(iv++,radius*cos(p+e)*cos(t+e));
        _vb.put(iv++,radius*cos(p+e)*sin(t+e));
        _vb.put(iv++,radius*sin(p+e));
        _vb.put(iv++,radius*cos(p-e)*cos(t+e));
        _vb.put(iv++,radius*cos(p-e)*sin(t+e));
        _vb.put(iv++,radius*sin(p-e));
        _nb.put(in++,u);
        _nb.put(in++,v);
        _nb.put(in++,w);
        _nb.put(in++,u);
        _nb.put(in++,v);
        _nb.put(in++,w);
        _nb.put(in++,u);
        _nb.put(in++,v);
        _nb.put(in++,w);
        _nb.put(in++,u);
        _nb.put(in++,v);
        _nb.put(in++,w);
        _cb.put(ic++,r);
        _cb.put(ic++,g);
        _cb.put(ic++,b);
        _cb.put(ic++,r);
        _cb.put(ic++,g);
        _cb.put(ic++,b);
        _cb.put(ic++,r);
        _cb.put(ic++,g);
        _cb.put(ic++,b);
        _cb.put(ic++,r);
        _cb.put(ic++,g);
        _cb.put(ic++,b);
      }
    }
  };
  public static void main(String[] args) {
    TestSimple.run(args,canvas,true);
  }
}
