/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static edu.mines.jtk.util.MathPlus.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.*;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.awt.ColorMapListener;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.*;

/**
 * A view of a sampled functions f(x1,x2), displayed as a 2-D array of pixels.
 * Sample values are converted to pixel colors in two steps. In the first
 * step, sample values are converted to unsigned bytes in the range [0,255].
 * In the second, step, these bytes are converted to pixel colors, through
 * a specified color model.
 * <p>
 * The first step is a mapping from sample values to unsigned byte values.
 * This mapping is linear, except for clipping, which ensures that no byte 
 * value lies outside the range [0,255]. The linear mapping is defined by 
 * two clip values, clipMin and clipMax. The minimum clip value clipMin 
 * corresponds to byte index 0, and the maximum clip value clipMax 
 * corresponds to byte index 255. Sample values less than clipMin are 
 * mapped to byte index 0; sample values greater than clipMax are mapped 
 * to byte index 255.
 * <p>
 * Pixel byte values are computed for every pixel displayed by this view. 
 * Because the view typically contains more (or fewer) pixels than samples, 
 * this first mapping often requires interpolation between sampled values of 
 * the function f(x1,x2). Either linear or nearest-neighbor interpolation
 * may be specified for this first step.
 * <p>
 * The second step depends on the number of sampled functions specified,
 * either one, three or four. For one function, the byte values are indices 
 * for a table of 256 colors (a color map) in a specified index color model.
 * See {@link edu.mines.jtk.awt.ColorMap} for more details.
 * <p>
 * For three (or four) functions, the byte values are interpreted directly
 * as color components red, green, and blue (and alpha). In this case, any
 * indexed color model specified is not used. The number of color components
 * equals the number of sampled functions specified.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.09.27
 */
public class PixelsView extends TiledView {

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
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   */
  public PixelsView(float[][] f) {
    this(new float[][][]{f});
  }

  /**
   * Constructs a pixels view of the specified sampled functions f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[nc][n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length, n2 = f.length, and nc is the number of functions
   *  to be mapped to colors, either one, three, or four.
   */
  public PixelsView(float[][][] f) {
    set(f);
  }

  /**
   * Constructs a pixels view of the specified sampled function f(x1,x2).
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where
   *  n1 and n2 denote the number of samples in s1 and s2, respectively.
   */
  public PixelsView(Sampling s1, Sampling s2, float[][] f) {
    this(s1,s2,new float[][][]{f});
  }

  /**
   * Constructs a pixels view of the specified sampled functions f(x1,x2).
   * Assumes zero first sample values and unit sampling intervals.
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[nc][n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length, n2 = f.length, and nc is the number of functions
   *  to be mapped to colors, either one, three, or four.
   */
  public PixelsView(Sampling s1, Sampling s2, float[][][] f) {
    set(s1,s2,f);
  }

  /**
   * Sets the sampled function f(x1,x2) for this view.
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length and n2 = f.length.
   */
  public void set(float[][] f) {
    set(new float[][][]{f});
  }

  /**
   * Sets the sampled functions f(x1,x2) for this view.
   * Assumes zero first sample values and unit sampling intervals.
   * @param f array[nc][n2][n1] of sampled function values f(x1,x2), where 
   *  n1 = f[0].length, n2 = f.length, and nc is the number of functions
   *  to be mapped to colors, either one, three, or four.
   */
  public void set(float[][][] f) {
    set(new Sampling(f[0][0].length),new Sampling(f[0].length),f);
  }

  /**
   * Sets the sampled function f(x1,x2) for this view.
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where
   *  n1 and n2 denote the number of samples in s1 and s2, respectively.
   */
  public void set(Sampling s1, Sampling s2, float[][] f) {
    set(s1,s2,new float[][][]{f});
  }

