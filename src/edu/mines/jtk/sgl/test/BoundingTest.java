/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl.test;

import junit.framework.*;
import java.util.Random;
import edu.mines.jtk.sgl.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests classes for matrix, point, and vector math.
 * @author Dave Hale
 * @version 2005.05.20
 */
public class BoundingTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(BoundingTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testBox() {
    int ntrial = 100;
    for (int itrial=0; itrial<ntrial; ++itrial) {
      BoundingBox bb = new BoundingBox();
      assertTrue(bb.isEmpty());
      int nexpand = 100;
      for (int iexpand=0; iexpand<nexpand; ++iexpand) {
        Point3 c = randomPoint3();
        double r = randomDouble();
        BoundingSphere bs = new BoundingSphere(c,r);
        bb.expandBy(bs);
        assertTrue(!bb.isEmpty());
        int npoint=100;
        for (int ipoint=0; ipoint<npoint; ++ipoint) {
          Point3 p = randomPoint3();
          if (bs.contains(p))
            assertTrue(bb.contains(p));
        }
      }
    }
  }

  public void testSphere() {
    int ntrial = 100;
    for (int itrial=0; itrial<ntrial; ++itrial) {
      BoundingSphere bs = new BoundingSphere();
      assertTrue(bs.isEmpty());
      int nexpand = 100;
      for (int iexpand=0; iexpand<nexpand; ++iexpand) {
        Point3 p = randomPoint3();
        Point3 q = randomPoint3();
        BoundingBox bb = new BoundingBox(p,q);
        if (randomDouble()>0.5) {
          bs.expandBy(bb);
        } else {
          bs.expandRadiusBy(bb);
        }
        assertTrue(!bs.isEmpty());
        int npoint=100;
        for (int ipoint=0; ipoint<npoint; ++ipoint) {
          Point3 r = randomPoint3();
          if (bb.contains(r))
            assertTrue(bs.contains(r));
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static Random _random = new Random(314159);

  private static final double TOLERANCE = 100*DBL_EPSILON;

  private static double randomDouble() {
    return _random.nextDouble();
  }

  private static Point3 randomPoint3() {
    double x = _random.nextDouble();
    double y = _random.nextDouble();
    double z = _random.nextDouble();
    return new Point3(x,y,z);
  }

  private static void assertEquals(Tuple3 e, Tuple3 a) {
    assertEquals(e.x,a.x,TOLERANCE);
    assertEquals(e.y,a.y,TOLERANCE);
    assertEquals(e.z,a.z,TOLERANCE);
  }

  private static void assertEquals(double e, double a) {
    assertEquals(e,a,TOLERANCE);
  }
}
