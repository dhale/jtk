/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A view of a sampled function f(x1,x2), displayed as a 2-D array of pixels.
 * Sample values are converted to pixel colors in two steps. In the first
 * step, sample values are converted to unsigned bytes in the range [0,255].
 * In the second, step, these bytes are converted to pixel colors, through
 * a specified color model.
 * <p>
 * The first step is a mapping from sample values to unsigned byte indices.
 * This mapping is linear, except for clipping, which ensures that no byte 
 * index lies outside the range [0,255]. The linear mapping is defined by 
 * two clip values, clipMin and clipMax. The minimum clip value clipMin 
 * corresponds to byte index 0, and the maximum clip value clipMax 
 * corresponds to byte index 255. Sample values less than clipMin are 
 * mapped to byte index 0; sample values greater than clipMax are mapped 
 * to byte index 255.
 * <p>
 * One byte index is computed for each pixel displayed by this view. 
 * Because the view typically contains more (or fewer) pixels than samples, 
 * this first mapping often requires interpolation between sampled values of 
 * the function f(x1,x2). Either linear or nearest-neighbor interpolation
 * may be specified for this first step.
 * <p>
 * The second step is a table lookup. It uses the pixel bytes computed in 
 * the first mapping as indices in a specified colormap. The colormap is 
 * an array of 256 colors, one for each of the 256 possible byte indices. 
 * See {@link edu.mine.jtk.util.ByteIndexColorModel} for more details.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.09.27
 */
public class PixelsView extends TiledView {

  /**
   * Orientation of sample axes x1 and x2. For example, the orientation 
   * X1RIGHT_X2UP corresponds to x1 increasing horizontally from left to 
   * right, and x2 increasing vertically from bottom to top. The default
   * is X1RIGHT_X2UP.
   */
  public enum Orientation {
    X1RIGHT_X2UP,
    X1DOWN_X2RIGHT
  }

  /**
   * Method used to interpolate pixels between samples of f(x1,x2).
   * The method NEAREST simply uses the nearest sample value. The method 
   * LINEAR linearly interpolates the four nearest sample values. The 
   * default interpolation method is LINEAR.
   */
  public enum Interpolation {
    NEAREST,
    LINEAR
  }

  /**
   * Constructs a pixels view of the specified sampled function f(x1,x2).
   * @param f array[n2][n1] of sampled function values f(x1,x2), where n1 = 
   *  f[0].length and n2 = f.length. This array is referenced, not copied.
   */
  public PixelsView(float[][] f) {
    this(new Sampling(f[0].length),new Sampling(f.length),f);
  }

  /**
   * Constructs a pixels view of the specified sampled function f(x1,x2).
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where
   *  n1 and n2 denote the number of samples in s1 and s2, respectively.
   *  This array is referenced, not copied.
   */
  public PixelsView(Sampling s1, Sampling s2, float[][] f) {
    Check.argument(s1.isUniform(),"s1 is uniform");
    Check.argument(s2.isUniform(),"s2 is uniform");
    Check.argument(Array.isRegular(f),"f is regular");
    Check.argument(s1.getCount()==f[0].length,"s1 consistent with f");
    Check.argument(s2.getCount()==f.length,"s2 consistent with f");
    _s1 = s1;
    _s2 = s2;
    _f = f;
    updateClips();
    updateSampling();
  }

  /**
   * Sets the byte index color model for this view.
   * The default color model is a linear gray map.
   * @param cm the color model.
   */
  public void setColorModel(ByteIndexColorModel colorModel) {
    _colorModel = colorModel;
    repaint();
  }

