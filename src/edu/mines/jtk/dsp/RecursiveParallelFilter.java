/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;

/**
 * Recursive parallel filter.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.19
 */
public class RecursiveParallelFilter extends RecursiveFilter {

  /**
   * Constructs a recursive filter with specified poles, zeros, and gain.
   * Any poles or zeros at zero (the origin of the complex z-plane) are
   * ignored. The number of non-zero zeros cannot exceed the number of 
   * non-zero poles, and all poles must be unique.
   * @param poles array of complex poles.
   * @param zeros array of complex poles.
   * @param gain the filter gain.
   */
  public RecursiveParallelFilter(
    Cdouble[] poles, Cdouble[] zeros, double gain) 
  {
    init(poles,zeros,gain);
  }

  /**
   * Applies this filter. The input and output arrays must be distinct,
   * and the length of the output array must not exceed the length of the
   * input array.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyForward(float[] x, float[] y) {
    Check.argument(x!=y,"x!=y");
    Check.argument(x.length>=y.length,"x.length>=y.length");
    int n = y.length;

    // Constant scale factor (could be zero).
    for (int i=0; i<n; ++i)
      y[i] = _c*x[i];

    // Apply 2nd-order filters.
    for (int i2=0; i2<_n2; ++i2)
      _f2[i2].accumulateForward(x,y);
  }

  public void accumulateForward(float[] x, float[] y) {
    // TODO: implement this method.
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected RecursiveParallelFilter() {
  }

  protected void init(Cdouble[] poles, Cdouble[] zeros, double gain) {

    // Ignore any poles or zeros at the origin.
    poles = nonZero(poles);
    zeros = nonZero(zeros);

    // Number of non-zero zeros must not exceed number of non-zero poles.
    Check.argument(
      zeros.length<=poles.length,
      "number of non-zero zeros does not exceed number of non-zero poles");

    // Non-zero poles must be unique.
    Check.argument(polesUnique(poles),"all poles are unique");

    // Number of poles and zeros.
    _np = poles.length;
    _nz = zeros.length;

    // Sort poles and zeros so that complex conjugate pairs are first.
    poles = sort(poles);
    zeros = sort(zeros);

    // Count complex and real poles.
    _nc = 0;
    _nr = 0;
    for (int ip=0; ip<_np; ++ip) {
      if (poles[ip].i!=0.0) {
        ++_nc;
      } else {
        ++_nr;
      }
    }

    // Construct 2nd-order filters and constant scale factor c.
    _n2 = _nr+_nc/2;
    _f2 = new Recursive2ndOrderFilter[_n2];
    double c = (_nz==_np)?gain:0.0;
    for (int i2=0,jp=0; i2<_n2; ++i2,++jp) {
      Cdouble hj = Hj(jp,poles,zeros,gain);
      double b0,b1,b2,a1,a2;
      if (poles[jp].i!=0.0) {
        Cdouble pj = poles[jp++];
        Cdouble qj = pj.inv();
        b0 = hj.r-hj.i*qj.r/qj.i;
        b1 = hj.i/qj.i;
        b2 = 0.0;
        a1 = -2.0*pj.r;
        a2 = pj.norm();
      } else {
        Cdouble pj = poles[jp];
        b0 = hj.r;
        b1 = 0.0;
        b2 = 0.0;
        a1 = -pj.r;
        a2 = 0.0;
      }
      _f2[i2] = new Recursive2ndOrderFilter(
        (float)b0,(float)b1,(float)b2,(float)a1,(float)a2);
      if (_nz==_np)
        c -= b0;
    }
    _c = (float)c;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _np; // number of poles
  private int _nz; // number of zeros
  private int _nc; // number of complex poles (must be even)
  private int _nr; // number of real poles
  private float _c; // constant scale factor
  private int _n2; // number of recursive 2nd-order filters
  private Recursive2ndOrderFilter[] _f2; // the filters

  /**
   * Evaluates residue of H(z) for the jp'th pole. The residue is H(z) 
   * evaluated without division by the factor corresponding to the jp'th 
   * pole, which would be zero. If that pole is complex (has a non-zero 
   * imaginary part), then division by the factor corresponding to its 
   * conjugate is omitted as well.
   */
  private Cdouble Hj(
    int jp, Cdouble[] poles, Cdouble[] zeros, double gain) 
  {
    Cdouble pj = poles[jp];
    Cdouble pc = pj.conj();
    Cdouble qj = pj.inv();
    Cdouble c1 = new Cdouble(1.0,0.0);
    Cdouble hz = new Cdouble(c1);
    for (int iz=0; iz<_nz; ++iz) {
      Cdouble zi = zeros[iz];
      hz.timesEquals(c1.minus(zi.times(qj)));
    }
    Cdouble hp = new Cdouble(c1);
    for (int ip=0; ip<_np; ++ip) {
      Cdouble pi = poles[ip];
      if (pi.r!=pj.r && pi.i!=pj.i && -pi.i!=pj.i)
        hp.timesEquals(c1.minus(pi.times(qj)));
    }
    return hz.over(hp).times(gain);
  }

  /**
   * Returns true if all poles are unique; false, otherwise.
   */
  private static boolean polesUnique(Cdouble[] poles) {
    int np = poles.length;
    for (int ip=0; ip<np; ++ip) {
      Cdouble pi = poles[ip];
      for (int jp=ip+1; jp<np; ++jp) {
        Cdouble pj = poles[jp];
        if (pi.equals(pj))
          return false;
      }
    }
    return true;
  }

  /**
   * Returns non-zero poles or zeros.
   */
  private static Cdouble[] nonZero(Cdouble[] c) {
    int n = c.length;
    int m = 0;
    for (int i=0; i<n; ++i) {
      if (c[i].r!=0.0 || c[i].i!=0.0)
        ++m;
    }
    Cdouble[] d = new Cdouble[m];
    m = 0;
    for (int i=0; i<n; ++i) {
      if (c[i].r!=0.0 || c[i].i!=0.0)
        d[i] = c[i];
    }
    return d;
  }

  /**
   * Sorts array of poles or zeros. After sorting, any complex conjugate 
   * pairs are first in the array, followed by any real poles or zeros.
   * Also ensures that any complex poles or zeros have conjugate mates.
   * @return array of sorted poles or zeros.
   */
  private static Cdouble[] sort(Cdouble[] c) {
    int n = c.length;
    Cdouble[] cs = new Cdouble[n];
    int ns = 0;
    for (int i=0; i<n; ++i) {
      if (!c[i].isReal()) {
        Cdouble cc = c[i].conj();
        int j = 0;
        while (j<n && !cc.equals(c[j]))
          ++j;
        Check.argument(j<n,"complex "+c[i]+" has a conjugate mate");
        if (i<j) {
          cs[ns++] = c[i];
          cs[ns++] = c[j];
        }
      }
    }
    for (int i=0; i<n; ++i) {
      if (c[i].isReal())
        cs[ns++] = c[i];
    }
    return cs;
  }
}
