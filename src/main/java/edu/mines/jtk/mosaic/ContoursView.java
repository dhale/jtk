/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.Iterator;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.awt.ColorMapListener;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.AxisTics;
import edu.mines.jtk.util.Check;
import edu.mines.jtk.util.Clips;
import edu.mines.jtk.awt.ColorMapped;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * A view of a sampled function f(x1,x2), displayed with contour lines.
 * <p>
 * @author Dave Hale and Chris Engelsma, Colorado School of Mines
 * @version 2009.07.06
 */
public class ContoursView extends TiledView implements ColorMapped {

  /**
   * Orientation of sample axes x1 and x2. For example, the default 
   * orientation X1RIGHT_X2UP corresponds to x1 increasing horizontally 
   * from left to right, and x2 increasing vertically from bottom to top.
   */
  public enum Orientation {
    X1RIGHT_X2UP,
    X1DOWN_X2RIGHT
  }

  public enum Line {
    DEFAULT,
    SOLID,
    DASH,
    DOT,
    DASH_DOT,
  }

  /**
   * Constructs a contours view of the specified sampled function f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   */
  public ContoursView(float[][] f) {
    set(f);
  }

  /**
   * Constructs a contours view of the specified sampled function f(x1,x2).
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where
   *  n1 and n2 denote the number of samples in s1 and s2, respectively.
   */
  public ContoursView(Sampling s1, Sampling s2, float[][] f) {
    set(s1,s2,f);
  }

  /**
   * Sets the sampled function f(x1,x2) for this view.
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   */
  public void set(float[][] f) {
    set(new Sampling(f[0].length),new Sampling(f.length),f);
  }

  /**
   * Sets the sampled function f(x1,x2) for this view.
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where
   *  n1 and n2 denote the number of samples in s1 and s2, respectively.
   */
  public void set(Sampling s1, Sampling s2, float[][] f) {
    Check.argument(s1.isUniform(),"s1 is uniform");
    Check.argument(s2.isUniform(),"s2 is uniform");
    Check.argument(isRegular(f),"f is regular");
    Check.argument(s1.getCount()==f[0].length,"s1 consistent with f"); 
    Check.argument(s2.getCount()==f.length,"s2 consistent with f");
    _s1 = s1;
    _s2 = s2;
    _f = copy(f);
    _clips = new Clips(f);
    updateArraySampling();
    _cs = null;
    _cl = null;
  }

 

  /**
   * Sets the orientation of sample axes.
   * @param orientation the orientation.
   */
  public void setOrientation(Orientation orientation) {
    if (_orientation!=orientation) {
      _orientation = orientation;
      updateArraySampling();
      repaint();
    }
  }

  /**
   * Gets the orientation of sample axes.
   * @return the orientation.
   */
  public Orientation getOrientation() {
    return _orientation;
  }

  /**
   * Sets the contour line style.
   * The default style is solid.
   * @param style the line style.
   */
  public void setLineStyle(Line style) {
    if (_lineStyle!=style) {
      _lineStyle = style;
      repaint();
    }
  }

  /**
   * Sets the contour line style for negative-valued contours.
   * By default, all contours share the same line style.
   * @param style the line style.
   */
  public void setLineStyleNegative(Line style) {
    if (_lineStyleNegative!=style) {
      _lineStyleNegative = style;
      repaint();
    }
  }

  /**
   * Sets the contour line width.
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
   * Sets the color of each contour line to the specified color.
   * @param color the contour line color.
   */
  public void setLineColor(Color color) {
    _colorMap.setColorModel(color);
    repaint();
  }

  /**
   * Sets the color model used to map contour values to colors.
   * If set, then byte 0 of the color model corresponds to the minimum 
   * clip value, and byte 255 corresponds to the maximum clip value.
   * If not set, then all contour lines have the same color.
   * @param colorModel the color model.
   */
  public void setColorModel(IndexColorModel colorModel) {
    _colorMap.setColorModel(colorModel);
    repaint();
  }

  public ColorMap getColorMap() {
    return _colorMap;
  }

  /**
   * Enables or disables automatically computed readable contour values. 
   * Here, readable values are multiples of 1, 2, and 5 times some 
   * power of ten. If enabled, then any specified number of contours
   * serves as an upper bound on the number of contour values.
   * Readable contour values are the default.
   * @param readableContours true, for readable contours; false, otherwise.
   */
  public void setReadableContours(boolean readableContours) {
    if (_readableContours!=readableContours) {
      _readableContours = readableContours;
      _cs = null;
      _cl = null;
      repaint();
    }
  }

