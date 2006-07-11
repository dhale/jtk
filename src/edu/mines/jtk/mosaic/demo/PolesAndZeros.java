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
 * A simple demonstration of {@link edu.mines.jtk.mosaic.PlotFrame}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.03
 */
public class PolesAndZeros {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new PolesAndZeros();
      }
    });
  }

  public PolesAndZeros() {
    _pzp = new PoleZeroPlot();
  }
  private PoleZeroPlot _pzp;
  //private FilterPlot

  private class PoleZeroPlot {
    PoleZeroPlot() {
      this(new Cdouble[0],new Cdouble[0]);
    }
    PoleZeroPlot(Cdouble[] poles, Cdouble[] zeros) {
      _plotPanel = new PlotPanel();
      _plotPanel.setTitle("poles and zeros");
      _plotPanel.setHLabel("real");
      _plotPanel.setVLabel("imaginary");
      _plotPanel.setHLimits(-2.0,2.0);
      _plotPanel.setVLimits(-2.0,2.0);
      _gridView = _plotPanel.addGrid("H0-V0-");
      _circleView = makeCircleView();
      _plotPanel.addTiledView(_circleView);
      set(poles,zeros);
      _plotFrame = new PlotFrame(_plotPanel);
      _plotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      _plotFrame.setSize(400,440);
      _plotFrame.setVisible(true);
    }
    void set(Cdouble[] poles, Cdouble[] zeros) {
      int np = poles.length;
      int nz = zeros.length;
      float[] xp = new float[np];
      float[] yp = new float[np];
      float[] xz = new float[nz];
      float[] yz = new float[nz];
      for (int ip=0; ip<np; ++ip) {
        xp[ip] = (float)poles[ip].r;
        yp[ip] = (float)poles[ip].i;
      }
      for (int iz=0; iz<nz; ++iz) {
        xz[iz] = (float)zeros[iz].r;
        yz[iz] = (float)zeros[iz].i;
      }
      if (_polesView==null) {
        _polesView = _plotPanel.addPoints(xp,yp);
        _polesView.setMarkStyle(PointsView.Mark.CROSS);
      } else {
        _polesView.set(xp,yp);
      }
      if (_zerosView==null) {
        _zerosView = _plotPanel.addPoints(xz,yz);
        _zerosView.setMarkStyle(PointsView.Mark.HOLLOW_CIRCLE);
      } else {
        _zerosView.set(xz,yz);
      }
    }
    private PointsView makeCircleView() {
      int nt = 1000;
      double dt = 2.0*DBL_PI/(nt-1);
      float[] x = new float[nt];
      float[] y = new float[nt];
      for (int it=0; it<nt; ++it) {
        float t = (float)(it*dt);
        x[it] = cos(t);
        y[it] = sin(t);
      }
      return new PointsView(x,y);
    }
    private PlotFrame _plotFrame;
    private PlotPanel _plotPanel;
    private PointsView _polesView;
    private PointsView _zerosView;
    private PointsView _circleView;
    private GridView _gridView;
  }
}
