/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import static java.lang.Math.*;

/**
 * A sinc interpolator for uniformly-sampled functions y(x). Interpolators
 * can be designed for two parameters: (1) a maximum error and (2) either 
 * a maximum frequency or a maximum length. The maximum frequency is that
 * frequency below which errors are guaranteed to be less than a specified
 * maximum error. The parameter not specified, either maximum frequency or
 * maximum length, is computed when an interpolator is designed.
 * <p>
 * The length of an interpolator is the number of input samples required to
 * interpolate a single output sample. Ideally, the weights applied to each 
 * input sample are values of a sinc function. Although the ideal sinc 
 * function yields zero interpolation error for all frequencies up to the 
 * Nyquist frequency (0.5 cycles/sample), it has infinite length.
 * <p>
 * With recursive filtering, infinite-length approximations to the sinc
 * function are feasible and, in some applications, most efficient. When
 * the number of interpolated (output) samples is large relative to the
 * number of input samples, the cost of recursive filtering is amortized 
 * over those many output samples, and can be negligible. However, this 
 * cost becomes significant when only a few output samples are interpolated
 * for each sequence of input samples.
 * <p>
 * This interpolator is based on a <em>finite-length</em> approximation 
 * to the sinc function. The efficiency of finite-length interpolators 
 * like this one does not depend on the number of interpolated output 
 * samples. This interpolator is also robust in the presence of noise 
 * spikes, which affect only nearby samples.
 * <p>
 * Finite-length interpolators present a tradeoff between cost and accuracy.
 * Interpolators with small lengths are most efficient, and those with high 
 * maximum frequencies and small maximum errors are most accurate.
 * <p>
 * The default parameters are maximum error = 0.01 (1%) and maximum length
 * = 8 samples, which yields a maximum frequency greater than 0.325 
 * cycles/sample (65% of Nyquist).
 * <p>
 * When interpolating multiple uniformly sampled functions y(x) that share 
 * a common sampling of x, some redundant computations can be eliminated by 
 * specifying the sampling only once before interpolating multiple times. 
 * The resulting performance increase may be especially significant when 
 * only a few (perhaps only one) output samples are interpolated for each 
 * sequence of input samples.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.08.01
 */
public class SincInterpolator {

  /**
   * The method used to extrapolate samples when interpolating.
   * Sampled functions are defined implicitly by extrapolation outside the 
   * domain for which samples are specified explicitly, with either zero or 
   * constant values. If constant, the extrapolated values are the first and 
   * last input sample values. The default is extrapolation with zeros.
   */
  public enum Extrapolation {
    ZERO, 
    CONSTANT,
  };

  /**
   * Returns a sinc interpolator with specified maximum error and length.
   * @param emax the maximum error for frequencies less than fmax; e.g., 
   *  0.01 for 1% percent error. Must be greater than 0.0 and less than 1.0.
   * @param lmax the maximum interpolator length, in samples. Must be even
   *  and greater than zero.
   * @return the sinc interpolator.
   */
  public static SincInterpolator fromErrorAndLength(
    double emax, int lmax)
  {
    return new SincInterpolator(emax,0.0,lmax);
  }

  /**
   * Returns a sinc interpolator with specified maximum error and frequency.
   * @param emax the maximum error for frequencies less than fmax; e.g., 
   *  0.01 for 1% percent error. Must be greater than 0.0 and less than 1.0.
   * @param fmax the maximum frequency, in cycles per sample. 
   *  Must be greater than 0.0 and less than 0.5.
   * @return the sinc interpolator.
   */
  public static SincInterpolator fromErrorAndFrequency(
    double emax, double fmax)
  {
    return new SincInterpolator(emax,fmax,0);
  }

  /**
   * Returns a sinc interpolator with specified maximum frequency and length.
   * The product (1-2*fmax)*lmax must be greater than one. When this product 
   * is less than one, a useful upper bound on interpolation error cannot be 
   * computed.
   * @param fmax the maximum frequency, in cycles per sample. 
   *  Must be greater than 0.0 and less than 0.5*(1.0-1.0/lmax).
   * @param lmax the maximum interpolator length, in samples. 
   *  Must be an even integer greater than 1.0/(1.0-2.0*fmax).
   * @return the sinc interpolator.
   */
  public static SincInterpolator fromFrequencyAndLength(
    double fmax, int lmax)
  {
    Check.argument((1.0-2.0*fmax)*lmax>1.0,"(1.0-2.0*fmax)*lmax>1.0");
    return new SincInterpolator(0.0,fmax,lmax);
  }

  /**
   * Gets the maximum error for this interpolator.
   * @return the maximum error.
   */
  public double getMaximumError() {
    return _emax;
  }

  /**
   * Gets the maximum frequency for this interpolator.
   * @return the maximum frequency.
   */
  public double getMaximumFrequency() {
    return _fmax;
  }

