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
package edu.mines.jtk.util;

/**
 * A complex number, with double-precision real and imaginary parts.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.06
 */
public class Cdouble {

  /**
   * The complex constant (0.0,1.0).
   */
  public static final Cdouble DBL_I = new Cdouble(0.0,1.0);

  /**
   * The real part.
   */
  public double r;

  /**
   * The imaginary part.
   */
  public double i;

  /**
   * Constructs a complex number with zero real and imaginary parts.
   */
  public Cdouble() {
    this(0.0,0.0);
  }

  /**
   * Constructs a complex number with zero imaginary part.
   * @param r the real part.
   */
  public Cdouble(double r) {
    this(r,0.0);
  }

  /**
   * Constructs a complex number.
   * @param r the real part.
   * @param i the imaginary part.
   */
  public Cdouble(double r, double i) {
    this.r = r;
    this.i = i;
  }

  /**
   * Constructs a copy of the specified complex number.
   * @param x the complex number.
   */
  public Cdouble(Cdouble x) {
    this(x.r,x.i);
  }

  /**
   * Returns the sum z + x, where z is this complex number.
   * @param x a complex number.
   * @return z + x.
   */
  public Cdouble plus(Cdouble x) {
    return new Cdouble(this).plusEquals(x);
  }

  /**
   * Returns the difference z - x, where z is this complex number.
   * @param x a complex number.
   * @return z - x.
   */
  public Cdouble minus(Cdouble x) {
    return new Cdouble(this).minusEquals(x);
  }

  /**
   * Returns the product z * x, where z is this complex number.
   * @param x a complex number.
   * @return z * x.
   */
  public Cdouble times(Cdouble x) {
    return new Cdouble(this).timesEquals(x);
  }

  /**
   * Returns the quotent z / x, where z is this complex number.
   * @param x a complex number.
   * @return z / x.
   */
  public Cdouble over(Cdouble x) {
    return new Cdouble(this).overEquals(x);
  }

  /**
   * Returns the sum z + x, where z is this complex number.
   * @param x a real number.
   * @return z + x.
   */
  public Cdouble plus(double x) {
    return new Cdouble(this).plusEquals(x);
  }

  /**
   * Returns the difference z - x, where z is this complex number.
   * @param x a real number.
   * @return z - x.
   */
  public Cdouble minus(double x) {
    return new Cdouble(this).minusEquals(x);
  }

  /**
   * Returns the product z * x, where z is this complex number.
   * @param x a real number.
   * @return z * x.
   */
  public Cdouble times(double x) {
    return new Cdouble(this).timesEquals(x);
  }

  /**
   * Returns the quotent z / x, where z is this complex number.
   * @param x a real number.
   * @return z / x.
   */
  public Cdouble over(double x) {
    return new Cdouble(this).overEquals(x);
  }

  /**
   * Returns the sum z += x, where z is this complex number.
   * @param x a complex number.
   * @return z += x.
   */
  public Cdouble plusEquals(Cdouble x) {
    r += x.r;
    i += x.i;
    return this;
  }

  /**
   * Returns the difference z -= x, where z is this complex number.
   * @param x a complex number.
   * @return z -= x.
   */
  public Cdouble minusEquals(Cdouble x) {
    r -= x.r;
    i -= x.i;
    return this;
  }

  /**
   * Returns the product z *= x, where z is this complex number.
   * @param x a complex number.
   * @return z *= x.
   */
  public Cdouble timesEquals(Cdouble x) {
    double tr = this.r;
    double ti = this.i;
    double xr = x.r;
    double xi = x.i;
    r = tr*xr-ti*xi;
    i = tr*xi+ti*xr;
    return this;
  }

  /**
   * Returns the quotient z /= x, where z is this complex number.
   * @param x a complex number.
   * @return z /= x.
   */
  public Cdouble overEquals(Cdouble x) {
    double tr = this.r;
    double ti = this.i;
    double xr = x.r;
    double xi = x.i;
    double d = norm(x);
    r = (tr*xr+ti*xi)/d;
    i = (ti*xr-tr*xi)/d;
    return this;
  }

  /**
   * Returns the sum z += x, where z is this complex number.
   * @param x a real number.
   * @return z += x.
   */
  public Cdouble plusEquals(double x) {
    r += x;
    return this;
  }

