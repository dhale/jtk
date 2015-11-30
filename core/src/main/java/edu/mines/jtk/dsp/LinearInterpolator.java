/****************************************************************************
Copyright 2011, Colorado School of Mines and others.
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

/**
 * A linear interpolator for uniformly-sampled functions y(x). 
 * Interpolation of functions y(x1,x2) is bi-linear.
 * Interpolation of functions y(x1,x2,x3) is tri-linear.
 * @author Dave Hale, Colorado School of Mines
 * @version 2011.02.27
 */
public class LinearInterpolator {

  /**
   * The method used to extrapolate samples when interpolating.
   * Sampled functions are defined implicitly by extrapolation outside the 
   * domain for which samples are specified explicitly, with either zero or 
   * constant values. If constant, the extrapolated values are the first and 
   * last uniform sample values. The default is extrapolation with zeros.
   */
  public enum Extrapolation {
    ZERO, 
    CONSTANT,
  }

  /**
   * Gets the extrapolation method for this interpolator.
   * @return the extrapolation method.
   */
  public Extrapolation getExtrapolation() {
    return _extrap;
  }

  /**
   * Sets the extrapolation method for this interpolator.
   * The default extrapolation method is extrapolation with zeros.
   * @param extrap the extrapolation method.
   */
  public void setExtrapolation(Extrapolation extrap) {
    _extrap = extrap;
  }

  /**
   * Sets the current sampling for a uniformly-sampled function y(x).
   * In some applications, this sampling never changes, and this method 
   * may be called only once for this interpolator.
   * @param nxu the number of uniform samples.
   * @param dxu the uniform sampling interval.
   * @param fxu the value x corresponding to the first uniform sample yu[0].
   */
  public void setUniformSampling(int nxu, double dxu, double fxu) {
    _nxu = nxu;
    _dxu = dxu;
    _fxu = fxu;
    _xf = fxu;
    _xs = 1.0/dxu;
    _xb = 2.0-_xf*_xs;
    _nxum = nxu-1;
  }

  /**
   * Sets the current samples for a uniformly-sampled function y(x).
   * If sample values are complex numbers, real and imaginary parts are 
   * packed in the array as real, imaginary, real, imaginary, and so on.
   * <p>
   * Sample values are passed by reference, not by copy. Changes to sample
   * values in the specified array will yield changes in interpolated values.
   * @param yu array[nxu] of uniform samples of y(x); 
   *  by reference, not by copy.
   */
  public void setUniformSamples(float[] yu) {
    _yu = yu;
  }

  /**
   * Sets the current sampling and samples for a function y(x). 
   * This method simply calls the two methods 
   * {@link #setUniformSampling(int,double,double)} and
   * {@link #setUniformSamples(float[])} 
   * with the specified parameters.
   * @param nxu the number of uniform samples.
   * @param dxu the uniform sampling interval.
   * @param fxu the value x corresponding to the first uniform sample yu[0].
   * @param yu array[nxu] of uniform samples of y(x); 
   *  by reference, not by copy.
   */
  public void setUniform(int nxu, double dxu, double fxu, float[] yu) {
    setUniformSampling(nxu,dxu,fxu);
    setUniformSamples(yu);
  }

  /**
   * Interpolates the current uniform samples as real numbers.
   * @param x the value x at which to interpolate y(x).
   * @return the interpolated y(x).
   */
  public float interpolate(double x) {
    int jyu;
    float yr;
    double xn = _xb+x*_xs;
    int ixn = (int)xn;
    float a1 = (float)(xn-ixn);
    float a0 = 1.0f-a1;
    int kyu = ixn-2;
    if (0<=kyu && kyu<_nxum) {
      yr = a0*_yu[kyu]+a1*_yu[kyu+1];
    } else if (_extrap==Extrapolation.ZERO) {
      yr = 0.0f;
      if (0<=kyu && kyu<_nxu)
        yr += a0*_yu[kyu];
      ++kyu;
      if (0<=kyu && kyu<_nxu)
        yr += a1*_yu[kyu];
    } else { // Extrapolation.CONSTANT)
      jyu = (kyu<0)?0:(_nxu<=kyu)?_nxum:kyu;
      yr = a0*_yu[jyu];
      ++kyu;
      jyu = (kyu<0)?0:(_nxu<=kyu)?_nxum:kyu;
      yr += a1*_yu[jyu];
    }
    return yr;
  }

