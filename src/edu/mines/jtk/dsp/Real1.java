/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A real-valued sampled function f(x1) of one variable x1.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.15
 */
public class Real1 {

  /**
   * Constructs a sampled function, with specified sampling and values zero.
   * @param x1 the sampling.
   */
  public Real1(Sampling x1) {
    _x1 = x1;
    _f = new float[_x1.getCount()];
  }

  /**
   * Constructs a sampled function, with specified sampling and values.
   * @param x1 the sampling of the variable x.
   * @param f array of function values f(x); referenced, not copied.
   *  The array length f.length must equal the number of samples in x.
   */
  public Real1(Sampling x1, float[] f) {
    Check.argument(x1.getCount()==f.length,
      "f.length equals the number of samples in x1");
    _x1 = x1;
    _f = Array.copy(f);
  }

  /**
   * Constructs a sampled function, with specified sampling and values zero.
   * @param nx1 the number (count) of samples.
   * @param dx1 the sampling interval (delta).
   * @param fx1 the first sample value.
   */
  public Real1(int nx1, double dx1, double fx1) {
    this(new Sampling(nx1,dx1,fx1));
  }

  /**
   * Constructs a sampled function, with specified sampling and values.
   * @param nx1 the number (count) of samples.
   * @param dx1 the sampling interval (delta).
   * @param fx1 the first sample value.
   * @param f array of function values f(x). 
   *  The array length f.length must equal nx1.
   */
  public Real1(int nx1, double dx1, double fx1, float[] f) {
    this(new Sampling(nx1,dx1,fx1),f);
  }

  /**
   * Gets the sampling of x1 for this function.
   * @return the sampling x1.
   */
  public Sampling getSampling() {
    return _x1;
  }

  /**
   * Gets the sampling of x1 for this function.
   * @return the sampling x1.
   */
  public Sampling getSampling1() {
    return _x1;
  }

  /**
   * Gets the sampling of x1 for this function. 
   * (Same as {@link #getSampling1()}).
   * @return the sampling x1.
   */
  public Sampling getX1() {
    return _x1;
  }

  /**
   * Gets the array of values f(x1) for this function.
   * @return the array of function values; by reference, not by copy.
   */
  public float[] getValues() {
    return _f;
  }

  /**
   * Gets the array of values f(x1) for this function.
   * (Same as {@link #getValues()}).
   * @return the array of function values; by reference, not by copy.
   */
  public float[] getF() {
    return _f;
  }

  /**
   * Gets the function value with specified index.
   * @param i1 the index for sampled variable x1.
   * @return the function value.
   */
  public float getValue(int i1) {
    return _f[i1];
  }

  /**
   * Gets the function value with specified index.
   * (Same as {@link #getValue(int)}).
   * @param i1 the index for sampled variable x1.
   * @return the function value.
   */
  public float getF(int i1) {
    return _f[i1];
  }

  /**
   * Returns this function resampled to have the specified sampling.
   * <p>
   * If the specified sampling is consistent with the sampling of this
   * function, this method copies the function values in the overlap 
   * between the two samplings, and assigns zero values to the function 
   * values where the two samplings do not overlap.
   * <p>
   * If the specified sampling is inconsistent with the sampling of this
   * function, then this method must interpolate or decimate this sampled 
   * function. Neither interpolation or decimation is supported, yet.
   * @param x1 the sampling.
   * @return the resampled function.
   * @exception UnsupportedOperationException if the specified sampling is 
   *  inconsistent with this sampling.
   */
  public Real1 resample(Sampling x1) {

    // Overlap between this and that sampling.
    Sampling t1 = _x1;
    Sampling s1 =  x1;
    int[] overlap = t1.overlapWith(s1);

    // If samplings are consistent, ...
    if (overlap!=null) {
      int ni = overlap[0];
      int it = overlap[1];
      int is = overlap[2];
      Real1 rt = this;
      Real1 rs = new Real1(s1);
      float[] ft = rt.getF();
      float[] fs = rs.getF();
      while (--ni>=0)
        fs[is++] = ft[it++];
      return rs;
    }

    // Sampling inconsistent, so must interpolate or decimate.
    throw new UnsupportedOperationException("no interpolation, yet");
  }

