package com.lgc.idh.util;

import com.lgc.idh.lang.Check;
import com.lgc.idh.lang.M;

/**
 * Finds a local minimum of a univariate function f(x), without derivatives,
 * using Brent's algorithm. (Brent, Richard P., 1973, Algorithms for 
 * minimization without derivatives: Prentice Hall.)
 * <p>
 * This algorithm uses a combination of golden section search and quadratic 
 * interpolation. Convergence is never much slower than that for the golden
 * section search, which decreases the interval of uncertainty by a factor 
 * of roughly 0.618 for each evaluation of f(x). This is linear convergence.
 * If f(x) has a continuous second derivative that is positive at the minimum, 
 * then convergence is superlinear, usually of order roughly 1.324.
 * <p>
 * The function is never evaluated at two points x closer together than
 * sqrt(DBL_EPSILON)*abs(x) + tol/3, where tol is a user-specified tolerance.
 * The algorithm finds the minimizing x within an interval of uncertainty
 * less than 3*sqrt(DBL_EPSILON)*abs(x) + tol.
 * <p>
 * If f(x) has no local minimum within the search range [a,b], then the 
 * the algorithm returns a or b, depending on whether f(a) is less than or 
 * greater than f(b). If f(x) is constant in [a,b], then the location of 
 * the minimum is undefined.
 * <p>
 * This implementation of Brent's algorithm is adapted from the FORTRAN 
 * subroutine FMIN, in G.Forsythe, M.Malcolm, C.Moler, 1977, Computer 
 * methods for mathematical computations: Prentice Hall. That subroutine 
 * is adapted from Brent's Algol 60 procedure localmin.
 * @author Dave Hale
 * @version 2003.07.15
 */
public class BrentMinFinder {

  /**
   * Interface for a function f(x), used during the search for a minimum.
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
   * Constructs a new minimum finder for the specified function.
   * @param func the function.
   */
  public BrentMinFinder(Function func) {
    _func = func;
  }

  /**
   * Finds a local minimum within the specified search interval [a,b].
   * @param a the lower limit of the search interval.
   * @param b the upper limit of the search interval.
   * @param tol the accuracy with which to find the minimum.
   * @return the abscissa x for which f(x) is a local minimum.
   */
  public double findMin(double a, double b, double tol) {
    Check.argument(a<b,"a<b");
    Check.argument(tol>0.0,"tol>0");

    // First golden section step.
    double x = a+C*(b-a);
    double v = x;
    double w = x;
    double fx = _func.eval(x);
    double fv = fx;
    double fw = fx;
    double d = 0.0;
    double e = 0.0;

    // While not converged, ...
    for (;;) {
      double xm = 0.5*(a+b);
      double tol1 = SMALL*Math.abs(x)+THIRD*tol;
      double tol2 = 2.0*tol1;
      double u,fu;

      // Check for convergence.
      if (Math.abs(x-xm)<=tol2-0.5*(b-a))
        return x;
      
      // If a quadratic step may be acceptable, test it;
      // otherwise, take a golden section step.
      if (Math.abs(e)>tol1) {

        // If used, the quadratic step will be p/q.
        double r = (x-w)*(fx-fv);
        double q = (x-v)*(fx-fw);
        double p = (x-v)*q-(x-w)*r;
        q = 2.0*(q-r);
        if (q>0.0) {
          p = -p;
        } else {
          q = -q;
        }
        r = e;
        e = d;

        // If the quadratic step is acceptable, take it (but stay away from
        // a and b); otherwise, take a golden section step.
        if (Math.abs(p)<Math.abs(0.5*q*r) && p>q*(a-x) && p<q*(b-x)) {
          d = p/q;
          u = x+d;
          if (u-a<tol2 || b-u<tol2) d = (xm>=x)?tol1:-tol1;
        } else {
          e = (x>=xm)?a-x:b-x;
          d = C*e;
        }
      } else {
        e = (x>=xm)?a-x:b-x;
        d = C*e;
      }

      // Evaluate f(u) for u not too close to x.
      if (Math.abs(d)<tol1)
        d = (d>=0.0)?tol1:-tol1;
      u = x+d;  fu = _func.eval(u);

      // Update a, b, v, w, and x.
      if (fu<=fx) {
        if (u>=x) {
          a = x;
        } else {
          b = x;
        }
        v = w;  fv = fw;
        w = x;  fw = fx;
        x = u;  fx = fu;
      } else {
        if (u<x) {
          a = u;
        } else {
          b = u;
        }
        if (fu<=fw || w==x) {
          v = w;  fv = fw;
          w = u;  fw = fu;
        } else if (fu<=fv || v==x || v==w) {
          v = u;  fv = fu;
        }
      }
    }
  }

  private Function _func;
  
  // Constants.
  private static final double C = (3.0-Math.sqrt(5.0))/2.0;
  private static final double SMALL = Math.sqrt(M.DBL_EPSILON);
  private static final double THIRD = 1.0/3.0;

  private static final double abs(double a) {
    return (a>=0)?a:-a;
  }
}
