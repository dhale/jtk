/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import java.util.ArrayList;
import java.util.logging.Logger;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tensor-guided 2D gridding with bi-harmonic and harmonic splines.
 * At locations where gridded values are not constrained by specified
 * (known) sample values, the gridded values q satisfy the equation
 * (G'DGG'DG+tG'DG)q = 0, where G is a finite-difference approximation 
 * of the gradient operator, G' is its transpose, D is a tensor field, 
 * and t is a scalar constant that controls the tension, the weight of 
 * the harmonic G'DG operator relative to the bi-harmonic G'DGG'DG 
 * operator.
 * <p>
 * In the isotropic and homogeneous case, where D = I is the identity
 * matrix, this gridder is an implementation of Smith and Wessel's
 * (1990) gridding with continuous curvature splines in tension. The
 * finite-difference approximations to derivatives in G are different, 
 * however, because of the tensors D between G' and G in G'DG. These 
 * tensors D must be symmetric and positive-semidefinite (SPSD) so that 
 * the finite-difference operators G'DG and G'DGG'DG are SPSD as well.
 * <p>
 * Another difference between this implementation and that of Smith and 
 * Wessel (1990) is that a multigrid method is not used here to find the
 * gridded values q. Multigrid methods are ineffective for tensors fields 
 * D that are anisotropic and inhomogeneous. Instead, this implementation 
 * uses conjugate-gradient (CG) iterations.
 * <p>
 * The gridded values q must be obtained by solving iteratively the large 
 * sparse system of equations (G'DGG'DG+tG'DG)q = 0. To facilitate a CG
 * solver, these equations are rewritten as (K+MAM)q = (K-MAK)q, where 
 * A = G'DGG'DG+tG'DG, M is a diagonal matrix operator with ones where 
 * sample values are missing and zeros where values are known, and 
 * K = I-M is a diagonal matrix with ones where sample values are known 
 * and zero where they are missing. The matrix K+MAM on the left-hand side 
 * is symmetric and positive-definite (SPD), so that CG iterations can be 
 * used to solve for the unknown values in q. Only known values in q appear 
 * in the right-hand side column vector (K-MAK)q.
 * <p>
 * See Smith, W.H.F., and P. Wessel, 1990, Gridding with continuous
 * curvature splines in tension: Geophysics, 55, 293--305.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.01.19
 */
public class SplinesGridder2 implements Gridder2 {

  /**
   * Constructs a gridder for default tensors.
   */
  public SplinesGridder2() {
    this(null);
  }

  /**
   * Constructs a gridder for default tensors and specified samples.
   * The specified arrays are referenced; not copied.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public SplinesGridder2(float[] f, float[] x1, float[] x2) {
    this(null);
    setScattered(f,x1,x2);
  }

  /**
   * Constructs a gridder for the specified tensors.
   * @param tensors the tensors.
   */
  public SplinesGridder2(Tensors2 tensors) {
    setTensors(tensors);
  }

  /**
   * Constructs a gridder for the specified tensors and samples.
   * The specified arrays are referenced; not copied.
   * @param tensors the tensors.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public SplinesGridder2(
    Tensors2 tensors,
    float[] f, float[] x1, float[] x2) 
  {
    setTensors(tensors);
    setScattered(f,x1,x2);
  }

  /**
   * Sets the tensor field used by this gridder.
   * The default is a homogeneous and isotropic tensor field.
   * @param tensors the tensors; null for default tensors.
   */
  public void setTensors(Tensors2 tensors) {
    _tensors = tensors;
    if (_tensors==null) {
      _tensors = new Tensors2() {
        public void getTensor(int i1, int i2, float[] d) {
          d[0] = 1.0f;
          d[1] = 0.0f;
          d[2] = 1.0f;
        }
      };
    }
  }

  /**
   * Sets the tension, the weight for the harmonic spline.
   * The default tension is 0.0, for a purely bi-harmonic spline.
   * <p>
   * This tension parameter is not equivalent to the parameter t in
   * the class documentation above. The latter would be infinite if 
   * the tension specified here could be set to 1.0.
   * @param tension the tension; must be in the range [0:1).
   */
  public void setTension(double tension) {
    Check.argument(0<=tension,"0<=tension");
    Check.argument(tension<1,"tension<1");
    _tension = (float)tension;
  }

  /**
   * Sets the maximum number of conjugate-gradient iterations.
   * The default maximum number of iterations is 10,000.
   * @param niter the maximum number of iterations.
   */
  public void setMaxIterations(int niter) {
    _niter = niter;
  }

  /**
   * Returns the number of conjugate-gradient iterations required.
   * The number returned corresponds to the last use of this gridder.
   * @return the number of iterations.
   */
  public int getIterationCount() {
    return _residuals.size()-1;
  }

  /**
   * Gets the initial residual and one residual for each iteration.
   * The residuals returned correspond to the last use of this gridder.
   * <p>
   * Residuals are normalized root-mean-square differences between
   * the left and right sides of the system of equations that are 
   * solved iteratively when computing gridded values. The returned 
   * residuals are normalized, so that the zeroth residual (before any 
   * conjugate-gradient iterations are performed) is one.
   * @return array of residuals.
   */
  public float[] getResiduals() {
    int n = _residuals.size();
    float[] r = new float[n];
    for (int i=0; i<n; ++i)
      r[i] = _residuals.get(i);
    return r;
  }

  /**
   * Computes gridded values that are missing in the specified array.
   * Missing values are those equal to the specified null value.
   * @param qnull the null value representing missing samples.
   * @param q array in which missing (null) values are to be replaced. 
   */
  public void gridMissing(float qnull, float[][] q) {
    int n1 = q[0].length;
    int n2 = q.length;
    boolean[][] m = new boolean[n2][n1];
    for (int i2=0; i2<n2; ++i2)
      for (int i1=0; i1<n1; ++i1)
        m[i2][i1] = q[i2][i1]==qnull;
    gridMissing(m,q);
  }

  /**
   * Computes gridded values that are missing in the specified array.
   * Missing values are those with missing-value flags set to true.
   * @param m array of missing-value flags; true where value is missing.
   * @param q array in which flagged missing values are to be replaced.
   */
  public void gridMissing(boolean[][] m, float[][] q) {
    int n1 = m[0].length;
    int n2 = m.length;
    float s = 0.02f*(n1-1+n2-1);
    float t = _tension/(1.0f-_tension)/(s*s);
    LaplaceOperator2 lop = new LaplaceOperator2(_ldk,_tensors,t,m);
    SmoothOperator2 sop = new SmoothOperator2();
    float[][] b = new float[n2][n1];
    lop.applyRhs(q,b);
    solve(lop,sop,b,q);
  }

  ///////////////////////////////////////////////////////////////////////////
  // interface Gridder2

  public void setScattered(float[] f, float[] x1, float[] x2) {
    _f = f;
    _x1 = x1;
    _x2 = x2;
  }

  public float[][] grid(Sampling s1, Sampling s2) {
    Check.argument(s1.isUniform(),"s1 is uniform");
    Check.argument(s2.isUniform(),"s2 is uniform");
    Check.state(_f!=null,"scattered samples have been set");
    Check.state(_x1!=null,"scattered samples have been set"); 
    Check.state(_x2!=null,"scattered samples have been set");
    PolyTrend2 pt = new PolyTrend2(1,_f,_x1,_x2);
    pt.detrend();
    SimpleGridder2 sg = new SimpleGridder2(_f,_x1,_x2);
    sg.setNullValue(QNULL);
    float[][] q = sg.grid(s1,s2);
    gridMissing(QNULL,q);
    pt.restore(q,s1,s2);
    pt.restore();
    return q;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final float QNULL = FLT_MIN*1.314159f; // tiny null value
  private Tensors2 _tensors;
  private float _tension = 0.0f;
  private float[] _f,_x1,_x2;
  private float _small = 0.0001f;
  private int _niter = 10000;
  private ArrayList<Float> _residuals = new ArrayList<Float>();
  private LocalDiffusionKernel _ldk = 
    new LocalDiffusionKernel(LocalDiffusionKernel.Stencil.D22);

  private static Logger log = 
    Logger.getLogger(SplinesGridder2.class.getName());

  private static interface Operator2 {
    public void apply(float[][] x, float[][] y);
  }

  // Smoothing operator SS used for preconditioning. This operator
  // attenuates frequencies near the Nyquist limit for which 
  // finite-difference approximations in G'DG are poor.
  private static class SmoothOperator2 implements Operator2 {
    public void apply(float[][] x, float[][] y) {
      smoothS(x,y);
      smoothS(y,y);
    }
  }

  // The left-hand-side operator K + M(G'DGG'DG + tG'DG)M. 
  // Can also apply the right-hand-side operator K - M(...)K.
  private static class LaplaceOperator2 implements Operator2 {
    LaplaceOperator2(
      LocalDiffusionKernel ldk, Tensors2 d, float t, boolean[][] m) 
    {
      _ldk = ldk;
      _d = d;
      _t = t;
      _m = m;
      _z = new float[m.length][m[0].length];
    }
    public void apply(float[][] x, float[][] y) {
      int n1 = x[0].length;
      int n2 = x.length;
      // z = Mx
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          _z[i2][i1] = _m[i2][i1]?x[i2][i1]:0.0f;
      // y = G'DGz
      szero(y);
      _ldk.apply(_d,_z,y);
      // z = (G'DG+tI)y
      mul(_t,y,_z);
      _ldk.apply(_d,y,_z);
      // y = (I-M)x + Mz
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          y[i2][i1] = _m[i2][i1]?_z[i2][i1]:x[i2][i1];
    }
    public void applyRhs(float[][] x, float[][] y) {
      int n1 = x[0].length;
      int n2 = x.length;
      // z = (I-M)x
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          _z[i2][i1] = _m[i2][i1]?0.0f:x[i2][i1];
      // y = G'DGz
      szero(y);
      _ldk.apply(_d,_z,y);
      // z = (G'DG+tI)y
      mul(_t,y,_z);
      _ldk.apply(_d,y,_z);
      // y = (I-M)x - Mz
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          y[i2][i1] = _m[i2][i1]?-_z[i2][i1]:x[i2][i1];
    }
    private LocalDiffusionKernel _ldk;
    private Tensors2 _d;
    private float _t;
    private boolean[][] _m;
    private float[][] _z;
  }

  // This old operator S(K+MAM)S is currently not used. Its disadvantage
  // is that gridded values q must be obtained by smoothing the solution
  // p to S(K+MAM)Sp = S(K-MAK)q via q = Sp. It is therefore difficult
  // to test for convergence using the rough values in p. Using this
  // operator is also more complicated than using SS as a preconditioner 
  // with K+MAM.
  private static class XLaplaceOperator2 implements Operator2 {
    XLaplaceOperator2(
      LocalDiffusionKernel ldk, Tensors2 d, float t, boolean[][] m) 
    {
      _ldk = ldk;
      _d = d;
      _t = t;
      _m = m;
      _w = new float[m.length][m[0].length];
      _z = new float[m.length][m[0].length];
    }
    public void apply(float[][] x, float[][] y) {
      int n1 = x[0].length;
      int n2 = x.length;
      // w = Sx
      smoothS(x,_w);
      // z = Mw
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          _z[i2][i1] = _m[i2][i1]?_w[i2][i1]:0.0f;
      // y = G'DGz
      szero(y);
      _ldk.apply(_d,_z,y);
      // z = (G'DG+tI)y
      mul(_t,y,_z);
      _ldk.apply(_d,y,_z);
      // y = (I-M)w + Mz
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          y[i2][i1] = _m[i2][i1]?_z[i2][i1]:_w[i2][i1];
      // y = S'y
      smoothS(y,y);
    }
    public void applyRhs(float[][] x, float[][] y) {
      int n1 = x[0].length;
      int n2 = x.length;
      // z = (I-M)x
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          _z[i2][i1] = _m[i2][i1]?0.0f:x[i2][i1];
      // y = G'DGz
      szero(y);
      _ldk.apply(_d,_z,y);
      // z = (G'DG+tI)y
      mul(_t,y,_z);
      _ldk.apply(_d,y,_z);
      // y = (I-M)x-Mz
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          y[i2][i1] = _m[i2][i1]?-_z[i2][i1]:x[i2][i1];
      // y = S'y
      smoothS(y,y);
    }
    private LocalDiffusionKernel _ldk;
    private Tensors2 _d;
    private float _t;
    private boolean[][] _m;
    private float[][] _w;
    private float[][] _z;
  }

  // Conjugate-gradient solution of Ax = b, with preconditioner M.
  private void solve(Operator2 a, Operator2 m, float[][] b, float[][] x) {
    _residuals.clear();
    int n1 = b[0].length;
    int n2 = b.length;
    //float small = _small;
    //float small = _small*sqrt(1.0e6f/n1/n2);
    float small = _small*1.0e5f/n1/n2;
    float[][] d = new float[n2][n1];
    float[][] q = new float[n2][n1];
    float[][] r = new float[n2][n1];
    float[][] s = new float[n2][n1];
    szero(x); // begin with x = 0 to ensure x is always smooth
    scopy(b,r); // r = b (because Ax = 0)
    m.apply(r,s); // s = Mr
    scopy(s,d); // d = s
    float delta = sdot(r,s); // r's = r'Mr
    float rnorm = sqrt(delta);
    float rnormBegin = rnorm;
    float rnormSmall = rnorm*small;
    _residuals.add(1.0f);
    log.fine("solve: small="+small);
    int iter;
    for (iter=0; iter<_niter && rnorm>rnormSmall; ++iter) {
      log.finer("  iter="+iter+" rnorm="+(rnorm/rnormBegin));
      a.apply(d,q); // q = Ad
      float alpha = delta/sdot(d,q); // alpha = r'Mr/d'Ad
      saxpy( alpha,d,x); // x = x+alpha*d
      saxpy(-alpha,q,r); // r = r-alpha*q
      m.apply(r,s); // s = Mr
      float deltaOld = delta;
      delta = sdot(r,s); // delta = r's = r'Mr
      float beta = delta/deltaOld;
      sxpay(beta,s,d); // d = s+beta*d
      rnorm = sqrt(delta);
      _residuals.add(rnorm/rnormBegin);
    }
    log.fine("        iter="+iter+" rnorm="+(rnorm/rnormBegin));
  }
  private static void szero(float[][] x) {
    zero(x);
  }
  private static void scopy(float[][] x, float[][] y) {
    copy(x,y);
  }
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
  private static void smoothS(float[][] x, float[][] y) {
    int n1 = x[0].length;
    int n2 = x.length;
    int n1m = n1-1;
    int n2m = n2-1;
    float[][] t = new float[3][n1];
    copy(x[0],t[0]);
    copy(x[1],t[1]);
    for (int i2=0; i2<n2; ++i2) {
      int i2m = (i2>0)?i2-1:0;
      int i2p = (i2<n2m)?i2+1:n2m;
      int j2m = i2m%3;
      int j2  = i2%3;
      int j2p = i2p%3;
      copy(x[i2p],t[j2p]);
      float[] x2m = t[j2m];
      float[] x2p = t[j2p];
      float[] x20 = t[j2];
      float[] y20 = y[i2];
      for (int i1=0; i1<n1; ++i1) {
        int i1m = (i1>0)?i1-1:0;
        int i1p = (i1<n1m)?i1+1:n1m;
        y20[i1] = 0.2500f*(x20[i1 ]) +
                  0.1250f*(x20[i1m]+x20[i1p]+x2m[i1 ]+x2p[i1 ]) +
                  0.0625f*(x2m[i1m]+x2m[i1p]+x2p[i1m]+x2p[i1p]);
      }
    }
  }
  private static void xsmoothS(float[][] x, float[][] y) {
    if (x==y) x = copy(x);
    int n1 = x[0].length;
    int n2 = x.length;
    for (int i2=0; i2<n2; ++i2) {
      int i2m = i2-1; if (i2m<  0) i2m += 2;
      int i2p = i2+1; if (i2p>=n2) i2p -= 2;
      float[] x2m = x[i2m];
      float[] x2p = x[i2p];
      float[] x20 = x[i2];
      float[] y20 = y[i2];
      for (int i1=0; i1<n1; ++i1) {
        int i1m = i1-1; if (i1m<  0) i1m += 2;
        int i1p = i1+1; if (i1p>=n1) i1p -= 2;
        y20[i1] = 0.2500f*(x20[i1 ]) +
                  0.1250f*(x20[i1m]+x20[i1p]+x2m[i1 ]+x2p[i1 ]) +
                  0.0625f*(x2m[i1m]+x2m[i1p]+x2p[i1m]+x2p[i1p]);
      }
    }
  }
  private static void xxsmoothS(float[][] x, float[][] y) {
    if (x==y) 
      x = copy(x);
    zero(y);
    int n1 = x[0].length;
    int n2 = x.length;
    for (int i2=1,m2=0; i2<n2; ++i2,++m2) {
      for (int i1=1,m1=0; i1<n1; ++i1,++m1) {
        float xs = 0.0625f*(
          x[i2][i1] + 
          x[i2][m1] +
          x[m2][i1] +
          x[m2][m1]);
        y[i2][i1] += xs;
        y[i2][m1] += xs;
        y[m2][i1] += xs;
        y[m2][m1] += xs;
      }
    }
  }
  private static void testSmooth() {
    // y'(Sx) = x'(S'y) = x'(Sy)
    int n1 = 5;
    int n2 = 5;
    /*
    float[][] x = sub(randfloat(n1,n2),0.5f);
    float[][] y = sub(randfloat(n1,n2),0.5f);
    */
    float[][] x = zerofloat(n1,n2);
    float[][] y = zerofloat(n1,n2);
    x[0][0] = x[n2-1][0] = x[0][n1-1] = x[n2-1][n1-1] = 1.0f;
    y[0][0] = y[n2-1][0] = y[0][n1-1] = y[n2-1][n1-1] = 1.0f;
    //x[n2/2][n1/2] = 1.0f;
    //y[n2/2][n1/2] = 1.0f;
    float[][] sx = zerofloat(n1,n2);
    float[][] sy = zerofloat(n1,n2);
    smoothS(x,sx); smoothS(sx,sx);
    smoothS(y,sy); smoothS(sy,sy);
    dump(sx);
    dump(sy);
    float ysx = sum(mul(y,sx));
    float xsy = sum(mul(x,sy));
    System.out.println("ysx="+ysx+" xsy="+xsy);
  }
  private static void main(String[] args) {
    testSmooth();
  }
}
