/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.io.*;
import edu.mines.jtk.opengl.*;
import edu.mines.jtk.util.*;

import static edu.mines.jtk.opengl.Gl.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * An axis-aligned panel that draws a 2-D image of a slice of a 3-D array.
 * The corner points of the image panel's frame determines3-D array is 
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.06.04
 */
public class ImagePanel extends AxisAlignedPanel {

  /**
   * Constructs an image panel.
   * @param sx sampling of the X axis.
   * @param sy sampling of the Y axis.
   * @param sz sampling of the Z axis.
   * @param f the floats to slice for images.
   */
  public ImagePanel(Sampling sx, Sampling sy, Sampling sz, Float3 f) {
    _sx = sx;
    _sy = sy;
    _sz = sz;
    _f = f;
  }

  /**
   * Gets a box constraint for this panel. The constraint is consistent
   * with the sampling of this image.
   * @return the box constraint.
   */
  public BoxConstraint getBoxConstraint() {
    return new BoxConstraint(_sx,_sy,_sz);
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void draw(DrawContext dc) {
    drawTextures();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Axis _axis; // axis orthogonal to plane of this panel
  private Sampling _sx,_sy,_sz; // sampling of x, y, z axes
  private Float3 _f; // 3-D indexed collection of floats

  private double _xmin,_ymin,_zmin; // minimum array coordinates
  private double _xmax,_ymax,_zmax; // maximum array coordinates

  // This panel can draw ns*nt samples sliced from nx*ny*nz samples from
  // an array. The dimensions ns and nt are chosen from array dimensions 
  // nx, ny, and nz, depending on which axis of the array is orthogonal to 
  // the plane of this panel.
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
  //
  // This panel may or may not draw its entire mosaic of ms*mt textures;
  // the corner points of the frame containing this panel determine the
  // subset of the ms*mt textures drawn. For fast drawing, this panel 
  // maintains a cache of the required textures in an array[mt][ms].
  // In this array, only those textures that are required for drawing
  // are non-null.

  // Sampling of panels and textures, as described above.
  int _ls,_lt; // numbers of samples per textures
  int _ms,_mt; // numbers of textures in panel
  int _ns,_nt; // numbers of samples in panel
  double _ds,_dt; // sampling intervals in panel
  double _fs,_ft; // first sample values in panel

  // The texture cache; textures required for drawing are non-null.
  private GlTextureName[][] _tn; // array[_mt][_ms] of texture names

  // The subset of samples that must be drawn depends on frame corner points.
  int _kxmin,_kymin,_kzmin; // min sample-in-array indices
  int _kxmax,_kymax,_kzmax; // max sample-in-array indices
  int _ksmin,_ktmin; // min sample-in-panel indices
  int _ksmax,_ktmax; // max sample-in-panel indices
  int _jsmin,_jtmin; // min texture-in-cache indices
  int _jsmax,_jtmax; // max texture-in-cache indices

  // Used when computing a texture.
  int[] _pixels; // array[_lt][_ls] of pixels for one texture

  private void drawTextures() {

    // If parent is not a frame, do not know where to draw.
    AxisAlignedFrame frame = getFrame();
    if (frame==null)
      return;

    // If necessary, update sampling.
    Axis axis = frame.getAxis();
    if (_axis!=axis)
      updateSampling(axis,_sx,_sy,_sz);

    // If necessary, update bounds.
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
      updateBounds(xmin,ymin,zmin,xmax,ymax,zmax);

    // Draw textures.
    glShadeModel(GL_FLAT);
    glEnable(GL_TEXTURE_2D);
    glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_REPLACE);
    float ss = 1.0f/(float)(_ls-1);
    float tt = 1.0f/(float)(_lt-1);
    double xa = 0.5*(_xmin+_xmax);
    double ya = 0.5*(_ymin+_ymax);
    double za = 0.5*(_zmin+_zmax);
    for (int jt=_jtmin; jt<=_jtmax; ++jt) {
      for (int js=_jsmin; js<=_jsmax; ++js) {
        int ks0 = js*(_ls-1);
        int kt0 = jt*(_lt-1);
        int ks1 = ks0+_ls-1;
        int kt1 = kt0+_lt-1;
        ks0 = max(_ksmin,ks0);
        kt0 = max(_ktmin,kt0);
        ks1 = min(_ksmax,ks1);
        kt1 = min(_ktmax,kt1);
        float s0 = (float)(ks0%(_ls-1))*ss;
        float t0 = (float)(kt0%(_lt-1))*tt;
        float s1 = (float)(ks1-ks0)*ss;
        float t1 = (float)(kt1-kt0)*tt;
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
        glBindTexture(GL_TEXTURE_2D,0);
      }
    }
    glDisable(GL_TEXTURE_2D);
  }

  private void updateSampling(
    Axis axis, Sampling sx, Sampling sy, Sampling sz) 
  {
    disposeTextures();
    int nx = _sx.getCount();
    int ny = _sy.getCount();
    int nz = _sz.getCount();
    double dx = _sx.getDelta();
    double dy = _sy.getDelta();
    double dz = _sz.getDelta();
    double fx = _sx.getFirst();
    double fy = _sy.getFirst();
    double fz = _sz.getFirst();
    _axis = axis;
    _sx = sx;
    _sy = sy;
    _sz = sz;
    _ls = 16; // for debugging
    _lt = 16; // for debugging
    //_ls = 64;
    //_lt = 64;
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
    _pixels = new int[_ls*_lt];
  }

  private void updateBounds(
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
    boolean stale = false;
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
            tn = makeTexture(js,jt);
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

  private GlTextureName makeTexture(int js, int jt) {
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
    float scale = 1.0f/(float)(_ls+_lt-2);
    for (int it=0; it<_lt; ++it) {
      for (int is=0; is<_ls; ++is) {
        float gray = scale*(float)(is+it);
        _pixels[is+_ls*it] = pixelFromFloat(gray);
      }
    }
    glPixelStorei(GL_UNPACK_ALIGNMENT,1);
    GlTextureName tn = _tn[jt][js];
    glBindTexture(GL_TEXTURE_2D,tn.name());
    glTexSubImage2D(
      GL_TEXTURE_2D,0,0,0,_ls,_lt,GL_RGBA,GL_UNSIGNED_BYTE,_pixels);
    glBindTexture(GL_TEXTURE_2D,0);
  }

  private int pixelFromFloat(float f) {
    int r = (int)(f*255.0f);
    int g = (int)(f*255.0f);
    int b = (int)(f*255.0f);
    int a = 255;
    return ((r&0xff)<<0)|((g&0xff)<<8)|((b&0xff)<<16)|((a&0xff)<<24);
  }
}