  /**
   * Interpolates the current uniform samples as real numbers.
   * @param nx the number of output samples.
   * @param x array[nx] of values x at which to interpolate y(x).
   * @param y array[nx] of interpolated output y(x).
   */
  public void interpolate(int nx, float[] x, float[] y) {
    for (int ix=0;  ix<nx; ++ix)
      y[ix] = interpolate(x[ix]);
  }

  /**
   * Interpolates the current uniform samples as real numbers. 
   * <p>
   * This method does not perform any anti-alias filtering, which may or 
   * may not be necessary to avoid aliasing when the specified output
   * sampling interval exceeds the current uniform sampling interval.
   * @param nx the number of output samples.
   * @param dx the output sampling interval.
   * @param fx the value x corresponding to the first output sample y[0].
   * @param y array[nx] of interpolated output y(x).
   */
  public void interpolate(int nx, double dx, double fx, float[] y) {
    for (int ix=0; ix<nx; ++ix)
      y[ix] = interpolate(fx+ix*dx);
  }

  /**
   * Sets the current sampling for a uniformly-sampled function y(x1,x2).
   * In some applications, this sampling never changes, and this method 
   * may be called only once for this interpolator.
   * @param nx1u the number of uniform samples in 1st dimension.
   * @param dx1u the uniform sampling interval in 1st dimension.
   * @param fx1u the value x1 correponding to the first sample yu[0][0].
   * @param nx2u the number of uniform samples in 2nd dimension.
   * @param dx2u the uniform sampling interval in 2nd dimension.
   * @param fx2u the value x2 correponding to the first sample yu[0][0].
   */
  public void setUniformSampling(
    int nx1u, double dx1u, double fx1u,
    int nx2u, double dx2u, double fx2u) {
    _nx1u = nx1u;
    _x1f = fx1u;
    _x1s = 1.0/dx1u;
    _x1b = 2-_x1f*_x1s;
    _nx1um = nx1u-1;
    _nx2u = nx2u;
    _x2f = fx2u;
    _x2s = 1.0/dx2u;
    _x2b = 2-_x2f*_x2s;
    _nx2um = nx2u-1;
  }

  /**
   * Sets the current samples for a uniformly-sampled function y(x1,x2).
   * If sample values are complex numbers, real and imaginary parts are 
   * packed in the array as real, imaginary, real, imaginary, and so on.
   * <p>
   * Sample values are passed by reference, not by copy. Changes to sample
   * values in the specified array will yield changes in interpolated values.
   * @param yu array[nx2u][nx1u] of samples of y(x1,x2); 
   *  by reference, not by copy.
   */
  public void setUniformSamples(float[][] yu) {
    _yyu = yu;
  }

  /**
   * Sets the current sampling and samples for a function y(x1,x2). 
   * This method simply calls the two methods 
   * {@link #setUniformSampling(int,double,double,int,double,double)} and
   * {@link #setUniformSamples(float[][])} 
   * with the specified parameters.
   * @param nx1u the number of uniform samples in 1st dimension.
   * @param dx1u the uniform sampling interval in 1st dimension.
   * @param fx1u the value x1 correponding to the first sample yu[0][0].
   * @param nx2u the number of uniform samples in 2nd dimension.
   * @param dx2u the uniform sampling interval in 2nd dimension.
   * @param fx2u the value x2 correponding to the first sample yu[0][0].
   * @param yu array[nx2u][nx1u] of samples of y(x1,x2); 
   *  by reference, not by copy.
   */
  public void setUniform(
    int nx1u, double dx1u, double fx1u,
    int nx2u, double dx2u, double fx2u,
    float[][] yu) {
    setUniformSampling(nx1u,dx1u,fx1u,nx2u,dx2u,fx2u);
    setUniformSamples(yu);
  }

