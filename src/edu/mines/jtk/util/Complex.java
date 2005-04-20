/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * A complex number, with real and imaginary parts.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.04
 */
public class Complex {

  /**
   * The complex constant (0.0f,1.0f).
   */
  public static final Complex I = new Complex(0.0f,1.0f);

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
  public Complex() {
    this(0.0f,0.0f);
  }

  /**
   * Constructs a complex number with zero imaginary part.
   * @param r the real part.
   */
  public Complex(float r) {
    this(r,0.0f);
  }

  /**
   * Constructs a complex number.
   * @param r the real part.
   * @param i the imaginary part.
   */
  public Complex(float r, float i) {
    this.r = r;
    this.i = i;
  }

  /**
   * Constructs a copy of the specified complex number.
   * @param x the complex number.
   */
  public Complex(Complex x) {
    this(x.r,x.i);
  }

  /**
   * Returns true if this complex number is real (has zero imaginary part).
   * @return true, if real; false, otherwise.
   */
  public boolean isReal() {
    return i==0.0f;
  }

  /**
   * Returns true if this complex number is imaginary (has zero real part).
   * @return true, if imaginary; false, otherwise.
   */
  public boolean isImag() {
    return r==0.0f;
  }

  /**
   * Returns the sum z + x, where z is this complex number.
   * @param x a complex number.
   * @return z + x.
   */
  public Complex plus(Complex x) {
    return new Complex(this).plusEquals(x);
  }

  /**
   * Returns the difference z - x, where z is this complex number.
   * @param x a complex number.
   * @return z - x.
   */
  public Complex minus(Complex x) {
    return new Complex(this).minusEquals(x);
  }

  /**
   * Returns the product z * x, where z is this complex number.
   * @param x a complex number.
   * @return z * x.
   */
  public Complex times(Complex x) {
    return new Complex(this).timesEquals(x);
  }

  /**
   * Returns the quotent z / x, where z is this complex number.
   * @param x a complex number.
   * @return z / x.
   */
  public Complex over(Complex x) {
    return new Complex(this).overEquals(x);
  }

  /**
   * Returns the sum z + x, where z is this complex number.
   * @param x a real number.
   * @return z + x.
   */
  public Complex plus(float x) {
    return new Complex(this).plusEquals(x);
  }

  /**
   * Returns the difference z - x, where z is this complex number.
   * @param x a real number.
   * @return z - x.
   */
  public Complex minus(float x) {
    return new Complex(this).minusEquals(x);
  }

  /**
   * Returns the product z * x, where z is this complex number.
   * @param x a real number.
   * @return z * x.
   */
  public Complex times(float x) {
    return new Complex(this).timesEquals(x);
  }

  /**
   * Returns the quotent z / x, where z is this complex number.
   * @param x a real number.
   * @return z / x.
   */
  public Complex over(float x) {
    return new Complex(this).overEquals(x);
  }

  /**
   * Returns the complex conjugate of this complex number.
   * @return the complex conjugate.
   */
  public Complex conj() {
    return new Complex(r,-i);
  }

  /**
   * Returns the complex inverse of this complex number.
   * @return the complex inverse.
   */
  public Complex inv() {
    float d = norm();
    return new Complex(r/d,-i/d);
  }

  /**
   * Returns the sum z += x, where z is this complex number.
   * @param x a complex number.
   * @return z += x.
   */
  public Complex plusEquals(Complex x) {
    r += x.r;
    i += x.i;
    return this;
  }

  /**
   * Returns the difference z -= x, where z is this complex number.
   * @param x a complex number.
   * @return z -= x.
   */
  public Complex minusEquals(Complex x) {
    r -= x.r;
    i -= x.i;
    return this;
  }

