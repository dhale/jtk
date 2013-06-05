/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Computes medians or weighted medians.
 * <p>
 * The weighted median of n values x[i] is the value x that minimizes 
 * the following function:
 * <pre>
 *        n-1
 * f(x) = sum w[i]*abs(x-x[i])
 *        i=0
 * </pre>
 * Here, the x[.] are values for which the median is to be computed,
 * the w[.] are positive weights, and x is the desired weighted median.
 * The function f(x) is convex and piecewise linear, so at least one
 * (but no more than two) of the specified values x[i] minimizes the 
 * function f(x). 
 * <p>
 * To find a minimum, we define sums of weights w[.] for which x[.]
 * is less than, equal to, and greater than x:
 * <pre>
 * wl(x) = sum of w[i] for x[i] &lt; x
 * wm(x) = sum of w[i] for x[i] = x
 * wr(x) = sum of w[i] for x[i] &gt; x
 * </pre>
 * and then define the left and right derivatives of f(x):
 * <pre>
 * dl(x) = wl(x)-wm(x)-wr(x) &lt;= 0
 * dr(x) = wl(x)+wm(x)-wr(x) &gt;= 0
 * </pre>
 * These inequality conditions are necessary and sufficient for x 
 * to minimize f(x), that is, for x to be the weighted median.
 * <p>
 * When either dl(x) = 0 or dr(x) = 0, then the function f(x) has 
 * zero slope between two values x[i] that both minimize f(x), and 
 * the weighted median is then computed to be the average of those 
 * two minimizing values. For example, such an average is always 
 * computed when the number of values x[.] is even and all weights 
 * w[.] are equal.
 * <p>
 * Computational complexity for both the median and the weighted 
 * median is O(n), where n is the number of values x[.] (and weights 
 * w[.]). In benchmark tests for large n, the cost of a median is 
 * about 16 times more costly than a simple mean, and the cost of a 
 * weighted median is about 1.5 times more costly than an unweighted 
 * median.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.10.11
 */
public class MedianFinder {

  /**
   * Constructs a median finder.
   * @param n number of values for which to compute medians.
   */
  public MedianFinder(int n) {
    _n = n;
    _x = new float[n];
  }

  /**
   * Returns the median of the specified array of values.
   * @param x array of values.
   * @return the median.
   */
  public float findMedian(float[] x) {
    Check.argument(_n==x.length,"length of x is valid");
    copy(x,_x);
    int k = (_n-1)/2;
    quickPartialSort(k,_x);
    float xmed = _x[k];
    if (_n%2==0) {
      float xmin = _x[_n-1];
      for (int i=_n-2; i>k; --i)
        if (_x[i]<xmin)
          xmin = _x[i];
      xmed = 0.5f*(xmed+xmin);
    }
    return xmed;
  }

