/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.util.*;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A view of points (x,y) with marks and/or lines.
 * <em>Not yet fully implemented!</em>
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.25
 */
public class MarkLineView extends TiledView {

  /**
   * Constructs a view of a sampled function y(x) of one variable x.
   * @param sx the sampling of the variable x.
   * @param y array of sampled function values y(x).
   */
  public MarkLineView(Sampling sx, float[] y) {
    Check.argument(sx.getCount()==y.length,"sx count equals length of y");
    int nxy = sx.getCount();
    float[] xt = new float[nxy];
    float[] yt = new float[nxy];
    for (int ixy=0; ixy<nxy; ++ixy) {
      xt[ixy] = (float)sx.getValue(ixy);
      yt[ixy] = y[ixy];
    }
    _ns = 1;
    _nxy.add(nxy);
    _x.add(xt);
    _y.add(yt);
    updateBestProjectors();
    repaint();
  }

  /**
   * Sets the line color.
   * The default line color is the tile foreground color. 
   * That default is used if the specified line color is null.
   * @param lineColor the line color; null, for tile foreground color.
   */
  public void setLineColor(Color lineColor) {
    if (!equalColors(_lineColor,lineColor)) {
      _lineColor = lineColor;
      repaint();
    }
  }

  /**
   * Sets the line width.
   * The default line is zero, for the thinnest lines.
   * @param lineWidth the line width.
   */
  public void setLineWidth(float lineWidth) {
    if (_lineWidth!=lineWidth) {
      _lineWidth = lineWidth;
      repaint();
    }
  }

  public void paint(Graphics2D g2d) {
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

    // Our preferred projectors.
    Projector bhp = getBestHorizontalProjector();
    Projector bvp = getBestVerticalProjector();

    // The projectors (and transcaler) we must use.
    Projector hp = getHorizontalProjector();
    Projector vp = getVerticalProjector();
    Transcaler ts = getTranscaler();
    double resolution = ts.getResolution();

    // Color, if not null.
    if (_lineColor!=null) 
      g2d.setColor(_lineColor);

    // Line width, if not zero or if resolution not one.
    if (_lineWidth!=0.0f || resolution!=1.0) {
      float lineWidth = (float)(max(_lineWidth,1.0)*resolution);
      g2d.setStroke(new BasicStroke(lineWidth));
    }

    // For all line segments, ...
    for (int is=0; is<_ns; ++is) {

      // Draw line segment.
      // TODO: draw marks, too.
      int nxy = _nxy.get(is);
      float[] x = _x.get(is);
      float[] y = _y.get(is);
      int x1 = ts.x(hp.u(x[0]));
      int y1 = ts.y(vp.u(y[0]));
      for (int ixy=1; ixy<nxy; ++ixy) {
        float xi = x[ixy];
        float yi = y[ixy];
        int x2 = ts.x(hp.u(xi));
        int y2 = ts.y(vp.u(yi));
        g2d.drawLine(x1,y1,x2,y2);
        x1 = x2;
        y1 = y2;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  int _ns; // number of plot segments
  ArrayList<Integer> _nxy = new ArrayList<Integer>();
  ArrayList<float[]> _x = new ArrayList<float[]>();
  ArrayList<float[]> _y = new ArrayList<float[]>();
  private Color _lineColor;
  private float _lineWidth;

  // Called when we might need realignment.
  private void updateBestProjectors() {

    // Min and max (x,y) values.
    float xmin =  FLT_MAX;
    float ymin =  FLT_MAX;
    float xmax = -FLT_MAX;
    float ymax = -FLT_MAX;
    for (int is=0; is<_ns; ++is) {
      int nxy = _nxy.get(is);
      float[] x = _x.get(is);
      float[] y = _y.get(is);
      for (int ixy=0; ixy<nxy; ++ixy) {
        float xi = x[ixy];
        float yi = y[ixy];
        xmin = min(xmin,xi);
        ymin = min(ymin,yi);
        xmax = max(xmax,xi);
        ymax = max(ymax,yi);
      }
    }

    // TODO: handle mark size
    // TODO: handle x vertical, y horizontal

    // Best projectors.
    Projector bhp = new Projector(xmin,xmax,0.0,1.0);
    Projector bvp = new Projector(ymax,ymin,0.0,1.0);
    setBestProjectors(bhp,bvp);
  }

  private boolean equalColors(Color ca, Color cb) {
    return (ca==null)?cb==null:ca.equals(cb);
  }
}
