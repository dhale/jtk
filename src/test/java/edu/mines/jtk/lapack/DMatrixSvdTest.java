/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.lapack.DMatrixTest.assertEqualExact;
import static edu.mines.jtk.lapack.DMatrixTest.assertEqualFuzzy;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.lapack.DMatrixSvd}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.15
 */
public class DMatrixSvdTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(DMatrixSvdTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testSimple() {
    test(new DMatrix(new double[][]{
      {1.0, 0.0},
      {0.0, 2.0},
      {0.0, 0.0},
    }));
    test(new DMatrix(new double[][]{
      {0.0, 2.0},
      {3.0, 4.0},
    }));
    test(new DMatrix(new double[][]{
      {0.0, 2.0},
      {3.0, 4.0},
      {5.0,  6.0},
    }));
  }

  public void testRandom() {
    test(DMatrix.random(100,100));
    test(DMatrix.random(10,100));
    test(DMatrix.random(100,10));
  }

  public void testCond() {
    DMatrix a = new DMatrix(new double[][]{
      {1.0, 3.0},
      {7.0, 9.0},
    });
    int m = a.getM();
    int n = a.getN();
    DMatrixSvd svd = new DMatrixSvd(a);
    double[] s = svd.getSingularValues();
    double smax = max(s);
    assertEqualExact(s[0],smax);
    double smin = min(s);
    assertEqualExact(s[min(m,n)-1],smin);
    double cond = svd.cond();
    assertEqualExact(smax/smin,cond);
  }

  public void testRank() {
    DMatrix a = new DMatrix(new double[][]{
      {1.0, 3.0},
      {7.0, 9.0},
    });
    DMatrixSvd svda = new DMatrixSvd(a);
    assertEqualExact(svda.rank(),2);
    DMatrix b = new DMatrix(new double[][]{
      {1.0, 3.0},
      {7.0, 9.0},
      {0.0, 0.0},
    });
    DMatrixSvd svdb = new DMatrixSvd(b);
    assertEqualExact(svdb.rank(),2);
    DMatrix c = new DMatrix(new double[][]{
      {1.0, 3.0, 0.0},
      {7.0, 9.0, 0.0},
    });
    DMatrixSvd svdc = new DMatrixSvd(c);
    assertEqualExact(svdc.rank(),2);
  }

  private void test(DMatrix a) {
    DMatrixSvd svd = new DMatrixSvd(a);
    DMatrix u = svd.getU();
    DMatrix s = svd.getS();
    DMatrix vt = svd.getVTranspose();
    DMatrix usvt = u.times(s).times(vt);
    assertEqualFuzzy(a,usvt);
  }
}