  /**
   * Sets the number of contour values. 
   * If readable contours are enabled, then this number is an upper bound,
   * and the actual number of contours may be less than this number.
   * Otherwise, if readable contours are disabled, then contour values 
   * will be evenly spaced between, but not including, the minimum and 
   * maximum clip values. The default number of contour values is 25.
   * @param n the number of contour values.
   */
  public void setContours(int n) {
    _nc = n;
    _cs = null;
    _cl = null;
    repaint();
  }

  /**
   * Sets the contour values to those in the specified array.
   * If this method is called, then clips (or percentiles) are not used 
   * to determine contour values, and readable contours are disabled.
   * @param c the array of contour values.
   */
  public void setContours(float[] c) {
    double[] cd = new double[c.length];
    for (int i=0; i<c.length; ++i) 
      cd[i] = (double)c[i];
    setContours(new Sampling(cd));
  }

  /**
   * Sets the contour values to the specified sampling.
   * If this method is called, then clips (or percentiles) are not used 
   * to determine contour values, and readable contours are disabled.
   * @param cs the contour sampling.
   */
  public void setContours(Sampling cs) {
    _readableContours = false;
    _cs = cs;
    _cl = null;
    repaint();
  }

  /**
   * Gets the contour values.  
   * @return array of contour values.
   */
  public float[] getContours() {
    updateContourSampling();
    float[] values = new float[_cs.getCount()];
    for (int n=0; n<values.length; n++)
      values[n] = _cl.get(n).fc;
    return values;
  }

  /**
   * Sets the clips for this view. These values limit the range used
   * to determine contour values. Function values f(x1,x2) less than
   * clipMin and greater than clipMax are ignored.
   * <p>
   * Calling this method disables the computation of clips from percentiles.
   * Previous clip values will be forgotten.
   * @param clipMin the lower bound on contour values.
   * @param clipMax the upper bound on contour values.
   */
  public void setClips(float clipMin, float clipMax) {
    _clips.setClips(clipMin,clipMax);
    _cs = null;
    _cl = null;
    repaint();
  }

  /**
   * Gets the minimum clip value.
   * @return the minimum clip value.
   */
  public float getClipMin() {
    return _clips.getClipMin();
  }

  /**
   * Gets the maximum clip value.
   * @return the maximum clip value.
   */
  public float getClipMax() {
    return _clips.getClipMax();
  }

  /**
   * Sets the percentiles used to compute clips for this view. The default 
   * percentiles are 0 and 100, which correspond to the minimum and maximum 
   * values of the sampled function f(x1,x2).
   * <p>
   * Calling this method enables the computation of clips from percentiles.
   * Any clip values specified or computed previously will be forgotten.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   */
  public void setPercentiles(float percMin, float percMax) {
    _clips.setPercentiles(percMin,percMax);
    _cs = null;
    _cl = null;
    repaint();
  }

  /**
   * Gets the minimum percentile.
   * @return the minimum percentile.
   */
  public float getPercentileMin() {
    return _clips.getPercentileMin();
  }

  /**
   * Gets the maximum percentile.
   * @return the maximum percentile.
   */
  public float getPercentileMax() {
    return _clips.getPercentileMax();
  }

  /**
   * Adds the specified color map listener.
   * @param cml the listener.
   */
  public void addColorMapListener(ColorMapListener cml) {
    _colorMap.addListener(cml);
  }

  /**
   * Removes the specified color map listener.
   * @param cml the listener.
   */
  public void removeColorMapListener(ColorMapListener cml) {
    _colorMap.removeListener(cml);
  }

