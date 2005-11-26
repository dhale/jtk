/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Recursive implementation of a Gaussian filter and derivatives. Filters 
 * include the 0th, 1st, and 2nd derivatives. The impulse response of the 
 * 0th-derivative smoothing filter is infinitely long, and is approximately 
 * h[n] = 1.0/(sqrt(2*PI)*sigma)*exp(-0.5*(n*n)/(sigma*sigma)). Here,
 * sigma denotes the standard width of the Gaussian.
 * <p>
 * For large filter widths sigma, this recursive implementation can be
 * much more efficient than convolution with a truncated Gaussian.
 * Specifically, if the Gaussian is truncated for |n| &gt; 4*sigma, then
 * this recursive implementation requires 2/sigma of the multiplications 
 * required by convolution. In other words, for sigma &gt; 2, this
 * recursive implementation should be more efficient than convolution.
 * <p>
 * This implementation is based on the design by Deriche, R., 1993,
 * Recursively implementing the Gaussian and its derivatives: INRIA
 * Research Report, number 1893.
 * <p>
 * For any application of this filter, input and output arrays may be the 
 * same array. When the filter cannot be applied in-place, intermediate
 * arrays are constructed internally.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.11.25
 */
public class RecursiveGaussianFilter {

  /**
   * Construct a Gaussian filter with specified width.
   * @param sigma the width; must not be less than 1.
   */
  public RecursiveGaussianFilter(double sigma) {
    Check.argument(sigma>=1.0,"sigma>=1.0");
    makeND(sigma);
  }

