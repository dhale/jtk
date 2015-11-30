/****************************************************************************
Copyright 2008, Colorado School of Mines and others.
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

import java.util.logging.Logger;

import edu.mines.jtk.util.Parallel;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Local smoothing of images with tensor filter coefficients.
 * Smoothing is performed by solving a sparse symmetric positive-definite
 * (SPD) system of equations: (I+G'DG)y = x, where G is a gradient operator, 
 * D is an SPD tensor field, x is an input image, and y is an output image.
 * <p>
 * The sparse system of filter equations (I+G'DG)y = x is solved iteratively, 
 * beginning with y = x. Iterations continue until either the error in the 
 * solution y is below a specified threshold or the number of iterations 
 * exceeds a specified limit.
 * <p>
 * For low wavenumbers the output of this filter approximates the solution 
 * to an anisotropic inhomogeneous diffusion equation, where the filter 
 * input x corresponds to the initial condition at time t = 0 and filter 
 * output y corresponds to the solution at some later time t.
 * <p>
 * Additional smoothing filters may be applied to the input image x before 
 * or after solving the sparse system of equations for the smoothed output 
 * image y. These additional filters compensate for deficiencies in the 
 * gradient operator G, which is a finite-difference approximation that is 
 * poor for high wavenumbers near the Nyquist limit. The extra smoothing
 * filters attenuate these high wavenumbers.
 * <p> 
 * The additional smoothing filter S is a simple 3x3 (or, in 3D, 3x3x3) 
 * weighted-average filter that zeros Nyquist wavenumbers. This filter 
 * is fast and has non-negative coefficients. However, it may smooth too 
 * much, as it attenuates all non-zero wavenumbers, not only the highest
 * wavenumbers. Moreover, this filter is not isotropic. 
 * <p>
 * The other additional smoothing operator L is an isotropic low-pass 
 * filter designed to pass wavenumbers up to a specified maximum.
 * Although slower than S, the cost of applying L to the input image x is 
 * likely to be insignificant relative to the cost of solving the sparse 
 * system of equations for the output image y.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.12.31
 */
public class LocalSmoothingFilter {

  /**
   * Constructs a local smoothing filter with default parameters.
   * The default parameter small is 0.01 and the default maximum 
   * number of iterations is 100. Uses a default 2x2 stencil for the 
   * derivatives in the operator G.
   */
  public LocalSmoothingFilter() {
    this(0.01,100);
  }

  /**
   * Constructs a local smoothing filter with specified iteration parameters.
   * Uses a default 2x2 stencil for the derivatives in the operator G.
   * @param small stop when norm of residuals is less than this factor 
   *  times the norm of the input array.
   * @param niter stop when number of iterations exceeds this limit.
   */
  public LocalSmoothingFilter(double small, int niter) {
    _small = (float)small;
    _niter = niter;
    _ldk = new LocalDiffusionKernel(LocalDiffusionKernel.Stencil.D22);
  }

  /**
   * Constructs a local smoothing filter with specified parameters.
   * @param small stop when norm of residuals is less than this factor 
   *  times the norm of the input array.
   * @param niter stop when number of iterations exceeds this limit.
   * @param ldk the local diffusion kernel that computes y += (I+G'DG)x.
   */
  public LocalSmoothingFilter(
    double small, int niter, LocalDiffusionKernel ldk)
  {
    _small = (float)small;
    _niter = niter;
    _ldk = ldk;
  }

  /**
   * Sets the use of a preconditioner in this local smoothing filter.
   * A preconditioner requires extra memory and more computing time
   * per iteration, but may result in fewer iterations.
   * The default is to not use a preconditioner.
   * @param pc true, to use a preconditioner; false, otherwise.
   */
  public void setPreconditioner(boolean pc) {
    _pc = pc;
  }

