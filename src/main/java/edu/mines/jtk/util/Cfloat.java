/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * A complex number, with single-precision real and imaginary parts.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.06
 */
public class Cfloat {

  /**
   * The complex constant (0.0f,1.0f).
   */
  public static final Cfloat FLT_I = new Cfloat(0.0f,1.0f);

  /**
   * The real part.
   */
  public float r;

  /**
   * The imaginary part.
   */
  public float i;

  /**
   * Constructs a complex number with zero real and imaginary parts.
   */
  public Cfloat() {
    this(0.0f,0.0f);
  }

  /**
   * Constructs a complex number with zero imaginary part.
   * @param r the real part.
   */
  public Cfloat(float r) {
    this(r,0.0f);
  }

  /**
   * Constructs a complex number.
   * @param r the real part.
   * @param i the imaginary part.
   */
  public Cfloat(float r, float i) {
    this.r = r;
    this.i = i;
  }

  /**
   * Constructs a copy of the specified complex number.
   * @param x the complex number.
   */
  public Cfloat(Cfloat x) {
    this(x.r,x.i);
  }

  /**
   * Returns the sum z + x, where z is this complex number.
   * @param x a complex number.
   * @return z + x.
   */
  public Cfloat plus(Cfloat x) {
    return new Cfloat(this).plusEquals(x);
  }

  /**
   * Returns the difference z - x, where z is this complex number.
   * @param x a complex number.
   * @return z - x.
   */
  public Cfloat minus(Cfloat x) {
    return new Cfloat(this).minusEquals(x);
  }

  /**
   * Returns the product z * x, where z is this complex number.
   * @param x a complex number.
   * @return z * x.
   */
  public Cfloat times(Cfloat x) {
    return new Cfloat(this).timesEquals(x);
  }

  /**
   * Returns the quotent z / x, where z is this complex number.
   * @param x a complex number.
   * @return z / x.
   */
  public Cfloat over(Cfloat x) {
    return new Cfloat(this).overEquals(x);
  }

  /**
   * Returns the sum z + x, where z is this complex number.
   * @param x a real number.
   * @return z + x.
   */
  public Cfloat plus(float x) {
    return new Cfloat(this).plusEquals(x);
  }

  /**
   * Returns the difference z - x, where z is this complex number.
   * @param x a real number.
   * @return z - x.
   */
  public Cfloat minus(float x) {
    return new Cfloat(this).minusEquals(x);
  }

  /**
   * Returns the product z * x, where z is this complex number.
   * @param x a real number.
   * @return z * x.
   */
  public Cfloat times(float x) {
    return new Cfloat(this).timesEquals(x);
  }

  /**
   * Returns the quotent z / x, where z is this complex number.
   * @param x a real number.
   * @return z / x.
   */
  public Cfloat over(float x) {
    return new Cfloat(this).overEquals(x);
  }

  /**
   * Returns the sum z += x, where z is this complex number.
   * @param x a complex number.
   * @return z += x.
   */
  public Cfloat plusEquals(Cfloat x) {
    r += x.r;
    i += x.i;
    return this;
  }

  /**
   * Returns the difference z -= x, where z is this complex number.
   * @param x a complex number.
   * @return z -= x.
   */
  public Cfloat minusEquals(Cfloat x) {
    r -= x.r;
    i -= x.i;
    return this;
  }

  /**
   * Returns the product z *= x, where z is this complex number.
   * @param x a complex number.
   * @return z *= x.
   */
  public Cfloat timesEquals(Cfloat x) {
    float tr = this.r;
    float ti = this.i;
    float xr = x.r;
    float xi = x.i;
    r = tr*xr-ti*xi;
    i = tr*xi+ti*xr;
    return this;
  }

  /**
   * Returns the quotient z /= x, where z is this complex number.
   * @param x a complex number.
   * @return z /= x.
   */
  public Cfloat overEquals(Cfloat x) {
    float tr = this.r;
    float ti = this.i;
    float xr = x.r;
    float xi = x.i;
    float d = norm(x);
    r = (tr*xr+ti*xi)/d;
    i = (ti*xr-tr*xi)/d;
    return this;
  }

  /**
   * Returns the sum z += x, where z is this complex number.
   * @param x a real number.
   * @return z += x.
   */
  public Cfloat plusEquals(float x) {
    r += x;
    return this;
  }

