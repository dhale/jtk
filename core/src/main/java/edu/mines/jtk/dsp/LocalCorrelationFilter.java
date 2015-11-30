/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
 * Local cross-correlation of two arrays with seamless overlapping windows.
 * Given two input arrays f and g and a specified lag, this filter computes 
 * an output array c of local cross-correlation coefficients, one for each
 * sample in the input arrays f and g.
 * <p>
 * Two types of cross-correlation are implemented. Both types can be 
 * normalized to obtain cross-correlation coefficients with magnitudes 
 * that do not exceed one. The normalization varies, depending on the 
 * type of cross-correlation.
 * <p>
 * <em>Simple</em> cross-correlation computes an array of products 
 * h[j] = f[j]*g[j+lag] and then filters this array of products with a 
 * window. The resulting correlation cfg[k,lag] is not symmetric with 
 * respect to lag; cfg[k,-lag] = cgf[k-lag,lag] != cgf[k,lag]. For
 * simple cross-correlation, normalization scale factors vary with lag
 * and should be applied before picking correlation peaks.
 * <p>
 * <em>Symmetric</em> cross-correlation computes an array of products
 * h[j] = f[j-lag/2]*g[j+lag/2] and therefore requires interpolation
 * between samples for odd lags. (For efficiency, we interpolate the 
 * products h, not the inputs f and g.) The resulting correlation is 
 * symmetric with respect to lag; cfg[k,lag] = cgf[k,-lag]. Moreover,
 * when inputs f and g are the same, each local auto-correlation has a
 * Fourier transform (a power spectrum) that is positive-semidefinite.
 * For symmetric cross-correlation, normalization scale factors do not 
 * vary with lag, and therefore need not be applied before picking 
 * correlation peaks.
 * <p>
 * Two correlation windows are implemented: Gaussian and rectangular.
 * Gaussian windows should be used for most applications. Rectangular
 * windows are provided primarily for comparison, because they are so
 * often used by others.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.08.11
 */
public class LocalCorrelationFilter {

  /**
   * Cross-correlations types.
   */
  public enum Type {
    SIMPLE,
    SYMMETRIC
  }

  /**
   * Cross-correlations windows.
   */
  public enum Window {
    GAUSSIAN,
    RECTANGLE
  }

  /**
   * Construct a correlation filter with specified parameters.
   * When applied to multi-dimensional arrays, the filter has the
   * same half-width for all dimensions.
   * @param type the correlation type.
   * @param window the correlation window.
   * @param sigma the correlation window half-width; must not be less than 1.
   */
  public LocalCorrelationFilter(Type type, Window window, double sigma) {
    this(type,window,sigma,sigma,sigma);
  }

  /**
   * Construct a correlation filter with specified parameters.
   * When applied to multi-dimensional arrays, the filter has half-width 
   * sigma1 for the 1st dimension and half-width sigma2 for 2nd and higher 
   * dimensions.
   * @param type the correlation type.
   * @param window the correlation window.
   * @param sigma1 correlation window half-width for 1st dimension; 
   *  must not be less than 1.
   * @param sigma2 correlation window half-width for 2nd and higher 
   *  dimensions; must not be less than 1.
   */
  public LocalCorrelationFilter(
    Type type, Window window, double sigma1, double sigma2) 
  {
    this(type,window,sigma1,sigma2,sigma2);
  }

  /**
   * Construct a correlation filter with specified parameters.
   * When applied to multi-dimensional arrays, the filter has half-width 
   * sigma1 for the 1st dimension, half-width sigma2 for the 2nd dimension,
   * and half-width sigma3 for 3rd and higher dimensions.
   * @param type the correlation type.
   * @param window the correlation window.
   * @param sigma1 correlation window half-width for 1st dimension; 
   *  must not be less than 1.
   * @param sigma2 correlation window half-width for 2nd dimension;
   *  must not be less than 1.
   * @param sigma3 correlation window half-width for 3rd and higher 
   * dimensions; must not be less than 1.
   */
  public LocalCorrelationFilter(
    Type type, Window window, double sigma1, double sigma2, double sigma3) 
  {
    Check.argument(sigma1>=1.0,"sigma1>=1.0");
    Check.argument(sigma2>=1.0,"sigma2>=1.0");
    Check.argument(sigma3>=1.0,"sigma3>=1.0");
    _type = type;
    _window = window;
    _sigma1 = sigma1;
    _sigma2 = sigma2;
    _sigma3 = sigma3;
    if (window==Window.GAUSSIAN) {
      _f1 = new GaussianFilter(sigma1);
      _f2 = new GaussianFilter(sigma2);
      _f3 = new GaussianFilter(sigma3);
    } else {
      _f1 = new RectangleFilter(sigma1);
      _f2 = new RectangleFilter(sigma2);
      _f3 = new RectangleFilter(sigma3);
    }
  }