  /**
   * Applies this filter for specified constant scale factor.
   * Local smoothing for 1D arrays is a special case that requires no tensors. 
   * All tensors are implicitly scalar values equal to one, so that filtering 
   * is determined entirely by the specified constant scale factor.
   * @param c constant scale factor.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float c, float[] x, float[] y) {
    apply(c,null,x,y);
  }

  /**
   * Applies this filter for specified scale factors.
   * Local smoothing for 1D arrays is a special case that requires no tensors. 
   * All tensors are implicitly scalar values equal to one, so that filtering 
   * is determined entirely by the specified scale factors.
   * @param c constant scale factor.
   * @param s array of scale factors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float c, float[] s, float[] x, float[] y) {
    int n1 = x.length;

    // Sub-diagonal e of SPD tridiagonal matrix I+G'DG; e[0] = e[n1] = 0.0.
    float[] e = new float[n1+1];
    if (s!=null) {
      c = -0.5f*c;
      for (int i1=1; i1<n1; ++i1)
        e[i1] = c*(s[i1]+s[i1-1]);
    } else {
      c = -c;
      for (int i1=1; i1<n1; ++i1)
        e[i1] = c;
    }

    // Work array w overwrites sub-diagonal array e.
    float[] w = e;

    // Solve tridiagonal system of equations (I+G'DG)y = x.
    float t = 1.0f-e[0]-e[1];
    y[0] = x[0]/t;
    for (int i1=1; i1<n1; ++i1) {
      float di = 1.0f-e[i1]-e[i1+1]; // diagonal element
      float ei = e[i1]; // sub-diagonal element
      w[i1] = ei/t;
      t = di-ei*w[i1];
      y[i1] = (x[i1]-ei*y[i1-1])/t;
    }
    for (int i1=n1-1; i1>0; --i1)
      y[i1-1] -= w[i1]*y[i1];
  }

  /**
   * Applies this filter for identity tensors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][] x, float[][] y) 
  {
    apply(null,1.0f,null,x,y);
  }

  /**
   * Applies this filter for identity tensors and specified scale factor.
   * @param c constant scale factor.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float c, float[][] x, float[][] y) {
    apply(null,c,null,x,y);
  }

  /**
   * Applies this filter for identity tensors and specified scale factors.
   * @param c constant scale factor.
   * @param s array of scale factors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float c, float[][] s, float[][] x, float[][] y) {
    apply(null,c,s,x,y);
  }

  /**
   * Applies this filter for specified tensors.
   * @param d tensors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(Tensors2 d, float[][] x, float[][] y) 
  {
    apply(d,1.0f,null,x,y);
  }

  /**
   * Applies this filter for specified tensors and scale factor.
   * @param d tensors.
   * @param c constant scale factor for tensors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(Tensors2 d, float c, float[][] x, float[][] y) {
    apply(d,c,null,x,y);
  }

  /**
   * Applies this filter for specified tensors and scale factors.
   * @param d tensors.
   * @param c constant scale factor for tensors.
   * @param s array of scale factors for tensors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(
    Tensors2 d, float c, float[][] s, float[][] x, float[][] y) 
  {
    Operator2 a = new A2(_ldk,d,c,s);
    scopy(x,y);
    if (_pc) {
      Operator2 m = new M2(d,c,s,x);
      solve(a,m,x,y);
    } else {
      solve(a,x,y);
    }
  }

  /**
   * Applies this filter for identity tensors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][][] x, float[][][] y) 
  {
    apply(null,1.0f,null,x,y);
  }

  /**
   * Applies this filter for identity tensors and specified scale factor.
   * @param c constant scale factor.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float c, float[][][] x, float[][][] y) {
    apply(null,c,null,x,y);
  }

  /**
   * Applies this filter for identity tensors and specified scale factors.
   * @param c constant scale factor.
   * @param s array of scale factors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float c, float[][][] s, float[][][] x, float[][][] y) {
    apply(null,c,s,x,y);
  }

  /**
   * Applies this filter for specified tensors.
   * @param d tensors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(Tensors3 d, float[][][] x, float[][][] y) 
  {
    apply(d,1.0f,null,x,y);
  }

  /**
   * Applies this filter for specified tensors and scale factor.
   * @param d tensors.
   * @param c constant scale factor for tensors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(Tensors3 d, float c, float[][][] x, float[][][] y) {
    apply(d,c,null,x,y);
  }

  /**
   * Applies this filter for specified tensors and scale factors.
   * @param d tensors.
   * @param c constant scale factor for tensors.
   * @param s array of scale factors for tensors.
   * @param x input array.
   * @param y output array.
   */
  public void apply(
    Tensors3 d, float c, float[][][] s, float[][][] x, float[][][] y) 
  {
    Operator3 a = new A3(_ldk,d,c,s);
    scopy(x,y);
    if (_pc) {
      Operator3 m = new M3(d,c,s,x);
      solve(a,m,x,y);
    } else {
      solve(a,x,y);
    }
  }

