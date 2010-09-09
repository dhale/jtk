/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.LocalCausalFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.15
 */
public class LocalCausalFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(LocalCausalFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1Random() {
    int[] lag1 = {0,1,2};
    final float[] ar = {  1.00f, -1.80f,  0.81f}; // (1-0.9z)(1-0.9z)
    final float[] as = {  1.00f, -1.60f,  0.64f}; // (1-0.8z)(1-0.8z)
    LocalCausalFilter lcf = new LocalCausalFilter(lag1);
    LocalCausalFilter.A1 a1 = new LocalCausalFilter.A1() {
      public void get(int i1, float[] a) {
        if (i1%2==0) {
          a[0] = ar[0];  a[1] = ar[1];  a[2] = ar[2];
        } else {
          a[0] = as[0];  a[1] = as[1];  a[2] = as[2];
        }
      }
    };
    int n = 100;
    float tiny = n*10.0f*FLT_EPSILON;

    { // y'Ax == x'A'y
      float[] x = rands(n);
      float[] y = rands(n);
      float[] ax = zeros(n);
      float[] ay = zeros(n);
      lcf.apply(a1,x,ax);
      lcf.applyTranspose(a1,y,ay);
      float dyx = dot(y,ax);
      float dxy = dot(x,ay);
      assertEquals(dyx,dxy,tiny);
    }

    { // y'Bx == x'B'y (for B = inv(A))
      float[] x = rands(n);
      float[] y = rands(n);
      float[] bx = zeros(n);
      float[] by = zeros(n);
      lcf.applyInverse(a1,x,bx);
      lcf.applyInverseTranspose(a1,y,by);
      float dyx = dot(y,bx);
      float dxy = dot(x,by);
      assertEquals(dyx,dxy,tiny);
    }

    { // x == BAx (for B = inv(A))
      float[] x = rands(n);
      float[] y = copy(x);
      lcf.apply(a1,y,y); // in-place
      lcf.applyInverse(a1,y,y); // in-place
      assertEqual(x,y);
    }

    { // x == A'B'x (for B = inv(A))
      float[] x = rands(n);
      float[] y = zeros(n);
      lcf.applyInverseTranspose(a1,x,y); // *not* in-place
      lcf.applyTranspose(a1,y,y); // in-place
      assertEqual(x,y);
    }
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
    float[] aa = { 
       1.79548454f, -0.64490664f, -0.03850411f, -0.01793403f, -0.00708972f,
      -0.02290331f, -0.04141619f, -0.08457147f, -0.20031442f, -0.55659920f
    };
    final float[] ar = mul(1.0f,aa);
    final float[] as = mul(2.0f,aa);
    LocalCausalFilter lcf = new LocalCausalFilter(lag1,lag2);
    LocalCausalFilter.A2 a2 = new LocalCausalFilter.A2() {
      public void get(int i1, int i2, float[] a) {
        if ((i1+i2)%2==0) {
          copy(ar,a);
        } else {
          copy(as,a);
        }
      }
    };
    int n1 = 19;
    int n2 = 21;
    float tiny = n1*n2*10.0f*FLT_EPSILON;

    { // y'Ax == x'A'y
      float[][] x = rands(n1,n2);
      float[][] y = rands(n1,n2);
      float[][] ax = zeros(n1,n2);
      float[][] ay = zeros(n1,n2);
      lcf.apply(a2,x,ax);
      lcf.applyTranspose(a2,y,ay);
      float dyx = dot(y,ax);
      float dxy = dot(x,ay);
      assertEquals(dyx,dxy,tiny);
    }

    { // y'Bx == x'B'y (for B = inv(A))
      float[][] x = rands(n1,n2);
      float[][] y = rands(n1,n2);
      float[][] bx = zeros(n1,n2);
      float[][] by = zeros(n1,n2);
      lcf.applyInverse(a2,x,bx);
      lcf.applyInverseTranspose(a2,y,by);
      float dyx = dot(y,bx);
      float dxy = dot(x,by);
      assertEquals(dyx,dxy,tiny);
    }

    { // x == BAx (for B = inv(A))
      float[][] x = rands(n1,n2);
      float[][] y = copy(x);
      lcf.apply(a2,y,y); // in-place
      lcf.applyInverse(a2,y,y); // in-place
      assertEqual(x,y);
    }

    { // x == A'B'x (for B = inv(A))
      float[][] x = rands(n1,n2);
      float[][] y = zeros(n1,n2);
      lcf.applyInverseTranspose(a2,x,y); // *not* in-place
      lcf.applyTranspose(a2,y,y); // in-place
      assertEqual(x,y);
    }
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
    float[] aa = {
                                 2.3110454f, -0.4805547f, -0.0143204f, 
      -0.0291793f, -0.1057476f, -0.4572746f, -0.0115732f, -0.0047283f, 
      -0.0149963f, -0.0408317f, -0.0945958f, -0.0223166f, -0.0062781f, 
      -0.0213786f, -0.0898909f, -0.4322719f
    };
    final float[] ar = mul(1.0f,aa);
    final float[] as = mul(2.0f,aa);
    LocalCausalFilter lcf = new LocalCausalFilter(lag1,lag2,lag3);
    LocalCausalFilter.A3 a3 = new LocalCausalFilter.A3() {
      public void get(int i1, int i2, int i3, float[] a) {
        if ((i1+i2+i3)%2==0) {
          copy(ar,a);
        } else {
          copy(as,a);
        }
      }
    };
    int n1 = 11;
    int n2 = 13;
    int n3 = 12;
    float tiny = n1*n2*n3*10.0f*FLT_EPSILON;

    { // y'Ax == x'A'y
      float[][][] x = rands(n1,n2,n3);
      float[][][] y = rands(n1,n2,n3);
      float[][][] ax = zeros(n1,n2,n3);
      float[][][] ay = zeros(n1,n2,n3);
      lcf.apply(a3,x,ax);
      lcf.applyTranspose(a3,y,ay);
      float dyx = dot(y,ax);
      float dxy = dot(x,ay);
      assertEquals(dyx,dxy,tiny);
    }

    { // y'Bx == x'B'y (for B = inv(A))
      float[][][] x = rands(n1,n2,n3);
      float[][][] y = rands(n1,n2,n3);
      float[][][] bx = zeros(n1,n2,n3);
      float[][][] by = zeros(n1,n2,n3);
      lcf.applyInverse(a3,x,bx);
      lcf.applyInverseTranspose(a3,y,by);
      float dyx = dot(y,bx);
      float dxy = dot(x,by);
      assertEquals(dyx,dxy,tiny);
    }

    { // x == BAx (for B = inv(A))
      float[][][] x = rands(n1,n2,n3);
      float[][][] y = copy(x);
      lcf.apply(a3,y,y); // in-place
      lcf.applyInverse(a3,y,y); // in-place
      assertEqual(x,y);
    }

    { // x == A'B'x (for B = inv(A))
      float[][][] x = rands(n1,n2,n3);
      float[][][] y = zeros(n1,n2,n3);
      lcf.applyInverseTranspose(a3,x,y); // *not* in-place
      lcf.applyTranspose(a3,y,y); // in-place
      assertEqual(x,y);
    }
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

  /*
  public void test1TwoPoles() {
    final int n = 1001;
    final float f = 0.0f;
    final float c = cos(2.0f*FLT_PI*f);
    final float[] r = new float[n];
    float[] x = new float[n];
    for (int i=0; i<n; ++i) {
      x[i] = (i%(n/8)==1)?1.0f:0.0f;
      r[i] = 0.1f+0.8f*sin(FLT_PI*i/(n-1));
    }
    int[] lag1 = {0,1,2};
    LocalCausalFilter lcf = new LocalCausalFilter(lag1);
    LocalCausalFilter.A1 a1 = new LocalCausalFilter.A1() {
      public void get(int i, float[] a) {
        a[0] = 1.0f;
        a[1] = -2.0f*c*r[i];
        a[2] = r[i]*r[i];
      }
    };
    float[] y = new float[n];
    float[] z = new float[n];
    lcf.applyInverseTranspose(a1,x,z);
    lcf.applyInverse(a1,z,z);
    edu.mines.jtk.mosaic.SimplePlot.asSequence(z);
    LocalCausalFilter.A12 a12 = new LocalCausalFilter.A12() {
      public void get(int i, float[] s, float[][] a) {
        float ft = (r[i]-_ftable)*_stable;
        int it = (int)(ft);
        s[1] = ft-it;
        s[0] = 1.0f-s[1];
        a[0] = _atable[it];
        a[1] = _atable[it+1];
        if (540<i &&  i<560)
          System.out.println("i="+i+" it="+it+" s0="+s[0]+" s1="+s[1]);
      }
      private int _ntable = 19;
      private float _dtable = 1.0f/(_ntable-1);
      private float _ftable = 0.0f;
      private float _stable = 0.999999f/_dtable;
      private float[][] _atable = new float[_ntable][];
      {
        for (int itable=0; itable<_ntable; ++itable) {
          float r = _ftable+itable*_dtable;
          _atable[itable] = new float[]{1.0f,-2.0f*r*c,r*r};
        }
      }
    };
    LocalCausalFilter.A1 a1Average = new LocalCausalFilter.A1() {
      public void get(int i, float[] a) {
        float ft = (r[i]-_ftable)*_stable;
        int it = (int)(ft);
        float s1 = ft-it;
        float s0 = 1.0f-s1;
        float[] at0 = _atable[it];
        float[] at1 = _atable[it];
        a[0] = s0*at0[0]+s1*at1[0];
        a[1] = s0*at0[1]+s1*at1[1];
        a[2] = s0*at0[2]+s1*at1[2];
      }
      private int _ntable = 49;
      private float _dtable = 1.0f/(_ntable-1);
      private float _ftable = 0.0f;
      private float _stable = 0.999999f/_dtable;
      private float[][] _atable = new float[_ntable][];
      {
        for (int itable=0; itable<_ntable; ++itable) {
          float r = _ftable+itable*_dtable;
          _atable[itable] = new float[]{1.0f,-2.0f*r*c,r*r};
        }
      }
    };
    LocalCausalFilter.A1 a1Lo = new LocalCausalFilter.A1() {
      public void get(int i, float[] a) {
        float ft = (r[i]-_ftable)*_stable;
        int it = (int)(ft);
        float[] at = _atable[it];
        a[0] = at[0];
        a[1] = at[1];
        a[2] = at[2];
      }
      private int _ntable = 19;
      private float _dtable = 1.0f/(_ntable-1);
      private float _ftable = 0.0f;
      private float _stable = 0.999999f/_dtable;
      private float[][] _atable = new float[_ntable][];
      {
        for (int itable=0; itable<_ntable; ++itable) {
          float r = _ftable+itable*_dtable;
          _atable[itable] = new float[]{1.0f,-2.0f*r*c,r*r};
        }
      }
    };
    LocalCausalFilter.A1 a1Hi = new LocalCausalFilter.A1() {
      public void get(int i, float[] a) {
        float ft = (r[i]-_ftable)*_stable;
        int it = (int)(ft);
        float[] at = _atable[it+1];
        a[0] = at[0];
        a[1] = at[1];
        a[2] = at[2];
      }
      private int _ntable = 19;
      private float _dtable = 1.0f/(_ntable-1);
      private float _ftable = 0.0f;
      private float _stable = 0.999999f/_dtable;
      private float[][] _atable = new float[_ntable][];
      {
        for (int itable=0; itable<_ntable; ++itable) {
          float r = _ftable+itable*_dtable;
          _atable[itable] = new float[]{1.0f,-2.0f*r*c,r*r};
        }
      }
    };
    lcf.applyInverseTranspose(a1Average,x,z);
    lcf.applyInverse(a1Average,z,z);
    edu.mines.jtk.mosaic.SimplePlot.asSequence(z);
  }
  */
}
