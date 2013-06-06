/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import javax.swing.SwingUtilities;
import edu.mines.jtk.dsp.Sampling;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.sgl.ImagePanelGroup}.
 * @author Dave Hale
 * @version 2007.01.19
 */
public class ImagePanelGroupTest {

  public static void main(final String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        go(args);
      }
    });
  }
  private static void go(final String[] args) {
    int n1 = 101;
    int n2 = 121;
    int n3 = 141;
    double d1 = 1.0/(n1-1);
    double d2 = d1;
    double d3 = d1;
    double f1 = 0.0;
    double f2 = 0.0;
    double f3 = 0.0;
    Sampling s1 = new Sampling(n1,d1,f1);
    Sampling s2 = new Sampling(n2,d2,f2);
    Sampling s3 = new Sampling(n3,d3,f3);
    float k1 = 4.0f*FLT_PI*(float)d1;
    float k2 = 4.0f*FLT_PI*(float)d2;
    float k3 = 4.0f*FLT_PI*(float)d3;
    float[][][] f = sin(rampfloat(0.0f,k1,k2,k3,n1,n2,n3));
    final ImagePanelGroup ipg = new ImagePanelGroup(s1,s2,s3,f);
    ipg.setPercentiles(1,99);
    World world = new World();
    world.addChild(ipg);
    final TestFrame frame = new TestFrame(world);
    frame.getOrbitView().setScale(2.0);
    frame.setVisible(true);
    if (args.length>0) {
      frame.getViewCanvas().paintToFile("f0"+args[0]);
      ipg.setSlices(0,0,0);
      frame.getViewCanvas().paintToFile("f1"+args[0]);
    }
  }
}
