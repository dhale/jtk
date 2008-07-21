package edu.mines.jtk.util.test;

import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.ArrayQueue;

/**
 * Tests {@link edu.mines.jtk.util.ArrayQueue}.
 * @author Dave Hale, Colorado School of Mines
 * @version 07/16/2008.
 */
public class ArrayQueueTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ArrayQueueTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testRandom() {
    ArrayQueue<Integer> aq = new ArrayQueue<Integer>();
    Random r = new Random();
    int niter = 1000;
    int nadd = 0;
    int nrem = 0;
    for (int iter=0; iter<niter; ++iter) {
      if (r.nextFloat()>0.5f) {
        int n = r.nextInt(100);
        for (int i=0; i<n; ++i) {
          aq.add(nadd);
          ++nadd;
        }
      } else if (aq.size()>0) {
        int n = aq.size();
        if (r.nextFloat()>0.05f) 
          n = r.nextInt(n);
        for (int i=0; i<n; ++i) {
          assertEquals(nrem,aq.remove().intValue());
          ++nrem;
        }
      }
    }
  }
}
