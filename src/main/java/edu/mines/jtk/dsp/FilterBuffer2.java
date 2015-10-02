/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
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

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * A buffer that facilitates the implementation of 2D filters. In effect, 
 * this class pads a 2D array with extra values on the sides, but without 
 * making a padded copy of the entire 2D array. Instead, this buffer 
 * maintains copies of only a subset of the 1D arrays stored in the 2D 
 * array. For typical 2D filters, that subset includes all input values 
 * required to update a subset (often a single 1D array) of output values.
 * <p>
 * Output (filtered) values y[i2][i1] are assumed to depend on a limited
 * subset of input values x[i2-l2:i2+m2][i1-l1:i1+m1], where l1, m1, l2, 
 * and m2 are non-negative integers that define the extent of the filter. 
 * Finite-difference stencils are examples of such filters.
 * <p>
 * One purpose of this buffer is to pad the input array so that indices
 * such as i1-l1, i1+m1, i2-l2, and i2+m2 are never out of bounds, say,
 * when i1-l1&lt;0. Instead, values are extrapolated, when necessary.
 * Padding with extrapolated values simplifies filters by eliminating 
 * special processing near the ends of arrays. Here, for example, is a
 * program for a simple five-sample finite-difference approximation to
 * a Laplacian:
 * <pre><code>
 *  float[][] x = ... // an array[n2][n1] of input values
 *  float[][] y = ... // an array[n2][n1] of output values
 *  FilterBuffer2 fbx = new FilterBuffer2(1,1,1,1,x); // l1 = m1 = l2 = m2 = 1
 *  fbx.setExtrapolation(FilterBuffer2.Extrapolation.ZERO_SLOPE);
 *  for (int i2=0; i2&lt;n2; ++i2) { // the outer loop
 *    float[] xm = fbx.get(i2-1); // x[i2-1], extrapolation when i2 = 0
 *    float[] x0 = fbx.get(i2  ); // x[i2  ], extrapolation unnecessary
 *    float[] xp = fbx.get(i2+1); // x[i2+1], extrapolation when i2 = n2-1
 *    float[] y0 = y[i2]; // cache output array reference, for efficiency
 *    for (int i1=0,j1=1; i1&lt;n1; ++i1,++j1) // the inner loop
 *      y0[i1] = xm[j1]+xp[j1]+x0[j1-1]+x0[j1+1]-4.0f*x0[j1];
 *  }
 * </code></pre>
 * In the inner loop, the input index j1 is one greater than the output
 * index i1. This difference accounts for the padding with an extra 
 * l1 = 1 value at the beginning of the arrays xm, x0 and xp. An extra 
 * m1 = 1 value of padding is also provided at the end of each of these 
 * three arrays. In other words, the arrays xm, x0 and xp each contain 
 * l1+n1+m1 values. This padding enables the inner loop to be written 
 * simply, with no special cases near the ends of the arrays.
 * <p>
 * While simple, the program above is also efficient. First, the buffer
 * contains only three 1D arrays, independent of the 2D array length n2. 
 * Second, although input array values must be copied to the buffer, 
 * those values are used multiple times before being replaced by other 
 * values.
 * <p>
 * Another purpose of this class is to facilitate filtering in place. In 
 * the example above, the input array x and output array y can be the same 
 * array, so that output values replace input values. This works because 
 * input values x are copied into the buffer <em>before</em> they are 
 * overwritten by output values y.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.12.21
 */
public class FilterBuffer2 {

  /**
   * The mode indicates whether buffered values will be accessed and/or 
   * modified. The default mode is input-only, which implies access 
   * without modification.
   */
  public enum Mode {
    /**
     * Input-only buffer. When first got, values are copied from the
     * buffered array into this buffer, where they may be accessed but
     * should not be modified. Before being replaced, values in an 
     * input-only buffer are never copied back into the buffered array.
     * Flushing an input-only buffer does nothing.
     */
    INPUT,
    /**
     * Output-only buffer. When first got, buffered values are zeroed.
     * Because buffered values may be modified, they are copied back to 
     * the buffered array (1) when necessary to make room for other 
     * values gotten or (2) when the output buffer is flushed.
     */
    OUTPUT,
    /**
     * Input-output buffer. When first got, values are copied from the
     * buffered array into this buffer, where they may be both accessed
     * and modified. Buffered values are copied back to the buffered 
     * array (1) when necessary to make room for other values gotten 
     * or (2) when the output buffer is flushed.
     */
    INPUT_OUTPUT
  }

  /**
   * The method used to extrapolate values beyond the ends of input arrays.
   * The default is extrapolation with zero values.
   */
  public enum Extrapolation {
    /**
     * Extrapolate with zero values.
     */
    ZERO_VALUE,
    /**
     * Extrapolate values at the ends with zero slope.
     */
    ZERO_SLOPE
  }

  /**
   * Constructs a buffer for the specified array. The buffer will 
   * store l2+1+m2 1D arrays, each with l1+n1+m1 values. Array
   * lengths n1 and n2 are set to n1=a[0].length and n2=a.length.
   * @param l1 number of extra values at beginning in 1st dimension.
   * @param m1 number of extra values at end in 1st dimension.
   * @param l2 number of extra values at beginning in 2nd dimension.
   * @param m2 number of extra values at end in 2nd dimension.
   * @param a the array[n2][n1] of values to be buffered.
   */
  public FilterBuffer2(int l1, int m1, int l2, int m2, float[][] a) {
    this(l1,m1,a[0].length,l2,m2,a.length);
    setArray(a);
  }