  /**
   * Applies a simple 3x3 weighted-average smoothing filter S.
   * Input and output arrays x and y may be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void applySmoothS(float[][] x, float[][] y) {
    smoothS(x,y);
  }

  /**
   * Applies a simple 3x3x3 weighted-average smoothing filter S.
   * Input and output arrays x and y may be the same array.
   * @param x input array.
   * @param y output array.
   */
  public void applySmoothS(float[][][] x, float[][][] y) {
    smoothS(x,y);
  }

  /**
   * Applies an isotropic low-pass smoothing filter L.
   * Input and output arrays x and y may be the same array.
   * @param kmax maximum wavenumber not attenuated, in cycles/sample.
   * @param x input array.
   * @param y output array.
   */
  public void applySmoothL(double kmax, float[][] x, float[][] y) {
    smoothL(kmax,x,y);
  }

  /**
   * Applies an isotropic low-pass smoothing filter L.
   * Input and output arrays x and y may be the same array.
   * @param kmax maximum wavenumber not attenuated, in cycles/sample.
   * @param x input array.
   * @param y output array.
   */
  public void applySmoothL(double kmax, float[][][] x, float[][][] y) {
    smoothL(kmax,x,y);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final boolean PARALLEL = true; // false for single-threaded

  private static Logger log = 
    Logger.getLogger(LocalSmoothingFilter.class.getName());

  private float _small; // stop iterations when residuals are small
  private int _niter; // number of iterations
  private boolean _pc; // true, for preconditioned CG iterations
  private LocalDiffusionKernel _ldk; // computes y += (I+G'DG)x
  private BandPassFilter _lpf; // lowpass filter, null until applied
  private double _kmax; // maximum wavenumber for lowpass filter

  /*
   * A symmetric positive-definite operator.
   */
  private static interface Operator2 {
    public void apply(float[][] x, float[][] y);
  }
  private static interface Operator3 {
    public void apply(float[][][] x, float[][][] y);
  }

  private static class A2 implements Operator2 {
    A2(LocalDiffusionKernel ldk, Tensors2 d, float c, float[][] s) {
      _ldk = ldk;
      _d = d;
      _c = c;
      _s = s;
    }
    public void apply(float[][] x, float[][] y) {
      scopy(x,y);
      _ldk.apply(_d,_c,_s,x,y);
    }
    private LocalDiffusionKernel _ldk;
    private Tensors2 _d;
    private float _c;
    private float[][] _s;
  }

  private static class M2 implements Operator2 {
    M2(Tensors2 d, float c, float[][] s, float[][] x)  {
      int n1 = x[0].length;
      int n2 = x.length;
      _p = fillfloat(1.0f,n1,n2);
      c *= 0.25f;
      float[] di = new float[3];
      for (int i2=1,m2=0; i2<n2; ++i2,++m2) {
        for (int i1=1,m1=0; i1<n1; ++i1,++m1) {
          float si = s!=null?s[i2][i1]:1.0f;
          float csi = c*si;
          float d11 = csi;
          float d12 = 0.0f;
          float d22 = csi;
          if (d!=null) {
            d.getTensor(i1,i2,di);
            d11 = di[0]*csi;
            d12 = di[1]*csi;
            d22 = di[2]*csi;
          }
          _p[i2][i1] += (d11+d12)+( d12+d22);
          _p[m2][m1] += (d11+d12)+( d12+d22);
          _p[i2][m1] += (d11-d12)+(-d12+d22);
          _p[m2][i1] += (d11-d12)+(-d12+d22);
        }
      }
      div(1.0f,_p,_p);
    }
    public void apply(float[][] x, float[][] y) {
      sxy(_p,x,y);
    }
    private float[][] _p;
  }

  private static class A3 implements Operator3 {
    A3(LocalDiffusionKernel ldk, Tensors3 d, float c, float[][][] s) {
      _ldk = ldk;
      _d = d;
      _c = c;
      _s = s;
    }
    public void apply(float[][][] x, float[][][] y) {
      scopy(x,y);
      _ldk.apply(_d,_c,_s,x,y);
    }
    private LocalDiffusionKernel _ldk;
    private Tensors3 _d;
    private float _c;
    private float[][][] _s;
  }

  private static class M3 implements Operator3 {
    M3(Tensors3 d, float c, float[][][] s, float[][][] x)  {
      int n1 = x[0][0].length;
      int n2 = x[0].length;
      int n3 = x.length;
      _p = fillfloat(1.0f,n1,n2,n3);
      c *= 0.0625f;
      float[] di = new float[6];
      for (int i3=1,m3=0; i3<n3; ++i3,++m3) {
        for (int i2=1,m2=0; i2<n2; ++i2,++m2) {
          for (int i1=1,m1=0; i1<n1; ++i1,++m1) {
            float si = s!=null?s[i3][i2][i1]:1.0f;
            float csi = c*si;
            float d11 = csi;
            float d12 = 0.0f;
            float d13 = 0.0f;
            float d22 = csi;
            float d23 = 0.0f;
            float d33 = csi;
            if (d!=null) {
              d.getTensor(i1,i2,i3,di);
              d11 = di[0]*csi;
              d12 = di[1]*csi;
              d13 = di[2]*csi;
              d22 = di[3]*csi;
              d23 = di[4]*csi;
              d33 = di[5]*csi;
            }
            _p[i3][i2][i1] += ( d11+d12+d13)+( d12+d22+d23)+( d13+d23+d33);
            _p[m3][m2][m1] += ( d11+d12+d13)+( d12+d22+d23)+( d13+d23+d33);
            _p[i3][m2][i1] += ( d11-d12+d13)+(-d12+d22-d23)+( d13-d23+d33);
            _p[m3][i2][m1] += ( d11-d12+d13)+(-d12+d22-d23)+( d13-d23+d33);
            _p[m3][i2][i1] += ( d11+d12-d13)+( d12+d22-d23)+(-d13-d23+d33);
            _p[i3][m2][m1] += ( d11+d12-d13)+( d12+d22-d23)+(-d13-d23+d33);
            _p[m3][m2][i1] += ( d11-d12-d13)+(-d12+d22+d23)+(-d13+d23+d33);
            _p[i3][i2][m1] += ( d11-d12-d13)+(-d12+d22+d23)+(-d13+d23+d33);
          }
        }
      }
      div(1.0f,_p,_p);
    }
    public void apply(float[][][] x, float[][][] y) {
      sxy(_p,x,y);
    }
    private float[][][] _p;
  }

  /*
   * Computes y = lowpass(x). Arrays x and y may be the same array.
   */
  private void smoothL(double kmax, float[][] x, float[][] y) {
    ensureLowpassFilter(kmax);
    _lpf.apply(x,y);
  }
  private void smoothL(double kmax, float[][][] x, float[][][] y) {
    ensureLowpassFilter(kmax);
    _lpf.apply(x,y);
  }
  private void ensureLowpassFilter(double kmax) {
    if (_lpf==null || _kmax!=kmax) {
      _kmax = kmax;
      double kdelta = 0.5-kmax;
      double kupper = kmax+0.5*kdelta;
      _lpf = new BandPassFilter(0.0,kupper,kdelta,0.01);
      _lpf.setExtrapolation(BandPassFilter.Extrapolation.ZERO_SLOPE);
      _lpf.setFilterCaching(false);
    }
  }

  /*
   * Computes y = S'Sx. Arrays x and y may be the same array.
   */
  private static void smoothS(float[][] x, float[][] y) {
    int n1 = x[0].length;
    int n2 = x.length;
    int n1m = n1-1;
    int n2m = n2-1;
    float[][] t = new float[3][n1];
    scopy(x[0],t[0]);
    scopy(x[0],t[1]);
    for (int i2=0; i2<n2; ++i2) {
      int i2m = (i2>0)?i2-1:0;
      int i2p = (i2<n2m)?i2+1:n2m;
      int j2m = i2m%3;
      int j2  = i2%3;
      int j2p = i2p%3;
      scopy(x[i2p],t[j2p]);
      float[] x2m = t[j2m];
      float[] x2p = t[j2p];
      float[] x20 = t[j2];
      float[] y2 = y[i2];
      for (int i1=0; i1<n1; ++i1) {
        int i1m = (i1>0)?i1-1:0;
        int i1p = (i1<n1m)?i1+1:n1m;
        y2[i1] = 0.2500f*(x20[i1 ]) +
                 0.1250f*(x20[i1m]+x20[i1p]+x2m[i1 ]+x2p[i1 ]) +
                 0.0625f*(x2m[i1m]+x2m[i1p]+x2p[i1m]+x2p[i1p]);
      }
    }
  }

  /*
   * Computes y = S'Sx. Arrays x and y may be the same array.
   */
  private static void smoothS(float[][][] x, float[][][] y) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    int n1m = n1-1;
    int n2m = n2-1;
    int n3m = n3-1;
    float[][][] t = new float[3][n2][n1];
    scopy(x[0],t[0]);
    scopy(x[0],t[1]);
    for (int i3=0; i3<n3; ++i3) {
      int i3m = (i3>0)?i3-1:0;
      int i3p = (i3<n3m)?i3+1:n3m;
      int j3m = i3m%3;
      int j3  = i3%3;
      int j3p = i3p%3;
      scopy(x[i3p],t[j3p]);
      float[][] x3m = t[j3m];
      float[][] x3p = t[j3p];
      float[][] x30 = t[j3];
      float[][] y30 = y[i3];
      for (int i2=0; i2<n2; ++i2) {
        int i2m = (i2>0)?i2-1:0;
        int i2p = (i2<n2m)?i2+1:n2m;
        float[] x3m2m = x3m[i2m];
        float[] x3m20 = x3m[i2 ];
        float[] x3m2p = x3m[i2p];
        float[] x302m = x30[i2m];
        float[] x3020 = x30[i2 ];
        float[] x302p = x30[i2p];
        float[] x3p2m = x3p[i2m];
        float[] x3p20 = x3p[i2 ];
        float[] x3p2p = x3p[i2p];
        float[] y3020 = y30[i2 ];
        for (int i1=0; i1<n1; ++i1) {
          int i1m = (i1>0)?i1-1:0;
          int i1p = (i1<n1m)?i1+1:n1m;
          y3020[i1] = 0.125000f*(x3020[i1 ]) +
                      0.062500f*(x3020[i1m]+x3020[i1p]+
                                 x302m[i1 ]+x302p[i1 ]+
                                 x3m20[i1 ]+x3p20[i1 ]) +
                      0.031250f*(x3m20[i1m]+x3m20[i1p]+
                                 x3m2m[i1 ]+x3m2p[i1 ]+
                                 x302m[i1m]+x302m[i1p]+
                                 x302p[i1m]+x302p[i1p]+
                                 x3p20[i1m]+x3p20[i1p]+
                                 x3p2m[i1 ]+x3p2p[i1 ]) +
                      0.015625f*(x3m2m[i1m]+x3m2m[i1p]+
                                 x3m2p[i1m]+x3m2p[i1p]+
                                 x3p2m[i1m]+x3p2m[i1p]+
                                 x3p2p[i1m]+x3p2p[i1p]);
        }
      }
    }
  }

