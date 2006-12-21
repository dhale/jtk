/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.Array;
import edu.mines.jtk.la.TridiagonalFMatrix;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.la.TridiagonalFMatrix}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.12.12
 */
public class TridiagonalFMatrixTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(TridiagonalFMatrixTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testSolve() {
    int n = 100;
    float[] a = Array.randfloat(n);
    float[] b = Array.randfloat(n);
    float[] c = Array.randfloat(n);
    for (int i=0; i<n; ++i)
      b[i] += a[i]+c[i]; // ensure non-singular (and positive-definite)
    TridiagonalFMatrix t = new TridiagonalFMatrix(n,a,b,c);
    float[] r = Array.randfloat(n);
    float[] u = Array.zerofloat(n);
    t.solve(r,u);
    float[] s = t.times(u);
    assertEqualFuzzy(r,s);
  }

  private static void assertEqualFuzzy(float[] a, float[] b) {
    int n = a.length;
    double eps = 0.000001*max(Array.max(a),Array.max(b));
    for (int j=0; j<n; ++j) {
      assertEquals(a[j],b[j],eps);
    }
  }
}
