/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.DBL_EPSILON;
import static edu.mines.jtk.util.MathPlus.abs;

/**
 * Brent's algorithm for finding a zero of a function of one variable.
 * Searches an interval [a,b] for an argument x for which a function
 * f(x) = 0.
 * <p>
 * This algorithm uses a combination of bisection and inverse linear and/or
 * quadratic interpolation. Convergence is never much slower than that for 
 * bisection. If f(x) has a continuous second derivative near a simple zero, 
 * then the algorithm will tend towards superlinear convergence of order at
 * least 1.618.
 * <p>
 * Let xzero be the argument x that results from a search for the zero.
 * That search is terminated when the difference between xzero and the 
 * true zeroing argument x is less than tol+4*EPS*abs(xzero), where
 * tol is a specified tolerance and EPS is DBL_EPSILON (approximately 
 * 1.0e-16), machine epsilon for IEEE double precision arithmetic.
 * <p>
 * This implementation is adapted from the Fortran subroutine ZEROIN, by
 * Forsythe, G.E., Malcolm, M.A., and Moler, C.B. 1977, Computer Methods 
 * for Mathematical Computations, Prentice Hall. That Fortran function is, 
 * in turn, a translation of the Algol 60 program by Brent, R., 1973, 
 * Algorithms for Minimization Without Derivatives, Prentice Hall.
 * @author Dave Hale, Colorado School of Mines
 * @version 2001.07.10, 2006.07.12
 */
public class BrentZeroFinder {

  /**
   * A function f(x) of one variable x.
   */
  public interface Function {

    /**
     * Returns the value of this function f(x) for the specified argument x.
     * @param x the function argument x.
     * @return the function value f(x).
     */
    public double evaluate(double x);
  }

  /**
   * Constructs a zero finder for the specified function.
   * @param f the function.
   */
  public BrentZeroFinder(Function f) {
    _f = f;
  }

  /**
   * Returns the function value f(x) for the specified argument x.
   * @param x the argument at which to evaluate f(x).
   * @return the function value f(x).
   */
  public double f(double x) {
    return _f.evaluate(x);
  }

  /**
   * Finds a zero within the specified search interval [a,b].
   * The function values f(a) and f(b) must not have the same sign.
   * @param a the lower limit of the search interval.
   * @param b the upper limit of the search interval.
   * @param tol the accuracy with which to find the zero.
   * @return the abscissa x for which f(x) is approximately zero.
   */
  public double findZero(double a, double b, double tol) {
    double fa = _f.evaluate(a);
    double fb = _f.evaluate(b);
    return findZero(a,fa,b,fb,tol);
  }

  /**
   * Finds a zero within the specified search interval [a,b], beginning
   * with specified function values f(a) and f(b), which must not have 
   * the same sign.
   * @param a the lower limit of the search interval.
   * @param fa the function f(x) evaluated at x = a.
   * @param b the upper limit of the search interval.
   * @param fb the function f(x) evaluated at x = b.
   * @param tol the accuracy with which to find the zero.
   * @return the abscissa x for which f(x) is approximately zero.
   */
  public double findZero(
    double a, double fa, double b, double fb, double tol) 
  {
    Check.argument(a<b, "Invalid Search Interval");
    Check.argument((fa<=0.0 && fb>=0.0) || (fa>=0.0 && fb<=0.0), 
		    "Function values must not have same sign");
    Check.argument(tol>0.0, "Accuracy must be greater than zero");

    // This algorithm maintains three points a, b, and c such that:
    // a = the previous best estimate of the root,
    // b = the current best estimate of the root, and
    // c = some previous estimate of the root for which sgn[f(c)] != sgn[f(b)].
    // In other words, at all times, the points b and c bracket the root.
    double c = a;
    double fc = fa;

    // While not yet converged, ...
    for (;;) {

      // The previous step.
      double e = b-a;

      // If necessary, swap so that b is the current best estimate.
      if (abs(fc)<abs(fb)) {
        a = b;  fa = fb;
        b = c;  fb = fc;
        c = a;  fc = fa;
      }

      // Simple bisection step.
      double d = 0.5*(c-b);

      // Test for convergence.
      double dtol = 2.0*EPS*abs(b)+0.5*tol;
      if (abs(d)<=dtol || fb==0.0) 
        return b;

      // If inverse interpolation may be acceptable.
      if (abs(e)>=dtol && abs(fa)>abs(fb)) {

        // If used, the inverse interpolation step will be p/q.
        double p,q;

        // If only two distinct points, try inverse linear interpolation;
        // otherwise, try inverse quadratic interpolation.
        if (a==c) {
          double s = fb/fa;
          p = 2.0*d*s;
          q = 1.0-s;
        } else {
          double r = fb/fc;
          double s = fb/fa;
          q = fa/fc;
          p = s*(2.0*d*q*(q-r)-(b-a)*(r-1.0));
          q = (q-1.0)*(r-1.0)*(s-1.0);
        }
        if (p>0.0) {
          q = -q;
        } else {
          p = -p;
        }

        // If the inverse interpolation step is acceptable, use it;
        if (2.0*p<3.0*d*q-abs(dtol*q) && p<abs(0.5*e*q))
          d = p/q;
      }

      // The step must not be less than the tolerance.
      if (abs(d)<dtol)
        d = (d>0.0)?dtol:-dtol;

      // Save the previous best estimate and then step to the new one.
      a  = b;  fa = fb;
      b += d;  fb = _f.evaluate(b);

      // If necessary, swap so that b and c bracket the root.
      if ((fb>0.0 && fc>0.0) || (fb<0.0 && fc<0.0)) {
        c = a;  fc = fa;
      }
    }
  }

  private Function _f;

  private static final double EPS = DBL_EPSILON;
}
