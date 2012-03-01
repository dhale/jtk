/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.Color;
import javax.swing.SwingUtilities;

/**
 * Tests {@link edu.mines.jtk.sgl.SimpleFrame}.
 * @author Chris Engelsma, Colorado School of Mines.
 * @version 2009.07.20
 */
public class SimpleFrameTest {

  public static void main(final String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        go(args);
      }
    });
  }

  public static void go(String[] args) {
    float[] xyz = makeSineWave();
    xyz = addBulge(xyz);
    xyz = addTear(xyz);
    SimpleFrame sf = new SimpleFrame();
    TriangleGroup tg = sf.addTriangles(xyz);
    tg.setColor(Color.BLUE);
    if (args.length>0)
      sf.paintToFile(args[0]);
  }

  private static float[] makeSineWave() {
    int nx = 100;
    int ny = 100;
    float dx = 10.0f/(float)nx;
    float dy = 10.0f/(float)ny;
    float[] xyz = new float[3*6*nx*ny];
    for (int ix=0,i=0; ix<nx; ++ix) {
      float x0 = ix*dx;
      float x1 = (ix+1)*dx;
      for (int iy=0; iy<ny; ++iy) {
        float y0 = iy*dy;
        float y1 = (iy+1)*dy;
        xyz[i++] = x0;  xyz[i++] = y0;  xyz[i++] = sin(x0,y0);
        xyz[i++] = x0;  xyz[i++] = y1;  xyz[i++] = sin(x0,y1);
        xyz[i++] = x1;  xyz[i++] = y0;  xyz[i++] = sin(x1,y0);
        xyz[i++] = x1;  xyz[i++] = y0;  xyz[i++] = sin(x1,y0);
        xyz[i++] = x0;  xyz[i++] = y1;  xyz[i++] = sin(x0,y1);
        xyz[i++] = x1;  xyz[i++] = y1;  xyz[i++] = sin(x1,y1);
      }
    }
    return xyz;
  }
  private static float sin(float x, float y) {
    return (float)(5.0+0.25*Math.sin(x+y));
  }

  private static float[] addBulge(float[] xyz) {
    int n = xyz.length;
    float[] t = new float[n];
    for (int i=0; i<n; i+=3) {
      float x = xyz[i  ];
      float y = xyz[i+1];
      float z = xyz[i+2];
      z -= exp(x,y);
      t[i  ] = x;
      t[i+1] = y;
      t[i+2] = z;
    }
    return t;
  }
  private static float exp(float x, float y) {
    x -= 5.0f;
    y -= 5.0f;
    x *= 0.4f;
    y *= 0.8f;
    return (float)(2.0*Math.exp(-x*x-y*y));
  }

  private static float[] addTear(float[] xyz) {
    int n = xyz.length;
    float[] t = new float[n];
    int nt = n/9;
    for (int it=0,i=0,j=0; it<nt; ++it) {
      float xa = xyz[i++];  
      float ya = xyz[i++];  
      float za = xyz[i++];  
      float xb = xyz[i++];  
      float yb = xyz[i++];  
      float zb = xyz[i++];  
      float xc = xyz[i++];  
      float yc = xyz[i++];  
      float zc = xyz[i++];  
      float x = 0.333333f*(xa+xb+xc);
      if (x>5.0f) {
        za += exp(xa,ya);
        zb += exp(xb,yb);
        zc += exp(xc,yc);
      }
      t[j++] = xa;  t[j++] = ya;  t[j++] = za;
      t[j++] = xb;  t[j++] = yb;  t[j++] = zb;
      t[j++] = xc;  t[j++] = yc;  t[j++] = zc;
    }
    return t;
  }
}
