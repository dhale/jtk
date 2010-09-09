/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.la.DMatrix;
import edu.mines.jtk.la.DMatrixLud;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Interpolation of scattered data f(x1,x2) with radial basis functions.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.01.15
 */
public class RadialInterpolator2 {

  /**
   * The interface implemented by all radial basis functions.
   */
  interface Basis {

    /**
     * Evaluates this basis function for the specified distance.
     * @param r the radial distance.
     * @return the value of this basis function.
     */
    public double evaluate(double r);
  }

  /**
   * Sandwell's biharmonic basis function g(r)  = r*r*(log(r)-1).
   * <p>
   * A scale factor s may be specified that divides all distances r, so 
   * that the basis function becomes g(r/s).
   * <p>
   * See Sandwell, 1987, Biharmonic spline interpolation of GEOS-3 and
   * SEASAT altimeter data: Geophysical research letters, 14, 139-142.
   */
  public static class Biharmonic implements Basis {

    /**
     * Constructs a basis with scale factor one.
     */
    public Biharmonic() {
      this(1.0);
    }

    /**
     * Constructs a basis for the specified scale factor.
     * @param scale the factor by which to divide distances r.
     */
    public Biharmonic(double scale) {
      _s = 1.0/scale;
    }

    public double evaluate(double r) {
      double g = 0.0;
      if (r>0.0) {
        r *= _s;
        g = r*r*(log(r)-1.0);
      }
      return g;
    }
    private double _s;
  }

  /**
   * The Wessel-Bercovici basis function g(r) for splines with tension.
   * This function is K0(p*r)+log(p*r)+c, where p = sqrt(t/(1-t)), t is
   * a tension parameter in the range [0,1), and c is a constant such 
   * that g(r) = 0. K0 denotes the modified Bessel function of the second 
   * kind and order zero. For the special case where tension t = p = 0, 
   * the biharmonic basis function g(r) = r*r*(log(r)-1) is used instead.
   * <p>
   * For consistency when using the same tension parameter t for different 
   * sets of data, distances r should be normalized to be dimensionless. A 
   * scale factor s may be specified that divides all distances r, so that 
   * the basis function becomes g(r/s). The scale factor suggested by Wessel 
   * and Bercovici is s = rmax/50, where rmax is the maximum seperation 
   * between points to be interpolated.
   * <p>
   * See Wessel and Bercovici, 1998, Interpolation with splines in
   * tension: a Green's function approach: Mathematical Geology 30,
   * 77-93.
   */
  public static class WesselBercovici implements Basis {

    /**
     * Constructs a basis for the specified tension.
     * This basis does not normalize distances r.
     * @param tension the tension; must be in the range [0,1).
     */
    public WesselBercovici(double tension) {
      this(tension,1.0);
    }

    /**
     * Constructs a basis for the specified tension and scale factor.
     * @param tension the tension; must be in the range [0,1).
     * @param scale the factor by which to divide distances r.
     */
    public WesselBercovici(double tension, double scale) {
      Check.argument(0.0<=tension,"0.0<=tension");
      Check.argument(tension<1.0,"tension<1.0");
      _p = sqrt(tension/(1.0-tension));
      _q = 2.0/_p;
      _s = 1.0/scale;
    }

    public double evaluate(double r) {
      // Adapted from Wessel's greenspline.c in GMT (Generic Mapping Tools).
      double g = 0.0;
      if (r>0.0) {
        r *= _s;
        if (_p==0.0) {
          g = r*r*(log(r)-1.0);
        } else {
          double pr = _p*r;
          if (r<=_q) {
            double t = pr*pr;
            double y = 0.25*t;
            double z = t/14.0625;
            g = (-log(0.5*pr) * 
                (z*(3.5156229 + 
                 z*(3.0899424 + 
                 z*(1.2067492 + 
                 z*(0.2659732 + 
                 z*(0.360768e-1 + 
                 z*0.45813e-2))))))) + 
              (y*(0.42278420 + 
               y*(0.23069756 + 
               y*(0.3488590e-1 + 
               y*(0.262698e-2 + 
               y*(0.10750e-3 + 
               y*0.74e-5))))));
          } else {
            double y = _q/r;
            g = (exp(-pr)/sqrt(pr)) * 
              (1.25331414 + 
               y*(-0.7832358e-1 + 
               y*(0.2189568e-1 + 
               y*(-0.1062446e-1 + 
               y*(0.587872e-2 + 
               y*(-0.251540e-2 + 
               y*0.53208e-3)))))) + 
               log(pr)-LOG2+EULER_GAMMA;
          }
        }
      }
      return g;
    }
    private double _p,_q,_s;
    private static final double LOG2 = 0.69314718055994530942;
    private static final double EULER_GAMMA = 0.577215664901532860606512;
  }

  /**
   * Constructs a gridder with specified known (scattered) samples.
   * @param basis the radial basis function.
   * @param f array of known sample values f(x1,x2).
   * @param x1 array of known sample x1 coordinates.
   * @param x2 array of known sample x2 coordinates.
   */
  public RadialInterpolator2(Basis basis, float[] f, float[] x1, float[] x2) {
    _basis = basis;
    setSamples(f,x1,x2);
  }

  /**
   * Sets the radial basis function used by this interpolator.
   * @param basis the radial basis function.
   */
  public void setBasis(Basis basis) {
    _basis = basis;
  }

