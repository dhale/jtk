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

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Clips (clipMin,clipMax) are lower and upper bounds for arrays of values.
 * The name "clips" comes from the way these bounds are used, as when 
 * displaying data. Typically, below/above clipMin/clipMax are clipped 
 * to these bounds. Clips maintain the bounds, but do not perform the 
 * clipping.
 * <p>
 * The lower bound clipMin must be less than the upper bound clipMax;
 * this restriction is silently enforced by all methods of this class.
 * This means that clips returned may not exactly equal those specified.
 * <p>
 * This class is most useful when computing clips from percentiles.
 * For example, the bounds clipMin and clipMax may correspond to 
 * percMin=1% and percMax=99%, respectively. The default percentiles
 * percMin=0% and percMax=100% correspond to minimum and maximum values.
 * <p>
 * Clips maintain a reference to an array of values, so that clips can be 
 * updated when percentiles are changed. If not using percentiles, because 
 * clipMin and clipMax are specified explicitly, then these arrays are 
 * ignored.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.10
 */
public class Clips {

  /**
   * Constructs clips for the specified array. 
   * Uses default percentiles of 0.0 and 100.0.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(float[] f) {
    this(0.0,100.0,f);
  }

  /**
   * Constructs clips for the specified array. 
   * Uses default percentiles of 0.0 and 100.0.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(float[][] f) {
    this(0.0,100.0,f);
  }

  /**
   * Constructs clips for the specified array. 
   * Uses default percentiles of 0.0 and 100.0.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(float[][][] f) {
    this(0.0,100.0,f);
  }

  /**
   * Constructs clips for the specified array. 
   * Uses default percentiles of 0.0 and 100.0.
   * @param f3 abstract 3-D array of values; by reference, not by copy.
   */
  public Clips(Float3 f3) {
    this(0.0,100.0,f3);
  }

