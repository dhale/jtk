/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util.test;

import junit.framework.*;
import edu.mines.jtk.util.Array;

/**
 * Tests {@link edu.mines.jtk.util.Array}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.04
 */
public class ArrayTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ArrayTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testMonotonic() {
    double[] a = {};
    assertTrue(Array.isMonotonic(a));
    assertTrue(Array.isIncreasing(a));
    assertTrue(Array.isDecreasing(a));

    double[] a0 = {0};
    assertTrue(Array.isMonotonic(a0));
    assertTrue(Array.isIncreasing(a0));
    assertTrue(Array.isDecreasing(a0));

    double[] a01 = {0,1};
    assertTrue(Array.isMonotonic(a01));
    assertTrue(Array.isIncreasing(a01));
    assertTrue(!Array.isDecreasing(a01));

    double[] a10 = {1,0};
    assertTrue(Array.isMonotonic(a10));
    assertTrue(!Array.isIncreasing(a10));
    assertTrue(Array.isDecreasing(a10));

    double[] a101 = {1,0,1};
    assertTrue(!Array.isMonotonic(a101));
    assertTrue(!Array.isIncreasing(a101));
    assertTrue(!Array.isDecreasing(a101));

    double[] a010 = {0,1,0};
    assertTrue(!Array.isMonotonic(a010));
    assertTrue(!Array.isIncreasing(a010));
    assertTrue(!Array.isDecreasing(a010));
  }

  public void testBinarySearch() {
    double[] a = {};
    checkSearch(a,1);

    double[] a0 = {2};
    checkSearch(a0,1);
    checkSearch(a0,2);
    checkSearch(a0,3);

    double[] a13 = {1,3};
    checkSearch(a13,0);
    checkSearch(a13,1);
    checkSearch(a13,2);
    checkSearch(a13,3);
    checkSearch(a13,4);

    double[] a31 = {3,1};
    checkSearch(a31,0);
    checkSearch(a31,1);
    checkSearch(a31,2);
    checkSearch(a31,3);
    checkSearch(a31,4);

    double[] a135 = {1,3,5};
    checkSearch(a135,0);
    checkSearch(a135,1);
    checkSearch(a135,2);
    checkSearch(a135,3);
    checkSearch(a135,4);
    checkSearch(a135,5);
    checkSearch(a135,6);

    double[] a531 = {5,3,1};
    checkSearch(a531,0);
    checkSearch(a531,1);
    checkSearch(a531,2);
    checkSearch(a531,3);
    checkSearch(a531,4);
    checkSearch(a531,5);
    checkSearch(a531,6);
  }
  private void checkSearch(double[] a, double x) {
    int n = a.length;
    int i = Array.binarySearch(a,x);
    validateSearch(a,x,i);
    for (int is=-2; is<n+2; ++is) {
      i = Array.binarySearch(a,x,is);
      validateSearch(a,x,i);
    }
  }
  private void validateSearch(double[] a, double x, int i) {
    int n = a.length;
    if (i>=0) {
      assertTrue(a[i]==x);
    } else {
      i = -(i+1);
      if (n==0) {
        assertTrue(i==0);
      } else if (n<2 || a[0]<a[n-1]) {
        if (i==0) {
          assertTrue(x<a[i]);
        } else if (i==n) {
          assertTrue(a[i-1]<x);
        } else {
          assertTrue(a[i-1]<x && x<a[i]);
        }
      } else {
        if (i==0) {
          assertTrue(x>a[i]);
        } else if (i==n) {
          assertTrue(a[i-1]>x);
        } else {
          assertTrue(a[i-1]>x && x>a[i]);
        }
      }
    }
  }
}
