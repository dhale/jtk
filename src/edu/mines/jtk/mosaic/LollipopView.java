/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.M.*;

/**
 * A lollipop view of a sampled function f(x) of one variable x.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.01.01
 */
public class LollipopView extends TiledView {

  /**
   * The visibility of function value zero in the view. To understand these 
   * options, define fmin and fmax to be the minimum and maximum function 
   * values, respectively. Then, ...
   * <p>
   * If NORMAL, the values fmax and fmin correspond to the top and bottom 
   * of the view, respectively. Depending on those values, the function 
   * value zero may or may not be displayed.
   * <p>
   * If ALWAYS, the function value zero will <em>always</em> be displayed. 
   * If fmin &lt;= 0 &lt;= fmax, then this options behaves like NORMAL.
   * If 0 &lt; fmin, then value zero corresponds to the bottom of the view. 
   * If fmax &lt; 0, then value zero corresponds to the top of the view.
   * <p>
   * If MIDDLE, the function value zero corresponds to the <em>middle</em>
   * of the the view, halfway between the top and bottom. The function 
   * values ftop and -ftop correspond to the top and bottom of the view, 
   * respectively, where ftop is the maximum of the absolute values of
   * fmin and fmax.
   */
  public enum ShowZero {
    NORMAL, ALWAYS, MIDDLE
  }

  /**
   * Constructs a lollipop view.
   * @param sx the sampling of the variable x; by reference, not copied.
   * @param f the sampled function f(x); by reference, not copied.
   */
  public LollipopView(Sampling sx, float[] f) {
    Check.argument(!sx.isEmpty(),"sampling is not empty");
    Check.argument(sx.getCount()==f.length,"sx count equals length of f");
    set(sx,f);
  }
  
  /**
   * Sets the sampling and function values. Sets these values by reference,
   * not by copy. Changes to these values are not
   * @param sx the sampling of the variable x; by reference, not copied.
   * @param f the sampled function f(x); by reference, not copied.
   */
  public void set(Sampling sx, float[] f) {
    _sx = sx;
    _f = f;
    updateBestProjectors();
    repaint();
  }

  /**
   * Gets the sampling. Returns the sampling by reference, not by copy.
   * @return the sampling.
   */
  public Sampling getSampling() {
    return _sx;
  }

  /**
   * Gets the function values. Returns the values by reference, not by copy.
   * @return the function values.
   */
  public float[] getFunction() {
    return _f;
  }


  /**
   * Sets the visibility of function value zero in this view.
   * The default visibility is ALWAYS.
   * @param showZero the visibility of function value zero.
   */
  public void setShowZero(ShowZero showZero) {
    if (_showZero!=showZero) {
      _showZero = showZero;
      updateBestProjectors();
      repaint();
    }
  }

  /**
   * Sets the color used to paint the lollipops. 
   * The default color is the tile foreground color. 
   * That default is used if the specified color is null.
   * @param color the lollipop color; null, for tile foreground color.
   */
  public void setLollipopColor(Color color) {
    if (!equalColors(_lollipopColor,color)) {
      _lollipopColor = color;
      repaint();
    }
  }

  public void paint(Graphics2D g2d) {
    /*
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    */

    // Lollipop sampling.
    int nx = _sx.getCount();
    double dx = _sx.getDelta();
    double fx = _sx.getFirst();
    double lx = _sx.getLast();

    // Our preferred projectors.
    Projector bhp = getBestHorizontalProjector();
    Projector bvp = getBestVerticalProjector();

    // The projectors (and transcaler) we must use.
    Projector hp = getHorizontalProjector();
    Projector vp = getVerticalProjector();
    Transcaler ts = getTranscaler();

    // Radius of lollipop balls, in normalized coordinates. 
    // Must compensate for projector merging.
    double rbx = ballRadiusX()*hp.getScaleRatio(bhp);
    double rby = ballRadiusY()*vp.getScaleRatio(bvp);

    // Radius of lollipop balls, in pixels.
    int rx = ts.width(rbx);
    int ry = ts.height(rby);
    int rb = min(rx,ry);

    // Lollipop color, if specified.
    if (_lollipopColor!=null) 
      g2d.setColor(_lollipopColor);

    // Horizontal line for function value 0.0.
    int xf = ts.x(hp.u(fx));
    int xl = ts.x(hp.u(lx));
    int x1 = min(xf,xl)-rb;
    int x2 = max(xf,xl)+rb;
    int y0 = ts.y(vp.u(0.0));
    g2d.drawLine(x1,y0,x2,y0);

    // One lollipop for each sample.
    for (int ix=0; ix<nx; ++ix) {
      double xi = fx+ix*dx;
      double fi = _f[ix];
      int x = ts.x(hp.u(xi));
      int y = ts.y(vp.u(fi));
      g2d.drawLine(x,y0,x,y);
      g2d.fillOval(x-rb,y-rb,1+2*rb,1+2*rb);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  Sampling _sx;
  float[] _f;
  private Color _lollipopColor = null;
  private ShowZero _showZero = ShowZero.ALWAYS;

  // Called when we might need realignment.
  private void updateBestProjectors() {

    // Min and max sample values.
    double nx = _sx.getCount();
    double xf = _sx.getFirst();
    double xl = _sx.getLast();
    double xmin = min(xf,xl);
    double xmax = max(xf,xl);
    if (xmin==xmax) {
      double tiny = 100.0*DBL_EPSILON*max(1.0,xmin);
      xmin -= tiny;
      xmax += tiny;
    }

    // Min and max function values.
    double fmin = _f[0];
    double fmax = _f[0];
    for (int ix=0; ix<nx; ++ix) {
      if (_f[ix]<fmin)
        fmin = _f[ix];
      if (_f[ix]>fmax)
        fmax = _f[ix];
    }

    // Adjust for visibility of function value zero.
    if (_showZero==ShowZero.ALWAYS) {
      fmin = min(0.0,fmin);
      fmax = max(0.0,fmax);
    } else if (_showZero==ShowZero.MIDDLE) {
      fmax = max(abs(fmin),abs(fmax));
      fmin = -fmax;
    }
    if (fmin==fmax) {
      double tiny = 100.0*DBL_EPSILON*max(1.0,fmin);
      fmin -= tiny;
      fmax += tiny;
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
    return 0.99/(2.0*nx);
  }

  private double ballRadiusY() {
    return 1.0/20.0;
  }

  private boolean equalColors(Color ca, Color cb) {
    return (ca==null)?cb==null:ca.equals(cb);
  }
}
