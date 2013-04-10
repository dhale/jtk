/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.image.IndexColorModel;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * A plot panel with three pixels views of slices from a 3-D array.
 * <p>
 * Pixels views are arranged in one of four ways, depending on the
 * orientation of this panel. All arrangements are L-shaped with an
 * empty tile in the upper-right corner of a 2x2 mosaic contained in
 * this panel.
 * <p>
 * This class has numerous methods that enable changing various attributes 
 * of the three pixels views while keeping them consistent. Although such
 * attributes can be set independently for each pixels view, one should
 * use the methods in this class when possible.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.08.11
 */
public class PlotPanelPixels3 extends PlotPanel {
  private static final long serialVersionUID = 1L;

  /**
   * Orientations of the plot panel are defined by the orientation of the
   * tile in the lower-left corner of the 2x2 mosaic. The four possible
   * orientations are X1DOWN_X2RIGHT, X1DOWN_X3RIGHT, X1RIGHT_X2UP, and
   * X1RIGHT_X3UP. The other two tiles are oriented so that they share 
   * axes with the lower-left tile.
   * <p>
   * The orientations of the x2 and x3 axes can be determined automatically
   * from that of the x1 axis to minimize the size of the empty tile in 
   * the upper-right corner. The two possible orientations of the x1 axis 
   * are X1DOWN and X1RIGHT. If one of these is specified, then one of the
   * other four orientations will automatically be chosen accordingly.
   * <p>
   * The default orientation is X1DOWN, which will result in either an
   * X1DOWN_X2RIGHT or X1DOWN_X3RIGHT orientation being used.
   */
  public enum Orientation {
    X1DOWN,
    X1DOWN_X2RIGHT,
    X1DOWN_X3RIGHT,
    X1RIGHT,
    X1RIGHT_X2UP,
    X1RIGHT_X3UP
  }

  /**
   * Placement of labeled axes. The default axes placement is LEFT_BOTTOM.
   */
  public enum AxesPlacement {
    LEFT_BOTTOM,
    NONE
  }

  /**
   * Constructs a plot panel with three pixels views.
   * @param orientation the orientation of views.
   * @param axesPlacement the placement of axes.
   * @param s1 sampling of the 1st dimension.
   * @param s2 sampling of the 2nd dimension.
   * @param s3 sampling of the 3rd dimension.
   * @param f 3-D array of floats.
   */
  public PlotPanelPixels3(
    Orientation orientation, AxesPlacement axesPlacement,
    Sampling s1, Sampling s2, Sampling s3, float[][][] f)
  {
    this(orientation,axesPlacement,s1,s2,s3,new SimpleFloat3(f));
  }

  /**
   * Constructs a plot panel with three pixels views.
   * @param orientation the orientation of views.
   * @param axesPlacement the placement of axes.
   * @param s1 sampling of the 1st dimension.
   * @param s2 sampling of the 2nd dimension.
   * @param s3 sampling of the 3rd dimension.
   * @param f array of 3-D arrays of floats.
   */
  public PlotPanelPixels3(
    Orientation orientation, AxesPlacement axesPlacement,
    Sampling s1, Sampling s2, Sampling s3, float[][][][] f)
  {
    this(orientation,axesPlacement,s1,s2,s3,toFloat3(f));
  }
  private static Float3[] toFloat3(float[][][][] f) {
    Float3[] f3 = new Float3[f.length];
    for (int i=0; i<f.length; ++i)
      f3[i] = new SimpleFloat3(f[i]);
    return f3;
  }

  /**
   * Constructs a plot panel with three pixels views.
   * @param orientation the orientation of views.
   * @param axesPlacement the placement of axes.
   * @param s1 sampling of the 1st dimension.
   * @param s2 sampling of the 2nd dimension.
   * @param s3 sampling of the 3rd dimension.
   * @param f3 abstract 3-D array of floats.
   */
  public PlotPanelPixels3(
    Orientation orientation, AxesPlacement axesPlacement,
    Sampling s1, Sampling s2, Sampling s3, Float3 f3)
  {
    this(orientation,axesPlacement,s1,s2,s3,new Float3[]{f3});
  }

