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
      int ifx = ifmin+random.nextInt(1+ifmax-ifmin);
      int ify = ifmin+random.nextInt(1+ifmax-ifmin);
      int ifz = ifmin+random.nextInt(1+ifmax-ifmin);
      float[] x = new float[lx];
      float[] y = new float[ly];
      float[] zs = new float[lz];
      float[] zf = new float[lz];
      /*
      System.out.println("lx="+lx+" ifx="+ifx);
      for (int ix=0; ix<lx; ++ix) {
        x[ix] = random.nextFloat();
        System.out.println("ix="+ix+" x="+x[ix]);
      }
      System.out.println("ly="+ly+" ify="+ify);
      for (int iy=0; iy<ly; ++iy) {
        y[iy] = random.nextFloat();
        System.out.println("iy="+iy+" y="+y[iy]);
      }
      */
      Conv.conv(lx,ifx,x,ly,ify,y,lz,ifz,zf);
      convSimple(lx,ifx,x,ly,ify,y,lz,ifz,zs);
      for (int iz=0; iz<lz; ++iz) {
        //System.out.println("iz="+iz+" zs="+zs[iz]+" zf="+zf[iz]);
        assertEquals(zs[iz],zf[iz],0.001);
      }
    }
  }

  private static void convSimple(
    int lx, int ifx, float[] x,
    int ly, int ify, float[] y,
    int lz, int ifz, float[] z)
  {
    int ilo = ifz-ifx-ify;
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
}