  /**
   * Applies the 0th-derivative filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply0(float[] x, float[] y) {
    applyN(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply1(float[] x, float[] y) {
    applyN(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply2(float[] x, float[] y) {
    applyN(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply0X(float[][] x, float[][] y) {
    applyNX(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply1X(float[][] x, float[][] y) {
    applyNX(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply2X(float[][] x, float[][] y) {
    applyNX(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX0(float[][] x, float[][] y) {
    applyXN(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX1(float[][] x, float[][] y) {
    applyXN(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX2(float[][] x, float[][] y) {
    applyXN(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply0XX(float[][][] x, float[][][] y) {
    applyNXX(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply1XX(float[][][] x, float[][][] y) {
    applyNXX(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 1st dimension.
   * Applies no filter along the 2nd or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply2XX(float[][][] x, float[][][] y) {
    applyNXX(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX0X(float[][][] x, float[][][] y) {
    applyXNX(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX1X(float[][][] x, float[][][] y) {
    applyXNX(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 2nd dimension.
   * Applies no filter along the 1st or 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyX2X(float[][][] x, float[][][] y) {
    applyXNX(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 3rd dimension.
   * Applies no filter along the 1st or 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyXX0(float[][][] x, float[][][] y) {
    applyXXN(0,x,y);
  }

  /**
   * Applies the 1st-derivative filter along the 3rd dimension.
   * Applies no filter along the 1st or 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyXX1(float[][][] x, float[][][] y) {
    applyXXN(1,x,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 3rd dimension.
   * Applies no filter along the 1st or 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyXX2(float[][][] x, float[][][] y) {
    applyXXN(2,x,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st and 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply00(float[][] x, float[][] y) {
    applyXN(0,x,y);
    applyNX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st dimension
   * and the 0th-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply10(float[][] x, float[][] y) {
    applyXN(0,x,y);
    applyNX(1,y,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st dimension
   * and the 1st-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply01(float[][] x, float[][] y) {
    applyXN(1,x,y);
    applyNX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st and 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply11(float[][] x, float[][] y) {
    applyXN(1,x,y);
    applyNX(1,y,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 1st dimension
   * and the 0th-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply20(float[][] x, float[][] y) {
    applyXN(0,x,y);
    applyNX(2,y,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st dimension
   * and the 2nd-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply02(float[][] x, float[][] y) {
    applyXN(2,x,y);
    applyNX(0,y,y);
  }

  /**
   * Applies the 0th-derivative filter along the 1st, 2nd and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply000(float[][][] x, float[][][] y) {
    applyXXN(0,x,y);
    applyXNX(0,y,y);
    applyNXX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st dimension
   * and the 0th-derivative filter along the 2nd and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply100(float[][][] x, float[][][] y) {
    applyXXN(0,x,y);
    applyXNX(0,y,y);
    applyNXX(1,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 2nd dimension
   * and the 0th-derivative filter along the 1st and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply010(float[][][] x, float[][][] y) {
    applyXXN(0,x,y);
    applyXNX(1,y,y);
    applyNXX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 3rd dimension
   * and the 0th-derivative filter along the 1st and 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply001(float[][][] x, float[][][] y) {
    applyXXN(1,x,y);
    applyXNX(0,y,y);
    applyNXX(0,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st and 2nd dimensions
   * and the 0th-derivative filter along the 3rd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply110(float[][][] x, float[][][] y) {
    applyXXN(0,x,y);
    applyXNX(1,y,y);
    applyNXX(1,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 1st and 3rd dimensions
   * and the 0th-derivative filter along the 2nd dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply101(float[][][] x, float[][][] y) {
    applyXXN(1,x,y);
    applyXNX(0,y,y);
    applyNXX(1,y,y);
  }

  /**
   * Applies the 1st-derivative filter along the 2nd and 3rd dimensions
   * and the 0th-derivative filter along the 1st dimension.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply011(float[][][] x, float[][][] y) {
    applyXXN(1,x,y);
    applyXNX(1,y,y);
    applyNXX(0,y,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 1st dimension
   * and the 0th-derivative filter along the 2nd and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply200(float[][][] x, float[][][] y) {
    applyXXN(0,x,y);
    applyXNX(0,y,y);
    applyNXX(2,y,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 2nd dimension
   * and the 0th-derivative filter along the 1st and 3rd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply020(float[][][] x, float[][][] y) {
    applyXXN(0,x,y);
    applyXNX(2,y,y);
    applyNXX(0,y,y);
  }

  /**
   * Applies the 2nd-derivative filter along the 3rd dimension
   * and the 0th-derivative filter along the 1st and 2nd dimensions.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply002(float[][][] x, float[][][] y) {
    applyXXN(2,x,y);
    applyXNX(0,y,y);
    applyNXX(0,y,y);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Deriche's fitting parameters for
  // 4th-order recursive filters for 0th,     1st,     2nd derivatives.
  private static double[] a0 = {  1.6800, -0.6472, -1.3310};
  private static double[] a1 = {  3.7350, -4.5310,  3.6610};
  private static double[] b0 = {  1.7830,  1.5270,  1.2400};
  private static double[] b1 = {  1.7230,  1.5160,  1.3140};
  private static double[] c0 = { -0.6803,  0.6494,  0.3225};
  private static double[] c1 = { -0.2598,  0.9557, -1.7380};
  private static double[] w0 = {  0.6318,  0.6719,  0.7480};
  private static double[] w1 = {  1.9970,  2.0720,  2.1660};
  private float[] _n0,_n1,_n2,_n3; // numerator coefficients
  private float[] _d1,_d2,_d3,_d4; // denominator coefficients

  /**
   * Makes Deriche's numerator and denominator coefficients.
   */
  private void makeND(double sigma) {
    _n0 = new float[3];
    _n1 = new float[3];
    _n2 = new float[3];
    _n3 = new float[3];
    _d1 = new float[3];
    _d2 = new float[3];
    _d3 = new float[3];
    _d4 = new float[3];

    // For 0th, 1st, and 2nd derivatives, ...
    for (int i=0; i<3; ++i) {
      double n0 = a0[i]+c0[i];
      double n1 = exp(-b1[i]/sigma) * (
                    c1[i]*sin(w1[i]/sigma) -
                    (c0[i]+2.0*a0[i])*cos(w1[i]/sigma)) +
                  exp(-b0[i]/sigma) * (
                    a1[i]*sin(w0[i]/sigma) -
                    (2.0*c0[i]+a0[i])*cos(w0[i]/sigma));
      double n2 = 2.0*exp(-(b0[i]+b1[i])/sigma) * (
                    (a0[i]+c0[i])*cos(w1[i]/sigma)*cos(w0[i]/sigma) -
                    a1[i]*cos(w1[i]/sigma)*sin(w0[i]/sigma) -
                    c1[i]*cos(w0[i]/sigma)*sin(w1[i]/sigma)) +
                  c0[i]*exp(-2.0*b0[i]/sigma) +
                  a0[i]*exp(-2.0*b1[i]/sigma);
      double n3 = exp(-(b1[i]+2.0*b0[i])/sigma) * (
                    c1[i]*sin(w1[i]/sigma) -
                    c0[i]*cos(w1[i]/sigma)) +
                  exp(-(b0[i]+2.0*b1[i])/sigma) * (
                    a1[i]*sin(w0[i]/sigma) -
                    a0[i]*cos(w0[i]/sigma));
      double d1 = -2.0*exp(-b0[i]/sigma)*cos(w0[i]/sigma) -
                   2.0*exp(-b1[i]/sigma)*cos(w1[i]/sigma);
      double d2 = 4.0*exp(-(b0[i]+b1[i])/sigma) *
                    cos(w0[i]/sigma)*cos(w1[i]/sigma) +
                  exp(-2.0*b0[i]/sigma) +
                  exp(-2.0*b1[i]/sigma);
      double d3 = -2.0*exp(-(b0[i]+2.0*b1[i])/sigma)*cos(w0[i]/sigma) -
                   2.0*exp(-(b1[i]+2.0*b0[i])/sigma)*cos(w1[i]/sigma);
      double d4 = exp(-2.0*(b0[i]+b1[i])/sigma);
      /*
      double scale = 1.0/(sqrt(2.0*PI)*sigma);
      if (i>0)
        scale /= sigma*sigma;
      n0 *= scale;
      n1 *= scale;
      n2 *= scale;
      n3 *= scale;
      */
      _n0[i] = (float)n0;
      _n1[i] = (float)n1;
      _n2[i] = (float)n2;
      _n3[i] = (float)n3;
      _d1[i] = (float)d1;
      _d2[i] = (float)d2;
      _d3[i] = (float)d3;
      _d4[i] = (float)d4;
    }
    scaleN(sigma);
  }

