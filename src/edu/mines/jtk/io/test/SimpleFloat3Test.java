/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.io.test;

import junit.framework.*;
import java.util.Random;
import edu.mines.jtk.io.*;
import edu.mines.jtk.util.Array;

/**
 * Tests {@link edu.mines.jtk.io.SimpleFloat3}.
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
    float[][][] a = Array.randfloat(n1,n2,n3);
    SimpleFloat3 sf3 = new SimpleFloat3(a);
    float[][][] c = Array.copy(a);
    test1(sf3,c);
    test2(sf3,c);
    test3(sf3,c);
  }

  public void testRandom() {
    int n1 = 10;
    int n2 = 11;
    int n3 = 12;
    float[][][] a = Array.randfloat(n1,n2,n3);
    SimpleFloat3 sf3 = new SimpleFloat3(a);
    float[][][] c = Array.copy(a);
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
        f3.get(     n1,1,1,0,i2,i3,b1);
        f3.set(     n1,1,1,0,i2,i3,b1);
        assertEqual(n1,1,1,0,i2,i3,a,b1);
      }
    }
    float[] b2 = new float[n2];
    for (int i3=0; i3<n3; ++i3) {
      for (int i1=0; i1<n1; ++i1) {
        f3.get(     1,n2,1,i1,0,i3,b2);
        f3.set(     1,n2,1,i1,0,i3,b2);
        assertEqual(1,n2,1,i1,0,i3,a,b2);
      }
    }
    float[] b3 = new float[n3];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        f3.get(     1,1,n3,i1,i2,0,b3);
        f3.set(     1,1,n3,i1,i2,0,b3);
        assertEqual(1,1,n3,i1,i2,0,a,b3);
      }
    }
  }

  private void test2(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    float[][] b12 = new float[n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      f3.get(     n1,n2,1,0,0,i3,b12);
      assertEqual(n1,n2,1,0,0,i3,a,b12);
    }
    float[][] b13 = new float[n3][n1];
    for (int i2=0; i2<n2; ++i2) {
      f3.get(     n1,1,n3,0,i2,0,b13);
      assertEqual(n1,1,n3,0,i2,0,a,b13);
    }
    float[][] b23 = new float[n3][n2];
    for (int i1=0; i1<n1; ++i1) {
      f3.get(     1,n2,n3,i1,0,0,b23);
      assertEqual(1,n2,n3,i1,0,0,a,b23);
    }
  }

  private void test3(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    float[][][] b123 = new float[n3][n2][n1];
    f3.get(     n1,n2,n3,0,0,0,b123);
    assertEqual(n1,n2,n3,0,0,0,a,b123);
  }

  private void testRandom1(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    int ntrial = NTRIAL;
    float[] b = new float[n1*n2*n3];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1+_random.nextInt(n1-1);
      int m2 = 1+_random.nextInt(n2-1);
      int m3 = 1+_random.nextInt(n3-1);
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get(m1,m2,m3,j1,j2,j3,b);
      f3.set(m1,m2,m3,j1,j2,j3,b);
      assertEqual(m1,m2,m3,j1,j2,j3,a,b);
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
      f3.get(m1,m2,m3,j1,j2,j3,b12);
      f3.set(m1,m2,m3,j1,j2,j3,b12);
      assertEqual(m1,m2,m3,j1,j2,j3,a,b12);
    }
    float[][] b13 = new float[n3][n1];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1+_random.nextInt(n1-1);
      int m2 = 1;
      int m3 = 1+_random.nextInt(n3-1);
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get(m1,m2,m3,j1,j2,j3,b13);
      f3.set(m1,m2,m3,j1,j2,j3,b13);
      assertEqual(m1,m2,m3,j1,j2,j3,a,b13);
    }
    float[][] b23 = new float[n3][n2];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1;
      int m2 = 1+_random.nextInt(n2-1);
      int m3 = 1+_random.nextInt(n3-1);
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get(m1,m2,m3,j1,j2,j3,b23);
      f3.set(m1,m2,m3,j1,j2,j3,b23);
      assertEqual(m1,m2,m3,j1,j2,j3,a,b23);
    }
  }

  private void testRandom3(Float3 f3, float[][][] a) {
    int n1 = f3.getN1();
    int n2 = f3.getN2();
    int n3 = f3.getN3();
    int ntrial = NTRIAL;
    float[][][] b = new float[n3][n2][n1];
    for (int itrial=0; itrial<ntrial; ++itrial) {
      int m1 = 1+_random.nextInt(n1-1);
      int m2 = 1+_random.nextInt(n2-1);
      int m3 = 1+_random.nextInt(n3-1);
      int j1 = _random.nextInt(n1-m1);
      int j2 = _random.nextInt(n2-m2);
      int j3 = _random.nextInt(n3-m3);
      f3.get(m1,m2,m3,j1,j2,j3,b);
      f3.set(m1,m2,m3,j1,j2,j3,b);
      assertEqual(m1,m2,m3,j1,j2,j3,a,b);
    }
  }

  private static void assertEqual(
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

  private static void assertEqual(
    int m1, int m2, int m3, 
    int j1, int j2, int j3,
    float[][][] a, float[][] s) 
  {
    if (m1==1) {
      for (int i3=0; i3<m3; ++i3) {
        for (int i2=0; i2<m2; ++i2) {
          assertEquals(a[i3+j3][i2+j2][j1],s[i3][i2]);
        }
      }
    } else if (m2==1) {
      for (int i3=0; i3<m3; ++i3) {
        for (int i1=0; i1<m1; ++i1) {
          assertEquals(a[i3+j3][j2][i1+j1],s[i3][i1]);
        }
      }
    } else {
      for (int i2=0; i2<m2; ++i2) {
        for (int i1=0; i1<m1; ++i1) {
          assertEquals(a[j3][i2+j2][i1+j1],s[i2][i1]);
        }
      }
    }
  }

  private static void assertEqual(
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
