/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.awt;

import java.awt.image.IndexColorModel;
import static java.lang.Math.floor;

import edu.mines.jtk.util.Check;
import edu.mines.jtk.util.FloatByteMap;

/**
 * Maps arrays of floats to colors.
 * <p>
 * This mapping consists of two parts. Floats are first mapped to bytes,
 * and then those bytes are mapped to colors.
 * <p>
 * The first mapping from floats to bytes is linear between minimum and
 * maximum clip values. Values below and above these min and max values 
 * are clipped. Clips may be computed from percentiles or may be specified 
 * explicitly. By default, the clip min and max values are the minimum
 * and maximum values in specified arrays of floats.
 * <p>
 * The second mapping depends on the number of arrays of floats specified.
 * When only one array is specified, each byte from the float-to-byte
 * mapping is used as an index for an index color model with 256 colors.
 * <p>
 * When three arrays are specified, a float value from each of the three
 * arrays is first mapped independently (using different float-to-byte
 * maps, possibly with different clips) to obtain three bytes. These
 * three bytes are then interpreted directly as color components in 
 * either (red,green,blue) or (hue,saturation,brightness) color models. 
 * Because each color component can have 256 values, millions of colors
 * are possible.
 * <p>
 * When four arrays are specified, the mapping is the same as for three
 * arrays, except that the fourth array corresponds to an alpha (opacity) 
 * component.
 * <p>
 * An index color model may be useful even when three or four arrays are
 * specified, by specifying one of the arrays to serve as a source of 
 * of byte indices. This capability enables a single float value to be
 * mapped to a color, even when three or four arrays of floats are
 * specified. For example, if the index color model contained fully 
 * saturated and bright colors with different hues, then one might
 * specify the array corresponding to the hue component as the source
 * of byte indices. The indexed colors might then be displayed in a color
 * bar.
 * <p>
 * By default, direct color components with indices 0, 1, and 2 are red, 
 * green, and blue, respectively. If HSB components are enabled, then 
 * these same indices instead correspond to components hue, saturation, 
 * and brightness. Enabling HSB adds an extra conversion step to the
 * mapping from floats to colors. After three bytes are computed from
 * three floats, these HSB byte values are then mapped to the nearest 
 * RGB byte values. The hues corresponding to min/max byte values 0
 * and 255 are red (0.00) and blue (0.67), but these hues may be 
 * specified explicitly. This extra conversion step is never performed
 * for indexed color mappings of single float values.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.08.08
 */
public class FloatColorMap extends ColorMap {

  /**
   * Constructs a color map with only indexed colors.
   * @param f an array of floats.
   * @param icm the index color model.
   */
  public FloatColorMap(float[][] f, IndexColorModel icm) {
    this(new float[][][]{f},0,icm);
  }

  /**
   * Constructs a color map with more than one color components.
   * @param f arrays of floats, one array for each color component.
   * @param ic array index of the component for the index color model.
   * @param icm the index color model corresponding to one component.
   */
  public FloatColorMap(float[][][] f, int ic, IndexColorModel icm) {
    super(icm);
    Check.argument(
      f.length==1 || f.length==3 || f.length==4,
      "number of arrays (color components) equals 1, 3, or 4");
    int nc = f.length;
    FloatByteMap[] fbm = new FloatByteMap[nc];
    for (int jc=0; jc<nc; ++jc)
      fbm[jc] = new FloatByteMap(f[jc]);
    _fbmi0 = fbm[0];
    _fbmi1 = (nc>1)?fbm[1]:fbm[0];
    _fbmi2 = (nc>1)?fbm[2]:fbm[0];
    _fbmi3 = (nc>3)?fbm[3]:null;
    _fbmic = fbm[ic];
  }

  /**
   * Enables or disables HSB components. By default, color components 
   * 0, 1, and 2 (if specified) correspond to red, green, and blue. If HSB
   * components are enabled, these indices correspond to hue, saturation,
   * and brightness. Then, as floats are mapped to colors, bytes for HSB
   * are converted to RGB.
   * @param hsb true, to enable HSB components; false, for RGB components.
   */
  public void setHSB(boolean hsb) {
    _hsb = hsb;
  }

  /**
   * Sets hue values corresponding to byte values 0 and 255.
   * These hues are used only when HSB components are enabled.
   * @param hue000 hue corresponding to byte value 0; default is 0.00 (red).
   * @param hue255 hue corresponding to byte value 255; default is 0.67 (blue).
   */
  public void setHues(double hue000, double hue255) {
    _hue000 = (float)hue000;
    _hue255 = (float)hue255;
  }

  /**
   * Gets the color index corresponding to the specified value.
   * @param f the value to be mapped to index.
   * @return the index in the range [0,255].
   */
  public int getIndex(float f) {
    return _fbmic.getByte(f);
  }

  /**
   * Gets the 32-bit color in 0xAARRGGBB format for the specified value.
   * This method uses the index color model.
   * @param f the value to be mapped to a color.
   * @return the pixel.
   */
  public int getARGB(float f) {
    return getColorModel().getRGB(getIndex(f));
  }

