/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.util.RandomFloat}.
 * @author Dave Hale, Zachary Pember, Colorado School of Mines
 * @version 2001.02.05, 2006.08.23
 */
public class RandomFloatTest extends TestCase {
  public static void main(String[] args) {
    if (args.length>0 && args[0].equals("bench"))
      bench();
    TestSuite suite = new TestSuite(RandomFloatTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testNothing() {
  }

  public static void bench() {
    Random r = new Random();
    RandomFloat rf = new RandomFloat();
    Stopwatch sw = new Stopwatch();
    int nf;
    float sum;
    double rate;
    for(int ntrial=0; ntrial<3; ++ntrial) {

      System.out.println();
      System.out.println("java.util.Random:");
      sw.restart();
      sum = 0.0f;
      for(nf=0; sw.time()<1.0; ++nf)
	sum += r.nextFloat();
      sw.stop();
      rate = nf/sw.time();
      System.out.println("  uniform: float/s="+rate+" sum="+sum);
      sw.restart();
      sum = 0.0f;
      for(nf=0; sw.time()<1.0; ++nf)
	sum += (float)r.nextGaussian();
      sw.stop();
      rate = nf/sw.time();
      System.out.println("   normal: float/s="+rate+" sum="+sum);

      System.out.println();
      System.out.println("edu.mines.jtk.util.RandomFloat:");
      sw.restart();
      sum = 0.0f;
      for(nf=0; sw.time()<1.0; ++nf)
	sum += rf.uniform();
      sw.stop();
      rate = nf/sw.time();
      System.out.println("  uniform: float/s="+rate+" sum="+sum);
      sw.restart();
      sum = 0.0f;
      for(nf=0; sw.time()<1.0; ++nf)
	sum += rf.normal();
      sw.stop();
      rate = nf/sw.time();
      System.out.println("   normal: float/s="+rate+" sum="+sum);
    }
  }
}

