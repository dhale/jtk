/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util.test;

import junit.framework.*;
import edu.mines.jtk.util.BrentMinFinder;

/**
 * Tests {@link edu.mines.jtk.util.BrentMinFinder}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.09.15
 */
public class BrentMinFinderTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(BrentMinFinderTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testSimple() {
    BrentMinFinder bmf = new BrentMinFinder(new BrentMinFinder.Function() {
      public double evaluate(double x) {
        return x*(x*x-2.0)-5.0;
      }
    });
    double xmin = bmf.findMin(0.0,1.0,1.0e-5);
    trace("xmin="+xmin);
    assertEquals(0.81650,xmin,0.00001);
  }

  private void trace(String s) {
    //System.out.println(s);
  }
}
