/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.dsp.Sampling}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.10
 */
public class SamplingTest extends TestCase {

  public static void main(String[] args) {
    TestSuite suite = new TestSuite(SamplingTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testUniform() {
    int n = 1000;
    double d = 1.0/3.0;
    double f = 1.0/6.0;
    double tiny = d*Sampling.DEFAULT_TOLERANCE;
    double[] v = new double[n];
    double vi = f;
    for (int i=0; i<n; ++i,vi+=d)
      v[i] = vi; // f+i*d;
    Sampling us = new Sampling(n,d,f);
    Sampling vs = new Sampling(v);

    assertTrue(us.isUniform());
    assertTrue(vs.isUniform());
    assertTrue(us.isEquivalentTo(vs));

    for (int i=0; i<n; ++i,vi+=d) {
      int j = us.indexOf(v[i]);
      assertEquals(i,j);
      j = vs.indexOf(v[i]);
      assertEquals(i,j);
    }

    int[] overlap = us.overlapWith(vs);
    assertEquals(n,overlap[0]);
    assertEquals(0,overlap[1]);
    assertEquals(0,overlap[2]);

    Sampling sm = us.mergeWith(vs);
    assertTrue(sm.isEquivalentTo(us));
    assertTrue(sm.isEquivalentTo(vs));

    Sampling sr = us.shift(10*d);
    overlap = us.overlapWith(sr);
    assertEquals(n-10,overlap[0]);
    assertEquals(10,overlap[1]);
    assertEquals(0,overlap[2]);

    Sampling sl = us.shift(-10*d);
    overlap = us.overlapWith(sl);
    assertEquals(n-10,overlap[0]);
    assertEquals(0,overlap[1]);
    assertEquals(10,overlap[2]);

    sr = us.shift(n*d);
    overlap = us.overlapWith(sr);
    assertEquals(0,overlap[0]);
    assertEquals(n,overlap[1]);
    assertEquals(0,overlap[2]);

    sl = us.shift(-n*d);
    overlap = us.overlapWith(sl);
    assertEquals(0,overlap[0]);
    assertEquals(0,overlap[1]);
    assertEquals(n,overlap[2]);

    sm = us.mergeWith(sr);
    sm = sm.mergeWith(sl);
    assertEquals(n*3,sm.getCount());
    assertTrue(sm.isUniform());

    sr = us.shift(2*n*d);
    sm = us.mergeWith(sr);
    assertEquals(n*2,sm.getCount());
    assertTrue(!sm.isUniform());

    Sampling sp = us.prepend(100);
    assertEquals(n+100,sp.getCount());
    assertEquals(f-100*d,sp.getFirst(),tiny);

    Sampling sa = us.append(100);
    assertEquals(n+100,sa.getCount());
    assertEquals(f,sa.getFirst(),tiny);

    Sampling sd = us.decimate(3);
    assertEquals(1+(n-1)/3,sd.getCount());
    assertEquals(d*3,sd.getDelta(),tiny);
    assertEquals(f,sd.getFirst(),tiny);

    Sampling si = us.interpolate(3);
    assertEquals(1+(n-1)*3,si.getCount());
    assertEquals(d/3,si.getDelta(),tiny);
    assertEquals(f,si.getFirst(),tiny);
  }

  public void testBoundsAndForUniform() {
    Sampling s = new Sampling(11,1.0,0.0);
    assertEquals(s.isInBoundsExtended(-0.5001),false);
    assertEquals(s.isInBoundsExtended(-0.4999),true);
    assertEquals(s.isInBoundsExtended(10.4999),true);
    assertEquals(s.isInBoundsExtended(10.5001),false);
    assertEquals(s.getValueExtended(-1),-1.0);
    assertEquals(s.getValueExtended(0),0.0);
    assertEquals(s.getValueExtended(10),10.0);
    assertEquals(s.getValueExtended(11),11.0);
    assertEquals(s.indexOfNearestExtended(-0.5001),-1);
    assertEquals(s.indexOfNearestExtended(-0.4999),0);
    assertEquals(s.indexOfNearestExtended(0.0),0);
    assertEquals(s.indexOfNearestExtended(0.4999),0);
    assertEquals(s.indexOfNearestExtended(0.5001),1);
    assertEquals(s.indexOfNearestExtended(1.0),1);
    assertEquals(s.indexOfNearestExtended(1.4999),1);
    assertEquals(s.indexOfNearestExtended(1.5001),2);
    assertEquals(s.normalizedDifferenceExtended(
             1.5,s.indexOfFloorExtended(1.5)),0.5);
    s = new Sampling(10,1.0,-5.0);
    assertEquals(s.indexOfFloorExtended(-6.0),-1);
    assertEquals(s.indexOfFloorExtended(-5.9999),-1);
    assertEquals(s.indexOfFloorExtended(-5.5),-1);
    assertEquals(s.indexOfFloorExtended(-5.0001),-1);
    assertEquals(s.indexOfFloorExtended(-5.0),0);
    assertEquals(s.indexOfFloorExtended(-4.9999),0);
    assertEquals(s.indexOfFloorExtended(-4.5),0);
    assertEquals(s.indexOfFloorExtended(-4.0001),0);
    assertEquals(s.indexOfFloorExtended(-4.0),1);
    assertEquals(s.indexOfFloorExtended(0.0),5);
    s = new Sampling(10,2.0,10.0);
    assertEquals(s.normalizedDifferenceExtended(
            10.0,s.indexOfFloorExtended(10.0)),0.0);
    assertEquals(s.normalizedDifferenceExtended(
            10.5,s.indexOfFloorExtended(10.5)),0.25);
    assertEquals(s.normalizedDifferenceExtended(
            11.0,s.indexOfFloorExtended(11.0)),0.5);
    assertEquals(s.normalizedDifferenceExtended(
            11.5,s.indexOfFloorExtended(11.5)),0.75);
    assertEquals(s.normalizedDifferenceExtended(
            12.0,s.indexOfFloorExtended(12.0)),0.0);
  }

}
