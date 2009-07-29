/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import java.util.logging.Logger;
import java.util.concurrent.atomic.AtomicInteger;

import edu.mines.jtk.util.AtomicFloat;
import edu.mines.jtk.util.Threads;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Local smoothing of images with tensor filter coefficients.
 * Smoothing is performed by solving a sparse symmetric positive-definite
 * system of equations: (S'S+G'DG)y = S'Sx, where G is a matrix of gradient 
 * operators, S is a matrix of smoothing operators, D is a matrix of tensor 
 * filter coefficients, x is an input image, and y is an output image.
 * <p>
 * The smoothing operators S compensate for deficiencies in the gradient
 * operators G. Finite-difference approximations in G break down for high 
 * wavenumbers near the Nyquist limit, and the smoothing operator S 
 * attenuates those high wavenumbers. Because S'S appears on both left
 * and right sides of the filter equations, a local smoothing filter does 
 * nothing for tensor coefficients D = 0.
 * <p>
 * The sparse system of filter equations is solved iteratively, beginning
 * with y = x. Iterations continue until either the error in the solution 
 * y is below a specified threshold or the number of iterations exceeds a 
 * specified limit.
 * <p>
 * For low wavenumbers the output of this filter approximates the solution 
 * to an anisotropic inhomogeneous diffusion equation, where the filter 
 * input x corresponds to the initial condition at time t = 0 and filter 
 * output y corresponds to the solution at some later time t.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.11.16
 */
public class LocalSmoothingFilter {

  /**
   * Constructs a local smoothing filter with default parameters.
   * The default parameter small is 0.01 and the default maximum 
   * number of iterations is 100.
   */
  public LocalSmoothingFilter() {
    this(0.01,100);
  }

