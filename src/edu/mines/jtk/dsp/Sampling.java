/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
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
 * function value, we restrict samplings to be strictly increasing. In other
 * words, no two samples have equal value, and sample values increase with 
 * increasing sample index.
 * <p>
 * Samplings are either uniform or non-uniform. Uniform samplings are
 * represented by a sample count n, a sampling interval d, and a first
 * sample value f. Non-uniform samplings are represented by an array of 
 * sample values.
 * <p>
 * All sample values are computed and stored in <em>double precision</em>. 
 * This double precision can be especially important in uniform samplings, 
 * because the sampling interval d and first sample value f are often used 
 * to compute sample values for thousands of samples, in loops like this one:
 * <pre><code>
 *   int n = sampling.getCount();
 *   double d = sampling.getDelta();
 *   double f = sampling.getFirst();
 *   double v = f;
 *   for (int i=0; i&lt;n; ++i,v+=d) {
 *     // some calculation that uses the sample value v
 *   }
 * </code></pre>
 * In each iteration of the loop above, the sample value x is computed by 
 * accumulating the sampling interval d. This computation is fast, but it 
 * also yields rounding error that can grow quadratically with the number 
 * of samples n. If v were computed in single (float) precision, then this 
 * rounding error could exceed the sampling interval d for as few as 
 * n=10,000 samples.
 * <p>
 * If accumulating in double precision is insufficient, a more accurate 
 * and more costly way to compute sample values is as follows:
 * <pre><code>
 *   // ...
 *   double v = f;
 *   for (int i=0; i&lt;n; ++i,v=f+i*d) {
 *     // some calculation that uses the sample value v
 *   }
 * </code></pre>
 * With this computation of sample values, rounding errors can grow only 
 * linearly with the number of samples n.
 * <p>
 * Two samplings are considered equivalent if their sample values differ
 * by no more than the <em>sampling tolerance</em>. This tolerance may be
 * specified, as a fraction of the sampling interval, when a sampling is
 * constructed. Alternatively, a default tolerance may be used. When
 * comparing two samplings, the smaller of their tolerances is used.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.10
 */
public class Sampling {

  /**
   * A default fraction used to test for equivalent sample values. 
   * By default, if the difference between two sample values does not 
   * exceed this fraction of the sampling interval, then those values 
   * are considered equivalent. This default is used when a tolerance 
   * is not specified explicitly when a sampling is constructed.
   */
  public static final double DEFAULT_TOLERANCE = 1.0e-6;

  /**
   * Constructs an empty sampling. An empty sampling has no samples. The 
   * sampling interval (delta) is 1.0, and the first sample value is 0.0.
   */
  public Sampling() {
    this(0,1.0,0.0);
  }

  /**
   * Constructs a uniform sampling with specified count. The sampling 
   * interval (delta) is 1.0, and the first sample value is 0.0.
   * @param n the number (count) of samples.
   */
  public Sampling(int n) {
    this(n,1.0,0.0);
  }

  /**
   * Constructs a uniform sampling with specified parameters.
   * @param n the number (count) of samples; must be non-negative.
   * @param d the sampling interval (delta); must be positive.
   * @param f the first sample value.
   */
  public Sampling(int n, double d, double f) {
    this(n,d,f,DEFAULT_TOLERANCE);
  }

  /**
   * Constructs a sampling with specified parameters.
   * @param n the number (count) of samples; must be non-negative.
   * @param d the sampling interval (delta); must be positive.
   * @param f the first sample value.
   * @param t the sampling tolerance, expressed as fraction of delta.
   */
  public Sampling(int n, double d, double f, double t) {
    Check.argument(0<=n,"n is not less than zero");
    Check.argument(0.0<d,"d is greater than zero");
    _n = n;
    _d = d;
    _f = f;
    _v = null;
    _t = _d*t;
  }

  /**
   * Constructs a sampling from the specified array of values. The values 
   * must be strictly increasing. The constructed sampling may or may not 
   * be uniform, depending on the specified values.
   * @param v the sampling values; copied, not referenced.
   */
  public Sampling(double[] v) {
    this(v,DEFAULT_TOLERANCE);
  }

  /**
   * Constructs a sampling from the specified array of values and tolerance.
   * The values must be strictly increasing. The constructed sampling may or
   * may not be uniform, depending on the specified values.
   * @param v the sampling values; copied, not referenced.
   * @param t the sampling tolerance, expressed as fraction of delta.
   */
  public Sampling(double[] v, double t) {
    Check.argument(Array.isIncreasing(v),"v is increasing");
    _n = v.length;
    _d = (_n<2)?1.0:(v[_n-1]-v[0])/(_n-1);
    _f = (_n<1)?0.0:v[0];
    _t = _d*t;
    boolean uniform = true;
    for (int i=0; i<_n && uniform; ++i) {
      double vi = _f+i*_d;
      if (!almostEqual(v[i],vi,_t))
        uniform = false;
    }
    _v = (uniform)?null:Array.copy(v);
  }

  /**
   * Gets the number of samples.
   * @return the number of samples.
   */
  public int getCount() {
    return _n;
  }

  /**
   * Gets the sampling interval. If not uniformly sampled, the sampling
   * interval is the average difference between sample values.
   * @return the sampling interval; 1.0, if fewer than two samples.
   */
  public double getDelta() {
    return _d;
  }

  /**
   * Gets the first sample value.
   * @return the first sample value; 0.0, if this sampling is empty.
   */
  public double getFirst() {
    return _f;
  }

