package edu.mines.jtk.dsp;

import edu.mines.jtk.dsp.SincInterp;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Synthetic warping functions for 3-D images.
 * The function u(x) describes the warping, in which a point x
 * is displaced to a point y(x) = x+u(x).
 * <p>
 * Warping is the computation of the image g(y) = f(x(y)).
 * Unwarping is the computation of the image f(x) = g(y(x)).
 * <p>
 * For warping, we need the function x(y) = y-u(x(y)) = y-uy(y). We 
 * compute the displacement uy(y) by iteration so that uy(y) = u(x(y)).
 * <p>
 * We also define a midpoint m(x) = (x+y(x))/2, and compute the 
 * displacement um(m) = u(x(m)) from u(x) by iteration so that 
 * um(m) = u(x(m)).
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.05.30
 */
public abstract class WarpFunction3 {

  /**
   * Returns a warping for a constant shift.
   * @param u1 shift in 1st dimension.
   * @param u2 shift in 2nd dimension.
   * @param u3 shift in 3rd dimension.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   * @param n3 number of samples in 3rd dimension.
   */
  public static WarpFunction3 constant(
    double u1, double u2, double u3, 
    int n1, int n2, int n3) 
  {
    return new ConstantWarp3(u1,u2,u3,n1,n2,n3);
  }

  /**
   * Returns a derivative-of-Gaussian warping.
   * @param u1 maximum shift in 1st dimension.
   * @param u2 maximum shift in 2nd dimension.
   * @param u3 maximum shift in 3rd dimension.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   * @param n3 number of samples in 3rd dimension.
   */
  public static WarpFunction3 gaussian(
    double u1, double u2, double u3, 
    int n1, int n2, int n3) 
  {
    return new GaussianWarp3(u1,u2,u3,n1,n2,n3);
  }

  /**
   * Returns a sinusoidal warping.
   * @param u1 maximum shift in 1st dimension.
   * @param u2 maximum shift in 2nd dimension.
   * @param u3 maximum shift in 3rd dimension.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   * @param n3 number of samples in 3rd dimension.
   */
  public static WarpFunction3 sinusoid(
    double u1, double u2, double u3, 
    int n1, int n2, int n3) 
  {
    return new SinusoidWarp3(u1,u2,u3,n1,n2,n3);
  }

  /**
   * Returns a constant-plus-sinusoidal warping.
   * @param c1 constant shift in 1st dimension.
   * @param c2 constant shift in 2nd dimension.
   * @param c3 constant shift in 3rd dimension.
   * @param u1 maximum sinusoidal shift in 1st dimension.
   * @param u2 maximum sinusoidal shift in 2nd dimension.
   * @param u3 maximum sinusoidal shift in 3rd dimension.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   * @param n3 number of samples in 3rd dimension.
   */
  public static WarpFunction3 constantPlusSinusoid(
    double c1, double c2, double c3,
    double u1, double u2, double u3, 
    int n1, int n2, int n3) 
  {
    return new SinusoidWarp3(c1,c2,c3,u1,u2,u3,n1,n2,n3);
  }

  /**
   * Returns the 1st component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @param x3 3rd coordinate of the point x.
   * @return 1st component of shift.
   */
  public abstract double u1(double x1, double x2, double x3);

  /**
   * Returns the 2nd component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @param x3 3rd coordinate of the point x.
   * @return 2nd component of shift.
   */
  public abstract double u2(double x1, double x2, double x3);

  /**
   * Returns the 3rd component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @param x3 3rd coordinate of the point x.
   * @return 3rd component of shift.
   */
  public abstract double u3(double x1, double x2, double x3);

  /**
   * Returns the 1st component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @param x3 3rd coordinate of the point x.
   * @return 1st component of shift.
   */
  public double u1x(double x1, double x2, double x3) {
    return u1(x1,x2,x3);
  }

  /**
   * Returns the 2nd component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @param x3 3rd coordinate of the point x.
   * @return 2nd component of shift.
   */
  public double u2x(double x1, double x2, double x3) {
    return u2(x1,x2,x3);
  }

  /**
   * Returns the 3rd component of the shift u(x).
   * @param x1 1st coordinate of the point x.
   * @param x2 2nd coordinate of the point x.
   * @param x3 3rd coordinate of the point x.
   * @return 3rd component of shift.
   */
  public double u3x(double x1, double x2, double x3) {
    return u3(x1,x2,x3);
  }

