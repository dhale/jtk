/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.util.ArrayList;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * A view of points (x1,x2) with marks at points and/or lines between them.
 * Points (x1,x2) may be specified as arrays x1 and x2 of coordinates. Each 
 * pair of arrays x1 and x2 corresponds to one plot segment. Multiple plot 
 * segments may be specified by arrays of arrays of x1 and x2 coordinates.
 * <p>
 * For each point (x1,x2), a mark with a specified style, size, and color
 * may be painted. Between each consecutive pair of points (x1,x2) within 
 * a plot segment, lines with specified style, width, and color may be 
 * painted.
 * <p>
 * For example, to view sampled functions x2 = sin(x1) and x2 = cos(x1),
 * one might construct two plot segments by specifying an array of two 
 * arrays of x1 coordinates and a corresponding array of two arrays of 
 * x2 coordinates.
 * <p>
 * Note that mark and line attributes are constant for each points view.
 * These attributes do not vary among plot segments. To paint marks and 
 * lines with different attributes, construct multiple views.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.28
 */
public class PointsView extends TiledView {

  /**
   * Orientation of axes x1 and x2. For example, the default orientation 
   * X1RIGHT_X2UP corresponds to x1 increasing horizontally from left to 
   * right, and x2 increasing vertically from bottom to top.
   */
  public enum Orientation {
    X1RIGHT_X2UP,
    X1DOWN_X2RIGHT
  }

  /**
   * The style of mark plotted at points (x1,x2).
   * The default mark style is none.
   */
  public enum Mark {
    NONE,
    POINT,
    PLUS,
    CROSS,
    ASTERISK,
    HOLLOW_CIRCLE,
    HOLLOW_SQUARE,
    FILLED_CIRCLE,
    FILLED_SQUARE,
  }

  /**
   * The style of line plotted between consecutive points (x1,x2).
   * The default line style is solid.
   */
  public enum Line {
    NONE,
    SOLID,
    DASH,
    DOT,
    DASH_DOT,
  }

  /**
   * Constructs a view of points (x1,x2) with specified x2 coordinates.
   * The corresponding coordinates x1 are assumed to be 0, 1, 2, ....
   * @param x2 array of x2 coordinates.
   */
  public PointsView(float[] x2) {
    float[] x1 = rampfloat(0.0f,1.0f,x2.length);
    set(x1,x2);
  }

  /**
   * Constructs a view of points (x1,x2) for a sampled function x2(x1).
   * @param s1 the sampling of x1 coordinates.
   * @param x2 array of x2 coordinates.
   */
  public PointsView(Sampling s1, float[] x2) {
    set(s1,x2);
  }

  /**
   * Constructs a view of points (x1,x2) with a single plot segment.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   */
  public PointsView(float[] x1, float[] x2) {
    set(x1,x2);
  }

  /**
   * Constructs a view of points (x1,x2) with multiple plot segments.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * @param x1 array of arrays of x1 coordinates.
   * @param x2 array of arrays of x2 coordinates.
   */
  public PointsView(float[][] x1, float[][] x2) {
    set(x1,x2);
  }

  /**
   * Constructs a view of points (x1,x2,x3) with a single plot segment.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * If x3 is not null, its length must equal that of x1 and x2.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   * @param x3 array of x3 coordinates; null, if none.
   */
  public PointsView(float[] x1, float[] x2, float[] x3) {
    set(x1,x2,x3);
  }

  /**
   * Constructs a view of points (x1,x2,x3) with multiple plot segments.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * If x3 is not null, its length must equal that of x1 and x2.
   * @param x1 array of arrays of x1 coordinates.
   * @param x2 array of arrays of x2 coordinates.
   * @param x3 array of arrays of x3 coordinates.
   */
  public PointsView(float[][] x1, float[][] x2, float[][] x3) {
    set(x1,x2,x3);
  }

