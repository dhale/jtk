/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * A linear shift-invariant filter implemented by fast Fourier transform.
 * This filtering is equivalent to computing the convolution y = h*x,
 * where h, x and y are filter, input and output arrays, respectively.
 * <p>
 * The filter is specified as a 1D, 2D or 3D array of coefficients.
 * Filter dimension must match that of arrays to be filtered. For 
 * example, a filter constructed with a 2D array of coefficients
 * cannot be applied to a 1D array.
 * <p>
 * The linear shift-invariant filtering performed by this class is a 
 * convolution sum. Each output sample in y is a sum of scaled input 
 * samples in x. For 1D filters this sum is
 * <pre><code>
 *         nh-1-kh
 *   y[i] =  sum  h[kh+j]*x[i-j] ; i = 0, 1, 2, ..., ny-1 = nx-1
 *          j=-kh
 * </code></pre>
 * For each output sample y[i], kh is the array index of the filter 
 * coefficient h[kh] that scales the corresponding input sample x[i]. 
 * For example, in a symmetric filter with odd length nh, the index 
 * kh = (nh-1)/2 is that of the middle coefficient in the array h.
 * In other words, kh is the array index of the filter's origin.
 * <p>
 * The lengths nx and ny of the input and output arrays x and y are 
 * assumed to be equal. By default, values beyond the ends of an input 
 * array x in the convolution sum above are assumed to be zero. That
 * is, zero values are used for x[i-j] when i-j &lt; 0 or when 
 * i-j &gt;= nx. Other methods for defining values beyond the ends of 
 * the array x may be specified. With any of these methods, the input 
 * array x is padded with extra values so that x[i-j] is defined for 
 * any i-j in the range [kh-nh+1:kh+nx-1] required by the convolution sum.
 * <p>
 * For efficiency, this filter can cache the fast Fourier transform of 
 * its coefficients h when the filter is first applied to any input 
 * array x. The filter may then be applied again, without recomputing 
 * its FFT, to other input arrays x that have the same lengths. The FFT 
 * of a cached filter is recomputed only when the lengths of the input 
 * and output arrays have changed. Because this caching consumes memory,
 * it is disabled by default.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.12.14
 */
public class FftFilter {

  /**
   * The method used to extrapolate values beyond the ends of input arrays.
   * The default is extrapolation with zero values.
   */
  public enum Extrapolation {
    /**
     * Extrapolate with zero values.
     */
    ZERO_VALUE,
    /**
     * Extrapolate values at the ends with zero slope.
     */
    ZERO_SLOPE
  }

  /**
   * Constructs an FFT filter for specified filter coefficients.
   * The filter's origin is the center of the array.
   * @param h array of filter coefficients; copied, not referenced.
   */
  public FftFilter(float[] h) {
    this((h.length-1)/2,h);
  }

  /**
   * Constructs an FFT filter for specified filter coefficients.
   * The coefficient h[kh] corresponds to the filter's origin.
   * @param kh array index of the filter's origin.
   * @param h array of filter coefficients; copied, not referenced.
   */
  public FftFilter(int kh, float[] h) {
    Check.argument(0<=kh && kh<h.length,"index kh is valid");
    _nh1 = h.length;
    _kh1 = kh;
    _h1 = copy(h);
  }

  /**
   * Constructs an FFT filter for specified filter coefficients.
   * The filter's origin is the center of the array.
   * @param h array of filter coefficients; copied, not referenced.
   */
  public FftFilter(float[][] h) {
    this((h[0].length-1)/2,(h.length-1)/2,h);
  }

  /**
   * Constructs an FFT filter for specified filter coefficients.
   * The coefficient h[kh2][kh1] corresponds to the filter's origin.
   * @param kh1 array index in 1st dimension of the filter's origin.
   * @param kh2 array index in 2nd dimension of the filter's origin.
   * @param h array of filter coefficients; copied, not referenced.
   */
  public FftFilter(int kh1, int kh2, float[][] h) {
    Check.argument(0<=kh1 && kh1<h[0].length,"index kh1 is valid");
    Check.argument(0<=kh2 && kh2<h.length,"index kh2 is valid");
    _nh1 = h[0].length;
    _nh2 = h.length;
    _kh1 = kh1;
    _kh2 = kh2;
    _h2 = copy(h);
  }

  /**
   * Constructs an FFT filter for specified filter coefficients.
   * The filter's origin is the center of the array.
   * @param h array of filter coefficients; copied, not referenced.
   */
  public FftFilter(float[][][] h) {
    this((h[0][0].length-1)/2,(h[0].length-1)/2,(h.length-1)/2,h);
  }

