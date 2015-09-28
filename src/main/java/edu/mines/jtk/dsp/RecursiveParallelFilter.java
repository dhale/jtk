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

/**
 * A recursive parallel filter is implemented as a sum of 2nd-order filters.
 * In other words, the output of a recursive parallel filter is the sum of 
 * the outputs of 2nd-order recursive filters applied to the same input.
 * <p>
 * An advantage of recursive parallel filters is that they can be applied
 * in both forward and reverse directions to obtain symmetric zero-phase 
 * filters, without end effects. The 2nd-order filters applied in this
 * two-way forward-and-reverse application are not the same as those 
 * applied in one-way forward or reverse applications.
 * <p>
 * A disadvantage of recursive parallel filters is that they cannot be
 * applied in-place; input and output arrays must be distinct arrays.
 * Also, in the current implementation, the number of non-zero zeros
 * cannot exceed the number of non-zero poles, and all poles must be
 * unique.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.19
 */
public class RecursiveParallelFilter {

  /**
   * Constructs a recursive filter with specified poles, zeros, and gain.
   * Any poles or zeros at zero (the origin of the complex z-plane) are
   * ignored. The number of non-zero zeros cannot exceed the number of 
   * non-zero poles, and all poles must be unique.
   * @param poles array of complex poles.
   * @param zeros array of complex poles.
   * @param gain the filter gain.
   */
  public RecursiveParallelFilter(
    Cdouble[] poles, Cdouble[] zeros, double gain) 
  {
    init(poles,zeros,gain);
  }

  /**
   * Applies this filter in the forward direction. 
   * Input and output arrays must be distinct arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyForward(float[] x, float[] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulateForward(x,y);
  }

  /**
   * Applies this filter in the reverse direction. 
   * Input and output arrays must be distinct arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyReverse(float[] x, float[] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulateReverse(x,y);
  }

  /**
   * Applies this filter in the forward and reverse directions.
   * Note that this method does not simply call the methods
   * {@link #applyForward(float[],float[])} and
   * {@link #applyReverse(float[],float[])} in sequence.
   * Input and output arrays must be distinct arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void applyForwardReverse(float[] x, float[] y) {
    scale(_c*_g,x,y);
    for (int i2=0; i2<_n2; i2+=2) {
      _f2[i2  ].accumulateForward(x,y);
      _f2[i2+1].accumulateReverse(x,y);
    }
  }

  /**
   * Applies this filter along the 1st dimension in the forward direction. 
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1Forward(float[][] x, float[][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate1Forward(x,y);
  }

  /**
   * Applies this filter along the 1st dimension in the reverse direction. 
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1Reverse(float[][] x, float[][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate1Reverse(x,y);
  }

  /**
   * Applies this filter along the 1st dimension in the forward and 
   * reverse directions.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1ForwardReverse(float[][] x, float[][] y) {
    scale(_c*_g,x,y);
    for (int i2=0; i2<_n2; i2+=2) {
      _f2[i2  ].accumulate1Forward(x,y);
      _f2[i2+1].accumulate1Reverse(x,y);
    }
  }

  /**
   * Applies this filter along the 2nd dimension in the forward direction.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2Forward(float[][] x, float[][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate2Forward(x,y);
  }

  /**
   * Applies this filter along the 2nd dimension in the reverse direction.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2Reverse(float[][] x, float[][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate2Reverse(x,y);
  }

  /**
   * Applies this filter along the 2nd dimension in the forward and 
   * reverse directions.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2ForwardReverse(float[][] x, float[][] y) {
    scale(_c*_g,x,y);
    for (int i2=0; i2<_n2; i2+=2) {
      _f2[i2  ].accumulate2Forward(x,y);
      _f2[i2+1].accumulate2Reverse(x,y);
    }
  }

  /**
   * Applies this filter along the 1st dimension in the forward direction. 
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1Forward(float[][][] x, float[][][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate1Forward(x,y);
  }

  /**
   * Applies this filter along the 1st dimension in the reverse direction. 
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1Reverse(float[][][] x, float[][][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate1Reverse(x,y);
  }

  /**
   * Applies this filter along the 1st dimension in the forward and 
   * reverse directions.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply1ForwardReverse(float[][][] x, float[][][] y) {
    scale(_c*_g,x,y);
    for (int i2=0; i2<_n2; i2+=2) {
      _f2[i2  ].accumulate1Forward(x,y);
      _f2[i2+1].accumulate1Reverse(x,y);
    }
  }

  /**
   * Applies this filter along the 2nd dimension in the forward direction.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2Forward(float[][][] x, float[][][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate2Forward(x,y);
  }

  /**
   * Applies this filter along the 2nd dimension in the reverse direction.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2Reverse(float[][][] x, float[][][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate2Reverse(x,y);
  }

  /**
   * Applies this filter along the 2nd dimension in the forward and 
   * reverse directions.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply2ForwardReverse(float[][][] x, float[][][] y) {
    scale(_c*_g,x,y);
    for (int i2=0; i2<_n2; i2+=2) {
      _f2[i2  ].accumulate2Forward(x,y);
      _f2[i2+1].accumulate2Reverse(x,y);
    }
  }

  /**
   * Applies this filter along the 3rd dimension in the forward direction.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply3Forward(float[][][] x, float[][][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate3Forward(x,y);
  }

  /**
   * Applies this filter along the 3rd dimension in the reverse direction.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply3Reverse(float[][][] x, float[][][] y) {
    scale(_c,x,y);
    for (int i1=0; i1<_n1; ++i1)
      _f1[i1].accumulate3Reverse(x,y);
  }

  /**
   * Applies this filter along the 3rd dimension in the forward and 
   * reverse directions.
   * Input and output arrays must be distinct regular arrays.
   * Lengths of the input and output arrays must be equal.
   * @param x the input array.
   * @param y the output array.
   */
  public void apply3ForwardReverse(float[][][] x, float[][][] y) {
    scale(_c*_g,x,y);
    for (int i2=0; i2<_n2; i2+=2) {
      _f2[i2  ].accumulate3Forward(x,y);
      _f2[i2+1].accumulate3Reverse(x,y);
    }
  }

