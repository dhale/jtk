/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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

