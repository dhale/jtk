/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * Basic math functions. Like the standard class {@link java.lang.Math}, but
 * with overloaded methods that return floats when passed float arguments. 
 * (This eliminates ugly casts when using floats.) This class also defines
 * useful additional constants, such as {@link #FLT_PI}.
 * @see java.lang.Math
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.04
 */
public class MathPlus {

  /**
   * The double value that is closer than any other to <i>e</i>,
   * the base of the natural logarithm.
   */
  public static final double E = Math.E;

  /**
   * The float value that is closer than any other to <i>e</i>,
   * the base of the natural logarithm.
   */
  public static final float FLT_E = (float)E;

  /**
   * The double value that is closer than any other to <i>e</i>,
   * the base of the natural logarithm.
   */
  public static final double DBL_E = E;

  /**
   * The double value that is closer than any other to <i>pi</i>, 
   * the ratio of the circumference of a circle to its diameter.
   */
  public static final double PI = Math.PI;

  /**
   * The float value that is closer than any other to <i>pi</i>, 
   * the ratio of the circumference of a circle to its diameter.
   */
  public static final float FLT_PI = (float)PI;

  /**
   * The double value that is closer than any other to <i>pi</i>, 
   * the ratio of the circumference of a circle to its diameter.
   */
  public static final double DBL_PI = PI;

  /**
   * The maximum positive float value.
   */
  public static final float FLT_MAX = Float.MAX_VALUE;

  /**
   * The minimum positive float value.
   */
  public static final float FLT_MIN = Float.MIN_VALUE;

  /**
   * The smallest float value e such that (1+e) does not equal 1.
   */
  public static final float FLT_EPSILON = 1.19209290e-07f;

  /**
   * The maximum positive double value.
   */
  public static final double DBL_MAX = Double.MAX_VALUE;

  /**
   * The minimum positive double value.
   */
  public static final double DBL_MIN = Double.MIN_VALUE;

  /**
   * The smallest double value e such that (1+e) does not equal 1.
   */
  public static final double DBL_EPSILON = 2.2204460492503131e-16d;

  /**
   * Returns the trigonometric sine of an angle.
   * @param x the angle, in radians.
   * @return the sine of the argument.
   */
  public static float sin(float x) {
    return (float)Math.sin(x);
  }

  /**
   * Returns the trigonometric sine of an angle.
   * @param x the angle, in radians.
   * @return the sine of the argument.
   */
  public static double sin(double x) {
    return Math.sin(x);
  }

  /**
   * Returns the trigonometric cosine of an angle.
   * @param x the angle, in radians.
   * @return the cosine of the argument.
   */
  public static float cos(float x) {
    return (float)Math.cos(x);
  }

  /**
   * Returns the trigonometric cosine of an angle.
   * @param x the angle, in radians.
   * @return the cosine of the argument.
   */
  public static double cos(double x) {
    return Math.cos(x);
  }

  /**
   * Returns the trigonometric tangent of an angle.
   * @param x the angle, in radians.
   * @return the tangent of the argument.
   */
  public static float tan(float x) {
    return (float)Math.tan(x);
  }

  /**
   * Returns the trigonometric tangent of an angle.
   * @param x the angle, in radians.
   * @return the tangent of the argument.
   */
  public static double tan(double x) {
    return Math.tan(x);
  }

  /**
   * Returns the arc sine of the specified value, in the range
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * @param x the value.
   * @return the arc sine.
   */
  public static float asin(float x) {
    return (float)Math.asin(x);
  }

  /**
   * Returns the arc sine of the specified value, in the range
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * @param x the value.
   * @return the arc sine.
   */
  public static double asin(double x) {
    return Math.asin(x);
  }

  /**
   * Returns the arc cosine of the specified value, in the range 
   * 0.0 through <i>pi</i>.
   * @param x the value.
   * @return the arc cosine.
   */
  public static float acos(float x) {
    return (float)Math.acos(x);
  }

  /**
   * Returns the arc cosine of the specified value, in the range 
   * 0.0 through <i>pi</i>.
   * @param x the value.
   * @return the arc cosine.
   */
  public static double acos(double x) {
    return Math.acos(x);
  }

  /**
   * Returns the arc tangent of the specified value, in the range 
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * @param x the value.
   * @return the arc tangent.
   */
  public static float atan(float x) {
    return (float)Math.atan(x);
  }

  /**
   * Returns the arc tangent of the specified value, in the range 
   * -<i>pi</i>/2 through <i>pi</i>/2.
   * @param x the value.
   * @return the arc tangent.
   */
  public static double atan(double x) {
    return Math.atan(x);
  }

  /**
   * Computes the arc tangent of the specified y/x, in the range 
   * -<i>pi</i> to <i>pi</i>.
   * @param y the ordinate coordinate y.
   * @param x the abscissa coordinate x.
   * @return the arc tangent.
   */
  public static float atan2(float y, float x) {
    return (float)Math.atan2(y,x);
  }

  /**
   * Computes the arc tangent of the specified y/x, in the range 
   * -<i>pi</i> to <i>pi</i>.
   * @param y the ordinate coordinate y.
   * @param x the abscissa coordinate x.
   * @return the arc tangent.
   */
  public static double atan2(double y, double x) {
    return Math.atan2(y,x);
  }

  /**
   * Converts an angle measured in degrees to radians.
   * @param angdeg an angle, in degrees.
   * @return the angle in radians.
   */
  public static float toRadians(float angdeg) {
    return (float)Math.toRadians(angdeg);
  }

  /**
   * Converts an angle measured in degrees to radians.
   * @param angdeg an angle, in degrees.
   * @return the angle in radians.
   */
  public static double toRadians(double angdeg) {
    return Math.toRadians(angdeg);
  }

  /**
   * Converts an angle measured in radians to degrees.
   * @param angrad an angle, in radians.
   * @return the angle in degrees.
   */
  public static float toDegrees(float angrad) {
    return (float)Math.toDegrees(angrad);
  }

  /**
   * Converts an angle measured in radians to degrees.
   * @param angrad an angle, in radians.
   * @return the angle in degrees.
   */
  public static double toDegrees(double angrad) {
    return Math.toDegrees(angrad);
  }

  /**
   * Returns the value of <i>e</i> raised to the specified power.
   * @param x the exponent.
   * @return the value.
   */
  public static float exp(float x) {
    return (float)Math.exp(x);
  }

  /**
   * Returns the value of <i>e</i> raised to the specified power.
   * @param x the exponent.
   * @return the value.
   */
  public static double exp(double x) {
    return Math.exp(x);
  }

  /**
   * Returns the natural logarithm (base <i>e</i>) of the specified value.
   * @param x the value.
   * @return the natural logarithm.
   */
  public static float log(float x) {
    return (float)Math.log(x);
  }

  /**
   * Returns the natural logarithm (base <i>e</i>) of the specified value.
   * @param x the value.
   * @return the natural logarithm.
   */
  public static double log(double x) {
    return Math.log(x);
  }

  /**
   * Returns the logarithm base 10 of the specified value.
   * @param x the value.
   * @return the logarithm base 10.
   */
  public static float log10(float x) {
    return (float)Math.log10(x);
  }

  /**
   * Returns the logarithm base 10 of the specified value.
   * @param x the value.
   * @return the logarithm base 10.
   */
  public static double log10(double x) {
    return Math.log10(x);
  }

  /**
   * Returns the positive square root of a the specified value.
   * @param x the value.
   * @return the positive square root.
   */
  public static float sqrt(float x) {
    return (float)Math.sqrt(x);
  }

  /**
   * Returns the positive square root of a the specified value.
   * @param x the value.
   * @return the positive square root.
   */
  public static double sqrt(double x) {
    return Math.sqrt(x);
  }

  /**
   * Returns the value of x raised to the y'th power.
   * @param x the base.
   * @param y the exponent.
   * @return the value.
   */
  public static float pow(float x, float y) {
    return (float)Math.pow(x,y);
  }

  /**
   * Returns the value of x raised to the y'th power.
   * @param x the base.
   * @param y the exponent.
   * @return the value.
   */
  public static double pow(double x, double y) {
    return Math.pow(x,y);
  }

  /**
   * Returns the hyperbolic sine of the specified value.
   * @param x the value.
   * @return the hyperbolic sine.
   */
  public static float sinh(float x) {
    return (float)Math.sinh(x);
  }

  /**
   * Returns the hyperbolic sine of the specified value.
   * @param x the value.
   * @return the hyperbolic sine.
   */
  public static double sinh(double x) {
    return Math.sinh(x);
  }

  /**
   * Returns the hyperbolic cosine of the specified value.
   * @param x the value.
   * @return the hyperbolic cosine.
   */
  public static float cosh(float x) {
    return (float)Math.cosh(x);
  }

  /**
   * Returns the hyperbolic cosine of the specified value.
   * @param x the value.
   * @return the hyperbolic cosine.
   */
  public static double cosh(double x) {
    return Math.cosh(x);
  }

  /**
   * Returns the hyperbolic tangent of the specified value.
   * @param x the value.
   * @return the hyperbolic tangent.
   */
  public static float tanh(float x) {
    return (float)Math.tanh(x);
  }

  /**
   * Returns the hyperbolic tangent of the specified value.
   * @param x the value.
   * @return the hyperbolic tangent.
   */
  public static double tanh(double x) {
    return Math.tanh(x);
  }

  /**
   * Returns the smallest (closest to negative infinity) value that is greater 
   * than or equal to the argument and is equal to a mathematical integer.
   * @param x a value.
   * @return the smallest value.
   */
  public static float ceil(float x) {
    return (float)Math.ceil(x);
  }

  /**
   * Returns the smallest (closest to negative infinity) value that is greater 
   * than or equal to the argument and is equal to a mathematical integer.
   * @param x a value.
   * @return the smallest value.
   */
  public static double ceil(double x) {
    return Math.ceil(x);
  }

  /**
   * Returns the largest (closest to positive infinity) value that is less 
   * than or equal to the argument and is equal to a mathematical integer.
   * @param x a value.
   * @return the largest value.
   */
  public static float floor(float x) {
    return (float)Math.floor(x);
  }

  /**
   * Returns the largest (closest to positive infinity) value that is less 
   * than or equal to the argument and is equal to a mathematical integer.
   * @param x a value.
   * @return the largest value.
   */
  public static double floor(double x) {
    return Math.floor(x);
  }

  /**
   * Returns the value that is closest to the specified value and is equal 
   * to a mathematical integer.
   * @param x the value.
   * @return the closest value.
   */
  public static float rint(float x) {
    return (float)Math.rint(x);
  }

  /**
   * Returns the value that is closest to the specified value and is equal 
   * to a mathematical integer.
   * @param x the value.
   * @return the closest value.
   */
  public static double rint(double x) {
    return Math.rint(x);
  }

  /**
   * Returns the closest int to the specified value. The result is the 
   * value of the expression <code>(int)Math.floor(a+0.5f)</code>.
   * @param x the value.
   * @return the value rounded to the nearest int.
   */
  public static int round(float x) {
    return Math.round(x);
  }

  /**
   * Returns the closest long to the specified value. The result is the 
   * value of the expression <code>(long)Math.floor(a+0.5)</code>.
   * @param x the value.
   * @return the value rounded to the nearest long.
   */
  public static long round(double x) {
    return Math.round(x);
  }

  /**
   * Returns the signum of the specified value. The signum is zero if the 
   * argument is zero, 1.0 if the argument is greater than zero, -1.0 if 
   * the argument is less than zero.
   * @param x the value.
   * @return the signum.
   */
  public static float signum(float x) {
    return (x>0.0f)?1.0f:((x<0.0f)?-1.0f:0.0f);
  }

  /**
   * Returns the signum of the specified value. The signum is zero if the 
   * argument is zero, 1.0 if the argument is greater than zero, -1.0 if 
   * the argument is less than zero.
   * @param x the value.
   * @return the signum.
   */
  public static double signum(double x) {
    return (x>0.0)?1.0:((x<0.0)?-1.0:0.0);
  }

  /**
   * Returns the absolute value of the specified value.
   * @param x the value.
   * @return the absolute value.
   */
  public static int abs(int x) {
    return (x>=0)?x:-x;
  }

  /**
   * Returns the absolute value of the specified value.
   * @param x the value.
   * @return the absolute value.
   */
  public static long abs(long x) {
    return (x>=0L)?x:-x;
  }

  /**
   * Returns the absolute value of the specified value.
   * If this is a problem, use {@code Math.abs}.
   * @param x the value.
   * @return the absolute value.
   */
  public static float abs(float x) {
    return (x>=0.0f)?x:-x;
  }

  /**
   * Returns the absolute value of the specified value.
   * If this is a problem, use {@code Math.abs}.
   * @param x the value.
   * @return the absolute value.
   */
  public static double abs(double x) {
    return (x>=0.0d)?x:-x;
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @return the maximum value.
   */
  public static int max(int a, int b) {
    return (a>=b)?a:b;
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @return the maximum value.
   */
  public static int max(int a, int b, int c) {
    return max(a,max(b,c));
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @param d a value.
   * @return the maximum value.
   */
  public static int max(int a, int b, int c, int d) {
    return max(a,max(b,max(c,d)));
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @return the maximum value.
   */
  public static long max(long a, long b) {
    return (a>=b)?a:b;
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @return the maximum value.
   */
  public static long max(long a, long b, long c) {
    return max(a,max(b,c));
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @param d a value.
   * @return the maximum value.
   */
  public static long max(long a, long b, long c, long d) {
    return max(a,max(b,max(c,d)));
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @return the maximum value.
   */
  public static float max(float a, float b) {
    return (a>=b)?a:b;
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @return the maximum value.
   */
  public static float max(float a, float b, float c) {
    return max(a,max(b,c));
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @param d a value.
   * @return the maximum value.
   */
  public static float max(float a, float b, float c, float d) {
    return max(a,max(b,max(c,d)));
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @return the maximum value.
   */
  public static double max(double a, double b) {
    return (a>=b)?a:b;
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @return the maximum value.
   */
  public static double max(double a, double b, double c) {
    return max(a,max(b,c));
  }

  /**
   * Returns the maximum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @param d a value.
   * @return the maximum value.
   */
  public static double max(double a, double b, double c, double d) {
    return max(a,max(b,max(c,d)));
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @return the minimum value.
   */
  public static int min(int a, int b) {
    return (a<=b)?a:b;
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @return the minimum value.
   */
  public static int min(int a, int b, int c) {
    return min(a,min(b,c));
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @param d a value.
   * @return the minimum value.
   */
  public static int min(int a, int b, int c, int d) {
    return min(a,min(b,min(c,d)));
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @return the minimum value.
   */
  public static long min(long a, long b) {
    return (a<=b)?a:b;
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @return the minimum value.
   */
  public static long min(long a, long b, long c) {
    return min(a,min(b,c));
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @param d a value.
   * @return the minimum value.
   */
  public static long min(long a, long b, long c, long d) {
    return min(a,min(b,min(c,d)));
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @return the minimum value.
   */
  public static float min(float a, float b) {
    return (a<=b)?a:b;
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @return the minimum value.
   */
  public static float min(float a, float b, float c) {
    return min(a,min(b,c));
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @param d a value.
   * @return the minimum value.
   */
  public static float min(float a, float b, float c, float d) {
    return min(a,min(b,min(c,d)));
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @return the minimum value.
   */
  public static double min(double a, double b) {
    return (a<=b)?a:b;
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @return the minimum value.
   */
  public static double min(double a, double b, double c) {
    return min(a,min(b,c));
  }

  /**
   * Returns the minimum of the specified values.
   * @param a a value.
   * @param b a value.
   * @param c a value.
   * @param d a value.
   * @return the minimum value.
   */
  public static double min(double a, double b, double c, double d) {
    return min(a,min(b,min(c,d)));
  }

  // Static methods only.
  private MathPlus() {
  }
}