  /**
   * Gets the maximum length for this interpolator.
   * @return the maximum length.
   */
  public int getMaximumLength() {
    return _lmax;
  }

  /**
   * Gets the extrapolation method for this interpolator.
   * @return the extrapolation method.
   */
  public Extrapolation getExtrapolation() {
    return _extrap;
  }

  /**
   * Sets the extrapolation method for this interpolator.
   * The default extrapolation method is extrapolation with zeros.
   * @param extrap the extrapolation method.
   */
  public void setExtrapolation(Extrapolation extrap) {
    _extrap = extrap;
  }

  /**
   * Sets the current input sampling for a uniformly-sampled function y(x).
   * In some applications, this sampling never changes, and this method 
   * may be called only once for this interpolator.
   * @param nxin the number of input samples.
   * @param dxin the interval at which the input y(x) is sampled.
   * @param fxin the first x at which the input y(x) is sampled.
   */
  public void setInputSampling(int nxin, double dxin, double fxin) {
    if (_stab==null)
      makeTable();
    _nxin = nxin;
    _dxin = dxin;
    _fxin = fxin;
    _ioutb = -_ltab-_ltab/2+1;
    _xoutf = fxin;
    _xouts = 1.0/dxin;
    _xoutb = _ltab-_xoutf*_xouts;
    _nxinm = nxin-_ltab;
  }

  /**
   * Sets the current input samples for a uniformly-sampled function y(x).
   * If input sample values are complex numbers, real and imaginary parts 
   * are packed in the array as real, imaginary, real, imaginary, and so on.
   * @param yin array of input samples of y(x).
   */
  public void setInputSamples(float[] yin) {
    _yin = yin;
  }

  /**
   * Sets the current input sampling and samples for a function y(x). 
   * This method simply calls the two methods 
   * {@link #setInputSampling(int,double,double)} and
   * {@link #setInputSamples(float[])} 
   * with the specified parameters.
   * @param nxin the number of input samples.
   * @param dxin the sampling interval for input y(x).
   * @param fxin the first x value at which the input y(x) is sampled.
   * @param yin array of input samples of y(x).
   */
  public void setInput(int nxin, double dxin, double fxin, float[] yin) {
    setInputSampling(nxin,dxin,fxin);
    setInputSamples(yin);
  }

  /**
   * Interpolates the current input samples as real numbers.
   * @param xout the value x at which to interpolate an output y(x).
   * @return the interpolated output y(x).
   */
  public float interpolate(double xout) {

    // Which input samples?
    double xoutn = _xoutb+xout*_xouts;
    int ixoutn = (int)xoutn;
    int kyin = _ioutb+ixoutn;

    // Which sinc approximation?
    double frac = xoutn-ixoutn;
    if (frac<0.0)
      frac += 1.0;
    int ktab = (int)(frac*_ntabm1+0.5);
    float[] stab = _stab[ktab];

    // If no extrapolation is necessary, use a fast loop.
      // Otherwise, extrapolate input samples, as necessary.
    float youtr = 0.0f;
    if (kyin>=0 && kyin<=_nxinm) {
      for (int itab=0; itab<_ltab; ++itab,++kyin)
        youtr += _yin[kyin]*stab[itab];
    } else if (_extrap==Extrapolation.ZERO) {
      for (int itab=0; itab<_ltab; ++itab,++kyin) {
        if (0<=kyin && kyin<_nxin)
          youtr += _yin[kyin]*stab[itab];
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int itab=0; itab<_ltab; ++itab,++kyin) {
        int jyin = (kyin<0)?0:(_nxin<=kyin)?_nxin-1:kyin;
        youtr += _yin[jyin]*stab[itab];
      }
    }
    return youtr;
  }

  /**
   * Interpolates the current input samples as real numbers.
   * @param nxout the number of output samples to interpolate.
   * @param xout array of values x at which to interpolate output y(x).
   * @param yout array of interpolated output y(x).
   */
  public void interpolate(int nxout, float[] xout, float[] yout) {

    // Loop over all output samples.
    for (int ixout=0;  ixout<nxout; ++ixout) {

      // Which input samples?
      double xoutn = _xoutb+xout[ixout]*_xouts;
      int ixoutn = (int)xoutn;
      int kyin = _ioutb+ixoutn;

      // Which sinc approximation?
      double frac = xoutn-ixoutn;
      if (frac<0.0)
        frac += 1.0;
      int ktab = (int)(frac*_ntabm1+0.5);
      float[] stab = _stab[ktab];

      // If no extrapolation is necessary, use a fast loop.
        // Otherwise, extrapolate input samples, as necessary.
      float youtr = 0.0f;
      if (kyin>=0 && kyin<=_nxinm) {
        for (int itab=0; itab<_ltab; ++itab,++kyin)
          youtr += _yin[kyin]*stab[itab];
      } else if (_extrap==Extrapolation.ZERO) {
        for (int itab=0; itab<_ltab; ++itab,++kyin) {
          if (0<=kyin && kyin<_nxin)
            youtr += _yin[kyin]*stab[itab];
        }
      } else if (_extrap==Extrapolation.CONSTANT) {
        for (int itab=0; itab<_ltab; ++itab,++kyin) {
          int jyin = (kyin<0)?0:(_nxin<=kyin)?_nxin-1:kyin;
          youtr += _yin[jyin]*stab[itab];
        }
      }
      yout[ixout] = youtr;
    }
  }

