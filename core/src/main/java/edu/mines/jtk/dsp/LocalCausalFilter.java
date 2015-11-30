/****************************************************************************
Copyright 2007, Colorado School of Mines and others.
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
 * A multi-dimensional causal filter with locally variable coefficients.
 * The output samples of a causal filter depend only on present and past 
 * input samples. In two dimensions, causal filters are also called 
 * non-symmetric half-plane (NSHP) filters, and this notion of causal 
 * can be extended to higher dimensions.
 * <p>
 * Local causal filters have coefficients that may vary from one output
 * sample to the next. Such a filter is <em>not</em> shift invariant. Its 
 * application is not equivalent to convolution with its impulse response.
 * <p>
 * Though not shift-invariant, a local causal filter is a linear operator 
 * with a corresponding anti-causal transpose (adjoint) operator. A local 
 * causal filter may have a causal inverse, and its transpose may have an 
 * anti-causal inverse.
 * <p>
 * A local causal filter is a stable all-zero filter that may or may not 
 * be minimum-phase; that is, it may or may not have a causal stable 
 * inverse. That inverse is a recursive all-pole filter, as described by
 * Claerbout, J., 1998, Multidimensional recursive filters via a helix: 
 * Geophysics, v. 63, n. 5, p. 1532-1541.
 * <p>
 * The filter and its transpose and inverse may all be applied in-place; 
 * that is, the input and output arrays may be the same array. However,
 * <em>the inverse-transpose filter cannot be applied in-place.</em>
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.15
 */
public class LocalCausalFilter {

  /**
   * Interface for filter coefficients indexed in 1 dimension.
   * Filter coefficients may vary with sample index, and will be got 
   * through this interface for every output sample computed.
   */
  public interface A1 {
    
    /**
     * Gets local filter coefficients for the specified sample.
     * @param i1 sample index in 1st dimension.
     * @param a array to be filled with coefficients.
     */
    public void get(int i1, float[] a);
  }

  /**
   * Interface for filter coefficients indexed in 2 dimensions.
   * Filter coefficients may vary with sample indices, and will be got 
   * through this interface for every output sample computed.
   */
  public interface A2 {
    
    /**
     * Gets local filter coefficients for the specified sample.
     * @param i1 sample index in 1st dimension.
     * @param i2 sample index in 2nd dimension.
     * @param a array to be filled with coefficients.
     */
    public void get(int i1, int i2, float[] a);
  }

  /**
   * Interface for filter coefficients indexed in 3 dimensions.
   * Filter coefficients may vary with sample indices, and will be got 
   * through this interface for every output sample computed.
   */
  public interface A3 {
    
    /**
     * Gets local filter coefficients for the specified sample.
     * @param i1 sample index in 1st dimension.
     * @param i2 sample index in 2nd dimension.
     * @param i3 sample index in 3rd dimension.
     * @param a array to be filled with coefficients.
     */
    public void get(int i1, int i2, int i3, float[] a);
  }

  /**
   * Constructs a local causal filter for specified lag1.
   * By default, all lag2 and lag3 are assumed to be zero.
   * <p>
   * For j=0 only, lag1[j] is zero.
   * All lag1[j] must be non-negative.
   * @param lag1 array of lags.
   */
  public LocalCausalFilter(int[] lag1) {
    initLags(lag1);
  }

  /**
   * Constructs a local causal filter for specified lag1 and lag2.
   * By default, all lag3 are assumed to be zero.
   * <p>
   * For j=0 only, lag1[j] and lag2[j] are zero.
   * All lag2[j] must be non-negative.
   * If lag2[j] is zero, then lag1[j] must be non-negative.
   * @param lag1 array of lags in 1st dimension.
   * @param lag2 array of lags in 2nd dimension.
   */
  public LocalCausalFilter(int[] lag1, int[] lag2) {
    initLags(lag1,lag2);
  }

