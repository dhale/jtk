/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.Complex;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.dsp.Cap}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class CapTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(CapTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test3() {
    int n1 = 3;
    int n2 = 4;
    int n3 = 5;
    Complex c0 = new Complex();
    Complex ca = new Complex(1.0f,2.0f);
    Complex cb1 = new Complex(2.0f,3.0f);
    Complex cb2 = new Complex(3.0f,4.0f);
    Complex cb3 = new Complex(4.0f,5.0f);
    float[][][] cx,cy,cz;

    assertEqual(Cap.zero(n1,n2,n3),Cap.fill(c0,n1,n2,n3));

    cx = Cap.ramp(ca,cb1,cb2,cb3,n1,n2,n3);
    assertEqual(cx,Cap.sub(Cap.add(cx,cx),cx));
    assertEqual(cx,Cap.sub(Cap.add(cx,ca),ca));
    assertEqual(Cap.fill(ca,n1,n2,n3),Cap.sub(Cap.add(ca,cx),cx));

    cx = Cap.ramp(ca,cb1,cb2,cb3,n1,n2,n3);
    assertEqual(cx,Cap.div(Cap.mul(cx,cx),cx));
    assertEqual(cx,Cap.div(Cap.mul(cx,ca),ca));
    assertEqual(Cap.fill(ca,n1,n2,n3),Cap.div(Cap.mul(ca,cx),cx));

    cx = Cap.ramp(ca,cb1,cb2,cb3,n1,n2,n3);
    assertEqual(Cap.norm(cx),Cap.abs(Cap.mul(cx,Cap.conj(cx))));

    float[][][] rr = Rap.fill(1.0f,n1,n2,n3);
    float[][][] ra = Rap.ramp(0.0f,1.0f,1.0f,1.0f,n1,n2,n3);
    cx = Cap.polar(rr,ra);
    float[][][] rx = Rap.cos(ra);
    float[][][] ry = Rap.sin(ra);
    cy = Cap.complex(rx,ry);
    assertEqual(cx,cy);
    Complex ci = new Complex(0.0f,1.0f);
    float[][][] ciw = Cap.ramp(c0,ci,ci,ci,n1,n2,n3);
    cz = Cap.exp(ciw);
    assertEqual(cx,cz);
  }

  private void assertEqual(float[][][] cx, float[][][] cy) {
    assertTrue(Cap.equal(cx,cy));
  }

  private void assertAlmostEqual(float[][][] cx, float[][][] cy) {
    float tolerance = 100.0f*FLT_EPSILON;
    assertTrue(Cap.equal(tolerance,cx,cy));
  }
}
