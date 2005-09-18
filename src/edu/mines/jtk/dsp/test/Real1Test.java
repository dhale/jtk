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

  static final int N = 100;
  static final double TINY = N*10*FLT_EPSILON;
  static final Real1 FILL1 = Real1.fill(1.0,N);
  static final Real1 FILL2 = Real1.fill(2.0,N);
  static final Real1 RAMP1 = Real1.ramp(0.0,1.0,N);
  static final Real1 RAMP2 = Real1.ramp(0.0,2.0,N);

  public void testMath() {
    Real1 ra = RAMP1;
    Real1 rb = RAMP1;
    Real1 rc = ra.plus(rb);
    Real1 re = RAMP2;
    assertEquals(re,rc);
  }

  public void testResample() {
    Real1 ra = FILL1;
    Sampling sa = ra.getT();
    int n1 = sa.getCount();
    double d1 = sa.getDelta();

    int m1 = n1/3;
    Sampling sb = sa.shift(-m1*d1);
    Real1 rb = ra.resample(sb);
    float[] xb = rb.getX();
    for (int i1=0; i1<m1; ++i1)
      assertEquals(0.0,xb[i1],0.0);
    for (int i1=m1; i1<n1; ++i1)
      assertEquals(1.0,xb[i1],0.0);

    Sampling sc = sa.shift(m1*d1);
    Real1 rc = ra.resample(sc);
    float[] xc = rc.getX();
    for (int i1=0; i1<n1-m1; ++i1)
      assertEquals(1.0,xc[i1],0.0);
    for (int i1=n1-m1; i1<n1; ++i1)
      assertEquals(0.0,xc[i1],0.0);
  }

  void assertEquals(Real1 e, Real1 a) {
    Sampling se = e.getT();
    Sampling sa = a.getT();
    assertTrue(sa.isEquivalentTo(se));
    float[] xe = e.getX();
    float[] xa = a.getX();
    int n = xe.length;
    for (int i=0; i<n; ++i)
      assertEquals(xe[i],xa[i],TINY);
  }
}