  public void paint(Graphics2D g2d) {

    updateContourSampling();
    updateContours();

    // Projectors and transcaler.
    Projector hp = getHorizontalProjector();
    Projector vp = getVerticalProjector();
    Transcaler ts = getTranscaler();

    // Compute the view rectangle in pixels (device coordinates). Each sample 
    // contributes a rectangle of pixels that corresponds to an area _dx*_dy 
    // in sampling coordinates. The view rectangle for all _nx*_ny samples is 
    // then _nx*_dx*_ny*_dy. Projectors and transcaler map this rectangle to
    // pixel coordinates.
    double vx0 = _fx-0.5*_dx;
    double vx1 = _fx+_dx*(_nx-0.5);
    double vy0 = _fy-0.5*_dy;
    double vy1 = _fy+_dy*(_ny-0.5);
    double ux0 = hp.u(vx0);
    double ux1 = hp.u(vx1);
    double uy0 = vp.u(vy0);
    double uy1 = vp.u(vy1);
    double uxmin = min(ux0,ux1);
    double uxmax = max(ux0,ux1);
    double uymin = min(uy0,uy1);
    double uymax = max(uy0,uy1);
    int xd = ts.x(uxmin);
    int yd = ts.y(uymin);
    int wd = ts.width(uxmax-uxmin);
    int hd = ts.height(uymax-uymin);
    Rectangle viewRect = new Rectangle(xd,yd,wd,hd);

    // Restrict drawing to intersection of view and clip rectangles.
    Rectangle clipRect = g2d.getClipBounds();
    if (clipRect==null)
      clipRect = viewRect;
    clipRect = clipRect.intersection(viewRect);
    if (clipRect.isEmpty())
      return;
    
    /* Drawing not yet restricted to clip rectangle.
    // Sample coordinates corresponding to the clip rectangle.
    int xc = clipRect.x;
    int yc = clipRect.y;
    int wc = clipRect.width;
    int hc = clipRect.height;
    double xu = ts.x(xc);
    double yu = ts.y(yc);
    double wu = ts.width(wc);
    double hu = ts.height(hc);
    double x0 = hp.v(xu);
    double y0 = vp.v(yu);
    double x1 = hp.v(xu+wu);
    double y1 = vp.v(yu+hu);

    // Image pixel sampling.
    int nx = wc;
    int ny = hc;
    double dx = (x1-x0)/max(1,nx-1);
    double dy = (y1-y0)/max(1,ny-1);
    double fx = x0;
    double fy = y0;
    */

    float lineWidth = 1.0f;

    // Graphic context for contour lines
    Graphics2D gline;
    gline = (Graphics2D)g2d.create();
    float[] dash = null;
    Line lineStyle = _lineStyle;
    if (lineStyle==Line.DEFAULT)
      lineStyle = Line.SOLID;
    if (lineStyle!=Line.SOLID) {
      float dotLength = lineWidth;
      float dashLength = 5.0f*lineWidth;
      float gapLength = 5.0f*lineWidth;
      if (lineStyle==Line.DASH) {
        dash = new float[]{dashLength,gapLength};
      } else if (lineStyle==Line.DOT) {
        dash = new float[]{dotLength,gapLength};
      } else if (lineStyle==Line.DASH_DOT) {
        dash = new float[]{dashLength,gapLength,dotLength,gapLength};
      }
    }
    float width = lineWidth;
    if (_lineWidth!=0.0f)
      width *= _lineWidth;
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

    BasicStroke bsneg = null;
    if (_lineStyleNegative!=Line.DEFAULT) {
      dash = null;
      if (_lineStyleNegative!=Line.SOLID) {
        float dotLength = lineWidth;
        float dashLength = 5.0f*lineWidth;
        float gapLength = 5.0f*lineWidth;
        if (_lineStyleNegative==Line.DASH) {
          dash = new float[]{dashLength,gapLength};
        } else if (_lineStyleNegative==Line.DOT) {
          dash = new float[]{dotLength,gapLength};
        } else if (_lineStyleNegative==Line.DASH_DOT) {
          dash = new float[]{dashLength,gapLength,dotLength,gapLength};
        }
      }
      width = lineWidth;
      if (_lineWidth!=0.0f)
        width *= _lineWidth;
      if (dash!=null) {
        int cap = BasicStroke.CAP_ROUND;
        int join = BasicStroke.JOIN_ROUND;
        float miter = 10.0f;
        float phase = 0.0f;
        bsneg = new BasicStroke(width,cap,join,miter,dash,phase);
      } else {
        bsneg = new BasicStroke(width);
      }
    }
    
    IndexColorModel cm = _colorMap.getColorModel();

    for (int is=0; is<_cs.getCount(); ++is) {
      float fc = _cl.get(is).fc;
      ArrayList<float[]> cx1 = _cl.get(is).x1;
      ArrayList<float[]> cx2 = _cl.get(is).x2;
      Iterator<float[]> it1 = cx1.iterator();
      Iterator<float[]> it2 = cx2.iterator();
      // If assigning a ColorMap to the contours, then assign the values
      // of the contours to their ColorMap equivalents within a 0-255 range.
      if (cm!=null) {
        //Normalize the color components for 0 - 255
        int index;
        if (fc<_clipMin) index = 0;
        else if (fc>_clipMax) index = 255;
        else index = (int)((fc-_clipMin)/(_clipMax-_clipMin)*255f);
        gline.setColor(new Color(cm.getRGB(index)));
      }
      // If we're using dashed negatives, then let's change the 
      // line stroke depending on whether the value is negative
      // or positive.
      if (_lineStyleNegative!=Line.DEFAULT) {
        if (fc<0.0f) gline.setStroke(bsneg);
        if (fc>=0.0f) gline.setStroke(bs);
      }

      while (it1.hasNext()) {
        float[] xc1 = it1.next();
        float[] xc2 = it2.next();
        int n = xc1.length;
        int[] xcon = new int[xc1.length];
        int[] ycon = new int[xc2.length];
        computeXY(hp,vp,ts,n,xc1,xc2,xcon,ycon);
        if (gline!=null) 
          gline.drawPolyline(xcon,ycon,n);
      }
    }
  }
  