  /**
   * Returns the difference z -= x, where z is this complex number.
   * @param x a real number.
   * @return z -= x.
   */
  public Cfloat minusEquals(float x) {
    r -= x;
    return this;
  }

  /**
   * Returns the product z *= x, where z is this complex number.
   * @param x a real number.
   * @return z *= x.
   */
  public Cfloat timesEquals(float x) {
    r *= x;
    i *= x;
    return this;
  }

  /**
   * Returns the quotient z /= x, where z is this complex number.
   * @param x a real number.
   * @return z /= x.
   */
  public Cfloat overEquals(float x) {
    r /= x;
    i /= x;
    return this;
  }

  /**
   * Returns the conjugate z = conj(z), where z is this complex number.
   * @return z = conj(z).
   */
  public Cfloat conjEquals() {
    i = -i;
    return this;
  }

  /**
   * Returns the inverse z = inv(z), where z is this complex number.
   * @return z = inv(z).
   */
  public Cfloat invEquals() {
    float d = norm();
    r =  r/d;
    i = -i/d;
    return this;
  }

  /**
   * Returns the negative z = neg(z), where z is this complex number.
   * @return z = neg(z).
   */
  public Cfloat negEquals() {
    r = -r;
    i = -i;
    return this;
  }

  /**
   * Determines whether this complex number is real (has zero imaginary part).
   * @return true, if real; false, otherwise.
   */
  public boolean isReal() {
    return i==0.0f;
  }

  /**
   * Determines whether this complex number is imaginary (has zero real part).
   * @return true, if imaginary; false, otherwise.
   */
  public boolean isImag() {
    return r==0.0f;
  }

  /**
   * Returns the complex conjugate of this complex number.
   * @return the complex conjugate.
   */
  public Cfloat conj() {
    return new Cfloat(r,-i);
  }

  /**
   * Returns the complex inverse of this complex number.
   * @return the complex inverse.
   */
  public Cfloat inv() {
    float d = norm();
    return new Cfloat(r/d,-i/d);
  }

  /**
   * Returns the complex negative of this complex number.
   * @return the complex negative.
   */
  public Cfloat neg() {
    return new Cfloat(-r,-i);
  }

  /**
   * Returns the magnitude of this complex number.
   * @return the magnitude.
   */
  public float abs() {
    return abs(this);
  }

  /**
   * Returns the argument of this complex number.
   * @return the argument.
   */
  public float arg() {
    return arg(this);
  }

  /**
   * Returns the norm of this complex number.
   * The norm is the sum of the squares of the real and imaginary parts.
   * @return the norm.
   */
  public float norm() {
    return norm(this);
  }

  /**
   * Returns the square-root of this complex number.
   * @return the square-root.
   */
  public Cfloat sqrt() {
    return sqrt(this);
  }

  /**
   * Returns the exponential of this complex number.
   * @return the exponential.
   */
  public Cfloat exp() {
    return exp(this);
  }

  /**
   * Returns the natural logarithm of this complex number.
   * @return the natural logarithm.
   */
  public Cfloat log() {
    return log(this);
  }

  /**
   * Returns the logarithm base 10 of this complex number.
   * @return the logarithm base 10.
   */
  public Cfloat log10() {
    return log10(this);
  }

  /**
   * Returns z to the y'th power, where z is this complex number.
   * @param y a real number.
   * @return z to the y'th power.
   */
  public Cfloat pow(float y) {
    return pow(this,y);
  }

  /**
   * Returns z to the y'th power, where z is this complex number.
   * @param y a complex number.
   * @return z to the y'th power.
   */
  public Cfloat pow(Cfloat y) {
    return pow(this,y);
  }

  /**
   * Returns the sine of this complex number.
   * @return the sine.
   */
  public Cfloat sin() {
    return sin(this);
  }

  /**
   * Returns the cosine of this complex number.
   * @return the cosine.
   */
  public Cfloat cos() {
    return cos(this);
  }

  /**
   * Returns the tangent of this complex number.
   * @return the tangent.
   */
  public Cfloat tan() {
    return tan(this);
  }

  /**
   * Returns the hyberbolic sine of this complex number.
   * @return the hyberbolic sine.
   */
  public Cfloat sinh() {
    return sinh(this);
  }

