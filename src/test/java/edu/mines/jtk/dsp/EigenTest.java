/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Stopwatch;

/**
 * Tests {@link edu.mines.jtk.dsp.Eigen}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.31
 */
public class EigenTest extends TestCase {
  public static void main(String[] args) {
    if (args.length>0 && args[0].equals("bench")) {
      benchSymmetric33();
    } else {
      TestSuite suite = new TestSuite(EigenTest.class);
      junit.textui.TestRunner.run(suite);
    }
  }

  public void testSymmetric22() {
    int nrand = 10000;
    double[][] v = new double[2][2];
    double[] d = new double[2];
    for (int irand=0; irand<nrand; ++irand) {
      double[][] a = randdouble(2,2);
      a = add(a, transpose(a));
      Eigen.solveSymmetric22(a,v,d);
      check(a,v,d);
    }
  }

  public void testSymmetric33() {
    double[][] v = new double[3][3];
    double[] d = new double[3];
    int nrand = 10000;
    for (int irand=0; irand<nrand; ++irand) {
      //double[][] a = randdouble(3,3);
      //a = add(a,transpose(a));
      double[][] a = makeRandomSymmetric33();
      Eigen.solveSymmetric33(a,v,d);
      check(a,v,d);
    }
  }

  public void testSymmetric33Special() {
    double[][] v = new double[3][3];
    double[] d = new double[3];
    double[][][] as = {ASMALL,A100,A110,A111,ATEST1};
    for (double[][] a:as) {
      Eigen.solveSymmetric33(a,v,d);
      check(a,v,d);
    }
  }

  private void check(double[][] a, double[][] v, double[] d) {
    int n = a.length;
    for (int k=0; k<n; ++k) {
      assertTrue(k==0 || d[k-1]>=d[k]);
      for (int i=0; i<n; ++i) {
        double av = 0.0f;
        for (int j=0; j<n; ++j) {
          av += a[i][j]*v[k][j];
        }
        double vd = v[k][i]*d[k];
        assertEquals(av,vd,0.0001);
      }
    }
  }

  private static final double[][] A100 = {
    {1.0,0.0,0.0},
    {0.0,0.0,0.0},
    {0.0,0.0,0.0}
  };
  private static final double[][] A110 = {
    {1.0,0.0,0.0},
    {0.0,1.0,0.0},
    {0.0,0.0,0.0}
  };
  private static final double[][] A111 = {
    {1.0,0.0,0.0},
    {0.0,1.0,0.0},
    {0.0,0.0,1.0}
  };
  private static final double[][] ASMALL = {
    {-1.08876e-13,  1.87872e-17,  1.29275e-16},
    { 1.87872e-17, -7.65274e-15, -1.13984e-14},
    { 1.29275e-16, -1.13984e-14, -2.53222e-14}
  };
  private static final double[][] ATEST1 = {
    { 0.54957539, -0.00555262,  0.09809611},
    {-0.00555262,  0.41839826, -0.00414489},  
    { 0.09809611, -0.00414489,  0.49139029}
  };

  private static Random r = new Random();

  // Symmetric 3x3 matrix with specified eigenvalues e and eigenvectors u,w.
  private static double[][] makeSymmetric33(
    double[] e, double[] u, double[] w) 
  {
    double eu = e[0], ev = e[1], ew = e[2];
    double u1 = u[0], u2 = u[1], u3 = u[2];
    double w1 = w[0], w2 = w[1], w3 = w[2];
    double[][] a = new double[3][3];
    double esum = eu+ev+ew;
    ev = esum-eu-ew;
    eu -= ev;
    ew -= ev;
    a[0][0] = eu*u1*u1+ew*w1*w1+ev; // a11
    a[0][1] = eu*u1*u2+ew*w1*w2   ; // a12
    a[0][2] = eu*u1*u3+ew*w1*w3   ; // a13
    a[1][0] = a[0][1]             ; // a21
    a[1][1] = eu*u2*u2+ew*w2*w2+ev; // a22
    a[1][2] = eu*u2*u3+ew*w2*w3   ; // a23
    a[2][0] = a[0][2]             ; // a31
    a[2][1] = a[1][2]             ; // a32
    a[2][2] = eu*u3*u3+ew*w3*w3+ev; // a33
    return a;
  }

  // Random symmetric 3x3 matrix.
  private static double[][] makeRandomSymmetric33() {
    double[] e = makeRandomEigenvalues3();
    double[] u = makeRandomEigenvector3();
    double[] w = makeOrthogonalVector3(u);
    return makeSymmetric33(e,u,w);
  }

  // Random eigenvalues.
  private static double[] makeRandomEigenvalues3() {
    double a1 = r.nextDouble();
    double a2 = r.nextDouble();
    double a3 = r.nextDouble();
    double au = Math.max(Math.max(a1,a2),a3);
    double aw = Math.min(Math.min(a1,a2),a3);
    double av = a1+a2+a3-au-aw;
    return new double[]{au,av,aw};
  }

  // Random unit vector with non-negative 3rd component.
  private static double[] makeRandomEigenvector3() {
    double a = r.nextDouble()-0.5;
    double b = r.nextDouble()-0.5;
    double c = r.nextDouble()-0.5;
    if (c<0.0) {
      a = -a;
      b = -b;
      c = -c;
    }
    double s = 1.0/Math.sqrt(a*a+b*b+c*c);
    return new double[]{a*s,b*s,c*s};
  }

  // Random unit vector orthogonal to specified vector.
  private static double[] makeOrthogonalVector3(double[] v1) {
    double a1 = v1[0];
    double b1 = v1[1];
    double c1 = v1[2];
    double a2 = r.nextDouble()-0.5;
    double b2 = r.nextDouble()-0.5;
    double c2 = r.nextDouble()-0.5;
    double d11 = a1*a1+b1*b1+c1*c1;
    double d12 = a1*a2+b1*b2+c1*c2;
    double s = d12/d11;
    double a = a2-s*a1;
    double b = b2-s*b1;
    double c = c2-s*c1;
    if (c<0.0) {
      a = -a;
      b = -b;
      c = -c;
    }
    s = 1.0/Math.sqrt(a*a+b*b+c*c);
    return new double[]{a*s,b*s,c*s};
  }

  private static void benchSymmetric33() {
    int nrand = 10000;
    double[][][] a = new double[nrand][][];
    for (int irand=0; irand<nrand; ++irand) {
      a[irand] = randdouble(3,3);
      a[irand] = add(a[irand],transpose(a[irand]));
    }
    double[][] v = new double[3][3];
    double[] d = new double[3];
    Stopwatch s = new Stopwatch();
    int nloop,rate;
    double maxtime = 2.0;
    s.reset();
    s.start();
    for (nloop=0; s.time()<maxtime; ++nloop) {
      for (int irand=0; irand<nrand; ++irand) {
        Eigen.solveSymmetric33(a[irand],v,d);
      }
    }
    s.stop();
    rate = (int)((double)nloop*(double)nrand/s.time());
    System.out.println("Number of 3x3 eigen-decompositions per second");
    System.out.println("jacobi: rate="+rate);
    s.reset();
    s.start();
    for (nloop=0; s.time()<maxtime; ++nloop) {
      for (int irand=0; irand<nrand; ++irand) {
        Eigen.solveSymmetric33Fast(a[irand],v,d);
      }
    }
    s.stop();
    rate = (int)((double)nloop*(double)nrand/s.time());
    System.out.println("hybrid: rate="+rate);
  }
}
