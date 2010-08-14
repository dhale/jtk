/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.opt.LineSearch}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.09.02
 */
public class LineSearchTest extends TestCase {

  public static void main(String[] args) {
    TestSuite suite = new TestSuite(LineSearchTest.class);
    junit.textui.TestRunner.run(suite);
  }

  /**
   * Mor'e and Thuente's test function 1.
   */
  public void testMT1() {
    trace("Function 1");
    LineSearch.Function func = new LineSearch.Function() {
      public double[] evaluate(double s) {
        double t = 1.0/(s*s+beta);
        double f = -s*t;
        double g = (s*s-beta)*t*t;
        return new double[]{f,g};
      }
      private double beta = 2.0;
    };
    LineSearch ls = new LineSearch(func,1.0e-10,1.0e-3,1.0e-1);
    double[] se = new double[]{ 1.4, 1.4, 10.0, 37.0};
    double[] ge = new double[]{-9.2e-3, 4.7e-3, 9.4e-3, 7.3e-4};
    int[] ne = new int[]{7, 4, 2, 5};
    testMT(func,ls,se,ge,ne);
  }

  /**
   * Mor'e and Thuente's test function 2.
   */
  public void testMT2() {
    trace("Function 2");
    LineSearch.Function func = new LineSearch.Function() {
      public double[] evaluate(double s) {
        double t = s+beta2;
        double f = t*t*t*t*t-2.0*t*t*t*t;
        double g = 5.0*t*t*t*t-8.0*t*t*t;
        return new double[]{f,g};
      }
      private double beta2 = 4.0e-3;
    };
    LineSearch ls = new LineSearch(func,1.0e-10,1.0e-1,1.0e-1);
    double[] se = new double[]{1.6, 1.6, 1.6, 1.6};
    double[] ge = new double[]{7.1e-9, 1.0e-10, -5.0e-9, -2.3e-8};
    int[] ne = new int[]{13, 9, 9, 12};
    testMT(func,ls,se,ge,ne);
  }

  /**
   * Mor'e and Thuente's test function 3.
   */
  public void testMT3() {
    trace("Function 3");
    LineSearch.Function func = new LineSearch.Function() {
      public double[] evaluate(double s) {
        int nwig = 39;
        double f,g;
        if (s<1.0-beta3) {
          f = 1.0-s;
          g = -1.0;
        } else if (s<1.0+beta3) {
          f = (s-1.0)*(s-1.0)/(2.0*beta3)+beta3/2.0;
          g = (s-1.0)/beta3;
        } else {
          f = s-1.0;
          g = 1.0;
        }
        double t1 = 2.0*nwig*atan(1.0);
        double t2 = (1.0-beta3)/t1;
        f += t2*sin(t1*s);
        g += (1.0-beta3)*cos(t1*s);
        return new double[]{f,g};
      }
      private double beta3 = 1.0e-2;
    };
    LineSearch ls = new LineSearch(func,1.0e-10,1.0e-1,1.0e-1);
    double[] se = new double[]{1.0, 1.0, 1.0, 1.0};
    double[] ge = new double[]{-5.1e-5, -1.9e-4, -2.0e-6, -1.6e-5};
    int[] ne = new int[]{13, 13, 11, 14};
    testMT(func,ls,se,ge,ne);
  }

  /**
   * Mor'e and Thuente's test function 4.
   */
  public void testMT4() {
    trace("Function 4");
    LineSearch.Function func = new LineSearch.Function() {
      public double[] evaluate(double s) {
        double a1 = 1.0e-3;
        double a2 = 1.0e-3;
        double t1 = sqrt(1.0+a1*a1)-a1;
        double t2 = sqrt(1.0+a2*a2)-a2;
        double f = t1*sqrt((1.0-s)*(1.0-s)+a2*a2)+t2*sqrt(s*s+a1*a1);
        double g = 
          -t1*((1.0-s)/sqrt((1.0-s)*(1.0-s)+a2*a2))+t2*(s/sqrt(s*s+a1*a1));
        return new double[]{f,g};
      }
    };
    LineSearch ls = new LineSearch(func,1.0e-10,1.0e-3,1.0e-3);
    double[] se = new double[]{0.08, 0.10, 0.35, 0.83};
    double[] ge = new double[]{-6.9e-5, -4.9e-5, -2.9e-6, 1.6e-5};
    int[] ne = new int[]{5, 2, 4, 5};
    testMT(func,ls,se,ge,ne);
  }