  /**
   * Returns the product z *= x, where z is this complex number.
   * @param x a complex number.
   * @return z *= x.
   */
  public Complex timesEquals(Complex x) {
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
  public Complex overEquals(Complex x) {
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
  public Complex plusEquals(float x) {
    r += x;
    return this;
  }

  /**
   * Returns the difference z -= x, where z is this complex number.
   * @param x a real number.
   * @return z -= x.
   */
  public Complex minusEquals(float x) {
    r -= x;
    return this;
  }

  /**
   * Returns the product z *= x, where z is this complex number.
   * @param x a real number.
   * @return z *= x.
   */
  public Complex timesEquals(float x) {
    r *= x;
    i *= x;
    return this;
  }

  /**
   * Returns the quotient z /= x, where z is this complex number.
   * @param x a real number.
   * @return z /= x.
   */
  public Complex overEquals(float x) {
    r /= x;
    i /= x;
    return this;
  }

  /**
   * Returns the conjugate z = conj(z), where z is this complex number.
   * @return z = conj(z).
   */
  public Complex conjEquals() {
    i = -i;
    return this;
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
  public Complex sqrt() {
    return sqrt(this);
  }

  /**
   * Returns the exponential of this complex number.
   * @return the exponential.
   */
  public Complex exp() {
    return exp(this);
  }

  /**
   * Returns the natural logarithm of this complex number.
   * @return the natural logarithm.
   */
  public Complex log() {
    return log(this);
  }

  /**
   * Returns the logarithm base 10 of this complex number.
   * @return the logarithm base 10.
   */
  public Complex log10() {
    return log10(this);
  }

  /**
   * Returns z to the y'th power, where z is this complex number.
   * @param y a real number.
   * @return z to the y'th power.
   */
  public Complex pow(float y) {
    return pow(this,y);
  }

  /**
   * Returns z to the y'th power, where z is this complex number.
   * @param y a complex number.
   * @return z to the y'th power.
   */
  public Complex pow(Complex y) {
    return pow(this,y);
  }

  /**
   * Returns the sine of this complex number.
   * @return the sine.
   */
  public Complex sin() {
    return sin(this);
  }

  /**
   * Returns the cosine of this complex number.
   * @return the cosine.
   */
  public Complex cos() {
    return cos(this);
  }

  /**
   * Returns the tangent of this complex number.
   * @return the tangent.
   */
  public Complex tan() {
    return tan(this);
  }

  /**
   * Returns the hyberbolic sine of this complex number.
   * @return the hyberbolic sine.
   */
  public Complex sinh() {
    return sinh(this);
  }

  /**
   * Returns the hyberbolic cosine of this complex number.
   * @return the hyberbolic cosine.
   */
  public Complex cosh() {
    return cosh(this);
  }

  /**
   * Returns the hyberbolic tangent of this complex number.
   * @return the hyberbolic tangent.
   */
  public Complex tanh() {
    return tanh(this);
  }

  /**
   * Returns the conjugate of x.
   * @param x a complex number.
   * @return the conjugate.
   */
  public static Complex conj(Complex x) {
    return new Complex(x.r,-x.i);
  }

  /**
   * Returns the complex number (r*cos(a),r*sin(a)).
   * @param r the polar radius.
   * @param a the polar angle.
   * @return the complex number.
   */
  public static Complex polar(float r, float a) {
    return new Complex(r*cos(a),r*sin(a));
  }

  /**
   * Returns the sum x + y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the sum.
   */
  public static Complex add(Complex x, Complex y) {
    return x.plus(y);
  }

  /**
   * Returns the difference x - y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the difference.
   */
  public static Complex sub(Complex x, Complex y) {
    return x.minus(y);
  }

  /**
   * Returns the product x * y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the product.
   */
  public static Complex mul(Complex x, Complex y) {
    return x.times(y);
  }

  /**
   * Returns the quotient x * y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the quotient.
   */
  public static Complex div(Complex x, Complex y) {
    return x.over(y);
  }

  /**
   * Returns the magnitude of a complex number.
   * @param x a complex number.
   * @return the magnitude.
   */
  public static float abs(Complex x) {
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
  public static float arg(Complex x) {
    return atan2(x.i,x.r);
  }

  /**
   * Returns the norm of a complex number.
   * The norm is the sum of the squares of the real and imaginary parts.
   * @param x a complex number.
   * @return the norm.
   */
  public static float norm(Complex x) {
    return x.r*x.r+x.i*x.i;
  }

  /**
   * Returns the square root of a complex number.
   * @param x a complex number.
   * @return the square root.
   */
  public static Complex sqrt(Complex x) {
    if (x.r==0.0f) {
      float t = sqrt(0.5f*abs(x.i));
      return new Complex(t,(x.i<0.0f)?-t:t);
    } else {
      float t = sqrt(2.0f*(abs(x)+abs(x.r)));
      float u = 0.5f*t;
      return (x.r>0.0f) ? 
        new Complex(u,x.i/t) :
        new Complex(abs(x.i)/t,(x.i<0.0f)?-u:u);
    }
  }

  /**
   * Returns the exponential of a complex number.
   * @param x a complex number.
   * @return the exponential.
   */
  public static Complex exp(Complex x) {
    return polar(exp(x.r),x.i);
  }

  /**
   * Returns the natural logarithm of a complex number.
   * @param x a complex number.
   * @return the natural logarithm.
   */
  public static Complex log(Complex x) {
    return new Complex(log(abs(x)),arg(x));
  }

  /**
   * Returns the logarithm base 10 of a complex number.
   * @param x a complex number.
   * @return the logarithm base 10.
   */
  public static Complex log10(Complex x) {
    return log(x).overEquals(log(10.0f));
  }

  /**
   * Returns x to the y'th power.
   * @param x a complex number.
   * @param y a real number.
   * @return x to the y'th power.
   */
  public static Complex pow(Complex x, float y) {
    if (x.i==0.0f)
      return new Complex(pow(x.r,y));
    Complex t = log(x);
    return polar(exp(y*t.r),y*t.i);
  }

  /**
   * Returns x to the y'th power.
   * @param x a real number.
   * @param y a complex number.
   * @return x to the y'th power.
   */
  public static Complex pow(float x, Complex y) {
    if (x==0.0f)
      return new Complex();
    return polar(pow(x,y.r),y.i*log(x));
  }

  /**
   * Returns x to the y'th power.
   * @param x a complex number.
   * @param y a complex number.
   * @return x to the y'th power.
   */
  public static Complex pow(Complex x, Complex y) {
    if (x.r==0.0f && x.i==0.0f)
      return new Complex();
    return exp(y.times(log(x)));
  }

  /**
   * Returns the sine of a complex number.
   * @param x a complex number.
   * @return the sine.
   */
  public static Complex sin(Complex x) {
    return new Complex(sin(x.r)*cosh(x.i),cos(x.r)*sinh(x.i));
  }

  /**
   * Returns the cosine of a complex number.
   * @param x a complex number.
   * @return the cosine.
   */
  public static Complex cos(Complex x) {
    return new Complex(cos(x.r)*cosh(x.i),-sin(x.r)*sinh(x.i));
  }

  /**
   * Returns the tangent of a complex number.
   * @param x a complex number.
   * @return the tangent.
   */
  public static Complex tan(Complex x) {
    return sin(x).overEquals(cos(x));
  }

  /**
   * Returns the hyperbolic sine of a complex number.
   * @param x a complex number.
   * @return the hyperbolic sine.
   */
  public static Complex sinh(Complex x) {
    return new Complex(sinh(x.r)*cos(x.i),cosh(x.r)*sin(x.i));
  }

  /**
   * Returns the hyperbolic cosine of a complex number.
   * @param x a complex number.
   * @return the hyperbolic cosine.
   */
  public static Complex cosh(Complex x) {
    return new Complex(cosh(x.r)*cos(x.i),sinh(x.r)*sin(x.i));
  }

  /**
   * Returns the hyperbolic tangent of a complex number.
   * @param x a complex number.
   * @return the hyperbolic tangent.
   */
  public static Complex tanh(Complex x) {
    return sinh(x).overEquals(cosh(x));
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    Complex that = (Complex)obj;
    return this.r==that.r && this.i==that.i;
  }

  public int hashCode() {
    return (int)(Float.floatToIntBits(r)^Float.floatToIntBits(i));
  }

  public String toString() {
    if (i>=0.0f) {
      return "("+r+"+"+i+"i)";
    } else {
      return "("+r+"-"+(-i)+"i)";
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Complex(double r, double i) {
    this.r = (float)r;
    this.i = (float)i;
  }

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

  private static float log10(float x) {
    return (float)Math.log10(x);
  }

  private static float pow(float x, float y) {
    return (float)Math.pow(x,y);
  }

  private static float atan2(float y, float x) {
    return (float)Math.atan2(y,x);
  }
}
