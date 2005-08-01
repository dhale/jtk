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
 * can be optimized for any two of three design parameters - maximum length, 
 * maximum frequency, and maximum error for frequencies less than the 
 * maximum frequency. 
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
 * over those many output samples, and is negligible. However, this cost 
 * becomes significant when only a few output samples are needed.
 * <p>
 * This interpolator is based on a <em>finite-length</em> approximation 
 * to the sinc function. The efficiency of finite-length interpolators 
 * like this one does not depend on the number of interpolated output 
 * samples. This interpolator is also robust in the presence of noise 
 * spikes, which affect only nearby samples.
 * <p>
 * Finite-length interpolators present a tradeoff between cost and accuracy.
 * Interpolators with small lengths are most efficient, and those with high
 * maximum frequencies and low maximum errors are most accurate. Because
 * only two of these three parameters can be specified, one should specify
 * those two that are most important for a particular application.
 * <p>
 * The default parameters are maximum length = 8 and maximum frequency = 
 * 0.325 cycles/sample (65% of Nyquist), which yields an error less than 
 * 0.01 (1%) for all frequencies less than the maximum frequency.
 * <p>
 * When interpolating multiple uniformly sampled functions y(x) that share 
 * a common sampling of x, some redundant computations can be eliminated by 
 * specifying the sampling only once before interpolating multiple times. 
 * The resulting performance increase may be especially significant when 
 * only a few (perhaps only one) output samples are interpolated for each 
 * sequence of input samples.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.26
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
   * Returns a sinc interpolator with specified maximum length and frequency.
   * @param lmax the maximum interpolator length, in samples. Must be even
   *  and greater than zero.
   * @param emax the maximum error for frequencies less than fmax; e.g., 
   *  0.01 for 1% percent error. Must be greater than 0.0 and less than 1.0.
   * @param fmax the maximum frequency, in cycles per sample. Must be greater
   *  than 0.0 and less than 0.5.
   * @return the sinc interpolator.
   */
  public static SincInterpolator fromLengthAndFrequency(
    int lmax, double fmax)
  {
    Check.argument(0<lmax,"0 < lmax");
    Check.argument(lmax%2==0,"lmax is even");
    Check.argument(0.0<fmax,"0.0 < fmax");
    Check.argument(fmax<0.5,"fmax < 0.5");
    return new SincInterpolator(lmax,fmax,0);
  }

  /**
   * Returns a sinc interpolator with specified maximum error and length.
   * @param emax the maximum error for frequencies less than fmax; e.g., 
   *  0.01 for 1% percent error. Must be greater than 0.0 and less than 1.0.
   * @param lmax the maximum interpolator length, in samples. Must be even
   *  and greater than zero.
   * @return the sinc interpolator.
   */
  public static SincInterpolator fromLengthAndError(
    int lmax, double emax)
  {
    Check.argument(0<lmax,"0 < lmax");
    Check.argument(lmax%2==0,"lmax is even");
    Check.argument(0.0<emax,"0.0 < emax");
    Check.argument(emax<1.0,"emax < 1.0");
    return new SincInterpolator(lmax,0,emax);
  }

  /**
   * Returns a sinc interpolator with specified maximum error and frequency.
   * @param emax the maximum error for frequencies less than fmax; e.g., 
   *  0.01 for 1% percent error. Must be greater than 0.0 and less than 1.0.
   * @param fmax the maximum frequency, in cycles per sample. Must be greater
   *  than 0.0 and less than 0.5.
   * @return the sinc interpolator.
   */
  public static SincInterpolator fromFrequencyAndError(
    double fmax, double emax)
  {
    Check.argument(0.0<fmax,"0.0 < fmax");
    Check.argument(fmax<0.5,"fmax < 0.5");
    Check.argument(0.0<emax,"0.0 < emax");
    Check.argument(emax<1.0,"emax < 1.0");
    return new SincInterpolator(0,fmax,emax);
  }

  /**
   * Constructs a default sinc interpolator. This interpolator will have
   * maximum length = 8, maximum frequency = 0.325 cycles/sample (65% of 
   * Nyquist), and error less than 0.01 (1%) for all frequencies less than 
   * the maximum frequency.
   */
  public SincInterpolator() {
    this(8,0.325,0.0);
  }

  /**
   * Constructs a sinc interpolator with specified parameters. The
   * parameters are maximum length, frequency, and error. 
   * <p>
   * Exactly one of these three parameters must be zero, because the sinc 
   * interpolator is defined by any two of them. The third parameter, the 
   * one with value zero, is computed in the design of the interpolator.
   * @param lmax the maximum interpolator length, in samples. Must be even.
   *  If zero, the interpolator length will be computed.
   * @param fmax the maximum frequency, in cycles per sample. Must be less
   *  than 0.5. If zero, the maximum frequency will be computed.
   * @param emax the maximum error for frequencies less than fmax; e.g., 
   *  0.01 for 1% percent error. Must be less than 1.0. If zero, the 
   *  maximum error will be computed.
   */
  public SincInterpolator(int lmax, double fmax, double emax) {
    Check.argument(lmax%2==0,"lmax is even");
    Check.argument(fmax<0.5,"fmax < 0.5");
    Check.argument(emax<1.0,"emax < 1.0");
    Check.argument(lmax==0 && fmax!=0.0 && emax!=0.0 ||
                   lmax!=0 && fmax==0.0 && emax!=0.0 ||
                   lmax!=0 && fmax!=0.0 && emax==0.0,
                   "exactly one of lmax, fmax, and emax is zero");

    // The Kaiser window is defined by any two of length, transition width, 
    // and error. The transition width is the difference between the Nyquist 
    // frequency 0.5 and the maximum frequency, if specified. We let the 
    // windowing account for half the interpolation error. 
    double wwin = 0.5-fmax;
    double ewin = 0.5*emax;
    KaiserWindow window = null;
    if (emax==0.0) {
      window = KaiserWindow.fromLengthAndWidth(lmax,wwin);
      ewin = window.getError();
      emax = 2.0*ewin;
    } else if (fmax==0.0) {
      window = KaiserWindow.fromLengthAndError(lmax,ewin);
      fmax = 0.5-window.getWidth();
    } else {
      window = KaiserWindow.fromWidthAndError(wwin,ewin);
      double lwin = window.getLength();
      lmax = (int)lwin; 
      if (lmax<lwin || lmax%2==1)
        ++lmax;
    }

    // The number of interpolators in the table depends on the error
    // in table lookup - more interpolators corresponds to less error.
    // The error in table lookup is whatever is left of the maximum
    // error, after accounting for the error due to windowing the sinc.
    double etab = emax-ewin;
    double dtab = etab*2.0/fmax;
    int ntab = 1+(int)ceil(1.0/dtab);
    int ltab = lmax;
    float[][] stab = new float[ntab][ltab];

    // The first and last interpolators are shifted unit impulses.
    // Handle these two cases exactly, with no rounding errors.
    for (int j=0; j<ltab; ++j) {
      stab[     0][j] = 0.0f;
      stab[ntab-1][j] = 0.0f;
    }
    stab[     0][ltab/2-1] = 1.0f;
    stab[ntab-1][ltab/2  ] = 1.0f;

    // Other interpolators are sampled Kaiser-windowed sinc functions.
    for (int itab=1; itab<ntab-1; ++itab) {
      double x = -ltab/2+1-dtab*itab;
      for (int i=0; i<ltab; ++i,x+=1.0) {
        stab[itab][i] = (float)(sinc(x)*window.evaluate(x));
      }
    }

    // The table of interpolators.
    _ltab = ltab;
    _ntab = ntab;
    _dtab = dtab;
    _stab = stab;
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
   * Determines whether or not this interpolator is complex.
   * @return true, if complex; false, otherwise.
   */
  public boolean isComplex() {
    return _complex;
  }

  /**
   * Sets the complex state of this interpolator. The default state is 
   * false, which corresponds to a real interpolator. Input and output 
   * samples are passed as arrays to and from an interpolator. A complex 
   * interpolator assumes that complex values in those arrays are packed 
   * as real, imaginary, real, imaginary, and so on.
   * @param complex true, for complex; false, otherwise.
   */
  public void setComplex(boolean complex) {
    _complex = true;
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
    _nxin = nxin;
    _dxin = dxin;
    _fxin = fxin;
    _ioutb = -_ltab-_ltab/2+1;
    _xoutf = fxin;
    _xouts = 1.0/dxin;
    _xoutb = _ltab-_xoutf*_xouts;
    _ntabm1 = _ntab-1;
    _nxinm = nxin-_ltab;
  }

  /**
   * Sets the current input samples for a uniformly-sampled function y(x).
   * If this interpolator is complex, real and imaginary parts of complex 
   * input values are packed as real, imaginary, real, imaginary, and so on.
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
   * Interpolates the current input samples of a function y(x).
   * If this interpolator is complex, then this method returns only the
   * real part of the complex interpolated output sample.
   * @param xout the value x at which to interpolate an output sample.
   * @return the interpolated output sample.
   */
  public float interpolate(double xout) {
    interpolate(xout,0,_yout);
    return _yout[0];
  }

  /**
   * Interpolates the current input samples of a function y(x).
   * @param xout the value x at which to interpolate an output sample.
   * @param iout the index in the output array of the interpolated output
   *  sample. If complex, the array index of the real part is 2*iout, and 
   *  the array index of the imaginary part is 2*iout+1.
   * @param yout array of output samples. This method modifies only the value 
   *  yout[iout] or, if complex, the values yout[2*iout] and yout[2*iout+1].
   */
  public void interpolate(double xout, int iout, float[] yout) {
    if (_complex) {
      interpolateComplex(xout,iout,yout);
    } else {
      interpolateReal(xout,iout,yout);
    }
  }

  /**
   * Interpolates the current samples of a uniformly-sampled function y(x).
   * @param nxout the number of output samples to interpolate.
   * @param xout array of values x at which to interpolate output y(x).
   * @param yout array of interpolated output y(x).
   */
  public void interpolate(int nxout, float[] xout, float[] yout) {
    if (_complex) {
      for (int ixout=0;  ixout<nxout; ++ixout)
        interpolateComplex(xout[ixout],ixout,yout);
    } else {
      for (int ixout=0;  ixout<nxout; ++ixout)
        interpolateReal(xout[ixout],ixout,yout);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

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
  private boolean _complex; // true if input/output samples are complex
  private float[] _yin; // real or complex samples

  // Temporary array for output values.
  private float[] _yout = new float[2];

  // Table of interpolation coefficients.
  private int _ltab; // length of interpolators in table
  private int _ntab; // number of interpolators in table
  private double _dtab; // sampling interval in table
  private float[][] _stab; // array[ntab][ltab] of sinc(x)

  private static double sinc(double x) {
    return (x!=0.0)?sin(PI*x)/(PI*x):1.0;
  }

  private void interpolateReal(double xout, int iout, float[] yout) {

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
    yout[iout] = youtr;
  }

  private void interpolateComplex(double xout, int iout, float[] yout) {

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
        int jyin = (kyin<0)?0:(_nxin<=kyin)?2*_nxin-1:2*kyin;
        float stabi = stab[itab];
        youtr += _yin[jyin  ]*stabi;
        youti += _yin[jyin+1]*stabi;
      }
    }
    yout[iout  ] = youtr;
    yout[iout+1] = youti;
  }
}
