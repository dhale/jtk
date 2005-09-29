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
import javax.swing.*;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A view of a sampled function f(x1,x2), displayed as a 2-D array of pixels.
 * <em>Not yet fully implemented!</em>
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
   * @param s1 the sampling of the variable x1.
   * @param s2 the sampling of the variable x2.
   * @param f array[n2][n1] of sampled function values f(x1,x2), where
   *  n1 and n2 denote the number of samples in s1 and s2, respectively.
   *  This array is referenced, not copied.
   */
  public PixelsView(Sampling s1, Sampling s2, float[][] f) {
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
   * Set orientation of sample axes x1 and x2.
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
   * Get orientation of sample axes x1 and x2.
   * @return the orientation.
   */
  public Orientation getOrientation() {
    return _orientation;
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
   * By default, minimum and maximum clip values are computed from
   * percentiles. If clip values are set explicitly, then percentiles
   * are ignored, and the specified clip values are used.
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
   * By default, minimum and maximum clip values are computed from
   * percentiles. If clip values are set explicitly, then percentiles
   * are ignored, and the specified clip values are used. If percentiles
   * are set once again, the clip values will be recomputed.
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
    
    // Sample coordinates corresponding to the clip rectangle.
    Rectangle clipRect = g2d.getClipBounds();
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
    double dx = abs(x1-x0)/nx;
    double dy = abs(y1-y0)/ny;
    double fx = min(x0,x1);
    double fy = min(y0,y1);

    // Interpolate samples f(x1,x2) to obtain image bytes.
    byte[] b = interpolateImageBytes(nx,dx,fx,ny,dy,fy);

    // Draw image.
    DataBuffer db = new DataBufferByte(b,nx*ny,0);
    ColorModel cm = makeColorModel();
    int[] bm = new int[]{0xff};
    SampleModel sm = new SinglePixelPackedSampleModel(
      DataBuffer.TYPE_BYTE,nx,ny,bm);
    WritableRaster wr = Raster.createWritableRaster(sm,db,null);
    BufferedImage bi = new BufferedImage(cm,wr,false,null);
    g2d.drawImage(bi,xc,yc,null);
  }
  private ColorModel makeColorModel() {
    byte[] r = new byte[256];
    byte[] g = new byte[256];
    byte[] b = new byte[256];
    for (int i=0; i<256; ++i) {
      r[i] = (byte)i;
      g[i] = (byte)i;
      b[i] = (byte)i;
    }
    return new IndexColorModel(8,256,r,g,b);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // The specified sampled floats are simply referenced, not copied.
  Sampling _s1;
  Sampling _s2;
  float[][] _f;

  // View orientation.
  Orientation _orientation = Orientation.X1RIGHT_X2UP;

  // Interpolation method.
  Interpolation _interpolation = Interpolation.LINEAR;

  // Clips and percentiles.
  float _clipMin; // mapped to colormap byte index 0
  float _clipMax; // mapped to colormap byte index 255
  float _percMin = 0.0f; // may be used to compute _clipMin
  float _percMax = 100.0f; // may be used to compute _clipMax
  boolean _usePercentiles = true; // true, if using percentiles

  // Sampling of the function f(x1,x2) in the pixel (x,y) coordinate system. 
  boolean _transposed;
  int _nx;
  double _dx;
  double _fx;
  int _ny;
  double _dy;
  double _fy;

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
        int k = (int)rint(_percMin*(n-1));
        Array.quickPartialSort(k,a);
        _clipMin = a[k];
      }
      if (_percMax==100.0f) {
        _clipMax = Array.max(_f);
      } else {
        int k = (int)rint(_percMax*(n-1));
        Array.quickPartialSort(k,a);
        _clipMax = a[k];
      }
    }
  }

  /**
   * Updates the (x,y) sampling for this view. This sampling corresponds to
   * the (x,y) coordinate system, with origin in the top-left corner, and it
   * depends on the pixel view orientation. If this sampling is transposed, 
   * then x1 corresponds to y and x2 corresponds to x; otherwise, x1 
   * corresponds to x and x2 corresponds to y.
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
      _nx = n2;
      _dx = d2;
      _fx = f2;
      _ny = n1;
      _dy = d1;
      _fy = f1;
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      _transposed = false;
      _nx = n1;
      _dx = d1;
      _fx = f1;
      _ny = n2;
      _dy = -d2;
      _fy = f2+d2*(n2-1);
    }
    updateBestProjectors();
  }

  /**
   * Called when we might need realignment.
   */
  private void updateBestProjectors() {
    double x0 = _fx;
    double x1 = _fx+_dx*(_nx-1);
    double y0 = _fy;
    double y1 = _fy+_dy*(_ny-1);
    Projector bhp = new Projector(x0,x1,0.0,1.0);
    Projector bvp = new Projector(y0,y1,0.0,1.0);
    setBestProjectors(bhp,bvp);
  }

  /**
   * Interpolates sampled floats to image bytes. The bytes in the returned 
   * array[nx*ny] will be used as indices in a colormapped buffered image.
   */
  private byte[] interpolateImageBytes(
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
          interpx(jy+1,nx,kf,wf,temp2);
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
          interpx(jy+1,nx,kf,wf,temp2);
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
   * Interpolates one row of sampled floats to pixel resolution.
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
   * Interpolates one row of bytes between temp1 and temp2.
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


  ///////////////////////////////////////////////////////////////////////////
  // Temporary, during development only.

  public PixelsView() {
    int n1 = 11;
    int n2 = 11;
    _s1 = new Sampling(n1);
    _s2 = new Sampling(n2);
    _f = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        _f[i2][i1] = (float)(i1+i2)/(float)(n2+n1-2);
      }
    }
    updateClips();
    updateSampling();
  }
  public static void main(String[] args) {
    Set<Mosaic.AxesPlacement> axesPlacement = EnumSet.of(
      Mosaic.AxesPlacement.LEFT,
      Mosaic.AxesPlacement.TOP
    );
    Mosaic.BorderStyle borderStyle = Mosaic.BorderStyle.FLAT;
    Mosaic mosaic = new Mosaic(1,1,axesPlacement,borderStyle);
    mosaic.setPreferredSize(new Dimension(300,300));
    Tile tile = mosaic.getTile(0,0);
    tile.addTiledView(new PixelsView());
    TileZoomMode zoomMode = new TileZoomMode(mosaic.getModeManager());
    zoomMode.setActive(true);
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(mosaic,BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }
}
