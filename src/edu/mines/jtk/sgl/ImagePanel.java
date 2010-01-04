/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.image.IndexColorModel;
import java.nio.IntBuffer;
import java.util.ArrayList;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.awt.ColorMapListener;
import edu.mines.jtk.dsp.Sampling;
import static edu.mines.jtk.ogl.Gl.*;
import edu.mines.jtk.ogl.GlTextureName;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.max;
import static edu.mines.jtk.util.MathPlus.min;

/**
 * An axis-aligned panel that draws a 2D image of a slice of a 3D array.
 * The corner points of the image panel's axis-aligned frame determines 
 * which slice of the 3D array is drawn.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.06.04
 */
public class ImagePanel extends AxisAlignedPanel {

  /**
   * Constructs an image panel with default unit sampling for 3D array.
   * @param f 3D array of floats.
   */
  public ImagePanel(float[][][] f) {
    this(new Sampling(f[0][0].length),
         new Sampling(f[0].length),
         new Sampling(f.length),
         f);
  }

  /**
   * Constructs an image panel for specified sampling and 3D array.
   * @param s1 sampling of 1st dimension (Z axis).
   * @param s2 sampling of 2nd dimension (Y axis).
   * @param s3 sampling of 3rd dimension (X axis).
   * @param f 3D array of floats.
   */
  public ImagePanel(Sampling s1, Sampling s2, Sampling s3, float[][][] f) {
    this(s1,s2,s3,new SimpleFloat3(f));
  }

  /**
   * Constructs an image panel for specified sampling and abstract 3D array.
   * @param s1 sampling of 1st dimension (Z axis).
   * @param s2 sampling of 2nd dimension (Y axis).
   * @param s3 sampling of 3rd dimension (X axis).
   * @param f abstract 3D array of floats.
   */
  public ImagePanel(Sampling s1, Sampling s2, Sampling s3, Float3 f) {
    _sx = s3;
    _sy = s2;
    _sz = s1;
    _f = f;
    _clips = new Clips(_f);
  }

  /**
   * Notifies this panel that values in the referenced 3D array have changed.
   */
  public void update() {
    _texturesDirty = true;
    dirtyDraw();
  }

  /**
   * Gets a box constraint for this panel. The constraint is consistent
   * with the sampling of this image.
   * @return the box constraint.
   */
  public BoxConstraint getBoxConstraint() {
    return new BoxConstraint(_sx,_sy,_sz);
  }

  /**
   * Sets the index color model for this panel.
   * The default color model is a black-to-white gray model.
   * @param colorModel the index color model.
   */
  public void setColorModel(IndexColorModel colorModel) {
    _colorMap.setColorModel(colorModel);
    _texturesDirty = true;
    dirtyDraw();
  }

  /**
   * Gets the index color model for this panel.
   * @return the index color model.
   */
  public IndexColorModel getColorModel() {
    return _colorMap.getColorModel();
  }

