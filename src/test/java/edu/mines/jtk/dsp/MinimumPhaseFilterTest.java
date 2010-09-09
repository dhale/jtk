/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.MinimumPhaseFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.10.13
 */
public class MinimumPhaseFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(MinimumPhaseFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1Random() {
    int[] lag1 = {0,1,2};
    float[] a = {2.0f,1.8f,0.81f};
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,a);
    int n = 100;
    float[] x,y,z;

    x = rands(n);
    y = zeros(n);
    z = zeros(n);
    mpf.apply(x,y);
    mpf.applyInverse(y,z);
    assertEqual(x,z);
    mpf.applyTranspose(x,y);
    mpf.applyInverseTranspose(y,z);
    assertEqual(x,z);

    float d1,d2;
    float tiny = n*10.0f*FLT_EPSILON;
    x = rands(n);
    y = rands(n);
    z = zeros(n);
    mpf.apply(x,z);
    d1 = dot(z,y);
    mpf.applyTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
    mpf.applyInverse(x,z);
    d1 = dot(z,y);
    mpf.applyInverseTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
  }

  public void test2Random() {
    int[] lag1 = {
       0, 1, 2, 3, 4,
      -4,-3,-2,-1, 0
    };
    int[] lag2 = {
       0, 0, 0, 0, 0,
       1, 1, 1, 1, 1
    };
    float[] a = { 
       1.79548454f, -0.64490664f, -0.03850411f, -0.01793403f, -0.00708972f,
      -0.02290331f, -0.04141619f, -0.08457147f, -0.20031442f, -0.55659920f
    };

    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,lag2,a);
    int n1 = 19;
    int n2 = 21;
    float[][] x,y,z;

    x = rands(n1,n2);
    y = zeros(n1,n2);
    z = zeros(n1,n2);
    mpf.apply(x,y);
    mpf.applyInverse(y,z);
    assertEqual(x,z);
    mpf.applyTranspose(x,y);
    mpf.applyInverseTranspose(y,z);
    assertEqual(x,z);

    float d1,d2;
    float tiny = n1*n2*10.0f*FLT_EPSILON;
    x = rands(n1,n2);
    y = rands(n1,n2);
    z = zeros(n1,n2);
    mpf.apply(x,z);
    d1 = dot(z,y);
    mpf.applyTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
    mpf.applyInverse(x,z);
    d1 = dot(z,y);
    mpf.applyInverseTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
  }

  public void test3Random() {
    int[] lag1 = {
                   0, 1, 2,
            -2,-1, 0, 1, 2,
            -2,-1, 0, 1, 2,
            -2,-1, 0,
    };
    int[] lag2 = {
                   0, 0, 0,      
             1, 1, 1, 1, 1,      
            -1,-1,-1,-1,-1,      
             0, 0, 0,
    };
    int[] lag3 = {
                   0, 0, 0,      
             0, 0, 0, 0, 0,      
             1, 1, 1, 1, 1,      
             1, 1, 1,
    };
    float[] a = {
                                 2.3110454f, -0.4805547f, -0.0143204f, 
      -0.0291793f, -0.1057476f, -0.4572746f, -0.0115732f, -0.0047283f, 
      -0.0149963f, -0.0408317f, -0.0945958f, -0.0223166f, -0.0062781f, 
      -0.0213786f, -0.0898909f, -0.4322719f
    };
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,lag2,lag3,a);
    int n1 = 11;
    int n2 = 13;
    int n3 = 12;
    float[][][] x,y,z;

    x = rands(n1,n2,n3);
    y = zeros(n1,n2,n3);
    z = zeros(n1,n2,n3);
    mpf.apply(x,y);
    mpf.applyInverse(y,z);
    assertEqual(x,z);
    mpf.applyTranspose(x,y);
    mpf.applyInverseTranspose(y,z);
    assertEqual(x,z);

    float d1,d2;
    float tiny = n1*n2*n3*10.0f*FLT_EPSILON;
    x = rands(n1,n2,n3);
    y = rands(n1,n2,n3);
    z = zeros(n1,n2,n3);
    mpf.apply(x,z);
    d1 = dot(z,y);
    mpf.applyTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
    mpf.applyInverse(x,z);
    d1 = dot(z,y);
    mpf.applyInverseTranspose(y,z);
    d2 = dot(z,x);
    assertEquals(d1,d2,tiny);
  }

  public void testFactorFomelExample() {
    float[] r = {24.0f,242.0f,867.0f,1334.0f,867.0f,242.0f,24.0f};
    int[] lag1 = {0,1,2,3};
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1);
    mpf.factorWilsonBurg(10,0.0f,r);
    int nlag = lag1.length;
    float[] x = new float[nlag];
    float[] a = new float[nlag];
    x[0] = 1.0f;
    mpf.apply(x,a);
    assertEquals(24.0f,a[0],10*FLT_EPSILON);
    assertEquals(26.0f,a[1],10*FLT_EPSILON);
    assertEquals( 9.0f,a[2],10*FLT_EPSILON);
    assertEquals( 1.0f,a[3],10*FLT_EPSILON);
  }

  public void testFactorLaplacian2() {
    float[][] r = {
      { 0.000f,-0.999f, 0.000f},
      {-0.999f, 4.000f,-0.999f},
      { 0.000f,-0.999f, 0.000f}
    };
    int[] lag1 = {
                   0, 1, 2, 3, 4,
      -4,-3,-2,-1, 0
    };
    int[] lag2 = {
                   0, 0, 0, 0, 0,
       1, 1, 1, 1, 1
    };
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,lag2);
    mpf.factorWilsonBurg(100,FLT_EPSILON,r);
    float[][] s = new float[3][3];
    float[][] t = new float[3][3];
    s[1][1] = 1.0f;
    mpf.apply(s,t);
    mpf.applyTranspose(t,s);
    float emax = 0.01f*r[1][1];
    for (int i2=0; i2<3; ++i2) {
      for (int i1=0; i1<3; ++i1) {
        assertEquals(r[i2][i1],s[i2][i1],emax);
      }
    }
    //System.out.println("2-D Laplacian:");
    //dump(r);
    //dump(s);
  }

  public void testFactorLaplacian3() {
    float[][][] r = {
      {
        { 0.000f, 0.000f, 0.000f},
        { 0.000f,-0.999f, 0.000f},
        { 0.000f, 0.000f, 0.000f}
      },{
        { 0.000f,-0.999f, 0.000f},
        {-0.999f, 6.000f,-0.999f},
        { 0.000f,-0.999f, 0.000f}
      },{
        { 0.000f, 0.000f, 0.000f},
        { 0.000f,-0.999f, 0.000f},
        { 0.000f, 0.000f, 0.000f}
      }
    };
    int[] lag1 = {
                   0, 1, 2,
            -2,-1, 0, 1, 2,
            -2,-1, 0, 1, 2,
            -2,-1, 0,
    };
    int[] lag2 = {
                   0, 0, 0,      
             1, 1, 1, 1, 1,      
            -1,-1,-1,-1,-1,      
             0, 0, 0,
    };
    int[] lag3 = {
                   0, 0, 0,      
             0, 0, 0, 0, 0,      
             1, 1, 1, 1, 1,      
             1, 1, 1,
    };
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,lag2,lag3);
    mpf.factorWilsonBurg(100,FLT_EPSILON,r);
    float[][][] s = new float[3][3][3];
    float[][][] t = new float[3][3][3];
    s[1][1][1] = 1.0f;
    mpf.apply(s,t);
    mpf.applyTranspose(t,s);
    float emax = 0.01f*r[1][1][1];
    for (int i3=0; i3<3; ++i3) {
      for (int i2=0; i2<3; ++i2) {
        for (int i1=0; i1<3; ++i1) {
          assertEquals(r[i3][i2][i1],s[i3][i2][i1],emax);
        }
      }
    }
    //System.out.println("3-D Laplacian:");
    //dump(r);
    //dump(s);
  }

  public void xtestFactorPlane2Filter() {
    int[] lag1 = {
                0, 1, 2, 3,
      -3,-2,-1, 0, 1
    };
    int[] lag2 = {
                0, 0, 0, 0,
       1, 1, 1, 1, 1
    };
    float[][] s = new float[3][7];
    float[][] t = new float[3][7];
    int maxiter = 100;
    float epsilon = FLT_EPSILON;
    //int ntheta = 33;
    //float dtheta = FLT_PI/(float)(ntheta-1);
    //float ftheta = -FLT_PI/2.0f;
    int ntheta = 2;
    float dtheta =  FLT_PI/4.0f;
    float ftheta = -FLT_PI/8.0f;
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,lag2);
    for (int itheta=0; itheta<ntheta; ++itheta) {
      float theta = ftheta+itheta*dtheta;
      float n1 = cos(theta);
      float n2 = sin(theta);
      System.out.println("theta="+theta+" n1="+n1+" n2="+n2);
      float m12 = 0.5f*(n1-n2);
      float p12 = 0.5f*(n1+n2);
      float[][] r = {
        {     -m12*m12, -2.0f*m12*p12,     -p12*p12},
        { 2.0f*m12*p12,         1.01f, 2.0f*m12*p12},
        {     -p12*p12, -2.0f*m12*p12,     -m12*m12}
      };
      mpf.factorWilsonBurg(maxiter,epsilon,r);
      dump(r);
      zero(s);
      int k1 = (s[0].length-1)/2;
      int k2 = (s.length-1)/2;
      s[k2][k1] = 1.0f;
      mpf.apply(s,t);
      mpf.applyTranspose(t,s);
      dump(s);
      dump(t);
    }
  }

  public void xtestFactorPlane3Filter() {
    /*
    int[] lag1 = {
                0, 1, 2,
         -2,-1, 0, 1, 2,
         -2,-1, 0, 1, 2,
         -2,-1, 0, 1, 2,
         -2,-1, 0, 1, 2,
         -2,-1, 0, 1, 2,
         -2,-1, 0, 1,
    };
    int[] lag2 = {
                0, 0, 0,
          1, 1, 1, 1, 1,
          2, 2, 2, 2, 2,
         -2,-2,-2,-2,-2,
         -1,-1,-1,-1,-1,
          0, 0, 0, 0, 0,
          1, 1, 1, 1,
    };
    int[] lag3 = {
                0, 0, 0,
          0, 0, 0, 0, 0,
          0, 0, 0, 0, 0,
          1, 1, 1, 1, 1,
          1, 1, 1, 1, 1,
          1, 1, 1, 1, 1,
          1, 1, 1, 1,
    };
    */
    int[] lag1 = {
                   0, 1, 2, 3,
         -3,-2,-1, 0, 1, 2, 3,
         -3,-2,-1, 0, 1, 2, 3,
         -3,-2,-1, 0, 1, 2, 3,

         -3,-2,-1, 0, 1, 2, 3,
         -3,-2,-1, 0, 1, 2, 3,
         -3,-2,-1, 0, 1, 2, 3,
         -3,-2,-1, 0, 1, 2, 3,
         -3,-2,-1, 0, 1, 2, 3,
         -3,-2,-1, 0, 1, 2, 3,
         -3,-2,-1, 0, 1, 2, 3,
    };
    int[] lag2 = {
                   0, 0, 0, 0,
          1, 1, 1, 1, 1, 1, 1,
          2, 2, 2, 2, 2, 2, 2,
          3, 3, 3, 3, 3, 3, 3,

         -3,-3,-3,-3,-3,-3,-3,
         -2,-2,-2,-2,-2,-2,-2,
         -1,-1,-1,-1,-1,-1,-1,
          0, 0, 0, 0, 0, 0, 0,
          1, 1, 1, 1, 1, 1, 1,
          2, 2, 2, 2, 2, 2, 2,
          3, 3, 3, 3, 3, 3, 3,
    };
    int[] lag3 = {
                   0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0,

          1, 1, 1, 1, 1, 1, 1,
          1, 1, 1, 1, 1, 1, 1,
          1, 1, 1, 1, 1, 1, 1,
          1, 1, 1, 1, 1, 1, 1,
          1, 1, 1, 1, 1, 1, 1,
          1, 1, 1, 1, 1, 1, 1,
          1, 1, 1, 1, 1, 1, 1,
    };
    float[][][] s = new float[3][7][7];
    float[][][] t = new float[3][7][7];
    int m = lag1.length;
    float[] amax = new float[m];
    int maxiter = 100;
    float epsilon = FLT_EPSILON;
    int nphi = 19;
    float dphi = 2.0f*FLT_PI/(float)(nphi-1);
    float fphi = -FLT_PI;
    int ntheta = 5;
    float dtheta = 0.5f*FLT_PI/(float)(ntheta-1);
    float ftheta = 0.0f;
    //nphi = 2;
    //dphi =  FLT_PI/2.0f;
    //fphi = -FLT_PI/4.0f;
    //ntheta = 2;
    //dtheta = FLT_PI/4.0f;
    //ftheta = FLT_PI/4.0f;
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,lag2,lag3);
    for (int iphi=0; iphi<nphi; ++iphi) {
      float phi = fphi+iphi*dphi;
      for (int itheta=0; itheta<ntheta; ++itheta) {
        float theta = ftheta+itheta*dtheta;
        float n1 = cos(theta);
        float n2 = sin(theta)*cos(phi);
        float n3 = sin(theta)*sin(phi);
        System.out.println("\nphi="+phi+" theta="+theta+
                           " n1="+n1+" n2="+n2+" n3="+n3);
        float m12 = 0.5f*(n1-n2);
        float m13 = 0.5f*(n1-n3);
        float m23 = 0.5f*(n2-n3);
        float p12 = 0.5f*(n1+n2);
        float p13 = 0.5f*(n1+n3);
        float p23 = 0.5f*(n2+n3);
        float[][][] r = {{
          {         0.0f,         -m23*m23,              0.0f},
          {      -m13*m13,  -2.0f*(m13*p13+m23*p23),  -p13*p13},
          {         0.0f,         -p23*p23,              0.0f}
        },{
          {      -m12*m12,   2.0f*(-m12*p12+m23*p23), -p12*p12},
          { 2.0f*(m12*p12+m13*p13),  2.02f,    2.0f*(m12*p12+m13*p13)},
          {      -p12*p12,   2.0f*(-m12*p12+m23*p23), -m12*m12}
        },{
          {         0.0f,         -p23*p23,              0.0f},
          {      -p13*p13,  -2.0f*(m13*p13+m23*p23),  -m13*m13},
          {         0.0f,         -m23*m23,              0.0f}
        }};
        mpf.factorWilsonBurg(maxiter,epsilon,r);
        float[] a = mpf.getA();
        for (int j=0; j<m; ++j) {
          if (abs(a[j])>amax[j])
            amax[j] = abs(a[j]);
        }
        //dump(r);
        zero(s);
        int k1 = (s[0][0].length-1)/2;
        int k2 = (s[0].length-1)/2;
        int k3 = (s.length-1)/2;
        s[k3][k2][k1] = 1.0f;
        mpf.apply(s,t);
        mpf.applyTranspose(t,s);
        //dump(s);
        //dump(t);
      }
    }
    for (int j=0; j<m; ++j)
      System.out.println("lag1="+lag1[j]+
                        " lag2="+lag2[j]+
                        " lag3="+lag3[j]+
                        " amax="+amax[j]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static float[] rands(int n1) {
    return sub(randfloat(n1),0.5f);
  }
  private static float[][] rands(int n1, int n2) {
    return sub(randfloat(n1,n2),0.5f);
  }
  private static float[][][] rands(int n1, int n2, int n3) {
    return sub(randfloat(n1,n2,n3),0.5f);
  }

  private static float[] zeros(int n1) {
    return zerofloat(n1);
  }
  private static float[][] zeros(int n1, int n2) {
    return zerofloat(n1,n2);
  }
  private static float[][][] zeros(int n1, int n2, int n3) {
    return zerofloat(n1,n2,n3);
  }

  private static float dot(float[] x, float[] y) {
    return sum(mul(x,y));
  }
  private static float dot(float[][] x, float[][] y) {
    return sum(mul(x,y));
  }
  private static float dot(float[][][] x, float[][][] y) {
    return sum(mul(x,y));
  }

  private static void assertEqual(float[] re, float[] ra) {
    int n = re.length;
    float tolerance = (float)(n)*FLT_EPSILON;
    for (int i=0; i<n; ++i)
      assertEquals(re[i],ra[i],tolerance);
  }

  private static void assertEqual(float[][] re, float[][] ra) {
    int n2 = re.length;
    int n1 = re[0].length;
    float tolerance = (float)(n1*n2)*FLT_EPSILON;
    for (int i2=0; i2<n2; ++i2)
      for (int i1=0; i1<n1; ++i1)
        assertEquals(re[i2][i1],ra[i2][i1],tolerance);
  }

  private static void assertEqual(float[][][] re, float[][][] ra) {
    int n3 = re.length;
    int n2 = re[0].length;
    int n1 = re[0][0].length;
    float tolerance = (float)(n1*n2*n3)*FLT_EPSILON;
    for (int i3=0; i3<n3; ++i3)
      for (int i2=0; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          assertEquals(re[i3][i2][i1],ra[i3][i2][i1],tolerance);
  }
}