  /**
   * Sets (x1,x2) coordinates for a sampled function x2(x1).
   * @param s1 the sampling of x1 coordinates.
   * @param x2 array of x2 coordinates.
   */
  public void set(Sampling s1, float[] x2) {
    Check.argument(s1.getCount()==x2.length,"s1 count equals x2 length");
    int n1 = x2.length;
    float[] x1 = new float[n1];
    for (int i1=0; i1<n1; ++i1)
      x1[i1] = (float)s1.getValue(i1);
    set(x1,x2);
  }

  /**
   * Sets arrays of (x1,x2) coordinates for a single plot segment.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   */
  public void set(float[] x1, float[] x2) {
    set(x1,x2,null);
  }

  /**
   * Sets arrays of (x1,x2,x3) coordinates for a single plot segment.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * If x3 is not null, its length must equal that of x1 and x2.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   * @param x3 array of x3 coordinates; null, if none.
   */
  public void set(float[] x1, float[] x2, float[] x3) {
    Check.argument(x1.length==x2.length,"x1.length equals x2.length");
    if (x3!=null)
      Check.argument(x1.length==x3.length,"x1.length equals x3.length");
    _ns = 1;
    _nx.clear();
    _x1.clear();
    _x2.clear();
    _x3.clear();
    _nxmax = x1.length;
    _nx.add(x1.length);
    _x1.add(copy(x1));
    _x2.add(copy(x2));
    if (x3!=null)
      _x3.add(copy(x3));
    updateBestProjectors();
    repaint();
  }

  /**
   * Sets array of arrays of (x1,x2) coordinates for multiple plot segments.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * @param x1 array of arrays of x1 coordinates.
   * @param x2 array of arrays of x2 coordinates.
   */
  public void set(float[][] x1, float[][] x2) {
    set(x1,x2,null);
  }

  /**
   * Sets array of arrays of (x1,x2,x3) coordinates for multiple plot segments.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * If x3 is not null, its length must equal that of x1 and x2.
   * @param x1 array of arrays of x1 coordinates.
   * @param x2 array of arrays of x2 coordinates.
   * @param x3 array of arrays of x3 coordinates; null, if none.
   */
  public void set(float[][] x1, float[][] x2, float[][] x3) {
    Check.argument(x1.length==x2.length,"x1.length equals x2.length");
    if (x3!=null)
      Check.argument(x1.length==x3.length,"x1.length equals x3.length");
    _ns = x1.length;
    _nx.clear();
    _x1.clear();
    _x2.clear();
    _x3.clear();
    _nxmax = 0;
    for (int is=0; is<_ns; ++is) {
      Check.argument(x1[is].length==x2[is].length,
                    "x1[i].length equals x2[i].length");
      _nxmax = max(_nxmax,x1[is].length);
      _nx.add(x1[is].length);
      _x1.add(copy(x1[is]));
      _x2.add(copy(x2[is]));
      if (x3!=null)
        _x3.add(copy(x3[is]));
    }
    updateBestProjectors();
    repaint();
  }

  /**
   * Sets the orientation of (x1,x2) axes.
   * @param orientation the orientation.
   */
  public void setOrientation(Orientation orientation) {
    if (_orientation!=orientation) {
      _orientation = orientation;
      updateBestProjectors();
      repaint();
    }
  }

  /**
   * Gets the orientation of (x1,x2) axes.
   * @return the orientation.
   */
  public Orientation getOrientation() {
    return _orientation;
  }

