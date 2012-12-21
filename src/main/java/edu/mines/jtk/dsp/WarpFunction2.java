package edu.mines.jtk.dsp;

import edu.mines.jtk.dsp.SincInterp;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Synthetic warping functions for 2-D images.
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
 * @version 2011.09.12
 */
public abstract class WarpFunction2 {

  /**
   * Returns a warping for a constant shift.
   * @param u1 shift in 1st dimension.
   * @param u2 shift in 2nd dimension.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   */
  public static WarpFunction2 constant(double u1, double u2, int n1, int n2) {
    return new ConstantWarp2(u1,u2,n1,n2);
  }

  /**
   * Returns a derivative-of-Gaussian warping.
   * @param u1 maximum shift in 1st dimension.
   * @param u2 maximum shift in 2nd dimension.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   */
  public static WarpFunction2 gaussian(double u1, double u2, int n1, int n2) {
    return new GaussianWarp2(u1,u2,n1,n2);
  }

  /**
   * Returns a sinusoidal warping.
   * @param u1 maximum shift in 1st dimension.
   * @param u2 maximum shift in 2nd dimension.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   */
  public static WarpFunction2 sinusoid(double u1, double u2, int n1, int n2) {
    return new SinusoidWarp2(u1,u2,n1,n2);
  }

  /**
   * Returns a constant-plus-sinusoidal warping.
   * @param c1 constant shift in 1st dimension.
   * @param c2 constant shift in 2nd dimension.
   * @param u1 maximum sinusoidal shift in 1st dimension.
   * @param u2 maximum sinusoidal shift in 2nd dimension.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   */
  public static WarpFunction2 constantPlusSinusoid(
    double c1, double c2, double u1, double u2, int n1, int n2) 
  {
    return new SinusoidWarp2(c1,c2,u1,u2,n1,n2);
  }

  /**
   * Returns the 1st component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @return 1st component of shift.
   */
  public abstract double u1(double x1, double x2);

  /**
   * Returns the 2nd component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @return 2nd component of shift.
   */
  public abstract double u2(double x1, double x2);

  /**
   * Returns the 1st component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @return 1st component of shift.
   */
  public double u1x(double x1, double x2) {
    return u1(x1,x2);
  }

  /**
   * Returns the 2nd component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @return 2nd component of shift.
   */
  public double u2x(double x1, double x2) {
    return u2(x1,x2);
  }

  /**
   * Returns the 1st component of the shift um(m) = u(x(m)).
   * @param m1 1st coordinate of the point m.
   * @param m2 2nd coordinate of the point m.
   * @return 1st component of shift.
   */
  public double u1m(double m1, double m2) {
    double u1p;
    double u1m = 0.0;
    double u2m = 0.0;
    do {
      u1p = u1m;
      u1m = u1(m1-0.5*u1m,m2-0.5*u2m);
      u2m = u2(m1-0.5*u1m,m2-0.5*u2m);
    } while (abs(u1m-u1p)>0.0001);
    return u1m;
  }

  /**
   * Returns the 2nd component of the shift um(m) = u(x(m)).
   * @param m1 1st coordinate of the point m.
   * @param m2 2nd coordinate of the point m.
   * @return 2nd component of shift.
   */
  public double u2m(double m1, double m2) {
    double u2p;
    double u1m = 0.0;
    double u2m = 0.0;
    do {
      u2p = u2m;
      u1m = u1(m1-0.5*u1m,m2-0.5*u2m);
      u2m = u2(m1-0.5*u1m,m2-0.5*u2m);
    } while (abs(u2m-u2p)>0.0001);
    return u2m;
  }

  /**
   * Returns the 1st component of the shift uy(y) = u(x(y)).
   * @param y1 1st coordinate of the point y.
   * @param y2 2nd coordinate of the point y.
   * @return 1st component of shift.
   */
  public double u1y(double y1, double y2) {
    double u1p;
    double u1y = 0.0;
    double u2y = 0.0;
    do {
      u1p = u1y;
      u1y = u1(y1-u1y,y2-u2y);
      u2y = u2(y1-u1y,y2-u2y);
    } while (abs(u1y-u1p)>0.0001);
    return u1y;
  }

  /**
   * Returns the 2nd component of the shift uy(y) = u(x(y)).
   * @param y1 1st coordinate of the point y.
   * @param y2 2nd coordinate of the point y.
   * @return 2nd component of shift.
   */
  public double u2y(double y1, double y2) {
    double u2p;
    double u1y = 0.0;
    double u2y = 0.0;
    do {
      u2p = u2y;
      u1y = u1(y1-u1y,y2-u2y);
      u2y = u2(y1-u1y,y2-u2y);
    } while (abs(u2y-u2p)>0.0001);
    return u2y;
  }

