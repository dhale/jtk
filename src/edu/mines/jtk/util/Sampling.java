/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * Sampling of one variable. Many measurements are sampled.
 * This is a digital world, and we are all just living in it.
 * <p>
 * Samplings are often used to represent independent variables for sampled 
 * functions. They describe the values at which a function is sampled. For 
 * efficiency, and to guarantee a unique mapping from sample value to 
 * function value, we restrict samplings to be monotonic-definite. That is, 
 * sample values strictly decrease or increase as sample index increases.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.01.01
 */
public class Sampling {

  /**
   * Constructs an empty sampling. An empty sampling has no samples, and
   * it's sampling interval and first sample value are zero.
   */
  public Sampling() {
  }

  /**
   * Constructs a sampling with specified count, delta, and first value.
   * @param n the number (count) of samples.
   * @param d the sampling interval (delta).
   * @param f the first sample value.
   */
  public Sampling(int n, double d, double f) {
    _n = n;
    _d = (_n<2)?0.0:d;
    _f = (_n<1)?0.0:f;
    _v = null;
  }

  /**
   * Constructs a sampling from the specified array of values. The values 
   * must be monotonic-definite; i.e., strictly increasing or decreasing.
   * @param v the sampling values; copied, not referenced.
   */
  public Sampling(double[] v) {
    Check.argument(Array.isMonotonicDefinite(v),"v is monotonic-definite");
    _n = v.length;
    _d = (_n<2)?0.0:v[1]-v[0];
    _f = (_n<1)?0.0:v[0];
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
  public double getDelta() {
    return _d;
  }

  /**
   * Gets the first sample value. Returns zero, if this sampling is empty.
   * @return the first sample value.
   */
  public double getFirst() {
    return _f;
  }

  /**
   * Gets the sample value with specified index.
   * @param i the index.
   * @return the sample value.
   */
  public double getValue(int i) {
    Check.index(_n,i);
    return (_v!=null)?_v[i]:_f+i*_d;
  }

  /**
   * Gets the sample values.
   * @return the sample values; returned by copy, not by reference.
   */
  public double[] getValues() {
    double[] v = null;
    if (_v!=null) {
      v = Array.copy(_v);
    } else {
      v = new double[_n];
      for (int i=0; i<_n; ++i)
        v[i] = _f+i*_d;
    }
    return v;
  }

  /**
   * Determines whether this sampling has no samples.
   * @return true, if no samples; false, otherwise.
   */
  public boolean isEmpty() {
    return _n>0;
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
      if ((float)_v[i]!=(float)(vi))
        return false;
    }
    _d = d;
    _f = f;
    _v = null;
    return true;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  int _n; // number of samples
  double _d; // sampling interval
  double _f; // value of first sample
  double[] _v; // array[n] of sample values; null, if uniform
}