  /**
   * Returns the sum this + ra of sampled functions this and ra.
   * The samplings of this and ra must be equivalent.
   * @param ra a sampled function.
   * @return the sum.
   */
  public Real1 plus(Real1 ra) {
    return add(this,ra);
  }

  /**
   * Returns the sum this + ar of this sampled function and constant ar.
   * @param ar a constant.
   * @return the sum.
   */
  public Real1 plus(float ar) {
    return add(this,ar);
  }

  ///////////////////////////////////////////////////////////////////////////
  // public static

  /**
   * Returns a sampled function with value zero.
   * @param n1 the number of samples.
   * @return the sampled function.
   */
  public static Real1 zero(int n1) {
    return new Real1(new Sampling(n1));
  }

  /**
   * Returns a sampled function with value zero.
   * @param x1 the sampling.
   * @return the sampled function.
   */
  public static Real1 zero(Sampling x1) {
    return new Real1(x1);
  }

  /**
   * Returns a sampled function with constant value.
   * @param n1 the number of samples.
   * @param ar the constant.
   * @return the sampled function.
   */
  public static Real1 fill(int n1, double ar) {
    return fill(new Sampling(n1),ar);
  }

  /**
   * Returns a sampled function with constant value.
   * @param x1 the sampling.
   * @param ar the constant.
   * @return the sampled function.
   */
  public static Real1 fill(Sampling x1, double ar) {
    int n1 = x1.getCount();
    Real1 ra = new Real1(x1);
    float[] fa = ra.getF();
    float far = (float)ar;
    while (--n1>=0)
      fa[n1] = far;
    return ra;
  }

  /**
   * Returns a sampled linear (ramp) function.
   * The function values are f1a+i1*d1a, for i1 in [0:n1).
   * @param n1 the number of samples
   * @param d1a the function value delta.
   * @param f1a the first function value.
   * @return the sampled function.
   */
  public static Real1 ramp(int n1, double d1a, double f1a) {
    Real1 ra = new Real1(n1,1.0,0.0);
    float[] fa = ra.getF();
    while (--n1>=0)
      fa[n1] = (float)(f1a+n1*d1a);
    return ra;
  }