  ///////////////////////////////////////////////////////////////////////////
  // private

  private static class Contour {
    float fc; // contoured function value
    int ns = 0; // number of segments
    ArrayList<float[]> x1 = new ArrayList<float[]>(); // x1[] per segment
    ArrayList<float[]> x2 = new ArrayList<float[]>(); // x2[] per segment
    Contour (float fc) {
      this.fc = fc;
    }
    void append(FloatList x1List, FloatList x2List) {
      ++this.ns;
      this.x1.add(x1List.trim());
      this.x2.add(x2List.trim());
    }
  }

  private static class FloatList {
    public int n;
    public float[] a = new float[64];
    public void add(double f) {
      if (n==a.length) {
        float[] t = new float[2*a.length];
        for (int i=0; i<n; ++i)
          t[i] = a[i];
        a = t;
      }
      a[n++] = (float)f;
    }
    public float[] trim() {
      float[] t = new float[n];
      for (int i=0; i<n; ++i)
        t[i] = a[i];
      return t;
    }
  }

  // Contour line attributes.
  private float _lineWidth = 0.0f;
  private Line _lineStyle = Line.SOLID; 
  private Line _lineStyleNegative = Line.DEFAULT;
  private ColorMap _colorMap = new ColorMap(ColorMap.JET);

  // The sampled floats.
  private Sampling _s1; // sampling of 1st dimension
  private Sampling _s2; // sampling of 2nd dimension
  private float[][] _f; // copy of array of floats

  // View orientation
  private Orientation _orientation = Orientation.X1RIGHT_X2UP;

  // Sampling of the function f(x1,x2) in the pixel (x,y) coordinate system. 
  private boolean _transposed; // true, if (x,y) <=> (x2,x1)
  private boolean _xflipped; // true, if axis decreases with increasing x
  private boolean _yflipped; // true, if axis decreases with increasing y
  private int _nx; // number of samples for x axis
  private double _dx; // sampling interval for x axis
  private double _fx; // first sample for x axis
  private int _ny; // number of samples for y axis
  private double _dy; // sampling interval for y axis
  private double _fy; // first sample for y axis

  // Clipping
  private Clips _clips;
  private float _clipMin;
  private float _clipMax;

  // Contour sampling and list of contours.
  private int _nc = 25; // number of contours; maybe less if readable contours
  private boolean _readableContours = true; // true, for readable contour vals
  private Sampling _cs; // contour sampling
  private ArrayList<Contour> _cl; // list of contours
  
  /**
   * Update the clips if necessary.
   */
  private void updateClips() {
    float clipMin = _clips.getClipMin();
    float clipMax = _clips.getClipMax();
    if (_clipMin!=clipMin || _clipMax!=clipMax) {
      _clipMin = clipMin;
      _clipMax = clipMax;
      _colorMap.setValueRange(clipMin,clipMax);
    }
  }

