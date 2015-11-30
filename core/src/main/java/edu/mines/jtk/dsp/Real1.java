/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
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
 * A real-valued sampled function of one variable.
 * A {@link Real1} combines a {@link Sampling} with a reference to an
 * an array of function values. In this way, a {@link Real1} <em>wraps</em> 
 * an array of function values. Because array values are referenced (not 
 * copied), the cost of wrapping any array with a {@link Real1} is small.
 * <p> 
 * One consequence of referencing the array of function values is that 
 * changes to elements in such an array are reflected in <em>all</em> 
 * {@link Real1}s that reference that array. If this behavior is not
 * desired, the copy constructor {@link Real1#Real1(Real1)} creates a 
 * new array copy of function values.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.08.25
 */
public class Real1 {

  /**
   * Constructs a function with specified sampling and values zero.
   * @param s the sampling.
   */
  public Real1(Sampling s) {
    _s = s;
    _v = new float[s.getCount()];
  }

  /**
   * Constructs a function with specified values and default sampling.
   * The default sampling has number (count) of samples = v.length, sampling 
   * interval (delta) = 1.0 and first sample value (first) = 0.0.
   * @param v array of function values; referenced, not copied.
   */
  public Real1(float[] v) {
    _s = new Sampling(v.length,1.0,0.0);
    _v = v;
  }

  /**
   * Constructs a function with specified sampling and values.
   * @param s the sampling.
   * @param v array of function values; referenced, not copied.
   *  The array length v.length must equal the number of samples in s.
   */
  public Real1(Sampling s, float[] v) {
    Check.argument(s.getCount()==v.length,
      "v.length equals the number of samples in s");
    _s = s;
    _v = v;
  }

  /**
   * Constructs a function with specified sampling and values zero.
   * @param n the number (count) of samples.
   * @param d the sampling interval (delta).
   * @param f the value of the first sample.
   */
  public Real1(int n, double d, double f) {
    this(new Sampling(n,d,f));
  }

  /**
   * Constructs a function with specified sampling and values.
   * @param n the number (count) of time samples.
   * @param d the sampling interval (delta).
   * @param f the value of the first sample.
   * @param v array of function values; referenced, not copied. 
   *  The array length v.length must equal n.
   */
  public Real1(int n, double d, double f, float[] v) {
    this(new Sampling(n,d,f),v);
  }

  /**
   * Constructs a copy of the specified sampled function. This constructor 
   * <em>copies</em> (does not reference) the array of function values from 
   * the specified sampled function.
   * @param r the function to copy.
   */
  public Real1(Real1 r) {
    this(r._s, copy(r._v));
  }

  /**
   * Gets the sampling for this function.
   * @return the sampling.
   */
  public Sampling getSampling() {
    return _s;
  }

  /**
   * Gets the array of function values for this function.
   * @return the array of function values; by reference, not by copy.
   */
  public float[] getValues() {
    return _v;
  }

  /**
   * Returns this function resampled to have the specified sampling.
   * <p>
   * If the specified sampling is compatible with the sampling of this
   * function, this method copies the function values in the overlap 
   * between the two samplings, and assigns zero values to the function 
   * values where the two samplings do not overlap.
   * <p>
   * If the specified sampling is incompatible with the sampling of this
   * function, then this method must interpolate or decimate this function 
   * Neither interpolation or decimation is supported, yet.
   * @param s the sampling.
   * @return the resampled function.
   * @exception UnsupportedOperationException if the specified sampling is 
   *  incompatible with this sampling.
   */
  public Real1 resample(Sampling s) {
    Sampling t = _s;

    // Overlap between this and that sampling.
    int[] overlap = t.overlapWith(s);

    // If samplings are compatible, ...
    if (overlap!=null) {
      int ni = overlap[0];
      int it = overlap[1];
      int is = overlap[2];
      Real1 rt = this;
      Real1 rs = new Real1(s);
      float[] xt = rt.getValues();
      float[] xs = rs.getValues();
      while (--ni>=0)
        xs[is++] = xt[it++];
      return rs;
    }

    // Sampling incompatible, so must interpolate or decimate.
    throw new UnsupportedOperationException("no interpolation, yet");
  }

  /**
   * Returns the sum this + ra of functions this and ra.
   * The samplings of this and ra must be equivalent.
   * @param ra a function.
   * @return the sum.
   */
  public Real1 plus(Real1 ra) {
    return add(this,ra);
  }

  /**
   * Returns the sum this + ar of this function and constant ar.
   * @param ar a constant.
   * @return the sum.
   */
  public Real1 plus(float ar) {
    return add(this,ar);
  }

  /**
   * Convolves this function with the specified function. The two functions 
   * must be uniformly sampled with equal sampling intervals.
   * @param ra the function with which to convolve.
   * @return the convolution function.
   */
  public Real1 convolve(Real1 ra) {
    Real1 rx = this;
    Real1 ry = ra;
    Sampling sx = rx.getSampling();
    Sampling sy = ry.getSampling();
    double dx = sx.getDelta();
    double dy = sy.getDelta();
    Check.state(sx.isUniform(),"sampling is uniform");
    Check.argument(sy.isUniform(),"sampling is uniform");
    Check.argument(dx==dy,"sampling intervals are equal");
    int lx = sx.getCount();
    int ly = sy.getCount();
    double fx = sx.getFirst();
    double fy = sy.getFirst();
    float[] x = rx.getValues();
    float[] y = ry.getValues();
    int lz = lx+ly-1;
    double dz = dx;
    double fz = fx+fy;
    float[] z = new float[lz];
    Conv.conv(lx,0,x,ly,0,y,lz,0,z);
    return new Real1(lz,dz,fz,z);
  }

  /**
   * Gets sampling for the Fourier transform of this function. The first 
   * sample value will be zero, because the Fourier transform of a real 
   * function has conjugate-symmetry.
   * <p>
   * A minimum number of Fourier transform samples must be specified, and 
   * the number of samlpes in the returned sampling will not be less than 
   * the larger of that specified minimum and the number of samples in this 
   * function.
   * @param nmin the minimum number of samples after Fourier transform.
   * @return the Fourier transform sampling.
   */
  public Sampling getFourierSampling(int nmin) {
    int nt = _s.getCount();
    double dt = _s.getDelta();
    int nfft = FftReal.nfftSmall(max(nmin,nt));
    int nf = nfft/2+1;
    double df = 1.0/(nfft*dt);
    double ff = 0.0;
    return new Sampling(nf,df,ff);
  }

  /* *
   * Computes the Fourier transform of this function.
   * @param f the frequency sampling.
   * @return the Fourier transform.
   */
  //public Complex1 fourierTransform(Sampling f);

  ///////////////////////////////////////////////////////////////////////////
  // public static

  /**
   * Returns a sampled function with values zero.
   * @param n the number of samples.
   * @return the function.
   */
  public static Real1 zero(int n) {
    return new Real1(new Sampling(n));
  }

  /**
   * Returns a sampled function with values zero.
   * @param s the sampling.
   * @return the function.
   */
  public static Real1 zero(Sampling s) {
    return new Real1(s);
  }

  /**
   * Returns a function with constant values.
   * @param ar the constant.
   * @param n the number of samples.
   * @return the function.
   */
  public static Real1 fill(double ar, int n) {
    return Real1.fill(ar,new Sampling(n));
  }

  /**
   * Returns a function with constant values.
   * @param ar the constant.
   * @param s the sampling.
   * @return the function.
   */
  public static Real1 fill(double ar, Sampling s) {
    int n = s.getCount();
    return new Real1(s, fillfloat((float)ar,n));
  }

  /**
   * Returns a sampled linear (ramp) function.
   * The function values are fv+iv*dv, for iv in [0:nv).
   * @param fv the first function value.
   * @param dv the function value delta.
   * @param nv the number of values.
   * @return the function.
   */
  public static Real1 ramp(double fv, double dv, int nv) {
    return Real1.ramp(fv,dv,new Sampling(nv));
  }

  /**
   * Returns a sampled linear (ramp) function.
   * The function values are fv+dv*(s-f), for s = f, f+d, ..., f+d*(n-1),
   * where n, d, and f are the sampling count, delta, and first value.
   * @param fv the first function value.
   * @param dv the function value delta.
   * @param s the sampling.
   * @return the function.
   */
  public static Real1 ramp(double fv, double dv, Sampling s) {
    int n = s.getCount();
    double d = s.getDelta();
    double f = s.getFirst();
    return new Real1(s, rampfloat((float)(fv-f*dv),(float)(d*dv),n));
  }

  /**
   * Returns the sum ra + rb of two functions ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a function.
   * @param rb a function.
   * @return the sum.
   */
  public static Real1 add(Real1 ra, Real1 rb) {
	return binaryOp(ra,rb,_add);
  }

  /**
   * Returns the sum ar + rb of constant ar and function rb.
   * @param ar a constant.
   * @param rb a function.
   * @return the sum.
   */
  public static Real1 add(float ar, Real1 rb) {
    return binaryOp(ar,rb,_add);
  }

  /**
   * Returns the sum ra + br of function ra and constant br.
   * @param ra a function.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 add(Real1 ra, float br) {
    return binaryOp(ra,br,_add);
  }

  /**
   * Returns the difference ra - rb of two functions ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a function.
   * @param rb a function.
   * @return the difference.
   */
  public static Real1 sub(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_sub);
  }

  /**
   * Returns the difference ar - rb of constant ar and function rb.
   * @param ar a constant.
   * @param rb a function.
   * @return the sum.
   */
  public static Real1 sub(float ar, Real1 rb) {
    return binaryOp(ar,rb,_sub);
  }

  /**
   * Returns the difference ra - br of function ra and constant br.
   * @param ra a function.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 sub(Real1 ra, float br) {
    return binaryOp(ra,br,_sub);
  }

  /**
   * Returns the product ra * rb of two functions ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a function.
   * @param rb a function.
   * @return the difference.
   */
  public static Real1 mul(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_mul);
  }

  /**
   * Returns the product ar * rb of constant ar and function rb.
   * @param ar a constant.
   * @param rb a function.
   * @return the sum.
   */
  public static Real1 mul(float ar, Real1 rb) {
    return binaryOp(ar,rb,_mul);
  }

  /**
   * Returns the product ra * br of function ra and constant br.
   * @param ra a function.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 mul(Real1 ra, float br) {
    return binaryOp(ra,br,_mul);
  }

  /**
   * Returns the quotient ra / rb of two functions ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a function.
   * @param rb a function.
   * @return the difference.
   */
  public static Real1 div(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_div);
  }

  /**
   * Returns the quotient ar / rb of constant ar and function rb.
   * @param ar a constant.
   * @param rb a function.
   * @return the sum.
   */
  public static Real1 div(float ar, Real1 rb) {
    return binaryOp(ar,rb,_div);
  }

  /**
   * Returns the quotient ra / br of function ra and constant br.
   * @param ra a function.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 div(Real1 ra, float br) {
    return binaryOp(ra,br,_div);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  Sampling _s; // sampling of one independent variable
  float[] _v; // array of function values

  /*
  private static void ensureSamplingEquivalent(Real1 ra, Real1 rb) {
    ensureSamplingEquivalent(ra.getSampling(),rb.getSampling());
  }
  
  private static void ensureSamplingEquivalent(Sampling sa, Sampling sb) {
    Check.argument(sa.isEquivalentTo(sb),"samplings equivalent");
  }
  */


  ///////////////////////////////////////////////////////////////////////////
  // Binary operations made generic to reduce code bloat.
  private static Real1 binaryOp(Real1 ra, Real1 rb, Binary ab) {
    Sampling sa = ra.getSampling();
    Sampling sb = rb.getSampling();
    Check.argument(sa.isEquivalentTo(sb),"samplings equivalent");
    Sampling sc = sa;
    Real1 rc = new Real1(sc);
    float[] va = ra.getValues();
    float[] vb = rb.getValues();
    float[] vc = rc.getValues();
    ab.apply(vc.length,va,0,vb,0,vc,0);
    return rc;
  }
  private static Real1 binaryOp(float ar, Real1 rb, Binary ab) {
    Sampling sb = rb.getSampling();
    Sampling sc = sb;
    Real1 rc = new Real1(sc);
    float[] vb = rb.getValues();
    float[] vc = rc.getValues();
    ab.apply(vc.length,ar,vb,0,vc,0);
    return rc;
  }
  private static Real1 binaryOp(Real1 ra, float br, Binary ab) {
    Sampling sa = ra.getSampling();
    Sampling sc = sa;
    Real1 rc = new Real1(sc);
    float[] va = ra.getValues();
    float[] vc = rc.getValues();
    ab.apply(vc.length,va,0,br,vc,0);
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
