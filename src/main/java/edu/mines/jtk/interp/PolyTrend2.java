/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.la.DMatrix;
import edu.mines.jtk.la.DMatrixQrd;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Check;

/**
 * A low-order polynomial trend in scattered data f(x1,x2).
 * The trend is computed by least-squares fitting of the scattered
 * data values. This class enables the computed trend to be easily 
 * removed from scattered data and restored to interpolated data.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.01.17
 */
public class PolyTrend2 {

  /**
   * Constructs a trend with specified scattered samples.
   * The specified arrays are referenced; not copied.
   * <p>
   * If insufficient samples are available for the specified order,
   * then a fit is performed with a polynomial of lower order than
   * that specified. Note that an order zero (constant) polynomial
   * fit is always possible when at least one sample is specified.
   * @param order order of polynomial; must be 0, 1, or 2.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public PolyTrend2(int order, float[] f, float[] x1, float[] x2) {
    Check.argument(0<=order,"0<=order");
    Check.argument(order<=2,"order<=2");
    _order = order;
    setSamples(f,x1,x2);
  }

  /**
   * Sets the known (scattered) samples to be fit.
   * The specified arrays are referenced, not copied.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public void setSamples(float[] f, float[] x1, float[] x2) {
    _n = f.length;
    _f = f;
    _x1 = x1;
    _x2 = x2;
    if (_order>0)
      initCenter();
    if (_order==2 && _n>=6) {
      initOrder2();
    } else if (_order==1 && _n>=3) {
      initOrder1();
    } else {
      initOrder0();
    }
  }

  /**
   * Removes this trend from its referenced scattered sample values.
   * Modifies values in the array f referenced by this trend.
   * @throws IllegalStateException if the trend has already been removed.
   */
  public void detrend() {
    Check.state(!_detrend,"trend not yet removed");
    detrend(_f,_x1,_x2);
    _detrend = true;
  }

  /**
   * Restores this trend to its referenced scattered sample valuess.
   * Modifies values in the array f referenced by this trend.
   * @throws IllegalStateException if the trend has not yet been removed.
   */
  public void restore() {
    Check.state(_detrend,"trend has been removed");
    restore(_f,_x1,_x2);
    _detrend = false;
  }

  /**
   * Removes this trend from the specified sample.
   * @param f the sample value.
   * @param x1 the sample x1 coordinate.
   * @param x2 the sample x2 coordinate.
   * @return the sample value with trend removed.
   */
  public float detrend(float f, float x1, float x2) {
    double fi = f-_f0;
    if (_order>0) {
      double y1 = x1-_x1c;
      double y2 = x2-_x2c;
      fi -= _f1*y1+_f2*y2;
      if (_order>1) {
        double y11 = y1*y1;
        double y12 = y1*y2;
        double y22 = y2*y2;
        fi -= _f11*y11+_f12*y12+_f22*y22;
      }
    }
    return (float)fi;
  }

  /**
   * Restores this trend to the specified sample.
   * @param f the sample value.
   * @param x1 the sample x1 coordinate.
   * @param x2 the sample x2 coordinate.
   * @return the sample value with trend restored.
   */
  public float restore(float f, float x1, float x2) {
    double fi = f+_f0;
    if (_order>0) {
      double y1 = x1-_x1c;
      double y2 = x2-_x2c;
      fi += _f1*y1+_f2*y2;
      if (_order>1) {
        double y11 = y1*y1;
        double y12 = y1*y2;
        double y22 = y2*y2;
        fi += _f11*y11+_f12*y12+_f22*y22;
      }
    }
    return (float)fi;
  }

