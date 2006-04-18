/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Local cross-correlation of two arrays with overlapping Gaussian windows.
 * Given two input arrays f and g and a specified lag, this filter computes 
 * an output array r of local cross-correlation coefficients. The output 
 * coefficients are local in that each sample in the array r contains the 
 * cross-correlation of the arrays f and g multiplied by a Gaussian window
 * that is centered at that sample. In other words, for each sample in the
 * input arrays f and g, this filter computes an output local cross-correlation
 * coefficient r.
 * <p>
 * <em>Warning: not yet completed or optimized for performance.</em>
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.22
 */
public class LocalCorrelationFilter {

  /**
   * Construct a correlation filter with specified half-width.
   * When applied to multi-dimensional arrays, the filter has the
   * same half-width for all dimensions.
   * @param sigma the correlation window half-width; must not be less than 1.
   */
  public LocalCorrelationFilter(double sigma) {
    this(sigma,sigma,sigma);
  }

  /**
   * Construct a correlation filter with specified half-widths.
   * When applied to multi-dimensional arrays, the filter has half-width 
   * sigma1 for the 1st dimension and half-width sigma2 for 2nd and higher 
   * dimensions.
   * @param sigma1 correlation window half-width for 1st dimension; 
   *  must not be less than 1.
   * @param sigma2 correlation window half-width for 2nd and higher 
   *  dimensions; must not be less than 1.
   */
  public LocalCorrelationFilter(double sigma1, double sigma2) {
    this(sigma1,sigma2,sigma2);
  }

  /**
   * Construct a correlation filter with specified widths.
   * When applied to multi-dimensional arrays, the filter has half-width 
   * sigma1 for the 1st dimension, half-width sigma2 for the 2nd dimension,
   * and half-width sigma3 for 3rd and higher dimensions.
   * @param sigma1 correlation window half-width for 1st dimension; 
   *  must not be less than 1.
   * @param sigma2 correlation window half-width for 2nd dimension;
   *  must not be less than 1.
   * @param sigma3 correlation window half-width for 3rd and higher 
   * dimensions; must not be less than 1.
   */
  public LocalCorrelationFilter(double sigma1, double sigma2, double sigma3) {
    Check.argument(sigma1>=1.0,"sigma1>=1.0");
    Check.argument(sigma2>=1.0,"sigma2>=1.0");
    Check.argument(sigma3>=1.0,"sigma3>=1.0");
    _sigma1 = sigma1;
    _sigma2 = sigma2;
    _sigma3 = sigma3;
    _rgf1 = new RecursiveGaussianFilter(sigma1/sqrt(2.0));
    _rgf2 = new RecursiveGaussianFilter(sigma2/sqrt(2.0));
    _rgf3 = new RecursiveGaussianFilter(sigma3/sqrt(2.0));
  }

  /**
   * Applies this correlation filter for the specified lag.
   * @param lag the lag.
   * @param f the 1st input array; can be the same as g.
   * @param g the 2nd input array; can be the same as f.
   * @param c the output array; cannot be the same as f or g.
   */
  public void apply(int lag, float[] f, float[] g, float[] c) {
    Check.argument(f!=c,"f!=c");
    Check.argument(g!=c,"g!=c");
    //  lag  l1f  l1g
    //  -2   -1   -1
    //  -1   -1    0
    //   0    0    0
    //   1    0    1
    //   2    1    1
    int n1 = f.length;
    int l1 = lag;
    int l1f = (l1>=0)?(l1+0)/2:(l1-1)/2;
    int l1g = (l1>=0)?(l1+1)/2:(l1+0)/2;
    double scale1 = sqrt(PI)*_sigma1*exp((-0.25*l1*l1)/(_sigma1*_sigma1));
    float scale = (float)scale1;
    int i1min = max(abs(l1f),abs(l1g));
    int i1max = n1-i1min;
    float[] h = new float[n1];
    for (int i1=i1min; i1<i1max; ++i1) {
      c[i1] = scale*f[i1-l1f]*g[i1+l1g];
    }
    if (l1f!=l1g)
      shift(c,h);
    else
      Array.copy(c,h);
    _rgf1.apply0(h,c);
  }

