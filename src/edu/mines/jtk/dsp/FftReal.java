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
 * real numbers yields nfft/2+1 complex numbers. (The imaginary parts of 
 * the first and last complex numbers are always zero.) For real-to-complex 
 * and complex-to-real transforms, nfft is always an even number.
 * <p>
 * Complex numbers are packed into arrays of floats as 
 * [real_0, imag_0, real_1, imag_1, ...]. Here, real_k and imag_k correspond 
 * to the real and imaginary parts of the complex number with index k. 
 * <p>
 * When input and output arrays are the same array, transforms are performed 
 * in-place. For example, an input array rx[nfft] of nfft real numbers may be 
 * the same as an output array cy[nfft+2] of nfft/2+1 complex numbers. By
 * "the same array", we mean that rx==cy. In this case, both rx.length and
 * cy.length equal nfft+2. When we write rx[nfft] (here and below), we imply 
 * that only the first nfft floats in the input array rx are accessed.
 * <p>
 * Transforms may be performed for each dimension of a multi-dimensional 
 * array. For example, we may transform the 1st dimension of an input array 
 * rx[n2][nfft] of n2*nfft real numbers to an output array cy[n2][nfft+2] of 
 * n2*(nfft/2+1) complex numbers. Or, we may transform the 2nd dimension of 
 * an input array rx[nfft][n1] of nfft*n1 real numbers to a complex array 
 * cy[nfft/2+1][2*n1] of (nfft/2+1)*n1 complex numbers. In either case, the
 * input array rx and the output array cy may be the same array.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class FftReal {

  /**
   * Constructs a new FFT, with specified length. Valid FFT lengths can 
   * be obtained by calling the methods {@link #nfftSmall(int)} and 
   * {@link #nfftFast(int)}. Alternatively, the methods {@link #small(int)} 
   * and {@link #fast(int)} return an FFT with valid length.
   * @param nfft the FFT length, which must be valid.
   */
  public FftReal(int nfft) {
    Check.argument(
      nfft%2==0 && Pfacc.nfftValid(nfft/2),
      "nfft="+nfft+" is valid FFT length");
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
   * Computes a real-to-complex fast Fourier transform.
   * Transforms an input array rx[nfft] of nfft real numbers to an 
   * output array cy[nfft+2] of nfft/2+1 complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param rx the input array.
   * @param cy the output array.
   */
  public void realToComplex(int sign, float[] rx, float[] cy) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(rx.length>=_nfft,"rx.length is valid");
    Check.argument(cy.length>=_nfft+2,"cy.length is valid");
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
   * Transforms an input array cx[nfft+2] of nfft/2+1 complex numbers 
   * to an output array ry[nfft] of nfft real numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param cx the input array.
   * @param ry the output array.
   */
  public void complexToReal(int sign, float[] cx, float[] ry) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(cx.length>=_nfft+2,"cy.length is valid");
    Check.argument(ry.length>=_nfft,"rx.length is valid");
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
   * Transforms an input array rx[n2][nfft] of n2*nfft real numbers to 
   * an output array cy[n2][nfft+2] of n2*(nfft/2+1) complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n2 the 2nd dimension of arrays.
   * @param rx the input array.
   * @param cy the output array.
   */
  public void realToComplex1(int sign, int n2, float[][] rx, float[][] cy) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(rx.length>=n2,"rx.length is valid");
    Check.argument(cy.length>=n2,"cy.length is valid");
    for (int i2=0; i2<n2; ++i2) {
      Check.argument(rx[i2].length>=_nfft,"rx[i2].length is valid");
      Check.argument(cy[i2].length>=_nfft+2,"cy[i2].length is valid");
      realToComplex(sign,rx[i2],cy[i2]);
    }
  }

  /**
   * Computes a complex-to-real dimension-1 fast Fourier transform. 
   * Transforms an input array cx[n2][nfft+2] of n2*(nfft/2+1) complex 
   * numbers to an output array ry[n2][nfft] of n2*nfft real numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n2 the 2nd dimension of arrays.
   * @param cx the input array.
   * @param ry the output array.
   */
  public void complexToReal1(int sign, int n2, float[][] cx, float[][] ry) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(cx.length>=n2,"cx.length is valid");
    Check.argument(ry.length>=n2,"ry.length is valid");
    for (int i2=0; i2<n2; ++i2) {
      Check.argument(cx[i2].length>=_nfft+2,"cx[i2].length is valid");
      Check.argument(ry[i2].length>=_nfft,"ry[i2].length is valid");
      complexToReal(sign,cx[i2],ry[i2]);
    }
  }

  /**
   * Computes a real-to-complex dimension-2 fast Fourier transform. 
   * Transforms an input array rx[nfft][n1] of nfft*n1 real numbers to an 
   * output array cy[nfft/2+1][2*n1] of (nfft/2+1)*n1 complex numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n1 the 1st dimension of arrays.
   * @param rx the input array.
   * @param cy the output array.
   */
  public void realToComplex2(int sign, int n1, float[][] rx, float[][] cy) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(rx.length>=_nfft,"rx.length is valid");
    Check.argument(cy.length>=_nfft/2+1,"cy.length is valid");
    for (int i2=0,j2=0; j2<_nfft; ++i2,j2+=2) {
      Check.argument(rx[j2  ].length>=n1,"rx[i2].length is valid");
      Check.argument(rx[j2+1].length>=n1,"rx[i2].length is valid");
      Check.argument(cy[i2].length>=2*n1,"cy[i2].length is valid");
      float[] rxj2r= rx[j2  ];
      float[] rxj2i= rx[j2+1];
      float[] cyi2 = cy[i2];
      for (int i1=0,j1=0; i1<n1; ++i1,j1+=2) {
        cyi2[j1  ] = 0.5f*rxj2r[i1];
        cyi2[j1+1] = 0.5f*rxj2i[i1];
      }
    }
    Pfacc.transform2a(sign,n1,_nfft/2,cy);
    float[] cyj2 = cy[0];
    float[] cyk2 = cy[_nfft/2];
    for (int i1=0; i1<n1; ++i1) {
      cyk2[i1  ] = 2.0f*(cyj2[i1]-cyj2[i1+1]);
      cyj2[i1  ] = 2.0f*(cyj2[i1]+cyj2[i1+1]);
      cyk2[i1+1] = 0.0f;
      cyj2[i1+1] = 0.0f;
    }
    double theta = sign*2.0*PI/_nfft;
    double wt = sin(0.5*theta);
    double wpr = -2.0*wt*wt; // = cos(theta)-1, with less rounding error
    double wpi = sin(theta); // = sin(theta)
    double wr = 1.0+wpr;
    double wi = wpi;
    for (int j2=1,k2=_nfft/2-1; j2<=k2; ++j2,--k2) {
      cyj2 = cy[j2];
      cyk2 = cy[k2];
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
   * Transforms an input array cx[nfft/2+1][2*n1] of (nfft/2+1)*n1 complex
   * numbers to an output array ry[nfft][n1] of nfft*n1 real numbers.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n1 the 1st dimension of arrays.
   * @param cx the input array.
   * @param ry the output array.
   */
  public void complexToReal2(int sign, int n1, float[][] cx, float[][] ry) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(cx.length>=_nfft/2+1,"cx.length is valid");
    Check.argument(ry.length>=_nfft,"ry.length is valid");
    for (int i2=_nfft/2-1,j2=_nfft-2; j2>0; --i2,j2-=2) {
      Check.argument(cx[i2].length>=2*n1,"cx[i2].length is valid");
      Check.argument(ry[j2  ].length>=n1,"ry[i2].length is valid");
      Check.argument(ry[j2+1].length>=n1,"ry[i2].length is valid");
      float[] cxi2 = cx[i2];
      float[] ryj2r = ry[j2  ];
      float[] ryj2i = ry[j2+1];
      for (int i1=0,j1=0; i1<n1; ++i1,j1+=2) {
        ryj2r[i1] = cxi2[j1  ];
        ryj2i[i1] = cxi2[j1+1];
      }
    }
    float[] cxzero = cx[0];
    float[] cxlast = cx[_nfft/2];
    float[] ry0 = ry[0];
    float[] ry1 = ry[1];
    Check.argument(cxzero.length>=2*n1,"cx[i2].length is valid");
    Check.argument(cxlast.length>=2*n1,"cx[i2].length is valid");
    Check.argument(ry0.length>=n1,"ry[0].length is valid");
    Check.argument(ry1.length>=n1,"ry[1].length is valid");
    for (int i1=0,j1=0; i1<n1; ++i1,j1+=2) {
      ry1[i1] = cxzero[j1]-cxlast[j1];
      ry0[i1] = cxzero[j1]+cxlast[j1];
    }
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
    Pfacc.transform2b(sign,n1,_nfft/2,ry);
  }

  /**
   * Scales n1 real numbers in the specified array by 1/nfft. The 
   * inverse of a real-to-complex FFT is a complex-to-real FFT (with 
   * opposite sign) followed by this scaling.
   * @param n1 1st (only) dimension of the array rx.
   * @param rx the input/output array[n1].
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
    return nfftSmall(n); // TODO: implement
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nfft; // FFT length (number of real numbers to transform)
}