  /**
   * Sets the clips for this panel. An image panel maps array values
   * to bytes, which are then used as indices into a specified color 
   * model. This mapping from array values to byte indices is linear, 
   * and so depends on only these two clip values. The minimum clip 
   * value corresponds to byte index 0, and the maximum clip value 
   * corresponds to byte index 255. Array values outside of the range 
   * [clipMin,clipMax] are clipped to lie inside this range.
   * <p>
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param clipMin the sample value corresponding to color model index 0.
   * @param clipMax the sample value corresponding to color model index 255.
   */
  public void setClips(double clipMin, double clipMax) {
    _clips.setClips(clipMin,clipMax);
    _texturesDirty = true;
    dirtyDraw();
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
   * Sets the percentiles used to compute clips for this panel. The default 
   * percentiles are 0 and 100, which correspond to the minimum and maximum 
   * array values.
   * <p>
   * Calling this method enables the computation of clips from percentiles.
   * Any clip values specified or computed previously will be forgotten.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   */
  public void setPercentiles(double percMin, double percMax) {
    _clips.setPercentiles(percMin,percMax);
    _texturesDirty = true;
    dirtyDraw();
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

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void draw(DrawContext dc) {
    updateClipMinMax();
    drawTextures();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Axis _axis; // axis orthogonal to plane of this panel
  private Sampling _sx,_sy,_sz; // sampling of x, y, z axes
  private Float3 _f; // 3D indexed floats

  // Coordinate bounds.
  private double _xmin,_ymin,_zmin; // minimum array coordinates
  private double _xmax,_ymax,_zmax; // maximum array coordinates

  // Clips.
  private Clips _clips;
  private float _clipMin = 0.0f;
  private float _clipMax = 1.0f;

  // Color map with default gray color model.
  private ColorMap _colorMap = new ColorMap(_clipMin,_clipMax,ColorMap.GRAY);

  // This panel can draw up to ns*nt samples sliced from nx*ny*nz samples
  // of an array. The dimensions ns and nt are chosen from array dimensions 
  // nx, ny, and nz, depending on which axis of the array is orthogonal to 
  // the plane of this panel. Specifically,
  // Axis.X:  ns = ny,  nt = nz
  // Axis.Y:  ns = nx,  nt = nz
  // Axis.Z:  ns = nx,  nt = ny
  //
  // The panel is drawn as a mosaic of ms*mt textures. The size of each
  // textures is ls*lt samples, where ls and lt are powers of two. To 
  // enable seamless linear interpolation, these textures must overlap 
  // by one sample. For either the s or t dimensions, given l>3 and n, 
  // the number of textures in the panel is m = 1+(n-2)/(l-1).
  //
  // By convention, we index samples within the panel with ks and kt,
  // textures within the panel by js and jt, and samples within each 
  // texture by is and it. In other words, index variables i, j, and k 
  // correspond to dimensions l, m, and n. 
  // 
  // Here is an example of indices k and j for l=4, m=3, and n=10:
  // 0 1 2 3 4 5 6 7 8 9    sample index k
  // 0 0 0 0                texture index j=0
  //       1 1 1 1          texture index j=1
  //             2 2 2 2    texture index j=2
  // Note the one-sample overlap of the textures.
  //
  // The panel may or may not draw its entire mosaic of ms*mt textures.
  // The corner points of the frame containing this panel determine the
  // subset of the ms*mt textures drawn. For fast drawing, this panel 
  // maintains a cache of the required textures in an array[mt][ms].
  // In this array, only those textures that are required for drawing
  // are non-null.

  // Sampling of panels and textures, as described above.
  private int _ls,_lt; // numbers of samples per textures
  private int _ms,_mt; // numbers of textures in panel
  private int _ns,_nt; // numbers of samples in panel
  private double _ds,_dt; // sampling intervals in panel
  private double _fs,_ft; // first sample values in panel

  // The texture cache; textures required for drawing are non-null.
  private GlTextureName[][] _tn; // array[_mt][_ms] of texture names
  private boolean _texturesDirty = true; // do textures need updating?

  // The subset of samples that must be drawn depends on frame corner points.
  private int _kxmin,_kymin,_kzmin; // min sample-in-array indices
  private int _kxmax,_kymax,_kzmax; // max sample-in-array indices
  private int _ksmin,_ktmin; // min sample-in-panel indices
  private int _ksmax,_ktmax; // max sample-in-panel indices
  private int _jsmin,_jtmin; // min texture-in-cache indices
  private int _jsmax,_jtmax; // max texture-in-cache indices

  // Used when creating/loading a texture.
  private IntBuffer _pixels; // array[_lt][_ls] of image pixels for one texture
  private float[][] _floats; // array[_ls][_lt] of image floats for one texture

  /**
   * Update the clip min/max for this panel, if necessary.
   */
  private void updateClipMinMax() {
    float clipMin = _clips.getClipMin();
    float clipMax = _clips.getClipMax();
    if (_clipMin!=clipMin || _clipMax!=clipMax) {
      _clipMin = clipMin;
      _clipMax = clipMax;
      _colorMap.setValueRange(_clipMin,_clipMax);
      _texturesDirty = true;
    }
  }

  private void drawTextures() {

    // If parent is not a frame, do not know where to draw.
    AxisAlignedFrame frame = getFrame();
    if (frame==null)
      return;

    // If necessary, update sampling.
    Axis axis = frame.getAxis();
    if (_axis!=axis)
      updateSampling(axis,_sx,_sy,_sz);

    // If necessary, update bounds and textures.
    Point3 qmin = frame.getCornerMin();
    Point3 qmax = frame.getCornerMax();
    double xmin = qmin.x;
    double ymin = qmin.y;
    double zmin = qmin.z;
    double xmax = qmax.x;
    double ymax = qmax.y;
    double zmax = qmax.z;
    if (_xmin!=xmin || _ymin!=ymin || _zmin!=zmin ||
        _xmax!=xmax || _ymax!=ymax || _zmax!=zmax)
      updateBoundsAndTextures(xmin,ymin,zmin,xmax,ymax,zmax);

    // If necessary, update textures.
    if (_texturesDirty)
      updateTextures();

    // Prepare to draw textures.
    glShadeModel(GL_FLAT);
    glEnable(GL_TEXTURE_2D);
    glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_REPLACE);

    // Push image(s) slightly below any lines we may draw on top.
    float[] v = {0.0f};
    glGetFloatv(GL_POLYGON_OFFSET_FACTOR,v,0); float factor = v[0];
    glGetFloatv(GL_POLYGON_OFFSET_UNITS,v,0); float units = v[0];
    glEnable(GL_POLYGON_OFFSET_FILL);
    glPolygonOffset(factor+1.0f,units+1.0f);

    // Shift and scale factors for computing texture coordinates.
    float sa = 0.5f/(float)_ls;
    float ta = 0.5f/(float)_lt;
    float sb = 1.0f/(float)_ls;
    float tb = 1.0f/(float)_lt;

    // Average of corner coordinates. (Will use only one of these.)
    double xa = 0.5*(_xmin+_xmax);
    double ya = 0.5*(_ymin+_ymax);
    double za = 0.5*(_zmin+_zmax);

    // For all textures in the cache, ...
    for (int jt=_jtmin; jt<=_jtmax; ++jt) {
      for (int js=_jsmin; js<=_jsmax; ++js) {

        // Indices of samples needed for this texture.
        int ks0 = js*(_ls-1);
        int kt0 = jt*(_lt-1);
        int ks1 = ks0+_ls-1;
        int kt1 = kt0+_lt-1;
        ks0 = max(_ksmin,ks0);
        kt0 = max(_ktmin,kt0);
        ks1 = min(_ksmax,ks1);
        kt1 = min(_ktmax,kt1);

        // Texture coordinates. In the example pictured here, we assume 
        // three textures (js = 0,1,2) with ls = 4. For each texture, the 
        // "|" correspond to texture coordinates 0.0 and 1.0. The "x" 
        // correspond to image samples, The s coordinate interval between 
        // image samples is 1.0/ls, and the s coordinate of the first image 
        // sample is 0.5/ls.
        // ks:      0   1   2   3   4   5   6   7
        // js=0:  | x   x   x   x |
        // js=1:              | x   x   x   x |
        // js=2:                          | x   x   x   x |
        float s0 = sa+(float)(ks0%(_ls-1))*sb;
        float t0 = ta+(float)(kt0%(_lt-1))*tb;
        float s1 = s0+(float)(ks1-ks0)*sb;
        float t1 = t0+(float)(kt1-kt0)*tb;

        // Draw the texture.
        GlTextureName tn = _tn[jt][js];
        glBindTexture(GL_TEXTURE_2D,tn.name());
        glBegin(GL_POLYGON);
        if (_axis==Axis.X) {
          double y0 = _fs+ks0*_ds;
          double z0 = _ft+kt0*_dt;
          double y1 = _fs+ks1*_ds;
          double z1 = _ft+kt1*_dt;
          glTexCoord2f(s0,t0);  glVertex3d(xa,y0,z0);
          glTexCoord2f(s1,t0);  glVertex3d(xa,y1,z0);
          glTexCoord2f(s1,t1);  glVertex3d(xa,y1,z1);
          glTexCoord2f(s0,t1);  glVertex3d(xa,y0,z1);
        } else if (_axis==Axis.Y) {
          double x0 = _fs+ks0*_ds;
          double z0 = _ft+kt0*_dt;
          double x1 = _fs+ks1*_ds;
          double z1 = _ft+kt1*_dt;
          glTexCoord2f(s0,t0);  glVertex3d(x0,ya,z0);
          glTexCoord2f(s1,t0);  glVertex3d(x1,ya,z0);
          glTexCoord2f(s1,t1);  glVertex3d(x1,ya,z1);
          glTexCoord2f(s0,t1);  glVertex3d(x0,ya,z1);
        } else {
          double x0 = _fs+ks0*_ds;
          double y0 = _ft+kt0*_dt;
          double x1 = _fs+ks1*_ds;
          double y1 = _ft+kt1*_dt;
          glTexCoord2f(s0,t0);  glVertex3d(x0,y0,za);
          glTexCoord2f(s1,t0);  glVertex3d(x1,y0,za);
          glTexCoord2f(s1,t1);  glVertex3d(x1,y1,za);
          glTexCoord2f(s0,t1);  glVertex3d(x0,y1,za);
        }
        glEnd();
      }
    }

    // Done with textures for now.
    glBindTexture(GL_TEXTURE_2D,0);
    glDisable(GL_TEXTURE_2D);
  }

  private void updateSampling(
    Axis axis, Sampling sx, Sampling sy, Sampling sz) 
  {
    disposeTextures();
    int nx = sx.getCount();
    int ny = sy.getCount();
    int nz = sz.getCount();
    double dx = sx.getDelta();
    double dy = sy.getDelta();
    double dz = sz.getDelta();
    double fx = sx.getFirst();
    double fy = sy.getFirst();
    double fz = sz.getFirst();
    _axis = axis;
    _sx = sx;
    _sy = sy;
    _sz = sz;
    _ls = 64;
    _lt = 64;
    if (_axis==Axis.X) {
      _ns = ny;
      _ds = dy;
      _fs = fy;
      _nt = nz;
      _dt = dz;
      _ft = fz;
    } else if (_axis==Axis.Y) {
      _ns = nx;
      _ds = dx;
      _fs = fx;
      _nt = nz;
      _dt = dz;
      _ft = fz;
    } else {
      _ns = nx;
      _ds = dx;
      _fs = fx;
      _nt = ny;
      _dt = dy;
      _ft = fy;
    }
    _ms = 1+(_ns-2)/(_ls-1);
    _mt = 1+(_nt-2)/(_lt-1);
    _tn = new GlTextureName[_mt][_ms];
    _kxmin = 0;  _kxmax = -1;
    _kymin = 0;  _kymax = -1;
    _kzmin = 0;  _kzmax = -1;
    _ksmin = 0;  _ksmax = -1;
    _ktmin = 0;  _ktmax = -1;
    _jsmin = 0;  _jsmax = -1;
    _jtmin = 0;  _jtmax = -1;
    _pixels = Direct.newIntBuffer(_ls*_lt);
    _floats = new float[_ls][_lt];
  }

  private void updateBoundsAndTextures(
    double xmin, double ymin, double zmin,
    double xmax, double ymax, double zmax)
  {
    _xmin = max(xmin,_sx.getFirst());
    _ymin = max(ymin,_sy.getFirst());
    _zmin = max(zmin,_sz.getFirst());
    _xmax = min(xmax,_sx.getLast());
    _ymax = min(ymax,_sy.getLast());
    _zmax = min(zmax,_sz.getLast());
    int kxmin = _sx.indexOfNearest(_xmin);
    int kymin = _sy.indexOfNearest(_ymin);
    int kzmin = _sz.indexOfNearest(_zmin);
    int kxmax = _sx.indexOfNearest(_xmax);
    int kymax = _sy.indexOfNearest(_ymax);
    int kzmax = _sz.indexOfNearest(_zmax);
    boolean stale;
    if (_axis==Axis.X) {
      stale = _kxmin!=kxmin;
      _kxmin = kxmin;
      _ksmin = _kymin = kymin;
      _ktmin = _kzmin = kzmin;
      _kxmax = kxmax;
      _ksmax = _kymax = kymax;
      _ktmax = _kzmax = kzmax;
    } else if (_axis==Axis.Y) {
      stale = _kymin!=kymin;
      _ksmin = _kxmin = kxmin;
      _kymin = kymin;
      _ktmin = _kzmin = kzmin;
      _ksmax = _kxmax = kxmax;
      _kymax = kymax;
      _ktmax = _kzmax = kzmax;
    } else {
      stale = _kzmin!=kzmin;
      _ksmin = _kxmin = kxmin;
      _ktmin = _kymin = kymin;
      _kzmin = kzmin;
      _ksmax = _kxmax = kxmax;
      _ktmax = _kymax = kymax;
      _kzmax = kzmax;
    }

    // New texture-in-cache index bounds.
    int jsmin = _ksmin/(_ls-1);
    int jtmin = _ktmin/(_lt-1);
    int jsmax = max(0,_ksmax-1)/(_ls-1);
    int jtmax = max(0,_ktmax-1)/(_lt-1);

    // Stale textures are in the cache but no longer needed.
    // Move stale textures from the cache to a stale list.
    ArrayList<GlTextureName> staleList = new ArrayList<GlTextureName>();
    for (int jt=_jtmin; jt<=_jtmax; ++jt) {
      for (int js=_jsmin; js<=_jsmax; ++js) {
        if (stale || js<jsmin || jt<jtmin || jsmax<js || jtmax<jt) {
          if (_tn[jt][js]!=null) {
            staleList.add(_tn[jt][js]);
            _tn[jt][js] = null;
          }
        }
      }
    }
    int nstale = staleList.size();

    // Update texture cache. For each texture required but not cached,
    // if possible, reuse a stale texture, else make a new texture.
    for (int jt=jtmin; jt<=jtmax; ++jt) {
      for (int js=jsmin; js<=jsmax; ++js) {
        GlTextureName tn = _tn[jt][js];
        if (tn==null) {
          if (!staleList.isEmpty()) {
            tn = staleList.remove(--nstale);
          } else {
            tn = makeTexture();
          }
          _tn[jt][js] = tn;
          loadTexture(js,jt);
        }
      }
    }

    // Dispose any stale textures that remain in the list.
    while (nstale>0) {
      GlTextureName tn = staleList.remove(--nstale);
      tn.dispose();
    }

    // Update texture-in-cache index bounds.
    _jsmin = jsmin;
    _jtmin = jtmin;
    _jsmax = jsmax;
    _jtmax = jtmax;

    // Textures now clean.
    _texturesDirty = false;
  }

  private void updateTextures() {

    // Reload only those textures already cached.
    for (int jt=_jtmin; jt<=_jtmax; ++jt) {
      for (int js=_jsmin; js<=_jsmax; ++js) {
        if (_tn[jt][js]!=null)
          loadTexture(js,jt);
      }
    }

    // Textures now clean.
    _texturesDirty = false;
  }

  private void disposeTextures() {
    if (_tn!=null) {
      for (int jt=0; jt<_mt; ++jt) {
        for (int js=0; js<_ms; ++js) {
          GlTextureName tn = _tn[jt][js];
          if (tn!=null)
            tn.dispose();
        }
      }
      _tn = null;
    }
  }

  private GlTextureName makeTexture() {
    glPixelStorei(GL_UNPACK_ALIGNMENT,1);
    GlTextureName tn = new GlTextureName();
    glBindTexture(GL_TEXTURE_2D,tn.name());
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexImage2D(
      GL_TEXTURE_2D,0,GL_RGBA,_ls,_lt,0,GL_RGBA,GL_UNSIGNED_BYTE,_pixels);
    glBindTexture(GL_TEXTURE_2D,0);
    return tn;
  }

  private void loadTexture(int js, int jt) {
    int ks = js*(_ls-1);
    int kt = jt*(_lt-1);
    int ls = min(_ls,_ns-ks);
    int lt = min(_lt,_nt-kt);
    if (_axis==Axis.X) {
      _f.get12(lt,ls,kt,ks,_kxmin,_floats);
    } else if (_axis==Axis.Y) {
      _f.get13(lt,ls,kt,_kymin,ks,_floats);
    } else if (_axis==Axis.Z) {
      _f.get23(lt,ls,_kzmin,kt,ks,_floats);
    }
    float fscale = 255.0f/(_clipMax-_clipMin);
    float fshift = _clipMin;
    IndexColorModel icm = _colorMap.getColorModel();
    for (int is=0; is<ls; ++is) {
      for (int it=0; it<lt; ++it) {
        float fi = (_floats[is][it]-fshift)*fscale;
        if (fi<0.0f)
          fi = 0.0f;
        if (fi>255.0f)
          fi = 255.0f;
        int i = (int)(fi+0.5f);
        int r = icm.getRed(i);
        int g = icm.getGreen(i);
        int b = icm.getBlue(i);
        int a = icm.getAlpha(i);
        int p = (r&0xff)|((g&0xff)<<8)|((b&0xff)<<16)|((a&0xff)<<24);
        _pixels.put(is+it*_ls,p);
      }
    }
    glPixelStorei(GL_UNPACK_ALIGNMENT,1);
    GlTextureName tn = _tn[jt][js];
    glBindTexture(GL_TEXTURE_2D,tn.name());
    glTexSubImage2D(
      GL_TEXTURE_2D,0,0,0,_ls,_lt,GL_RGBA,GL_UNSIGNED_BYTE,_pixels);
    glBindTexture(GL_TEXTURE_2D,0);
  }
}
