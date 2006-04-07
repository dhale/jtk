/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.BrentMinFinder;
import edu.mines.jtk.util.Check;
import static java.lang.Math.*;

/**
 * A sinc interpolator for bandlimited uniformly-sampled functions y(x). 
 * Interpolators can be designed for any two of three parameters: maximum 
 * error (emax), maximum frequency (fmax) and maximum length (lmax). The 
 * parameter not specified is computed when an interpolator is designed.
 * <p>
 * Below the specified (or computed) maximum frequency fmax, the maximum 
 * interpolation error should be less than the specified (or computed) 
 * maximum error emax. For frequencies above fmax, interpolation error 
 * may be much greater. Therefore, sequences to be interpolated should 
 * be bandlimited to frequencies less than fmax.
 * <p>
 * The maximum length lmax of an interpolator is an even positive integer. 
 * It is the number of input samples required to interpolate a single 
 * output sample. Ideally, the weights applied to each input sample are 
 * values of a sinc function. Although the ideal sinc function yields zero 
 * interpolation error for all frequencies up to the Nyquist frequency 
 * (0.5 cycles/sample), it has infinite length.
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
 * Interpolators with small maximum lengths are most efficient, and those 
 * with high maximum frequencies and small maximum errors are most accurate.
 * <p>
 * When interpolating multiple uniformly sampled functions y(x) that share 
 * a common sampling of x, some redundant computations can be eliminated by 
 * specifying the sampling only once before interpolating multiple times. 
 * The resulting performance increase may be especially significant when 
 * only a few (perhaps only one) output samples are interpolated for each 
 * sequence of input samples.
 * <p>
 * For efficiency, interpolation coefficients (sinc approximations) are 
 * tabulated when an interpolator is constructed. The cost of building 
 * the table can easily exceed that of interpolating one input sequence 
 * of samples, depending on the number of input samples. Therefore, one 
 * typically constructs and reuses a single interpolator more than once.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.08.07
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
   * Computes the maximum frequency fmax. Note that for some parameters
   * emax and lmax, the maximum freuency fmax may be zero. In this case,
   * the returned interpolator is useless.
   * @param emax the maximum error for frequencies less than fmax; e.g., 
   *  0.01 for 1% percent error. 0.0 &lt; emax &lt;= 0.1 is required.
   * @param lmax the maximum interpolator length, in samples. 
   *  Must be an even integer not less than 8.
   * @return the sinc interpolator.
   */
  public static SincInterpolator fromErrorAndLength(
    double emax, int lmax)
  {
    return new SincInterpolator(emax,0.0,lmax);
  }

  /**
   * Returns a sinc interpolator with specified maximum error and frequency.
   * Computes the maximum length lmax.
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
   * Computes the maximum error emax.
   * <p>
   * The product (1-2*fmax)*lmax must be greater than one. For when this 
   * product is less than one, a useful upper bound on interpolation error 
   * cannot be computed.
   * @param fmax the maximum frequency, in cycles per sample. 
   *  Must be greater than 0.0 and less than 0.5*(1.0-1.0/lmax).
   * @param lmax the maximum interpolator length, in samples. 
   *  Must be an even integer not less than 8 and greater than 
   *  1.0/(1.0-2.0*fmax).
   * @return the sinc interpolator.
   */
  public static SincInterpolator fromFrequencyAndLength(
    double fmax, int lmax)
  {
    return new SincInterpolator(0.0,fmax,lmax);
  }

  /**
   * Returns a sinc interpolator using Ken Larner's least-squares method.
   * This interpolator is based on a Technical Memorandum written in 1980
   * by Ken Larner while at Western Geophysical. It is included here only
   * for historical and testing purposes. It is less flexible and yields 
   * more interpolation error than other interpolators constructed by this
   * class.
   * @param lmax the maximum interpolator length, in samples. 
   *  Must be an even integer between 8 and 16, inclusive.
   * @return the sinc interpolator.
   */
  public static SincInterpolator fromKenLarner(int lmax) {
    return new SincInterpolator(lmax);
  }
  private SincInterpolator(int lmax) {
    Check.argument(lmax%2==0,"lmax is even");
    Check.argument(lmax>=8,"lmax>=8");
    Check.argument(lmax<=16,"lmax<=16");
    _emax = 0.01;
    _fmax = 0.033+0.132*log(lmax); // Ken's empirical relationship
    _lmax = lmax;
    _nsinc = 2049;
    _dsinc = 1.0/(_nsinc-1);
    _lsinc = lmax;
    makeTableKenLarner();
  }

  /**
   * Constructs a default sinc interpolator. The default design parameters 
   * are fmax = 0.3 cycles/sample (60% of Nyquist) and lmax = 8 samples.
   * For these parameters, the computed maximum error is less than 0.007
   * (0.7%). In testing, observed maximum error is less than 0.005 (0.5%).
   */
  public SincInterpolator() {
    this(0.0,0.3,8);
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
   * Gets the number of bytes consumed by the table of interpolators.
   * The use of interpolators with small emax and large lmax may require 
   * the computation of large tables. This method can be used to determine 
   * how much memory is consumed by the table of an interpolator, before 
   * that is computed (when the interpolator is used for the first time).
   * @return the number of bytes.
   */
  public long getTableBytes() {
    long nbytes = 4L;
    nbytes *= _lsinc;
    nbytes *= _nsinc;
    return nbytes;
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
   * @param dxin the input sampling interval.
   * @param fxin the value x for the first input sample yin[0].
   */
  public void setInputSampling(int nxin, double dxin, double fxin) {
    if (_asinc==null)
      makeTable();
    _nxin = nxin;
    _dxin = dxin;
    _fxin = fxin;
    _xoutf = fxin;
    _xouts = 1.0/dxin;
    _xoutb = _lsinc-_xoutf*_xouts;
    _nxinm = nxin-_lsinc;
  }

  /**
   * Sets the current input samples for a uniformly-sampled function y(x).
   * If input sample values are complex numbers, real and imaginary parts 
   * are packed in the array as real, imaginary, real, imaginary, and so on.
   * <p>
   * Input samples are passed by reference, not by copy. Changes to sample
   * values in the specified array will yield changes in interpolated values.
   * @param yin array of input samples of y(x); by reference, not by copy.
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
   * @param dxin the input sampling interval.
   * @param fxin the value x for the first input sample yin[0].
   * @param yin array of input samples of y(x); by reference, not by copy.
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
    int ksinc = (int)(frac*_nsincm1+0.5);
    float[] asinc = _asinc[ksinc];

    // If no extrapolation is necessary, use a fast loop.
      // Otherwise, extrapolate input samples, as necessary.
    float youtr = 0.0f;
    if (kyin>=0 && kyin<=_nxinm) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyin)
        youtr += _yin[kyin]*asinc[isinc];
    } else if (_extrap==Extrapolation.ZERO) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyin) {
        if (0<=kyin && kyin<_nxin)
          youtr += _yin[kyin]*asinc[isinc];
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyin) {
        int jyin = (kyin<0)?0:(_nxin<=kyin)?_nxin-1:kyin;
        youtr += _yin[jyin]*asinc[isinc];
      }
    }
    return youtr;
  }

  /**
   * Interpolates the current input samples as real numbers.
   * @param nxout the number of output samples.
   * @param xout array of values x at which to interpolate output y(x).
   * @param yout array of interpolated output y(x).
   */
  public void interpolate(int nxout, float[] xout, float[] yout) {
    for (int ixout=0;  ixout<nxout; ++ixout)
      yout[ixout] = interpolate(xout[ixout]);
  }

  /**
   * Interpolates the current input samples as real numbers. 
   * <p>
   * This method does not perform any anti-alias filtering, which may or 
   * may not be necessary to avoid aliasing when the specified output
   * sampling interval exceeds the input sampling interval.
   * @param nxout the number of output samples.
   * @param dxout the output sampling interval.
   * @param fxout the value x for the first output sample yout[0].
   * @param yout array of interpolated output y(x).
   */
  public void interpolate(int nxout, double dxout, double fxout, float[] yout) {
    if (dxout==_dxin) {
      shift(nxout,fxout,yout);
    } else {
      for (int ixout=0; ixout<nxout; ++ixout)
        yout[ixout] = interpolate(fxout+ixout*dxout);
    }
  }

  /**
   * Interpolates the current input samples as complex numbers. 
   * Complex output samples are packed in the specified output array as 
   * real, imaginary, real, imaginary, and so on.
   * @param nxout the number of output samples.
   * @param xout array of values x at which to interpolate output y(x).
   * @param yout array of interpolated output y(x).
   */
  public void interpolateComplex(int nxout, float[] xout, float[] yout) {
    for (int ixout=0;  ixout<nxout; ++ixout)
      interpolateComplex(ixout,xout[ixout],yout);
  }

  /**
   * Interpolates the current input samples as complex numbers. 
   * Complex output samples are packed in the specified output array as 
   * real, imaginary, real, imaginary, and so on.
   * @param nxout the number of output samples.
   * @param dxout the output sampling interval.
   * @param fxout the value x for the first output sample (yout[0],yout[1]).
   * @param yout array of interpolated output y(x).
   */
  public void interpolateComplex(
    int nxout, double dxout, double fxout, float[] yout) 
  {
    for (int ixout=0;  ixout<nxout; ++ixout)
      interpolateComplex(ixout,fxout+ixout*dxout,yout);
  }

  /**
   * Finds a local maximum of the function y(x) near the specified value x.
   * The search for the maximum is restricted to an interval centered at the 
   * specified x and with width equal to the current input sampling interval.
   * <p>
   * Typically, the specified x corresponds to an input sample for which the 
   * value y(x) of that sample is not less than the values of the two nearest 
   * neighboring samples.
   * @param x the sample value near that for which y(x) is a maximum.
   * @return the sample value xmax for which y(xmax) is a local maximum.
   */
  public double findMax(double x) {
    double a = x-0.5*_dxin;
    double b = x+0.5*_dxin;
    double tol = _dsinc*_dxin;
    return _maxFinder.findMin(a,b,tol);
  }
  private BrentMinFinder _maxFinder = new BrentMinFinder(
    new BrentMinFinder.Function(){
      public double evaluate(double x) {
        return -interpolate(x);
      }
    });

  /**
   * Sets the current input sampling for a uniformly-sampled function y(x1,x2).
   * In some applications, this sampling never changes, and this method 
   * may be called only once for this interpolator.
   * @param nx1in the number of input samples in 1st dimension.
   * @param dx1in the input sampling interval in 1st dimension.
   * @param fx1in the value x1 for the first input sample yin[0][0].
   * @param nx2in the number of input samples in 2nd dimension.
   * @param dx2in the input sampling interval in 2nd dimension.
   * @param fx2in the value x2 for the first input sample yin[0][0].
   */
  public void setInputSampling(
    int nx1in, double dx1in, double fx1in,
    int nx2in, double dx2in, double fx2in) {
    if (_asinc==null)
      makeTable();
    _nx1in = nx1in;
    _dx1in = dx1in;
    _fx1in = fx1in;
    _x1outf = fx1in;
    _x1outs = 1.0/dx1in;
    _x1outb = _lsinc-_x1outf*_x1outs;
    _nx1inm = nx1in-_lsinc;
    _nx2in = nx2in;
    _dx2in = dx2in;
    _fx2in = fx2in;
    _x2outf = fx2in;
    _x2outs = 1.0/dx2in;
    _x2outb = _lsinc-_x2outf*_x2outs;
    _nx2inm = nx2in-_lsinc;
  }

  /**
   * Sets the current input samples for a uniformly-sampled function y(x1,x2).
   * If input sample values are complex numbers, real and imaginary parts 
   * are packed in the array as real, imaginary, real, imaginary, and so on.
   * <p>
   * Input samples are passed by reference, not by copy. Changes to sample
   * values in the specified array will yield changes in interpolated values.
   * @param yin array of input samples of y(x1,x2); by reference, not by copy.
   */
  public void setInputSamples(float[][] yin) {
    _yyin = yin;
  }

  /**
   * Sets the current input sampling and samples for a function y(x1,x2). 
   * This method simply calls the two methods 
   * {@link #setInputSampling(int,double,double,int,double,double)} and
   * {@link #setInputSamples(float[][])} 
   * with the specified parameters.
   * @param nx1in the number of input samples in 1st dimension.
   * @param dx1in the input sampling interval in 1st dimension.
   * @param fx1in the value x1 for the first input sample yin[0][0].
   * @param nx2in the number of input samples in 2nd dimension.
   * @param dx2in the input sampling interval in 2nd dimension.
   * @param fx2in the value x2 for the first input sample yin[0][0].
   * @param yin array of input samples of y(x1,x2); by reference, not by copy.
   */
  public void setInput(
    int nx1in, double dx1in, double fx1in,
    int nx2in, double dx2in, double fx2in,
    float[][] yin) {
    setInputSampling(nx1in,dx1in,fx1in,nx2in,dx2in,fx2in);
    setInputSamples(yin);
  }

  /**
   * Interpolates the current input samples as real numbers.
   * @param x1out the value x1 at which to interpolate an output y(x1,x2).
   * @param x2out the value x2 at which to interpolate an output y(x1,x2).
   * @return the interpolated output y(x1,x2).
   */
  public float interpolate(double x1out, double x2out) {

    // Which input samples?
    double x1outn = _x1outb+x1out*_x1outs;
    double x2outn = _x2outb+x2out*_x2outs;
    int ix1outn = (int)x1outn;
    int ix2outn = (int)x2outn;
    int ky1in = _ioutb+ix1outn;
    int ky2in = _ioutb+ix2outn;

    // Which sinc approximations?
    double frac1 = x1outn-ix1outn;
    double frac2 = x2outn-ix2outn;
    if (frac1<0.0)
      frac1 += 1.0;
    if (frac2<0.0)
      frac2 += 1.0;
    int ksinc1 = (int)(frac1*_nsincm1+0.5);
    int ksinc2 = (int)(frac2*_nsincm1+0.5);
    float[] asinc1 = _asinc[ksinc1];
    float[] asinc2 = _asinc[ksinc2];

    // If no extrapolation is necessary, use a fast loop.
    // Otherwise, extrapolate input samples, as necessary.
    float youtr = 0.0f;
    if (ky1in>=0 && ky1in<=_nx1inm &&  ky2in>=0 &&  ky2in<=_nx2inm) {
      for (int i2sinc=0; i2sinc<_lsinc; ++i2sinc,++ky2in) {
        float asinc22 = asinc2[i2sinc];
        float[] yyink2 = _yyin[ky2in];
        float youtr2 = 0.0f;
        for (int i1sinc=0,my1in=ky1in; i1sinc<_lsinc; ++i1sinc,++my1in)
          youtr2 += yyink2[my1in]*asinc1[i1sinc];
        youtr += asinc22*youtr2;
      }
    } else if (_extrap==Extrapolation.ZERO) {
      for (int i2sinc=0; i2sinc<_lsinc; ++i2sinc,++ky2in) {
        if (0<=ky2in && ky2in<_nx2in) {
          for (int i1sinc=0,my1in=ky1in; i1sinc<_lsinc; ++i1sinc,++my1in) {
            if (0<=my1in && my1in<_nx1in)
              youtr += _yyin[ky2in][my1in]*asinc2[i2sinc]*asinc1[i1sinc];
          }
        }
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int i2sinc=0; i2sinc<_lsinc; ++i2sinc,++ky2in) {
        int jy2in = (ky2in<0)?0:(_nx2in<=ky2in)?_nx2in-2:ky2in;
        for (int i1sinc=0,my1in=ky1in; i1sinc<_lsinc; ++i1sinc,++my1in) {
          int jy1in = (my1in<0)?0:(_nx1in<=my1in)?_nx1in-1:my1in;
          youtr += _yyin[jy2in][jy1in]*asinc2[i2sinc]*asinc1[i1sinc];
        }
      }
    }
    return youtr;
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
  private double _xoutf;
  private double _xouts;
  private double _xoutb;
  private int _nxinm;

  // Current input samples.
  private float[] _yin; // real or complex samples

  // Current 2-D input sampling.
  private int _nx1in,_nx2in;
  private double _dx1in,_dx2in;
  private double _fx1in,_fx2in;
  private double _x1outf,_x2outf;
  private double _x1outs,_x2outs;
  private double _x1outb,_x2outb;
  private int _nx1inm,_nx2inm;

  // Current 2-D input samples.
  private float[][] _yyin; // real or complex samples

  // Table of sinc interpolation coefficients.
  private int _lsinc; // length of sinc approximations
  private int _nsinc; // number of sinc approximations
  private double _dsinc; // sampling interval in table
  private float[][] _asinc; // array[nsinc][lsinc] of sinc approximations
  private double _nsincm1; // nsinc-1
  private int _ioutb; // -lsinc-lsinc/2+1

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
      Check.argument(lmax>=8,"lmax>=8");
      Check.argument(lmax%2==0,"lmax is even");
      Check.argument((1.0-2.0*fmax)*lmax>1.0,"(1.0-2.0*fmax)*lmax>1.0");
    } else if (fmax==0.0) {
      Check.argument(emax<=0.1,"emax<=0.1");
      Check.argument(lmax>=8,"lmax>=8");
      Check.argument(lmax%2==0,"lmax is even");
    } else if (lmax==0) {
      Check.argument(emax<=0.1,"emax<=0.1");
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
    // due to windowing. That windowing error is three times the error 
    // reported by the Kaiser window, because the conventional Kaiser
    // window stopband error is aliased multiple times with the passband
    // error. (A factor of at least two is necessary to handle the first 
    // aliasing, but experiments showed this factor to be inadequate. The 
    // factor three was found in testing to be sufficient for a wide range 
    // of design parameters.) Also, apply a lower bound to the error, so 
    // that the table of sinc approximations does not become too large.
    if (emax==0.0) {
      kwin = KaiserWindow.fromWidthAndLength(wwin,lmax);
      ewin = 3.0*kwin.getError();
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
      kwin = KaiserWindow.fromErrorAndLength(ewin/3.0,lmax);
      fmax = max(0.0,0.5-0.5*kwin.getWidth());
    }
    
    // Else if maximum error and width are specified, first compute a
    // lower bound on the maximum length, and then round that up to the
    // nearest even integer not less than 8. Finally, reconstruct a Kaiser 
    // window with that maximum length, ignoring the maximum frequency, 
    // which may be exceeded.
    else {
      kwin = KaiserWindow.fromErrorAndWidth(ewin/3.0,wwin);
      double lwin = kwin.getLength();
      lmax = (int)(lwin); 
      while (lmax<lwin || lmax<8 || lmax%2==1)
        ++lmax;
      kwin = KaiserWindow.fromErrorAndLength(ewin/3.0,lmax);
    }

    // The number of interpolators in the table depends on the error
    // in table lookup - more interpolators corresponds to less error.
    // The error in table lookup is whatever is left of the maximum
    // error, after accounting for the error due to windowing the sinc.
    // The number of interpolators is a power of two plus 1, so that table 
    // lookup error is zero when interpolating halfway between samples.
    double etab = emax-ewin;
    _dsinc = (fmax>0.0)?etab/(PI*fmax):1.0;
    int nsincMin = 1+(int)ceil(1.0/_dsinc);
    _nsinc = 2;
    while (_nsinc<nsincMin)
      _nsinc *= 2;
    ++_nsinc;
    _dsinc = 1.0/(_nsinc-1);
    _lsinc = lmax;

    // Save design parameters and Kaiser window, so we can build the table.
    _emax = emax;
    _fmax = fmax;
    _lmax = lmax;
    _kwin = kwin;
  }

  /**
   * Builds the table of interpolators. This may be costly, so we do it 
   * only when an interpolator is used, not when it is constructed. Users` 
   * may construct more than one interpolator, perhaps in a search for 
   * optimal design parameters, without actually using all of them.
   */
  private void makeTable() {
    _asinc = new float[_nsinc][_lsinc];
    _nsincm1 = _nsinc-1;
    _ioutb = -_lsinc-_lsinc/2+1;

    // The first and last interpolators are shifted unit impulses.
    // Handle these two cases exactly, with no rounding errors.
    for (int j=0; j<_lsinc; ++j) {
      _asinc[       0][j] = 0.0f;
      _asinc[_nsinc-1][j] = 0.0f;
    }
    _asinc[       0][_lsinc/2-1] = 1.0f;
    _asinc[_nsinc-1][_lsinc/2  ] = 1.0f;

    // Other interpolators are sampled Kaiser-windowed sinc functions.
    for (int isinc=1; isinc<_nsinc-1; ++isinc) {
      double x = -_lsinc/2+1-_dsinc*isinc;
      for (int i=0; i<_lsinc; ++i,x+=1.0) {
        _asinc[isinc][i] = (float)(sinc(x)*_kwin.evaluate(x));
      }
    }
  }

  private static double sinc(double x) {
    return (x!=0.0)?sin(PI*x)/(PI*x):1.0;
  }

  private void interpolateComplex(int ixout, double xout, float[] yout) {

    // Which input samples?
    double xoutn = _xoutb+xout*_xouts;
    int ixoutn = (int)xoutn;
    int kyin = _ioutb+ixoutn;

    // Which sinc approximation?
    double frac = xoutn-ixoutn;
    if (frac<0.0)
      frac += 1.0;
    int ksinc = (int)(frac*_nsincm1+0.5);
    float[] asinc = _asinc[ksinc];

    // If no extrapolation is necessary, use a fast loop.
      // Otherwise, extrapolate input samples, as necessary.
    float youtr = 0.0f;
    float youti = 0.0f;
    if (kyin>=0 && kyin<=_nxinm) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyin) {
        int jyin = 2*kyin;
        float asinci = asinc[isinc];
        youtr += _yin[jyin  ]*asinci;
        youti += _yin[jyin+1]*asinci;
      }
    } else if (_extrap==Extrapolation.ZERO) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyin) {
        if (0<=kyin && kyin<_nxin) {
          int jyin = 2*kyin;
          float asinci = asinc[isinc];
          youtr += _yin[jyin  ]*asinci;
          youti += _yin[jyin+1]*asinci;
        }
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyin) {
        int jyin = (kyin<0)?0:(_nxin<=kyin)?2*_nxin-2:2*kyin;
        float asinci = asinc[isinc];
        youtr += _yin[jyin  ]*asinci;
        youti += _yin[jyin+1]*asinci;
      }
    }
    int jxout = 2*ixout;
    yout[jxout  ] = youtr;
    yout[jxout+1] = youti;
  }

  private void shift(int nxout, double fxout, float[] yout) {

    // Input sampling.
    int nxin = _nxin;
    double dxin = _dxin;
    double fxin = _fxin;
    double lxin = fxin+(nxin-1)*dxin;

    // Which output samples are near beginning and end of input sequence?
    double dxout = dxin;
    double xout1 = fxin+dxin*_lsinc/2;
    double xout2 = lxin-dxin*_lsinc/2;
    double xout1n = (xout1-fxout)/dxout;
    double xout2n = (xout2-fxout)/dxout;
    int ixout1 = max(0,min(nxout,(int)xout1n)+1);
    int ixout2 = max(0,min(nxout,(int)xout2n)-1);

    // Interpolate output samples near beginning of input sequence.
    for (int ixout=0; ixout<ixout1; ++ixout) {
      double xout = fxout+ixout*dxout;
      yout[ixout] = interpolate(xout);
    }

    // Interpolate output samples near end of input sequence.
    for (int ixout=ixout2; ixout<nxout; ++ixout) {
      double xout = fxout+ixout*dxout;
      yout[ixout] = interpolate(xout);
    }

    // Now we ignore the ends, and use a single sinc approximation.

    // Which input samples?
    double xoutn = _xoutb+(fxout+ixout1*dxout)*_xouts;
    int ixoutn = (int)xoutn;
    int kyin = _ioutb+ixoutn;

    // Which sinc approximation?
    double frac = xoutn-ixoutn;
    if (frac<0.0)
      frac += 1.0;
    int ksinc = (int)(frac*_nsincm1+0.5);
    float[] asinc = _asinc[ksinc];

    // Interpolate for output indices ixout1 <= ixout <= ixout2.
    for (int ixout=ixout1; ixout<ixout2; ++ixout,++kyin) {
      float youtr = 0.0f;
      for (int isinc=0,jyin=kyin; isinc<_lsinc; ++isinc,++jyin)
        youtr += _yin[jyin]*asinc[isinc];
      yout[ixout] = youtr;
    }
  }

  /**
   * Builds the table of interpolators using Ken Larner's old method. 
   * This method exists for comparison with the Kaiser window method.
   * Ken's method is the one that has been in SU and ProMAX for years.
   */
  private void makeTableKenLarner() {
    _asinc = new float[_nsinc][_lsinc];
    _nsincm1 = _nsinc-1;
    _ioutb = -_lsinc-_lsinc/2+1;

    // The first and last interpolators are shifted unit impulses.
    // Handle these two cases exactly, with no rounding errors.
    for (int j=0; j<_lsinc; ++j) {
      _asinc[       0][j] = 0.0f;
      _asinc[_nsinc-1][j] = 0.0f;
    }
    _asinc[       0][_lsinc/2-1] = 1.0f;
    _asinc[_nsinc-1][_lsinc/2  ] = 1.0f;

    // Other interpolators are least-squares approximations to sincs.
    for (int isinc=1; isinc<_nsinc-1; ++isinc) {
      double frac = (double)isinc/(double)(_nsinc-1);
      mksinc(frac,_lsinc,_asinc[isinc]);
    }
  }
  private static void mksinc(double d, int lsinc, float[] sinc) {
    double[] s = new double[lsinc];
    double[] a = new double[lsinc];
    double[] c = new double[lsinc];
    double[] w = new double[lsinc];
    double fmax = 0.033+0.132*log(lsinc);
    if (fmax>0.5)
      fmax = 0.5;
    for (int j=0; j<lsinc; ++j) {
      a[j] = sinc(2.0*fmax*j);
      c[j] = sinc(2.0*fmax*(lsinc/2-j-1+d));
    }
    stoepd(lsinc,a,c,s,w);
    for (int j=0; j<lsinc; ++j)
      sinc[j] = (float)s[j];
  }
  private static void stoepd (
    int n, double[] r, double[] g, double[] f, double[] a)
  {
    if (r[0]==0.0) 
      return;
    a[0] = 1.0;
    double v = r[0];
    f[0] = g[0]/r[0];
    for (int j=1; j<n; j++) {
      a[j] = 0.0;
      f[j] = 0.0;
      double e = 0.0;
      for (int i=0; i<j; i++)
        e += a[i]*r[j-i];
      double c = e/v;
      v -= c*e;
      for (int i=0; i<=j/2; i++) {
        double bot = a[j-i]-c*a[i];
        a[i] -= c*a[j-i];
        a[j-i] = bot;
      }
      double w = 0.0;
      for (int i=0; i<j; i++)
        w += f[i]*r[j-i];
      c = (w-g[j])/v;
      for (int i=0; i<=j; i++)
        f[i] -= c*a[j-i];
    }
  }
}
