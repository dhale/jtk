/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.awt.*;
import java.awt.image.*;

/**
 * An index color model (colormap) in which the indices are bytes.
 * A byte index color model is a colormap with 256 colors, one for 
 * each possible byte value in the range [0,255], inclusive.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.10.03
 */
public class ByteIndexColorModel extends IndexColorModel {

  /**
   * Grays from black to white.
   */
  public static final ByteIndexColorModel GRAY = getGray();

  /**
   * Colors from red to blue like Matlab's jet color map.
   */
  public static final ByteIndexColorModel JET = getJet();

  /**
   * Hues from red to blue. 
   */
  public static final ByteIndexColorModel HUE = getHue();

  /**
   * Eight complete cycles of hues.
   */
  public static final ByteIndexColorModel PRISM = getPrism();

  /**
   * Colors from red to white to blue.
   */
  public static final ByteIndexColorModel RED_WHITE_BLUE = getRedWhiteBlue();

  /**
   * Gets a linear gray black-to-white color model.
   * @return the color model.
   */
  public static ByteIndexColorModel getGray() {
    return getGray(0.0,1.0);
  }

  /**
   * Gets a linear gray color model for the specified gray levels. Gray
   * levels equal to 0.0 and 1.0 correspond to colors black and white, 
   * respectively.
   * @param g0 the gray level corresponding to byte value 0.
   * @param g255 the gray level corresponding to byte value 255.
   * @return the color model.
   */
  public static ByteIndexColorModel getGray(double g0, double g255) {
    Color[] c = new Color[256];
    for (int i=0; i<256; ++i) {
      float g = (float)(g0+i*(g255-g0)/255.0);
      c[i] = new Color(g,g,g);
    }
    return new ByteIndexColorModel(c);
  }

  /**
   * Gets a red-to-blue color model like Matlab's jet color map.
   * @return the color model.
   */
  public static ByteIndexColorModel getJet() {
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
    return new ByteIndexColorModel(c);
  }

  /**
   * Gets a color model with eight complete cycles of hues.
   * @return the color model.
   */
  public static ByteIndexColorModel getPrism() {
    return getHue(0.0,8.0);
  }

  /**
   * Gets a red-to-blue linear hue color model.
   * @return the color model.
   */
  public static ByteIndexColorModel getHue() {
    return getHue(0.0,0.67);
  }

  /**
   * Gets a linear hue colormap for the specified hues. Hues equal to 
   * 0.00, 0.33, and 0.67, and 1.00 correspond approximately to the 
   * colors red, green, blue, and red, respectively.
   * @param h0 the hue corresponding to byte value 0.
   * @param h255 the hue corresponding to byte value 255.
   * @return the color model.
   */
  public static ByteIndexColorModel getHue(double h0, double h255) {
    Color[] c = new Color[256];
    for (int i=0; i<256; ++i) {
      float h = (float)(h0+i*(h255-h0)/255.0);
      c[i] = Color.getHSBColor(h,1.0f,1.0f);
    }
    return new ByteIndexColorModel(c);
  }

  /**
   * Gets a red-white-blue color model.
   * @return the color model.
   */
  public static ByteIndexColorModel getRedWhiteBlue() {
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
    return new ByteIndexColorModel(c);
  }

  /**
   * Constructs a byte index color model for the specified colors.
   * @param c array[256] of colors.
   */
  public ByteIndexColorModel(Color[] c) {
    this(getReds(c),getGreens(c),getBlues(c));
  }

  /**
   * Constructs a byte index color model for the specified colors.
   * Red, green, and blue components are floats in the range [0,1],
   * inclusive.
   * @param r array[256] of reds.
   * @param g array[256] of greens.
   * @param b array[256] of blues.
   */
  public ByteIndexColorModel(float[] r, float[] g, float[] b) {
    this(getBytes(r),getBytes(g),getBytes(b));
  }

  /**
   * Constructs a byte index color model for the specified colors.
   * Red, green, and blue components are bytes in the range [0,255],
   * inclusive.
   * @param r array[256] of reds.
   * @param g array[256] of greens.
   * @param b array[256] of blues.
   */
  public ByteIndexColorModel(byte[] r, byte[] g, byte[] b) {
    super(8,256,r,g,b);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

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
