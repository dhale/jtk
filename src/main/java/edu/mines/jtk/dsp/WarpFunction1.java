package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Synthetic warping functions for 1-D sequences.
 * The function u(x) describes the warping, in which a point x
 * is displaced to a point y(x) = x+u(x).
 * <p>
 * Warping is the computation of the sequence g(y) = f(x(y)).
 * Unwarping is the computation of the sequence f(x) = g(y(x)).
 * <p>
 * For warping, we need the function x(y) = y-u(x(y)) = y-uy(y). We 
 * compute the displacement uy(y) by iteration so that uy(y) = u(x(y)).
 * <p>
 * We also define a midpoint m(x) = (x+y(x))/2, and compute the 
 * displacement um(m) = u(x(m)) from u(x) by iteration so that 
 * um(m) = u(x(m)).
 * @author Dave Hale, Colorado School of Mines
 * @version 2011.08.30
 */
public abstract class WarpFunction1 {

  /**
   * Returns a warping for a constant shift.
   * @param u shift.
   * @param n number of samples.
   */
  public static WarpFunction1 constant(double u, int n) {
    return new ConstantWarp1(u,n);
  }

  /**
   * Returns a derivative-of-Gaussian warping.
   * @param u maximum shift.
   * @param n number of samples.
   */
  public static WarpFunction1 gaussian(double u, int n) {
    return new GaussianWarp1(u,n);
  }

  /**
   * Returns a sinusoidal warping.
   * @param u maximum shift.
   * @param n number of samples.
   */
  public static WarpFunction1 sinusoid(double u, int n) {
    return new SinusoidWarp1(u,n);
  }

  /**
   * Returns a constant-plus-sinusoidal warping.
   * @param c constant shift.
   * @param u maximum sinusoidal shift.
   * @param n number of samples.
   */
  public static WarpFunction1 constantPlusSinusoid(double c, double u, int n) {
    return new SinusoidWarp1(c,u,n);
  }

  /**
   * Returns the shift u(x).
   * @param x the coordinate x.
   * @return the shift.
   */
  public abstract double u(double x);

  /**
   * Returns the shift u(x).
   * @param x the coordinate x.
   * @return the shift.
   */
  public double ux(double x) {
    return u(x);
  }

  /**
   * Returns the shift um(m) = u(x(m)).
   * @param m the coordinate m.
   * @return the shift.
   */
  public double um(double m) {
    double um = 0.0;
    double up;
    do {
      up = um;
      um = u(m-0.5*um);
    } while (abs(um-up)>0.0001);
    return um;
  }

  /**
   * Returns the shift uy(y) = u(x(y)).
   * @param y the coordinate y.
   * @return the shift.
   */
  public double uy(double y) {
    double uy = 0.0;
    double up;
    do {
      up = uy;
      uy = u(y-uy);
    } while (abs(uy-up)>0.0001);
    return uy;
  }

  /**
   * Returns an array[n] of shifts u(x).
   * @return array of shifts.
   */
  public float[] ux() {
    float[] u = new float[_n];
    for (int i=0; i<_n; ++i) {
      double x = i;
      u[i] = (float)ux(x);
    }
    return u;
  }

  /**
   * Returns an array[n] of shifts um(m) = u(x(m)).
   * @return array of shifts.
   */
  public float[] um() {
    float[] u = new float[_n];
    for (int i=0; i<_n; ++i) {
      double m = i;
      u[i] = (float)um(m);
    }
    return u;
  }

  /**
   * Returns an array[n] of shifts uy(y) = u(x(y)).
   * @return array of shifts.
   */
  public float[] uy() {
    float[] u = new float[_n];
    for (int i=0; i<_n; ++i) {
      double y = i;
      u[i] = (float)uy(y);
    }
    return u;
  }

