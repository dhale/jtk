/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Check;
import static java.lang.Math.*;

/**
 * Fast Fourier transform of real-valued arrays. The FFT length nfft equals
 * the number of <em>real</em> numbers transformed. The transform of nfft
 * real numbers yields nfft/2+1 complex numbers. Those complex numbers are
 * packed into arrays of floats as [real_0, imag_0, real_1, imag_1, ...].
 * Here, real_k and imag_k correspond to the real and imaginary parts of 
 * the complex number with array index k. The first and last imaginary 
 * parts, imag_0 and imag_nfft/2, are zero.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class FftReal {

  public static void main(String[] args) {
    int n = 8;
    FftReal fft = FftReal.small(n);
    int nfft = fft.getNfft();
    float[] ra = new float[nfft+2];
    float[] ca = ra;
    ra[1] = 1.0f;
    fft.realToComplex(1,ra,ca);
    for (int i=0,j=0; i<nfft/2+1; ++i,j+=2)
      System.out.println("ca["+i+"] = ("+ca[j]+","+ca[j+1]+")");
    fft.complexToReal(-1,ca,ra);
    fft.scale(nfft,ra);
    for (int i=0; i<n; ++i)
      System.out.println("ra["+i+"] = "+ra[i]);
    testRandom(1441440);
  }
  private static void testRandom(int n) {
    FftReal fft = FftReal.small(n);
    int nfft = fft.getNfft();
    float[] ca = new float[nfft+2];
    float[] ra = ca;
    float[] rb = new float[nfft];
    java.util.Random random = new java.util.Random(314159);
    for (int i=0; i<n; ++i)
      ra[i] = random.nextFloat();
    fft.realToComplex( 1,ra,ca);
    fft.complexToReal(-1,ca,rb);
    fft.scale(nfft,rb);
    double sum = 0.0;
    for (int i=0; i<n; ++i)
      sum += abs(rb[i]-ra[i]);
    double err = sum/n;
    System.out.println("err="+err);
  }

  /**
   * Constructs a new FFT, with specified length. Valid FFT lengths can 
   * be obtained by calling the methods {@link #nfftSmall(int)} and 
   * {@link #nfftFast(int)}. Alternatively, the methods {@link #small(int)} 
   * and {@link #fast(int)} return an FFT with valid length.
   * @param nfft the FFT length, which must be valid.
   */
  public FftReal(int nfft) {
    _fftc = new FftComplex(nfft/2);
    _nfft = nfft;
  }

  /**
   * Gets the FFT length nfft for this FFT.
   * @return the FFT length.
   */
  public int getNfft() {
    return _nfft;
  }

  /**
   * Computes a real-to-complex fast Fourier transform. Transforms nfft
   * real numbers to nfft/2+1 complex numbers. If the input and output 
   * arrays are the same array, the transform will be performed in-place.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param rx the input array; rx.length must not be less than nfft.
   * @param cy the output array; cy.length must not be less than nfft+2.
   */
  public void realToComplex(int sign, float[] rx, float[] cy) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(rx.length>=_nfft,"rx.length is sufficient");
    Check.argument(cy.length>=_nfft+2,"cy.length is sufficient");
    int n = _nfft;
    while (--n>0)
      cy[n] = 0.5f*rx[n];
    _fftc.complexToComplex(sign,cy,cy);
    cy[_nfft] = 2.0f*(cy[0]-cy[1]);
    cy[0    ] = 2.0f*(cy[0]+cy[1]);
    cy[_nfft+1] = 0.0f;
    cy[1      ] = 0.0f;
    double theta = sign*2.0*PI/_nfft;
    double wt = sin(0.5*theta);
    double wpi = sin(theta); // = sin(theta)
    double wpr = -2.0*wt*wt; // = cos(theta)-1, with less rounding error
    double wi = wpi;
    double wr = 1.0+wpr;
    for (int j=2,k=_nfft-2; j<=k; j+=2,k-=2) {
      float sumr = cy[j  ]+cy[k  ];
      float sumi = cy[j+1]+cy[k+1];
      float difr = cy[j  ]-cy[k  ];
      float difi = cy[j+1]-cy[k+1];
      float tmpr = (float)(wi*difr+wr*sumi);
      float tmpi = (float)(wi*sumi-wr*difr);
      cy[j  ] = sumr+tmpr;
      cy[j+1] = tmpi+difi;
      cy[k  ] = sumr-tmpr;
      cy[k+1] = tmpi-difi;
      wt = wr;
      wr += wr*wpr-wi*wpi;
      wi += wi*wpr+wt*wpi;
    }
  }

  /**
   * Computes a complex-to-real fast Fourier transform. Transforms nfft/2+1
   * complex numbers to nfft real numbers. If the input and output arrays
   * are the same array, the transform will be performed in-place.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param cx the input array; cx.length must not be less than nfft+2.
   * @param ry the output array; ry.length must not be less than nfft.
   */
  public void complexToReal(int sign, float[] cx, float[] ry) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(cx.length>=_nfft+2,"cy.length is sufficient");
    Check.argument(ry.length>=_nfft,"rx.length is sufficient");
    int n = _nfft;
    while (--n>2)
      ry[n] = cx[n];
    ry[1] = cx[0]-cx[_nfft];
    ry[0] = cx[0]+cx[_nfft];
    double theta = -sign*2.0*PI/_nfft;
    double wt = sin(0.5*theta);
    double wpi = sin(theta); // = sin(theta)
    double wpr = -2.0*wt*wt; // = cos(theta)-1, with less rounding error
    double wi = wpi;
    double wr = 1.0+wpr;
    for (int j=2,k=_nfft-2; j<=k; j+=2,k-=2) {
      float sumr = ry[j  ]+ry[k  ];
      float sumi = ry[j+1]+ry[k+1];
      float difr = ry[j  ]-ry[k  ];
      float difi = ry[j+1]-ry[k+1];
      float tmpr = (float)(wi*difr-wr*sumi);
      float tmpi = (float)(wi*sumi+wr*difr);
      ry[j  ] = sumr+tmpr;
      ry[j+1] = tmpi+difi;
      ry[k  ] = sumr-tmpr;
      ry[k+1] = tmpi-difi;
      wt = wr;
      wr += wr*wpr-wi*wpi;
      wi += wi*wpr+wt*wpi;
    }
    _fftc.complexToComplex(sign,ry,ry);
  }

  /**
   * Scales n1 real numbers in the specified array by 1/nfft. The 
   * inverse of a real-to-complex FFT is a complex-to-real FFT (with 
   * opposite sign) followed by this scaling.
   * @param n1 1st (only) dimension of the array rx.
   * @param rx the input/output array.
   */
  public void scale(int n1, float[] rx) {
    float s = 1.0f/(float)_nfft;
    while (--n1>=0)
      rx[n1] *= s;
  }

  /**
   * Returns a new FFT optimized for memory. The FFT length will be the 
   * smallest valid length that is not less than the specified length n.
   * @param n the lower bound on FFT length.
   * @return the FFT.
   */
  public static FftReal small(int n) {
    return new FftReal(nfftSmall(n));
  }

  /**
   * Returns a new FFT optimized for speed. The FFT length will be the 
   * fastest valid length that is not less than the specified length n.
   * @param n the lower bound on FFT length.
   * @return the FFT.
   */
  public static FftReal fast(int n) {
    return new FftReal(nfftFast(n));
  }

  /**
   * Returns an FFT length optimized for memory. The FFT length will be the 
   * smallest valid length that is not less than the specified length n.
   * @param n the lower bound on FFT length.
   * @return the FFT length.
   * @exception IllegalArgumentException if the specified length n exceeds
   *  the maximum length supported by this implementation. Currently, the 
   *  maximum length is 1,441,440.
   */
  public static int nfftSmall(int n) {
    Check.argument(n<=1441440,"n does not exceed 1441440");
    return 2*FftComplex.nfftSmall((n+1)/2);
  }

  /**
   * Returns an FFT length optimized for speed. The FFT length will be the 
   * fastest valid length that is not less than the specified length n.
   * @param n the lower bound on FFT length.
   * @return the FFT length.
   * @exception IllegalArgumentException if the specified length n exceeds
   *  the maximum length supported by this implementation. Currently, the 
   *  maximum length is 1,441,440.
   */
  public static int nfftFast(int n) {
    return nfftSmall(n);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nfft; // FFT length (number of real numbers to transform)
  private FftComplex _fftc; // FFT for complex-to-complex transforms
}