  /**
   * Returns an array[n2][n1] of 1st components of shifts u(x).
   * @return array of 1st components of shifts.
   */
  public float[][] u1x() {
    float[][] u = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      double x2 = i2;
      for (int i1=0; i1<_n1; ++i1) {
        double x1 = i1;
        u[i2][i1] = (float)u1x(x1,x2);
      }
    }
    return u;
  }

  /**
   * Returns an array[n2][n1] of 2nd components of shifts u(x).
   * @return array of 2nd components of shifts.
   */
  public float[][] u2x() {
    float[][] u = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      double x2 = i2;
      for (int i1=0; i1<_n1; ++i1) {
        double x1 = i1;
        u[i2][i1] = (float)u2x(x1,x2);
      }
    }
    return u;
  }

  /**
   * Returns an array[n2][n1] of 1st components of shifts um(m) = u(x(m)).
   * @return array of 1st components of shifts.
   */
  public float[][] u1m() {
    float[][] u = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      double m2 = i2;
      for (int i1=0; i1<_n1; ++i1) {
        double m1 = i1;
        u[i2][i1] = (float)u1m(m1,m2);
      }
    }
    return u;
  }

  /**
   * Returns an array[n2][n1] of 2nd components of shifts um(m) = u(x(m)).
   * @return array of 2nd components of shifts.
   */
  public float[][] u2m() {
    float[][] u = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      double m2 = i2;
      for (int i1=0; i1<_n1; ++i1) {
        double m1 = i1;
        u[i2][i1] = (float)u2m(m1,m2);
      }
    }
    return u;
  }

  /**
   * Returns an array[n2][n1] of 1st components of shifts uy(y) = u(x(y)).
   * @return array of 1st components of shifts.
   */
  public float[][] u1y() {
    float[][] u = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      double y2 = i2;
      for (int i1=0; i1<_n1; ++i1) {
        double y1 = i1;
        u[i2][i1] = (float)u1y(y1,y2);
      }
    }
    return u;
  }

  /**
   * Returns an array[n2][n1] of 2nd components of shifts uy(y) = u(x(y)).
   * @return array of 2nd components of shifts.
   */
  public float[][] u2y() {
    float[][] u = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      double y2 = i2;
      for (int i1=0; i1<_n1; ++i1) {
        double y1 = i1;
        u[i2][i1] = (float)u2y(y1,y2);
      }
    }
    return u;
  }

  /**
   * Warps a sampled function using only 1st components of shifts.
   * @param f array of values f(x).
   * @return array of values g(y) = f(y-u1(x(y)).
   */
  public float[][] warp1(float[][] f) {
    SincInterp si = new SincInterp();
    float[][] g = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        g[i2][i1] = si.interpolate(_n1,1.0,0.0,f[i2],i1-u1y(i1,i2));
      }
    }
    return g;
  }

  /**
   * Warps a sampled function using only all components of shifts.
   * @param f array of values f(x).
   * @return array of values g(y) = f(y-u(x(y)).
   */
  public float[][] warp(float[][] f) {
    SincInterp si = new SincInterp();
    float[][] g = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      double y2 = i2;
      for (int i1=0; i1<_n1; ++i1) {
        double y1 = i1;
        double x1 = y1-u1y(y1,y2);
        double x2 = y2-u2y(y1,y2);
        g[i2][i1] = si.interpolate(_n1,1.0,0.0,_n2,1.0,0.0,f,x1,x2);
      }
    }
    return g;
  }

  /**
   * Unwarps a sampled function using only 1st components of shifts.
   * @param g array of values g(x).
   * @return array of values f(x) = g(x+u1(x)).
   */
  public float[][] unwarp1(float[][] g) {
    SincInterp si = new SincInterp();
    float[][] f = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        f[i2][i1] = si.interpolate(_n1,1.0,0.0,g[i2],i1+u1x(i1,i2));
      }
    }
    return f;
  }

  /**
   * Unwarps a sampled function using only all components of shifts.
   * @param g array of values g(x).
   * @return array of values f(x) = g(x+u(x)).
   */
  public float[][] unwarp(float[][] g) {
    SincInterp si = new SincInterp();
    float[][] f = new float[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      double x2 = i2;
      for (int i1=0; i1<_n1; ++i1) {
        double x1 = i1;
        double y1 = x1+u1x(x1,x2);
        double y2 = x2+u2x(x1,x2);
        f[i2][i1] = si.interpolate(_n1,1.0,0.0,_n2,1.0,0.0,g,y1,y2);
      }
    }
    return f;
  }

  protected WarpFunction2(int n1, int n2) {
    _n1 = n1;
    _n2 = n2;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n1,_n2;

  /**
   * Constant (zero-strain) warping.
   */
  private static class ConstantWarp2 extends WarpFunction2 {
    public ConstantWarp2(double u1, double u2, int n1, int n2) {
      super(n1,n2);
      _u1 = u1;
      _u2 = u2;
    }
    public double u1(double x1, double x2) {
      return _u1;
    }
    public double u2(double x1, double x2) {
      return _u2;
    }
    private double _u1,_u2;
  }

  /**
   * Derivative-of-Gaussian warping.
   */
  private static class GaussianWarp2 extends WarpFunction2 {
    public GaussianWarp2(double u1max, double u2max, int n1, int n2) {
      super(n1,n2);
      _a1 = (n1-1)/2.0;
      _a2 = (n2-1)/2.0;
      _b1 = _a1/3.0;
      _b2 = _a2/3.0;
      _c1 = u1max*exp(0.5)/_b1;
      _c2 = u2max*exp(0.5)/_b2;
    }
    public double u1(double x1, double x2) {
      double xa1 = x1-_a1;
      double xa2 = x2-_a2;
      return -_c1*xa1*exp(-0.5*((xa1*xa1)/(_b1*_b1)+(xa2*xa2)/(_b2*_b2)));
    }
    public double u2(double x1, double x2) {
      double xa1 = x1-_a1;
      double xa2 = x2-_a2;
      return -_c2*xa2*exp(-0.5*((xa1*xa1)/(_b1*_b1)+(xa2*xa2)/(_b2*_b2)));
    }
    private double _a1,_a2;
    private double _b1,_b2;
    private double _c1,_c2;
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
}
