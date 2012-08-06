/****************************************************************************
Copyright (c) 2011, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * Tests {@link edu.mines.jtk.sgl.QuadGroup}.
 * @author Dave Hale
 * @version 2011.12.05
 */
public class QuadGroupTest {

  public static void main(String[] args) {
    float[] xyz = makeSineWave();
    xyz = addBulge(xyz);
    xyz = addTear(xyz);
    float[] rgb = makeColors(xyz);

    System.out.println("Making QuadGroup ...");
    QuadGroup tg = new QuadGroup(true,xyz,rgb);
    System.out.println("QuadGroup bounding sphere =\n" +
      tg.getBoundingSphere(true));

    StateSet states = new StateSet();
    ColorState cs = new ColorState();
    cs.setColor(Color.CYAN);
    states.add(cs);
    LightModelState lms = new LightModelState();
    lms.setTwoSide(true);
    states.add(lms);
    MaterialState ms = new MaterialState();
    ms.setColorMaterial(GL_AMBIENT_AND_DIFFUSE);
    ms.setSpecular(Color.white);
    ms.setShininess(100.0f);
    states.add(ms);
    tg.setStates(states);

    World world = new World();
    world.addChild(tg);

    TestFrame frame = new TestFrame(world);
    OrbitView view = frame.getOrbitView();
    view.setWorldSphere(new BoundingSphere(5,5,5,5));
    frame.setSize(new Dimension(800,600));
    frame.setVisible(true);
  }

  private static float[] makeSineWave() {
    int nx = 100;
    int ny = 100;
    float dx = 10.0f/(float)nx;
    float dy = 10.0f/(float)ny;
    float[] xyz = new float[3*4*nx*ny];
    for (int ix=0,i=0; ix<nx; ++ix) {
      float x0 = ix*dx;
      float x1 = (ix+1)*dx;
      for (int iy=0; iy<ny; ++iy) {
        float y0 = iy*dy;
        float y1 = (iy+1)*dy;
        xyz[i++] = x0;  xyz[i++] = y0;  xyz[i++] = sin(x0,y0);
        xyz[i++] = x0;  xyz[i++] = y1;  xyz[i++] = sin(x0,y1);
        xyz[i++] = x1;  xyz[i++] = y1;  xyz[i++] = sin(x1,y1);
        xyz[i++] = x1;  xyz[i++] = y0;  xyz[i++] = sin(x1,y0);
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
    float[] q = new float[n];
    int nq = n/12;
    for (int iq=0,i=0,j=0; iq<nq; ++iq) {
      float xa = xyz[i++];  
      float ya = xyz[i++];  
      float za = xyz[i++];  
      float xb = xyz[i++];  
      float yb = xyz[i++];  
      float zb = xyz[i++];  
      float xc = xyz[i++];  
      float yc = xyz[i++];  
      float zc = xyz[i++];  
      float xd = xyz[i++];  
      float yd = xyz[i++];  
      float zd = xyz[i++];  
      float x = 0.25f*(xa+xb+xc+xd);
      if (x>5.0f) {
        za += exp(xa,ya);
        zb += exp(xb,yb);
        zc += exp(xc,yc);
        zd += exp(xd,yd);
      }
      q[j++] = xa;  q[j++] = ya;  q[j++] = za;
      q[j++] = xb;  q[j++] = yb;  q[j++] = zb;
      q[j++] = xc;  q[j++] = yc;  q[j++] = zc;
      q[j++] = xd;  q[j++] = yd;  q[j++] = zd;
    }
    return q;
  }

  private static float[] makeColors(float[] xyz) {
    int nv = xyz.length/3;
    float[] rgb = new float[3*nv];
    for (int iv=0,jv=0,jc=0; iv<nv; ++iv) {
      float x = xyz[jv++];
      float y = xyz[jv++];
      float z = xyz[jv++];
      float s = 1.0f/(float)Math.sqrt(x*x+y*y+z*z);
      rgb[jc++] = x*s;
      rgb[jc++] = y*s;
      rgb[jc++] = z*s;
    }
    return rgb;
  }

  /*
  private static void bench(float[] xyz, float[] rgb) {
    System.out.println("Benchmarking QuadGroup construction");
    int nv = xyz.length/3;
    int nq = nv/4;
    System.out.println("  number of vertices = "+nv);
    System.out.println("  number of quads = "+nq);

    int nqg;
    Stopwatch sw = new Stopwatch();
    for (int ntrial=0; ntrial<3; ++ntrial) {
      sw.restart();
      for (nqg=0; sw.time()<1.0; ++nqg)
        new QuadGroup(true,xyz,rgb);
      sw.stop();
      double rate = (double)nqg*(double)nq/sw.time();
      System.out.println("  quads/sec = "+rate);
    }
  }
  */
}
