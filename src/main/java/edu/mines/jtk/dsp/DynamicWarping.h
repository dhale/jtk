/*
The algorithm contained in the .cxx and .h are used with permission from Dave Hale 
of Colorado School of Mines and is under the terms of Common Public License v1.0.

The complete java source code can be downloaded from Dave Hale's website:
https://github.com/dhale/jtk/blob/master/src/main/java/edu/mines/jtk/dsp/DynamicWarping.java
and
https://github.com/dhale/jtk/blob/master/src/main/java/edu/mines/jtk/dsp/RecursiveExponentialFilter.java

The original documentation for Dynamic Warping.

/****************************************************************************
Copyright (c) 2012, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Dynamic warping of sequences and images.
 * <p>
 * For sequences f and g, dynamic warping finds a sequence of 
 * shifts u such that f[i1] ~ g[i1+u[i1]], subject to a bound b1 
 * on strain, the rate at which the shifts u[i1] vary with sample 
 * index i1.
 * <p>
 * An increasing u[i1] = u[i1-1] + 1 implies that, between indices
 * i1-1 and i1, g[i1] is a stretched version of f[i1] ~ g[i1+u[i1]].
 * For, in this case, values in f for indices i1 and i1-1 are one 
 * sample apart, but corresponding values in g are two samples 
 * apart, which implies stretching by 100%. Likewise, a decreasing 
 * u[i1] = u[i1-1] - 1 implies squeezing by 100%.
 * <p>
 * In practice, 100% strain (stretching or squeezing) may be extreme.
 * Therefore, the upper bound on strain may be smaller than one. For 
 * example, if the bound b1 = 0.5, then |u[i1]-u[i1-1]| &le; 0.5.
 * <p>
 * For 2D images f and g, dynamic warping finds a 2D array of shifts
 * u[i2][i1] such that f[i2][i1] ~ g[i2][i1+u[i2][i1]], subject to 
 * bounds b1 and b2 on strains, the rates at which shifts u[i2][i1] 
 * vary with samples indices i1 and i2, respectively.
 * <p>
 * For 3D images f and g, dynamic warping finds a 3D array of shifts
 * u[i3][i2][i1] in a similar way. However, finding shifts for 3D 
 * images may require an excessive amount of memory. Dynamic image 
 * warping requires a temporary array of nlag*nsample floats, where 
 * the number of lags nlag = 1+shiftMax-shiftMin and nsample is the 
 * number of image samples. For 3D images, the product nlag*nsample 
 * is likely to be too large for the temporary array to fit in random-
 * access memory (RAM). In this case, shifts u are obtained by blending 
 * together shifts computed from overlapping subsets of the 3D image.
 * <p>
 * Estimated shifts u can be smoothed, and the extent of smoothing 
 * along each dimension is inversely proportional to the strain limit 
 * for that dimension. These extents can be scaled by specified factors 
 * for more or less smoothing. The default scale factors are zero, for 
 * no smoothing.
 * <p>
 * This class provides numerous methods, but typical applications
 * require only several of these, usually only the methods that find
 * and apply shifts. The many other methods are provided only for 
 * atypical applications and research.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.07.05
 
 The original documentation for RecursiveExponentialFilter.

/****************************************************************************
Copyright (c) 2011, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import edu.mines.jtk.util.Parallel;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Recursive symmetric exponential smoothing filter. Except perhaps near
 * the edges of input and output arrays, the impulse response of this 
 * two-sided filter is symmetric and decays exponentially from its peak 
 * value at zero lag. Specifically, the impulse response has the form
 * h[n] = a^abs(n)*(1-a)/(1+a), where a is a parameter in the range
 * [0:1) derived from a specified half-width sigma.
 * <p>
 * Like the Gaussian filter, the impulse response of the exponential
 * filter is nowhere zero. The half-width sigma for the exponential 
 * filter is here defined so that, for low frequencies, the frequency 
 * response of the exponential filter approximates that for a Gaussian 
 * filter with the same specified half-width sigma. Specifically, the
 * value, slope and curvature of the frequency responses will be the
 * same for exponential and Gaussian filters if the same half-widths
 * are specified.
 * <p>
 * This smoothing filter is faster than a recursive Gaussian filter. 
 * This filter also provides a variety of boundary conditions that
 * can be used to control the filtering of samples near the edges
 * of arrays. For most (but not all) of these boundary conditions, 
 * this filter is symmetric and positive-definite (SPD). This means, 
 * for example, that it can be used as a preconditioner in 
 * conjugate-gradient solutions of SPD systems of equations.
 * <p>
 * Multidimensional filters are applied as a cascade of one-dimensional
 * filters applied for each dimension of multidimensional arrays. In 
 * contrast to the Gaussian filter, this cascade for the exponential 
 * filter does not have isotropic impulse or frequency responses.
 * <p>
 * All smoothing can be performed in place, so that input and output 
 * arrays can be the same array.
 *
 * @author Dave Hale &amp; Simon Luo, Colorado School of Mines
 * @version 2011.10.01

*/



