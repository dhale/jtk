/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
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

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * An easy to use fast Fourier transform.
 * This class is less flexible than {@link FftComplex} and {@link FftReal}.
 * For example, the user has less control over the sampling of frequency.
 * However, for many applications this class may be simpler to use.
 * <p>
 * For example, the following program shows how to use this class to
 * filter a real-valued sequence in the frequency domain.
 * <pre><code>
 *   Fft fft = new Fft(nx); // nx = number of samples of f(x)
 *   Sampling sk = fft.getFrequencySampling1();
 *   int nk = sk.getCount(); // number of frequencies sampled
 *   float[] f = ... // nx real samples of input f(x)
 *   float[] g = fft.applyForward(f); // nk complex samples of g(k)
 *   for (int kk=0,kr=0,ki=kr+1; kk&lt;nk; ++kk,kr+=2,ki+=2) {
 *     double k = sk.getValue(kk); // frequency k in cycles/sample
 *     // modify g[kr], the real part of g(k)
 *     // modify g[ki], the imag part of g(k)
 *   }
 *   float[] h = fft.applyInverse(g); // nx real samples of output h(x)
 * </code></pre>
 * This example is almost as simple for multi-dimensional transforms.
 * <p>
 * A forward transform computes an output array of complex values g(k) 
 * from an input array of real or complex values f(x). An inverse 
 * transform computes the corresponding real or complex values f(x) 
 * from g(k). For definiteness, in this documentation, the variable x 
 * represents spatial coordinates and the variable k represents spatial 
 * frequencies (or wavenumbers). For functions of time, simply replace 
 * the word "space" with "time" in this documentation.
 * <p>
 * This class enables transforms of 1D, 2D, and 3D arrays. For example,
 * a 2D array f[nx2][nx1] represents nx2*nx1 samples of a function
 * f(x1,x2) of two spatial coordinates x1 and x2. In addition to
 * numbers of samples nx1 and nx2, sampling intervals dx1 and dx2
 * and first sampled coordinates fx1 and fx2 may also be specified.
 * (The default sampling interval is 1.0 and the default first sample
 * coordinate is 0.0.) These sampling parameters may be specified with
 * samplings sx1 and sx2.
 * <p>
 * For each specified spatial sampling sx, this class defines a 
 * corresponding frequency sampling sk, in which units of frequency
 * are cycles per unit distance. The number of frequencies sampled 
 * is computed so that the Fourier transform is fast, but the number 
 * of frequency samples is never less than the number of space samples. 
 * Arrays to be transformed may be padded with zeros to obtain the 
 * required frequency sampling. Optional additional padding may be
 * specified to sample frequency more finely.
 * <p>
 * A frequency sampling sk may be centered. Such a centered frequency
 * sampling always has an odd number of samples, and zero frequency
 * corresponds to the middle sample in the array of complex transformed 
 * values. The default is not centered, so that zero frequency corresponds 
 * to the first sample, the one with index 0.
 * <p>
 * Arrays input to forward transforms may contain either real or
 * complex values. If complex, values are packed sequentially as 
 * (real,imaginary) pairs of consecutive floats. The default input 
 * type is real.
 * <p>
 * Signs of the exponents in the complex exponentials used in forward 
 * transforms may be specified. The opposite signs are used for inverse 
 * transforms. The default signs are -1 for forward transforms and 1 for 
 * inverse transforms.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.12.04
 */
public class Fft {

  /** 
   * Constructs an FFT for the specified 1D array of real values.
   * Spatial dimensions are determined from the dimensions of the
   * specified array. Spatial sampling intervals are 1.0, and first 
   * sample coordinates are 0.0.
   * @param f an array with dimensions like those to be transformed.
   */
  public Fft(float[] f) {
    this(f.length);
  }

  /** 
   * Constructs an FFT for the specified 2D array of real values.
   * Spatial dimensions are determined from the dimensions of the
   * specified array. Spatial sampling intervals are 1.0, and first 
   * sample coordinates are 0.0.
   * @param f an array with dimensions like those to be transformed.
   */
  public Fft(float[][] f) {
    this(f[0].length,f.length);
  }

  /** 
   * Constructs an FFT for the specified 3D array of real values.
   * Spatial dimensions are determined from the dimensions of the
   * specified array. Spatial sampling intervals are 1.0, and first 
   * sample coordinates are 0.0.
   * @param f an array with dimensions like those to be transformed.
   */
  public Fft(float[][][] f) {
    this(f[0][0].length,f[0].length,f.length);
  }

  /** 
   * Constructs an FFT for the specified 1D array of values.
   * Spatial dimensions are determined from the dimensions of the
   * specified array. Spatial sampling intervals are 1.0, and first 
   * sample coordinates are 0.0.
   * @param complex true, for complex values; false, for real values.
   * @param f an array with dimensions like those to be transformed.
   */
  public Fft(boolean complex, float[] f) {
    this(f.length/2);
    setComplex(complex);
  }

  /** 
   * Constructs an FFT for the specified 2D array of values.
   * Spatial dimensions are determined from the dimensions of the
   * specified array. Spatial sampling intervals are 1.0, and first 
   * sample coordinates are 0.0.
   * @param complex true, for complex values; false, for real values.
   * @param f an array with dimensions like those to be transformed.
   */
  public Fft(boolean complex, float[][] f) {
    this(f[0].length/2,f.length);
    setComplex(complex);
  }

