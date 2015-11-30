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

import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tics for annotating an axis. Given values at the endpoints of the axis,
 * axes tics are constructed by computing parameters for both major and
 * minor tics. Major tics are a subset of minor tics. Typically, major 
 * tics are labeled with character strings that represent their values. 
 * <p>
 * Axes tics can be constructed in two ways, by specifying either (1) the 
 * interval between major tics or (2) the maximum number of major tics.
 * <p>
 * In the first case, when the major tic interval (a positive number) is 
 * specified, other tic parameters are easily computed. For example, the 
 * value of the first major tic equals the smallest multiple of the major 
 * tic interval that is not less than the minimum of the axis endpoint 
 * values. Likewise, the number of major tics is computed so that the 
 * value of the last major tic is not greater than the maximum of the 
 * axis endpoint values.
 * <p>
 * In the second case, when the maximum number of major tics is specified, 
 * the major tic interval is computed to be 2, 5, or 10 times some power 
 * of 10. Then, other tic parameters are computed as in the first case. 
 * The tricky part in this second case is choosing the best number from 
 * the set {2,5,10}. That best number is called the tic <em>multiple</em>, 
 * and is computed so that the number of major tics is close to, but not 
 * greater than, the specified maximum number of major tics.
 * <p>
 * After construction, the counts, increments, and first values of both 
 * major and minor tics are available.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.14
 */
public class AxisTics {

  /**
   * Constructs axis tics for a specified major tic interval.
   * @param x1 the value at one end of the axis.
   * @param x2 the value at the other end of the axis.
   * @param dtic the major tic interval; a positive number.
   */
  public AxisTics(double x1, double x2, double dtic) {
    double xmin = min(x1,x2);
    double xmax = max(x1,x2);
    xmin -= (xmax-xmin)*FLT_EPSILON;
    xmax += (xmax-xmin)*FLT_EPSILON;
    double d = abs(dtic);
    double f = ceil(xmin/d)*d;
    //double f = ((int)(xmin/d)-1)*d;
    //while (f<xmin)
    //  f += d;
    int n = 1+(int)((xmax-f)/d);
    _xmin = xmin;
    _xmax = xmax;
    _ntic = n;
    _dtic = d;
    _ftic = f;
    computeMultiple();
    computeMinorTics();
  }

  /**
   * Constructs axis tics for a specified maximum number of major tics.
   * @param x1 the value at one end of the axis.
   * @param x2 the value at the other end of the axis.
   * @param ntic the maximum number of major tics.
   */
  public AxisTics(double x1, double x2, int ntic) {
    double xmin = _xmin = min(x1,x2);
    double xmax = _xmax = max(x1,x2);
    xmin -= (xmax-xmin)*FLT_EPSILON;
    xmax += (xmax-xmin)*FLT_EPSILON;
    int nmax = (ntic>=2)?ntic:2;
    double dmax = (xmax-xmin)/(nmax-1);
    int nmult = _mult.length;
    int nbest = 0;
    int mbest = 0;
    double dbest = 0.0;
    double fbest = 0.0;
    for (int imult=0; imult<nmult; ++imult) {
      int m = _mult[imult];
      int l = (int)floor(log10(dmax/m));
      double d = m*pow(10.0,l);
      double f = ceil(xmin/d)*d;
      int n = 1+(int)((xmax-f)/d);
      if (n>nmax) {
        d *= 10;
        f = ceil(xmin/d)*d;
        n = 1+(int)((xmax-f)/d);
      }
      if (nbest<n && n<=ntic) {
        nbest = n;
        mbest = m;
        dbest = d;
        fbest = f;
      }
    }
    nbest = 1+(int)((xmax-fbest)/dbest);
    if (nbest<2) {
      _ntic = 2;
      _dtic = _xmax-_xmin;
      _ftic = _xmin;
      computeMultiple();
    } else {
      _ntic = nbest;
      _dtic = dbest;
      _ftic = fbest;
      _mtic = mbest;
    }
    computeMinorTics();
  }

  /**
   * Gets the number of major tics.
   * @return the number of major tics.
   */
  public int getCountMajor() {
    return _ntic;
  }

  /**
   * Gets major tic interval.
   * @return the major tic interval.
   */
  public double getDeltaMajor() {
    return _dtic;
  }

  /**
   * Gets the value of the first major tic.
   * @return the value of the first major tic.
   */
  public double getFirstMajor() {
    return _ftic;
  }

  /**
   * Gets the number of minor tics.
   * @return the number of minor tics.
   */
  public int getCountMinor() {
    return _nticMinor;
  }

  /**
   * Gets minor tic interval.
   * @return the minor tic interval.
   */
  public double getDeltaMinor() {
    return _dticMinor;
  }

  /**
   * Gets the value of the first minor tic.
   * @return the value of the first minor tic.
   */
  public double getFirstMinor() {
    return _fticMinor;
  }

  /**
   * Gets the tic multiple. The tic multiple is the number of (major 
   * and minor) tics per major tic, except near the ends of the axis.
   * Between any pair of major tics there are multiple-1 minor tics.
   * @return the tic multiple.
   */
  public int getMultiple() {
    return _mtic;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _xmin;
  private double _xmax;
  private int _mtic;
  private int _ntic;
  private double _dtic;
  private double _ftic;
  private int _nticMinor;
  private double _dticMinor;
  private double _fticMinor;

  private static final int[] _mult = {2,5,10};

  private void computeMultiple() {
    _mtic = 1;
    double l10 = log10(_dtic/10.0);
    double l5 = log10(_dtic/5.0);
    double l2 = log10(_dtic/2.0);
    if (almostEqual(rint(l10),l10)) {
      _mtic = 10;
    } else if (almostEqual(rint(l5),l5)) {
      _mtic = 5;
    } else if (almostEqual(rint(l2),l2)) {
      _mtic = 2;
    }
  }

  private void computeMinorTics() {
    double dm = _dtic/_mtic;
    double fm = _ftic;
    while (_xmin<=fm-dm)
      fm -= dm;
    int nm = 1+(int)((_xmax-fm)/dm);
    _nticMinor = nm;
    _dticMinor = dm;
    _fticMinor = fm;
  }

  private static boolean almostEqual(double x1, double x2) {
    return abs(x1-x2)<=max(abs(x1),abs(x2))*100.0*DBL_EPSILON;
  }
}
