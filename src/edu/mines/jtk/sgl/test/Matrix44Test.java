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
 * Tests {@link edu.mines.jtk.sgl.Matrix44}.
 * @author Dave Hale
 * @version 2005.05.20
 */
public class Matrix44Test extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(Matrix44Test.class);
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

      assertEquals(i,a.clone().timesEquals(ai));
      assertEquals(i,a.clone().transposeEquals().timesTranspose(ai));
      assertEquals(i,a.clone().transposeTimesEquals(ai.transpose()));
    }
  }

  public void testMatrixVector() {
    int ntrial = 10;
    for (int itrial=0; itrial<ntrial; ++itrial) {
      Vector3 v = randomVector3();
      assertEquals(1.0,v.normalize().length());
      assertEquals(1.0,v.clone().normalizeEquals().length());
      assertEquals(1.0,v.normalize().lengthSquared());
      assertEquals(v.dot(v),v.lengthSquared());

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

  public static Matrix44 randomMatrix44() {
    double[] m = new double[16];
    for (int i=0; i<16; ++i)
      m[i] = _random.nextDouble();
    m[ 0] += 4.0; // Make matrix
    m[ 5] += 4.0; // diagonally
    m[10] += 4.0; // dominant so 
    m[15] += 4.0; // inverse exists.
    return new Matrix44(m);
  }

  public static Matrix44 randomMatrix33() {
    double[] m = new double[16];
    for (int i=0; i<16; ++i)
      m[i] = _random.nextDouble();
    m[ 0] += 4.0; // Make matrix diagonally
    m[ 5] += 4.0; // dominant so  
    m[10] += 4.0; // inverse exists.
    m[ 3] = m[12] = 0.0;
    m[ 7] = m[13] = 0.0;
    m[11] = m[14] = 0.0;
    m[15] = 1.0;
    return new Matrix44(m);
  }

  public static Point3 randomPoint3() {
    double x = _random.nextDouble();
    double y = _random.nextDouble();
    double z = _random.nextDouble();
    return new Point3(x,y,z);
  }

  public static Vector3 randomVector3() {
    double x = _random.nextDouble();
    double y = _random.nextDouble();
    double z = _random.nextDouble();
    return new Vector3(x,y,z);
  }

  public static Point4 randomPoint4() {
    double x = _random.nextDouble();
    double y = _random.nextDouble();
    double z = _random.nextDouble();
    double w = _random.nextDouble();
    return new Point4(x,y,z,w);
  }

  public static void assertEquals(Matrix44 e, Matrix44 a) {
    double t = 100.0*DBL_EPSILON;
    double[] em = e.m;
    double[] am = a.m;
    for (int i=0; i<16; ++i)
      assertEquals(em[i],am[i],t);
  }

  public static void assertEquals(Tuple3 e, Tuple3 a) {
    double t = 100.0*DBL_EPSILON;
    assertEquals(e.x,a.x,t);
    assertEquals(e.y,a.y,t);
    assertEquals(e.z,a.z,t);
  }

  public static void assertEquals(Tuple4 e, Tuple4 a) {
    double t = 100.0*DBL_EPSILON;
    assertEquals(e.x,a.x,t);
    assertEquals(e.y,a.y,t);
    assertEquals(e.z,a.z,t);
    assertEquals(e.w,a.w,t);
  }

  public static void assertEquals(double e, double a) {
    double t = 100.0*DBL_EPSILON;
    assertEquals(e,a,t);
  }

  private static Random _random = new Random(314159);
}