#ifndef DynamicWarping_include
#define DynamicWarping_include

#include <string>
#include <vector>

using namespace std;

class RecursiveExponentialFilter;

/******************************* INCLUDE FILES ********************************/
class DynamicWarping 
{
public:
  enum ErrorExtrapolation
  {
    DTW_NEAREST,
    DTW_AVERAGE,
    DTW_REFLECT
  };

  DynamicWarping(int shiftMin, int shiftMax);
 ~DynamicWarping();

  static const string NEAREST_STR;
  static const string AVERAGE_STR;
  static const string REFLECT_STR;

  void setStrainMax               (double strainMax);

  void setErrorExtrapolation      (ErrorExtrapolation ee);

  void setErrorExponent           (double e);

  void setErrorSmoothing          (int esmooth);

  void setShiftSmoothing          (double usmooth);

  vector<float> findShifts        (const vector<float>& f, const vector<float>& g);

  void findShifts                 (const vector<float>& f, const vector<float>& g, vector<float>& u);

  void computeErrorsAllocMem      (const vector<float>& f, const vector<float>& g, vector< vector<float> >& e);

  static DynamicWarping::ErrorExtrapolation 
             mapStrToErrorExtrapolationMethod      (const string& optStr);

  static string mapErrorExtrapolationMethodToStr   (DynamicWarping::ErrorExtrapolation method);

  void smoothErrors               (const vector< vector<float> >& e, vector< vector<float> >& es);

  vector<float> smoothShifts      (const vector<float>& u);

  void smoothShifts               (const vector<float>&u, vector<float>& us);

  void accumulateForwardAllocMem  (const vector< vector<float> >& e, vector< vector<float> >& d);

  void accumulateForward          (const vector< vector<float> >& e, vector< vector<float> >& d);

  void backtrackReverse           (const vector< vector<float> >&d, 
                                   const vector< vector<float> >&e,
                                         vector<float>          &u);
private:
  float error                     (float f, float g);

  void computeErrors              (const vector<float> &f,
                                   const vector<float> &g,
                                   vector< vector<float> > &e);

  static void accumulate          (int dir,
                                   int b,
                                   const vector< vector<float> >& e,
                                   vector< vector<float> >& d);

  static void backtrack           (int dir,
                                   int b,
                                   int lmin,
                                   const vector<vector<float> >& d,
                                   const vector<vector<float> >& e,
                                         vector<float>         & u);

  static void normalizeErrors     (vector < vector<float> >&e );

  static void shiftAndScale       (float emin, float emax, vector< vector<float> >&e);

  static void smoothErrors1       (int b,
                                   const vector<vector<float> >& e,
                                         vector<vector<float> >& es);

  void updateSmoothingFilter();

private:
  int                         _nl;
  int                         _lmin, _lmax;
  ErrorExtrapolation          _extrap;
  float                       _epow;
  int                         _esmooth;
  double                      _usmooth1;
  int                         _bstrain1;
  RecursiveExponentialFilter  *_ref1;
};

class RecursiveExponentialFilter 
{
public:
  enum Edges 
  {
    INPUT_ZERO_VALUE,
    INPUT_ZERO_SLOPE,
    OUTPUT_ZERO_VALUE,
    OUTPUT_ZERO_SLOPE
  };

  RecursiveExponentialFilter(double sigma);
 ~RecursiveExponentialFilter();

  void apply                    (const vector<float>& x, vector<float>& y);

  void apply1                   (const vector<float>& x, vector<float>& y);

private:
  static float aFromSigma       (double sigma);
  static void  smooth1          (bool ei,
                                 bool zs,
                                 float a,
                                 const vector<float>& x,
                                       vector<float>& y);

  static void smooth1Ei         (bool zs,
                                 float a,
                                 const vector<float>& x,
                                       vector<float>& y);

  static void smooth1Eo          (bool zs,
                                  float a,
                                  const vector<float>& x,
                                        vector<float>& y);

private:
  float _sigma1;
  float _a1;
  bool  _ei;
  bool  _zs;

};

#endif