  /**
   * Returns the difference z -= x, where z is this complex number.
   * @param x a real number.
   * @return z -= x.
   */
  public Cdouble minusEquals(double x) {
    r -= x;
    return this;
  }

  /**
   * Returns the product z *= x, where z is this complex number.
   * @param x a real number.
   * @return z *= x.
   */
  public Cdouble timesEquals(double x) {
    r *= x;
    i *= x;
    return this;
  }

  /**
   * Returns the quotient z /= x, where z is this complex number.
   * @param x a real number.
   * @return z /= x.
   */
  public Cdouble overEquals(double x) {
    r /= x;
    i /= x;
    return this;
  }

  /**
   * Returns the conjugate z = conj(z), where z is this complex number.
   * @return z = conj(z).
   */
  public Cdouble conjEquals() {
    i = -i;
    return this;
  }

  /**
   * Returns the inverse z = inv(z), where z is this complex number.
   * @return z = inv(z).
   */
  public Cdouble invEquals() {
    double d = norm();
    r =  r/d;
    i = -i/d;
    return this;
  }

  /**
   * Returns the negative z = neg(z), where z is this complex number.
   * @return z = neg(z).
   */
  public Cdouble negEquals() {
    r = -r;
    i = -i;
    return this;
  }

  /**
   * Determines whether this complex number is real (has zero imaginary part).
   * @return true, if real; false, otherwise.
   */
  public boolean isReal() {
    return i==0.0;
  }

  /**
   * Determines whether this complex number is imaginary (has zero real part).
   * @return true, if imaginary; false, otherwise.
   */
  public boolean isImag() {
    return r==0.0;
  }

  /**
   * Returns the complex conjugate of this complex number.
   * @return the complex conjugate.
   */
  public Cdouble conj() {
    return new Cdouble(r,-i);
  }

  /**
   * Returns the complex inverse of this complex number.
   * @return the complex inverse.
   */
  public Cdouble inv() {
    double d = norm();
    return new Cdouble(r/d,-i/d);
  }

  /**
   * Returns the complex negative of this complex number.
   * @return the complex negative.
   */
  public Cdouble neg() {
    return new Cdouble(-r,-i);
  }

  /**
   * Returns the magnitude of this complex number.
   * @return the magnitude.
   */
  public double abs() {
    return abs(this);
  }

  /**
   * Returns the argument of this complex number.
   * @return the argument.
   */
  public double arg() {
    return arg(this);
  }

  /**
   * Returns the norm of this complex number.
   * The norm is the sum of the squares of the real and imaginary parts.
   * @return the norm.
   */
  public double norm() {
    return norm(this);
  }

  /**
   * Returns the square-root of this complex number.
   * @return the square-root.
   */
  public Cdouble sqrt() {
    return sqrt(this);
  }

  /**
   * Returns the exponential of this complex number.
   * @return the exponential.
   */
  public Cdouble exp() {
    return exp(this);
  }

  /**
   * Returns the natural logarithm of this complex number.
   * @return the natural logarithm.
   */
  public Cdouble log() {
    return log(this);
  }

  /**
   * Returns the logarithm base 10 of this complex number.
   * @return the logarithm base 10.
   */
  public Cdouble log10() {
    return log10(this);
  }

  /**
   * Returns z to the y'th power, where z is this complex number.
   * @param y a real number.
   * @return z to the y'th power.
   */
  public Cdouble pow(double y) {
    return pow(this,y);
  }

  /**
   * Returns z to the y'th power, where z is this complex number.
   * @param y a complex number.
   * @return z to the y'th power.
   */
  public Cdouble pow(Cdouble y) {
    return pow(this,y);
  }

  /**
   * Returns the sine of this complex number.
   * @return the sine.
   */
  public Cdouble sin() {
    return sin(this);
  }

  /**
   * Returns the cosine of this complex number.
   * @return the cosine.
   */
  public Cdouble cos() {
    return cos(this);
  }

  /**
   * Returns the tangent of this complex number.
   * @return the tangent.
   */
  public Cdouble tan() {
    return tan(this);
  }

  /**
   * Returns the hyberbolic sine of this complex number.
   * @return the hyberbolic sine.
   */
  public Cdouble sinh() {
    return sinh(this);
  }

  /**
   * Returns the hyberbolic cosine of this complex number.
   * @return the hyberbolic cosine.
   */
  public Cdouble cosh() {
    return cosh(this);
  }

