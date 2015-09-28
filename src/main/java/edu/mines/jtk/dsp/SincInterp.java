/****************************************************************************
Copyright 2012, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.dsp;

import java.util.HashMap;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

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
 * When interpolating multiple values of y(x) from a single sequence of
 * uniformly sampled values, efficiency may be improved by using one of the
 * methods that enables specification of multiple x values at which to
 * interpolate.
 *
 * @author Dave Hale, Colorado School of Mines
 * @author Bill Harlan, Landmark Graphics
 * @version 2012.12.21
 * @deprecated Use class {@link edu.mines.jtk.dsp.SincInterpolator} instead.
 */
public class SincInterp {

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
   * emax and lmax, the maximum frequency fmax may be zero. In this case,
   * the returned interpolator is useless.
   * @param emax the maximum error for frequencies less than fmax; e.g., 
   *  0.01 for 1% percent error. 0.0 &lt; emax &lt;= 0.1 is required.
   * @param lmax the maximum interpolator length, in samples. 
   *  Must be an even integer not less than 8.
   * @return the sinc interpolator.
   */
  public static SincInterp fromErrorAndLength(
    double emax, int lmax)
  {
    return new SincInterp(emax,0.0,lmax);
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
  public static SincInterp fromErrorAndFrequency(
    double emax, double fmax)
  {
    return new SincInterp(emax,fmax,0);
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
  public static SincInterp fromFrequencyAndLength(
    double fmax, int lmax)
  {
    return new SincInterp(0.0,fmax,lmax);
  }

  /**
   * Constructs a default sinc interpolator. The default design parameters 
   * are fmax = 0.3 cycles/sample (60% of Nyquist) and lmax = 8 samples.
   * For these parameters, the computed maximum error is less than 0.007
   * (0.7%). In testing, observed maximum error is less than 0.005 (0.5%).
   */
  public SincInterp() {
    this(0.0,0.3,8);
  }

  /**
   * Gets the maximum error for this interpolator.
   * @return the maximum error.
   */
  public double getMaximumError() {
    return _table.design.emax;
  }

  /**
   * Gets the maximum frequency for this interpolator.
   * @return the maximum frequency.
   */
  public double getMaximumFrequency() {
    return _table.design.fmax;
  }

  /**
   * Gets the maximum length for this interpolator.
   * @return the maximum length.
   */
  public int getMaximumLength() {
    return _table.design.lmax;
  }

  /**
   * Gets the number of bytes consumed by the table of interpolators.
   * The use of interpolators with small emax and large lmax may require 
   * the computation of large tables. This method can be used to determine 
   * how much memory is consumed by the table for an interpolator.
   * @return the number of bytes.
   */
  public long getTableBytes() {
    long nbytes = 4L;
    nbytes *= _table.lsinc;
    nbytes *= _table.nsinc;
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
   * Interpolates one real value y(x).
   * @param nxu number of input samples.
   * @param dxu input sampling interval.
   * @param fxu first input sampled x value.
   * @param yu input array of sampled values y(x).
   * @param xi value x at which to interpolate.
   * @return interpolated value y(x).
   */
  public float interpolate(
    int nxu, double dxu, double fxu, float[] yu, double xi)
  {
    double xscale = 1.0/dxu;
    double xshift = _lsinc-fxu*xscale;
    int nxum = nxu-_lsinc;
    return interpolate(xscale,xshift,nxum,nxu,yu,xi);
  }

  /**
   * Interpolates multiple real values y(x).
   * @param nxu number of input samples.
   * @param dxu input sampling interval.
   * @param fxu first input sampled x value.
   * @param yu input array of sampled values y(x).
   * @param nxi number of output samples.
   * @param xi input array of x values at which to interpolate.
   * @param yi output array of interpolated values y(x).
   */
  public void interpolate(
    int nxu, double dxu, double fxu, float[] yu, 
    int nxi, float[] xi, float[] yi)
  {
    double xscale = 1.0/dxu;
    double xshift = _lsinc-fxu*xscale;
    int nxum = nxu-_lsinc;
    for (int ixi=0; ixi<nxi; ++ixi)
      yi[ixi] = interpolate(xscale,xshift,nxum,nxu,yu,xi[ixi]);
  }

  /**
   * Interpolates multiple real values y(x).
   * @param nxu number of input samples.
   * @param dxu input sampling interval.
   * @param fxu first input sampled x value.
   * @param yu input array of sampled values y(x).
   * @param nxi number of output samples.
   * @param dxi output sampling interval.
   * @param fxi first output sampled x value.
   * @param yi output array of interpolated values y(x).
   */
  public void interpolate(
    int nxu, double dxu, double fxu, float[] yu, 
    int nxi, double dxi, double fxi, float[] yi)
  {
    if (dxu==dxi) {
      shift(nxu,dxu,fxu,yu,nxi,fxi,yi);
    } else {
      double xscale = 1.0/dxu;
      double xshift = _lsinc-fxu*xscale;
      int nxum = nxu-_lsinc;
      for (int ixi=0; ixi<nxi; ++ixi) {
        double xi = fxi+ixi*dxi;
        yi[ixi] = interpolate(xscale,xshift,nxum,nxu,yu,xi);
      }
    }
  }

  /**
   * Interpolates one real value y(x1,x2).
   * @param nx1u number of input samples in 1st dimension.
   * @param dx1u input sampling interval in 1st dimension.
   * @param fx1u first input sampled x value in 1st dimension.
   * @param nx2u number of input samples in 2nd dimension.
   * @param dx2u input sampling interval in 2nd dimension.
   * @param fx2u first input sampled x value in 2nd dimension.
   * @param yu input array of sampled values y(x).
   * @param x1i 1st coordinate of x at which to interpolate.
   * @param x2i 2nd coordinate of x at which to interpolate.
   * @return interpolated value y(x).
   */
  public float interpolate(
    int nx1u, double dx1u, double fx1u, 
    int nx2u, double dx2u, double fx2u, 
    float[][] yu, double x1i, double x2i)
  {
    double x1scale = 1.0/dx1u;
    double x2scale = 1.0/dx2u;
    double x1shift = _lsinc-fx1u*x1scale;
    double x2shift = _lsinc-fx2u*x2scale;
    int nx1um = nx1u-_lsinc;
    int nx2um = nx2u-_lsinc;
    return interpolate(
      x1scale,x1shift,nx1um,nx1u,
      x2scale,x2shift,nx2um,nx2u,
      yu,x1i,x2i);
  }

  /**
   * Interpolates one real value y(x1,x2,x3).
   * @param nx1u number of input samples in 1st dimension.
   * @param dx1u input sampling interval in 1st dimension.
   * @param fx1u first input sampled x value in 1st dimension.
   * @param nx2u number of input samples in 2nd dimension.
   * @param dx2u input sampling interval in 2nd dimension.
   * @param fx2u first input sampled x value in 2nd dimension.
   * @param nx3u number of input samples in 3rd dimension.
   * @param dx3u input sampling interval in 3rd dimension.
   * @param fx3u first input sampled x value in 3rd dimension.
   * @param yu input array of sampled values y(x).
   * @param x1i 1st coordinate of x at which to interpolate.
   * @param x2i 2nd coordinate of x at which to interpolate.
   * @param x3i 3rd coordinate of x at which to interpolate.
   * @return interpolated value y(x).
   */
  public float interpolate(
    int nx1u, double dx1u, double fx1u, 
    int nx2u, double dx2u, double fx2u, 
    int nx3u, double dx3u, double fx3u, 
    float[][][] yu, double x1i, double x2i, double x3i)
  {
    double x1scale = 1.0/dx1u;
    double x2scale = 1.0/dx2u;
    double x3scale = 1.0/dx3u;
    double x1shift = _lsinc-fx1u*x1scale;
    double x2shift = _lsinc-fx2u*x2scale;
    double x3shift = _lsinc-fx3u*x3scale;
    int nx1um = nx1u-_lsinc;
    int nx2um = nx2u-_lsinc;
    int nx3um = nx3u-_lsinc;
    return interpolate(
      x1scale,x1shift,nx1um,nx1u,
      x2scale,x2shift,nx2um,nx2u,
      x3scale,x3shift,nx3um,nx3u,
      yu,x1i,x2i,x3i);
  }

  /**
   * Interpolates one real value y(x).
   * @param sxu sampling of input samples.
   * @param yu input array of uniformly sampled values y(x).
   * @param xi value x at which to interpolate.
   * @return interpolated value y(x).
   */
  public float interpolate(Sampling sxu, float[] yu, double xi) {
    Check.argument(sxu.isUniform(),"input sampling is uniform");
    return interpolate(sxu.getCount(),sxu.getDelta(),sxu.getFirst(),yu,xi);
  }

  /**
   * Interpolates multiple real values y(x).
   * @param sxu sampling of input samples.
   * @param yu input array of uniformly sampled values y(x).
   * @param sxi sampling of output samples.
   * @param yi output array of interpolated values y(x).
   */
  public void interpolate(
    Sampling sxu, float[] yu, 
    Sampling sxi, float[] yi) 
  {
    Check.argument(sxu.isUniform(),"input sampling is uniform");
    if (sxi.isUniform()) {
      interpolate(sxu.getCount(),sxu.getDelta(),sxu.getFirst(),yu,
                  sxi.getCount(),sxi.getDelta(),sxi.getFirst(),yi);
    } else {
      int nxu = sxu.getCount();
      int nxi = sxi.getCount();
      double xscale = 1.0/sxu.getDelta();
      double xshift = _lsinc-sxu.getFirst()*xscale;
      int nxum = nxu-_lsinc;
      for (int ixi=0; ixi<nxi; ++ixi) {
        double xi = sxi.getValue(ixi);
        yi[ixi] = interpolate(xscale,xshift,nxum,nxu,yu,xi);
      }
    }
  }

  /**
   * Interpolates one real value y(x1,x2).
   * @param sx1u sampling of input x in 1st dimension.
   * @param sx2u sampling of input x in 2nd dimension.
   * @param yu input array of sampled values y(x).
   * @param x1i 1st coordinate of x at which to interpolate.
   * @param x2i 2nd coordinate of x at which to interpolate.
   * @return interpolated value y(x).
   */
  public float interpolate(
    Sampling sx1u, Sampling sx2u,
    float[][] yu, double x1i, double x2i)
  {
    Check.argument(sx1u.isUniform(),"input sampling of x1 is uniform");
    Check.argument(sx2u.isUniform(),"input sampling of x2 is uniform");
    return interpolate(
      sx1u.getCount(),sx1u.getDelta(),sx1u.getFirst(),
      sx2u.getCount(),sx2u.getDelta(),sx2u.getFirst(),
      yu,x1i,x2i);
  }

  /**
   * Interpolates one real value y(x1,x2,x3).
   * @param sx1u sampling of input x in 1st dimension.
   * @param sx2u sampling of input x in 2nd dimension.
   * @param sx3u sampling of input x in 3rd dimension.
   * @param yu input array of sampled values y(x).
   * @param x1i 1st coordinate of x at which to interpolate.
   * @param x2i 2nd coordinate of x at which to interpolate.
   * @param x3i 3rd coordinate of x at which to interpolate.
   * @return interpolated value y(x).
   */
  public float interpolate(
    Sampling sx1u, Sampling sx2u, Sampling sx3u,
    float[][][] yu, double x1i, double x2i, double x3i)
  {
    Check.argument(sx1u.isUniform(),"input sampling of x1 is uniform");
    Check.argument(sx2u.isUniform(),"input sampling of x2 is uniform");
    Check.argument(sx3u.isUniform(),"input sampling of x3 is uniform");
    return interpolate(
      sx1u.getCount(),sx1u.getDelta(),sx1u.getFirst(),
      sx2u.getCount(),sx2u.getDelta(),sx2u.getFirst(),
      sx3u.getCount(),sx3u.getDelta(),sx3u.getFirst(),
      yu,x1i,x2i,x3i);
  }

  /**
   * Interpolates multiple complex values y(x).
   * Complex output samples are packed in the specified output array as 
   * real, imag, real, imag, ....
   * @param nxu number of input samples.
   * @param dxu input sampling interval.
   * @param fxu first input sampled x value.
   * @param yu input array[2*nxu] of sampled complex values y(x).
   * @param nxi number of output samples.
   * @param dxi output sampling interval.
   * @param fxi first output sampled x value.
   * @param yi output array[2*nxi] of interpolated complex values y(x).
   */
  public void interpolateComplex(
    int nxu, double dxu, double fxu, float[] yu, 
    int nxi, double dxi, double fxi, float[] yi)
  {
    double xscale = 1.0/dxu;
    double xshift = _lsinc-fxu*xscale;
    int nxum = nxu-_lsinc;
    for (int ixi=0; ixi<nxi; ++ixi) {
      double xi = fxi+ixi*dxi;
      interpolateComplex(xscale,xshift,nxum,nxu,yu,ixi,xi,yi);
    }
  }

  /**
   * Interpolates multiple complex values y(x).
   * Complex output samples are packed in the specified output array as 
   * real, imag, real, imag, ....
   * @param nxu number of input samples.
   * @param dxu input sampling interval.
   * @param fxu first input sampled x value.
   * @param yu input array[2*nxu] of sampled complex values y(x).
   * @param nxi number of output samples.
   * @param xi input array[nxi] of x values at which to interpolate.
   * @param yi output array[2*nxi] of interpolated complex values y(x).
   */
  public void interpolateComplex(
    int nxu, double dxu, double fxu, float[] yu, 
    int nxi, float[] xi, float[] yi)
  {
    double xscale = 1.0/dxu;
    double xshift = _lsinc-fxu*xscale;
    int nxum = nxu-_lsinc;
    for (int ixi=0; ixi<nxi; ++ixi)
      interpolateComplex(xscale,xshift,nxum,nxu,yu,ixi,xi[ixi],yi);
  }

  /**
   * Interpolates multiple complex values y(x).
   * Complex output samples are packed in the specified output array as 
   * real, imag, real, imag, ....
   * @param sxu sampling of input samples.
   * @param yu input array[2*nxu] of sampled complex values y(x).
   * @param sxi sampling of output samples.
   * @param yi output array[2*nxi] of interpolated complex values y(x).
   */
  public void interpolateComplex(
    Sampling sxu, float[] yu, 
    Sampling sxi, float[] yi)
  {
    Check.argument(sxu.isUniform(),"input sampling is uniform");
    if (sxi.isUniform()) {
      interpolateComplex(sxu.getCount(),sxu.getDelta(),sxu.getFirst(),yu,
                         sxi.getCount(),sxi.getDelta(),sxi.getFirst(),yi);
    } else {
      int nxu = sxu.getCount();
      int nxi = sxi.getCount();
      double xscale = 1.0/sxu.getDelta();
      double xshift = _lsinc-sxu.getFirst()*xscale;
      int nxum = nxu-_lsinc;
      for (int ixi=0; ixi<nxi; ++ixi) {
        double xi = sxi.getValue(ixi);
        interpolateComplex(xscale,xshift,nxum,nxu,yu,ixi,xi,yi);
      }
    }
  }

  /**
   * Accumulates a specified real value y(x) into uniformly-sampled yu.
   * Accumulation is the transpose (not the inverse) of interpolation.
   * Whereas interpolation gathers from uniformly sampled values.
   * accumulation scatters into uniformly sampled values.
   * @param xa value x at which to accumulate.
   * @param ya value y(x) to accumulate.
   * @param nxu number of input/output samples.
   * @param dxu input/output sampling interval.
   * @param fxu first input/output sampled x value.
   * @param yu input/output array of sampled values y(x).
   */
  public void accumulate(
    double xa, float ya,
    int nxu, double dxu, double fxu, float[] yu) 
  {
    double xscale = 1.0/dxu;
    double xshift = _lsinc-fxu*xscale;
    int nxum = nxu-_lsinc;
    accumulate(xscale,xshift,nxum,xa,ya,nxu,yu);
  }

  /**
   * Accumulates a specified real value y(x) into uniformly-sampled yu.
   * Accumulation is the transpose (not the inverse) of interpolation.
   * Whereas interpolation gathers from uniformly sampled values.
   * accumulation scatters into uniformly sampled values.
   * @param nxa number of values to accumulate.
   * @param xa input array of values x at which to accumulate.
   * @param ya input array of values y(x) to accumulate.
   * @param nxu number of input/output samples.
   * @param dxu input/output sampling interval.
   * @param fxu first input/output sampled x value.
   * @param yu input/output array of sampled values y(x).
   */
  public void accumulate(
    int nxa, float[] xa, float[] ya,
    int nxu, double dxu, double fxu, float[] yu) 
  {
    double xscale = 1.0/dxu;
    double xshift = _lsinc-fxu*xscale;
    int nxum = nxu-_lsinc;
    for (int ixa=0; ixa<nxa; ++ixa)
      accumulate(xscale,xshift,nxum,xa[ixa],ya[ixa],nxu,yu);
  }

  /**
   * Get a copy of the interpolation table.  Returns a copy of this
   * interpolator's table of sinc interpolation coefficients.
   * @return A copy of the table.
   */
  public float[][] getTable() {
    return copy(_table.asinc);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Fraction of error due to windowing; remainder is due to table lookup.
  private static final double EWIN_FRAC = 0.9;

  // Maximum table size, when maximum error and frequency are specified.
  private static final int NTAB_MAX = 16385;

  // Extrapolation method.
  private Extrapolation _extrap = Extrapolation.ZERO;

  // Table of sinc interpolation coefficients.
  private Table _table; // with all fields cached below
  private int _lsinc; // length of sinc approximations
  private int _nsinc; // number of sinc approximations
  private double _dsinc; // sampling interval in table
  private float[][] _asinc; // array[nsinc][lsinc] of sinc approximations
  private double _nsincm1; // nsinc-1
  private int _ishift; // -lsinc-lsinc/2+1

  /**
   * Constructs a sinc interpolator with specified parameters.
   * Exactly one of the parameters must be zero, and is computed here.
   */
  private SincInterp(double emax,double fmax,int lmax) {
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
    _table = getTable(emax,fmax,lmax);
    _lsinc = _table.lsinc;
    _nsinc = _table.nsinc;
    _nsincm1 = _table.nsincm1;
    _ishift = _table.ishift;
    _dsinc = _table.dsinc;
    _asinc = _table.asinc;
  }

  /**
   * Design parameters.
   */
  private static class Design {
    double emax;
    double fmax; 
    int lmax;
    Design(double emax, double fmax, int lmax) {
      this.emax = emax;
      this.fmax = fmax;
      this.lmax = lmax;
    }
    public int hashCode() {
      long lemax = Double.doubleToLongBits(emax);
      long lfmax = Double.doubleToLongBits(fmax);
      return (int)(lemax^(lemax>>>32)) ^
             (int)(lfmax^(lfmax>>>32)) ^
             lmax;
    }
    public boolean equals(Object object) {
      Design that = (Design)object;
      return this.emax==that.emax &&
             this.fmax==that.fmax &&
             this.lmax==that.lmax;
    }
  }

  /**
   * Table of sinc interpolator coefficients.
   */
  private static class Table {
    Design design; // here, all three design parameters are non-zero
    int lsinc,nsinc,nsincm1,ishift;
    double dsinc;
    float[][] asinc;
  }

  /**
   * Builds a table of interpolators for specified design parameters.
   * Exactly one of the design parameters must be zero.
   */
  private static Table makeTable(Design design) {
    double emax = design.emax;
    double fmax = design.fmax;
    int lmax = design.lmax;

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
    double dsinc = (fmax>0.0)?etab/(PI*fmax):1.0;
    int nsincMin = 1+(int)ceil(1.0/dsinc);
    int nsinc = 2;
    while (nsinc<nsincMin)
      nsinc *= 2;
    ++nsinc;
    int lsinc = lmax;
    Table table = makeTable(nsinc,lsinc,kwin);
    table.design = new Design(emax,fmax,lmax);
    _tables.put(design,table); // key is design with one zero parameter
    return table;
  }

  /**
   * Builds a table of interpolators for a specified Kaiser window.
   */
  private static Table makeTable(int nsinc, int lsinc, KaiserWindow kwin) {
    float[][] asinc = new float[nsinc][lsinc];
    int nsincm1 = nsinc-1;
    int ishift = -lsinc-lsinc/2+1;
    double dsinc = 1.0/(nsinc-1);

    // The first and last interpolators are shifted unit impulses.
    // Handle these two cases exactly, with no rounding errors.
    for (int j=0; j<lsinc; ++j) {
      asinc[      0][j] = 0.0f;
      asinc[nsinc-1][j] = 0.0f;
    }
    asinc[      0][lsinc/2-1] = 1.0f;
    asinc[nsinc-1][lsinc/2  ] = 1.0f;

    // Other interpolators are sampled Kaiser-windowed sinc functions.
    for (int isinc=1; isinc<nsinc-1; ++isinc) {
      double x = -lsinc/2+1-dsinc*isinc;
      for (int i=0; i<lsinc; ++i,x+=1.0) {
        asinc[isinc][i] = (float)(sinc(x)*kwin.evaluate(x));
      }
    }
    Table table = new Table();
    table.lsinc = lsinc;
    table.nsinc = nsinc;
    table.nsincm1 = nsincm1;
    table.ishift = ishift;
    table.dsinc = dsinc;
    table.asinc = asinc;
    return table;
  }
  private static double sinc(double x) {
    return (x!=0.0)?sin(PI*x)/(PI*x):1.0;
  }

  /**
   * Map from design parameters to tables of coefficients.
   * This map saves both time and space required to compute the tables.
   */
  private final static HashMap<Design,Table> _tables = new HashMap<Design,Table>();
  private static Table getTable(double emax, double fmax, int lmax) {
    Design design = new Design(emax,fmax,lmax);
    synchronized(_tables) {
      Table table = _tables.get(design);
      if (table==null)
        table = makeTable(design);
      return table;
    }
  }

  private float interpolate(
    double xscale, double xshift, int nxum, int nxu, 
    float[] yu, double x)
  {
    // Which uniform samples?
    double xn = xshift+x*xscale;
    int ixn = (int)xn;
    int kyu = _ishift+ixn;

    // Which sinc approximation?
    double frac = xn-ixn;
    if (frac<0.0)
      frac += 1.0;
    int ksinc = (int)(frac*_nsincm1+0.5);
    float[] asinc = _asinc[ksinc];

    // If no extrapolation is necessary, use a fast loop.
      // Otherwise, extrapolate uniform samples, as necessary.
    float yr = 0.0f;
    if (kyu>=0 && kyu<=nxum) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu)
        yr += yu[kyu]*asinc[isinc];
    } else if (_extrap==Extrapolation.ZERO) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        if (0<=kyu && kyu<nxu)
          yr += yu[kyu]*asinc[isinc];
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        int jyu = (kyu<0)?0:(nxu<=kyu)?nxu-1:kyu;
        yr += yu[jyu]*asinc[isinc];
      }
    }
    return yr;
  }

