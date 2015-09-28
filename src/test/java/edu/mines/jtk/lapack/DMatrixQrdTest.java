/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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
package edu.mines.jtk.lapack;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.lapack.DMatrixTest.assertEqualFuzzy;

/**
 * Tests {@link edu.mines.jtk.lapack.DMatrixQrd}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.14
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