  /**
   * Sets the input arrays to be cross-correlated.
   * The input arrays f and g can be the same array.
   * @param f the input array f; by reference, not copied.
   * @param g the input array g; by reference, not copied.
   */
  public void setInputs(float[] f, float[] g) {
    if (f==null || g==null) {
      _dimension = _n1 = _n2 = _n3 = 0;
      _f = null;
      _g = null;
    } else {
      Check.argument(f.length==g.length,"f.length==g.length");
      _dimension = 1;
      _n1 = f.length;
      _n2 = _n3 = 0;
      _f = new float[1][1][];
      _g = new float[1][1][];
      _f[0][0] = f;
      _g[0][0] = g;
    }
    _s = null;
  }

  /**
   * Sets the input arrays to be cross-correlated.
   * The input arrays f and g can be the same array.
   * @param f the input array f; by reference, not copied.
   * @param g the input array g; by reference, not copied.
   */
  public void setInputs(float[][] f, float[][] g) {
    if (f==null || g==null) {
      _dimension = _n1 = _n2 = _n3 = 0;
      _f = null;
      _g = null;
    } else {
      Check.argument(f[0].length==g[0].length,"f[0].length==g[0].length");
      Check.argument(f.length==g.length,"f.length==g.length");
      Check.argument(isRegular(f),"f is regular");
      Check.argument(isRegular(g),"g is regular");
      _dimension = 2;
      _n1 = f[0].length;
      _n2 = f.length;
      _n3 = 0;
      _f = new float[1][][];
      _g = new float[1][][];
      _f[0] = f;
      _g[0] = g;
    }
    _s = null;
  }

  /**
   * Sets the input arrays to be cross-correlated.
   * The input arrays f and g can be the same array.
   * @param f the input array f; by reference, not copied.
   * @param g the input array g; by reference, not copied.
   */
  public void setInputs(float[][][] f, float[][][] g) {
    if (f==null || g==null) {
      _dimension = _n1 = _n2 = _n3 = 0;
      _f = null;
      _g = null;
    } else {
      Check.argument(
        f[0][0].length==g[0][0].length,"f[0][0].length==g[0][0].length");
      Check.argument(f[0].length==g[0].length,"f[0].length==g[0].length");
      Check.argument(f.length==g.length,"f.length==g.length");
      Check.argument(isRegular(f),"f is regular");
      Check.argument(isRegular(g),"g is regular");
      _dimension = 3;
      _n1 = f[0][0].length;
      _n2 = f[0].length;
      _n3 = f.length;
      _f = f;
      _g = g;
    }
    _s = null;
  }

  /**
   * Correlates the current inputs for the specified lag.
   * @param lag the correlation lag.
   * @param c the output array; cannot be the same as inputs f or g.
   */
  public void correlate(int lag, float[] c) {
    checkDimensions(c);
    correlate(lag,_f[0][0],_g[0][0],c);
  }

  /**
   * Correlates the current inputs for the specified lag.
   * @param lag1 the lag in the 1st dimension.
   * @param lag2 the lag in the 2nd dimension.
   * @param c the output array; cannot be the same as inputs f or g.
   */
  public void correlate(int lag1, int lag2, float[][] c) {
    checkDimensions(c);
    correlate(lag1,lag2,_f[0],_g[0],c);
  }