  /**
   * Constructs a local smoothing filter with specified iteration parameters.
   * @param small stop when norm of residuals is less than this factor times
   *  the norm of the input array.
   * @param niter stop when number of iterations exceeds this limit.
   */
  public LocalSmoothingFilter(double small, int niter) {
    _small = (float)small;
    _niter = niter;
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
   * Applies this filter for specified tensor coefficients.
   * @param d tensor coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void apply(Tensors2 d, float[][] x, float[][] y) 
  {
    apply(d,1.0f,x,y);
  }

  /**
   * Applies this filter for specified tensor coefficients and scale factor.
   * @param d tensor coefficients.
   * @param c constant scale factor for tensor coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void apply(Tensors2 d, float c, float[][] x, float[][] y) {
    apply(d,c,null,x,y);
  }

  /**
   * Applies this filter for specified tensor coefficients and scale factors.
   * @param d tensor coefficients.
   * @param c constant scale factor for tensor coefficients.
   * @param s array of scale factors for tensor coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void apply(
    Tensors2 d, float c, float[][] s, float[][] x, float[][] y) 
  {
    Operator2 a = new LhsOperator2(d,c,s);
    float[][] r = applyRhs(x);
    solve(a,r,y);
  }

  /**
   * Applies this filter for specified tensor coefficients.
   * @param d tensor coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void apply(Tensors3 d, float[][][] x, float[][][] y) 
  {
    apply(d,1.0f,x,y);
  }

  /**
   * Applies this filter for specified tensor coefficients and scale factor.
   * @param d tensor coefficients.
   * @param c constant scale factor for tensor coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void apply(Tensors3 d, float c, float[][][] x, float[][][] y) {
    apply(d,c,null,x,y);
  }

  /**
   * Applies this filter for specified tensor coefficients and scale factors.
   * @param d tensor coefficients.
   * @param c constant scale factor for tensor coefficients.
   * @param s array of scale factors for tensor coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void apply(
    Tensors3 d, float c, float[][][] s, float[][][] x, float[][][] y) 
  {
    Operator3 a = new LhsOperator3(d,c,s);
    float[][][] r = applyRhs(x);
    solve(a,r,y);
  }
  private void testSpd(Tensors3 d, float c, float[][][] s) {
    int n1 = s[0][0].length;
    int n2 = s[0].length;
    int n3 = s.length;
    Operator3 a = new LhsOperator3(d,c,s);
    for (int itest=0; itest<100; ++itest) {
      float[][][] x = sub(randfloat(n1,n2,n3),0.5f);
      float[][][] y = zerofloat(n1,n2,n3);
      a.apply(x,y);
      float xy = sdot(x,y);
      System.out.println("itest="+itest+" xy="+xy);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static Logger log = 
    Logger.getLogger(LocalSmoothingFilter.class.getName());

  private static final boolean PARALLEL = true; // false for single-threaded
  private static final boolean SMOOTH = true; // false for I instead of S'S

  private float _small; // stop iterations when residuals are small
  private int _niter; // number of iterations

  /**
   * A symmetric positive-definite operator.
   */
  private static interface Operator2 {
    public void apply(float[][] x, float[][] y);
  }
  private static interface Operator3 {
    public void apply(float[][][] x, float[][][] y);
  }

  private static class LhsOperator2 implements Operator2 {
    LhsOperator2(Tensors2 d, float c, float[][] s) {
      _d = d;
      _c = c;
      _s = s;
    }
    public void apply(float[][] x, float[][] y) {
      applyLhs(_d,_c,_s,x,y);
    }
    private Tensors2 _d;
    private float _c;
    private float[][] _s;
  }

  private static class LhsOperator3 implements Operator3 {
    LhsOperator3(Tensors3 d, float c, float[][][] s) {
      _d = d;
      _c = c;
      _s = s;
    }
    public void apply(float[][][] x, float[][][] y) {
      applyLhs(_d,_c,_s,x,y);
    }
    private Tensors3 _d;
    private float _c;
    private float[][][] _s;
  }

  /**
   * Returns y = S'Sx.
   */
  private static float[][] applyRhs(float[][] x) {
    if (!SMOOTH)
      return x;
    int n1 = x[0].length;
    int n2 = x.length;
    float[][] y = new float[n2][n1];
    for (int i2=1; i2<n2; ++i2) {
      for (int i1=1; i1<n1; ++i1) {
        float x00 = x[i2  ][i1  ];
        float x01 = x[i2  ][i1-1];
        float x10 = x[i2-1][i1  ];
        float x11 = x[i2-1][i1-1];
        //         0.0625 = 1/16
        float xs = 0.0625f*(x00+x01+x10+x11);
        y[i2  ][i1  ] += xs;
        y[i2  ][i1-1] += xs;
        y[i2-1][i1  ] += xs;
        y[i2-1][i1-1] += xs;
      }
    }
    return y;
  }

  /**
   * Returns y = S'Sx.
   */
  private static float[][][] applyRhs(float[][][] x) {
    if (!SMOOTH)
      return x;
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    float[][][] y = new float[n3][n2][n1];
    for (int i3=1; i3<n3; ++i3) {
      for (int i2=1; i2<n2; ++i2) {
        float[] x00 = x[i3  ][i2  ];
        float[] x01 = x[i3  ][i2-1];
        float[] x10 = x[i3-1][i2  ];
        float[] x11 = x[i3-1][i2-1];
        float[] y00 = y[i3  ][i2  ];
        float[] y01 = y[i3  ][i2-1];
        float[] y10 = y[i3-1][i2  ];
        float[] y11 = y[i3-1][i2-1];
        for (int i1=1; i1<n1; ++i1) {
          int i1m = i1-1;
          float x000 = x00[i1 ];
          float x001 = x00[i1m];
          float x010 = x01[i1 ];
          float x011 = x01[i1m];
          float x100 = x10[i1 ];
          float x101 = x10[i1m];
          float x110 = x11[i1 ];
          float x111 = x11[i1m];
          //         0.015625 = 1/64
          float xs = 0.015625f*(x000+x001+x010+x011+x100+x101+x110+x111);
          y00[i1 ] += xs;
          y00[i1m] += xs;
          y01[i1 ] += xs;
          y01[i1m] += xs;
          y10[i1 ] += xs;
          y10[i1m] += xs;
          y11[i1 ] += xs;
          y11[i1m] += xs;
        }
      }
    }
    return y;
  }

  /**
   * Computes y = (S'S+G'DG)x. Arrays x and y must be distinct.
   */
  private static void applyLhs(
    Tensors2 d, float c, float[][] s, float[][] x, float[][] y) 
  {
    int n1 = x[0].length;
    int n2 = x.length;
    if (SMOOTH) {
      szero(y);
    } else {
      scopy(x,y);
    }
    float[] di = new float[3];
    for (int i2=1; i2<n2; ++i2) {
      for (int i1=1; i1<n1; ++i1) {
        d.getTensor(i1,i2,di);
        float csi = (s!=null)?c*s[i2][i1]:c;
        float d11 = di[0]*csi;
        float d12 = di[1]*csi;
        float d22 = di[2]*csi;
        float x00 = x[i2  ][i1  ];
        float x01 = x[i2  ][i1-1];
        float x10 = x[i2-1][i1  ];
        float x11 = x[i2-1][i1-1];
        float xa = x00-x11;
        float xb = x01-x10;
        float x1 = 0.25f*(xa-xb);
        float x2 = 0.25f*(xa+xb);
        float y1 = d11*x1+d12*x2;
        float y2 = d12*x1+d22*x2;
        float ya = y1+y2;
        float yb = y1-y2;
        if (SMOOTH) { // 0.0625 = 1/16
          float xs = 0.0625f*(x00+x01+x10+x11);
          y[i2  ][i1  ] += ya+xs;
          y[i2  ][i1-1] -= yb-xs;
          y[i2-1][i1  ] += yb+xs;
          y[i2-1][i1-1] -= ya-xs;
        } else {
          y[i2  ][i1  ] += ya;
          y[i2  ][i1-1] -= yb;
          y[i2-1][i1  ] += yb;
          y[i2-1][i1-1] -= ya;
        }
      }
    }
  }

  private static void applyLhs(
    Tensors3 d, float c, float[][][] s, float[][][] x, float[][][] y) 
  {
    if (SMOOTH) {
      szero(y);
    } else {
      scopy(x,y);
    }
    if (PARALLEL) {
      applyLhsParallel(d,c,s,x,y);
    } else {
      applyLhsSerial(d,c,s,x,y);
    }
  }

  private static void applyLhsSerial(
    Tensors3 d, float c, float[][][] s, float[][][] x, float[][][] y) 
  {
    int n3 = x.length;
    for (int i3=1; i3<n3; ++i3)
      applyLhsSlice3(i3,d,c,s,x,y);
  }

  private static void applyLhsParallel(
    final Tensors3 d, final float c, final float[][][] s, 
    final float[][][] x, final float[][][] y) 
  {
    final int n3 = x.length;

    // i3 = 1, 3, 5, ...
    final AtomicInteger a1 = new AtomicInteger(1);
    Thread[] thread1 = Threads.makeArray();
    for (int ithread=0; ithread<thread1.length; ++ithread) {
      thread1[ithread] = new Thread(new Runnable() {
        public void run() {
          for (int i3=a1.getAndAdd(2); i3<n3; i3=a1.getAndAdd(2))
            applyLhsSlice3(i3,d,c,s,x,y);
        }
      });
    }
    Threads.startAndJoin(thread1);

    // i3 = 2, 4, 6, ...
    final AtomicInteger a2 = new AtomicInteger(2);
    Thread[] thread2 = Threads.makeArray();
    for (int ithread=0; ithread<thread2.length; ++ithread) {
      thread2[ithread] = new Thread(new Runnable() {
        public void run() {
          for (int i3=a2.getAndAdd(2); i3<n3; i3=a2.getAndAdd(2))
            applyLhsSlice3(i3,d,c,s,x,y);
        }
      });
    }
    Threads.startAndJoin(thread2);
  }


  /**
   * Computes y = (S'S+D'TD)x for one constant-i3 slice.
   */
  private static void applyLhsSlice3(
    int i3, Tensors3 d, float c, float[][][] s, float[][][] x, float[][][] y) 
  {
    float[] di = new float[6];
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    for (int i2=1; i2<n2; ++i2) {
      float[] x00 = x[i3  ][i2  ];
      float[] x01 = x[i3  ][i2-1];
      float[] x10 = x[i3-1][i2  ];
      float[] x11 = x[i3-1][i2-1];
      float[] y00 = y[i3  ][i2  ];
      float[] y01 = y[i3  ][i2-1];
      float[] y10 = y[i3-1][i2  ];
      float[] y11 = y[i3-1][i2-1];
      for (int i1=1,i1m=0; i1<n1; ++i1,++i1m) {
        d.getTensor(i1,i2,i3,di);
        float csi = (s!=null)?c*s[i3][i2][i1]:c;
        float d11 = di[0]*csi;
        float d12 = di[1]*csi;
        float d13 = di[2]*csi;
        float d22 = di[3]*csi;
        float d23 = di[4]*csi;
        float d33 = di[5]*csi;
        applyLhs(i1,d11,d12,d13,d22,d23,d33,x00,x01,x10,x11,y00,y01,y10,y11);
      }
    }
  }

  /**
   * Computes y = (S'S+D'TD)x for one sample.
   */
  private static void applyLhs(int i1,
   float d11, float d12, float d13, float d22, float d23, float d33,
   float[] x00, float[] x01, float[] x10, float[] x11,
   float[] y00, float[] y01, float[] y10, float[] y11)
  {
    int i1m = i1-1;
    float x000 = x00[i1 ];
    float x001 = x00[i1m];
    float x010 = x01[i1 ];
    float x011 = x01[i1m];
    float x100 = x10[i1 ];
    float x101 = x10[i1m];
    float x110 = x11[i1 ];
    float x111 = x11[i1m];
    //float x1 = 0.0625f*(x000+x010+x100+x110-x001-x011-x101-x111);
    //float x2 = 0.0625f*(x000+x001+x100+x101-x010-x011-x110-x111);
    //float x3 = 0.0625f*(x000+x001+x010+x011-x100-x101-x110-x111);
    float xa = x000-x111;
    float xb = x001-x110;
    float xc = x010-x101;
    float xd = x100-x011;
    float x1 = 0.0625f*(xa-xb+xc+xd);
    float x2 = 0.0625f*(xa+xb-xc+xd);
    float x3 = 0.0625f*(xa+xb+xc-xd);
    float y1 = d11*x1+d12*x2+d13*x3;
    float y2 = d12*x1+d22*x2+d23*x3;
    float y3 = d13*x1+d23*x2+d33*x3;
    float ya = y1+y2+y3;
    float yb = y1-y2+y3;
    float yc = y1+y2-y3;
    float yd = y1-y2-y3;
    if (SMOOTH) {
      float xs = 0.015625f*(x000+x001+x010+x011+x100+x101+x110+x111);
      y00[i1 ] += ya+xs;
      y00[i1m] -= yd-xs;
      y01[i1 ] += yb+xs;
      y01[i1m] -= yc-xs;
      y10[i1 ] += yc+xs;
      y10[i1m] -= yb-xs;
      y11[i1 ] += yd+xs;
      y11[i1m] -= ya-xs;
    } else {
      y00[i1 ] += ya;
      y00[i1m] -= yd;
      y01[i1 ] += yb;
      y01[i1m] -= yc;
      y10[i1 ] += yc;
      y10[i1m] -= yb;
      y11[i1 ] += yd;
      y11[i1m] -= ya;
    }
  }

  /**
   * Solves Ax = b via conjugate gradient iterations. (No preconditioner.)
   * Uses the initial values of x; does not assume they are zero.
   */
  private void solve(Operator2 a, float[][] b, float[][] x) {
    int n1 = b[0].length;
    int n2 = b.length;
    float[][] d = new float[n2][n1];
    float[][] q = new float[n2][n1];
    float[][] r = new float[n2][n1];
    scopy(b,r);
    a.apply(x,q);
    saxpy(-1.0f,q,r); // r = b-Ax
    scopy(r,d);
    float delta = sdot(r,r);
    float deltaBegin = delta;
    float deltaSmall = sdot(b,b)*_small*_small;
    log.fine("solve: delta="+delta);
    int iter;
    for (iter=0; iter<_niter && delta>deltaSmall; ++iter) {
      log.finer("  iter="+iter+" delta="+delta+" ratio="+delta/deltaBegin);
      a.apply(d,q);
      float dq = sdot(d,q);
      float alpha = delta/dq;
      saxpy( alpha,d,x);
      saxpy(-alpha,q,r);
      float deltaOld = delta;
      delta = sdot(r,r);
      float beta = delta/deltaOld;
      sxpay(beta,r,d);
    }
    log.fine("  iter="+iter+" delta="+delta+" ratio="+delta/deltaBegin);
  }
  private void solve(Operator3 a, float[][][] b, float[][][] x) {
    int n1 = b[0][0].length;
    int n2 = b[0].length;
    int n3 = b.length;
    float[][][] d = new float[n3][n2][n1];
    float[][][] q = new float[n3][n2][n1];
    float[][][] r = new float[n3][n2][n1];
    scopy(b,r);
    a.apply(x,q);
    saxpy(-1.0f,q,r); // r = b-Ax
    scopy(r,d);
    float delta = sdot(r,r);
    float deltaBegin = delta;
    float deltaSmall = sdot(b,b)*_small*_small;
    log.fine("solve: delta="+delta);
    int iter;
    for (iter=0; iter<_niter && delta>deltaSmall; ++iter) {
      log.finer("  iter="+iter+" delta="+delta+" ratio="+delta/deltaBegin);
      a.apply(d,q);
      float dq = sdot(d,q);
      float alpha = delta/dq;
      saxpy( alpha,d,x);
      saxpy(-alpha,q,r);
      float deltaOld = delta;
      delta = sdot(r,r);
      float beta = delta/deltaOld;
      sxpay(beta,r,d);
    }
    log.fine("  iter="+iter+" delta="+delta+" ratio="+delta/deltaBegin);
  }

  // Zeros array x.
  private static void szero(float[][] x) {
    zero(x);
  }
  private static void szero(float[][][] x) {
    if (PARALLEL) {
      szeroP(x);
    } else {
      szeroS(x);
    }
  }
  private static void szeroS(float[][][] x) {
    int n3 = x.length;
    for (int i3=0; i3<n3; ++i3)
      szero(x[i3]);
  }
  private static void szeroP(final float[][][] x) {
    final int n3 = x.length;
    final AtomicInteger a3 = new AtomicInteger(0);
    Thread[] threads = Threads.makeArray();
    for (int ithread=0; ithread<threads.length; ++ithread) {
      threads[ithread] = new Thread(new Runnable() {
        public void run() {
          for (int i3=a3.getAndIncrement(); i3<n3; i3=a3.getAndIncrement())
            szero(x[i3]);
        }
      });
    }
    Threads.startAndJoin(threads);
  }

  // Copys array x to array y.
  private static void scopy(float[][] x, float[][] y) {
    copy(x,y);
  }
  private static void scopy(float[][][] x, float[][][] y) {
    if (PARALLEL) {
      scopyP(x,y);
    } else {
      scopyS(x,y);
    }
  }
  private static void scopyS(float[][][] x, float[][][] y) {
    int n3 = x.length;
    for (int i3=0; i3<n3; ++i3)
      scopy(x[i3],y[i3]);
  }
  private static void scopyP(final float[][][] x, final float[][][] y) {
    final int n3 = x.length;
    final AtomicInteger a3 = new AtomicInteger(0);
    Thread[] threads = Threads.makeArray();
    for (int ithread=0; ithread<threads.length; ++ithread) {
      threads[ithread] = new Thread(new Runnable() {
        public void run() {
          for (int i3=a3.getAndIncrement(); i3<n3; i3=a3.getAndIncrement())
            scopy(x[i3],y[i3]);
        }
      });
    }
    Threads.startAndJoin(threads);
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
  private static float sdot(float[][][] x, float[][][] y) {
    if (PARALLEL) {
      return sdotP(x,y);
    } else {
      return sdotS(x,y);
    }
  }
  private static float sdotS(float[][][] x, float[][][] y) {
    int n3 = x.length;
    float d = 0.0f;
    for (int i3=0; i3<n3; ++i3)
      d += sdot(x[i3],y[i3]);
    return d;
  }
  private static float sdotP(final float[][][] x, final float[][][] y) {
    final int n3 = x.length;
    final AtomicFloat ad = new AtomicFloat(0.0f);
    final AtomicInteger a3 = new AtomicInteger(0);
    Thread[] threads = Threads.makeArray();
    for (int ithread=0; ithread<threads.length; ++ithread) {
      threads[ithread] = new Thread(new Runnable() {
        public void run() {
          float d = 0.0f;
          for (int i3=a3.getAndIncrement(); i3<n3; i3=a3.getAndIncrement())
            d += sdot(x[i3],y[i3]);
          ad.getAndAdd(d);
        }
      });
    }
    Threads.startAndJoin(threads);
    return ad.get();
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
  private static void saxpy(float a, float[][][] x, float[][][] y) {
    if (PARALLEL) {
      saxpyP(a,x,y);
    } else {
      saxpyS(a,x,y);
    }
  }
  private static void saxpyS(float a, float[][][] x, float[][][] y) {
    int n3 = x.length;
    for (int i3=0; i3<n3; ++i3)
      saxpy(a,x[i3],y[i3]);
  }
  private static void saxpyP(
    final float a, final float[][][] x, final float[][][] y)
  {
    final int n3 = x.length;
    final AtomicInteger a3 = new AtomicInteger(0);
    Thread[] threads = Threads.makeArray();
    for (int ithread=0; ithread<threads.length; ++ithread) {
      threads[ithread] = new Thread(new Runnable() {
        public void run() {
          for (int i3=a3.getAndIncrement(); i3<n3; i3=a3.getAndIncrement())
            saxpy(a,x[i3],y[i3]);
        }
      });
    }
    Threads.startAndJoin(threads);
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
  private static void sxpay(float a, float[][][] x, float[][][] y) {
    if (PARALLEL) {
      sxpayP(a,x,y);
    } else {
      sxpayS(a,x,y);
    }
  }
  private static void sxpayS(float a, float[][][] x, float[][][] y) {
    int n3 = x.length;
    for (int i3=0; i3<n3; ++i3)
      sxpay(a,x[i3],y[i3]);
  }
  private static void sxpayP(
    final float a, final float[][][] x, final float[][][] y)
  {
    final int n3 = x.length;
    final AtomicInteger a3 = new AtomicInteger(0);
    Thread[] threads = Threads.makeArray();
    for (int ithread=0; ithread<threads.length; ++ithread) {
      threads[ithread] = new Thread(new Runnable() {
        public void run() {
          for (int i3=a3.getAndIncrement(); i3<n3; i3=a3.getAndIncrement())
            sxpay(a,x[i3],y[i3]);
        }
      });
    }
    Threads.startAndJoin(threads);
  }

  private static final boolean TRACE = true;
  private static void trace(String s) {
    if (TRACE)
      System.out.println(s);
  }
} 