  /**
   * Interpolates the current uniform samples as real numbers.
   * @param x1 the value x1 at which to interpolate y(x1,x2).
   * @param x2 the value x2 at which to interpolate y(x1,x2).
   * @return the interpolated y(x1,x2).
   */
  public float interpolate(double x1, double x2) {
    int jy1u,jy2u,my1u;
    float yr;
    double x1n = _x1b+x1*_x1s;
    double x2n = _x2b+x2*_x2s;
    int ix1n = (int)x1n;
    int ix2n = (int)x2n;
    int ky1u = ix1n-2;
    int ky2u = ix2n-2;
    float a11 = (float)(x1n-ix1n);
    float a12 = (float)(x2n-ix2n);
    float a01 = 1.0f-a11;
    float a02 = 1.0f-a12;
    if (ky1u>=0 && ky1u<_nx1um && 
        ky2u>=0 && ky2u<_nx2um) {
      float[] yyuk0 = _yyu[ky2u  ];
      float[] yyuk1 = _yyu[ky2u+1];
      yr = a01*a02*yyuk0[ky1u  ] +
           a11*a02*yyuk0[ky1u+1] +
           a01*a12*yyuk1[ky1u  ] +
           a11*a12*yyuk1[ky1u+1];
    } else if (_extrap==Extrapolation.ZERO) {
      yr = 0.0f;
      if (0<=ky2u && ky2u<_nx2u) {
        my1u = ky1u;
        if (0<=my1u && my1u<_nx1u)
          yr += a01*a02*_yyu[ky2u][my1u];
        ++my1u;
        if (0<=my1u && my1u<_nx1u)
          yr += a11*a02*_yyu[ky2u][my1u];
      }
      ++ky2u;
      if (0<=ky2u && ky2u<_nx2u) {
        my1u = ky1u;
        if (0<=my1u && my1u<_nx1u)
          yr += a01*a12*_yyu[ky2u][my1u];
        ++my1u;
        if (0<=my1u && my1u<_nx1u)
          yr += a11*a12*_yyu[ky2u][my1u];
      }
    } else { // Extrapolation.CONSTANT
      jy2u = (ky2u<0)?0:(_nx2u<=ky2u)?_nx2um:ky2u;
      my1u = ky1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr = a01*a02*_yyu[jy2u][jy1u];
      ++my1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a11*a02*_yyu[jy2u][jy1u];
      ++ky2u;
      jy2u = (ky2u<0)?0:(_nx2u<=ky2u)?_nx2um:ky2u;
      my1u = ky1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a01*a12*_yyu[jy2u][jy1u];
      ++my1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a11*a12*_yyu[jy2u][jy1u];
    }
    return yr;
  }

  /**
   * Sets the current sampling for a uniformly-sampled function y(x1,x2,x3).
   * In some applications, this sampling never changes, and this method 
   * may be called only once for this interpolator.
   * @param nx1u the number of uniform samples in 1st dimension.
   * @param dx1u the uniform sampling interval in 1st dimension.
   * @param fx1u the value x1 correponding to the first sample yu[0][0][0].
   * @param nx2u the number of uniform samples in 2nd dimension.
   * @param dx2u the uniform sampling interval in 2nd dimension.
   * @param fx2u the value x2 correponding to the first sample yu[0][0][0].
   * @param nx3u the number of uniform samples in 3rd dimension.
   * @param dx3u the uniform sampling interval in 3rd dimension.
   * @param fx3u the value x3 correponding to the first sample yu[0][0][0].
   */
  public void setUniformSampling(
    int nx1u, double dx1u, double fx1u,
    int nx2u, double dx2u, double fx2u,
    int nx3u, double dx3u, double fx3u) 
  {
    _nx1u = nx1u;
    _x1f = fx1u;
    _x1s = 1.0/dx1u;
    _x1b = 2-_x1f*_x1s;
    _nx1um = nx1u-1;
    _nx2u = nx2u;
    _x2f = fx2u;
    _x2s = 1.0/dx2u;
    _x2b = 2-_x2f*_x2s;
    _nx2um = nx2u-1;
    _nx3u = nx3u;
    _x3f = fx3u;
    _x3s = 1.0/dx3u;
    _x3b = 2-_x3f*_x3s;
    _nx3um = nx3u-1;
  }

