/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.la;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

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
    float[] a = randfloat(n);
    float[] b = randfloat(n);
    float[] c = randfloat(n);
    for (int i=0; i<n; ++i)
      b[i] += a[i]+c[i]; // ensure non-singular (and positive-definite)
    TridiagonalFMatrix t = new TridiagonalFMatrix(n,a,b,c);
    float[] r = randfloat(n);
    float[] u = zerofloat(n);
    t.solve(r,u);
    float[] s = t.times(u);
    assertEqualFuzzy(r,s);
  }

  private static void assertEqualFuzzy(float[] a, float[] b) {
    int n = a.length;
    double eps = 0.000001*max(max(a),max(b));
    for (int j=0; j<n; ++j) {
      assertEquals(a[j],b[j],eps);
    }
  }
}
