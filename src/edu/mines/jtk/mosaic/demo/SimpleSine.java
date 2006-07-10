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
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new SimpleSine();
      }
    });
  }

  public SimpleSine() {
    float[] x = Array.rampfloat(0.0f,4.0f*FLT_PI/200.0f,201);
    float[] s = Array.sin(x);
    _panel = new PlotPanel();
    _pointsView = _panel.addPoints(x,s);
    _pointsView.setStyle("r-o");
    _gridView = _panel.addGrid();
    _panel.setTitle("The sine function");
    _panel.setHLabel("x");
    _panel.setVLabel("sin(x)");
    _frame = new PlotFrame(_panel);
    _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    _frame.setVisible(true);
  }

  public PlotFrame getFrame() {
    return _frame;
  }

  public PlotPanel getPanel() {
    return _panel;
  }

  public PointsView getPointsView() {
    return _pointsView;
  }

  public GridView getGridView() {
    return _gridView;
  }

  private PlotFrame _frame;
  private PlotPanel _panel;
  private PointsView _pointsView;
  private GridView _gridView;
}
