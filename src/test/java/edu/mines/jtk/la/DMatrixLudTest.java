/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.la.DMatrixTest.assertEqualFuzzy;

/**
 * Tests {@link edu.mines.jtk.la.DMatrixLud}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.09.15
 */
public class DMatrixLudTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(DMatrixLudTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testSingular() {
    DMatrix a = new DMatrix(new double[][]{
      {0.0,  0.0},
      {3.0,  4.0},
    });
    DMatrixLud lud = new DMatrixLud(a);
    assertTrue(lud.isSingular());
    assertFalse(lud.isNonSingular());
  }

  public void testSimple() {
    test(new DMatrix(new double[][]{
      {0.0,  2.0},
      {3.0,  4.0},
    }));
    test(new DMatrix(new double[][]{
      {0.0,  2.0},
      {3.0,  4.0},
      {5.0,  6.0},
    }));
  }

  public void testRandom() {
    test(DMatrix.random(100,100));
    test(DMatrix.random(101,100));
  }

  private void test(DMatrix a) {
    int m = a.getM();
    int n = a.getN();

    DMatrixLud lud = new DMatrixLud(a);
    int[] piv = lud.getPivot();
    DMatrix l = lud.getL();
    DMatrix u = lud.getU();
    DMatrix lu = l.times(u);
    assertEqualFuzzy(a.get(piv,null),lu);

    if (m==n) {
      int nrhs = 2;
      DMatrix b = DMatrix.random(m,nrhs);
      DMatrix x = lud.solve(b);
      DMatrix ax = a.times(x);
      assertEqualFuzzy(ax,b);
    }
  }
}
