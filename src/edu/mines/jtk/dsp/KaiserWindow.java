/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A Kaiser window is often used in FIR filter design. It is easy to use 
 * and exhibits near-optimal properties for many filter design problems.
 * The Kaiser window may be defined by any two of three parameters: 
 * window length, transition width, and maximum absolute error. 
 * <p> 
 * For definiteness, let's assume that the Kaiser window is a function w(x) 
 * of argument x. Then, the window length is the range of x, centered about 
 * x = 0, for which the Kaiser window is non-zero. In other words, w(x) = 
 * 0 for x &gt; length/2. When windowing functions of time, both the window
 * length and the argument x of w(x) have dimensions of time. 
 * <p>
 * The transition width is the width of the central lobe in the Fourier 
 * transform of the window. For band-pass filters, this is the width of 
 * the transition between pass and stop bands. When windowing functions 
 * of time, the dimensions of transition width are 1/time (frequency). 
 * In any case, the product of window length and transition width is 
 * dimensionless.
 * <p>
 * The maximum absolute error corresponds to the magnitude of the ripples
 * adjacent to the central lobe in the Fourier transform of the window. If
 * we assume that an ideal band-pass filter has magnitude one in the pass
 * band, then the maximum (or minimum) amplitude in the passband of a 
 * windowed filter is one plus (or minus) the maximum amplitude error.
 * Likewise, the maximum amplitude in the stopband of such a windowed
 * filter equals the maximum amplitude error.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.17
 */
public class KaiserWindow {

  /**
   * Returns a Kaiser window with specified length and transition width.
   * @param length the (two-sided) window length.
   * @param width the transition width.
   * @return the Kaiser window.
   */
  public static KaiserWindow fromLengthAndWidth(double length, double width) {
    Check.argument(length>0.0,"length > 0");
    Check.argument(width>0.0,"width > 0");
    double d = length*width;
    double a = (d>=0.9222)?7.95+14.36*d:AMIN;
    double error = pow(10.0,-a/20.0);
    return new KaiserWindow(length,width,error);
  }

  /**
   * Returns a Kaiser window with specified length and absolute error.
   * @param length the (two-sided) window length.
   * @param error the maximum absolute error.
   * @return the Kaiser window.
   */
  public static KaiserWindow fromLengthAndError(double length, double error) {
    Check.argument(length>0.0,"length > 0");
    Check.argument(error<1.0,"error < 1");
    double a = -20.0*log10(error);
    double d = (a>AMIN)?(a-7.95)/14.36:0.9222;
    double width = d/length;
    return new KaiserWindow(length,width,error);
  }

  /**
   * Returns a Kaiser window with specified transition width and absolute error.
   * @param width the transition width.
   * @param error the maximum absolute error.
   * @return the Kaiser window.
   */
  public static KaiserWindow fromWidthAndError(double width, double error) {
    Check.argument(width>0.0,"width > 0");
    Check.argument(error<1.0,"error < 1");
    double a = -20.0*log10(error);
    double d = (a>AMIN)?(a-7.95)/14.36:0.9222;
    double length = d/width;
    return new KaiserWindow(length,width,error);
  }

  /**
   * Gets the value of this Kaiser window function for specified x.
   * @param x the value at which to evaluate the Kaiser window function.
   * @return the Kaiser window function value.
   */
  public double getValue(double x) {
    double xx = x*x;
    return (xx<=_xxmax)?_scale*ino(_alpha*sqrt(1.0-xx/_xxmax)):0.0;
  }

  private static final double AMIN = 20.96;
  private double _length;
  private double _width;
  private double _error;
  private double _alpha;
  private double _scale;
  private double _xxmax;

  private KaiserWindow(double length, double width, double error) {
    Check.argument(length>0.0,"length > 0");
    Check.argument(width>0.0,"width > 0");
    Check.argument(error<1.0,"error < 1");
    _length = length;
    _width = width;
    _error = error;
    double a = -20.0*log10(_error);
    if (a<=AMIN) {
      _alpha = 0.0;
    } else if (a<=50.0) {
      _alpha = 0.5842*pow(a-AMIN,0.4)+0.07886*(a-AMIN);
    } else {
      _alpha = 0.1102*(a-8.7);
    }
    _scale = 1.0/ino(_alpha);
    _xxmax = 0.25*_length*_length;
  }

  private double ino(double x) {
    double s = 1.0;
    double ds = 1.0;
    double d = 0.0;
    do {
      d += 2.0;
      ds *= (x*x)/(d*d);
      s += ds;
    } while (ds>s*DBL_EPSILON);
    return s;
  }
}
