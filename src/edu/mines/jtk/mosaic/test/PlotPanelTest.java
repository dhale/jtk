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
 * Tests {@link edu.mines.jtk.mosaic.PlotPanel}
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.24
 */
public class PlotPanelTest {

  public static void main(String[] args) {

    int n1 = 11;
    int n2 = 11;
    float d1 = 1.0f/(float)max(1,n1-1);
    float d2 = 1.0f/(float)max(1,n2-1);
    float[][] f = Array.rampfloat(0.0f,d1,d2,n1,n2);

    PlotPanel plotPanel = new PlotPanel();
    plotPanel.setPreferredSize(new Dimension(600,300));
    plotPanel.addPixels(f);
    plotPanel.addColorBar();

    //ModeManager modeManager = mosaic.getModeManager();
    //TileZoomMode zoomMode = new TileZoomMode(modeManager);
    //zoomMode.setActive(true);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(plotPanel,BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
    try {
      plotPanel.paintToPng(300,6,"junk.png");
    } catch (java.io.IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
