/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import static java.lang.Math.*;
import edu.mines.jtk.io.Float3;

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
 * Clips can maintain a reference to an array of values, so that clips 
 * can be updated when percentiles are changed. If not using percentiles,
 * because clipMin and clipMax are specified explicitly, then these arrays 
 * are ignored.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.10
 */
public class Clips {

  /**
   * Constructs clips with specified clipMin and clipMax.
   * Does not use percentiles for an array of values to compute clips.
   * @param clipMin values &lt; clipMin will be clipped to clipMin.
   * @param clipMax values &gt; clipMax will be clipped to clipMax.
   */
  public Clips(double clipMin, double clipMax) {
    _usePercentiles = false;
    _clipMin = (float)clipMin;
    _clipMax = (float)clipMax;
    _clipsDirty = false;
    makeClipsValid();
  }

  /**
   * Constructs clips for the specified array of values. 
   * Uses default percentiles of 0.0 and 100.0.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(float[] f) {
    this(0.0,100.0,f);
  }

  /**
   * Constructs clips for the specified array of values. 
   * Uses default percentiles of 0.0 and 100.0.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(float[][] f) {
    this(0.0,100.0,f);
  }

  /**
   * Constructs clips for the specified array of values. 
   * Uses default percentiles of 0.0 and 100.0.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(float[][][] f) {
    this(0.0,100.0,f);
  }

  /**
   * Constructs clips for the specified array of values. 
   * Uses default percentiles of 0.0 and 100.0.
   * @param f3 abstract 3-D array of values; by reference, not by copy.
   */
  public Clips(Float3 f3) {
    this(0.0,100.0,f3);
  }

  /**
   * Constructs clips for the specified percentiles and array of values. 
   * Computation of clips is deferred until they are got.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(double percMin, double percMax, float[] f) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _clipsDirty = true;
    _f = f;
  }

  /**
   * Constructs clips for the specified percentiles and array of values. 
   * Computation of clips is deferred until they are got.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(double percMin, double percMax, float[][] f) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _clipsDirty = true;
    _f = f;
  }

  /**
   * Constructs clips for the specified percentiles and array of values. 
   * Computation of clips is deferred until they are got.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f array of values; by reference, not by copy.
   */
  public Clips(double percMin, double percMax, float[][][] f) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _clipsDirty = true;
    _f = f;
  }

  /**
   * Constructs clips for the specified percentiles and array of values. 
   * Computation of clips is deferred until they are got.
   * @param percMin the percentile corresponding to clipMin.
   * @param percMax the percentile corresponding to clipMax.
   * @param f3 abstract 3-D array of values; by reference, not by copy.
   */
  public Clips(double percMin, double percMax, Float3 f3) {
    _percMin = (float)percMin;
    _percMax = (float)percMax;
    _clipsDirty = true;
    _f = f3;
  }

  /**
   * Sets the clips.
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
   * Any clip values specified or computed previously will be forgotten.
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

  /**
   * Sets the values for which to compute clips using percentiles.
   * Computation of clips is deferred until they are got.
   * @param f array of values; by reference, not by copy.
   */
  public void setValues(float[] f) {
    _clipsDirty = true;
    _f = f;
  }

  /**
   * Sets the values for which to compute clips using percentiles.
   * Computation of clips is deferred until they are got.
   * @param f array of values; by reference, not by copy.
   */
  public void setValues(float[][] f) {
    _clipsDirty = true;
    _f = f;
  }

  /**
   * Sets the values for which to compute clips using percentiles.
   * Computation of clips is deferred until they are got.
   * @param f array of values; by reference, not by copy.
   */
  public void setValues(float[][][] f) {
    _clipsDirty = true;
    _f = f;
  }

  /**
   * Sets the values for which to compute clips using percentiles.
   * Computation of clips is deferred until they are got.
   * @param f3 abstract 3-D array of values; by reference, not by copy.
   */
  public void setValues(Float3 f3) {
    _clipsDirty = true;
    _f = f3;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private boolean _clipsDirty = true; // true, if clips must be computed
  private float _clipMin; // values < clipMin will be clipped
  private float _clipMax; // values > clipMax will be clipped
  private float _percMin = 0.0f; // may be used to compute _clipMin
  private float _percMax = 100.0f; // may be used to compute _clipMax
  private boolean _usePercentiles = true; // true, if using percentiles
  private Object _f; // the float values

  private void updateClips() {
    if (_clipsDirty && _usePercentiles) {
      boolean clipsComputed = false;

      // If we just need min and max values, ...
      if (_percMin==0.0f && _percMax==100.0f) {
        if (_f instanceof float[]) {
          float[] a = (float[])_f;
          _clipMin = Array.min(a);
          _clipMax = Array.max(a);
          clipsComputed = true;
        } else if (_f instanceof float[][]) {
          float[][] a = (float[][])_f;
          _clipMin = Array.min(a);
          _clipMax = Array.max(a);
          clipsComputed = true;
        } else if (_f instanceof float[][][]) {
          float[][][] a = (float[][][])_f;
          _clipMin = Array.min(a);
          _clipMax = Array.max(a);
          clipsComputed = true;
        } else if (_f instanceof Float3) {
          Float3 f3 = (Float3)_f;
          int n1 = f3.getN1();
          int n2 = f3.getN2();
          int n3 = f3.getN3();
          float[][] a = new float[n2][n1];
          for (int i3=0; i3<n3; ++i3) {
            f3.get12(n1,n2,0,0,i3,a);
            _clipMin = min(_clipMin,Array.min(a));
            _clipMax = max(_clipMax,Array.min(a));
          }
          clipsComputed = true;
        }
      }

      // Else if we must compute percentiles, ...
      else {
        float[] a = null;
        if (_f instanceof float[]) {
          a = Array.copy((float[])_f);
        } else if (_f instanceof float[][]) {
          a = Array.flatten((float[][])_f);
        } else if (_f instanceof float[][][]) {
          a = Array.flatten((float[][][])_f);
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
            _clipMin = Array.min(a);
          } else {
            Array.quickPartialSort(kmin,a);
            _clipMin = a[kmin];
          }
          int kmax = (int)rint(_percMax*0.01*(n-1));
          if (kmax>=n-1) {
            _clipMax = Array.max(a);
          } else {
            Array.quickPartialSort(kmax,a);
            _clipMax = a[kmax];
          }
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
