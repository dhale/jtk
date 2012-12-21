/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * Tests {@link edu.mines.jtk.sgl.TriangleGroup}.
 * @author Dave Hale
 * @version 2006.06.28
 */
public class TriangleGroupTest {

  public static void main(String[] args) {
    float[] xyz = makeSineWave();
    xyz = addBulge(xyz);
    xyz = addTear(xyz);
    float[] rgb = makeColors(xyz);

    TriangleGroup tg = new TriangleGroup(true,xyz,rgb);
    System.out.println("TriangleGroup bounding sphere =\n" +
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
    ms.setSpecular(Color.WHITE);
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
    System.out.println("Benchmarking TriangleGroup construction");
    int nv = xyz.length/3;
    int nt = nv/3;
    System.out.println("  number of vertices = "+nv);
    System.out.println("  number of triangles = "+nt);

    int ntg;
    Stopwatch sw = new Stopwatch();
    for (int ntrial=0; ntrial<3; ++ntrial) {
      sw.restart();
      for (ntg=0; sw.time()<1.0; ++ntg)
        new TriangleGroup(true,xyz,rgb);
      sw.stop();
      double rate = (double)ntg*(double)nt/sw.time();
      System.out.println("  triangles/sec = "+rate);
    }
  }
  */
}
