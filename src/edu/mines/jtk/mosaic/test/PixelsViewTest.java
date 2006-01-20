/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.test;

import static java.lang.Math.*;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.*;
import edu.mines.jtk.mosaic.*;

/**
 * Tests {@link edu.mines.jtk.mosaic.PixelsView}
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.10.04
 */
public class PixelsViewTest {

  public static void main(String[] args) {
    int n1 = 11;
    int n2 = 11;
    float d1 = 1.0f/(float)max(1,n1-1);
    float d2 = 1.0f/(float)max(1,n2-1);
    float[][] f = Array.rampfloat(0.0f,d1,d2,n1,n2);

    Sampling s1 = new Sampling(n1,0.5,0.25*(n1-1));
    Sampling s2 = new Sampling(n2,0.5,0.25*(n2-1));

    PlotPanel panel = new PlotPanel(1,2);
    PixelsView pv0 = panel.addPixels(0,0,f);
    pv0.setInterpolation(PixelsView.Interpolation.NEAREST);
    pv0.setColorMap(PixelsView.ColorMap.JET);
    pv0.setPercentiles(0.0f,100.0f);

    PixelsView pv0b = panel.addPixels(0,0,s1,s2,f);
    pv0b.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv0b.setColorMap(PixelsView.ColorMap.GRAY);
    pv0b.setPercentiles(0.0f,100.0f);

    PixelsView pv1 = panel.addPixels(0,1,f);
    pv1.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv1.setColorMap(PixelsView.ColorMap.GRAY);
    pv1.setPercentiles(0.0f,100.0f);

    PixelsView pv1b = panel.addPixels(0,1,s1,s2,f);
    pv1b.setInterpolation(PixelsView.Interpolation.NEAREST);
    pv1b.setColorMap(PixelsView.ColorMap.JET);
    pv1b.setPercentiles(0.0f,100.0f);

    PlotFrame frame = new PlotFrame(panel);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.paintToPng(300,6,"junk.png");
  }
}