  /**
   * Returns the hyberbolic cosine of this complex number.
   * @return the hyberbolic cosine.
   */
  public Cfloat cosh() {
    return cosh(this);
  }

  /**
   * Returns the hyberbolic tangent of this complex number.
   * @return the hyberbolic tangent.
   */
  public Cfloat tanh() {
    return tanh(this);
  }

  /**
   * Determines whether x is real (has zero imaginary part).
   * @param x a complex number.
   * @return true, if real; false, otherwise.
   */
  public static boolean isReal(Cfloat x) {
    return x.i==0.0f;
  }

  /**
   * Determines whether x is imaginary (has zero real part).
   * @param x a complex number.
   * @return true, if imaginary; false, otherwise.
   */
  public static boolean isImag(Cfloat x) {
    return x.r==0.0f;
  }

  /**
   * Returns the conjugate of x.
   * @param x a complex number.
   * @return the conjugate.
   */
  public static Cfloat conj(Cfloat x) {
    return new Cfloat(x.r,-x.i);
  }

  /**
   * Returns the inverse of x.
   * @param x a complex number.
   * @return the complex inverse.
   */
  public static Cfloat inv(Cfloat x) {
    float d = x.norm();
    return new Cfloat(x.r/d,-x.i/d);
  }

  /**
   * Returns the negative of x.
   * @param x a complex number.
   * @return the negative.
   */
  public static Cfloat neg(Cfloat x) {
    return new Cfloat(-x.r,-x.i);
  }

  /**
   * Returns the complex number (r*cos(a),r*sin(a)).
   * @param r the polar radius.
   * @param a the polar angle.
   * @return the complex number.
   */
  public static Cfloat polar(float r, float a) {
    return new Cfloat(r*cos(a),r*sin(a));
  }

  /**
   * Returns the sum x + y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the sum.
   */
  public static Cfloat add(Cfloat x, Cfloat y) {
    return x.plus(y);
  }

  /**
   * Returns the difference x - y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the difference.
   */
  public static Cfloat sub(Cfloat x, Cfloat y) {
    return x.minus(y);
  }

  /**
   * Returns the product x * y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the product.
   */
  public static Cfloat mul(Cfloat x, Cfloat y) {
    return x.times(y);
  }

  /**
   * Returns the quotient x * y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the quotient.
   */
  public static Cfloat div(Cfloat x, Cfloat y) {
    return x.over(y);
  }

  /**
   * Returns the magnitude of a complex number.
   * @param x a complex number.
   * @return the magnitude.
   */
  public static float abs(Cfloat x) {
    float ar = abs(x.r);
    float ai = abs(x.i);
    float s = max(abs(ar),abs(ai));
    if (s==0.0f)
      return 0.0f;
    ar /= s;
    ai /= s;
    return s*sqrt(ar*ar+ai*ai);
  }

  /**
   * Returns the argument of a complex number.
   * @param x a complex number.
   * @return the argument.
   */
  public static float arg(Cfloat x) {
    return atan2(x.i,x.r);
  }

  /**
   * Returns the norm of a complex number.
   * The norm is the sum of the squares of the real and imaginary parts.
   * @param x a complex number.
   * @return the norm.
   */
  public static float norm(Cfloat x) {
    return x.r*x.r+x.i*x.i;
  }

  /**
   * Returns the square root of a complex number.
   * @param x a complex number.
   * @return the square root.
   */
  public static Cfloat sqrt(Cfloat x) {
    if (x.r==0.0f) {
      float t = sqrt(0.5f*abs(x.i));
      return new Cfloat(t,(x.i<0.0f)?-t:t);
    } else {
      float t = sqrt(2.0f*(abs(x)+abs(x.r)));
      float u = 0.5f*t;
      return (x.r>0.0f) ? 
        new Cfloat(u,x.i/t) :
        new Cfloat(abs(x.i)/t,(x.i<0.0f)?-u:u);
    }
  }

  /**
   * Returns the exponential of a complex number.
   * @param x a complex number.
   * @return the exponential.
   */
  public static Cfloat exp(Cfloat x) {
    return polar(exp(x.r),x.i);
  }

  /**
   * Returns the natural logarithm of a complex number.
   * @param x a complex number.
   * @return the natural logarithm.
   */
  public static Cfloat log(Cfloat x) {
    return new Cfloat(log(abs(x)),arg(x));
  }

