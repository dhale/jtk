/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.awt;

import java.awt.Color;
import java.awt.image.IndexColorModel;

import javax.swing.event.EventListenerList;

import edu.mines.jtk.util.Check;

/**
 * Transforms floats to color indices or components.
 * <p>
 * (1) float to byte using clips/percentiles
 * (2) byte to RGB (if indexed) or 
 * <p>
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.08.05
 */
public abstract class FloatColorMap {
public class FloatIndexColorMap extends FloatColorMap {

public class FloatColorMap {

  /**
   * Constructs a color map for specified values and index color model.
   * The integers 0 and 255 must be valid pixels for the color model.
   * @param f array of floats.
   * @param colorModel the index color model.
   */
  public FloatColorMap(float[] f, IndexColorModel colorModel) {
    _iclips = new Clips(f);
  }

  public FloatColors(float[] fr, float[] fg, float[] fb) {
    _rclips = new Clips(fr);
    _gclips = new Clips(fg);
    _bclips = new Clips(fb);
  }

  public FloatColors(float[] fa, float[] fr, float[] fg, float[] fb) {
    _aclips = new Clips(fa);
    _rclips = new Clips(fr);
    _gclips = new Clips(fg);
    _bclips = new Clips(fb);
  }

  /**
   * Gets the color index corresponding to the specified value.
   * @param f the value to be mapped to index.
   * @return the index in the range [0,255].
   */
  public int getIndex(float f) {
    updateIndexMapping();
    return map(f,_imin,_imax,_ishift,_iscale);
  }

  /**
   * Gets the alpha color component corresponding to the specified value.
   * @param f the value to be mapped to alpha.
   * @return the alpha in the range [0,255].
   */
  public int getAlpha(float f) {
    updateDirectMapping();
    return map(f,_amin,_amax,_ashift,_ascale);
  }

  /**
   * Gets the red color component corresponding to the specified value.
   * @param f the value to be mapped to red.
   * @return the red in the range [0,255].
   */
  public int getRed(float f) {
    updateDirectMapping();
    return map(f,_rmin,_rmax,_rshift,_rscale);
  }

  /**
   * Gets the green color component corresponding to the specified value.
   * @param f the value to be mapped to green.
   * @return the green in the range [0,255].
   */
  public int getGreen(float f) {
    updateDirectMapping();
    return map(f,_gmin,_gmax,_gshift,_gscale);
  }

  /**
   * Gets the blue color component corresponding to the specified value.
   * @param f the value to be mapped to blue.
   * @return the blue in the range [0,255].
   */
  public int getBlue(float f) {
    updateDirectMapping();
    return map(f,_bmin,_bmax,_bshift,_bscale);
  }

  /**
   * Gets a packed integer pixel corresponding to the specified values.
   * Packs the pixel as for BufferedImage.TYPE_INT_ARGB. For this type,
   * bits 0:7 = red, bits 8:15 = green, bits 16:23 = blue, and bits 
   * 24-31 = alpha.
   * @param fa the value to be mapped to alpha.
   * @param fr the value to be mapped to red.
   * @param fg the value to be mapped to green.
   * @param fb the value to be mapped to blue.
   * @return the packed integer pixel.
   */
  public int getARGB(float fa, float fr, float fg, float fb) {
    updateDirectMapping();
    int a = map(f,_amin,_amax,_ashift,_ascale);
    int r = map(f,_rmin,_rmax,_rshift,_rscale);
    int g = map(f,_gmin,_gmax,_gshift,_gscale);
    int b = map(f,_bmin,_bmax,_bshift,_bscale);
    return (r&0xff)|((g&0xff)<<8)|((b&0xff)<<16)|((a&0xff)<<24);
  }

  /**
   * Gets a packed integer pixel corresponding to the specified values.
   * Packs the pixel as for BufferedImage.TYPE_INT_RGB. For this type,
   * bits 0:7 = red, bits 8:15 = green, and bits 16:23 = blue.
   * @param fr the value to be mapped to red.
   * @param fg the value to be mapped to green.
   * @param fb the value to be mapped to blue.
   * @return the packed integer pixel.
   */
  public int getRGB(float fr, float fg, float fb) {
    updateDirectMapping();
    int r = map(f,_rmin,_rmax,_rshift,_rscale);
    int g = map(f,_gmin,_gmax,_gshift,_gscale);
    int b = map(f,_bmin,_bmax,_bshift,_bscale);
    return (r&0xff)|((g&0xff)<<8)|((b&0xff)<<16);
  }

  /**
   * Sets the clips for the color index mapping.
   * Sets the clips for the red color component mapping.
   * <p>
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param clipMin the sample value corresponding to color model index 0.
   * @param clipMax the sample value corresponding to color model index 255.
   */
  public void setClipsIndex(double clipMin, double clipMax) {
    _iclips.setClips(clipMin,clipMax);
  }

  /**
   * Gets the minimum clip value for the color index mapping.
   * @return the minimum clip value.
   */
  public float getClipMinIndex() {
    return _iclips.getClipMin();
  }

