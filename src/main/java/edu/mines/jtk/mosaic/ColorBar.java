/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.util.EnumSet;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.awt.ColorMapListener;
import edu.mines.jtk.dsp.Sampling;

/**
 * A color bar is a view of a color map, a mapping from values to colors.
 * A color bar listens for changes to a colormap, and updates itself 
 * accordingly.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.24
 */
public class ColorBar extends IPanel implements ColorMapListener {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new color bar with no label.
   */
  public ColorBar() {
    this(null);
  }

  /**
   * Constructs a new color bar with specified label.
   * @param label the label; null, if none.
   */
  public ColorBar(String label) {
    super();
    _mosaic = new Mosaic(1,1,EnumSet.of(Mosaic.AxesPlacement.RIGHT));
    if (label!=null)
      _mosaic.getTileAxisRight(0).setLabel(label);
    _mosaic.setWidthMinimum(0,15);
    _mosaic.setWidthElastic(0,0);
    _tile = _mosaic.getTile(0,0);
    this.setLayout(new BorderLayout());
    this.add(_mosaic,BorderLayout.CENTER);
  }

  /**
   * Sets the label for this color bar.
   * @param label the label; null, if none.
   */
  public void setLabel(String label) {
    _mosaic.getTileAxisRight(0).setLabel(label);
    revalidate();
  }

  public void setWidthMinimum(int widthMinimum) {
    _mosaic.getTileAxisRight(0).setWidthMinimum(widthMinimum);
    revalidate();
  }

  /**
   * Sets the format for major tic annotation for this color bar.
   * The default format is "%1.4G", which yields a minimum of 1 digit,
   * with up to 4 digits of precision. Any trailing zeros and decimal
   * point are removed from tic annotation.
   * @param format the format.
   */
  public void setFormat(String format) {
    _mosaic.getTileAxisRight(0).setFormat(format);
    revalidate();
  }

  /**
   * Sets the major labeled tic interval in the axis for this color bar.
   * @param interval the major labeled tic interval.
   */
  public void setInterval(double interval) {
    _mosaic.getTileAxisRight(0).setInterval(interval);
    revalidate();
  }

  /**
   * Gets the tile used to display this color bar.
   * @return the tile.
   */
  public Tile getTile() {
    return _tile;
  }

  public void colorMapChanged(ColorMap cm) {
    float vmin = (float)cm.getMinValue();
    float vmax = (float)cm.getMaxValue();
    if (vmin==vmax) {
      vmin -= Math.ulp(vmin);
      vmax += Math.ulp(vmax);
    }
    int nv = 256;
    double dv = (vmax-vmin)/(nv-1);
    double fv = vmin;
    Sampling vs = new Sampling(nv,dv,fv);
    float[][] va = new float[nv][1];
    Color[] ca = new Color[nv];
    for (int iv=0; iv<nv; ++iv) {
      float vi = (float)vs.getValue(iv);
      va[iv][0] = vi;
      ca[iv] = cm.getColor(vi);
    }
    if (_pixels==null) {
      _pixels = new PixelsView(va);
      _pixels.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
      _pixels.setInterpolation(PixelsView.Interpolation.LINEAR);
      _tile.addTiledView(_pixels);
    }
    IndexColorModel icm = ColorMap.makeIndexColorModel(ca);
    _pixels.setClips(vmin,vmax);
    _pixels.setColorModel(icm);
    Sampling s1 = new Sampling(1);
    Sampling s2 = vs;
    _pixels.set(s1,s2,va);
  }

  public void paintToRect(Graphics2D g2d, int x, int y, int w, int h) {
    _mosaic.paintToRect(g2d,x,y,w,h);
  }

  // Override base class implementation.
  public void setFont(Font font) {
    super.setFont(font);
    if (_mosaic!=null)
      _mosaic.setFont(font);
    revalidate();
  }

  // Override base class implementation.
  public void setForeground(Color color) {
    super.setForeground(color);
    if (_mosaic!=null)
      _mosaic.setForeground(color);
  }

  // Override base class implementation.
  public void setBackground(Color color) {
    super.setBackground(color);
    if (_mosaic!=null)
      _mosaic.setBackground(color);
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    paintToRect((Graphics2D)g,0,0,getWidth(),getHeight());
  }

  protected Mosaic getMosaic() {
    return _mosaic;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private Tile _tile;
  private PixelsView _pixels;
}
