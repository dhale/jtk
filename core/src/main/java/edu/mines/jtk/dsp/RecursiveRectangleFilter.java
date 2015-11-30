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
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * Recursive implementation of a rectangle filter.
 * The rectangle filter is a moving average defined by the following sum:
 * <pre><code>
 *               m
 *   y[i] = s * sum x[i+j]
 *              j=l
 * 
 * for 0&lt;=i&lt;n, l&lt;=m, and s = 1.0/(1.0+m-l).
 * </code></pre>
 * This recursive implementation computes 
 * <pre><code>
 *   y[i] = y[i-1]+s*(x[i+m]-x[i+l-1]),
 * </code></pre>
 * with care taken to initialize y[0] and handle array index bounds.
 * For long filters (large 1+m-l), this recursive implementation may be 
 * much more efficient than the more straightforward sum for each index i.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.08.13
 */
public class RecursiveRectangleFilter {

  /**
   * Constructs a rectangle filter with specified index bounds.
   * @param l the lower index bound in the sum; must not be greater than m.
   * @param m the upper index bound in the sum; must not be less than l.
   */
  public RecursiveRectangleFilter(int l, int m) {
    Check.argument(l<=m,"l<=m");
    _l = l;
    _m = m;
  }

  /**
   * Applies the filter.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[] x, float[] y) {
    checkArrays(x,y);
    if (x==y)
      x = copy(x);
    int n = x.length;
    int m = _m;
    int l = _l;
    int l1 = l-1;
    float s = 1.0f/(float)(1+m-l);

    // Initialize y[0].
    y[0] = 0.0f;
    int ilo = max(0,l);
    int ihi = min(n,m+1);
    for (int i=ilo; i<ihi; ++i)
      y[0] += s*x[i];

    // Off left: i+m < 0.
    ilo = 1;
    ihi = min(n,-m);
    for (int i=ilo; i<ihi; ++i)
      y[i] = y[i-1];

    // Rolling on: i+l-1 < 0 <= i+m
    ilo = max(1,-m);
    ihi = min(n,n-m,1-l);
    for (int i=ilo; i<ihi; ++i)
      y[i] = y[i-1]+s*x[i+m];

    // Middle: either (i+l-1 < 0 and n <= i+m) or (0 <= i+l-1 and i+m < n)
    if (1-l>n-m) {
      ilo = max(1,n-m);
      ihi = min(n,1-l);
      for (int i=ilo; i<ihi; ++i)
        y[i] = y[i-1];
    } else {
      ilo = max(1,1-l);
      ihi = min(n,n-m);
      for (int i=ilo; i<ihi; ++i)
        y[i] = y[i-1]+s*(x[i+m]-x[i+l1]);
    }

    // Rolling off: i+l-1 < n <= i+m
    ilo = max(1,n-m,1-l);
    ihi = min(n,n+1-l);
    for (int i=ilo; i<ihi; ++i)
      y[i] = y[i-1]-s*x[i+l1];

    // Off right: n <= i+l-1
    ilo = max(1,n+1-l);
    ihi = n;
    for (int i=ilo; i<ihi; ++i)
      y[i] = y[i-1];
  }

  /**
   * Applies the filter along the 1st dimension.
   * Applies no filter along the 2nd dimension.
   * @param x input array.
   * @param y output array.
   */
  public void apply1(float[][] x, float[][] y) {
    checkArrays(x,y);
    int n2 = x.length;
    for (int i2=0; i2<n2; ++i2)
      apply(x[i2],y[i2]);
  }

