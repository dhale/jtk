/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.test;

import edu.mines.jtk.util.*;
import edu.mines.jtk.mosaic.*;

/**
 * Tests {@link edu.mines.jtk.mosaic.PlotFrame}
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.25
 */
public class PlotFrameTest {

  public static void main(String[] args) {

    int n1 = 101;
    int n2 = 101;
    float[][] f = Array.sin(Array.rampfloat(0.0f,0.1f,0.1f,n1,n2));
    float ax = (float)(0.5*(n2-1));
    float[] x1 = Array.rampfloat(0.0f,1.0f,n1);
    float[] x2 = Array.add(ax,Array.mul(ax,Array.sin(Array.mul(0.1f,x1))));

    PlotFrame.Orientation orientation = PlotFrame.Orientation.X1DOWN_X2RIGHT;
    PlotFrame pf = new PlotFrame(1,2,orientation);

    PixelsView pxv0 = pf.addPixels(0,0,f);
    PixelsView pxv1 = pf.addPixels(0,1,f);
    pxv0.setColorMap(PixelsView.ColorMap.GRAY);
    pxv1.setColorMap(PixelsView.ColorMap.JET);

    PointsView ptv0 = pf.addPoints(0,0,x1,x2);
    PointsView ptv1 = pf.addPoints(0,1,x1,x2);
    ptv0.setStyle("r--.");
    ptv1.setStyle("k-o");

    pf.addColorBar("amplitude");
    pf.setTitle("A Test of PlotFrame");
    pf.setX1Label("depth (km)");
    pf.setX2Label(0,"offset (km)");
    pf.setX2Label(1,"velocity (km/s)");
    pf.setVisible(true);

    try {
      pf.paintToPng(300,6,"junk.png");
    } catch (java.io.IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
