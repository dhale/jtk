/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;

/**
 * Sampling of one variable.
 * <p>
 * Samplings are often used to represent independent variables for sampled 
 * functions. They describe the values at which a function is sampled. For 
 * efficiency, and to guarantee a unique mapping from sample value to 
 * function value, we restrict samplings to be monotonic-definite. That is, 
 * sample values strictly decrease or increase as sample index increases.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.01
 */
public class Sampling {

  /**
   * Constructs an empty sampling. An empty sampling has no samples, and
   * it's sampling interval and first sample value are zero.
   */
  public Sampling() {
  }

  /**
   * Constructs a sampling with specified count. The sampling interval 
   * (delta) is one, and the first sample value is zero.
   * @param n the number (count) of samples.
   */
  public Sampling(int n) {
    this(n,1.0f,0.0f);
  }

  /**
   * Constructs a sampling with specified count, delta, and first value.
   * @param n the number (count) of samples.
   * @param d the sampling interval (delta).
   * @param f the first sample value.
   */
  public Sampling(int n, float d, float f) {
    Check.argument(0<=n,"n is not less than zero");
    _n = n;
    _d = (_n<2)?0.0f:d;
    _f = (_n<1)?0.0f:f;
    _v = null;
  }

  /**
   * Constructs a sampling from the specified array of values. The values 
   * must be monotonic-definite; i.e., strictly increasing or decreasing.
   * @param v the sampling values; copied, not referenced.
   */
  public Sampling(float[] v) {
    Check.argument(Array.isMonotonicDefinite(v),"v is monotonic-definite");
    _n = v.length;
    _d = (_n<2)?0.0f:v[1]-v[0];
    _f = (_n<1)?0.0f:v[0];
    _v = (_n<2)?null:Array.copy(v);
  }

  /**
   * Gets the number of samples.
   * @return the number of samples.
   */
  public int getCount() {
    return _n;
  }

  /**
   * Gets the sampling interval. If not uniformly sampled, returns the
   * difference between the first two sample values. Returns zero, if
   * this sampling has fewer than two samples.
   * @return the sampling interval.
   */
  public float getDelta() {
    return _d;
  }

  /**
   * Gets the first sample value. Returns zero, if this sampling is empty.
   * @return the first sample value.
   */
  public float getFirst() {
    return _f;
  }

  /**
   * Gets the last sample value. Returns zero, if this sampling is empty.
   * @return the last sample value.
   */
  public float getLast() {
    return (_n==0)?0.0f:(_v!=null)?_v[_n-1]:_f+(_n-1)*_d;
  }

  /**
   * Gets the sample value with specified index.
   * @param i the index.
   * @return the sample value.
   */
  public float getValue(int i) {
    Check.index(_n,i);
    return (_v!=null)?_v[i]:_f+i*_d;
  }

  /**
   * Gets the sample values.
   * @return the sample values; returned by copy, not by reference.
   */
  public float[] getValues() {
    float[] v = null;
    if (_v!=null) {
      v = Array.copy(_v);
    } else {
      v = new float[_n];
      for (int i=0; i<_n; ++i)
        v[i] = (float)(_f+i*_d);
    }
    return v;
  }

  /**
   * Determines whether this sampling has no samples.
   * @return true, if no samples; false, otherwise.
   */
  public boolean isEmpty() {
    return _n==0;
  }

  /**
   * Determines whether this sampling is uniform. A sampling is uniform 
   * if the difference between adjacent sample values is constant. That
   * difference is the <em>sampling interval</em>.
   * <p>
   * Note that samplings constructed with an array of sample values may or 
   * may not be uniform. Such samplings are defined to be uniform if their 
   * values match those that would be computed assuming a constant sampling 
   * interval, to within at least <em>float</em> precision. If and when 
   * such a sampling is found to be uniform, its array of sample values is 
   * discarded, and it is represented thereafter by the equivalent constant 
   * sampling interval.
   * @return true, if uniform; false, otherwise.
   */
  public boolean isUniform() {
    if (_v==null)
      return true;
    double d = _v[1]-_v[0];
    double f = _v[0];
    double vi = f;
    for (int i=0; i<_n; ++i,vi+=d) {
      if (_v[i]!=(float)vi)
        return false;
    }
    _d = (float)d;
    _f = (float)f;
    _v = null;
    return true;
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    Sampling that = (Sampling)obj;
    if (isUniform()!=that.isUniform())
      return false;
    if (isUniform()) {
      return getCount()==that.getCount() &&
             getDelta()==that.getDelta() &&
             getFirst()==that.getFirst();
    } else {
      float[] tv = that._v;
      for (int i=0; i<_n; ++i) {
        if (_v[i]!=tv[i])
          return false;
      }
      return true;
    }
  }

  public int hashCode() {
    if (isUniform()) {
      return _n^Float.floatToIntBits(_d)^Float.floatToIntBits(_f);
    } else {
      int hash = 0;
      for (int i=0; i<_n; ++i)
        hash ^= Float.floatToIntBits(_v[i]);
      return hash;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  int _n; // number of samples
  float _d; // sampling interval
  float _f; // value of first sample
  float[] _v; // array[n] of sample values; null, if uniform
}