  public void apply(
    int lag1, int lag2, float[][] f, float[][] g, float[][] c) 
  {
    Check.argument(f!=c,"f!=c");
    Check.argument(g!=c,"g!=c");
    int n1 = f[0].length;
    int l1 = lag1;
    int l1f = (l1>=0)?(l1+0)/2:(l1-1)/2;
    int l1g = (l1>=0)?(l1+1)/2:(l1+0)/2;
    int n2 = f.length;
    int l2 = lag2;
    int l2f = (l2>=0)?(l2+0)/2:(l2-1)/2;
    int l2g = (l2>=0)?(l2+1)/2:(l2+0)/2;
    double scale1 = sqrt(PI)*_sigma1*exp((-0.25*l1*l1)/(_sigma1*_sigma1));
    double scale2 = sqrt(PI)*_sigma2*exp((-0.25*l2*l2)/(_sigma2*_sigma2));
    float scale = (float)(scale1*scale2);
    int i1min = max(abs(l1f),abs(l1g));
    int i1max = n1-i1min;
    int i2min = max(abs(l2f),abs(l2g));
    int i2max = n2-i2min;
    float[][] h = new float[n2][n1];
    for (int i2=i2min; i2<i2max; ++i2) {
      float[] f2 = f[i2-l2f];
      float[] g2 = g[i2+l2g];
      float[] c2 = c[i2];
      for (int i1=i1min; i1<i1max; ++i1) {
        c2[i1] = scale*f2[i1-l1f]*g2[i1+l1g];
      }
    }
    if (l1f!=l1g)
      shift1(c,h);
    else
      Array.copy(c,h);
    if (l2f!=l2g)
      shift2(h,c);
    else
      Array.copy(h,c);
    _rgf1.apply0X(c,h);
    _rgf2.applyX0(h,c);
  }

