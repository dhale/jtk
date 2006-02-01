/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;
import java.util.Random;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.Array;
import static edu.mines.jtk.util.MathPlus.*;

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

  public void testRandom() {
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
      if (k>0)
        assertTrue(d[k-1]>=d[k]);
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
