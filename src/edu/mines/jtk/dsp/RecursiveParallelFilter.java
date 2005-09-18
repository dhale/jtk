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
   * Applies this filter. The input and output arrays must be distinct.
   * The length of the output array must not exceed that of the input
   * array.
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

    // Single-pole filters for real poles.
    for (int ip=0; ip<_nr; ++ip) {
      float b0 = _b[ip][0];
      float a1 = _a[ip][1];
      float yim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float yi = b0*x[i]-a1*yim1;
        y[i] += yi;
        yim1 = yi;
      }
    }

    // Double-pole filters for complex poles and their conjugates.
    for (int ip=_nr; ip<_np; ++ip) {
      float b0 = _b[ip][0];
      float b1 = _b[ip][1];
      float a1 = _a[ip][1];
      float a2 = _a[ip][2];
      float yim2 = 0.0f;
      float yim1 = 0.0f;
      float xim1 = 0.0f;
      for (int i=0; i<n; ++i) {
        float xi = x[i];
        float yi = b0*xi+b1*xim1-a1*yim1-a2*yim2;
        y[i] += yi;
        yim2 = yim1;
        yim1 = yi;
        xim1 = xi;
      }
    }
  }

  public void accumulateForward(float[] x, float[] y) {
    // TODO: implement this method.
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected RecursiveParallelFilter() {
  }

  protected void init(Cdouble[] poles, Cdouble[] zeros, double gain) {

    // Ensure that the number of zeros must not exceed the number of poles.
    // Here (and only here), we count any conjugate zeros and poles.
    int np = poles.length;
    int nz = zeros.length;
    int npr = countReal(poles);
    int nzr = countReal(zeros);
    int npc = 2*(np-npr);
    int nzc = 2*(nz-nzr);
    int npt = npr+npc; // total number of poles (including conjugates)
    int nzt = nzr+nzc; // total number of zeros (including conjugates).
    System.out.println("npt="+npt);
    System.out.println("nzt="+nzt);
    Check.argument(nzt<=npt,"number of zeros does not exceed number of poles");

    // Sort so that real poles and zeros are first in arrays.
    poles = sortPoles(poles);
    zeros = sortZeros(zeros);

    // Numbers of poles (not counting conjugates), and number of real poles.
    _np = np;
    _nr = npr;

    // Arrays for numerator and denominator filter coefficients.
    _b = new float[_np][2];
    _a = new float[_np][3];

    // Filter coefficients for real poles.
    for (int jp=0; jp<_nr; ++jp) {
      Cdouble hj = Hj(jp,poles,zeros,gain);
      Cdouble pj = poles[jp];
      _b[jp][0] = (float)(hj.r);
      _a[jp][0] = (float)(1.0);
      _a[jp][1] = (float)(-pj.r);
      System.out.println("hj="+hj);
      System.out.println("pj="+pj);
      System.out.println("b0="+_b[jp][0]);
      System.out.println("a0="+_a[jp][0]);
      System.out.println("a1="+_a[jp][1]);
    }

    // Filter coefficients for complex poles and their conjugates.
    for (int jp=_nr; jp<_np; ++jp) {
      Cdouble hj = Hj(jp,poles,zeros,gain);
      Cdouble pj = poles[jp];
      Cdouble qj = pj.inv();
      double b0 = hj.r-hj.i*qj.r/qj.i;
      _b[jp][0] = (float)(b0);
      _b[jp][1] = (float)(hj.i/qj.i);
      _a[jp][0] = (float)(1.0);
      _a[jp][1] = (float)(2.0*pj.r);
      _a[jp][2] = (float)(-pj.norm());
      System.out.println("pj="+pj);
      System.out.println("qj="+qj);
      System.out.println("b0="+_b[jp][0]);
      System.out.println("b1="+_b[jp][1]);
      System.out.println("a0="+_a[jp][0]);
      System.out.println("a1="+_a[jp][1]);
      System.out.println("a2="+_a[jp][2]);
    }

    // Constant factor; non-zero only if the total number of zeros 
    // equals the total number of poles.
    double c = 0.0;
    if (nzt==npt) {
      c = gain;
      for (int jp=0; jp<np; ++jp)
        c -= _b[jp][0];
    }
    _c = (float)c;
    System.out.println("c="+_c);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  int _np; // number of poles (not counting any conjugates)
  int _nr; // number of real poles
  private float[][] _b; // numerator filter coefficients
  private float[][] _a; // denominator filter coefficients
  private float _c; // constant scale factor

  /**
   * Evaluates residue of H(z) for the jp'th pole. The residue is H(z) 
   * evaluated without division by the factor corresponding to the jp'th 
   * pole, which would be zero. If that pole is complex (has a non-zero 
   * imaginary part), then division by the factor corresponding to its 
   * conjugate is omitted as well.
   */
  private static Cdouble Hj(
    int jp, Cdouble[] poles, Cdouble[] zeros, double gain) 
  {
    Cdouble pj = poles[jp];
    Cdouble qj = pj.inv();
    Cdouble c1 = new Cdouble(1.0,0.0);
    Cdouble hz = new Cdouble(c1);
    int nz = zeros.length;
    for (int iz=0; iz<nz; ++iz) {
      Cdouble zi = zeros[iz];
      hz.timesEquals(c1.minus(zi.times(qj)));
      if (zi.i!=0.0)
        hz.timesEquals(c1.minus(zi.conj().times(qj)));
    }
    Cdouble hp = new Cdouble(c1);
    int np = poles.length;
    for (int ip=0; ip<np; ++ip) {
      if (ip==jp)
        continue;
      Cdouble pi = poles[ip];
      hp.timesEquals(c1.minus(pi.times(qj)));
      if (pi.i!=0.0)
        hz.timesEquals(c1.minus(pi.conj().times(qj)));
    }
    return hz.over(hp).times(gain);
  }

  /**
   * Returns count of real numbers in array of complex numbers.
   * @return count of real numbers.
   */
  private static int countReal(Cdouble[] c) {
    int nc = c.length;
    int nr = 0;
    for (int ic=0; ic<nc; ++ic) {
      if (c[ic].i==0.0)
        ++nr;
    }
    return nr;
  }

  /**
   * Sorts zeros so that real zeros are first in array of zeros.
   * @param zeros array of zeros.
   * @return array of zeros, sorted.
   */
  private static Cdouble[] sortZeros(Cdouble[] zeros) {
    int nz = zeros.length;
    int nr = 0;
    Cdouble[] zcopy = new Cdouble[nz];
    for (int iz=0; iz<nz; ++iz) {
      Cdouble zi = zcopy[iz] = new Cdouble(zeros[iz]);
      if (zi.i==0.0) {
        zcopy[iz] = zcopy[nr];
        zcopy[nr] = zi;
        ++nr;
      }
    }
    return zcopy;
  }

  /**
   * Sorts poles so that real poles are first in array of poles.
   * First, ensures no duplicate poles; each pole and its conjugate
   * must be unique.
   * @param poles array of poles.
   * @return array of poles, sorted.
   */
  private static Cdouble[] sortPoles(Cdouble[] poles) {
    int np = poles.length;
    for (int ip=0; ip<np; ++ip) {
      Cdouble pi = poles[ip];
      Cdouble qi = pi.conj();
      for (int jp=0; jp<ip; ++jp) {
        Cdouble pj = poles[jp];
        Check.argument(!pj.equals(pi),"poles not equal");
        Check.argument(!pj.equals(qi),"poles not equal");
      }
    }
    int nr = 0;
    Cdouble[] pcopy = new Cdouble[np];
    for (int ip=0; ip<np; ++ip) {
      Cdouble pi = pcopy[ip] = new Cdouble(poles[ip]);
      if (pi.i==0.0) {
        pcopy[ip] = pcopy[nr];
        pcopy[nr] = pi;
        ++nr;
      }
    }
    return pcopy;
  }
}
