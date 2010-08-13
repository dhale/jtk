/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.interp.LasserreVolume}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.06.10
 */
public class LasserreVolumeTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(LasserreVolumeTest.class);
    junit.textui.TestRunner.run(suite);
  }

  private LasserreVolume lv2 = new LasserreVolume(2);
  private LasserreVolume lv3 = new LasserreVolume(3);

  public void testTriangle() {
    lv2.clear();
    lv2.addHalfSpace( 1.0, 1.0, 1.0); // make a simple
    lv2.addHalfSpace(-1.0, 0.0, 0.0); // triangle with
    lv2.addHalfSpace( 0.0,-1.0, 0.0); // 3 half-planes
    assertEquals(0.5,lv2.getVolume());
  }

  public void testRedundant() {
    lv2.clear();
    lv2.addHalfSpace(-1.0, 1.0, 0.0); // make a simple
    lv2.addHalfSpace( 1.0, 0.0, 1.0); // triangle with
    lv2.addHalfSpace( 0.0,-1.0, 0.0); // 3 half-planes
    assertEquals(0.5,lv2.getVolume());
    lv2.addHalfSpace(-1.0, 0.0, 0.0); // redundant, not parallel
    assertEquals(0.5,lv2.getVolume());
    lv2.addHalfSpace( 1.0, 1.0, 2.0); // redundant, not parallel
    assertEquals(0.5,lv2.getVolume());
    lv2.addHalfSpace( 2.0,-1.0, 2.0); // redundant, not parallel
    assertEquals(0.5,lv2.getVolume());
    lv2.addHalfSpace( 2.0,-1.0, 4.0); // redundant, not parallel
    assertEquals(0.5,lv2.getVolume());
    lv2.addHalfSpace( 2.0, 0.0, 2.0); // redundant, parallel
    assertEquals(1.0,lv2.getVolume()); // WRONG ANSWER!
  }

  public void testUnitSquare() {
    lv2.clear();
    lv2.addHalfSpace( 1.0, 0.0, 1.0);
    lv2.addHalfSpace(-1.0, 0.0, 0.0);
    lv2.addHalfSpace( 0.0, 1.0, 1.0);
    lv2.addHalfSpace( 0.0,-1.0, 0.0);
    assertEquals(1.0,lv2.getVolume());
    lv2.addHalfSpace( 1.0, 0.0, 2.0); // redundant, parallel
    assertEquals(2.0,lv2.getVolume()); // WRONG ANSWER!
  }

  public void testOctahedron() {
    lv2.clear();
    lv2.addHalfSpace( 1.0, 0.0, 1.0);
    lv2.addHalfSpace(-1.0, 0.0, 1.0);
    lv2.addHalfSpace( 0.0, 1.0, 1.0);
    lv2.addHalfSpace( 0.0,-1.0, 1.0);
    lv2.addHalfSpace( 1.0, 1.0, 1.5);
    lv2.addHalfSpace(-1.0, 1.0, 1.5);
    lv2.addHalfSpace( 1.0,-1.0, 1.5);
    lv2.addHalfSpace(-1.0,-1.0, 1.5);
    assertEquals(3.5,lv2.getVolume());
  }

  public void testUnitCube() {
    lv3.clear();
    lv3.addHalfSpace( 1.0, 0.0, 0.0, 1.0);
    lv3.addHalfSpace(-1.0, 0.0, 0.0, 0.0);
    lv3.addHalfSpace( 0.0, 1.0, 0.0, 1.0);
    lv3.addHalfSpace( 0.0,-1.0, 0.0, 0.0);
    lv3.addHalfSpace( 0.0, 0.0, 1.0, 1.0);
    lv3.addHalfSpace( 0.0, 0.0,-1.0, 0.0);
    assertEquals(1.0,lv3.getVolume());
  }

  private static void assertEquals(double e, double a) {
    assertEquals(e,a,1.0e-10);
  }
}