  /**
   * Correlates the current inputs for the specified lag.
   * @param lag1 the lag in the 1st dimension.
   * @param lag2 the lag in the 2nd dimension.
   * @param lag3 the lag in the 3rd dimension.
   * @param c the output array; cannot be the same as inputs f or g.
   */
  public void correlate(int lag1, int lag2, int lag3, float[][][] c) {
    checkDimensions(c);
    correlate(lag1,lag2,lag3,_f,_g,c);
  }

  /**
   * Normalizes the cross-correlation for a specified lag.
   * @param lag the lag.
   * @param c the cross-correlation to be modified.
   */
  public void normalize(int lag, float[] c) {
    checkDimensions(c);
    if (_s==null)
      updateNormalize();
    int n1 = _n1;
    int l1 = lag;
    if (_type==Type.SIMPLE) {
      float[] sf = _s[0][0][0];
      float[] sg = _s[1][0][0];
      int i1min = max(0,-l1);
      int i1max = min(n1,n1-l1);
      for (int i1=0; i1<i1min; ++i1) {
        c[i1] *= sf[i1]*sg[0];
      }
      for (int i1=i1min; i1<i1max; ++i1) {
        c[i1] *= sf[i1]*sg[i1+l1];
      }
      for (int i1=i1max; i1<n1; ++i1) {
        c[i1] *= sf[i1]*sg[n1-1];
      }
    } else if (_type==Type.SYMMETRIC) {
      float[] s = _s[0][0][0];
      for (int i1=0; i1<n1; ++i1) {
        c[i1] *= s[i1];
      }
    }
  }

  /**
   * Normalizes the cross-correlation for a specified lag.
   * @param lag1 the lag.
   * @param c the cross-correlation to be modified.
   */
  public void normalize(int lag1, int lag2, float[][] c) {
    checkDimensions(c);
    if (_s==null)
      updateNormalize();
    int n1 = _n1;
    int n2 = _n2;
    int l1 = lag1;
    int l2 = lag2;
    if (_type==Type.SIMPLE) {
      float[][] sf = _s[0][0];
      float[][] sg = _s[1][0];
      int i1min = max(0,-l1);
      int i1max = min(n1,n1-l1);
      for (int i2=0; i2<n2; ++i2) {
        float[] c2 = c[i2];
        float[] sf2 = sf[i2];
        float[] sg2 = sg[max(0,min(n2-1,i2+l2))];
        for (int i1=0; i1<i1min; ++i1) {
          c2[i1] *= sf2[i1]*sg2[0];
        }
        for (int i1=i1min; i1<i1max; ++i1) {
          c2[i1] *= sf2[i1]*sg2[i1+l1];
        }
        for (int i1=i1max; i1<n1; ++i1) {
          c2[i1] *= sf2[i1]*sg2[n1-1];
        }
      }
    } else if (_type==Type.SYMMETRIC) {
      float[][] s = _s[0][0];
      for (int i2=0; i2<n2; ++i2) {
        float[] c2 = c[i2];
        float[] s2 = s[i2];
        for (int i1=0; i1<n1; ++i1) {
          c2[i1] *= s2[i1];
        }
      }
    }
  }

