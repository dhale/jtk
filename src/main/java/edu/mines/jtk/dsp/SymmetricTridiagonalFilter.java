/****************************************************************************
Copyright (c) 2013, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;
import edu.mines.jtk.util.Parallel;

/**
 * A symmetric filter with three constant (shift-invariant) coefficients.
 * Application of this filter is equivalent to multiplication by a tridiagonal
 * matrix. Application of the inverse of this filter (if the inverse exists)
 * is equivalent to solving a tridiagonal system of equations.
 * <p>
 * When applied to an input array x of length n, the filter computes elements
 * of an output y as follows:
 * <pre><code>
 * y[i] =            af*x[i] + b*x[i+1] ; for i = 0
 * y[i] = b*x[i-1] + ai*x[i] + b*x[i+1] ; for i = 1, 2, ..., n-2
 * y[i] = b*x[i-1] + al*x[i]            ; for i = n-1
 * </code></pre>
 * To facilitate different assumptions about values off the ends of arrays
 * (boundary conditions), the coefficients <code>af</code> and <code>al</code>
 * for the first and last equations may differ from the coefficient
 * <code>ai</code> used in the interior equations. In a tridiagonal matrix
 * representation of this filter, the coefficient <code>af</code> is in the
 * upper left corner, and the coefficient <code>al</code> is in the lower
 * right corner.
 * <p>
 * For example, the choice <code>af = al = ai</code> is equivalent to assuming
 * zero values off the ends of input arrays. Likewise, the choice <code>af =
 * al = ai+b</code> is equivalent to assuming zero-slope.
 * <p>
 * This filter and its inverse (if that exists) may be applied in place. For
 * all methods, the input array <code>x</code> and output array <code>y</code>
 * can be the same array.
 * <p>
 * When applying an inverse filter, only a few simple checks are performed to
 * ensure that the inverse exists, that the tridiagonal matrix is not
 * singular. For example <code>|a|&ge;2|b|</code> is required, so that the
 * matrix is diagonally dominant.
 * <p>
 * This software is an adaptation of Algorithm 4.1 in Boisvert, R.F., 1991,
 * Algorithms for special tridiagonal systems: SIAM J. Sci. Stat. Comput., v.
 * 12, no. 2, pp. 423-442.
 * 
 * @author Dave Hale, Colorado School of Mines.
 * @version 2013.07.23
 */
public class SymmetricTridiagonalFilter {

  /**
   * Constructs a symmetric tridiagonal filter.
   * @param af the diagonal coefficient a for the first sample.
   * @param ai the diagonal coefficient a for interior samples.
   * @param al the diagonal coefficient a for the last sample.
   * @param b the off-diagonal coefficient b.
   */
  public SymmetricTridiagonalFilter(
    double af, double ai, double al, double b) 
  {
    _af = af;
    _ai = ai;
    _al = al;
    _b = b;
  }

