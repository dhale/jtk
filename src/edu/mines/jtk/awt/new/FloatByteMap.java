/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import edu.mines.jtk.util.Clips;

/**
 * Transforms float values to unsigned byte values in [0:255].
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.08.05
 */
public class FloatByteMap {

  /**
   * Constructs a map for specified values.
   * @param f array of values; by reference, not by copy.
   */
  public FloatByteMap(float[] f) {
    _clips = new Clips(f);
  }

  /**
   * Constructs a map for specified values.
   * @param f array of values; by reference, not by copy.
   */
  public FloatByteMap(float[][] f) {
    _clips = new Clips(f);
  }

  /**
   * Constructs a map for specified values.
   * @param f array of values; by reference, not by copy.
   */
  public FloatByteMap(float[][][] f) {
    _clips = new Clips(f);
  }

  /**
   * Constructs a map for specified values.
   * @param f3 abstract 3-D array of values; by reference, not by copy.
   */
  public FloatByteMap(Float3 f3) {
    _clips = new Clips(f3);
  }

  /**
   * Constructs a map for the specified percentiles and array. 
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public FloatByteMap(double percMin, double percMax, float[] f) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _f = f;
  }

  /**
   * Constructs a map for the specified percentiles and array. 
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public FloatByteMap(double percMin, double percMax, float[][] f) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _f = f;
  }

  /**
   * Constructs clips for the specified percentiles and array. 
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public FloatByteMap(double percMin, double percMax, float[][][] f) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _f = f;
  }

  /**
   * Constructs clips for the specified percentiles and array. 
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f3 abstract 3-D array of values; by reference, not by copy.
   */
  public FloatByteMap(double percMin, double percMax, Float3 f3) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _f = f3;
  }

  /**
   * Gets the byte value corresponding to the specified float value.
   * @param f the float value to be mapped.
   * @return the unsigned byte value in the range [0,255].
   */
  public int getByte(float f) {
    if (_dirty)
      update();
    if (f<_flower) f = _flower;
    if (f>_fupper) f = _fupper;
    return (int)((f-_flower)*_fscale);
  }

  /**
   * Sets the clips for this mapping.
   * <p>
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param clipMin the sample value corresponding to byte value 0.
   * @param clipMax the sample value corresponding to color model index 255.
   */
  public void setClips(double clipMin, double clipMax) {
    _clips.setClips(clipMin,clipMax);
  }

  /**
   * Gets the minimum clip value for this mapping.
   * @return the minimum clip value.
   */
  public float getClipMinIndex() {
    return _clips.getClipMin();
  }

  /**
   * Gets the maximum clip value for this mapping.
   * @return the maximum clip value.
   */
  public float getClipMaxIndex() {
    return _clips.getClipMax();
  }

  /**
   * Sets the percentiles used to compute clips for this mapping. The 
   * default percentiles are 0 and 100, which correspond to the minimum 
   * and maximum array values.
   * <p>
   * Calling this method enables the computation of clips from percentiles.
   * Any clip values specified or computed previously will be forgotten.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   */
  public void setPercentiles(double percMin, double percMax) {
    _clips.setPercentiles(percMin,percMax);
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

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Clips _clips;
  private float _fmin,_fmax;
  private float _flower,_fupper,_fscale;
  private boolean _dirty = true;

  // Divide the range [fmin,fmax] into 256 equal intervals with df.
  // Then fmin, fmin+df, fmin+2*df, ..., fmax-df are the left endpoints
  // of the intervals mapped to indices 0, 1, 2, ..., 255. For completeness,
  // we also map the value fmax to index 255. This method computes constants
  // that support this mapping.
  private void update() {
    if (_dirty) {
      _fmin = (float)_clips.getClipMin();
      _fmax = (float)_clips.getClipMax();
      _fscale = 256.0f/(_fmax-_fmin);
      _flower = _fmin;
      _fupper = _flower+255.5f/_fscale;
      _dirty = false;
    }
  }
}