  /**
   * Gets the byte index color model for this view.
   * @return the color model.
   */
  public ByteIndexColorModel getColorModel() {
    return _colorModel;
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
   * Sets the method for interpolation between samples.
   * @param interpolation the interpolation method.
   */
  public void setInterpolation(Interpolation interpolation) {
    if (_interpolation!=interpolation) {
      _interpolation = interpolation;
      updateBestProjectors();
      repaint();
    }
  }

  /**
   * Gets the method for interpolation between samples.
   * @return the interpolation method.
   */
  public Interpolation getInterpolation() {
    return _interpolation;
  }

  /**
   * Sets the clips for this view. A pixels view maps values of the sampled 
   * function f(x1,x2) to bytes, which are then used as indices into a 
   * specified colormap. This mapping from sample values to byte indices is 
   * linear, and so depends on only these two clip values. The minimum clip 
   * value corresponds to byte index 0, and the maximum clip value corresponds 
   * to byte index 255. Sample values outside of the range (clipMin,clipMax)
   * are clipped to lie inside this range.
   * <p>
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param clipMin the sample value corresponding to colormap byte index 0.
   * @param clipMax the sample value corresponding to colormap byte index 255.
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

    // Bounding rectangle in pixels (device coordinates) for this view.
    // This view should never draw outside of this rectangle.
    double vx0 = _fx;
    double vx1 = _fx+_dx*(_nx-1);
    double vy0 = _fy;
    double vy1 = _fy+_dy*(_ny-1);
    double ux0 = hp.u(vx0);
    double ux1 = hp.u(vx1);
    double uy0 = vp.u(vy0);
    double uy1 = vp.u(vy1);
    double uxmin = min(ux0,ux1)-_uxMargin;
    double uxmax = max(ux0,ux1)+_uxMargin;
    double uymin = min(uy0,uy1)-_uyMargin;
    double uymax = max(uy0,uy1)+_uyMargin;
    int xd = ts.x(uxmin);
    int yd = ts.y(uymin);
    int wd = ts.width(uxmax-uxmin);
    int hd = ts.height(uymax-uymin);
    Rectangle viewRect = new Rectangle(xd,yd,wd,hd);

    // Restrict drawing to intersection of view and clip rectangles.
    Rectangle clipRect = g2d.getClipBounds();
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

    // Interpolate samples f(x1,x2) to obtain image bytes.
    byte[] b;
    if (_interpolation==Interpolation.LINEAR) {
      b = interpolateImageBytesLinear(nx,dx,fx,ny,dy,fy);
    } else {
      b = interpolateImageBytesNearest(nx,dx,fx,ny,dy,fy);
    }

    // Draw image.
    DataBuffer db = new DataBufferByte(b,nx*ny,0);
    int dataType = DataBuffer.TYPE_BYTE;
    int[] bitMasks = new int[]{0xff};
    SampleModel sm = new SinglePixelPackedSampleModel(dataType,nx,ny,bitMasks);
    WritableRaster wr = Raster.createWritableRaster(sm,db,null);
    BufferedImage bi = new BufferedImage(_colorModel,wr,false,null);
    g2d.drawImage(bi,xc,yc,null);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // The specified sampled floats are simply referenced, not copied.
  private Sampling _s1;
  private Sampling _s2;
  private float[][] _f;

  // Color model.
  private ByteIndexColorModel _colorModel = 
    ByteIndexColorModel.linearGray(0,1);

  // View orientation.
  private Orientation _orientation = Orientation.X1RIGHT_X2UP;

  // Interpolation method.
  private Interpolation _interpolation = Interpolation.LINEAR;

  // Clips and percentiles.
  private float _clipMin; // mapped to colormap byte index 0
  private float _clipMax; // mapped to colormap byte index 255
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

  // Margins in normalized coordinates.
  private double _uxMargin;
  private double _uyMargin;


  /**
   * If using percentiles, computes corresponding clip values.
   */
  private void updateClips() {
    if (_usePercentiles) {
      float[] a = (_percMin!=0.0f || _percMax!=0.0f)?Array.flatten(_f):null;
      int n = (a!=null)?a.length:0;
      if (_percMin==0.0f) {
        _clipMin = Array.min(_f);
      } else {
        int k = (int)rint(_percMin*0.01*(n-1));
        Array.quickPartialSort(k,a);
        _clipMin = a[k];
      }
      if (_percMax==100.0f) {
        _clipMax = Array.max(_f);
      } else {
        int k = (int)rint(_percMax*0.01*(n-1));
        Array.quickPartialSort(k,a);
        _clipMax = a[k];
      }
    }
  }

  /**
   * Updates the (x,y) sampling for this view. This sampling corresponds to
   * the pixel (x,y) coordinate system, and it depends on the pixel view 
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
      double tiny = FLT_EPSILON*max(1.0,x0);
      x0 -= tiny;
      x1 += tiny;
    }
    if (y0==y1) {
      double tiny = FLT_EPSILON*max(1.0,y0);
      y0 -= tiny;
      y1 += tiny;
    }

    // Margins in normalized coordinates.
    _uxMargin = 0.0;
    _uyMargin = 0.0;
    if (_interpolation==Interpolation.NEAREST) {
      _uxMargin = 0.5/max(_nx,1.0+FLT_EPSILON);
      _uyMargin = 0.5/max(_ny,1.0+FLT_EPSILON);
    }

    // (ux0,uy0) = normalized coordinates for (left,top) of view.
    // (ux1,uy1) = normalized coordinates for (right,bottom) of view.
    double ux0 = _uxMargin;
    double uy0 = _uyMargin;
    double ux1 = 1.0-_uxMargin;
    double uy1 = 1.0-_uyMargin;

    // Best projectors.
    Projector bhp = new Projector(x0,x1,ux0,ux1);
    Projector bvp = new Projector(y0,y1,uy0,uy1);
    setBestProjectors(bhp,bvp);
  }

  /**
   * Linear interpolation of sampled floats to image bytes. The bytes in 
   * the returned array[nx*ny] will be used as indices in a colormapped 
   * buffered image.
   */
  private byte[] interpolateImageBytesLinear(
    int nx, double dx, double fx,
    int ny, double dy, double fy)
  {
    // Array of bytes.
    byte[] b = new byte[nx*ny];

    // Array temp1 will contain one row of sampled floats, interpolated to 
    // pixel resolution. Likewise, array temp2 will contain an adjacent row
    // of sampled floats, interpolated to pixel resolution. Image pixels
    // are interpolated between these two rows. The index jy1 is the row
    // index of the sampled floats that correspond to the array temp1.
    // Initially, jy1 is garbage, because we have no values in temp1.
    float[] temp1 = new float[nx];
    float[] temp2 = new float[nx];
    int jy1 = -2;

    // Precomputed indices and weights for fast interpolation in x direction.
    int[] kf = new int[nx];
    float[] wf = new float[nx];
    for (int ix=0; ix<nx; ++ix) {
      double xi = fx+ix*dx;
      double xn = (xi-_fx)/_dx;
      if (xn<=0.0) {
        kf[ix] = 0;
        wf[ix] = 0.0f;
      } else if (xn>=_nx-1) {
        kf[ix] = _nx-2;
        wf[ix] = 1.0f;
      } else {
        kf[ix] = (int)xn;
        wf[ix] = (float)(xn-(int)xn);
      }
    }

    // For all pixel y, ...
    for (int iy=0; iy<ny; ++iy) {
      double yi = fy+iy*dy;

      // Index of sample y.
      double yn = max(0.0,min(_ny-1,(yi-_fy)/_dy));
      int jy = max(0,min(_ny-2,(int)yn));

      // If image y is not between current sampled y, ...
      if (jy!=jy1 || iy==0) {

        // If temp2 is still useful, make it temp1 and compute temp2.
        if (jy==jy1+1 && iy!=0) {              
          float[] temp = temp1;
          temp1 = temp2;
          temp2 = temp;
          interpx(min(jy+1,_ny-1),nx,kf,wf,temp2);
        }

        // Else if temp1 is still useful, make it temp2 and compute temp1.
        else if (jy==jy1-1 && iy!=0) {
          float[] temp = temp1;
          temp1 = temp2;
          temp2 = temp;
          interpx(jy,nx,kf,wf,temp1);
        }

        // Else compute both temp1 and temp2. */
        else {
          interpx(jy  ,nx,kf,wf,temp1);
          interpx(min(jy+1,_ny-1),nx,kf,wf,temp2);
        }                 

        // Remember index jy1 corresponding to temp1.
        jy1 = jy;
      }

      // Interpolate in y direction between temp1 and temp2.
      double frac = yn-jy;
      interpy(nx,frac,temp1,temp2,iy*nx,b);
    }                         

    return b;
  }

  /**
   * Linear interpolation of one row of sampled floats to pixel resolution.
   * Also maps _clipMin to 0.0f, and _clipMax to 255.0f.
   */
  private void interpx(int jy, int nx, int[] kf, float[] wf, float[] t) {
    float fscale = 255.0f/(float)(_clipMax-_clipMin);
    float fshift = (float)_clipMin;
    if (_transposed) {
      for (int ix=0; ix<nx; ++ix) {
        int kx = kf[ix];
        float wx = wf[ix];
        float f1 = (_f[kx  ][jy]-fshift)*fscale;
        float f2 = (_f[kx+1][jy]-fshift)*fscale;
        t[ix] = (1.0f-wx)*f1+wx*f2;
      }
    } else {
      float[] fjy = _f[jy];
      for (int ix=0; ix<nx; ++ix) {
        int kx = kf[ix];
        float wx = wf[ix];
        float f1 = (fjy[kx  ]-fshift)*fscale;
        float f2 = (fjy[kx+1]-fshift)*fscale;
        t[ix] = (1.0f-wx)*f1+wx*f2;
      }
    }
  }

  /**
   * Linear interpolation of one row of bytes between temp1 and temp2.
   */
  private void interpy(
    int nx, double frac, float[] temp1, float[] temp2, int kb, byte[] b)
  {
    float w2 = (float)frac;
    float w1 = 1.0f-w2;
    for (int ix=0,ib=kb; ix<nx; ++ix,++ib) {
      float ti = w1*temp1[ix]+w2*temp2[ix];
      if (ti<0.0f)
        ti = 0.0f;
      if (ti>255.0f)
        ti = 255.0f;
      b[ib] = (byte)(ti+0.5f);
    }
  }

  /**
   * Nearest-neighbor interpolation of sampled floats to image bytes. The 
   * bytes in the returned array[nx*ny] will be used as indices in a 
   * colormapped buffered image.
   */
  private byte[] interpolateImageBytesNearest(
    int nx, double dx, double fx,
    int ny, double dy, double fy)
  {
    // Array of bytes.
    byte[] b = new byte[nx*ny];

    // Array temp will contain one row of bytes interpolated to pixel 
    // resolution. The index jytemp is the row index of the sampled 
    // floats that correspond to the array temp. Initially, jytemp is 
    // garbage, because we have no values in temp.
    byte[] temp = new byte[nx];
    int jytemp = -1;

    // Precomputed indices for fast interpolation in x direction.
    int[] kf = new int[nx];
    for (int ix=0; ix<nx; ++ix) {
      double xi = fx+ix*dx;
      double xn = (xi-_fx)/_dx;
      if (xn<=0.0) {
        kf[ix] = 0;
      } else if (xn>=_nx-1) {
        kf[ix] = _nx-1;
      } else {
        kf[ix] = (int)(xn+0.5);
      }
    }

    // For all pixel y, ...
    for (int iy=0; iy<ny; ++iy) {
      double yi = fy+iy*dy;

      // Index of sample y.
      double yn = max(0.0,min(_ny-1,(yi-_fy)/_dy));
      int jy = max(0,min(_ny-1,(int)(yn+0.5)));

      // If necessary, interpolate a new row of bytes.
      if (jy!=jytemp) {
        interpx(jy,nx,kf,temp);
        jytemp = jy;
      }

      // Copy bytes to byte array.
      for (int i=0,j=iy*nx; i<nx; ++i,++j)
        b[j] = temp[i];
    }

    return b;
  }

  /**
   * Nearest-neighbor interpolation of one row of sampled floats to pixels.
   */
  private void interpx(int jy, int nx, int[] kf, byte[] b) {
    float fscale = 255.0f/(float)(_clipMax-_clipMin);
    float fshift = (float)_clipMin;
    if (_transposed) {
      for (int ix=0; ix<nx; ++ix) {
        int kx = kf[ix];
        float fi = (_f[kx][jy]-fshift)*fscale;
        if (fi<0.0f)
          fi = 0.0f;
        if (fi>255.0f)
          fi = 255.0f;
        b[ix] = (byte)(fi+0.5f);
      }
    } else {
      float[] fjy = _f[jy];
      for (int ix=0; ix<nx; ++ix) {
        int kx = kf[ix];
        float fi = (fjy[kx]-fshift)*fscale;
        if (fi<0.0f)
          fi = 0.0f;
        if (fi>255.0f)
          fi = 255.0f;
        b[ix] = (byte)(fi+0.5f);
      }
    }
  }
}
