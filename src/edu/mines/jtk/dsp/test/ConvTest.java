/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;
import edu.mines.jtk.dsp.*;
import static edu.mines.jtk.util.Array.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.dsp.Conv}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class ConvTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ConvTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testRandom() {
    java.util.Random random = new java.util.Random(314159);
    int ntest = 1000;
    int ifmin = -2;
    int ifmax =  2;
    int lmin = 1;
    int lmax = 8;
    for (int itest=0; itest<ntest; ++itest) {
      int lx = lmin+random.nextInt(1+lmax-lmin);
      int ly = lmin+random.nextInt(1+lmax-lmin);
      int lz = lmin+random.nextInt(1+lmax-lmin);
      int kx = ifmin+random.nextInt(1+ifmax-ifmin);
      int ky = ifmin+random.nextInt(1+ifmax-ifmin);
      int kz = ifmin+random.nextInt(1+ifmax-ifmin);
      float[] x = new float[lx];
      float[] y = new float[ly];
      float[] zs = new float[lz];
      float[] zf = new float[lz];
      convSimple(lx,kx,x,ly,ky,y,lz,kz,zs);
      Conv.conv(lx,kx,x,ly,ky,y,lz,kz,zf);
      assertEquals(zs,zf);
    }
  }

  private static void convSimple(
    int lx, int kx, float[] x,
    int ly, int ky, float[] y,
    int lz, int kz, float[] z)
  {
    int ilo = kz-kx-ky;
    int ihi = ilo+lz-1;
    for (int i=ilo; i<=ihi; ++i) {
      int jlo = i-ly+1;
      if (jlo<0)
        jlo = 0;
      int jhi = i;
      if (jhi>=lx)
        jhi = lx-1;
      float sum = 0.0f;
      for (int j=jlo; j<=jhi; ++j)
        sum += x[j]*y[i-j];
      z[i-ilo] = sum;
    }
  }

  private static final float TOLERANCE = 100.0f*FLT_EPSILON;
  private static void assertEquals(float[] a, float[] b) {
    int n = a.length;
    for (int i=0; i<n; ++i) {
      assertEquals(a[i],b[i],TOLERANCE);
    }
  }
}
