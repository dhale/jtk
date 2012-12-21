/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;


import edu.mines.jtk.opt.BrentMinFinder;
import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * A sinc interpolator for bandlimited uniformly-sampled functions y(x). 
 * <p>
 * <em>
 * This class is deprecated because it is not thread-safe. 
 * Use the thread-safe class {@link edu.mines.jtk.dsp.SincInterp} instead.
 * </em>
 * <p>
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
 * It is the number of uniform samples required to interpolate a single 
 * value y(x). Ideally, the weights applied to each uniform sample are 
 * values of a sinc function. Although the ideal sinc function yields zero 
 * interpolation error for all frequencies up to the Nyquist frequency 
 * (0.5 cycles/sample), it has infinite length.
 * <p>
 * With recursive filtering, infinite-length approximations to the sinc
 * function are feasible and, in some applications, most efficient. When
 * the number of interpolated values is large relative to the number of 
 * uniform samples, the cost of recursive filtering is amortized over those 
 * many interpolated values, and can be negligible. However, this cost 
 * becomes significant when only a few values are interpolated for each 
 * sequence of uniform samples.
 * <p>
 * This interpolator is based on a <em>finite-length</em> approximation 
 * to the sinc function. The efficiency of finite-length interpolators 
 * like this one does not depend on the number of samples interpolated.
 * Also, this interpolator is robust in the presence of noise spikes, 
 * which affect only nearby samples.
 * <p>
 * Finite-length interpolators present a tradeoff between cost and accuracy.
 * Interpolators with small maximum lengths are most efficient, and those 
 * with high maximum frequencies and small maximum errors are most accurate.
 * <p>
 * When interpolating multiple uniformly sampled functions y(x) that share 
 * a common sampling of x, some redundant computations can be eliminated by 
 * specifying the sampling only once before interpolating multiple times. 
 * The resulting performance increase may be especially significant when 
 * only a few (perhaps only one) values are interpolated for each sequence 
 * of uniform samples.
 * <p>
 * For efficiency, interpolation coefficients (sinc approximations) are 
 * tabulated when an interpolator is constructed. The cost of building 
 * the table can easily exceed that of interpolating one sequence of 
 * samples, depending on the number of uniform samples. Therefore, one 
 * typically constructs an interpolator and uses it more than once.
 * @deprecated 
 * @author Dave Hale, Colorado School of Mines; Bill Harlan, Landmark Graphics
 * @version 2005.08.07
 */
@Deprecated
public class SincInterpolator {

  /**
   * The method used to extrapolate samples when interpolating.
   * Sampled functions are defined implicitly by extrapolation outside the 
   * domain for which samples are specified explicitly, with either zero or 
   * constant values. If constant, the extrapolated values are the first and 
   * last uniform sample values. The default is extrapolation with zeros.
   */
  public enum Extrapolation {
    ZERO, 
    CONSTANT,
  }

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
   * Sets the current sampling for a uniformly-sampled function y(x).
   * In some applications, this sampling never changes, and this method 
   * may be called only once for this interpolator.
   * @param nxu the number of uniform samples.
   * @param dxu the uniform sampling interval.
   * @param fxu the value x corresponding to the first uniform sample yu[0].
   */
  public void setUniformSampling(int nxu, double dxu, double fxu) {
    if (_asinc==null)
      makeTable();
    _nxu = nxu;
    _dxu = dxu;
    _fxu = fxu;
    _xf = fxu;
    _xs = 1.0/dxu;
    _xb = _lsinc-_xf*_xs;
    _nxum = nxu-_lsinc;
  }

  /**
   * Sets the current samples for a uniformly-sampled function y(x).
   * If sample values are complex numbers, real and imaginary parts are 
   * packed in the array as real, imaginary, real, imaginary, and so on.
   * <p>
   * Sample values are passed by reference, not by copy. Changes to sample
   * values in the specified array will yield changes in interpolated values.
   * @param yu array[nxu] of uniform samples of y(x); 
   *  by reference, not by copy.
   */
  public void setUniformSamples(float[] yu) {
    _yu = yu;
  }

