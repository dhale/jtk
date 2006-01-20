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

    PlotPanel.Orientation orientation = PlotPanel.Orientation.X1DOWN_X2RIGHT;
    PlotPanel panel = new PlotPanel(1,2,orientation);

    PixelsView pxv0 = panel.addPixels(0,0,f);
    PixelsView pxv1 = panel.addPixels(0,1,f);
    pxv0.setColorMap(PixelsView.ColorMap.GRAY);
    pxv1.setColorMap(PixelsView.ColorMap.JET);

    PointsView ptv0 = panel.addPoints(0,0,x1,x2);
    PointsView ptv1 = panel.addPoints(0,1,x1,x2);
    ptv0.setStyle("r--.");
    ptv1.setStyle("k-o");

    panel.addColorBar("amplitude");
    panel.setTitle("A Test of PlotFrame");
    panel.setVLabel("depth (km)");
    panel.setHLabel(0,"offset (km)");
    panel.setHLabel(1,"velocity (km/s)");

    PlotFrame frame = new PlotFrame(panel);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.paintToPng(300,6,"junk.png");
  }
}
