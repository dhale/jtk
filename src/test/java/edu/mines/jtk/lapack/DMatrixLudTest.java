/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.lapack.DMatrixTest.assertEqualFuzzy;

/**
 * Tests {@link edu.mines.jtk.lapack.DMatrixLud}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.12
 */
public class DMatrixLudTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(DMatrixLudTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testSimple() {
    DMatrix a = new DMatrix(new double[][]{
      {0.0,  2.0},
      {3.0,  4.0},
    });
    test(a);
    DMatrixLud lud = new DMatrixLud(a);
    assertEqualFuzzy(-6.0,lud.det());
  }

  public void testRandom() {
    test(DMatrix.random(100,100));
    test(DMatrix.random(101,100));
    test(DMatrix.random(100,101));
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private void test(DMatrix a) {
    int m = a.getM();
    int n = a.getN();

    DMatrixLud lud = new DMatrixLud(a);
    assertFalse(lud.isSingular());
    int[] pi = lud.getPivotIndices();
    DMatrix p = lud.getP();
    DMatrix l = lud.getL();
    DMatrix u = lud.getU();
    DMatrix lu = l.times(u);
    DMatrix plu = p.times(lu);
    DMatrix ap = a.get(pi,null);
    assertEqualFuzzy(ap,lu);
    assertEqualFuzzy(a,plu);

    if (m==n) {
      int nrhs = 10;
      DMatrix b = DMatrix.random(m,nrhs);
      DMatrix x = lud.solve(b);
      DMatrix ax = a.times(x);
      assertEqualFuzzy(ax,b);
    }
  }
}