  /** 
   * Constructs an FFT for the specified 3D array of values.
   * Spatial dimensions are determined from the dimensions of the
   * specified array. Spatial sampling intervals are 1.0, and first 
   * sample coordinates are 0.0.
   * @param complex true, for complex values; false, for real values.
   * @param f an array with dimensions like those to be transformed.
   */
  public Fft(boolean complex, float[][][] f) {
    this(f[0][0].length/2,f[0].length,f.length);
    setComplex(complex);
  }

  /** 
   * Constructs an FFT with specified number of space samples.
   * The sampling interval is 1.0 and the first sample coordinate is 0.0.
   * @param nx1 number of samples in the 1st dimension.
   */
  public Fft(int nx1) {
    this(new Sampling(nx1,1.0,0.0));
  }

  /** 
   * Constructs an FFT with specified numbers of space samples.
   * Sampling intervals are 1.0 and first sample coordinates are 0.0.
   * @param nx1 number of samples in the 1st dimension.
   * @param nx2 number of samples in the 2nd dimension.
   */
  public Fft(int nx1, int nx2) {
    this(new Sampling(nx1,1.0,0.0),
         new Sampling(nx2,1.0,0.0));
  }

  /** 
   * Constructs an FFT with specified numbers of space samples.
   * Sampling intervals are 1.0 and first sample coordinates are 0.0.
   * @param nx1 number of samples in the 1st dimension.
   * @param nx2 number of samples in the 2nd dimension.
   * @param nx3 number of samples in the 3rd dimension.
   */
  public Fft(int nx1, int nx2, int nx3) {
    this(new Sampling(nx1,1.0,0.0),
         new Sampling(nx2,1.0,0.0),
         new Sampling(nx3,1.0,0.0));
  }

  /** 
   * Constructs an FFT with specified space sampling.
   * @param sx1 space sampling for the 1st dimension.
   */
  public Fft(Sampling sx1) {
    this(sx1,null,null);
  }

  /** 
   * Constructs an FFT with specified space sampling.
   * @param sx1 space sampling for the 1st dimension.
   * @param sx2 space sampling for the 2nd dimension.
   */
  public Fft(Sampling sx1, Sampling sx2) {
    this(sx1,sx2,null);
  }

  /** 
   * Constructs an FFT with specified space sampling.
   * @param sx1 space sampling for the 1st dimension.
   * @param sx2 space sampling for the 2nd dimension.
   * @param sx3 space sampling for the 3rd dimension.
   */
  public Fft(Sampling sx1, Sampling sx2, Sampling sx3) {
    _sx1 = sx1;
    _sx2 = sx2;
    _sx3 = sx3;
    _sign1 = -1;
    _sign2 = -1;
    _sign3 = -1;
    updateSampling1();
    updateSampling2();
    updateSampling3();
  }

  /**
   * Sets the type of input (output) values for forward (inverse) transforms.
   * The default type is real.
   * @param complex true, for complex values; false, for real values.
   */
  public void setComplex(boolean complex) {
    if (_complex!=complex) {
      _complex = complex;
      updateSampling1();
    }
  }

  /**
   * Sets the ability of this transform to overwrite specified arrays.
   * The array specified in an inverse transform is either copied or 
   * overwritten internally by the inverse transform. Copying preserves 
   * the values in the specified array, but wastes memory in the case 
   * when those values are no longer needed. If overwrite is true, then
   * the inverse transform will be performed in place, so that no copy 
   * is necessary. The default is false.
   * @param overwrite true, to overwrite; false, to copy.
   */
  public void setOverwrite(boolean overwrite) {
    _overwrite = overwrite;
  }

  /**
   * Gets the frequency sampling for the 1st dimension.
   * @return the frequency sampling.
   */
  public Sampling getFrequencySampling1() {
    return _sk1;
  }

  /**
   * Gets the frequency sampling for the 2nd dimension.
   * @return the frequency sampling.
   */
  public Sampling getFrequencySampling2() {
    return _sk2;
  }

  /**
   * Gets the frequency sampling for the 3rd dimension.
   * @return the frequency sampling.
   */
  public Sampling getFrequencySampling3() {
    return _sk3;
  }

  /**
   * Sets the sign used for forward transforms in all dimensions.
   * The opposite sign is used for inverse transforms.
   * The default sign is -1.
   * @param sign the sign, -1 or 1. 
   */
  public void setSign(int sign) {
    setSign1(sign);
    setSign2(sign);
    setSign3(sign);
  }

  /**
   * Sets the sign used for forward transforms in the 1st dimension.
   * The opposite sign is used for inverse transforms.
   * The default sign is -1.
   * @param sign the sign, -1 or 1. 
   */
  public void setSign1(int sign) {
    _sign1 = (sign>=0)?1:-1;
  }

  /**
   * Sets the sign used for forward transforms in the 2nd dimension.
   * The opposite sign is used for inverse transforms.
   * The default sign is -1.
   * @param sign the sign, -1 or 1. 
   */
  public void setSign2(int sign) {
    _sign2 = (sign>=0)?1:-1;
  }

