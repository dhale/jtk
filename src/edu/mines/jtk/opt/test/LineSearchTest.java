/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt.test;

import junit.framework.*;

import edu.mines.jtk.opt.LineSearch;
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

  public void testMT1() {
    trace("Function 1");
    LineSearch.Function func = new LineSearch.Function() {
      public double f(double s) {
        return -s/(s*s+beta);
      }
      public double g(double s) {
        double t = 1.0/(s*s+beta);
        return (s*s-beta)*t*t;
      }
      private double beta = 2.0;
    };
    LineSearch ls = new LineSearch(func,1.0e-10,1.0e-3,1.0e-1);
    double[] se = new double[]{ 1.4, 1.4, 10.0, 37.0};
    double[] ge = new double[]{-9.2e-3, 4.7e-3, 9.4e-3, 7.3e-4};
    int[] ne = new int[]{7, 4, 2, 5};
    testMT(func,ls,se,ge,ne);
  }

  public void testMT2() {
    trace("Function 2");
    LineSearch.Function func = new LineSearch.Function() {
      public double f(double s) {
        double t = s+beta2;
        return t*t*t*t*t-2.0*t*t*t*t;
      }
      public double g(double s) {
        double t = s+beta2;
        return 5.0*t*t*t*t-8.0*t*t*t;
      }
      private double beta2 = 4.0e-3;
    };
    LineSearch ls = new LineSearch(func,1.0e-10,1.0e-1,1.0e-1);
    double[] se = new double[]{1.6, 1.6, 1.6, 1.6};
    double[] ge = new double[]{7.1e-9, 1.0e-10, -5.0e-9, -2.3e-8};
    int[] ne = new int[]{13, 9, 9, 12};
    testMT(func,ls,se,ge,ne);
  }

  public void testMT3() {
    trace("Function 3");
    LineSearch.Function func = new LineSearch.Function() {
      public double f(double s) {
        int nwig = 39;
        double f;
        if (s<1.0-beta3) {
          f = 1.0-s;
        } else if (s<1.0+beta3) {
          f = (s-1.0)*(s-1.0)/(2.0*beta3)+beta3/2.0;
        } else {
          f = s-1.0;
        }
        double t1 = 2.0*nwig*atan(1.0);
        double t2 = (1.0-beta3)/t1;
        f += t2*sin(t1*s);
        return f;
      }
      public double g(double s) {
        int nwig = 39;
        double g;
        if (s<1.0-beta3) {
          g = -1.0;
        } else if (s<1.0+beta3) {
          g = (s-1.0)/beta3;
        } else {
          g = 1.0;
        }
        double t1 = 2.0*nwig*atan(1.0);
        double t2 = (1.0-beta3)/t1;
        g += (1.0-beta3)*cos(t1*s);
        return g;
      }
      private double beta3 = 1.0e-2;
    };
    LineSearch ls = new LineSearch(func,1.0e-10,1.0e-1,1.0e-1);
    double[] se = new double[]{1.0, 1.0, 1.0, 1.0};
    double[] ge = new double[]{-5.1e-5, -1.9e-4, -2.0e-6, -1.6e-5};
    int[] ne = new int[]{13, 13, 11, 14};
    testMT(func,ls,se,ge,ne);
  }

  private void testMT(
    LineSearch.Function func, LineSearch ls, 
    double[] se, double[] ge, int[] ne)
  {
    double s = 1.0e-3;
    for (int ie=0; ie<se.length; ++ie,s*=100.0) {
      double f = func.f(0.0);
      double g = func.g(0.0);
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
