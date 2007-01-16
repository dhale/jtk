/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.MathPlus.*;

import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Check;

/**
 * A multi-dimensional causal filter with locally variable coefficients.
 * The output samples of a causal filter depend only on present and past 
 * input samples. In two dimensions, causal filters are also called 
 * non-symmetric half-plane (NSHP) filters, and this concept can be
 * extended to higher dimensions.
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
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.15
 */
public class LocalCausalFilter {

  /**
   * Interface for getting coefficients of 1-D filters.
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
   * Interface for getting coefficients of 2-D filters.
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

  ///////////////////////////////////////////////////////////////////////////
  // 1-D

  /**
   * Applies this filter. 
   * Uses lag1; ignores lag2 or lag3, if specified.
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
   * @param y input array.
   * @param x output array.
   */
  public void applyInverseTranspose(A1 a1, float[] y, float[] x) {
    Array.zero(x);
    float[] a = new float[_m];
    int n1 = y.length;
    int i1lo = min(_max1,n1);
    for (int i1=n1-1; i1>=i1lo; --i1) {
      a1.get(i1,a);
      x[i1] = (y[i1]-x[i1])/a[0];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        x[k1] += a[j]*x[i1];
      }
    }
    for (int i1=i1lo-1; i1>=0; --i1) {
      a1.get(i1,a);
      x[i1] = (y[i1]-x[i1])/a[0];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          x[k1] += a[j]*x[i1];
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // 2-D

  /**
   * Applies this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
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
      for (int i1=n1-1; i1>i1hi; --i1) {
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
    //// TODO:
    /*
    for (int i2=n2-1; i2>=i2lo; --i2) {
      for (int i1=n1-1; i1>i1hi; --i1) {
        a2.get(i1,i2,a);
        float xi = x[i2][i1];
        float y[i2][i1] = a[0]*xi;
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            y[k2][k1] += a[j]*xi;
        }
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
    */
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _m;
  private int _min1,_max1;
  private int _min2,_max2;
  private int _min3,_max3;
  private int[] _lag1;
  private int[] _lag2;
  private int[] _lag3;

  private void initLags(int[] lag1) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    for (int j=1; j<lag1.length; ++j)
      Check.argument(lag1[j]>0,"lag1["+j+"]>0");
    _m = lag1.length;
    _lag1 = Array.copy(lag1);
    _lag2 = Array.zeroint(_m);
    _lag3 = Array.zeroint(_m);
    _min1 = Array.min(lag1);
    _max1 = Array.max(lag1);
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
    _lag1 = Array.copy(lag1);
    _lag2 = Array.copy(lag2);
    _lag3 = Array.zeroint(_m);
    _min1 = Array.min(lag1);
    _min2 = Array.min(lag2);
    _max1 = Array.max(lag1);
    _max2 = Array.max(lag2);
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
    _lag1 = Array.copy(lag1);
    _lag2 = Array.copy(lag2);
    _lag3 = Array.copy(lag3);
    _min1 = Array.min(lag1);
    _min2 = Array.min(lag2);
    _min3 = Array.min(lag3);
    _max1 = Array.max(lag1);
    _max2 = Array.max(lag2);
    _max3 = Array.max(lag3);
  }
}