  /**
   * Interpolates the current input samples as complex numbers. Complex 
   * output samples are packed in the specified output array as real, 
   * imaginary, real, imaginary, and so on.
   * @param nxout the number of output samples to interpolate.
   * @param xout array of values x at which to interpolate output y(x).
   * @param yout array of interpolated output y(x).
   */
  public void interpolateComplex(int nxout, float[] xout, float[] yout) {

    // Loop over all output samples.
    for (int ixout=0;  ixout<nxout; ++ixout) {

      // Which input samples?
      double xoutn = _xoutb+xout[ixout]*_xouts;
      int ixoutn = (int)xoutn;
      int kyin = _ioutb+ixoutn;

      // Which sinc approximation?
      double frac = xoutn-ixoutn;
      if (frac<0.0)
        frac += 1.0;
      int ktab = (int)(frac*_ntabm1+0.5);
      float[] stab = _stab[ktab];

      // If no extrapolation is necessary, use a fast loop.
        // Otherwise, extrapolate input samples, as necessary.
      float youtr = 0.0f;
      float youti = 0.0f;
      if (kyin>=0 && kyin<=_nxinm) {
        for (int itab=0,jyin=2*kyin; itab<_ltab; ++itab,jyin+=2) {
          float stabi = stab[itab];
          youtr += _yin[jyin  ]*stabi;
          youti += _yin[jyin+1]*stabi;
        }
      } else if (_extrap==Extrapolation.ZERO) {
        for (int itab=0; itab<_ltab; ++itab,++kyin) {
          if (0<=kyin && kyin<_nxin) {
            int jyin = 2*kyin;
            float stabi = stab[itab];
            youtr += _yin[jyin  ]*stabi;
            youti += _yin[jyin+1]*stabi;
          }
        }
      } else if (_extrap==Extrapolation.CONSTANT) {
        for (int itab=0; itab<_ltab; ++itab,++kyin) {
          int jyin = (kyin<0)?0:(_nxin<=kyin)?2*_nxin-2:2*kyin;
          float stabi = stab[itab];
          youtr += _yin[jyin  ]*stabi;
          youti += _yin[jyin+1]*stabi;
        }
      }
      int jxout = 2*ixout;
      yout[jxout  ] = youtr;
      yout[jxout+1] = youti;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Fraction of error due to windowing; remainder is due to table lookup.
  private static final double EWIN_FRAC = 0.9;

  // Maximum table size, when maximum error and frequency are specified.
  private static final int NTAB_MAX = 16385;

  // Design parameters.
  private double _emax;
  private double _fmax;
  private int _lmax;
  
  // Kaiser window used in design.
  private KaiserWindow _kwin;

  // Extrapolation method.
  private Extrapolation _extrap = Extrapolation.ZERO;

  // Current input sampling.
  private int _nxin;
  private double _dxin;
  private double _fxin;
  private int _ioutb;
  private double _xoutf;
  private double _xouts;
  private double _xoutb;
  private double _ntabm1;
  private int _nxinm;

  // Current input samples.
  private float[] _yin; // real or complex samples

  // Table of interpolation coefficients.
  private int _ltab; // length of interpolators in table
  private int _ntab; // number of interpolators in table
  private double _dtab; // sampling interval in table
  private float[][] _stab; // array[ntab][ltab] of sinc(x)

  private SincInterpolator() {
    this(0.01,0.0,8);
  }

  /**
   * Constructs a sinc interpolator with specified parameters.
   * Exactly one of the parameters must be zero, and is computed here.
   */
  private SincInterpolator(double emax, double fmax, int lmax) {
    Check.argument(emax==0.0 && fmax!=0.0 && lmax!=0 ||
                   emax!=0.0 && fmax==0.0 && lmax!=0 ||
                   emax!=0.0 && fmax!=0.0 && lmax==0,
                   "exactly one of emax, fmax, and lmax is zero");
    if (emax==0.0) {
      Check.argument(fmax<0.5,"fmax<0.5");
      Check.argument(lmax%2==0,"lmax is even");
    } else if (fmax==0.0) {
      Check.argument(emax<1.0,"emax<1.0");
      Check.argument(lmax%2==0,"lmax is even");
    } else if (lmax==0) {
      Check.argument(emax<1.0,"emax<1.0");
      Check.argument(fmax<0.5,"fmax<0.5");
    }

    // The Kaiser window transition width is twice the difference 
    // between the Nyquist frequency 0.5 and the maximum frequency.
    double wwin = 2.0*(0.5-fmax);

    // The Kaiser window accounts for a hard-wired fraction of the maximum 
    // interpolation error. The other error will be due to table lookup.
    double ewin = emax*EWIN_FRAC;
    KaiserWindow kwin = null;

    // If maximum frequency and length are specified, compute the error
    // due to windowing. That windowing error is twice the error reported 
    // by the Kaiser window, because the stopband error is aliased with the
    // passpand error. Also, apply a lower bound to the error, so that the
    // table of sinc approximations does not become too large.
    if (emax==0.0) {
      kwin = KaiserWindow.fromWidthAndLength(wwin,lmax);
      ewin = 2.0*kwin.getError();
      emax = ewin/EWIN_FRAC;
      double etabMin = 1.1*PI*fmax/(NTAB_MAX-1);
      double emaxMin = etabMin/(1.0-EWIN_FRAC);
      if (emax<emaxMin) {
        emax = emaxMin;
        ewin = emax*EWIN_FRAC;
      }
    }

    // Else if maximum error and length are specified, compute the 
    // maximum frequency. That maximum frequency is the difference
    // between the Nyquist frequency 0.5 and half the transition width
    // reported by the Kaiser window. However, the maximum frequency
    // cannot be less than zero. It will be zero when the specified
    // maximum error and/or length are too small for a practical
    // interpolator.
    else if (fmax==0.0) {
      kwin = KaiserWindow.fromErrorAndLength(0.5*ewin,lmax);
      fmax = max(0.0,0.5-0.5*kwin.getWidth());
    }
    
    // Else if maximum error and width are specified, first compute a
    // lower bound on the maximum length, and then round that up to the
    // nearest even integer. Finally, reconstruct a Kaiser window with
    // that maximum length, ignoring the maximum frequency, which may
    // be exceeded.
    else {
      kwin = KaiserWindow.fromErrorAndWidth(0.5*ewin,wwin);
      double lwin = kwin.getLength();
      lmax = (int)(lwin); 
      if (lmax<lwin)
        ++lmax;
      if (lmax%2==1)
        ++lmax;
      kwin = KaiserWindow.fromErrorAndLength(0.5*ewin,lmax);
    }

    // Save design parameters and Kaiser window.
    _emax = emax;
    _fmax = fmax;
    _lmax = lmax;
    _kwin = kwin;

    // The number of interpolators in the table depends on the error
    // in table lookup - more interpolators corresponds to less error.
    // The error in table lookup is whatever is left of the maximum
    // error, after accounting for the error due to windowing the sinc.
    // The number of interpolators is a power of two plus 1, so that table 
    // lookup error is zero when interpolating halfway between samples.
    double etab = emax-ewin;
    _dtab = (fmax>0.0)?etab/(PI*fmax):1.0;
    int ntabMin = 1+(int)ceil(1.0/_dtab);
    _ntab = 2;
    while (_ntab<ntabMin)
      _ntab *= 2;
    ++_ntab;
    _dtab = 1.0/(_ntab-1);
    _ltab = lmax;

    // System.out.println(
    //   "emax="+_emax+" fmax="+_fmax+" lmax="+_lmax+" ntab="+_ntab);
  }

  /**
   * Builds the table of interpolators. This may be costly, so we do it 
   * only when an interpolator is used, not when it is constructed. Users` 
   * may construct more than one interpolator, perhaps in a search for 
   * optimal design parameters, without actually using all of them.
   */
  private void makeTable() {
    _ntabm1 = _ntab-1;
    _stab = new float[_ntab][_ltab];

    // The first and last interpolators are shifted unit impulses.
    // Handle these two cases exactly, with no rounding errors.
    for (int j=0; j<_ltab; ++j) {
      _stab[      0][j] = 0.0f;
      _stab[_ntab-1][j] = 0.0f;
    }
    _stab[      0][_ltab/2-1] = 1.0f;
    _stab[_ntab-1][_ltab/2  ] = 1.0f;

    // Other interpolators are sampled Kaiser-windowed sinc functions.
    for (int itab=1; itab<_ntab-1; ++itab) {
      double x = -_ltab/2+1-_dtab*itab;
      for (int i=0; i<_ltab; ++i,x+=1.0) {
        _stab[itab][i] = (float)(sinc(x)*_kwin.evaluate(x));
      }
    }
  }

  private static double sinc(double x) {
    return (x!=0.0)?sin(PI*x)/(PI*x):1.0;
  }
}
