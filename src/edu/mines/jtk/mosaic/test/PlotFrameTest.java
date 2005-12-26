/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.test;

import static java.lang.Math.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.gui.*;
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
    float d1 = 1.0f/(float)max(1,n1-1);
    float d2 = 1.0f/(float)max(1,n2-1);
    float[][] f = Array.rampfloat(0.0f,d1,d2,n1,n2);
    f = Array.sin(Array.mul(10.0f,f));

    PlotFrame.Orientation orientation = PlotFrame.Orientation.X1DOWN_X2RIGHT;
    PlotFrame plotFrame = new PlotFrame(1,2,orientation);
    PixelsView pv0 = plotFrame.addPixels(0,0,f);
    PixelsView pv1 = plotFrame.addPixels(0,1,f);
    pv0.setColorModel(ByteIndexColorModel.linearGray(0.0,1.0));
    pv1.setColorModel(ByteIndexColorModel.linearHue(0.0,0.67));
    plotFrame.addColorBar("amplitude");
    plotFrame.setTitle("A Test of PlotFrame");
    plotFrame.setX1Label("depth (km)");
    plotFrame.setX2Label(0,"offset (km)");
    plotFrame.setX2Label(1,"velocity (km/s)");
    plotFrame.setVisible(true);

    try {
      plotFrame.paintToPng(300,6,"junk.png");
    } catch (java.io.IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
