/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
 * A quantile estimator that enables incremental updates. In other words,
 * the estimator processes samples sequentially in one pass, and does not 
 * require all samples to be sorted, or even partially sorted, in fast 
 * random-access memory. 
 * <p>
 * The quantile estimate is probably not useful for fewer than 10 samples.
 * <p>
 * The estimate is most accurate for cumulative distribution functions 
 * that are smooth in the neighborhood of the desired quantile q. For
 * such distributions, the accuracy of the estimate improves with 
 * successive updates.
 * <p>
 * This class is an implementation of the algorithm published by Jain,
 * R. and Chlamtac, I., 1985, The PP algorithm for dynamic calculation of
 * quantiles and histograms without storing observations:  Comm. ACM, 
 * v. 28, n. 10.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2002.03.03, 2006.07.13
 */
public class Quantiler {

  /**
   * Constructs a quantiler for the specified quantile fraction.
   * @param q the quantile fraction; 0 &lt;= q &lt;= 1 is required.
   */
  public Quantiler(float q) {
    Check.argument(0.0f<=q,"0.0f<=q");
    Check.argument(q<=1.0f,"q<=1.0f");
    _q = q;
    _m0 = -1.0;
    _q2 = 0.0f;
    _inited = (_q==0.0 || _q==1.0);
  }

  /**
   * Constructs a quantiler for the specified quantile fraction and null value.
   * @param q the quantile; 0 &lt;= q &lt;= 1 is required.
   * @param fnull the null value to be ignored in estimating the quantile.
   */
  public Quantiler(float q, float fnull) {
    Check.argument(0.0f<=q,"0.0f<=q");
    Check.argument(q<=1.0f,"q<=1.0f");
    _q = q;
    _fnull = fnull;
    _ignoreNull = true;
    _m0 = -1.0;
    _q2 = fnull;
    _inited = (_q==0.0 || _q==1.0);
  }

  /**
   * Returns the current quantile estimate.
   * @return the current quantile estimate.
   */
  public float estimate() {
    return (float)_q2;
  }

  /**
   * Updates the quantile estimate with the specified sample.
   * @param f the sample used to update the estimate.
   * @return the updated quantile estimate.
   */
  public float update(float f) {
    if (!_inited) {
      initOne(f);
    } else {
      updateOne(f);
    }
    return estimate();
  }

  /**
   * Updates the quantile estimate with the specified samples.
   * @param f array[] of samples used to update the estimate.
   * @return the updated quantile estimate.
   */
  public float update(float[] f) {
    int n = f.length;
    int i = 0;
    for (; !_inited && i<n; ++i)
      initOne(f[i]);
    for (; i<n; ++i)
      updateOne(f[i]);
    return estimate();
  }

  /**
   * Updates the quantile estimate with the specified samples.
   * @param f array[][] of samples used to update the estimate.
   * @return the updated quantile estimate.
   */
  public float update(float[][] f) {
    int n = f.length;
    for (int i=0; i<n; ++i)
      update(f[i]);
    return estimate();
  }

  /**
   * Updates the quantile estimate with the specified samples.
   * @param f array[][][] of samples used to update the estimate.
   * @return the updated quantile estimate.
   */
  public float update(float[][][] f) {
    int n = f.length;
    for (int i=0; i<n; ++i)
      update(f[i]);
    return estimate();
  }

  /**
   * Estimates the specified quantile for the specified and samples.
   * @param q the quantile; 0 &lt;= q &lt;= 1 is required.
   * @param f array[] of samples used to compute the estimate.
   * @return the quantile estimate.
   */
  public static float estimate(float q, float[] f) {
    Quantiler qu = new Quantiler(q);
    return qu.update(f);
  }

  /**
   * Estimates the specified quantile for the specified and samples.
   * @param q the quantile; 0 &lt;= q &lt;= 1 is required.
   * @param f array[][] of samples used to compute the estimate.
   * @return the quantile estimate.
   */
  public static float estimate(float q, float[][] f) {
    Quantiler qu = new Quantiler(q);
    return qu.update(f);
  }

  /**
   * Estimates the specified quantile for the specified and samples.
   * @param q the quantile; 0 &lt;= q &lt;= 1 is required.
   * @param f array[][][] of samples used to compute the estimate.
   * @return the quantile estimate.
   */
  public static float estimate(float q, float[][][] f) {
    Quantiler qu = new Quantiler(q);
    return qu.update(f);
  }

  /**
   * Estimates the specified quantile for the specified null value and samples.
   * @param q the quantile; 0 &lt;= q &lt;= 1 is required.
   * @param fnull the null value to be ignored in estimating the quantile.
   * @param f array[] of samples used to compute the estimate.
   * @return the quantile estimate.
   */
  public static float estimate(float q, float fnull, float[] f) {
    Quantiler qu = new Quantiler(q,fnull);
    return qu.update(f);
  }

  /**
   * Estimates the specified quantile for the specified null value and samples.
   * @param q the quantile; 0 &lt;= q &lt;= 1 is required.
   * @param fnull the null value to be ignored in estimating the quantile.
   * @param f array[][] of samples used to compute the estimate.
   * @return the quantile estimate.
   */
  public static float estimate(float q, float fnull, float[][] f) {
    Quantiler qu = new Quantiler(q,fnull);
    return qu.update(f);
  }

