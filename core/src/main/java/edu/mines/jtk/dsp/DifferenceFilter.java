/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.dsp;

/**
 * A difference filter, with a transpose, inverse, and inverse-transpose.
 * A 1-D difference filter is an approximation to a backward-difference
 * filter: y[i] = x[i]-0.999*x[i-1]. The constant 0.999 is less than
 * one so that the recursive inverse filter y[i] = x[i]+0.999*y[i-1] is 
 * stable. The inverse filter is sometimes called "leaky integration",
 * and is especially useful for preconditioning inverse problems with
 * smooth solutions.
 * <p>
 * Sequential application of the backward-difference filter and its 
 * transpose yields an approximation to a negative centered 2nd-difference 
 * filter: y[i] = -x[i-1]+2*x[i]-x[i+1].
 * <p>
 * Extensions to 2-D and 3-D backward-difference filters are defined as in
 * Claerbout, J., 1998, Multidimensional recursive filters via a helix: 
 * Geophysics, v. 63, n. 5, p. 1532-1541.
 * <p>
 * These extensions were obtained here by factoring the negative centered 
 * 2-D and 3-D 2nd-difference filters, respectively, using the Wilson-Burg 
 * algorithm, as in Fomel, S., Sava, P., Rickett, J., and Claerbout, J., 
 * 2003, The Wilson-Burg method of spectral factorization with application 
 * to helical filtering: Geophysical Prospecting, v. 51, p. 409-420.
 * <p>
 * For all dimensions, these approximations yield less than one percent 
 * error in the negative centered 2nd-difference filter, relative to the
 * exact central filter coefficient. For example, the error for a 2-D 
 * filter is less than 0.04 = 0.01*4, where 4 is the central coefficient 
 * in the exact negative 2nd-difference filter.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.10.10
 */
