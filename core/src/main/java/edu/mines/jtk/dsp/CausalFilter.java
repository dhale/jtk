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
 * A multi-dimensional causal filter that is linear and shift-invariant.
 * The output samples of a causal filter depend only on present and past 
 * input samples. In two dimensions, causal filters are also called 
 * non-symmetric half-plane (NSHP) filters, and this notion of causal 
 * can be extended to higher dimensions.
 * <p>
 * A causal filter is a linear operator with a corresponding anti-causal 
 * transpose (adjoint) operator. A causal filter may have a causal inverse, 
 * and its transpose may have an anti-causal inverse.
 * <p>
 * A causal filter is a stable all-zero filter that may or may not 
 * be minimum-phase; that is, it may or may not have a causal stable 
 * inverse. That inverse is a recursive all-pole filter, as described by
 * Claerbout, J., 1998, Multidimensional recursive filters via a helix: 
 * Geophysics, v. 63, n. 5, p. 1532-1541.
 * <p>
 * The filter and its transpose, inverse, and inverse-transpose may all 
 * be applied in-place; that is, the input and output arrays may be the
 * same array.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.15
 */
public class CausalFilter {

  /**
   * Constructs a unit-impulse filter for specified lag1.
   * By default, all lag2 and lag3 are assumed to be zero.
   * <p>
   * For j=0 only, lag1[j] is zero.
   * All lag1[j] must be non-negative.
   * @param lag1 array of lags.
   */
  public CausalFilter(int[] lag1) {
    this(lag1,impulse(lag1.length));
  }

  /**
   * Constructs a unit-impulse filter for specified lag1 and lag2.
   * By default, all lag3 are assumed to be zero.
   * <p>
   * For j=0 only, lag1[j] and lag2[j] are zero.
   * All lag2[j] must be non-negative.
   * If lag2[j] is zero, then lag1[j] must be non-negative.
   * @param lag1 array of lags in 1st dimension.
   * @param lag2 array of lags in 2nd dimension.
   */
  public CausalFilter(int[] lag1, int[] lag2) {
    this(lag1,lag2,impulse(lag1.length));
  }

  /**
   * Constructs a unit-impulse filter for specified lag1, lag2, and lag3.
   * <p>
   * For j=0 only, lag1[j] and lag2[j] and lag3[j] are zero.
   * All lag3[j] must be non-negative.
   * If lag3[j] is zero, then lag2[j] must be non-negative.
   * If lag3[j] and lag2[j] are zero, then lag1[j] must be non-negative.
   * @param lag1 array of lags in 1st dimension.
   * @param lag2 array of lags in 2nd dimension.
   * @param lag3 array of lags in 3rd dimension.
   */
  public CausalFilter(int[] lag1, int[] lag2, int[] lag3) {
    this(lag1,lag2,lag3,impulse(lag1.length));
  }

  /**
   * Constructs a causal filter for specified lag1.
   * By default, all lag2 and lag3 are assumed to be zero.
   * <p>
   * For j=0 only, lag1[j] is zero.
   * All lag1[j] must be non-negative.
   * @param lag1 array of lags.
   * @param a array of filter coefficients for each lag.
   */
  public CausalFilter(int[] lag1, float[] a) {
    initLags(lag1,a);
    initA(a);
  }

  /**
   * Constructs a causal filter for specified lag1 and lag2.
   * By default, all lag3 are assumed to be zero.
   * <p>
   * For j=0 only, lag1[j] and lag2[j] are zero.
   * All lag2[j] must be non-negative.
   * If lag2[j] is zero, then lag1[j] must be non-negative.
   * @param lag1 array of lags in 1st dimension.
   * @param lag2 array of lags in 2nd dimension.
   * @param a array of filter coefficients for each lag.
   */
  public CausalFilter(int[] lag1, int[] lag2, float[] a) {
    initLags(lag1,lag2,a);
    initA(a);
  }

