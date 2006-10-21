/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.MathPlus.*;

import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Check;

/**
 * A minimum-phase filter is a causal stable filter with a causal stable 
 * inverse. The filter and its inverse also have corresponding transposes
 * which are like the filter and inverse applied in the reverse direction.
 * <p>
 * Minimum-phase filters are generalized to multi-dimensional arrays as in
 * Claerbout, J., 1998, Multidimensional recursive filters via a helix: 
 * Geophysics, v. 63, n. 5, p. 1532-1541.
 * <p>
 * Filter constructors do not ensure that specified lags and coefficients 
 * correspond to minimum-phase filters. If not minimum-phase, then the 
 * causal inverse and inverse-transpose filters are unstable.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.10.10
 */
public class MinimumPhaseFilter {

  /**
   * Constructs a minimum-phase filter for specified lag1.
   * By default, all lag2 and lag3 are assumed to be zero.
   * <p>
   * For j=0 only, lag1[j] is zero.
   * All lag1[j] must be non-negative.
   * @param lag1 array of lags.
   * @param a array of filter coefficients for each lag.
   */
  public MinimumPhaseFilter(int[] lag1, float[] a) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1.length==a.length,"lag1.length==a.length");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    Check.argument(a[0]!=0.0f,"a[0]!=0");
    for (int j=1; j<a.length; ++j)
      Check.argument(lag1[j]>0,"lag1["+j+"]>0");
    _m = lag1.length;
    _lag1 = Array.copy(lag1);
    _lag2 = Array.zeroint(_m);
    _lag3 = Array.zeroint(_m);
    _min1 = Array.min(lag1);
    _max1 = Array.max(lag1);
    _a = Array.copy(a);
    _a0 = a[0];
    _a0i = 1.0f/a[0];
  }

  /**
   * Constructs a minimum-phase filter for specified lag1 and lag2.
   * By default, all lag3 are assumed to be zero.
   * <p>
   * For j=0 only, lag1[j] and lag2[j] are zero.
   * All lag2[j] must be non-negative.
   * If lag2[j] is zero, then lag1[j] must be non-negative.
   * @param lag1 array of lags in 1st dimension.
   * @param lag2 array of lags in 2nd dimension.
   * @param a array of filter coefficients for each lag.
   */
  public MinimumPhaseFilter(int[] lag1, int[] lag2, float[] a) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1.length==a.length,"lag1.length==a.length");
    Check.argument(lag2.length==a.length,"lag2.length==a.length");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    Check.argument(lag2[0]==0,"lag2[0]==0");
    Check.argument(a[0]!=0.0f,"a[0]!=0");
    for (int j=1; j<a.length; ++j) {
      Check.argument(lag2[j]>=0,"lag2["+j+"]>=0");
      if (lag2[j]==0)
        Check.argument(lag1[j]>0,"if lag2==0, lag1["+j+"]>0");
    }
    _m = a.length;
    _lag1 = Array.copy(lag1);
    _lag2 = Array.copy(lag2);
    _lag3 = Array.zeroint(_m);
    _min1 = Array.min(lag1);
    _min2 = Array.min(lag2);
    _max1 = Array.max(lag1);
    _max2 = Array.max(lag2);
    _a = Array.copy(a);
    _a0 = a[0];
    _a0i = 1.0f/a[0];
  }

  /**
   * Constructs a minimum-phase filter for specified lag1, lag2, and lag3.
   * <p>
   * For j=0 only, lag1[j] and lag2[j] and lag3[j] are zero.
   * All lag3[j] must be non-negative.
   * If lag3[j] is zero, then lag2[j] must be non-negative.
   * If lag3[j] and lag2[j] are zero, then lag1[j] must be non-negative.
   * @param lag1 array of lags in 1st dimension.
   * @param lag2 array of lags in 2nd dimension.
   * @param lag3 array of lags in 3rd dimension.
   * @param a array of filter coefficients for each lag.
   */
  public MinimumPhaseFilter(int[] lag1, int[] lag2, int[] lag3, float[] a) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1.length==a.length,"lag1.length==a.length");
    Check.argument(lag2.length==a.length,"lag2.length==a.length");
    Check.argument(lag3.length==a.length,"lag3.length==a.length");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    Check.argument(lag2[0]==0,"lag2[0]==0");
    Check.argument(lag3[0]==0,"lag3[0]==0");
    Check.argument(a[0]!=0.0f,"a[0]!=0");
    for (int j=1; j<a.length; ++j) {
      Check.argument(lag3[j]>=0,"lag3["+j+"]>=0");
      if (lag3[j]==0) {
        Check.argument(lag2[j]>=0,"if lag3==0, lag2["+j+"]>=0");
        if (lag2[j]==0)
          Check.argument(lag1[j]>0,"if lag3==0 && lag2==0, lag1["+j+"]>0");
      }
    }
    _m = a.length;
    _lag1 = Array.copy(lag1);
    _lag2 = Array.copy(lag2);
    _lag3 = Array.copy(lag3);
    _min1 = Array.min(lag1);
    _min2 = Array.min(lag2);
    _min3 = Array.min(lag3);
    _max1 = Array.max(lag1);
    _max2 = Array.max(lag2);
    _max3 = Array.max(lag3);
    _a = Array.copy(a);
    _a0 = a[0];
    _a0i = 1.0f/a[0];
  }

  /**
   * Applies this filter. 
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[] x, float[] y) {
    int n1 = y.length;
    int i1lo = min(_max1,n1);
    for (int i1=0; i1<i1lo; ++i1) {
      float yi = _a0*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
    for (int i1=i1lo; i1<n1; ++i1) {
      float yi = _a0*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
  }

  /**
   * Applies this filter. 
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][] x, float[][] y) {
    int n1 = y[0].length;
    int n2 = y.length;
    int i1lo = max(0,_max1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = (i1lo<=i1hi)?min(_max2,n2):n2;
    for (int i2=0; i2<i2lo; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1 && k1<n1 && 0<=k2)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
    for (int i2=i2lo; i2<n2; ++i2) {
      for (int i1=0; i1<i1lo; ++i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1lo; i1<i1hi; ++i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1hi; i1<n1; ++i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
  }

  /**
   * Applies this filter. 
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][][] x, float[][][] y) {
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
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 &&  k2<n2 && 0<=k3)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
    }
    for (int i3=i3lo; i3<n3; ++i3) {
      for (int i2=0; i2<i2lo; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k2 && 0<=k1 && k1<n1)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
      for (int i2=i2lo; i2<i2hi; ++i2) {
        for (int i1=0; i1<i1lo; ++i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1lo; i1<i1hi; ++i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1hi; i1<n1; ++i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (k1<n1)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
      for (int i2=i2hi; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (k2<n2 && 0<=k1 && k1<n1)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
    }
  }

  /**
   * Applies the transpose of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * @param x input array.
   * @param y output array.
   */
  public void applyTranspose(float[] x, float[] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    int n1 = y.length;
    int i1hi = max(n1-_max1,0);
    for (int i1=n1-1; i1>=i1hi; --i1) {
      float yi = _a0*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        if (k1<n1)
          yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
    for (int i1=i1hi-1; i1>=0; --i1) {
      float yi = _a0*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
  }

  /**
   * Applies the transpose of this filter.
   * Uses lag1 and lag2; ignores lag3, if specified.
   * @param x input array.
   * @param y output array.
   */
  public void applyTranspose(float[][] x, float[][] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    Check.state(_lag2!=null,"lag2 has been specified");
    int n1 = y[0].length;
    int n2 = y.length;
    int i1lo = max(0,-_min1);
    int i1hi = min(n1,n1-_max1);
    int i2hi = (i1lo<=i1hi)?max(n2-_max2,0):0;
    for (int i2=n2-1; i2>=i2hi; --i2) {
      for (int i1=n1-1; i1>=0; --i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (0<=k1 && k1<n1 && k2<n2)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
    for (int i2=i2hi-1; i2>=0; --i2) {
      for (int i1=n1-1; i1>=i1hi; --i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (k1<n1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1hi-1; i1>=i1lo; --i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1lo-1; i1>=0; --i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (0<=k1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
  }

  /**
   * Applies the transpose of this filter.
   * Uses lag1, lag2, and lag3.
   * @param x input array.
   * @param y output array.
   */
  public void applyTranspose(float[][][] x, float[][][] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    Check.state(_lag2!=null,"lag2 has been specified");
    Check.state(_lag3!=null,"lag3 has been specified");
    int n1 = y[0][0].length;
    int n2 = y[0].length;
    int n3 = y.length;
    int i1lo = max(0,-_min1);
    int i1hi = min(n1,n1-_max1);
    int i2lo = max(0,-_min2);
    int i2hi = min(n2,n2-_max2);
    int i3hi = (i1lo<=i1hi && i2lo<=i2hi)?max(n3-_max3,0):0;
    for (int i3=n3-1; i3>=i3hi; --i3) {
      for (int i2=n2-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 && k2<n2 && k3<n3)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
    }
    for (int i3=i3hi-1; i3>=0; --i3) {
      for (int i2=n2-1; i2>=i2hi; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (k2<n2 && 0<=k1 && k1<n1)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
      for (int i2=i2hi-1; i2>=i2lo; --i2) {
        for (int i1=n1-1; i1>=i1hi; --i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (k1<n1)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1hi-1; i1>=i1lo; --i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1lo-1; i1>=0; --i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
      for (int i2=i2lo-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k2 && 0<=k1 && k1<n1)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
    }
  }

  /**
   * Applies the inverse of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * @param x input array.
   * @param y output array.
   */
  public void applyInverse(float[] x, float[] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    int n1 = y.length;
    int i1lo = min(_max1,n1);
    for (int i1=0; i1<i1lo; ++i1) {
      float yi = x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          yi -= _a[j]*y[k1];
      }
      y[i1] = _a0i*yi;
    }
    for (int i1=i1lo; i1<n1; ++i1) {
      float yi = x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        yi -= _a[j]*y[k1];
      }
      y[i1] = _a0i*yi;
    }
  }

  /**
   * Applies the inverse of this filter.
   * Uses lag1 and lag2; ignores lag3, if specified.
   * @param x input array.
   * @param y output array.
   */
  public void applyInverse(float[][] x, float[][] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    Check.state(_lag2!=null,"lag2 has been specified");
    int n1 = y[0].length;
    int n2 = y.length;
    int i1lo = max(0,_max1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = (i1lo<=i1hi)?min(_max2,n2):n2;
    for (int i2=0; i2<i2lo; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        float yi = x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1 && k1<n1 && 0<=k2)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = _a0i*yi;
      }
    }
    for (int i2=i2lo; i2<n2; ++i2) {
      for (int i1=0; i1<i1lo; ++i1) {
        float yi = x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = _a0i*yi;
      }
      for (int i1=i1lo; i1<i1hi; ++i1) {
        float yi = x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = _a0i*yi;
      }
      for (int i1=i1hi; i1<n1; ++i1) {
        float yi = x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = _a0i*yi;
      }
    }
  }

  /**
   * Applies the inverse of this filter. 
   * Uses lag1, lag2, and lag3.
   * @param x input array.
   * @param y output array.
   */
  public void applyInverse(float[][][] x, float[][][] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    Check.state(_lag2!=null,"lag2 has been specified");
    Check.state(_lag3!=null,"lag3 has been specified");
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
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 &&  k2<n2 && 0<=k3)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
      }
    }
    for (int i3=i3lo; i3<n3; ++i3) {
      for (int i2=0; i2<i2lo; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k2 && 0<=k1 && k1<n1)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
      }
      for (int i2=i2lo; i2<i2hi; ++i2) {
        for (int i1=0; i1<i1lo; ++i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
        for (int i1=i1lo; i1<i1hi; ++i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
        for (int i1=i1hi; i1<n1; ++i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (k1<n1)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
      }
      for (int i2=i2hi; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (k2<n2 && 0<=k1 && k1<n1)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
      }
    }
  }

  /**
   * Applies the inverse transpose of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * @param x input array.
   * @param y output array.
   */
  public void applyInverseTranspose(float[] x, float[] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    int n1 = y.length;
    int i1hi = max(n1-_max1,0);
    for (int i1=n1-1; i1>=i1hi; --i1) {
      float yi = x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        if (k1<n1)
          yi -= _a[j]*y[k1];
      }
      y[i1] = _a0i*yi;
    }
    for (int i1=i1hi-1; i1>=0; --i1) {
      float yi = x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        yi -= _a[j]*y[k1];
      }
      y[i1] = _a0i*yi;
    }
  }

  /**
   * Applies the inverse transpose of this filter.
   * Uses lag1 and lag2; ignores lag3, if specified.
   * @param x input array.
   * @param y output array.
   */
  public void applyInverseTranspose(float[][] x, float[][] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    Check.state(_lag2!=null,"lag2 has been specified");
    int n1 = y[0].length;
    int n2 = y.length;
    int i1lo = max(0,-_min1);
    int i1hi = min(n1,n1-_max1);
    int i2hi = (i1lo<=i1hi)?max(n2-_max2,0):0;
    for (int i2=n2-1; i2>=i2hi; --i2) {
      for (int i1=n1-1; i1>=0; --i1) {
        float yi = x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (0<=k1 && k1<n1 && k2<n2)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = _a0i*yi;
      }
    }
    for (int i2=i2hi-1; i2>=0; --i2) {
      for (int i1=n1-1; i1>=i1hi; --i1) {
        float yi = x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (k1<n1)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = _a0i*yi;
      }
      for (int i1=i1hi-1; i1>=i1lo; --i1) {
        float yi = x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = _a0i*yi;
      }
      for (int i1=i1lo-1; i1>=0; --i1) {
        float yi = x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (0<=k1)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = _a0i*yi;
      }
    }
  }

  /**
   * Applies the inverse transpose of this filter.
   * Uses lag1, lag2, and lag3.
   * @param x input array.
   * @param y output array.
   */
  public void applyInverseTranspose(float[][][] x, float[][][] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    Check.state(_lag2!=null,"lag2 has been specified");
    Check.state(_lag3!=null,"lag3 has been specified");
    int n1 = y[0][0].length;
    int n2 = y[0].length;
    int n3 = y.length;
    int i1lo = max(0,-_min1);
    int i1hi = min(n1,n1-_max1);
    int i2lo = max(0,-_min2);
    int i2hi = min(n2,n2-_max2);
    int i3hi = (i1lo<=i1hi && i2lo<=i2hi)?max(n3-_max3,0):0;
    for (int i3=n3-1; i3>=i3hi; --i3) {
      for (int i2=n2-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 && k2<n2 && k3<n3)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
      }
    }
    for (int i3=i3hi-1; i3>=0; --i3) {
      for (int i2=n2-1; i2>=i2hi; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (k2<n2 && 0<=k1 && k1<n1)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
      }
      for (int i2=i2hi-1; i2>=i2lo; --i2) {
        for (int i1=n1-1; i1>=i1hi; --i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (k1<n1)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
        for (int i1=i1hi-1; i1>=i1lo; --i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
        for (int i1=i1lo-1; i1>=0; --i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
      }
      for (int i2=i2lo-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float yi = x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k2 && 0<=k1 && k1<n1)
              yi -= _a[j]*y[k3][k2][k1];
          }
          y[i3][i2][i1] = _a0i*yi;
        }
      }
    }
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
  private float[] _a;
  private float _a0,_a0i;

  ///////////////////////////////////////////////////////////////////////////
  // Experimental Wilson-Burg factorization
  // A better interface is to begin with an already construct MPF, and then
  // provide a method to update that MPF for a specified auto-correlation.
  // Also, the convergence criterion needs work; iterations may not end.
  // The amount of zero-padding required in the work arrays s, t, and u
  // is unknown. In any case, the auto-correlation should not be centered
  // in these arrays, since we want to minimize end-effect errors in the
  // cascade of inverse and inverseTranspose filters.
  // For all these reasons, keep private for now.

  private static MinimumPhaseFilter factor(float[] r, int lag1[]) {
    int nlag = lag1.length;
    int min1 = Array.min(lag1);
    int max1 = Array.max(lag1);
    int m1 = max1-min1;
    int n1 = r.length+20*m1;
    int k1 = (n1-1)/2;
    int l1 = (r.length-1)/2;
    float[] s = new float[n1];
    float[] t = new float[n1];
    float[] u = new float[n1];
    Array.copy(r.length,0,r,k1-l1,s);
    float[] a = Array.zerofloat(nlag);
    a[0] = sqrt(s[k1]);
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,a);
    boolean converged = false;
    while (!converged) {
      //Array.dump(a);
      mpf.applyInverseTranspose(s,t);
      mpf.applyInverse(t,u);
      u[k1] += 1.0f;
      for (int i1=0; i1<k1; ++i1)
        u[i1] = 0.0f;
      u[k1] *= 0.5f;
      mpf.apply(u,t);
      int m = a.length;
      converged = true;
      for (int j=0; j<m; ++j) {
        int j1 = k1+lag1[j];
        if (0<=j1 && j1<n1 && a[j]!=t[j1]) {
          a[j] = t[j1];
          converged = false;
        }
      }
      mpf = new MinimumPhaseFilter(lag1,a);
    }
    return mpf;
  }

  private static MinimumPhaseFilter factor(
    float[][] r, int lag1[], int[] lag2) 
  {
    int nlag = lag1.length;
    int min1 = Array.min(lag1);
    int max1 = Array.max(lag1);
    int min2 = Array.min(lag2);
    int max2 = Array.max(lag2);
    int m1 = max1-min1;
    int m2 = max2-min2;
    int n1 = r[0].length+4*m1;
    int n2 = r.length+4*m2;
    int k1 = (n1-1)/2;
    int k2 = (n2-1)/2;
    int l1 = (r[0].length-1)/2;
    int l2 = (r.length-1)/2;
    float[][] s = new float[n2][n1];
    float[][] t = new float[n2][n1];
    float[][] u = new float[n2][n1];
    Array.copy(r[0].length,r.length,0,0,r,k1-l1,k2-l2,s);
    float[] a = Array.zerofloat(nlag);
    a[0] = sqrt(s[k2][k1]);
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,lag2,a);
    boolean converged = false;
    while (!converged) {
      Array.dump(a);
      mpf.applyInverseTranspose(s,t);
      mpf.applyInverse(t,u);
      u[k2][k1] += 1.0f;
      u[k2][k1] *= 0.5f;
      for (int i2=0; i2<k2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          u[i2][i1] = 0.0f;
      for (int i1=0; i1<k1; ++i1)
        u[k2][i1] = 0.0f;
      mpf.apply(u,t);
      int m = a.length;
      converged = true;
      for (int j=0; j<m; ++j) {
        int j1 = k1+lag1[j];
        int j2 = k2+lag2[j];
        if (0<=j1 && j1<n1 && 0<=j2 && j2<n2 && a[j]!=t[j2][j1]) {
          a[j] = t[j2][j1];
          converged = false;
        }
      }
      mpf = new MinimumPhaseFilter(lag1,lag2,a);
      Array.zero(t);
      t[k2][k1] = 1.0f;
      mpf.apply(t,u);
      mpf.applyTranspose(u,t);
      Array.dump(t);
    }
    return mpf;
  }

  private static MinimumPhaseFilter factor(
    float[][][] r, int lag1[], int[] lag2, int[] lag3) 
  {
    int nlag = lag1.length;
    int min1 = Array.min(lag1);
    int max1 = Array.max(lag1);
    int min2 = Array.min(lag2);
    int max2 = Array.max(lag2);
    int min3 = Array.min(lag3);
    int max3 = Array.max(lag3);
    int m1 = max1-min1;
    int m2 = max2-min2;
    int m3 = max3-min3;
    int n1 = r[0][0].length+2*m1;
    int n2 = r[0].length+2*m2;
    int n3 = r.length+2*m3;
    int k1 = (n1-1)/2;
    int k2 = (n2-1)/2;
    int k3 = (n3-1)/2;
    int l1 = (r[0][0].length-1)/2;
    int l2 = (r[0].length-1)/2;
    int l3 = (r.length-1)/2;
    float[][][] s = new float[n3][n2][n1];
    float[][][] t = new float[n3][n2][n1];
    float[][][] u = new float[n3][n2][n1];
    Array.copy(r[0][0].length,r[0].length,r.length,0,0,0,r,k1-l1,k2-l2,k3-l3,s);
    float[] a = Array.zerofloat(nlag);
    a[0] = sqrt(s[k3][k2][k1]);
    MinimumPhaseFilter mpf = new MinimumPhaseFilter(lag1,lag2,lag3,a);
    boolean converged = false;
    while (!converged) {
      Array.dump(a);
      mpf.applyInverseTranspose(s,t);
      mpf.applyInverse(t,u);
      u[k3][k2][k1] += 1.0f;
      u[k3][k2][k1] *= 0.5f;
      for (int i3=0; i3<k3; ++i3)
        for (int i2=0; i2<n2; ++i2)
          for (int i1=0; i1<n1; ++i1)
            u[i3][i2][i1] = 0.0f;
      for (int i2=0; i2<k2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          u[k3][i2][i1] = 0.0f;
      for (int i1=0; i1<k1; ++i1)
        u[k3][k2][i1] = 0.0f;
      mpf.apply(u,t);
      int m = a.length;
      converged = true;
      for (int j=0; j<m; ++j) {
        int j1 = k1+lag1[j];
        int j2 = k2+lag2[j];
        int j3 = k3+lag3[j];
        if (0<=j1 && j1<n1 && 
            0<=j2 && j2<n2 && 
            0<=j3 && j3<n3 &&
            a[j]!=t[j3][j2][j1]) {
          a[j] = t[j3][j2][j1];
          converged = false;
        }
      }
      mpf = new MinimumPhaseFilter(lag1,lag2,lag3,a);
      Array.zero(t);
      t[k3][k2][k1] = 1.0f;
      mpf.apply(t,u);
      mpf.applyTranspose(u,t);
      Array.dump(t);
    }
    return mpf;
  }
}
