/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Check;

/**
 * Fast Fourier transform of complex-valued arrays. The FFT length nfft equals 
 * the number of <em>complex</em> numbers transformed. The transform of nfft 
 * complex numbers yields nfft complex numbers. Those complex numbers are 
 * packed into arrays of floats as [real_0, imag_0, real_1, imag_1, ...].
 * Here, real_k and imag_k correspond to the real and imaginary parts, 
 * respectively, of the complex number with array index k.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.18
 */
public class FftComplex {

  /**
   * Constructs a new FFT, with specified length. Valid FFT lengths can 
   * be obtained by calling the methods {@link #nfftSmall(int)} and 
   * {@link #nfftFast(int)}. Alternatively, the methods {@link #small(int)} 
   * and {@link #fast(int)} return an FFT with valid length.
   * @param nfft the FFT length, which must be valid.
   */
  public FftComplex(int nfft) {
    Check.argument(Pfacc.nfftValid(nfft),"nfft="+nfft+" is valid FFT length");
    _nfft = nfft;
  }

  /**
   * Gets the FFT length for this FFT.
   * @return the FFT length.
   */
  public int getNfft() {
    return _nfft;
  }

  /**
   * Computes a complex-to-complex fast Fourier transform. 
   * Transforms an input array cx[2*nfft] of nfft complex numbers to an 
   * output array cy[2*nfft] of nfft complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param cx the input array.
   * @param cy the output array.
   */
  public void complexToComplex(int sign, float[] cx, float[] cy) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(cx.length>=2*_nfft,"cx.length is valid");
    Check.argument(cy.length>=2*_nfft,"cy.length is valid");
    if (cx!=cy)
      System.arraycopy(cx,0,cy,0,_nfft);
    Pfacc.transform(sign,_nfft,cy);
  }

  /**
   * Computes a complex-to-complex dimension-1 fast Fourier transform. 
   * Transforms an input array cx[n2][2*nfft] of n2*nfft complex numbers 
   * to an output array cy[n2][2*nfft] of n2*nfft complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n2 the 2nd dimension of arrays.
   * @param cx the input array.
   * @param cy the output array.
   */
  public void complexToComplex1(int sign, int n2, float[][] cx, float[][] cy) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(cx.length>=n2,"cx.length is valid");
    Check.argument(cy.length>=n2,"cy.length is valid");
    for (int i2=0; i2<n2; ++i2) {
      Check.argument(cx[i2].length>=2*_nfft,"cx[i2].length is valid");
      Check.argument(cy[i2].length>=2*_nfft,"cy[i2].length is valid");
      complexToComplex(sign,cx[i2],cy[i2]);
    }
  }

  /**
   * Computes a complex-to-complex dimension-2 fast Fourier transform. 
   * Transforms an input array cx[nfft][2*n1] of nfft*n1 complex numbers 
   * to an output array cy[nfft][2*n1] of nfft*n1 complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n1 the 1st dimension of arrays.
   * @param cx the input array.
   * @param cy the output array.
   */
  public void complexToComplex2(int sign, int n1, float[][] cx, float[][] cy) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(cx.length>=_nfft,"cx.length is valid");
    Check.argument(cy.length>=_nfft,"cy.length is valid");
    if (cx!=cy) {
      for (int i2=0; i2<_nfft; ++i2) {
        Check.argument(cx[i2].length>=2*n1,"cx[i2].length is valid");
        Check.argument(cy[i2].length>=2*n1,"cy[i2].length is valid");
        System.arraycopy(cx[i2],0,cy[i2],0,2*n1);
      }
    }
    Pfacc.transform2a(sign,n1,_nfft,cy);
  }

  /**
   * Scales n1 complex numbers in the specified array by 1/nfft. 
   * The inverse of a complex-to-complex FFT is a complex-to-complex 
   * FFT (with opposite sign) followed by this scaling.
   * @param n1 1st (only) dimension of the array cx.
   * @param cx the input/output array[2*n1].
   */
  public void scale(int n1, float[] cx) {
    float s = 1.0f/(float)_nfft;
    int n = 2*n1;
    while (--n>=0)
      cx[n] *= s;
  }

  /**
   * Scales n1*n2 complex numbers in the specified array by 1/nfft. 
   * The inverse of a complex-to-complex FFT is a complex-to-complex 
   * FFT (with opposite sign) followed by this scaling.
   * @param n1 the 1st dimension of the array cx.
   * @param n2 the 2nd dimension of the array cx.
   * @param cx the input/output array[n2][2*n1].
   */
  public void scale(int n1, int n2, float[][] cx) {
    float s = 1.0f/(float)_nfft;
    for (int i2=0; i2<n2; ++i2) {
      float[] cxi2 = cx[i2];
      int n = 2*n1;
      while (--n>=0)
        cxi2[n] *= s;
    }
  }

  /**
   * Returns a new FFT optimized for memory. The FFT length will be the 
   * smallest valid length that is not less than the specified length n.
   * @param n the lower bound on FFT length.
   * @return the FFT.
   */
  public static FftComplex small(int n) {
    return new FftComplex(nfftSmall(n));
  }

  /**
   * Returns a new FFT optimized for speed. The FFT length will be the 
   * fastest valid length that is not less than the specified length n.
   * @param n the lower bound on FFT length.
   * @return the FFT.
   */
  public static FftComplex fast(int n) {
    return new FftComplex(nfftFast(n));
  }

  /**
   * Returns an FFT length optimized for memory. The FFT length will be the 
   * smallest valid length that is not less than the specified length n.
   * @param n the lower bound on FFT length.
   * @return the FFT length.
   * @exception IllegalArgumentException if the specified length n exceeds
   *  the maximum length supported by this implementation. Currently, the 
   *  maximum length is 720,720.
   */
  public static int nfftSmall(int n) {
    Check.argument(n<=720720,"n does not exceed 720720");
    return Pfacc.nfftSmall(n);
  }

  /**
   * Returns an FFT length optimized for speed. The FFT length will be the 
   * fastest valid length that is not less than the specified length n.
   * @param n the lower bound on FFT length.
   * @return the FFT length.
   * @exception IllegalArgumentException if the specified length n exceeds
   *  the maximum length supported by this implementation. Currently, the 
   *  maximum length is 720,720.
   */
  public static int nfftFast(int n) {
    return nfftSmall(n); // TODO: implement
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nfft; // FFT length (number of complex numbers to transform)
}
