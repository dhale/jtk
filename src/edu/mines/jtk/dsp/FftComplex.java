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

  public static void main(String[] args) {
    int n = 18;
    FftComplex fft = FftComplex.small(n);
    int nfft = fft.getNfft();
    float[] ca = new float[2*nfft];
    ca[2] = 1.0f;
    pfacc(1,nfft,ca);
    for (int i=0,j=0; i<nfft; ++i,j+=2)
      System.out.println("("+ca[j]+","+ca[j+1]+")");
  }

  /**
   * Constructs a new FFT, with specified length. Valid FFT lengths can 
   * be obtained by calling the methods {@link #nfftSmall(int)} and 
   * {@link #nfftFast(int)}. Alternatively, the methods {@link #small(int)} 
   * and {@link #fast(int)} return an FFT with valid length.
   * @param nfft the FFT length, which must be valid.
   */
  public FftComplex(int nfft) {
    boolean valid = Array.binarySearch(_ntable,nfft)>=0;
    Check.argument(valid,"nfft="+nfft+" is valid FFT length");
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
   * Computes a complex-to-complex fast Fourier transform. Transforms nfft
   * complex numbers to nfft complex numbers. If the input and output arrays
   * are the same array, the transform will be performed in-place.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param cx the input array; cx.length must not be less than 2*nfft.
   * @param cy the output array; cy.length must not be less than 2*nfft.
   */
  public void complexToComplex(int sign, float[] cx, float[] cy) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(cx.length>=2*_nfft,"cx.length is sufficient");
    Check.argument(cy.length>=2*_nfft,"cy.length is sufficient");
    if (cx!=cy)
      System.arraycopy(cx,0,cy,0,_nfft);
    pfacc(sign,_nfft,cy);
  }

  /**
   * Computes a complex-to-complex fast Fourier transform. Transforms nfft
   * complex numbers to nfft complex numbers. If the input and output arrays
   * are the same array, the transform will be performed in-place.
   * @param sign the sign (1 or -1) of the exponent used in the FFT.
   * @param n1 the number of transforms; 1st dimension of arrays.
   * @param cx the input array; cx.length must not be less than nfft.
   * @param cy the output array; cy.length must not be less than nfft.
   */
  public void complexToComplex2(int sign, int n1, float[][] cx, float[][] cy) {
    Check.argument(sign==1 || sign==-1,"sign equals 1 or -1");
    Check.argument(cx.length>=_nfft,"cx.length is sufficient");
    Check.argument(cy.length>=_nfft,"cy.length is sufficient");
    if (cx!=cy) {
      for (int i2=0; i2<_nfft; ++i2)
        System.arraycopy(cx[i2],0,cy[i2],0,2*n1);
    }
    pfacc2(sign,n1,_nfft,cy);
  }

  /**
   * Scales n1 complex numbers in the specified array by 1/nfft. 
   * The inverse of a complex-to-complex FFT is a complex-to-complex 
   * FFT (with opposite sign) followed by this scaling.
   * @param n1 1st (only) dimension of the array cx.
   * @param cx the input/output array
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
   * @param cx the input/output array.
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
    int itable = Array.binarySearch(_ntable,n);
    if (itable<0) itable = -(itable+1);
    return _ntable[itable];
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
    return nfftSmall(n);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _nfft; // FFT length (number of complex numbers to transform)

  /**
   * Prime-factor complex-to-complex FFT. The FFT length nfft must be composed 
   * of mutually prime factors from the set {2,3,4,5,7,8,9,11,13,16}. This
   * restriction implies that n cannot exceed 720720 = 5*7*9*11*13*16.
   * <p>
   * References: 
   * <ul><li>
   * Temperton, C., 1985, Implementation of a self-sorting in-place prime 
   * factor fft algorithm:  Journal of Computational Physics, v. 58, 
   * p. 283-299.
   * </li><li>
   * Temperton, C., 1988, A new set of minimum-add rotated rotated dft 
   * modules: Journal of Computational Physics, v. 75, p. 190-198.
   * </li></ul>
   * @param sign the sign of the exponent in the Fourier transform.
   * @param nfft the FFT length.
   * @param z array[2*nfft] of at least nfft packed complex numbers.
   */
  private static void pfacc(int sign, int nfft, float[] z) {

    // What is left of n after dividing by factors.
    int nleft = nfft;

    // Loop over all possible factors, from largest to smallest.
    for (int jfac=0; jfac<NFAC; ++jfac) {

      // Skip the current factor, if not a mutually prime factor of n
      int ifac = _kfac[jfac];
      int ndiv = nleft/ifac;
      if (ndiv*ifac!=nleft)
        continue;

      // What is left of n (nleft), and n divided by the current factor (m).
      nleft = ndiv;
      int m = nfft/ifac;
 
      // Rotation factor mu and stride mm.
      int mu = 0;
      int mm = 0;
      for (int kfac=1; kfac<=ifac && mm%ifac!=1; ++kfac) {
        mu = kfac;
        mm = kfac*m;
      }
      if (sign<0)
        mu = ifac-mu;

      // Array stride, bound, and indices.
      int jinc = 2*mm;
      int jmax = 2*nfft;
      int j0 = 0;
      int j1 = j0+jinc;

      // Factor 2.
      if (ifac==2) {
        pfa2(z,mu,m,j0,j1);
        continue;
      }
      int j2 = (j1+jinc)%jmax;

      // Factor 3.
      if (ifac==3) {
        pfa3(z,mu,m,j0,j1,j2);
        continue;
      }
      int j3 = (j2+jinc)%jmax;

      // Factor 4.
      if (ifac==4) {
        pfa4(z,mu,m,j0,j1,j2,j3);
        continue;
      }
      int j4 = (j3+jinc)%jmax;

      // Factor 5.
      if (ifac==5) {
        pfa5(z,mu,m,j0,j1,j2,j3,j4);
        continue;
      }
      int j5 = (j4+jinc)%jmax;
      int j6 = (j5+jinc)%jmax;

      // Factor 7.
      if (ifac==7) {
        pfa7(z,mu,m,j0,j1,j2,j3,j4,j5,j6);
        continue;
      }
      int j7 = (j6+jinc)%jmax;

      // Factor 8.
      if (ifac==8) {
        pfa8(z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7);
        continue;
      }
      int j8 = (j7+jinc)%jmax;

      // Factor 9.
      if (ifac==9) {
        pfa9(z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7,j8);
        continue;
      }
      int j9 = (j8+jinc)%jmax;
      int j10 = (j9+jinc)%jmax;

      // Factor 11.
      if (ifac==11) {
        pfa11(z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7,j8,j9,j10);
        continue;
      }
      int j11 = (j10+jinc)%jmax;
      int j12 = (j11+jinc)%jmax;

      // Factor 13.
      if (ifac==13) {
        pfa13(z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7,j8,j9,j10,j11,j12);
        continue;
      }
      int j13 = (j12+jinc)%jmax;
      int j14 = (j13+jinc)%jmax;
      int j15 = (j14+jinc)%jmax;

      // Factor 16.
      if (ifac==16) {
        pfa16(z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7,j8,j9,j10,j11,j12,j13,j14,j15);
        continue;
      }
    }
  }
  private static void pfa2(float[] z, int mu, int m,
    int j0, int j1)
  {
    for (int i=0; i<m; ++i) {
      float t1r = z[j0  ]-z[j1  ];
      float t1i = z[j0+1]-z[j1+1];
      z[j0  ] = z[j0  ]+z[j1  ];
      z[j0+1] = z[j0+1]+z[j1+1];
      z[j1  ] = t1r;
      z[j1+1] = t1i;
      int jt = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }
  private static void pfa3(float[] z, int mu, int m,
    int j0, int j1, int j2)
  {
    float c1;
    if (mu==1) {
      c1 =  P866;
    } else {
      c1 = -P866;
    }
    for (int i=0; i<m; ++i) {
      float t1r = z[j1  ]+z[j2  ];
      float t1i = z[j1+1]+z[j2+1];
      float y1r = z[j0  ]-0.5f*t1r;
      float y1i = z[j0+1]-0.5f*t1i;
      float y2r = c1*(z[j1  ]-z[j2  ]);
      float y2i = c1*(z[j1+1]-z[j2+1]);
      z[j0  ] = z[j0  ]+t1r;
      z[j0+1] = z[j0+1]+t1i;
      z[j1  ] = y1r-y2i;
      z[j1+1] = y1i+y2r;
      z[j2  ] = y1r+y2i;
      z[j2+1] = y1i-y2r;
      int jt = j2+2;
      j2 = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }
  private static void pfa4(float[] z, int mu, int m,
    int j0, int j1, int j2, int j3)
  {
    float c1;
    if (mu==1) {
      c1 =  PONE;
    } else {
      c1 = -PONE;
    }
    for (int i=0; i<m; ++i) {
      float t1r = z[j0  ]+z[j2  ];
      float t1i = z[j0+1]+z[j2+1];
      float t2r = z[j1  ]+z[j3  ];
      float t2i = z[j1+1]+z[j3+1];
      float y1r = z[j0  ]-z[j2  ];
      float y1i = z[j0+1]-z[j2+1];
      float y3r = c1*(z[j1  ]-z[j3  ]);
      float y3i = c1*(z[j1+1]-z[j3+1]);
      z[j0  ] = t1r+t2r;
      z[j0+1] = t1i+t2i;
      z[j1  ] = y1r-y3i;
      z[j1+1] = y1i+y3r;
      z[j2  ] = t1r-t2r;
      z[j2+1] = t1i-t2i;
      z[j3  ] = y1r+y3i;
      z[j3+1] = y1i-y3r;
      int jt = j3+2;
      j3 = j2+2;
      j2 = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }
  private static void pfa5(float[] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4)
  {
    float c1,c2,c3;
    if (mu==1) {
      c1 =  P559;
      c2 =  P951;
      c3 =  P587;
    } else if (mu==2) {
      c1 = -P559;
      c2 =  P587;
      c3 = -P951;
    } else if (mu==3) {
      c1 = -P559;
      c2 = -P587;
      c3 =  P951;
    } else { 
      c1 =  P559;
      c2 = -P951;
      c3 = -P587;
    }
    for (int i=0; i<m; ++i) {
      float t1r = z[j1  ]+z[j4  ];
      float t1i = z[j1+1]+z[j4+1];
      float t2r = z[j2  ]+z[j3  ];
      float t2i = z[j2+1]+z[j3+1];
      float t3r = z[j1  ]-z[j4  ];
      float t3i = z[j1+1]-z[j4+1];
      float t4r = z[j2  ]-z[j3  ];
      float t4i = z[j2+1]-z[j3+1];
      float t5r = t1r+t2r;
      float t5i = t1i+t2i;
      float t6r = c1*(t1r-t2r);
      float t6i = c1*(t1i-t2i);
      float t7r = z[j0  ]-0.25f*t5r;
      float t7i = z[j0+1]-0.25f*t5i;
      float y1r = t7r+t6r;
      float y1i = t7i+t6i;
      float y2r = t7r-t6r;
      float y2i = t7i-t6i;
      float y3r = c3*t3r-c2*t4r;
      float y3i = c3*t3i-c2*t4i;
      float y4r = c2*t3r+c3*t4r;
      float y4i = c2*t3i+c3*t4i;
      z[j0  ] = z[j0  ]+t5r;
      z[j0+1] = z[j0+1]+t5i;
      z[j1  ] = y1r-y4i;
      z[j1+1] = y1i+y4r;
      z[j2  ] = y2r-y3i;
      z[j2+1] = y2i+y3r;
      z[j3  ] = y2r+y3i;
      z[j3+1] = y2i-y3r;
      z[j4  ] = y1r+y4i;
      z[j4+1] = y1i-y4r;
      int jt = j4+2;
      j4 = j3+2;
      j3 = j2+2;
      j2 = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }
  private static void pfa7(float[] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6)
  {
    float c1,c2,c3,c4,c5,c6;
    if (mu==1) {
      c1 =  P623;
      c2 = -P222;
      c3 = -P900;
      c4 =  P781;
      c5 =  P974;
      c6 =  P433;
    } else if (mu==2) {
      c1 = -P222;
      c2 = -P900;
      c3 =  P623;
      c4 =  P974;
      c5 = -P433;
      c6 = -P781;
    } else if (mu==3) {
      c1 = -P900;
      c2 =  P623;
      c3 = -P222;
      c4 =  P433;
      c5 = -P781;
      c6 =  P974;
    } else if (mu==4) {
      c1 = -P900;
      c2 =  P623;
      c3 = -P222;
      c4 = -P433;
      c5 =  P781;
      c6 = -P974;
    } else if (mu==5) {
      c1 = -P222;
      c2 = -P900;
      c3 =  P623;
      c4 = -P974;
      c5 =  P433;
      c6 =  P781;
    } else {
      c1 =  P623;
      c2 = -P222;
      c3 = -P900;
      c4 = -P781;
      c5 = -P974;
      c6 = -P433;
    }
    for (int i=0; i<m; ++i) {
      float t1r = z[j1  ]+z[j6  ];
      float t1i = z[j1+1]+z[j6+1];
      float t2r = z[j2  ]+z[j5  ];
      float t2i = z[j2+1]+z[j5+1];
      float t3r = z[j3  ]+z[j4  ];
      float t3i = z[j3+1]+z[j4+1];
      float t4r = z[j1  ]-z[j6  ];
      float t4i = z[j1+1]-z[j6+1];
      float t5r = z[j2  ]-z[j5  ];
      float t5i = z[j2+1]-z[j5+1];
      float t6r = z[j3  ]-z[j4  ];
      float t6i = z[j3+1]-z[j4+1];
      float t7r = z[j0  ]-0.5f*t3r;
      float t7i = z[j0+1]-0.5f*t3i;
      float t8r = t1r-t3r;
      float t8i = t1i-t3i;
      float t9r = t2r-t3r;
      float t9i = t2i-t3i;
      float y1r = t7r+c1*t8r+c2*t9r;
      float y1i = t7i+c1*t8i+c2*t9i;
      float y2r = t7r+c2*t8r+c3*t9r;
      float y2i = t7i+c2*t8i+c3*t9i;
      float y3r = t7r+c3*t8r+c1*t9r;
      float y3i = t7i+c3*t8i+c1*t9i;
      float y4r = c6*t4r-c4*t5r+c5*t6r;
      float y4i = c6*t4i-c4*t5i+c5*t6i;
      float y5r = c5*t4r-c6*t5r-c4*t6r;
      float y5i = c5*t4i-c6*t5i-c4*t6i;
      float y6r = c4*t4r+c5*t5r+c6*t6r;
      float y6i = c4*t4i+c5*t5i+c6*t6i;
      z[j0  ] = z[j0  ]+t1r+t2r+t3r;
      z[j0+1] = z[j0+1]+t1i+t2i+t3i;
      z[j1  ] = y1r-y6i;
      z[j1+1] = y1i+y6r;
      z[j2  ] = y2r-y5i;
      z[j2+1] = y2i+y5r;
      z[j3  ] = y3r-y4i;
      z[j3+1] = y3i+y4r;
      z[j4  ] = y3r+y4i;
      z[j4+1] = y3i-y4r;
      z[j5  ] = y2r+y5i;
      z[j5+1] = y2i-y5r;
      z[j6  ] = y1r+y6i;
      z[j6+1] = y1i-y6r;
      int jt = j6+2;
      j6 = j5+2;
      j5 = j4+2;
      j4 = j3+2;
      j3 = j2+2;
      j2 = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }
  private static void pfa8(float[] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6, int j7)
  {
    float c1,c2,c3;
    if (mu==1) {
      c1 =  PONE;
      c2 =  P707;
    } else if (mu==3) {
      c1 = -PONE;
      c2 = -P707;
    } else if (mu==5) {
      c1 =  PONE;
      c2 = -P707;
    } else {
      c1 = -PONE;
      c2 =  P707;
    }
    c3 = c1*c2;
    for (int i=0; i<m; ++i) {
      float t1r = z[j0  ]+z[j4  ];
      float t1i = z[j0+1]+z[j4+1];
      float t2r = z[j0  ]-z[j4  ];
      float t2i = z[j0+1]-z[j4+1];
      float t3r = z[j1  ]+z[j5  ];
      float t3i = z[j1+1]+z[j5+1];
      float t4r = z[j1  ]-z[j5  ];
      float t4i = z[j1+1]-z[j5+1];
      float t5r = z[j2  ]+z[j6  ];
      float t5i = z[j2+1]+z[j6+1];
      float t6r = c1*(z[j2  ]-z[j6  ]);
      float t6i = c1*(z[j2+1]-z[j6+1]);
      float t7r = z[j3  ]+z[j7  ];
      float t7i = z[j3+1]+z[j7+1];
      float t8r = z[j3  ]-z[j7  ];
      float t8i = z[j3+1]-z[j7+1];
      float t9r = t1r+t5r;
      float t9i = t1i+t5i;
      float t10r = t3r+t7r;
      float t10i = t3i+t7i;
      float t11r = c2*(t4r-t8r);
      float t11i = c2*(t4i-t8i);
      float t12r = c3*(t4r+t8r);
      float t12i = c3*(t4i+t8i);
      float y1r = t2r+t11r;
      float y1i = t2i+t11i;
      float y2r = t1r-t5r;
      float y2i = t1i-t5i;
      float y3r = t2r-t11r;
      float y3i = t2i-t11i;
      float y5r = t12r-t6r;
      float y5i = t12i-t6i;
      float y6r = c1*(t3r-t7r);
      float y6i = c1*(t3i-t7i);
      float y7r = t12r+t6r;
      float y7i = t12i+t6i;
      z[j0  ] = t9r+t10r;
      z[j0+1] = t9i+t10i;
      z[j1  ] = y1r-y7i;
      z[j1+1] = y1i+y7r;
      z[j2  ] = y2r-y6i;
      z[j2+1] = y2i+y6r;
      z[j3  ] = y3r-y5i;
      z[j3+1] = y3i+y5r;
      z[j4  ] = t9r-t10r;
      z[j4+1] = t9i-t10i;
      z[j5  ] = y3r+y5i;
      z[j5+1] = y3i-y5r;
      z[j6  ] = y2r+y6i;
      z[j6+1] = y2i-y6r;
      z[j7  ] = y1r+y7i;
      z[j7+1] = y1i-y7r;
      int jt = j7+2;
      j7 = j6+2;
      j6 = j5+2;
      j5 = j4+2;
      j4 = j3+2;
      j3 = j2+2;
      j2 = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }
  private static void pfa9(float[] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6, int j7, int j8)
  {
    float c1,c2,c3,c4,c5,c6,c7,c8,c9;
    if (mu==1) {
      c1 =  P866;
      c2 =  P766;
      c3 =  P642;
      c4 =  P173;
      c5 =  P984;
    } else if (mu==2) {
      c1 = -P866;
      c2 =  P173;
      c3 =  P984;
      c4 = -P939;
      c5 =  P342;
    } else if (mu==4) {
      c1 =  P866;
      c2 = -P939;
      c3 =  P342;
      c4 =  P766;
      c5 = -P642;
    } else if (mu==5) {
      c1 = -P866;
      c2 = -P939;
      c3 = -P342;
      c4 =  P766;
      c5 =  P642;
    } else if (mu==7) {
      c1 =  P866;
      c2 =  P173;
      c3 = -P984;
      c4 = -P939;
      c5 = -P342;
    } else {
      c1 = -P866;
      c2 =  P766;
      c3 = -P642;
      c4 =  P173;
      c5 = -P984;
    }
    c6 = c1*c2;
    c7 = c1*c3;
    c8 = c1*c4;
    c9 = c1*c5;
    for (int i=0; i<m; ++i) {
      float t1r  = z[j3  ]+z[j6  ];
      float t1i  = z[j3+1]+z[j6+1];
      float t2r  = z[j0  ]-0.5f*t1r;
      float t2i  = z[j0+1]-0.5f*t1i;
      float t3r  = c1*(z[j3  ]-z[j6  ]);
      float t3i  = c1*(z[j3+1]-z[j6+1]);
      float t4r  = z[j0  ]+t1r;
      float t4i  = z[j0+1]+t1i;
      float t5r  = z[j4  ]+z[j7  ];
      float t5i  = z[j4+1]+z[j7+1];
      float t6r  = z[j1  ]-0.5f*t5r;
      float t6i  = z[j1+1]-0.5f*t5i;
      float t7r  = z[j4  ]-z[j7  ];
      float t7i  = z[j4+1]-z[j7+1];
      float t8r  = z[j1  ]+t5r;
      float t8i  = z[j1+1]+t5i;
      float t9r  = z[j2  ]+z[j5  ];
      float t9i  = z[j2+1]+z[j5+1];
      float t10r = z[j8  ]-0.5f*t9r;
      float t10i = z[j8+1]-0.5f*t9i;
      float t11r = z[j2  ]-z[j5  ];
      float t11i = z[j2+1]-z[j5+1];
      float t12r = z[j8  ]+t9r;
      float t12i = z[j8+1]+t9i;
      float t13r = t8r+t12r;
      float t13i = t8i+t12i;
      float t14r = t6r+t10r;
      float t14i = t6i+t10i;
      float t15r = t6r-t10r;
      float t15i = t6i-t10i;
      float t16r = t7r+t11r;
      float t16i = t7i+t11i;
      float t17r = t7r-t11r;
      float t17i = t7i-t11i;
      float t18r = c2*t14r-c7*t17r;
      float t18i = c2*t14i-c7*t17i;
      float t19r = c4*t14r+c9*t17r;
      float t19i = c4*t14i+c9*t17i;
      float t20r = c3*t15r+c6*t16r;
      float t20i = c3*t15i+c6*t16i;
      float t21r = c5*t15r-c8*t16r;
      float t21i = c5*t15i-c8*t16i;
      float t22r = t18r+t19r;
      float t22i = t18i+t19i;
      float t23r = t20r-t21r;
      float t23i = t20i-t21i;
      float y1r  = t2r+t18r;
      float y1i  = t2i+t18i;
      float y2r  = t2r+t19r;
      float y2i  = t2i+t19i;
      float y3r  = t4r-0.5f*t13r;
      float y3i  = t4i-0.5f*t13i;
      float y4r  = t2r-t22r;
      float y4i  = t2i-t22i;
      float y5r  = t3r-t23r;
      float y5i  = t3i-t23i;
      float y6r  = c1*(t8r-t12r);
      float y6i  = c1*(t8i-t12i);
      float y7r  = t21r-t3r;
      float y7i  = t21i-t3i;
      float y8r  = t3r+t20r;
      float y8i  = t3i+t20i;
      z[j0  ] = t4r+t13r;
      z[j0+1] = t4i+t13i;
      z[j1  ] = y1r-y8i;
      z[j1+1] = y1i+y8r;
      z[j2  ] = y2r-y7i;
      z[j2+1] = y2i+y7r;
      z[j3  ] = y3r-y6i;
      z[j3+1] = y3i+y6r;
      z[j4  ] = y4r-y5i;
      z[j4+1] = y4i+y5r;
      z[j5  ] = y4r+y5i;
      z[j5+1] = y4i-y5r;
      z[j6  ] = y3r+y6i;
      z[j6+1] = y3i-y6r;
      z[j7  ] = y2r+y7i;
      z[j7+1] = y2i-y7r;
      z[j8  ] = y1r+y8i;
      z[j8+1] = y1i-y8r;
      int jt = j8+2;
      j8 = j7+2;
      j7 = j6+2;
      j6 = j5+2;
      j5 = j4+2;
      j4 = j3+2;
      j3 = j2+2;
      j2 = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }
  private static void pfa11(float[] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, 
    int j6, int j7, int j8, int j9, int j10)
  {
    float c1,c2,c3,c4,c5,c6,c7,c8,c9,c10;
    if (mu==1) {
      c1  =  P841;
      c2  =  P415;
      c3  = -P142;
      c4  = -P654;
      c5  = -P959;
      c6  =  P540;
      c7  =  P909;
      c8  =  P989;
      c9  =  P755;
      c10 =  P281;
    } else if (mu==2) {
      c1  =  P415;
      c2  = -P654;
      c3  = -P959;
      c4  = -P142;
      c5  =  P841;
      c6  =  P909;
      c7  =  P755;
      c8  = -P281;
      c9  = -P989;
      c10 = -P540;
    } else if (mu==3) {
      c1  = -P142;
      c2  = -P959;
      c3  =  P415;
      c4  =  P841;
      c5  = -P654;
      c6  =  P989;
      c7  = -P281;
      c8  = -P909;
      c9  =  P540;
      c10 =  P755;
    } else if (mu==4) {
      c1  = -P654;
      c2  = -P142;
      c3  =  P841;
      c4  = -P959;
      c5  =  P415;
      c6  =  P755;
      c7  = -P989;
      c8  =  P540;
      c9  =  P281;
      c10 = -P909;
    } else if (mu==5) {
      c1  = -P959;
      c2  =  P841;
      c3  = -P654;
      c4  =  P415;
      c5  = -P142;
      c6  =  P281;
      c7  = -P540;
      c8  =  P755;
      c9  = -P909;
      c10 =  P989;
    } else if (mu==6) {
      c1  = -P959;
      c2  =  P841;
      c3  = -P654;
      c4  =  P415;
      c5  = -P142;
      c6  = -P281;
      c7  =  P540;
      c8  = -P755;
      c9  =  P909;
      c10 = -P989;
    } else if (mu==7) {
      c1  = -P654;
      c2  = -P142;
      c3  =  P841;
      c4  = -P959;
      c5  =  P415;
      c6  = -P755;
      c7  =  P989;
      c8  = -P540;
      c9  = -P281;
      c10 =  P909;
    } else if (mu==8) {
      c1  = -P142;
      c2  = -P959;
      c3  =  P415;
      c4  =  P841;
      c5  = -P654;
      c6  = -P989;
      c7  =  P281;
      c8  =  P909;
      c9  = -P540;
      c10 = -P755;
    } else if (mu==9) {
      c1  =  P415;
      c2  = -P654;
      c3  = -P959;
      c4  = -P142;
      c5  =  P841;
      c6  = -P909;
      c7  = -P755;
      c8  =  P281;
      c9  =  P989;
      c10 =  P540;
    } else {
      c1  =  P841;
      c2  =  P415;
      c3  = -P142;
      c4  = -P654;
      c5  = -P959;
      c6  = -P540;
      c7  = -P909;
      c8  = -P989;
      c9  = -P755;
      c10 = -P281;
    }
    for (int i=0; i<m; ++i) {
      float t1r  = z[j1  ]+z[j10  ];
      float t1i  = z[j1+1]+z[j10+1];
      float t2r  = z[j2  ]+z[j9  ];
      float t2i  = z[j2+1]+z[j9+1];
      float t3r  = z[j3  ]+z[j8  ];
      float t3i  = z[j3+1]+z[j8+1];
      float t4r  = z[j4  ]+z[j7  ];
      float t4i  = z[j4+1]+z[j7+1];
      float t5r  = z[j5  ]+z[j6  ];
      float t5i  = z[j5+1]+z[j6+1];
      float t6r  = z[j1  ]-z[j10  ];
      float t6i  = z[j1+1]-z[j10+1];
      float t7r  = z[j2  ]-z[j9  ];
      float t7i  = z[j2+1]-z[j9+1];
      float t8r  = z[j3  ]-z[j8  ];
      float t8i  = z[j3+1]-z[j8+1];
      float t9r  = z[j4  ]-z[j7  ];
      float t9i  = z[j4+1]-z[j7+1];
      float t10r = z[j5  ]-z[j6  ];
      float t10i = z[j5+1]-z[j6+1];
      float t11r = z[j0  ]-0.5f*t5r;
      float t11i = z[j0+1]-0.5f*t5i;
      float t12r = t1r-t5r;
      float t12i = t1i-t5i;
      float t13r = t2r-t5r;
      float t13i = t2i-t5i;
      float t14r = t3r-t5r;
      float t14i = t3i-t5i;
      float t15r = t4r-t5r;
      float t15i = t4i-t5i;
      float y1r  = t11r+c1*t12r+c2*t13r+c3*t14r+c4*t15r;
      float y1i  = t11i+c1*t12i+c2*t13i+c3*t14i+c4*t15i;
      float y2r  = t11r+c2*t12r+c4*t13r+c5*t14r+c3*t15r;
      float y2i  = t11i+c2*t12i+c4*t13i+c5*t14i+c3*t15i;
      float y3r  = t11r+c3*t12r+c5*t13r+c2*t14r+c1*t15r;
      float y3i  = t11i+c3*t12i+c5*t13i+c2*t14i+c1*t15i;
      float y4r  = t11r+c4*t12r+c3*t13r+c1*t14r+c5*t15r;
      float y4i  = t11i+c4*t12i+c3*t13i+c1*t14i+c5*t15i;
      float y5r  = t11r+c5*t12r+c1*t13r+c4*t14r+c2*t15r;
      float y5i  = t11i+c5*t12i+c1*t13i+c4*t14i+c2*t15i;
      float y6r  = c10*t6r-c6*t7r+c9*t8r-c7*t9r+c8*t10r;
      float y6i  = c10*t6i-c6*t7i+c9*t8i-c7*t9i+c8*t10i;
      float y7r  = c9*t6r-c8*t7r+c6*t8r+c10*t9r-c7*t10r;
      float y7i  = c9*t6i-c8*t7i+c6*t8i+c10*t9i-c7*t10i;
      float y8r  = c8*t6r-c10*t7r-c7*t8r+c6*t9r+c9*t10r;
      float y8i  = c8*t6i-c10*t7i-c7*t8i+c6*t9i+c9*t10i;
      float y9r  = c7*t6r+c9*t7r-c10*t8r-c8*t9r-c6*t10r;
      float y9i  = c7*t6i+c9*t7i-c10*t8i-c8*t9i-c6*t10i;
      float y10r = c6*t6r+c7*t7r+c8*t8r+c9*t9r+c10*t10r;
      float y10i = c6*t6i+c7*t7i+c8*t8i+c9*t9i+c10*t10i;
      z[j0  ]  = z[j0  ]+t1r+t2r+t3r+t4r+t5r;
      z[j0+1]  = z[j0+1]+t1i+t2i+t3i+t4i+t5i;
      z[j1  ]  = y1r-y10i;
      z[j1+1]  = y1i+y10r;
      z[j2  ]  = y2r-y9i;
      z[j2+1]  = y2i+y9r;
      z[j3  ]  = y3r-y8i;
      z[j3+1]  = y3i+y8r;
      z[j4  ]  = y4r-y7i;
      z[j4+1]  = y4i+y7r;
      z[j5  ]  = y5r-y6i;
      z[j5+1]  = y5i+y6r;
      z[j6  ]  = y5r+y6i;
      z[j6+1]  = y5i-y6r;
      z[j7  ]  = y4r+y7i;
      z[j7+1]  = y4i-y7r;
      z[j8  ]  = y3r+y8i;
      z[j8+1]  = y3i-y8r;
      z[j9  ]  = y2r+y9i;
      z[j9+1]  = y2i-y9r;
      z[j10  ] = y1r+y10i;
      z[j10+1] = y1i-y10r;
      int jt = j10+2;
      j10 = j9+2;
      j9 = j8+2;
      j8 = j7+2;
      j7 = j6+2;
      j6 = j5+2;
      j5 = j4+2;
      j4 = j3+2;
      j3 = j2+2;
      j2 = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }
  private static void pfa13(float[] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6, 
    int j7, int j8, int j9, int j10, int j11, int j12)
  {
    float c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12;
    if (mu==1) {
      c1  =  P885;
      c2  =  P568;
      c3  =  P120;
      c4  = -P354;
      c5  = -P748;
      c6  = -P970;
      c7  =  P464;
      c8  =  P822;
      c9  =  P992;
      c10 =  P935;
      c11 =  P663;
      c12 =  P239;
    } else if (mu==2) {
      c1  =  P568;
      c2  = -P354;
      c3  = -P970;
      c4  = -P748;
      c5  =  P120;
      c6  =  P885;
      c7  =  P822;
      c8  =  P935;
      c9  =  P239;
      c10 = -P663;
      c11 = -P992;
      c12 = -P464;
    } else if (mu==3) {
      c1  =  P120;
      c2  = -P970;
      c3  = -P354;
      c4  =  P885;
      c5  =  P568;
      c6  = -P748;
      c7  =  P992;
      c8  =  P239;
      c9  = -P935;
      c10 = -P464;
      c11 =  P822;
      c12 =  P663;
    } else if (mu==4) {
      c1  = -P354;
      c2  = -P748;
      c3  =  P885;
      c4  =  P120;
      c5  = -P970;
      c6  =  P568;
      c7  =  P935;
      c8  = -P663;
      c9  = -P464;
      c10 =  P992;
      c11 = -P239;
      c12 = -P822;
    } else if (mu==5) {
      c1  = -P748;
      c2  =  P120;
      c3  =  P568;
      c4  = -P970;
      c5  =  P885;
      c6  = -P354;
      c7  =  P663;
      c8  = -P992;
      c9  =  P822;
      c10 = -P239;
      c11 = -P464;
      c12 =  P935;
    } else if (mu==6) {
      c1  = -P970;
      c2  =  P885;
      c3  = -P748;
      c4  =  P568;
      c5  = -P354;
      c6  =  P120;
      c7  =  P239;
      c8  = -P464;
      c9  =  P663;
      c10 = -P822;
      c11 =  P935;
      c12 = -P992;
    } else if (mu==7) {
      c1  = -P970;
      c2  =  P885;
      c3  = -P748;
      c4  =  P568;
      c5  = -P354;
      c6  =  P120;
      c7  = -P239;
      c8  =  P464;
      c9  = -P663;
      c10 =  P822;
      c11 = -P935;
      c12 =  P992;
    } else if (mu==8) {
      c1  = -P748;
      c2  =  P120;
      c3  =  P568;
      c4  = -P970;
      c5  =  P885;
      c6  = -P354;
      c7  = -P663;
      c8  =  P992;
      c9  = -P822;
      c10 =  P239;
      c11 =  P464;
      c12 = -P935;
    } else if (mu==9) {
      c1  = -P354;
      c2  = -P748;
      c3  =  P885;
      c4  =  P120;
      c5  = -P970;
      c6  =  P568;
      c7  = -P935;
      c8  =  P663;
      c9  =  P464;
      c10 = -P992;
      c11 =  P239;
      c12 =  P822;
    } else if (mu==10) {
      c1  =  P120;
      c2  = -P970;
      c3  = -P354;
      c4  =  P885;
      c5  =  P568;
      c6  = -P748;
      c7  = -P992;
      c8  = -P239;
      c9  =  P935;
      c10 =  P464;
      c11 = -P822;
      c12 = -P663;
    } else if (mu==11) {
      c1  =  P568;
      c2  = -P354;
      c3  = -P970;
      c4  = -P748;
      c5  =  P120;
      c6  =  P885;
      c7  = -P822;
      c8  = -P935;
      c9  = -P239;
      c10 =  P663;
      c11 =  P992;
      c12 =  P464;
    } else {
      c1  =  P885;
      c2  =  P568;
      c3  =  P120;
      c4  = -P354;
      c5  = -P748;
      c6  = -P970;
      c7  = -P464;
      c8  = -P822;
      c9  = -P992;
      c10 = -P935;
      c11 = -P663;
      c12 = -P239;
    }
    for (int i=0; i<m; ++i) {
      float t1r  = z[j1  ]+z[j12  ];
      float t1i  = z[j1+1]+z[j12+1];
      float t2r  = z[j2  ]+z[j11  ];
      float t2i  = z[j2+1]+z[j11+1];
      float t3r  = z[j3  ]+z[j10  ];
      float t3i  = z[j3+1]+z[j10+1];
      float t4r  = z[j4  ]+z[j9  ];
      float t4i  = z[j4+1]+z[j9+1];
      float t5r  = z[j5  ]+z[j8  ];
      float t5i  = z[j5+1]+z[j8+1];
      float t6r  = z[j6  ]+z[j7  ];
      float t6i  = z[j6+1]+z[j7+1];
      float t7r  = z[j1  ]-z[j12  ];
      float t7i  = z[j1+1]-z[j12+1];
      float t8r  = z[j2  ]-z[j11  ];
      float t8i  = z[j2+1]-z[j11+1];
      float t9r  = z[j3  ]-z[j10  ];
      float t9i  = z[j3+1]-z[j10+1];
      float t10r = z[j4  ]-z[j9  ];
      float t10i = z[j4+1]-z[j9+1];
      float t11r = z[j5  ]-z[j8  ];
      float t11i = z[j5+1]-z[j8+1];
      float t12r = z[j6  ]-z[j7  ];
      float t12i = z[j6+1]-z[j7+1];
      float t13r = z[j0  ]-0.5f*t6r;
      float t13i = z[j0+1]-0.5f*t6i;
      float t14r = t1r-t6r;
      float t14i = t1i-t6i;
      float t15r = t2r-t6r;
      float t15i = t2i-t6i;
      float t16r = t3r-t6r;
      float t16i = t3i-t6i;
      float t17r = t4r-t6r;
      float t17i = t4i-t6i;
      float t18r = t5r-t6r;
      float t18i = t5i-t6i;
      float y1r  = t13r+c1*t14r+c2*t15r+c3*t16r+c4*t17r+c5*t18r;
      float y1i  = t13i+c1*t14i+c2*t15i+c3*t16i+c4*t17i+c5*t18i;
      float y2r  = t13r+c2*t14r+c4*t15r+c6*t16r+c5*t17r+c3*t18r;
      float y2i  = t13i+c2*t14i+c4*t15i+c6*t16i+c5*t17i+c3*t18i;
      float y3r  = t13r+c3*t14r+c6*t15r+c4*t16r+c1*t17r+c2*t18r;
      float y3i  = t13i+c3*t14i+c6*t15i+c4*t16i+c1*t17i+c2*t18i;
      float y4r  = t13r+c4*t14r+c5*t15r+c1*t16r+c3*t17r+c6*t18r;
      float y4i  = t13i+c4*t14i+c5*t15i+c1*t16i+c3*t17i+c6*t18i;
      float y5r  = t13r+c5*t14r+c3*t15r+c2*t16r+c6*t17r+c1*t18r;
      float y5i  = t13i+c5*t14i+c3*t15i+c2*t16i+c6*t17i+c1*t18i;
      float y6r  = t13r+c6*t14r+c1*t15r+c5*t16r+c2*t17r+c4*t18r;
      float y6i  = t13i+c6*t14i+c1*t15i+c5*t16i+c2*t17i+c4*t18i;
      float y7r  = c12*t7r-c7*t8r+c11*t9r-c8*t10r+c10*t11r-c9*t12r;
      float y7i  = c12*t7i-c7*t8i+c11*t9i-c8*t10i+c10*t11i-c9*t12i;
      float y8r  = c11*t7r-c9*t8r+c8*t9r-c12*t10r-c7*t11r+c10*t12r;
      float y8i  = c11*t7i-c9*t8i+c8*t9i-c12*t10i-c7*t11i+c10*t12i;
      float y9r  = c10*t7r-c11*t8r-c7*t9r+c9*t10r-c12*t11r-c8*t12r;
      float y9i  = c10*t7i-c11*t8i-c7*t9i+c9*t10i-c12*t11i-c8*t12i;
      float y10r = c9*t7r+c12*t8r-c10*t9r-c7*t10r+c8*t11r+c11*t12r;
      float y10i = c9*t7i+c12*t8i-c10*t9i-c7*t10i+c8*t11i+c11*t12i;
      float y11r = c8*t7r+c10*t8r+c12*t9r-c11*t10r-c9*t11r-c7*t12r;
      float y11i = c8*t7i+c10*t8i+c12*t9i-c11*t10i-c9*t11i-c7*t12i;
      float y12r = c7*t7r+c8*t8r+c9*t9r+c10*t10r+c11*t11r+c12*t12r;
      float y12i = c7*t7i+c8*t8i+c9*t9i+c10*t10i+c11*t11i+c12*t12i;
      z[j0  ]  = z[j0  ]+t1r+t2r+t3r+t4r+t5r+t6r;
      z[j0+1]  = z[j0+1]+t1i+t2i+t3i+t4i+t5i+t6i;
      z[j1  ]  = y1r-y12i;
      z[j1+1]  = y1i+y12r;
      z[j2  ]  = y2r-y11i;
      z[j2+1]  = y2i+y11r;
      z[j3  ]  = y3r-y10i;
      z[j3+1]  = y3i+y10r;
      z[j4  ]  = y4r-y9i;
      z[j4+1]  = y4i+y9r;
      z[j5  ]  = y5r-y8i;
      z[j5+1]  = y5i+y8r;
      z[j6  ]  = y6r-y7i;
      z[j6+1]  = y6i+y7r;
      z[j7  ]  = y6r+y7i;
      z[j7+1]  = y6i-y7r;
      z[j8  ]  = y5r+y8i;
      z[j8+1]  = y5i-y8r;
      z[j9  ]  = y4r+y9i;
      z[j9+1]  = y4i-y9r;
      z[j10  ] = y3r+y10i;
      z[j10+1] = y3i-y10r;
      z[j11  ] = y2r+y11i;
      z[j11+1] = y2i-y11r;
      z[j12  ] = y1r+y12i;
      z[j12+1] = y1i-y12r;
      int jt = j12+2;
      j12 = j11+2;
      j11 = j10+2;
      j10 = j9+2;
      j9 = j8+2;
      j8 = j7+2;
      j7 = j6+2;
      j6 = j5+2;
      j5 = j4+2;
      j4 = j3+2;
      j3 = j2+2;
      j2 = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }
  private static void pfa16(float[] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6, int j7, int j8, 
    int j9, int j10, int j11, int j12, int j13, int j14, int j15)
  {
    float c1,c2,c3,c4,c5,c6,c7;
    if (mu==1) {
      c1 =  PONE;
      c2 =  P923;
      c3 =  P382;
      c4 =  P707;
    } else if (mu==3) {
      c1 = -PONE;
      c2 =  P382;
      c3 =  P923;
      c4 = -P707;
    } else if (mu==5) {
      c1 =  PONE;
      c2 = -P382;
      c3 =  P923;
      c4 = -P707;
    } else if (mu==7) {
      c1 = -PONE;
      c2 = -P923;
      c3 =  P382;
      c4 =  P707;
    } else if (mu==9) {
      c1 =  PONE;
      c2 = -P923;
      c3 = -P382;
      c4 =  P707;
    } else if (mu==11) {
      c1 = -PONE;
      c2 = -P382;
      c3 = -P923;
      c4 = -P707;
    } else if (mu==13) {
      c1 =  PONE;
      c2 =  P382;
      c3 = -P923;
      c4 = -P707;
    } else {
      c1 = -PONE;
      c2 =  P923;
      c3 = -P382;
      c4 =  P707;
    }
    c5 = c1*c4;
    c6 = c1*c3;
    c7 = c1*c2;
    for (int i=0; i<m; ++i) {
      float t1r  = z[j0  ]+z[j8  ];
      float t1i  = z[j0+1]+z[j8+1];
      float t2r  = z[j4  ]+z[j12  ];
      float t2i  = z[j4+1]+z[j12+1];
      float t3r  = z[j0  ]-z[j8  ];
      float t3i  = z[j0+1]-z[j8+1];
      float t4r  = c1*(z[j4  ]-z[j12  ]);
      float t4i  = c1*(z[j4+1]-z[j12+1]);
      float t5r  = t1r+t2r;
      float t5i  = t1i+t2i;
      float t6r  = t1r-t2r;
      float t6i  = t1i-t2i;
      float t7r  = z[j1  ]+z[j9  ];
      float t7i  = z[j1+1]+z[j9+1];
      float t8r  = z[j5  ]+z[j13  ];
      float t8i  = z[j5+1]+z[j13+1];
      float t9r  = z[j1  ]-z[j9  ];
      float t9i  = z[j1+1]-z[j9+1];
      float t10r = z[j5  ]-z[j13  ];
      float t10i = z[j5+1]-z[j13+1];
      float t11r = t7r+t8r;
      float t11i = t7i+t8i;
      float t12r = t7r-t8r;
      float t12i = t7i-t8i;
      float t13r = z[j2  ]+z[j10  ];
      float t13i = z[j2+1]+z[j10+1];
      float t14r = z[j6  ]+z[j14  ];
      float t14i = z[j6+1]+z[j14+1];
      float t15r = z[j2  ]-z[j10  ];
      float t15i = z[j2+1]-z[j10+1];
      float t16r = z[j6  ]-z[j14  ];
      float t16i = z[j6+1]-z[j14+1];
      float t17r = t13r+t14r;
      float t17i = t13i+t14i;
      float t18r = c4*(t15r-t16r);
      float t18i = c4*(t15i-t16i);
      float t19r = c5*(t15r+t16r);
      float t19i = c5*(t15i+t16i);
      float t20r = c1*(t13r-t14r);
      float t20i = c1*(t13i-t14i);
      float t21r = z[j3  ]+z[j11  ];
      float t21i = z[j3+1]+z[j11+1];
      float t22r = z[j7  ]+z[j15  ];
      float t22i = z[j7+1]+z[j15+1];
      float t23r = z[j3  ]-z[j11  ];
      float t23i = z[j3+1]-z[j11+1];
      float t24r = z[j7  ]-z[j15  ];
      float t24i = z[j7+1]-z[j15+1];
      float t25r = t21r+t22r;
      float t25i = t21i+t22i;
      float t26r = t21r-t22r;
      float t26i = t21i-t22i;
      float t27r = t9r+t24r;
      float t27i = t9i+t24i;
      float t28r = t10r+t23r;
      float t28i = t10i+t23i;
      float t29r = t9r-t24r;
      float t29i = t9i-t24i;
      float t30r = t10r-t23r;
      float t30i = t10i-t23i;
      float t31r = t5r+t17r;
      float t31i = t5i+t17i;
      float t32r = t11r+t25r;
      float t32i = t11i+t25i;
      float t33r = t3r+t18r;
      float t33i = t3i+t18i;
      float t34r = c2*t29r-c6*t30r;
      float t34i = c2*t29i-c6*t30i;
      float t35r = t3r-t18r;
      float t35i = t3i-t18i;
      float t36r = c7*t27r-c3*t28r;
      float t36i = c7*t27i-c3*t28i;
      float t37r = t4r+t19r;
      float t37i = t4i+t19i;
      float t38r = c3*t27r+c7*t28r;
      float t38i = c3*t27i+c7*t28i;
      float t39r = t4r-t19r;
      float t39i = t4i-t19i;
      float t40r = c6*t29r+c2*t30r;
      float t40i = c6*t29i+c2*t30i;
      float t41r = c4*(t12r-t26r);
      float t41i = c4*(t12i-t26i);
      float t42r = c5*(t12r+t26r);
      float t42i = c5*(t12i+t26i);
      float y1r  = t33r+t34r;
      float y1i  = t33i+t34i;
      float y2r  = t6r+t41r;
      float y2i  = t6i+t41i;
      float y3r  = t35r+t40r;
      float y3i  = t35i+t40i;
      float y4r  = t5r-t17r;
      float y4i  = t5i-t17i;
      float y5r  = t35r-t40r;
      float y5i  = t35i-t40i;
      float y6r  = t6r-t41r;
      float y6i  = t6i-t41i;
      float y7r  = t33r-t34r;
      float y7i  = t33i-t34i;
      float y9r  = t38r-t37r;
      float y9i  = t38i-t37i;
      float y10r = t42r-t20r;
      float y10i = t42i-t20i;
      float y11r = t36r+t39r;
      float y11i = t36i+t39i;
      float y12r = c1*(t11r-t25r);
      float y12i = c1*(t11i-t25i);
      float y13r = t36r-t39r;
      float y13i = t36i-t39i;
      float y14r = t42r+t20r;
      float y14i = t42i+t20i;
      float y15r = t38r+t37r;
      float y15i = t38i+t37i;
      z[j0  ]  = t31r+t32r;
      z[j0+1]  = t31i+t32i;
      z[j1  ]  = y1r-y15i;
      z[j1+1]  = y1i+y15r;
      z[j2  ]  = y2r-y14i;
      z[j2+1]  = y2i+y14r;
      z[j3  ]  = y3r-y13i;
      z[j3+1]  = y3i+y13r;
      z[j4  ]  = y4r-y12i;
      z[j4+1]  = y4i+y12r;
      z[j5  ]  = y5r-y11i;
      z[j5+1]  = y5i+y11r;
      z[j6  ]  = y6r-y10i;
      z[j6+1]  = y6i+y10r;
      z[j7  ]  = y7r-y9i;
      z[j7+1]  = y7i+y9r;
      z[j8  ]  = t31r-t32r;
      z[j8+1]  = t31i-t32i;
      z[j9  ]  = y7r+y9i;
      z[j9+1]  = y7i-y9r;
      z[j10  ] = y6r+y10i;
      z[j10+1] = y6i-y10r;
      z[j11  ] = y5r+y11i;
      z[j11+1] = y5i-y11r;
      z[j12  ] = y4r+y12i;
      z[j12+1] = y4i-y12r;
      z[j13  ] = y3r+y13i;
      z[j13+1] = y3i-y13r;
      z[j14  ] = y2r+y14i;
      z[j14+1] = y2i-y14r;
      z[j15  ] = y1r+y15i;
      z[j15+1] = y1i-y15r;
      int jt = j15+2;
      j15 = j14+2;
      j14 = j13+2;
      j13 = j12+2;
      j12 = j11+2;
      j11 = j10+2;
      j10 = j9+2;
      j9 = j8+2;
      j8 = j7+2;
      j7 = j6+2;
      j6 = j5+2;
      j5 = j4+2;
      j4 = j3+2;
      j3 = j2+2;
      j2 = j1+2;
      j1 = j0+2;
      j0 = jt;
    }
  }

  /**
   * Prime-factor complex-to-complex multiple FFT. Performs multiple
   * transforms across the 2nd (slowest) dimension of a 2-D array.
   * @param sign the sign of the exponent in the Fourier transform.
   * @param n1 the number of transforms (fast dimension).
   * @param nfft the FFT length (slow dimension).
   * @param z array[nfft][2*n1] of at least n1*nfft packed complex numbers.
   */
  private static void pfacc2(int sign, int n1, int nfft, float[][] z) {

    // What is left of n after dividing by factors.
    int nleft = nfft;

    // Loop over all possible factors, from largest to smallest.
    for (int jfac=0; jfac<NFAC; ++jfac) {

      // Skip the current factor, if not a mutually prime factor of n
      int ifac = _kfac[jfac];
      int ndiv = nleft/ifac;
      if (ndiv*ifac!=nleft)
        continue;

      // What is left of n (nleft), and n divided by the current factor (m).
      nleft = ndiv;
      int m = nfft/ifac;
 
      // Rotation factor mu and stride mm.
      int mu = 0;
      int mm = 0;
      for (int kfac=1; kfac<=ifac && mm%ifac!=1; ++kfac) {
        mu = kfac;
        mm = kfac*m;
      }
      if (sign<0)
        mu = ifac-mu;

      // Array stride, bound, and indices.
      int jinc = mm;
      int jmax = nfft;
      int j0 = 0;
      int j1 = j0+jinc;

      // Factor 2.
      if (ifac==2) {
        pfa2(n1,z,mu,m,j0,j1);
        continue;
      }
      int j2 = (j1+jinc)%jmax;

      // Factor 3.
      if (ifac==3) {
        pfa3(n1,z,mu,m,j0,j1,j2);
        continue;
      }
      int j3 = (j2+jinc)%jmax;

      // Factor 4.
      if (ifac==4) {
        pfa4(n1,z,mu,m,j0,j1,j2,j3);
        continue;
      }
      int j4 = (j3+jinc)%jmax;

      // Factor 5.
      if (ifac==5) {
        pfa5(n1,z,mu,m,j0,j1,j2,j3,j4);
        continue;
      }
      int j5 = (j4+jinc)%jmax;
      int j6 = (j5+jinc)%jmax;

      // Factor 7.
      if (ifac==7) {
        pfa7(n1,z,mu,m,j0,j1,j2,j3,j4,j5,j6);
        continue;
      }
      int j7 = (j6+jinc)%jmax;

      // Factor 8.
      if (ifac==8) {
        pfa8(n1,z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7);
        continue;
      }
      int j8 = (j7+jinc)%jmax;

      // Factor 9.
      if (ifac==9) {
        pfa9(n1,z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7,j8);
        continue;
      }
      int j9 = (j8+jinc)%jmax;
      int j10 = (j9+jinc)%jmax;

      // Factor 11.
      if (ifac==11) {
        pfa11(n1,z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7,j8,j9,j10);
        continue;
      }
      int j11 = (j10+jinc)%jmax;
      int j12 = (j11+jinc)%jmax;

      // Factor 13.
      if (ifac==13) {
        pfa13(n1,z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7,j8,j9,j10,j11,j12);
        continue;
      }
      int j13 = (j12+jinc)%jmax;
      int j14 = (j13+jinc)%jmax;
      int j15 = (j14+jinc)%jmax;

      // Factor 16.
      if (ifac==16) {
        pfa16(n1,z,mu,m,j0,j1,j2,j3,j4,j5,j6,j7,j8,j9,j10,j11,j12,j13,j14,j15);
        continue;
      }
    }
  }
  private static void pfa2(int n1, float[][] z, int mu, int m,
    int j0, int j1)
  {
    int m1 = 2*n1;
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r = zj0[i1  ]-zj1[i1  ];
        float t1i = zj0[i1+1]-zj1[i1+1];
        zj0[i1  ] = zj0[i1  ]+zj1[i1  ];
        zj0[i1+1] = zj0[i1+1]+zj1[i1+1];
        zj1[i1  ] = t1r;
        zj1[i1+1] = t1i;
      }
      int jt = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }
  private static void pfa3(int n1, float[][] z, int mu, int m,
    int j0, int j1, int j2)
  {
    int m1 = 2*n1;
    float c1;
    if (mu==1) {
      c1 =  P866;
    } else {
      c1 = -P866;
    }
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      float[] zj2 = z[j2];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r = zj1[i1  ]+zj2[i1  ];
        float t1i = zj1[i1+1]+zj2[i1+1];
        float y1r = zj0[i1  ]-0.5f*t1r;
        float y1i = zj0[i1+1]-0.5f*t1i;
        float y2r = c1*(zj1[i1  ]-zj2[i1  ]);
        float y2i = c1*(zj1[i1+1]-zj2[i1+1]);
        zj0[i1  ] = zj0[i1  ]+t1r;
        zj0[i1+1] = zj0[i1+1]+t1i;
        zj1[i1  ] = y1r-y2i;
        zj1[i1+1] = y1i+y2r;
        zj2[i1  ] = y1r+y2i;
        zj2[i1+1] = y1i-y2r;
      }
      int jt = j2+1;
      j2 = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }
  private static void pfa4(int n1, float[][] z, int mu, int m,
    int j0, int j1, int j2, int j3)
  {
    int m1 = 2*n1;
    float c1;
    if (mu==1) {
      c1 =  PONE;
    } else {
      c1 = -PONE;
    }
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      float[] zj2 = z[j2];
      float[] zj3 = z[j3];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r = zj0[i1  ]+zj2[i1  ];
        float t1i = zj0[i1+1]+zj2[i1+1];
        float t2r = zj1[i1  ]+zj3[i1  ];
        float t2i = zj1[i1+1]+zj3[i1+1];
        float y1r = zj0[i1  ]-zj2[i1  ];
        float y1i = zj0[i1+1]-zj2[i1+1];
        float y3r = c1*(zj1[i1  ]-zj3[i1  ]);
        float y3i = c1*(zj1[i1+1]-zj3[i1+1]);
        zj0[i1  ] = t1r+t2r;
        zj0[i1+1] = t1i+t2i;
        zj1[i1  ] = y1r-y3i;
        zj1[i1+1] = y1i+y3r;
        zj2[i1  ] = t1r-t2r;
        zj2[i1+1] = t1i-t2i;
        zj3[i1  ] = y1r+y3i;
        zj3[i1+1] = y1i-y3r;
      }
      int jt = j3+1;
      j3 = j2+1;
      j2 = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }
  private static void pfa5(int n1, float[][] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4)
  {
    int m1 = 2*n1;
    float c1,c2,c3;
    if (mu==1) {
      c1 =  P559;
      c2 =  P951;
      c3 =  P587;
    } else if (mu==2) {
      c1 = -P559;
      c2 =  P587;
      c3 = -P951;
    } else if (mu==3) {
      c1 = -P559;
      c2 = -P587;
      c3 =  P951;
    } else { 
      c1 =  P559;
      c2 = -P951;
      c3 = -P587;
    }
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      float[] zj2 = z[j2];
      float[] zj3 = z[j3];
      float[] zj4 = z[j4];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r = zj1[i1  ]+zj4[i1  ];
        float t1i = zj1[i1+1]+zj4[i1+1];
        float t2r = zj2[i1  ]+zj3[i1  ];
        float t2i = zj2[i1+1]+zj3[i1+1];
        float t3r = zj1[i1  ]-zj4[i1  ];
        float t3i = zj1[i1+1]-zj4[i1+1];
        float t4r = zj2[i1  ]-zj3[i1  ];
        float t4i = zj2[i1+1]-zj3[i1+1];
        float t5r = t1r+t2r;
        float t5i = t1i+t2i;
        float t6r = c1*(t1r-t2r);
        float t6i = c1*(t1i-t2i);
        float t7r = zj0[i1  ]-0.25f*t5r;
        float t7i = zj0[i1+1]-0.25f*t5i;
        float y1r = t7r+t6r;
        float y1i = t7i+t6i;
        float y2r = t7r-t6r;
        float y2i = t7i-t6i;
        float y3r = c3*t3r-c2*t4r;
        float y3i = c3*t3i-c2*t4i;
        float y4r = c2*t3r+c3*t4r;
        float y4i = c2*t3i+c3*t4i;
        zj0[i1  ] = zj0[i1  ]+t5r;
        zj0[i1+1] = zj0[i1+1]+t5i;
        zj1[i1  ] = y1r-y4i;
        zj1[i1+1] = y1i+y4r;
        zj2[i1  ] = y2r-y3i;
        zj2[i1+1] = y2i+y3r;
        zj3[i1  ] = y2r+y3i;
        zj3[i1+1] = y2i-y3r;
        zj4[i1  ] = y1r+y4i;
        zj4[i1+1] = y1i-y4r;
      }
      int jt = j4+1;
      j4 = j3+1;
      j3 = j2+1;
      j2 = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }
  private static void pfa7(int n1, float[][] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6)
  {
    int m1 = 2*n1;
    float c1,c2,c3,c4,c5,c6;
    if (mu==1) {
      c1 =  P623;
      c2 = -P222;
      c3 = -P900;
      c4 =  P781;
      c5 =  P974;
      c6 =  P433;
    } else if (mu==2) {
      c1 = -P222;
      c2 = -P900;
      c3 =  P623;
      c4 =  P974;
      c5 = -P433;
      c6 = -P781;
    } else if (mu==3) {
      c1 = -P900;
      c2 =  P623;
      c3 = -P222;
      c4 =  P433;
      c5 = -P781;
      c6 =  P974;
    } else if (mu==4) {
      c1 = -P900;
      c2 =  P623;
      c3 = -P222;
      c4 = -P433;
      c5 =  P781;
      c6 = -P974;
    } else if (mu==5) {
      c1 = -P222;
      c2 = -P900;
      c3 =  P623;
      c4 = -P974;
      c5 =  P433;
      c6 =  P781;
    } else {
      c1 =  P623;
      c2 = -P222;
      c3 = -P900;
      c4 = -P781;
      c5 = -P974;
      c6 = -P433;
    }
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      float[] zj2 = z[j2];
      float[] zj3 = z[j3];
      float[] zj4 = z[j4];
      float[] zj5 = z[j5];
      float[] zj6 = z[j6];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r = zj1[i1  ]+zj6[i1  ];
        float t1i = zj1[i1+1]+zj6[i1+1];
        float t2r = zj2[i1  ]+zj5[i1  ];
        float t2i = zj2[i1+1]+zj5[i1+1];
        float t3r = zj3[i1  ]+zj4[i1  ];
        float t3i = zj3[i1+1]+zj4[i1+1];
        float t4r = zj1[i1  ]-zj6[i1  ];
        float t4i = zj1[i1+1]-zj6[i1+1];
        float t5r = zj2[i1  ]-zj5[i1  ];
        float t5i = zj2[i1+1]-zj5[i1+1];
        float t6r = zj3[i1  ]-zj4[i1  ];
        float t6i = zj3[i1+1]-zj4[i1+1];
        float t7r = zj0[i1  ]-0.5f*t3r;
        float t7i = zj0[i1+1]-0.5f*t3i;
        float t8r = t1r-t3r;
        float t8i = t1i-t3i;
        float t9r = t2r-t3r;
        float t9i = t2i-t3i;
        float y1r = t7r+c1*t8r+c2*t9r;
        float y1i = t7i+c1*t8i+c2*t9i;
        float y2r = t7r+c2*t8r+c3*t9r;
        float y2i = t7i+c2*t8i+c3*t9i;
        float y3r = t7r+c3*t8r+c1*t9r;
        float y3i = t7i+c3*t8i+c1*t9i;
        float y4r = c6*t4r-c4*t5r+c5*t6r;
        float y4i = c6*t4i-c4*t5i+c5*t6i;
        float y5r = c5*t4r-c6*t5r-c4*t6r;
        float y5i = c5*t4i-c6*t5i-c4*t6i;
        float y6r = c4*t4r+c5*t5r+c6*t6r;
        float y6i = c4*t4i+c5*t5i+c6*t6i;
        zj0[i1  ] = zj0[i1  ]+t1r+t2r+t3r;
        zj0[i1+1] = zj0[i1+1]+t1i+t2i+t3i;
        zj1[i1  ] = y1r-y6i;
        zj1[i1+1] = y1i+y6r;
        zj2[i1  ] = y2r-y5i;
        zj2[i1+1] = y2i+y5r;
        zj3[i1  ] = y3r-y4i;
        zj3[i1+1] = y3i+y4r;
        zj4[i1  ] = y3r+y4i;
        zj4[i1+1] = y3i-y4r;
        zj5[i1  ] = y2r+y5i;
        zj5[i1+1] = y2i-y5r;
        zj6[i1  ] = y1r+y6i;
        zj6[i1+1] = y1i-y6r;
      }
      int jt = j6+1;
      j6 = j5+1;
      j5 = j4+1;
      j4 = j3+1;
      j3 = j2+1;
      j2 = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }
  private static void pfa8(int n1, float[][] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6, int j7)
  {
    int m1 = 2*n1;
    float c1,c2,c3;
    if (mu==1) {
      c1 =  PONE;
      c2 =  P707;
    } else if (mu==3) {
      c1 = -PONE;
      c2 = -P707;
    } else if (mu==5) {
      c1 =  PONE;
      c2 = -P707;
    } else {
      c1 = -PONE;
      c2 =  P707;
    }
    c3 = c1*c2;
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      float[] zj2 = z[j2];
      float[] zj3 = z[j3];
      float[] zj4 = z[j4];
      float[] zj5 = z[j5];
      float[] zj6 = z[j6];
      float[] zj7 = z[j7];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r = zj0[i1  ]+zj4[i1  ];
        float t1i = zj0[i1+1]+zj4[i1+1];
        float t2r = zj0[i1  ]-zj4[i1  ];
        float t2i = zj0[i1+1]-zj4[i1+1];
        float t3r = zj1[i1  ]+zj5[i1  ];
        float t3i = zj1[i1+1]+zj5[i1+1];
        float t4r = zj1[i1  ]-zj5[i1  ];
        float t4i = zj1[i1+1]-zj5[i1+1];
        float t5r = zj2[i1  ]+zj6[i1  ];
        float t5i = zj2[i1+1]+zj6[i1+1];
        float t6r = c1*(zj2[i1  ]-zj6[i1  ]);
        float t6i = c1*(zj2[i1+1]-zj6[i1+1]);
        float t7r = zj3[i1  ]+zj7[i1  ];
        float t7i = zj3[i1+1]+zj7[i1+1];
        float t8r = zj3[i1  ]-zj7[i1  ];
        float t8i = zj3[i1+1]-zj7[i1+1];
        float t9r = t1r+t5r;
        float t9i = t1i+t5i;
        float t10r = t3r+t7r;
        float t10i = t3i+t7i;
        float t11r = c2*(t4r-t8r);
        float t11i = c2*(t4i-t8i);
        float t12r = c3*(t4r+t8r);
        float t12i = c3*(t4i+t8i);
        float y1r = t2r+t11r;
        float y1i = t2i+t11i;
        float y2r = t1r-t5r;
        float y2i = t1i-t5i;
        float y3r = t2r-t11r;
        float y3i = t2i-t11i;
        float y5r = t12r-t6r;
        float y5i = t12i-t6i;
        float y6r = c1*(t3r-t7r);
        float y6i = c1*(t3i-t7i);
        float y7r = t12r+t6r;
        float y7i = t12i+t6i;
        zj0[i1  ] = t9r+t10r;
        zj0[i1+1] = t9i+t10i;
        zj1[i1  ] = y1r-y7i;
        zj1[i1+1] = y1i+y7r;
        zj2[i1  ] = y2r-y6i;
        zj2[i1+1] = y2i+y6r;
        zj3[i1  ] = y3r-y5i;
        zj3[i1+1] = y3i+y5r;
        zj4[i1  ] = t9r-t10r;
        zj4[i1+1] = t9i-t10i;
        zj5[i1  ] = y3r+y5i;
        zj5[i1+1] = y3i-y5r;
        zj6[i1  ] = y2r+y6i;
        zj6[i1+1] = y2i-y6r;
        zj7[i1  ] = y1r+y7i;
        zj7[i1+1] = y1i-y7r;
      }
      int jt = j7+1;
      j7 = j6+1;
      j6 = j5+1;
      j5 = j4+1;
      j4 = j3+1;
      j3 = j2+1;
      j2 = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }
  private static void pfa9(int n1, float[][] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6, int j7, int j8)
  {
    int m1 = 2*n1;
    float c1,c2,c3,c4,c5,c6,c7,c8,c9;
    if (mu==1) {
      c1 =  P866;
      c2 =  P766;
      c3 =  P642;
      c4 =  P173;
      c5 =  P984;
    } else if (mu==2) {
      c1 = -P866;
      c2 =  P173;
      c3 =  P984;
      c4 = -P939;
      c5 =  P342;
    } else if (mu==4) {
      c1 =  P866;
      c2 = -P939;
      c3 =  P342;
      c4 =  P766;
      c5 = -P642;
    } else if (mu==5) {
      c1 = -P866;
      c2 = -P939;
      c3 = -P342;
      c4 =  P766;
      c5 =  P642;
    } else if (mu==7) {
      c1 =  P866;
      c2 =  P173;
      c3 = -P984;
      c4 = -P939;
      c5 = -P342;
    } else {
      c1 = -P866;
      c2 =  P766;
      c3 = -P642;
      c4 =  P173;
      c5 = -P984;
    }
    c6 = c1*c2;
    c7 = c1*c3;
    c8 = c1*c4;
    c9 = c1*c5;
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      float[] zj2 = z[j2];
      float[] zj3 = z[j3];
      float[] zj4 = z[j4];
      float[] zj5 = z[j5];
      float[] zj6 = z[j6];
      float[] zj7 = z[j7];
      float[] zj8 = z[j8];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r  = zj3[i1  ]+zj6[i1  ];
        float t1i  = zj3[i1+1]+zj6[i1+1];
        float t2r  = zj0[i1  ]-0.5f*t1r;
        float t2i  = zj0[i1+1]-0.5f*t1i;
        float t3r  = c1*(zj3[i1  ]-zj6[i1  ]);
        float t3i  = c1*(zj3[i1+1]-zj6[i1+1]);
        float t4r  = zj0[i1  ]+t1r;
        float t4i  = zj0[i1+1]+t1i;
        float t5r  = zj4[i1  ]+zj7[i1  ];
        float t5i  = zj4[i1+1]+zj7[i1+1];
        float t6r  = zj1[i1  ]-0.5f*t5r;
        float t6i  = zj1[i1+1]-0.5f*t5i;
        float t7r  = zj4[i1  ]-zj7[i1  ];
        float t7i  = zj4[i1+1]-zj7[i1+1];
        float t8r  = zj1[i1  ]+t5r;
        float t8i  = zj1[i1+1]+t5i;
        float t9r  = zj2[i1  ]+zj5[i1  ];
        float t9i  = zj2[i1+1]+zj5[i1+1];
        float t10r = zj8[i1  ]-0.5f*t9r;
        float t10i = zj8[i1+1]-0.5f*t9i;
        float t11r = zj2[i1  ]-zj5[i1  ];
        float t11i = zj2[i1+1]-zj5[i1+1];
        float t12r = zj8[i1  ]+t9r;
        float t12i = zj8[i1+1]+t9i;
        float t13r = t8r+t12r;
        float t13i = t8i+t12i;
        float t14r = t6r+t10r;
        float t14i = t6i+t10i;
        float t15r = t6r-t10r;
        float t15i = t6i-t10i;
        float t16r = t7r+t11r;
        float t16i = t7i+t11i;
        float t17r = t7r-t11r;
        float t17i = t7i-t11i;
        float t18r = c2*t14r-c7*t17r;
        float t18i = c2*t14i-c7*t17i;
        float t19r = c4*t14r+c9*t17r;
        float t19i = c4*t14i+c9*t17i;
        float t20r = c3*t15r+c6*t16r;
        float t20i = c3*t15i+c6*t16i;
        float t21r = c5*t15r-c8*t16r;
        float t21i = c5*t15i-c8*t16i;
        float t22r = t18r+t19r;
        float t22i = t18i+t19i;
        float t23r = t20r-t21r;
        float t23i = t20i-t21i;
        float y1r  = t2r+t18r;
        float y1i  = t2i+t18i;
        float y2r  = t2r+t19r;
        float y2i  = t2i+t19i;
        float y3r  = t4r-0.5f*t13r;
        float y3i  = t4i-0.5f*t13i;
        float y4r  = t2r-t22r;
        float y4i  = t2i-t22i;
        float y5r  = t3r-t23r;
        float y5i  = t3i-t23i;
        float y6r  = c1*(t8r-t12r);
        float y6i  = c1*(t8i-t12i);
        float y7r  = t21r-t3r;
        float y7i  = t21i-t3i;
        float y8r  = t3r+t20r;
        float y8i  = t3i+t20i;
        zj0[i1  ] = t4r+t13r;
        zj0[i1+1] = t4i+t13i;
        zj1[i1  ] = y1r-y8i;
        zj1[i1+1] = y1i+y8r;
        zj2[i1  ] = y2r-y7i;
        zj2[i1+1] = y2i+y7r;
        zj3[i1  ] = y3r-y6i;
        zj3[i1+1] = y3i+y6r;
        zj4[i1  ] = y4r-y5i;
        zj4[i1+1] = y4i+y5r;
        zj5[i1  ] = y4r+y5i;
        zj5[i1+1] = y4i-y5r;
        zj6[i1  ] = y3r+y6i;
        zj6[i1+1] = y3i-y6r;
        zj7[i1  ] = y2r+y7i;
        zj7[i1+1] = y2i-y7r;
        zj8[i1  ] = y1r+y8i;
        zj8[i1+1] = y1i-y8r;
      }
      int jt = j8+1;
      j8 = j7+1;
      j7 = j6+1;
      j6 = j5+1;
      j5 = j4+1;
      j4 = j3+1;
      j3 = j2+1;
      j2 = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }
  private static void pfa11(int n1, float[][] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, 
    int j6, int j7, int j8, int j9, int j10)
  {
    int m1 = 2*n1;
    float c1,c2,c3,c4,c5,c6,c7,c8,c9,c10;
    if (mu==1) {
      c1  =  P841;
      c2  =  P415;
      c3  = -P142;
      c4  = -P654;
      c5  = -P959;
      c6  =  P540;
      c7  =  P909;
      c8  =  P989;
      c9  =  P755;
      c10 =  P281;
    } else if (mu==2) {
      c1  =  P415;
      c2  = -P654;
      c3  = -P959;
      c4  = -P142;
      c5  =  P841;
      c6  =  P909;
      c7  =  P755;
      c8  = -P281;
      c9  = -P989;
      c10 = -P540;
    } else if (mu==3) {
      c1  = -P142;
      c2  = -P959;
      c3  =  P415;
      c4  =  P841;
      c5  = -P654;
      c6  =  P989;
      c7  = -P281;
      c8  = -P909;
      c9  =  P540;
      c10 =  P755;
    } else if (mu==4) {
      c1  = -P654;
      c2  = -P142;
      c3  =  P841;
      c4  = -P959;
      c5  =  P415;
      c6  =  P755;
      c7  = -P989;
      c8  =  P540;
      c9  =  P281;
      c10 = -P909;
    } else if (mu==5) {
      c1  = -P959;
      c2  =  P841;
      c3  = -P654;
      c4  =  P415;
      c5  = -P142;
      c6  =  P281;
      c7  = -P540;
      c8  =  P755;
      c9  = -P909;
      c10 =  P989;
    } else if (mu==6) {
      c1  = -P959;
      c2  =  P841;
      c3  = -P654;
      c4  =  P415;
      c5  = -P142;
      c6  = -P281;
      c7  =  P540;
      c8  = -P755;
      c9  =  P909;
      c10 = -P989;
    } else if (mu==7) {
      c1  = -P654;
      c2  = -P142;
      c3  =  P841;
      c4  = -P959;
      c5  =  P415;
      c6  = -P755;
      c7  =  P989;
      c8  = -P540;
      c9  = -P281;
      c10 =  P909;
    } else if (mu==8) {
      c1  = -P142;
      c2  = -P959;
      c3  =  P415;
      c4  =  P841;
      c5  = -P654;
      c6  = -P989;
      c7  =  P281;
      c8  =  P909;
      c9  = -P540;
      c10 = -P755;
    } else if (mu==9) {
      c1  =  P415;
      c2  = -P654;
      c3  = -P959;
      c4  = -P142;
      c5  =  P841;
      c6  = -P909;
      c7  = -P755;
      c8  =  P281;
      c9  =  P989;
      c10 =  P540;
    } else {
      c1  =  P841;
      c2  =  P415;
      c3  = -P142;
      c4  = -P654;
      c5  = -P959;
      c6  = -P540;
      c7  = -P909;
      c8  = -P989;
      c9  = -P755;
      c10 = -P281;
    }
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      float[] zj2 = z[j2];
      float[] zj3 = z[j3];
      float[] zj4 = z[j4];
      float[] zj5 = z[j5];
      float[] zj6 = z[j6];
      float[] zj7 = z[j7];
      float[] zj8 = z[j8];
      float[] zj9 = z[j9];
      float[] zj10 = z[j10];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r  = zj1[i1  ]+zj10[i1  ];
        float t1i  = zj1[i1+1]+zj10[i1+1];
        float t2r  = zj2[i1  ]+zj9[i1  ];
        float t2i  = zj2[i1+1]+zj9[i1+1];
        float t3r  = zj3[i1  ]+zj8[i1  ];
        float t3i  = zj3[i1+1]+zj8[i1+1];
        float t4r  = zj4[i1  ]+zj7[i1  ];
        float t4i  = zj4[i1+1]+zj7[i1+1];
        float t5r  = zj5[i1  ]+zj6[i1  ];
        float t5i  = zj5[i1+1]+zj6[i1+1];
        float t6r  = zj1[i1  ]-zj10[i1  ];
        float t6i  = zj1[i1+1]-zj10[i1+1];
        float t7r  = zj2[i1  ]-zj9[i1  ];
        float t7i  = zj2[i1+1]-zj9[i1+1];
        float t8r  = zj3[i1  ]-zj8[i1  ];
        float t8i  = zj3[i1+1]-zj8[i1+1];
        float t9r  = zj4[i1  ]-zj7[i1  ];
        float t9i  = zj4[i1+1]-zj7[i1+1];
        float t10r = zj5[i1  ]-zj6[i1  ];
        float t10i = zj5[i1+1]-zj6[i1+1];
        float t11r = zj0[i1  ]-0.5f*t5r;
        float t11i = zj0[i1+1]-0.5f*t5i;
        float t12r = t1r-t5r;
        float t12i = t1i-t5i;
        float t13r = t2r-t5r;
        float t13i = t2i-t5i;
        float t14r = t3r-t5r;
        float t14i = t3i-t5i;
        float t15r = t4r-t5r;
        float t15i = t4i-t5i;
        float y1r  = t11r+c1*t12r+c2*t13r+c3*t14r+c4*t15r;
        float y1i  = t11i+c1*t12i+c2*t13i+c3*t14i+c4*t15i;
        float y2r  = t11r+c2*t12r+c4*t13r+c5*t14r+c3*t15r;
        float y2i  = t11i+c2*t12i+c4*t13i+c5*t14i+c3*t15i;
        float y3r  = t11r+c3*t12r+c5*t13r+c2*t14r+c1*t15r;
        float y3i  = t11i+c3*t12i+c5*t13i+c2*t14i+c1*t15i;
        float y4r  = t11r+c4*t12r+c3*t13r+c1*t14r+c5*t15r;
        float y4i  = t11i+c4*t12i+c3*t13i+c1*t14i+c5*t15i;
        float y5r  = t11r+c5*t12r+c1*t13r+c4*t14r+c2*t15r;
        float y5i  = t11i+c5*t12i+c1*t13i+c4*t14i+c2*t15i;
        float y6r  = c10*t6r-c6*t7r+c9*t8r-c7*t9r+c8*t10r;
        float y6i  = c10*t6i-c6*t7i+c9*t8i-c7*t9i+c8*t10i;
        float y7r  = c9*t6r-c8*t7r+c6*t8r+c10*t9r-c7*t10r;
        float y7i  = c9*t6i-c8*t7i+c6*t8i+c10*t9i-c7*t10i;
        float y8r  = c8*t6r-c10*t7r-c7*t8r+c6*t9r+c9*t10r;
        float y8i  = c8*t6i-c10*t7i-c7*t8i+c6*t9i+c9*t10i;
        float y9r  = c7*t6r+c9*t7r-c10*t8r-c8*t9r-c6*t10r;
        float y9i  = c7*t6i+c9*t7i-c10*t8i-c8*t9i-c6*t10i;
        float y10r = c6*t6r+c7*t7r+c8*t8r+c9*t9r+c10*t10r;
        float y10i = c6*t6i+c7*t7i+c8*t8i+c9*t9i+c10*t10i;
        zj0[i1  ]  = zj0[i1  ]+t1r+t2r+t3r+t4r+t5r;
        zj0[i1+1]  = zj0[i1+1]+t1i+t2i+t3i+t4i+t5i;
        zj1[i1  ]  = y1r-y10i;
        zj1[i1+1]  = y1i+y10r;
        zj2[i1  ]  = y2r-y9i;
        zj2[i1+1]  = y2i+y9r;
        zj3[i1  ]  = y3r-y8i;
        zj3[i1+1]  = y3i+y8r;
        zj4[i1  ]  = y4r-y7i;
        zj4[i1+1]  = y4i+y7r;
        zj5[i1  ]  = y5r-y6i;
        zj5[i1+1]  = y5i+y6r;
        zj6[i1  ]  = y5r+y6i;
        zj6[i1+1]  = y5i-y6r;
        zj7[i1  ]  = y4r+y7i;
        zj7[i1+1]  = y4i-y7r;
        zj8[i1  ]  = y3r+y8i;
        zj8[i1+1]  = y3i-y8r;
        zj9[i1  ]  = y2r+y9i;
        zj9[i1+1]  = y2i-y9r;
        zj10[i1  ] = y1r+y10i;
        zj10[i1+1] = y1i-y10r;
      }
      int jt = j10+1;
      j10 = j9+1;
      j9 = j8+1;
      j8 = j7+1;
      j7 = j6+1;
      j6 = j5+1;
      j5 = j4+1;
      j4 = j3+1;
      j3 = j2+1;
      j2 = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }
  private static void pfa13(int n1, float[][] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6, 
    int j7, int j8, int j9, int j10, int j11, int j12)
  {
    int m1 = 2*n1;
    float c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12;
    if (mu==1) {
      c1  =  P885;
      c2  =  P568;
      c3  =  P120;
      c4  = -P354;
      c5  = -P748;
      c6  = -P970;
      c7  =  P464;
      c8  =  P822;
      c9  =  P992;
      c10 =  P935;
      c11 =  P663;
      c12 =  P239;
    } else if (mu==2) {
      c1  =  P568;
      c2  = -P354;
      c3  = -P970;
      c4  = -P748;
      c5  =  P120;
      c6  =  P885;
      c7  =  P822;
      c8  =  P935;
      c9  =  P239;
      c10 = -P663;
      c11 = -P992;
      c12 = -P464;
    } else if (mu==3) {
      c1  =  P120;
      c2  = -P970;
      c3  = -P354;
      c4  =  P885;
      c5  =  P568;
      c6  = -P748;
      c7  =  P992;
      c8  =  P239;
      c9  = -P935;
      c10 = -P464;
      c11 =  P822;
      c12 =  P663;
    } else if (mu==4) {
      c1  = -P354;
      c2  = -P748;
      c3  =  P885;
      c4  =  P120;
      c5  = -P970;
      c6  =  P568;
      c7  =  P935;
      c8  = -P663;
      c9  = -P464;
      c10 =  P992;
      c11 = -P239;
      c12 = -P822;
    } else if (mu==5) {
      c1  = -P748;
      c2  =  P120;
      c3  =  P568;
      c4  = -P970;
      c5  =  P885;
      c6  = -P354;
      c7  =  P663;
      c8  = -P992;
      c9  =  P822;
      c10 = -P239;
      c11 = -P464;
      c12 =  P935;
    } else if (mu==6) {
      c1  = -P970;
      c2  =  P885;
      c3  = -P748;
      c4  =  P568;
      c5  = -P354;
      c6  =  P120;
      c7  =  P239;
      c8  = -P464;
      c9  =  P663;
      c10 = -P822;
      c11 =  P935;
      c12 = -P992;
    } else if (mu==7) {
      c1  = -P970;
      c2  =  P885;
      c3  = -P748;
      c4  =  P568;
      c5  = -P354;
      c6  =  P120;
      c7  = -P239;
      c8  =  P464;
      c9  = -P663;
      c10 =  P822;
      c11 = -P935;
      c12 =  P992;
    } else if (mu==8) {
      c1  = -P748;
      c2  =  P120;
      c3  =  P568;
      c4  = -P970;
      c5  =  P885;
      c6  = -P354;
      c7  = -P663;
      c8  =  P992;
      c9  = -P822;
      c10 =  P239;
      c11 =  P464;
      c12 = -P935;
    } else if (mu==9) {
      c1  = -P354;
      c2  = -P748;
      c3  =  P885;
      c4  =  P120;
      c5  = -P970;
      c6  =  P568;
      c7  = -P935;
      c8  =  P663;
      c9  =  P464;
      c10 = -P992;
      c11 =  P239;
      c12 =  P822;
    } else if (mu==10) {
      c1  =  P120;
      c2  = -P970;
      c3  = -P354;
      c4  =  P885;
      c5  =  P568;
      c6  = -P748;
      c7  = -P992;
      c8  = -P239;
      c9  =  P935;
      c10 =  P464;
      c11 = -P822;
      c12 = -P663;
    } else if (mu==11) {
      c1  =  P568;
      c2  = -P354;
      c3  = -P970;
      c4  = -P748;
      c5  =  P120;
      c6  =  P885;
      c7  = -P822;
      c8  = -P935;
      c9  = -P239;
      c10 =  P663;
      c11 =  P992;
      c12 =  P464;
    } else {
      c1  =  P885;
      c2  =  P568;
      c3  =  P120;
      c4  = -P354;
      c5  = -P748;
      c6  = -P970;
      c7  = -P464;
      c8  = -P822;
      c9  = -P992;
      c10 = -P935;
      c11 = -P663;
      c12 = -P239;
    }
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      float[] zj2 = z[j2];
      float[] zj3 = z[j3];
      float[] zj4 = z[j4];
      float[] zj5 = z[j5];
      float[] zj6 = z[j6];
      float[] zj7 = z[j7];
      float[] zj8 = z[j8];
      float[] zj9 = z[j9];
      float[] zj10 = z[j10];
      float[] zj11 = z[j11];
      float[] zj12 = z[j12];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r  = zj1[i1  ]+zj12[i1  ];
        float t1i  = zj1[i1+1]+zj12[i1+1];
        float t2r  = zj2[i1  ]+zj11[i1  ];
        float t2i  = zj2[i1+1]+zj11[i1+1];
        float t3r  = zj3[i1  ]+zj10[i1  ];
        float t3i  = zj3[i1+1]+zj10[i1+1];
        float t4r  = zj4[i1  ]+zj9[i1  ];
        float t4i  = zj4[i1+1]+zj9[i1+1];
        float t5r  = zj5[i1  ]+zj8[i1  ];
        float t5i  = zj5[i1+1]+zj8[i1+1];
        float t6r  = zj6[i1  ]+zj7[i1  ];
        float t6i  = zj6[i1+1]+zj7[i1+1];
        float t7r  = zj1[i1  ]-zj12[i1  ];
        float t7i  = zj1[i1+1]-zj12[i1+1];
        float t8r  = zj2[i1  ]-zj11[i1  ];
        float t8i  = zj2[i1+1]-zj11[i1+1];
        float t9r  = zj3[i1  ]-zj10[i1  ];
        float t9i  = zj3[i1+1]-zj10[i1+1];
        float t10r = zj4[i1  ]-zj9[i1  ];
        float t10i = zj4[i1+1]-zj9[i1+1];
        float t11r = zj5[i1  ]-zj8[i1  ];
        float t11i = zj5[i1+1]-zj8[i1+1];
        float t12r = zj6[i1  ]-zj7[i1  ];
        float t12i = zj6[i1+1]-zj7[i1+1];
        float t13r = zj0[i1  ]-0.5f*t6r;
        float t13i = zj0[i1+1]-0.5f*t6i;
        float t14r = t1r-t6r;
        float t14i = t1i-t6i;
        float t15r = t2r-t6r;
        float t15i = t2i-t6i;
        float t16r = t3r-t6r;
        float t16i = t3i-t6i;
        float t17r = t4r-t6r;
        float t17i = t4i-t6i;
        float t18r = t5r-t6r;
        float t18i = t5i-t6i;
        float y1r  = t13r+c1*t14r+c2*t15r+c3*t16r+c4*t17r+c5*t18r;
        float y1i  = t13i+c1*t14i+c2*t15i+c3*t16i+c4*t17i+c5*t18i;
        float y2r  = t13r+c2*t14r+c4*t15r+c6*t16r+c5*t17r+c3*t18r;
        float y2i  = t13i+c2*t14i+c4*t15i+c6*t16i+c5*t17i+c3*t18i;
        float y3r  = t13r+c3*t14r+c6*t15r+c4*t16r+c1*t17r+c2*t18r;
        float y3i  = t13i+c3*t14i+c6*t15i+c4*t16i+c1*t17i+c2*t18i;
        float y4r  = t13r+c4*t14r+c5*t15r+c1*t16r+c3*t17r+c6*t18r;
        float y4i  = t13i+c4*t14i+c5*t15i+c1*t16i+c3*t17i+c6*t18i;
        float y5r  = t13r+c5*t14r+c3*t15r+c2*t16r+c6*t17r+c1*t18r;
        float y5i  = t13i+c5*t14i+c3*t15i+c2*t16i+c6*t17i+c1*t18i;
        float y6r  = t13r+c6*t14r+c1*t15r+c5*t16r+c2*t17r+c4*t18r;
        float y6i  = t13i+c6*t14i+c1*t15i+c5*t16i+c2*t17i+c4*t18i;
        float y7r  = c12*t7r-c7*t8r+c11*t9r-c8*t10r+c10*t11r-c9*t12r;
        float y7i  = c12*t7i-c7*t8i+c11*t9i-c8*t10i+c10*t11i-c9*t12i;
        float y8r  = c11*t7r-c9*t8r+c8*t9r-c12*t10r-c7*t11r+c10*t12r;
        float y8i  = c11*t7i-c9*t8i+c8*t9i-c12*t10i-c7*t11i+c10*t12i;
        float y9r  = c10*t7r-c11*t8r-c7*t9r+c9*t10r-c12*t11r-c8*t12r;
        float y9i  = c10*t7i-c11*t8i-c7*t9i+c9*t10i-c12*t11i-c8*t12i;
        float y10r = c9*t7r+c12*t8r-c10*t9r-c7*t10r+c8*t11r+c11*t12r;
        float y10i = c9*t7i+c12*t8i-c10*t9i-c7*t10i+c8*t11i+c11*t12i;
        float y11r = c8*t7r+c10*t8r+c12*t9r-c11*t10r-c9*t11r-c7*t12r;
        float y11i = c8*t7i+c10*t8i+c12*t9i-c11*t10i-c9*t11i-c7*t12i;
        float y12r = c7*t7r+c8*t8r+c9*t9r+c10*t10r+c11*t11r+c12*t12r;
        float y12i = c7*t7i+c8*t8i+c9*t9i+c10*t10i+c11*t11i+c12*t12i;
        zj0[i1  ]  = zj0[i1  ]+t1r+t2r+t3r+t4r+t5r+t6r;
        zj0[i1+1]  = zj0[i1+1]+t1i+t2i+t3i+t4i+t5i+t6i;
        zj1[i1  ]  = y1r-y12i;
        zj1[i1+1]  = y1i+y12r;
        zj2[i1  ]  = y2r-y11i;
        zj2[i1+1]  = y2i+y11r;
        zj3[i1  ]  = y3r-y10i;
        zj3[i1+1]  = y3i+y10r;
        zj4[i1  ]  = y4r-y9i;
        zj4[i1+1]  = y4i+y9r;
        zj5[i1  ]  = y5r-y8i;
        zj5[i1+1]  = y5i+y8r;
        zj6[i1  ]  = y6r-y7i;
        zj6[i1+1]  = y6i+y7r;
        zj7[i1  ]  = y6r+y7i;
        zj7[i1+1]  = y6i-y7r;
        zj8[i1  ]  = y5r+y8i;
        zj8[i1+1]  = y5i-y8r;
        zj9[i1  ]  = y4r+y9i;
        zj9[i1+1]  = y4i-y9r;
        zj10[i1  ] = y3r+y10i;
        zj10[i1+1] = y3i-y10r;
        zj11[i1  ] = y2r+y11i;
        zj11[i1+1] = y2i-y11r;
        zj12[i1  ] = y1r+y12i;
        zj12[i1+1] = y1i-y12r;
      }
      int jt = j12+1;
      j12 = j11+1;
      j11 = j10+1;
      j10 = j9+1;
      j9 = j8+1;
      j8 = j7+1;
      j7 = j6+1;
      j6 = j5+1;
      j5 = j4+1;
      j4 = j3+1;
      j3 = j2+1;
      j2 = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }
  private static void pfa16(int n1, float[][] z, int mu, int m,
    int j0, int j1, int j2, int j3, int j4, int j5, int j6, int j7, int j8, 
    int j9, int j10, int j11, int j12, int j13, int j14, int j15)
  {
    int m1 = 2*n1;
    float c1,c2,c3,c4,c5,c6,c7;
    if (mu==1) {
      c1 =  PONE;
      c2 =  P923;
      c3 =  P382;
      c4 =  P707;
    } else if (mu==3) {
      c1 = -PONE;
      c2 =  P382;
      c3 =  P923;
      c4 = -P707;
    } else if (mu==5) {
      c1 =  PONE;
      c2 = -P382;
      c3 =  P923;
      c4 = -P707;
    } else if (mu==7) {
      c1 = -PONE;
      c2 = -P923;
      c3 =  P382;
      c4 =  P707;
    } else if (mu==9) {
      c1 =  PONE;
      c2 = -P923;
      c3 = -P382;
      c4 =  P707;
    } else if (mu==11) {
      c1 = -PONE;
      c2 = -P382;
      c3 = -P923;
      c4 = -P707;
    } else if (mu==13) {
      c1 =  PONE;
      c2 =  P382;
      c3 = -P923;
      c4 = -P707;
    } else {
      c1 = -PONE;
      c2 =  P923;
      c3 = -P382;
      c4 =  P707;
    }
    c5 = c1*c4;
    c6 = c1*c3;
    c7 = c1*c2;
    for (int i=0; i<m; ++i) {
      float[] zj0 = z[j0];
      float[] zj1 = z[j1];
      float[] zj2 = z[j2];
      float[] zj3 = z[j3];
      float[] zj4 = z[j4];
      float[] zj5 = z[j5];
      float[] zj6 = z[j6];
      float[] zj7 = z[j7];
      float[] zj8 = z[j8];
      float[] zj9 = z[j9];
      float[] zj10 = z[j10];
      float[] zj11 = z[j11];
      float[] zj12 = z[j12];
      float[] zj13 = z[j13];
      float[] zj14 = z[j14];
      float[] zj15 = z[j15];
      for (int i1=0; i1<m1; i1+=2) {
        float t1r  = zj0[i1  ]+zj8[i1  ];
        float t1i  = zj0[i1+1]+zj8[i1+1];
        float t2r  = zj4[i1  ]+zj12[i1  ];
        float t2i  = zj4[i1+1]+zj12[i1+1];
        float t3r  = zj0[i1  ]-zj8[i1  ];
        float t3i  = zj0[i1+1]-zj8[i1+1];
        float t4r  = c1*(zj4[i1  ]-zj12[i1  ]);
        float t4i  = c1*(zj4[i1+1]-zj12[i1+1]);
        float t5r  = t1r+t2r;
        float t5i  = t1i+t2i;
        float t6r  = t1r-t2r;
        float t6i  = t1i-t2i;
        float t7r  = zj1[i1  ]+zj9[i1  ];
        float t7i  = zj1[i1+1]+zj9[i1+1];
        float t8r  = zj5[i1  ]+zj13[i1  ];
        float t8i  = zj5[i1+1]+zj13[i1+1];
        float t9r  = zj1[i1  ]-zj9[i1  ];
        float t9i  = zj1[i1+1]-zj9[i1+1];
        float t10r = zj5[i1  ]-zj13[i1  ];
        float t10i = zj5[i1+1]-zj13[i1+1];
        float t11r = t7r+t8r;
        float t11i = t7i+t8i;
        float t12r = t7r-t8r;
        float t12i = t7i-t8i;
        float t13r = zj2[i1  ]+zj10[i1  ];
        float t13i = zj2[i1+1]+zj10[i1+1];
        float t14r = zj6[i1  ]+zj14[i1  ];
        float t14i = zj6[i1+1]+zj14[i1+1];
        float t15r = zj2[i1  ]-zj10[i1  ];
        float t15i = zj2[i1+1]-zj10[i1+1];
        float t16r = zj6[i1  ]-zj14[i1  ];
        float t16i = zj6[i1+1]-zj14[i1+1];
        float t17r = t13r+t14r;
        float t17i = t13i+t14i;
        float t18r = c4*(t15r-t16r);
        float t18i = c4*(t15i-t16i);
        float t19r = c5*(t15r+t16r);
        float t19i = c5*(t15i+t16i);
        float t20r = c1*(t13r-t14r);
        float t20i = c1*(t13i-t14i);
        float t21r = zj3[i1  ]+zj11[i1  ];
        float t21i = zj3[i1+1]+zj11[i1+1];
        float t22r = zj7[i1  ]+zj15[i1  ];
        float t22i = zj7[i1+1]+zj15[i1+1];
        float t23r = zj3[i1  ]-zj11[i1  ];
        float t23i = zj3[i1+1]-zj11[i1+1];
        float t24r = zj7[i1  ]-zj15[i1  ];
        float t24i = zj7[i1+1]-zj15[i1+1];
        float t25r = t21r+t22r;
        float t25i = t21i+t22i;
        float t26r = t21r-t22r;
        float t26i = t21i-t22i;
        float t27r = t9r+t24r;
        float t27i = t9i+t24i;
        float t28r = t10r+t23r;
        float t28i = t10i+t23i;
        float t29r = t9r-t24r;
        float t29i = t9i-t24i;
        float t30r = t10r-t23r;
        float t30i = t10i-t23i;
        float t31r = t5r+t17r;
        float t31i = t5i+t17i;
        float t32r = t11r+t25r;
        float t32i = t11i+t25i;
        float t33r = t3r+t18r;
        float t33i = t3i+t18i;
        float t34r = c2*t29r-c6*t30r;
        float t34i = c2*t29i-c6*t30i;
        float t35r = t3r-t18r;
        float t35i = t3i-t18i;
        float t36r = c7*t27r-c3*t28r;
        float t36i = c7*t27i-c3*t28i;
        float t37r = t4r+t19r;
        float t37i = t4i+t19i;
        float t38r = c3*t27r+c7*t28r;
        float t38i = c3*t27i+c7*t28i;
        float t39r = t4r-t19r;
        float t39i = t4i-t19i;
        float t40r = c6*t29r+c2*t30r;
        float t40i = c6*t29i+c2*t30i;
        float t41r = c4*(t12r-t26r);
        float t41i = c4*(t12i-t26i);
        float t42r = c5*(t12r+t26r);
        float t42i = c5*(t12i+t26i);
        float y1r  = t33r+t34r;
        float y1i  = t33i+t34i;
        float y2r  = t6r+t41r;
        float y2i  = t6i+t41i;
        float y3r  = t35r+t40r;
        float y3i  = t35i+t40i;
        float y4r  = t5r-t17r;
        float y4i  = t5i-t17i;
        float y5r  = t35r-t40r;
        float y5i  = t35i-t40i;
        float y6r  = t6r-t41r;
        float y6i  = t6i-t41i;
        float y7r  = t33r-t34r;
        float y7i  = t33i-t34i;
        float y9r  = t38r-t37r;
        float y9i  = t38i-t37i;
        float y10r = t42r-t20r;
        float y10i = t42i-t20i;
        float y11r = t36r+t39r;
        float y11i = t36i+t39i;
        float y12r = c1*(t11r-t25r);
        float y12i = c1*(t11i-t25i);
        float y13r = t36r-t39r;
        float y13i = t36i-t39i;
        float y14r = t42r+t20r;
        float y14i = t42i+t20i;
        float y15r = t38r+t37r;
        float y15i = t38i+t37i;
        zj0[i1  ]  = t31r+t32r;
        zj0[i1+1]  = t31i+t32i;
        zj1[i1  ]  = y1r-y15i;
        zj1[i1+1]  = y1i+y15r;
        zj2[i1  ]  = y2r-y14i;
        zj2[i1+1]  = y2i+y14r;
        zj3[i1  ]  = y3r-y13i;
        zj3[i1+1]  = y3i+y13r;
        zj4[i1  ]  = y4r-y12i;
        zj4[i1+1]  = y4i+y12r;
        zj5[i1  ]  = y5r-y11i;
        zj5[i1+1]  = y5i+y11r;
        zj6[i1  ]  = y6r-y10i;
        zj6[i1+1]  = y6i+y10r;
        zj7[i1  ]  = y7r-y9i;
        zj7[i1+1]  = y7i+y9r;
        zj8[i1  ]  = t31r-t32r;
        zj8[i1+1]  = t31i-t32i;
        zj9[i1  ]  = y7r+y9i;
        zj9[i1+1]  = y7i-y9r;
        zj10[i1  ] = y6r+y10i;
        zj10[i1+1] = y6i-y10r;
        zj11[i1  ] = y5r+y11i;
        zj11[i1+1] = y5i-y11r;
        zj12[i1  ] = y4r+y12i;
        zj12[i1+1] = y4i-y12r;
        zj13[i1  ] = y3r+y13i;
        zj13[i1+1] = y3i-y13r;
        zj14[i1  ] = y2r+y14i;
        zj14[i1+1] = y2i-y14r;
        zj15[i1  ] = y1r+y15i;
        zj15[i1+1] = y1i-y15r;
      }
      int jt = j15+1;
      j15 = j14+1;
      j14 = j13+1;
      j13 = j12+1;
      j12 = j11+1;
      j11 = j10+1;
      j10 = j9+1;
      j9 = j8+1;
      j8 = j7+1;
      j7 = j6+1;
      j6 = j5+1;
      j5 = j4+1;
      j4 = j3+1;
      j3 = j2+1;
      j2 = j1+1;
      j1 = j0+1;
      j0 = jt;
    }
  }

  // Constants used in this implementation of the prime-factor FFT.
  private static final float P120 = 0.120536680f;
  private static final float P142 = 0.142314838f;
  private static final float P173 = 0.173648178f;
  private static final float P222 = 0.222520934f;
  private static final float P239 = 0.239315664f;
  private static final float P281 = 0.281732557f;
  private static final float P342 = 0.342020143f;
  private static final float P354 = 0.354604887f;
  private static final float P382 = 0.382683432f;
  private static final float P415 = 0.415415013f;
  private static final float P433 = 0.433883739f;
  private static final float P464 = 0.464723172f;
  private static final float P540 = 0.540640817f;
  private static final float P559 = 0.559016994f;
  private static final float P568 = 0.568064747f;
  private static final float P587 = 0.587785252f;
  private static final float P623 = 0.623489802f;
  private static final float P642 = 0.642787610f;
  private static final float P654 = 0.654860734f;
  private static final float P663 = 0.663122658f;
  private static final float P707 = 0.707106781f;
  private static final float P748 = 0.748510748f;
  private static final float P755 = 0.755749574f;
  private static final float P766 = 0.766044443f;
  private static final float P781 = 0.781831482f;
  private static final float P822 = 0.822983866f;
  private static final float P841 = 0.841253533f;
  private static final float P866 = 0.866025404f;
  private static final float P885 = 0.885456026f;
  private static final float P900 = 0.900968868f;
  private static final float P909 = 0.909631995f;
  private static final float P923 = 0.923879533f;
  private static final float P935 = 0.935016243f;
  private static final float P939 = 0.939692621f;
  private static final float P951 = 0.951056516f;
  private static final float P959 = 0.959492974f;
  private static final float P970 = 0.970941817f;
  private static final float P974 = 0.974927912f;
  private static final float P984 = 0.984807753f;
  private static final float P989 = 0.989821442f;
  private static final float P992 = 0.992708874f;
  private static final float PONE = 1.000000000f;

  // Factors supported in this implementation of the prime-factor FFT.
  // Methods above require that these factors be in descending order.
  private static final int NFAC = 10;
  private static final int _kfac[] = {
    16, 13, 11, 9, 8, 7, 5, 4, 3, 2
  };

  // FFT lengths supported in this implementation of the prime-factor FFT.
  // These lengths are the products of mutually prime factors chosen from 
  // the set above. For example, note that 17 and 32 are not in this table;
  // 17 is not in the set above, and 32 = 2*16 is not valid, because the 
  // factors 2 and 16 share the prime factor 2.
  private static final int NTABLE = 240;
  private static final int _ntable[] = {
         1, 2, 3, 4, 5, 6, 7, 8, 9, 
        10, 11, 12, 13, 14, 15, 16, 18, 20, 21, 22, 24, 26, 28, 30, 33, 
        35, 36, 39, 40, 42, 44, 45, 48, 52, 55, 56, 60, 63, 65, 66, 70, 
        72, 77, 78, 80, 84, 88, 90, 91, 99,
       104, 105, 110, 112, 117, 120, 126, 130, 132, 140, 143, 144, 154, 
       156, 165, 168, 176, 180, 182, 195, 198, 208, 210, 220, 231, 234, 
       240, 252, 260, 264, 273, 280, 286, 308, 312, 315, 330, 336, 360, 
       364, 385, 390, 396, 420, 429, 440, 455, 462, 468, 495, 504, 520, 
       528, 546, 560, 572, 585, 616, 624, 630, 660, 693, 715, 720, 728, 
       770, 780, 792, 819, 840, 858, 880, 910, 924, 936, 990,
      1001, 1008, 1040, 1092, 1144, 1155, 1170, 1232, 1260, 1287, 1320, 
      1365, 1386, 1430, 1456, 1540, 1560, 1584, 1638, 1680, 1716, 1820, 
      1848, 1872, 1980, 2002, 2145, 2184, 2288, 2310, 2340, 2520, 2574, 
      2640, 2730, 2772, 2860, 3003, 3080, 3120, 3276, 3432, 3465, 3640, 
      3696, 3960, 4004, 4095, 4290, 4368, 4620, 4680, 5005, 5040, 5148, 
      5460, 5544, 5720, 6006, 6160, 6435, 6552, 6864, 6930, 7280, 7920, 
      8008, 8190, 8580, 9009, 9240, 9360,
     10010, 10296, 10920, 11088, 11440, 12012, 12870, 13104, 13860, 
     15015, 16016, 16380, 17160, 18018, 18480, 20020, 20592, 21840, 
     24024, 25740, 27720, 30030, 32760, 34320, 36036, 40040, 45045, 
     48048, 51480, 55440, 60060, 65520, 72072, 80080, 90090,
    102960, 120120, 144144, 180180, 240240, 360360, 720720
  };
}
