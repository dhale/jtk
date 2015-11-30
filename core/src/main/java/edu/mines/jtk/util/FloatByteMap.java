/****************************************************************************
Copyright 2007, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.util;

/**
 * Maps float values to unsigned byte values in the range [0,255].
 * This mapping is useful in graphics where the bytes are indices
 * or components of colors in color maps.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.08.08
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
    _clips = new Clips(percMin,percMax,f);
  }

  /**
   * Constructs a map for the specified percentiles and array. 
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public FloatByteMap(double percMin, double percMax, float[][] f) {
    _clips = new Clips(percMin,percMax,f);
  }

  /**
   * Constructs clips for the specified percentiles and array. 
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public FloatByteMap(double percMin, double percMax, float[][][] f) {
    _clips = new Clips(percMin,percMax,f);
  }

  /**
   * Constructs clips for the specified percentiles and array. 
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f3 abstract 3-D array of values; by reference, not by copy.
   */
  public FloatByteMap(double percMin, double percMax, Float3 f3) {
    _clips = new Clips(percMin,percMax,f3);
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
   * Gets byte values corresponding to specified float values.
   * @param f input array of float values to be mapped.
   * @param b output array of unsigned byte values in the range [0,255].
   */
  public void getBytes(float[] f, byte[] b) {
    getBytes(f,b,0);
  }

  /**
   * Gets byte values corresponding to specified float values.
   * @param f input array of float values to be mapped.
   * @param b output array of unsigned byte values in the range [0,255].
   */
  public void getBytes(float[][] f, byte[][] b) {
    int n = f.length;
    for (int i=0; i<n; ++i)
      getBytes(f[i],b[i]);
  }

  /**
   * Gets byte values corresponding to specified float values.
   * @param f input array of float values to be mapped.
   * @param b output array of unsigned byte values in the range [0,255].
   */
  public void getBytes(float[][] f, byte[] b) {
    int n1 = f[0].length;
    int n2 = f.length;
    for (int i2=0; i2<n2; ++i2)
      getBytes(f[i2],b,i2*n1);
  }

  /**
   * Gets byte values corresponding to specified float values.
   * @param f input array of float values to be mapped.
   * @param b output array of unsigned byte values in the range [0,255].
   */
  public void getBytes(float[][][] f, byte[][][] b) {
    int n = f.length;
    for (int i=0; i<n; ++i)
      getBytes(f[i],b[i]);
  }

  /**
   * Gets byte values corresponding to specified float values.
   * @param f input array of float values to be mapped.
   * @param b output array of unsigned byte values in the range [0,255].
   */
  public void getBytes(float[][][] f, byte[] b) {
    int n1 = f[0][0].length;
    int n2 = f[0].length;
    int n3 = f.length;
    for (int i3=0; i3<n3; ++i3)
      for (int i2=0; i2<n2; ++i2)
        getBytes(f[i3][i2],b,i2*n1+i3*n1*n2);
  }

  /**
   * Gets byte values corresponding to specified float values.
   * @param f3 input array of float values to be mapped.
   * @param b output array of unsigned byte values in the range [0,255].
   */
  public void getBytes(Float3 f3, byte[][][] b) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    float[][] fi3 = new float[n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      f3.get12(n1,n2,0,0,i3,fi3);
      getBytes(fi3,b[i3]);
    }
  }

  /**
   * Gets byte values corresponding to specified float values.
   * @param f3 input array of float values to be mapped.
   * @param b output array of unsigned byte values in the range [0,255].
   */
  public void getBytes(Float3 f3, byte[] b) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    float[][] fi3 = new float[n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      f3.get12(n1,n2,0,0,i3,fi3);
      for (int i2=0; i2<n2; ++i2)
        getBytes(fi3[i2],b,i2*n1+i3*n1*n2);
    }
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
  public float getClipMin() {
    return _clips.getClipMin();
  }

  /**
   * Gets the maximum clip value for this mapping.
   * @return the maximum clip value.
   */
  public float getClipMax() {
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
      _fmin = _clips.getClipMin();
      _fmax = _clips.getClipMax();
      _fscale = 256.0f/(_fmax-_fmin);
      _flower = _fmin;
      _fupper = _flower+255.5f/_fscale;
      _dirty = false;
    }
  }

  private void getBytes(float[] f, byte[] b, int j) {
    if (_dirty)
      update();
    int n = f.length;
    for (int i=0; i<n; ++i,++j) {
      float fi = f[i];
      if (fi<_flower) fi = _flower;
      if (fi>_fupper) fi = _fupper;
      b[j] = (byte)((fi-_flower)*_fscale);
    }
  }
}
