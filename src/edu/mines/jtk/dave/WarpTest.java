/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dave;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.la.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Test warping and unwarping of an image.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.03.28
 */
public class WarpTest {

  public static float[][][] findWarpL(
    float sigma, int m1, int m2, float[][] p, float[][] q) {
    LocalCorrelationFilter lcf = new LocalCorrelationFilter(sigma);
    int n1 = p[0].length;
    int n2 = p.length;
    float[][] c = new float[n2][n1];
    float[][] cmax = new float[n2][n1];
    float[][][] lag = new float[2][n2][n1];
    float[][] lag1 = lag[0];
    float[][] lag2 = lag[1];
    for (int l2=-m2; l2<=m2; ++l2) {
      for (int l1=-m1; l1<=m1; ++l1) {
        lcf.apply(l1,l2,p,q,c);
        for (int i2=0; i2<n2; ++i2) { 
          for (int i1=0; i1<n1; ++i1) { 
            float ci = c[i2][i1];
            if (ci>cmax[i2][i1]) {
              cmax[i2][i1] = ci;
              lag1[i2][i1] = l1;
              lag2[i2][i1] = l2;
            }
          }
        }
      }
    }
    return lag;
  }

  public static float[][][] findWarpU(
    float sigma, int m1, int m2, float[][] p, float[][] q) {
    LocalCorrelationFilter lcf = new LocalCorrelationFilter(sigma);
    int n1 = p[0].length;
    int n2 = p.length;
    float[][] c = new float[n2][n1];

    // Cross-correlations for only every 3rd sample in each dimension.
    int n1c = 1+(n1-3)/3;
    int n2c = 1+(n2-3)/3;

    // Integer lags of cross-correlation peaks.
    float[][] cmax = new float[n2c][n1c];
    int[][] lag1 = new int[n2c][n1c];
    int[][] lag2 = new int[n2c][n1c];
    for (int l2=-m2; l2<=m2; ++l2) {
      for (int l1=-m1; l1<=m1; ++l1) {
        lcf.apply(l1,l2,p,q,c);
        for (int i2c=0; i2c<n2c; ++i2c) {
          for (int i1c=0; i1c<n1c; ++i1c) {
            float ci = c[1+3*i2c][1+3*i1c];
            if (ci>cmax[i2c][i1c]) {
              cmax[i2c][i1c] = ci;
              lag1[i2c][i1c] = l1;
              lag2[i2c][i1c] = l2;
            }
          }
        }
      }
    }

    // 3x3 array of cross-correlations for lags nearest peaks.
    float[][][] c33 = new float[n2c][n1c][9];
    for (int l2=-m2; l2<=m2; ++l2) {
      for (int l1=-m1; l1<=m1; ++l1) {
        lcf.apply(l1,l2,p,q,c);
        for (int i2c=0; i2c<n2c; ++i2c) {
          for (int i1c=0; i1c<n1c; ++i1c) {
            int lag1c = lag1[i2c][i1c];
            int lag2c = lag2[i2c][i1c];
            int j1 = l1-lag1c;
            int j2 = l2-lag2c;
            if (-1<=j1 && j1<=1 && -1<=j2 && j2<=1)
              c33[i2c][i1c][(j1+1)+3*(j2+1)] = c[1+3*i2c][1+3*i1c];
          }
        }
      }
    }

    // QR decomposition of matrix for least-squares quadratic fit to peak.
    double[][] ea = new double[9][6];
    double[][] eb = new double[9][1];
    for (int j2=-1,i=0; j2<=1; ++j2) {
      for (int j1=-1; j1<=1; ++j1,++i) {
        ea[i][0] = 1.0;
        ea[i][1] = j1;
        ea[i][2] = j2;
        ea[i][3] = j1*j1;
        ea[i][4] = j1*j2;
        ea[i][5] = j2*j2;
      }
    }
    DMatrix a = new DMatrix(ea);
    DMatrix b = new DMatrix(eb);
    DMatrixQrd qrd = new DMatrixQrd(a);
    //System.out.println("Q="+qrd.getQ());
    //System.out.println("R="+qrd.getR());

    // Locations of peaks of best-fit quadratics.
    float[][][] u = new float[2][n2c][n1c];
    float[][] u1 = u[0];
    float[][] u2 = u[1];
    for (int i2c=0; i2c<n2c; ++i2c) {
      for (int i1c=0; i1c<n1c; ++i1c) {
        for (int j=0; j<9; ++j) {
          eb[j][0] = c33[i2c][i1c][j];
        }
        DMatrix x = qrd.solve(b);
        double[][] ex = x.getArray();
        double den = ex[4][0]*ex[4][0]-4.0*ex[3][0]*ex[5][0];
        double nu1 = (2.0*ex[1][0]*ex[5][0]-ex[2][0]*ex[4][0])/den;
        double nu2 = (2.0*ex[2][0]*ex[3][0]-ex[1][0]*ex[4][0])/den;
        double u1i = (abs(nu1)<=1.0)?lag1[i2c][i1c]+nu1:lag1[i2c][i1c];
        double u2i = (abs(nu2)<=1.0)?lag2[i2c][i1c]+nu2:lag2[i2c][i1c];
        u1[i2c][i1c] = (float)u1i;
        u2[i2c][i1c] = (float)u2i;
      }
    }
    return u;
  }

