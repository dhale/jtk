/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static edu.mines.jtk.util.MathPlus.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Check;

/**
 * A view of a sampled function f(x1,x2), displayed with contour lines.
 * <p>
 * <em>NOT YET IMPLEMENTED!</em>
 * <p>
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.02
 */
public class ContoursView extends TiledView {

  /**
   * Orientation of sample axes x1 and x2. For example, the default 
   * orientation X1RIGHT_X2UP corresponds to x1 increasing horizontally 
   * from left to right, and x2 increasing vertically from bottom to top.
   */
  public enum Orientation {
    X1RIGHT_X2UP,
    X1DOWN_X2RIGHT
  }

  /**
   * Constructs a pixels view of the specified sampled function f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   */
  public ContoursView(float[][] f) {
    set(f);
  }

  /**
   * Constructs a pixels view of the specified sampled function f(x1,x2).
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
    Check.argument(Array.isRegular(f),"f is regular");
    Check.argument(s1.getCount()==f[0].length,"s1 consistent with f");
    Check.argument(s2.getCount()==f.length,"s2 consistent with f");
    _s1 = s1;
    _s2 = s2;
    _f = Array.copy(f);
    updateClips();
    updateSampling();
    updateContours();
  }

  /**
   * Sets the orientation of sample axes.
   * @param orientation the orientation.
   */
  public void setOrientation(Orientation orientation) {
    if (_orientation!=orientation) {
      _orientation = orientation;
      updateSampling();
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
   * Sets the clips for this view. Contour lines are not plotted for
   * function values f(x1,x2) outside the specified min-max range.
   * <p>
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param clipMin the sample value corresponding to color map byte index 0.
   * @param clipMax the sample value corresponding to color map byte index 255.
   */
  public void setClips(float clipMin, float clipMax) {
    Check.argument(clipMin<clipMax,"clipMin<clipMax");
    if (_clipMin!=clipMin || _clipMax!=clipMax) {
      _clipMin = clipMin;
      _clipMax = clipMax;
      _usePercentiles = false;
      repaint();
    }
  }

  /**
   * Gets the minimum clip value.
   * @return the minimum clip value.
   */
  public float getClipMin() {
    return _clipMin;
  }

  /**
   * Gets the maximum clip value.
   * @return the maximum clip value.
   */
  public float getClipMax() {
    return _clipMax;
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
    Check.argument(0.0f<=percMin,"0<=percMin");
    Check.argument(percMin<percMax,"percMin<percMax");
    Check.argument(percMax<=100.0f,"percMax<=100");
    if (_percMin!=percMin || _percMax!=percMax) {
      _percMin = percMin;
      _percMax = percMax;
      _usePercentiles = true;
      updateClips();
      repaint();
    }
  }

  /**
   * Gets the minimum percentile.
   * @return the minimum percentile.
   */
  public float getPercentileMin() {
    return _percMin;
  }

  /**
   * Gets the maximum percentile.
   * @return the maximum percentile.
   */
  public float getPercentileMax() {
    return _percMax;
  }

  public void paint(Graphics2D g2d) {

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

  // The sampled floats.
  private Sampling _s1; // sampling of 1st dimension
  private Sampling _s2; // sampling of 2nd dimension
  private float[][] _f; // copy of array of floats

  // View orientation.
  private Orientation _orientation = Orientation.X1RIGHT_X2UP;

  // Clips and percentiles.
  private float _clipMin; // mapped to color map byte index 0
  private float _clipMax; // mapped to color map byte index 255
  private float _percMin = 0.0f; // may be used to compute _clipMin
  private float _percMax = 100.0f; // may be used to compute _clipMax
  private boolean _usePercentiles = true; // true, if using percentiles

  // Sampling of the function f(x1,x2) in the pixel (x,y) coordinate system. 
  private boolean _transposed;
  private boolean _xflipped;
  private boolean _yflipped;
  private int _nx;
  private double _dx;
  private double _fx;
  private int _ny;
  private double _dy;
  private double _fy;

  // Contour sampling and corresponding list of contours.
  private Sampling _cs;
  private ArrayList<Contour> _cl;

  /**
   * If using percentiles, computes corresponding clip values.
   */
  private void updateClips() {
    if (_usePercentiles) {
      float[] a = (_percMin!=0.0f || _percMax!=0.0f)?Array.flatten(_f):null;
      int n = (a!=null)?a.length:0;
      int kmin = (int)rint(_percMin*0.01*(n-1));
      if (kmin<=0) {
        _clipMin = Array.min(_f);
      } else {
        Array.quickPartialSort(kmin,a);
        _clipMin = a[kmin];
      }
      int kmax = (int)rint(_percMax*0.01*(n-1));
      if (kmax>=n-1) {
        _clipMax = Array.max(_f);
      } else {
        Array.quickPartialSort(kmax,a);
        _clipMax = a[kmax];
      }
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
  private void updateSampling() {
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

    // Margins in normalized coordinates.
    double uxMargin = (_nx>1)?0.5/_nx:0.0;
    double uyMargin = (_ny>1)?0.5/_ny:0.0;

    // (ux0,uy0) = normalized coordinates for (left,top) of view.
    // (ux1,uy1) = normalized coordinates for (right,bottom) of view.
    double ux0 = uxMargin;
    double uy0 = uyMargin;
    double ux1 = 1.0-uxMargin;
    double uy1 = 1.0-uyMargin;

    // Best projectors.
    Projector bhp = new Projector(x0,x1,ux0,ux1);
    Projector bvp = new Projector(y0,y1,uy0,uy1);
    setBestProjectors(bhp,bvp);
  }

  private void updateContours() {
    int nc = _cs.getCount();
    _cl = new ArrayList<Contour>();
    for (int ic=0; ic<nc; ++ic) {
      float fc = (float)_cs.getValue(ic);
      Contour c = makeContour(fc,_s1,_s2,_f);
      _cl.add(c);
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
      float d = delta(fc,f[i2][i1],f[i2][i1+1]);
      x1.add(f1+(i1+d)*d1);
      x2.add(f2+(i2+1)*d2);
      clrs(i1,++i2,flags);
      return (i2<n2-1)?index+n1:-1;
    } 
    
    // Else if exiting east, ...
    else if (wset(i1+1,i2,flags)) {
      float d = delta(fc,f[i2][i1],f[i2+1][i1]);
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