  /**
   * Normalizes the cross-correlation for a specified lag.
   * @param lag1 the lag in the 1st dimension.
   * @param lag2 the lag in the 2nd dimension.
   * @param lag3 the lag in the 3rd dimension.
   * @param c the cross-correlation to be modified.
   */
  public void normalize(int lag1, int lag2, int lag3, float[][][] c) {
    checkDimensions(c);
    if (_s==null)
      updateNormalize();
    int n1 = _n1;
    int n2 = _n2;
    int n3 = _n3;
    int l1 = lag1;
    int l2 = lag2;
    int l3 = lag3;
    if (_type==Type.SIMPLE) {
      float[][][] sf = _s[0];
      float[][][] sg = _s[1];
      int i1min = max(0,-l1);
      int i1max = min(n1,n1-l1);
      for (int i3=0; i3<n3; ++i3) {
        float[][] c3 = c[i3];
        float[][] sf3 = sf[i3];
        float[][] sg3 = sg[max(0,min(n3-1,i3+l3))];
        for (int i2=0; i2<n2; ++i2) {
          float[] c32 = c3[i2];
          float[] sf32 = sf3[i2];
          float[] sg32 = sg3[max(0,min(n2-1,i2+l2))];
          for (int i1=0; i1<i1min; ++i1) {
            c32[i1] *= sf32[i1]*sg32[0];
          }
          for (int i1=i1min; i1<i1max; ++i1) {
            c32[i1] *= sf32[i1]*sg32[i1+l1];
          }
          for (int i1=i1max; i1<n1; ++i1) {
            c32[i1] *= sf32[i1]*sg32[n1-1];
          }
        }
      }
    } else if (_type==Type.SYMMETRIC) {
      float[][][] s = _s[0];
      for (int i3=0; i3<n3; ++i3) {
        float[][] c3 = c[i3];
        float[][] s3 = s[i3];
        for (int i2=0; i2<n2; ++i2) {
          float[] c32 = c3[i2];
          float[] s32 = s3[i2];
          for (int i1=0; i1<n1; ++i1) {
            c32[i1] *= s32[i1];
          }
        }
      }
    }
  }

  /** 
   * Removes bias by subtracting local means from the specified array.
   * @param f the input array.
   * @return the output array, with bias subtracted.
   */
  public float[] unbias(float[] f) {
    int n1 = f.length;
    float[] t = new float[n1];
    _f1.apply(f,t);
    sub(f,t,t);
    return t;
  }

  /** 
   * Removes bias by subtracting local means from the specified array.
   * @param f the input array.
   * @return the output array, with bias subtracted.
   */
  public float[][] unbias(float[][] f) {
    int n1 = f[0].length;
    int n2 = f.length;
    float[][] t = new float[n2][n1];
    _f1.apply1(f,t);
    _f2.apply2(t,t);
    sub(f,t,t);
    return t;
  }

  /** 
   * Removes bias by subtracting local means from the specified array.
   * @param f the input array.
   * @return the output array, with bias subtracted.
   */
  public float[][][] unbias(float[][][] f) {
    int n1 = f[0][0].length;
    int n2 = f[0].length;
    int n3 = f.length;
    float[][][] t = new float[n3][n2][n1];
    _f1.apply1(f,t);
    _f2.apply2(t,t);
    _f3.apply3(t,t);
    sub(f,t,t);
    return t;
  }


  ///////////////////////////////////////////////////////////////////////////
  // private

  private Window _window = Window.GAUSSIAN; // window for correlations
  private Type _type = Type.SYMMETRIC; // correlation type
  private double _sigma1,_sigma2,_sigma3; // window half-widths
  private Filter _f1,_f2,_f3; // filters used to implement windows
  private int _dimension; // dimension of input arrays; 0 if no inputs
  private int _n1,_n2,_n3; // array lengths
  private float[][][] _f,_g; // inputs f and g; by reference
  private float[][][][] _s; // normalization scale factors

  // Kaiser-windowed sinc interpolation coefficients for half-sample shifts.
  private static float S1 =  0.6157280f;
  private static float S2 = -0.1558022f;
  private static float S3 =  0.0509014f;
  private static float S4 = -0.0115417f;
  private static float[] S = {S4,S3,S2,S1,S1,S2,S3,S4};

