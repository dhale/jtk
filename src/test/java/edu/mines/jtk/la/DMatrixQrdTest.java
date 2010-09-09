/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.la.DMatrixTest.assertEqualFuzzy;

/**
 * Tests {@link edu.mines.jtk.la.DMatrixQrd}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.13
 */
public class DMatrixQrdTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(DMatrixQrdTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testRankDeficient() {
    DMatrix a = new DMatrix(new double[][]{
      {0.0,  0.0},
      {3.0,  4.0},
    });
    DMatrixQrd qrd = new DMatrixQrd(a);
    assertFalse(qrd.isFullRank());
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

    DMatrixQrd qrd = new DMatrixQrd(a);
    DMatrix q = qrd.getQ();
    DMatrix r = qrd.getR();
    DMatrix qr = q.times(r);
    assertEqualFuzzy(a,qr);

    if (m==n) {
      int nrhs = 2;
      DMatrix b = DMatrix.random(m,nrhs);
      DMatrix x = qrd.solve(b);
      DMatrix ax = a.times(x);
      assertEqualFuzzy(ax,b);
    }
  }
}
