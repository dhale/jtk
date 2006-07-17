package edu.mines.jtk.sgl.test;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.io.*;
import edu.mines.jtk.sgl.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.sgl.AxisAlignedQuad}.
 * @author Dave Hale
 * @version 2006.06.29
 */
public class AxisAlignedQuadTest {

  public static void main(String[] args) {
    Point3 qmin = new Point3(0,0,0);
    Point3 qmax = new Point3(1,1,1);

    AxisAlignedQuad aaq = new AxisAlignedQuad(Axis.Y,qmin,qmax);
    AxisAlignedFrame aaf = aaq.getFrame();

    int nx = 101;
    int ny = 101;
    int nz = 101;
    double dx = 1.0/(nx-1);
    double dy = 1.0/(ny-1);
    double dz = 1.0/(nz-1);
    double fx = 0.0;
    double fy = 0.0;
    double fz = 0.0;
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
          a[ix][iy][iz] = sin(4.0f*FLT_PI*(x+y+z));
        }
      }
    }
    Float3 f = new SimpleFloat3(a);
    ImagePanel iop = new ImagePanel(sx,sy,sz,f);
    aaf.addChild(iop);

    World world = new World();
    world.addChild(aaq);

    TestFrame frame = new TestFrame(world);
    frame.setVisible(true);
  }
}