  /**
   * Returns the hyberbolic tangent of this complex number.
   * @return the hyberbolic tangent.
   */
  public Cdouble tanh() {
    return tanh(this);
  }

  /**
   * Determines whether x is real (has zero imaginary part).
   * @param x a complex number.
   * @return true, if real; false, otherwise.
   */
  public static boolean isReal(Cdouble x) {
    return x.i==0.0;
  }

  /**
   * Determines whether x is imaginary (has zero real part).
   * @param x a complex number.
   * @return true, if imaginary; false, otherwise.
   */
  public static boolean isImag(Cdouble x) {
    return x.r==0.0;
  }

  /**
   * Returns the conjugate of x.
   * @param x a complex number.
   * @return the conjugate.
   */
  public static Cdouble conj(Cdouble x) {
    return new Cdouble(x.r,-x.i);
  }

  /**
   * Returns the inverse of x.
   * @param x a complex number.
   * @return the complex inverse.
   */
  public static Cdouble inv(Cdouble x) {
    double d = x.norm();
    return new Cdouble(x.r/d,-x.i/d);
  }

  /**
   * Returns the negative of x.
   * @param x a complex number.
   * @return the negative.
   */
  public static Cdouble neg(Cdouble x) {
    return new Cdouble(-x.r,-x.i);
  }

  /**
   * Returns the complex number (r*cos(a),r*sin(a)).
   * @param r the polar radius.
   * @param a the polar angle.
   * @return the complex number.
   */
  public static Cdouble polar(double r, double a) {
    return new Cdouble(r*cos(a),r*sin(a));
  }

  /**
   * Returns the sum x + y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the sum.
   */
  public static Cdouble add(Cdouble x, Cdouble y) {
    return x.plus(y);
  }

  /**
   * Returns the difference x - y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the difference.
   */
  public static Cdouble sub(Cdouble x, Cdouble y) {
    return x.minus(y);
  }

  /**
   * Returns the product x * y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the product.
   */
  public static Cdouble mul(Cdouble x, Cdouble y) {
    return x.times(y);
  }

  /**
   * Returns the quotient x * y.
   * @param x a complex number.
   * @param y a complex number.
   * @return the quotient.
   */
  public static Cdouble div(Cdouble x, Cdouble y) {
    return x.over(y);
  }

  /**
   * Returns the magnitude of a complex number.
   * @param x a complex number.
   * @return the magnitude.
   */
  public static double abs(Cdouble x) {
    double ar = abs(x.r);
    double ai = abs(x.i);
    double s = max(abs(ar),abs(ai));
    if (s==0.0)
      return 0.0;
    ar /= s;
    ai /= s;
    return s*sqrt(ar*ar+ai*ai);
  }

  /**
   * Returns the argument of a complex number.
   * @param x a complex number.
   * @return the argument.
   */
  public static double arg(Cdouble x) {
    return atan2(x.i,x.r);
  }

  /**
   * Returns the norm of a complex number.
   * The norm is the sum of the squares of the real and imaginary parts.
   * @param x a complex number.
   * @return the norm.
   */
  public static double norm(Cdouble x) {
    return x.r*x.r+x.i*x.i;
  }

  /**
   * Returns the square root of a complex number.
   * @param x a complex number.
   * @return the square root.
   */
  public static Cdouble sqrt(Cdouble x) {
    if (x.r==0.0) {
      double t = sqrt(0.5*abs(x.i));
      return new Cdouble(t,(x.i<0.0)?-t:t);
    } else {
      double t = sqrt(2.0*(abs(x)+abs(x.r)));
      double u = 0.5*t;
      return (x.r>0.0) ? 
        new Cdouble(u,x.i/t) :
        new Cdouble(abs(x.i)/t,(x.i<0.0)?-u:u);
    }
  }

  /**
   * Returns the exponential of a complex number.
   * @param x a complex number.
   * @return the exponential.
   */
  public static Cdouble exp(Cdouble x) {
    return polar(exp(x.r),x.i);
  }

  /**
   * Returns the natural logarithm of a complex number.
   * @param x a complex number.
   * @return the natural logarithm.
   */
  public static Cdouble log(Cdouble x) {
    return new Cdouble(log(abs(x)),arg(x));
  }