  private void correlate(int lag, float[] f, float[] g, float[] c) {
    Check.argument(f!=c,"f!=c");
    Check.argument(g!=c,"g!=c");
    int n1 = f.length;
    int l1 = lag;

    // Conventional lags for f and g for simple correlation.
    int l1f = 0;
    int l1g = l1;

    // Shifted lags for symmetric correlation.
    if (_type==Type.SYMMETRIC) {
      // Examples of symmetric lags:
      // lag  ...  -2  -1   0   1   2  ...
      // l1f  ...  -1  -1   0   0   1  ...
      // l1g  ...  -1   0   0   1   1  ...
      l1f = (l1>=0)?(l1  )/2:(l1-1)/2;
      l1g = (l1>=0)?(l1+1)/2:(l1  )/2;
    }

    // Scale factor so that center of window = 1.
    double scale1 = 1.0;
    if (_window==Window.GAUSSIAN) {
      scale1 *= sqrt(2.0*PI)*_sigma1;
    } else {
      scale1 *= 1.0+2.0*_sigma1;
    }

    // If symmetric correlation, need extra lag-dependent scaling.
    // This scaling accounts for the separation (by lag samples) of 
    // the two windows implicitly applied to f and g. The filter we
    // apply below to the correlation product h is the product of 
    // those two windows.
    if (_type==Type.SYMMETRIC) {
      if (_window==Window.GAUSSIAN) {
        scale1 *= exp((-0.125*l1*l1)/(_sigma1*_sigma1));
      } else {
        scale1 *= max(0.0,1.0+2.0*_sigma1-abs(l1))/(1.0+2.0*_sigma1);
      }
    }
    float scale = (float)scale1;

    // Correlation product.
    float[] h = new float[n1];
    int i1min = max(0,l1f,-l1g);
    int i1max = min(n1,n1+l1f,n1-l1g);
    for (int i1=i1min; i1<i1max; ++i1) {
      h[i1] = scale*f[i1-l1f]*g[i1+l1g];
    }

    // If Gaussian and symmetric and odd lag, delay (shift) by 1/2 sample.
    if (_window==Window.GAUSSIAN && _type==Type.SYMMETRIC) {
      if (l1f!=l1g) {
        shift(h,c);
        copy(c,h);
      }
    }

    // Filter correlation product with window. For symmetric correlations
    // with a rectangle window, the width of the product rectangle depends 
    // on the lag, so we construct a new rectangle filter for each lag.
    Filter f1 = _f1;
    if (_window==Window.RECTANGLE && _type==Type.SYMMETRIC)
      f1 = new RectangleFilter(_sigma1,l1);
    f1.apply(h,c);
  }

  private void correlate(
    int lag1, int lag2, float[][] f, float[][] g, float[][] c) 
  {
    Check.argument(f!=c,"f!=c");
    Check.argument(g!=c,"g!=c");
    int n1 = f[0].length;
    int n2 = f.length;
    int l1 = lag1;
    int l2 = lag2;

    // Conventional lags for f and g for simple correlation.
    int l1f = 0;
    int l1g = l1;
    int l2f = 0;
    int l2g = l2;

    // Shifted lags for symmetric correlation.
    if (_type==Type.SYMMETRIC) {
      l1f = (l1>=0)?(l1  )/2:(l1-1)/2;
      l1g = (l1>=0)?(l1+1)/2:(l1  )/2;
      l2f = (l2>=0)?(l2  )/2:(l2-1)/2;
      l2g = (l2>=0)?(l2+1)/2:(l2  )/2;
    }

    // Scale factor so that center of window = 1.
    double scale1 = 1.0;
    double scale2 = 1.0;
    if (_window==Window.GAUSSIAN) {
      scale1 *= sqrt(2.0*PI)*_sigma1;
      scale2 *= sqrt(2.0*PI)*_sigma2;
    } else {
      scale1 *= 1.0+2.0*_sigma1;
      scale2 *= 1.0+2.0*_sigma2;
    }

    // If symmetric correlation, need extra lag-dependent scaling.
    // This scaling accounts for the separation (by lag samples) of 
    // the two windows implicitly applied to f and g. The filter we
    // apply below to the correlation product h is the product of 
    // those two windows.
    if (_type==Type.SYMMETRIC) {
      if (_window==Window.GAUSSIAN) {
        scale1 *= exp((-0.125*l1*l1)/(_sigma1*_sigma1));
        scale2 *= exp((-0.125*l2*l2)/(_sigma2*_sigma2));
      } else {
        scale1 *= max(0.0,1.0+2.0*_sigma1-abs(l1))/(1.0+2.0*_sigma1);
        scale2 *= max(0.0,1.0+2.0*_sigma2-abs(l2))/(1.0+2.0*_sigma2);
      }
    }
    float scale = (float)(scale1*scale2);

    // Correlation product.
    float[][] h = new float[n2][n1];
    int i1min = max(0,l1f,-l1g);
    int i1max = min(n1,n1+l1f,n1-l1g);
    int i2min = max(0,l2f,-l2g);
    int i2max = min(n2,n2+l2f,n2-l2g);
    for (int i2=i2min; i2<i2max; ++i2) {
      float[] f2 = f[i2-l2f];
      float[] g2 = g[i2+l2g];
      float[] h2 = h[i2];
      for (int i1=i1min; i1<i1max; ++i1) {
        h2[i1] = scale*f2[i1-l1f]*g2[i1+l1g];
      }
    }

    // If Gaussian and symmetric and odd lag, delay (shift) by 1/2 sample.
    if (_window==Window.GAUSSIAN && _type==Type.SYMMETRIC) {
      if (l1f!=l1g) {
        shift1(h,c);
        copy(c,h);
      }
      if (l2f!=l2g) {
        shift2(h,c);
        copy(c,h);
      }
    }

    // Filter correlation product with window. For symmetric correlations
    // with a rectangle window, the width of the product rectangle depends 
    // on the lag, so we construct a new rectangle filter for each lag.
    Filter f1 = _f1;
    Filter f2 = _f2;
    if (_window==Window.RECTANGLE && _type==Type.SYMMETRIC) {
      f1 = new RectangleFilter(_sigma1,l1);
      f2 = new RectangleFilter(_sigma2,l2);
    }
    f1.apply1(h,c);
    copy(c,h);
    f2.apply2(h,c);
  }