  /**
   * Gets the last sample value.
   * @return the last sample value; 0.0, if this sampling is empty.
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
    return _n==0;
  }

  /**
   * Determines whether this sampling is uniform. A sampling is uniform 
   * if its values can be computed, to within the sampling tolerance, by
   * the expression v = f+i*d, for sampling indices i = 0, 1, ..., n-1.
   * Samplings with fewer than two samples (even empty samplings) are 
   * considered to be uniform.
   * <p>
   * Note that, by this definition, samplings constructed with an array 
   * of sample values may or may not be uniform.
   * @return true, if uniform; false, otherwise.
   */
  public boolean isUniform() {
    return _v==null;
  }

  /**
   * Returns the index of the sample with specified value. If this 
   * sampling has a sample value that equals (to within the sampling 
   * tolerance) the specified value, then this method returns the 
   * index of that sample. Otherwise, this method returns -1.
   * @param x the value.
   * @return the index of the matching sample; -1, if none.
   */
  public int indexOf(double x) {
    int i = -1;
    if (isUniform()) {
      int j = (int)Math.round((x-_f)/_d);
      if (0<=j && j<_n && almostEqual(x,_f+j*_d,_t))
        i = j;
    } else {
      int j = Array.binarySearch(_v,x);
      if (0<=j) {
        i = j;
      } else {
        j = -(j+1);
        if (j>0 && almostEqual(x,_v[j-1],_t)) {
          i = j-1;
        } else if (j<_n && almostEqual(x,_v[j],_t)) {
          i = j;
        }
      }
    }
    return i;
  }

  /**
   * Determines the overlap between this sampling and the specified sampling.
   * Both the specified sampling and this sampling represent a first-to-last
   * range of sample values. The overlap is a contiguous set of samples that 
   * have values that are equal, to within the minimum sampling tolerance of 
   * the two samplings. This set is represented by an array of three ints, 
   * {n,it,is}, where n is the number of overlapping samples, and it and is 
   * denote the indices of the first samples in the overlapping parts of this 
   * sampling (t) and the specified sampling (s). There exist three cases.
   * <ul><li>
   * The ranges of sample values overlap, and the values in the overlapping 
   * parts are equivalent. In this case, the two samplings are compatible; 
   * and this method returns the array {n,it,is}, as described 
   * above.
   * </li><li>
   * The ranges of sample values in the two samplings do not overlap.
   * In this case, the two samplings are compatible; and this method 
   * returns either {0,nt,0} or {0,0,ns}, depending on whether all
   * sample values in this sampling are less than or greater than
   * those in the specified sampling, respectively.
   * </li><li>
   * The ranges of sample values overlap, but the values in the overlapping 
   * parts are not equivalent. In this case, the two samplings are 
   * incompatible; and this method returns null.
   * </li></ul>
   * @param s the sampling to compare with this sampling.
   * @return the array {n,it,is} that describes the overlap; null, if 
   *  the specified sampling is incompatible with this sampling.
   */
  public int[] overlapWith(Sampling s) {
    Sampling t = this;
    int nt = t.getCount();
    int ns = s.getCount();
    double tf = t.getFirst();
    double sf = s.getFirst();
    double tl = t.getLast();
    double sl = s.getLast();
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
        double tiny = toleranceWith(s);
        for (jt=it,js=is; jt!=mt; ++jt,++js) {
          if (!almostEqual(t.getValue(jt),s.getValue(js),tiny))
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
      double[] vm = new double[nm];
      int jm = 0;
      for (int jt=0; jt<it; ++jt)
        vm[jm++] = t.getValue(jt);
      for (int js=0; js<is; ++js)
        vm[jm++] = s.getValue(js);
      for (int jt=it; jt<it+n; ++jt)
        vm[jm++] = t.getValue(jt);
      for (int jt=it+n; jt<nt; ++jt)
        vm[jm++] = t.getValue(jt);
      for (int js=is+n; js<ns; ++js)
        vm[jm++] = s.getValue(js);
      return new Sampling(vm);
    }
  }

  /**
   * Determines whether this sampling is equivalent to the specified sampling.
   * Two samplings are equivalent if each of their sample values differs by 
   * no more than the sampling tolerance.
   * @param s the sampling to compare to this sampling.
   * @return true, if equivalent; false, otherwise.
   */
  public boolean isEquivalentTo(Sampling s) {
    Sampling t = this;
    if (t.isUniform()!=s.isUniform())
      return false;
    if (t.isUniform()) {
      if (t.getCount()!=s.getCount())
        return false;
      double tiny = toleranceWith(s);
      double tf = t.getFirst();
      double tl = t.getLast();
      double sf = s.getFirst();
      double sl = s.getLast();
      return almostEqual(tf,sf,tiny) && almostEqual(tl,sl,tiny);
    } else {
      double tiny = toleranceWith(s);
      for (int i=0; i<_n; ++i) {
        if (!almostEqual(_v[i],s.getValue(i),tiny))
          return false;
      }
      return true;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n; // number of samples
  private double _d; // sampling interval
  private double _f; // value of first sample
  private double[] _v; // array[n] of sample values; null, if uniform
  private double _t; // sampling tolerance (multiplied by _d)

  private boolean almostEqual(double v1, double v2, double tiny) {
    double diff = v1-v2;
    return (diff<0.0)?-diff<tiny:diff<tiny;
  }

  private double toleranceWith(Sampling s) {
    return (_t<s._t)?_t:s._t;
  }
}