  public static float[][] warp(float[][] x1, float[][] x2, float[][] p) {
    int n1 = p[0].length;
    int n2 = p.length;
    SincInterpolator si = new SincInterpolator();
    si.setInput(n1,1.0,0.0,n2,1.0,0.0,p);
    float[][] q = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        q[i2][i1] = si.interpolate(x1[i2][i1],x2[i2][i1]);
      }
    }
    return q;
  }
  public static float[][][] warpGauss(
    float a1, float a2, 
    float b1, float b2, 
    float s1, float s2, 
    int n1, int n2)
  {
    float[][][] x = new float[2][n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      float y2 = (float)i2-b2;
      for (int i1=0; i1<n1; ++i1) {
        float y1 = (float)i1-b1;
        x[0][i2][i1] = b1+y1*(1.0f+a1*gauss(s1,s2,y1,y2));
        x[1][i2][i1] = b2+y2*(1.0f+a2*gauss(s1,s2,y1,y2));
      }
    }
    return x;
  }
  public static float[][][] unwarpGauss(
    float a1, float a2, 
    float b1, float b2, 
    float s1, float s2, 
    int n1, int n2)
  {
    float[][][] y = new float[2][n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      float x2 = (float)i2-b2;
      for (int i1=0; i1<n1; ++i1) {
        float x1 = (float)i1-b1;
        float y1 = x1;
        float y2 = x2;
        float y1p,y2p;
        do {
          y1p = y1;
          y2p = y2;
          y1 = x1/(1.0f+a1*gauss(s1,s2,y1p,y2p));
          y2 = x2/(1.0f+a2*gauss(s1,s2,y1p,y2p));
        } while (abs(y1-y1p)>0.001f || abs(y2-y2p)>0.001f);
        y[0][i2][i1] = y1+b1;
        y[1][i2][i1] = y2+b2;
      }
    }
    return y;
  }
  private static float gauss(float s1, float s2, float x1, float x2) {
    float e1 = x1/s1;
    float e2 = x2/s2;
    return exp(-0.5f*(e1*e1+e2*e2));
  }

  private static final float Q20 = 1.0f/3.0f;
  private static final float Q21 = 1.0f/sqrt(6.0f);
  private static final float Q22 = 1.0f/2.0f;
  private static final float Q23 = 1.0f/(3.0f*sqrt(2.0f));
  private static final float Q24 = sqrt(2.0f)/3.0f;
  private static final float Q2Z = 0.0f;
  private static final float[][] Q2 = {
    {-Q21,-Q21, Q22, Q23, Q23},
    { Q2Z,-Q21, Q2Z,-Q24, Q23},
    { Q21,-Q21,-Q22, Q23, Q23},
    {-Q21, Q2Z, Q2Z, Q23,-Q24},
    { Q2Z, Q2Z, Q2Z,-Q24,-Q24},
    { Q21, Q2Z, Q2Z, Q23,-Q24},
    {-Q21, Q21,-Q22, Q23, Q23},
    { Q2Z, Q21, Q2Z,-Q24, Q23},
    { Q21, Q21, Q22, Q23, Q23},
  };
  private static final float S21 = 1.0f/sqrt(6.0f);
  private static final float S22 = 1.0f/2.0f;
  private static final float S23 = 1.0f/sqrt(2.0f);
  private static final float[] S2 = {
    S21, S21, S22, S23, S23
  };
}