  /**
   * Applies the filter along the 2nd dimension.
   * Applies no filter along the 1st dimension.
   * @param x input array.
   * @param y output array.
   */
  public void apply2(float[][] x, float[][] y) {
    checkArrays(x,y);
    int n1 = x[0].length;
    int n2 = x.length;
    if (x==y)
      x = copy(x);
    int m = _m;
    int l = _l;
    float s = 1.0f/(float)(1+m-l);

    // Initialize y[0].
    zero(y[0]);
    int i2lo = max(0,l);
    int i2hi = min(n2,m+1);
    for (int i2=i2lo; i2<i2hi; ++i2) {
      float[] y2 = y[0];
      float[] x2 = x[i2];
      for (int i1=0; i1<n1; ++i1)
        y2[i1] += s*x2[i1];
    }

    // Off left: i2+m < 0.
    i2lo = 1;
    i2hi = min(n2,-m);
    for (int i2=i2lo; i2<i2hi; ++i2) {
      float[] y2 = y[i2];
      float[] y2p = y[i2-1];
      for (int i1=0; i1<n1; ++i1)
        y2[i1] = y2p[i1];
    }

    // Rolling on: i2+l-1 < 0 <= i2+m
    i2lo = max(1,-m);
    i2hi = min(n2,n2-m,1-l);
    for (int i2=i2lo; i2<i2hi; ++i2) {
      float[] y2 = y[i2];
      float[] y2p = y[i2-1];
      float[] x2m = x[i2+m];
      for (int i1=0; i1<n1; ++i1)
        y2[i1] = y2p[i1]+s*x2m[i1];
    }

    // Middle: either (i2+l-1 < 0 and n2 <= i2+m) 
    //             or (0 <= i2+l-1 and i2+m < n2)
    if (1-l>n2-m) {
      i2lo = max(1,n2-m);
      i2hi = min(n2,1-l);
      for (int i2=i2lo; i2<i2hi; ++i2) {
        float[] y2 = y[i2];
        float[] y2p = y[i2-1];
        for (int i1=0; i1<n1; ++i1)
          y2[i1] = y2p[i1];
      }
    } else {
      i2lo = max(1,1-l);
      i2hi = min(n2,n2-m);
      for (int i2=i2lo; i2<i2hi; ++i2) {
        float[] y2 = y[i2];
        float[] y2p = y[i2-1];
        float[] x2m = x[i2+m];
        float[] x2l = x[i2+l-1];
        for (int i1=0; i1<n1; ++i1)
          y2[i1] = y2p[i1]+s*(x2m[i1]-x2l[i1]);
      }
    }

    // Rolling off: i2+l-1 < n2 <= i2+m
    i2lo = max(1,n2-m,1-l);
    i2hi = min(n2,n2+1-l);
    for (int i2=i2lo; i2<i2hi; ++i2) {
      float[] y2 = y[i2];
      float[] y2p = y[i2-1];
      float[] x2l = x[i2+l-1];
      for (int i1=0; i1<n1; ++i1)
        y2[i1] = y2p[i1]-s*x2l[i1];
    }

    // Off right: n2 <= i2+l-1
    i2lo = max(1,n2+1-l);
    i2hi = n2;
    for (int i2=i2lo; i2<i2hi; ++i2) {
      float[] y2 = y[i2];
      float[] y2p = y[i2-1];
      for (int i1=0; i1<n1; ++i1)
        y2[i1] = y2p[i1];
    }
  }

  /**
   * Applies the filter along the 1st dimension.
   * Applies no filter along the 2nd or 3rd dimensions.
   * @param x input array.
   * @param y output array.
   */
  public void apply1(float[][][] x, float[][][] y) {
    checkArrays(x,y);
    int n3 = y.length;
    for (int i3=0; i3<n3; ++i3)
      apply1(x[i3],y[i3]);
  }

  /**
   * Applies the filter along the 2nd dimension.
   * Applies no filter along the 1st or 3rd dimensions.
   * @param x input array.
   * @param y output array.
   */
  public void apply2(float[][][] x, float[][][] y) {
    checkArrays(x,y);
    int n3 = y.length;
    for (int i3=0; i3<n3; ++i3)
      apply2(x[i3],y[i3]);
  }

  /**
   * Applies the filter along the 3rd dimension.
   * Applies no filter along the 1st or 2nd dimensions.
   * @param x input array.
   * @param y output array.
   */
  public void apply3(float[][][] x, float[][][] y) {
    checkArrays(x,y);
    int n3 = y.length;
    int n2 = y[0].length;
    int n1 = y[0][0].length;
    float[][] x2 = new float[n3][n1];
    float[][] y2 = new float[n3][n1];
    for (int i2=0; i2<n2; ++i2) {
      for (int i3=0; i3<n3; ++i3) {
        float[] x32 = x[i3][i2];
        float[] x23 = x2[i3];
        for (int i1=0; i1<n1; ++i1) {
          x23[i1] = x32[i1];
        }
      }
      apply2(x2,y2);
      for (int i3=0; i3<n3; ++i3) {
        float[] y32 = y[i3][i2];
        float[] y23 = y2[i3];
        for (int i1=0; i1<n1; ++i1) {
          y32[i1] = y23[i1];
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _l; // lower index bound
  private int _m; // upper index bound

  private static void checkArrays(float[] x, float[] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
  }

  private static void checkArrays(float[][] x, float[][] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
    Check.argument(x[0].length==y[0].length,"x[0].length==y[0].length");
    Check.argument(isRegular(x),"x is regular");
    Check.argument(isRegular(y),"y is regular");
  }

  private static void checkArrays(float[][][] x, float[][][] y) {
    Check.argument(x.length==y.length,"x.length==y.length");
    Check.argument(x[0].length==y[0].length,"x[0].length==y[0].length");
    Check.argument(x[0][0].length==y[0][0].length,
      "x[0][0].length==y[0][0].length");
    Check.argument(isRegular(x),"x is regular");
    Check.argument(isRegular(y),"y is regular");
  }
}