  /**
   * Estimates the specified quantile for the specified null value and samples.
   * @param q the quantile; 0 &lt;= q &lt;= 1 is required.
   * @param fnull the null value to be ignored in estimating the quantile.
   * @param f array[][][] of samples used to compute the estimate.
   * @return the quantile estimate.
   */
  public static float estimate(float q, float fnull, float[][][] f) {
    Quantiler qu = new Quantiler(q,fnull);
    return qu.update(f);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private float _q; // the desired quantile
  private float _fnull; // null sample value, if ignoring nulls
  private double _m0,_m1,_m2,_m3,_m4; // marker positions
  private double _q0,_q1,_q2,_q3,_q4; // marker heights
  private double     _f1,_f2,_f3    ; // desired marker positions
  private double     _d1,_d2,_d3    ; // desired marker position increments
  private boolean _ignoreNull; // true if ignoring fnull samples
  private boolean _inited; // true if estimator has been initialized

  private void initOne(float f) {

    // If ignoring null samples, check for null.
    if (_ignoreNull && f==_fnull) 
      return;

    // If fewer than 5 (non-null) samples, may not complete initialization.
    if (_m0<0.0) {
      _m0 = 0.0;
      _q0 = f;
    } else if (_m1==0.0) {
      _m1 = 1.0;
      _q1 = f;
    } else if (_m2==0.0) {
      _m2 = 2.0;
      _q2 = f;
    } else if (_m3==0.0) {
      _m3 = 3.0;
      _q3 = f;
    } else if (_m4==0.0) {
      _m4 = 4.0;
      _q4 = f;
    }
    if (_m4==0.0)
      return;

    // Initialize marker heights to five samples sorted.
    double[] y = {_q0,_q1,_q2,_q3,_q4};
    for (int i=1; i<5; ++i) {
      for (int j=i; j>0 && y[j-1]>y[j]; --j) {
        double ytemp = y[j-1];
        y[j-1] = y[j];
        y[j] = ytemp;
      }
    }
    _q0 = y[0];
    _q1 = y[1];
    _q2 = y[2];
    _q3 = y[3];
    _q4 = y[4];

    // Initialize desired marker positions.
    _f1 = 2.0*_q;
    _f2 = 4.0*_q;
    _f3 = 2.0+2.0*_q;

    // Compute increments in desired marker positions.
    _d1 = _q/2.0;
    _d2 = _q;
    _d3 = (1.0+_q)/2.0;

    // The estimator is now initialized and the current estimate is q2.
    _inited = true;
  }

  private void updateOne(float f) {
    assert _inited:"quantiler is initialized";

    // If ignoring null samples, check for null.
    if (_ignoreNull && f==_fnull) 
      return;
      
    // If min or max, handle as special case; otherwise, ...
    if (_q==0.0f) {
      if (f<_q2)
        _q2 = f;
    } else if (_q==1.0f) {
      if (f>_q2)
        _q2 = f;
    } else {
    
      // Increment marker locations and update min and max.
      if (f<_q0) {
        _m1 += 1.0;
        _m2 += 1.0;
        _m3 += 1.0;
        _m4 += 1.0;
        _q0 = f;
      } else if (f<_q1) {
        _m1 += 1.0;
        _m2 += 1.0;
        _m3 += 1.0;
        _m4 += 1.0;
      } else if (f<_q2) {
        _m2 += 1.0;
        _m3 += 1.0;
        _m4 += 1.0;
      } else if (f<_q3) {
        _m3 += 1.0;
        _m4 += 1.0;
      } else if (f<_q4) {
        _m4 += 1.0;
      } else {
        _m4 += 1.0;
        _q4 = f;
      }
      
      // Increment desired marker positions.
      _f1 += _d1;
      _f2 += _d2;
      _f3 += _d3;

      // If necessary, adjust height and location of markers 1, 2, and 3.
      double mm,mp;
      mm = _m1-1.0;
      mp = _m1+1.0;
      if (_f1>=mp && _m2>mp) {
        _q1 = qp(mp,_m0,_m1,_m2,_q0,_q1,_q2);
        _m1 = mp;
      } else if (_f1<=mm && _m0<mm) {
        _q1 = qm(mm,_m0,_m1,_m2,_q0,_q1,_q2);
        _m1 = mm;
      }
      mm = _m2-1.0;
      mp = _m2+1.0;
      if (_f2>=mp && _m3>mp) {
        _q2 = qp(mp,_m1,_m2,_m3,_q1,_q2,_q3);
        _m2 = mp;
      } else if (_f2<=mm && _m1<mm) {
        _q2 = qm(mm,_m1,_m2,_m3,_q1,_q2,_q3);
        _m2 = mm;
      }
      mm = _m3-1.0;
      mp = _m3+1.0;
      if (_f3>=mp && _m4>mp) {
        _q3 = qp(mp,_m2,_m3,_m4,_q2,_q3,_q4);
        _m3 = mp;
      } else if (_f3<=mm && _m2<mm) {
        _q3 = qm(mm,_m2,_m3,_m4,_q2,_q3,_q4);
        _m3 = mm;
      }
    }
  }

  private static double qp(
    double mp,
    double m0, double m1, double m2,
    double q0, double q1, double q2)
  {
    double qt = q1+((mp-m0)*(q2-q1)/(m2-m1)+(m2-mp)*(q1-q0)/(m1-m0))/(m2-m0);
    return (qt<=q2)?qt:q1+(q2-q1)/(m2-m1);
  }

  private static double qm(
    double mm,
    double m0, double m1, double m2,
    double q0, double q1, double q2)
  {
    double qt = q1-((mm-m0)*(q2-q1)/(m2-m1)+(m2-mm)*(q1-q0)/(m1-m0))/(m2-m0);
    return (q0<=qt)?qt:q1+(q0-q1)/(m0-m1);
  }
}