  public void apply(
    int lag1, int lag2, int lag3, float[][][] f, float[][][] g, float[][][] c) 
  {
    Check.argument(f!=c,"f!=c");
    Check.argument(g!=c,"g!=c");
    int n1 = f[0][0].length;
    int l1 = lag1;
    int l1f = (l1>=0)?(l1+0)/2:(l1-1)/2;
    int l1g = (l1>=0)?(l1+1)/2:(l1+0)/2;
    int n2 = f[0].length;
    int l2 = lag2;
    int l2f = (l2>=0)?(l2+0)/2:(l2-1)/2;
    int l2g = (l2>=0)?(l2+1)/2:(l2+0)/2;
    int n3 = f.length;
    int l3 = lag3;
    int l3f = (l3>=0)?(l3+0)/2:(l3-1)/2;
    int l3g = (l3>=0)?(l3+1)/2:(l3+0)/2;
    double scale1 = sqrt(PI)*_sigma1*exp((-0.25*l1*l1)/(_sigma1*_sigma1));
    double scale2 = sqrt(PI)*_sigma2*exp((-0.25*l2*l2)/(_sigma2*_sigma2));
    double scale3 = sqrt(PI)*_sigma3*exp((-0.25*l3*l3)/(_sigma3*_sigma3));
    float scale = (float)(scale1*scale2*scale3);
    int i1min = max(abs(l1f),abs(l1g));
    int i1max = n1-i1min;
    int i2min = max(abs(l2f),abs(l2g));
    int i2max = n2-i2min;
    int i3min = max(abs(l3f),abs(l3g));
    int i3max = n3-i3min;
    float[][][] h = new float[n3][n2][n1];
    for (int i3=i3min; i3<i3max; ++i3) {
      for (int i2=i2min; i2<i2max; ++i2) {
        float[] f32 = f[i3-l3f][i2-l2f];
        float[] g32 = g[i3+l3g][i2+l2g];
        float[] c32 = h[i3][i2];
        for (int i1=i1min; i1<i1max; ++i1) {
          c32[i1] = scale*f32[i1-l1f]*g32[i1+l1g];
        }
      }
    }
    if (l1f!=l1g)
      ;//shift1(c,h);
    else
      Array.copy(c,h);
    if (l2f!=l2g)
      ;//shift2(h,c);
    else
      Array.copy(h,c);
    if (l3f!=l3g)
      ;//shift3(c,h);
    else
      Array.copy(c,h);
    _rgf1.apply0XX(h,c);
    _rgf2.applyX0X(c,h);
    _rgf3.applyXX0(h,c);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private
  private double _sigma1;
  private double _sigma2;
  private double _sigma3;
  private RecursiveGaussianFilter _rgf1;
  private RecursiveGaussianFilter _rgf2;
  private RecursiveGaussianFilter _rgf3;

  // Kaiser-windowed sinc interpolation coefficients for half-sample shifts.
  private static float S1 =  0.6157280f;
  private static float S2 = -0.1558022f;
  private static float S3 =  0.0509014f;
  private static float S4 = -0.0115417f;
  private static float[] S = {S4,S3,S2,S1,S1,S2,S3,S4};

  private static void shift(float[] f, float[] g) {
    int n1 = f.length;
    int i1b,i1e;

    // Rolling on.
    i1b = 0;
    i1e = min(4,n1);
    for (int i1=i1b; i1<i1e; ++i1) {
      int ib = max(0,4-i1);
      int ie = min(8,4-i1+n1);
      g[i1] = 0.0f;
      for (int i=ib; i<ie; ++i)
        g[i1] += S[i]*f[i1+i-4];
    }

    // Middle.
    i1b = 4;
    i1e = n1-3;
    for (int i1=i1b; i1<i1e; ++i1) {
      g[i1] = S4*(f[i1-4]+f[i1+3]) +
              S3*(f[i1-3]+f[i1+2]) +
              S2*(f[i1-2]+f[i1+1]) +
              S1*(f[i1-1]+f[i1  ]);
    }

    // Rolling off.
    i1b = max(0,n1-3);
    i1e = n1;
    for (int i1=i1b; i1<i1e; ++i1) {
      int ib = max(0,4-i1);
      int ie = min(8,4-i1+n1);
      g[i1] = 0.0f;
      for (int i=ib; i<ie; ++i)
        g[i1] += S[i]*f[i1+i-4];
    }
  }

  private static void shift1(float[][] f, float[][] g) {
    int n2 = f.length;
    for (int i2=0; i2<n2; ++i2)
      shift(f[i2],g[i2]);
  }

  private static void shift2(float[][] f, float[][] g) {
    int n2 = f.length;
    int n1 = f[0].length;
    int i2b,i2e;

    // Rolling on.
    i2b = 0;
    i2e = min(4,n2);
    for (int i2=i2b; i2<i2e; ++i2) {
      int ib = max(0,4-i2);
      int ie = min(8,4-i2+n2);
      for (int i1=0; i1<n1; ++i1)
        g[i2][i1] = 0.0f;
      for (int i=ib; i<ie; ++i)
        for (int i1=0; i1<n1; ++i1)
          g[i2][i1] += S[i]*f[i2+i-4][i1];
    }

    // Middle.
    i2b = 4;
    i2e = n2-3;
    for (int i2=i2b; i2<i2e; ++i2) {
      float[] g2 = g[i2];
      float[] fm4 = f[i2-4];
      float[] fm3 = f[i2-3];
      float[] fm2 = f[i2-2];
      float[] fm1 = f[i2-1];
      float[] fp0 = f[i2  ];
      float[] fp1 = f[i2+1];
      float[] fp2 = f[i2+2];
      float[] fp3 = f[i2+3];
      for (int i1=0; i1<n1; ++i1)
        g2[i1] = S4*(fm4[i1]+fp3[i1]) +
                 S3*(fm3[i1]+fp2[i1]) +
                 S2*(fm2[i1]+fp1[i1]) +
                 S1*(fm1[i1]+fp0[i1]);
    }

    // Rolling off.
    i2b = max(0,n2-3);
    i2e = n2;
    for (int i2=i2b; i2<i2e; ++i2) {
      int ib = max(0,4-i2);
      int ie = min(8,4-i2+n2);
      for (int i1=0; i1<n1; ++i1)
        g[i2][i1] = 0.0f;
      for (int i=ib; i<ie; ++i)
        for (int i1=0; i1<n1; ++i1)
          g[i2][i1] += S[i]*f[i2+i-4][i1];
    }
  }

  private static float[] makeWindow(double sigma) {
    int m = 1+2*(int)(4.0*sigma);
    int j = (m-1)/2;
    float[] w = new float[m];
    double s = -0.5/(sigma*sigma);
    for (int i=0; i<m; ++i) {
      double x = i-j;
      w[i] = (float)exp(s*x*x);
    }
    return w;
  }

  /**
   * Multiplies a specified array by a specified window. The window 
   * must have odd length, so that its center is uniquely defined.
   * Multiplication is limited to within the bounds of the specified arrays.
   * @param w the window array.
   * @param jf the index of the input array f at which to center the window.
   * @param f the input array.
   * @param jg the index of the output array g corresponding to index jf.
   * @param g the output array.
   */
  public static void applyWindow(
    float[] w, 
    int jf, float[] f, 
    int jg, float[] g) 
  {
    int nf = f.length;
    int ng = g.length;
    int nw = w.length;
    jf -= (nw-1)/2;
    jg -= (nw-1)/2;
    int imin = max(0,-jf,-jg);
    int imax = min(nw,nf-jf,ng-jg);
    for (int i=imin; i<imax; ++i)
      g[jg+i] = w[i]*f[jf+i];
  }

  /**
   * Multiplies a specified array by a specified separable window. The
   * multi-dimensional window is the product of multiple 1-D windows. All 
   * windows must have odd length, to make their centers uniquely defined.
   * Multiplication is limited to within the bounds of the specified arrays.
   * @param w1 the window array for the 1st dimension.
   * @param w2 the window array for the 2nd dimension.
   * @param j1f the index in the 1st dimension of the input array f at 
   *  which to center the window.
   * @param j2f the index in the 2nd dimension of the input array f at 
   *  which to center the window.
   * @param f the input array.
   * @param j1g the index of the output array g corresponding to index j1f.
   * @param j2g the index of the output array g corresponding to index j2f.
   * @param g the output array.
   */
  public static void applyWindow(
    float[] w1, float[] w2, 
    int j1f, int j2f, float[][] f, 
    int j1g, int j2g, float[][] g) 
  {
    int n1f = f[0].length;
    int n1g = g[0].length;
    int n1w = w1.length;
    j1f -= (n1w-1)/2;
    j1g -= (n1w-1)/2;
    int i1min = max(0,-j1f,-j1g);
    int i1max = min(n1w,n1f-j1f,n1g-j1g);
    int n2f = f.length;
    int n2g = g.length;
    int n2w = w2.length;
    j2f -= (n2w-1)/2;
    j2g -= (n2w-1)/2;
    int i2min = max(0,-j2f,-j2g);
    int i2max = min(n2w,n2f-j2f,n2g-j2g);
    for (int i2=i2min; i2<i2max; ++i2) {
      float w2i2 = w2[i2];
      float[] fi2 = f[j2f+i2];
      float[] gi2 = g[j2g+i2];
      for (int i1=i1min; i1<i1max; ++i1) {
        gi2[j1g+i1] = w2i2*w1[i1]*fi2[j1f+i1];
      }
    }
  }

  public void apply(
    int l1min, int l1max, int j1c, int k1c,
    float[] f, float[] g, float[][] c)
  {
    int n1f = f.length;
    int n1c = c.length;
    float[] t = new float[n1f];
    for (int l1=l1min; l1<=l1max; ++l1) {
      apply(l1,f,g,t);
      for (int i1c=0; i1c<n1c; ++i1c) {
        c[i1c][l1-l1min] = t[j1c+i1c*k1c];
      }
    }
  }

  public void applyFft(
    int l1min, int l1max, int j1c, int k1c,
    float[] f, float[] g, float[][] c)
  {
    float[] w1 = makeWindow(_sigma1);
    int n1f = f.length;
    int n1c = c.length;
    int n1w = w1.length;
    int n1h = (n1w-1)/2;
    int n1l = l1max-l1min+1;
    int n1p = n1w+max(-l1min,n1l-1+l1min);
    int n1fft = FftReal.nfftFast(n1p);
    int n1pad = n1fft+2;
    FftReal fft = new FftReal(n1fft);
    float[] fpad = new float[n1pad];
    float[] gpad = new float[n1pad];
    int j1f = max(0, l1min);
    int j1g = max(0,-l1min);
    for (int i1c=0; i1c<n1c; ++i1c) {
      int m1c = j1c+i1c*k1c;
      Array.zero(fpad);
      Array.zero(gpad);
      applyWindow(w1,m1c,f,m1c+j1f,fpad);
      applyWindow(w1,m1c,g,m1c+j1g,gpad);
      fft.realToComplex(-1,fpad,fpad);
      fft.realToComplex(-1,gpad,gpad);
      for (int i1=0; i1<n1pad; i1+=2) {
        float fr = fpad[i1  ];
        float fi = fpad[i1+1];
        float gr = gpad[i1  ];
        float gi = gpad[i1+1];
        gpad[i1  ] = fr*gr+fi*gi;
        gpad[i1+1] = fr*gi-fi*gr;
      }
      fft.complexToReal(1,gpad,gpad);
      float s = 1.0f/(float)n1fft;
      float[] cc = c[i1c];
      for (int i1=0; i1<n1l; ++i1)
        cc[i1] = s*gpad[i1];
    }
  }
}
