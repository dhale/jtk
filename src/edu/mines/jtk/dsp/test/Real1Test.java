/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;
import edu.mines.jtk.dsp.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.dsp.Real1}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.10
 */
public class Real1Test extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(Real1Test.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testMath() {
    int n = 100;
    Real1 ra = Real1.ramp(n,1.0,0.0);
    Real1 rb = Real1.ramp(n,1.0,0.0);
    Real1 rc = ra.plus(rb);
    Real1 re = Real1.ramp(n,2.0,0.0);
    assertEquals(re,rc,10*n*FLT_EPSILON);
  }

  void assertEquals(Real1 e, Real1 a, double tiny) {
    Sampling se = e.getX1();
    Sampling sa = a.getX1();
    assertTrue(sa.isEquivalentTo(se));
    float[] fe = e.getValues();
    float[] fa = a.getValues();
    int n = fe.length;
    for (int i=0; i<n; ++i)
      assertEquals(fe[i],fa[i],tiny);
  }
}