  /**
   * Sets the current sampling and samples for a function y(x). 
   * This method simply calls the two methods 
   * {@link #setUniformSampling(int,double,double)} and
   * {@link #setUniformSamples(float[])} 
   * with the specified parameters.
   * @param nxu the number of uniform samples.
   * @param dxu the uniform sampling interval.
   * @param fxu the value x corresponding to the first uniform sample yu[0].
   * @param yu array[nxu] of uniform samples of y(x); 
   *  by reference, not by copy.
   */
  public void setUniform(int nxu, double dxu, double fxu, float[] yu) {
    setUniformSampling(nxu,dxu,fxu);
    setUniformSamples(yu);
  }

  /**
   * Interpolates the current uniform samples as real numbers.
   * @param x the value x at which to interpolate y(x).
   * @return the interpolated y(x).
   */
  public float interpolate(double x) {

    // Which uniform samples?
    double xn = _xb+x*_xs;
    int ixn = (int)xn;
    int kyu = _ib+ixn;

    // Which sinc approximation?
    double frac = xn-ixn;
    if (frac<0.0)
      frac += 1.0;
    int ksinc = (int)(frac*_nsincm1+0.5);
    float[] asinc = _asinc[ksinc];

    // If no extrapolation is necessary, use a fast loop.
      // Otherwise, extrapolate uniform samples, as necessary.
    float yr = 0.0f;
    if (kyu>=0 && kyu<=_nxum) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu)
        yr += _yu[kyu]*asinc[isinc];
    } else if (_extrap==Extrapolation.ZERO) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        if (0<=kyu && kyu<_nxu)
          yr += _yu[kyu]*asinc[isinc];
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        int jyu = (kyu<0)?0:(_nxu<=kyu)?_nxu-1:kyu;
        yr += _yu[jyu]*asinc[isinc];
      }
    }
    return yr;
  }

  /**
   * Interpolates the current uniform samples as real numbers.
   * @param nx the number of output samples.
   * @param x array[nx] of values x at which to interpolate y(x).
   * @param y array[nx] of interpolated output y(x).
   */
  public void interpolate(int nx, float[] x, float[] y) {
    for (int ix=0;  ix<nx; ++ix)
      y[ix] = interpolate(x[ix]);
  }

  /**
   * Interpolates the current uniform samples as real numbers. 
   * <p>
   * This method does not perform any anti-alias filtering, which may or 
   * may not be necessary to avoid aliasing when the specified output
   * sampling interval exceeds the current uniform sampling interval.
   * @param nx the number of output samples.
   * @param dx the output sampling interval.
   * @param fx the value x corresponding to the first output sample y[0].
   * @param y array[nx] of interpolated output y(x).
   */
  public void interpolate(int nx, double dx, double fx, float[] y) {
    if (dx==_dxu) {
      shift(nx,fx,y);
    } else {
      for (int ix=0; ix<nx; ++ix)
        y[ix] = interpolate(fx+ix*dx);
    }
  }

  /**
   * Interpolates the current uniform samples as complex numbers. 
   * Complex output samples are packed in the specified output array as 
   * real, imaginary, real, imaginary, and so on.
   * @param nx the number of output samples.
   * @param x array[nx] of values x at which to interpolate y(x).
   * @param y array[2*nx] of interpolated output y(x).
   */
  public void interpolateComplex(int nx, float[] x, float[] y) {
    for (int ix=0;  ix<nx; ++ix)
      interpolateComplex(ix,x[ix],y);
  }

  /**
   * Interpolates the current uniform samples as complex numbers. 
   * Complex output samples are packed in the specified output array as 
   * real, imaginary, real, imaginary, and so on.
   * @param nx the number of output samples.
   * @param dx the output sampling interval.
   * @param fx the value x corresponding to the first output sample (y[0],y[1]).
   * @param y array[2*nx] of interpolated output y(x).
   */
  public void interpolateComplex(
    int nx, double dx, double fx, float[] y) 
  {
    for (int ix=0;  ix<nx; ++ix)
      interpolateComplex(ix,fx+ix*dx,y);
  }

  /**
   * Finds a local maximum of the function y(x) near the specified value x.
   * The search for the maximum is restricted to an interval centered at the 
   * specified x and having width equal to the current uniform sampling 
   * interval.
   * <p>
   * Typically, the specified x corresponds to an uniform sample for which 
   * the value y(x) of that sample is not less than the values of the two 
   * nearest neighboring samples.
   * @param x the center of the interval in which y(x) is a maximum.
   * @return the value xmax for which y(xmax) is a local maximum.
   */
  public double findMax(double x) {
    return findMax(x,1.0);
  }

  /**
   * Finds a local minimum or maximum of the function y(x) near the
   * specified value x.  The type type of extremum is determined by
   * the second argument:  Any positive value means find the maximum,
   * otherwise find the minimum.  For simplicity and readibility,
   * use 1 for maximum, -1 for minimum.  The search for the extremum
   * is restricted to an interval centered at the specified x and having
   * width equal to the current uniform sampling interval.
   * <p>
   * Typically, the specified x corresponds to an uniform sample for which 
   * the value y(x) of that sample is not less than the values of the two 
   * nearest neighboring samples.
   * @param x the center of the interval in which y(x) is an extremum.
   * @param type the type of extremum; 1 for maximum, -1 for minimum.
   * @return the value xmax for which y(xmax) is a local minimum or maximum.
   */
  public double findMax(double x, double type) {
    double a = x-0.5*_dxu;
    double b = x+0.5*_dxu;
    double tol = _dsinc*_dxu;

    final double sign = type > 0 ? -1 : 1;
    BrentMinFinder finder = new BrentMinFinder(
      new BrentMinFinder.Function(){
        public double evaluate(double x) {
          return sign*interpolate(x);
        }
      });

    return finder.findMin(a,b,tol);
  }


  /**
   * Sets the current sampling for a uniformly-sampled function y(x1,x2).
   * In some applications, this sampling never changes, and this method 
   * may be called only once for this interpolator.
   * @param nx1u the number of uniform samples in 1st dimension.
   * @param dx1u the uniform sampling interval in 1st dimension.
   * @param fx1u the value x1 correponding to the first sample yu[0][0].
   * @param nx2u the number of uniform samples in 2nd dimension.
   * @param dx2u the uniform sampling interval in 2nd dimension.
   * @param fx2u the value x2 correponding to the first sample yu[0][0].
   */
  public void setUniformSampling(
    int nx1u, double dx1u, double fx1u,
    int nx2u, double dx2u, double fx2u) {
    if (_asinc==null)
      makeTable();
    _nx1u = nx1u;
    //_dx1u = dx1u;
    //_fx1u = fx1u;
    _x1f = fx1u;
    _x1s = 1.0/dx1u;
    _x1b = _lsinc-_x1f*_x1s;
    _nx1um = nx1u-_lsinc;
    _nx2u = nx2u;
    //_dx2u = dx2u;
    //_fx2u = fx2u;
    _x2f = fx2u;
    _x2s = 1.0/dx2u;
    _x2b = _lsinc-_x2f*_x2s;
    _nx2um = nx2u-_lsinc;
  }

  /**
   * Sets the current samples for a uniformly-sampled function y(x1,x2).
   * If sample values are complex numbers, real and imaginary parts are 
   * packed in the array as real, imaginary, real, imaginary, and so on.
   * <p>
   * Sample values are passed by reference, not by copy. Changes to sample
   * values in the specified array will yield changes in interpolated values.
   * @param yu array[nx2u][nx1u] of samples of y(x1,x2); 
   *  by reference, not by copy.
   */
  public void setUniformSamples(float[][] yu) {
    _yyu = yu;
  }

  /**
   * Sets the current sampling and samples for a function y(x1,x2). 
   * This method simply calls the two methods 
   * {@link #setUniformSampling(int,double,double,int,double,double)} and
   * {@link #setUniformSamples(float[][])} 
   * with the specified parameters.
   * @param nx1u the number of uniform samples in 1st dimension.
   * @param dx1u the uniform sampling interval in 1st dimension.
   * @param fx1u the value x1 correponding to the first sample yu[0][0].
   * @param nx2u the number of uniform samples in 2nd dimension.
   * @param dx2u the uniform sampling interval in 2nd dimension.
   * @param fx2u the value x2 correponding to the first sample yu[0][0].
   * @param yu array[nx2u][nx1u] of samples of y(x1,x2); 
   *  by reference, not by copy.
   */
  public void setUniform(
    int nx1u, double dx1u, double fx1u,
    int nx2u, double dx2u, double fx2u,
    float[][] yu) {
    setUniformSampling(nx1u,dx1u,fx1u,nx2u,dx2u,fx2u);
    setUniformSamples(yu);
  }

  /**
   * Interpolates the current uniform samples as real numbers.
   * @param x1 the value x1 at which to interpolate y(x1,x2).
   * @param x2 the value x2 at which to interpolate y(x1,x2).
   * @return the interpolated y(x1,x2).
   */
  public float interpolate(double x1, double x2) {

    // Which uniform samples?
    double x1n = _x1b+x1*_x1s;
    double x2n = _x2b+x2*_x2s;
    int ix1n = (int)x1n;
    int ix2n = (int)x2n;
    int ky1u = _ib+ix1n;
    int ky2u = _ib+ix2n;

    // Which sinc approximations?
    double frac1 = x1n-ix1n;
    double frac2 = x2n-ix2n;
    if (frac1<0.0)
      frac1 += 1.0;
    if (frac2<0.0)
      frac2 += 1.0;
    int ksinc1 = (int)(frac1*_nsincm1+0.5);
    int ksinc2 = (int)(frac2*_nsincm1+0.5);
    float[] asinc1 = _asinc[ksinc1];
    float[] asinc2 = _asinc[ksinc2];

    // If no extrapolation is necessary, use a fast loop.
    // Otherwise, extrapolate uniform samples, as necessary.
    float yr = 0.0f;
    if (ky1u>=0 && ky1u<=_nx1um &&  ky2u>=0 &&  ky2u<=_nx2um) {
      for (int i2sinc=0; i2sinc<_lsinc; ++i2sinc,++ky2u) {
        float asinc22 = asinc2[i2sinc];
        float[] yyuk2 = _yyu[ky2u];
        float yr2 = 0.0f;
        for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u)
          yr2 += yyuk2[my1u]*asinc1[i1sinc];
        yr += asinc22*yr2;
      }
    } else if (_extrap==Extrapolation.ZERO) {
      for (int i2sinc=0; i2sinc<_lsinc; ++i2sinc,++ky2u) {
        if (0<=ky2u && ky2u<_nx2u) {
          for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u) {
            if (0<=my1u && my1u<_nx1u)
              yr += _yyu[ky2u][my1u]*asinc2[i2sinc]*asinc1[i1sinc];
          }
        }
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int i2sinc=0; i2sinc<_lsinc; ++i2sinc,++ky2u) {
        int jy2u = (ky2u<0)?0:(_nx2u<=ky2u)?_nx2u-2:ky2u;
        for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u) {
          int jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1u-1:my1u;
          yr += _yyu[jy2u][jy1u]*asinc2[i2sinc]*asinc1[i1sinc];
        }
      }
    }
    return yr;
  }

  /**
   * Sets the current sampling for a uniformly-sampled function y(x1,x2,x3).
   * In some applications, this sampling never changes, and this method 
   * may be called only once for this interpolator.
   * @param nx1u the number of uniform samples in 1st dimension.
   * @param dx1u the uniform sampling interval in 1st dimension.
   * @param fx1u the value x1 correponding to the first sample yu[0][0][0].
   * @param nx2u the number of uniform samples in 2nd dimension.
   * @param dx2u the uniform sampling interval in 2nd dimension.
   * @param fx2u the value x2 correponding to the first sample yu[0][0][0].
   * @param nx3u the number of uniform samples in 3rd dimension.
   * @param dx3u the uniform sampling interval in 3rd dimension.
   * @param fx3u the value x3 correponding to the first sample yu[0][0][0].
   */
  public void setUniformSampling(
    int nx1u, double dx1u, double fx1u,
    int nx2u, double dx2u, double fx2u,
    int nx3u, double dx3u, double fx3u) 
  {
    if (_asinc==null)
      makeTable();
    _nx1u = nx1u;
    //_dx1u = dx1u;
    //_fx1u = fx1u;
    _x1f = fx1u;
    _x1s = 1.0/dx1u;
    _x1b = _lsinc-_x1f*_x1s;
    _nx1um = nx1u-_lsinc;
    _nx2u = nx2u;
    //_dx2u = dx2u;
    //_fx2u = fx2u;
    _x2f = fx2u;
    _x2s = 1.0/dx2u;
    _x2b = _lsinc-_x2f*_x2s;
    _nx2um = nx2u-_lsinc;
    _nx3u = nx3u;
    //_dx3u = dx3u;
    //_fx3u = fx3u;
    _x3f = fx3u;
    _x3s = 1.0/dx3u;
    _x3b = _lsinc-_x3f*_x3s;
    _nx3um = nx3u-_lsinc;
  }

  /**
   * Sets the current samples for a uniformly-sampled function y(x1,x2,x3).
   * If sample values are complex numbers, real and imaginary parts are 
   * packed in the array as real, imaginary, real, imaginary, and so on.
   * <p>
   * Sample values are passed by reference, not by copy. Changes to sample
   * values in the specified array will yield changes in interpolated values.
   * @param yu array[nx3u][nx2u][nx1u] of samples of y(x1,x2,x3); 
   *  by reference, not by copy.
   */
  public void setUniformSamples(float[][][] yu) {
    _yyyu = yu;
  }

  /**
   * Sets the current sampling and samples for a function y(x1,x2,x3). 
   * This method simply calls the two methods 
   * {@link #setUniformSampling(
   *          int,double,double,int,double,double,int,double,double)} and
   * {@link #setUniformSamples(float[][][])} 
   * with the specified parameters.
   * @param nx1u the number of uniform samples in 1st dimension.
   * @param dx1u the uniform sampling interval in 1st dimension.
   * @param fx1u the value x1 correponding to the first sample yu[0][0][0].
   * @param nx2u the number of uniform samples in 2nd dimension.
   * @param dx2u the uniform sampling interval in 2nd dimension.
   * @param fx2u the value x2 correponding to the first sample yu[0][0][0].
   * @param nx3u the number of uniform samples in 3rd dimension.
   * @param dx3u the uniform sampling interval in 3rd dimension.
   * @param fx3u the value x3 correponding to the first sample yu[0][0][0].
   * @param yu array[nx3u][nx2u][nx1u] of samples of y(x1,x2,x3); 
   *  by reference, not by copy.
   */
  public void setUniform(
    int nx1u, double dx1u, double fx1u,
    int nx2u, double dx2u, double fx2u,
    int nx3u, double dx3u, double fx3u,
    float[][][] yu) 
  {
    setUniformSampling(nx1u,dx1u,fx1u,nx2u,dx2u,fx2u,nx3u,dx3u,fx3u);
    setUniformSamples(yu);
  }

  /**
   * Interpolates the current uniform samples as real numbers.
   * @param x1 the value x1 at which to interpolate y(x1,x2,x3).
   * @param x2 the value x2 at which to interpolate y(x1,x2,x3).
   * @param x3 the value x3 at which to interpolate y(x1,x2,x3).
   * @return the interpolated y(x1,x2,x3).
   */
  public float interpolate(double x1, double x2, double x3) {

    // Which uniform samples?
    double x1n = _x1b+x1*_x1s;
    double x2n = _x2b+x2*_x2s;
    double x3n = _x3b+x3*_x3s;
    int ix1n = (int)x1n;
    int ix2n = (int)x2n;
    int ix3n = (int)x3n;
    int ky1u = _ib+ix1n;
    int ky2u = _ib+ix2n;
    int ky3u = _ib+ix3n;

    // Which sinc approximations?
    double frac1 = x1n-ix1n;
    double frac2 = x2n-ix2n;
    double frac3 = x3n-ix3n;
    if (frac1<0.0)
      frac1 += 1.0;
    if (frac2<0.0)
      frac2 += 1.0;
    if (frac3<0.0)
      frac3 += 1.0;
    int ksinc1 = (int)(frac1*_nsincm1+0.5);
    int ksinc2 = (int)(frac2*_nsincm1+0.5);
    int ksinc3 = (int)(frac3*_nsincm1+0.5);
    float[] asinc1 = _asinc[ksinc1];
    float[] asinc2 = _asinc[ksinc2];
    float[] asinc3 = _asinc[ksinc3];

    // If no extrapolation is necessary, use a fast loop.
    // Otherwise, extrapolate uniform samples, as necessary.
    float yr = 0.0f;
    if (ky1u>=0 && ky1u<=_nx1um &&  
        ky2u>=0 && ky2u<=_nx2um &&  
        ky3u>=0 && ky3u<=_nx3um) {
      for (int i3sinc=0; i3sinc<_lsinc; ++i3sinc,++ky3u) {
        float asinc33 = asinc3[i3sinc];
        float[][] yyy3 = _yyyu[ky3u];
        float yr2 = 0.0f;
        for (int i2sinc=0,my2u=ky2u; i2sinc<_lsinc; ++i2sinc,++my2u) {
          float asinc22 = asinc2[i2sinc];
          float[] yyy32 = yyy3[my2u];
          float yr1 = 0.0f;
          for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u)
            yr1 += yyy32[my1u]*asinc1[i1sinc];
          yr2 += asinc22*yr1;
        }
        yr += asinc33*yr2;
      }
    } else if (_extrap==Extrapolation.ZERO) {
      for (int i3sinc=0; i3sinc<_lsinc; ++i3sinc,++ky3u) {
        if (0<=ky3u && ky3u<_nx3u) {
          for (int i2sinc=0,my2u=ky2u; i2sinc<_lsinc; ++i2sinc,++my2u) {
            if (0<=my2u && my2u<_nx2u) {
              for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u) {
                if (0<=my1u && my1u<_nx1u)
                  yr += _yyyu[ky3u][my2u][my1u] *
                        asinc3[i3sinc] *
                        asinc2[i2sinc] *
                        asinc1[i1sinc];
              }
            }
          }
        }
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int i3sinc=0; i3sinc<_lsinc; ++i3sinc,++ky3u) {
        int jy3u = (ky3u<0)?0:(_nx3u<=ky3u)?_nx3u-2:ky3u;
        for (int i2sinc=0,my2u=ky2u; i2sinc<_lsinc; ++i2sinc,++my2u) {
          int jy2u = (my2u<0)?0:(_nx2u<=my2u)?_nx2u-2:my2u;
          for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u) {
            int jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1u-1:my1u;
            yr += _yyyu[jy3u][jy2u][jy1u] *
                  asinc3[i3sinc] *
                  asinc2[i2sinc] *
                  asinc1[i1sinc];
          }
        }
      }
    }
    return yr;
  }

  /**
   * Accumulates a specified real value y(x) into the current samples.
   * Accumulation is like the transpose (not the inverse) of interpolation.
   * This method modifies the current uniform sample values yu.
   * @param x the value x at which to accumulate y(x).
   * @param y the value y(x) to accumulate.
   */
  public void accumulate(double x, float y) {

    // Which uniform samples?
    double xn = _xb+x*_xs;
    int ixn = (int)xn;
    int kyu = _ib+ixn;

    // Which sinc approximation?
    double frac = xn-ixn;
    if (frac<0.0)
      frac += 1.0;
    int ksinc = (int)(frac*_nsincm1+0.5);
    float[] asinc = _asinc[ksinc];

    // If no extrapolation is necessary, use a fast loop.
      // Otherwise, extrapolate uniform samples, as necessary.
    if (kyu>=0 && kyu<=_nxum) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu)
        _yu[kyu] += y*asinc[isinc];
    } else if (_extrap==Extrapolation.ZERO) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        if (0<=kyu && kyu<_nxu)
          _yu[kyu] += y*asinc[isinc];
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        int jyu = (kyu<0)?0:(_nxu<=kyu)?_nxu-1:kyu;
        _yu[jyu] += y*asinc[isinc];
      }
    }
  }

  /**
   * Accumulates an array of real values y(x) into the current samples.
   * Accumulation is like the transpose (not the inverse) of interpolation.
   * This method modifies the current uniform sample values yu.
   * @param nx the number of values to accumulate.
   * @param x array[nx] of values x at which to accumulate y(x).
   * @param y array[nx] of values y(x) to accumulate.
   */
  public void accumulate(int nx, float[] x, float[] y) {
    for (int ix=0; ix<nx; ++ix)
      accumulate(x[ix],y[ix]);
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

  // Current uniform sampling.
  private int _nxu;
  private double _dxu;
  private double _fxu;
  private double _xf;
  private double _xs;
  private double _xb;
  private int _nxum;

  // Current uniform samples.
  private float[] _yu; // real or complex samples

  // Current 2-D or 3-D uniform sampling.
  private int _nx1u,_nx2u,_nx3u;
  //private double _dx1u,_dx2u,_dx3u;
  //private double _fx1u,_fx2u,_fx3u;
  private double _x1f,_x2f,_x3f;
  private double _x1s,_x2s,_x3s;
  private double _x1b,_x2b,_x3b;
  private int _nx1um,_nx2um,_nx3um;

  // Current 2-D or 3-D uniform samples.
  private float[][] _yyu;
  private float[][][] _yyyu;

  // Table of sinc interpolation coefficients.
  private int _lsinc; // length of sinc approximations
  private int _nsinc; // number of sinc approximations
  private double _dsinc; // sampling interval in table
  private float[][] _asinc; // array[nsinc][lsinc] of sinc approximations
  private double _nsincm1; // nsinc-1
  private int _ib; // -lsinc-lsinc/2+1

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
    KaiserWindow kwin;

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
    _ib = -_lsinc-_lsinc/2+1;

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

  /**
   * Get a copy of the interpolation table.  Returns a copy of this
   * interpolator's table of sinc interpolation coefficients.
   * @return A copy of the table.
   */
  public float[][] getTable() {
    if (_asinc==null)
      makeTable();
    assert _asinc != null;
    return copy(_asinc);
  }

  private static double sinc(double x) {
    return (x!=0.0)?sin(PI*x)/(PI*x):1.0;
  }

  private void interpolateComplex(int ix, double x, float[] y) {

    // Which uniform samples?
    double xn = _xb+x*_xs;
    int ixn = (int)xn;
    int kyu = _ib+ixn;

    // Which sinc approximation?
    double frac = xn-ixn;
    if (frac<0.0)
      frac += 1.0;
    int ksinc = (int)(frac*_nsincm1+0.5);
    float[] asinc = _asinc[ksinc];

    // If no extrapolation is necessary, use a fast loop.
      // Otherwise, extrapolate uniform samples, as necessary.
    float yr = 0.0f;
    float yi = 0.0f;
    if (kyu>=0 && kyu<=_nxum) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        int jyu = 2*kyu;
        float asinci = asinc[isinc];
        yr += _yu[jyu  ]*asinci;
        yi += _yu[jyu+1]*asinci;
      }
    } else if (_extrap==Extrapolation.ZERO) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        if (0<=kyu && kyu<_nxu) {
          int jyu = 2*kyu;
          float asinci = asinc[isinc];
          yr += _yu[jyu  ]*asinci;
          yi += _yu[jyu+1]*asinci;
        }
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        int jyu = (kyu<0)?0:(_nxu<=kyu)?2*_nxu-2:2*kyu;
        float asinci = asinc[isinc];
        yr += _yu[jyu  ]*asinci;
        yi += _yu[jyu+1]*asinci;
      }
    }
    int jx = 2*ix;
    y[jx  ] = yr;
    y[jx+1] = yi;
  }

  private void shift(int nx, double fx, float[] y) {

    // Uniform sampling.
    int nxu = _nxu;
    double dxu = _dxu;
    double fxu = _fxu;
    double lxu = fxu+(nxu-1)*dxu;

    // Which output samples are near beginning and end of uniform sequence?
    double dx = dxu;
    double x1 = fxu+dxu*_lsinc/2;
    double x2 = lxu-dxu*_lsinc/2;
    double x1n = (x1-fx)/dx;
    double x2n = (x2-fx)/dx;
    int ix1 = max(0,min(nx,(int)x1n+1));
    int ix2 = max(0,min(nx,(int)x2n-1));

    // Interpolate output samples near beginning of uniform sequence.
    for (int ix=0; ix<ix1; ++ix) {
      double x = fx+ix*dx;
      y[ix] = interpolate(x);
    }

    // Interpolate output samples near end of uniform sequence.
    for (int ix=ix2; ix<nx; ++ix) {
      double x = fx+ix*dx;
      y[ix] = interpolate(x);
    }

    // Now we ignore the ends, and use a single sinc approximation.

    // Which uniform samples?
    double xn = _xb+(fx+ix1*dx)*_xs;
    int ixn = (int)xn;
    int kyu = _ib+ixn;

    // Which sinc approximation?
    double frac = xn-ixn;
    if (frac<0.0)
      frac += 1.0;
    int ksinc = (int)(frac*_nsincm1+0.5);
    float[] asinc = _asinc[ksinc];

    // Interpolate for output indices ix1 <= ix <= ix2.
    for (int ix=ix1; ix<ix2; ++ix,++kyu) {
      float yr = 0.0f;
      for (int isinc=0,jyu=kyu; isinc<_lsinc; ++isinc,++jyu)
        yr += _yu[jyu]*asinc[isinc];
      y[ix] = yr;
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
    _ib = -_lsinc-_lsinc/2+1;

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
