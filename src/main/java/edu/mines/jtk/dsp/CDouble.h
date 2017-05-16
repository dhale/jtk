/*
The algorithm contained in the .cxx and .h are used with permission from Dave Hale 
of Colorado School of Mines and is under the terms of Common Public License v1.0.

The code is a straight port from java to C++ by Tiancheng Song of CGG.
The complete java source code can be downloaded from Dave Hale's website:
https://github.com/dhale/jtk/blob/master/src/main/java/edu/mines/jtk/util/Cdouble.java
*/

/******************************************************************************
 *  \brief    Dave Hale's complex number.
 *   A complex number, with double-precision real and imaginary parts.
 *   @author Dave Hale, Colorado School of Mines
 *   @version 2005.05.06
 ******************************************************************************/

#ifndef CDouble_include
#define CDouble_include

#include <string>
#include <vector>
#include <cmath>
#include <stdint.h> // for uint64_t
#include <sstream>  // for std::ostringstream

using namespace std;

class Cdouble {
public :
  static Cdouble DBL_I;
  double r;
  double i;
  Cdouble();
  Cdouble(double r);
  Cdouble(double r, double i);
  Cdouble(const Cdouble &x);
  Cdouble plus(const Cdouble &x);
  Cdouble minus(Cdouble x);
  Cdouble times(Cdouble x);
  Cdouble over(Cdouble x);
  Cdouble plus(double x);
  Cdouble minus(double x);
  Cdouble times(double x);
  Cdouble over(double x);
  Cdouble plusEquals(Cdouble x);
  Cdouble minusEquals(Cdouble x);
  Cdouble timesEquals(Cdouble x);
  Cdouble overEquals(Cdouble x);
  Cdouble plusEquals(double x);
  Cdouble minusEquals(double x);
  Cdouble timesEquals(double x);
  Cdouble overEquals(double x);
  Cdouble conjEquals();
  Cdouble invEquals();
  Cdouble negEquals();
  bool isReal();
  bool isImag();
  Cdouble conj();
  Cdouble inv();
  Cdouble neg();
  double abs();
  double arg();
  double norm();
  Cdouble sqrt();
  Cdouble exp();
  Cdouble log();
  Cdouble log10();
  Cdouble pow(double y);
  Cdouble pow(Cdouble y);
  Cdouble sin();
  Cdouble cos();
  Cdouble tan();
  Cdouble sinh();
  Cdouble cosh();
  Cdouble tanh();
  static bool isReal(Cdouble x);
  static bool isImag(Cdouble x) ;
  static Cdouble conj(Cdouble x);
  static Cdouble inv(Cdouble x);
  static Cdouble neg(Cdouble x);
  static Cdouble polar(double r, double a);
  static Cdouble add(Cdouble x, Cdouble y);
  static Cdouble sub(Cdouble x, Cdouble y);
  static Cdouble mul(Cdouble x, Cdouble y);
  static Cdouble div(Cdouble x, Cdouble y);
  static double abs(Cdouble x);
  static double arg(Cdouble x);
  static double norm(Cdouble x);
  static Cdouble sqrt(Cdouble x);
  static Cdouble exp(Cdouble x);
  static Cdouble log(Cdouble x);
  static Cdouble log10(Cdouble x);
  static Cdouble pow(Cdouble x, double y);
  static Cdouble pow(double x, Cdouble y);
  static Cdouble pow(Cdouble x, Cdouble y);
  static Cdouble sin(Cdouble x);
  static Cdouble cos(Cdouble x);
  static Cdouble tan(Cdouble x);
  static Cdouble sinh(Cdouble x);
  static Cdouble cosh(Cdouble x);
  static Cdouble tanh(Cdouble x);
  bool equals(Cdouble obj);
  static inline uint64_t doubleToRawBits(double x);
  int hashCode();
  string toString();

private:
  static double max(double x, double y);
  static double abs(double x);
  static double sqrt(double x);
  static double cos(double x);
  static double sin(double x);
  static double cosh(double x);
  static double sinh(double x);
  static double exp(double x);
  static double log(double x);
  static double pow(double x, double y);
  static double atan2(double y, double x);
};

#endif
