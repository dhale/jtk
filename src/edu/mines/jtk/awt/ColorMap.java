/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
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
 * A color map converts a range of double values to colors. 
 * For any double value, a color map
 * (1) transforms the value to an integer pixel in the range [0,255],
 * (2) maps this integer pixel to a color using an index color model.
 * <p>
 * The method {@link #getIndex(double)} performs step (1). For any 
 * double value, that method
 * (1a) clips to a specified min-max range of values,
 * (1b) linearly translates and scales to [0.0,255.0], and
 * (1c) rounds to the nearest integer pixel in [0,255].
 * Extensions of this class may of course override this method to 
 * implement alternative mappings.
 * <p>
 * A color map maintains a list of color map listeners, and notifies those
 * listeners whenever its mapping from values to colors has changed.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.01
 */
public class ColorMap {

  /**
   * Color model for grays from black to white.
   */
  public static final IndexColorModel GRAY = getGray();

  /**
   * Color model for red to blue like Matlab's jet color map.
   */
  public static final IndexColorModel JET = getJet();

  /**
   * Color model for hues from red to blue. 
   */
  public static final IndexColorModel HUE = getHue();

  /**
   * Color model for eight complete cycles of hues.
   */
  public static final IndexColorModel PRISM = getPrism();

  /**
   * Color model for red to white to blue.
   */
  public static final IndexColorModel RED_WHITE_BLUE = getRedWhiteBlue();

  /**
   * Constructs a color map for values in [0,1] the index color model.
   * The integers 0 and 255 must be valid pixels for the color model.
   * @param colorModel the index color model.
   */
  public ColorMap(IndexColorModel colorModel) {
    this(0.0,1.0,colorModel);
  }

  /**
   * Constructs a color map for specified values and index color model.
   * The integers 0 and 255 must be valid pixels for the color model.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   * @param colorModel the index color model.
   */
  public ColorMap(double vmin, double vmax, IndexColorModel colorModel) {
    Check.argument(colorModel.isValid(0),"0 is valid for color model");
    Check.argument(colorModel.isValid(255),"255 is valid for color model");
    _vmin = vmin;
    _vmax = vmax;
    _colorModel = colorModel;
    cacheColors();
  }

  /**
   * Constructs a color map for specified values and colors.
   * The default value range is [0.0,1.0].
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   * @param c array[256] of colors.
   */
  public ColorMap(double vmin, double vmax, Color[] c) {
    this(vmin,vmax,getReds(c),getGreens(c),getBlues(c));
  }

  /**
   * Constructs a color map for specified values and colors. Red, green, 
   * and blue components are bytes in the range [0,255], inclusive.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   * @param r array[256] of reds.
   * @param g array[256] of greens.
   * @param b array[256] of blues.
   */
  public ColorMap(double vmin, double vmax, byte[] r, byte[] g, byte[] b) {
    this(vmin,vmax,new IndexColorModel(8,256,r,g,b));
  }

  /**
   * Constructs a color map for specified values and colors. Red, green, 
   * and blue components are floats in the range [0,1], inclusive.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   * @param r array[256] of reds.
   * @param g array[256] of greens.
   * @param b array[256] of blues.
   */
  public ColorMap(double vmin, double vmax, float[] r, float[] g, float[] b) {
    this(vmin,vmax,getBytes(r),getBytes(g),getBytes(b));
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
