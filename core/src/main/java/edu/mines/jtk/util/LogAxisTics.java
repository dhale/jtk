/****************************************************************************
Copyright 2016, Colorado School of Mines.
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
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class LogAxisTics extends AxisTics {

  /**
   * Constructs axis tics for a specified maximum number of major tics.
   * @param x1 the value at one end of the axis.
   * @param x2 the value at the other end of the axis.
   * @param ntic the maximum number of major tics.
   */
  public LogAxisTics(double x1, double x2, int ntic) {
    super(x1,x2,ntic);
    double xmin = min(x1,x2);
    double xmax = max(x1,x2);
    _expMin = log10(xmin);
    _expMax = log10(xmax);
    _dtic = 1;
    _ftic = pow(10,ceil(_expMin));
    _ntic = ntic;
    computeMultiple();
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
   * Gets the tic multiple. The tic multiple is the number of (major and minor)
   * tics per major tic, except near the ends of the axis. Between any pair of
   * major tics there are multiple-1 minor tics.
   * @return the tic multiple.
   */
  public int getMultiple() {
    return _mtic;
  }

  /**
   * Gets the minor tic skip value, the first minor tic at which to skip because
   * it coincides with a major tic.
   * @return the first minor tic to skip
   */
  public int getFirstMinorSkip() {
    return _ktic;
  }

  // /////////////////////////////////////////////////////////////////////////
  // private

  // private double _xmin;
  // private double _xmax;
  private int _mtic;
  private int _ktic;
  private int _ntic;
  private double _dtic;
  private double _ftic;
  private int _nticMinor;
  private double _dticMinor;
  private double _fticMinor;
  private double _expMin;
  private double _expMax;

  private void computeMultiple() {
    _mtic = 9;
  }

  private void computeMinorTics() {
    // the number of log-spaced tics that the first value is behind the first
    // major tic
    double c = pow(10.0,(_expMin - ceil(_expMin) + 1));
    // the number of log-spaced tics that the last value is ahead of the
    // previous-to-first major tic
    int c2 = 10 - (int) ceil(c);
    // the number of log-spaced tics that the last value is behind the
    // next-after-last major tic
    double d = pow(10.0,(_expMax - floor(_expMax)));
    // the number of log-spaced tics that the last value is ahead of the last
    // major tic
    int d2 = (int) floor(d) - 1;

    _nticMinor = 10 * (_ntic) + c2 + d2;
    _dticMinor = 1;
    _fticMinor = pow(10,ceil(_expMin) - 1) * (int) (c);
    _ktic = c2 + 1;
  }

}
