package com.lgc.idh.util;

import com.lgc.idh.lang.Check;
import com.lgc.idh.lang.M;

/**
 * Finds a zero of a univariate function f(x), without derivatives, using
 * Brent's algorithm. (Brent, Richard P., 1973, Algorithms for minimization 
 * without derivatives: Prentice Hall.)
 * <p>
 * This algorithm uses a combination of bisection and inverse linear and/or
 * quadratic interpolation. Convergence is never much slower than that for 
 * bisection. If f(x) has a continuous second derivative near a simple zero, 
 * then the algorithm will tend towards superlinear convergence of order at
 * least 1.618.
 * <p>
 * For a user-specified tolerance tol, this algorithm finds a zero x within 
 * an interval of uncertainty less than 4*DBL_EPSILON*abs(x) + tol.
 * <p>
 * This implementation of Brent's algorithm is adapted from the FORTRAN 
 * subroutine ZEROIN, in G.Forsythe, M.Malcolm, C.Moler, 1977, Computer 
 * methods for mathematical computations: Prentice Hall. That subroutine 
 * is adapted from Brent's Algol 60 procedure zero.
 * @author Dave Hale
 * @version 2001.07.10
 */
public class BrentZeroFinder {

  /**
   * Interface for a function f(x), used during the search for a zero.
   */
  public interface Function {

    /**
     * Evaluates the function f(x) at the specified x.
     * @param x the x coordinate at which to evaluate f(x).
     * @return the function value f(x).
     */
    double eval(double x);
  }

  /**
   * Constructs a new zero finder for the specified function.
   * @param func the function.
   */
  public BrentZeroFinder(Function func) {
    _func = func;
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
    double fa = _func.eval(a);
    double fb = _func.eval(b);
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
    Check.argument(a<b);
    Check.argument((fa<=0.0 && fb>=0.0) || (fa>=0.0 && fb<=0.0));
    Check.argument(tol>0.0);

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
      double dtol = 2.0*M.DBL_EPSILON*abs(b)+0.5*tol;
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
      b += d;  fb = _func.eval(b);

      // If necessary, swap so that b and c bracket the root.
      if ((fb>0.0 && fc>0.0) || (fb<0.0 && fc<0.0)) {
        c = a;  fc = fa;
      }
    }
  }

  private Function _func;

  private static final double abs(double a) {
    return (a>=0)?a:-a;
  }
}
