/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.*;
import java.awt.*;
import edu.mines.jtk.util.*;

/**
 * A lollypop view of a sampled function f(x) of one variable x.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.01.01
 */
public class LollypopView extends TiledView {

  /**
   * Constructs a lollypop view.
   * @param sx the sampling of the variable x; by reference, not copied.
   * @param f the sampled function f(x); by reference, not copied.
   */
  public LollypopView(Sampling sx, float[] f) {
    Check.argument(!sx.isEmpty(),"sampling is not empty");
    Check.argument(sx.getCount()==f.length,"sx count equals length of f");
    set(sx,f);
  }
  
  /**
   * Sets the sampling and function values.
   * @param sx the sampling of the variable x.
   * @param f the sampled function f(x).
   */
  public void set(Sampling sx, float[] f) {
    _sx = sx;
    _f = f;
    updateBestProjectors();
  }

  public void paint(Graphics2D g2d) {
    Projector hp = getHorizontalProjector();
    Projector vp = getVerticalProjector();
    Transcaler ts = getTranscaler();

    // Radius in pixels of balls.
    double rbx = ballRadiusX();
    double rby = ballRadiusY();
    double hsr = hp.getScaleRatio(getBestHorizontalProjector());
    double vsr = vp.getScaleRatio(getBestVerticalProjector());
    int rx = ts.x(rbx*hsr)-ts.x(0.0);
    int ry = ts.y(rby*vsr)-ts.y(0.0);
    int rb = min(rx,ry);

    // Horizontal line for function value 0.0.
    int x0 = ts.x(hp.u0());
    int x1 = ts.x(hp.u1());
    int y0 = ts.y(vp.u(0.0));
    g2d.drawLine(x0,y0,x1,y0);

    // Lollypop for each sample.
    int nx = _sx.getCount();
    double dx = _sx.getDelta();
    double fx = _sx.getFirst();
    for (int ix=0; ix<nx; ++ix) {
      double xi = fx+ix*dx;
      double fi = _f[ix];
      int x = ts.x(hp.u(xi));
      int y = ts.y(vp.u(fi));
      g2d.drawLine(x,y0,x,y);
      g2d.fillOval(x-rb,y-rb,2*rb,2*rb);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  Sampling _sx;
  float[] _f;

  // Called when we might need realignment.
  private void updateBestProjectors() {

    // Min and max sample values.
    double nx = _sx.getCount();
    double xf = _sx.getFirst();
    double xl = _sx.getLast();
    double xmin = min(xf,xl);
    double xmax = max(xf,xl);

    // Min and max function values.
    double fmin = _f[0];
    double fmax = _f[0];
    for (int ix=0; ix<nx; ++ix) {
      if (_f[ix]<fmin)
        fmin = _f[ix];
      if (_f[ix]>fmax)
        fmax = _f[ix];
    }

    // Best projectors.
    double rbx = ballRadiusX();
    double rby = ballRadiusY();
    Projector bhp = new Projector(xmin,xmax,rbx,1.0-rbx);
    Projector bvp = new Projector(fmax,fmin,rby,1.0-rby);
    setBestProjectors(bhp,bvp);
  }

  private double ballRadiusX() {
    double nx = _sx.getCount();
    return 0.9/(2.0*nx);
  }

  private double ballRadiusY() {
    return 1.0/25.0;
  }
}
