/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A minimum-phase filter is a causal stable filter with a causal stable 
 * inverse. The filter and its inverse also have corresponding transposes
 * which are like the filter and inverse applied in the reverse direction.
 * <p>
 * Minimum-phase filters are generalized to multi-dimensional arrays via
 * Claerbout's (19xx) concept of filtering on a helix.
 * <p>
 * The filter coefficient for zero lag implicitly equals one; this 
 * coefficient must not be specified explicitly.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.10.10
 */
public class MinimumPhaseFilter {

  /**
   * Constructs a minimum-phase filter.
   * All lag1[j] must be positive.
   * @param lag1 array of lags.
   * @param a array of filter coefficients.
   */
  public MinimumPhaseFilter(int[] lag1, float[] a) {
    _m = a.length;
    _a = Array.copy(a);
    copyLags(lag1);
  }

  /**
   * Constructs a minimum-phase filter.
   * All lag2[j] must be non-negative.
   * For lag2[j]==0, all lag1[j] must be positive.
   * @param lag1 array of lags in 1st dimension.
   * @param lag2 array of lags in 2nd dimension.
   * @param a array of filter coefficients.
   */
  public MinimumPhaseFilter(int[] lag1, int[] lag2, float[] a) {
    _m = a.length;
    _a = Array.copy(a);
    copyLags(lag1);
    copyLags(lag2);
  }

  /**
   * Constructs a minimum-phase filter.
   * All lag3[j] must be non-negative.
   * For lag3[j]==0, all lag2[j] must be non-negative.
   * For lag3[j]==0 and lag2[j]==0, all lag1[j] must be positive.
   * @param lag1 array of lags in 1st dimension.
   * @param lag2 array of lags in 2nd dimension.
   * @param lag3 array of lags in 3rd dimension.
   * @param a array of filter coefficients.
   */
  public MinimumPhaseFilter(int[] lag1, int[] lag2, int[] lag3, float[] a) {
    _m = a.length;
    _a = Array.copy(a);
    copyLags(lag1);
    copyLags(lag2);
    copyLags(lag3);
  }