  /**
   * Constructs clips for the specified percentiles and array. 
   * Computation of clips is deferred until they are got.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(double percMin, double percMax, float[] f) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _f = f;
  }

  /**
   * Constructs clips for the specified percentiles and array. 
   * Computation of clips is deferred until they are got.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(double percMin, double percMax, float[][] f) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _f = f;
  }

  /**
   * Constructs clips for the specified percentiles and array. 
   * Computation of clips is deferred until they are got.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(double percMin, double percMax, float[][][] f) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _f = f;
  }

  /**
   * Constructs clips for the specified percentiles and array. 
   * Computation of clips is deferred until they are got.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f3 abstract 3-D array of values; by reference, not by copy.
   */
  public Clips(double percMin, double percMax, Float3 f3) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _f = f3;
  }

  /**
   * Sets the array of values for these clips.
   * These values are used only when computing clips from percentiles.
   * @param f array of values; by reference, not by copy.
   */
  public void setArray(float[] f) {
    _f = f;
    _clipsDirty = true;
  }

  /**
   * Sets the array of values for these clips.
   * These values are used only when computing clips from percentiles.
   * @param f array of values; by reference, not by copy.
   */
  public void setArray(float[][] f) {
    _f = f;
    _clipsDirty = true;
  }

  /**
   * Sets the array of values for these clips.
   * These values are used only when computing clips from percentiles.
   * @param f array of values; by reference, not by copy.
   */
  public void setArray(float[][][] f) {
    _f = f;
    _clipsDirty = true;
  }

  /**
   * Sets the array of values for these clips.
   * These values are used only when computing clips from percentiles.
   * @param f3 array of values; by reference, not by copy.
   */
  public void setArray(Float3 f3) {
    _f = f3;
    _clipsDirty = true;
  }

  /**
   * Sets the clip min and max values explicitly.
   * Calling this method disables the computation of clips from percentiles.
   * Any clip values computed or specified previously will be forgotten.
   * @param clipMin values &lt; clipMin will be clipped to clipMin.
   * @param clipMax values &gt; clipMax will be clipped to clipMax.
   */
  public void setClips(double clipMin, double clipMax) {
    Check.argument(clipMin<clipMax,"clipMin<clipMax");
    if (_clipMin!=(float)clipMin || 
        _clipMax!=(float)clipMax || 
        _usePercentiles) {
      _usePercentiles = false;
      _clipMin = (float)clipMin;
      _clipMax = (float)clipMax;
      _clipsDirty = false;
    }
  }

  /**
   * Gets the minimum clip value.
   * @return the minimum clip value.
   */
  public float getClipMin() {
    updateClips();
    return _clipMin;
  }

  /**
   * Gets the maximum clip value.
   * @return the maximum clip value.
   */
  public float getClipMax() {
    updateClips();
    return _clipMax;
  }

  /**
   * Sets the percentiles used to compute clips for this view. The default 
   * percentiles are 0.0 and 100.0, which correspond to the minimum and 
   * maximum values in the array of values to be clipped.
   * <p>
   * Calling this method enables the computation of clips from percentiles.
   * Any clip min and max specified or computed previously will be forgotten.
   * <p>
   * Clip min and max values can only be computed if an array of values has
   * been specified. If clips were constructed without such an array, then
   * both percentiles and an array of values should be set to enable the
   * use of percentiles.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   */
  public void setPercentiles(double percMin, double percMax) {
    Check.argument(0.0<=percMin,"0<=percMin");
    Check.argument(percMin<percMax,"percMin<percMax");
    Check.argument(percMax<=100.0,"percMax<=100");
    if (_percMin!=(float)percMin || 
        _percMax!=(float)percMax || 
        !_usePercentiles) {
      _usePercentiles = true;
      _percMin = (float)percMin;
      _percMax = (float)percMax;
      _clipsDirty = true;
    }
  }

  /**
   * Gets the minimum percentile.
   * @return the minimum percentile.
   */
  public float getPercentileMin() {
    return _percMin;
  }

  /**
   * Gets the maximum percentile.
   * @return the maximum percentile.
   */
  public float getPercentileMax() {
    return _percMax;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private boolean _clipsDirty = true; // true, if clips must be computed
  private float _clipMin; // values < clipMin will be clipped
  private float _clipMax; // values > clipMax will be clipped
  private float _percMin = 0.0f; // may be used to compute _clipMin
  private float _percMax = 100.0f; // may be used to compute _clipMax
  private boolean _usePercentiles = true; // true, if using percentiles
  private Object _f; // array used to compute clips from percentiles

  private void updateClips() {
    if (_clipsDirty && _usePercentiles) {
      boolean clipsComputed = false;

      // If we just need min and max values, ...
      if (_percMin==0.0f && _percMax==100.0f) {
        if (_f instanceof float[]) {
          float[] a = (float[])_f;
          _clipMin = min(a);
          _clipMax = max(a);
          clipsComputed = true;
        } else if (_f instanceof float[][]) {
          float[][] a = (float[][])_f;
          _clipMin = min(a);
          _clipMax = max(a);
          clipsComputed = true;
        } else if (_f instanceof float[][][]) {
          float[][][] a = (float[][][])_f;
          _clipMin = min(a);
          _clipMax = max(a);
          clipsComputed = true;
        } else if (_f instanceof Float3) {
          Float3 f3 = (Float3)_f;
          int n1 = f3.getN1();
          int n2 = f3.getN2();
          int n3 = f3.getN3();
          float[][] a = new float[n2][n1];
          for (int i3=0; i3<n3; ++i3) {
            f3.get12(n1,n2,0,0,i3,a);
            _clipMin = min(_clipMin,min(a));
            _clipMax = max(_clipMax,max(a));
          }
          clipsComputed = true;
        }
      }

      // Else if we must compute percentiles, ...
      else {
        float[] a = null;
        if (_f instanceof float[]) {
          a = copy((float[])_f);
        } else if (_f instanceof float[][]) {
          a = flatten((float[][])_f);
        } else if (_f instanceof float[][][]) {
          a = flatten((float[][][])_f);
        } else if (_f instanceof Float3) {
          Float3 f3 = (Float3)_f;
          int n1 = f3.getN1();
          int n2 = f3.getN2();
          int n3 = f3.getN3();
          a = new float[n1*n2*n3];
          f3.get123(n1,n2,n3,0,0,0,a);
        }
        if (a!=null) {
          int n = a.length;
          int kmin = (int)rint(_percMin*0.01*(n-1));
          if (kmin<=0) {
            _clipMin = min(a);
          } else {
            quickPartialSort(kmin,a);
            _clipMin = a[kmin];
          }
          int kmax = (int)rint(_percMax*0.01*(n-1));
          if (kmax>=n-1) {
            _clipMax = max(a);
          } else {
            quickPartialSort(kmax,a);
            _clipMax = a[kmax];
          }
          clipsComputed = true;
        }
      }

      // If clips were computed, make them valid and not dirty.
      if (clipsComputed) {
        makeClipsValid();
        _clipsDirty = false;
      }
    }
  }
  private void makeClipsValid() {
    if (_clipMin>=_clipMax) {
      double clipAvg = 0.5*(_clipMin+_clipMax);
      double tiny = max(1.0,Math.ulp(1.0f)*abs(clipAvg));
      _clipMin -= tiny;
      _clipMax += tiny;
    }
  }
}
