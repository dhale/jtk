/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Float3;
import static edu.mines.jtk.util.MathPlus.FLT_PI;
import static edu.mines.jtk.util.MathPlus.sin;
import edu.mines.jtk.util.SimpleFloat3;

/**
 * Tests {@link edu.mines.jtk.sgl.AxisAlignedQuad}.
 * @author Dave Hale
 * @version 2006.06.29
 */
public class AxisAlignedQuadTest {

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

    TestFrame frame = new TestFrame(world);
    frame.setVisible(true);
  }
}
