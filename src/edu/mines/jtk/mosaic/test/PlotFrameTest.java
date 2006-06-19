/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.test;

import edu.mines.jtk.dsp.*;
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
    double d1 = 0.03;
    double f1 = -1.3;
    Sampling s1 = new Sampling(n1,d1,f1);
    int n2 = 101;
    double d2 = 29.0;
    double f2 = 33.0;
    Sampling s2 = new Sampling(n2,d2,f2);

    float[][] f = Array.sin(Array.rampfloat(0.0f,0.1f,0.1f,n1,n2));
    float ax = (float)(f2+d2*n2/2.0);
    float bx = (float)(0.45*d2*(n2-1));
    float cx = (float)(0.1/d1);
    float[] x1 = Array.rampfloat((float)f1,(float)d1,n1);
    float[] x2 = Array.add(ax,Array.mul(bx,Array.sin(Array.mul(cx,x1))));

    PlotPanel.Orientation orientation = PlotPanel.Orientation.X1DOWN_X2RIGHT;
    PlotPanel panel = new PlotPanel(1,2,orientation);

    PixelsView pxv0 = panel.addPixels(0,0,s1,s2,f);
    PixelsView pxv1 = panel.addPixels(0,1,s1,s2,f);
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

    final PlotFrame frame = new PlotFrame(panel);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setFontSize(24);
    frame.pack();
    frame.setVisible(true);
    frame.paintToPng(300,6,"junk.png");
  }
}
