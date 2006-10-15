/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;

/**
 * A difference filter, with a transpose, inverse, and inverse-transpose.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.10.10
 */
public class DifferenceFilter {

  ///////////////////////////////////////////////////////////////////////////
  // test
  public static void main(String[] args) {
    int n1 = 5;
    int n2 = 5;
    int m1 = (n1-1)/2;
    int m2 = (n2-1)/2;
    float[][] x = Array.zerofloat(n1,n2);
    float[][] y = Array.zerofloat(n1,n2);
    float[][] z = Array.zerofloat(n1,n2);
    /*
    x[0][0] = 1.0f;
    x[0][n1-1] = 1.0f;
    x[n2-1][0] = 1.0f;
    x[n2-1][n1-1] = 1.0f;
    */
    x[m2][m1] = 1.0f;
    DifferenceFilter df = new DifferenceFilter();
    df.apply(x,y);
    Array.dump(y);
    df.applyTranspose(y,z);
    Array.dump(z);
  }

  /**
   * Applies this difference filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply(float[] x, float[] y) {
    int n = y.length;
    y[0] = x[0];
    for (int i=1; i<n; ++i)
      y[i] = x[i]+AP1*x[i-1];
  }

  /**
   * Applies this difference filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply(float[][] x, float[][] y) {
    float xm0,xm1,xm2,xm3,xm4;
    float xp0,xp1,xp2,xp3,xp4;
    int n1 = x[0].length;
    int n2 = x.length;
    xm0 = xm1 = xm2 = xm3 = xm4 = 0.0f;
    for (int i1=0; i1<n1; ++i1) {
      xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[0][i1];
      y[0][i1] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4;
    }
    for (int i2=1; i2<n2; ++i2) {
      xm0 = xm1 = xm2 = xm3 = xm4 = 0.0f;
      xp0 = xp1 = xp2 = xp3 = xp4 = 0.0f;
      if (n1>=4)
        xp4 = x[i2-1][3];
      if (n1>=3)
        xp3 = x[i2-1][2];
      if (n1>=2)
        xp2 = x[i2-1][1];
      if (n1>=1)
        xp1 = x[i2-1][0];
      for (int i1=0; i1<n1-4; ++i1) {
        xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[i2  ][i1  ];
        xp0 = xp1;  xp1 = xp2;  xp2 = xp3;  xp3 = xp4;  xp4 = x[i2-1][i1+4];
        y[i2][i1] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4 +
                    A1M0*xp0+A1M1*xp1+A1M2*xp2+A1M3*xp3+A1M4*xp4;
      }
      if (n1>=4) {
        xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[i2][n1-4];
        xp0 = xp1;  xp1 = xp2;  xp2 = xp3;  xp3 = xp4;
        y[i2][n1-4] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4 +
                      A1M0*xp0+A1M1*xp1+A1M2*xp2+A1M3*xp3;
      }
      if (n1>=3) {
        xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[i2][n1-3];
        xp0 = xp1;  xp1 = xp2;  xp2 = xp3;
        y[i2][n1-3] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4 +
                      A1M0*xp0+A1M1*xp1+A1M2*xp2;
      }
      if (n1>=2) {
        xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[i2][n1-2];
        xp0 = xp1;  xp1 = xp2;
        y[i2][n1-2] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4 +
                      A1M0*xp0+A1M1*xp1;
      }
      if (n1>=1) {
        xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[i2][n1-1];
        xp0 = xp1;
        y[i2][n1-1] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4 +
                      A1M0*xp0;
      }
    }
  }

  /**
   * Applies the transpose of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyTranspose(float[] x, float[] y) {
    int n = y.length;
    y[n-1] = x[n-1];
    for (int i=n-2; i>=0; --i)
      y[i] = x[i]+AP1*x[i+1];
  }

  /**
   * Applies the transpose of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyTranspose(float[][] x, float[][] y) {
    float xm0,xm1,xm2,xm3,xm4;
    float xp0,xp1,xp2,xp3,xp4;
    int n1 = x[0].length;
    int n2 = x.length;
    xp0 = xp1 = xp2 = xp3 = xp4 = 0.0f;
    for (int i1=n1-1; i1>=0; --i1) {
      xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[n2-1][i1];
      y[n2-1][i1] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4;
    }
    for (int i2=n2-2; i2>=0; --i2) {
      xm0 = xm1 = xm2 = xm3 = xm4 = 0.0f;
      xp0 = xp1 = xp2 = xp3 = xp4 = 0.0f;
      if (n1>=4)
        xm4 = x[i2+1][n1-4];
      if (n1>=3)
        xm3 = x[i2+1][n1-3];
      if (n1>=2)
        xm2 = x[i2+1][n1-2];
      if (n1>=1)
        xm1 = x[i2+1][n1-1];
      for (int i1=n1-1; i1>=4; --i1) {
        xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[i2  ][i1  ];
        xm0 = xm1;  xm1 = xm2;  xm2 = xm3;  xm3 = xm4;  xm4 = x[i2+1][i1-4];
        y[i2][i1] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4 +
                    A1M0*xm0+A1M1*xm1+A1M2*xm2+A1M3*xm3+A1M4*xm4;
      }
      if (n1>3) {
        xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[i2][3];
        xm0 = xm1;  xm1 = xm2;  xm2 = xm3;  xm3 = xm4;
        y[i2][3] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4 +
                   A1M0*xm0+A1M1*xm1+A1M2*xm2+A1M3*xm3;
      }
      if (n1>2) {
        xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[i2][2];
        xm0 = xm1;  xm1 = xm2;  xm2 = xm3;
        y[i2][2] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4 +
                   A1M0*xm0+A1M1*xm1+A1M2*xm2;
      }
      if (n1>1) {
        xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[i2][1];
        xm0 = xm1;  xm1 = xm2;
        y[i2][1] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4 +
                   A1M0*xm0+A1M1*xm1;
      }
      if (n1>0) {
        xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[i2][0];
        xm0 = xm1;
        y[i2][0] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4 +
                   A1M0*xm0;
      }
    }
  }

  /**
   * Applies the inverse of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyInverse(float[] x, float[] y) {
    int n = y.length;
    y[0] = x[0];
    for (int i=1; i<n; ++i)
      y[i] = x[i]-AP1*y[i-1];
  }

  /**
   * Applies the inverse of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyInverse(float[][] x, float[][] y) {
    float xm0;
    float ym0,ym1,ym2,ym3,ym4;
    float yp0,yp1,yp2,yp3,yp4;
    int n1 = x[0].length;
    int n2 = x.length;
    ym0 = ym1 = ym2 = ym3 = ym4 = 0.0f;
    for (int i1=0; i1<n1; ++i1) {
      ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[0][i1];
      y[0][i1] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4);
    }
    for (int i2=1; i2<n2; ++i2) {
      ym0 = ym1 = ym2 = ym3 = ym4 = 0.0f;
      yp0 = yp1 = yp2 = yp3 = yp4 = 0.0f;
      if (n1>=4)
        yp4 = y[i2-1][3];
      if (n1>=3)
        yp3 = y[i2-1][2];
      if (n1>=2)
        yp2 = y[i2-1][1];
      if (n1>=1)
        yp1 = y[i2-1][0];
      for (int i1=0; i1<n1-4; ++i1) {
        ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[i2  ][i1  ];
        yp0 = yp1;  yp1 = yp2;  yp2 = yp3;  yp3 = yp4;  yp4 = y[i2-1][i1+4];
        y[i2][i1] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4 -
                           A1M0*yp0-A1M1*yp1-A1M2*yp2-A1M3*yp3-A1M4*yp4);
      }
      if (n1>=4) {
        ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[i2][n1-4];
        yp0 = yp1;  yp1 = yp2;  yp2 = yp3;  yp3 = yp4;
        y[i2][n1-4] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4 -
                             A1M0*yp0-A1M1*yp1-A1M2*yp2-A1M3*yp3);
      }
      if (n1>=3) {
        ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[i2][n1-3];
        yp0 = yp1;  yp1 = yp2;  yp2 = yp3;
        y[i2][n1-3] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4 -
                             A1M0*yp0-A1M1*yp1-A1M2*yp2);
      }
      if (n1>=2) {
        ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[i2][n1-2];
        yp0 = yp1;  yp1 = yp2;
        y[i2][n1-2] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4 -
                             A1M0*yp0-A1M1*yp1);
      }
      if (n1>=1) {
        ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[i2][n1-1];
        yp0 = yp1;
        y[i2][n1-1] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4 -
                             A1M0*yp0);
      }
    }
  }

  /**
   * Applies the inverse transpose of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyInverseTranspose(float[] x, float[] y) {
    int n = y.length;
    y[n-1] = x[n-1];
    for (int i=n-2; i>=0; --i)
      y[i] = x[i]-AP1*y[i+1];
  }

  /**
   * Applies the inverse transpose of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyInverseTranspose(float[][] x, float[][] y) {
    float xp0;
    float ym0,ym1,ym2,ym3,ym4;
    float yp0,yp1,yp2,yp3,yp4;
    int n1 = x[0].length;
    int n2 = x.length;
    yp0 = yp1 = yp2 = yp3 = yp4 = 0.0f;
    for (int i1=n1-1; i1>=0; --i1) {
      yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[n2-1][i1];
      y[n2-1][i1] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4);
    }
    for (int i2=n2-2; i2>=0; --i2) {
      ym0 = ym1 = ym2 = ym3 = ym4 = 0.0f;
      yp0 = yp1 = yp2 = yp3 = yp4 = 0.0f;
      if (n1>=4)
        ym4 = y[i2+1][n1-4];
      if (n1>=3)
        ym3 = y[i2+1][n1-3];
      if (n1>=2)
        ym2 = y[i2+1][n1-2];
      if (n1>=1)
        ym1 = y[i2+1][n1-1];
      for (int i1=n1-1; i1>=4; --i1) {
        yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[i2  ][i1  ];
        ym0 = ym1;  ym1 = ym2;  ym2 = ym3;  ym3 = ym4;  ym4 = y[i2+1][i1-4];
        y[i2][i1] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4 -
                           A1M0*ym0-A1M1*ym1-A1M2*ym2-A1M3*ym3-A1M4*ym4);
      }
      if (n1>3) {
        yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[i2][3];
        ym0 = ym1;  ym1 = ym2;  ym2 = ym3;  ym3 = ym4;
        y[i2][3] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4 -
                          A1M0*ym0-A1M1*ym1-A1M2*ym2-A1M3*ym3);
      }
      if (n1>2) {
        yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[i2][2];
        ym0 = ym1;  ym1 = ym2;  ym2 = ym3;
        y[i2][2] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4 -
                          A1M0*ym0-A1M1*ym1-A1M2*ym2);
      }
      if (n1>1) {
        yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[i2][1];
        ym0 = ym1;  ym1 = ym2;
        y[i2][1] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4 -
                          A1M0*ym0-A1M1*ym1);
      }
      if (n1>0) {
        yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[i2][0];
        ym0 = ym1;
        y[i2][0] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4 -
                          A1M0*ym0);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final float AP1 = -0.999f;
  private static final float A0P4 = -0.00708972f;
  private static final float A0P3 = -0.01793403f; 
  private static final float A0P2 = -0.03850411f; 
  private static final float A0P1 = -0.64490664f; 
  private static final float A0P0 =  1.79548454f; 
  private static final float A1M0 = -0.55659920f;
  private static final float A1M1 = -0.20031442f;
  private static final float A1M2 = -0.08457147f; 
  private static final float A1M3 = -0.04141619f; 
  private static final float A1M4 = -0.02290331f; 
  private static final float AIP0 =  1.0f/A0P0; 
}