  private void correlate(
    int lag1, int lag2, int lag3, float[][][] f, float[][][] g, float[][][] c) 
  {
    Check.argument(f!=c,"f!=c");
    Check.argument(g!=c,"g!=c");
    int n1 = f[0][0].length;
    int n2 = f[0].length;
    int n3 = f.length;
    int l1 = lag1;
    int l2 = lag2;
    int l3 = lag3;

    // Conventional lags for f and g for simple correlation.
    int l1f = 0;
    int l1g = l1;
    int l2f = 0;
    int l2g = l2;
    int l3f = 0;
    int l3g = l3;

    // Shifted lags for symmetric correlation.
    if (_type==Type.SYMMETRIC) {
      l1f = (l1>=0)?(l1  )/2:(l1-1)/2;
      l1g = (l1>=0)?(l1+1)/2:(l1  )/2;
      l2f = (l2>=0)?(l2  )/2:(l2-1)/2;
      l2g = (l2>=0)?(l2+1)/2:(l2  )/2;
      l3f = (l3>=0)?(l3  )/2:(l3-1)/2;
      l3g = (l3>=0)?(l3+1)/2:(l3  )/2;
    }

    // Scale factor so that center of window = 1.
    double scale1 = 1.0;
    double scale2 = 1.0;
    double scale3 = 1.0;
    if (_window==Window.GAUSSIAN) {
      scale1 *= sqrt(2.0*PI)*_sigma1;
      scale2 *= sqrt(2.0*PI)*_sigma2;
      scale3 *= sqrt(2.0*PI)*_sigma3;
    } else {
      scale1 *= 1.0+2.0*_sigma1;
      scale2 *= 1.0+2.0*_sigma2;
      scale3 *= 1.0+2.0*_sigma3;
    }

    // If symmetric correlation, need extra lag-dependent scaling.
    // This scaling accounts for the separation (by lag samples) of 
    // the two windows implicitly applied to f and g. The filter we
    // apply below to the correlation product h is the product of 
    // those two windows.
    if (_type==Type.SYMMETRIC) {
      if (_window==Window.GAUSSIAN) {
        scale1 *= exp((-0.125*l1*l1)/(_sigma1*_sigma1));
        scale2 *= exp((-0.125*l2*l2)/(_sigma2*_sigma2));
        scale3 *= exp((-0.125*l3*l3)/(_sigma3*_sigma3));
      } else {
        scale1 *= max(0.0,1.0+2.0*_sigma1-abs(l1))/(1.0+2.0*_sigma1);
        scale2 *= max(0.0,1.0+2.0*_sigma2-abs(l2))/(1.0+2.0*_sigma2);
        scale3 *= max(0.0,1.0+2.0*_sigma3-abs(l3))/(1.0+2.0*_sigma3);
      }
    }
    float scale = (float)(scale1*scale2*scale3);

    // Correlation product.
    float[][][] h = new float[n3][n2][n1];
    int i1min = max(0,l1f,-l1g);
    int i1max = min(n1,n1+l1f,n1-l1g);
    int i2min = max(0,l2f,-l2g);
    int i2max = min(n2,n2+l2f,n2-l2g);
    int i3min = max(0,l3f,-l3g);
    int i3max = min(n3,n3+l3f,n3-l3g);
    for (int i3=i3min; i3<i3max; ++i3) {
      float[][] f3 = f[i3-l3f];
      float[][] g3 = g[i3+l3g];
      float[][] h3 = h[i3];
      for (int i2=i2min; i2<i2max; ++i2) {
        float[] f32 = f3[i2-l2f];
        float[] g32 = g3[i2+l2g];
        float[] h32 = h3[i2];
        for (int i1=i1min; i1<i1max; ++i1) {
          h32[i1] = scale*f32[i1-l1f]*g32[i1+l1g];
        }
      }
    }

    // If Gaussian and symmetric and odd lag, delay (shift) by 1/2 sample.
    if (_window==Window.GAUSSIAN && _type==Type.SYMMETRIC) {
      if (l1f!=l1g) {
        shift1(h,c);
        copy(c,h);
      }
      if (l2f!=l2g) {
        shift2(h,c);
        copy(c,h);
      }
      if (l3f!=l3g) {
        shift3(h,c);
        copy(c,h);
      }
    }

    // Filter correlation product with window. For symmetric correlations
    // with a rectangle window, the width of the product rectangle depends 
    // on the lag, so we construct a new rectangle filter for each lag.
    Filter f1 = _f1;
    Filter f2 = _f2;
    Filter f3 = _f3;
    if (_window==Window.RECTANGLE && _type==Type.SYMMETRIC) {
      f1 = new RectangleFilter(_sigma1,l1);
      f2 = new RectangleFilter(_sigma2,l2);
      f3 = new RectangleFilter(_sigma3,l3);
    }
    f1.apply1(h,c);
    copy(c,h);
    f2.apply2(h,c);
    copy(c,h);
    f3.apply3(h,c);
  }

