/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.MathPlus.DBL_EPSILON;

/**
 * Tests classes for matrix, point, and vector math.
 * @author Dave Hale
 * @version 2005.05.20
 */
public class MatrixPointVectorTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(MatrixPointVectorTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testMatrix() {
    int ntrial = 10;
    for (int itrial=0; itrial<ntrial; ++itrial) {
      Matrix44 i = Matrix44.identity();
      Matrix44 a = randomMatrix44();

      Matrix44 at = a.transpose();
      assertEquals(a,at.transpose());

      Matrix44 ai = a.inverse();
      assertEquals(a,ai.inverse());

      assertEquals(i,a.times(ai));
      assertEquals(i,a.transpose().timesTranspose(ai));
      assertEquals(i,a.transposeTimes(ai.transpose()));

      Matrix44 ac = new Matrix44(a);
      assertEquals(i,ac.timesEquals(ai));
      ac = new Matrix44(a);
      assertEquals(i,ac.transposeEquals().timesTranspose(ai));
      ac = new Matrix44(a);
      assertEquals(i,ac.transposeTimesEquals(ai.transpose()));
    }
  }

  public void testVector() {
    int ntrial = 10;
    for (int itrial=0; itrial<ntrial; ++itrial) {
      Vector3 u = randomVector3();
      Vector3 v = randomVector3();
      Vector3 vc = new Vector3(v);
      assertEquals(v,v.negate().negate());
      assertEquals(v,vc.negateEquals().negateEquals());
      assertEquals(1.0,v.normalize().length());
      assertEquals(1.0,vc.normalizeEquals().length());
      assertEquals(1.0,v.normalize().lengthSquared());
      assertEquals(v.dot(v),v.lengthSquared());
      assertEquals(0.0,u.cross(v).dot(u));
      assertEquals(0.0,u.cross(v).dot(v));
    }
  }

  public void testPoint() {
    int ntrial = 10;
    for (int itrial=0; itrial<ntrial; ++itrial) {
      Point3 p = randomPoint3();
      Point3 pc = new Point3(p);
      Vector3 v = randomVector3();
      assertEquals(p,p.plus(v).minus(v));
      assertEquals(p,pc.plusEquals(v).minusEquals(v));
      Point3 q = p.minus(v);
      assertEquals(q.distanceTo(p),v.length());
    }
  }

  public void testMatrixVector() {
    int ntrial = 10;
    for (int itrial=0; itrial<ntrial; ++itrial) {
      Vector3 v = randomVector3();
      Matrix44 a = randomMatrix33();
      Matrix44 ata = a.transposeTimes(a);
      assertEquals(ata.times(v),a.transposeTimes(a.times(v)));
      Matrix44 aat = a.timesTranspose(a);
      assertEquals(aat.times(v),a.times(a.transposeTimes(v)));
    }
  }

  public void testMatrixPoint() {
    int ntrial = 10;
    for (int itrial=0; itrial<ntrial; ++itrial) {
      Matrix44 a,ata,aat;
      a = randomMatrix33();
      ata = a.transposeTimes(a);
      aat = a.timesTranspose(a);
      Point3 p3 = randomPoint3();
      assertEquals(ata.times(p3),a.transposeTimes(a.times(p3)));
      assertEquals(aat.times(p3),a.times(a.transposeTimes(p3)));
      a = randomMatrix44();
      ata = a.transposeTimes(a);
      aat = a.timesTranspose(a);
      Point4 p4 = randomPoint4();
      assertEquals(ata.times(p4),a.transposeTimes(a.times(p4)));
      assertEquals(aat.times(p4),a.times(a.transposeTimes(p4)));
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static Random _random = new Random(314159);

  private static final double TOLERANCE = 100*DBL_EPSILON;

  /**
   * Returns a diagonally dominant random 3x3 matrix.
   */
  private static Matrix44 randomMatrix33() {
    double[] m = new double[16];
    for (int i=0; i<16; ++i)
      m[i] = _random.nextDouble();
    m[ 0] += 4.0;
    m[ 5] += 4.0;
    m[10] += 4.0;
    m[ 3] = m[12] = 0.0;
    m[ 7] = m[13] = 0.0;
    m[11] = m[14] = 0.0;
    m[15] = 1.0;
    return new Matrix44(m);
  }

  /**
   * Returns Makes a diagonally dominant random 4x4 matrix.
   */
  private static Matrix44 randomMatrix44() {
    double[] m = new double[16];
    for (int i=0; i<16; ++i)
      m[i] = _random.nextDouble();
    m[ 0] += 4.0;
    m[ 5] += 4.0;
    m[10] += 4.0;
    m[15] += 4.0;
    return new Matrix44(m);
  }

  private static Point3 randomPoint3() {
    double x = _random.nextDouble();
    double y = _random.nextDouble();
    double z = _random.nextDouble();
    return new Point3(x,y,z);
  }

  private static Vector3 randomVector3() {
    double x = _random.nextDouble();
    double y = _random.nextDouble();
    double z = _random.nextDouble();
    return new Vector3(x,y,z);
  }

  private static Point4 randomPoint4() {
    double x = _random.nextDouble();
    double y = _random.nextDouble();
    double z = _random.nextDouble();
    double w = _random.nextDouble();
    return new Point4(x,y,z,w);
  }

  private static void assertEquals(Matrix44 e, Matrix44 a) {
    double[] em = e.m;
    double[] am = a.m;
    for (int i=0; i<16; ++i)
      assertEquals(em[i],am[i],TOLERANCE);
  }

  private static void assertEquals(Tuple3 e, Tuple3 a) {
    assertEquals(e.x,a.x,TOLERANCE);
    assertEquals(e.y,a.y,TOLERANCE);
    assertEquals(e.z,a.z,TOLERANCE);
  }

  private static void assertEquals(Tuple4 e, Tuple4 a) {
    assertEquals(e.x,a.x,TOLERANCE);
    assertEquals(e.y,a.y,TOLERANCE);
    assertEquals(e.z,a.z,TOLERANCE);
    assertEquals(e.w,a.w,TOLERANCE);
  }

  private static void assertEquals(double e, double a) {
    assertEquals(e,a,TOLERANCE);
  }
}