public class DifferenceFilter {

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
    xm0 = xm1 = xm2 = xm3 = 0.0f;
    for (int i1=0; i1<n1; ++i1) {
      xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[0][i1];
      y[0][i1] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4;
    }
    for (int i2=1; i2<n2; ++i2) {
      xm0 = xm1 = xm2 = xm3 = 0.0f;
      xp1 = xp2 = xp3 = xp4 = 0.0f;
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
                    APM0*xp0+APM1*xp1+APM2*xp2+APM3*xp3+APM4*xp4;
      }
      if (n1>=4) {
        xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[i2][n1-4];
        xp0 = xp1;  xp1 = xp2;  xp2 = xp3;  xp3 = xp4;
        y[i2][n1-4] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4 +
                      APM0*xp0+APM1*xp1+APM2*xp2+APM3*xp3;
      }
      if (n1>=3) {
        xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[i2][n1-3];
        xp0 = xp1;  xp1 = xp2;  xp2 = xp3;
        y[i2][n1-3] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4 +
                      APM0*xp0+APM1*xp1+APM2*xp2;
      }
      if (n1>=2) {
        xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[i2][n1-2];
        xp0 = xp1;  xp1 = xp2;
        y[i2][n1-2] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4 +
                      APM0*xp0+APM1*xp1;
      }
      if (n1>=1) {
        xm4 = xm3;  xm3 = xm2;  xm2 = xm1;  xm1 = xm0;  xm0 = x[i2][n1-1];
        xp0 = xp1;
        y[i2][n1-1] = A0P0*xm0+A0P1*xm1+A0P2*xm2+A0P3*xm3+A0P4*xm4 +
                      APM0*xp0;
      }
    }
  }

  /**
   * Applies this difference filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void apply(float[][][] x, float[][][] y) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    int n2m1 = n2-1;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        float x0mm2=0.0f, x0mm1=0.0f, x0mm0=0.0f, x0mp1=0.0f, x0mp2=0.0f;
        float x00m2     , x00m1=0.0f, x00m0=0.0f;
        float                         xm0m0=0.0f, xm0p1=0.0f, xm0p2=0.0f;
        float xmpm2=0.0f, xmpm1=0.0f, xmpm0=0.0f, xmpp1=0.0f, xmpp2=0.0f;
        if (n1>0) {
          if (i2>0)
            x0mp1 = x[i3][i2-1][0];
          if (i3>0) {
            xm0p1 = x[i3-1][i2][0];
            if (i2<n2m1)
              xmpp1 = x[i3-1][i2+1][0];
          }
        }
        if (n1>1) {
          if (i2>0)
            x0mp2 = x[i3][i2-1][1];
          if (i3>0) {
            xm0p2 = x[i3-1][i2][1];
            if (i2<n2m1)
              xmpp2 = x[i3-1][i2+1][1];
          }
        }
        for (int i1=0; i1<n1-2; ++i1) {
          x00m2 = x00m1;  
          x00m1 = x00m0;  
          x00m0 = x[i3][i2][i1];
          if (i2>0) {
            x0mm2 = x0mm1;
            x0mm1 = x0mm0;
            x0mm0 = x0mp1;
            x0mp1 = x0mp2;
            x0mp2 = x[i3][i2-1][i1+2];
          }
          if (i3>0) {
            if (i2<n2m1) {
              xmpm2 = xmpm1;
              xmpm1 = xmpm0;
              xmpm0 = xmpp1;
              xmpp1 = xmpp2;
              xmpp2 = x[i3-1][i2+1][i1+2];
            }
            xm0m0 = xm0p1;
            xm0p1 = xm0p2;
            xm0p2 = x[i3-1][i2][i1+2];
          }
          y[i3][i2][i1] =           A00P0*x00m0+A00P1*x00m1+A00P2*x00m2 +
            A0PM2*x0mp2+A0PM1*x0mp1+A0PP0*x0mm0+A0PP1*x0mm1+A0PP2*x0mm2 +
            APMM2*xmpp2+APMM1*xmpp1+APMP0*xmpm0+APMP1*xmpm1+APMP2*xmpm2 +
            AP0M2*xm0p2+AP0M1*xm0p1+AP0P0*xm0m0;
        }
        if (n1>1) {
          x00m2 = x00m1;  
          x00m1 = x00m0;  
          x00m0 = x[i3][i2][n1-2];
          x0mm2 = x0mm1;
          x0mm1 = x0mm0;
          x0mm0 = x0mp1;
          x0mp1 = x0mp2;
          xmpm2 = xmpm1;
          xmpm1 = xmpm0;
          xmpm0 = xmpp1;
          xmpp1 = xmpp2;
          xm0m0 = xm0p1;
          xm0p1 = xm0p2;
          y[i3][i2][n1-2] =         A00P0*x00m0+A00P1*x00m1+A00P2*x00m2 +
                        A0PM1*x0mp1+A0PP0*x0mm0+A0PP1*x0mm1+A0PP2*x0mm2 +
                        APMM1*xmpp1+APMP0*xmpm0+APMP1*xmpm1+APMP2*xmpm2 +
                        AP0M1*xm0p1+AP0P0*xm0m0;
        }
        if (n1>0) {
          x00m2 = x00m1;  
          x00m1 = x00m0;  
          x00m0 = x[i3][i2][n1-1];
          x0mm2 = x0mm1;
          x0mm1 = x0mm0;
          x0mm0 = x0mp1;
          xmpm2 = xmpm1;
          xmpm1 = xmpm0;
          xmpm0 = xmpp1;
          xm0m0 = xm0p1;
          y[i3][i2][n1-1] =         A00P0*x00m0+A00P1*x00m1+A00P2*x00m2 +
                                    A0PP0*x0mm0+A0PP1*x0mm1+A0PP2*x0mm2 +
                                    APMP0*xmpm0+APMP1*xmpm1+APMP2*xmpm2 +
                                    AP0P0*xm0m0;
        }
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
    xp0 = xp1 = xp2 = xp3 = 0.0f;
    for (int i1=n1-1; i1>=0; --i1) {
      xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[n2-1][i1];
      y[n2-1][i1] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4;
    }
    for (int i2=n2-2; i2>=0; --i2) {
      xm1 = xm2 = xm3 = xm4 = 0.0f;
      xp0 = xp1 = xp2 = xp3 = 0.0f;
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
                    APM0*xm0+APM1*xm1+APM2*xm2+APM3*xm3+APM4*xm4;
      }
      if (n1>3) {
        xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[i2][3];
        xm0 = xm1;  xm1 = xm2;  xm2 = xm3;  xm3 = xm4;
        y[i2][3] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4 +
                   APM0*xm0+APM1*xm1+APM2*xm2+APM3*xm3;
      }
      if (n1>2) {
        xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[i2][2];
        xm0 = xm1;  xm1 = xm2;  xm2 = xm3;
        y[i2][2] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4 +
                   APM0*xm0+APM1*xm1+APM2*xm2;
      }
      if (n1>1) {
        xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[i2][1];
        xm0 = xm1;  xm1 = xm2;
        y[i2][1] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4 +
                   APM0*xm0+APM1*xm1;
      }
      if (n1>0) {
        xp4 = xp3;  xp3 = xp2;  xp2 = xp1;  xp1 = xp0;  xp0 = x[i2][0];
        xm0 = xm1;
        y[i2][0] = A0P0*xp0+A0P1*xp1+A0P2*xp2+A0P3*xp3+A0P4*xp4 +
                   APM0*xm0;
      }
    }
  }

  /**
   * Applies the transpose of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyTranspose(float[][][] x, float[][][] y) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    int n1m1 = n1-1;
    int n2m1 = n2-1;
    int n3m1 = n3-1;
    for (int i3=n3m1; i3>=0; --i3) {
      for (int i2=n2m1; i2>=0; --i2) {
        float x0mm2=0.0f, x0mm1=0.0f, x0mm0=0.0f, x0mp1=0.0f, x0mp2=0.0f;
        float x00m2     , x00m1=0.0f, x00m0=0.0f;
        float                         xm0m0=0.0f, xm0p1=0.0f, xm0p2=0.0f;
        float xmpm2=0.0f, xmpm1=0.0f, xmpm0=0.0f, xmpp1=0.0f, xmpp2=0.0f;
        if (n1>0) {
          if (i2<n2m1)
            x0mp1 = x[i3][i2+1][n1-1];
          if (i3<n3m1) {
            xm0p1 = x[i3+1][i2][n1-1];
            if (i2>0)
              xmpp1 = x[i3+1][i2-1][n1-1];
          }
        }
        if (n1>1) {
          if (i2<n2m1)
            x0mp2 = x[i3][i2+1][n1-2];
          if (i3<n3m1) {
            xm0p2 = x[i3+1][i2][n1-2];
            if (i2>0)
              xmpp2 = x[i3+1][i2-1][n1-2];
          }
        }
        for (int i1=n1m1; i1>=2; --i1) {
          x00m2 = x00m1;  
          x00m1 = x00m0;  
          x00m0 = x[i3][i2][i1];
          if (i2<n2m1) {
            x0mm2 = x0mm1;
            x0mm1 = x0mm0;
            x0mm0 = x0mp1;
            x0mp1 = x0mp2;
            x0mp2 = x[i3][i2+1][i1-2];
          }
          if (i3<n3m1) {
            if (i2>0) {
              xmpm2 = xmpm1;
              xmpm1 = xmpm0;
              xmpm0 = xmpp1;
              xmpp1 = xmpp2;
              xmpp2 = x[i3+1][i2-1][i1-2];
            }
            xm0m0 = xm0p1;
            xm0p1 = xm0p2;
            xm0p2 = x[i3+1][i2][i1-2];
          }
          y[i3][i2][i1] =           A00P0*x00m0+A00P1*x00m1+A00P2*x00m2 +
            A0PM2*x0mp2+A0PM1*x0mp1+A0PP0*x0mm0+A0PP1*x0mm1+A0PP2*x0mm2 +
            APMM2*xmpp2+APMM1*xmpp1+APMP0*xmpm0+APMP1*xmpm1+APMP2*xmpm2 +
            AP0M2*xm0p2+AP0M1*xm0p1+AP0P0*xm0m0;
        }
        if (n1>1) {
          x00m2 = x00m1;  
          x00m1 = x00m0;  
          x00m0 = x[i3][i2][1];
          x0mm2 = x0mm1;
          x0mm1 = x0mm0;
          x0mm0 = x0mp1;
          x0mp1 = x0mp2;
          xmpm2 = xmpm1;
          xmpm1 = xmpm0;
          xmpm0 = xmpp1;
          xmpp1 = xmpp2;
          xm0m0 = xm0p1;
          xm0p1 = xm0p2;
          y[i3][i2][1] =            A00P0*x00m0+A00P1*x00m1+A00P2*x00m2 +
                        A0PM1*x0mp1+A0PP0*x0mm0+A0PP1*x0mm1+A0PP2*x0mm2 +
                        APMM1*xmpp1+APMP0*xmpm0+APMP1*xmpm1+APMP2*xmpm2 +
                        AP0M1*xm0p1+AP0P0*xm0m0;
        }
        if (n1>0) {
          x00m2 = x00m1;  
          x00m1 = x00m0;  
          x00m0 = x[i3][i2][0];
          x0mm2 = x0mm1;
          x0mm1 = x0mm0;
          x0mm0 = x0mp1;
          xmpm2 = xmpm1;
          xmpm1 = xmpm0;
          xmpm0 = xmpp1;
          xm0m0 = xm0p1;
          y[i3][i2][0] =            A00P0*x00m0+A00P1*x00m1+A00P2*x00m2 +
                                    A0PP0*x0mm0+A0PP1*x0mm1+A0PP2*x0mm2 +
                                    APMP0*xmpm0+APMP1*xmpm1+APMP2*xmpm2 +
                                    AP0P0*xm0m0;
        }
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
    ym0 = ym1 = ym2 = ym3 = 0.0f;
    for (int i1=0; i1<n1; ++i1) {
      ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[0][i1];
      y[0][i1] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4);
    }
    for (int i2=1; i2<n2; ++i2) {
      ym0 = ym1 = ym2 = ym3 = 0.0f;
      yp1 = yp2 = yp3 = yp4 = 0.0f;
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
                           APM0*yp0-APM1*yp1-APM2*yp2-APM3*yp3-APM4*yp4);
      }
      if (n1>=4) {
        ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[i2][n1-4];
        yp0 = yp1;  yp1 = yp2;  yp2 = yp3;  yp3 = yp4;
        y[i2][n1-4] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4 -
                             APM0*yp0-APM1*yp1-APM2*yp2-APM3*yp3);
      }
      if (n1>=3) {
        ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[i2][n1-3];
        yp0 = yp1;  yp1 = yp2;  yp2 = yp3;
        y[i2][n1-3] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4 -
                             APM0*yp0-APM1*yp1-APM2*yp2);
      }
      if (n1>=2) {
        ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[i2][n1-2];
        yp0 = yp1;  yp1 = yp2;
        y[i2][n1-2] = ym0 = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4 -
                             APM0*yp0-APM1*yp1);
      }
      if (n1>=1) {
        ym4 = ym3;  ym3 = ym2;  ym2 = ym1;  ym1 = ym0;  xm0 = x[i2][n1-1];
        yp0 = yp1;
        y[i2][n1-1] = AIP0*(xm0-A0P1*ym1-A0P2*ym2-A0P3*ym3-A0P4*ym4 -
                       APM0*yp0);
      }
    }
  }

  /**
   * Applies the inverse of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyInverse(float[][][] x, float[][][] y) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    int n2m1 = n2-1;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        float x00m0;
        float y0mm2=0.0f, y0mm1=0.0f, y0mm0=0.0f, y0mp1=0.0f, y0mp2=0.0f;
        float y00m2     , y00m1=0.0f, y00m0=0.0f;
        float                         ym0m0=0.0f, ym0p1=0.0f, ym0p2=0.0f;
        float ympm2=0.0f, ympm1=0.0f, ympm0=0.0f, ympp1=0.0f, ympp2=0.0f;
        if (n1>0) {
          if (i2>0)
            y0mp1 = y[i3][i2-1][0];
          if (i3>0) {
            ym0p1 = y[i3-1][i2][0];
            if (i2<n2m1)
              ympp1 = y[i3-1][i2+1][0];
          }
        }
        if (n1>1) {
          if (i2>0)
            y0mp2 = y[i3][i2-1][1];
          if (i3>0) {
            ym0p2 = y[i3-1][i2][1];
            if (i2<n2m1)
              ympp2 = y[i3-1][i2+1][1];
          }
        }
        for (int i1=0; i1<n1-2; ++i1) {
          x00m0 = x[i3][i2][i1];
          y00m2 = y00m1;
          y00m1 = y00m0;
          if (i2>0) {
            y0mm2 = y0mm1;
            y0mm1 = y0mm0;
            y0mm0 = y0mp1;
            y0mp1 = y0mp2;
            y0mp2 = y[i3][i2-1][i1+2];
          }
          if (i3>0) {
            if (i2<n2m1) {
              ympm2 = ympm1;
              ympm1 = ympm0;
              ympm0 = ympp1;
              ympp1 = ympp2;
              ympp2 = y[i3-1][i2+1][i1+2];
            }
            ym0m0 = ym0p1;
            ym0p1 = ym0p2;
            ym0p2 = y[i3-1][i2][i1+2];
          }
          y[i3][i2][i1] = y00m0 =   AI0P0*(x00m0-A00P1*y00m1-A00P2*y00m2 -
             A0PM2*y0mp2-A0PM1*y0mp1-A0PP0*y0mm0-A0PP1*y0mm1-A0PP2*y0mm2 -
             APMM2*ympp2-APMM1*ympp1-APMP0*ympm0-APMP1*ympm1-APMP2*ympm2 -
             AP0M2*ym0p2-AP0M1*ym0p1-AP0P0*ym0m0);
        }
        if (n1>1) {
          x00m0 = x[i3][i2][n1-2];
          y00m2 = y00m1;  
          y00m1 = y00m0;  
          y0mm2 = y0mm1;
          y0mm1 = y0mm0;
          y0mm0 = y0mp1;
          y0mp1 = y0mp2;
          ympm2 = ympm1;
          ympm1 = ympm0;
          ympm0 = ympp1;
          ympp1 = ympp2;
          ym0m0 = ym0p1;
          ym0p1 = ym0p2;
          y[i3][i2][n1-2] = y00m0 = AI0P0*(x00m0-A00P1*y00m1-A00P2*y00m2 -
                         A0PM1*y0mp1-A0PP0*y0mm0-A0PP1*y0mm1-A0PP2*y0mm2 -
                         APMM1*ympp1-APMP0*ympm0-APMP1*ympm1-APMP2*ympm2 -
                         AP0M1*ym0p1-AP0P0*ym0m0);
        }
        if (n1>0) {
          x00m0 = x[i3][i2][n1-1];
          y00m2 = y00m1;  
          y00m1 = y00m0;  
          y0mm2 = y0mm1;
          y0mm1 = y0mm0;
          y0mm0 = y0mp1;
          ympm2 = ympm1;
          ympm1 = ympm0;
          ympm0 = ympp1;
          ym0m0 = ym0p1;
          y[i3][i2][n1-1] = AI0P0*(x00m0-A00P1*y00m1-A00P2*y00m2 -
                             A0PP0*y0mm0-A0PP1*y0mm1-A0PP2*y0mm2 -
                             APMP0*ympm0-APMP1*ympm1-APMP2*ympm2 -
                             AP0P0*ym0m0);
        }
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
    yp0 = yp1 = yp2 = yp3 = 0.0f;
    for (int i1=n1-1; i1>=0; --i1) {
      yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[n2-1][i1];
      y[n2-1][i1] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4);
    }
    for (int i2=n2-2; i2>=0; --i2) {
      ym1 = ym2 = ym3 = ym4 = 0.0f;
      yp0 = yp1 = yp2 = yp3 = 0.0f;
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
                           APM0*ym0-APM1*ym1-APM2*ym2-APM3*ym3-APM4*ym4);
      }
      if (n1>3) {
        yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[i2][3];
        ym0 = ym1;  ym1 = ym2;  ym2 = ym3;  ym3 = ym4;
        y[i2][3] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4 -
                          APM0*ym0-APM1*ym1-APM2*ym2-APM3*ym3);
      }
      if (n1>2) {
        yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[i2][2];
        ym0 = ym1;  ym1 = ym2;  ym2 = ym3;
        y[i2][2] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4 -
                          APM0*ym0-APM1*ym1-APM2*ym2);
      }
      if (n1>1) {
        yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[i2][1];
        ym0 = ym1;  ym1 = ym2;
        y[i2][1] = yp0 = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4 -
                          APM0*ym0-APM1*ym1);
      }
      if (n1>0) {
        yp4 = yp3;  yp3 = yp2;  yp2 = yp1;  yp1 = yp0;  xp0 = x[i2][0];
        ym0 = ym1;
        y[i2][0] = AIP0*(xp0-A0P1*yp1-A0P2*yp2-A0P3*yp3-A0P4*yp4 -
                    APM0*ym0);
      }
    }
  }

  /**
   * Applies the inverse transpose of this filter.
   * @param x the filter input.
   * @param y the filter output.
   */
  public void applyInverseTranspose(float[][][] x, float[][][] y) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    int n1m1 = n1-1;
    int n2m1 = n2-1;
    int n3m1 = n3-1;
    for (int i3=n3m1; i3>=0; --i3) {
      for (int i2=n2m1; i2>=0; --i2) {
        float x00m0;
        float y0mm2=0.0f, y0mm1=0.0f, y0mm0=0.0f, y0mp1=0.0f, y0mp2=0.0f;
        float y00m2     , y00m1=0.0f, y00m0=0.0f;
        float                         ym0m0=0.0f, ym0p1=0.0f, ym0p2=0.0f;
        float ympm2=0.0f, ympm1=0.0f, ympm0=0.0f, ympp1=0.0f, ympp2=0.0f;
        if (n1>0) {
          if (i2<n2m1)
            y0mp1 = y[i3][i2+1][n1-1];
          if (i3<n3m1) {
            ym0p1 = y[i3+1][i2][n1-1];
            if (i2>0)
              ympp1 = y[i3+1][i2-1][n1-1];
          }
        }
        if (n1>1) {
          if (i2<n2m1)
            y0mp2 = y[i3][i2+1][n1-2];
          if (i3<n3m1) {
            ym0p2 = y[i3+1][i2][n1-2];
            if (i2>0)
              ympp2 = y[i3+1][i2-1][n1-2];
          }
        }
        for (int i1=n1m1; i1>=2; --i1) {
          x00m0 = x[i3][i2][i1];
          y00m2 = y00m1;  
          y00m1 = y00m0;  
          if (i2<n2m1) {
            y0mm2 = y0mm1;
            y0mm1 = y0mm0;
            y0mm0 = y0mp1;
            y0mp1 = y0mp2;
            y0mp2 = y[i3][i2+1][i1-2];
          }
          if (i3<n3m1) {
            if (i2>0) {
              ympm2 = ympm1;
              ympm1 = ympm0;
              ympm0 = ympp1;
              ympp1 = ympp2;
              ympp2 = y[i3+1][i2-1][i1-2];
            }
            ym0m0 = ym0p1;
            ym0p1 = ym0p2;
            ym0p2 = y[i3+1][i2][i1-2];
          }
          y[i3][i2][i1] = y00m0 =   AI0P0*(x00m0-A00P1*y00m1-A00P2*y00m2 -
             A0PM2*y0mp2-A0PM1*y0mp1-A0PP0*y0mm0-A0PP1*y0mm1-A0PP2*y0mm2 -
             APMM2*ympp2-APMM1*ympp1-APMP0*ympm0-APMP1*ympm1-APMP2*ympm2 -
             AP0M2*ym0p2-AP0M1*ym0p1-AP0P0*ym0m0);
        }
        if (n1>1) {
          x00m0 = x[i3][i2][1];
          y00m2 = y00m1;  
          y00m1 = y00m0;  
          y0mm2 = y0mm1;
          y0mm1 = y0mm0;
          y0mm0 = y0mp1;
          y0mp1 = y0mp2;
          ympm2 = ympm1;
          ympm1 = ympm0;
          ympm0 = ympp1;
          ympp1 = ympp2;
          ym0m0 = ym0p1;
          ym0p1 = ym0p2;
          y[i3][i2][1] = y00m0 =    AI0P0*(x00m0-A00P1*y00m1-A00P2*y00m2 -
                         A0PM1*y0mp1-A0PP0*y0mm0-A0PP1*y0mm1-A0PP2*y0mm2 -
                         APMM1*ympp1-APMP0*ympm0-APMP1*ympm1-APMP2*ympm2 -
                         AP0M1*ym0p1-AP0P0*ym0m0);
        }
        if (n1>0) {
          x00m0 = x[i3][i2][0];
          y00m2 = y00m1;  
          y00m1 = y00m0;  
          y0mm2 = y0mm1;
          y0mm1 = y0mm0;
          y0mm0 = y0mp1;
          ympm2 = ympm1;
          ympm1 = ympm0;
          ympm0 = ympp1;
          ym0m0 = ym0p1;
          y[i3][i2][0] = AI0P0*(x00m0-A00P1*y00m1-A00P2*y00m2 -
                          A0PP0*y0mm0-A0PP1*y0mm1-A0PP2*y0mm2 -
                          APMP0*ympm0-APMP1*ympm1-APMP2*ympm2 -
                          AP0P0*ym0m0);
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Coefficient for 1-D difference filter:     lag1
  //private static final float AP0 =  1.000f;  //  0
  private static final float AP1 = -0.999f;  //  1

  // Coefficients for 2-D difference filter:          lag1 lag2
  private static final float A0P0 =  1.79548454f;  //  0    0
  private static final float A0P1 = -0.64490664f;  //  1    0
  private static final float A0P2 = -0.03850411f;  //  2    0
  private static final float A0P3 = -0.01793403f;  //  3    0
  private static final float A0P4 = -0.00708972f;  //  4    0
  private static final float APM0 = -0.55659920f;  //  0    1
  private static final float APM1 = -0.20031442f;  // -1    1
  private static final float APM2 = -0.08457147f;  // -2    1
  private static final float APM3 = -0.04141619f;  // -3    1
  private static final float APM4 = -0.02290331f;  // -4    1
  private static final float AIP0 =  1.0f/A0P0;

  // Coefficients for 3-D difference filter:          lag1 lag2 lag3
  private static final float A00P0 =  2.3110454f;  //  0    0    0
  private static final float A00P1 = -0.4805547f;  //  1    0    0
  private static final float A00P2 = -0.0143204f;  //  2    0    0
  private static final float A0PM2 = -0.0291793f;  // -2    1    0
  private static final float A0PM1 = -0.1057476f;  // -1    1    0
  private static final float A0PP0 = -0.4572746f;  //  0    1    0
  private static final float A0PP1 = -0.0115732f;  //  1    1    0
  private static final float A0PP2 = -0.0047283f;  //  2    1    0
  private static final float APMM2 = -0.0149963f;  // -2   -1    1
  private static final float APMM1 = -0.0408317f;  // -1   -1    1
  private static final float APMP0 = -0.0945958f;  //  0   -1    1
  private static final float APMP1 = -0.0223166f;  //  1   -1    1
  private static final float APMP2 = -0.0062781f;  //  2   -1    1
  private static final float AP0M2 = -0.0213786f;  // -2    0    1
  private static final float AP0M1 = -0.0898909f;  // -1    0    1
  private static final float AP0P0 = -0.4322719f;  //  0    0    1
  private static final float AI0P0 = 1.0f/A00P0;
}