  /**
   * Sets the sampled functions f(x1,x2) for this view.
   * @param s1 the sampling of the variable x1; must be uniform.
   * @param s2 the sampling of the variable x2; must be uniform.
   * @param f array[nc][n2][n1] of sampled function values f(x1,x2), where
   *  n1 and n2 denote the number of samples in s1 and s2, respectively,
   *  and nc denotes the number of functions to be mapped to colors.
   */
  public void set(Sampling s1, Sampling s2, float[][][] f) {
    Check.argument(s1.isUniform(),"s1 is uniform");
    Check.argument(s2.isUniform(),"s2 is uniform");
    Check.argument(Array.isRegular(f),"f is regular");
    Check.argument(s1.getCount()==f[0][0].length,"s1 consistent with f");
    Check.argument(s2.getCount()==f[0].length,"s2 consistent with f");
    Check.argument(
      f.length==1 || f.length==3 || f.length==4,
      "number of sampled functions is one, three, or four");
    _nc = f.length;
    _s1 = s1;
    _s2 = s2;
    _f = Array.copy(f);
    _clips = new Clips[_nc];
    for (int ic=0; ic<_nc; ++ic)
      _clips[ic] = new Clips(_f[ic]);
    _clipMin = new float[_nc];
    _clipMax = new float[_nc];
    updateSampling();
    repaint();
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
   * Sets the index color model for this view.
   * The default color model is a black-to-white gray model.
   * This index color model is not used for three or four components.
   * @param colorModel the index color model.
   */
  public void setColorModel(IndexColorModel colorModel) {
    _colorMap.setColorModel(colorModel);
    repaint();
  }

  /**
   * Gets the index color model used for this view.
   * @return the index color model; null, of more than one component.
   */
  public IndexColorModel getColorModel() {
    return (_nc==1)?_colorMap.getColorModel():null;
  }

  /**
   * Sets the clips for this view. A pixels view maps values of the sampled 
   * function f(x1,x2) to bytes, which are then used as indices into a 
   * specified color model. This mapping from sample values to byte indices 
   * is linear, and so depends on only these two clip values. The minimum clip 
   * value corresponds to byte index 0, and the maximum clip value corresponds 
   * to byte index 255. Sample values outside of the range (clipMin,clipMax)
   * are clipped to lie inside this range.
   * <p>
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param clipMin the sample value corresponding to color model index 0.
   * @param clipMax the sample value corresponding to color model index 255.
   */
  public void setClips(float clipMin, float clipMax) {
    _clips[0].setClips(clipMin,clipMax);
    repaint();
  }

  /**
   * Gets the minimum clip value.
   * @return the minimum clip value.
   */
  public float getClipMin() {
    return _clips[0].getClipMin();
  }

  /**
   * Gets the maximum clip value.
   * @return the maximum clip value.
   */
  public float getClipMax() {
    return _clips[0].getClipMax();
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
    _clips[0].setPercentiles(percMin,percMax);
    repaint();
  }

  /**
   * Gets the minimum percentile.
   * @return the minimum percentile.
   */
  public float getPercentileMin() {
    return _clips[0].getPercentileMin();
  }

  /**
   * Gets the maximum percentile.
   * @return the maximum percentile.
   */
  public float getPercentileMax() {
    return _clips[0].getPercentileMax();
  }

  /**
   * Sets the clips for the specified component.
   * @param ic the index of the component.
   * @param clipMin the sample value corresponding to byte value 0.
   * @param clipMax the sample value corresponding to byte value 255.
   */
  public void setClips(int ic, float clipMin, float clipMax) {
    _clips[ic].setClips(clipMin,clipMax);
    repaint();
  }

  /**
   * Gets the minimum clip value for the specified component.
   * @param ic the index of the component.
   * @return the minimum clip value.
   */
  public float getClipMin(int ic) {
    return _clips[ic].getClipMin();
  }

  /**
   * Gets the maximum clip value for the specified component.
   * @param ic the index of the component.
   * @return the maximum clip value.
   */
  public float getClipMax(int ic) {
    return _clips[ic].getClipMax();
  }

  /**
   * Sets the percentiles for the specified component.
   * @param ic the index of the component.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   */
  public void setPercentiles(int ic, float percMin, float percMax) {
    _clips[ic].setPercentiles(percMin,percMax);
    repaint();
  }

  /**
   * Gets the minimum percentile for the specified component.
   * @param ic the index of the component.
   * @return the minimum percentile.
   */
  public float getPercentileMin(int ic) {
    return _clips[ic].getPercentileMin();
  }

  /**
   * Gets the maximum percentile for the specified component.
   * @param ic the index of the component.
   * @return the maximum percentile.
   */
  public float getPercentileMax(int ic) {
    return _clips[ic].getPercentileMax();
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

    // Ensure clips are correct.
    updateClips();

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
    int nxy = nx*ny;
    double dx = (x1-x0)/max(1,nx-1);
    double dy = (y1-y0)/max(1,ny-1);
    double fx = x0;
    double fy = y0;

    // If only one component (using index color model) ...
    if (_nc==1) {
      byte[] b;
      if (_interpolation==Interpolation.LINEAR) {
        b = interpolateImageBytesLinear(0,nx,dx,fx,ny,dy,fy);
      } else {
        b = interpolateImageBytesNearest(0,nx,dx,fx,ny,dy,fy);
      }
      ColorModel colorModel = _colorMap.getColorModel();
      DataBuffer db = new DataBufferByte(b,nxy,0);
      int dataType = DataBuffer.TYPE_BYTE;
      int[] bitMasks = new int[]{0xff};
      SampleModel sm = new SinglePixelPackedSampleModel(
        dataType,nx,ny,bitMasks);
      WritableRaster wr = Raster.createWritableRaster(sm,db,null);
      BufferedImage bi = new BufferedImage(colorModel,wr,false,null);
      g2d.drawImage(bi,xc,yc,null);
    }

    // else, if three or four components (using direct color model) ...
    else {
      byte[][] b = new byte[_nc][];
      for (int ic=0; ic<_nc; ++ic) {
        if (_interpolation==Interpolation.LINEAR) {
          b[ic] = interpolateImageBytesLinear(ic,nx,dx,fx,ny,dy,fy);
        } else {
          b[ic] = interpolateImageBytesNearest(ic,nx,dx,fx,ny,dy,fy);
        }
      }
      int[] i = new int[nxy];
      if (_nc==3) {
        byte[] b0 = b[0], b1 = b[1], b2 = b[2];
        for (int ixy=0; ixy<nxy; ++ixy)
          i[ixy] = 0xff000000           | 
                   ((b0[ixy]&0xff)<<16) | 
                   ((b1[ixy]&0xff)<< 8) | 
                   ((b2[ixy]&0xff)    );
      } else {
        byte[] b0 = b[0], b1 = b[1], b2 = b[2], b3 = b[3];
        for (int ixy=0; ixy<nxy; ++ixy)
          i[ixy] = ((b3[ixy]&0xff)<<24) |
                   ((b0[ixy]&0xff)<<16) | 
                   ((b1[ixy]&0xff)<< 8) | 
                   ((b2[ixy]&0xff)    );
      }
      DataBuffer db = new DataBufferInt(i,nxy,0);
      ColorModel colorModel = new DirectColorModel(32,
        0x00ff0000,
        0x0000ff00,
        0x000000ff, 
        0xff000000);
      int[] bitMasks = new int[]{
        0x00ff0000,
        0x0000ff00,
        0x000000ff,
        0xff000000};
      int dataType = DataBuffer.TYPE_INT;
      SampleModel sm = new SinglePixelPackedSampleModel(
        dataType,nx,ny,bitMasks);
      WritableRaster wr = Raster.createWritableRaster(sm,db,null);
      BufferedImage bi = new BufferedImage(colorModel,wr,false,null);
      g2d.drawImage(bi,xc,yc,null);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // The sampled floats.
  private int _nc; // number of components
  private Sampling _s1; // sampling of 1st dimension
  private Sampling _s2; // sampling of 2nd dimension
  private float[][][] _f; // copy of array of floats

  // View orientation.
  private Orientation _orientation = Orientation.X1RIGHT_X2UP;

  // Interpolation method.
  private Interpolation _interpolation = Interpolation.LINEAR;

  // Clips, one for each component.
  Clips[] _clips;
  float[] _clipMin;
  float[] _clipMax;

  // Color map with default gray color model.
  private ColorMap _colorMap = new ColorMap(ColorMap.GRAY);

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

  /**
   * Update the clips if necessary.
   */
  private void updateClips() {
    if (_clipMin==null)
      _clipMin = new float[_nc];
    if (_clipMax==null)
      _clipMax = new float[_nc];
    for (int ic=0; ic<_nc; ++ic) {
      float clipMin = _clips[ic].getClipMin();
      float clipMax = _clips[ic].getClipMax();
      if (_clipMin[ic]!=clipMin || _clipMax[ic]!=clipMax) {
        _clipMin[ic] = clipMin;
        _clipMax[ic] = clipMax;
        if (_nc==1)
          _colorMap.setValueRange(_clipMin[ic],_clipMax[ic]);
      }
    }
  }

  /**
   * Updates the (x,y) sampling for this view. This sampling corresponds to
   * the pixel (x,y) coordinate system, and it depends on the pixel view 
   * orientation. If this sampling is transposed, then x1 corresponds to y 
   * and x2 corresponds to x; otherwise, x1 corresponds to x and x2 
   * corresponds to y. In either case, the coordinates (_fx,_fy) correspond
   * to the sampled function value _f[ic][0][0].
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

  /**
   * Linear interpolation of sampled floats to image bytes. The bytes in 
   * the returned array[nx*ny] will be used as indices in a color-mapped 
   * buffered image.
   */
  private byte[] interpolateImageBytesLinear(
    int ic,
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
          interpx(ic,min(jy+1,_ny-1),nx,kf,wf,temp2);
        }

        // Else if temp1 is still useful, make it temp2 and compute temp1.
        else if (jy==jy1-1 && iy!=0) {
          float[] temp = temp1;
          temp1 = temp2;
          temp2 = temp;
          interpx(ic,jy,nx,kf,wf,temp1);
        }

        // Else compute both temp1 and temp2. */
        else {
          interpx(ic,             jy,nx,kf,wf,temp1);
          interpx(ic,min(jy+1,_ny-1),nx,kf,wf,temp2);
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
  private void interpx(
    int ic, int jy, int nx, int[] kf, float[] wf, float[] t) 
  {
    float[][] fc = _f[ic];
    float fscale = 255.0f/(_clipMax[ic]-_clipMin[ic]);
    float fshift = _clipMin[ic];
    if (_transposed) {
      if (_nx==1) {
        float f0 = (fc[0][jy]-fshift)*fscale;
        for (int ix=0; ix<nx; ++ix)
          t[ix] = f0;
      } else {
        for (int ix=0; ix<nx; ++ix) {
          int kx = kf[ix];
          float wx = wf[ix];
          float f1 = (fc[kx  ][jy]-fshift)*fscale;
          float f2 = (fc[kx+1][jy]-fshift)*fscale;
          t[ix] = (1.0f-wx)*f1+wx*f2;
        }
      }
    } else {
      float[] fjy = fc[jy];
      if (_nx==1) {
        float f0 = (fjy[0]-fshift)*fscale;
        for (int ix=0; ix<nx; ++ix)
          t[ix] = f0;
      } else {
        for (int ix=0; ix<nx; ++ix) {
          int kx = kf[ix];
          float wx = wf[ix];
          float f1 = (fjy[kx  ]-fshift)*fscale;
          float f2 = (fjy[kx+1]-fshift)*fscale;
          t[ix] = (1.0f-wx)*f1+wx*f2;
        }
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
   * color-mapped buffered image.
   */
  private byte[] interpolateImageBytesNearest(
    int ic,
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
        interpx(ic,jy,nx,kf,temp);
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
  private void interpx(int ic, int jy, int nx, int[] kf, byte[] b) {
    float[][] fc = _f[ic];
    float fscale = 255.0f/(_clipMax[ic]-_clipMin[ic]);
    float fshift = _clipMin[ic];
    if (_transposed) {
      for (int ix=0; ix<nx; ++ix) {
        int kx = kf[ix];
        float fi = (fc[kx][jy]-fshift)*fscale;
        if (fi<0.0f)
          fi = 0.0f;
        if (fi>255.0f)
          fi = 255.0f;
        b[ix] = (byte)(fi+0.5f);
      }
    } else {
      float[] fjy = fc[jy];
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
