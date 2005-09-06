/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.Array.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A real-valued sequence of samples of a function x(t) of one variable t. 
 * For definiteness, we may think of the variable t as having dimensions
 * of time, but x(t) could be a function of some variable with other 
 * dimensions. For consistency, we may think of the Fourier transform 
 * X(f) of x(t) as a function of frequency.
 * <p>
 * A {@link Real1} combines a {@link Sampling} t with a reference to an
 * an array of function values. In this way, a {@link Real1} <em>wraps</em> 
 * an array of function values. Because array values are referenced (not 
 * copied), the cost of wrapping any array with a {@link Real1} is small.
 * <p> 
 * One consequence of referencing the array of function values is that 
 * changes to elements in such an array are reflected in <em>all</em> 
 * {@link Real1}s that reference that array. One should be aware of this
 * behavior, and may use {@link Real1#Real1(Real1)} when it is unwanted.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.08.25
 */
public class Real1 {

  /**
   * Constructs a sequence with specified sampling and values zero.
   * @param t the sampling.
   */
  public Real1(Sampling t) {
    _t = t;
    _x = new float[t.getCount()];
  }

  /**
   * Constructs a sequence with specified values and default sampling.
   * The default sampling has number (count) of samples = x.length, sampling 
   * interval (delta) = 1.0 and first sample value (first) = 0.0.
   * @param x array of function values x(t); referenced, not copied.
   */
  public Real1(float[] x) {
    _t = new Sampling(x.length,1.0,0.0);
    _x = x;
  }

  /**
   * Constructs a sequence with specified sampling and values.
   * @param t the sampling.
   * @param x array of function values x(t); referenced, not copied.
   *  The array length x.length must equal the number of samples in t.
   */
  public Real1(Sampling t, float[] x) {
    Check.argument(t.getCount()==x.length,
      "x.length equals the number of samples in t");
    _t = t;
    _x = x;
  }

  /**
   * Constructs a sequence with specified sampling and values zero.
   * @param nt the number (count) of samples.
   * @param dt the sampling interval (delta).
   * @param ft the value t of the first sample.
   */
  public Real1(int nt, double dt, double ft) {
    this(new Sampling(nt,dt,ft));
  }

  /**
   * Constructs a sequence with specified sampling and values.
   * @param nt the number (count) of time samples.
   * @param dt the sampling interval (delta).
   * @param ft the value t of the first sample.
   * @param x array of function values x(t); referenced, not copied. 
   *  The array length x.length must equal nt.
   */
  public Real1(int nt, double dt, double ft, float[] x) {
    this(new Sampling(nt,dt,ft),x);
  }

  /**
   * Constructs a copy of the specified sequence. This constructor 
   * <em>copies</em> (does not reference) the array of function values 
   * from the specified sequence.
   * @param r the sequence to copy.
   */
  public Real1(Real1 r) {
    this(r._t,copy(r._x));
  }

  /**
   * Gets the sampling of t for this sequence.
   * @return the sampling t.
   */
  public Sampling getT() {
    return _t;
  }

  /**
   * Gets the array of function values x(t) for this sequence.
   * @return the array of function values; by reference, not by copy.
   */
  public float[] getX() {
    return _x;
  }

  /**
   * Returns this sequence resampled to have the specified sampling.
   * <p>
   * If the specified sampling is compatible with the sampling of this
   * sequence, this method copies the function values in the overlap 
   * between the two samplings, and assigns zero values to the function 
   * values where the two samplings do not overlap.
   * <p>
   * If the specified sampling is incompatible with the sampling of this
   * sequence, then this method must interpolate or decimate this sequence 
   * Neither interpolation or decimation is supported, yet.
   * @param t the sampling.
   * @return the resampled sequence.
   * @exception UnsupportedOperationException if the specified sampling is 
   *  incompatible with this sampling.
   */
  public Real1 resample(Sampling t) {

    // Overlap between this and that sampling.
    Sampling r = _t;
    Sampling s =  t;
    int[] overlap = r.overlapWith(s);

    // If samplings are compatible, ...
    if (overlap!=null) {
      int ni = overlap[0];
      int ir = overlap[1];
      int is = overlap[2];
      Real1 rr = this;
      Real1 rs = new Real1(s);
      float[] xr = rr.getX();
      float[] xs = rs.getX();
      while (--ni>=0)
        xs[is++] = xr[ir++];
      return rs;
    }

    // Sampling incompatible, so must interpolate or decimate.
    throw new UnsupportedOperationException("no interpolation, yet");
  }

  /**
   * Returns the sum this + ra of sequences this and ra.
   * The samplings of this and ra must be equivalent.
   * @param ra a sequence.
   * @return the sum.
   */
  public Real1 plus(Real1 ra) {
    return add(this,ra);
  }

  /**
   * Returns the sum this + ar of this sequence and constant ar.
   * @param ar a constant.
   * @return the sum.
   */
  public Real1 plus(float ar) {
    return add(this,ar);
  }

  /**
   * Convolves this sequence with the specified sequence. The two sequences 
   * must be uniformly sampled with equal sampling intervals.
   * @param ra the sequence with which to convolve.
   * @return the convolution sequence.
   */
  public Real1 convolve(Real1 ra) {
    Real1 rx = this;
    Real1 ry = ra;
    Sampling tx = rx.getT();
    Sampling ty = ry.getT();
    double dx = tx.getDelta();
    double dy = ty.getDelta();
    Check.state(tx.isUniform(),"sampling is uniform");
    Check.argument(ty.isUniform(),"sampling is uniform");
    Check.argument(dx==dy,"sampling intervals are equal");
    int lx = tx.getCount();
    int ly = ty.getCount();
    double fx = tx.getFirst();
    double fy = ty.getFirst();
    float[] x = rx.getX();
    float[] y = ry.getX();
    int lz = lx+ly-1;
    double dz = dx;
    double fz = fx+fy;
    float[] z = new float[lz];
    Conv.conv(lx,0,x,ly,0,y,lz,0,z);
    return new Real1(lz,dz,fz,z);
  }

  /**
   * Filters this sequence with the specified recursive filter.
   * Applies the filter in the forward direction. Makes the sampling of 
   * the filtered sequence equal to the the sampling of this sequence.
   * @param rf the recursive filter.
   * @return the filtered sequence.
   */
  public Real1 filterForward(RecursiveFilter rf) {
    float[] y = new float[_x.length];
    rf.applyForward(_x,y);
    return new Real1(_t,y);
  }

  /* *
   * Filters this sequence with the specified recursive filter.
   * Applies the filter in the reverse direction. Makes the sampling of 
   * the filtered sequence equal to the the sampling of this sequence.
   * @param rf the recursive filter.
   * @return the filtered sequence.
   */
  //public Real1 filterReverse(RecursiveFilter rf);

  /**
   * Gets frequency sampling corresponding to the sampling of this sequence.
   * The frequency of the first sample will be zero, because the Fourier
   * transform of a real sequence has conjugate-symmetry.
   * <p>
   * A minimum number of frequency samples must be specified, and the
   * returned frequency sampling will not be less than the larger of
   * that specified minimum and the number of samples in this sequence.
   * @param nmin the minimum number of frequency samples.
   * @return the frequency sampling.
   */
  public Sampling getFrequencySampling(int nmin) {
    int nt = _t.getCount();
    double dt = _t.getDelta();
    int nfft = FftReal.nfftSmall(max(nmin,nt));
    int nf = nfft/2+1;
    double df = 1.0/(nfft*dt);
    double ff = 0.0;
    return new Sampling(nf,df,ff);
  }

  /* *
   * Computes the Fourier transform of this sequence.
   * @param f the frequency sampling.
   * @return the Fourier transform.
   */
  //public Complex1 fourierTransform(Sampling f);

  ///////////////////////////////////////////////////////////////////////////
  // public static

  /**
   * Returns a sequence with value zero.
   * @param nt the number of samples.
   * @return the sequence.
   */
  public static Real1 zero(int nt) {
    return new Real1(new Sampling(nt));
  }

  /**
   * Returns a sequence with value zero.
   * @param t the sampling.
   * @return the sequence.
   */
  public static Real1 zero(Sampling t) {
    return new Real1(t);
  }

  /**
   * Returns a sequence with constant value.
   * @param ar the constant.
   * @param nt the number of samples.
   * @return the sequence.
   */
  public static Real1 fill(double ar, int nt) {
    return Real1.fill(ar,new Sampling(nt));
  }

  /**
   * Returns a sequence with constant value.
   * @param ar the constant.
   * @param t the sampling.
   * @return the sequence.
   */
  public static Real1 fill(double ar, Sampling t) {
    int nt = t.getCount();
    return new Real1(t,fillfloat((float)ar,nt));
  }

  /**
   * Returns a sampled linear (ramp) sequence.
   * The function values are fx+i*dx, for i in [0:nt).
   * @param fx the first function value.
   * @param dx the function value delta.
   * @param nt the number of samples
   * @return the sequence.
   */
  public static Real1 ramp(double fx, double dx, int nt) {
    return Real1.ramp(fx,dx,new Sampling(nt));
  }

  /**
   * Returns a sampled linear (ramp) sequence.
   * The function values are fx+(t-ft)*dx, 
   * for t = ft, ft+dt, ..., ft+(nt-1)*dt.
   * @param fx the first function value.
   * @param dx the function value delta.
   * @param t the sampling.
   * @return the sequence.
   */
  public static Real1 ramp(double fx, double dx, Sampling t) {
    int nt = t.getCount();
    double dt = t.getDelta();
    double ft = t.getFirst();
    return new Real1(t,rampfloat((float)(fx-ft*dx),(float)(dt*dx),nt));
  }

  /**
   * Returns the sum ra + rb of two sequences ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a sequence.
   * @param rb a sequence.
   * @return the sum.
   */
  public static Real1 add(Real1 ra, Real1 rb) {
    ensureSamplingEquivalent(ra,rb);
    return new Real1(ra._t,Array.add(ra._x,rb._x));
  }

  /**
   * Returns the sum ar + rb of constant ar and sequence rb.
   * @param ar a constant.
   * @param rb a sequence.
   * @return the sum.
   */
  public static Real1 add(float ar, Real1 rb) {
    return new Real1(rb._t,Array.add(ar,rb._x));
  }

  /**
   * Returns the sum ra + br of sequence ra and constant br.
   * @param ra a sequence.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 add(Real1 ra, float br) {
    return new Real1(ra._t,Array.add(ra._x,br));
  }

  /**
   * Returns the difference ra - rb of two sequences ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a sequence.
   * @param rb a sequence.
   * @return the difference.
   */
  public static Real1 sub(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_sub);
  }

  /**
   * Returns the difference ar - rb of constant ar and sequence rb.
   * @param ar a constant.
   * @param rb a sequence.
   * @return the sum.
   */
  public static Real1 sub(float ar, Real1 rb) {
    return binaryOp(ar,rb,_sub);
  }

  /**
   * Returns the difference ra - br of sequence ra and constant br.
   * @param ra a sequence.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 sub(Real1 ra, float br) {
    return binaryOp(ra,br,_sub);
  }

  /**
   * Returns the product ra * rb of two sequences ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a sequence.
   * @param rb a sequence.
   * @return the difference.
   */
  public static Real1 mul(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_mul);
  }

  /**
   * Returns the product ar * rb of constant ar and sequence rb.
   * @param ar a constant.
   * @param rb a sequence.
   * @return the sum.
   */
  public static Real1 mul(float ar, Real1 rb) {
    return binaryOp(ar,rb,_mul);
  }

  /**
   * Returns the product ra * br of sequence ra and constant br.
   * @param ra a sequence.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 mul(Real1 ra, float br) {
    return binaryOp(ra,br,_mul);
  }

  /**
   * Returns the quotient ra / rb of two sequences ra and rb.
   * The samplings of ra and rb must be equivalent.
   * @param ra a sequence.
   * @param rb a sequence.
   * @return the difference.
   */
  public static Real1 div(Real1 ra, Real1 rb) {
    return binaryOp(ra,rb,_div);
  }

  /**
   * Returns the quotient ar / rb of constant ar and sequence rb.
   * @param ar a constant.
   * @param rb a sequence.
   * @return the sum.
   */
  public static Real1 div(float ar, Real1 rb) {
    return binaryOp(ar,rb,_div);
  }

  /**
   * Returns the quotient ra / br of sequence ra and constant br.
   * @param ra a sequence.
   * @param br a constant.
   * @return the sum.
   */
  public static Real1 div(Real1 ra, float br) {
    return binaryOp(ra,br,_div);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  Sampling _t; // sampling of variable t
  float[] _x; // array of function values x(t)

  private static void ensureSamplingEquivalent(Real1 ra, Real1 rb) {
    ensureSamplingEquivalent(ra.getT(),rb.getT());
  }

  private static void ensureSamplingEquivalent(Sampling ta, Sampling tb) {
    Check.argument(ta.isEquivalentTo(tb),"samplings equivalent");
  }


  ///////////////////////////////////////////////////////////////////////////
  // Binary operations made generic to reduce code bloat.
  private static Real1 binaryOp(Real1 ra, Real1 rb, Binary ab) {
    Sampling ta = ra.getT();
    Sampling tb = rb.getT();
    Check.argument(ta.isEquivalentTo(tb),"samplings equivalent");
    Sampling tc = ta;
    Real1 rc = new Real1(tc);
    float[] xa = ra.getX();
    float[] xb = rb.getX();
    float[] xc = rc.getX();
    ab.apply(xc.length,xa,0,xb,0,xc,0);
    return rc;
  }
  private static Real1 binaryOp(float ar, Real1 rb, Binary ab) {
    Sampling tb = rb.getT();
    Sampling tc = tb;
    Real1 rc = new Real1(tc);
    float[] fb = rb.getX();
    float[] fc = rc.getX();
    ab.apply(fc.length,ar,fb,0,fc,0);
    return rc;
  }
  private static Real1 binaryOp(Real1 ra, float br, Binary ab) {
    Sampling ta = ra.getT();
    Sampling tc = ta;
    Real1 rc = new Real1(tc);
    float[] fa = ra.getX();
    float[] fc = rc.getX();
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
