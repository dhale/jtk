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

import static java.lang.Math.PI;
import static java.lang.Math.sin;

import edu.mines.jtk.util.Check;

/**
 * Fast Fourier transform of real-valued arrays. The FFT length nfft 
 * equals the number of <em>real</em> numbers transformed. The transform 
 * of nfft real numbers yields nfft/2+1 complex numbers. (The imaginary 
 * parts of the first and last complex numbers are always zero.) For 
 * real-to-complex and complex-to-real transforms, nfft is always an even 
 * number.
 * <p>
 * Complex numbers are packed into arrays of floats as [real_0, imag_0, 
 * real_1, imag_1, ...]. Here, real_k and imag_k correspond to the real 
 * and imaginary parts of the complex number with index k. 
 * <p>
 * When input and output arrays are the same array, transforms are performed 
 * in-place. For example, an input array rx[nfft] of nfft real numbers may be 
 * the same as an output array cy[nfft+2] of nfft/2+1 complex numbers. By
 * "the same array", we mean that rx==cy. In this case, both rx.length and
 * cy.length equal nfft+2. When we write rx[nfft] (here and below), we imply 
 * that only the first nfft floats in the input array rx are accessed.
 * <p>
 * Transforms may be performed for any dimension of a multi-dimensional 
 * array. For example, we may transform the 1st dimension of an input array 
 * rx[n2][nfft] of n2*nfft real numbers to an output array cy[n2][nfft+2] of 
 * n2*(nfft/2+1) complex numbers. Or, we may transform the 2nd dimension of 
 * an input array rx[nfft][n1] of nfft*n1 real numbers to an output array 
 * cy[nfft/2+1][2*n1] of (nfft/2+1)*n1 complex numbers. In either case, the 
 * input array rx and the output array cy may be the same array, such that 
 * the transform may be performed in-place. 
 * <p>
 * In-place transforms are typically used to reduce memory consumption. 
 * Note, however, that memory consumption is reduced for only dimension-1 
 * in-place transforms. Dimension-2 (and higher) in-place transforms save 
 * no memory, because of the contiguous packing of real and imaginary parts 
 * of complex numbers in multi-dimensional arrays of floats. (See above.)
 * Therefore, dimension-1 transforms are best when performing real-to-complex 
 * or complex-to-real transforms of multi-dimensional arrays.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class FftReal {

  /**
   * Constructs a new FFT, with specified length. Valid FFT lengths 
   * can be obtained by calling the methods {@link #nfftSmall(int)} 
   * and {@link #nfftFast(int)}.
   * @param nfft the FFT length, which must be valid.
   */
  public FftReal(int nfft) {
    Check.argument(
      nfft%2==0 && Pfacc.nfftValid(nfft/2),
      "nfft="+nfft+" is valid FFT length");
    _nfft = nfft;
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
    return 2*Pfacc.nfftSmall((n+1)/2);
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
    Check.argument(n<=1441440,"n does not exceed 1441440");
    return 2*Pfacc.nfftFast((n+1)/2);
  }

  /**
   * Gets the FFT length nfft for this FFT.
   * @return the FFT length.
   */
  public int getNfft() {
    return _nfft;
  }

  /**
   * Computes a real-to-complex fast Fourier transform.
   * Transforms a 1-D input array rx[nfft] of nfft real numbers to 
   * a 1-D output array cy[nfft+2] of nfft/2+1 complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param rx the input array.
   * @param cy the output array.
   */
  public void realToComplex(int sign, float[] rx, float[] cy) {
    checkSign(sign);
    checkArray(_nfft,rx,"rx");
    checkArray(_nfft+2,cy,"cy");
    int n = _nfft;
    while (--n>=0)
      cy[n] = 0.5f*rx[n];
    Pfacc.transform(sign,_nfft/2,cy);
    cy[_nfft] = 2.0f*(cy[0]-cy[1]);
    cy[0    ] = 2.0f*(cy[0]+cy[1]);
    cy[_nfft+1] = 0.0f;
    cy[1      ] = 0.0f;
    double theta = sign*2.0*PI/_nfft;
    double wt = sin(0.5*theta);
    double wpr = -2.0*wt*wt; // = cos(theta)-1, with less rounding error
    double wpi = sin(theta); // = sin(theta)
    double wr = 1.0+wpr;
    double wi = wpi;
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
   * Computes a complex-to-real fast Fourier transform. 
   * Transforms a 1-D input array cx[nfft+2] of nfft/2+1 complex numbers 
   * to a 1-D output array ry[nfft] of nfft real numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param cx the input array.
   * @param ry the output array.
   */
  public void complexToReal(int sign, float[] cx, float[] ry) {
    checkSign(sign);
    checkArray(_nfft+2,cx,"cx");
    checkArray(_nfft,ry,"ry");
    if (cx!=ry) {
      int n = _nfft;
      while (--n>=2)
        ry[n] = cx[n];
    }
    ry[1] = cx[0]-cx[_nfft];
    ry[0] = cx[0]+cx[_nfft];
    double theta = -sign*2.0*PI/_nfft;
    double wt = sin(0.5*theta);
    double wpr = -2.0*wt*wt; // = cos(theta)-1, with less rounding error
    double wpi = sin(theta); // = sin(theta)
    double wr = 1.0+wpr;
    double wi = wpi;
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
    Pfacc.transform(sign,_nfft/2,ry);
  }

  /**
   * Computes a real-to-complex dimension-1 fast Fourier transform. 
   * Transforms a 2-D input array rx[n2][nfft] of n2*nfft real numbers to 
   * a 2-D output array cy[n2][nfft+2] of n2*(nfft/2+1) complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n2 the 2nd dimension of arrays.
   * @param rx the input array.
   * @param cy the output array.
   */
  public void realToComplex1(int sign, int n2, float[][] rx, float[][] cy) {
    checkSign(sign);
    checkArray(_nfft,n2,rx,"rx");
    checkArray(_nfft+2,n2,cy,"cy");
    for (int i2=0; i2<n2; ++i2)
      realToComplex(sign,rx[i2],cy[i2]);
  }

  /**
   * Computes a complex-to-real dimension-1 fast Fourier transform. 
   * Transforms a 2-D input array cx[n2][nfft+2] of n2*(nfft/2+1) complex 
   * numbers to a 2-D output array ry[n2][nfft] of n2*nfft real numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n2 the 2nd dimension of arrays.
   * @param cx the input array.
   * @param ry the output array.
   */
  public void complexToReal1(int sign, int n2, float[][] cx, float[][] ry) {
    checkSign(sign);
    checkArray(_nfft+2,n2,cx,"cx");
    checkArray(_nfft,n2,ry,"ry");
    for (int i2=0; i2<n2; ++i2)
      complexToReal(sign,cx[i2],ry[i2]);
  }

  /**
   * Computes a real-to-complex dimension-2 fast Fourier transform. 
   * Transforms a 2-D input array rx[nfft][n1] of nfft*n1 real numbers to a
   * 2-D output array cy[nfft/2+1][2*n1] of (nfft/2+1)*n1 complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n1 the 1st dimension of arrays.
   * @param rx the input array.
   * @param cy the output array.
   */
  public void realToComplex2(int sign, int n1, float[][] rx, float[][] cy) {
    checkSign(sign);
    checkArray(n1,_nfft,rx,"rx");
    checkArray(2*n1,_nfft/2+1,cy,"cy");

    // Pack real input rx into complex output cy. This is complicated 
    // so that it works when input and output arrays are the same.
    for (int i1=n1-1,j1=i1*2; i1>=0; --i1,j1-=2) {
      for (int i2=_nfft-2,j2=i2/2; i2>=0; i2-=2,--j2) {
        cy[j2][j1  ] = 0.5f*rx[i2  ][i1];
        cy[j2][j1+1] = 0.5f*rx[i2+1][i1];
      }
    }

    // Dimension-2 complex-to-complex transform.
    Pfacc.transform2a(sign,n1,_nfft/2,cy);

    // Finish transform.
    float[] cy0 = cy[0];
    float[] cyn = cy[_nfft/2];
    for (int i1=2*n1-2; i1>=0; i1-=2) {
      cyn[i1  ] = 2.0f*(cy0[i1]-cy0[i1+1]);
      cy0[i1  ] = 2.0f*(cy0[i1]+cy0[i1+1]);
      cyn[i1+1] = 0.0f;
      cy0[i1+1] = 0.0f;
    }
    double theta = sign*2.0*PI/_nfft;
    double wt = sin(0.5*theta);
    double wpr = -2.0*wt*wt; // = cos(theta)-1, with less rounding error
    double wpi = sin(theta); // = sin(theta)
    double wr = 1.0+wpr;
    double wi = wpi;
    for (int j2=1,k2=_nfft/2-1; j2<=k2; ++j2,--k2) {
      float[] cyj2 = cy[j2];
      float[] cyk2 = cy[k2];
      for (int i1=0,j1=0; i1<n1; ++i1,j1+=2) {
        float sumr = cyj2[j1  ]+cyk2[j1  ];
        float sumi = cyj2[j1+1]+cyk2[j1+1];
        float difr = cyj2[j1  ]-cyk2[j1  ];
        float difi = cyj2[j1+1]-cyk2[j1+1];
        float tmpr = (float)(wi*difr+wr*sumi);
        float tmpi = (float)(wi*sumi-wr*difr);
        cyj2[j1  ] = sumr+tmpr;
        cyj2[j1+1] = tmpi+difi;
        cyk2[j1  ] = sumr-tmpr;
        cyk2[j1+1] = tmpi-difi;
      }
      wt = wr;
      wr += wr*wpr-wi*wpi;
      wi += wi*wpr+wt*wpi;
    }
  }

  /**
   * Computes a complex-to-real dimension-2 fast Fourier transform. 
   * Transforms a 2-D input array cx[nfft/2+1][2*n1] of (nfft/2+1)*n1 complex
   * numbers to a 2-D output array ry[nfft][n1] of nfft*n1 real numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n1 the 1st dimension of arrays.
   * @param cx the input array.
   * @param ry the output array.
   */
  public void complexToReal2(int sign, int n1, float[][] cx, float[][] ry) {
    checkSign(sign);
    checkArray(2*n1,_nfft/2+1,cx,"cx");
    checkArray(n1,_nfft,ry,"ry");

    // Unpack complex input cx into real output ry. This is complicated 
    // so that it works when input and output arrays are the same.
    for (int i1=0,j1=0; j1<n1; i1+=2,++j1) {
      float cx0 = cx[0      ][i1];
      float cxn = cx[_nfft/2][i1];
      for (int i2=_nfft/2-1,j2=2*i2; i2>0; --i2,j2-=2) {
        ry[j2  ][j1] = cx[i2][i1  ];
        ry[j2+1][j1] = cx[i2][i1+1];
      }
      ry[1][j1] = cx0-cxn;
      ry[0][j1] = cx0+cxn;
    }

    // Begin transform.
    double theta = -sign*2.0*PI/_nfft;
    double wt = sin(0.5*theta);
    double wpr = -2.0*wt*wt; // = cos(theta)-1, with less rounding error
    double wpi = sin(theta); // = sin(theta)
    double wr = 1.0+wpr;
    double wi = wpi;
    for (int j2=2,k2=_nfft-2; j2<=k2; j2+=2,k2-=2) {
      float[] ryj2r = ry[j2  ];
      float[] ryj2i = ry[j2+1];
      float[] ryk2r = ry[k2  ];
      float[] ryk2i = ry[k2+1];
      for (int i1=0; i1<n1; ++i1) {
        float sumr = ryj2r[i1]+ryk2r[i1];
        float sumi = ryj2i[i1]+ryk2i[i1];
        float difr = ryj2r[i1]-ryk2r[i1];
        float difi = ryj2i[i1]-ryk2i[i1];
        float tmpr = (float)(wi*difr-wr*sumi);
        float tmpi = (float)(wi*sumi+wr*difr);
        ryj2r[i1] = sumr+tmpr;
        ryj2i[i1] = tmpi+difi;
        ryk2r[i1] = sumr-tmpr;
        ryk2i[i1] = tmpi-difi;
      }
      wt = wr;
      wr += wr*wpr-wi*wpi;
      wi += wi*wpr+wt*wpi;
    }

    // Dimension-2 complex-to-complex transform.
    Pfacc.transform2b(sign,n1,_nfft/2,ry);
  }

  /**
   * Computes a real-to-complex dimension-1 fast Fourier transform. 
   * Transforms a 3-D input array rx[n3][n2][nfft] of n3*n2*nfft real 
   * numbers to a 3-D output array cy[n3][n2][nfft+2] of n3*n2*(nfft/2+1) 
   * complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n2 the 2nd dimension of arrays.
   * @param n3 the 3rd dimension of arrays.
   * @param rx the input array.
   * @param cy the output array.
   */
  public void realToComplex1(
    int sign, int n2, int n3, float[][][] rx, float[][][] cy) 
  {
    checkSign(sign);
    checkArray(_nfft,n2,n3,rx,"rx");
    checkArray(_nfft+2,n2,n3,cy,"cy");
    for (int i3=0; i3<n3; ++i3)
      realToComplex1(sign,n2,rx[i3],cy[i3]);
  }

  /**
   * Computes a complex-to-real dimension-1 fast Fourier transform. 
   * Transforms a 3-D input array cx[n3][n2][nfft+2] of n3*n2*(nfft/2+1) 
   * complex numbers to a 3-D output array ry[n3][n2][nfft] of n3*n2*nfft 
   * real numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n2 the 2nd dimension of arrays.
   * @param n3 the 3rd dimension of arrays.
   * @param cx the input array.
   * @param ry the output array.
   */
  public void complexToReal1(
    int sign, int n2, int n3, float[][][] cx, float[][][] ry) 
  {
    checkSign(sign);
    checkArray(_nfft+2,n2,n3,cx,"cx");
    checkArray(_nfft,n2,n3,ry,"ry");
    for (int i3=0; i3<n3; ++i3)
      complexToReal1(sign,n2,cx[i3],ry[i3]);
  }

  /**
   * Scales n1 real numbers in the specified array by 1/nfft. 
   * The inverse of a real-to-complex FFT is a complex-to-real FFT 
   * (with opposite sign) followed by this scaling.
   * @param n1 1st (only) dimension of the array rx.
   * @param rx the input/output array[n1].
   */
  public void scale(int n1, float[] rx) {
    float s = 1.0f/(float)_nfft;
    while (--n1>=0)
      rx[n1] *= s;
  }

  /**
   * Scales n1*n2 real numbers in the specified array by 1/nfft. 
   * The inverse of a real-to-complex FFT is a complex-to-real FFT 
   * (with opposite sign) followed by this scaling.
   * @param n1 the 1st dimension of the array rx.
   * @param n2 the 2nd dimension of the array rx.
   * @param rx the input/output array[n2][n1].
   */
  public void scale(int n1, int n2, float[][] rx) {
    for (int i2=0; i2<n2; ++i2)
      scale(n1,rx[i2]);
  }

  /**
   * Scales n1*n2*n3 real numbers in the specified array by 1/nfft. 
   * The inverse of a real-to-complex FFT is a complex-to-real FFT 
   * (with opposite sign) followed by this scaling.
   * @param n1 the 1st dimension of the array rx.
   * @param n2 the 2nd dimension of the array rx.
   * @param n3 the 3rd dimension of the array rx.
   * @param rx the input/output array[n3][n2][n1].
   */
  public void scale(int n1, int n2, int n3, float[][][] rx) {
    for (int i3=0; i3<n3; ++i3)
      scale(n1,n2,rx[i3]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nfft; // FFT length (number of real numbers to transform)

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