  /**
   * Constructs an FFT filter for specified filter coefficients.
   * The coefficient h[kh3][kh2][kh1] corresponds to the filter's origin.
   * @param kh1 array index in 1st dimension of the filter's origin.
   * @param kh2 array index in 2nd dimension of the filter's origin.
   * @param kh3 array index in 3rd dimension of the filter's origin.
   * @param h array of filter coefficients; copied, not referenced.
   */
  public FftFilter(int kh1, int kh2, int kh3, float[][][] h) {
    Check.argument(0<=kh1 && kh1<h[0][0].length,"index kh1 is valid");
    Check.argument(0<=kh2 && kh2<h[0].length,"index kh2 is valid");
    Check.argument(0<=kh3 && kh3<h.length,"index kh3 is valid");
    _nh1 = h[0][0].length;
    _nh2 = h[0].length;
    _nh3 = h.length;
    _kh1 = kh1;
    _kh2 = kh2;
    _kh3 = kh3;
    _h3 = copy(h);
  }

  /**
   * Sets the method used to extrapolate values beyond the ends of input arrays.
   * @param extrapolation the extrapolation method.
   */
  public void setExtrapolation(Extrapolation extrapolation) {
    _extrapolation = extrapolation;
  }

  /**
   * Enables or disables caching of the Fourier transform of the filter.
   * Caching consumes memory but improves performance by about 50% when
   * the same filter is applied repeatedly to arrays that have the same
   * dimensions.
   * @param filterCaching true, to enable caching; false, to disable.
   */
  public void setFilterCaching(boolean filterCaching) {
    _filterCaching = filterCaching;
  }

  /**
   * Applies this filter.
   * @param x input array.
   * @return filtered array.
   */
  public float[] apply(float[] x) {
    float[] y = new float[x.length];
    apply(x,y);
    return y;
  }

  /**
   * Applies this filter.
   * Input and output arrays may be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[] x, float[] y) {
    Check.state(_h1!=null,"1D filter is available");
    int nx1 = x.length;
    updateFfts(nx1);
    float[] xfft = new float[_nfft1+2];
    copy(nx1,x,xfft);
    extrapolate(xfft);
    _fft1.realToComplex(-1,xfft,xfft);
    int nk1 = _nfft1/2+1;
    for (int ik1=0,k1r=0,k1i=1; ik1<nk1; ++ik1,k1r+=2,k1i+=2) {
      float xr = xfft[k1r];
      float xi = xfft[k1i];
      float hr = _h1fft[k1r];
      float hi = _h1fft[k1i];
      xfft[k1r] = xr*hr-xi*hi;
      xfft[k1i] = xr*hi+xi*hr;
    }
    if (!_filterCaching) _h1fft = null;
    _fft1.complexToReal(1,xfft,xfft);
    copy(nx1,xfft,y);
  }

  /**
   * Applies this filter.
   * @param x input array.
   * @return filtered array.
   */
  public float[][] apply(float[][] x) {
    float[][] y = new float[x.length][x[0].length];
    apply(x,y);
    return y;
  }

  /**
   * Applies this filter.
   * Input and output arrays may be the same array.
   * @param x input array.
   * @param y output filtered array.
   */
  public void apply(float[][] x, float[][] y) {
    Check.state(_h2!=null,"2D filter is valid");
    int nx1 = x[0].length;
    int nx2 = x.length;
    updateFfts(nx1,nx2);
    float[][] xfft = new float[_nfft2][_nfft1+2];
    copy(nx1,nx2,x,xfft);
    extrapolate(xfft);
    _fft1.realToComplex1(-1,_nfft2,xfft,xfft);
    _fft2.complexToComplex2(-1,_nfft1/2+1,xfft,xfft);
    int nk1 = _nfft1/2+1;
    int nk2 = _nfft2;
    for (int ik2=0; ik2<nk2; ++ik2) {
      float[] x2 = xfft[ik2];
      float[] h2 = _h2fft[ik2];
      for (int ik1=0,k1r=0,k1i=1; ik1<nk1; ++ik1,k1r+=2,k1i+=2) {
        float xr = x2[k1r];
        float xi = x2[k1i];
        float hr = h2[k1r];
        float hi = h2[k1i];
        x2[k1r] = xr*hr-xi*hi;
        x2[k1i] = xr*hi+xi*hr;
      }
    }
    if (!_filterCaching) _h2fft = null;
    _fft2.complexToComplex2(1,_nfft1/2+1,xfft,xfft);
    _fft1.complexToReal1(1,_nfft2,xfft,xfft);
    copy(nx1,nx2,xfft,y);
  }

