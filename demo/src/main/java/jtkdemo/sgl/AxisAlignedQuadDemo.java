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
package jtkdemo.sgl;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.*;
import edu.mines.jtk.sgl.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Demos {@link edu.mines.jtk.sgl.AxisAlignedQuad}.
 * @author Dave Hale
 * @version 2006.06.29
 */
public class AxisAlignedQuadDemo {

  public static void main(String[] args) {
    int nx = 101;
    int ny = 121;
    int nz = 141;
    double dx = 1.0/(nx-1);
    double dy = dx;
    double dz = dx;
    double fx = 0.0;
    double fy = 0.0;
    double fz = 0.0;
    double lx = fx+(nx-1)*dx;
    double ly = fy+(ny-1)*dy;
    double lz = fz+(nz-1)*dz;

    Point3 qmin = new Point3(fx,fy,fz);
    Point3 qmax = new Point3(lx,ly,lz);

    AxisAlignedQuad aaq = new AxisAlignedQuad(Axis.Y,qmin,qmax);
    AxisAlignedFrame aaf = aaq.getFrame();
    Sampling sx = new Sampling(nx,dx,fx);
    Sampling sy = new Sampling(ny,dy,fy);
    Sampling sz = new Sampling(nz,dz,fz);
    float[][][] a = new float[nx][ny][nz];
    for (int ix=0; ix<nx; ++ix) {
      float x = (float)(ix*dx);
      for (int iy=0; iy<ny; ++iy) {
        float y = (float)(iy*dy);
        for (int iz=0; iz<nz; ++iz) {
          float z = (float)(iz*dz);
          //a[ix][iy][iz] = x+y+z;
          a[ix][iy][iz] = sin(4.0f*FLT_PI*(x+y+z));
        }
      }
    }
    Float3 f3 = new SimpleFloat3(a);
    ImagePanel iop = new ImagePanel(sz,sy,sx,f3);
    aaf.addChild(iop);

    World world = new World();
    world.addChild(aaq);

    DemoFrame frame = new DemoFrame(world);
    frame.setVisible(true);
  }
}
