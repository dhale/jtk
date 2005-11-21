/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;
import edu.mines.jtk.util.Array;
import edu.mines.jtk.dsp.Recursive2ndOrderFilter;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.dsp.Recursive2ndOrderFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.10
 */
public class Recursive2ndOrderFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(Recursive2ndOrderFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1() {
    int n = 100;
    float[] x = Array.randfloat(n);
    float b0 = 1.00f;
    float b1 = 2.00f;
    float b2 = 1.00f;
    float a1 = 1.80f;
    float a2 = 0.81f;
    Recursive2ndOrderFilter rf = new Recursive2ndOrderFilter(b0,b1,b2,a1,a2);

    float[] y1 = Array.copy(x);
    rf.applyForward(y1,y1);
    float[] y2 = Array.reverse(x);
    rf.applyReverse(y2,y2);
    y2 = Array.reverse(y2);
    assertEqual(y1,y2);

    rf.accumulateForward(y1,y1);
    y2 = Array.reverse(y2);
    rf.accumulateReverse(y2,y2);
    y2 = Array.reverse(y2);
    assertEqual(y1,y2);
  }

  private void assertEqual(float[] re, float[] ra) {
    int n = re.length;
    float tolerance = (float)(n)*FLT_EPSILON;
    for (int i=0; i<n; ++i)
      assertEquals(re[i],ra[i],tolerance);
  }
}
