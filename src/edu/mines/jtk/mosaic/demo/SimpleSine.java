/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.demo;

import javax.swing.*;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.*;
import edu.mines.jtk.mosaic.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Demonstrates {@link edu.mines.jtk.mosaic.PlotFrame}
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.03
 */
public class SimpleSine {

  public static void main(String[] args) {
    float[] x = Array.rampfloat(0.0f,4.0f*FLT_PI/200.0f,201);
    float[] s = Array.sin(x);
    PlotPanel panel = new PlotPanel();
    PointsView pv = panel.addPoints(x,s);
    pv.setStyle("r-o");
    panel.setTitle("The sine function");
    panel.setHLabel("x");
    panel.setVLabel("sin(x)");
    panel.addGrid();
    PlotFrame frame = new PlotFrame(panel);
    frame.setVisible(true);
  }
}
