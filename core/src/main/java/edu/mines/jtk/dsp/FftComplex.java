/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * Fast Fourier transform of complex-valued arrays. The FFT length 
 * nfft equals the number of <em>complex</em> numbers transformed. 
 * The transform of nfft complex numbers yields nfft complex numbers. 
 * Those complex numbers are packed into arrays of floats as [real_0, 
 * imag_0, real_1, imag_1, ...]. Here, real_k and imag_k correspond to 
 * the real and imaginary parts, respectively, of the complex number 
 * with array index k.
 * <p>
 * When input and output arrays are the same array, transforms are 
 * performed in-place. For example, an input array cx[2*nfft] of nfft 
 * complex numbers may be the same as an output array cy[2*nfft] of 
 * nfft complex numbers. By "the same array", we mean that cx==cy.
 * <p>
 * Transforms may be performed for any dimension of a multi-dimensional 
 * array. For example, we may transform the 1st dimension of an input 
 * array cx[n2][2*nfft] of n2*nfft complex numbers to an output array 
 * cy[n2][2*nfft] of n2*nfft complex numbers. Or, we may transform the 
 * 2nd dimension of an input array cx[nfft][2*n1] of nfft*n1 complex 
 * numbers to an output array cy[nfft][2*n1] of nfft*n1 complex numbers. 
 * In either case, the input array cx and the output array cy may be the 
 * same array, such that the transform may be performed in-place. 
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class FftComplex {

  /**
   * Constructs a new FFT, with specified length. Valid FFT lengths 
   * can be obtained by calling the methods {@link #nfftSmall(int)} 
   * and {@link #nfftFast(int)}.
   * @param nfft the FFT length, which must be valid.
   */
  public FftComplex(int nfft) {
    Check.argument(Pfacc.nfftValid(nfft),"nfft="+nfft+" is valid FFT length");
    _nfft = nfft;
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
    Check.argument(n<=720720,"n does not exceed 720720");
    return Pfacc.nfftFast(n);
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
   * Transforms a 1-D input array cx[2*nfft] of nfft complex numbers 
   * to a 1-D output array cy[2*nfft] of nfft complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param cx the input array.
   * @param cy the output array.
   */
  public void complexToComplex(int sign, float[] cx, float[] cy) {
    checkSign(sign);
    checkArray(2*_nfft,cx,"cx");
    checkArray(2*_nfft,cy,"cy");
    if (cx!=cy)
      ccopy(_nfft,cx,cy);
    Pfacc.transform(sign,_nfft,cy);
  }

  /**
   * Computes a complex-to-complex dimension-1 fast Fourier transform. 
   * Transforms a 2-D input array cx[n2][2*nfft] of n2*nfft complex numbers 
   * to a 2-D output array cy[n2][2*nfft] of n2*nfft complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n2 the 2nd dimension of arrays.
   * @param cx the input array.
   * @param cy the output array.
   */
  public void complexToComplex1(int sign, int n2, float[][] cx, float[][] cy) {
    checkSign(sign);
    checkArray(2*_nfft,n2,cx,"cx");
    checkArray(2*_nfft,n2,cy,"cy");
    for (int i2=0; i2<n2; ++i2)
      complexToComplex(sign,cx[i2],cy[i2]);
  }

  /**
   * Computes a complex-to-complex dimension-2 fast Fourier transform. 
   * Transforms a 2-D input array cx[nfft][2*n1] of nfft*n1 complex numbers 
   * to a 2-D output array cy[nfft][2*n1] of nfft*n1 complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n1 the 1st dimension of arrays.
   * @param cx the input array.
   * @param cy the output array.
   */
  public void complexToComplex2(int sign, int n1, float[][] cx, float[][] cy) {
    checkSign(sign);
    checkArray(2*n1,_nfft,cx,"cx");
    checkArray(2*n1,_nfft,cy,"cy");
    if (cx!=cy) 
      ccopy(n1,_nfft,cx,cy);
    Pfacc.transform2a(sign,n1,_nfft,cy);
  }

  /**
   * Computes a complex-to-complex dimension-1 fast Fourier transform. 
   * Transforms a 3-D input array cx[n3][n2][2*nfft] of n3*n2*nfft complex 
   * numbers to a 3-D output array cy[n3][n2][2*nfft] of n3*n2*nfft complex 
   * numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n2 the 2nd dimension of arrays.
   * @param n3 the 3rd dimension of arrays.
   * @param cx the input array.
   * @param cy the output array.
   */
  public void complexToComplex1(
    int sign, int n2, int n3, float[][][] cx, float[][][] cy)
  {
    checkSign(sign);
    checkArray(2*_nfft,n2,n3,cx,"cx");
    checkArray(2*_nfft,n2,n3,cy,"cy");
    for (int i3=0; i3<n3; ++i3)
        complexToComplex1(sign,n2,cx[i3],cy[i3]);
  }

  /**
   * Computes a complex-to-complex dimension-2 fast Fourier transform. 
   * Transforms a 3-D input array cx[n3][nfft][2*n1] of n3*nfft*n1 complex 
   * numbers to a 3-D output array cy[n3][nfft][2*n1] of n3*nfft*n1 complex 
   * numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n1 the 1st dimension of arrays.
   * @param n3 the 3rd dimension of arrays.
   * @param cx the input array.
   * @param cy the output array.
   */
  public void complexToComplex2(
    int sign, int n1, int n3, float[][][] cx, float[][][] cy)
  {
    checkSign(sign);
    checkArray(2*n1,_nfft,n3,cx,"cx");
    checkArray(2*n1,_nfft,n3,cy,"cy");
    for (int i3=0; i3<n3; ++i3)
        complexToComplex2(sign,n1,cx[i3],cy[i3]);
  }

  /**
   * Computes a complex-to-complex dimension-3 fast Fourier transform. 
   * Transforms a 3-D input array cx[nfft][n2][2*n1] of nfft*n2*n1 complex 
   * numbers to a 3-D output array cy[nfft][n2][2*n1] of nfft*n2*n1 complex 
   * numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n1 the 1st dimension of arrays.
   * @param n2 the 2nd dimension of arrays.
   * @param cx the input array.
   * @param cy the output array.
   */
  public void complexToComplex3(
    int sign, int n1, int n2, float[][][] cx, float[][][] cy)
  {
    checkSign(sign);
    checkArray(2*n1,n2,_nfft,cx,"cx");
    checkArray(2*n1,n2,_nfft,cy,"cy");
    float[][] cxi2 = new float[_nfft][];
    float[][] cyi2 = new float[_nfft][];
    for (int i2=0; i2<n2; ++i2) {
      for (int i3=0; i3<_nfft; ++i3) {
        cxi2[i3] = cx[i3][i2];
        cyi2[i3] = cy[i3][i2];
      }
      complexToComplex2(sign,n1,cxi2,cyi2);
    }
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
    for (int i2=0; i2<n2; ++i2)
      scale(n1,cx[i2]);
  }

  /**
   * Scales n1*n2*n3 complex numbers in the specified array by 1/nfft. 
   * The inverse of a complex-to-complex FFT is a complex-to-complex 
   * FFT (with opposite sign) followed by this scaling.
   * @param n1 the 1st dimension of the array cx.
   * @param n2 the 2nd dimension of the array cx.
   * @param n3 the 3rd dimension of the array cx.
   * @param cx the input/output array[n3][n2][2*n1].
   */
  public void scale(int n1, int n2, int n3, float[][][] cx) {
    for (int i3=0; i3<n3; ++i3)
      scale(n1,n2,cx[i3]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nfft; // FFT length (number of complex numbers to transform)

  private static void checkSign(int sign) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
  }

  private static void checkArray(int n, float[] a, String name) {
    Check.argument(a.length>=n,"dimensions of "+name+" are valid");
  }

  private static void checkArray(int n1, int n2, float[][] a, String name) {
    boolean ok = a.length>=n2;
    for (int i2=0; i2<n2 && ok; ++i2)
      ok = a[i2].length>=n1;
    Check.argument(ok,"dimensions of "+name+" are valid");
  }

  private static void checkArray(
    int n1, int n2, int n3, float[][][] a, String name) 
  {
    boolean ok = a.length>=n3;
    for (int i3=0; i3<n3 && ok; ++i3) {
      ok = a[i3].length>=n2;
      for (int i2=0; i2<n2 && ok; ++i2) {
        ok = a[i3][i2].length>=n1;
      }
    }
    Check.argument(ok,"dimensions of "+name+" are valid");
  }
}
