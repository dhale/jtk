/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;
import static java.lang.Math.*;

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
   * Construct a correlation filter with specified width.
   * @param sigma the correlation window width; must not be less than 1.
   */
  public LocalCorrelationFilter(double sigma) {
    Check.argument(sigma>=1.0,"sigma>=1.0");
    _sigma = sigma;
    _rgf = new RecursiveGaussianFilter(sigma/sqrt(2.0));
  }

  /**
   * Applies this correlation filter for the specified lag.
   * @param lag the lag.
   * @param f the 1st input array; can be the same as g.
   * @param g the 2nd input array; can be the same as f.
   * @param r the output array; cannot be the same as f or g.
   */
  public void apply(int lag, float[] f, float[] g, float[] r) {
    Check.argument(f!=r,"f!=r");
    Check.argument(g!=r,"g!=r");
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
    float scale1 = (float)exp((-0.25*l1*l1)/(_sigma*_sigma));
    float scales = (float)(1.0/(sqrt(PI)*2*_sigma));
    float scale = scales*scale1;
    int i1min = max(abs(l1f),abs(l1g));
    int i1max = n1-i1min;
    float[] h = new float[n1];
    for (int i1=i1min; i1<i1max; ++i1) {
      r[i1] = scale*f[i1-l1f]*g[i1+l1g];
    }
    if (l1f!=l1g)
      shift(r,h);
    else
      Array.copy(r,h);
    _rgf.apply0(h,r);
  }

  public void apply(
    int lag1, int lag2, float[][] f, float[][] g, float[][] r) 
  {
    Check.argument(f!=r,"f!=r");
    Check.argument(g!=r,"g!=r");
    int n1 = f[0].length;
    int l1 = lag1;
    int l1f = (l1>=0)?(l1+0)/2:(l1-1)/2;
    int l1g = (l1>=0)?(l1+1)/2:(l1+0)/2;
    int n2 = f.length;
    int l2 = lag2;
    int l2f = (l2>=0)?(l2+0)/2:(l2-1)/2;
    int l2g = (l2>=0)?(l2+1)/2:(l2+0)/2;
    float scale1 = (float)exp((-0.25*l1*l1)/(_sigma*_sigma));
    float scale2 = (float)exp((-0.25*l2*l2)/(_sigma*_sigma));
    float scales = (float)(1.0/(PI*4*_sigma*_sigma));
    float scale = scales*scale1*scale2;
    int i1min = max(abs(l1f),abs(l1g));
    int i1max = n1-i1min;
    int i2min = max(abs(l2f),abs(l2g));
    int i2max = n2-i2min;
    float[][] h = new float[n2][n1];
    for (int i2=i2min; i2<i2max; ++i2) {
      for (int i1=i1min; i1<i1max; ++i1) {
        h[i2][i1] = scale*f[i2-l2f][i1-l1f]*g[i2+l2g][i1+l1g];
      }
    }
    if (l1f!=l1g)
      shift1(h,r);
    else
      Array.copy(h,r);
    if (l2f!=l2g)
      shift2(r,h);
    else
      Array.copy(r,h);
    _rgf.apply00(h,r);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private
  private double _sigma;
  private RecursiveGaussianFilter _rgf;

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
}