  private void shift(
    int nxu, double dxu, double fxu, float[] yu,
    int nxi,             double fxi, float[] yi)
  {
    double lxu = fxu+(nxu-1)*dxu;
    double xscale = 1.0/dxu;
    double xshift = _lsinc-fxu*xscale;
    int nxum = nxu-_lsinc;

    // Which output samples are near beginning and end of uniform sequence?
    double dx = dxu;
    double x1 = fxu+dxu*_lsinc/2;
    double x2 = lxu-dxu*_lsinc/2;
    double x1n = (x1-fxi)/dx;
    double x2n = (x2-fxi)/dx;
    int ix1 = max(0,min(nxi,(int)x1n+1));
    int ix2 = max(0,min(nxi,(int)x2n-1));

    // Interpolate output samples near beginning of uniform sequence.
    for (int ixi=0; ixi<ix1; ++ixi) {
      double xi = fxi+ixi*dx;
      yi[ixi] = interpolate(xscale,xshift,nxum,nxu,yu,xi);
    }

    // Interpolate output samples near end of uniform sequence.
    for (int ixi=ix2; ixi<nxi; ++ixi) {
      double xi = fxi+ixi*dx;
      yi[ixi] = interpolate(xscale,xshift,nxum,nxu,yu,xi);
    }

    // Now we ignore the ends, and use a single sinc approximation.

    // Which uniform samples?
    double xn = xshift+(fxi+ix1*dx)*xscale;
    int ixn = (int)xn;
    int kyu = _ishift+ixn;

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
        yr += yu[jyu]*asinc[isinc];
      yi[ix] = yr;
    }
  }

  private void accumulate(
    double xscale, double xshift, int nxum,
    double x, float y, int nxu, float[] yu) 
  {
    // Which uniform samples?
    double xn = xshift+x*xscale;
    int ixn = (int)xn;
    int kyu = _ishift+ixn;

    // Which sinc approximation?
    double frac = xn-ixn;
    if (frac<0.0)
      frac += 1.0;
    int ksinc = (int)(frac*_nsincm1+0.5);
    float[] asinc = _asinc[ksinc];

    // If no extrapolation is necessary, use a fast loop.
      // Otherwise, extrapolate uniform samples, as necessary.
    if (kyu>=0 && kyu<=nxum) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu)
        yu[kyu] += y*asinc[isinc];
    } else if (_extrap==Extrapolation.ZERO) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        if (0<=kyu && kyu<nxu)
          yu[kyu] += y*asinc[isinc];
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        int jyu = (kyu<0)?0:(nxu<=kyu)?nxu-1:kyu;
        yu[jyu] += y*asinc[isinc];
      }
    }
  }

  private float interpolate(
    double x1scale, double x1shift, int nx1um, int nx1u,
    double x2scale, double x2shift, int nx2um, int nx2u,
    float[][] yu, double x1, double x2)
  {
    // Which uniform samples?
    double x1n = x1shift+x1*x1scale;
    double x2n = x2shift+x2*x2scale;
    int ix1n = (int)x1n;
    int ix2n = (int)x2n;
    int ky1u = _ishift+ix1n;
    int ky2u = _ishift+ix2n;

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
    if (ky1u>=0 && ky1u<=nx1um &&  ky2u>=0 &&  ky2u<=nx2um) {
      for (int i2sinc=0; i2sinc<_lsinc; ++i2sinc,++ky2u) {
        float asinc22 = asinc2[i2sinc];
        float[] yuk2 = yu[ky2u];
        float yr2 = 0.0f;
        for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u)
          yr2 += yuk2[my1u]*asinc1[i1sinc];
        yr += asinc22*yr2;
      }
    } else if (_extrap==Extrapolation.ZERO) {
      for (int i2sinc=0; i2sinc<_lsinc; ++i2sinc,++ky2u) {
        if (0<=ky2u && ky2u<nx2u) {
          for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u) {
            if (0<=my1u && my1u<nx1u)
              yr += yu[ky2u][my1u]*asinc2[i2sinc]*asinc1[i1sinc];
          }
        }
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int i2sinc=0; i2sinc<_lsinc; ++i2sinc,++ky2u) {
        int jy2u = (ky2u<0)?0:(nx2u<=ky2u)?nx2u-2:ky2u;
        for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u) {
          int jy1u = (my1u<0)?0:(nx1u<=my1u)?nx1u-1:my1u;
          yr += yu[jy2u][jy1u]*asinc2[i2sinc]*asinc1[i1sinc];
        }
      }
    }
    return yr;
  }

  private float interpolate(
    double x1scale, double x1shift, int nx1um, int nx1u,
    double x2scale, double x2shift, int nx2um, int nx2u,
    double x3scale, double x3shift, int nx3um, int nx3u,
    float[][][] yu, double x1, double x2, double x3)
  {
    // Which uniform samples?
    double x1n = x1shift+x1*x1scale;
    double x2n = x2shift+x2*x2scale;
    double x3n = x3shift+x3*x3scale;
    int ix1n = (int)x1n;
    int ix2n = (int)x2n;
    int ix3n = (int)x3n;
    int ky1u = _ishift+ix1n;
    int ky2u = _ishift+ix2n;
    int ky3u = _ishift+ix3n;

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
    if (ky1u>=0 && ky1u<=nx1um &&  
        ky2u>=0 && ky2u<=nx2um &&  
        ky3u>=0 && ky3u<=nx3um) {
      for (int i3sinc=0; i3sinc<_lsinc; ++i3sinc,++ky3u) {
        float asinc33 = asinc3[i3sinc];
        float[][] yu3 = yu[ky3u];
        float yr2 = 0.0f;
        for (int i2sinc=0,my2u=ky2u; i2sinc<_lsinc; ++i2sinc,++my2u) {
          float asinc22 = asinc2[i2sinc];
          float[] yu32 = yu3[my2u];
          float yr1 = 0.0f;
          for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u)
            yr1 += yu32[my1u]*asinc1[i1sinc];
          yr2 += asinc22*yr1;
        }
        yr += asinc33*yr2;
      }
    } else if (_extrap==Extrapolation.ZERO) {
      for (int i3sinc=0; i3sinc<_lsinc; ++i3sinc,++ky3u) {
        if (0<=ky3u && ky3u<nx3u) {
          for (int i2sinc=0,my2u=ky2u; i2sinc<_lsinc; ++i2sinc,++my2u) {
            if (0<=my2u && my2u<nx2u) {
              for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u) {
                if (0<=my1u && my1u<nx1u)
                  yr += yu[ky3u][my2u][my1u] *
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
        int jy3u = (ky3u<0)?0:(nx3u<=ky3u)?nx3u-2:ky3u;
        for (int i2sinc=0,my2u=ky2u; i2sinc<_lsinc; ++i2sinc,++my2u) {
          int jy2u = (my2u<0)?0:(nx2u<=my2u)?nx2u-2:my2u;
          for (int i1sinc=0,my1u=ky1u; i1sinc<_lsinc; ++i1sinc,++my1u) {
            int jy1u = (my1u<0)?0:(nx1u<=my1u)?nx1u-1:my1u;
            yr += yu[jy3u][jy2u][jy1u] *
                  asinc3[i3sinc] *
                  asinc2[i2sinc] *
                  asinc1[i1sinc];
          }
        }
      }
    }
    return yr;
  }

  private void interpolateComplex(
    double xscale, double xshift, int nxum, int nxu, 
    float[] yu, int ix, double x, float[] y) 
  {
    // Which uniform samples?
    double xn = xshift+x*xscale;
    int ixn = (int)xn;
    int kyu = _ishift+ixn;

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
    if (kyu>=0 && kyu<=nxum) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        int jyu = 2*kyu;
        float asinci = asinc[isinc];
        yr += yu[jyu  ]*asinci;
        yi += yu[jyu+1]*asinci;
      }
    } else if (_extrap==Extrapolation.ZERO) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        if (0<=kyu && kyu<nxu) {
          int jyu = 2*kyu;
          float asinci = asinc[isinc];
          yr += yu[jyu  ]*asinci;
          yi += yu[jyu+1]*asinci;
        }
      }
    } else if (_extrap==Extrapolation.CONSTANT) {
      for (int isinc=0; isinc<_lsinc; ++isinc,++kyu) {
        int jyu = (kyu<0)?0:(nxu<=kyu)?2*nxu-2:2*kyu;
        float asinci = asinc[isinc];
        yr += yu[jyu  ]*asinci;
        yi += yu[jyu+1]*asinci;
      }
    }
    int jx = 2*ix;
    y[jx  ] = yr;
    y[jx+1] = yi;
  }
}