  // Conjugate-gradient solution of Ax = b, with no preconditioner.
  // Uses the initial values of x; does not assume they are zero.
  private void solve(Operator2 a, float[][] b, float[][] x) {
    int n1 = b[0].length;
    int n2 = b.length;
    float[][] d = new float[n2][n1];
    float[][] q = new float[n2][n1];
    float[][] r = new float[n2][n1];
    scopy(b,r);
    a.apply(x,q);
    saxpy(-1.0f,q,r); // r = b-Ax
    scopy(r,d); // d = r
    float delta = sdot(r,r); // delta = r'r
    float bnorm = sqrt(sdot(b,b));
    float rnorm = sqrt(delta);
    float rnormBegin = rnorm;
    float rnormSmall = bnorm*_small;
    int iter;
    log.fine("solve: bnorm="+bnorm+" rnorm="+rnorm);
    for (iter=0; iter<_niter && rnorm>rnormSmall; ++iter) {
      log.finer("  iter="+iter+" rnorm="+rnorm+" ratio="+rnorm/rnormBegin);
      a.apply(d,q); // q = Ad
      float dq = sdot(d,q); // d'q = d'Ad
      float alpha = delta/dq; // alpha = r'r/d'Ad
      saxpy( alpha,d,x); // x = x+alpha*d
      saxpy(-alpha,q,r); // r = r-alpha*q
      float deltaOld = delta;
      delta = sdot(r,r); // delta = r'r
      float beta = delta/deltaOld;
      sxpay(beta,r,d); // d = r+beta*d
      rnorm = sqrt(delta);
    }
    log.fine("  iter="+iter+" rnorm="+rnorm+" ratio="+rnorm/rnormBegin);
  }
  private void solve(Operator3 a, float[][][] b, float[][][] x) {
    int n1 = b[0][0].length;
    int n2 = b[0].length;
    int n3 = b.length;
    float[][][] d = new float[n3][n2][n1];
    float[][][] q = new float[n3][n2][n1];
    float[][][] r = new float[n3][n2][n1];
    scopy(b,r); a.apply(x,q); saxpy(-1.0f,q,r); // r = b-Ax
    scopy(r,d);
    float delta = sdot(r,r);
    float bnorm = sqrt(sdot(b,b));
    float rnorm = sqrt(delta);
    float rnormBegin = rnorm;
    float rnormSmall = bnorm*_small;
    int iter;
    log.fine("solve: bnorm="+bnorm+" rnorm="+rnorm);
    for (iter=0; iter<_niter && rnorm>rnormSmall; ++iter) {
      log.finer("  iter="+iter+" rnorm="+rnorm+" ratio="+rnorm/rnormBegin);
      a.apply(d,q);
      float dq = sdot(d,q);
      float alpha = delta/dq;
      saxpy( alpha,d,x);
      if (iter%100<99) {
        saxpy(-alpha,q,r);
      } else {
        scopy(b,r); a.apply(x,q); saxpy(-1.0f,q,r);
      }
      float deltaOld = delta;
      delta = sdot(r,r);
      float beta = delta/deltaOld;
      sxpay(beta,r,d);
      rnorm = sqrt(delta);
    }
    log.fine("  iter="+iter+" rnorm="+rnorm+" ratio="+rnorm/rnormBegin);
  }