  /**
   * Constructs a plot panel with three pixels views.
   * @param orientation the orientation of views.
   * @param axesPlacement the placement of axes.
   * @param s1 sampling of the 1st dimension.
   * @param s2 sampling of the 2nd dimension.
   * @param s3 sampling of the 3rd dimension.
   * @param f3 array of abstract 3-D array of floats, one for each component.
   */
  public PlotPanelPixels3(
    Orientation orientation, AxesPlacement axesPlacement,
    Sampling s1, Sampling s2, Sampling s3, Float3[] f3)
  {
    super(2,2,
      plotPanelOrientation(orientation),
      plotPanelAxesPlacement(axesPlacement));
    _nc = f3.length;
    _f3 = f3;
    _clips = new Clips[_nc];
    for (int ic=0; ic<_nc; ++ic)
      _clips[ic] = new Clips(_f3[ic]);
    init(orientation,axesPlacement,s1,s2,s3);
    setPercentiles(0.0f,100.0f);
  }

  /**
   * Gets the pixels view for the 1-2 slice.
   */
  public PixelsView getPixelsView12() {
    return _p12;
  }

  /**
   * Gets the pixels view for the 1-3 slice.
   */
  public PixelsView getPixelsView13() {
    return _p13;
  }

  /**
   * Gets the pixels view for the 2-3 slice.
   */
  public PixelsView getPixelsView23() {
    return _p23;
  }

  /**
   * Sets sample index for 2-3 slice of 1st dimension.
   * @param k1 sample index for 1st dimension.
   */
  public void setSlice23(int k1) {
    setSlices(k1,_k2,_k3);
  }

  /**
   * Sets sample index for slice of 2nd dimension.
   * @param k2 sample index for 2nd dimension.
   */
  public void setSlice13(int k2) {
    setSlices(_k1,k2,_k3);
  }

  /**
   * Sets sample index for slice of 3rd dimension.
   * @param k3 sample index for 3rd dimension.
   */
  public void setSlice12(int k3) {
    setSlices(_k1,_k2,k3);
  }

  /**
   * Sets sample indices for all slices.
   * @param k1 sample index for 1st dimension.
   * @param k2 sample index for 2nd dimension.
   * @param k3 sample index for 3rd dimension.
   */
  public void setSlices(int k1, int k2, int k3) {
    if (_k1!=k1) {
      _k1 = k1;
      if (_transpose23) {
        _p23.set(_s3,_s2,slice23());
      } else {
        _p23.set(_s2,_s3,slice23());
      }
      _l12.set(lines12a(),lines12b());
      _l13.set(lines13a(),lines13b());
    }
    if (_k2!=k2) {
      _k2 = k2;
      _p13.set(_s1,_s3,slice13());
      _l12.set(lines12a(),lines12b());
      _l23.set(lines23a(),lines23b());
    }
    if (_k3!=k3) {
      _k3 = k3;
      _p12.set(_s1,_s2,slice12());
      _l13.set(lines13a(),lines13b());
      _l23.set(lines23a(),lines23b());
    }
  }

  /**
   * Sets the method for interpolation between samples.
   * @param interpolation the interpolation method.
   */
  public void setInterpolation(PixelsView.Interpolation interpolation) {
    _p12.setInterpolation(interpolation);
    _p13.setInterpolation(interpolation);
    _p23.setInterpolation(interpolation);
  }

  /**
   * Sets the color of lines drawn to indicate slice locations.
   * @param color the line color; if null, no lines are drawn.
   */
  public void setLineColor(Color color) {
    if (color==null) {
      hideLines();
    } else {
      showLines();
      _l12.setLineColor(color);
      _l13.setLineColor(color);
      _l23.setLineColor(color);
    }
  }