  /**
   * Returns the sum ra + rb of two sampled functions ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a sampled function.
   * @param rb a sampled function.
   * @return the sum.
   */
  public static Real1 add(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_add);
  }

  /**
   * Returns the sum ar + rb of constant ar and sampled function rb.
   * @param ar a constant.
   * @param rb a sampled function.
   * @return the sum.
   */
  public static Real1 add(float ar, Real1 rb) {
    return binaryOp(ar,rb,_add);
  }

  /**
   * Returns the sum ra + br of sampled function ra and constant br.
   * @param ra a sampled function.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 add(Real1 ra, float br) {
    return binaryOp(ra,br,_add);
  }

  /**
   * Returns the difference ra - rb of two sampled functions ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a sampled function.
   * @param rb a sampled function.
   * @return the difference.
   */
  public static Real1 sub(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_sub);
  }

  /**
   * Returns the difference ar - rb of constant ar and sampled function rb.
   * @param ar a constant.
   * @param rb a sampled function.
   * @return the sum.
   */
  public static Real1 sub(float ar, Real1 rb) {
    return binaryOp(ar,rb,_sub);
  }

  /**
   * Returns the difference ra - br of sampled function ra and constant br.
   * @param ra a sampled function.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 sub(Real1 ra, float br) {
    return binaryOp(ra,br,_sub);
  }

  /**
   * Returns the product ra * rb of two sampled functions ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a sampled function.
   * @param rb a sampled function.
   * @return the difference.
   */
  public static Real1 mul(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_mul);
  }

  /**
   * Returns the product ar * rb of constant ar and sampled function rb.
   * @param ar a constant.
   * @param rb a sampled function.
   * @return the sum.
   */
  public static Real1 mul(float ar, Real1 rb) {
    return binaryOp(ar,rb,_mul);
  }

  /**
   * Returns the product ra * br of sampled function ra and constant br.
   * @param ra a sampled function.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 mul(Real1 ra, float br) {
    return binaryOp(ra,br,_mul);
  }

  /**
   * Returns the quotient ra / rb of two sampled functions ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a sampled function.
   * @param rb a sampled function.
   * @return the difference.
   */
  public static Real1 div(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_div);
  }

  /**
   * Returns the quotient ar / rb of constant ar and sampled function rb.
   * @param ar a constant.
   * @param rb a sampled function.
   * @return the sum.
   */
  public static Real1 div(float ar, Real1 rb) {
    return binaryOp(ar,rb,_div);
  }

  /**
   * Returns the quotient ra / br of sampled function ra and constant br.
   * @param ra a sampled function.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 div(Real1 ra, float br) {
    return binaryOp(ra,br,_div);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  Sampling _x1; // sampling of variable x1
  float[] _f; // array of function values f(x1)


  ///////////////////////////////////////////////////////////////////////////
  // Binary operations made generic to reduce code bloat.
  private static Real1 binaryOp(Real1 ra, Real1 rb, Binary ab) {
    Sampling sa = ra.getX1();
    Sampling sb = rb.getX1();
    Check.argument(sa.isEquivalentTo(sb),"samplings equivalent");
    Sampling sc = sa;
    Real1 rc = new Real1(sc);
    float[] fa = ra.getF();
    float[] fb = rb.getF();
    float[] fc = rc.getF();
    ab.apply(fc.length,fa,0,fb,0,fc,0);
    return rc;
  }
  private static Real1 binaryOp(float ar, Real1 rb, Binary ab) {
    Sampling sb = rb.getX1();
    Sampling sc = sb;
    Real1 rc = new Real1(sc);
    float[] fb = rb.getF();
    float[] fc = rc.getF();
    ab.apply(fc.length,ar,fb,0,fc,0);
    return rc;
  }
  private static Real1 binaryOp(Real1 ra, float br, Binary ab) {
    Sampling sa = ra.getX1();
    Sampling sc = sa;
    Real1 rc = new Real1(sc);
    float[] fa = ra.getF();
    float[] fc = rc.getF();
    ab.apply(fc.length,fa,0,br,fc,0);
    return rc;
  }
  private interface Binary {
    public void apply(int n, 
      float[] a, int ia, float[] b, int ib, float[] c, int ic);
    public void apply(int n,
      float a, float[] b, int ib, float[] c, int ic);
    public void apply(int n,
      float a[], int ia, float b, float[] c, int ic);
  }
  private static Binary _add = new Binary() {
    public void apply(int n, 
      float[] a, int ia, float[] b, int ib, float[] c, int ic) 
    {
      while (--n>=0)
        c[ic++] = a[ia++]+b[ib++];
    }
    public void apply(int n, float a, float[] b, int ib, float[] c, int ic) {
      while (--n>=0)
        c[ic++] = a+b[ib++];
    }
    public void apply(int n, float a[], int ia, float b, float[] c, int ic) {
      while (--n>=0)
        c[ic++] = a[ia++]+b;
    }
  };
  private static Binary _sub = new Binary() {
    public void apply(int n, 
      float[] a, int ia, float[] b, int ib, float[] c, int ic) 
    {
      while (--n>=0)
        c[ic++] = a[ia++]-b[ib++];
    }
    public void apply(int n, float a, float[] b, int ib, float[] c, int ic) {
      while (--n>=0)
        c[ic++] = a-b[ib++];
    }
    public void apply(int n, float a[], int ia, float b, float[] c, int ic) {
      while (--n>=0)
        c[ic++] = a[ia++]-b;
    }
  };
  private static Binary _mul = new Binary() {
    public void apply(int n, 
      float[] a, int ia, float[] b, int ib, float[] c, int ic) 
    {
      while (--n>=0)
        c[ic++] = a[ia++]*b[ib++];
    }
    public void apply(int n, float a, float[] b, int ib, float[] c, int ic) {
      while (--n>=0)
        c[ic++] = a*b[ib++];
    }
    public void apply(int n, float a[], int ia, float b, float[] c, int ic) {
      while (--n>=0)
        c[ic++] = a[ia++]*b;
    }
  };
  private static Binary _div = new Binary() {
    public void apply(int n, 
      float[] a, int ia, float[] b, int ib, float[] c, int ic) 
    {
      while (--n>=0)
        c[ic++] = a[ia++]/b[ib++];
    }
    public void apply(int n, float a, float[] b, int ib, float[] c, int ic) {
      while (--n>=0)
        c[ic++] = a/b[ib++];
    }
    public void apply(int n, float a[], int ia, float b, float[] c, int ic) {
      while (--n>=0)
        c[ic++] = a[ia++]/b;
    }
  };
}
