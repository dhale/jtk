/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Searches along a line for a minimum of a continuously differentiable 
 * function of one or more variables. Uses values f(s) of the function and 
 * its directional-derivative f'(s) (the dot product of a search-direction 
 * vector and the function's gradient) to find a step s that minimizes the 
 * function along the line constraining the search. The search assumes that 
 * f'(0) &lt; 0, and searches for a positive s that minimizes f(s).
 * <p>
 * This implementation uses Mor'e and Thuente's algorithm with guaranteed 
 * sufficient decrease. It iteratively searches for a step s that at each
 * iteration satisfies both a sufficient-decrease condition and a curvature 
 * condition.
 * <p>
 * The sufficient decrease condition (1) is
 * <pre>
 *   f(s) &lt;= f(0) + ftol*f'(0)*s,
 * </pre>
 * and the curvature condition (2) is
 * <pre>
 *   abs(f'(s)) &lt;= gtol*abs(f'(0)),
 * </pre>
 * for specified non-negative tolerances ftol and gtol.
 * <p>
 * Condition (1) ensures a sufficient decrease in the function f(s),
 * provided that s is not too small. Condition (2) ensures that s is not 
 * too small, and usually guarantees that s is near a local minimizer of 
 * f. It is called a curvature condition because it implies that
 * <pre>
 *   f'(s) - f'(0) &gt; (1-gtol)*abs(f'(0)),
 * </pre>
 * so that the average curvature of f on the interval (0,s) is positive.
 * <p>
 * The curvature condition (2) is especially important in a quasi-Newton
 * method for function minimization, because it guarantees that a
 * positive-definite quasi-Newton update is possible. If ftol is less than 
 * gtol and the function f(s) is bounded below, then there exists a step s 
 * that satisfies both conditions. If such a step cannot be found, then
 * only the first sufficient-decrease condition (1) is satisfied.
 * <p>
 * Mor'e and Thuente's algorithm initially choses an interval [sa,sb] that 
 * contains a minimizer of a modified function 
 * <pre>
 *   h(s) = f(s) - f(0) - ftol*f'(0)*s
 * </pre>
 * If h(s) &lt;= 0 and f'(s) &gt;= 0 for some step s, then the interval 
 * [a,b] is chosen so that it contains a minimizer of f.
 * <p>
 * If no step can be found that satisfies both conditions, then the 
 * algorithm ends unconverged. In this case the step s satisifies only 
 * the sufficient-decrease condition.
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.09.02
 */
public class LineSearch {

  /**
   * The function to be minimized.
   */
  public interface Function {
    // TODO: combine eval of f and f'.

    /**
     * Evaluates the function for a specified step.
     * @param s the step.
     * @return the value f(s).
     */
    double f(double s);

    /**
     * Evaluates the derivative for a specified step.
     * @param s the step.
     * @return the derivative g(s) = f'(s).
     */
    double g(double s);
  }

  /**
   * Line search converged. Conditions (1) and (2) are satisifed.
   */
  public static final int CONVERGED = 1;

  /**
   * Line search ended because the step equals a specified minimum.
   */
  public static final int SMIN = 2;

  /**
   * Line search ended because the step equals a specified maximum.
   */
  public static final int SMAX = 3;

  /**
   * Line search ended because the step has been resolved to within
   * a specified tolerance.
   */
  public static final int STOL = 4;

  /**
   * Line search failed due to rounding error.
   */
  public static final int FAILED = 5;

  /**
   * The result of a line search.
   */
  public class Result {

    /**
     * The step s.
     */
    public final double s;

    /**
     * The value of the function f(s).
     */
    public final double f;

    /**
     * The value of the derivative f'(s).
     */
    public final double g;

    /**
     * The condition that ended the search.
     */
    public final int ended;

    /**
     * The number of function and derivative evaluations.
     */
    public final int neval;

    /**
     * Determines whether the line search converged.
     * @return true, if converged; false, otherwise.
     */
    public boolean converged() {
      return ended==CONVERGED;
    }

    private Result(double s, double f, double g, int ended, int neval) {
      this.s = s;
      this.f = f;
      this.g = g;
      this.ended = ended;
      this.neval = neval;
    }
  }

  /**
   * Constructs a line search with specified tolerances.
   * @param stol non-negative relative tolerance for an acceptable step.
   *  The search ends if the search interval [slo,shi] is smaller than 
   *  this tolerance times the upper bound shi.
   * @param ftol non-negative tolerance for sufficient-decrease condition (1).
   * @param gtol non-negative tolerance for curvature condition (2).
   */
  public LineSearch(Function func, double stol, double ftol, double gtol) {
    Check.argument(stol>=0.0,"stol>=0.0");
    Check.argument(ftol>=0.0,"ftol>=0.0");
    Check.argument(gtol>=0.0,"gtol>=0.0");
    _func = func;
    _stol = stol;
    _ftol = ftol;
    _gtol = gtol;
  }

  private static final double SLO_FACTOR = 1.1;
  private static final double SHI_FACTOR = 4.0;

  /**
   * Searches for a minimizing step.
   * @param s current estimate of a satisfactory step. Must be positive. 
   * @param f value f(0) of the function f at s = 0.
   * @param g value f'(0) of the derivative of f at s = 0.
   */
  public Result search(
    double s, double f, double g, double smin, double smax) 
  {
    Check.argument(smin>=0.0,"smin>=0.0");
    Check.argument(smin<=smax,"smin<=smax");
    Check.argument(smin<=s,"smin<=s");
    Check.argument(s<=smax,"s<=smax");
    Check.argument(g<0.0,"g<0.0");

    StepInterval si = new StepInterval();

    boolean stage1 = true;
    boolean bracketed = false;
    double finit = f;
    double ginit = g;
    double gtest = _ftol*ginit;
    double width = smax-smin;
    double widthOld = 2.0*width;

    double sa = 0.0;
    double fa = finit;
    double ga = ginit;
    double sb = 0.0;
    double fb = finit;
    double gb = ginit;
    double slo = 0.0;
    double shi = s*(1.0+SHI_FACTOR);

    f = _func.f(s); 
    g = _func.g(s); 
    int neval = 1;
    int ended = 0;
    while (ended==0) {

      // If h(s) <= 0 and f'(s) >= 0 for some step, then begin stage 2.
      double ftest = finit+s*gtest;
      if (stage1 && f<=ftest && g>=0.0)
        stage1 = false;

      // If done searching (for whatever reason), ...
      if (bracketed && (s<=slo || s>=shi)) {
        ended = FAILED;
      } else if (bracketed && shi-slo <= _stol*shi) {
        ended = STOL;
      } else if (s==smax && f<=ftest && g<=gtest) {
        ended = SMAX;
      } else if (s==smin && (f>ftest || g>=gtest)) {
        ended = SMIN;
      } else if (f<=ftest && abs(g)<=_gtol*(-ginit)) {
        ended = CONVERGED;
      }
      
      // Else, if still searching, ...
      else {

        // During the first stage, use a modified function to compute 
        // the step if a lower function value has been obtained, but 
        // the decrease is insufficient.
        if (stage1 && f<=fa && f>ftest) {

          // Modify function and derivative values.
          double fm = f-s*gtest;
          double fam = fa-sa*gtest;
          double fbm = fb-sb*gtest;
          double gm = g-gtest;
          double gam = ga-gtest;
          double gbm = gb-gtest;

          // Update sa, sb, and compute the new step s.
          si.sa = sa;  si.fa = fam;  si.ga = gam;
          si.sb = sb;  si.fb = fbm;  si.gb = gbm;
          si.bracketed = bracketed;
          s = updateStep(s,fm,gm,slo,shi,si);
          sa = si.sa;  fam = si.fa;  gam = si.ga;
          sb = si.sb;  fbm = si.fb;  gbm = si.gb;
          bracketed = si.bracketed;

          // Unmodify function and derivative values.
          fa = fam+sa*gtest;
          fb = fbm+sb*gtest;
          ga = gam+gtest;
          gb = gbm+gtest;
        } 
        
        // Otherwise, use the unmodified function f.
        else {

          // Update sa, sb, and compute the new step s.
          si.sa = sa;  si.fa = fa;  si.ga = ga;
          si.sb = sb;  si.fb = fb;  si.gb = gb;
          si.bracketed = bracketed;
          s = updateStep(s,f,g,slo,shi,si);
          sa = si.sa;  fa = si.fa;  ga = si.ga;
          sb = si.sb;  fb = si.fb;  gb = si.gb;
          bracketed = si.bracketed;
        }

        // Decide if a bisection step is needed.
        if (bracketed) {
          if (abs(sb-sa)>=0.66*widthOld) 
            s = sa+0.5*(sb-sa);
          widthOld = width;
          width = abs(sb-sa);
        }

        // Set the minimum and maximum steps allowed for s.
        if (bracketed) {
          slo = min(sa,sb);
          shi = max(sa,sb);
        } else {
          slo = s+SLO_FACTOR*(s-sa);
          shi = s+SHI_FACTOR*(s-sa);
        }

        // Force the step to be within specified bounds.
        s = max(s,smin);
        s = min(s,smax);

        // If no progress, step s is best found so far.
        if ((bracketed && (s<=slo || s>=shi)) ||
            (bracketed && shi-slo<=_stol*shi)) 
          s = sa;
      }

      // Evaluate function f(s) and derivative f'(s).
      f = _func.f(s); 
      g = _func.g(s); 
      ++neval;
    }

    return new Result(s,f,g,ended,neval);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Function _func;
  private double _stol,_ftol,_gtol;

  private static class StepInterval {
    double sa,fa,ga;
    double sb,fb,gb;
    boolean bracketed;
  }

  // Updates a specified step interval, and returns an updated step.
  private double updateStep(
    double sp, double fp, double gp, 
    double smin, double smax, StepInterval si) 
  {
    double sa = si.sa;
    double fa = si.fa;
    double ga = si.ga;
    double sb = si.sb;
    double fb = si.fb;
    double gb = si.gb;
    boolean bracketed = si.bracketed;

    double sgng = gp*(ga/abs(ga));
    double spf = sp;

    // First case: A higher function value. The minimum is bracketed.
    // If the cubic step is closer to sa than the quadratic step, the
    // cubic step is taken, otherwise the average of the cubic and
    // quadratic steps is taken.
    if (fp>fa) {
      double theta = 3.0*(fa-fp)/(sp-sa)+ga+gp;
      double s = max(abs(theta),abs(ga),abs(gp));
      double gamma = s*sqrt((theta/s)*(theta/s)-(ga/s)*(gp/s));
      if (sp<sa) 
        gamma = -gamma;
      double p = (gamma-ga)+theta;
      double q = ((gamma-ga)+gamma)+gp;
      double r = p/q;
      double spc = sa+r*(sp-sa);
      double spq = sa+(0.5*(ga/((fa-fp)/(sp-sa)+ga)))*(sp-sa);
      if (abs(spc-sa)<abs(spq-sa)) {
        spf = spc;
      } else {
        spf = spc+0.5*(spq-spc);
      }
      bracketed = true;
    }

    // Second case: A lower function value and derivatives of opposite
    // sign. The minimum is bracketed. If the cubic step is farther from
    // sp than the secant step, the cubic step is taken, otherwise the
    // secant step is taken.
    else if (sgng<0.0) {
      double theta = 3.0*(fa-fp)/(sp-sa)+ga+gp;
      double s = max(abs(theta),abs(ga),abs(gp));
      double gamma = s*sqrt((theta/s)*(theta/s)-(ga/s)*(gp/s));
      if (sp>sa)
        gamma = -gamma;
      double p = (gamma-gp)+theta;
      double q = ((gamma-gp)+gamma)+ga;
      double r = p/q;
      double spc = sp+r*(sa-sp);
      double spq = sp+(gp/(gp-ga))*(sa-sp);
      if (abs(spc-sp)>abs(spq-sp)) {
        spf = spc;
      } else {
        spf = spq;
      }
      bracketed = true;
    }

    // Third case: A lower function value, derivatives of the same sign,
    // and the magnitude of the derivative decreases.
    else if (abs(gp)<abs(ga)) {

      // The cubic step is computed only if the cubic tends to infinity
      // in the direction of the step or if the minimum of the cubic
      // is beyond sp. Otherwise the cubic step is defined to be the
      // secant step.
      double theta = 3.0*(fa-fp)/(sp-sa)+ga+gp;
      double s = max(abs(theta),abs(ga),abs(gp));

      // The case gamma = 0 arises only if the cubic does not tend
      // to infinity in the direction of the step.
      double gamma = s*sqrt(max(0.0,(theta/s)*(theta/s)-(ga/s)*(gp/s)));
      if (sp>sa) 
        gamma = -gamma;
      double p = (gamma-gp)+theta;
      double q = (gamma+(ga-gp))+gamma;
      double r = p/q;
      double spc;
      if (r<0.0 && gamma!=0.0) {
        spc = sp + r*(sa-sp);
      } else if (sp>sa) {
        spc = smax;
      } else {
        spc = smin;
      }
      double spq = sp+(gp/(gp-ga))*(sa-sp);

      // If a minimizer has been bracketed, ... 
      if (bracketed) {

        // If the cubic step is closer to sp than the secant step, the
        // cubic step is taken, otherwise the secant step is taken.
        if (abs(spc-sp)<abs(spq-sp)) {
          spf = spc;
        } else {
          spf = spq;
        }
        if (sp>sa) {
          spf = min(sp+0.66*(sb-sp),spf);
        } else {
          spf = max(sp+0.66*(sb-sp),spf);
        }
      }
      
      // Else, if a minimizer has not been bracketed, ...
      else {

        // If the cubic step is farther from sp than the secant step, 
        // the cubic step is taken, otherwise the secant step is taken.
        if (abs(spc-sp)>abs(spq-sp)) {
          spf = spc;
        } else {
          spf = spq;
        }
        spf = min(smax,spf);
        spf = max(smin,spf);
      }
    }

    // Fourth case: A lower function value, derivatives of the same sign,
    // and the magnitude of the derivative does not decrease. If the
    // minimum is not bracketed, the step is either smin or smax,
    // otherwise the cubic step is taken.
    else {
      if (bracketed) {
        double theta = 3.0*(fp-fb)/(sb-sp)+gb+gp;
        double s = max(abs(theta),abs(gb),abs(gp));
        double gamma = s*sqrt((theta/s)*(theta/s)-(gb/s)*(gp/s));
        if (sp>sb)
          gamma = -gamma;
        double p = (gamma-gp)+theta;
        double q = ((gamma-gp)+gamma)+gb;
        double r = p/q;
        double spc = sp+r*(sb-sp);
        spf = spc;
      } else if (sp>sa) {
        spf = smax;
      } else {
        spf = smin;
      }
    }

    // Update the step interval.
    if (fp>fa) {
      si.sb = sp;
      si.fb = fp;
      si.gb = gp;
    } else {
      if (sgng<0.0) {
        si.sb = sa;
        si.fb = fa;
        si.gb = ga;
      }
      si.sa = sp;
      si.fa = fp;
      si.ga = gp;
    }
    si.bracketed = bracketed;

    // Return new step.
    return spf;
  }
}