  /**
   * Sets the label for axis 1.
   * @param label the label.
   */
  public void setLabel1(String label) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setVLabel(1,label);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      setVLabel(1,label);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      setHLabel(0,label);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      setHLabel(0,label);
    }
  }

  /**
   * Sets the label for axis 2.
   * @param label the label.
   */
  public void setLabel2(String label) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setHLabel(0,label);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      setHLabel(1,label);
      setVLabel(0,label);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      setVLabel(1,label);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      setHLabel(1,label);
      setVLabel(0,label);
    }
  }

  /**
   * Sets the label for axis 3.
   * @param label the label.
   */
  public void setLabel3(String label) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setHLabel(1,label);
      setVLabel(0,label);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      setHLabel(0,label);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      setHLabel(1,label);
      setVLabel(0,label);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      setVLabel(1,label);
    }
  }

  /**
   * Sets the format for axis 1.
   * @param format the format.
   */
  public void setFormat1(String format) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setVFormat(1,format);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      setVFormat(1,format);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      setHFormat(0,format);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      setHFormat(0,format);
    }
  }

  /**
   * Sets the format for axis 2.
   * @param format the format.
   */
  public void setFormat2(String format) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setHFormat(0,format);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      setHFormat(1,format);
      setVFormat(0,format);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      setVFormat(1,format);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      setHFormat(1,format);
      setVFormat(0,format);
    }
  }

  /**
   * Sets the format for axis 3.
   * @param format the format.
   */
  public void setFormat3(String format) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setHFormat(1,format);
      setVFormat(0,format);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      setHFormat(0,format);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      setHFormat(1,format);
      setVFormat(0,format);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      setVFormat(1,format);
    }
  }

  /**
   * Sets the interval for axis 1.
   * @param interval the interval.
   */
  public void setInterval1(double interval) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setVInterval(1,interval);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      setVInterval(1,interval);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      setHInterval(0,interval);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      setHInterval(0,interval);
    }
  }

  /**
   * Sets the interval for axis 2.
   * @param interval the interval.
   */
  public void setInterval2(double interval) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setHInterval(0,interval);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      setHInterval(1,interval);
      setVInterval(0,interval);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      setVInterval(1,interval);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      setHInterval(1,interval);
      setVInterval(0,interval);
    }
  }

  /**
   * Sets the interval for axis 3.
   * @param interval the interval.
   */
  public void setInterval3(double interval) {
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      setHInterval(1,interval);
      setVInterval(0,interval);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      setHInterval(0,interval);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      setHInterval(1,interval);
      setVInterval(0,interval);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      setVInterval(1,interval);
    }
  }

  /**
   * Sets the index color model for this panel.
   * The default color model is a black-to-white gray model.
   * @param colorModel the index color model.
   */
  public void setColorModel(IndexColorModel colorModel) {
    if (_nc==1) {
      _colorMap.setColorModel(colorModel);
      _p12.setColorModel(colorModel);
      _p13.setColorModel(colorModel);
      _p23.setColorModel(colorModel);
    }
  }

  /**
   * Gets the index color model for this panel.
   * @return the index color model; null, if a direct color model is being 
   * used (for multiple color components) instead of an index color model.
   */
  public IndexColorModel getColorModel() {
    return (_nc==1)?_colorMap.getColorModel():null;
  }

  /**
   * Sets the clips for this panel.
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param clipMin the sample value corresponding to color model index 0.
   * @param clipMax the sample value corresponding to color model index 255.
   */
  public void setClips(float clipMin, float clipMax) {
    for (int ic=0; ic<_nc; ++ic)
      _clips[ic].setClips(clipMin,clipMax);
    _p12.setClips(clipMin,clipMax);
    _p13.setClips(clipMin,clipMax);
    _p23.setClips(clipMin,clipMax);
  }

  /**
   * Gets the minimum clip value.
   * @return the minimum clip value.
   */
  public float getClipMin() {
    return _clips[0].getClipMin();
  }

  /**
   * Gets the maximum clip value.
   * @return the maximum clip value.
   */
  public float getClipMax() {
    return _clips[0].getClipMax();
  }

  /**
   * Sets the percentiles used to compute clips for this panel.
   * Calling this method enables the computation of clips from percentiles.
   * Any clip values specified or computed previously will be forgotten.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   */
  public void setPercentiles(float percMin, float percMax) {
    for (int ic=0; ic<_nc; ++ic) {
      _clips[ic].setPercentiles(percMin,percMax);
      float clipMin = _clips[ic].getClipMin();
      float clipMax = _clips[ic].getClipMax();
      _p12.setClips(clipMin,clipMax);
      _p13.setClips(clipMin,clipMax);
      _p23.setClips(clipMin,clipMax);
    }
  }

  /**
   * Gets the minimum percentile.
   * @return the minimum percentile.
   */
  public float getPercentileMin() {
    return _clips[0].getPercentileMin();
  }

  /**
   * Gets the maximum percentile.
   * @return the maximum percentile.
   */
  public float getPercentileMax() {
    return _clips[0].getPercentileMax();
  }

  /**
   * Sets the clips for the specified color component.
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param ic the index (0, 1, 2, or 3) of the color component.
   * @param clipMin the sample value corresponding to color byte value 0.
   * @param clipMax the sample value corresponding to color byte value 255.
   */
  public void setClips(int ic, float clipMin, float clipMax) {
    _clips[ic].setClips(clipMin,clipMax);
    clipMin = _clips[ic].getClipMin();
    clipMax = _clips[ic].getClipMax();
    _p12.setClips(ic,clipMin,clipMax);
    _p13.setClips(ic,clipMin,clipMax);
    _p23.setClips(ic,clipMin,clipMax);
  }

  /**
   * Gets the minimum clip value for the specified color component.
   * @param ic the index (0, 1, 2, or 3) of the color component.
   * @return the minimum clip value.
   */
  public float getClipMin(int ic) {
    return _clips[ic].getClipMin();
  }

  /**
   * Gets the maximum clip value for the specified color component.
   * @param ic the index (0, 1, 2, or 3) of the color component.
   * @return the maximum clip value.
   */
  public float getClipMax(int ic) {
    return _clips[ic].getClipMax();
  }

  /**
   * Sets the percentiles for the specified color component.
   * Calling this method enables the computation of clips from percentiles.
   * Any clip values specified or computed previously will be forgotten.
   * @param ic the index (0, 1, 2, or 3) of the color component.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   */
  public void setPercentiles(int ic, float percMin, float percMax) {
    _clips[ic].setPercentiles(percMin,percMax);
    float clipMin = _clips[ic].getClipMin();
    float clipMax = _clips[ic].getClipMax();
    _p12.setClips(ic,clipMin,clipMax);
    _p13.setClips(ic,clipMin,clipMax);
    _p23.setClips(ic,clipMin,clipMax);
  }

  /**
   * Gets the minimum percentile for the specified color component.
   * @param ic the index (0, 1, 2, or 3) of the color component.
   * @return the minimum percentile.
   */
  public float getPercentileMin(int ic) {
    return _clips[ic].getPercentileMin();
  }

  /**
   * Gets the maximum percentile for the specified color component.
   * @param ic the index (0, 1, 2, or 3) of the color component.
   * @return the maximum percentile.
   */
  public float getPercentileMax(int ic) {
    return _clips[ic].getPercentileMax();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n1,_n2,_n3; // numbers of samples
  private int _k1,_k2,_k3; // slice indices
  private Sampling _s1,_s2,_s3; // sampling
  private PixelsView _p12; // pixels for 12 slices
  private PixelsView _p13; // pixels for 13 slices
  private PixelsView _p23; // pixels for 23 slices
  private PointsView _l12; // lines for 12 slices
  private PointsView _l13; // lines for 13 slices
  private PointsView _l23; // lines for 23 slices
  private boolean _transpose23; // true if transposing 23 to 32
  private int _nc; // number of color components
  private Clips[] _clips; // clips for all three slices
  private Float3[] _f3; // array of abstract 3-D arrays of floats
  private ColorMap _colorMap = new ColorMap(ColorMap.GRAY);
  private Orientation _orientation;
  private AxesPlacement _axesPlacement;

  private static PlotPanel.AxesPlacement plotPanelAxesPlacement(
    AxesPlacement axesPlacement) 
  {
    if (axesPlacement==AxesPlacement.LEFT_BOTTOM) {
      return PlotPanel.AxesPlacement.LEFT_BOTTOM;
    } else {
      return PlotPanel.AxesPlacement.NONE;
    }
  }
  private static PlotPanel.Orientation plotPanelOrientation(
    Orientation orientation) 
  {
    if (orientation==Orientation.X1DOWN ||
        orientation==Orientation.X1DOWN_X2RIGHT ||
        orientation==Orientation.X1DOWN_X3RIGHT) {
      return PlotPanel.Orientation.X1DOWN_X2RIGHT;
    } else {
      return PlotPanel.Orientation.X1RIGHT_X2UP;
    }
  }

  private void init(
    Orientation orientation, AxesPlacement axesPlacement,
    Sampling s1, Sampling s2, Sampling s3) 
  {
    _s1 = s1;
    _s2 = s2;
    _s3 = s3;
    _n1 = s1.getCount();
    _n2 = s2.getCount();
    _n3 = s3.getCount();
    _k1 = _n1/2;
    _k2 = _n2/2;
    _k3 = _n3/2;
    if (orientation==Orientation.X1DOWN) {
      orientation = (_n2>_n3) ?
        Orientation.X1DOWN_X2RIGHT :
        Orientation.X1DOWN_X3RIGHT;
    } else if (orientation==Orientation.X1RIGHT) {
      orientation = (_n2>_n3) ?
        Orientation.X1RIGHT_X2UP :
        Orientation.X1RIGHT_X3UP;
    }
    _orientation = orientation;
    _axesPlacement = axesPlacement;
    if (orientation==Orientation.X1DOWN_X2RIGHT) {
      _transpose23 = false;
      _p12 = addPixels(1,0,s1,s2,slice12());
      _p13 = addPixels(1,1,s1,s3,slice13());
      _p23 = addPixels(0,0,s2,s3,slice23());
      _p12.setOrientation(PixelsView.Orientation.X1DOWN_X2RIGHT);
      _p13.setOrientation(PixelsView.Orientation.X1DOWN_X2RIGHT);
      _p23.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
      _l12 = addPoints(1,0,lines12a(),lines12b());
      _l13 = addPoints(1,1,lines13a(),lines13b());
      _l23 = addPoints(0,0,lines23a(),lines23b());
      _l12.setOrientation(PointsView.Orientation.X1DOWN_X2RIGHT);
      _l13.setOrientation(PointsView.Orientation.X1DOWN_X2RIGHT);
      _l23.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
      getMosaic().setWidthElastic( 0,100*_n2/_n3);
      getMosaic().setHeightElastic(0,100*_n3/_n1);
    } else if (orientation==Orientation.X1DOWN_X3RIGHT) {
      _transpose23 = true;
      _p12 = addPixels(1,1,s1,s2,slice12());
      _p13 = addPixels(1,0,s1,s3,slice13());
      _p23 = addPixels(0,0,s3,s2,slice23());
      _p12.setOrientation(PixelsView.Orientation.X1DOWN_X2RIGHT);
      _p13.setOrientation(PixelsView.Orientation.X1DOWN_X2RIGHT);
      _p23.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
      _l12 = addPoints(1,1,lines12a(),lines12b());
      _l13 = addPoints(1,0,lines13a(),lines13b());
      _l23 = addPoints(0,0,lines23a(),lines23b());
      _l12.setOrientation(PointsView.Orientation.X1DOWN_X2RIGHT);
      _l13.setOrientation(PointsView.Orientation.X1DOWN_X2RIGHT);
      _l23.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
      getMosaic().setWidthElastic( 0,100*_n3/_n2);
      getMosaic().setHeightElastic(0,100*_n2/_n1);
    } else if (orientation==Orientation.X1RIGHT_X2UP) {
      _transpose23 = true;
      _p12 = addPixels(1,0,s1,s2,slice12());
      _p13 = addPixels(0,0,s1,s3,slice13());
      _p23 = addPixels(1,1,s3,s2,slice23());
      _p12.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
      _p13.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
      _p23.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
      _l12 = addPoints(1,0,lines12a(),lines12b());
      _l13 = addPoints(0,0,lines13a(),lines13b());
      _l23 = addPoints(1,1,lines23a(),lines23b());
      _l12.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
      _l13.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
      _l23.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
      getMosaic().setWidthElastic( 0,100*_n1/_n3);
      getMosaic().setHeightElastic(0,100*_n3/_n2);
    } else if (orientation==Orientation.X1RIGHT_X3UP) {
      _transpose23 = false;
      _p12 = addPixels(0,0,s1,s2,slice12());
      _p13 = addPixels(1,0,s1,s3,slice13());
      _p23 = addPixels(1,1,s2,s3,slice23());
      _p12.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
      _p13.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
      _p23.setOrientation(PixelsView.Orientation.X1RIGHT_X2UP);
      _l12 = addPoints(0,0,lines12a(),lines12b());
      _l13 = addPoints(1,0,lines13a(),lines13b());
      _l23 = addPoints(1,1,lines23a(),lines23b());
      _l12.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
      _l13.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
      _l23.setOrientation(PointsView.Orientation.X1RIGHT_X2UP);
      getMosaic().setWidthElastic( 0,100*_n1/_n2);
      getMosaic().setHeightElastic(0,100*_n2/_n3);
    }
  }

  private float[][][] slice12() {
    float[][][] x = new float[_nc][_n2][_n1];
    for (int ic=0; ic<_nc; ++ic)
      _f3[ic].get12(_n1,_n2,0,0,_k3,x[ic]);
    return x;
  }
  private float[][][] slice13() {
    float[][][] x = new float[_nc][_n3][_n1];
    for (int ic=0; ic<_nc; ++ic)
      _f3[ic].get13(_n1,_n3,0,_k2,0,x[ic]);
    return x;
  }
  private float[][][] slice23() {
    float[][][] x = new float[_nc][][];
    float[][] xic = new float[_n3][_n2];
    for (int ic=0; ic<_nc; ++ic) {
      _f3[ic].get23(_n2,_n3,_k1,0,0,xic);
      x[ic] = (_transpose23)?transpose(xic):copy(xic);
    }
    return x;
  }

  private float[][] lines12a() {
    float xa1 = (float)_s1.getValue(0);
    float xk1 = (float)_s1.getValue(_k1);
    float xb1 = (float)_s1.getValue(_n1-1);
    return new float[][]{{xk1,xk1},{xa1,xb1}};
  }
  private float[][] lines12b() {
    float xa2 = (float)_s2.getValue(0);
    float xk2 = (float)_s2.getValue(_k2);
    float xb2 = (float)_s2.getValue(_n2-1);
    return new float[][]{{xa2,xb2},{xk2,xk2}};
  }
  private float[][] lines13a() {
    float xa1 = (float)_s1.getValue(0);
    float xk1 = (float)_s1.getValue(_k1);
    float xb1 = (float)_s1.getValue(_n1-1);
    return new float[][]{{xk1,xk1},{xa1,xb1}};
  }
  private float[][] lines13b() {
    float xa3 = (float)_s3.getValue(0);
    float xk3 = (float)_s3.getValue(_k3);
    float xb3 = (float)_s3.getValue(_n3-1);
    return new float[][]{{xa3,xb3},{xk3,xk3}};
  }
  private float[][] lines23a() {
    if (_transpose23) {
      float xa3 = (float)_s3.getValue(0);
      float xk3 = (float)_s3.getValue(_k3);
      float xb3 = (float)_s3.getValue(_n3-1);
      return new float[][]{{xk3,xk3},{xa3,xb3}};
    } else {
      float xa2 = (float)_s2.getValue(0);
      float xk2 = (float)_s2.getValue(_k2);
      float xb2 = (float)_s2.getValue(_n2-1);
      return new float[][]{{xk2,xk2},{xa2,xb2}};
    }
  }
  private float[][] lines23b() {
    if (_transpose23) {
      float xa2 = (float)_s2.getValue(0);
      float xk2 = (float)_s2.getValue(_k2);
      float xb2 = (float)_s2.getValue(_n2-1);
      return new float[][]{{xa2,xb2},{xk2,xk2}};
    } else {
      float xa3 = (float)_s3.getValue(0);
      float xk3 = (float)_s3.getValue(_k3);
      float xb3 = (float)_s3.getValue(_n3-1);
      return new float[][]{{xa3,xb3},{xk3,xk3}};
    }
  }

  private void showLines() {
    Mosaic mosaic = getMosaic();
    Tile t00 = mosaic.getTile(0,0);
    Tile t10 = mosaic.getTile(1,0);
    Tile t11 = mosaic.getTile(1,1);
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      t00.addTiledView(_l23);
      t10.addTiledView(_l12);
      t11.addTiledView(_l13);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      t00.addTiledView(_l23);
      t10.addTiledView(_l13);
      t11.addTiledView(_l12);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      t00.addTiledView(_l13);
      t10.addTiledView(_l12);
      t11.addTiledView(_l23);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      t00.addTiledView(_l12);
      t10.addTiledView(_l13);
      t11.addTiledView(_l23);
    }
  }
  private void hideLines() {
    Mosaic mosaic = getMosaic();
    Tile t00 = mosaic.getTile(0,0);
    Tile t10 = mosaic.getTile(1,0);
    Tile t11 = mosaic.getTile(1,1);
    if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      t00.removeTiledView(_l23);
      t10.removeTiledView(_l12);
      t11.removeTiledView(_l13);
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      t00.removeTiledView(_l23);
      t10.removeTiledView(_l13);
      t11.removeTiledView(_l12);
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      t00.removeTiledView(_l13);
      t10.removeTiledView(_l12);
      t11.removeTiledView(_l23);
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      t00.removeTiledView(_l12);
      t10.removeTiledView(_l13);
      t11.removeTiledView(_l23);
    }
  }

  TileAxis[] getAxes(int i) {
    Mosaic mosaic = getMosaic();
    TileAxis al0 = mosaic.getTileAxisLeft(0);
    TileAxis al1 = mosaic.getTileAxisLeft(1);
    TileAxis ab0 = mosaic.getTileAxisBottom(0);
    TileAxis ab1 = mosaic.getTileAxisBottom(1);
    TileAxis[] a = null;
    if (_axesPlacement==AxesPlacement.NONE) {
      a = new TileAxis[]{};
    } else if (_orientation==Orientation.X1DOWN_X2RIGHT) {
      if (i==1) a = new TileAxis[]{al1};
      if (i==2) a = new TileAxis[]{ab0};
      if (i==3) a = new TileAxis[]{al0,ab1};
    } else if (_orientation==Orientation.X1DOWN_X3RIGHT) {
      if (i==1) a = new TileAxis[]{al1};
      if (i==2) a = new TileAxis[]{al0,ab1};
      if (i==3) a = new TileAxis[]{ab0};
    } else if (_orientation==Orientation.X1RIGHT_X2UP) {
      if (i==1) a = new TileAxis[]{ab0};
      if (i==2) a = new TileAxis[]{al1};
      if (i==3) a = new TileAxis[]{al0,ab1};
    } else if (_orientation==Orientation.X1RIGHT_X3UP) {
      if (i==1) a = new TileAxis[]{ab0};
      if (i==2) a = new TileAxis[]{al0,ab1};
      if (i==3) a = new TileAxis[]{al1};
    }
    return a;
  }
}
