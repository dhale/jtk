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
   * Returns a copy of the specified array.
   * @param a the array.
   * @return the copy.
   */
  public static int[] copy(int[] a) {
    int n = a.length;
    int[] b = new int[n];
    System.arraycopy(a,0,b,0,n);
    return b;
  }

  /**
   * Determines whether the specified array is monotonic. The array is 
   * monotonic if its elements a[i] either increase or decrease (but 
   * not both) with array index i, with no equal values.
   * @param a the array.
   * @return true, if monotonic (or a.length&lt;2); false, otherwise.
   */
  public static boolean isMonotonic(double[] a) {
    return isIncreasing(a) || isDecreasing(a);
  }

  /**
   * Determines whether the specified array is monotonic. The array is 
   * monotonic if its elements a[i] either increase or decrease (but 
   * not both) with array index i, with no equal values.
   * @param a the array.
   * @return true, if monotonic (or a.length&lt;2); false, otherwise.
   */
  public static boolean isMonotonic(float[] a) {
    return isIncreasing(a) || isDecreasing(a);
  }

  /**
   * Determines whether the specified array is monotonic. The array is 
   * monotonic if its elements a[i] either increase or decrease (but 
   * not both) with array index i, with no equal values.
   * @param a the array.
   * @return true, if monotonic (or a.length&lt;2); false, otherwise.
   */
  public static boolean isMonotonic(int[] a) {
    return isIncreasing(a) || isDecreasing(a);
  }

  /**
   * Determines whether the specified array is increasing. The array is 
   * increasing if its elements a[i] increase with array index i, with 
   * no equal values.
   * @param a the array.
   * @return true, if increasing (or a.length&lt;2); false, otherwise.
   */
  public static boolean isIncreasing(double[] a) {
    int n = a.length;
    if (n>1) {
      for (int i=1; i<n; ++i) {
        if (a[i-1]>=a[i])
          return false;
      }
    }
    return true;
  }

  /**
   * Determines whether the specified array is increasing. The array is 
   * increasing if its elements a[i] increase with array index i, with 
   * no equal values.
   * @param a the array.
   * @return true, if increasing (or a.length&lt;2); false, otherwise.
   */
  public static boolean isIncreasing(float[] a) {
    int n = a.length;
    if (n>1) {
      for (int i=1; i<n; ++i) {
        if (a[i-1]>=a[i])
          return false;
      }
    }
    return true;
  }

  /**
   * Determines whether the specified array is increasing. The array is 
   * increasing if its elements a[i] increase with array index i, with 
   * no equal values.
   * @param a the array.
   * @return true, if increasing (or a.length&lt;2); false, otherwise.
   */
  public static boolean isIncreasing(int[] a) {
    int n = a.length;
    if (n>1) {
      for (int i=1; i<n; ++i) {
        if (a[i-1]>=a[i])
          return false;
      }
    }
    return true;
  }

  /**
   * Determines whether the specified array is decreasing. The array is 
   * decreasing if its elements a[i] decrease with array index i, with 
   * no equal values.
   * @param a the array.
   * @return true, if decreasing (or a.length&lt;2); false, otherwise.
   */
  public static boolean isDecreasing(double[] a) {
    int n = a.length;
    if (n>1) {
      for (int i=1; i<n; ++i) {
        if (a[i-1]<=a[i])
          return false;
      }
    }
    return true;
  }

  /**
   * Determines whether the specified array is decreasing. The array is 
   * decreasing if its elements a[i] decrease with array index i, with 
   * no equal values.
   * @param a the array.
   * @return true, if decreasing (or a.length&lt;2); false, otherwise.
   */
  public static boolean isDecreasing(float[] a) {
    int n = a.length;
    if (n>1) {
      for (int i=1; i<n; ++i) {
        if (a[i-1]<=a[i])
          return false;
      }
    }
    return true;
  }

  /**
   * Determines whether the specified array is decreasing. The array is 
   * decreasing if its elements a[i] decrease with array index i, with 
   * no equal values.
   * @param a the array.
   * @return true, if decreasing (or a.length&lt;2); false, otherwise.
   */
  public static boolean isDecreasing(int[] a) {
    int n = a.length;
    if (n>1) {
      for (int i=1; i<n; ++i) {
        if (a[i-1]<=a[i])
          return false;
      }
    }
    return true;
  }

  /**
   * Performs a binary search in a monotonic array of values. 
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(double[] a, double x) {
    return binarySearch(a,x,a.length);
  }

  /**
   * Performs a binary search in a monotonic array of values. 
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(float[] a, float x) {
    return binarySearch(a,x,a.length);
  }

  /**
   * Performs a binary search in a monotonic array of values. 
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(int[] a, int x) {
    return binarySearch(a,x,a.length);
  }

  /**
   * Performs a binary search in a monotonic array of values. 
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be passed in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(double[] a, double x, int i) {
    int n = a.length;
    int nm1 = n-1;
    int low = 0;
    int high = nm1;
    boolean increasing = n<2 || a[0]<a[1];
    if (i<n) {
      high = (0<=i)?i:-(i+1);
      low = high-1;
      int step = 1;
      if (increasing) {
        for (; 0<low && x<a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]<x; high+=step,step+=step)
          low = high;
      } else {
        for (; 0<low && x>a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]>x; high+=step,step+=step)
          low = high;
      }
      if (low<0) low = 0;
      if (high>nm1) high = nm1;
    }
    if (increasing) {
      while (low<=high) {
        int mid = (low+high)>>1;
        double amid = a[mid];
        if (amid<x)
          low = mid+1;
        else if (amid>x)
          high = mid-1;
        else
          return mid;
      }
    } else {
      while (low<=high) {
        int mid = (low+high)>>1;
        double amid = a[mid];
        if (amid>x)
          low = mid+1;
        else if (amid<x)
          high = mid-1;
        else
          return mid;
      }
    }
    return -(low+1);
  }

  /**
   * Performs a binary search in a monotonic array of values. 
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be passed in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(float[] a, float x, int i) {
    int n = a.length;
    int nm1 = n-1;
    int low = 0;
    int high = nm1;
    boolean increasing = n<2 || a[0]<a[1];
    if (i<n) {
      high = (0<=i)?i:-(i+1);
      low = high-1;
      int step = 1;
      if (increasing) {
        for (; 0<low && x<a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]<x; high+=step,step+=step)
          low = high;
      } else {
        for (; 0<low && x>a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]>x; high+=step,step+=step)
          low = high;
      }
      if (low<0) low = 0;
      if (high>nm1) high = nm1;
    }
    if (increasing) {
      while (low<=high) {
        int mid = (low+high)>>1;
        float amid = a[mid];
        if (amid<x)
          low = mid+1;
        else if (amid>x)
          high = mid-1;
        else
          return mid;
      }
    } else {
      while (low<=high) {
        int mid = (low+high)>>1;
        float amid = a[mid];
        if (amid>x)
          low = mid+1;
        else if (amid<x)
          high = mid-1;
        else
          return mid;
      }
    }
    return -(low+1);
  }

  /**
   * Performs a binary search in a monotonic array of values. 
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be passed in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(int[] a, int x, int i) {
    int n = a.length;
    int nm1 = n-1;
    int low = 0;
    int high = nm1;
    boolean increasing = n<2 || a[0]<a[1];
    if (i<n) {
      high = (0<=i)?i:-(i+1);
      low = high-1;
      int step = 1;
      if (increasing) {
        for (; 0<low && x<a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]<x; high+=step,step+=step)
          low = high;
      } else {
        for (; 0<low && x>a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]>x; high+=step,step+=step)
          low = high;
      }
      if (low<0) low = 0;
      if (high>nm1) high = nm1;
    }
    if (increasing) {
      while (low<=high) {
        int mid = (low+high)>>1;
        int amid = a[mid];
        if (amid<x)
          low = mid+1;
        else if (amid>x)
          high = mid-1;
        else
          return mid;
      }
    } else {
      while (low<=high) {
        int mid = (low+high)>>1;
        int amid = a[mid];
        if (amid>x)
          low = mid+1;
        else if (amid<x)
          high = mid-1;
        else
          return mid;
      }
    }
    return -(low+1);
  }

  // Static methods only.
  private Array() {
  }
}
