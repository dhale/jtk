/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * A histogram summarizes the distribution of values v in an array.
 * The range (vmax-vmin) of values v in the array is partitioned uniformly
 * into some number of bins. Each bin then contains the number of values 
 * that lie closest to the center of that bin.
 * <p>
 * If the values v in the array are assumed to be instances of some random
 * variable, then a probability density function may be estimated for that
 * variable by simply dividing the count in each bin by the total number of 
 * values in the array. The resulting fractions are called the densities.
 * <p>
 * The number of bins may be specified or computed automatically. In the
 * automatic case, we compute bin width = 2.0*(v75-v25)/pow(n,1.0/3.0),
 * where n denotes the number of values, and v25 and v75 are the 25th and 
 * 75th percentiles, respectively. The number of bins is then computed by 
 * dividing the range (vmax-vmin) of values by that bin width, rounding 
 * down to the nearest integer. In this way, the number of bins grows 
 * as the cube root of the number of values n.
 * <p>
 * Minimum and maximum values (vmin and vmax) may also be specified or
 * computed automatically. If specified, then only values in the range 
 * [vmin,vmax] are binned, and values outside this range are ignored.
 * <p>
 * Reference: Izenman, A. J., 1991, Recent developments in nonparametric 
 * density estimation: Journal of the American Statistical Association, 
 * v. 86, p. 205-224.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.10.18
 */
public class Histogram {

  /**
   * Constructs a histogram for the specified array of values.
   * Computes the number of bins to obtain a robust estimate of the density 
   * function. Counts and bins all values.
   * @param v the array of values.
   */
  public Histogram(float[] v) {
    initMinMax(v);
    init(v,0);
  }

  /**
   * Constructs a histogram for the specified array of values.
   * Counts and bins all values.
   * @param v the array of values.
   * @param nbin the number of bins.
   */
  public Histogram(float[] v, int nbin) {
    initMinMax(v);
    init(v,nbin);
  }

  /**
   * Constructs a histogram for the specified array of values.
   * Computes the number of bins to obtain a robust estimate of the density 
   * function. Counts and bins only those values in [vmin,vmax].
   * @param v the array of values.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   */
  public Histogram(float[] v, float vmin, float vmax) {
    Check.argument(vmin<=vmax,"vmin<=vmax");
    initMinMax(vmin,vmax);
    init(v,0);
  }

  /**
   * Constructs a histogram for the specified array of values.
   * Counts and bins only those values in [vmin,vmax].
   * @param v the array of values.
   * @param vmin the minimum value.
   * @param vmax the maximum value.
   * @param nbin the number of bins.
   */
  public Histogram(float[] v, float vmin, float vmax, int nbin) {
    Check.argument(vmin<=vmax,"vmin<=vmax");
    initMinMax(vmin,vmax);
    init(v,nbin);
  }

  /**
   * Gets the minimum value (vmin) for this histogram.
   * @return the minimum value.
   */
  public float getMinValue() {
    return _vmin;
  }

  /**
   * Gets the maximum value (vmax) for this histogram.
   * @return the maximum value.
   */
  public float getMaxValue() {
    return _vmax;
  }

  /**
   * Gets the number of bins in this histogram.
   * @return the number of bins.
   */
  public int getBinCount() {
    return _sbin.getCount();
  }

  /**
   * Gets the bin width (delta) for this histogram.
   * @return the bin width.
   */
  public double getBinDelta() {
    return _sbin.getDelta();
  }

  /**
   * Gets the value of the center of the first bin for this histogram.
   * @return the value of the center of the first bin.
   */
  public double getBinFirst() {
    return _sbin.getFirst();
  }

  /**
   * Gets the bin sampling for this histogram.
   * Values sampled are the centers of the bins.
   * @return the bin sampling.
   */
  public Sampling getBinSampling() {
    return _sbin;
  }

  /**
   * Gets the array of counts, one count for each bin.
   * @return array[nbin] of counts, where nbin is the number of bins.
   */
  public long[] getCounts() {
    return copy(_h);
  }