  /**
   * Gets the 32-bit color in 0xffRRGGBB format for specified values.
   * This method does not use the index color model.
   * @param f0 the value for color component 0.
   * @param f1 the value for color component 1.
   * @param f2 the value for color component 2.
   * @return the color.
   */
  public int getRGB(float f0, float f1, float f2) {
    int i0 = _fbmi0.getByte(f0);
    int i1 = _fbmi1.getByte(f1);
    int i2 = _fbmi2.getByte(f2);
    return (_hsb)?rgbFromHsb(i0,i1,i2):rgbFromRgb(i0,i1,i2);
  }

  /**
   * Gets the 32-bit color in 0xAARRGGBB format for specified values.
   * This method does not use the index color model.
   * @param f0 the value for color component 0.
   * @param f1 the value for color component 1.
   * @param f2 the value for color component 2.
   * @param f3 the value for color component 3.
   * @return the color.
   */
  public int getARGB(float f0, float f1, float f2, float f3) {
    int i0 = _fbmi0.getByte(f0);
    int i1 = _fbmi1.getByte(f1);
    int i2 = _fbmi2.getByte(f2);
    int i3 = (_fbmi3!=null)?_fbmi3.getByte(f3):0xff;
    return (_hsb)?argbFromHsba(i0,i1,i2,i3):argbFromRgba(i0,i1,i2,i3);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Override

  /**
   * Gets the color index corresponding to the specified value.
   * @param v the value to be mapped to index.
   * @return the index in the range [0,255].
   */
  public int getIndex(double v) {
    return getIndex((float)v);
  }

  /**
   * Gets the minimum value in the range of mapped values.
   * @return the minimum value.
   */
  public double getMinValue() {
    return _fbmic.getClipMin();
  }

  /**
   * Gets the maximum value in the range of mapped values.
   * @return the maximum value.
   */
  public double getMaxValue() {
    return _fbmic.getClipMax();
  }

  /**
   * Sets the min-max range of values mapped to colors. Values outside this 
   * range are clipped. The default range is the min and max clips in the 
   * mapping from floats to bytes.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   */
  public void setValueRange(double vmin, double vmax) {
    if ((float)vmin!=getMinValue() || (float)vmax!=getMaxValue()) {
      _fbmic.setClips((float)vmin,(float)vmax);
      super.setValueRange(vmin,vmax);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  //private int nc; // number of color components: 1, 3, or 4.
  //private int _ic; // array index of component for index color model
  //private FloatByteMap[] _fbm; // float-byte maps, one for each component
  private FloatByteMap _fbmic; // the float-byte map for index color model
  private FloatByteMap _fbmi0; // cached fbm[0]
  private FloatByteMap _fbmi1; // cached fbm[1]
  private FloatByteMap _fbmi2; // cached fbm[2]
  private FloatByteMap _fbmi3; // cached fbm[3]
  private boolean _hsb; // true if components are hue, sat, brightness
  private float _hue000; // corresponds to byte value 000
  private float _hue255; // corresponds to byte value 255

  private int argbFromRgba(int r, int g, int b, int a) {
    return (a<<24) | (r << 16) | (g << 8) | b;
  }

  private int rgbFromRgb(int r, int g, int b) {
    return 0xff000000 | (r << 16) | (g << 8) | b;
  }

  private int argbFromHsba(int hu, int sa, int br, int al) {
    return (al<<24) | rgbFromHsb(hu,sa,br);
  }

  private int rgbFromHsb(int hu, int sa, int br) {
    float scale = 1.0f/255.0f;
    if (hu<0) hu += 256;
    if (sa<0) sa += 256;
    if (br<0) br += 256;
    float hue = _hue000+(_hue255-_hue000)*hu*scale;
    float sat = (float)sa*scale;
    float bri = (float)br*scale;
    int r = 0, g = 0, b = 0;
    if (sat==0.0f) {
      r = g = b = (int)(bri*255.0f+0.5f);
    } else {
      float h = (hue-(float)floor(hue))*6.0f;
      float f = h-(float)floor(h);
      float p = bri*(1.0f-sat);
      float q = bri*(1.0f-sat*f);
      float t = bri*(1.0f-sat*(1.0f-f));
      switch ((int)h) {
      case 0:
        r = (int)(bri*255.0f+0.5f);
        g = (int)(  t*255.0f+0.5f);
        b = (int)(  p*255.0f+0.5f);
        break;
      case 1:
        r = (int)(  q*255.0f+0.5f);
        g = (int)(bri*255.0f+0.5f);
        b = (int)(  p*255.0f+0.5f);
        break;
      case 2:
        r = (int)(  p*255.0f+0.5f);
        g = (int)(bri*255.0f+0.5f);
        b = (int)(  t*255.0f+0.5f);
        break;
      case 3:
        r = (int)(  p*255.0f+0.5f);
        g = (int)(  q*255.0f+0.5f);
        b = (int)(bri*255.0f+0.5f);
        break;
      case 4:
        r = (int)(  t*255.0f+0.5f);
        g = (int)(  p*255.0f+0.5f);
        b = (int)(bri*255.0f+0.5f);
        break;
      case 5:
        r = (int)(bri*255.0f+0.5f);
        g = (int)(  p*255.0f+0.5f);
        b = (int)(  q*255.0f+0.5f);
        break;
      }
    }
    return rgbFromRgb(r,g,b);
  }
}