  private void updateNormalize() {
    if (_dimension==0)
      return;
    int ns = (_type==Type.SIMPLE)?2:1;
    int n1 = max(1,_n1);
    int n2 = max(1,_n2);
    int n3 = max(1,_n3);
    _s = new float[ns][n3][n2][n1];
    if (_type==Type.SIMPLE) {
      if (_dimension==1) {
        float[] f = _f[0][0];
        float[] g = _g[0][0];
        float[] sf = _s[0][0][0];
        float[] sg = _s[1][0][0];
        correlate(0,f,f,sf);
        correlate(0,g,g,sg);
        sqrt(sf,sf);
        sqrt(sg,sg);
        div(1.0f,sf,sf);
        div(1.0f,sg,sg);
      } else if (_dimension==2) {
        float[][] f = _f[0];
        float[][] g = _g[0];
        float[][] sf = _s[0][0];
        float[][] sg = _s[1][0];
        correlate(0,0,f,f,sf);
        correlate(0,0,g,g,sg);
        sqrt(sf,sf);
        sqrt(sg,sg);
        div(1.0f,sf,sf);
        div(1.0f,sg,sg);
      } else {
        float[][][] f = _f;
        float[][][] g = _g;
        float[][][] sf = _s[0];
        float[][][] sg = _s[1];
        correlate(0,0,0,f,f,sf);
        correlate(0,0,0,g,g,sg);
        sqrt(sf,sf);
        sqrt(sg,sg);
        div(1.0f,sf,sf);
        div(1.0f,sg,sg);
      }
    } else {
      if (_dimension==1) {
        float[] f = _f[0][0];
        float[] g = _g[0][0];
        float[] s = _s[0][0][0];
        float[] sf = s;
        float[] sg = new float[_n1];
        correlate(0,f,f,sf);
        correlate(0,g,g,sg);
        mul(sf,sg,s);
        sqrt(s,s);
        div(1.0f,s,s);
      } else if (_dimension==2) {
        float[][] f = _f[0];
        float[][] g = _g[0];
        float[][] s = _s[0][0];
        float[][] sf = s;
        float[][] sg = new float[_n2][_n1];
        correlate(0,0,f,f,sf);
        correlate(0,0,g,g,sg);
        mul(sf,sg,s);
        sqrt(s,s);
        div(1.0f,s,s);
      } else {
        float[][][] f = _f;
        float[][][] g = _g;
        float[][][] s = _s[0];
        float[][][] sf = s;
        float[][][] sg = new float[_n3][_n2][_n1];
        correlate(0,0,0,f,f,sf);
        correlate(0,0,0,g,g,sg);
        mul(sf,sg,s);
        sqrt(s,s);
        div(1.0f,s,s);
      }
    }
  }

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