  /**
   * Sets the sign used for forward transforms in the 3rd dimension.
   * The opposite sign is used for inverse transforms.
   * The default sign is -1.
   * @param sign the sign, -1 or 1. 
   */
  public void setSign3(int sign) {
    _sign3 = (sign>=0)?1:-1;
  }

  /**
   * Sets the centering of frequency samplings for all dimensions.
   * If centered, the number of frequency samples is always odd,
   * and zero frequency corresponds to the middle sample. The 
   * default center is false, so that zero frequency corresponds to 
   * the sample with index zero in the output transformed array.
   * @param center true, for centering; false, otherwise.
   */
  public void setCenter(boolean center) {
    setCenter1(center);
    setCenter2(center);
    setCenter3(center);
  }

  /**
   * Sets the centering of frequency sampling for the 1st dimension.
   * If centered, the number of frequency samples is always odd,
   * and zero frequency corresponds to the middle sample. The 
   * default center is false, so that zero frequency corresponds to 
   * the sample with index zero in the output transformed array.
   * @param center true, for centering; false, otherwise.
   */
  public void setCenter1(boolean center) {
    if (_center1!=center) {
      _center1 = center;
      updateSampling1();
    }
  }

  /**
   * Sets the centering of frequency sampling for the 2nd dimension.
   * If centered, the number of frequency samples is always odd,
   * and zero frequency corresponds to the middle sample. The 
   * default center is false, so that zero frequency corresponds to 
   * the sample with index zero in the output transformed array.
   * @param center true, for centering; false, otherwise.
   */
  public void setCenter2(boolean center) {
    if (_center2!=center) {
      _center2 = center;
      updateSampling2();
    }
  }

  /**
   * Sets the centering of frequency sampling for the 3rd dimension.
   * If centered, the number of frequency samples is always odd,
   * and zero frequency corresponds to the middle sample. The 
   * default center is false, so that zero frequency corresponds to 
   * the sample with index zero in the output transformed array.
   * @param center true, for centering; false, otherwise.
   */
  public void setCenter3(boolean center) {
    if (_center3!=center) {
      _center3 = center;
      updateSampling3();
    }
  }

  /**
   * Sets the minimum padding with zeros for all array dimensions.
   * The default minimum is zero. However, some amount of padding 
   * may be required by the FFT.
   * @param padding the minimum padding.
   */
  public void setPadding(int padding) {
    setPadding1(padding);
    setPadding2(padding);
    setPadding3(padding);
  }

  /**
   * Sets the minimum padding with zeros for the 1st array dimension.
   * The default minimum is zero. However, some amount of padding may 
   * be required by the FFT.
   * @param padding the minimum padding.
   */
  public void setPadding1(int padding) {
    if (_padding1!=padding) {
      _padding1 = padding;
      updateSampling1();
    }
  }

  /**
   * Sets the minimum padding with zeros for the 2nd array dimension.
   * The default minimum is zero. However, some amount of padding may 
   * be required by the FFT.
   * @param padding the minimum padding.
   */
  public void setPadding2(int padding) {
    if (_padding2!=padding) {
      _padding2 = padding;
      updateSampling2();
    }
  }

  /**
   * Sets the minimum padding with zeros for the 3rd array dimension.
   * The default minimum is zero. However, some amount of padding may 
   * be required by the FFT.
   * @param padding the minimum padding.
   */
  public void setPadding3(int padding) {
    if (_padding3!=padding) {
      _padding3 = padding;
      updateSampling3();
    }
  }

  /**
   * Applies a forward space-to-frequency transform of a 1D array.
   * @param f the array to be transformed, a sampled function of space.
   * @return the transformed array, a sampled function of frequency.
   */
  public float[] applyForward(float[] f) {
    ensureSamplingX1(f);
    float[] fpad = pad(f);
    if (_complex) {
      _fft1c.complexToComplex(_sign1,fpad,fpad);
    } else {
      _fft1r.realToComplex(_sign1,fpad,fpad);
    }
    phase(fpad);
    center(fpad);
    return fpad;
  }

  /**
   * Applies a forward space-to-frequency transform of a 2D array.
   * @param f the array to be transformed, a sampled function of space.
   * @return the transformed array, a sampled function of frequency.
   */
  public float[][] applyForward(float[][] f) {
    ensureSamplingX2(f);
    float[][] fpad = pad(f);
    int nx2 = _sx2.getCount();
    if (_complex) {
      _fft1c.complexToComplex1(_sign1,nx2,fpad,fpad);
      _fft2.complexToComplex2(_sign2,_nfft1,fpad,fpad);
    } else {
      _fft1r.realToComplex1(_sign1,nx2,fpad,fpad);
      _fft2.complexToComplex2(_sign2,_nfft1/2+1,fpad,fpad);
    }
    phase(fpad);
    center(fpad);
    return fpad;
  }

  /**
   * Applies a forward space-to-frequency transform of a 3D array.
   * @param f the array to be transformed, a sampled function of space.
   * @return the transformed array, a sampled function of frequency.
   */
  public float[][][] applyForward(float[][][] f) {
    ensureSamplingX3(f);
    float[][][] fpad = pad(f);
    int nx2 = _sx2.getCount();
    int nx3 = _sx3.getCount();
    if (_complex) {
      _fft1c.complexToComplex1(_sign1,nx2,nx3,fpad,fpad);
      _fft2.complexToComplex2(_sign2,_nfft1,nx3,fpad,fpad);
      _fft3.complexToComplex3(_sign3,_nfft1,_nfft2,fpad,fpad);
    } else {
      _fft1r.realToComplex1(_sign1,nx2,nx3,fpad,fpad);
      _fft2.complexToComplex2(_sign2,_nfft1/2+1,nx3,fpad,fpad);
      _fft3.complexToComplex3(_sign3,_nfft1/2+1,_nfft2,fpad,fpad);
    }
    phase(fpad);
    center(fpad);
    return fpad;
  }