  /**
   * Sets the current samples for a uniformly-sampled function y(x1,x2,x3).
   * If sample values are complex numbers, real and imaginary parts are 
   * packed in the array as real, imaginary, real, imaginary, and so on.
   * <p>
   * Sample values are passed by reference, not by copy. Changes to sample
   * values in the specified array will yield changes in interpolated values.
   * @param yu array[nx3u][nx2u][nx1u] of samples of y(x1,x2,x3); 
   *  by reference, not by copy.
   */
  public void setUniformSamples(float[][][] yu) {
    _yyyu = yu;
  }

  /**
   * Sets the current sampling and samples for a function y(x1,x2,x3). 
   * This method simply calls the two methods 
   * {@link #setUniformSampling(
   *          int,double,double,int,double,double,int,double,double)} and
   * {@link #setUniformSamples(float[][][])} 
   * with the specified parameters.
   * @param nx1u the number of uniform samples in 1st dimension.
   * @param dx1u the uniform sampling interval in 1st dimension.
   * @param fx1u the value x1 correponding to the first sample yu[0][0][0].
   * @param nx2u the number of uniform samples in 2nd dimension.
   * @param dx2u the uniform sampling interval in 2nd dimension.
   * @param fx2u the value x2 correponding to the first sample yu[0][0][0].
   * @param nx3u the number of uniform samples in 3rd dimension.
   * @param dx3u the uniform sampling interval in 3rd dimension.
   * @param fx3u the value x3 correponding to the first sample yu[0][0][0].
   * @param yu array[nx3u][nx2u][nx1u] of samples of y(x1,x2,x3); 
   *  by reference, not by copy.
   */
  public void setUniform(
    int nx1u, double dx1u, double fx1u,
    int nx2u, double dx2u, double fx2u,
    int nx3u, double dx3u, double fx3u,
    float[][][] yu) 
  {
    setUniformSampling(nx1u,dx1u,fx1u,nx2u,dx2u,fx2u,nx3u,dx3u,fx3u);
    setUniformSamples(yu);
  }