  private void scaleN(double sigma) {
    int n = 1+2*(int)(20.0*sigma);
    float[] x = new float[n];
    float[] y0 = new float[n];
    float[] y1 = new float[n];
    float[] y2 = new float[n];
    int m = n/2;
    x[m] = 1.0f;
    apply0(x,y0);
    apply1(x,y1);
    apply2(x,y2);
    double[] s = new double[3];
    for (int i=0,j=n-1; i<j; ++i,--j) {
      double t = i-m;
      s[0] += y0[i]+y0[j];
      s[1] += -t*(y1[i]-y1[j]);
      s[2] += t*t*(y2[i]+y2[j]);
    }
    s[0] += y0[m];
    s[2] *= 0.5;
    for (int i=0; i<3; ++i) {
      _n0[i] /= s[i];
      _n1[i] /= s[i];
      _n2[i] /= s[i];
      _n3[i] /= s[i];
    }
  }

  private void applyN(int nd, float[] x, float[] y) {
    checkArrays(x,y);
    if (x==y)
      x = Array.copy(x);
    int m = y.length;
    float n0 = _n0[nd],  n1 = _n1[nd],  n2 = _n2[nd],  n3 = _n3[nd];
    float d1 = _d1[nd],  d2 = _d2[nd],  d3 = _d3[nd],  d4 = _d4[nd];
    float yim4 = 0.0f,  yim3 = 0.0f,  yim2 = 0.0f,  yim1 = 0.0f;
    float               xim3 = 0.0f,  xim2 = 0.0f,  xim1 = 0.0f;
    for (int i=0; i<m; ++i) {
      float xi = x[i];
      float yi = n0*xi+n1*xim1+n2*xim2+n3*xim3 -
                       d1*yim1-d2*yim2-d3*yim3-d4*yim4;
      y[i] = yi;
      yim4 = yim3;  yim3 = yim2;  yim2 = yim1;  yim1 = yi;
                    xim3 = xim2;  xim2 = xim1;  xim1 = xi;
    }
    n1 = n1-d1*n0;
    n2 = n2-d2*n0;
    n3 = n3-d3*n0;
    float n4 = -d4*n0;
    if (nd%2!=0) {
      n1 = -n1;  n2 = -n2;  n3 = -n3;  n4 = -n4;
    }
    float yip4 = 0.0f,  yip3 = 0.0f,  yip2 = 0.0f,  yip1 = 0.0f;
    float xip4 = 0.0f,  xip3 = 0.0f,  xip2 = 0.0f,  xip1 = 0.0f;
    for (int i=m-1; i>=0; --i) {
      float xi = x[i];
      float yi = n1*xip1+n2*xip2+n3*xip3+n4*xip4 -
                 d1*yip1-d2*yip2-d3*yip3-d4*yip4;
      y[i] += yi;
      yip4 = yip3;  yip3 = yip2;  yip2 = yip1;  yip1 = yi;
      xip4 = xip3;  xip3 = xip2;  xip2 = xip1;  xip1 = xi;
    }
  }

  private void applyNX(int nd, float[][] x, float[][] y) {
    int m2 = y.length;
    for (int i2=0; i2<m2; ++i2)
      applyN(nd,x[i2],y[i2]);
  }