  /**
   * Returns the logarithm base 10 of a complex number.
   * @param x a complex number.
   * @return the logarithm base 10.
   */
  public static Cfloat log10(Cfloat x) {
    return log(x).overEquals(log(10.0f));
  }

  /**
   * Returns x to the y'th power.
   * @param x a complex number.
   * @param y a real number.
   * @return x to the y'th power.
   */
  public static Cfloat pow(Cfloat x, float y) {
    if (x.i==0.0f)
      return new Cfloat(pow(x.r,y));
    Cfloat t = log(x);
    return polar(exp(y*t.r),y*t.i);
  }

  /**
   * Returns x to the y'th power.
   * @param x a real number.
   * @param y a complex number.
   * @return x to the y'th power.
   */
  public static Cfloat pow(float x, Cfloat y) {
    if (x==0.0f)
      return new Cfloat();
    return polar(pow(x,y.r),y.i*log(x));
  }

  /**
   * Returns x to the y'th power.
   * @param x a complex number.
   * @param y a complex number.
   * @return x to the y'th power.
   */
  public static Cfloat pow(Cfloat x, Cfloat y) {
    if (x.r==0.0f && x.i==0.0f)
      return new Cfloat();
    return exp(y.times(log(x)));
  }

  /**
   * Returns the sine of a complex number.
   * @param x a complex number.
   * @return the sine.
   */
  public static Cfloat sin(Cfloat x) {
    return new Cfloat(sin(x.r)*cosh(x.i),cos(x.r)*sinh(x.i));
  }

  /**
   * Returns the cosine of a complex number.
   * @param x a complex number.
   * @return the cosine.
   */
  public static Cfloat cos(Cfloat x) {
    return new Cfloat(cos(x.r)*cosh(x.i),-sin(x.r)*sinh(x.i));
  }

  /**
   * Returns the tangent of a complex number.
   * @param x a complex number.
   * @return the tangent.
   */
  public static Cfloat tan(Cfloat x) {
    return sin(x).overEquals(cos(x));
  }

  /**
   * Returns the hyperbolic sine of a complex number.
   * @param x a complex number.
   * @return the hyperbolic sine.
   */
  public static Cfloat sinh(Cfloat x) {
    return new Cfloat(sinh(x.r)*cos(x.i),cosh(x.r)*sin(x.i));
  }

  /**
   * Returns the hyperbolic cosine of a complex number.
   * @param x a complex number.
   * @return the hyperbolic cosine.
   */
  public static Cfloat cosh(Cfloat x) {
    return new Cfloat(cosh(x.r)*cos(x.i),sinh(x.r)*sin(x.i));
  }

  /**
   * Returns the hyperbolic tangent of a complex number.
   * @param x a complex number.
   * @return the hyperbolic tangent.
   */
  public static Cfloat tanh(Cfloat x) {
    return sinh(x).overEquals(cosh(x));
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    Cfloat that = (Cfloat)obj;
    return this.r==that.r && this.i==that.i;
  }

  public int hashCode() {
    return Float.floatToIntBits(r)^Float.floatToIntBits(i);
  }

  public String toString() {
    if (i==0.0f) {
      return "("+r+"+0.0i)";
    } else if (i>0.0f) {
      return "("+r+"+"+i+"i)";
    } else {
      return "("+r+"-"+(-i)+"i)";
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static float max(float x, float y) {
    return (x>=y)?x:y;
  }

  private static float abs(float x) {
    return (x>=0.0f)?x:-x;
  }

  private static float sqrt(float x) {
    return (float)Math.sqrt(x);
  }

  private static float sin(float x) {
    return (float)Math.sin(x);
  }

  private static float cos(float x) {
    return (float)Math.cos(x);
  }

  private static float sinh(float x) {
    return (float)Math.sinh(x);
  }

  private static float cosh(float x) {
    return (float)Math.cosh(x);
  }

  private static float exp(float x) {
    return (float)Math.exp(x);
  }

  private static float log(float x) {
    return (float)Math.log(x);
  }

  /*
  private static float log10(float x) {
    return (float)Math.log10(x);
  }
  */

  private static float pow(float x, float y) {
    return (float)Math.pow(x,y);
  }

  private static float atan2(float y, float x) {
    return (float)Math.atan2(y,x);
  }
}
