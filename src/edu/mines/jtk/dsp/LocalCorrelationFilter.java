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

  /**
   * Applies this correlation filter for the specified lag.
   * @param lag1 the lag in the 1st dimension.
   * @param lag2 the lag in the 2nd dimension.
   * @param f the 1st input array; can be the same as g.
   * @param g the 2nd input array; can be the same as f.
   * @param c the output array; cannot be the same as f or g.
   */
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

  /**
   * Applies this correlation filter for the specified lag.
   * @param lag1 the lag in the 1st dimension.
   * @param lag2 the lag in the 2nd dimension.
   * @param lag3 the lag in the 3rd dimension.
   * @param f the 1st input array; can be the same as g.
   * @param g the 2nd input array; can be the same as f.
   * @param c the output array; cannot be the same as f or g.
   */
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
        float[] c32 = c[i3][i2];
        for (int i1=i1min; i1<i1max; ++i1) {
          c32[i1] = scale*f32[i1-l1f]*g32[i1+l1g];
        }
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
    if (l3f!=l3g)
      shift3(c,h);
    else
      Array.copy(c,h);
    _rgf1.apply0XX(h,c);
    _rgf2.applyX0X(c,h);
    _rgf3.applyXX0(h,c);
  }

  /**
   * Applies this correlation filter for the specified lags.
   * The number of lags is nl1 = l1max-l1min+1.
   * @param l1min the minimum lag in the 1st dimension.
   * @param l1max the maximum lag in the 1st dimension.
   * @param j1c the sample index of the first correlation.
   * @param k1c the sample stride between correlations.
   * @param f the 1st input array; can be the same as g.
   * @param g the 2nd input array; can be the same as f.
   * @param c the output array; cannot be the same as f or g.
   */
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

  /**
   * Like {@link #apply(int,int,int,int,float[],float[],float[][])}, but
   * uses conventional windowing and FFTs to perform the cross-correlations.
   * Best for small numbers of cross-correlation windows.
   * The number of lags is nl1 = l1max-l1min+1.
   * @param l1min the minimum lag in the 1st dimension.
   * @param l1max the maximum lag in the 1st dimension.
   * @param j1c the sample index of the first correlation.
   * @param k1c the sample stride between correlations.
   * @param f the 1st input array; can be the same as g.
   * @param g the 2nd input array; can be the same as f.
   * @param c the output array; cannot be the same as f or g.
   */
  // Not yet tested!
  private void applyFft(
    int l1min, int l1max, int j1c, int k1c,
    float[] f, float[] g, float[][] c)
  {
    float[] w1 = makeGaussianWindow(_sigma1);
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
      applyWindow(w1,m1c,f,n1h+j1f,fpad);
      applyWindow(w1,m1c,g,n1h+j1g,gpad);
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

  // Not yet tested!
  private void applyFft(
    int l1min, int l1max, int j1c, int k1c,
    int l2min, int l2max, int j2c, int k2c,
    float[][] f, float[][] g, float[][][] c)
  {
    float[] w1 = makeGaussianWindow(_sigma1);
    float[] w2 = makeGaussianWindow(_sigma2);
    int j1f = max(0, l1min);
    int j1g = max(0,-l1min);
    int n1f = f[0].length;
    int n1g = g[0].length;
    int n1c = c[0].length;
    int n1w = w1.length;
    int n1h = (n1w-1)/2;
    int n1l = l1max-l1min+1;
    int n1p = n1w+max(-l1min,n1l-1+l1min);
    int n1fft = FftReal.nfftFast(n1p);
    int n1pad = n1fft+2;
    int j2f = max(0, l2min);
    int j2g = max(0,-l2min);
    int n2f = f.length;
    int n2g = g.length;
    int n2c = c.length;
    int n2w = w2.length;
    int n2h = (n2w-1)/2;
    int n2l = l2max-l2min+1;
    int n2p = n2w+max(-l2min,n2l-1+l2min);
    int n2fft = FftComplex.nfftFast(n2p);
    int n2pad = n2fft*2;
    FftReal fft1 = new FftReal(n1fft);
    FftComplex fft2 = new FftComplex(n2fft);
    float[][] fpad = new float[n2pad][n1pad];
    float[][] gpad = new float[n2pad][n1pad];
    for (int i2c=0; i2c<n2c; ++i2c) {
      int m2c = j2c+i2c*k2c;
      for (int i1c=0; i1c<n1c; ++i1c) {
        int m1c = j1c+i1c*k1c;
        Array.zero(fpad);
        Array.zero(gpad);
        applyWindow(w1,w2,m1c,m2c,f,n1h+j1f,n2h+j2f,fpad);
        applyWindow(w1,w2,m1c,m2c,g,n1h+j1g,n2h+j2g,gpad);
        fft1.realToComplex1(-1,n2p,fpad,fpad);
        fft1.realToComplex1(-1,n2p,gpad,gpad);
        fft2.complexToComplex2(-1,n1fft/2+1,fpad,fpad);
        fft2.complexToComplex2(-1,n1fft/2+1,gpad,gpad);
        for (int i2=0; i2<n2fft; ++i2) {
          float[] fpad2 = fpad[i2];
          float[] gpad2 = gpad[i2];
          for (int i1=0; i1<n1pad; i1+=2) {
            float fr = fpad2[i1  ];
            float fi = fpad2[i1+1];
            float gr = gpad2[i1  ];
            float gi = gpad2[i1+1];
            gpad2[i1  ] = fr*gr+fi*gi;
            gpad2[i1+1] = fr*gi-fi*gr;
          }
        }
        fft2.complexToComplex2(1,n1fft/2+1,gpad,gpad);
        fft1.realToComplex1(1,n2l,gpad,gpad);
        float s = 1.0f/((float)n1fft*(float)n2fft);
        float[] cc = c[i2c][i1c];
        for (int i2=0,ic=0; i2<n2l; ++i2) {
          float[] gpad2 = gpad[i2];
          for (int i1=0; i1<n1l; ++i1,++ic) {
            cc[ic] = s*gpad2[i1];
          }
        }
      }
    }
  }

  // Not yet tested!
  private void applyFft(
    int l1min, int l1max, int j1c, int k1c,
    int l2min, int l2max, int j2c, int k2c,
    int l3min, int l3max, int j3c, int k3c,
    float[][][] f, float[][][] g, float[][][][] c)
  {
    float[] w1 = makeGaussianWindow(_sigma1);
    float[] w2 = makeGaussianWindow(_sigma2);
    float[] w3 = makeGaussianWindow(_sigma3);
    int j1f = max(0, l1min);
    int j1g = max(0,-l1min);
    int n1f = f[0][0].length;
    int n1g = g[0][0].length;
    int n1c = c[0][0].length;
    int n1w = w1.length;
    int n1h = (n1w-1)/2;
    int n1l = l1max-l1min+1;
    int n1p = n1w+max(-l1min,n1l-1+l1min);
    int n1fft = FftReal.nfftFast(n1p);
    int n1pad = n1fft+2;
    int j2f = max(0, l2min);
    int j2g = max(0,-l2min);
    int n2f = f[0].length;
    int n2g = g[0].length;
    int n2c = c[0].length;
    int n2w = w2.length;
    int n2h = (n2w-1)/2;
    int n2l = l2max-l2min+1;
    int n2p = n2w+max(-l2min,n2l-1+l2min);
    int n2fft = FftComplex.nfftFast(n2p);
    int n2pad = n2fft*2;
    int j3f = max(0, l3min);
    int j3g = max(0,-l3min);
    int n3f = f.length;
    int n3g = g.length;
    int n3c = c.length;
    int n3w = w3.length;
    int n3h = (n3w-1)/2;
    int n3l = l3max-l3min+1;
    int n3p = n3w+max(-l3min,n3l-1+l3min);
    int n3fft = FftComplex.nfftFast(n3p);
    int n3pad = n3fft*2;
    FftReal fft1 = new FftReal(n1fft);
    FftComplex fft2 = new FftComplex(n2fft);
    FftComplex fft3 = new FftComplex(n3fft);
    float[][][] fpad = new float[n3pad][n2pad][n1pad];
    float[][][] gpad = new float[n3pad][n2pad][n1pad];
    for (int i3c=0; i3c<n3c; ++i3c) {
      int m3c = j3c+i3c*k3c;
      for (int i2c=0; i2c<n2c; ++i2c) {
        int m2c = j2c+i2c*k2c;
        for (int i1c=0; i1c<n1c; ++i1c) {
          int m1c = j1c+i1c*k1c;
          Array.zero(fpad);
          Array.zero(gpad);
          applyWindow(w1,w2,w3,m1c,m2c,m3c,f,n1h+j1f,n2h+j2f,j3f+n3h,fpad);
          applyWindow(w1,w2,w3,m1c,m2c,m3c,g,n1h+j1g,n2h+j2g,j3g+n3h,gpad);
          fft1.realToComplex1(-1,n2p,n3p,fpad,fpad);
          fft1.realToComplex1(-1,n2p,n3p,gpad,gpad);
          fft2.complexToComplex2(-1,n1fft/2+1,n3p,fpad,fpad);
          fft2.complexToComplex2(-1,n1fft/2+1,n3p,gpad,gpad);
          fft3.complexToComplex3(-1,n1fft/2+1,n2fft,fpad,fpad);
          fft3.complexToComplex3(-1,n1fft/2+1,n2fft,gpad,gpad);
          for (int i3=0; i3<n3fft; ++i3) {
            float[][] fpad3 = fpad[i3];
            float[][] gpad3 = gpad[i3];
            for (int i2=0; i2<n2fft; ++i2) {
              float[] fpad32 = fpad3[i2];
              float[] gpad32 = gpad3[i2];
              for (int i1=0; i1<n1pad; i1+=2) {
                float fr = fpad32[i1  ];
                float fi = fpad32[i1+1];
                float gr = gpad32[i1  ];
                float gi = gpad32[i1+1];
                gpad32[i1  ] = fr*gr+fi*gi;
                gpad32[i1+1] = fr*gi-fi*gr;
              }
            }
          }
          fft3.complexToComplex3(1,n1fft/2+1,n2fft,gpad,gpad);
          fft2.complexToComplex2(1,n1fft/2+1,n3l,gpad,gpad);
          fft1.realToComplex1(1,n2l,n3l,gpad,gpad);
          float s = 1.0f/((float)n1fft*(float)n2fft*(float)n3fft);
          float[] cc = c[i3c][i2c][i1c];
          for (int i3=0,ic=0; i3<n3l; ++i3) {
            float[][] gpad3 = gpad[i3];
            for (int i2=0; i2<n2l; ++i2) {
              float[] gpad32 = gpad3[i2];
              for (int i1=0; i1<n1l; ++i1,++ic) {
                cc[ic] = s*gpad32[i1];
              }
            }
          }
        }
      }
    }
  }

  /**
   * Searches for lags for which cross-correlations are maximized.
   * @param f the 1st input array; can be the same as g.
   * @param g the 2nd input array; can be the same as f.
   * @param min minimum lag
   * @param max maximum lag
   * @param lag output array of lags
   */
  public void findMaxLags(
    float[] f, float[] g, int min, int max, byte[] lag) 
  {
    // Initialize arrays of lags.
    int n = f.length;
    for (int i=0; i<n; ++i)
      lag[i] = 0;

    // Array for cross-correlations.
    float[] c = new float[n];
    float[] cmax = new float[n];

    // Search begins in the middle of the specified range of lags.
    Lags lags = new Lags(min,max);
    int l = (min+max)/2;

    // While lags remain to be processed, ...
    boolean done = false;
    while (!done) {

      // Apply correlation filter for this lag.
      apply(l,f,g,c);

      // Correlations have been computed for this lag, 
      // but no maxima have yet been found.
      lags.markLag(l);

      // Look for maxima; if found, mark this lag accordingly.
      boolean foundMax = false;
      for (int i=0; i<n; ++i) { 
        float ci = c[i];
        if (ci>cmax[i]) {
          cmax[i] = ci;
          lag[i] = (byte)l;
          foundMax = true;
        }
      }
      if (foundMax)
        lags.markMax(l);

      // Which lag to process next?
      int[] ls = lags.nextLag();
      if (ls==null) {
        done = true;
      } else {
        l = ls[0];
      }
    }
  }

  /**
   * Searches for lags for which cross-correlations are maximized.
   * @param f the 1st input array; can be the same as g.
   * @param g the 2nd input array; can be the same as f.
   * @param min1 minimum lag in 1st dimension
   * @param max1 maximum lag in 1st dimension
   * @param min2 minimum lag in 2nd dimension
   * @param max2 maximum lag in 2nd dimension
   * @param lag1 output array of lags in the 1st dimension.
   * @param lag2 output array of lags in the 2nd dimension.
   */
  public void findMaxLags(
    float[][] f, float[][] g, 
    int min1, int max1, int min2, int max2,
    byte[][] lag1, byte[][] lag2) 
  {
    // Initialize arrays of lags.
    int n1 = f[0].length;
    int n2 = f.length;
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        lag1[i2][i1] = 0;
        lag2[i2][i1] = 0;
      }
    }

    // Array for cross-correlations.
    float[][] c = new float[n2][n1];
    float[][] cmax = new float[n2][n1];

    // Search begins in the middle of the specified range of lags.
    Lags lags = new Lags(min1,max1,min2,max2);
    int l1 = (min1+max1)/2;
    int l2 = (min2+max2)/2;

    // While lags remain to be processed, ...
    boolean done = false;
    while (!done) {

      // Apply correlation filter for this lag.
      apply(l1,l2,f,g,c);

      // Correlations have been computed for this lag, 
      // but no maxima have yet been found.
      lags.markLag(l1,l2);

      // Look for maxima; if found, mark this lag accordingly.
      boolean foundMax = false;
      for (int i2=0; i2<n2; ++i2) { 
        float[] c2 = c[i2];
        float[] cmax2 = cmax[i2];
        for (int i1=0; i1<n1; ++i1) { 
          float ci = c2[i1];
          if (ci>cmax2[i1]) {
            cmax[i2][i1] = ci;
            lag1[i2][i1] = (byte)l1;
            lag2[i2][i1] = (byte)l2;
            foundMax = true;
          }
        }
      }
      if (foundMax)
        lags.markMax(l1,l2);

      // Which lag to process next?
      int[] ls = lags.nextLag();
      if (ls==null) {
        done = true;
      } else {
        l1 = ls[0];
        l2 = ls[1];
      }
    }
  }

  public void findMaxLags(
    float[][][] f, float[][][] g, 
    int min1, int max1, int min2, int max2, int min3, int max3,
    byte[][][] lag1, byte[][][] lag2, byte[][][] lag3) 
  {
    // Initialize arrays of lags.
    int n1 = f[0][0].length;
    int n2 = f[0].length;
    int n3 = f.length;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          lag1[i3][i2][i1] = 0;
          lag2[i3][i2][i1] = 0;
          lag3[i3][i2][i1] = 0;
        }
      }
    }

    // Array for cross-correlations.
    float[][][] c = new float[n3][n2][n1];
    float[][][] cmax = new float[n3][n2][n1];

    // Search begins in the middle of the specified range of lags.
    Lags lags = new Lags(min1,max1,min2,max2,min3,max3);
    int l1 = (min1+max1)/2;
    int l2 = (min2+max2)/2;
    int l3 = (min3+max3)/2;

    // While lags remain to be processed, ...
    boolean done = false;
    while (!done) {

      // Apply correlation filter for this lag.
      System.out.println("findMaxLags: l1="+l1+" l2="+l2+" l3="+l3);
      apply(l1,l2,l3,f,g,c);

      // Correlations have been computed for this lag, 
      // but no maxima have yet been found.
      lags.markLag(l1,l2,l3);

      // Look for maxima; if found, mark this lag accordingly.
      boolean foundMax = false;
      for (int i3=0; i3<n3; ++i3) { 
        for (int i2=0; i2<n2; ++i2) { 
          float[] c32 = c[i3][i2];
          float[] cmax32 = cmax[i3][i2];
          byte[] lag132 = lag1[i3][i2];
          byte[] lag232 = lag2[i3][i2];
          byte[] lag332 = lag3[i3][i2];
          for (int i1=0; i1<n1; ++i1) { 
            float ci = c32[i1];
            if (ci>cmax32[i1]) {
              cmax32[i1] = ci;
              lag132[i1] = (byte)l1;
              lag232[i1] = (byte)l2;
              lag332[i1] = (byte)l3;
              foundMax = true;
            }
          }
        }
      }
      if (foundMax)
        lags.markMax(l1,l2,l3);

      // Which lag to process next?
      int[] ls = lags.nextLag();
      if (ls==null) {
        done = true;
      } else {
        l1 = ls[0];
        l2 = ls[1];
        l3 = ls[2];
      }
    }
  }

  public void refineLags( float[] f, float[] g, byte[] l, float[] u) {
    int n = f.length;

    // Minimum and maximum lags.
    int min = l[0];
    int max = l[0];
    for (int i=1; i<n; ++i) {
      int lag = l[i];
      if (lag<min) min = lag;
      if (lag>max) max = lag;
    }
    System.out.println("refineLags:");
    System.out.println("  min="+min+" max="+max);

    // Coefficients for quadratic fit.
    float[] c = new float[n];
    float[] a1 = new float[n];
    float[] a2 = new float[n];
    for (int lag=min-1; lag<=max+1; ++lag) {
      apply(lag,f,g,c);
      for (int i=0; i<n; ++i) {
        int k = lag-l[i];
        if (-1<=k && k<=1) {
          k += 1;
          float[] ck = C1[k];
          float ci = c[i];
          a1[i] += ck[1]*ci;
          a2[i] += ck[2]*ci;
        }
      }
    }

    // Refined lags.
    for (int i=0; i<n; ++i) {
      float a1i = a1[i];
      float a2i = a2[i];
      float w = 0.0f;
      if (a2i<0.0)
        w = -0.5f*a1i/a2i;
      if (w<-1.0f) {
        w = -1.0f;
      } else if (w>1.0f) {
        w = 1.0f;
      }
      u[i] = (float)(w+l[i]);
    }
  }

  public void refineLags(
    float[][] f, float[][] g, 
    byte[][] l1, byte[][] l2,
    float[][] u1, float[][] u2) 
  {
    int n1 = f[0].length;
    int n2 = f.length;

    // Minimum and maximum lags.
    int min1 = l1[0][0];
    int max1 = l1[0][0];
    int min2 = l2[0][0];
    int max2 = l2[0][0];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        int lag1 = l1[i2][i1];
        int lag2 = l2[i2][i1];
        if (lag1<min1) min1 = lag1;
        if (lag1>max1) max1 = lag1;
        if (lag2<min2) min2 = lag2;
        if (lag2>max2) max2 = lag2;
      }
    }
    System.out.println("refineLags:");
    System.out.println("  min1="+min1+" max1="+max1);
    System.out.println("  min2="+min2+" max2="+max2);

    // Coefficients for quadratic fit.
    float[][] c = new float[n2][n1];
    float[][] a1 = new float[n2][n1];
    float[][] a2 = new float[n2][n1];
    float[][] a3 = new float[n2][n1];
    float[][] a4 = new float[n2][n1];
    float[][] a5 = new float[n2][n1];
    for (int lag2=min2-1; lag2<=max2+1; ++lag2) {
      for (int lag1=min1-1; lag1<=max1+1; ++lag1) {
        apply(lag1,lag2,f,g,c);
        for (int i2=0; i2<n2; ++i2) {
          for (int i1=0; i1<n1; ++i1) {
            int k1 = lag1-l1[i2][i1];
            int k2 = lag2-l2[i2][i1];
            if (-1<=k1 && k1<=1 && -1<=k2 && k2<=1) {
              int k = (k1+1)+3*(k2+1);
              float[] ck = C2[k];
              float ci = c[i2][i1];
              a1[i2][i1] += ck[1]*ci;
              a2[i2][i1] += ck[2]*ci;
              a3[i2][i1] += ck[3]*ci;
              a4[i2][i1] += ck[4]*ci;
              a5[i2][i1] += ck[5]*ci;
            }
          }
        }
      }
    }

    // Cholesky decomposition solves 2x2 system for refined lags.
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        double w1 = 0.0;
        double w2 = 0.0;
        double b1 = a1[i2][i1];
        double b2 = a2[i2][i1];
        double a21 = -a3[i2][i1];
        double a11 = -2.0*a4[i2][i1];
        double a22 = -2.0*a5[i2][i1];
        double d11 = a11;
        if (d11>0.0) {
          double l11 = sqrt(d11);
          double l21 = a21/l11;
          double d22 = a22-l21*l21;
          if (d22>0.0) {
            double l22 = sqrt(d22);
            double v1 = b1/l11;
            double v2 = (b2-l21*v1)/l22;
            w2 = v2/l22;
            w1 = (v1-l21*w2)/l11;
            if (w1<-1.0) {
              w1 = -1.0;
            } else if (w1>1.0) {
              w1 = 1.0;
            }
            if (w2<-1.0) {
              w2 = -1.0;
            } else if (w2>1.0) {
              w2 = 1.0;
            }
          }
        }
        u1[i2][i1] = (float)(w1+l1[i2][i1]);
        u2[i2][i1] = (float)(w2+l2[i2][i1]);
      }
    }
  }

  public void refineLags(
    float[][][] f, float[][][] g, 
    byte[][][] l1, byte[][][] l2, byte[][][] l3,
    float[][][] u1, float[][][] u2, float[][][] u3) 
  {
    int n1 = f[0][0].length;
    int n2 = f[0].length;
    int n3 = f.length;

    // Minimum and maximum lags.
    int min1 = l1[0][0][0];
    int max1 = l1[0][0][0];
    int min2 = l2[0][0][0];
    int max2 = l2[0][0][0];
    int min3 = l3[0][0][0];
    int max3 = l3[0][0][0];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          int lag1 = l1[i3][i2][i1];
          int lag2 = l2[i3][i2][i1];
          int lag3 = l3[i3][i2][i1];
          if (lag1<min1) min1 = lag1;
          if (lag1>max1) max1 = lag1;
          if (lag2<min2) min2 = lag2;
          if (lag2>max2) max2 = lag2;
          if (lag3<min3) min3 = lag3;
          if (lag3>max3) max3 = lag3;
        }
      }
    }
    System.out.println("refineLags:");
    System.out.println("  min1="+min1+" max1="+max1);
    System.out.println("  min2="+min2+" max2="+max2);
    System.out.println("  min3="+min3+" max3="+max3);

    // Coefficients for quadratic fit.
    float[][][] c = new float[n3][n2][n1];
    float[][][] a1 = new float[n3][n2][n1];
    float[][][] a2 = new float[n3][n2][n1];
    float[][][] a3 = new float[n3][n2][n1];
    float[][][] a4 = new float[n3][n2][n1];
    float[][][] a5 = new float[n3][n2][n1];
    float[][][] a6 = new float[n3][n2][n1];
    float[][][] a7 = new float[n3][n2][n1];
    float[][][] a8 = new float[n3][n2][n1];
    float[][][] a9 = new float[n3][n2][n1];
    for (int lag3=min3-1; lag3<=max3+1; ++lag3) {
      for (int lag2=min2-1; lag2<=max2+1; ++lag2) {
        for (int lag1=min1-1; lag1<=max1+1; ++lag1) {
          System.out.println("("+lag1+","+lag2+","+lag3+")");
          apply(lag1,lag2,lag3,f,g,c);
          for (int i3=0; i3<n3; ++i3) {
            for (int i2=0; i2<n2; ++i2) {
              for (int i1=0; i1<n1; ++i1) {
                int k1 = lag1-l1[i3][i2][i1];
                int k2 = lag2-l2[i3][i2][i1];
                int k3 = lag3-l3[i3][i2][i1];
                if (-1<=k1 && k1<=1 && -1<=k2 && k2<=1 && -1<=k3 &&  k3<=1) {
                  int k = (k1+1)+3*(k2+1)+9*(k3+1);
                  float[] ck = C3[k];
                  float ci = c[i3][i2][i1];
                  a1[i3][i2][i1] += ck[1]*ci;
                  a2[i3][i2][i1] += ck[2]*ci;
                  a3[i3][i2][i1] += ck[3]*ci;
                  a4[i3][i2][i1] += ck[4]*ci;
                  a5[i3][i2][i1] += ck[5]*ci;
                  a6[i3][i2][i1] += ck[6]*ci;
                  a7[i3][i2][i1] += ck[7]*ci;
                  a8[i3][i2][i1] += ck[8]*ci;
                  a9[i3][i2][i1] += ck[9]*ci;
                }
              }
            }
          }
        }
      }
    }

    // Cholesky decomposition solves 3x3 system for refined lags.
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          double w1 = 0.0;
          double w2 = 0.0;
          double w3 = 0.0;
          double b1 = a1[i3][i2][i1];
          double b2 = a2[i3][i2][i1];
          double b3 = a3[i3][i2][i1];
          double a21 = -a4[i3][i2][i1];
          double a31 = -a5[i3][i2][i1];
          double a32 = -a6[i3][i2][i1];
          double a11 = -2.0*a7[i3][i2][i1];
          double a22 = -2.0*a8[i3][i2][i1];
          double a33 = -2.0*a9[i3][i2][i1];
          double d11 = a11;
          if (d11>0.0) {
            double l11 = sqrt(d11);
            double l21 = a21/l11;
            double l31 = a31/l11;
            double d22 = a22-l21*l21;
            if (d22>0.0) {
              double l22 = sqrt(d22);
              double l32 = (a32-l31*l21)/l22;
              double d33 = a33-l31*l31-l32*l32;
              if (d33>0.0) {
                double l33 = sqrt(d33);
                double v1 = b1/l11;
                double v2 = (b2-l21*v1)/l22;
                double v3 = (b3-l31*v1-l32*v2)/l33;
                w3 = v3/l33;
                w2 = (v2-l32*w3)/l22;
                w1 = (v1-l21*w2-l31*w3)/l11;
                if (w1<-1.0) {
                  w1 = -1.0;
                } else if (w1>1.0) {
                  w1 = 1.0;
                }
                if (w2<-1.0) {
                  w2 = -1.0;
                } else if (w2>1.0) {
                  w2 = 1.0;
                }
                if (w3<-1.0) {
                  w3 = -1.0;
                } else if (w3>1.0) {
                  w3 = 1.0;
                }
              }
            }
          }
          u1[i3][i2][i1] = (float)(w1+l1[i3][i2][i1]);
          u2[i3][i2][i1] = (float)(w2+l2[i3][i2][i1]);
          u3[i3][i2][i1] = (float)(w3+l3[i3][i2][i1]);
        }
      }
    }
  }

  public void applyWindow(
    int jf, float[] f, 
    int jg, float[] g) 
  {
    float[] w1 = makeGaussianWindow(_sigma1);
    applyWindow(w1,jf,f,jg,g);
  }

  public void applyWindow(
    int j1f, int j2f, float[][] f, 
    int j1g, int j2g, float[][] g) 
  {
    float[] w1 = makeGaussianWindow(_sigma1);
    float[] w2 = makeGaussianWindow(_sigma2);
    applyWindow(w1,w2,j1f,j2f,f,j1g,j2g,g);
  }

  public void applyWindow(
    int j1f, int j2f, int j3f, float[][][] f, 
    int j1g, int j2g, int j3g, float[][][] g) 
  {
    float[] w1 = makeGaussianWindow(_sigma1);
    float[] w2 = makeGaussianWindow(_sigma2);
    float[] w3 = makeGaussianWindow(_sigma3);
    applyWindow(w1,w2,w3,j1f,j2f,j3f,f,j1g,j2g,j3g,g);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Information about lags used when searching for correlation maxima.
  // Lags for which correlations have been computed are marked. Lags for 
  // which correlation maxima have been found are marked differently.
  // The next lag to process is one that is not marked, but that is 
  // adjacent to a lag for which a maximum has been found. If no such 
  // next lag exists, then the next lag is null.
  private static class Lags {
    Lags(int min1, int max1) {
      this(min1,max1,0,0,0,0);
    }
    Lags(int min1, int max1, int min2, int max2) {
      this(min1,max1,min2,max2,0,0);
    }
    Lags(int min1, int max1, int min2, int max2, int min3, int max3) {
      int nl1 = 1+max1-min1;
      int nl2 = 1+max2-min2;
      int nl3 = 1+max3-min3;
      _min1 = min1;
      _max1 = max1;
      _min2 = min2;
      _max2 = max2;
      _min3 = min3;
      _max3 = max3;
      _mark = new byte[nl3][nl2][nl1];
    }
    void markLag(int l1) {
      markLag(l1,0,0);
    }
    void markLag(int l1, int l2) {
      markLag(l1,l2,0);
    }
    void markLag(int l1, int l2, int l3) {
      _mark[l3-_min3][l2-_min2][l1-_min1] = -1;
    }
    void markMax(int l1) {
      markMax(l1,0,0);
    }
    void markMax(int l1, int l2) {
      markMax(l1,l2,0);
    }
    void markMax(int l1, int l2, int l3) {
      _mark[l3-_min3][l2-_min2][l1-_min1] = 1;
    }
    boolean isMarkedLag(int l1) {
      return isMarkedLag(l1,0,0);
    }
    boolean isMarkedLag(int l1, int l2) {
      return isMarkedLag(l1,l2,0);
    }
    boolean isMarkedLag(int l1, int l2, int l3) {
      return !inBounds(l1,l2,l3) || _mark[l3-_min3][l2-_min2][l1-_min1]!=0;
    }
    boolean isMarkedMax(int l1) {
      return isMarkedMax(l1,0,0);
    }
    boolean isMarkedMax(int l1, int l2) {
      return isMarkedMax(l1,l2,0);
    }
    boolean isMarkedMax(int l1, int l2, int l3) {
      return inBounds(l1,l2,l3) && _mark[l3-_min3][l2-_min2][l1-_min1]==1;
    }
    boolean inBounds(int l1, int l2, int l3) {
      return l1>=_min1 && l1<=_max1 &&
             l2>=_min2 && l2<=_max2 &&
             l3>=_min3 && l3<=_max3;
    }
    int[] nextLag() {
      for (int l3=_min3; l3<=_max3; ++l3) {
        for (int l2=_min2; l2<=_max2; ++l2) {
          for (int l1=_min1; l1<=_max1; ++l1) {
            if (isMarkedMax(l1,l2,l3)) {
              for (int k3=l3-1; k3<=l3+1; ++k3) {
                for (int k2=l2-1; k2<=l2+1; ++k2) {
                  for (int k1=l1-1; k1<=l1+1; ++k1) {
                    if (!isMarkedLag(k1,k2,k3)) {
                      return new int[]{k1,k2,k3};
                    }
                  }
                }
              }
            }
          }
        }
      }
      return null;
    }
    int _min1,_max1,_min2,_max2,_min3,_max3;
    byte[][][] _mark;
  }

  // Fractions used in tables of coefficients below.
  private static final float C00 = 0.0f;
  private static final float C11 = 1.0f/1.0f;
  private static final float C12 = 1.0f/2.0f;
  private static final float C13 = 1.0f/3.0f;
  private static final float C14 = 1.0f/4.0f;
  private static final float C16 = 1.0f/6.0f;
  private static final float C19 = 1.0f/9.0f;
  private static final float C29 = 2.0f/9.0f;
  private static final float C59 = 5.0f/9.0f;
  private static final float C000 = 0.0f;
  private static final float C109 = 1.0f/ 9.0f;
  private static final float C112 = 1.0f/12.0f;
  private static final float C118 = 1.0f/18.0f;
  private static final float C127 = 1.0f/27.0f;
  private static final float C227 = 2.0f/27.0f;
  private static final float C427 = 4.0f/27.0f;
  private static final float C727 = 7.0f/27.0f;

  // Coefficients for 1-D lag refinement. Assume a sampled correlation
  // maximum at integer lag l. Let u be the fractional component of a
  // refined lag l+u. Near its maximum, we approximate the correlation
  // function c(l+u) by a quadratic function fit that interpolates the
  // three sampled correlation values surrounding the sample c(l).
  // The quadratic function is c(l+u) = a0 + a1*u + a2*u*u.
  // The three sampled correlation values c(l-1), c(l), and c(l+1)
  // yield three equations for the three coefficients a0, a1, and a2.
  // The rows of this array contain the weights that we apply to each
  // of the three correlation values in the computation of the three
  // quadratic coefficients. For example, the first (top) row contains
  // the weights applied to the sampled correlation value c(l-1).
  // When refining correlation lags, we use this table of weights to
  // accumulate the contributions of the three correlation values nearest
  // to each sampled correlation maximum. For lag refinement, we need only
  // the two coefficients a1 and a2, for the peak of the quadratic is at
  // u = -0.5*a1/a2.
  private static final float[][] C1 = {
  //  a0    a1    a2
    { C00, -C12,  C12}, // -1
    { C11,  C00, -C11}, //  0
    { C00,  C12,  C12}, //  1
  };


  // Coefficients for 2-D lag refinement. Assume a sampled correlation 
  // maximum at integer lag (l1,l2). Let (u1,u2) be fractional components 
  // of a refined lag (l1+u1,l2+u2). Near its maximum, we approximate the 
  // correlation function c(l1+u1,l2+u2) by a quadratic function least-
  // squares fit to the nine sampled correlation values surrounding the 
  // sampled c(l1,l2).
  // The quadratic function is
  // c(l1+u1,l2+u2) = a0 + a1*u1 + a2*u2 + a3*u1*u2 + a4*u1*u1 + a5*u2*u2
  // The nine sampled correlation values yield nine equations for the six
  // coefficients a0, a1, a2, a3, a4, and a5.
  // By QR decomposition of this overdetermined system of equations, we 
  // obtained the following array of constants. The rows of this array 
  // contain the weights that we apply to each of the nine correlation 
  // values in the computation of the six quadratic coefficients. For
  // example, the first (top) row contains the weights applied to the 
  // sampled correlation value c(l1-1,l2-1).
  // When refining correlation lags, we use this table of weights to 
  // accumulate the contributions of the nine correlation values nearest
  // to each sampled correlation maximum. For lag refinement, we need only 
  // the five coefficients a1, a2, a3, a4, and a5.
  private static final float[][] C2 = {
  //  a0    a1    a2    a3    a4    a5
    {-C19, -C16, -C16,  C14,  C16,  C16}, // (-1,-1)
    { C29,  C00, -C16,  C00, -C13,  C16}, // ( 0,-1)
    {-C19,  C16, -C16, -C14,  C16,  C16}, // ( 1,-1)
    { C29, -C16,  C00,  C00,  C16, -C13}, // (-1, 0)
    { C59,  C00,  C00,  C00, -C13, -C13}, // ( 0, 0)
    { C29,  C16,  C00,  C00,  C16, -C13}, // ( 1, 0)
    {-C19, -C16,  C16, -C14,  C16,  C16}, // (-1, 1)
    { C29,  C00,  C16,  C00, -C13,  C16}, // ( 0, 1)
    {-C19,  C16,  C16,  C14,  C16,  C16}, // ( 1, 1)
  };

  // Alternative coefficients? (Experimenting.)
  private static final float[][] C2X = {
    { C00,  C00,  C00,  C14,  C00,  C00}, // (-1,-1)
    { C00,  C00, -C12,  C00,  C00,  C12}, // ( 0,-1)
    { C00,  C00,  C00, -C14,  C00,  C00}, // ( 1,-1)
    { C00, -C12,  C00,  C00,  C12,  C00}, // (-1, 0)
    { C11,  C00,  C00,  C00, -C11, -C11}, // ( 0, 0)
    { C00,  C12,  C00,  C00,  C12,  C00}, // ( 1, 0)
    { C00,  C00,  C00, -C14,  C00,  C00}, // (-1, 1)
    { C00,  C00,  C12,  C00,  C00,  C12}, // ( 0, 1)
    { C00,  C00,  C00,  C14,  C00,  C00}, // ( 1, 1)
  };
  private static final float[][] C2Y = {
    { C00,  C00,  C00,  C00,  C00,  C00}, // (-1,-1)
    { C00,  C00, -C12,  C00,  C00,  C12}, // ( 0,-1)
    { C00,  C00,  C00,  C00,  C00,  C00}, // ( 1,-1)
    { C00, -C12,  C00,  C00,  C12,  C00}, // (-1, 0)
    { C11,  C00,  C00,  C00, -C11, -C11}, // ( 0, 0)
    { C00,  C12,  C00,  C00,  C12,  C00}, // ( 1, 0)
    { C00,  C00,  C00,  C00,  C00,  C00}, // (-1, 1)
    { C00,  C00,  C12,  C00,  C00,  C12}, // ( 0, 1)
    { C00,  C00,  C00,  C00,  C00,  C00}, // ( 1, 1)
  };

  // Coefficients for 3-D lag refinement. Here we fit 27 sampled correlation 
  // values with 10 coefficients of a 3-D quadratic function
  // c(l1+u1,l2+u2,l3+u3) = a0 + a1*u1    + a2*u2    + a3*u3    +
  //                             a4*u1*u2 + a5*u1*u3 + a6*u2*u3 +
  //                             a7*u1*u1 + a8*u2*u2 + a9*u3*u3
  // For lag refinement, we need only the last nine coefficients.
  private static final float[][] C3 = {
  //   a0     a1     a2     a3     a4     a5     a6     a7     a8     a9 
    {-C227, -C118, -C118, -C118,  C112,  C112,  C112,  C118,  C118,  C118},
    { C127,  C000, -C118, -C118,  C000,  C000,  C112, -C109,  C118,  C118}, 
    {-C227,  C118, -C118, -C118, -C112, -C112,  C112,  C118,  C118,  C118}, 
    { C127, -C118,  C000, -C118,  C000,  C112,  C000,  C118, -C109,  C118},
    { C427,  C000,  C000, -C118,  C000,  C000,  C000, -C109, -C109,  C118},
    { C127,  C118,  C000, -C118,  C000, -C112,  C000,  C118, -C109,  C118},
    {-C227, -C118,  C118, -C118, -C112,  C112, -C112,  C118,  C118,  C118}, 
    { C127,  C000,  C118, -C118,  C000,  C000, -C112, -C109,  C118,  C118}, 
    {-C227,  C118,  C118, -C118,  C112, -C112, -C112,  C118,  C118,  C118}, 
    { C127, -C118, -C118,  C000,  C112,  C000,  C000,  C118,  C118, -C109}, 
    { C427,  C000, -C118,  C000,  C000,  C000,  C000, -C109,  C118, -C109}, 
    { C127,  C118, -C118,  C000, -C112,  C000,  C000,  C118,  C118, -C109}, 
    { C427, -C118,  C000,  C000,  C000,  C000,  C000,  C118, -C109, -C109}, 
    { C727,  C000,  C000,  C000,  C000,  C000,  C000, -C109, -C109, -C109}, 
    { C427,  C118,  C000,  C000,  C000,  C000,  C000,  C118, -C109, -C109}, 
    { C127, -C118,  C118,  C000, -C112,  C000,  C000,  C118,  C118, -C109}, 
    { C427,  C000,  C118,  C000,  C000,  C000,  C000, -C109,  C118, -C109}, 
    { C127,  C118,  C118,  C000,  C112,  C000,  C000,  C118,  C118, -C109}, 
    {-C227, -C118, -C118,  C118,  C112, -C112, -C112,  C118,  C118,  C118}, 
    { C127,  C000, -C118,  C118,  C000,  C000, -C112, -C109,  C118,  C118}, 
    {-C227,  C118, -C118,  C118, -C112,  C112, -C112,  C118,  C118,  C118}, 
    { C127, -C118,  C000,  C118,  C000, -C112,  C000,  C118, -C109,  C118}, 
    { C427,  C000,  C000,  C118,  C000,  C000,  C000, -C109, -C109,  C118}, 
    { C127,  C118,  C000,  C118,  C000,  C112,  C000,  C118, -C109,  C118}, 
    {-C227, -C118,  C118,  C118, -C112, -C112,  C112,  C118,  C118,  C118}, 
    { C127,  C000,  C118,  C118,  C000,  C000,  C112, -C109,  C118,  C118}, 
    {-C227,  C118,  C118,  C118,  C112,  C112,  C112,  C118,  C118,  C118},
  };

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

  private static void shift1(float[][][] f, float[][][] g) {
    int n3 = f.length;
    for (int i3=0; i3<n3; ++i3)
      shift1(f[i3],g[i3]);
  }

  private static void shift2(float[][][] f, float[][][] g) {
    int n3 = f.length;
    for (int i3=0; i3<n3; ++i3)
      shift2(f[i3],g[i3]);
  }

  private static void shift3(float[][][] f, float[][][] g) {
    int n3 = f.length;
    int n2 = f[0].length;
    int n1 = f[0][0].length;
    float[][] f2 = new float[n3][];
    float[][] g2 = new float[n3][];
    for (int i2=0; i2<n2; ++i2) {
      for (int i3=0; i3<n3; ++i3) {
        f2[i3] = f[i3][i2];
        g2[i3] = g[i3][i2];
      }
      shift2(f2,g2);
    }
  }

  private static void get2(int i2, float[][][] x, float[][] x2) {
    int n3 = x2.length;
    int n1 = x2[0].length;
    for (int i3=0; i3<n3; ++i3) {
      float[] x32 = x[i3][i2];
      float[] x23 = x2[i3];
      for (int i1=0; i1<n1; ++i1) {
        x23[i1] = x32[i1];
      }
    }
  }

  private static void set2(int i2, float[][] x2, float[][][] x) {
    int n3 = x2.length;
    int n1 = x2[0].length;
    for (int i3=0; i3<n3; ++i3) {
      float[] x32 = x[i3][i2];
      float[] x23 = x2[i3];
      for (int i1=0; i1<n1; ++i1) {
        x32[i1] = x23[i1];
      }
    }
  }

  private static float[] makeGaussianWindow(double sigma) {
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
  private static void applyWindow(
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
  private static void applyWindow(
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
      float w2i = w2[i2];
      float[] f2 = f[j2f+i2];
      float[] g2 = g[j2g+i2];
      for (int i1=i1min; i1<i1max; ++i1) {
        g2[j1g+i1] = w2i*w1[i1]*f2[j1f+i1];
      }
    }
  }

  private static void applyWindow(
    float[] w1, float[] w2, float[] w3,
    int j1f, int j2f, int j3f, float[][][] f, 
    int j1g, int j2g, int j3g, float[][][] g) 
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
    int n3f = f.length;
    int n3g = g.length;
    int n3w = w3.length;
    j3f -= (n3w-1)/2;
    j3g -= (n3w-1)/2;
    int i3min = max(0,-j3f,-j3g);
    int i3max = min(n3w,n3f-j3f,n3g-j3g);
    for (int i3=i3min; i3<i3max; ++i3) {
      float w3i = w3[i3];
      float[][] f3 = f[j3f+i3];
      float[][] g3 = g[j3g+i3];
      for (int i2=i2min; i2<i2max; ++i2) {
        float w32i = w3i*w2[i2];
        float[] f32 = f3[j2f+i2];
        float[] g32 = g3[j2g+i2];
        for (int i1=i1min; i1<i1max; ++i1) {
          g32[j1g+i1] = w32i*w1[i1]*f32[j1f+i1];
        }
      }
    }
  }
}