  private void applyXN(int nd, float[][] x, float[][] y) {
    checkArrays(x,y);
    if (x==y)
      x = Array.copy(x);
    int m2 = y.length;
    int m1 = y[0].length;
    float n0 = _n0[nd],  n1 = _n1[nd],  n2 = _n2[nd],  n3 = _n3[nd];
    float d1 = _d1[nd],  d2 = _d2[nd],  d3 = _d3[nd],  d4 = _d4[nd];
    float[] yim4 = new float[m1];
    float[] yim3 = new float[m1];
    float[] yim2 = new float[m1];
    float[] yim1 = new float[m1];
    float[] xim4 = new float[m1];
    float[] xim3 = new float[m1];
    float[] xim2 = new float[m1];
    float[] xim1 = new float[m1];
    float[] yi = new float[m1];
    float[] xi = new float[m1];
    for (int i2=0; i2<m2; ++i2) {
      float[] x2 = x[i2];
      float[] y2 = y[i2];
      for (int i1=0; i1<m1; ++i1) {
        xi[i1] = x2[i1];
        yi[i1] = n0*xi[i1]+n1*xim1[i1]+n2*xim2[i1]+n3*xim3[i1]
                          -d1*yim1[i1]-d2*yim2[i1]-d3*yim3[i1]-d4*yim4[i1];
        y2[i1] = yi[i1];
      }
      float[] yt = yim4;
      yim4 = yim3;
      yim3 = yim2;
      yim2 = yim1;
      yim1 = yi;
      yi = yt;
      float[] xt = xim3;
      xim3 = xim2;
      xim2 = xim1;
      xim1 = xi;
      xi = xt;
    }
    n1 = n1-d1*n0;
    n2 = n2-d2*n0;
    n3 = n3-d3*n0;
    float n4 = -d4*n0;
    if (nd%2!=0) {
      n1 = -n1;  n2 = -n2;  n3 = -n3;  n4 = -n4;
    }
    float[] yip4 = yim4;
    float[] yip3 = yim3;
    float[] yip2 = yim2;
    float[] yip1 = yim1;
    float[] xip4 = xim4;
    float[] xip3 = xim3;
    float[] xip2 = xim2;
    float[] xip1 = xim1;
    for (int i1=0; i1<m1; ++i1) {
      yip4[i1] = 0.0f;
      yip3[i1] = 0.0f;
      yip2[i1] = 0.0f;
      yip1[i1] = 0.0f;
      xip4[i1] = 0.0f;
      xip3[i1] = 0.0f;
      xip2[i1] = 0.0f;
      xip1[i1] = 0.0f;
    }
    for (int i2=m2-1; i2>=0; --i2) {
      float[] x2 = x[i2];
      float[] y2 = y[i2];
      for (int i1=0; i1<m1; ++i1) {
        xi[i1] = x2[i1];
        yi[i1] = n1*xip1[i1]+n2*xip2[i1]+n3*xip3[i1]+n4*xip4[i1] -
                 d1*yip1[i1]-d2*yip2[i1]-d3*yip3[i1]-d4*yip4[i1];
        y2[i1] += yi[i1];
      }
      float[] yt = yip4;
      yip4 = yip3;
      yip3 = yip2;
      yip2 = yip1;
      yip1 = yi;
      yi = yt;
      float[] xt = xip4;
      xip4 = xip3;
      xip3 = xip2;
      xip2 = xip1;
      xip1 = xi;
      xi = xt;
    }
  }

  private void applyNXX(int nd, float[][][] x, float[][][] y) {
    int m3 = y.length;
    for (int i3=0; i3<m3; ++i3)
      applyNX(nd,x[i3],y[i3]);
  }

  private void applyXNX(int nd, float[][][] x, float[][][] y) {
    int m3 = y.length;
    for (int i3=0; i3<m3; ++i3)
      applyXN(nd,x[i3],y[i3]);
  }

  private void applyXXN(int nd, float[][][] x, float[][][] y) {
    checkArrays(x,y);
    int m3 = y.length;
    int m2 = y[0].length;
    int m1 = y[0][0].length;
    float[][] x2 = new float[m3][m1];
    float[][] y2 = new float[m3][m1];
    for (int i2=0; i2<m2; ++i2) {
      for (int i3=0; i3<m3; ++i3) {
        float[] x32 = x[i3][i2];
        float[] x23 = x2[i3];
        for (int i1=0; i1<m1; ++i1) {
          x23[i1] = x32[i1];
        }
      }
      applyXN(nd,x2,y2);
      for (int i3=0; i3<m3; ++i3) {
        float[] y32 = y[i3][i2];
        float[] y23 = y2[i3];
        for (int i1=0; i1<m1; ++i1) {
          y32[i1] = y23[i1];
        }
      }
    }
  }

