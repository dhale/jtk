/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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

import edu.mines.jtk.util.Cdouble;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.max;
import static edu.mines.jtk.util.MathPlus.pow;


/**
 * A recursive filter implemented as a cascade of 2nd-order filters.
 * The output of each 2nd-order recursive filter becomes the input to
 * the next 2nd-order recursive filter in the cascade.
 * <p>
 * An advantage of recursive cascade filters is that they can be
 * applied in-place; input and output arrays may be the same arrays.
 * <p>
 * A disadvantage of recursive cascade filters is that a forward-reverse
 * application yields only an approximation to a symmetric zero-phase 
 * impulse response. This approximation is worst at array ends where the
 * output of each 2nd-order filter truncated.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.19
 */
public class RecursiveCascadeFilter {

  /**
   * Constructs a recursive filter with specified poles, zeros, and gain.
   * The coefficients of this recursive filter are real. Therefore, poles 
   * and zeros with non-zero imaginary parts must have conjugate mates.
   * @param poles array of complex poles.
   * @param zeros array of complex poles.
   * @param gain the filter gain.
   */
  public RecursiveCascadeFilter(
    Cdouble[] poles, Cdouble[] zeros, double gain) {
    init(poles,zeros,gain);
  }

  /**
   * Applies this filter in the forward direction.
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyForward(float[] x, float[] y) {
    _f1[0].applyForward(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].applyForward(y,y);
  }

  /**
   * Applies this filter in the reverse direction.
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyReverse(float[] x, float[] y) {
    _f1[0].applyReverse(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].applyReverse(y,y);
  }

  /**
   * Applies this filter in the forward and reverse directions.
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyForwardReverse(float[] x, float[] y) {
    applyForward(x,y);
    applyReverse(y,y);
    /*
    _f1[0].applyForward(x,y);
    _f1[0].applyReverse(y,y);
    for (int i1=1; i1<_n1; ++i1) {
      _f1[i1].applyForward(y,y);
      _f1[i1].applyReverse(y,y);
    }
    */
  }

  /**
   * Applies this filter along the 1st dimension in the forward direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1Forward(float[][] x, float[][] y) {
    _f1[0].apply1Forward(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply1Forward(y,y);
  }

  /**
   * Applies this filter along the 1st dimension in the reverse direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1Reverse(float[][] x, float[][] y) {
    _f1[0].apply1Reverse(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply1Reverse(y,y);
  }

  /**
   * Applies this filter along the 1st dimension in the forward and 
   * reverse directions.
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1ForwardReverse(float[][] x, float[][] y) {
    apply1Forward(x,y);
    apply1Reverse(y,y);
    /*
    _f1[0].apply1Forward(x,y);
    _f1[0].apply1Reverse(y,y);
    for (int i1=1; i1<_n1; ++i1) {
      _f1[i1].apply1Forward(y,y);
      _f1[i1].apply1Reverse(y,y);
    }
    */
  }

  /**
   * Applies this filter along the 2nd dimension in the forward direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2Forward(float[][] x, float[][] y) {
    _f1[0].apply2Forward(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply2Forward(y,y);
  }

  /**
   * Applies this filter along the 2nd dimension in the reverse direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2Reverse(float[][] x, float[][] y) {
    _f1[0].apply2Reverse(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply2Reverse(y,y);
  }

  /**
   * Applies this filter along the 2nd dimension in the forward and 
   * reverse directions.
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2ForwardReverse(float[][] x, float[][] y) {
    apply2Forward(x,y);
    apply2Reverse(y,y);
    /*
    _f1[0].apply2Forward(x,y);
    _f1[0].apply2Reverse(y,y);
    for (int i1=1; i1<_n1; ++i1) {
      _f1[i1].apply2Forward(y,y);
      _f1[i1].apply2Reverse(y,y);
    }
    */
  }

  /**
   * Applies this filter along the 1st dimension in the forward direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1Forward(float[][][] x, float[][][] y) {
    _f1[0].apply1Forward(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply1Forward(y,y);
  }

  /**
   * Applies this filter along the 1st dimension in the reverse direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1Reverse(float[][][] x, float[][][] y) {
    _f1[0].apply1Reverse(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply1Reverse(y,y);
  }

  /**
   * Applies this filter along the 1st dimension in the forward and 
   * reverse directions.
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1ForwardReverse(float[][][] x, float[][][] y) {
    apply1Forward(x,y);
    apply1Reverse(y,y);
    /*
    _f1[0].apply1Forward(x,y);
    _f1[0].apply1Reverse(y,y);
    for (int i1=1; i1<_n1; ++i1) {
      _f1[i1].apply1Forward(y,y);
      _f1[i1].apply1Reverse(y,y);
    }
    */
  }

  /**
   * Applies this filter along the 2nd dimension in the forward direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2Forward(float[][][] x, float[][][] y) {
    _f1[0].apply2Forward(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply2Forward(y,y);
  }

  /**
   * Applies this filter along the 2nd dimension in the reverse direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2Reverse(float[][][] x, float[][][] y) {
    _f1[0].apply2Reverse(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply2Reverse(y,y);
  }

  /**
   * Applies this filter along the 2nd dimension in the forward and 
   * reverse directions.
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2ForwardReverse(float[][][] x, float[][][] y) {
    apply2Forward(x,y);
    apply2Reverse(y,y);
    /*
    _f1[0].apply2Forward(x,y);
    _f1[0].apply2Reverse(y,y);
    for (int i1=1; i1<_n1; ++i1) {
      _f1[i1].apply2Forward(y,y);
      _f1[i1].apply2Reverse(y,y);
    }
    */
  }

  /**
   * Applies this filter along the 3rd dimension in the forward direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply3Forward(float[][][] x, float[][][] y) {
    _f1[0].apply3Forward(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply3Forward(y,y);
  }

  /**
   * Applies this filter along the 3rd dimension in the reverse direction. 
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply3Reverse(float[][][] x, float[][][] y) {
    _f1[0].apply3Reverse(x,y);
    for (int i1=1; i1<_n1; ++i1)
      _f1[i1].apply3Reverse(y,y);
  }

  /**
   * Applies this filter along the 3rd dimension in the forward and 
   * reverse directions.
   * Input and output arrays may be the same array.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply3ForwardReverse(float[][][] x, float[][][] y) {
    apply3Forward(x,y);
    apply3Reverse(y,y);
    /*
    _f1[0].apply3Forward(x,y);
    _f1[0].apply3Reverse(y,y);
    for (int i1=1; i1<_n1; ++i1) {
      _f1[i1].apply3Forward(y,y);
      _f1[i1].apply3Reverse(y,y);
    }
    */
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected RecursiveCascadeFilter() {
  }

  protected void init(Cdouble[] poles, Cdouble[] zeros, double gain) {
    Check.argument(poles.length>0 || zeros.length>0,
      "at least one pole or zero is specified");

    // Sort poles and zeros so that complex conjugate pairs are first.
    poles = sortPolesOrZeros(poles);
    zeros = sortPolesOrZeros(zeros);

    // Construct 2nd-order filters.
    int np = poles.length;
    int nz = zeros.length;
    _n1 = max((np+1)/2,(nz+1)/2);
    _f1 = new Recursive2ndOrderFilter[_n1];
    gain = pow(gain,1.0/_n1);
    Cdouble c0 = new Cdouble(0.0,0.0);
    for (int i1=0,ip=0,iz=0; i1<_n1; ++i1) {
      Cdouble pole1 = (ip<np)?poles[ip++]:c0;
      Cdouble pole2 = (ip<np)?poles[ip++]:c0;
      Cdouble zero1 = (iz<nz)?zeros[iz++]:c0;
      Cdouble zero2 = (iz<nz)?zeros[iz++]:c0;
      _f1[i1] = new Recursive2ndOrderFilter(pole1,pole2,zero1,zero2,gain);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n1; // number of 2nd-order one-way filters
  private Recursive2ndOrderFilter[] _f1; // array of filters

  /**
   * Sorts array of poles or zeros. After sorting, any complex conjugate 
   * pairs are first in the array, followed by any real poles or zeros.
   * Also ensures that any complex poles or zeros have conjugate mates.
   * @return array of sorted 
   */
  private static Cdouble[] sortPolesOrZeros(Cdouble[] c) {
    c = c.clone(); // copy, so we can set to null during sorting
    int n = c.length;
    Cdouble[] cs = new Cdouble[n];
    int ns = 0;
    for (int i=0; i<n; ++i) {
      if (c[i]!=null && !c[i].isReal()) {
        Cdouble cc = c[i].conj();
        int j = i+1;
        while (j<n && !cc.equals(c[j]))
          ++j;
        Check.argument(j<n,"complex "+c[i]+" has a conjugate mate");
        cs[ns++] = c[i]; // copy pair to
        cs[ns++] = c[j]; // sorted output
        c[i] = null; // set pair to null, so
        c[j] = null; // not considered again
      }
    }
    for (int i=0; i<n; ++i) {
      if (c[i]!=null && c[i].isReal())
        cs[ns++] = c[i];
    }
    return cs;
  }
}