  /**
   * Constructs a causal filter for specified lag1, lag2, and lag3.
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
  public CausalFilter(int[] lag1, int[] lag2, int[] lag3, float[] a) {
    initLags(lag1,lag2,lag3,a);
    initA(a);
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

  /**
   * Gets a copy of the filter coefficients.
   * @return array of filter coefficients; by copy, not by reference.
   */
  public float[] getA() {
    return copy(_a);
  }

  /**
   * Wilson-Burg factorization for the specified 1-D auto-correlation.
   * Modifies this filter using the iterative Wilson-Burg algorithm. If this 
   * algorithm converges, the impulse response of this filter cascaded with 
   * its transpose approximates the specified auto-correlation.
   * @param maxiter maximum number of Wilson-Burg iterations.
   * @param epsilon tolerance for convergence. Iterations have converged
   *  when the change in all filter coefficients is less than this factor 
   *  times the square root of the zero-lag of the auto correlation.
   * @param r the auto-correlation. This 1-D array must have odd length.
   *  The middle array element is the zero-lag of the auto-correlation,
   *  and other elements are symmetric about the middle element.
   * @exception IllegalStateException if Wilson-Burg iterations do not
   *  converge within the specified maximum number of iterations.
   */
  public void factorWilsonBurg(int maxiter, float epsilon, float[] r) {
    Check.argument(r.length%2==1,"r.length is odd");

    // Maximum length of this filter's impulse response A.
    int m1 = _max1-_min1;

    // Lengths for zero-padded auto-correlation. We must pad with zeros
    // to reduce truncation of R/A' in Wilson-Burg iterations. Because 1/A'
    // has infinite length, we cannot completely eliminate this truncation 
    // error. We assume that the length of 1/A' is no more than ten times
    // that of A.
    int n1 = r.length+10*m1;

    // Indices of zero lag before and after padding with zeros.
    int l1 = (r.length-1)/2;
    int k1 = n1-1-_max1;

    // Workspace.
    float[] s = new float[n1];
    float[] t = new float[n1];
    float[] u = new float[n1];

    // S is R padded with zeros to reduce truncation of R/(AA').
    copy(l1+1+l1,0,r,k1-l1,s);

    // Initial factor is minimum-phase and matches lag zero of R.
    zero(_a);
    _a[0] = sqrt(s[k1]);
    _a0 = _a[0];
    _a0i = 1.0f/_a[0];

    // Loop for maximum iterations or until converged.
    int niter;
    boolean converged = false;
    float eemax = s[k1]*epsilon;
    for (niter=0; niter<maxiter && !converged; ++niter) {
      //dump(_a); // for debugging only

      // U(z) + U(1/z) = 1 + S(z)/(A(z)*A(1/z))
      this.applyInverseTranspose(s,t);
      this.applyInverse(t,u);
      u[k1] += 1.0f;

      // U(z) is the causal part we want; zero the anti-causal part.
      u[k1] *= 0.5f;
      for (int i1=0; i1<k1; ++i1)
        u[i1] = 0.0f;

      // The new A(z) is T(z)  = U(z)*A(z)
      this.apply(u,t);
      converged = true;
      for (int j=0; j<_m; ++j) {
        int j1 = k1+_lag1[j];
        if (0<=j1 && j1<n1) {
          float aj = t[j1];
          if (converged) {
            float e = _a[j]-aj;
            converged = e*e<=eemax;
          }
          _a[j] = aj;
        }
      }
      _a0 = _a[0];
      _a0i = 1.0f/_a[0];
    }
    Check.state(converged,"Wilson-Burg iterations converged");
  }

