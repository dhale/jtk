/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Local estimates of orientations of features in images.
 * <p>
 * <em>Warning: not yet completed or optimized for performance.</em>
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.02.02
 */
public class LocalOrientFilter {

  public LocalOrientFilter(double sigma) {
    _sigma = sigma;
    _lcf = new LocalCorrelationFilter(sigma);
  }

  public void applyA(float[][] f, float[][][] g) {
    Check.argument(g.length>=3,"g.length>=3");
    int n1 = g[0][0].length;
    int n2 = g[0].length;

    //  x x x
    //  x o o
    //  o o o
    float[][] r00 = new float[n2][n1];
    float[][] r0p = new float[n2][n1];
    float[][] rpp = new float[n2][n1];
    float[][] rp0 = new float[n2][n1];
    float[][] rpm = new float[n2][n1];
    int l = 2;
    _lcf.apply( 0, 0,f,f,r00);
    _lcf.apply( 0, l,f,f,r0p);
    _lcf.apply( l, l,f,f,rpp);
    _lcf.apply( l, 0,f,f,rp0);
    _lcf.apply( l,-l,f,f,rpm);

    float[][] a11 = new float[n2][n1];
    float[][] a12 = new float[n2][n1];
    float[][] a22 = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        a11[i2][i1] = 2.0f*(r00[i2][i1]-rp0[i2][i1]);
        a12[i2][i1] = 0.5f*(rpm[i2][i1]-rpp[i2][i1]);
        a22[i2][i1] = 2.0f*(r00[i2][i1]-r0p[i2][i1]);
      }
    }
    eigenSolve22(a11,a12,a22,g);
  }

  public void applyG(float[][] f, float[][][] g) {
    Check.argument(g.length>=3,"g.length>=3");
    int n1 = g[0][0].length;
    int n2 = g[0].length;

    RecursiveGaussianFilter rgf;
    rgf = new RecursiveGaussianFilter(max(_sigma/4.0,1.0));
    float[][] g1 = new float[n2][n1];
    float[][] g2 = new float[n2][n1];
    rgf.apply10(f,g1);
    rgf.apply01(f,g2);

    rgf = new RecursiveGaussianFilter(_sigma/sqrt(2.0));
    float[][] g1g1 = new float[n2][n1];
    float[][] g1g2 = new float[n2][n1];
    float[][] g2g2 = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        g1g1[i2][i1] = g1[i2][i1]*g1[i2][i1];
        g1g2[i2][i1] = g1[i2][i1]*g2[i2][i1];
        g2g2[i2][i1] = g2[i2][i1]*g2[i2][i1];
      }
    }
    rgf.apply00(g1g1,g1g1);
    rgf.apply00(g1g2,g1g2);
    rgf.apply00(g2g2,g2g2);

    float[][] a11 = new float[n2][n1];
    float[][] a12 = new float[n2][n1];
    float[][] a22 = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        a11[i2][i1] = g1g1[i2][i1];
        a12[i2][i1] = g1g2[i2][i1];
        a22[i2][i1] = g2g2[i2][i1];
      }
    }
    eigenSolve22(a11,a12,a22,g);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _sigma;
  private LocalCorrelationFilter _lcf;

  private static void eigenSolve22(
    float[][] a11, float[][] a12, float[][] a22, float[][][] g)
  {
    int n1 = a11[0].length;
    int n2 = a11.length;
    double[][] a = new double[2][2];
    double[][] v = new double[2][2];
    double[] d = new double[2];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        a[0][0] = a11[i2][i1];
        a[1][0] = a12[i2][i1];
        a[0][1] = a12[i2][i1];
        a[1][1] = a22[i2][i1];
        Eigen.solveSymmetric22(a,v,d);
        g[0][i2][i1] = (float)(1.0-d[1]/d[0]);
        if (v[0][0]>=0.0) {
          g[1][i2][i1] = (float)v[0][0];
          g[2][i2][i1] = (float)v[0][1];
        } else {
          g[1][i2][i1] = -(float)v[0][0];
          g[2][i2][i1] = -(float)v[0][1];
        }
      }
    }
  }
}
