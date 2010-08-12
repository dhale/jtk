/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.copy;
import static edu.mines.jtk.util.ArrayMath.randfloat;

/**
 * Tests {@link edu.mines.jtk.util.SimpleFloat3}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.05.24
 */
public class SimpleFloat3Test extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(SimpleFloat3Test.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test123() {
    int n1 = 10;
    int n2 = 11;
    int n3 = 12;
    float[][][] a = randfloat(n1,n2,n3);
    SimpleFloat3 sf3 = new SimpleFloat3(a);
    float[][][] c = copy(a);
    test1(sf3,c);
    test2(sf3,c);
    test3(sf3,c);
  }

  public void testRandom() {
    int n1 = 10;
    int n2 = 11;
    int n3 = 12;
    float[][][] a = randfloat(n1,n2,n3);
    SimpleFloat3 sf3 = new SimpleFloat3(a);
    float[][][] c = copy(a);
    testRandom1(sf3,c);
    testRandom2(sf3,c);
    testRandom3(sf3,c);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Random _random = new Random();
  private static final int NTRIAL = 10000;

  private void test1(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    float[] b1 = new float[n1];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        f3.get1(n1,0,i2,i3,b1);
        f3.set1(n1,0,i2,i3,b1);
        assertEqual1(n1,0,i2,i3,a,b1);
      }
    }
    float[] b2 = new float[n2];
    for (int i3=0; i3<n3; ++i3) {
      for (int i1=0; i1<n1; ++i1) {
        f3.get2(n2,i1,0,i3,b2);
        f3.set2(n2,i1,0,i3,b2);
        assertEqual2(n2,i1,0,i3,a,b2);
      }
    }
    float[] b3 = new float[n3];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        f3.get3(n3,i1,i2,0,b3);
        f3.set3(n3,i1,i2,0,b3);
        assertEqual3(n3,i1,i2,0,a,b3);
      }
    }
  }

  private void test2(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    float[][] b12 = new float[n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      f3.get12(n1,n2,0,0,i3,b12);
      f3.set12(n1,n2,0,0,i3,b12);
      assertEqual12(n1,n2,0,0,i3,a,b12);
    }
    float[][] b13 = new float[n3][n1];
    for (int i2=0; i2<n2; ++i2) {
      f3.get13(n1,n3,0,i2,0,b13);
      f3.set13(n1,n3,0,i2,0,b13);
      assertEqual13(n1,n3,0,i2,0,a,b13);
    }
    float[][] b23 = new float[n3][n2];
    for (int i1=0; i1<n1; ++i1) {
      f3.get23(n2,n3,i1,0,0,b23);
      f3.set23(n2,n3,i1,0,0,b23);
      assertEqual23(n2,n3,i1,0,0,a,b23);
    }
  }

  private void test3(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    float[][][] b123 = new float[n3][n2][n1];
    f3.get123(n1,n2,n3,0,0,0,b123);
    f3.set123(n1,n2,n3,0,0,0,b123);
    assertEqual123(n1,n2,n3,0,0,0,a,b123);
    float[] b = new float[n1*n2*n3];
    f3.get123(n1,n2,n3,0,0,0,b);
    f3.set123(n1,n2,n3,0,0,0,b);
    assertEqual123(n1,n2,n3,0,0,0,a,b);
  }

  private void testRandom1(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    int ntrial = NTRIAL;
    float[] b1 = new float[n1];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1+_random.nextInt(n1-1);
      int m2 = 1;
      int m3 = 1;
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get1(m1,j1,j2,j3,b1);
      f3.set1(m1,j1,j2,j3,b1);
      assertEqual1(m1,j1,j2,j3,a,b1);
    }
    float[] b2 = new float[n2];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1;
      int m2 = 1+_random.nextInt(n2-1);
      int m3 = 1;
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get2(m2,j1,j2,j3,b2);
      f3.set2(m2,j1,j2,j3,b2);
      assertEqual2(m2,j1,j2,j3,a,b2);
    }
    float[] b3 = new float[n3];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1;
      int m2 = 1;
      int m3 = 1+_random.nextInt(n3-1);
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get3(m3,j1,j2,j3,b3);
      f3.set3(m3,j1,j2,j3,b3);
      assertEqual3(m3,j1,j2,j3,a,b3);
    }
  }

  private void testRandom2(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    int ntrial = NTRIAL;
    float[][] b12 = new float[n2][n1];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1+_random.nextInt(n1-1);
      int m2 = 1+_random.nextInt(n2-1);
      int m3 = 1;
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get12(m1,m2,j1,j2,j3,b12);
      f3.set12(m1,m2,j1,j2,j3,b12);
      assertEqual12(m1,m2,j1,j2,j3,a,b12);
    }
    float[][] b13 = new float[n3][n1];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1+_random.nextInt(n1-1);
      int m2 = 1;
      int m3 = 1+_random.nextInt(n3-1);
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get13(m1,m3,j1,j2,j3,b13);
      f3.set13(m1,m3,j1,j2,j3,b13);
      assertEqual13(m1,m3,j1,j2,j3,a,b13);
    }
    float[][] b23 = new float[n3][n2];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1;
      int m2 = 1+_random.nextInt(n2-1);
      int m3 = 1+_random.nextInt(n3-1);
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get23(m2,m3,j1,j2,j3,b23);
      f3.set23(m2,m3,j1,j2,j3,b23);
      assertEqual23(m2,m3,j1,j2,j3,a,b23);
    }
  }

  private void testRandom3(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    int ntrial = NTRIAL;
    float[][][] b123 = new float[n3][n2][n1];
    float[] b = new float[n1*n2*n3];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1+_random.nextInt(n1-1);
      int m2 = 1+_random.nextInt(n2-1);
      int m3 = 1+_random.nextInt(n3-1);
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get123(m1,m2,m3,j1,j2,j3,b123);
      f3.set123(m1,m2,m3,j1,j2,j3,b123);
      assertEqual123(m1,m2,m3,j1,j2,j3,a,b123);
      f3.get123(m1,m2,m3,j1,j2,j3,b);
      f3.set123(m1,m2,m3,j1,j2,j3,b);
      assertEqual123(m1,m2,m3,j1,j2,j3,a,b);
    }
  }

  private static void assertEqual1(
    int m1,
    int j1, int j2, int j3,
    float[][][] a, float[] s) 
  {
    assertEqual123(m1,1,1,j1,j2,j3,a,s);
  }

  private static void assertEqual2(
    int m2,
    int j1, int j2, int j3,
    float[][][] a, float[] s) 
  {
    assertEqual123(1,m2,1,j1,j2,j3,a,s);
  }

  private static void assertEqual3(
    int m3,
    int j1, int j2, int j3,
    float[][][] a, float[] s) 
  {
    assertEqual123(1,1,m3,j1,j2,j3,a,s);
  }

  private static void assertEqual12(
    int m1, int m2,
    int j1, int j2, int j3,
    float[][][] a, float[][] s) 
  {
    for (int i2=0; i2<m2; ++i2) {
      for (int i1=0; i1<m1; ++i1) {
        assertEquals(a[j3][i2+j2][i1+j1],s[i2][i1]);
      }
    }
  }

  private static void assertEqual13(
    int m1, int m3,
    int j1, int j2, int j3,
    float[][][] a, float[][] s) 
  {
    for (int i3=0; i3<m3; ++i3) {
      for (int i1=0; i1<m1; ++i1) {
        assertEquals(a[i3+j3][j2][i1+j1],s[i3][i1]);
      }
    }
  }

  private static void assertEqual23(
    int m2, int m3,
    int j1, int j2, int j3,
    float[][][] a, float[][] s) 
  {
    for (int i3=0; i3<m3; ++i3) {
      for (int i2=0; i2<m2; ++i2) {
        assertEquals(a[i3+j3][i2+j2][j1],s[i3][i2]);
      }
    }
  }

  private static void assertEqual123(
    int m1, int m2, int m3, 
    int j1, int j2, int j3,
    float[][][] a, float[][][] s) 
  {
    for (int i3=0; i3<m3; ++i3) {
      for (int i2=0; i2<m2; ++i2) {
        for (int i1=0; i1<m1; ++i1) {
          assertEquals(a[i3+j3][i2+j2][i1+j1],s[i3][i2][i1]);
        }
      }
    }
  }

  private static void assertEqual123(
    int m1, int m2, int m3, 
    int j1, int j2, int j3,
    float[][][] a, float[] s) 
  {
    for (int i3=0; i3<m3; ++i3) {
      for (int i2=0; i2<m2; ++i2) {
        for (int i1=0; i1<m1; ++i1) {
          assertEquals(a[i3+j3][i2+j2][i1+j1],s[i3*m2*m1+i2*m1+i1]);
        }
      }
    }
  }
}