  /**
   * Warps a sampled function.
   * @param f array of values f(x).
   * @return array of values g(y) = f(y-u(x(y)).
   */
  public float[] warp(float[] f) {
    SincInterpolator si = new SincInterpolator();
    float[] g = new float[_n];
    for (int i=0; i<_n; ++i) {
      double y = i;
      double x = y-uy(y);
      g[i] = si.interpolate(_n,1.0,0.0,f,x);
    }
    return g;
  }

  /**
   * Unwarps a sampled function.
   * @param g array of values g(x).
   * @return array of values f(x) = g(x+u(x)).
   */
  public float[] unwarp(float[] g) {
    SincInterpolator si = new SincInterpolator();
    float[] f = new float[_n];
    for (int i=0; i<_n; ++i) {
      double x = i;
      double y = x+ux(x);
      f[i] = si.interpolate(_n,1.0,0.0,g,y);
    }
    return f;
  }

  protected WarpFunction1(int n) {
    _n = n;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n;

  /**
   * Constant (zero-strain) warping.
   */
  private static class ConstantWarp1 extends WarpFunction1 {
    public ConstantWarp1(double u, int n) {
      super(n);
      _u = u;
    }
    public double u(double x) {
      return _u;
    }
    public double umax() {
      return _u;
    }
    public double e(double x) {
      return 0.0;
    }
    public double emax() {
      return 0.0;
    }
    private double _u;
  }

  /**
   * Derivative-of-Gaussian warping.
   */
  private static class GaussianWarp1 extends WarpFunction1 {
    public GaussianWarp1(double umax, int n) {
      super(n);
      _a = (n-1)/2.0;
      _b = _a/3;
      _c = umax*exp(0.5)/_b;
      _umax = umax;
      _emax = _c;
    }
    public double u(double x) {
      double xa = x-_a;
      return -_c*xa*exp(-0.5*(xa*xa)/(_b*_b));
    }
    public double umax() {
      return _umax;
    }
    public double e(double x) {
      double xa = x-_a;
      return -_c*(1.0-(xa*xa)/(_b*_b))*exp(-0.5*(xa*xa)/(_b*_b));
    }
    public double emax() {
      return _emax;
    }
    private double _a;
    private double _b;
    private double _c;
    private double _umax;
    private double _emax;
  }

  /**
   * Sinusoidal warping.
   */
  private static class SinusoidWarp2 extends WarpFunction2 {
    public SinusoidWarp2(double u1max, double u2max, int n1, int n2) {
      this(0.0,0.0,u1max,u2max,n1,n2);
    }
    public SinusoidWarp2(
      double u1add, double u2add, 
      double u1max, double u2max, 
      int n1, int n2) 
    {
      super(n1,n2);
      double l1 = n1-1;
      double l2 = n2-1;
      _c1 = u1add;
      _c2 = u2add;
      _a1 = u1max;
      _a2 = u2max;
      _b1 = 2.0*PI/l1;
      _b2 = 2.0*PI/l2;
    }
    public double u1(double x1, double x2) {
      return _c1+_a1*sin(_b1*x1)*sin(0.5*_b2*x2);
    }
    public double u2(double x1, double x2) {
      return _c2+_a2*sin(_b2*x2)*sin(0.5*_b1*x1);
    }
    private double _a1,_a2;
    private double _b1,_b2;
    private double _c1,_c2;
  }

  /**
   * Sinusoidal warping.
   */
  private static class SinusoidWarp1 extends WarpFunction1 {
    public SinusoidWarp1(double umax, int n) {
      this(0.0,umax,n);
    }
    public SinusoidWarp1(double uadd, double umax, int n) {
      super(n);
      double l = n-1;
      _a = umax;
      _b = 2.0*PI/l;
      _c = uadd;
      _umax = umax;
      _emax = _a*_b;
    }
    public double u(double x) {
      return _c+_a*sin(_b*x);
    }
    public double umax() {
      return _umax;
    }
    public double e(double x) {
      return _a*_b*cos(_b*x);
    }
    public double emax() {
      return _emax;
    }
    private double _a,_b,_c;
    private double _umax;
    private double _emax;
  }
}