  /**
   * Applies this filter.
   * @param x input array.
   * @return filtered array.
   */
  public float[][][] apply(float[][][] x) {
    float[][][] y = new float[x.length][x[0].length][x[0][0].length];
    apply(x,y);
    return y;
  }

  /**
   * Applies this filter.
   * Input and output arrays may be the same array.
   * @param x input array.
   * @param y output filtered array.
   */
  public void apply(float[][][] x, float[][][] y) {
    Check.state(_h3!=null,"3D filter is valid");
    int nx1 = x[0][0].length;
    int nx2 = x[0].length;
    int nx3 = x.length;
    updateFfts(nx1,nx2,nx3);
    float[][][] xfft = new float[_nfft3][_nfft2][_nfft1+2];
    copy(nx1,nx2,nx3,x,xfft);
    extrapolate(xfft);
    _fft1.realToComplex1(-1,_nfft2,_nfft3,xfft,xfft);
    _fft2.complexToComplex2(-1,_nfft1/2+1,_nfft3,xfft,xfft);
    _fft3.complexToComplex3(-1,_nfft1/2+1,_nfft2,xfft,xfft);
    int nk1 = _nfft1/2+1;
    int nk2 = _nfft2;
    int nk3 = _nfft3;
    for (int ik3=0; ik3<nk3; ++ik3) {
      for (int ik2=0; ik2<nk2; ++ik2) {
        float[] x32 = xfft[ik3][ik2];
        float[] h32 = _h3fft[ik3][ik2];
        for (int ik1=0,k1r=0,k1i=1; ik1<nk1; ++ik1,k1r+=2,k1i+=2) {
          float xr = x32[k1r];
          float xi = x32[k1i];
          float hr = h32[k1r];
          float hi = h32[k1i];
          x32[k1r] = xr*hr-xi*hi;
          x32[k1i] = xr*hi+xi*hr;
        }
      }
    }
    if (!_filterCaching) _h3fft = null;
    _fft3.complexToComplex3(1,_nfft1/2+1,_nfft2,xfft,xfft);
    _fft2.complexToComplex2(1,_nfft1/2+1,_nfft3,xfft,xfft);
    _fft1.complexToReal1(1,_nfft2,_nfft3,xfft,xfft);
    copy(nx1,nx2,nx3,xfft,y);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nx1,_nx2,_nx3;
  private int _nh1,_nh2,_nh3;
  private int _kh1,_kh2,_kh3;
  private int _nfft1,_nfft2,_nfft3;
  private FftReal _fft1;
  private FftComplex _fft2,_fft3;
  private float[] _h1,_h1fft;
  private float[][] _h2,_h2fft;
  private float[][][] _h3,_h3fft;
  private Extrapolation _extrapolation = Extrapolation.ZERO_VALUE;
  private boolean _filterCaching;

  private void updateFfts(int nx1) {
    if (_fft1==null || _h1fft==null || _nx1!=nx1) {
      _nx1 = nx1;
      _nx2 = 0;
      _nx3 = 0;
      _nfft1 = FftReal.nfftFast(_nx1+_nh1);
      _fft1 = new FftReal(_nfft1);
      _fft2 = null;
      _fft3 = null;
      _h1fft = new float[_nfft1+2];
      _h2fft = null;
      _h3fft = null;
      float scale = 1.0f/(float)_nfft1;
      for (int ih1=0; ih1<_nh1; ++ih1) {
        int jh1 = ih1-_kh1;
        if (jh1<0) jh1 += _nfft1;
        _h1fft[jh1] = scale*_h1[ih1];
      }
      _fft1.realToComplex(-1,_h1fft,_h1fft);
    }
  }

  private void updateFfts(int nx1, int nx2) {
    if (_fft2==null || _h2fft==null || _nx1!=nx1 || _nx2!=nx2) {
      _nx1 = nx1;
      _nx2 = nx2;
      _nx3 = 0;
      _nfft1 = FftReal.nfftFast(_nx1+_nh1);
      _nfft2 = FftComplex.nfftFast(_nx2+_nh2);
      _fft1 = new FftReal(_nfft1);
      _fft2 = new FftComplex(_nfft2);
      _fft3 = null;
      _h1fft = null;
      _h2fft = new float[_nfft2][_nfft1+2];
      _h3fft = null;
      float scale = 1.0f/(float)_nfft1/(float)_nfft2;
      for (int ih2=0; ih2<_nh2; ++ih2) {
        int jh2 = ih2-_kh2;
        if (jh2<0) jh2 += _nfft2;
        for (int ih1=0; ih1<_nh1; ++ih1) {
          int jh1 = ih1-_kh1;
          if (jh1<0) jh1 += _nfft1;
          _h2fft[jh2][jh1] = scale*_h2[ih2][ih1];
        }
      }
      _fft1.realToComplex1(-1,_nfft2,_h2fft,_h2fft);
      _fft2.complexToComplex2(-1,_nfft1/2+1,_h2fft,_h2fft);
    }
  }

  private void updateFfts(int nx1, int nx2, int nx3) {
    if (_fft3==null || _h3fft==null || _nx1!=nx1 || _nx2!=nx2 || _nx3!=nx3) {
      _nx1 = nx1;
      _nx2 = nx2;
      _nx3 = nx3;
      _nfft1 = FftReal.nfftFast(_nx1+_nh1);
      _nfft2 = FftComplex.nfftFast(_nx2+_nh2);
      _nfft3 = FftComplex.nfftFast(_nx3+_nh3);
      _fft1 = new FftReal(_nfft1);
      _fft2 = new FftComplex(_nfft2);
      _fft3 = new FftComplex(_nfft3);
      _h1fft = null;
      _h2fft = null;
      _h3fft = new float[_nfft3][_nfft2][_nfft1+2];
      float scale = 1.0f/(float)_nfft1/(float)_nfft2/(float)_nfft3;
      for (int ih3=0; ih3<_nh3; ++ih3) {
        int jh3 = ih3-_kh3;
        if (jh3<0) jh3 += _nfft3;
        for (int ih2=0; ih2<_nh2; ++ih2) {
          int jh2 = ih2-_kh2;
          if (jh2<0) jh2 += _nfft2;
          for (int ih1=0; ih1<_nh1; ++ih1) {
            int jh1 = ih1-_kh1;
            if (jh1<0) jh1 += _nfft1;
            _h3fft[jh3][jh2][jh1] = scale*_h3[ih3][ih2][ih1];
          }
        }
      }
      _fft1.realToComplex1(-1,_nfft2,_nfft3,_h3fft,_h3fft);
      _fft2.complexToComplex2(-1,_nfft1/2+1,_nfft3,_h3fft,_h3fft);
      _fft3.complexToComplex3(-1,_nfft1/2+1,_nfft2,_h3fft,_h3fft);
    }
  }

  private void extrapolate(float[] xfft) {
    if (_extrapolation==Extrapolation.ZERO_SLOPE) {
      int mr1 = _nx1+_kh1;
      float xr1 = xfft[_nx1-1];
      for (int i1=_nx1; i1<mr1; ++i1)
        xfft[i1] = xr1;
      int ml1 = _nfft1+_kh1-_nh1+1;
      float xl1 = xfft[0];
      for (int i1=ml1; i1<_nfft1; ++i1)
        xfft[i1] = xl1;
    }
  }

  private void extrapolate(float[][] xfft) {
    if (_extrapolation==Extrapolation.ZERO_SLOPE) {
      for (int i2=0; i2<_nx2; ++i2)
        extrapolate(xfft[i2]);
      int mr2 = _nx2+_kh2;
      float[] xr2 = xfft[_nx2-1];
      for (int i2=_nx2; i2<mr2; ++i2)
        copy(xr2,xfft[i2]);
      int ml2 = _nfft2+_kh2-_nh2+1;
      float[] xl2 = xfft[0];
      for (int i2=ml2; i2<_nfft2; ++i2)
        copy(xl2,xfft[i2]);
    }
  }

  private void extrapolate(float[][][] xfft) {
    if (_extrapolation==Extrapolation.ZERO_SLOPE) {
      for (int i3=0; i3<_nx3; ++i3)
        extrapolate(xfft[i3]);
      int mr3 = _nx3+_kh3;
      float[][] xr3 = xfft[_nx3-1];
      for (int i3=_nx3; i3<mr3; ++i3)
        copy(xr3,xfft[i3]);
      int ml3 = _nfft3+_kh3-_nh3+1;
      float[][] xl3 = xfft[0];
      for (int i3=ml3; i3<_nfft3; ++i3)
        copy(xl3,xfft[i3]);
    }
  }
}
