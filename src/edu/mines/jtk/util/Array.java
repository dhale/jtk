/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import static java.lang.Math.*;

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
   * Returns a copy of the specified array.
   * @param a the array.
   * @return the copy.
   */
  public static float[] copy(float[] a) {
    int n = a.length;
    float[] b = new float[n];
    System.arraycopy(a,0,b,0,n);
    return b;
  }

  /**
   * Determines whether the specified array is monotonic-definite.
   * The array is monotonic-definite if its elements are strictly
   * increasing or decreasing, with no equal values.
   * @param a the array.
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

  /**
   * Determines whether the specified array is monotonic-definite.
   * The array is monotonic-definite if its elements are strictly
   * increasing or decreasing, with no equal values.
   * @param a the array.
   */
  public static boolean isMonotonicDefinite(float[] a) {
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

  /**
   * Performs a binary search in a monotonic-definite array of values. 
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be used in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is indeed 
   * monotonic-definite; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic-definite.
   * @param x the search value; the value to locate.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found; or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic-definite array.
   */
  public static int binarySearch(float[] a, float x, int i) {
    int n = a.length;

    // Interpret a negative index i as if returned from a previous call.
    if (i<0)
      i = -i-1;

    // Special case for n=0.
    if (n==0)
      return 0;

    // Lower index j, upper index k, and step m.
    int j = max(0,min(n-1,i));
    int k = j+1;
    int m = 1;

    // If values increasing, ...
    if (a[0]<a[n-1]) {

      // Find indices j and k such that 
      // (1) 0 <= j < k <= n and, 
      // (2) if possible, a[j] <= x < a[k].
      while (0<j && x<a[j]) {
        k = j;
        j -= m;
        m += m;
      }
      j = max(0,j);
      while (k<n && a[k]<=x) {
        j = k;
        k += m;
        m += m;
      }
      k = min(n,k);

      // Find index via bisection.
      for (int middle=(j+k)/2; middle!=j; middle=(j+k)/2) {
        if (a[middle]<=x)
          j = middle;
        else
          k = middle;
      }
    }

    // Else, if not increasing, ...
    else {

      // Find indices j and k such that 
      // (1) 0 <= j < k <= n and, 
      // (2) if possible, a[j] >= x > a[k].
      while (0<j && a[j]<x) {
        k = j;
        j -= m;
        m += m;
      }
      j = max(0,j);
      while (k<n && x<=a[k]) {
        j = k;
        k += m;
        m += m;
      }
      k = min(n,k);

      // Find index via bisection.
      for (int middle=(j+k)/2; middle!=j; middle=(j+k)/2) {
        if (x<=a[middle])
          j = middle;
        else
          k = middle;
      }
    }

    // Return the lower index j.
    return (0<=j && j<n && a[j]==x)?j:(-j-1);
  }
}