  /**
   * Interpolates the current uniform samples as real numbers.
   * @param x1 the value x1 at which to interpolate y(x1,x2,x3).
   * @param x2 the value x2 at which to interpolate y(x1,x2,x3).
   * @param x3 the value x3 at which to interpolate y(x1,x2,x3).
   * @return the interpolated y(x1,x2,x3).
   */
  public float interpolate(double x1, double x2, double x3) {
    int jy1u,jy2u,jy3u,my1u,my2u;
    float yr;
    double x1n = _x1b+x1*_x1s;
    double x2n = _x2b+x2*_x2s;
    double x3n = _x3b+x3*_x3s;
    int ix1n = (int)x1n;
    int ix2n = (int)x2n;
    int ix3n = (int)x3n;
    int ky1u = ix1n-2;
    int ky2u = ix2n-2;
    int ky3u = ix3n-2;
    float a11 = (float)(x1n-ix1n);
    float a12 = (float)(x2n-ix2n);
    float a13 = (float)(x3n-ix3n);
    float a01 = 1.0f-a11;
    float a02 = 1.0f-a12;
    float a03 = 1.0f-a13;
    if (ky1u>=0 && ky1u<_nx1um &&  
        ky2u>=0 && ky2u<_nx2um &&
        ky3u>=0 && ky3u<_nx3um) {
      float[] yyyuk00 = _yyyu[ky3u  ][ky2u  ];
      float[] yyyuk01 = _yyyu[ky3u  ][ky2u+1];
      float[] yyyuk10 = _yyyu[ky3u+1][ky2u  ];
      float[] yyyuk11 = _yyyu[ky3u+1][ky2u+1];
      yr = a01*a02*a03*yyyuk00[ky1u  ] +
           a11*a02*a03*yyyuk00[ky1u+1] +
           a01*a12*a03*yyyuk01[ky1u  ] +
           a11*a12*a03*yyyuk01[ky1u+1] +
           a01*a02*a13*yyyuk10[ky1u  ] +
           a11*a02*a13*yyyuk10[ky1u+1] +
           a01*a12*a13*yyyuk11[ky1u  ] +
           a11*a12*a13*yyyuk11[ky1u+1];
    } else if (_extrap==Extrapolation.ZERO) {
      yr = 0.0f;
      if (0<=ky3u && ky3u<_nx3u) {
        my2u = ky2u;
        if (0<=my2u && my2u<_nx2u) {
          my1u = ky1u;
          if (0<=my1u && my1u<_nx1u)
            yr += a01*a02*a03*_yyyu[ky3u][my2u][my1u];
          ++my1u;
          if (0<=my1u && my1u<_nx1u)
            yr += a11*a02*a03*_yyyu[ky3u][my2u][my1u];
        }
        ++my2u;
        if (0<=my2u && my2u<_nx2u) {
          my1u = ky1u;
          if (0<=my1u && my1u<_nx1u)
            yr += a01*a12*a03*_yyyu[ky3u][my2u][my1u];
          ++my1u;
          if (0<=my1u && my1u<_nx1u)
            yr += a11*a12*a03*_yyyu[ky3u][my2u][my1u];
        }
      }
      ++ky3u;
      if (0<=ky3u && ky3u<_nx3u) {
        my2u = ky2u;
        if (0<=my2u && my2u<_nx2u) {
          my1u = ky1u;
          if (0<=my1u && my1u<_nx1u)
            yr += a01*a02*a13*_yyyu[ky3u][my2u][my1u];
          ++my1u;
          if (0<=my1u && my1u<_nx1u)
            yr += a11*a02*a13*_yyyu[ky3u][my2u][my1u];
        }
        ++my2u;
        if (0<=my2u && my2u<_nx2u) {
          my1u = ky1u;
          if (0<=my1u && my1u<_nx1u)
            yr += a01*a12*a13*_yyyu[ky3u][my2u][my1u];
          ++my1u;
          if (0<=my1u && my1u<_nx1u)
            yr += a11*a12*a13*_yyyu[ky3u][my2u][my1u];
        }
      }
    } else { // Extrapolation.CONSTANT
      jy3u = (ky3u<0)?0:(_nx3u<=ky3u)?_nx3um:ky3u;
      my2u = ky2u;
      jy2u = (my2u<0)?0:(_nx2u<=my2u)?_nx2um:my2u;
      my1u = ky1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr = a01*a02*a03*_yyyu[jy3u][jy2u][jy1u];
      ++my1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a11*a02*a03*_yyyu[jy3u][jy2u][jy1u];
      ++my2u;
      jy2u = (my2u<0)?0:(_nx2u<=my2u)?_nx2um:my2u;
      my1u = ky1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a01*a12*a03*_yyyu[jy3u][jy2u][jy1u];
      ++my1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a11*a12*a03*_yyyu[jy3u][jy2u][jy1u];
      ++ky3u;
      jy3u = (ky3u<0)?0:(_nx3u<=ky3u)?_nx3um:ky3u;
      my2u = ky2u;
      jy2u = (my2u<0)?0:(_nx2u<=my2u)?_nx2um:my2u;
      my1u = ky1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a01*a02*a13*_yyyu[jy3u][jy2u][jy1u];
      ++my1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a11*a02*a13*_yyyu[jy3u][jy2u][jy1u];
      ++my2u;
      jy2u = (my2u<0)?0:(_nx2u<=my2u)?_nx2um:my2u;
      my1u = ky1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a01*a12*a13*_yyyu[jy3u][jy2u][jy1u];
      ++my1u;
      jy1u = (my1u<0)?0:(_nx1u<=my1u)?_nx1um:my1u;
      yr += a11*a12*a13*_yyyu[jy3u][jy2u][jy1u];
    }
    return yr;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Extrapolation method.
  private Extrapolation _extrap = Extrapolation.ZERO;

  // Current uniform sampling.
  private int _nxu;
  private double _dxu;
  private double _fxu;
  private double _xf;
  private double _xs;
  private double _xb;
  private int _nxum;

  // Current uniform samples.
  private float[] _yu;

  // Current 2-D or 3-D uniform sampling.
  private int _nx1u,_nx2u,_nx3u;
  private double _x1f,_x2f,_x3f;
  private double _x1s,_x2s,_x3s;
  private double _x1b,_x2b,_x3b;
  private int _nx1um,_nx2um,_nx3um;

  // Current 2-D or 3-D uniform samples.
  private float[][] _yyu;
  private float[][][] _yyyu;
}
