/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * Utilities for arrays.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.01.01
 */
public class Array {

  /**
   * Returns a copy of the specified array.
   * @param a the array.
   * @return the copy.
   */
  public static double[] copy(double[] a) {
    int n = a.length;
    double[] b = new double[n];
    System.arraycopy(a,0,b,0,n);
    return b;
  }

  /**
   * Determines whether the specified array is monotonic-definite.
   * The array is monotonic-definite if its elements are strictly
   * increasing or decreasing, with no equal values.
   */
  public static boolean isMonotonicDefinite(double[] a) {
    int n = a.length;
    if (n<2) {
      return true;
    } else if (a[0]<a[1]) {
      for (int i=2; i<n; ++i) {
        if (a[i-1]>=a[i])
          return false;
      }
      return true;
    } else if (a[1]<a[0]) {
      for (int i=2; i<n; ++i) {
        if (a[i-1]<=a[i])
          return false;
      }
      return true;
    } else {
      return false;
    }
  }

  // Static methods only.
  private Array() {
  }
}
