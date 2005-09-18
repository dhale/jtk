/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import edu.mines.jtk.util.Cdouble;
import static edu.mines.jtk.util.MathPlus.*;


/**
 * A recursive filter implemented as a cascade of 2nd-order filters.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.19
 */
public class RecursiveCascadeFilter extends RecursiveFilter {

  /**
   * Constructs a recursive filter with specified poles, zeros, and gain.
   * The coefficients of this recursive filter are real. Therefore, poles 
   * and zeros with non-zero imaginary parts must have conjugate mates.
   * @param poles array of complex poles.
   * @param zeros array of complex poles.
   * @param gain the filter gain.
   */
  public RecursiveCascadeFilter(
    Cdouble[] poles, Cdouble[] zeros, double gain) {
    init(poles,zeros,gain);
  }

  public void applyForward(float[] x, float[] y) {
    _f2[0].applyForward(x,y);
    for (int i2=1; i2<_n2; ++i2)
      _f2[i2].applyForward(y,y);
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected RecursiveCascadeFilter() {
  }

  protected void init(Cdouble[] poles, Cdouble[] zeros, double gain) {
    Check.argument(poles.length>0 || zeros.length>0,
      "at least one pole or zero is specified");

    // Sort poles and zeros so that complex conjugate pairs are first.
    poles = sortPolesOrZeros(poles);
    zeros = sortPolesOrZeros(zeros);

    // Construct 2nd-order filters.
    int np = poles.length;
    int nz = zeros.length;
    _n2 = max((np+1)/2,(nz+1)/2);
    _f2 = new Recursive2ndOrderFilter[_n2];
    gain = pow(gain,1.0/_n2);
    Cdouble c0 = new Cdouble(0.0,0.0);
    for (int i2=0,ip=0,iz=0; i2<_n2; ++i2) {
      Cdouble pole1 = (ip<np)?poles[ip++]:c0;
      Cdouble pole2 = (ip<np)?poles[ip++]:c0;
      Cdouble zero1 = (iz<nz)?zeros[iz++]:c0;
      Cdouble zero2 = (iz<nz)?zeros[iz++]:c0;
      _f2[i2] = new Recursive2ndOrderFilter(pole1,pole2,zero1,zero2,gain);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n2;
  private Recursive2ndOrderFilter[] _f2;

  /**
   * Sorts array of poles or zeros. After sorting, any complex conjugate 
   * pairs are first in the array, followed by any real poles or zeros.
   * Also ensures that any complex poles or zeros have conjugate mates.
   * @return array of sorted 
   */
  private static Cdouble[] sortPolesOrZeros(Cdouble[] c) {
    int n = c.length;
    Cdouble[] cs = new Cdouble[n];
    int ns = 0;
    for (int i=0; i<n; ++i) {
      if (!c[i].isReal()) {
        Cdouble cc = c[i].conj();
        int j = 0;
        while (j<n && !cc.equals(c[j]))
          ++j;
        Check.argument(j<n,"complex "+c[i]+" has a conjugate mate");
        if (i<j) {
          cs[ns++] = c[i];
          cs[ns++] = c[j];
        }
      }
    }
    for (int i=0; i<n; ++i) {
      if (c[i].isReal())
        cs[ns++] = c[i];
    }
    return cs;
  }
}