  /**
   * Returns the 1st component of the shift um(m) = u(x(m)).
   * @param m1 1st coordinate of the point m.
   * @param m2 2nd coordinate of the point m.
   * @param m3 3rd coordinate of the point m.
   * @return 1st component of shift.
   */
  public double u1m(double m1, double m2, double m3) {
    double u1p;
    double u1m = 0.0;
    double u2m = 0.0;
    double u3m = 0.0;
    do {
      u1p = u1m;
      u1m = u1(m1-0.5*u1m,m2-0.5*u2m,m3-0.5*u3m);
      u2m = u2(m1-0.5*u1m,m2-0.5*u2m,m3-0.5*u3m);
      u3m = u3(m1-0.5*u1m,m2-0.5*u2m,m3-0.5*u3m);
    } while (abs(u1m-u1p)>0.0001);
    return u1m;
  }

  /**
   * Returns the 2nd component of the shift um(m) = u(x(m)).
   * @param m1 1st coordinate of the point m.
   * @param m2 2nd coordinate of the point m.
   * @param m3 3rd coordinate of the point m.
   * @return 2nd component of shift.
   */
  public double u2m(double m1, double m2, double m3) {
    double u2p;
    double u1m = 0.0;
    double u2m = 0.0;
    double u3m = 0.0;
    do {
      u2p = u2m;
      u1m = u1(m1-0.5*u1m,m2-0.5*u2m,m3-0.5*u3m);
      u2m = u2(m1-0.5*u1m,m2-0.5*u2m,m3-0.5*u3m);
      u3m = u3(m1-0.5*u1m,m2-0.5*u2m,m3-0.5*u3m);
    } while (abs(u2m-u2p)>0.0001);
    return u2m;
  }

  /**
   * Returns the 3rd component of the shift um(m) = u(x(m)).
   * @param m1 1st coordinate of the point m.
   * @param m2 2nd coordinate of the point m.
   * @param m3 3rd coordinate of the point m.
   * @return 3rd component of shift.
   */
  public double u3m(double m1, double m2, double m3) {
    double u3p;
    double u1m = 0.0;
    double u2m = 0.0;
    double u3m = 0.0;
    do {
      u3p = u3m;
      u1m = u1(m1-0.5*u1m,m2-0.5*u2m,m3-0.5*u3m);
      u2m = u2(m1-0.5*u1m,m2-0.5*u2m,m3-0.5*u3m);
      u3m = u3(m1-0.5*u1m,m2-0.5*u2m,m3-0.5*u3m);
    } while (abs(u3m-u3p)>0.0001);
    return u3m;
  }

  /**
   * Returns the 1st component of the shift uy(y) = u(x(y)).
   * @param y1 1st coordinate of the point y.
   * @param y2 2nd coordinate of the point y.
   * @param y3 3rd coordinate of the point y.
   * @return 1st component of shift.
   */
  public double u1y(double y1, double y2, double y3) {
    double u1p;
    double u1y = 0.0;
    double u2y = 0.0;
    double u3y = 0.0;
    do {
      u1p = u1y;
      u1y = u1(y1-u1y,y2-u2y,y3-u3y);
      u2y = u2(y1-u1y,y2-u2y,y3-u3y);
      u3y = u3(y1-u1y,y2-u2y,y3-u3y);
    } while (abs(u1y-u1p)>0.0001);
    return u1y;
  }

  /**
   * Returns the 2nd component of the shift uy(y) = u(x(y)).
   * @param y1 1st coordinate of the point y.
   * @param y2 2nd coordinate of the point y.
   * @param y3 3rd coordinate of the point y.
   * @return 2nd component of shift.
   */
  public double u2y(double y1, double y2, double y3) {
    double u2p;
    double u1y = 0.0;
    double u2y = 0.0;
    double u3y = 0.0;
    do {
      u2p = u2y;
      u1y = u1(y1-u1y,y2-u2y,y3-u3y);
      u2y = u2(y1-u1y,y2-u2y,y3-u3y);
      u3y = u3(y1-u1y,y2-u2y,y3-u3y);
    } while (abs(u2y-u2p)>0.0001);
    return u2y;
  }

  /**
   * Returns the 3rd component of the shift uy(y) = u(x(y)).
   * @param y1 1st coordinate of the point y.
   * @param y2 2nd coordinate of the point y.
   * @param y3 3rd coordinate of the point y.
   * @return 3rd component of shift.
   */
  public double u3y(double y1, double y2, double y3) {
    double u3p;
    double u1y = 0.0;
    double u2y = 0.0;
    double u3y = 0.0;
    do {
      u3p = u3y;
      u1y = u1(y1-u1y,y2-u2y,y3-u3y);
      u2y = u2(y1-u1y,y2-u2y,y3-u3y);
      u3y = u3(y1-u1y,y2-u2y,y3-u3y);
    } while (abs(u3y-u3p)>0.0001);
    return u3y;
  }

