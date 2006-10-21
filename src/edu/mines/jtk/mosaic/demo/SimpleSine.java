/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.demo;

import static edu.mines.jtk.util.MathPlus.FLT_PI;

import java.awt.BorderLayout;
import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.Array;

/**
 * A simple demonstration of {@link edu.mines.jtk.mosaic.PlotFrame}.
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
    _plotPanel = new PlotPanel();
    _plotPanel.setTitle("The sine function");
    _plotPanel.setHLabel("x");
    _plotPanel.setVLabel("sin(x)");
    _gridView = _plotPanel.addGrid();
    _pointsView = _plotPanel.addPoints(x,s);
    _pointsView.setStyle("r-o");
    _plotFrame = new PlotFrame(_plotPanel);
    _plotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    _plotFrame.setVisible(true);
    _plotFrame.add(
      new Label("In either plot or axes, click-drag to zoom, click to unzoom."),
      BorderLayout.NORTH);
  }

  public PlotFrame getPlotFrame() {
    return _plotFrame;
  }

  public PlotPanel getPlotPanel() {
    return _plotPanel;
  }

  public PointsView getPointsView() {
    return _pointsView;
  }

  public GridView getGridView() {
    return _gridView;
  }

  private PlotFrame _plotFrame;
  private PlotPanel _plotPanel;
  private PointsView _pointsView;
  private GridView _gridView;
}