  /**
   * Sets the color, line style, and mark style from a style string.
   * This method provides a convenient way to set the most commonly
   * specified attributes of lines and marks painted by this view.
   * <p>
   * To specify a color, the style string may contain one of "r" for red,
   * "g" for green, "b" for blue, "c" for cyan, "m" for magenta, "y" for
   * yellow, "k" for black, or "w" for white. If the style string contains 
   * none of these colors, then the default color is used.
   * <p>
   * To specify a line style, the style string may contain one of "-" for 
   * solid lines, "--" for dashed lines, "-." for dotted lines, or "--."
   * for dash-dotted lines. If the style string contains none of these
   * line styles, then no lines are painted.
   * <p>
   * To specify a mark style, the style string may contain one of "." for
   * point, "+" for plus, "x" for cross, "o" for hollow circle", "O" for
   * filled circle, "s" for hollow square, or "S" for filled square. If
   * the style string contains none of these mark styles, then no marks
   * are painted.
   * @param style the style string.
   */
  public void setStyle(String style) {

    // Color.
    if (style.contains("r")) {
      setLineColor(Color.RED);
      setMarkColor(Color.RED);
    } else if (style.contains("g")) {
      setLineColor(Color.GREEN);
      setMarkColor(Color.GREEN);
    } else if (style.contains("b")) {
      setLineColor(Color.BLUE);
      setMarkColor(Color.BLUE);
    } else if (style.contains("c")) {
      setLineColor(Color.CYAN);
      setMarkColor(Color.CYAN);
    } else if (style.contains("m")) {
      setLineColor(Color.MAGENTA);
      setMarkColor(Color.MAGENTA);
    } else if (style.contains("y")) {
      setLineColor(Color.YELLOW);
      setMarkColor(Color.YELLOW);
    } else if (style.contains("k")) {
      setLineColor(Color.BLACK);
      setMarkColor(Color.BLACK);
    } else if (style.contains("w")) {
      setLineColor(Color.WHITE);
      setMarkColor(Color.WHITE);
    } else {
      setLineColor(null);
      setMarkColor(null);
    }

    // Line style.
    if (style.contains("--.")) {
      setLineStyle(Line.DASH_DOT);
    } else if (style.contains("--")) {
      setLineStyle(Line.DASH);
    } else if (style.contains("-.")) {
      setLineStyle(Line.DOT);
    } else if (style.contains("-")) {
      setLineStyle(Line.SOLID);
    } else {
      setLineStyle(Line.NONE);
    }

    // Mark style.
    if (style.contains("+")) {
      setMarkStyle(Mark.PLUS);
    } else if (style.contains("x")) {
      setMarkStyle(Mark.CROSS);
    } else if (style.contains("o")) {
      setMarkStyle(Mark.HOLLOW_CIRCLE);
    } else if (style.contains("O")) {
      setMarkStyle(Mark.FILLED_CIRCLE);
    } else if (style.contains("s")) {
      setMarkStyle(Mark.HOLLOW_SQUARE);
    } else if (style.contains("S")) {
      setMarkStyle(Mark.FILLED_SQUARE);
    } else if (style.contains(".")) {
      int i = style.indexOf(".");
      if (i==0 || style.charAt(i-1)!='-')
        setMarkStyle(Mark.POINT);
    } else {
      setMarkStyle(Mark.NONE);
    }
  }

  /**
   * Sets the line style.
   * The default style is solid.
   * @param style the line style.
   */
  public void setLineStyle(Line style) {
    _lineStyle = style;
    repaint();
  }

  /**
   * Sets the line width.
   * The default width is zero, for the thinnest lines.
   * @param width the line width.
   */
  public void setLineWidth(float width) {
    if (_lineWidth!=width) {
      _lineWidth = width;
      updateBestProjectors();
      repaint();
    }
  }

  /**
   * Sets the line color.
   * The default line color is the tile foreground color. 
   * That default is used if the specified line color is null.
   * @param color the line color; null, for tile foreground color.
   */
  public void setLineColor(Color color) {
    if (!equalColors(_lineColor,color)) {
      _lineColor = color;
      repaint();
    }
  }

  /**
   * Sets the mark style.
   * The default mark style is none, for no marks.
   * @param style the mark style.
   */
  public void setMarkStyle(Mark style) {
    if (_markStyle!=style) {
      _markStyle = style;
      updateBestProjectors();
      repaint();
    }
  }

