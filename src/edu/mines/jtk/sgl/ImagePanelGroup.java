/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.Iterator;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.awt.ColorMapListener;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.*;

/**
 * A group of image panels that display a single 3D array.
 * Specifically, an image panel group contains one or more axis-aligned 
 * frames, each containing one axis-aligned image panel child.
 * <p>
 * After constructing an image panel group, but before its image panels are 
 * drawn, one should set clips or percentiles. Otherwise, as each image panel 
 * is drawn for the first time, it will compute clip min and max values using 
 * default percentiles. Because all image panels in this group display the 
 * same array, much of this computation is redundant.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.18
 */
public class ImagePanelGroup extends Group {

  /**
   * Constructs an image panel group for all three axes.
   * Assumes default unit sampling.
   * @param f 3D array of floats.
   */
  public ImagePanelGroup(float[][][] f) {
    this(new Sampling(f[0][0].length),
         new Sampling(f[0].length),
         new Sampling(f.length),
         f);
  }

  /**
   * Constructs an image panel group for all three axes.
   * @param s1 sampling of 1st dimension (Z axis).
   * @param s2 sampling of 2nd dimension (Y axis).
   * @param s3 sampling of 3rd dimension (X axis).
   * @param f 3D array of floats.
   */
  public ImagePanelGroup(Sampling s1, Sampling s2, Sampling s3, float[][][] f) {
    this(s1,s2,s3,new SimpleFloat3(f));
  }

  /**
   * Constructs an image panel group for all three axes.
   * @param s1 sampling of 1st dimension (Z axis).
   * @param s2 sampling of 2nd dimension (Y axis).
   * @param s3 sampling of 3rd dimension (X axis).
   * @param f abstract 3D array of floats.
   */
  public ImagePanelGroup(Sampling s1, Sampling s2, Sampling s3, Float3 f) {
    this(s1,s2,s3,f,new Axis[]{Axis.X,Axis.Y,Axis.Z});
  }

  /**
   * Constructs image panel group for specified axes.
   * @param s1 sampling of 1st dimension (Z axis).
   * @param s2 sampling of 2nd dimension (Y axis).
   * @param s3 sampling of 3rd dimension (X axis).
   * @param f 3D array of floats.
   * @param axes array of axes, one for each image panel.
   */
  public ImagePanelGroup(
    Sampling s1, Sampling s2, Sampling s3, float[][][] f, Axis[] axes) 
  {
    this(s1,s2,s3,new SimpleFloat3(f),axes);
  }

  /**
   * Constructs image panel group for specified axes.
   * @param s1 sampling of 1st dimension (Z axis).
   * @param s2 sampling of 2nd dimension (Y axis).
   * @param s3 sampling of 3rd dimension (X axis).
   * @param f abstract 3D array of floats.
   * @param axes array of axes, one for each image panel.
   */
  public ImagePanelGroup(
    Sampling s1, Sampling s2, Sampling s3, Float3 f, Axis[] axes) 
  {
    _clips = new Clips(f);
    addPanels(s1,s2,s3,f,axes);
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
   * Sets the percentiles used to compute clips for this group. The default 
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
    //System.out.println("clip min="+clipMin+" max="+clipMax);
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

  // List of image panels.
  private ArrayList<ImagePanel> _ipList;

  // Clips.
  private Clips _clips;

  // Color map.
  private ColorMap _colorMap = new ColorMap(0.0,1.0,ColorMap.GRAY);

  private void addPanels(
    Sampling s1, Sampling s2, Sampling s3, Float3 f3, Axis[] axes) 
  {
    int nx = s3.getCount();
    int ny = s2.getCount();
    int nz = s1.getCount();
    double dx = s3.getDelta();
    double dy = s2.getDelta();
    double dz = s1.getDelta();
    double fx = s3.getFirst();
    double fy = s2.getFirst();
    double fz = s1.getFirst();
    double lx = fx+(nx-1)*dx;
    double ly = fy+(ny-1)*dy;
    double lz = fz+(nz-1)*dz;
    Point3 qmin = new Point3(fx,fy,fz);
    Point3 qmax = new Point3(lx,ly,lz);
    int np = axes.length;
    _ipList = new ArrayList<ImagePanel>(np);
    for (int jp=0; jp<np; ++jp) {
      AxisAlignedQuad aaq = new AxisAlignedQuad(axes[jp],qmin,qmax);
      ImagePanel ip = new ImagePanel(s1,s2,s3,f3);
      ip.setColorModel(getColorModel());
      aaq.getFrame().addChild(ip);
      this.addChild(aaq);
      _ipList.add(ip);
    }
  }
}
