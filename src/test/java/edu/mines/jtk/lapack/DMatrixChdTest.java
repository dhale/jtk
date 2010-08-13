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

/**
 * Tests {@link edu.mines.jtk.lapack.DMatrixChd}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.12
 */
public class DMatrixChdTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(DMatrixChdTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testSimple() {
    DMatrix a = new DMatrix(new double[][]{
      {1.0,  1.0},
      {1.0,  4.0},
    });
    test(a);
    DMatrixChd chd = new DMatrixChd(a);
    assertEqualFuzzy(3.0,chd.det());
  }

  public void testSimple2() {
    DMatrix a = new DMatrix(new double[][]{
      {4.0, 1.0, 1.0},
      {1.0, 2.0, 3.0},
      {1.0, 3.0, 6.0},
    });
    test(a);
  }

  public void testNotPositiveDefinite() {
    DMatrix a = new DMatrix(new double[][]{
      {0.0, 1.0, 1.0},
      {0.0, 2.0, 3.0},
      {0.0, 3.0, 6.0},
    });
    DMatrixChd chd = new DMatrixChd(a);
    assertFalse(chd.isPositiveDefinite());
    assertEqualExact(0.0,chd.det());
  }

  public void testRandom() {
    int n = 10;
    DMatrix a = DMatrix.random(n,n);
    a.plusEquals(a.transpose());
    DMatrix d = DMatrix.identity(n,n);
    d.timesEquals(n*a.norm1());
    a.plusEquals(d);
    test(a);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private void test(DMatrix a) {
    int m = a.getM();

    DMatrixChd chd = new DMatrixChd(a);
    assertTrue(chd.isPositiveDefinite());
    DMatrix l = chd.getL();
    DMatrix lt = l.transpose();
    DMatrix llt = l.times(lt);
    assertEqualFuzzy(a,llt);

    int nrhs = 10;
    DMatrix b = DMatrix.random(m,nrhs);
    DMatrix x = chd.solve(b);
    DMatrix ax = a.times(x);
    assertEqualFuzzy(ax,b);
  }
}
