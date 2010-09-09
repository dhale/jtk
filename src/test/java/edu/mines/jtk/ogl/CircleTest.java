/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import static java.lang.Math.*;
import java.util.Random;

import static edu.mines.jtk.ogl.Gl.*;
import edu.mines.jtk.util.Stopwatch;

/**
 * Draw circles with OpenGL. Adapted from Paul Heckbert's program circ3.c.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.08
 */
public class CircleTest {
  private static GlCanvas canvas = new GlCanvas() {
    public void glInit() {
      glClearColor(0.0f,0.0f,0.0f,0.0f);
      glEnable(GL_DEPTH_TEST);
      listCircleInit(_nsides);
      _stopwatch.start();
    }
    public void glResize(int x, int y, int width, int height) {
      glViewport(0,0,width,height);
      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glFrustum(-0.03,0.03,-0.03,0.03,0.1,20.0);
      glMatrixMode(GL_MODELVIEW);
      glLoadIdentity();
      glTranslated(0.0,0.0,-4.0);
      glRotated(-80.0,1.0,0.0,0.0);
    }
    public void glPaint() {
      glClear(GL_DEPTH_BUFFER_BIT|GL_COLOR_BUFFER_BIT);
      _wild = !_wild;
      if (!_wild)
        glColor3f(1.0f,1.0f,1.0f);
      double[] n = new double[3];
      for (int lat=0; lat<_nvert; ++lat) {
        double theta = PI*lat/_nvert;
        n[Z] = cos(theta);
        double nxy = sin(theta);
        for (int lon=0; lon<2*_nvert; ++lon) {
          double phi = 2.0*PI*lon/(2*_nvert);
          n[X] = nxy*sin(phi);
          n[Y] = nxy*cos(phi);
          if (_wild) 
            glColor3f(_random.nextFloat(),
                      _random.nextFloat(),
                      _random.nextFloat());
          switch(_imode) {
          case SIMPLE_CIRCLE:
            simpleCircle(n,n,_r,_nsides); 
            break;
          case LIST_CIRCLE:
            listCircle(n,n,_r);
            break;
          }
        }
      }
      glRotated(1.0,0.0,0.0,1.0);
      glFlush();
      ++_npaint;
      if (_stopwatch.time()>2.0) {
        _stopwatch.stop();
        int rate = (int)(_npaint/_stopwatch.time());
        System.out.println(_modes[_imode]+": frames/sec = "+rate);
        _npaint = 0;
        _stopwatch.restart();
        ++_imode;
        if (_imode==_modes.length)
          _imode = 0;
      }
    }
    private void listCircleInit(int nsides) {
      glNewList(UNIT_CIRCLE,GL_COMPILE);
      glBegin(GL_POLYGON);
      for (int i=0; i<nsides; ++i) {
        double ang = 2.0*PI*i/nsides;
        double cosang = cos(ang);
        double sinang = sin(ang);
        glVertex2d(cosang,sinang);
      }
      glEnd();
      glEndList();
    }
    private void listCircle(double[] c, double[] n, double r) {
      double tx = -asin(n[Y]);
      double ty = atan2(n[X],n[Z]);
      glPushMatrix();
      glTranslated(c[X],c[Y],c[Z]);
      glRotated(radToDeg(ty),0.0,1.0,0.0);
      glRotated(radToDeg(tx),1.0,0.0,0.0);
      glScaled(r,r,r);
      glCallList(UNIT_CIRCLE);
      glPopMatrix();
    }
    private void simpleCircle(double[] c, double[] n, double r, int nsides) {
      double[] u = (n[X]*n[X]<0.33) ?
                   create(0,n[Z],-n[Y]) :
                   create(-n[Z],0,n[X]);
      u = scale(u,r/length(u));
      double[] v = cross(n,u);
      v = scale(v,r/length(v));
      glBegin(GL_POLYGON);
      for (int i=0; i<nsides; ++i) {
        double ang = 2.0*PI*i/nsides;
        double cosang = cos(ang);
        double sinang = sin(ang);
        double x = c[X]+cosang*u[X]+sinang*v[X];
        double y = c[Y]+cosang*u[Y]+sinang*v[Y];
        double z = c[Z]+cosang*u[Z]+sinang*v[Z];
        glVertex3d(x,y,z);
      }
      glEnd();
    }
    private double[] create(double x, double y, double z) {
      return new double[]{x,y,z};
    }
    private double length(double[] a) {
      return sqrt(a[X]*a[X]+a[Y]*a[Y]+a[Z]*a[Z]);
    }
    private double[] scale(double[] a, double s) {
      return new double[] {s*a[X],s*a[Y],s*a[Z]};
    }
    private double[] cross(double[] a, double[] b) {
      return new double[] {
        a[Y]*b[Z]-b[Y]*a[Z],
        a[Z]*b[X]-b[Z]*a[X],
        a[X]*b[Y]-b[X]*a[Y],
      };
    }
    private double radToDeg(double rad) {
      return 180.0*rad/PI;
    }
    private int _nsides = 10; // number of sides per circle
    private int _nvert = 14; // number of circles vertically
    private double _r = 0.5*PI/_nvert*RFRAC;
    private boolean _wild;
    private Random _random = new Random();
    private Stopwatch _stopwatch = new Stopwatch();
    private int _npaint;
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;
    private static final double RFRAC = 0.4;
    private static final int UNIT_CIRCLE = 1; // display list
    private static final int SIMPLE_CIRCLE = 0;
    private static final int LIST_CIRCLE = 1;
    private int _imode;
    private String[] _modes = {
      "simple",
      "display list",
    };
  };
  public static void main(String[] args) {
    TestSimple.run(args,canvas,true);
  }
}
