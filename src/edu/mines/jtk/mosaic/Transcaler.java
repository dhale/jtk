/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.*;
import edu.mines.jtk.util.Check;

/**
 * Translates and scales normalized coordinates to/from device coordinates.
 * Device x coordinates range from 0 to width-1, and device y coordinates 
 * range from 0 to height-1, where width and height are integers that
 * represent the device <em>size</em>.
 * <p>
 * Normalized coordinates are floating-point coordinates in the range [0:1].
 * By default, the normalized x-coordinate range [0:1] corresponds to 
 * the device x-coordinate range [0:width-1]. Likewise, the normalized 
 * y-coordinate range [0:1] corresponds to the device y-coordinate range
 * [0:height-1]. This mapping can be changed, so that only a subset of the 
 * normalized coordinate unit rectangle is mapped to/from the device rectangle. 
 * That subset rectangle represents the normalized coordinate <em>bounds</em>.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.11
 */
public class Transcaler {

  /**
   * Constructs a transcaler with specified device size and default bounds.
   * @param wd the device width; must not be negative.
   * @param hd the device height; must not be negative.
   */
  public Transcaler(int wd, int hd) {
    setSize(wd,hd);
  }

  /**
   * Converts the specified normalized x-coordinate to device x-coordinate.
   * @param xn the normalized x-coordinate.
   * @return the device x-coordinate.
   */
  public int x(double xn) {
    return (int)(_xshift+_xscale*xn+0.5);
  }

  /**
   * Converts the specified normalized y-coordinate to device y-coordinate.
   * @param yn the normalized y-coordinate.
   * @return the device y-coordinate.
   */
  public int y(double yn) {
    return (int)(_yshift+_yscale*yn+0.5);
  }

  /**
   * Converts the specified device x-coordinate to normalized x-coordinate.
   * @param xd the device x-coordinate.
   * @return the normalized x-coordinate.
   */
  public double x(int xd) {
    return (xd-_xshift)/_xscale;
  }

  /**
   * Converts the specified device y-coordinate to normalized y-coordinate.
   * @param yd the device y-coordinate.
   * @return the normalized y-coordinate.
   */
  public double y(int yd) {
    return (yd-_yshift)/_yscale;
  }

  /**
   * Sets the device coordinate size.
   * @param wd the device coordinate width.
   * @param hd the device coordinate height.
   */
  public void setSize(int wd, int hd) {
    Check.argument(wd>=0.0,"width is non-negative");
    Check.argument(hd>=0.0,"height is non-negative");
    _wd = wd;
    _hd = hd;
    computeShiftAndScale();
  }

  /**
   * Gets the device coordinate width.
   * @return the device coordinate width.
   */
  public int getWidth() {
    return _wd;
  }

  /**
   * Gets the device coordinate height.
   * @return the device coordinate height.
   */
  public int getHeight() {
    return _hd;
  }

  /**
   * Sets the normalized coordinate bounds rectangle.
   * @param xn the normalized x-coordinate minimum.
   * @param yn the normalized y-coordinate minimum.
   * @param wn the normalized x-coordinate width; must not be negative.
   * @param hn the normalized y-coordinate height; must not be negative.
   */
  public void setBounds(double xn, double yn, double wn, double hn) {
    Check.argument(wn>=0.0,"width is non-negative");
    Check.argument(hn>=0.0,"height is non-negative");
    _xn = xn;
    _yn = yn;
    _wn = wn;
    _hn = hn;
    computeShiftAndScale();
  }

  /**
   * Sets the normalized coordinate bounds rectangle.
   * @param bounds the normalized coordinate bounds rectangle.
   */
  public void setBounds(DRectangle bounds) {
    setBounds(bounds.x,bounds.y,bounds.width,bounds.height);
  }

  /**
   * Gets the normalized coordinate bounds rectangle.
   * @return the normalized coordinate bounds rectangle.
   */
  public DRectangle getBounds() {
    return new DRectangle(_xn,_yn,_wn,_hn);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _wd,_hd;
  private double _xn = 0.0;
  private double _yn = 0.0;
  private double _wn = 1.0;
  private double _hn = 1.0;
  private double _xshift,_xscale;
  private double _yshift,_yscale;

  private void computeShiftAndScale() {
    _xn = max(0.0,min(1.0,_xn));
    _yn = max(0.0,min(1.0,_yn));
    _wn = max(0.0,min(1.0-_xn,_wn));
    _hn = max(0.0,min(1.0-_yn,_hn));
    _xscale = (_wd-1)/_wn;
    _xshift = -_xn*_xscale;
    _yscale = (_hd-1)/_hn;
    _yshift = -_yn*_yscale;
  }
}
