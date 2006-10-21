/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.dsp.Eigen;
import edu.mines.jtk.util.Array;

/**
 * Tests {@link edu.mines.jtk.dsp.Eigen}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.31
 */
public class EigenTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(EigenTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testSymmetric22() {
    int nrand = 10;
    for (int irand=0; irand<nrand; ++irand) {
      float[][] a = Array.randfloat(2,2);
      a = Array.add(a,Array.transpose(a));
      float[][] v = new float[2][2];
      float[] d = new float[2];
      Eigen.solveSymmetric22(a,v,d);
      check(a,v,d);
    }
  }

  public void testSymmetric33() {
    int nrand = 10;
    for (int irand=0; irand<nrand; ++irand) {
      float[][] a = Array.randfloat(3,3);
      a = Array.add(a,Array.transpose(a));
      float[][] v = new float[3][3];
      float[] d = new float[3];
      Eigen.solveSymmetric33(a,v,d);
      check(a,v,d);
    }
  }

  private void check(float[][] a, float[][] v, float[] d) {
    int n = a.length;
    for (int k=0; k<n; ++k) {
      assertTrue(k==0 || d[k-1]>=d[k]);
      for (int i=0; i<n; ++i) {
        float av = 0.0f;
        for (int j=0; j<n; ++j) {
          av += a[i][j]*v[k][j];
        }
        float vd = v[k][i]*d[k];
        assertEquals(av,vd,0.001);
      }
    }
  }
}