  /**
   * Constructs a local causal filter for specified lag1, lag2, and lag3.
   * <p>
   * For j=0 only, lag1[j] and lag2[j] and lag3[j] are zero.
   * All lag3[j] must be non-negative.
   * If lag3[j] is zero, then lag2[j] must be non-negative.
   * If lag3[j] and lag2[j] are zero, then lag1[j] must be non-negative.
   * @param lag1 array of lags in 1st dimension.
   * @param lag2 array of lags in 2nd dimension.
   * @param lag3 array of lags in 3rd dimension.
   */
  public LocalCausalFilter(int[] lag1, int[] lag2, int[] lag3) {
    initLags(lag1,lag2,lag3);
  }

  /**
   * Gets a copy of the lags in the 1st dimension.
   * @return array of lags; by copy, not by reference.
   */
  public int[] getLag1() {
    return copy(_lag1);
  }

  /**
   * Gets a copy of the lags in the 2nd dimension.
   * @return array of lags; by copy, not by reference.
   */
  public int[] getLag2() {
    return copy(_lag2);
  }

  /**
   * Gets a copy of the lags in the 3rd dimension.
   * @return array of lags; by copy, not by reference.
   */
  public int[] getLag3() {
    return copy(_lag3);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Note to programmers:
  // The filter implementations below are optimized to minimize if-tests in
  // inner loops. Each of the methods apply, applyTranspose, applyInverse 
  // and applyInverseTranspose could be implemented much more simply by only 
  // one set of nested loops. By splitting that set into multiple sections,
  // we eliminate some if-tests for most iterations. This optimization is
  // especially helpful in higher dimensions.

  ///////////////////////////////////////////////////////////////////////////
  // 1-D

  /**
   * Applies this filter. 
   * Uses lag1; ignores lag2 or lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a1 filter coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void apply(A1 a1, float[] x, float[] y) {
    float[] a = new float[_m];
    int n1 = x.length;
    int i1lo = min(_max1,n1);
    for (int i1=n1-1; i1>=i1lo; --i1) {
      a1.get(i1,a);
      float yi = a[0]*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        yi += a[j]*x[k1];
      }
      y[i1] = yi;
    }
    for (int i1=i1lo-1; i1>=0; --i1) {
      a1.get(i1,a);
      float yi = a[0]*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          yi += a[j]*x[k1];
      }
      y[i1] = yi;
    }
  }

  /**
   * Applies the transpose of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a1 filter coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void applyTranspose(A1 a1, float[] x, float[] y) {
    float[] a = new float[_m];
    int n1 = x.length;
    int i1lo = min(_max1,n1);
    for (int i1=0; i1<i1lo; ++i1) {
      a1.get(i1,a);
      float xi = x[i1];
      y[i1] = a[0]*xi;
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          y[k1] += a[j]*xi;
      }
    }
    for (int i1=i1lo; i1<n1; ++i1) {
      a1.get(i1,a);
      float xi = x[i1];
      y[i1] = a[0]*xi;
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        y[k1] += a[j]*xi;
      }
    }
  }

  /**
   * Applies the inverse of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a1 filter coefficients.
   * @param y input array.
   * @param x output array.
   */
  public void applyInverse(A1 a1, float[] y, float[] x) {
    float[] a = new float[_m];
    int n1 = y.length;
    int i1lo = min(_max1,n1);
    for (int i1=0; i1<i1lo; ++i1) {
      a1.get(i1,a);
      float xi = 0.0f;
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          xi += a[j]*x[k1];
      }
      x[i1] = (y[i1]-xi)/a[0];
    }
    for (int i1=i1lo; i1<n1; ++i1) {
      a1.get(i1,a);
      float xi = 0.0f;
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        xi += a[j]*x[k1];
      }
      x[i1] = (y[i1]-xi)/a[0];
    }
  }

  /**
   * Applies the inverse transpose of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * <p>
   * <em>The inverse transpose cannot be applied in-place; 
   * input and output arrays cannot be the same array.</em>
   * @param a1 filter coefficients.
   * @param y input array.
   * @param x output array.
   */
  public void applyInverseTranspose(A1 a1, float[] y, float[] x) {
    Check.argument(x!=y,"x!=y");
    zero(x);
    float[] a = new float[_m];
    int n1 = y.length;
    int i1lo = min(_max1,n1);
    for (int i1=n1-1; i1>=i1lo; --i1) {
      a1.get(i1,a);
      float xi = x[i1] = (y[i1]-x[i1])/a[0];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        x[k1] += a[j]*xi;
      }
    }
    for (int i1=i1lo-1; i1>=0; --i1) {
      a1.get(i1,a);
      float xi = x[i1] = (y[i1]-x[i1])/a[0];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          x[k1] += a[j]*xi;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // 2-D

  /**
   * Applies this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a2 filter coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void apply(A2 a2, float[][] x, float[][] y) {
    float[] a = new float[_m];
    int n1 = x[0].length;
    int n2 = x.length;
    int i1lo = max(0,_max1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = (i1lo<=i1hi)?min(_max2,n2):n2;
    for (int i2=n2-1; i2>=i2lo; --i2) {
      for (int i1=n1-1; i1>=i1hi; --i1) {
        a2.get(i1,i2,a);
        float yi = a[0]*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            yi += a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1hi-1; i1>=i1lo; --i1) {
        a2.get(i1,i2,a);
        float yi = a[0]*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          yi += a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1lo-1; i1>=0; --i1) {
        a2.get(i1,i2,a);
        float yi = a[0]*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            yi += a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
    for (int i2=i2lo-1; i2>=0; --i2) {
      for (int i1=n1-1; i1>=0; --i1) {
        a2.get(i1,i2,a);
        float yi = a[0]*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1 && k1<n1 && 0<=k2)
            yi += a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
  }

  /**
   * Applies the transpose of this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a2 filter coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void applyTranspose(A2 a2, float[][] x, float[][] y) {
    float[] a = new float[_m];
    int n1 = x[0].length;
    int n2 = x.length;
    int i1lo = max(0,_max1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = (i1lo<=i1hi)?min(_max2,n2):n2;
    for (int i2=0; i2<i2lo; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        a2.get(i1,i2,a);
        float xi = x[i2][i1];
        y[i2][i1] = a[0]*xi;
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1 && k1<n1 && 0<=k2)
            y[k2][k1] += a[j]*xi;
        }
      }
    }
    for (int i2=i2lo; i2<n2; ++i2) {
      for (int i1=0; i1<i1lo; ++i1) {
        a2.get(i1,i2,a);
        float xi = x[i2][i1];
        y[i2][i1] = a[0]*xi;
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            y[k2][k1] += a[j]*xi;
        }
      }
      for (int i1=i1lo; i1<i1hi; ++i1) {
        a2.get(i1,i2,a);
        float xi = x[i2][i1];
        y[i2][i1] = a[0]*xi;
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          y[k2][k1] += a[j]*xi;
        }
      }
      for (int i1=i1hi; i1<n1; ++i1) {
        a2.get(i1,i2,a);
        float xi = x[i2][i1];
        y[i2][i1] = a[0]*xi;
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            y[k2][k1] += a[j]*xi;
        }
      }
    }
  }

  /**
   * Applies the inverse of this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a2 filter coefficients.
   * @param y input array.
   * @param x output array.
   */
  public void applyInverse(A2 a2, float[][] y, float[][] x) {
    float[] a = new float[_m];
    int n1 = y[0].length;
    int n2 = y.length;
    int i1lo = min(_max1,n1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = (i1lo<=i1hi)?min(_max2,n2):n2;
    for (int i2=0; i2<i2lo; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        a2.get(i1,i2,a);
        float xi = 0.0f;
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1 && k1<n1 && 0<=k2)
            xi += a[j]*x[k2][k1];
        }
        x[i2][i1] = (y[i2][i1]-xi)/a[0];
      }
    }
    for (int i2=i2lo; i2<n2; ++i2) {
      for (int i1=0; i1<i1lo; ++i1) {
        a2.get(i1,i2,a);
        float xi = 0.0f;
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            xi += a[j]*x[k2][k1];
        }
        x[i2][i1] = (y[i2][i1]-xi)/a[0];
      }
      for (int i1=i1lo; i1<i1hi; ++i1) {
        a2.get(i1,i2,a);
        float xi = 0.0f;
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          xi += a[j]*x[k2][k1];
        }
        x[i2][i1] = (y[i2][i1]-xi)/a[0];
      }
      for (int i1=i1hi; i1<n1; ++i1) {
        a2.get(i1,i2,a);
        float xi = 0.0f;
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            xi += a[j]*x[k2][k1];
        }
        x[i2][i1] = (y[i2][i1]-xi)/a[0];
      }
    }
  }

  /**
   * Applies the inverse transpose of this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
   * <p>
   * <em>The inverse transpose cannot be applied in-place; 
   * input and output arrays cannot be the same array.</em>
   * @param a2 filter coefficients.
   * @param y input array.
   * @param x output array.
   */
  public void applyInverseTranspose(A2 a2, float[][] y, float[][] x) {
    Check.argument(x!=y,"x!=y");
    zero(x);
    float[] a = new float[_m];
    int n1 = y[0].length;
    int n2 = y.length;
    int i1lo = min(_max1,n1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = (i1lo<=i1hi)?min(_max2,n2):n2;
    for (int i2=n2-1; i2>=i2lo; --i2) {
      for (int i1=n1-1; i1>=i1hi; --i1) {
        a2.get(i1,i2,a);
        float xi = x[i2][i1] = (y[i2][i1]-x[i2][i1])/a[0];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            x[k2][k1] += a[j]*xi;
        }
      }
      for (int i1=i1hi-1; i1>=i1lo; --i1) {
        a2.get(i1,i2,a);
        float xi = x[i2][i1] = (y[i2][i1]-x[i2][i1])/a[0];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          x[k2][k1] += a[j]*xi;
        }
      }
      for (int i1=i1lo-1; i1>=0; --i1) {
        a2.get(i1,i2,a);
        float xi = x[i2][i1] = (y[i2][i1]-x[i2][i1])/a[0];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            x[k2][k1] += a[j]*xi;
        }
      }
    }
    for (int i2=i2lo-1; i2>=0; --i2) {
      for (int i1=n1-1; i1>=0; --i1) {
        a2.get(i1,i2,a);
        float xi = x[i2][i1] = (y[i2][i1]-x[i2][i1])/a[0];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1 && k1<n1 && 0<=k2)
            x[k2][k1] += a[j]*xi;
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // 3-D

  /**
   * Applies this filter. 
   * Uses lag1, lag2, and lag3.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a3 filter coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void apply(A3 a3, float[][][] x, float[][][] y) {
    float[] a = new float[_m];
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    int i1lo = max(0,_max1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = max(0,_max2);
    int i2hi = min(n2,n2+_min2);
    int i3lo = (i1lo<=i1hi && i2lo<=i2hi)?min(_max3,n3):n3;
    for (int i3=n3-1; i3>=i3lo; --i3) {
      for (int i2=n2-1; i2>=i2hi; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          a3.get(i1,i2,i3,a);
          float yi = a[0]*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && k2<n2)
              yi += a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
      for (int i2=i2hi-1; i2>=i2lo; --i2) {
        for (int i1=n1-1; i1>=i1hi; --i1) {
          a3.get(i1,i2,i3,a);
          float yi = a[0]*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (k1<n1)
              yi += a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1hi-1; i1>=i1lo; --i1) {
          a3.get(i1,i2,i3,a);
          float yi = a[0]*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            yi += a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1lo-1; i1>=0; --i1) {
          a3.get(i1,i2,i3,a);
          float yi = a[0]*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1)
              yi += a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
      for (int i2=i2lo-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          a3.get(i1,i2,i3,a);
          float yi = a[0]*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2)
              yi += a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
    }
    for (int i3=i3lo-1; i3>=0; --i3) {
      for (int i2=n2-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          a3.get(i1,i2,i3,a);
          float yi = a[0]*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 && k2<n2 && 0<=k3)
              yi += a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
    }
  }

  /**
   * Applies the transpose of this filter. 
   * Uses lag1, lag2, and lag3.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a3 filter coefficients.
   * @param x input array.
   * @param y output array.
   */
  public void applyTranspose(A3 a3, float[][][] x, float[][][] y) {
    float[] a = new float[_m];
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    int i1lo = max(0,_max1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = max(0,_max2);
    int i2hi = min(n2,n2+_min2);
    int i3lo = (i1lo<=i1hi && i2lo<=i2hi)?min(_max3,n3):n3;
    for (int i3=0; i3<i3lo; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1];
          y[i3][i2][i1] = a[0]*xi;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 && k2<n2 && 0<=k3)
              y[k3][k2][k1] += a[j]*xi;
          }
        }
      }
    }
    for (int i3=i3lo; i3<n3; ++i3) {
      for (int i2=0; i2<i2lo; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1];
          y[i3][i2][i1] = a[0]*xi;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2)
              y[k3][k2][k1] += a[j]*xi;
          }
        }
      }
      for (int i2=i2lo; i2<i2hi; ++i2) {
        for (int i1=0; i1<i1lo; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1];
          y[i3][i2][i1] = a[0]*xi;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1)
              y[k3][k2][k1] += a[j]*xi;
          }
        }
        for (int i1=i1lo; i1<i1hi; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1];
          y[i3][i2][i1] = a[0]*xi;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            y[k3][k2][k1] += a[j]*xi;
          }
        }
        for (int i1=i1hi; i1<n1; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1];
          y[i3][i2][i1] = a[0]*xi;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (k1<n1)
              y[k3][k2][k1] += a[j]*xi;
          }
        }
      }
      for (int i2=i2hi; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1];
          y[i3][i2][i1] = a[0]*xi;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && k2<n2)
              y[k3][k2][k1] += a[j]*xi;
          }
        }
      }
    }
  }

  /**
   * Applies the inverse of this filter. 
   * Uses lag1, lag2, and lag3.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a3 filter coefficients.
   * @param y output array.
   * @param x input array.
   */
  public void applyInverse(A3 a3, float[][][] y, float[][][] x) {
    float[] a = new float[_m];
    int n1 = y[0][0].length;
    int n2 = y[0].length;
    int n3 = y.length;
    int i1lo = max(0,_max1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = max(0,_max2);
    int i2hi = min(n2,n2+_min2);
    int i3lo = (i1lo<=i1hi && i2lo<=i2hi)?min(_max3,n3):n3;
    for (int i3=0; i3<i3lo; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = 0.0f;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 && k2<n2 && 0<=k3)
              xi += a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = (y[i3][i2][i1]-xi)/a[0];
        }
      }
    }
    for (int i3=i3lo; i3<n3; ++i3) {
      for (int i2=0; i2<i2lo; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = 0.0f;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2)
              xi += a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = (y[i3][i2][i1]-xi)/a[0];
        }
      }
      for (int i2=i2lo; i2<i2hi; ++i2) {
        for (int i1=0; i1<i1lo; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = 0.0f;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1)
              xi += a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = (y[i3][i2][i1]-xi)/a[0];
        }
        for (int i1=i1lo; i1<i1hi; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = 0.0f;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            xi += a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = (y[i3][i2][i1]-xi)/a[0];
        }
        for (int i1=i1hi; i1<n1; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = 0.0f;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (k1<n1)
              xi += a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = (y[i3][i2][i1]-xi)/a[0];
        }
      }
      for (int i2=i2hi; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          a3.get(i1,i2,i3,a);
          float xi = 0.0f;
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && k2<n2)
              xi += a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = (y[i3][i2][i1]-xi)/a[0];
        }
      }
    }
  }

  /**
   * Applies the inverse transpose of this filter. 
   * Uses lag1, lag2, and lag3.
   * <p>
   * <em>The inverse transpose cannot be applied in-place; 
   * input and output arrays cannot be the same array.</em>
   * @param a3 filter coefficients.
   * @param y output array.
   * @param x input array.
   */
  public void applyInverseTranspose(A3 a3, float[][][] y, float[][][] x) {
    Check.argument(x!=y,"x!=y");
    zero(x);
    float[] a = new float[_m];
    int n1 = y[0][0].length;
    int n2 = y[0].length;
    int n3 = y.length;
    int i1lo = max(0,_max1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = max(0,_max2);
    int i2hi = min(n2,n2+_min2);
    int i3lo = (i1lo<=i1hi && i2lo<=i2hi)?min(_max3,n3):n3;
    for (int i3=n3-1; i3>=i3lo; --i3) {
      for (int i2=n2-1; i2>=i2hi; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1] = (y[i3][i2][i1]-x[i3][i2][i1])/a[0];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && k2<n2)
              x[k3][k2][k1] += a[j]*xi;
          }
        }
      }
      for (int i2=i2hi-1; i2>=i2lo; --i2) {
        for (int i1=n1-1; i1>=i1hi; --i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1] = (y[i3][i2][i1]-x[i3][i2][i1])/a[0];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (k1<n1)
              x[k3][k2][k1] += a[j]*xi;
          }
        }
        for (int i1=i1hi-1; i1>=i1lo; --i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1] = (y[i3][i2][i1]-x[i3][i2][i1])/a[0];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            x[k3][k2][k1] += a[j]*xi;
          }
        }
        for (int i1=i1lo-1; i1>=0; --i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1] = (y[i3][i2][i1]-x[i3][i2][i1])/a[0];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1)
              x[k3][k2][k1] += a[j]*xi;
          }
        }
      }
      for (int i2=i2lo-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1] = (y[i3][i2][i1]-x[i3][i2][i1])/a[0];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2)
              x[k3][k2][k1] += a[j]*xi;
          }
        }
      }
    }
    for (int i3=i3lo-1; i3>=0; --i3) {
      for (int i2=n2-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          a3.get(i1,i2,i3,a);
          float xi = x[i3][i2][i1] = (y[i3][i2][i1]-x[i3][i2][i1])/a[0];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 && k2<n2 && 0<=k3)
              x[k3][k2][k1] += a[j]*xi;
          }
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _m; // number of lags and filter coefficients
  private int _min1,_max1; // min/max lags in 1st dimension
  private int _min2,_max2; // min/max lags in 2nd dimension
  private int       _max3; //     max lag  in 3rd dimension
  private int[] _lag1; // lags in 1st dimension
  private int[] _lag2; // lags in 2nd dimension
  private int[] _lag3; // lags in 3rd dimension

  private void initLags(int[] lag1) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    for (int j=1; j<lag1.length; ++j)
      Check.argument(lag1[j]>0,"lag1["+j+"]>0");
    _m = lag1.length;
    _lag1 = copy(lag1);
    _lag2 = zeroint(_m);
    _lag3 = zeroint(_m);
    _min1 = min(lag1);
    _max1 = max(lag1);
  }

  private void initLags(int[] lag1, int[] lag2) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    Check.argument(lag2[0]==0,"lag2[0]==0");
    for (int j=1; j<lag1.length; ++j) {
      Check.argument(lag2[j]>=0,"lag2["+j+"]>=0");
      if (lag2[j]==0)
        Check.argument(lag1[j]>0,"if lag2==0, lag1["+j+"]>0");
    }
    _m = lag1.length;
    _lag1 = copy(lag1);
    _lag2 = copy(lag2);
    _lag3 = zeroint(_m);
    _min1 = min(lag1);
    _min2 = min(lag2);
    _max1 = max(lag1);
    _max2 = max(lag2);
  }

  private void initLags(int[] lag1, int[] lag2, int[] lag3) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    Check.argument(lag2[0]==0,"lag2[0]==0");
    Check.argument(lag3[0]==0,"lag3[0]==0");
    for (int j=1; j<lag1.length; ++j) {
      Check.argument(lag3[j]>=0,"lag3["+j+"]>=0");
      if (lag3[j]==0) {
        Check.argument(lag2[j]>=0,"if lag3==0, lag2["+j+"]>=0");
        if (lag2[j]==0)
          Check.argument(lag1[j]>0,"if lag3==0 && lag2==0, lag1["+j+"]>0");
      }
    }
    _m = lag1.length;
    _lag1 = copy(lag1);
    _lag2 = copy(lag2);
    _lag3 = copy(lag3);
    _min1 = min(lag1);
    _min2 = min(lag2);
    _max1 = max(lag1);
    _max2 = max(lag2);
    _max3 = max(lag3);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Testing interface for linear interpolation of table-driven filters.
  // Not working, so keep private for now.

  /*
  /**
   * Interface for filter coefficients indexed in 1 dimension.
   * Filter coefficients may vary with sample index, and will be got 
   * through this interface for every output sample computed.
   * /
  private interface A12 {
    
    /**
     * Gets scale factors and filter coefficients for specified sample.
     * @param i1 sample index in 1st dimension.
     * @param s array[2] with two scale factors.
     * @param a array[2][] with two arrays of filter coefficients.
     * /
    public void get(int i1, float[] s, float[][] a);
  }

  /**
   * Applies the inverse of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a12 scale factors and filter coefficients.
   * @param y input array.
   * @param x output array.
   * /
  private void applyInverse(A12 a12, float[] y, float[] x) {
    int n1 = y.length;
    int mod1 = 1+_max1;
    float[] t0 = new float[mod1];
    float[] t1 = new float[mod1];
    float[] s = new float[2];
    float[][] a = new float[2][];
    for (int i1=0,i1m=0; i1<n1; ++i1,i1m=i1%mod1) {
      a12.get(i1,s,a);
      float s0 = s[0];
      float s1 = s[1];
      float[] a0 = a[0];
      float[] a1 = a[1];
      float x0 = 0.0f;
      float x1 = 0.0f;
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1) {
          int k1m = k1%mod1;
          x0 += a0[j]*t0[k1m];
          x1 += a1[j]*t1[k1m];
        }
      }
      t0[i1m] = (y[i1]-x0)/a0[0]; 
      t1[i1m] = (y[i1]-x1)/a1[0]; 
      x[i1] = s0*t0[i1m]+s1*t1[i1m];
    }
  }

  /**
   * Applies the inverse-transpose of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param a12 scale factors and filter coefficients.
   * @param y input array.
   * @param x output array.
   * /
  private void applyInverseTranspose(A12 a12, float[] y, float[] x) {
    int n1 = y.length;
    int mod1 = 1+_max1;
    float[] t0 = new float[mod1];
    float[] t1 = new float[mod1];
    float[] s = new float[2];
    float[][] a = new float[2][];
    for (int i1=n1-1,i1m=i1%mod1; i1>=0; --i1,i1m=i1%mod1) {
      a12.get(i1,s,a);
      float s0 = s[0];
      float s1 = s[1];
      float[] a0 = a[0];
      float[] a1 = a[1];
      float x0 = (s0*y[i1]-t0[i1m])/a0[0];
      float x1 = (s1*y[i1]-t1[i1m])/a1[0];
      x[i1] = x0+x1;
      t0[i1m] = 0.0f;
      t1[i1m] = 0.0f;
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1) {
          int k1m = k1%mod1;
          t0[k1m] += a0[j]*x0;
          t1[k1m] += a1[j]*x1;
        }
      }
    }
  }
  */
}