  /**
   * Applies this filter. 
   * Uses lag1; ignores lag2 or lag3, if specified.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[] x, float[] y) {
    Check.state(_lag1!=null,"lag1 has been specified");
    int n1 = y.length;
    int i1lo = min(_max1,n1);
    for (int i1=0; i1<i1lo; ++i1) {
      float yi = x[i1];
      for (int j=0; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
    for (int i1=i1lo; i1<n1; ++i1) {
      float yi = x[i1];
      for (int j=0; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
  }

  /**
   * Applies this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][] x, float[][] y) {
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
        for (int j=0; j<_m; ++j) {
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
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1lo; i1<i1hi; ++i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1hi; i1<n1; ++i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
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
   * Requires lag1, lag2, and lag3.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][][] x, float[][][] y) {
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
          for (int j=0; j<_m; ++j) {
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
          float yi = x[i3][i2][i1];
          for (int j=0; j<_m; ++j) {
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
          float yi = x[i3][i2][i1];
          for (int j=0; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1lo; i1<i1hi; ++i1) {
          float yi = x[i3][i2][i1];
          for (int j=0; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1hi; i1<n1; ++i1) {
          float yi = x[i3][i2][i1];
          for (int j=0; j<_m; ++j) {
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
          float yi = x[i3][i2][i1];
          for (int j=0; j<_m; ++j) {
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
      float yi = x[i1];
      for (int j=0; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        if (k1<n1)
          yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
    for (int i1=i1hi-1; i1>=0; --i1) {
      float yi = x[i1];
      for (int j=0; j<_m; ++j) {
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
    int i1lo = max(0,-_max1);
    int i1hi = min(n1,n1-_min1);
    int i2hi = (i1lo<=i1hi)?max(n2-_max2,0):0;
    for (int i2=n2-1; i2>=i2hi; --i2) {
      for (int i1=n1-1; i1>=0; --i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
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
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (k1<n1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1hi-1; i1>=i1lo; --i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1lo-1; i1>=0; --i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
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
      for (int j=0; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          yi -= _a[j]*y[k1];
      }
      y[i1] = yi;
    }
    for (int i1=i1lo; i1<n1; ++i1) {
      float yi = x[i1];
      for (int j=0; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        yi -= _a[j]*y[k1];
      }
      y[i1] = yi;
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
        for (int j=0; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1 && k1<n1 && 0<=k2)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
    for (int i2=i2lo; i2<n2; ++i2) {
      for (int i1=0; i1<i1lo; ++i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1lo; i1<i1hi; ++i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1hi; i1<n1; ++i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = yi;
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
      for (int j=0; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        if (k1<n1)
          yi -= _a[j]*y[k1];
      }
      y[i1] = yi;
    }
    for (int i1=i1hi-1; i1>=0; --i1) {
      float yi = x[i1];
      for (int j=0; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        yi -= _a[j]*y[k1];
      }
      y[i1] = yi;
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
    int i1lo = max(0,-_max1);
    int i1hi = min(n1,n1-_min1);
    int i2hi = (i1lo<=i1hi)?max(n2-_max2,0):0;
    for (int i2=n2-1; i2>=i2hi; --i2) {
      for (int i1=n1-1; i1>=0; --i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (0<=k1 && k1<n1 && k2<n2)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
    for (int i2=i2hi-1; i2>=0; --i2) {
      for (int i1=n1-1; i1>=i1hi; --i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (k1<n1)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1hi-1; i1>=i1lo; --i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1lo-1; i1>=0; --i1) {
        float yi = x[i2][i1];
        for (int j=0; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (0<=k1)
            yi -= _a[j]*y[k2][k1];
        }
        y[i2][i1] = yi;
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

  private void copyLags(int[] lag1) {
    Check.argument(lag1.length==_a.length,"lag1.length==a.length");
    int m = lag1.length;
    _lag1 = Array.copy(lag1);
    _min1 = Array.min(lag1);
    _max1 = Array.max(lag1);
    for (int j=0; j<m; ++j)
      Check.argument(lag1[j]>0,"lag1["+j+"]>0");
  }

  private void copyLags(int[] lag1, int[] lag2) {
    Check.argument(lag1.length==_a.length,"lag1.length==a.length");
    Check.argument(lag2.length==_a.length,"lag2.length==a.length");
    int m = lag1.length;
    _lag1 = Array.copy(lag1);
    _lag2 = Array.copy(lag2);
    _min1 = Array.min(lag1);
    _min2 = Array.min(lag2);
    _max1 = Array.max(lag1);
    _max2 = Array.max(lag2);
    for (int j=0; j<m; ++j) {
      Check.argument(lag2[j]>=0,"lag2["+j+"]>=0");
      if (lag2[j]==0)
        Check.argument(lag1[j]>0,"if lag2==0, lag1["+j+"]>0");
    }
  }

  private void copyLags(int[] lag1, int[] lag2, int[] lag3) {
    Check.argument(lag1.length==_a.length,"lag1.length==a.length");
    Check.argument(lag2.length==_a.length,"lag2.length==a.length");
    Check.argument(lag3.length==_a.length,"lag3.length==a.length");
    int m = lag1.length;
    _lag1 = Array.copy(lag1);
    _lag2 = Array.copy(lag2);
    _lag3 = Array.copy(lag3);
    _min1 = Array.min(lag1);
    _min2 = Array.min(lag2);
    _min3 = Array.min(lag3);
    _max1 = Array.max(lag1);
    _max2 = Array.max(lag2);
    _max3 = Array.max(lag3);
    for (int j=0; j<m; ++j) {
      Check.argument(lag3[j]>=0,"lag3["+j+"]>=0");
      if (lag3[j]==0) {
        Check.argument(lag2[j]>=0,"if lag3==0, lag2["+j+"]>=0");
        if (lag2[j]==0)
          Check.argument(lag1[j]>0,"if lag2==0 && lag3==0, lag1["+j+"]>0");
      }
    }
  }
}