  /**
   * Applies this filter.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void apply(float[] x, float[] y) {
    int n = x.length;
    int nm1 = n-1;
    float af = (float)_af;
    float ai = (float)_ai;
    float al = (float)_al;
    float b = (float)_b;
    float xim1;
    float xi = x[0];
    float xip1 = x[1];
    y[0] = af*xi+b*xip1;
    for (int i=1; i<nm1; ++i) {
      xim1 = xi;
      xi = xip1;
      xip1 = x[i+1];
      y[i] = ai*xi+b*(xim1+xip1);
    }
    xim1 = xi;
    xi = xip1;
    y[n-1] = al*xi+b*xim1;
  }

  /**
   * Applies this filter along the 1st dimension of a 2D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void apply1(final float[][] x, final float[][] y) {
    int n = x.length;
    Parallel.loop(n,new Parallel.LoopInt() {
    public void compute(int i) {
      apply(x[i],y[i]);
    }});
  }

  /**
   * Applies this filter along the 2nd dimension of a 2D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void apply2(final float[][] x, final float[][] y) {
    int n1 = x[0].length;
    int n2 = x.length;
    int n2m1 = n2-1;
    float af = (float)_af;
    float ai = (float)_ai;
    float al = (float)_al;
    float b = (float)_b;
    float[] xi2m1 = new float[n1];
    float[] xi2 = copy(x[0]);
    float[] xi2p1 = copy(x[1]);
    float[] yi2 = y[0];
    for (int i1=0; i1<n1; ++i1)
      yi2[i1] = af*xi2[i1]+b*xi2p1[i1];
    for (int i2=1; i2<n2m1; ++i2) {
      float[] xtemp = xi2m1;
      xi2m1 = xi2;
      xi2 = xi2p1;
      xi2p1 = xtemp;
      copy(x[i2+1],xi2p1);
      yi2 = y[i2];
      for (int i1=0; i1<n1; ++i1)
        yi2[i1] = ai*xi2[i1]+b*(xi2m1[i1]+xi2p1[i1]);
    }
    xi2m1 = xi2;
    xi2 = xi2p1;
    yi2 = y[n2-1];
    for (int i1=0; i1<n1; ++i1)
      yi2[i1] = al*xi2[i1]+b*xi2m1[i1];
  }

  /**
   * Applies this filter along the 1st dimension of a 3D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void apply1(final float[][][] x, final float[][][] y) {
    int n = x.length;
    Parallel.loop(n,new Parallel.LoopInt() {
    public void compute(int i) {
      apply1(x[i],y[i]);
    }});
  }

  /**
   * Applies this filter along the 2nd dimension of a 3D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void apply2(final float[][][] x, final float[][][] y) {
    int n = x.length;
    Parallel.loop(n,new Parallel.LoopInt() {
    public void compute(int i) {
      apply2(x[i],y[i]);
    }});
  }

  /**
   * Applies this filter along the 3rd dimension of a 3D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void apply3(float[][][] x, float[][][] y) {
    int n2 = x[0].length;
    int n3 = x.length;
    float[][][] xt = new float[n2][n3][];
    float[][][] yt = new float[n2][n3][];
    for (int i2=0; i2<n2; ++i2) {
      for (int i3=0; i3<n3; ++i3) {
        xt[i2][i3] = x[i3][i2];
        yt[i2][i3] = y[i3][i2];
      }
    }
    apply2(xt,yt);
  }

  /**
   * Applies the inverse of this filter.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void applyInverse(float[] x, float[] y) {
    checkInvertible();
    int n = x.length;
    int nm1 = n-1;

    // The trivial special case where b is zero.
    if (_b==0.0f) {
      y[0] = x[0]/(float)_af;
      float oa = 1.0f/(float)_ai;
      for (int i=1; i<nm1; ++i)
        y[i] = x[i]*oa;
      y[n-1] = x[n-1]/(float)_al;
      return;
    }

    // The non-trivial case where b is not zero.
    float u = (float)(_ai/_af);
    float v = (float)(_ai/_al);
    float a = (float)(_ai/_b); // alpha
    float aa = a*a; // alpha squared
    float b = -a*(1.0f+sqrt((1.0f-4.0f/aa)))/2.0f; // beta
    if (abs(b)>1)
      b = 1.0f/b;
    float bb = b*b; // beta squared
    float ss = (1.0f+bb)/u-bb; // sigma squared
    float gg = (1.0f+bb)/v-1.0f; // gamma squared

    // Rescale, while copying input array to output array.
    float scale = (1.0f+bb)/(float)_ai;
    for (int i=0; i<n; ++i)
      y[i] = scale*x[i];

    // If -1 < beta < 1, ...
    if (bb<1.0f) {

      // Factorization.
      float ynm1 = 0.0f;
      float c = (1.0f-bb-ss)/ss;
      float d = 1.0f-bb+gg*(1.0f+c*pow(bb,n-1));
      float e = pow(1.0f-abs(b),2.0f)*FLT_EPSILON/4.0f;
      int k = min((int)ceil(log(e)/log(abs(b))),2*(n-1));
      int m = k-n+1; // 2-n<= m <= n-1
      for (int i=m; i>0; --i)
        ynm1 = b*ynm1+y[i];
      ynm1 *= c;
      if (n-k<1)
        ynm1 = b*ynm1+(1.0f+c)*y[0];
      m = max(n-k,1); // 1 <= m <= n-1
      for (int i=m; i<n; ++i)
        ynm1 = b*ynm1+y[i];
      ynm1 /= d;

      // Backward substitution.
      y[n-1] -= gg*ynm1;
      for (int i=n-2; i>=0; --i)
        y[i] += b*y[i+1];

      // First y.
      y[0] /= ss;

      // Forward substitution.
      for (int i=1; i<nm1; ++i)
        y[i] += b*y[i-1];
      y[n-1] = ynm1;
    }
     
    // Else, if a special case beta = 1 or beta = -1, ...
    else {
      // Unlike Boisvert, we do not assume special values for u and v.

      // If Q is an n x (n+1) matrix (not a square matrix), ...
      if (ss>0.0f && gg>0.0f) {

        // Compute y[n-1] using special case of Boisvert's equation 9.
        float oss = 1.0f/ss;
        float sum = 0.0f;
        for (int j=0; j<nm1; ++j)
          sum = (sum+(j+oss)*y[j])*b;
        sum += (n-1+oss)*y[n-1];
        float ynm1 = sum/(1.0f+gg*(n-1)+gg/ss);

        // Back substitution.
        y[n-1] -= gg*ynm1;
        for (int i=n-2; i>=0; --i)
          y[i] += b*y[i+1];
        y[0] /= ss;

        // Forward substitution.
        for (int i=1; i<nm1; ++i)
          y[i] += b*y[i-1];
        y[n-1] = ynm1;
      } 

      // Else, if first column of Q is zero (so Q is a square matrix), ...
      else if (ss==0.0f) {

        // Forward substitution.
        y[0] *= -b;
        for (int i=1; i<nm1; ++i)
          y[i] = b*(y[i-1]-y[i]);

        // Last y.
        y[n-1] = (y[n-1]-y[n-2])/gg;

        // Backward substitution.
        for (int i=n-2; i>=0; --i)
          y[i] = b*(y[i+1]-y[i]);
      } 

      // Else, if last column of Q is zero (so Q is a square matrix), ...
      else if (gg==0.0f) {

        // Backward substitution.
        for (int i=n-2; i>0; --i)
          y[i] += b*y[i+1];

        // First y.
        y[0] = (y[0]+b*y[1])/ss;

        // Forward substitution.
        for (int i=1; i<n; ++i)
          y[i] += b*y[i-1];
      } 
      
      // Else, matrix QQ' is singular.
      else {
        Check.state(false,"filter is invertible");
      }
    }
  }

  /**
   * Applies the inverse of this filter along the 1st dimension of a 2D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void applyInverse1(final float[][] x, final float[][] y) {
    int n = x.length;
    Parallel.loop(n,new Parallel.LoopInt() {
    public void compute(int i) {
      applyInverse(x[i],y[i]);
    }});
  }

  /**
   * Applies the inverse of this filter along the 2nd dimension of a 2D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void applyInverse2(float[][] x, float[][] y) {
    checkInvertible();
    int n1 = x[0].length;
    int n2 = x.length;
    int n2m1 = n2-1;

    // The trivial special case where b is zero.
    if (_b==0.0f) {
      float oa = 1.0f/(float)_af;
      for (int i1=0; i1<n1; ++i1)
        y[0][i1] = x[0][i1]*oa;
      oa = 1.0f/(float)_ai;
      for (int i2=1; i2<n2m1; ++i2)
        for (int i1=0; i1<n1; ++i1)
          y[i2][i1] = x[i2][i1]*oa;
      oa = 1.0f/(float)_al;
      for (int i1=0; i1<n1; ++i1)
        y[n2m1][i1] = x[n2m1][i1]*oa;
      return;
    }

    // The non-trivial case where b is not zero.
    float u = (float)(_ai/_af);
    float v = (float)(_ai/_al);
    float a = (float)(_ai/_b); // alpha
    float aa = a*a; // alpha squared
    float b = -a*(1.0f+sqrt((1.0f-4.0f/aa)))/2.0f; // beta
    if (abs(b)>1)
      b = 1.0f/b;
    float bb = b*b; // beta squared
    float ss = (1.0f+bb)/u-bb; // sigma squared
    float gg = (1.0f+bb)/v-1.0f; // gamma squared

    // Rescale, while copying input array to output array.
    float scale = (1.0f+bb)/(float)_ai;
    for (int i2=0; i2<n2; ++i2)
      for (int i1=0; i1<n1; ++i1)
        y[i2][i1] = scale*x[i2][i1];

    // If -1 < beta < 1, ...
    if (bb<1.0f) {

      // Factorization.
      float[] yn2m1 = new float[n1];
      float c = (1.0f-bb-ss)/ss;
      float d = 1.0f-bb+gg*(1.0f+c*pow(bb,n2-1));
      float e = pow(1.0f-abs(b),2.0f)*FLT_EPSILON/4.0f;
      int k2 = min((int)ceil(log(e)/log(abs(b))),2*(n2-1));
      int m2 = k2-n2+1; // 2-n2<= m2 <= n2-1
      for (int i2=m2; i2>0; --i2)
        for (int i1=0; i1<n1; ++i1)
          yn2m1[i1] = b*yn2m1[i1]+y[i2][i1];
      for (int i1=0; i1<n1; ++i1)
        yn2m1[i1] *= c;
      if (n2-k2<1) {
        for (int i1=0; i1<n1; ++i1)
          yn2m1[i1] = b*yn2m1[i1]+(1.0f+c)*y[0][i1];
      }
      m2 = max(n2-k2,1); // 1 <= m2 <= n2-1
      for (int i2=m2; i2<n2; ++i2)
        for (int i1=0; i1<n1; ++i1)
          yn2m1[i1] = b*yn2m1[i1]+y[i2][i1];
      for (int i1=0; i1<n1; ++i1)
        yn2m1[i1] /= d;

      // Backward substitution.
      for (int i1=0; i1<n1; ++i1)
        y[n2-1][i1] -= gg*yn2m1[i1];
      for (int i2=n2-2; i2>=0; --i2)
        for (int i1=0; i1<n1; ++i1)
          y[i2][i1] += b*y[i2+1][i1];

      // First y.
      for (int i1=0; i1<n1; ++i1)
        y[0][i1] /= ss;

      // Forward substitution.
      for (int i2=1; i2<n2m1; ++i2)
        for (int i1=0; i1<n1; ++i1)
          y[i2][i1] += b*y[i2-1][i1];
      for (int i1=0; i1<n1; ++i1)
        y[n2m1][i1] = yn2m1[i1];
    }
     
    // Else, if a special case beta = 1 or beta = -1, ...
    else {
      // Unlike Boisvert, we do not assume special values for u and v.

      // If Q is an n x (n+1) matrix (not a square matrix), ...
      if (ss>0.0f && gg>0.0f) {

        // Compute y[n2-1] using special case of Boisvert's equation 9.
        float[] yn2m1 = new float[n1];
        float oss = 1.0f/ss;
        float sum = 0.0f;
        for (int i2=0; i2<n2m1; ++i2)
          for (int i1=0; i1<n1; ++i1)
            yn2m1[i1] = (yn2m1[i1]+(i2+oss)*y[i2][i1])*b;
        for (int i1=0; i1<n1; ++i1) {
          yn2m1[i1] += (n2m1+oss)*y[n2m1][i1];
          yn2m1[i1] /= 1.0f+gg*(n2m1)+gg/ss;
        }

        // Back substitution.
        for (int i1=0; i1<n1; ++i1)
          y[n2m1][i1] -= gg*yn2m1[i1];
        for (int i2=n2-2; i2>=0; --i2)
          for (int i1=0; i1<n1; ++i1)
            y[i2][i1] += b*y[i2+1][i1];
        for (int i1=0; i1<n1; ++i1)
          y[0][i1] /= ss;

        // Forward substitution.
        for (int i2=1; i2<n2m1; ++i2)
          for (int i1=0; i1<n1; ++i1)
            y[i2][i1] += b*y[i2-1][i1];
        for (int i1=0; i1<n1; ++i1)
          y[n2-1][i1] = yn2m1[i1];
      }

      // Else, if first column of Q is zero (so Q is a square matrix), ...
      else if (ss==0.0f) {

        // Forward substitution.
        for (int i1=0; i1<n1; ++i1)
          y[0][i1] *= -b;
        for (int i2=1; i2<n2m1; ++i2)
          for (int i1=0; i1<n1; ++i1)
            y[i2][i1] = b*(y[i2-1][i1]-y[i2][i1]);

        // Last y.
        for (int i1=0; i1<n1; ++i1)
          y[n2m1][i1] = (y[n2-1][i1]-y[n2-2][i1])/gg;

        // Backward substitution.
        for (int i2=n2-2; i2>=0; --i2)
          for (int i1=0; i1<n1; ++i1)
            y[i2][i1] = b*(y[i2+1][i1]-y[i2][i1]);
      } 

      // Else, if last column of Q is zero (so Q is a square matrix), ...
      else if (gg==0.0f) {

        // Backward substitution.
        for (int i2=n2-2; i2>0; --i2)
          for (int i1=0; i1<n1; ++i1)
            y[i2][i1] += b*y[i2+1][i1];

        // First y.
        for (int i1=0; i1<n1; ++i1)
          y[0][i1] = (y[0][i1]+b*y[1][i1])/ss;

        // Forward substitution.
        for (int i2=1; i2<n2; ++i2)
          for (int i1=0; i1<n1; ++i1)
            y[i2][i1] += b*y[i2-1][i1];
      } 
      
      // Else, matrix QQ' is singular.
      else {
        Check.state(false,"filter is invertible");
      }
    }
  }

  /**
   * Applies the inverse of this filter along the 1st dimension of a 3D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void applyInverse1(final float[][][] x, final float[][][] y) {
    int n = x.length;
    Parallel.loop(n,new Parallel.LoopInt() {
    public void compute(int i) {
      applyInverse1(x[i],y[i]);
    }});
  }

  /**
   * Applies the inverse of this filter along the 2nd dimension of a 3D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void applyInverse2(final float[][][] x, final float[][][] y) {
    int n = x.length;
    Parallel.loop(n,new Parallel.LoopInt() {
    public void compute(int i) {
      applyInverse2(x[i],y[i]);
    }});
  }

  /**
   * Applies the inverse of this filter along the 3rd dimension of a 3D array.
   * @param x input array x; may be the same as the output array y.
   * @param y output array y; may be the same as the input array x.
   */
  public void applyInverse3(float[][][] x, float[][][] y) {
    int n2 = x[0].length;
    int n3 = x.length;
    float[][][] xt = new float[n2][n3][];
    float[][][] yt = new float[n2][n3][];
    for (int i2=0; i2<n2; ++i2) {
      for (int i3=0; i3<n3; ++i3) {
        xt[i2][i3] = x[i3][i2];
        yt[i2][i3] = y[i3][i2];
      }
    }
    applyInverse2(xt,yt);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _af,_ai,_al,_b; // coefficients

  private void checkInvertible() {
    Check.state(abs(_ai)>=2.0*abs(_b),"filter is invertible");
  }

  ///////////////////////////////////////////////////////////////////////////
  // testing

  private static void trace(String s) {
    System.out.println(s);
  }

  public static void test1Simple() {
    trace("test1Simple");
    int n = 5;
    double af,ai,al,b;
    //af = al = 0.50; // zero-value
    af = al = 0.75; // zero-slope
    ai = 0.50;
    b  = 0.25;
    SymmetricTridiagonalFilter f = new SymmetricTridiagonalFilter(af,ai,al,b);
    float[] x = zerofloat(n);
    float[] y = zerofloat(n);
    float[] z = zerofloat(n);
    fill(1.0f,x);
    //x[n/2] = 1.0f;
    f.apply(x,y);
    f.applyInverse(y,z);
    //dump(x); dump(y); dump(z);
    assertEqual(x,z);
  }
  public static void test2Simple() {
    trace("test2Simple");
    int n1 = 5;
    int n2 = 4;
    double af,ai,al,b;
    //af = al = 0.50; // zero-value
    af = al = 0.75; // zero-slope
    ai = 0.50;
    b  = 0.25;
    SymmetricTridiagonalFilter f = new SymmetricTridiagonalFilter(af,ai,al,b);
    float[][] x = zerofloat(n1,n2);
    float[][] y = zerofloat(n1,n2);
    float[][] z = zerofloat(n1,n2);
    fill(1.0f,x);
    //x[n2/2][n1/2] = 1.0f;
    f.apply1(x,y);
    f.apply2(y,y);
    f.applyInverse1(y,z);
    f.applyInverse2(z,z);
    //dump(x); dump(y); dump(z);
    assertEqual(x,z);
  }
  public static void test3Simple() {
    trace("test3Simple");
    int n1 = 11;
    int n2 = 12;
    int n3 = 13;
    float[][][] r = randfloat(n1,n2,n3);
    float[][][] x = copy(r);
    float[][][] y = copy(r);
    SymmetricTridiagonalFilter stf = 
      new SymmetricTridiagonalFilter(2.6,2.5,2.7,1.2);
    stf.apply1(x,x);
    stf.apply2(x,x);
    stf.apply3(x,x);
    stf.apply1(y,y);
    y = transpose12(y);
    stf.apply1(y,y);
    y = transpose12(y);
    y = transpose23(y);
    stf.apply2(y,y);
    y = transpose23(y);
    assertEqual(x,y);
  }
  private static float[][][] transpose12(float[][][] x) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    float[][][] y = new float[n3][n1][n2];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          y[i3][i1][i2] = x[i3][i2][i1];
        }
      }
    }
    return y;
  }
  private static float[][][] transpose23(float[][][] x) {
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    int n3 = x.length;
    float[][][] y = new float[n2][n3][n1];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          y[i2][i3][i1] = x[i3][i2][i1];
        }
      }
    }
    return y;
  }
  private static SymmetricTridiagonalFilter makeRandomFilter() {
    java.util.Random r = new java.util.Random();
    float af,ai,al,b;
    boolean aeq2b = r.nextBoolean(); // |a| = |2b|?
    boolean abneg = r.nextBoolean(); // sgn(a) = sgn(b)?
    boolean afzs = r.nextBoolean(); // af for zero-slope?
    boolean alzs = r.nextBoolean(); // al for zero-slope?
    if (aeq2b && afzs==true && alzs==true) {
      if (r.nextBoolean()) {
        afzs = false;
      } else {
        alzs = false;
      }
    }
    b = r.nextFloat();
    ai = 2.0f*b;
    if (!aeq2b) ai += max(0.001,r.nextFloat())*b;
    if (abneg) ai = -ai;
    af = ai;
    al = ai;
    if (afzs) af = ai+b;
    if (alzs) al = ai+b;
    return new SymmetricTridiagonalFilter(af,ai,al,b);
  }
  public static void test1Random() {
    trace("test1Random");
    java.util.Random r = new java.util.Random();
    int ntest = 1000;
    for (int itest=0; itest<ntest; ++itest) {
      SymmetricTridiagonalFilter stf = makeRandomFilter();
      boolean inplace = r.nextBoolean(); // apply in-place?
      int n = 2+r.nextInt(10);
      float[] t = randfloat(r,n);
      float[] x = copy(t);
      float[] y = inplace?x:zerofloat(n);
      float[] z = inplace?x:zerofloat(n);
      stf.apply(x,y);
      stf.applyInverse(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
    }
  }
  public static void test2Random() {
    trace("test2Random");
    java.util.Random r = new java.util.Random();
    int ntest = 1000;
    for (int itest=0; itest<ntest; ++itest) {
      SymmetricTridiagonalFilter stf = makeRandomFilter();
      boolean inplace = r.nextBoolean(); // apply in-place?
      int n1 = 2+r.nextInt(11);
      int n2 = 2+r.nextInt(12);
      float[][] t = randfloat(r,n1,n2);
      float[][] x = copy(t);
      float[][] y = inplace?x:zerofloat(n1,n2);
      float[][] z = inplace?x:zerofloat(n1,n2);
      stf.apply1(x,y);
      stf.applyInverse1(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
      stf.apply2(x,y);
      stf.applyInverse2(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
    }
  }
  public static void test3Random() {
    trace("test3Random");
    java.util.Random r = new java.util.Random();
    int ntest = 1000;
    for (int itest=0; itest<ntest; ++itest) {
      SymmetricTridiagonalFilter stf = makeRandomFilter();
      boolean inplace = r.nextBoolean(); // apply in-place?
      int n1 = 2+r.nextInt(11);
      int n2 = 2+r.nextInt(12);
      int n3 = 2+r.nextInt(13);
      float[][][] t = randfloat(r,n1,n2,n3);
      float[][][] x = copy(t);
      float[][][] y = inplace?x:zerofloat(n1,n2,n3);
      float[][][] z = inplace?x:zerofloat(n1,n2,n3);
      stf.apply1(x,y);
      stf.applyInverse1(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
      stf.apply2(x,y);
      stf.applyInverse2(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
      stf.apply3(x,y);
      stf.applyInverse3(y,z);
      assertEqual(t,x);
      assertEqual(t,z);
    }
  }
  private static void assertEqual(float[] e, float[] a) {
    float tol = 0.001f*max(abs(e));
    assertEqual(e,a,tol);
  }
  private static void assertEqual(float[][] e, float[][] a) {
    float tol = 0.001f*max(abs(e));
    assertEqual(e,a,tol);
  }
  private static void assertEqual(float[][][] e, float[][][] a) {
    float tol = 0.001f*max(abs(e));
    assertEqual(e,a,tol);
  }
  private static void assertEqual(float[] e, float[] a, float tol) {
    int n = e.length;
    for (int i=0; i<n; ++i) {
      float error = abs(e[i]-a[i]);
      if (error>tol)
        trace("expected="+e[i]+" actual="+a[i]);
      assert error<tol;
    }
  }
  private static void assertEqual(float[][] e, float[][] a, float tol) {
    int n = e.length;
    for (int i=0; i<n; ++i)
      assertEqual(e[i],a[i],tol);
  }
  private static void assertEqual(float[][][] e, float[][][] a, float tol) {
    int n = e.length;
    for (int i=0; i<n; ++i)
      assertEqual(e[i],a[i],tol);
  }
  public static void main(String[] args) {
    test1Simple();
    test2Simple();
    test3Simple();
    test1Random();
    test2Random();
    test3Random();
  }
}
