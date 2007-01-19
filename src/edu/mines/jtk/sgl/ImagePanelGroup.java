/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.ogl.Gl.*;
import static edu.mines.jtk.util.MathPlus.*;

import java.awt.image.IndexColorModel;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.awt.ColorMapListener;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.ogl.GlTextureName;
import edu.mines.jtk.util.*;

/**
 * A group of image panels that display a single 3-D array.
 * <p>
 * After constructing this group, but before its image panels are drawn, 
 * one should set clips or percentiles. Otherwise, as each image panel 
 * is drawn for the first time, it will compute clip min and max values 
 * using default percentiles. Since all image panels in this group display 
 * the same array, much of this computation is redundant.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.03
 */
public class ImagePanelGroup extends Group {

  /**
   * Constructs an image panel group for all three axes.
   * @param sx sampling of the X axis.
   * @param sy sampling of the Y axis.
   * @param sz sampling of the Z axis.
   * @param f3 abstract 3-D array of floats.
   */
  public ImagePanelGroup(Sampling sx, Sampling sy, Sampling sz, Float3 f3) {
    this(sx,sy,sz,f3,new Axis[]{Axis.X,Axis.Y,Axis.Z});
  }

  /**
   * Constructs image panel group for specified axes.
   * @param sx sampling of the X axis.
   * @param sy sampling of the Y axis.
   * @param sz sampling of the Z axis.
   * @param f3 abstract 3-D array of floats.
   * @param axes array of axes, one for each image panel.
   */
  public ImagePanelGroup(
    Sampling sx, Sampling sy, Sampling sz, Float3 f3, Axis[] axes) 
  {
    _clips = new Clips(_f3);
    addPanels(sx,sy,sz,f3,axes);
  }

  /**
   * Gets an image panel in this group with the specified axis.
   * @param axis the axis.
   * @return the image panel; null, if none has the axis specified.
   */
  public ImagePanel getImagePanel(Axis axis) {
    for (ImagePanel ip:_ipList) {
      if (axis==ip.getFrame().getAxis())
        return ip;
    }
    return null;
  }

  /**
   * Gets an iterator for the image panels in this group.
   * @return the iterator.
   */
  public Iterator<ImagePanel> getImagePanels() {
    return _ipList.iterator();
  }

  /**
   * Sets the index color model for this group.
   * The default color model is a black-to-white gray model.
   * @param colorModel the index color model.
   */
  public void setColorModel(IndexColorModel colorModel) {
    _colorMap.setColorModel(colorModel);
    for (ImagePanel ip:_ipList)
      ip.setColorModel(colorModel);
  }

  /**
   * Gets the index color model for this group.
   * @return the index color model.
   */
  public IndexColorModel getColorModel() {
    return _colorMap.getColorModel();
  }

  /**
   * Sets the clips for this group. Image panels in this group map array 
   * values to bytes, which are then used as indices into a specified color 
   * model. This mapping from array values to byte indices is linear, and 
   * so depends on only these two clip values. The clip minimum value 
   * corresponds to byte index 0, and the clip maximum value corresponds to 
   * byte index 255. Sample values outside of the range [clipMin,clipMax] 
   * are clipped to lie inside this range.
   * <p>
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param clipMin the sample value corresponding to color model index 0.
   * @param clipMax the sample value corresponding to color model index 255.
   */
  public void setClips(double clipMin, double clipMax) {
    _clips.setClips(clipMin,clipMax);
    clipMin = _clips.getClipMin();
    clipMax = _clips.getClipMax();
    for (ImagePanel ip:_ipList)
      ip.setClips(clipMin,clipMax);
    _colorMap.setValueRange(clipMin,clipMax);
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
    double clipMin = _clips.getClipMin();
    double clipMax = _clips.getClipMax();
    for (ImagePanel ip:_ipList)
      ip.setClips(clipMin,clipMax);
    _colorMap.setValueRange(clipMin,clipMax);
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
  // private

  // Sampled floats in an abstract 3-D array.
  private Sampling _sx,_sy,_sz;
  private Float3 _f3;

  // List of image panels.
  private ArrayList<ImagePanel> _ipList;

  // Clips.
  Clips _clips;

  // Color map.
  private ColorMap _colorMap = new ColorMap(0.0,1.0,ColorMap.GRAY);

  private void addPanels(
    Sampling sx, Sampling sy, Sampling sz, Float3 f3, Axis[] axes) 
  {
    _sx = sx;
    _sy = sy;
    _sz = sz;
    _f3 = f3;
    int nx = sx.getCount();
    int ny = sy.getCount();
    int nz = sz.getCount();
    double dx = sx.getDelta();
    double dy = sy.getDelta();
    double dz = sz.getDelta();
    double fx = sx.getFirst();
    double fy = sy.getFirst();
    double fz = sz.getFirst();
    double lx = fx+(nx-1)*dx;
    double ly = fy+(ny-1)*dy;
    double lz = fz+(nz-1)*dz;
    Point3 qmin = new Point3(fx,fy,fz);
    Point3 qmax = new Point3(lx,ly,lz);
    int np = axes.length;
    _ipList = new ArrayList<ImagePanel>(np);
    for (int jp=0; jp<np; ++jp) {
      AxisAlignedQuad aaq = new AxisAlignedQuad(axes[jp],qmin,qmax);
      ImagePanel ip = new ImagePanel(sx,sy,sz,f3);
      ip.setColorModel(getColorModel());
      aaq.getFrame().addChild(ip);
      this.addChild(aaq);
      _ipList.add(ip);
    }
  }
}