  /**
   * Applies an inverse frequency-to-space transform of a 1D array.
   * @param g the array to be transformed, a sampled function of frequency.
   * @return the transformed array, a sampled function of space.
   */
  public float[] applyInverse(float[] g) {
    ensureSamplingK1(g);
    int nx1 = _sx1.getCount();
    float[] gpad = (_overwrite)?g:copy(g);
    uncenter(gpad);
    unphase(gpad);
    if (_complex) {
      _fft1c.complexToComplex(-_sign1,gpad,gpad);
      _fft1c.scale(nx1,gpad);
      return ccopy(nx1,gpad);
    } else {
      _fft1r.complexToReal(-_sign1,gpad,gpad);
      _fft1r.scale(nx1,gpad);
      return copy(nx1,gpad);
    }
  }

  /**
   * Applies an inverse frequency-to-space transform of a 2D array.
   * @param g the array to be transformed, a sampled function of frequency.
   * @return the transformed array, a sampled function of space.
   */
  public float[][] applyInverse(float[][] g) {
    ensureSamplingK2(g);
    float[][] gpad = (_overwrite)?g:copy(g);
    int nx1 = _sx1.getCount();
    int nx2 = _sx2.getCount();
    uncenter(gpad);
    unphase(gpad);
    if (_complex) {
      _fft2.complexToComplex2(-_sign2,_nfft1,gpad,gpad);
      _fft2.scale(_nfft1,nx2,gpad);
      _fft1c.complexToComplex1(-_sign1,nx2,gpad,gpad);
      _fft1c.scale(nx1,nx2,gpad);
      return ccopy(nx1,nx2,gpad);
    } else {
      _fft2.complexToComplex2(-_sign2,_nfft1/2+1,gpad,gpad);
      _fft2.scale(_nfft1/2+1,nx2,gpad);
      _fft1r.complexToReal1(-_sign1,nx2,gpad,gpad);
      _fft1r.scale(nx1,nx2,gpad);
      return copy(nx1,nx2,gpad);
    }
  }

