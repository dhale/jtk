/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;
import java.util.Random;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.Array;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.dsp.Histogram}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.10.18
 */
public class HistogramTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(HistogramTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testRamp() {
    int n = 5000;
    float[] v = Array.rampfloat(-0.5f,1.0f,n);
    Histogram h = new Histogram(v,1.0);
    assertEquals(n-1,h.getBinCount());
    assertEquals(1.0,h.getBinDelta());
    assertEquals(0.0,h.getBinFirst());
  }

  public void xtestGaussian() {
    Random r = new Random();
    int n = 100000;
    float[] v = new float[n];
    for (int i=0; i<n; ++i)
      v[i] = (float)r.nextGaussian();
    Histogram h = new Histogram(v);
    double dbin = h.getBinDelta();
    System.out.println("dbin="+dbin);
    int nbin = h.getBinCount();
    float[] c = new float[nbin];
    h.getCounts(c);
    Array.dump(c);
  }
}
