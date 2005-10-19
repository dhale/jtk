/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A histogram can be used to estimate a probability density function.
 * A histogram is constructed from an array of values by a process
 * called binning.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.10.18
 */
public class Histogram {

  public Histogram(float[] v) {
    int n = v.length;
    initMinMax(v);
    double dbin = _vmax-_vmin;
    if (dbin>0.0) {
      float[] s = Array.copy(v);
      int k25 = (int)rint(0.25*(n-1));
      Array.quickPartialSort(k25,s);
      double v25 = s[k25];
      int k75 = (int)rint(0.75*(n-1));
      Array.quickPartialSort(k75,s);
      double v75 = s[k75];
      if (v25<v75)
        dbin = 2.0*(v75-v25)*pow(n,-1.0/3.0);
    }
    initSampling(v,dbin);
    initCounts(v);
  }

  public Histogram(float[] v, int nbin) {
    initMinMax(v);
    double dbin = (_vmax-_vmin)/max(1,nbin-1);
    initSampling(v,dbin);
    initCounts(v);
  }

  public Histogram(float[] v, double dbin) {
    Check.argument(dbin>0.0,"dbin>0.0");
    initMinMax(v);
    initSampling(v,dbin);
    initCounts(v);
  }

  public double getMinValue() {
    return _vmin;
  }

  public double getMaxValue() {
    return _vmax;
  }

  public int getBinCount() {
    return _sbin.getCount();
  }

  public double getBinDelta() {
    return _sbin.getDelta();
  }

  public double getBinFirst() {
    return _sbin.getFirst();
  }

  public Sampling getBinSampling() {
    return _sbin;
  }

  public double[] getCounts() {
    double[] c = new double[getBinCount()];
    getCounts(c);
    return c;
  }

  public void getCounts(float[] c) {
    int nbin = getBinCount();
    for (int ibin=0; ibin<nbin; ++ibin)
      c[ibin] = (float)_h[ibin];
  }

  public void getCounts(double[] c) {
    int nbin = getBinCount();
    for (int ibin=0; ibin<nbin; ++ibin)
      c[ibin] = _h[ibin];
  }

  public double[] getDensity() {
    int nbin = getBinCount();
    double[] d = new double[nbin];
    getDensity(d);
    return d;
  }

  public void getDensity(float[] d) {
    double s = 1.0/_n;
    int nbin = getBinCount();
    for (int ibin=0; ibin<nbin; ++ibin)
      d[ibin] = (float)(s*_h[ibin]);
  }

  public void getDensity(double[] d) {
    double s = 1.0/_n;
    int nbin = getBinCount();
    for (int ibin=0; ibin<nbin; ++ibin)
      d[ibin] = s*_h[ibin];
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _vmin;
  private double _vmax;
  private Sampling _sbin;
  private int _n;
  private double[] _h;

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
  }

  private void initSampling(float[] v, double dbin) {
    if (dbin==0.0)
      dbin = max(0.5,abs(_vmin)*FLT_EPSILON);
    int nbin = (int)ceil((_vmax-_vmin)/dbin);
    dbin = (_vmax-_vmin)/nbin;
    double fbin = 0.5*(_vmin+_vmax-(nbin-1)*dbin);
    _sbin = new Sampling(nbin,dbin,fbin);
  }

  private void initCounts(float[] v) {
    int nbin = _sbin.getCount();
    double dbin = _sbin.getDelta();
    double fbin = _sbin.getFirst();
    _n = v.length;
    _h = new double[getBinCount()];
    for (int i=0; i<_n; ++i) {
      int ibin = (int)rint((v[i]-fbin)/dbin);
      if (ibin<0)
        ibin = 0;
      if (ibin>=nbin)
        ibin = nbin-1;
      _h[ibin] += 1.0;
    }
  }
}