  /**
   * Mor'e and Thuente's test function 5.
   */
  public void testMT5() {
    trace("Function 5");
    LineSearch.Function func = new LineSearch.Function() {
      public double[] evaluate(double s) {
        double a1 = 1.0e-2;
        double a2 = 1.0e-3;
        double t1 = sqrt(1.0+a1*a1)-a1;
        double t2 = sqrt(1.0+a2*a2)-a2;
        double f = t1*sqrt((1.0-s)*(1.0-s)+a2*a2)+t2*sqrt(s*s+a1*a1);
        double g = 
          -t1*((1.0-s)/sqrt((1.0-s)*(1.0-s)+a2*a2))+t2*(s/sqrt(s*s+a1*a1));
        return new double[]{f,g};
      }
    };
    LineSearch ls = new LineSearch(func,1.0e-10,1.0e-3,1.0e-3);
    double[] se = new double[]{0.075, 0.078, 0.073, 0.076};
    double[] ge = new double[]{ 1.9e-4, 7.4e-4, -2.6e-4, 4.5e-4};
    int[] ne = new int[]{7, 4, 8, 9};
    testMT(func,ls,se,ge,ne);
  }

  /**
   * Mor'e and Thuente's test function 6.
   */
  public void testMT6() {
    trace("Function 6");
    LineSearch.Function func = new LineSearch.Function() {
      public double[] evaluate(double s) {
        double a1 = 1.0e-3;
        double a2 = 1.0e-2;
        double t1 = sqrt(1.0+a1*a1)-a1;
        double t2 = sqrt(1.0+a2*a2)-a2;
        double f = t1*sqrt((1.0-s)*(1.0-s)+a2*a2)+t2*sqrt(s*s+a1*a1);
        double g = 
          -t1*((1.0-s)/sqrt((1.0-s)*(1.0-s)+a2*a2))+t2*(s/sqrt(s*s+a1*a1));
        return new double[]{f,g};
      }
    };
    LineSearch ls = new LineSearch(func,1.0e-10,1.0e-3,1.0e-3);
    double[] se = new double[]{0.93, 0.93, 0.92, 0.92};
    double[] ge = new double[]{ 5.2e-4, 8.4e-5, -2.4e-4, -3.2e-4};
    int[] ne = new int[]{14, 12, 9, 12};
    testMT(func,ls,se,ge,ne);
  }

  private void testMT(
    LineSearch.Function func, LineSearch ls, 
    double[] se, double[] ge, int[] ne)
  {
    double s = 1.0e-3;
    for (int ie=0; ie<se.length; ++ie,s*=100.0) {
      double[] fg = func.evaluate(0.0);
      double f = fg[0];
      double g = fg[1];
      double smin = 0.0;
      double smax = 4.0*max(1.0,s);
      LineSearch.Result lsr = ls.search(s,f,g,smin,smax);
      trace("s0="+s);
      trace(" s="+lsr.s+" f'(s)="+lsr.g +
            " ended="+lsr.ended +
            " neval="+lsr.neval);
      assertTrue(lsr.converged());
      assertEquals(se[ie],lsr.s,0.1*abs(se[ie]));
      //assertEquals(ge[ie],lsr.g,0.1*abs(ge[ie]));
      assertEquals(ne[ie],lsr.neval);
    }
  }
  
  private void trace(String s) {
    //System.out.println(s);
  }
}