  /**
   * Removes this trend from the specified samples.
   * @param f array of sample values to be detrended.
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public void detrend(float[] f, float[] x1, float[] x2) {
    int n = f.length;
    for (int i=0; i<n; ++i)
      f[i] = detrend(f[i],x1[i],x2[i]);
  }

  /**
   * Restores this trend to the specified samples.
   * @param f array of sample values to be restored.
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public void restore(float[] f, float[] x1, float[] x2) {
    int n = f.length;
    for (int i=0; i<n; ++i)
      f[i] = restore(f[i],x1[i],x2[i]);
  }

  /**
   * Removes this trend from the specified samples.
   * @param f array of sample values to be detrended.
   * @param s1 sampling of x1 coordinates.
   * @param s2 sampling of x2 coordinates.
   */
  public void detrend(float[][] f, Sampling s1, Sampling s2) {
    int n2 = f.length;
    int n1 = f[0].length;
    for (int i2=0; i2<n2; ++i2) {
      float x2 = (float)s2.getValue(i2);
      for (int i1=0; i1<n1; ++i1) {
        float x1 = (float)s1.getValue(i1);
        f[i2][i1] = detrend(f[i2][i1],x1,x2);
      }
    }
  }

  /**
   * Restores this trend to the specified samples.
   * @param f array of sample values to be restored.
   * @param s1 sampling of x1 coordinates.
   * @param s2 sampling of x2 coordinates.
   */
  public void restore(float[][] f, Sampling s1, Sampling s2) {
    int n2 = f.length;
    int n1 = f[0].length;
    for (int i2=0; i2<n2; ++i2) {
      float x2 = (float)s2.getValue(i2);
      for (int i1=0; i1<n1; ++i1) {
        float x1 = (float)s1.getValue(i1);
        f[i2][i1] = restore(f[i2][i1],x1,x2);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _order; // order of polynomial
  private int _n; // number of scattered samples
  private float[] _f,_x1,_x2; // scattered samples f(x1,x2)
  private double _x1c,_x2c; // center of (x1,x2) coordinates
  private double _f0,_f1,_f2,_f11,_f12,_f22; // poly coefficients
  private boolean _detrend; // true, if _f is currently detrended

  private void initCenter() {
    _x1c = 0.0;
    _x2c = 0.0;
    for (int i=0; i<_n; ++i) {
      _x1c += _x1[i];
      _x2c += _x2[i];
    }
    _x1c /= _n;
    _x2c /= _n;
  }
  private void initOrder0() {
    _f0 = _f1 = _f2 = _f11 = _f12 = _f22 = 0.0;
    for (int i=0; i<_n; ++i)
      _f0 += _f[i];
    _f0 /= _n;
  }
  private void initOrder1() {
    _f11 = _f12 = _f22 = 0.0;
    DMatrix a = new DMatrix(_n,3);
    DMatrix b = new DMatrix(_n,1);
    for (int i=0; i<_n; ++i) {
      double x1 = _x1[i]-_x1c;
      double x2 = _x2[i]-_x2c;
      a.set(i,0,1.0);
      a.set(i,1,x1);
      a.set(i,2,x2);
      b.set(i,0,_f[i]);
    }
    DMatrixQrd qrd = new DMatrixQrd(a);
    if (qrd.isFullRank()) {
      DMatrix f = qrd.solve(b);
      _f0 = f.get(0,0);
      _f1 = f.get(1,0);
      _f2 = f.get(2,0);
    } else {
      initOrder0();
    }
  }
  private void initOrder2() {
    DMatrix a = new DMatrix(_n,6);
    DMatrix b = new DMatrix(_n,1);
    for (int i=0; i<_n; ++i) {
      double x1 = _x1[i]-_x1c;
      double x2 = _x2[i]-_x2c;
      a.set(i,0,1.0);
      a.set(i,1,x1);
      a.set(i,2,x2);
      a.set(i,3,x1*x1);
      a.set(i,4,x1*x2);
      a.set(i,5,x2*x2);
      b.set(i,0,_f[i]);
    }
    DMatrixQrd qrd = new DMatrixQrd(a);
    if (qrd.isFullRank()) {
      DMatrix f = qrd.solve(b);
      _f0 = f.get(0,0);
      _f1 = f.get(1,0);
      _f2 = f.get(2,0);
      _f11 = f.get(3,0);
      _f12 = f.get(4,0);
      _f22 = f.get(5,0);
    } else {
      initOrder1();
    }
  }
}