  private static void checkArrays(float[] x, float[] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
  }

  private static void checkArrays(float[][] x, float[][] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
    Check.argument(x[0].length==y[0].length,"x[0].length==y[0].length");
    Check.argument(Array.isRegular(x),"x is regular");
    Check.argument(Array.isRegular(y),"y is regular");
  }

  private static void checkArrays(float[][][] x, float[][][] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
    Check.argument(x[0].length==y[0].length,"x[0].length==y[0].length");
    Check.argument(x[0][0].length==y[0][0].length,
      "x[0][0].length==y[0][0].length");
    Check.argument(Array.isRegular(x),"x is regular");
    Check.argument(Array.isRegular(y),"y is regular");
  }

  ///////////////////////////////////////////////////////////////////////////
  // Alternative implementation uses recursive parallel filters.
  // But we do not have the necessary zeros to construct a RPF.
  /*

  // Recursive parallel filters for 0th, 1st, and 2nd derivative filters.
  private RecursiveParallelFilter[] _rf = new RecursiveParallelFilter[3];

  private void makeFilters(double sigma) {

    // For 0th, 1st, and 2nd derivatives, ...
    for (int i=0; i<3; ++i) {

      // Poles.
      Cdouble p0 = new Cdouble(-b0[i]/sigma,w0[i]/sigma).exp();
      Cdouble p1 = p0.conj();
      Cdouble p2 = new Cdouble(-b1[i]/sigma,w1[i]/sigma).exp();
      Cdouble p3 = p2.conj();
      Cdouble[] poles = {p0,p1,p2,p3};

      // Coefficients for Deriche's cubic numerator polynomial.
      double n0 = a0[i]+c0[i];
      double n1 = exp(-b1[i]/sigma) * (
                    c1[i]*sin(w1[i]/sigma) -
                    (c0[i]+2.0*a0[i])*cos(w1[i]/sigma)) +
                  exp(-b0[i]/sigma) * (
                    a1[i]*sin(w0[i]/sigma) -
                    (2.0*c0[i]+a0[i])*cos(w0[i]/sigma));
      double n2 = 2.0*exp(-(b0[i]+b1[i])/sigma) * (
                    (a0[i]+c0[i])*cos(w1[i]/sigma)*cos(w0[i]/sigma) -
                    a1[i]*cos(w1[i]/sigma)*sin(w0[i]/sigma) -
                    c1[i]*cos(w0[i]/sigma)*sin(w1[i]/sigma)) +
                  c0[i]*exp(-2.0*b0[i]/sigma) +
                  a0[i]*exp(-2.0*b1[i]/sigma);
      double n3 = exp(-(b1[i]+2.0*b0[i])/sigma) * (
                    c1[i]*sin(w1[i]/sigma) -
                    c0[i]*cos(w1[i]/sigma)) +
                  exp(-(b0[i]+2.0*b1[i])/sigma) * (
                    a1[i]*sin(w0[i]/sigma) -
                    a0[i]*cos(w0[i]/sigma));

      // Zeros are roots (one real, two complex) of that cubic polynomial.
      double a = n1/n0; // coefficient of z^2
      double b = n2/n0; // coefficient of z^1
      double c = n3/n0; // coefficient of z^0
      double q = (a*a-3.0*b)/9.0;
      double r = (2.0*a*a*a-9.0*a*b+27.0*c)/54.0;
      Cdouble z0,z1,z2;
      if (r*r<q*q*q) {
        double theta = acos(r/sqrt(q*q*q));
        z0 = new Cdouble(-2.0*sqrt(q)*cos(theta/3.0)-a/3.0);
        z1 = new Cdouble(-2.0*sqrt(q)*cos((theta+2.0*PI)/3.0)-a/3.0);
        z2 = new Cdouble(-2.0*sqrt(q)*cos((theta-2.0*PI)/3.0)-a/3.0);
      } else {
        double aa = -signum(r)*pow(abs(r)+sqrt(r*r-q*q*q),1.0/3.0);
        double bb = (aa!=0.0)?q/aa:0.0;
        z0 = new Cdouble(aa+bb-a/3.0);
        z1 = new Cdouble(-0.5*(aa+bb)-a/3.0, 0.5*sqrt(3.0)*(aa-bb));
        z2 = new Cdouble(-0.5*(aa+bb)-a/3.0,-0.5*sqrt(3.0)*(aa-bb));
      }
      Cdouble[] zeros = {z0,z1,z2};

      // Gain.
      double gain = n0;

      // Filter.
      _rf[i] = new RecursiveParallelFilter(poles,zeros,gain);
    }
  }
  */
}