  // Conjugate-gradient solution of Ax = b, with preconditioner M.
  // Uses the initial values of x; does not assume they are zero.
  private void solve(Operator2 a, Operator2 m, float[][] b, float[][] x) {
    int n1 = b[0].length;
    int n2 = b.length;
    float[][] d = new float[n2][n1];
    float[][] q = new float[n2][n1];
    float[][] r = new float[n2][n1];
    float[][] s = new float[n2][n1];
    scopy(b,r);
    a.apply(x,q);
    saxpy(-1.0f,q,r); // r = b-Ax
    float bnorm = sqrt(sdot(b,b));
    float rnorm = sqrt(sdot(r,r));
    float rnormBegin = rnorm;
    float rnormSmall = bnorm*_small;
    m.apply(r,s); // s = Mr
    scopy(s,d); // d = s
    float delta = sdot(r,s); // r's = r'Mr
    int iter;
    log.fine("msolve: bnorm="+bnorm+" rnorm="+rnorm);
    for (iter=0; iter<_niter && rnorm>rnormSmall; ++iter) {
      log.finer("  iter="+iter+" rnorm="+rnorm+" ratio="+rnorm/rnormBegin);
      a.apply(d,q); // q = Ad
      float alpha = delta/sdot(d,q); // alpha = r'Mr/d'Ad
      saxpy( alpha,d,x); // x = x+alpha*d
      saxpy(-alpha,q,r); // r = r-alpha*q
      m.apply(r,s); // s = Mr
      float deltaOld = delta;
      delta = sdot(r,s); // delta = r's = r'Mr
      float beta = delta/deltaOld;
      sxpay(beta,s,d); // d = s+beta*d
      rnorm  = sqrt(sdot(r,r));
    }
    log.fine("  iter="+iter+" rnorm="+rnorm+" ratio="+rnorm/rnormBegin);
  }
  private void solve(Operator3 a, Operator3 m, float[][][] b, float[][][] x) {
    int n1 = b[0][0].length;
    int n2 = b[0].length;
    int n3 = b.length;
    float[][][] d = new float[n3][n2][n1];
    float[][][] q = new float[n3][n2][n1];
    float[][][] r = new float[n3][n2][n1];
    float[][][] s = new float[n3][n2][n1];
    scopy(b,r); a.apply(x,q); saxpy(-1.0f,q,r); // r = b-Ax
    float bnorm = sqrt(sdot(b,b));
    float rnorm = sqrt(sdot(r,r));
    float rnormBegin = rnorm;
    float rnormSmall = bnorm*_small;
    m.apply(r,s); // s = Mr
    scopy(s,d); // d = s
    float delta = sdot(r,s); // r's = r'Mr
    int iter;
    log.fine("msolve: bnorm="+bnorm+" rnorm="+rnorm);
    for (iter=0; iter<_niter && rnorm>rnormSmall; ++iter) {
      log.finer("  iter="+iter+" rnorm="+rnorm+" ratio="+rnorm/rnormBegin);
      a.apply(d,q); // q = Ad
      float alpha = delta/sdot(d,q); // alpha = r'Mr/d'Ad
      saxpy( alpha,d,x); // x = x+alpha*d
      if (iter%100<99) {
        saxpy(-alpha,q,r); // r = r-alpha*q
      } else {
        scopy(b,r); a.apply(x,q); saxpy(-1.0f,q,r); // r = b-Ax
      }
      m.apply(r,s); // s = Mr
      float deltaOld = delta;
      delta = sdot(r,s); // delta = r's = r'Mr
      float beta = delta/deltaOld;
      sxpay(beta,s,d); // d = s+beta*d
      rnorm  = sqrt(sdot(r,r));
    }
    log.fine("  iter="+iter+" rnorm="+rnorm+" ratio="+rnorm/rnormBegin);
  }