  private void checkDimension(int dimension) {
    Check.state(_dimension==dimension,"dimension is valid");
  }

  private void checkDimensions(float[] c) {
    Check.argument(_n1==c.length,"array length is valid");
    checkDimension(1);
  }

  private void checkDimensions(float[][] c) {
    Check.argument(isRegular(c),"c is regular");
    Check.argument(_n1==c[0].length,"array dimension 1 is valid");
    Check.argument(_n2==c.length,"array dimension 2 is valid");
    checkDimension(2);
  }

  private void checkDimensions(float[][][] c) {
    Check.argument(isRegular(c),"c is regular");
    Check.argument(_n1==c[0][0].length,"array dimension 1 is valid");
    Check.argument(_n2==c[0].length,"array dimension 2 is valid");
    Check.argument(_n3==c.length,"array dimension 3 is valid");
    checkDimension(3);
  }

  // This interface makes it easier to implement different windows.
  private interface Filter {
    public void apply(float[] x, float[] y);
    public void apply1(float[][] x, float[][] y);
    public void apply2(float[][] x, float[][] y);
    public void apply1(float[][][] x, float[][][] y);
    public void apply2(float[][][] x, float[][][] y);
    public void apply3(float[][][] x, float[][][] y);
  }
  private class RectangleFilter implements Filter {
    public RectangleFilter(double sigma) {
      this(sigma,0);
    }
    public RectangleFilter(double sigma, int lag) {
      int n = (int)round(1+2*sigma);
      int m = max(0,(n-1-abs(lag))/2);
      int l = (lag%2==0)?-m:-m-1;
      _rrf = new RecursiveRectangleFilter(l,m);
    }
    public void apply(float[] x, float[] y) {
      _rrf.apply(x,y);
    }
    public void apply1(float[][] x, float[][] y) {
      _rrf.apply1(x,y);
    }
    public void apply2(float[][] x, float[][] y) {
      _rrf.apply2(x,y);
    }
    public void apply1(float[][][] x, float[][][] y) {
      _rrf.apply1(x,y);
    }
    public void apply2(float[][][] x, float[][][] y) {
      _rrf.apply2(x,y);
    }
    public void apply3(float[][][] x, float[][][] y) {
      _rrf.apply3(x,y);
    }
    private RecursiveRectangleFilter _rrf;
  }
  private class GaussianFilter implements Filter {
    public GaussianFilter(double sigma) {
      _rgf = new RecursiveGaussianFilter(sigma);
    }
    public void apply(float[] x, float[] y) {
      _rgf.apply0(x,y);
    }
    public void apply1(float[][] x, float[][] y) {
      _rgf.apply0X(x,y);
    }
    public void apply2(float[][] x, float[][] y) {
      _rgf.applyX0(x,y);
    }
    public void apply1(float[][][] x, float[][][] y) {
      _rgf.apply0XX(x,y);
    }
    public void apply2(float[][][] x, float[][][] y) {
      _rgf.applyX0X(x,y);
    }
    public void apply3(float[][][] x, float[][][] y) {
      _rgf.applyXX0(x,y);
    }
    private RecursiveGaussianFilter _rgf;
  }
}
