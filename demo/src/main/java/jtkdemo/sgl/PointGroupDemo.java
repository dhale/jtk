/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
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
package jtkdemo.sgl;

import java.awt.*;

import edu.mines.jtk.sgl.*;
import static edu.mines.jtk.ogl.Gl.*;

/**
 * Demos {@link edu.mines.jtk.sgl.PointGroup}.
 * @author Dave Hale
 * @version 2009.01.13
 */
public class PointGroupDemo {

  public static void main(String[] args) {
    float[] xyz = makeSineWave();
    xyz = addBulge(xyz);
    float[] rgb = makeColors(xyz);

    PointGroup pg = new PointGroup(0.05f,xyz,rgb);
    System.out.println("PointGroup bounding sphere =\n" +
      pg.getBoundingSphere(true));

    /*
    StateSet states = new StateSet();
    ColorState cs = new ColorState();
    cs.setColor(Color.CYAN);
    states.add(cs);
    PointState ps = new PointState();
    ps.setSmooth(true);
    ps.setSize(3.0f);
    states.add(ps);
    pg.setStates(states);
    */

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
    pg.setStates(states);

    World world = new World();
    world.addChild(pg);

    DemoFrame frame = new DemoFrame(world);
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
    float[] xyz = new float[3*nx*ny];
    for (int ix=0,i=0; ix<nx; ++ix) {
      float xi = ix*dx;
      for (int iy=0; iy<ny; ++iy) {
        float yi = iy*dy;
        xyz[i++] = xi;  
        xyz[i++] = yi;  
        xyz[i++] = sin(xi,yi);
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
}
