/****************************************************************************
Copyright 2015, Colorado School of Mines.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.mosaic;
 
import java.awt.*;
import java.awt.image.*;
import java.util.*;
 
import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;
 
/**
 * A view of bars.
 * Points (x1,x2) may be specified as arrays x1 and x2 of coordinates. Each
 * pair of arrays x1 and x2 corresponds to one plot segment. Multiple plot
 * segments may be specified by arrays of arrays of x1 and x2 coordinates.
 * <p>
 * For example, to view sampled functions x2 = sin(x1) and x2 = cos(x1),
 * one might construct two plot segments by specifying an array of two
 * arrays of x1 coordinates and a corresponding array of two arrays of
 * x2 coordinates.
 * <p>
 * Note that mark and line attributes are constant for each points view.
 * These attributes do not vary among plot segments. To paint marks and
 * lines with different attributes, construct multiple views.
 * @author Chris Engelsma, Colorado School of Mines
 * @version 2015.10.26
 */
public class BarsView extends TiledView {
 
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
   * The bar alignment relative to the tick.
   * The default alignment is centered.
   */
  public enum Alignment {
    ALIGN_CENTER,
    ALIGN_BEFORE,
    ALIGN_AFTER
  }
 
  /**
   * Constructs a view of bars with specified x2 quantities.
   * The corresponding coordinates x1 are assumed to be 0, 1, 2, ....
   * @param x2 array of x2 coordinates.
   */
  public BarsView(float[] x2) {
    float[] x1 = rampfloat(0.0f,1.0f,x2.length);
    set(x1,x2);
  }
 
  /**
   * Constructs a view of bars for a sampled function x2(x1).
   * @param s1 the sampling of x1 coordinates.
   * @param x2 array of x2 values.
   */
  public BarsView(Sampling s1, float[] x2) {
    set(s1,x2);
  }

  /**
   * Constructs a view of bars for x2.length data sets.
   * @param x2 array of values containing x2.length separate sets.
   */
  public BarsView(float[][] x2) {
    set(null,x2);
  }

