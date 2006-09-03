/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import static edu.mines.jtk.util.MathPlus.*;

/**
 * Brent's algorithm for finding the minimum of a function of one variable.
 * Searches an interval [a,b] for an argument x that minimizes a function
 * f(x).
 * <p>
 * Brent's algorithm uses a combination of golden section search and
 * successive parabolic interpolation. Convergence is never much slower
 * than that for a Fibonacci search. If the function f(x) has a continuous
 * second derivative that is positive at the minimum (which is not at the
 * endpoints a or b, then convergence is superlinear, and usually of the 
 * order of about 1.324.
 * <p>
 * Let xmin be the argument x that results from a search for the minimum.
 * That search is terminated when the difference between xmin and the 
 * true minimizing argument x is less than a specified tolerance. The 
 * function f(x) is never evaluated at two points closer together than 
 * EPS*abs(xmin)+tol/3, where EPS is approximately 1.0e-8, the square 
 * root of machine epsilon for IEEE double precision arithmetic, and
 * tol is a specified tolerance.
 * <p>
 * If f(x) is a unimodal function and if the computed values of f(x) are 
 * always unimodal for arguments x separated by at least EPS*abs(x)+tol/3, 
 * then xmin approximates the global minimum of f(x) on the interval [a,b] 
 * with an error less than 3*EPS*abs(xmin)+tol. If f(x) is not unimodal, 
 * then xmin may approximate a local, but perhaps not global, minimum to 
 * the same accuracy.
 * <p>
 * This implementation is adapted from the Fortran function FMIN, by 
 * Forsythe, G.E., Malcolm, M.A., and Moler, C.B. 1977, Computer Methods 
 * for Mathematical Computations, Prentice Hall. That Fortran function is, 
 * in turn, a translation of the Algol 60 program by Brent, R., 1973, 
 * Algorithms for Minimization Without Derivatives, Prentice Hall.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.09.15
 */
public class BrentMinFinder {

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
   * Constructs a min finder for the specified function.
   * @param f the function to be minimized.
   */
  public BrentMinFinder(Function f) {
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
   * Returns an x in the specified interval for which f(x) is a minimum.
   * @param a the smallest value of x in the interval.
   * @param b the largest value of x in the interval.
   * @param tol the search tolerance; see notes above.
   * @return x for which f(x) is a minimum.
   */
  public double findMin(double a, double b, double tol) {
    double x = a+GSI*(b-a);
    double v = x;
    double w = x;
    double fx = f(x);
    double fv = fx;
    double fw = fx;
    double d = 0.0;
    double e = 0.0;

    // While not converged, ...
    for (double 
      xm = 0.5*(a+b), tol1 = EPS*abs(x)+tol/3.0, tol2 = 2.0*tol1;
      abs(x-xm) > tol2-0.5*(b-a);
      xm = 0.5*(a+b), tol1 = EPS*abs(x)+tol/3.0, tol2 = 2.0*tol1) {

      // Is a golden section step absolutely necessary?
      boolean gsstep = abs(e)<=tol1;

      // If not, try a parabolic step.
      if (!gsstep) {

        // Fit parabola.
        double r = (x-w)*(fx-fv);
        double q = (x-v)*(fx-fw);
        double p = (x-v)*q-(x-w)*r;
        q = 2.0*(q-r);
        if (q>0.0)
          p = -p;
        q = abs(q);
        r = e;
        e = d;

        // If parabolic step is acceptable, compute it.
        // Must not evaluate f(x) too close to a or b.
        if (abs(p)<abs(0.5*q*r) && p>q*(a-x) && p<q*(b-x)) {
          d = p/q;
          double u = x+d;
          if ((u-a)<tol2 || (b-u)<tol2)
            d = (xm>=x)?tol1:-tol1;

        // Otherwise, must take a golden section step.
        } else {
          gsstep = true;
        }
      }

      // If necessary, compute a golden section step.
      if (gsstep) {
        e = (x>=xm)?a-x:b-x;
        d = GSI*e;
      }

      // Take the computed step. Must not evaluate f(x) too close to x.
      double u = x+(abs(d)>=tol1?d:d>=0.0?tol1:-tol1);
      double fu = f(u);

      // Update a, b, v, w, and x.
      if (fu<=fx) {
        if (u>=x)
          a = x;
        else
          b = x;
        v = w;
        w = x;
        x = u;
        fv = fw;
        fw = fx;
        fx = fu;
      } else {
        if (u<x)
          a = u;
        else
          b = u;
        if (fu<=fw || w==x) {
          v = w;
          w = u;
          fv = fw;
          fw = fu;
        } else if (fu<=fv || v==x || v==w) {
          v = u;
          fv = fu;
        }
      }
    }
    return x;
  }

  // Squared inverse of the golden ratio.
  private static final double GSI = 0.5*(3.0-sqrt(5.0));

  // Square-root of machine epsilon.
  private static final double EPS = sqrt(DBL_EPSILON);

  private Function _f; // function to be minimized
}
