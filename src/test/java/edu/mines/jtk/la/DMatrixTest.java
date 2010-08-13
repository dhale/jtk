/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la;

import static java.lang.Math.max;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.la.DMatrix}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.07
 */
public class DMatrixTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(DMatrixTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testConstruct() {
    int m = 3;
    int n = 4;

    DMatrix z1 = new DMatrix(m,n);
    DMatrix z2 = new DMatrix(m,n,0.0);
    assertEqualExact(z1,z2);
    assertTrue(z1.equals(z2));
    assertFalse(z1.isSquare());
    assertFalse(z1.isSymmetric());

    DMatrix r1 = DMatrix.random(m,n);
    DMatrix r2 = new DMatrix(r1);
    assertEqualExact(r1,r2);
    assertTrue(r1.equals(r2));
    assertNotSame(r1.getArray(),r2.getArray());

    DMatrix r3 = DMatrix.random(m,n);
    assertFalse(r1.equals(r3));

    DMatrix i1 = DMatrix.identity(m,n);
    DMatrix i2 = new DMatrix(new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0}});
    assertTrue(z1.equals(z2));
    assertEqualExact(i1,i2);
  }

  public void testGetSet() {
    int m = 3;
    int n = 4;
    DMatrix s = new DMatrix(m,n);

    DMatrix r = DMatrix.random(m,n);
    assertEquals(m,r.getM());
    assertEquals(n,r.getN());
    assertEquals(m,r.getRowCount());
    assertEquals(n,r.getColumnCount());

    for (int i=0; i<m; ++i) {
      for (int j=0; j<n; ++j) {
        s.set(i,j,r.get(i,j));
      }
    }
    assertEqualExact(r,s);

    for (int i=0; i<m; ++i)
      s.set(i,null,r.get(i,null));
    assertEqualExact(r,s);

    for (int j=0; j<n; ++j)
      s.set(null,j,r.get(null,j));
    assertEqualExact(r,s);

    for (int i=0; i<m-1; ++i)
      s.set(i,i+1,null,r.get(i,i+1,null));
    assertEqualExact(r,s);

    for (int j=0; j<n-1; ++j)
      s.set(null,j,j+1,r.get(null,j,j+1));
    assertEqualExact(r,s);

    for (int i=0; i<m-1; ++i)
      for (int j=0; j<n-1; ++j)
      s.set(i,i+1,j,j+1,r.get(i,i+1,j,j+1));
    assertEqualExact(r,s);

    int[] jc = {n-1,1};
    for (int i=0; i<m; ++i)
      s.set(i,jc,r.get(i,jc));
    assertEqualExact(r,s);

    int[] ir = {m-1,1};
    for (int j=0; j<n; ++j)
      s.set(ir,j,r.get(ir,j));
    assertEqualExact(r,s);

    s.set(ir,jc,r.get(ir,jc));
    assertEqualExact(r,s);

    s.setPackedColumns(r.getPackedColumns());
    assertEqualExact(r,s);

    s.setPackedRows(r.getPackedRows());
    assertEqualExact(r,s);
  }

  public void testOther() {
    int m = 3;
    int n = 4;

    DMatrix r = DMatrix.random(m,n);
    DMatrix s = DMatrix.random(m,n);
    assertFalse(s.equals(r));

    assertEqualFuzzy(r,r.plus(s).minus(s));
    assertEqualFuzzy(r,r.times(2.0).times(0.5));

    DMatrix r0 = new DMatrix(r);
    assertEqualFuzzy(r0,r.negate().negate());
    assertEqualFuzzy(r0,r.transpose().transpose());
    assertEqualFuzzy(r0,r.plusEquals(s).minusEquals(s));
    assertEqualFuzzy(r0,r.timesEquals(2.0).timesEquals(0.5));
    assertEqualFuzzy(r0,r.arrayTimes(s).arrayRightDivide(s));
    assertEqualFuzzy(r0,r.arrayTimesEquals(s).arrayRightDivideEquals(s));
    assertEqualExact(r.arrayRightDivide(s),s.arrayLeftDivide(r));
    assertEqualFuzzy(r0,r.arrayLeftDivideEquals(s).arrayLeftDivideEquals(s));

    DMatrix t = r.times(r.transpose());
    t.plusEquals(t.transpose()).times(0.5);
    assertTrue(t.isSymmetric());

    n = t.getN();
    double[][] a = t.getArray();
    double trace = 0.0;
    for (int i=0; i<n; ++i)
      trace += a[i][i];
    assertTrue(trace==t.trace());
  }


  ///////////////////////////////////////////////////////////////////////////
  // package

  static void assertEqualExact(DMatrix a, DMatrix b) {
    assertEqual(a,b,false);
  }

  static void assertEqualFuzzy(DMatrix a, DMatrix b) {
    assertEqual(a,b,true);
  }

  static void assertEqual(DMatrix a, DMatrix b, boolean fuzzy) {
    assertEquals(a.getM(),b.getM());
    assertEquals(a.getN(),b.getN());
    int m = a.getM();
    int n = a.getN();
    double eps = (fuzzy)?0.000001*max(a.normF(),b.normF()):0.0;
    for (int i=0; i<m; ++i) {
      for (int j=0; j<n; ++j) {
        assertEquals(a.get(i,j),b.get(i,j),eps);
      }
    }
  }
}