  /**
   * Constructs a view of bars with multiple plot segments.
   * @param s1 the sampling of x1 coordinates.
   * @param x2 array of x2 values, containing x2.length plot segments.
   */
  public BarsView(Sampling s1, float[][] x2) {
    Check.argument(s1.getCount()==x2[0].length,"s1 count equals x2 length");
    int n1 = x2[0].length;
    int n2 = x2.length;
    float[][] x1 = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2)
      for (int i1=0; i1<n1; ++i1)
        x1[i2][i1] = (float)s1.getValue(i1);
    set(x1,x2);
  }

  /**
   * Constructs a view of bars with multiple plot segments.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   */
  public BarsView(float[][] x1, float[][] x2) {
    set(x1,x2);
  }
 
  /**
   * Constructs a view of bars with a single plot segment.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * @param x1 array of x1 coordinates.
   * @param x2 array of x2 coordinates.
   */
  public BarsView(float[] x1, float[] x2) {
    set(x1,x2);
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
   * Sets array of (x1,x2) coordinates for a plot segment.
   * The lengths of x1 and x2 must be equal.
   * @param x1 array of x1 values.
   * @param x2 array of x2 coordinates.
   */
  public void set(float[] x1, float[] x2) {
    Check.argument(x1.length==x2.length,"x1.length equals x2.length");
    _ns = 1;
    _nx.clear();
    _x1.clear();
    _x2.clear();
    _nxmax = x1.length;
    _nx.add(x1.length);
    _x1.add(copy(x1));
    _x2.add(copy(x2));
    _colorMaps = new ColorMap[_ns];
    for (int i=0; i<_ns; ++i) 
      _colorMaps[i] = new ColorMap(Color.CYAN);
    _lineColor = new Color[_ns];
    updateBestProjectors();
    repaint();
  }

  /**
   * Sets array of arrays of (x1,x2) coordinates for multiple plot segments.
   * The lengths of the specified arrays x1 and x2 must be equal.
   * @param x1 array of arrays of x1 values.
   * @param x2 array of arrays of x2 coordinates.
   */
  public void set(float[][] x1, float[][] x2) {
    Check.argument(x1.length==x2.length,"x1.length equals x2.length");
    _ns = x1.length;
    _nx.clear();
    _x1.clear();
    _x2.clear();
    _nxmax = 0;
    for (int is=0; is<_ns; ++is) {
      Check.argument(x1[is].length==x2[is].length,
                    "x1[i].length equals x2[i].length");
      _nxmax = max(_nxmax,x1[is].length);
      _nx.add(x1[is].length);
      _x1.add(copy(x1[is]));
      _x2.add(copy(x2[is]));
    }
    _colorMaps = new ColorMap[_ns];
    for (int i=0; i<_ns; ++i) 
      _colorMaps[i] = new ColorMap(Color.CYAN);
    _lineColor = new Color[_ns];
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
    } else if (style.contains("g")) {
      setLineColor(Color.GREEN);
    } else if (style.contains("b")) {
      setLineColor(Color.BLUE);
    } else if (style.contains("c")) {
      setLineColor(Color.CYAN);
    } else if (style.contains("m")) {
      setLineColor(Color.MAGENTA);
    } else if (style.contains("y")) {
      setLineColor(Color.YELLOW);
    } else if (style.contains("k")) {
      setLineColor(Color.BLACK);
    } else if (style.contains("w")) {
      setLineColor(Color.WHITE);
    } else {
      setLineColor(null);
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
 
  }
 
  /**
   * Sets the bar width.
   * The default width will be 1, or fully expanded bars.
   * @param style the bar width in range [0.0 - 1.0]
   */ 
  public void setBarWidth(float width) {
    _barWidth = width;
    repaint();
  }
 
  /**
   * Sets the fill color for all bar sets.
   * The default fill color is the tile background color.
   * @param color the bar color.
   */
  public void setFillColor(Color color) {
    for (int i=0; i<_ns; ++i)
      setFillColor(i,color);
  }

  /**
   * Sets a color model for all bar sets.
   * @param colorModel a color model.
   */
  public void setColorModel(IndexColorModel colorModel) {
    for (int is=0; is<_ns; ++is) 
      setColorModel(is,colorModel);
  }

  /**
   * Sets a color model for a specific bar set.
   * @param i index of a bar set.
   * @param colorModel a color model.
   */
  public void setColorModel(int i, IndexColorModel colorModel) {
    _colorMaps[i].setColorModel(colorModel);
    repaint();
  }

  /**
   * Sets a color map for all bar sets.
   * @param colorMap a color map.
   */
  public void setColorMap(ColorMap colorMap) {
    for (int is=0; is<_ns; ++is)
      setColorMap(is,colorMap);
  }

  /**
   * Sets a color map for a specific bar set.
   * @param i index of a bar set.
   * @param colorModel a color model.
   */
  public void setColorMap(int i, ColorMap colorMap) {
    _colorMaps[i] = colorMap;
    repaint();
  }

  /**
   * Sets the fill color for a set of bars.
   * The default fill color is the tile background color.
   * @param i the index of a bar set.
   * @param color the bar color.
   */
  public void setFillColor(int i, Color color) {
    _colorMaps[i] = new ColorMap(color);
    repaint();
  }
 
  /**
   * Sets the alignment of the bars.
   * The default alignment is centered about the value's tick mark.
   * @param alignment the bar alignment.
   */
  public void setAlignment(Alignment alignment) {
    if (_alignment!=alignment) {
      _alignment = alignment;
      updateBestProjectors();
      repaint();
    }
  }

  /**
   * Sets the stacking behavior of the bars.
   * The default stacking behavior is to plot multiple bars adjacent.
   * @param stack true, if stacking bars; false, otherwise.
   */
  public void setStackBars(boolean stack) {
    _stackingBars = stack;
    updateBestProjectors();
    repaint();
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
   * Sets the line color for all sets of bars.
   * The default line color is the tile foreground color.
   * That default is used if the specified line color is null.
   * @param color the line color; null, for tile foreground color.
   */
  public void setLineColor(Color color) {
    for (int i=0; i<_ns; ++i)
      setLineColor(i,color);
  }
 
  /**
   * Sets the line color for a set of bars.
   * The default line color is the tile foreground color.
   * That default is used if the specified line color is null.
   * @param ibar the index of the set of bars.
   * @param color the line color; null, for tile foreground color.
   */
  public void setLineColor(int ibar, Color color) {
    _lineColor[ibar] = color;
    repaint();
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
    }
 
    // Graphics context for text labels.
    Graphics2D gtext = null;
 
    // Arrays for (x,y) coordinates.
    int[] x = new int[_nxmax];
    int[] y = new int[_nxmax];
 
    Graphics2D gbar = (Graphics2D)g2d.create();
 
    float[] o1 = new float[] { 0.0f, 1.0f };
    float[] o2 = new float[] { 0.0f, 1.0f };
    int[] ox = new int[2];
    int[] oy = new int[2];

    // Convert color map to color arrays
    Color[][] colors = new Color[_ns][_nxmax];
    for (int is=0; is<_ns; ++is) {
      float[] x2 = _x2.get(is);
      for (int ix=0; ix<_nxmax; ++ix) 
        colors[is][ix] = _colorMaps[is].getColor(x2[ix]);
    }
 
    // Get origin in pixels
    computeXY(hp,vp,ts,1,o1,o2,ox,oy);

    float[] bottom = new float[_nxmax];
    int[] prevTop = new int[_nxmax];
    for (int i=0; i<_nxmax; ++i) 
      prevTop[i] = (_orientation==BarsView.Orientation.X1RIGHT_X2UP) 
        ? oy[0]
        : ox[0];

    int shift = 0;
    if (_alignment==Alignment.ALIGN_BEFORE)     shift = -1;
    else if (_alignment==Alignment.ALIGN_AFTER) shift = 1;

    // For all plot segments  ...
    for (int is=0; is<_ns; ++is) {
 
      // Compute (x,y) coordinates.
      int n = _nx.get(is);
      float[] x1 = copy(_x1.get(is));
      float[] x2 = copy(_x2.get(is));
      
      if (_stackingBars) 
        for (int i=0; i<n; ++i) 
          x2[i] += bottom[i];

      computeXY(hp,vp,ts,n,x1,x2,x,y);

      // First draw the filled bar, then the outline
      if (_colorMaps[is]!=null) 
        paintBars(gbar,_barWidth,prevTop,n,x,y,shift,is,true,colors[is]);
      if (_lineColor[is]!=null) 
        gbar.setColor(_lineColor[is]);
      else 
        gbar.setColor(Color.BLACK);
      paintBars(gbar,_barWidth,prevTop,n,x,y,shift,is,false,null);

      if (_stackingBars) {
        if (_orientation==BarsView.Orientation.X1RIGHT_X2UP) {
          bottom = copy(x2);
          prevTop = copy(y);
        } else {
          prevTop = copy(x);
          bottom = copy(x2);
        }
      }
    }
  }
 
  ///////////////////////////////////////////////////////////////////////////
  // private
 
  int _ns; // number of segments
  ArrayList<Integer> _nx = new ArrayList<Integer>(); // numbers of (x1,x2)
  ArrayList<float[]> _x1 = new ArrayList<float[]>(); // arrays of x1
  ArrayList<float[]> _x2 = new ArrayList<float[]>(); // arrays of x2
  int _nxmax; // maximum number of points in a segment

  // View orientation
  private Orientation _orientation = Orientation.X1RIGHT_X2UP;

  // View alignment
  private Alignment _alignment = Alignment.ALIGN_CENTER;

  // Flag for bar arrangement
  private boolean _stackingBars = false;

  // Visual stuff
  private Line _lineStyle = Line.SOLID;
  private float _lineWidth = 0.0f;
  private Color[] _lineColor = null;
  private float _barWidth = 1.0f;
  private String _textFormat = "%1.4G";

  // Color lists with default null
  private ColorMap[] _colorMaps = null;
 
  /**
   * Called when we might new realignment.
   */
  private void updateBestProjectors() {
    int max = Collections.max(_nx);
    float[] sum = new float[max];

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
        x1max = max(x1max,x1i);
        x2min = min(x2min,x2i);
        x2max = max(x2max,x2i);
        sum[ix] += x2[ix];
      }
    }

    if (_stackingBars) {
      x2max = max(0,max(sum));
      x2min = min(0,min(sum));
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
    if (_lineWidth>1.0f) {
      u0 = 0.01;
      u1 = 0.99;
    }
 
    // The two bars on the either end of the bars view will be clipped in
    // half, so we want to mitigate that by extending the bounds out by a half
    // bar width in either direction.
    float w = _barWidth/2.0f;
    if (_alignment==Alignment.ALIGN_CENTER) {
      x1min -= w;
      x1max += w;
    } else if (_alignment==Alignment.ALIGN_BEFORE) {
      x1min-=2*w;
    } else {
      x1max+=2*w;
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
 
  private void paintBars(
    Graphics2D g2d, float wb, int[] bottoms, int n,
    int[] x, int[] y, int s, int adj, boolean fill, Color[] colors) 
  {
    if (_orientation==BarsView.Orientation.X1RIGHT_X2UP)
      paintBarsVertical(g2d,wb,bottoms,n,x,y,s,adj,fill,colors);
    else 
      paintBarsHorizontal(g2d,wb,bottoms,n,x,y,s,adj,fill,colors);
  }

  private void paintBarsHorizontal(
    Graphics2D g2d, float wb, int[] bottoms, int n,
    int[] x, int[] y, int s, int adj, boolean fill, Color[] colors)
  {
    int pw,py;
    float frac = adj/(float)_ns;
    int w = (n>1) ? (int)(wb*(y[1]-y[0])) : 1;
    int hw = w/2;
    int yim1 = y[0]-hw+(s*hw);
    for (int i=0; i<n; ++i) {
      int zero = bottoms[i];
      w = (i>1) ? (int)(wb*(y[i]-y[i-1])) : w;
      hw = w/2;
      int ypos = max(yim1,y[i]-hw);
      int xpos = min(zero,x[i]);
      int h = abs(zero-x[i]);
      if (!_stackingBars) {
        py = ypos + (int)(w*frac);
        pw = w/_ns;
      } else {
        py = ypos;
        pw = w;
      }
      if (fill) {
        g2d.setColor(colors[i]);
        g2d.fillRect(xpos,py,h,pw);
      }
      else
        g2d.drawRect(xpos,py,h,pw);
      yim1 = ypos+w;
    }
  }
  private void paintBarsVertical(
    Graphics2D g2d, float wb, int[] bottoms, int n,
    int[] x, int[] y, int s, int adj, boolean fill, Color[] colors)
  {
    int pw,px;
    float frac = adj/(float)_ns;
    int w = (n>1) ? (int)(wb*(x[1]-x[0])) : 1;
    int hw = w/2;
    int xim1 = x[0]-hw+(s*hw);
    for (int i=0; i<n; ++i) {
      int zero = bottoms[i];
      w = (i>1) ? (int)(wb*(x[i]-x[i-1])) : w;
      hw = w/2;
      int xpos = max(xim1,x[i]-hw); // To circumvent any pixel rounding
      int ypos = min(zero,y[i]);
      int h = abs(zero-y[i]);

      if (!_stackingBars) {
        px = xpos + (int)(w*frac);
        pw = w/_ns;
      } else {
        px = xpos;
        pw = w;
      }
      if (fill) {
        g2d.setColor(colors[i]);
        g2d.fillRect(px,ypos,pw,h);
      }
      else 
        g2d.drawRect(px,ypos,pw,h);
      xim1 = xpos+w;
    }
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
