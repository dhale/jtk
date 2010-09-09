/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.FLT_EPSILON;
import static edu.mines.jtk.util.ArrayMath.rampfloat;

/**
 * Tests {@link edu.mines.jtk.util.Clips}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.19
 */
public class ClipsTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ClipsTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testPercentiles() {
    double tiny = 10.0f*FLT_EPSILON;
    int n = 101;
    //float[][][] f = rampfloat(0.0f,1.0f,0.0f,0.0f,n,n,n);
    //SimpleFloat3 f3 = new SimpleFloat3(f);
    //Clips clips = new Clips(f3);
    float[] f = rampfloat(0.0f,1.0f,n);
    Clips clips = new Clips(f);
    for (int imin=0,imax=n-1; imin<imax; ++imin,--imax) {
      double pmin = 100.0*imin/(n-1);
      double pmax = 100.0*imax/(n-1);
      clips.setPercentiles(pmin,pmax);
      float cmin = clips.getClipMin();
      float cmax = clips.getClipMax();
      assertEquals(imin,cmin,tiny);
      assertEquals(imax,cmax,tiny);
    }
  }
}