  /**
   * Sets the mark size.
   * The default mark size is half the tile font size.
   * The default is used if the specified mark size is zero.
   * @param size the mark size.
   */
  public void setMarkSize(float size) {
    if (_markSize!=size) {
      _markSize = size;
      updateBestProjectors();
      repaint();
    }
  }

  /**
   * Sets the mark color.
   * The default mark color is the tile foreground color. 
   * That default is used if the specified mark color is null.
   * @param color the mark color.
   */
  public void setMarkColor(Color color) {
    if (!equalColors(_markColor,color)) {
      _markColor = color;
      repaint();
    }
  }

  /**
   * Sets the format used for text labels.
   * The default format is "%1.4G".
   * @param format the text format.
   */
  public void setTextFormat(String format) {
    _textFormat = format;
    repaint();
  }

  public void paint(Graphics2D g2d) {
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

    Projector hp = getHorizontalProjector();
    Projector vp = getVerticalProjector();
    Transcaler ts = getTranscaler();

    // Font size and line width from graphics context.
    float fontSize = g2d.getFont().getSize2D();
    float lineWidth = 1.0f;
    Stroke stroke = g2d.getStroke();
    if (stroke instanceof BasicStroke) {
      BasicStroke bs = (BasicStroke)stroke;
      lineWidth = bs.getLineWidth();
    }

    // Graphics context for lines.
    Graphics2D gline = null;
    if (_lineStyle!=Line.NONE) {
      gline = (Graphics2D)g2d.create();
      float width = lineWidth;
      if (_lineWidth!=0.0f)
        width *= _lineWidth;
      float[] dash = null;
      if (_lineStyle!=Line.SOLID) {
        float dotLength = 0.5f*width;
        float dashLength = 2.0f*width;
        float gapLength = 2.0f*dotLength+dashLength;
        if (_lineStyle==Line.DASH) {
          dash = new float[]{dashLength,gapLength};
        } else if (_lineStyle==Line.DOT) {
          dash = new float[]{dotLength,gapLength};
        } else if (_lineStyle==Line.DASH_DOT) {
          dash = new float[]{dashLength,gapLength,dotLength,gapLength};
        }
      }
      BasicStroke bs;
      if (dash!=null) {
        int cap = BasicStroke.CAP_ROUND;
        int join = BasicStroke.JOIN_ROUND;
        float miter = 10.0f;
        float phase = 0.0f;
        bs = new BasicStroke(width,cap,join,miter,dash,phase);
      } else {
        bs = new BasicStroke(width);
      }
      gline.setStroke(bs);
      if (_lineColor!=null)
        gline.setColor(_lineColor);
    }

    // Graphics context for marks.
    Graphics2D gmark = null;
    int markSize = round(fontSize/2.0f);
    if (_markStyle!=Mark.NONE) {
      gmark = (Graphics2D)g2d.create();
      if (_markSize>=0.0f)
        markSize = round(_markSize*lineWidth);
      if (_markColor!=null)
        gmark.setColor(_markColor);
      float width = lineWidth;
      if (_lineWidth!=0.0f)
        width *= _lineWidth;
      BasicStroke bs = new BasicStroke(width);
      gmark.setStroke(bs);
    }

    // Graphics context for text labels.
    Graphics2D gtext = null;
    if (_x3.size()>0)
      gtext = (Graphics2D)g2d.create();

    // Arrays for (x,y) coordinates.
    int[] x = new int[_nxmax];
    int[] y = new int[_nxmax];

    // For all plot segments, ...
    for (int is=0; is<_ns; ++is) {

      // Compute (x,y) coordinates.
      int n = _nx.get(is);
      float[] x1 = _x1.get(is);
      float[] x2 = _x2.get(is);
      computeXY(hp,vp,ts,n,x1,x2,x,y);

      // Draw lines between consecutive points.
      if (gline!=null)
        gline.drawPolyline(x,y,n);

      // Draw marks at points.
      if (gmark!=null) {
        if (_markStyle==Mark.POINT) {
          paintPoint(gmark,n,x,y);
        } else if (_markStyle==Mark.PLUS) {
          paintPlus(gmark,markSize,n,x,y);
        } else if (_markStyle==Mark.CROSS) {
          paintCross(gmark,markSize,n,x,y);
        } else if (_markStyle==Mark.FILLED_CIRCLE) {
          paintFilledCircle(gmark,markSize,n,x,y);
        } else if (_markStyle==Mark.HOLLOW_CIRCLE) {
          paintHollowCircle(gmark,markSize,n,x,y);
        } else if (_markStyle==Mark.FILLED_SQUARE) {
          paintFilledSquare(gmark,markSize,n,x,y);
        } else if (_markStyle==Mark.HOLLOW_SQUARE) {
          paintHollowSquare(gmark,markSize,n,x,y);
        }
      }

      // Draw text labels.
      if (gtext!=null) {
        float[] z = _x3.get(is);
        paintLabel(gtext,markSize,n,x,y,z);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  int _ns; // number of segments
  ArrayList<Integer> _nx = new ArrayList<Integer>(); // numbers of (x1,x2,x3)
  ArrayList<float[]> _x1 = new ArrayList<float[]>(); // arrays of x1
  ArrayList<float[]> _x2 = new ArrayList<float[]>(); // arrays of x2
  ArrayList<float[]> _x3 = new ArrayList<float[]>(); // arrays of x3
  int _nxmax; // maximum number of points in a segment
  private Orientation _orientation = Orientation.X1RIGHT_X2UP;
  private Line _lineStyle = Line.SOLID;
  private float _lineWidth = 0.0f;
  private Color _lineColor = null;
  private Mark _markStyle = Mark.NONE;
  private float _markSize = -1.0f;
  private Color _markColor = null;
  private String _textFormat = "%1.4G";

  /**
   * Called when we might new realignment.
   */
  private void updateBestProjectors() {

    // Min and max (x1,x2) values.
    float x1min =  FLT_MAX;
    float x2min =  FLT_MAX;
    float x1max = -FLT_MAX;
    float x2max = -FLT_MAX;
    for (int is=0; is<_ns; ++is) {
      int nx = _nx.get(is);
      float[] x1 = _x1.get(is);
      float[] x2 = _x2.get(is);
      for (int ix=0; ix<nx; ++ix) {
        float x1i = x1[ix];
        float x2i = x2[ix];
        x1min = min(x1min,x1i);
        x2min = min(x2min,x2i);
        x1max = max(x1max,x1i);
        x2max = max(x2max,x2i);
      }
    }

    // Ensure x1min<x1max and x2min<x2max.
    if (x1min==x1max) {
      x1min -= ulp(x1min);
      x1max += ulp(x1max);
    }
    if (x2min==x2max) {
      x2min -= ulp(x2min);
      x2max += ulp(x2max);
    }

    // Assume mark sizes and line widths less than 2% of plot dimensions.
    // The goal is to avoid clipping big marks and wide lines. The problem
    // is that mark sizes and line widths are specified in screen pixels
    // (or points), but margins u0 and u1 are specified in normalized 
    // coordinates, fractions of our tile's width and height. Here, we do 
    // not know those dimensions.
    double u0 = 0.0;
    double u1 = 1.0;
    if (_markStyle!=Mark.NONE || _lineWidth>1.0f) {
      u0 = 0.01;
      u1 = 0.99;
    }

    // Best projectors.
    Projector bhp = null;
    Projector bvp = null;
    if (_orientation==Orientation.X1RIGHT_X2UP) {
      bhp = (x1min<x1max)?new Projector(x1min,x1max,u0,u1):null;
      bvp = (x2min<x2max)?new Projector(x2max,x2min,u0,u1):null;
    } else if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      bhp = (x2min<x2max)?new Projector(x2min,x2max,u0,u1):null;
      bvp = (x1min<x1max)?new Projector(x1min,x1max,u0,u1):null;
    }
    setBestProjectors(bhp,bvp);
  }

  private boolean equalColors(Color ca, Color cb) {
    return (ca==null)?cb==null:ca.equals(cb);
  }

  private void computeXY(
    Projector hp, Projector vp, Transcaler ts,
    int n, float[] x1, float[] x2, int[] x, int[] y) 
  {
    ts = ts.combineWith(hp,vp);
    float[] xv = null;
    float[] yv = null;
    if (_orientation==Orientation.X1RIGHT_X2UP) {
      xv = x1;
      yv = x2;
    } else if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      xv = x2;
      yv = x1;
    }
    for (int i=0; i<n; ++i) {
      x[i] = ts.x(xv[i]);
      y[i] = ts.y(yv[i]);
    }
  }

  /*
  private void paintLines(Graphics2D g2d, int n, int[] x, int[] y) {
    int x1 = x[0];
    int y1 = y[0];
    for (int i=1; i<n; ++i) {
      int x2 = x[i];
      int y2 = y[i];
      g2d.drawLine(x1,y1,x2,y2);
      x1 = x2;
      y1 = y2;
    }
  }
  */
  
  private void paintPoint(Graphics2D g2d, int n, int[] x, int[] y) {
    for (int i=0; i<n; ++i) {
      int xi = x[i];
      int yi = y[i];
      g2d.drawLine(xi,yi,xi,yi);
    }
  }
  
  private void paintPlus(Graphics2D g2d, int s, int n, int[] x, int[] y) {
    int wh = 2*(s/2);
    int xy = wh/2;
    for (int i=0; i<n; ++i) {
      int xi = x[i];
      int yi = y[i];
      g2d.drawLine(xi-xy,yi,xi+xy,yi);
      g2d.drawLine(xi,yi-xy,xi,yi+xy);
    }
  }
  
  private void paintCross(Graphics2D g2d, int s, int n, int[] x, int[] y) {
    int wh = 2*(s/2);
    int xy = wh/2;
    for (int i=0; i<n; ++i) {
      int xi = x[i];
      int yi = y[i];
      g2d.drawLine(xi-xy,yi-xy,xi+xy,yi+xy);
      g2d.drawLine(xi+xy,yi-xy,xi-xy,yi+xy);
    }
  }
  
  private void paintFilledCircle(
    Graphics2D g2d, int s, int n, int[] x, int[] y) 
  {
    int wh = 1+2*(s/2);
    int xy = wh/2;
    for (int i=0; i<n; ++i)
      g2d.fillOval(x[i]-xy,y[i]-xy,wh,wh);
  }
  
  private void paintHollowCircle(
    Graphics2D g2d, int s, int n, int[] x, int[] y) 
  {
    int wh = 1+2*(s/2);
    int xy = wh/2;
    for (int i=0; i<n; ++i)
      g2d.drawOval(x[i]-xy,y[i]-xy,wh-1,wh-1);
  }
  
  private void paintFilledSquare(
    Graphics2D g2d, int s, int n, int[] x, int[] y) 
  {
    int wh = 1+2*(s/2);
    int xy = wh/2;
    for (int i=0; i<n; ++i)
      g2d.fillRect(x[i]-xy,y[i]-xy,wh,wh);
  }
  
  private void paintHollowSquare(
    Graphics2D g2d, int s, int n, int[] x, int[] y) 
  {
    int wh = 1+2*(s/2);
    int xy = wh/2;
    for (int i=0; i<n; ++i)
      g2d.drawRect(x[i]-xy,y[i]-xy,wh-1,wh-1);
  }
  
  private void paintLabel(
    Graphics2D g2d, int s, int n, int[] x, int[] y, float[] z) 
  {
    s /= 2;
    for (int i=0; i<n; ++i) {
      int xi = x[i];
      int yi = y[i];
      g2d.drawString(String.format(_textFormat,z[i]),xi+s,yi-s);
    }
  }
}