  /**
   * Applies an inverse frequency-to-space transform of a 3D array.
   * @param g the array to be transformed, a sampled function of frequency.
   * @return the transformed array, a sampled function of space.
   */
  public float[][][] applyInverse(float[][][] g) {
    ensureSamplingK3(g);
    float[][][] gpad = (_overwrite)?g:copy(g);
    int nx1 = _sx1.getCount();
    int nx2 = _sx2.getCount();
    int nx3 = _sx3.getCount();
    uncenter(gpad);
    unphase(gpad);
    if (_complex) {
      _fft3.complexToComplex3(-_sign3,_nfft1,_nfft2,gpad,gpad);
      _fft3.scale(_nfft1,_nfft2,nx3,gpad);
      _fft2.complexToComplex2(-_sign2,_nfft1,nx3,gpad,gpad);
      _fft2.scale(_nfft1,nx2,nx3,gpad);
      _fft1c.complexToComplex1(-_sign1,nx2,nx3,gpad,gpad);
      _fft1c.scale(nx1,nx2,nx3,gpad);
      return ccopy(nx1,nx2,nx3,gpad);
    } else {
      _fft3.complexToComplex3(-_sign3,_nfft1/2+1,_nfft2,gpad,gpad);
      _fft3.scale(_nfft1/2+1,_nfft2,nx3,gpad);
      _fft2.complexToComplex2(-_sign2,_nfft1/2+1,nx3,gpad,gpad);
      _fft2.scale(_nfft1/2+1,nx2,nx3,gpad);
      _fft1r.complexToReal1(-_sign1,nx2,nx3,gpad,gpad);
      _fft1r.scale(nx1,nx2,nx3,gpad);
      return copy(nx1,nx2,nx3,gpad);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private FftReal _fft1r;
  private FftComplex _fft1c,_fft2,_fft3;
  private Sampling _sx1,_sx2,_sx3;
  private Sampling _sk1,_sk2,_sk3;
  private int _sign1,_sign2,_sign3;
  private int _nfft1,_nfft2,_nfft3;
  private int _padding1,_padding2,_padding3;
  private boolean _center1,_center2,_center3;
  private boolean _complex;
  private boolean _overwrite;

  private void updateSampling1() {
    if (_sx1==null)
      return;
    int nx = _sx1.getCount();
    double dx = _sx1.getDelta();
    int npad = nx+_padding1;
    int nfft,nk;
    double dk,fk;
    if (_complex) {
      nfft = FftComplex.nfftSmall(npad);
      dk = 1.0/(nfft*dx);
      if (_center1) {
        boolean even = nfft%2==0;
        nk = even?nfft+1:nfft;
        fk = even?-0.5/dx:-0.5/dx+0.5*dk;
      } else {
        nk = nfft;
        fk = 0.0;
      }
      if (_fft1c==null || _nfft1!=nfft) {
        _fft1c = new FftComplex(nfft);
        _fft1r = null;
        _nfft1 = nfft;
      }
    } else {
      nfft = FftReal.nfftSmall(npad);
      dk = 1.0/(nfft*dx);
      if (_center1) {
        nk = nfft+1;
        fk = -0.5f/dx;
      } else {
        nk = nfft/2+1;
        fk = 0.0;
      }
      if (_fft1r==null || _nfft1!=nfft) {
        _fft1r = new FftReal(nfft);
        _fft1c = null;
        _nfft1 = nfft;
      }
    }
    _sk1 = new Sampling(nk,dk,fk);
    //trace("sk1: nfft="+nfft+" nk="+nk+" dk="+dk+" fk="+fk);
  }
  private void updateSampling2() {
    if (_sx2==null)
      return;
    int nx = _sx2.getCount();
    double dx = _sx2.getDelta();
    int npad = nx+_padding2;
    int nfft = FftComplex.nfftSmall(npad);
    double dk = 1.0/(nfft*dx);
    double fk;
    int nk;
    if (_center2) {
      boolean even = nfft%2==0;
      nk = even?nfft+1:nfft;
      fk = even?-0.5/dx:-0.5/dx+0.5*dk;
    } else {
      nk = nfft;
      fk = 0.0;
    }
    if (_fft2==null || _nfft2!=nfft) {
      _fft2 = new FftComplex(nfft);
      _nfft2 = nfft;
    }
    _sk2 = new Sampling(nk,dk,fk);
    //trace("sk2: nfft="+nfft+" nk="+nk+" dk="+dk+" fk="+fk);
  }
  private void updateSampling3() {
    if (_sx3==null)
      return;
    int nx = _sx3.getCount();
    double dx = _sx3.getDelta();
    int npad = nx+_padding3;
    int nfft = FftComplex.nfftSmall(npad);
    double dk = 1.0/(nfft*dx);
    double fk;
    int nk;
    if (_center3) {
      boolean even = nfft%2==0;
      nk = even?nfft+1:nfft;
      fk = even?-0.5/dx:-0.5/dx+0.5*dk;
    } else {
      nk = nfft;
      fk = 0.0;
    }
    if (_fft3==null || _nfft3!=nfft) {
      _fft3 = new FftComplex(nfft);
      _nfft3 = nfft;
    }
    _sk3 = new Sampling(nk,dk,fk);
    //trace("sk3: nfft="+nfft+" nk="+nk+" dk="+dk+" fk="+fk);
  }

  private float[] pad(float[] f) {
    int nk1 = _sk1.getCount();
    float[] fpad = new float[2*nk1];
    if (_complex) {
      ccopy(f.length/2,f,fpad);
    } else {
      copy(f.length,f,fpad);
    }
    return fpad;
  }
  private float[][] pad(float[][] f) {
    int nk1 = _sk1.getCount();
    int nk2 = _sk2.getCount();
    float[][] fpad = new float[nk2][2*nk1];
    if (_complex) {
      ccopy(f[0].length/2,f.length,f,fpad);
    } else {
      copy(f[0].length,f.length,f,fpad);
    }
    return fpad;
  }
  private float[][][] pad(float[][][] f) {
    int nk1 = _sk1.getCount();
    int nk2 = _sk2.getCount();
    int nk3 = _sk3.getCount();
    float[][][] fpad = new float[nk3][nk2][2*nk1];
    if (_complex) {
      ccopy(f[0][0].length/2,f[0].length,f.length,f,fpad);
    } else {
      copy(f[0][0].length,f[0].length,f.length,f,fpad);
    }
    return fpad;
  }

  private void ensureSamplingX1(float[] f) {
    Check.state(_sx1!=null,"sampling sx1 exists for 1st dimension");
    int l1 = f.length;
    int n1 = _sx1.getCount();
    if (_complex)
      n1 *= 2;
    Check.argument(n1==l1,"array length consistent with sampling sx1");
  }
  private void ensureSamplingX2(float[][] f) {
    Check.state(_sx2!=null,"sampling sx2 exists for 2nd dimension");
    ensureSamplingX1(f[0]);
    int l2 = f.length;
    int n2 = _sx2.getCount();
    Check.argument(n2==l2,"array length consistent with sampling sx2");
  }
  private void ensureSamplingX3(float[][][] f) {
    Check.state(_sx3!=null,"sampling sx3 exists for 3rd dimension");
    ensureSamplingX2(f[0]);
    int l3 = f.length;
    int n3 = _sx3.getCount();
    Check.argument(n3==l3,"array length consistent with sampling sx3");
  }

  private void ensureSamplingK1(float[] f) {
    Check.state(_sk1!=null,"sampling sk1 exists for 1st dimension");
    int l1 = f.length;
    int n1 = _sk1.getCount();
    Check.argument(2*n1==l1,"array length consistent with sampling sk1");
  }
  private void ensureSamplingK2(float[][] f) {
    Check.state(_sk2!=null,"sampling sk2 exists for 2nd dimension");
    ensureSamplingK1(f[0]);
    int l2 = f.length;
    int n2 = _sk2.getCount();
    Check.argument(n2==l2,"array length consistent with sampling sk2");
  }
  private void ensureSamplingK3(float[][][] f) {
    Check.state(_sk3!=null,"sampling sk3 exists for 3rd dimension");
    ensureSamplingK2(f[0]);
    int l3 = f.length;
    int n3 = _sk3.getCount();
    Check.argument(n3==l3,"array length consistent with sampling sk3");
  }

  private void center(float[] f) {
    center1(f);
    if (_center1 && !_complex)
      // real, nfft = 8
      // x x x x 0 1 2 3 4
      // 4 3 2 1 0 1 2 3 4
      creflect(_nfft1/2,_nfft1/2,f);
  }
  private void center1(float[] f) {
    if (!_center1)
      return;
    int nk1 = _sk1.getCount();
    int nfft1 = _nfft1;
    boolean even1 = nfft1%2==0;
    if (_complex) {
      if (even1) {
        // complex, nfft = 8
        // 0 1 2 3 4 5 6 7 | 8
        // 4 5 6 7 0 1 2 3 | 4
        cswap(nfft1/2,0,nfft1/2,f);
        f[2*(nk1-1)  ] = f[0];
        f[2*(nk1-1)+1] = f[1];
      } else {
        // complex, nfft = 7
        // 0 1 2 3 4 5 6
        // 4 5 6 3 0 1 2
        // 4 5 6 0 1 2 3
        cswap((nfft1-1)/2,0,(nfft1+1)/2,f);
        crotateLeft((nfft1+1)/2,(nfft1-1)/2,f);
      }
    } else {
      // real, nfft = 8
      // 0 1 2 3 4
      // 0 1 2 3 0 1 2 3 4
      cshift(nfft1/2+1,0,nfft1/2,f);
    }
  }
  private void uncenter(float[] f) {
    uncenter1(f);
  }
  private void uncenter1(float[] f) {
    if (!_center1)
      return;
    int nfft1 = _nfft1;
    boolean even1 = nfft1%2==0;
    if (_complex) {
      if (even1) {
        // complex, nfft = 8
        // 4 5 6 7 0 1 2 3 | 8
        // 0 1 2 3 4 5 6 7 | 8
        cswap(nfft1/2,0,nfft1/2,f);
      } else {
        // complex, nfft = 7
        // 4 5 6 0 1 2 3
        // 4 5 6 3 0 1 2
        // 0 1 2 3 4 5 6
        crotateRight((nfft1+1)/2,(nfft1-1)/2,f);
        cswap((nfft1-1)/2,0,(nfft1+1)/2,f);
      }
    } else {
      // real, nfft = 8
      // 4 3 2 1 0 1 2 3 4
      // 0 1 2 3 4 1 2 3 4
      cshift(nfft1/2+1,nfft1/2,0,f);
    }
  }
  private static void creflect(int n, int i, float[] f) {
    int ir = 2*(i+1), ii = ir+1;
    int jr = 2*(i-1), ji = jr+1;
    for (int k=0; k<n; ++k,ir+=2,ii+=2,jr-=2,ji-=2) {
      f[jr] =  f[ir];
      f[ji] = -f[ii];
    }
  }
  private static void cshift(int n, int i, int j, float[] f) {
    if (i<j) {
      int ir = 2*(i+n-1), ii = ir+1;
      int jr = 2*(j+n-1), ji = jr+1;
      for (int k=0; k<n; ++k,ir-=2,ii-=2,jr-=2,ji-=2) {
        f[jr] = f[ir];
        f[ji] = f[ii];
      }
    } else {
      int ir = 2*i, ii = ir+1;
      int jr = 2*j, ji = jr+1;
      for (int k=0; k<n; ++k,ir+=2,ii+=2,jr+=2,ji+=2) {
        f[jr] = f[ir];
        f[ji] = f[ii];
      }
    }
  }
  private static void cswap(int n, int i, int j, float[] f) {
    int ir = 2*i, ii = ir+1;
    int jr = 2*j, ji = jr+1;
    for (int k=0; k<n; ++k,ir+=2,ii+=2,jr+=2,ji+=2) {
      float fir = f[ir]; f[ir] = f[jr]; f[jr] = fir;
      float fii = f[ii]; f[ii] = f[ji]; f[ji] = fii;
    }
  }
  private static void crotateLeft(int n, int j, float[] f) {
    float fjr = f[j*2  ];
    float fji = f[j*2+1];
    int i = j+1, ir = 2*i, ii = ir+1;
    for (int k=1; k<n; ++k,ir+=2,ii+=2) {
      f[ir-2] = f[ir];
      f[ii-2] = f[ii];
    }
    f[ir-2] = fjr;
    f[ii-2] = fji;
  }
  private static void crotateRight(int n, int j, float[] f) {
    int m = j+n-1;
    float fmr = f[m*2  ];
    float fmi = f[m*2+1];
    int i = m, ir = 2*i, ii = ir+1;
    for (int k=1; k<n; ++k,ir-=2,ii-=2) {
      f[ir] = f[ir-2];
      f[ii] = f[ii-2];
    }
    f[ir] = fmr;
    f[ii] = fmi;
  }

  private void center(float[][] f) {
    if (_center1) {
      for (int i2=0; i2<_nfft2; ++i2)
        center1(f[i2]);
    }
    center2(f);
    if (_center1 && !_complex)
      creflect(_nfft1/2,_nfft1/2,f);
  }
  private void uncenter(float[][] f) {
    uncenter2(f);
    if (_center1) {
      for (int i2=0; i2<_nfft2; ++i2)
        uncenter1(f[i2]);
    }
  }
  private void center2(float[][] f) {
    if (!_center2)
      return;
    int nk2 = _sk2.getCount();
    int nfft2 = _nfft2;
    boolean even2 = nfft2%2==0;
    if (even2) {
      // nfft even
      // 0 1 2 3 4 5 6 7 | 8
      // 4 5 6 7 0 1 2 3 | 4
      cswap(nfft2/2,0,nfft2/2,f);
      ccopy(f[0],f[nk2-1]);
    } else {
      // nfft odd
      // 0 1 2 3 4 5 6
      // 4 5 6 3 0 1 2
      // 4 5 6 0 1 2 3
      cswap((nfft2-1)/2,0,(nfft2+1)/2,f);
      crotateLeft((nfft2+1)/2,(nfft2-1)/2,f);
    }
  }
  private void uncenter2(float[][] f) {
    if (!_center2)
      return;
    int nfft2 = _nfft2;
    boolean even2 = nfft2%2==0;
    if (even2) {
      // nfft even
      // 4 5 6 7 0 1 2 3 | 8
      // 0 1 2 3 4 5 6 7 | 8
      cswap(nfft2/2,0,nfft2/2,f);
    } else {
      // nfft odd
      // 4 5 6 0 1 2 3
      // 4 5 6 3 0 1 2
      // 0 1 2 3 4 5 6
      crotateRight((nfft2+1)/2,(nfft2-1)/2,f);
      cswap((nfft2-1)/2,0,(nfft2+1)/2,f);
    }
  }
  private static void creflect(int n, int i, float[][] f) {
    int n2 = f.length;
    for (int i2=0,j2=n2-1; i2<n2; ++i2,--j2) {
      int ir = 2*(i+1), ii = ir+1;
      int jr = 2*(i-1), ji = jr+1;
      float[] fj2 = f[j2];
      float[] fi2 = f[i2];
      for (int k=0; k<n; ++k,ir+=2,ii+=2,jr-=2,ji-=2) {
        fj2[jr] =  fi2[ir];
        fj2[ji] = -fi2[ii];
      }
    }
  }
  private static void cswap(int n, int i, int j, float[][] f) {
    for (int k=0; k<n; ++k,++i,++j) {
      float[] fi = f[i]; f[i] = f[j]; f[j] = fi;
    }
  }
  private static void crotateLeft(int n, int j, float[][] f) {
      // nfft odd
      // 4 5 6 3 0 1 2
      // 4 5 6 0 1 2 3
      // crotateLeft(n=4,j=3,f);
    float[] fj = f[j];
    int m = j+n;
    int i;
    for (i=j+1; i<m; ++i)
      f[i-1] = f[i];
    f[i-1] = fj;
  }
  private static void crotateRight(int n, int j, float[][] f) {
    int m = j+n-1;
    float[] fm = f[m];
    int i;
    for (i=m; i>j; --i)
      f[i] = f[i-1];
    f[i] = fm;
  }

  private void center(float[][][] f) {
    if (_center1) {
      for (int i3=0; i3<_nfft3; ++i3)
        for (int i2=0; i2<_nfft2; ++i2)
          center1(f[i3][i2]);
    }
    if (_center2) {
        for (int i3=0; i3<_nfft3; ++i3)
          center2(f[i3]);
    }
    center3(f);
    if (_center1 && !_complex)
      creflect(_nfft1/2,_nfft1/2,f);
  }
  private void uncenter(float[][][] f) {
    uncenter3(f);
    if (_center2) {
      for (int i3=0; i3<_nfft3; ++i3)
        uncenter2(f[i3]);
    }
    if (_center1) {
      for (int i3=0; i3<_nfft3; ++i3)
        for (int i2=0; i2<_nfft2; ++i2)
          uncenter1(f[i3][i2]);
    }
  }
  private void center3(float[][][] f) {
    if (!_center3)
      return;
    int nk3 = _sk3.getCount();
    int nfft3 = _nfft3;
    boolean even3 = nfft3%2==0;
    if (even3) {
      // nfft even
      // 0 1 2 3 4 5 6 7 | 8
      // 4 5 6 7 0 1 2 3 | 4
      cswap(nfft3/2,0,nfft3/2,f);
      ccopy(f[0],f[nk3-1]);
    } else {
      // nfft odd
      // 0 1 2 3 4 5 6
      // 4 5 6 3 0 1 2
      // 4 5 6 0 1 2 3
      cswap((nfft3-1)/2,0,(nfft3+1)/2,f);
      crotateLeft((nfft3+1)/2,(nfft3-1)/2,f);
    }
  }
  private void uncenter3(float[][][] f) {
    if (!_center3)
      return;
    int nfft3 = _nfft3;
    boolean even3 = nfft3%2==0;
    if (even3) {
      // nfft even
      // 4 5 6 7 0 1 2 3 | 8
      // 0 1 2 3 4 5 6 7 | 8
      cswap(nfft3/2,0,nfft3/2,f);
    } else {
      // nfft odd
      // 4 5 6 0 1 2 3
      // 4 5 6 3 0 1 2
      // 0 1 2 3 4 5 6
      crotateRight((nfft3+1)/2,(nfft3-1)/2,f);
      cswap((nfft3-1)/2,0,(nfft3+1)/2,f);
    }
  }
  private static void creflect(int n, int i, float[][][] f) {
    int n2 = f[0].length;
    int n3 = f.length;
    for (int i3=0,j3=n3-1; i3<n3; ++i3,--j3) {
      for (int i2=0,j2=n2-1; i2<n2; ++i2,--j2) {
        int ir = 2*(i+1), ii = ir+1;
        int jr = 2*(i-1), ji = jr+1;
        float[] fj3j2 = f[j3][j2];
        float[] fi3i2 = f[i3][i2];
        for (int k=0; k<n; ++k,ir+=2,ii+=2,jr-=2,ji-=2) {
          fj3j2[jr] =  fi3i2[ir];
          fj3j2[ji] = -fi3i2[ii];
        }
      }
    }
  }
  private static void cswap(int n, int i, int j, float[][][] f) {
    for (int k=0; k<n; ++k,++i,++j) {
      float[][] fi = f[i]; f[i] = f[j]; f[j] = fi;
    }
  }
  private static void crotateLeft(int n, int j, float[][][] f) {
      // nfft odd
      // 4 5 6 3 0 1 2
      // 4 5 6 0 1 2 3
      // crotateLeft(n=4,j=3,f);
    float[][] fj = f[j];
    int m = j+n;
    int i;
    for (i=j+1; i<m; ++i)
      f[i-1] = f[i];
    f[i-1] = fj;
  }
  private static void crotateRight(int n, int j, float[][][] f) {
    int m = j+n-1;
    float[][] fm = f[m];
    int i;
    for (i=m; i>j; --i)
      f[i] = f[i-1];
    f[i] = fm;
  }

  private void phase(float[] f) {
    phase(_sign1,f);
  }
  private void unphase(float[] f) {
    phase(-_sign1,f);
  }
  private void phase(int sign1, float[] f) {
    double fx = _sx1.getFirst();
    if (fx==0.0)
      return;
    int nk = (_complex)?_nfft1:_nfft1/2+1;
    double dp = sign1*2.0*PI*_sk1.getDelta()*fx;
    for (int i=0,ir=0,ii=1; i<nk; ++i,ir+=2,ii+=2) {
      float p = (float)(i*dp);
      float cosp = cos(p);
      float sinp = sin(p);
      float fr = f[ir];
      float fi = f[ii];
      f[ir] = fr*cosp-fi*sinp;
      f[ii] = fi*cosp+fr*sinp;
    }
  }
  private void phase(float[][] f) {
    phase(_sign1,_sign2,f);
  }
  private void unphase(float[][] f) {
    phase(-_sign1,-_sign2,f);
  }
  private void phase(int sign1, int sign2, float[][] f) {
    double fx1 = _sx1.getFirst();
    double fx2 = _sx2.getFirst();
    if (fx1==0.0 && fx2==0.0)
      return;
    int nk1 = (_complex)?_nfft1:_nfft1/2+1;
    int nk2 = _nfft2;
    double dp1 = sign1*2.0*PI*_sk1.getDelta()*fx1;
    double dp2 = sign2*2.0*PI*_sk2.getDelta()*fx2;
    for (int i2=0; i2<nk2; ++i2) {
      double p2 = i2*dp2;
      float[] f2 = f[i2];
      for (int i1=0,ir=0,ii=1; i1<nk1; ++i1,ir+=2,ii+=2) {
        float p = (float)(i1*dp1+p2);
        float cosp = cos(p);
        float sinp = sin(p);
        float fr = f2[ir];
        float fi = f2[ii];
        f2[ir] = fr*cosp-fi*sinp;
        f2[ii] = fi*cosp+fr*sinp;
      }
    }
  }
  private void phase(float[][][] f) {
    phase(_sign1,_sign2,_sign3,f);
  }
  private void unphase(float[][][] f) {
    phase(-_sign1,-_sign2,-_sign3,f);
  }
  private void phase(int sign1, int sign2, int sign3, float[][][] f) {
    double fx1 = _sx1.getFirst();
    double fx2 = _sx2.getFirst();
    double fx3 = _sx3.getFirst();
    if (fx1==0.0 && fx2==0.0 && fx3==0.0)
      return;
    int nk1 = (_complex)?_nfft1:_nfft1/2+1;
    int nk2 = _nfft2;
    int nk3 = _nfft3;
    double dp1 = sign1*2.0*PI*_sk1.getDelta()*fx1;
    double dp2 = sign2*2.0*PI*_sk2.getDelta()*fx2;
    double dp3 = sign3*2.0*PI*_sk3.getDelta()*fx3;
    for (int i3=0; i3<nk3; ++i3) {
      for (int i2=0; i2<nk2; ++i2) {
        double p23 = i2*dp2+i3*dp3;
        float[] f32 = f[i3][i2];
        for (int i1=0,ir=0,ii=1; i1<nk1; ++i1,ir+=2,ii+=2) {
          float p = (float)(i1*dp1+p23);
          float cosp = cos(p);
          float sinp = sin(p);
          float fr = f32[ir];
          float fi = f32[ii];
          f32[ir] = fr*cosp-fi*sinp;
          f32[ii] = fi*cosp+fr*sinp;
        }
      }
    }
  }
}