  /**
   * Returns an array[n3][n2][n1] of 1st components of shifts u(x).
   * @return array of 1st components of shifts.
   */
  public float[][][] u1x() {
    float[][][] u = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          u[i3][i2][i1] = (float)u1x(x1,x2,x3);
        }
      }
    }
    return u;
  }

  /**
   * Returns an array[n3][n2][n1] of 2nd components of shifts u(x).
   * @return array of 2nd components of shifts.
   */
  public float[][][] u2x() {
    float[][][] u = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          u[i3][i2][i1] = (float)u2x(x1,x2,x3);
        }
      }
    }
    return u;
  }

  /**
   * Returns an array[n3][n2][n1] of 3rd components of shifts u(x).
   * @return array of 3rd components of shifts.
   */
  public float[][][] u3x() {
    float[][][] u = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          u[i3][i2][i1] = (float)u3x(x1,x2,x3);
        }
      }
    }
    return u;
  }

  /**
   * Returns an array[n3][n2][n1] of 1st components of shifts um(m) = u(x(m)).
   * @return array of 1st components of shifts.
   */
  public float[][][] u1m() {
    float[][][] u = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          u[i3][i2][i1] = (float)u1m(x1,x2,x3);
        }
      }
    }
    return u;
  }

  /**
   * Returns an array[n3][n2][n1] of 2nd components of shifts um(m) = u(x(m)).
   * @return array of 2nd components of shifts.
   */
  public float[][][] u2m() {
    float[][][] u = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          u[i3][i2][i1] = (float)u2m(x1,x2,x3);
        }
      }
    }
    return u;
  }

  /**
   * Returns an array[n3][n2][n1] of 3rd components of shifts um(m) = u(x(m)).
   * @return array of 3rd components of shifts.
   */
  public float[][][] u3m() {
    float[][][] u = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          u[i3][i2][i1] = (float)u3m(x1,x2,x3);
        }
      }
    }
    return u;
  }

  /**
   * Returns an array[n3][n2][n1] of 1st components of shifts uy(y) = u(x(y)).
   * @return array of 1st components of shifts.
   */
  public float[][][] u1y() {
    float[][][] u = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          u[i3][i2][i1] = (float)u1y(x1,x2,x3);
        }
      }
    }
    return u;
  }

  /**
   * Returns an array[n3][n2][n1] of 2nd components of shifts uy(y) = u(x(y)).
   * @return array of 2nd components of shifts.
   */
  public float[][][] u2y() {
    float[][][] u = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          u[i3][i2][i1] = (float)u2y(x1,x2,x3);
        }
      }
    }
    return u;
  }

  /**
   * Returns an array[n3][n2][n1] of 3rd components of shifts uy(y) = u(x(y)).
   * @return array of 3rd components of shifts.
   */
  public float[][][] u3y() {
    float[][][] u = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          u[i3][i2][i1] = (float)u3y(x1,x2,x3);
        }
      }
    }
    return u;
  }

  /**
   * Warps a sampled function using only 1st components of shifts.
   * @param f array of values f(x).
   * @return array of values g(y) = f(y-u1(x(y)).
   */
  public float[][][] warp1(float[][][] f) {
    SincInterp si = new SincInterp();
    float[][][] g = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          g[i3][i2][i1] = si.interpolate(
            _n1,1.0,0.0,f[i3][i2],i1-u1y(i1,i2,i3));
        }
      }
    }
    return g;
  }

  /**
   * Warps a sampled function using only all components of shifts.
   * @param f array of values f(x).
   * @return array of values g(y) = f(y-u(x(y)).
   */
  public float[][][] warp(float[][][] f) {
    SincInterp si = new SincInterp();
    float[][][] g = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double y3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double y2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double y1 = i1;
          double x1 = y1-u1y(y1,y2,y3);
          double x2 = y2-u2y(y1,y2,y3);
          double x3 = y3-u3y(y1,y2,y3);
          g[i3][i2][i1] = si.interpolate(
            _n1,1.0,0.0,_n2,1.0,0.0,_n3,1.0,0.0,f,
            x1,x2,x3);
        }
      }
    }
    return g;
  }

  /**
   * Unwarps a sampled function using only 1st components of shifts.
   * @param g array of values g(x).
   * @return array of values f(x) = g(x+u1(x)).
   */
  public float[][][] unwarp1(float[][][] g) {
    SincInterp si = new SincInterp();
    float[][][] f = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          f[i3][i2][i1] = si.interpolate(
            _n1,1.0,0.0,g[i3][i2],i1+u1x(i1,i2,i3));
        }
      }
    }
    return f;
  }

  /**
   * Unwarps a sampled function using only all components of shifts.
   * @param g array of values g(x).
   * @return array of values f(x) = g(x+u(x)).
   */
  public float[][][] unwarp(float[][][] g) {
    SincInterp si = new SincInterp();
    float[][][] f = new float[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      double x3 = i3;
      for (int i2=0; i2<_n2; ++i2) {
        double x2 = i2;
        for (int i1=0; i1<_n1; ++i1) {
          double x1 = i1;
          double y1 = x1+u1x(x1,x2,x3);
          double y2 = x2+u2x(x1,x2,x3);
          double y3 = x3+u3x(x1,x2,x3);
          f[i3][i2][i1] = si.interpolate(
            _n1,1.0,0.0,_n2,1.0,0.0,_n3,1.0,0.0,g,
            y1,y2,y3);
        }
      }
    }
    return f;
  }

  protected WarpFunction3(int n1, int n2, int n3) {
    _n1 = n1;
    _n2 = n2;
    _n3 = n3;
  }
  private int _n1,_n2,_n3;

  ///////////////////////////////////////////////////////////////////////////
  // private

  /**
   * Constant (zero-strain) warping.
   */
  private static class ConstantWarp3 extends WarpFunction3 {
    public ConstantWarp3(
      double u1, double u2, double u3, int n1, int n2, int n3) 
    {
      super(n1,n2,n3);
      _u1 = u1;
      _u2 = u2;
      _u3 = u3;
    }
    public double u1(double x1, double x2, double x3) { return _u1; }
    public double u2(double x1, double x2, double x3) { return _u2; }
    public double u3(double x1, double x2, double x3) { return _u3; }
    private double _u1,_u2,_u3;
  }

  /**
   * Derivative-of-Gaussian warping.
   */
  private static class GaussianWarp3 extends WarpFunction3 {
    public GaussianWarp3(
      double u1max, double u2max, double u3max, 
      int n1, int n2, int n3) 
    {
      super(n1,n2,n3);
      _a1 = (n1-1)/2.0;
      _a2 = (n2-1)/2.0;
      _a3 = (n3-1)/2.0;
      _b1 = _a1/3.0;
      _b2 = _a2/3.0;
      _b3 = _a3/3.0;
      _c1 = u1max*exp(0.5)/_b1;
      _c2 = u2max*exp(0.5)/_b2;
      _c3 = u3max*exp(0.5)/_b3;
    }
    public double u1(double x1, double x2, double x3) {
      double xa1 = x1-_a1;
      double xa2 = x2-_a2;
      double xa3 = x3-_a3;
      return -_c1*xa1*exp(-0.5*(
        (xa1*xa1)/(_b1*_b1)+
        (xa2*xa2)/(_b2*_b2)+
        (xa3*xa3)/(_b3*_b3)));
    }
    public double u2(double x1, double x2, double x3) {
      double xa1 = x1-_a1;
      double xa2 = x2-_a2;
      double xa3 = x3-_a3;
      return -_c2*xa2*exp(-0.5*(
        (xa1*xa1)/(_b1*_b1)+
        (xa2*xa2)/(_b2*_b2)+
        (xa3*xa3)/(_b3*_b3)));
    }
    public double u3(double x1, double x2, double x3) {
      double xa1 = x1-_a1;
      double xa2 = x2-_a2;
      double xa3 = x3-_a3;
      return -_c3*xa3*exp(-0.5*(
        (xa1*xa1)/(_b1*_b1)+
        (xa2*xa2)/(_b2*_b2)+
        (xa3*xa3)/(_b3*_b3)));
    }
    private double _a1,_a2,_a3;
    private double _b1,_b2,_b3;
    private double _c1,_c2,_c3;
  }

  /**
   * Sinusoidal warping.
   */
  private static class SinusoidWarp3 extends WarpFunction3 {
    public SinusoidWarp3(
      double u1max, double u2max, double u3max,
      int n1, int n2, int n3) {
      this(0.0,0.0,0.0,u1max,u2max,u3max,n1,n2,n3);
    }
    public SinusoidWarp3(
      double u1add, double u2add, double u3add,
      double u1sin, double u2sin, double u3sin,
      int n1, int n2, int n3) 
    {
      super(n1,n2,n3);
      double l1 = n1-1;
      double l2 = n2-1;
      double l3 = n3-1;
      _c1 = u1add;
      _c2 = u2add;
      _c3 = u3add;
      _a1 = u1sin;
      _a2 = u2sin;
      _a3 = u3sin;
      _b1 = 2.0*PI/l1;
      _b2 = 2.0*PI/l2;
      _b3 = 2.0*PI/l3;
    }
    public double u1(double x1, double x2, double x3) {
      return _c1+_a1*sin(_b1*x1)*sin(0.5*_b2*x2)*sin(0.5*_b3*x3);
    }
    public double u2(double x1, double x2, double x3) {
      return _c2+_a2*sin(_b2*x2)*sin(0.5*_b1*x1)*sin(0.5*_b3*x3);
    }
    public double u3(double x1, double x2, double x3) {
      return _c3+_a3*sin(_b3*x3)*sin(0.5*_b1*x1)*sin(0.5*_b2*x2);
    }
    private double _a1,_a2,_a3;
    private double _b1,_b2,_b3;
    private double _c1,_c2,_c3;
  }
}