  /**
   * Wilson-Burg factorization for the specified 2-D auto-correlation.
   * Modifies this filter using the iterative Wilson-Burg algorithm. If this 
   * algorithm converges, the impulse response of this filter cascaded with 
   * its transpose approximates the specified auto-correlation.
   * @param maxiter maximum number of Wilson-Burg iterations.
   * @param epsilon tolerance for convergence. Iterations have converged
   *  when the change in all filter coefficients is less than this factor 
   *  times the square root of the zero-lag of the auto correlation.
   * @param r the auto-correlation. This 2-D array must have odd lengths.
   *  The middle array element is the zero-lag of the auto-correlation,
   *  and other elements are symmetric about the middle element.
   * @exception IllegalStateException if Wilson-Burg iterations do not
   *  converge within the specified maximum number of iterations.
   */
  public void factorWilsonBurg(int maxiter, float epsilon, float[][] r) {
    Check.argument(r[0].length%2==1,"r[0].length is odd");
    Check.argument(r.length%2==1,"r.length is odd");

    // Maximum dimensions of this filter's impulse response A.
    int m1 = _max1-_min1;
    int m2 = _max2-_min2;

    // Dimensions for zero-padded auto-correlation. We must pad with zeros
    // to reduce truncation of R/A' in Wilson-Burg iterations. Because 1/A'
    // has infinite length, we cannot completely eliminate this truncation 
    // error. We assume that the length of 1/A' is no more than ten times
    // that of A.
    int n1 = r[0].length+10*m1;
    int n2 = r.length+10*m2;

    // Indices of zero lag before and after padding with zeros.
    int l1 = (r[0].length-1)/2;
    int l2 = (r.length-1)/2;
    int k1 = n1-1-_max1;
    int k2 = n2-1-_max2;

    // Workspace.
    float[][] s = new float[n2][n1];
    float[][] t = new float[n2][n1];
    float[][] u = new float[n2][n1];

    // S is R padded with zeros to reduce truncation of R/(AA').
    copy(l1+1+l1,l2+1+l2,0,0,r,k1-l1,k2-l2,s);

    // Initial factor is minimum-phase and matches lag zero of R.
    zero(_a);
    _a[0] = sqrt(s[k2][k1]);
    _a0 = _a[0];
    _a0i = 1.0f/_a[0];

    // Loop for maximum iterations or until converged.
    int niter;
    boolean converged = false;
    float eemax = s[k2][k1]*epsilon;
    for (niter=0; niter<maxiter && !converged; ++niter) {
      //dump(_a); // for debugging only

      // U(z) + U(1/z) = 1 + S(z)/(A(z)*A(1/z))
      this.applyInverseTranspose(s,t);
      this.applyInverse(t,u);
      u[k2][k1] += 1.0f;

      // U(z) is the causal part we want; zero the anti-causal part.
      u[k2][k1] *= 0.5f;
      for (int i2=0; i2<k2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          u[i2][i1] = 0.0f;
      for (int i1=0; i1<k1; ++i1)
        u[k2][i1] = 0.0f;

      // The new A(z) is T(z)  = U(z)*A(z)
      this.apply(u,t);
      converged = true;
      for (int j=0; j<_m; ++j) {
        int j1 = k1+_lag1[j];
        int j2 = k2+_lag2[j];
        if (0<=j1 && j1<n1 && 0<=j2 && j2<n2) {
          float aj = t[j2][j1];
          if (converged) {
            float e = _a[j]-aj;
            converged = e*e<=eemax;
          }
          _a[j] = aj;
        }
      }
      _a0 = _a[0];
      _a0i = 1.0f/_a[0];
    }
    Check.state(converged,"Wilson-Burg iterations converged");
  }

  /**
   * Wilson-Burg factorization for the specified 3-D auto-correlation.
   * Modifies this filter using the iterative Wilson-Burg algorithm. If this 
   * algorithm converges, the impulse response of this filter cascaded with 
   * its transpose approximates the specified auto-correlation.
   * @param maxiter maximum number of Wilson-Burg iterations.
   * @param epsilon tolerance for convergence. Iterations have converged
   *  when the change in all filter coefficients is less than this factor 
   *  times the square root of the zero-lag of the auto correlation.
   * @param r the auto-correlation. This 3-D array must have odd lengths.
   *  The middle array element is the zero-lag of the auto-correlation,
   *  and other elements are symmetric about the middle element.
   * @exception IllegalStateException if Wilson-Burg iterations do not
   *  converge within the specified maximum number of iterations.
   */
  public void factorWilsonBurg(int maxiter, float epsilon, float[][][] r) {
    Check.argument(r[0][0].length%2==1,"r[0][0].length is odd");
    Check.argument(r[0].length%2==1,"r[0].length is odd");
    Check.argument(r.length%2==1,"r.length is odd");

    // Maximum dimensions of this filter's impulse response A.
    int m1 = _max1-_min1;
    int m2 = _max2-_min2;
    int m3 = _max3-_min3;

    // Dimensions for zero-padded auto-correlation. We must pad with zeros
    // to reduce truncation of R/A' in Wilson-Burg iterations. Because 1/A'
    // has infinite length, we cannot completely eliminate this truncation 
    // error. We assume that the length of 1/A' is no more than ten times
    // that of A.
    int n1 = r[0][0].length+10*m1;
    int n2 = r[0].length+10*m2;
    int n3 = r.length+10*m3;

    // Indices of zero lag before and after padding with zeros.
    int l1 = (r[0][0].length-1)/2;
    int l2 = (r[0].length-1)/2;
    int l3 = (r.length-1)/2;
    int k1 = n1-1-_max1;
    int k2 = n2-1-_max2;
    int k3 = n3-1-_max3;

    // Workspace.
    float[][][] s = new float[n3][n2][n1];
    float[][][] t = new float[n3][n2][n1];
    float[][][] u = new float[n3][n2][n1];

    // S is R padded with zeros to reduce truncation of R/(AA').
    copy(l1+1+l1,l2+1+l2,l3+1+l3,0,0,0,r,k1-l1,k2-l2,k3-l3,s);

    // Initial factor is minimum-phase and matches lag zero of R.
    zero(_a);
    _a[0] = sqrt(s[k3][k2][k1]);
    _a0 = _a[0];
    _a0i = 1.0f/_a[0];

    // Loop for maximum iterations or until converged.
    int niter;
    boolean converged = false;
    float eemax = s[k3][k2][k1]*epsilon;
    for (niter=0; niter<maxiter && !converged; ++niter) {
      //dump(_a); // for debugging only
      //System.out.println("niter="+niter);
      //checkA(this,r);

      // U(z) + U(1/z) = 1 + S(z)/(A(z)*A(1/z))
      this.applyInverseTranspose(s,t);
      this.applyInverse(t,u);
      u[k3][k2][k1] += 1.0f;

      // U(z) is the causal part we want; zero the anti-causal part.
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

      // The new A(z) is T(z)  = U(z)*A(z)
      this.apply(u,t);
      converged = true;
      for (int j=0; j<_m; ++j) {
        int j1 = k1+_lag1[j];
        int j2 = k2+_lag2[j];
        int j3 = k3+_lag3[j];
        if (0<=j1 && j1<n1 && 0<=j2 && j2<n2 && 0<=j3 && j3<n3) {
          float aj = t[j3][j2][j1];
          if (converged) {
            float e = _a[j]-aj;
            converged = e*e<=eemax;
          }
          _a[j] = aj;
        }
      }
      _a0 = _a[0];
      _a0i = 1.0f/_a[0];
    }
    Check.state(converged,"Wilson-Burg iterations converged");
  }
  /*
  private static void checkA(CausalFilter cf, float[][][] r) {
    float[][][] t = new float[21][21][21];
    t[10][10][10] = 1.0f;
    cf.apply(t,t);
    cf.applyTranspose(t,t);
    float[][][] s = copy(3,3,3,9,9,9,t);
    dump(r);
    dump(s);
  }
  */

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
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[] x, float[] y) {
    int n1 = x.length;
    int i1lo = min(_max1,n1);
    for (int i1=n1-1; i1>=i1lo; --i1) {
      float yi = _a0*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
    for (int i1=i1lo-1; i1>=0; --i1) {
      float yi = _a0*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
  }

  /**
   * Applies the transpose of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param x input array.
   * @param y output array.
   */
  public void applyTranspose(float[] x, float[] y) {
    int n1 = x.length;
    int i1hi = max(n1-_max1,0);
    for (int i1=0; i1<i1hi; ++i1) {
      float yi = _a0*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
    for (int i1=i1hi; i1<n1; ++i1) {
      float yi = _a0*x[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        if (k1<n1)
          yi += _a[j]*x[k1];
      }
      y[i1] = yi;
    }
  }

  /**
   * Applies the inverse of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param y input array.
   * @param x output array.
   */
  public void applyInverse(float[] y, float[] x) {
    int n1 = y.length;
    int i1lo = min(_max1,n1);
    for (int i1=0; i1<i1lo; ++i1) {
      float xi = y[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        if (0<=k1)
          xi -= _a[j]*x[k1];
      }
      x[i1] = xi*_a0i;
    }
    for (int i1=i1lo; i1<n1; ++i1) {
      float xi = y[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1-_lag1[j];
        xi -= _a[j]*x[k1];
      }
      x[i1] = xi*_a0i;
    }
  }

  /**
   * Applies the inverse transpose of this filter.
   * Uses lag1; ignores lag2 or lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param y input array.
   * @param x output array.
   */
  public void applyInverseTranspose(float[] y, float[] x) {
    int n1 = y.length;
    int i1hi = max(n1-_max1,0);
    for (int i1=n1-1; i1>=i1hi; --i1) {
      float xi = y[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        if (k1<n1)
          xi -= _a[j]*x[k1];
      }
      x[i1] = xi*_a0i;
    }
    for (int i1=i1hi-1; i1>=0; --i1) {
      float xi = y[i1];
      for (int j=1; j<_m; ++j) {
        int k1 = i1+_lag1[j];
        xi -= _a[j]*x[k1];
      }
      x[i1] = xi*_a0i;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // 2-D

  /**
   * Applies this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][] x, float[][] y) {
    int n1 = x[0].length;
    int n2 = x.length;
    int i1lo = max(0,_max1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = (i1lo<=i1hi)?min(_max2,n2):n2;
    for (int i2=n2-1; i2>=i2lo; --i2) {
      for (int i1=n1-1; i1>=i1hi; --i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1hi-1; i1>=i1lo; --i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1lo-1; i1>=0; --i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
    for (int i2=i2lo-1; i2>=0; --i2) {
      for (int i1=n1-1; i1>=0; --i1) {
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
  }

  /**
   * Applies the transpose of this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param x input array.
   * @param y output array.
   */
  public void applyTranspose(float[][] x, float[][] y) {
    int n1 = x[0].length;
    int n2 = x.length;
    int i1lo = max(0,-_min1);
    int i1hi = min(n1,n1-_max1);
    int i2hi = (i1lo<=i1hi)?max(n2-_max2,0):0;
    for (int i2=0; i2<i2hi; ++i2) {
      for (int i1=0; i1<i1lo; ++i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (0<=k1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1lo; i1<i1hi; ++i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
      for (int i1=i1hi; i1<n1; ++i1) {
        float yi = _a0*x[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (k1<n1)
            yi += _a[j]*x[k2][k1];
        }
        y[i2][i1] = yi;
      }
    }
    for (int i2=i2hi; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
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
  }

  /**
   * Applies the inverse of this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param y input array.
   * @param x output array.
   */
  public void applyInverse(float[][] y, float[][] x) {
    int n1 = y[0].length;
    int n2 = y.length;
    int i1lo = min(_max1,n1);
    int i1hi = min(n1,n1+_min1);
    int i2lo = (i1lo<=i1hi)?min(_max2,n2):n2;
    for (int i2=0; i2<i2lo; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        float xi = y[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1 && k1<n1 && 0<=k2)
            xi -= _a[j]*x[k2][k1];
        }
        x[i2][i1] = xi*_a0i;
      }
    }
    for (int i2=i2lo; i2<n2; ++i2) {
      for (int i1=0; i1<i1lo; ++i1) {
        float xi = y[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (0<=k1)
            xi -= _a[j]*x[k2][k1];
        }
        x[i2][i1] = xi*_a0i;
      }
      for (int i1=i1lo; i1<i1hi; ++i1) {
        float xi = y[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          xi -= _a[j]*x[k2][k1];
        }
        x[i2][i1] = xi*_a0i;
      }
      for (int i1=i1hi; i1<n1; ++i1) {
        float xi = y[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1-_lag1[j];
          int k2 = i2-_lag2[j];
          if (k1<n1)
            xi -= _a[j]*x[k2][k1];
        }
        x[i2][i1] = xi*_a0i;
      }
    }
  }

  /**
   * Applies the inverse transpose of this filter. 
   * Uses lag1 and lag2; ignores lag3, if specified.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param y input array.
   * @param x output array.
   */
  public void applyInverseTranspose(float[][] y, float[][] x) {
    int n1 = y[0].length;
    int n2 = y.length;
    int i1lo = max(0,-_min1);
    int i1hi = min(n1,n1-_max1);
    int i2hi = (i1lo<=i1hi)?max(n2-_max2,0):0;
    for (int i2=n2-1; i2>=i2hi; --i2) {
      for (int i1=n1-1; i1>=0; --i1) {
        float xi = y[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (0<=k1 && k1<n1 && k2<n2)
            xi -= _a[j]*x[k2][k1];
        }
        x[i2][i1] = xi*_a0i;
      }
    }
    for (int i2=i2hi-1; i2>=0; --i2) {
      for (int i1=n1-1; i1>=i1hi; --i1) {
        float xi = y[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (k1<n1)
            xi -= _a[j]*x[k2][k1];
        }
        x[i2][i1] = xi*_a0i;
      }
      for (int i1=i1hi-1; i1>=i1lo; --i1) {
        float xi = y[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          xi -= _a[j]*x[k2][k1];
        }
        x[i2][i1] = xi*_a0i;
      }
      for (int i1=i1lo-1; i1>=0; --i1) {
        float xi = y[i2][i1];
        for (int j=1; j<_m; ++j) {
          int k1 = i1+_lag1[j];
          int k2 = i2+_lag2[j];
          if (0<=k1)
            xi -= _a[j]*x[k2][k1];
        }
        x[i2][i1] = xi*_a0i;
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
   * @param x input array.
   * @param y output array.
   */
  public void apply(float[][][] x, float[][][] y) {
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
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && k2<n2)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
      for (int i2=i2hi-1; i2>=i2lo; --i2) {
        for (int i1=n1-1; i1>=i1hi; --i1) {
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
        for (int i1=i1hi-1; i1>=i1lo; --i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1lo-1; i1>=0; --i1) {
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
      }
      for (int i2=i2lo-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
    }
    for (int i3=i3lo-1; i3>=0; --i3) {
      for (int i2=n2-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 && k2<n2 && 0<=k3)
              yi += _a[j]*x[k3][k2][k1];
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
   * @param x input array.
   * @param y output array.
   */
  public void applyTranspose(float[][][] x, float[][][] y) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    int i1lo = max(0,-_min1);
    int i1hi = min(n1,n1-_max1);
    int i2lo = max(0,-_min2);
    int i2hi = min(n2,n2-_max2);
    int i3hi = (i1lo<=i1hi && i2lo<=i2hi)?max(n3-_max3,0):0;
    for (int i3=0; i3<i3hi; ++i3) {
      for (int i2=0; i2<i2lo; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
      for (int i2=i2lo; i2<i2hi; ++i2) {
        for (int i1=0; i1<i1lo; ++i1) {
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
        for (int i1=i1lo; i1<i1hi; ++i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
        for (int i1=i1hi; i1<n1; ++i1) {
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
      }
      for (int i2=i2hi; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float yi = _a0*x[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1 && k1<n1 && k2<n2)
              yi += _a[j]*x[k3][k2][k1];
          }
          y[i3][i2][i1] = yi;
        }
      }
    }
    for (int i3=i3hi; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
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
  }

  /**
   * Applies the inverse of this filter. 
   * Uses lag1, lag2, and lag3.
   * @param y output array.
   * @param x input array.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   */
  public void applyInverse(float[][][] y, float[][][] x) {
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
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 && k2<n2 && 0<=k3)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
      }
    }
    for (int i3=i3lo; i3<n3; ++i3) {
      for (int i2=0; i2<i2lo; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
      }
      for (int i2=i2lo; i2<i2hi; ++i2) {
        for (int i1=0; i1<i1lo; ++i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
        for (int i1=i1lo; i1<i1hi; ++i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
        for (int i1=i1hi; i1<n1; ++i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (k1<n1)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
      }
      for (int i2=i2hi; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1-_lag1[j];
            int k2 = i2-_lag2[j];
            int k3 = i3-_lag3[j];
            if (0<=k1 && k1<n1 && k2<n2)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
      }
    }
  }

  /**
   * Applies the inverse transpose of this filter. 
   * Uses lag1, lag2, and lag3.
   * <p>
   * May be applied in-place; input and output arrays may be the same.
   * @param y output array.
   * @param x input array.
   */
  public void applyInverseTranspose(float[][][] y, float[][][] x) {
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
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2 && k2<n2 && k3<n3)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
      }
    }
    for (int i3=i3hi-1; i3>=0; --i3) {
      for (int i2=n2-1; i2>=i2hi; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1 && k1<n1 && k2<n2)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
      }
      for (int i2=i2hi-1; i2>=i2lo; --i2) {
        for (int i1=n1-1; i1>=i1hi; --i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (k1<n1)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
        for (int i1=i1hi-1; i1>=i1lo; --i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
        for (int i1=i1lo-1; i1>=0; --i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
      }
      for (int i2=i2lo-1; i2>=0; --i2) {
        for (int i1=n1-1; i1>=0; --i1) {
          float xi = y[i3][i2][i1];
          for (int j=1; j<_m; ++j) {
            int k1 = i1+_lag1[j];
            int k2 = i2+_lag2[j];
            int k3 = i3+_lag3[j];
            if (0<=k1 && k1<n1 && 0<=k2)
              xi -= _a[j]*x[k3][k2][k1];
          }
          x[i3][i2][i1] = xi*_a0i;
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _m; // number of lags and filter coefficients
  private int _min1,_max1; // min/max lags in 1st dimension
  private int _min2,_max2; // min/max lags in 2nd dimension
  private int _min3,_max3; // min/max lags in 3rd dimension
  private int[] _lag1; // lags in 1st dimension
  private int[] _lag2; // lags in 2nd dimension
  private int[] _lag3; // lags in 3rd dimension
  private float[] _a; // filter coefficients
  private float _a0,_a0i; // a[0] and 1/a[0]

  private static float[] impulse(int nlag) {
    float[] a = new float[nlag];
    a[0] = 1.0f;
    return a;
  }

  private void initLags(int[] lag1, float[] a) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1.length==a.length,"lag1.length==a.length");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    for (int j=1; j<a.length; ++j)
      Check.argument(lag1[j]>0,"lag1["+j+"]>0");
    _m = lag1.length;
    _lag1 = copy(lag1);
    _lag2 = zeroint(_m);
    _lag3 = zeroint(_m);
    _min1 = min(lag1);
    _max1 = max(lag1);
  }

  private void initLags(int[] lag1, int[] lag2, float[] a) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1.length==a.length,"lag1.length==a.length");
    Check.argument(lag2.length==a.length,"lag2.length==a.length");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    Check.argument(lag2[0]==0,"lag2[0]==0");
    for (int j=1; j<a.length; ++j) {
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

  private void initLags(int[] lag1, int[] lag2, int[] lag3, float[] a) {
    Check.argument(lag1.length>0,"lag1.length>0");
    Check.argument(lag1.length==a.length,"lag1.length==a.length");
    Check.argument(lag2.length==a.length,"lag2.length==a.length");
    Check.argument(lag3.length==a.length,"lag3.length==a.length");
    Check.argument(lag1[0]==0,"lag1[0]==0");
    Check.argument(lag2[0]==0,"lag2[0]==0");
    Check.argument(lag3[0]==0,"lag3[0]==0");
    for (int j=1; j<a.length; ++j) {
      Check.argument(lag3[j]>=0,"lag3["+j+"]>=0");
      if (lag3[j]==0) {
        Check.argument(lag2[j]>=0,"if lag3==0, lag2["+j+"]>=0");
        if (lag2[j]==0)
          Check.argument(lag1[j]>0,"if lag3==0 && lag2==0, lag1["+j+"]>0");
      }
    }
    _m = a.length;
    _lag1 = copy(lag1);
    _lag2 = copy(lag2);
    _lag3 = copy(lag3);
    _min1 = min(lag1);
    _min2 = min(lag2);
    _min3 = min(lag3);
    _max1 = max(lag1);
    _max2 = max(lag2);
    _max3 = max(lag3);
  }

  private void initA(float[] a) {
    _a = copy(a);
    _a0 = a[0];
    _a0i = 1.0f/a[0];
  }

  ///////////////////////////////////////////////////////////////////////////
  // EXPERIMENTAL
  /**
   * Wilson-Burg factorization for inverse of specified 1-D auto-correlation.
   * Modifies this filter using the iterative Wilson-Burg algorithm. If this 
   * algorithm converges, the impulse response of this filter cascaded with 
   * its transpose approximates the specified auto-correlation.
   * @param maxiter maximum number of Wilson-Burg iterations.
   * @param epsilon tolerance for convergence. Iterations have converged
   *  when the change in all filter coefficients is less than this factor 
   *  times the square root of the zero-lag of the auto correlation.
   * @param r the auto-correlation. This 1-D array must have odd length.
   *  The middle array element is the zero-lag of the auto-correlation,
   *  and other elements are symmetric about the middle element.
   * @exception IllegalStateException if Wilson-Burg iterations do not
   *  converge within the specified maximum number of iterations.
   */
  /*
  private void factorInverseWilsonBurg(int maxiter, float epsilon, float[] r) {
    Check.argument(r.length%2==1,"r.length is odd");

    // Maximum length of this filter's impulse response A.
    int m1 = _max1-_min1;

    // Lengths for zero-padded auto-correlation. We must pad with zeros
    // to reduce truncation of R*A' in Wilson-Burg iterations.
    int n1 = r.length+m1;

    // Indices of zero lag before and after padding with zeros.
    int l1 = (r.length-1)/2;
    int k1 = l1+m1;

    // Workspace.
    float[] s = new float[n1];
    float[] t = new float[n1];
    float[] u = new float[n1];

    // S is -R padded with zeros to reduce truncation of -AA'R.
    copy(r.length,0,r,k1-l1,s);
    for (int i1=0; i1<n1; ++i1)
      s[i1] = -s[i1];

    // Initial factor is minimum-phase and matches lag zero of 1/R.
    //zero(_a);
    //_a[0] = 1.0f/sqrt(rsum);
    _a0 = _a[0];
    _a0i = 1.0f/_a[0];

    // Loop for maximum iterations or until converged.
    int niter;
    boolean converged = false;
    float eemax = s[k1]*epsilon;
    for (niter=0; niter<maxiter && !converged; ++niter) {
      dump(_a); // for debugging only

      // U(z) + U(1/z) = 3 - A(z)*A(1/z)*S(z)
      this.applyTranspose(s,t);
      this.apply(t,u);
      dump(u);
      u[k1] += 3.0f;

      // U(z) is the causal part we want; zero the anti-causal part.
      u[k1] *= 0.5f;
      for (int i1=0; i1<k1; ++i1)
        u[i1] = 0.0f;

      // The new A(z) is T(z)  = U(z)*A(z)
      this.apply(u,t);
      converged = true;
      for (int j=0; j<_m; ++j) {
        int j1 = k1+_lag1[j];
        if (0<=j1 && j1<n1) {
          float aj = t[j1];
          if (converged) {
            float e = _a[j]-aj;
            converged = e*e<=eemax;
          }
          _a[j] = aj;
        }
      }
      _a0 = _a[0];
      _a0i = 1.0f/_a[0];
    }
    Check.state(converged,"Wilson-Burg iterations converged");
  }
  */
}