  /**
   * Returns the weighted median of the specified array of values.
   * @param w array of positive weights.
   * @param x array of values.
   * @return the weighted median.
   */
  public float findMedian(float[] w, float[] x) {
    Check.argument(_n==w.length,"length of w is valid");
    Check.argument(_n==x.length,"length of x is valid");
    if (_w==null)
      _w = new float[_n];
    copy(w,_w);
    copy(x,_x);
    if (_n<16) {
      return findMedianSmallN(_w,_x);
    } else {
      return findMedianLargeN(_w,_x);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int[] _m = new int[2]; // array for lower and upper indices
  private int _n; // number of values for which median is computed
  private float[] _w; // copy of weights for partial sorting
  private float[] _x; // copy of values for partial sorting

  private static int med3(float[] a, int i, int j, int k) {
    return a[i]<a[j] ? 
           (a[j]<a[k] ? j : a[i]<a[k] ? k : i) :
           (a[j]>a[k] ? j : a[i]>a[k] ? k : i);
  }

  private static void swap(float[] a, float[] b, int i, int j) {
    float ai = a[i];
    a[i] = a[j];
    a[j] = ai;
    float bi = b[i];
    b[i] = b[j];
    b[j] = bi;
  }

  private static void swap(float[] a, float[] b, int i, int j, int n) {
    while (n>0) {
      float ai = a[i];
      a[i  ] = a[j];
      a[j  ] = ai;
      float bi = b[i];
      b[i++] = b[j];
      b[j++] = bi;
      --n;
    }
  }

  private static void insertionSort(float[] w, float[] x, int p, int q) {
    for (int i=p+1; i<=q; ++i)
      for (int j=i; j>p && x[j-1]>x[j]; --j)
        swap(w,x,j,j-1);
  }

  private static void quickPartition(float[] w, float[] x, int[] m) {
    int p = m[0];
    int q = m[1];
    int k = med3(x,p,(p+q)/2,q);
    float y = x[k];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[b]<=y) {
        if (x[b]==y) 
          swap(w,x,a++,b);
        ++b;
      }
      while (c>=b && x[c]>=y) {
        if (x[c]==y)
          swap(w,x,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(w,x,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(w,x,p,b-r,r);
    swap(w,x,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }

  private float findMedianSmallN(float[] w, float[] x) {

    // Insertion sort is quick for small n.
    for (int i=1; i<_n; ++i)
      for (int j=i; j>0 && x[j-1]>x[j]; --j)
        swap(w,x,j,j-1);

    // Half the sum of all weights = wh. For the median x, 
    // we require wl(x) <= wh and wr(x) <= wh.
    float ws = 0.0f;
    for (int i=0; i<_n; ++i)
      ws += w[i];
    float wh = 0.5f*ws;

    // Index one above upper bound for left sum of weights.
    int kl = 0;
    float wl = w[kl];
    while (wl<wh)
      wl += w[++kl];

    // Index one below lower bound for right sum of weights.
    int kr = _n-1;
    float wr = w[kr];
    while (wr<wh)
      wr += w[--kr];

    // The weighted median.
    if (kl==kr)
      return x[kl];
    else
      return 0.5f*(x[kl]+x[kr]);
  }
 
  private float findMedianLargeN(float[] w, float[] x) {
    int p =    0, p0 = p;
    int q = _n-1, q0 = q;
    float wc = 0.0f; // compensates for weights outside the [p:q] window.
    float xnot = Float.MAX_VALUE;
    float xmed = xnot;
    while (p<q && xmed==xnot) {
      _m[0] = p;
      _m[1] = q;
      quickPartition(w,x,_m); // partition into left, middle, right
      int pp = _m[0];
      int qq = _m[1];
      float wl = 0.0f; // sum left weights
      for (int i=p; i<pp; ++i)
        wl += w[i];
      float wm = 0.0f; // sum middle weights
      for (int i=pp; i<=qq; ++i)
        wm += w[i];
      float wr = 0.0f; // sum right weights
      for (int i=qq+1; i<=q; ++i)
        wr += w[i];
      float dl = wc+wl-wm-wr; // left derivative
      float dr = wc+wl+wm-wr; // right derivative
      if (dl>0.0f) { // if left derivative > 0, ...
        q = pp-1; // minimum lies to the left
        wc -= wm+wr; // decrease weight compensation
      } else if (dr<0.0f) { // if right derivative < 0, ...
        p = qq+1; // minimum lies to the right
        wc += wl+wm; // increase weight compensation
      } else if (dl==0.0f) { // if left-derivative = 0, ...
        float xmax = x[p0]; // find largest value in the left partition
        for (int i=pp-1; i>p0; --i)
          if (x[i]>xmax)
            xmax = x[i];
        xmed = 0.5f*(x[pp]+xmax); // median = average of two values 
      } else if (dr==0.0f) { // if right-derivative = 0, ...
        float xmin = x[q0]; // find smallest value in the right partition
        for (int i=qq+1; i<q0; ++i)
          if (x[i]<xmin)
            xmin = x[i];
        xmed = 0.5f*(x[qq]+xmin); // median = average of two values
      } else { // left-derivative < 0 and right-derivative > 0
        xmed = x[pp]; // so median is unique
      }
    }
    if (xmed==xnot)
      xmed = x[p]; // = x[q]
    return xmed;
  }
}
