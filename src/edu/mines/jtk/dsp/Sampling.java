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
 * function value, we restrict samplings to be strictly increasing. That 
 * is, sample values strictly increase with increasing sample index.
 * <p>
 * Samplings are either uniform or non-uniform. Non-uniform samplings are
 * represented by an array of sample values, stored in <em>float</em>
 * precision. 
 * <p>
 * Uniform samplings are represented by three numbers: a sample count n, 
 * a sampling interval d, and a first sample value f. The number d and f 
 * are represented in <em>double</em> precision, so that they can safely 
 * be used to compute sample values for thousands of samples, in loops 
 * like this:
 * <pre><code>
 *   assert sampling.isUniform();
 *   int n = sampling.getCount();
 *   double d = sampling.getDelta();
 *   double f = sampling.getFirst();
 *   double x = f;
 *   for (int i=0; i&lt;n; ++i,x+=d) {
 *     // some calculation that uses the sample value x
 *   }
 * </code></pre>
 * In each iteration of the loop above, the sample value x is computed
 * by accumulating the sampling interval d. This is fast, but it also
 * yields rounding error that can grow with the <em>square</em> of the 
 * number of samples n. If x and d were computed and stored in float
 * precision, then for, say, 10,000 samples, this rounding error could
 * exceed the sampling interval d!
 * <p>
 * A more accurate and more costly way to compute sample values is as 
 * follows:
 * <pre><code>
 *   // ...
 *   double x = f;
 *   for (int i=0; i&lt;n; ++i,x=f+i*d) {
 *     // some calculation that uses the sample value x
 *   }
 * </code></pre>
 * With this computation of sample values, rounding errors can grow 
 * only linearly with the number of samples n.
 * <p>
 * Two samplings are considered equal if their sample values are equal 
 * to within <em>float</em> precision. If the samplings are uniform,
 * their sample values are assumed to be computed as in the second
 * (more accurate) loop above.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.08
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
    this(n,1.0,0.0);
  }

  /**
   * Constructs a sampling with specified count, delta, and first value.
   * @param n the number (count) of samples; must be non-negative.
   * @param d the sampling interval (delta); must be positive.
   * @param f the first sample value.
   */
  public Sampling(int n, double d, double f) {
    Check.argument(0<=n,"n is not less than zero");
    Check.argument(0.0<d,"d is greater than zero");
    _n = n;
    _d = (_n<2)?0.0:d;
    _f = (_n<1)?0.0:f;
    _v = null;
  }

  /**
   * Constructs a sampling from the specified array of values. 
   * The values must be strictly increasing.
   * @param v the sampling values; copied, not referenced.
   */
  public Sampling(float[] v) {
    Check.argument(Array.isIncreasing(v),"v is increasing");
    _n = v.length;
    _d = (_n<2)?0.0:(v[1]-v[0])/(_n-1);
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
   * Gets the last sample value. Returns zero, if this sampling is empty.
   * @return the last sample value.
   */
  public double getLast() {
    return (_n==0)?0.0:(_v!=null)?_v[_n-1]:_f+(_n-1)*_d;
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
   * Gets the sample values. Note that these values are returned
   * with float precision, whether or not the sampling is uniform.
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
      if ((float)_v[i]!=(float)vi)
        return false;
    }
    _d = d;
    _f = f;
    _v = null;
    return true;
  }

  /**
   * Returns the index of the sample with specified value.
   * If this sampling has a sample value that (to within float precision) 
   * equals the specified value, then this method returns the index of 
   * that sample. Otherwise, this method returns -1.
   * @param x the value.
   * @return the index of the matching sample; -1, if none.
   */
  public int indexOf(double x) {
    int i = -1;
    if (isUniform()) {
      int j = (int)Math.round((x-_f)/_d);
      if (0<=j && j<_n && (float)x==(float)(_f+j*_d))
        i = j;
    } else {
      int j = Array.binarySearch(_v,(float)x);
      if (0<=j)
        i = j;
    }
    return -1;
  }

  /**
   * Determines the overlap between this sampling and the specified sampling.
   * Both the specified sampling and this sampling represent a first-to-last
   * range of sample values. The overlap is a contiguous set of samples that 
   * have the same values. This set is represented by an array of three ints,
   * {n,it,is}, where n is the number of overlapping samples, and it and is 
   * are the indices of the first samples in the overlapping parts of this 
   * sampling (t) and the specified sampling (s). There exist three cases.
   * <ul><li>
   * The ranges of sample values overlap, and the sample values in the 
   * overlapping parts are consistent. In this case, the two samplings are
   * compatible; and this method returns the array {n,it,is}, as described 
   * above.
   * </li><li>
   * The ranges of sample values in the two samplings do not overlap.
   * In this case, the two samplings are compatible; and this method 
   * returns either {0,nt,0} or {0,0,ns}, depending on whether all
   * sample values in this sampling are less than or greater than
   * those in the specified sampling, respectively.
   * </li><li>
   * The ranges of sample values overlap, but the sample values in the 
   * overlapping parts are inconsistent. In this case, the two samplings
   * are incompatible; and this method returns null.
   * </li></ul>
   * @param s the sampling to compare with this sampling.
   * @return the array {n,it,is} that describes the overlap; null, if 
   *  the specified sampling is incompatible with this sampling.
   */
  public int[] overlapWith(Sampling s) {
    Sampling t = this;
    int nt = t.getCount();
    int ns = s.getCount();
    float tf = (float)t.getFirst();
    float sf = (float)s.getFirst();
    float tl = (float)t.getLast();
    float sl = (float)s.getLast();
    int it = 0;
    int is = 0;
    int jt = nt-1;
    int js = ns-1;
    if (tl<sf) {
      return new int[]{0,nt,0};
    } else if (sl<tf) {
      return new int[]{0,0,ns};
    } else {
      if (tf<sf) {
        it = t.indexOf(sf);
      } else {
        is = s.indexOf(tf);
      }
      if (it<0 || is<0)
        return null;
      if (tl<sl) {
        js = s.indexOf(tl);
      } else {
        jt = t.indexOf(sl);
      }
      if (jt<0 || js<0)
        return null;
      int mt = 1+jt-it;
      int ms = 1+js-is;
      if (mt!=ms)
        return null;
      if (!t.isUniform() || !s.isUniform()) {
        for (jt=it,js=is; jt!=mt; ++jt,++js) {
          float xt = (float)t.getValue(jt);
          float xs = (float)s.getValue(js);
          if (xt!=xs)
            return null;
        }
      }
      return new int[]{mt,it,is};
    }
  }

  /**
   * Returns the union of this sampling with the specified sampling. This
   * union is possible if and only if the two samplings are compatible.
   * @param s the sampling to merge with this sampling.
   * @return the merged sampling; null, if no merge is possible.
   * @see #overlapWith(Sampling)
   */
  public Sampling mergeWith(Sampling s) {
    Sampling t = this;
    int[] overlap = t.overlapWith(s);
    if (overlap==null)
      return null;
    int n = overlap[0];
    int it = overlap[1];
    int is = overlap[2];
    int nt = t.getCount();
    int ns = s.getCount();
    int nm = nt+ns-n;
    if (t.isUniform() && s.isUniform()) {
      double dm = t.getDelta();
      double fm = (it==0)?s.getFirst():t.getFirst();
      return new Sampling(nm,dm,fm);
    } else {
      float[] vm = new float[nm];
      int jm = 0;
      for (int jt=0; jt<it; ++jt)
        vm[jm++] = (float)t.getValue(jt);
      for (int js=0; js<is; ++js)
        vm[jm++] = (float)s.getValue(js);
      for (int jt=it; jt<it+n; ++jt)
        vm[jm++] = (float)t.getValue(jt);
      for (int jt=it+n; jt<nt; ++jt)
        vm[jm++] = (float)t.getValue(jt);
      for (int js=is+n; js<ns; ++js)
        vm[jm++] = (float)s.getValue(js);
      return new Sampling(vm);
    }
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    Sampling that = (Sampling)obj;
    if (this.isUniform()!=that.isUniform())
      return false;
    if (isUniform()) {
      if (this.getCount()!=that.getCount())
        return false;
      float xfThis = (float)this.getFirst();
      float xlThis = (float)this.getLast();
      float xfThat = (float)that.getFirst();
      float xlThat = (float)that.getLast();
      return xfThis==xfThat && xlThis==xlThat;
    } else {
      for (int i=0; i<_n; ++i) {
        if (_v[i]!=that.getValue(i))
          return false;
      }
      return true;
    }
  }

  public int hashCode() {
    if (isUniform()) {
      long dl = Double.doubleToLongBits(_d);
      long fl = Double.doubleToLongBits(_f);
      return _n ^ (int)(dl^(dl>>>32)) ^ (int)(fl^(fl>>>32));
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
  double _d; // sampling interval
  double _f; // value of first sample
  float[] _v; // array[n] of sample values; null, if uniform
}
