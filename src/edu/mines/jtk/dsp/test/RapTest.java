/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;
import edu.mines.jtk.dsp.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.dsp.Rap}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class RapTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(RapTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test3() {
    int n1 = 3;
    int n2 = 4;
    int n3 = 5;
    float r0 = 0.0f;
    float ra = 2.0f;
    float rb1 = 1.0f;
    float rb2 = 2.0f;
    float rb3 = 4.0f;
    float[][][] rx,ry,rz;

    assertEqual(Rap.zero(n1,n2,n3),Rap.fill(r0,n1,n2,n3));

    rx = Rap.ramp(ra,rb1,rb2,rb3,n1,n2,n3);
    assertEqual(rx,Rap.sub(Rap.add(rx,rx),rx));
    assertEqual(rx,Rap.sub(Rap.add(rx,ra),ra));
    assertEqual(Rap.fill(ra,n1,n2,n3),Rap.sub(Rap.add(ra,rx),rx));

    rx = Rap.ramp(ra,rb1,rb2,rb3,n1,n2,n3);
    assertEqual(rx,Rap.div(Rap.mul(rx,rx),rx));
    assertEqual(rx,Rap.div(Rap.mul(rx,ra),ra));
    assertEqual(Rap.fill(ra,n1,n2,n3),Rap.div(Rap.mul(ra,rx),rx));

    rx = Rap.ramp(ra,rb1,rb2,rb3,n1,n2,n3);
    assertEqual(rx,Rap.log(Rap.exp(rx)));

    rx = Rap.ramp(ra,rb1,rb2,rb3,n1,n2,n3);
    assertAlmostEqual(rx,Rap.mul(Rap.sqrt(rx),Rap.sqrt(rx)));

    rx = Rap.ramp(ra,rb1,rb2,rb3,n1,n2,n3);
    assertAlmostEqual(rx,Rap.pow(Rap.sqrt(rx),2.0f));

    rx = Rap.ramp(ra,rb1,rb2,rb3,n1,n2,n3);
    int[] imax = {-1,-1,-1};
    float rmax = Rap.findMax(rx,imax);
    assertTrue(rmax==rx[n3-1][n2-1][n1-1]);
    assertEquals(n1-1,imax[0]);
    assertEquals(n2-1,imax[1]);
    assertEquals(n3-1,imax[2]);

    rx = Rap.ramp(ra,rb1,rb2,rb3,n1,n2,n3);
    int[] imin = {-1,-1,-1};
    float rmin = Rap.findMin(rx,imin);
    assertTrue(rmin==rx[0][0][0]);
    assertEquals(0,imin[0]);
    assertEquals(0,imin[1]);
    assertEquals(0,imin[2]);
  }

  private void assertEqual(float[][][] rx, float[][][] ry) {
    assertTrue(Rap.equal(rx,ry));
  }

  private void assertAlmostEqual(float[][][] rx, float[][][] ry) {
    float tolerance = 100.0f*FLT_EPSILON;
    assertTrue(Rap.equal(tolerance,rx,ry));
  }

  private void printError(float[][][] rx, float[][][] ry) {
    System.out.println("maximum absolute error = " +
      Rap.findMax(Rap.abs(Rap.sub(rx,ry)),null));
  }
}