  // Zeros array x.
  private static void szero(float[] x) {
    zero(x);
  }
  private static void szero(float[][] x) {
    zero(x);
  }
  private static void szero(final float[][][] x) {
    final int n3 = x.length;
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        szero(x[i3]);
      }
    });
  }

  // Copys array x to array y.
  private static void scopy(float[] x, float[] y) {
    copy(x,y);
  }
  private static void scopy(float[][] x, float[][] y) {
    copy(x,y);
  }
  private static void scopy(final float[][][] x, final float[][][] y) {
    final int n3 = x.length;
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        scopy(x[i3],y[i3]);
      }
    });
  }

  // Returns the dot product x'y.
  private static float sdot(float[][] x, float[][] y) {
    int n1 = x[0].length;
    int n2 = x.length;
    float d = 0.0f;
    for (int i2=0; i2<n2; ++i2) {
      float[] x2 = x[i2], y2 = y[i2];
      for (int i1=0; i1<n1; ++i1) {
        d += x2[i1]*y2[i1];
      }
    }
    return d;
  }
  private static float sdot(final float[][][] x, final float[][][] y) {
    final int n3 = x.length;
    final float[] d3 = new float[n3];
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        d3[i3] = sdot(x[i3],y[i3]);
      }
    });
    float d = 0.0f;
    for (int i3=0; i3<n3; ++i3)
      d += d3[i3];
    return d;
  }

  // Computes y = y + a*x.
  private static void saxpy(float a, float[][] x, float[][] y) {
    int n1 = x[0].length;
    int n2 = x.length;
    for (int i2=0; i2<n2; ++i2) {
      float[] x2 = x[i2], y2 = y[i2];
      for (int i1=0; i1<n1; ++i1) {
        y2[i1] += a*x2[i1];
      }
    }
  }
  private static void saxpy(
    final float a, final float[][][] x, final float[][][] y)
  {
    final int n3 = x.length;
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        saxpy(a,x[i3],y[i3]);
      }
    });
  }

  // Computes y = x + a*y.
  private static void sxpay(float a, float[][] x, float[][] y) {
    int n1 = x[0].length;
    int n2 = x.length;
    for (int i2=0; i2<n2; ++i2) {
      float[] x2 = x[i2], y2 = y[i2];
      for (int i1=0; i1<n1; ++i1) {
        y2[i1] = a*y2[i1]+x2[i1];
      }
    }
  }
  private static void sxpay(
    final float a, final float[][][] x, final float[][][] y)
  {
    final int n3 = x.length;
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        sxpay(a,x[i3],y[i3]);
      }
    });
  }

  // Computes z = x*y.
  private static void sxy(float[][] x, float[][] y, float[][] z) {
    int n1 = x[0].length;
    int n2 = x.length;
    for (int i2=0; i2<n2; ++i2) {
      float[] x2 = x[i2], y2 = y[i2], z2 = z[i2];
      for (int i1=0; i1<n1; ++i1) {
        z2[i1] = x2[i1]*y2[i1];
      }
    }
  }
  private static void sxy(
    final float[][][] x, final float[][][] y, final float[][][] z) 
  {
    final int n3 = x.length;
    Parallel.loop(n3,new Parallel.LoopInt() {
      public void compute(int i3) {
        sxy(x[i3],y[i3],z[i3]);
      }
    });
  }
}