  /**
   * Gets the array of densities, one density for each bin.
   * A density for one bin equals the fraction of values in that bin.
   * @return array[nbin] of densities, where nbin is the number of bins.
   */
  public float[] getDensities() {
    int nbin = getBinCount();
    float[] d = new float[nbin];
    double s = 1.0/_nin;
    for (int ibin=0; ibin<nbin; ++ibin)
      d[ibin] = (float)(s*_h[ibin]);
    return d;
  }

  /**
   * Gets the number of values in the range [vmin,vmax].
   * @return the number of values.
   */
  public long getInCount() {
    return _nin;
  }

  /**
   * Gets the number of values less than vmin.
   * @return the number of values.
   */
  public long getLowCount() {
    return _nlo;
  }

  /**
   * Gets the number of values greater than vmax.
   * @return the number of values.
   */
  public long getHighCount() {
    return _nhi;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private float _vmin; // minimum value to count
  private float _vmax; // maximum value to count
  private boolean _computedMinMax; // true if vmin and vmax not specified
  private Sampling _sbin; // bin sampling
  private long[] _h; // bin counts
  private long _nin; // number of samples in [vmin,vmax]
  private long _nlo; // number of samples < vmin
  private long _nhi; // number of samples > vmax

  private void initMinMax(float[] v) {
    int n = v.length;
    _vmin = _vmax = v[0];
    for (int i=1; i<n; ++i) {
      float vi = v[i];
      if (vi<_vmin)
        _vmin = vi;
      if (vi>_vmax)
        _vmax = vi;
    }
    _computedMinMax = true;
  }

  private void initMinMax(float vmin, float vmax) {
    _vmin = vmin;
    _vmax = vmax;
    _computedMinMax = false;
  }

  /**
   * Returns a copy of the specified array v. 
   * Discards values outside the range [vmin,vmax].
   */
  private float[] trim(float[] v) {
    float[] t;
    if (_computedMinMax) {
      t = copy(v);
    } else {
      int n = v.length;
      t = new float[n];
      int m = 0;
      for (int i=0; i<n; ++i) {
        float vi = v[i];
        if (_vmin<=vi && vi<=_vmax)
          t[m++] = vi;
      }
      if (m<n)
        t = copy(m,t);
    }
    return t;
  }

  /**
   * Initializes the histogram. If nbin is zero, then this method computes
   * the number of bins.
   */
  private void init(float[] v, int nbin) {

    // Bin width must be positive.
    double dbin = (_vmax-_vmin)/max(1,nbin);
    if (dbin==0.0)
      dbin = max(1.0,2.0*abs(_vmin)*FLT_EPSILON);

    // If computing the number of bins, ...
    if (nbin==0) {

      // Must have at least one bin.
      nbin = 1;

      // If might have more than one bin, ...
      if (_vmin<_vmax) {

        // Count only those values in [vmin,vmax].
        float[] t = trim(v);
        int n = t.length;

        // If there exists at least one such value, ...
        if (n>0) {

          // Compute 25th and 75th percentiles.
          int k25 = (int)rint(0.25*(n-1));
          quickPartialSort(k25,t);
          double v25 = t[k25];
          int k75 = (int)rint(0.75*(n-1));
          quickPartialSort(k75,t);
          double v75 = t[k75];

          // Compute number and width of bins.
          if (v25<v75) {
            dbin = 2.0*(v75-v25)*pow(n,-1.0/3.0);
            nbin = max(1,(int)floor((_vmax-_vmin)/dbin));
            dbin = (_vmax-_vmin)/nbin;
          }
        }
      }
    }
    double fbin = _vmin+0.5*dbin;
    _sbin = new Sampling(nbin,dbin,fbin);

    // Count binned values.
    double vscl = 1.0/dbin;
    int n = v.length;
    _nlo = 0;
    _nhi = 0;
    _h = new long[nbin];
    _nin = 0;
    for (int i=0; i<n; ++i) {
      float vi = v[i];
      if (vi<_vmin) {
        ++_nlo;
      } else if (vi>_vmax) {
        ++_nhi;
      } else {
        int ibin = (int)rint((vi-fbin)*vscl);
        if (ibin<0) {
          ibin = 0;
        } else if (ibin>=nbin) {
          ibin = nbin-1;
        }
        ++_h[ibin];
        ++_nin;
      }
    }
  }
}