  /**
   * For experimental use only.
   */
  public void applyFrf(float[] x, float[] y) {
    scale(_c*_g,x,y);
    for (int i2=0; i2<_n2; i2+=2) {
      _f2[i2  ].accumulateForward(x,y);
      //_f2[i2+1].accumulateReverse(x,y);
    }
  }

  /**
   * For experimental use only.
   */
  public void applyFrr(float[] x, float[] y) {
    scale(_c*_g,x,y);
    for (int i2=0; i2<_n2; i2+=2) {
      //_f2[i2  ].accumulateForward(x,y);
      _f2[i2+1].accumulateReverse(x,y);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected RecursiveParallelFilter() {
  }

  protected void init(Cdouble[] poles, Cdouble[] zeros, double gain) {

    // Ignore any poles or zeros at the origin.
    poles = nonZero(poles);
    zeros = nonZero(zeros);

    // Number of non-zero zeros must not exceed number of non-zero poles.
    Check.argument(
      zeros.length<=poles.length,
      "number of non-zero zeros does not exceed number of non-zero poles");

    // Non-zero poles must be unique.
    Check.argument(polesUnique(poles),"all poles are unique");

    // Number of poles and zeros.
    _np = poles.length;
    _nz = zeros.length;

    // Sort poles and zeros so that complex conjugate pairs are first.
    poles = sort(poles);
    zeros = sort(zeros);

    // Count complex and real poles.
    _nc = 0;
    _nr = 0;
    for (int ip=0; ip<_np; ++ip) {
      if (poles[ip].i!=0.0) {
        ++_nc;
      } else {
        ++_nr;
      }
    }

    // Construct 2nd-order filters and constant scale factor c. One-way 
    // forward or reverse filters consist of n1 2nd-order filters. Two-way 
    // forward-and-reverse filters consist of n2 = 2*n1 2nd-order filters.
    _n1 = _nr+_nc/2;
    _n2 = 2*_n1;
    _f1 = new Recursive2ndOrderFilter[_n1];
    _f2 = new Recursive2ndOrderFilter[_n2];
    double c = (_nz==_np)?gain:0.0;
    for (int i1=0,i2=0,jp=0; i1<_n1; ++jp) {
      Cdouble pj = poles[jp];
      Cdouble hi = hi(pj,poles,zeros,gain);
      Cdouble hj = hr(pj,poles,zeros,gain);
      Cdouble hihj = hi.times(hj);
      double fb0,fb1,fb2,rb0,rb1,rb2,b0,b1,b2,a1,a2;
      if (pj.i==0.0) { // if pole is real, ...
        a1 = -pj.r;
        a2 = 0.0;
        b0 = hj.r;
        b1 = 0.0;
        b2 = 0.0;
        fb0 = hihj.r;
        fb1 = 0.0;
        fb2 = 0.0;
        rb0 = 0.0;
        rb1 = -fb0*a1;
        rb2 = 0.0;
      } else { // else if pole is complex, ...
        ++jp; // skip its conjugate mate
        Cdouble qj = pj.inv();
        a1 = -2.0*pj.r;
        a2 = pj.norm();
        b0 = hj.r-hj.i*qj.r/qj.i;
        b1 = hj.i/qj.i;
        b2 = 0.0;
        fb0 = hihj.r-hihj.i*qj.r/qj.i;
        fb1 = hihj.i/qj.i;
        fb2 = 0.0;
        rb0 = 0.0;
        rb1 = fb1-fb0*a1;
        rb2 = -fb0*a2;
      }
      _f1[i1++] = makeFilter(b0,b1,b2,a1,a2);
      _f2[i2++] = makeFilter(fb0,fb1,fb2,a1,a2);
      _f2[i2++] = makeFilter(rb0,rb1,rb2,a1,a2);
      if (_nz==_np)
        c -= b0;
    }
    _c = (float)c;
    _g = (float)gain;
  }
  private static Recursive2ndOrderFilter makeFilter(
    double b0, double b1, double b2, double a1, double a2)
  {
    return new Recursive2ndOrderFilter(
      (float)b0,(float)b1,(float)b2,(float)a1,(float)a2);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _np; // number of poles
  private int _nz; // number of zeros
  private int _nc; // number of complex poles (must be even)
  private int _nr; // number of real poles
  private float _c; // constant scale factor
  private float _g; // filter gain
  private int _n1; // number of 2nd-order filters for one-way filtering
  private int _n2; // number of 2nd-order filters for two-way filtering
  private Recursive2ndOrderFilter[] _f1; // for one-way filtering
  private Recursive2ndOrderFilter[] _f2; // for two-way filtering

  /**
   * Evaluates H(1/z).
   */
  private Cdouble hi(
    Cdouble z, Cdouble[] poles, Cdouble[] zeros, double gain) 
  {
    Cdouble c1 = new Cdouble(1.0,0.0);
    Cdouble hz = new Cdouble(c1);
    for (int iz=0; iz<_nz; ++iz) {
      Cdouble zi = zeros[iz];
      hz.timesEquals(c1.minus(zi.times(z)));
    }
    Cdouble hp = new Cdouble(c1);
    for (int ip=0; ip<_np; ++ip) {
      Cdouble pi = poles[ip];
      hp.timesEquals(c1.minus(pi.times(z)));
    }
    return hz.over(hp).times(gain);
  }

  /**
   * Evaluates residue of H(z) for the j'th pole. The residue is H(z) 
   * evaluated without division by the factor corresponding to the j'th 
   * pole of H(z), which would be zero. If that pole is complex (has a 
   * non-zero imaginary part), then division by the factor corresponding 
   * to its conjugate mate pole is omitted as well.
   */
  private Cdouble hr(
    Cdouble polej, Cdouble[] poles, Cdouble[] zeros, double gain) 
  {
    Cdouble pj = polej;
    Cdouble qj = pj.inv();
    Cdouble c1 = new Cdouble(1.0,0.0);
    Cdouble hz = new Cdouble(c1);
    for (int iz=0; iz<_nz; ++iz) {
      Cdouble zi = zeros[iz];
      hz.timesEquals(c1.minus(zi.times(qj)));
    }
    Cdouble hp = new Cdouble(c1);
    for (int ip=0; ip<_np; ++ip) {
      Cdouble pi = poles[ip];
      if (!pi.equals(pj) && !pi.equals(pj.conj()))
        hp.timesEquals(c1.minus(pi.times(qj)));
    }
    return hz.over(hp).times(gain);
  }

  /**
   * Returns true if all poles are unique; false, otherwise.
   */
  private static boolean polesUnique(Cdouble[] poles) {
    int np = poles.length;
    for (int ip=0; ip<np; ++ip) {
      Cdouble pi = poles[ip];
      for (int jp=ip+1; jp<np; ++jp) {
        Cdouble pj = poles[jp];
        if (pi.equals(pj))
          return false;
      }
    }
    return true;
  }

  /**
   * Returns non-zero poles or zeros.
   */
  private static Cdouble[] nonZero(Cdouble[] c) {
    int n = c.length;
    int m = 0;
    for (int i=0; i<n; ++i) {
      if (c[i].r!=0.0 || c[i].i!=0.0)
        ++m;
    }
    Cdouble[] d = new Cdouble[m];
    m = 0;
    for (int i=0; i<n; ++i) {
      if (c[i].r!=0.0 || c[i].i!=0.0)
        d[m++] = c[i];
    }
    return d;
  }

  /**
   * Sorts array of poles or zeros. After sorting, any complex conjugate 
   * pairs are first in the array, followed by any real poles or zeros.
   * Also ensures that any complex poles or zeros have conjugate mates.
   * @return array of sorted poles or zeros.
   */
  private static Cdouble[] sort(Cdouble[] c) {
    int n = c.length;
    Cdouble[] cs = new Cdouble[n];
    int ns = 0;
    for (int i=0; i<n; ++i) {
      if (!c[i].isReal()) {
        Cdouble cc = c[i].conj();
        int j = 0;
        while (j<n && !cc.equals(c[j]))
          ++j;
        Check.argument(j<n,"complex "+c[i]+" has a conjugate mate");
        if (i<j) {
          cs[ns++] = c[i];
          cs[ns++] = c[j];
        }
      }
    }
    for (int i=0; i<n; ++i) {
      if (c[i].isReal())
        cs[ns++] = c[i];
    }
    return cs;
  }

  private static void scale(float s, float[] x, float[] y) {
    int n1 = y.length;
    for (int i1=0; i1<n1; ++i1)
      y[i1] = s*x[i1];
  }

  private static void scale(float s, float[][] x, float[][] y) {
    int n2 = y.length;
    int n1 = y[0].length;
    for (int i2=0; i2<n2; ++i2) {
      float[] x2 = x[i2];
      float[] y2 = y[i2];
      for (int i1=0; i1<n1; ++i1)
        y2[i1] = s*x2[i1];
    }
  }

  private static void scale(float s, float[][][] x, float[][][] y) {
    int n3 = y.length;
    int n2 = y[0].length;
    int n1 = y[0][0].length;
    for (int i3=0; i3<n3; ++i3) {
      float[][] x3 = x[i3];
      float[][] y3 = y[i3];
      for (int i2=0; i2<n2; ++i2) {
        float[] x32 = x3[i2];
        float[] y32 = y3[i2];
        for (int i1=0; i1<n1; ++i1)
          y32[i1] = s*x32[i1];
      }
    }
  }
}