  /**
   * Constructs a buffer for specified array lengths. The buffer 
   * will store l2+1+m2 1D arrays, each with l1+n1+m1 values. Array
   * lengths n1 and n2 are specified explicitly here. An array with
   * those lengths must be specified later, but before accessing 
   * buffered values.
   * @param l1 number of extra values at beginning in 1st dimension.
   * @param m1 number of extra values at end in 1st dimension.
   * @param n1 number of values (not counting extras) in 1st dimension.
   * @param l2 number of extra values at beginning in 2nd dimension.
   * @param m2 number of extra values at end in 2nd dimension.
   * @param n2 number of values (not counting extras) in 2nd dimension.
   */
  public FilterBuffer2(int l1, int m1, int n1, int l2, int m2, int n2) {
    _l1 = l1;
    _m1 = m1;
    _n1 = n1;
    _l2 = l2;
    _m2 = m2;
    _n2 = n2;
    _nb1 = _l1+_n1+_m1;
    _nb2 = _l2+1+_m2;
    _i = new int[_nb2];
    _b = new float[_nb2][_nb1];
  }

  /**
   * Sets the array of values to be buffered. Array lengths must match 
   * those with which this buffer was constructed. Any values currently 
   * in this buffer are forgotten. Therefore, this method should not be
   * called while looping over array values.
   * @param a the array; referenced, not copied.
   */
  public void setArray(float[][] a) {
    Check.argument(_n1==a[0].length,"a[0].length is valid");
    Check.argument(_n2==a.length,"a.length is valid");
    _a = a;
    for (int j2=0; j2<_nb2; ++j2)
      _i[j2] = NO_INDEX;
  }

  /**
   * Sets the method used to extrapolate values beyond the ends of arrays.
   * @param extrapolation the extrapolation method.
   */
  public void setExtrapolation(Extrapolation extrapolation) {
    _extrapolation = extrapolation;
  }

  /**
   * Sets the mode (input, output, or input-output) for this buffer.
   * @param mode the mode.
   */
  public void setMode(Mode mode) {
    _input = mode==Mode.INPUT || mode==Mode.INPUT_OUTPUT;
    _output = mode==Mode.OUTPUT || mode==Mode.INPUT_OUTPUT;
  }

  /**
   * Copies values from the buffered array into this buffer.
   * If the values are already in this buffer, they are assumed to be 
   * unchanged, and no copy is performed. In either case, this method
   * returns a referenced to the buffered values.
   * <p>
   * The returned buffered array has l1+m1 extra values at the ends;
   * the first l1 values and the last m1 values are extrapolated.
   * @param i2 index in 2nd dimension of the buffered array to get.
   * @return reference to an array of buffered values.
   */
  public float[] get(int i2) {
    checkIndex(i2);
    int j2 = j2(i2);
    if (_i[j2]!=i2) {
      int k2 = _i[j2];
      if (_output && 0<=k2 && k2<_n2)
        copy(_n1,_l1,_b[j2],0,_a[k2]);
      if (_output && !_input) {
        zero(_b[j2]);
      } else if (_extrapolation==Extrapolation.ZERO_SLOPE) {
        k2 = max(0,min(_n2-1,i2));
        copy(_n1,0,_a[k2],_l1,_b[j2]);
        fill(_a[k2][0],_l1,0,_b[j2]);
        fill(_a[k2][_n1-1],_m1,_l1+_n1,_b[j2]);
      } else if (_extrapolation==Extrapolation.ZERO_VALUE) {
        if (0<=i2 && i2<_n2) {
          copy(_n1,0,_a[i2],_l1,_b[j2]);
          fill(0.0f,_l1,0,_b[j2]);
          fill(0.0f,_m1,_l1+_n1,_b[j2]);
        } else {
          zero(_b[j2]);
        }
      }
      _i[j2] = i2;
    }
    return _b[j2];
  }

  /**
   * Flushes this buffer, if it is an output or input-output buffer. 
   * Flushing copies all values (except extrapolated values) from this 
   * buffer to its buffered array. This method should be called after 
   * any loops in which output values have been buffered. For input-only 
   * buffers, this method does nothing.
   */
  public void flush() {
    if (_output) {
      for (int j2=0; j2<_nb2; ++j2) {
        int i2 = _i[j2];
        if (0<=i2 && i2<_n2)
          copy(_n1,_l1,_b[j2],0,_a[i2]);
        _i[j2] = NO_INDEX;
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // private

  private int NO_INDEX = Integer.MIN_VALUE;

  private int _l1,_l2;
  private int _m1,_m2;
  private int _n1,_n2;
  private int _nb1,_nb2;
  private float[][] _a,_b;
  private int[] _i;
  private boolean _input = true;
  private boolean _output = false;
  private Extrapolation _extrapolation = Extrapolation.ZERO_VALUE;

  private int j2(int i2) {
    return (i2+_l2)%_nb2;
  }

  private static void fill(float a, int n, int j, float[] b) {
    for (; n>0; --n,++j)
      b[j] = a;
  }

  private void checkIndex(int i2) {
    Check.state(_a!=null,"array of values has been specified");
    Check.argument(-_l2<=i2 && i2<=_n2+_m2-1,"index i2="+i2+" is in bounds");
  }
}