  /**
   * Updates the number of contours. This is based on the contour sampling,
   * which is determined by the clipping values of the data range.  Since
   * the defaults are 0 and 100, the entire range of data will be used
   * to determine the contour values.  If the clips are set, the contours
   * will be set to the new range.
   */
  private void updateContourSampling() {
    if (_cs==null) {
      updateClips();
      int nc;
      double dc,fc;
      if (_readableContours) {
        AxisTics at = new AxisTics(_clipMin,_clipMax,_nc);
        nc = at.getCountMajor();
        dc = at.getDeltaMajor();
        fc = at.getFirstMajor();
      } else {
        nc = _nc;
        dc = (_clipMax-_clipMin)/(nc+1);
        fc = _clipMin;
      }
      double[] cstep = new double[nc];
      cstep[0] = fc+dc;
      int count = 1;
      while (count<nc) {
        cstep[count] = cstep[count-1]+dc;
        count++;
      }
      _cs = new Sampling(cstep);
    }
  }

  /**
   * Updates the (x,y) sampling for this view. This sampling corresponds to
   * the tile (x,y) coordinate system, and it depends on the contours view 
   * orientation. If this sampling is transposed, then x1 corresponds to y 
   * and x2 corresponds to x; otherwise, x1 corresponds to x and x2 
   * corresponds to y. In either case, the coordinates (_fx,_fy) correspond
   * to the sampled function value _f[0][0].
   */
  private void updateArraySampling() {
    int n1 = _s1.getCount();
    int n2 = _s2.getCount();
    double d1 = _s1.getDelta();
    double d2 = _s2.getDelta();
    double f1 = _s1.getFirst();
    double f2 = _s2.getFirst();
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      _transposed = true;
      _xflipped = false;
      _yflipped = false;
      _nx = n2;
      _dx = d2;
      _fx = f2;
      _ny = n1;
      _dy = d1;
      _fy = f1;
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      _transposed = false;
      _xflipped = false;
      _yflipped = true;
      _nx = n1;
      _dx = d1;
      _fx = f1;
      _ny = n2;
      _dy = d2;
      _fy = f2;
    }
    updateBestProjectors();
  }

  /**
   * Called when we might need realignment.
   */
  private void updateBestProjectors() {

    // (x0,y0) = sample coordinates for (left,top) of view.
    // (x1,y1) = sample coordinates for (right,bottom) of view.
    double x0 = _fx;
    double x1 = _fx+_dx*(_nx-1);
    double y0 = _fy;
    double y1 = _fy+_dy*(_ny-1);
    if (_xflipped) {
      double xt = y0;
      x0 = x1;
      x1 = xt;
    }
    if (_yflipped) {
      double yt = y0;
      y0 = y1;
      y1 = yt;
    }

    // Special handling for x0 == x1 or y0 == y1.
    if (x0==x1) {
      double tiny = max(0.5,FLT_EPSILON*abs(x0));
      x0 -= tiny;
      x1 += tiny;
    }
    if (y0==y1) {
      double tiny = max(0.5,FLT_EPSILON*abs(y0));
      y0 -= tiny;
      y1 += tiny;
    }

    // Assume mark sizes and line widths less than 2% of plot dimensions.
    // The goal is to avoid clipping wide lines. The problem is that line 
    // widths are specified in screen pixels (or points), but margins u0 
    // and u1 are specified in normalized coordinates, fractions of our 
    // tile's width and height. Here, we do not know those dimensions.
    double u0 = 0.0;
    double u1 = 1.0;
    if (_lineWidth>1.0f) {
      u0 = 0.01;
      u1 = 0.99;
    }

    // Best projectors.
    Projector bhp = new Projector(x0,x1,u0,u1);
    Projector bvp = new Projector(y0,y1,u0,u1);
    setBestProjectors(bhp,bvp);
  }

  /**
   * Updates the contours for this view.
   */
  private void updateContours() {
    if (_cl==null) {
      int nc = _cs.getCount();
      _cl = new ArrayList<Contour>();
      for (int ic=0; ic<nc; ++ic) {
        float fc = (float)_cs.getValue(ic);
        Contour c = makeContour(fc,_s1,_s2,_f);
        _cl.add(c);
      }
    }
  }

  /**
   * Computes coordinates for a contour segment.
   */
  private void computeXY(
    Projector hp, Projector vp, Transcaler ts,
    int n, float[] x1, float[] x2, int[] x, int[] y) 
  {
    ts = ts.combineWith(hp,vp);
    float[] xv,yv;
    if (_transposed) {
      xv = x2;
      yv = x1;
    } else {
      xv = x1;
      yv = x2;
    }
    for (int i=0; i<n; ++i) {
      x[i] = ts.x(xv[i]);
      y[i] = ts.y(yv[i]);
    }
  }


  ///////////////////////////////////////////////////////////////////////////
  // Comments in functions below refer to cells with indices (i1,i2). Each 
  // cell has north, east, south, and west boundaries, defined as shown here:
  // 
  //              north
  //  (i1,i2+1)	--------- (i1+1,i2+1)
  //            | cell  |
  //      west  | i1,i2	| east
  //            |       |
  //    (i1,i2) --------- (i1+1,i2)
  //              south
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Returns a new contour for one specified function value fc.
   */
  private static Contour makeContour(float fc, 
    Sampling s1, Sampling s2, float[][] f)
  {
    int n1 = s1.getCount();
    double d1 = s1.getDelta();
    double f1 = s1.getFirst();
    int n2 = s2.getCount();
    double d2 = s2.getDelta();
    double f2 = s2.getFirst();
    int n1m1 = n1-1;
    int n2m1 = n2-1;
    int i1,i2,is,i;
    
    // Mark and count intersections with west and south edges of cells.
    byte[][] flags = new byte[n2][n1];
    int ni = 0;
    for (i2=0; i2<n2; ++i2) {
      for (i1=0; i1<n1; ++i1) {
        if (i2<n2m1 && between(fc,f[i2][i1],f[i2+1][i1])) {
          setw(i1,i2,flags);
          ++ni;
        }
        if (i1<n1m1 && between(fc,f[i2][i1],f[i2][i1+1])) {
          sets(i1,i2,flags);
          ++ni;
        }
      }
    }

    // Construct new contour.
    Contour c = new Contour(fc);

    // Append contour segments intersecting north boundary of grid.
    i2 = n2m1;
    for (i1=0,is=i1+i2*n1; i1<n1m1 && ni>0; ++i1,is+=1) {
      if (sset(i1,i2,flags)) {
        float d = delta(fc,f[i2][i1],f[i2][i1+1]);
        FloatList x1 = new FloatList();
        FloatList x2 = new FloatList();
        x1.add(f1+(i1+d)*d1);
        x2.add(f2+(i2  )*d2);
        clrs(i1,i2,flags);
        for (i=is-n1; i>=0; i=connect(i,fc,n1,d1,f1,n2,d2,f2,f,flags,x1,x2))
          --ni;
        c.append(x1,x2);
      }
    }

    // Append contour segments intersecting east boundary of grid.
    i1 = n1m1;
    for (i2=0,is=i1+i2*n1; i2<n2m1 && ni>0; ++i2,is+=n1) {
      if (wset(i1,i2,flags)) {
        float d = delta(fc,f[i2][i1],f[i2+1][i1]);
        FloatList x1 = new FloatList();
        FloatList x2 = new FloatList();
        x1.add(f1+(i1  )*d1);
        x2.add(f2+(i2+d)*d2);
        clrw(i1,i2,flags);
        for (i=is-1; i>=0; i=connect(i,fc,n1,d1,f1,n2,d2,f2,f,flags,x1,x2))
          --ni;
        c.append(x1,x2);
      }
    }

    // Append contour segments intersecting south boundary of grid.
    i2 = 0;
    for (i1=0,is=i1+i2*n1; i1<n1m1 && ni>0; ++i1,is+=1) {
      if (sset(i1,i2,flags)) {
        float d = delta(fc,f[i2][i1],f[i2][i1+1]);
        FloatList x1 = new FloatList();
        FloatList x2 = new FloatList();
        x1.add(f1+(i1+d)*d1);
        x2.add(f2+(i2  )*d2);
        clrs(i1,i2,flags);
        for (i=is; i>=0; i=connect(i,fc,n1,d1,f1,n2,d2,f2,f,flags,x1,x2))
          --ni;
        c.append(x1,x2);
      }
    }

    // Append contour segments intersecting west boundary of grid.
    i1 = 0;
    for (i2=0,is=i1+i2*n1; i2<n2m1 && ni>0; ++i2,is+=n1) {
      if (wset(i1,i2,flags)) {
        float d = delta(fc,f[i2][i1],f[i2+1][i1]);
        FloatList x1 = new FloatList();
        FloatList x2 = new FloatList();
        x1.add(f1+(i1  )*d1);
        x2.add(f2+(i2+d)*d2);
        clrw(i1,i2,flags);
        for (i=is; i>=0; i=connect(i,fc,n1,d1,f1,n2,d2,f2,f,flags,x1,x2))
          --ni;
        c.append(x1,x2);
      }
    }

    // Append contour segments intersecting interior cells.
    for (i2=1; i2<n2m1 && ni>0; ++i2) {
      for (i1=0,is=i1+i2*n1; i1<n1m1 && ni>0; ++i1,++is) {
        if (sset(i1,i2,flags)) {
          float d = delta(fc,f[i2][i1],f[i2][i1+1]);
          FloatList x1 = new FloatList();
          FloatList x2 = new FloatList();
          x1.add(f1+(i1+d)*d1);
          x2.add(f2+(i2  )*d2);
          clrs(i1,i2,flags);
          for (i=is; i>=0; i=connect(i,fc,n1,d1,f1,n2,d2,f2,f,flags,x1,x2))
            --ni;
          // Close the contours...
          x1.add(x1.a[0]);
          x2.add(x2.a[0]);
          c.append(x1,x2);
        } 
      }
    }

    return c;
  }

  /**
   * Connects two intersections of a contour for a cell (i1,i2), if possible.
   * When called, the index = i1+i2*n1 points to a cell (i1,i2) for which
   * one intersection has already been found and cleared. This method looks
   * for another intersection. If another intersection is found, this method 
   * clears it, appends the intersection coordinates (x,y) to the specified 
   * lists, and returns a modified index that indicates an adjacent cell. If 
   * another intersection is not found, or if the found intersection is a grid 
   * boundary, then this method returns -1.
   */
  private static int connect(
    int index, float fc,
    int n1, double d1, double f1, int n2, double d2, double f2, float[][] f, 
    byte[][] flags, FloatList x1, FloatList x2)
  {
    int i1 = index%n1;
    int i2 = index/n1;

    // If exiting north, ...
    if (sset(i1,i2+1,flags)) {
      float d = delta(fc,f[i2+1][i1],f[i2+1][i1+1]);
      x1.add(f1+(i1+d)*d1);
      x2.add(f2+(i2+1)*d2);
      clrs(i1,++i2,flags);
      return (i2<n2-1)?index+n1:-1;
    } 
    
    // Else if exiting east, ...
    else if (wset(i1+1,i2,flags)) {
      float d = delta(fc,f[i2][i1+1],f[i2+1][i1+1]);
      x1.add(f1+(i1+1)*d1);
      x2.add(f2+(i2+d)*d2);
      clrw(++i1,i2,flags);
      return (i1<n1-1)?index+1:-1;
    }

    // Else if exiting south, ...
    else if (sset(i1,i2,flags)) {
      float d = delta(fc,f[i2][i1],f[i2][i1+1]);
      x1.add(f1+(i1+d)*d1);
      x2.add(f2+(i2  )*d2);
      clrs(i1,i2,flags);
      return (i2>0)?index-n1:-1;
    } 
    
    // Else if exiting west, ...
    else if (wset(i1,i2,flags)) {
      float d = delta(fc,f[i2][i1],f[i2+1][i1]);
      x1.add(f1+(i1  )*d1);
      x2.add(f2+(i2+d)*d2);
      clrw(i1,i2,flags);
      return (i1>0)?index-1:-1;
    }
    
    // Else if no intersection exists, ...
    else {
      return -1;
    }
  }

  // Contour exit-directions.
  private static final byte WEST = 1;
  private static final byte SOUTH = 2;
  private static final byte NOT_WEST = ~WEST;
  private static final byte NOT_SOUTH = ~SOUTH;

  private static void sets(int i1, int i2, byte[][] flags) {
    flags[i2][i1] |= SOUTH;
  }
   
  private static void setw(int i1, int i2, byte[][] flags) {
    flags[i2][i1] |= WEST;
  }
 
  private static void clrs(int i1, int i2, byte[][] flags) {
    flags[i2][i1] &= NOT_SOUTH;
  }
 
  private static void clrw(int i1, int i2, byte[][] flags) {
    flags[i2][i1] &= NOT_WEST;
  }

  private static boolean sset(int i1, int i2, byte[][] flags) {
    return (flags[i2][i1]&SOUTH)!=0;
  }

  private static boolean wset(int i1, int i2, byte[][] flags) {
    return (flags[i2][i1]&WEST)!=0;
  }

  private static boolean between(float fc, float f1, float f2) {
    return (f1<=f2) ? f1<=fc && fc<f2 : f2<=fc && fc<f1;
  }

  private static float delta(float fc, float f1, float f2) {
    return (f1!=f2)?(fc-f1)/(f2-f1):1.0f;
  }
}