  /**
   * Sets the known (scattered) samples to be interpolated.
   * The specified arrays are copied; not referenced.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public void setSamples(float[] f, float[] x1, float[] x2) {
    _n = f.length;
    _f = copy(f);
    _x1 = copy(x1);
    _x2 = copy(x2);
    _w = null;
    if (_trend!=null)
      _trend.detrend(_f,_x1,_x2);
  }

  /**
   * Sets the metric tensor used to compute distances.
   * A metric tensor can make the radial basis function anisotropic, 
   * with elliptical contours of constant value. The default metric 
   * tensor is the identity matrix, which corresponds to an isotropic 
   * basis function.
   * <p>
   * Distance squared from the origin to a point (x1,x2) is 
   * x1*m11*x1 + 2*x1*m12*x2 + x2*m22*x2. The determinant
   * m11*m22-m12*m12 of the metric tensor must be non-negative.
   * @param m11 the metric tensor element (1,1).
   * @param m12 the metric tensor element (1,2).
   * @param m22 the metric tensor element (2,2).
   */
  public void setMetricTensor(double m11, double m12, double m22) {
    Check.argument(m11*m22>=m12*m12,"determinant is non-negative");
    if (_m11!=m11 || _m12!=m12 || _m22!=m22) {
      _m11 = m11;
      _m12 = m12;
      _m22 = m22;
      _mt = _m11!=1.0 || _m12!=0.0 || _m22!=1.0;
      _w = null;
    }
  }

  /**
   * Sets the order of the polynomial trend to be fit to sample values.
   * This trend is subtracted before computing weights for the radial 
   * basis functions, and it is restored when values are interpolated.
   * The default order is -1, so that no trend is removed.
   * @param order the order of the polynomial fit; must be -1, 0, 1, or 2.
   */
  public void setPolyTrend(int order) {
    Check.argument(-1<=order,"-1<=order");
    Check.argument(order<=2,"order<=2");
    if (_order!=order) {
      if (_trend!=null) {
        _trend.restore(_f,_x1,_x2);
        _trend = null;
      }
      if (order!=-1) {
        _trend = new PolyTrend2(order,_f,_x1,_x2);
        _trend.detrend(_f,_x1,_x2);
        //dump(_f);
      }
      _order = order;
      _w = null;
    }
  }

  /**
   * Returns a value interpolated at the specified point.
   * @param x1 the x1 coordinate of the point.
   * @param x2 the x2 coordinate of the point.
   * @return the interpolated value.
   */
  public float interpolate(float x1, float x2) {
    ensureWeights();
    double f = 0.0;
    double x1i = x1;
    double x2i = x2;
    for (int k=0; k<_n; ++k) {
      double x1k = _x1[k];
      double x2k = _x2[k];
      f += _w[k]*g(x1k,x2k,x1i,x2i);
    }
    float ff = (float)f;
    if (_trend!=null)
      ff = _trend.restore(ff,x1,x2);
    return ff;
  }

  /**
   * Returns an array of interpolated values sampled on a grid.
   * @param s1 the sampling of n1 x1 coordinates.
   * @param s2 the sampling of n2 x2 coordinates.
   * @return array[n2][n1] of interpolated values.
   */
  public float[][] interpolate(Sampling s1, Sampling s2) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    float[][] f = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      float x2 = (float)s2.getValue(i2);
      for (int i1=0; i1<n1; ++i1) {
        float x1 = (float)s1.getValue(i1);
        f[i2][i1] = interpolate(x1,x2);
      }
    }
    return f;
  }

  /**
   * Gets the weights that scale the basis for each known sample.
   * @return array of weights; by copy, not by reference.
   */
  public float[] getWeights() {
    ensureWeights();
    return copy(_w);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Basis _basis; // the radial basis function
  private int _n; // number of scattered samples
  private float[] _f,_x1,_x2; // scattered samples f(x1,x2)
  private float[] _w; // weights for radial basis functions
  private double _m11,_m12,_m22; // metric tensor elements
  private boolean _mt; // true iff using a metric tensor
  private PolyTrend2 _trend; // polynomial trend; null, if none
  private int _order = -1; // order of poly trend; -1, if none

  private double g(double x1a, double x2a, double x1b, double x2b) {
    return _basis.evaluate(r(x1a,x2a,x1b,x2b));
  }

  private double r(double x1a, double x2a, double x1b, double x2b) {
    return r(x1a-x1b,x2a-x2b);
  }

  private double r(double d1, double d2) {
    return _mt ?
      sqrt(_m11*d1*d1+2.0*_m12*d1*d2+_m22*d2*d2) :
      hypot(d1,d2);
  }

  private void ensureWeights() {
    if (_w!=null)
      return;
    DMatrix a = new DMatrix(_n,_n);
    DMatrix b = new DMatrix(_n,1);
    for (int i=0; i<_n; ++i) {
      double x1i = _x1[i];
      double x2i = _x2[i];
      for (int j=0; j<_n; ++j) {
        double x1j = _x1[j];
        double x2j = _x2[j];
        a.set(i,j,g(x1i,x2i,x1j,x2j));
      }
      b.set(i,0,_f[i]);
    }
    DMatrixLud lud = new DMatrixLud(a);
    DMatrix w = lud.solve(b);
    _w = new float[_n];
    for (int i=0; i<_n; ++i)
      _w[i] = (float)w.get(i,0);
  }
}
