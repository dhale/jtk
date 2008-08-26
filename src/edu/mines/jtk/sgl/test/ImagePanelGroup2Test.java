/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl.test;

import static edu.mines.jtk.util.MathPlus.*;
import edu.mines.jtk.util.*;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.sgl.*;

/**
 * Tests {@link edu.mines.jtk.sgl.ImagePanelGroup2}.
 * @author Dave Hale
 * @version 2008.08.24
 */
public class ImagePanelGroup2Test {

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
    Sampling sx = new Sampling(nx,dx,fx);
    Sampling sy = new Sampling(ny,dy,fy);
    Sampling sz = new Sampling(nz,dz,fz);
    float kx = 4.0f*FLT_PI*(float)dx;
    float ky = 4.0f*FLT_PI*(float)dy;
    float kz = 4.0f*FLT_PI*(float)dz;
    float[][][] fa = Array.rampfloat(0.0f,kz,ky,kx,nz,ny,nx);
    float[][][] fb = Array.sin(fa);
    Float3 f1 = new SimpleFloat3(fb);
    Float3 f2 = new SimpleFloat3(fa);
    ImagePanelGroup2 ipg = new ImagePanelGroup2(sx,sy,sz,f1,f2);
    ipg.setPercentiles1(1,99);
    World world = new World();
    world.addChild(ipg);
    TestFrame frame = new TestFrame(world);
    frame.setVisible(true);
  }
}
