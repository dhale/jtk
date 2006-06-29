package edu.mines.jtk.sgl.test;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.sgl.*;

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

    Sampling s = new Sampling(101,0.01,0.0);
    ImagePanel iop = new ImagePanel(s,s,s,null);
    aaf.addChild(iop);

    World world = new World();
    world.addChild(aaq);

    TestFrame frame = new TestFrame(world);
    frame.setVisible(true);
  }
}