  /**
   * Gets the maximum clip value for the color index mapping.
   * @return the maximum clip value.
   */
  public float getClipMaxIndex() {
    return _iclips.getClipMax();
  }

  /**
   * Sets the percentiles used to compute clips for the color index mapping. 
   * The default percentiles are 0 and 100, which correspond to the minimum 
   * and maximum array values.
   * <p>
   * Calling this method enables the computation of clips from percentiles.
   * Any clip values specified or computed previously will be forgotten.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   */
  public void setPercentilesIndex(double percMin, double percMax) {
    _iclips.setPercentiles(percMin,percMax);
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

  private boolean _indexed; // true if index color model; false if direct.
  private Clips _iclips,_aclips,_rclips,_gclips,_bclips;
  private float _imin,_imax,_ishift,_iscale; // float to index
  private float _amin,_amax,_ashift,_ascale; // float to alpha
  private float _rmin,_rmax,_rshift,_rscale; // float to red
  private float _gmin,_gmax,_gshift,_gscale; // float to green
  private float _bmin,_bmax,_bshift,_bscale; // float to blue
  private boolean _indexMappingDirty;
  private boolean _directMappingDirty;

  private static void checkIndexColorModel(colorModel) {
    Check.argument(colorModel.isValid(0),"0 is valid for color model");
    Check.argument(colorModel.isValid(255),"255 is valid for color model");
  }

  private static int map(
    float f, float fmin, float fmax, float fshift, float fscale)
  {
    if (f<fmin) f = fmin;
    if (f>fmax) f = fmax;
    return (int)((f-fshift)*fscale);
  }

  private void updateIndexMapping() {
    if (_indexMappingDirty) {
      _imin = _iclips.getClipMin();
      _imax = _iclips.getClipMax();
      _iscale = 256.0f/(_imax-_imin);
      _ishift = _imin;
      _indexMappingDirty = false;
    }
  }

  private void updateDirectMapping() {
    if (_directMappingDirty) {
      _amin = _aclips.getClipMin();
      _amax = _aclips.getClipMax();
      _ascale = 256.0f/(_amax-_amin);
      _ashift = _amin;
      _rmin = _rclips.getClipMin();
      _rmax = _rclips.getClipMax();
      _rscale = 256.0f/(_rmax-_rmin);
      _rshift = _rmin;
      _gmin = _gclips.getClipMin();
      _gmax = _gclips.getClipMax();
      _gscale = 256.0f/(_gmax-_gmin);
      _gshift = _gmin;
      _bmin = _bclips.getClipMin();
      _bmax = _bclips.getClipMax();
      _bscale = 256.0f/(_bmax-_bmin);
      _bshift = _bmin;
      _directMappingDirty = false;
    }
  }

  /**
   * Gets the minimum value in the range of mapped values.
   * @return the minimum value.
   */
  public double getMinValue() {
    return _vmin;
  }

  /**
   * Gets the maximum value in the range of mapped values.
   * @return the maximum value.
   */
  public double getMaxValue() {
    return _vmax;
  }

  /**
   * Gets the index color model used by this color map.
   * @return the index color model.
   */
  public IndexColorModel getColorModel() {
    return _colorModel;
  }

  /**
   * Gets the color corresponding to the specified value.
   * @param v the value to be mapped to a color.
   * @return the color.
   */
  public Color getColor(double v) {
    return _colors[getIndex(v)];
  }

  /**
   * Gets the index in the range [0,255] corresponding to the specified value.
   * @param v the value to be mapped to an index.
   * @return the index in the range [0,255].
   */
  public int getIndex(double v) {

    v = Math.max(_vmin,Math.min(_vmax,v));
    return (int)Math.round(255.0*(v-_vmin)/(_vmax-_vmin));
  }

  /**
   * Sets the min-max range of values mapped to colors. Values outside this 
   * range are clipped. The default range is [0.0,1.0].
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   */
  public void setValueRange(double vmin, double vmax) {
    if (_vmin!=vmin || _vmax!=vmax) {
      _vmin = vmin;
      _vmax = vmax;
      fireColorMapChanged();
    }
  }

  /**
   * Sets the index color model for this color map.
   * @param colorModel the index color model.
   */
  public void setColorModel(IndexColorModel colorModel) {
    _colorModel = colorModel;
    cacheColors();
    fireColorMapChanged();
  }

  /**
   * Adds the specified color map listener.
   * Then notifies the listener that this colormap has changed.
   * @param cml the listener.
   */
  public void addListener(ColorMapListener cml) {
    _colorMapListeners.add(ColorMapListener.class,cml);
    cml.colorMapChanged(this);
  }

  /**
   * Removes the specified color map listener.
   * @param cml the listener.
   */
  public void removeListener(ColorMapListener cml) {
    _colorMapListeners.remove(ColorMapListener.class,cml);
  }

  /**
   * Gets a linear gray black-to-white color model.
   * @return the color model.
   */
  public static IndexColorModel getGray() {
    return getGray(0.0,1.0);
  }

  /**
   * Gets a linear gray color model for the specified gray levels. Gray
   * levels equal to 0.0 and 1.0 correspond to colors black and white, 
   * respectively.
   * @param g0 the gray level corresponding to index value 0.
   * @param g255 the gray level corresponding to index value 255.
   * @return the color model.
   */
  public static IndexColorModel getGray(double g0, double g255) {
    Color[] c = new Color[256];
    for (int i=0; i<256; ++i) {
      float g = (float)(g0+i*(g255-g0)/255.0);
      c[i] = new Color(g,g,g);
    }
    return makeIndexColorModel(c);
  }

  /**
   * Gets a red-to-blue color model like Matlab's jet color map.
   * @return the color model.
   */
  public static IndexColorModel getJet() {
    Color[] c = new Color[256];
    for (int i=0; i<256; ++i) {
      float x = (float)i/255.0f;
      if (x<0.125f) {
        float a = x/0.125f;
        c[i] = new Color(0.0f,0.0f,0.5f+0.5f*a);
      } else if (x<0.375f) {
        float a = (x-0.125f)/0.25f;
        c[i] = new Color(0.0f,a,1.0f);
      } else if (x<0.625f) {
        float a = (x-0.375f)/0.25f;
        c[i] = new Color(a,1.0f,1.0f-a);
      } else if (x<0.875f) {
        float a = (x-0.625f)/0.25f;
        c[i] = new Color(1.0f,1.0f-a,0.0f);
      } else {
        float a = (x-0.875f)/0.125f;
        c[i] = new Color(1.0f-0.5f*a,0.0f,0.0f);
      }
    }
    return makeIndexColorModel(c);
  }

  /**
   * Gets a color model with eight complete cycles of hues.
   * @return the color model.
   */
  public static IndexColorModel getPrism() {
    return getHue(0.0,8.0);
  }

  /**
   * Gets a red-to-blue linear hue color model.
   * @return the color model.
   */
  public static IndexColorModel getHue() {
    return getHue(0.0,0.67);
  }

  /**
   * Gets a linear hue color model for the specified hues. Hues equal to 
   * 0.00, 0.33, and 0.67, and 1.00 correspond approximately to the colors 
   * red, green, blue, and red, respectively.
   * @param h0 the hue corresponding to index value 0.
   * @param h255 the hue corresponding to index value 255.
   * @return the color model.
   */
  public static IndexColorModel getHue(double h0, double h255) {
    Color[] c = new Color[256];
    for (int i=0; i<256; ++i) {
      float h = (float)(h0+i*(h255-h0)/255.0);
      c[i] = Color.getHSBColor(h,1.0f,1.0f);
    }
    return makeIndexColorModel(c);
  }

  /**
   * Gets a red-white-blue color model.
   * @return the color model.
   */
  public static IndexColorModel getRedWhiteBlue() {
    Color[] c = new Color[256];
    for (int i=0; i<256; ++i) {
      float x = (float)i/255.0f;
      if (x<0.5f) {
        float a = x/0.5f;
        c[i] = new Color(1.0f,a,a);
      } else {
        float a = (x-0.5f)/0.5f;
        c[i] = new Color(1.0f-a,1.0f-a,1.0f);
      }
    }
    return makeIndexColorModel(c);
  }

  /**
   * Returns an index color model for the specified array of 256 colors.
   * @param c array[256] of colors.
   * @return the index color model.
   */
  public static IndexColorModel makeIndexColorModel(Color[] c) {
    return new IndexColorModel(8,256,getReds(c),getGreens(c),getBlues(c));
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _vmin = 0.0;
  private double _vmax = 1.0;
  private IndexColorModel _colorModel;
  private Color[] _colors = new Color[256];
  private EventListenerList _colorMapListeners = new EventListenerList();

  private void fireColorMapChanged() {
    Object[] listeners = _colorMapListeners.getListenerList();
    for (int i=listeners.length-2; i>=0; i-=2) {
      ColorMapListener cml = (ColorMapListener)listeners[i+1];
      cml.colorMapChanged(this);
    }
  }

  private void cacheColors() {
    for (int index=0; index<256; ++index)
      _colors[index] = new Color(_colorModel.getRGB(index));
  }

  private static byte[] getReds(Color[] color) {
    int n = color.length;
    byte[] r = new byte[n];
    for (int i=0; i<n; ++i)
      r[i] = (byte)color[i].getRed();
    return r;
  }

  private static byte[] getGreens(Color[] color) {
    int n = color.length;
    byte[] g = new byte[n];
    for (int i=0; i<n; ++i)
      g[i] = (byte)color[i].getGreen();
    return g;
  }

  private static byte[] getBlues(Color[] color) {
    int n = color.length;
    byte[] b = new byte[n];
    for (int i=0; i<n; ++i)
      b[i] = (byte)color[i].getBlue();
    return b;
  }

  private static byte[] getBytes(float[] f) {
    int n = f.length;
    byte[] b = new byte[n];
    for (int i=0; i<n; ++i)
      b[i] = (byte)(f[i]*255.0f+0.5f);
    return b;
  }
}