  /**
   * Returns the logarithm base 10 of a complex number.
   * @param x a complex number.
   * @return the logarithm base 10.
   */
  public static Cdouble log10(Cdouble x) {
    return log(x).overEquals(log(10.0));
  }

  /**
   * Returns x to the y'th power.
   * @param x a complex number.
   * @param y a real number.
   * @return x to the y'th power.
   */
  public static Cdouble pow(Cdouble x, double y) {
    if (x.i==0.0)
      return new Cdouble(pow(x.r,y));
    Cdouble t = log(x);
    return polar(exp(y*t.r),y*t.i);
  }

  /**
   * Returns x to the y'th power.
   * @param x a real number.
   * @param y a complex number.
   * @return x to the y'th power.
   */
  public static Cdouble pow(double x, Cdouble y) {
    if (x==0.0)
      return new Cdouble();
    return polar(pow(x,y.r),y.i*log(x));
  }

  /**
   * Returns x to the y'th power.
   * @param x a complex number.
   * @param y a complex number.
   * @return x to the y'th power.
   */
  public static Cdouble pow(Cdouble x, Cdouble y) {
    if (x.r==0.0 && x.i==0.0)
      return new Cdouble();
    return exp(y.times(log(x)));
  }

  /**
   * Returns the sine of a complex number.
   * @param x a complex number.
   * @return the sine.
   */
  public static Cdouble sin(Cdouble x) {
    return new Cdouble(sin(x.r)*cosh(x.i),cos(x.r)*sinh(x.i));
  }

  /**
   * Returns the cosine of a complex number.
   * @param x a complex number.
   * @return the cosine.
   */
  public static Cdouble cos(Cdouble x) {
    return new Cdouble(cos(x.r)*cosh(x.i),-sin(x.r)*sinh(x.i));
  }

  /**
   * Returns the tangent of a complex number.
   * @param x a complex number.
   * @return the tangent.
   */
  public static Cdouble tan(Cdouble x) {
    return sin(x).overEquals(cos(x));
  }

  /**
   * Returns the hyperbolic sine of a complex number.
   * @param x a complex number.
   * @return the hyperbolic sine.
   */
  public static Cdouble sinh(Cdouble x) {
    return new Cdouble(sinh(x.r)*cos(x.i),cosh(x.r)*sin(x.i));
  }

  /**
   * Returns the hyperbolic cosine of a complex number.
   * @param x a complex number.
   * @return the hyperbolic cosine.
   */
  public static Cdouble cosh(Cdouble x) {
    return new Cdouble(cosh(x.r)*cos(x.i),sinh(x.r)*sin(x.i));
  }

  /**
   * Returns the hyperbolic tangent of a complex number.
   * @param x a complex number.
   * @return the hyperbolic tangent.
   */
  public static Cdouble tanh(Cdouble x) {
    return sinh(x).overEquals(cosh(x));
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    Cdouble that = (Cdouble)obj;
    return this.r==that.r && this.i==that.i;
  }

  public int hashCode() {
    long rbits = Double.doubleToLongBits(r);
    long ibits = Double.doubleToLongBits(i);
    return (int)(rbits^(rbits>>>32)^ibits^(ibits>>>32));
  }

  public String toString() {
    if (i==0.0) {
      return "("+r+"+0.0i)";
    } else if (i>0.0) {
      return "("+r+"+"+i+"i)";
    } else {
      return "("+r+"-"+(-i)+"i)";
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static double max(double x, double y) {
    return (x>=y)?x:y;
  }

  private static double abs(double x) {
    return (x>=0.0)?x:-x;
  }

  private static double sqrt(double x) {
    return Math.sqrt(x);
  }

  private static double sin(double x) {
    return Math.sin(x);
  }

  private static double cos(double x) {
    return Math.cos(x);
  }

  private static double sinh(double x) {
    return Math.sinh(x);
  }

  private static double cosh(double x) {
    return Math.cosh(x);
  }

  private static double exp(double x) {
    return Math.exp(x);
  }

  private static double log(double x) {
    return Math.log(x);
  }

  /*
  private static double log10(double x) {
    return Math.log10(x);
  }
  */

  private static double pow(double x, double y) {
    return Math.pow(x,y);
  }

  private static double atan2(double y, double x) {
    return Math.atan2(y,x);
  }
}
