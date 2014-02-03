/*! \file DynamicWarping.cxx */

/*
Please refer to the header file for complete documentation and licensing information.
*/


#include "DynamicWarping.h"
#include <cmath>
#include <cfloat>
#include <vector>
#include <algorithm>

const string DynamicWarping::NEAREST_STR ("Nearest");
const string DynamicWarping::AVERAGE_STR ("Average");
const string DynamicWarping::REFLECT_STR ("Reflect");


/**
   * Constructs a dynamic warping for specified bounds on shifts.
   * @param shiftMin lower bound on shift u.
   * @param shiftMax upper bound on shift u.
   */
DynamicWarping::DynamicWarping (int shiftMin, int shiftMax)
{
  //Check.argument(shiftMax-shiftMin>1,"shiftMax-shiftMin>1");
  _lmin = shiftMin;
  _lmax = shiftMax;
  _nl = 1+_lmax-_lmin;
  //_si = new SincInterp();
  _extrap = DynamicWarping::DTW_NEAREST;  
  _epow = 2.f;
  _usmooth1 = 0.0;
  _bstrain1 = 1;
  _ref1 = 0;
}

DynamicWarping::~DynamicWarping()
{
  delete _ref1;
  _ref1 = 0;
}

 /**
   * Sets bound on strain for all dimensions. Must be in (0,1].
   * The actual bound on strain is 1.0/ceil(1.0/strainMax), which
   * is less than the specified strainMax when 1.0/strainMax is not
   * an integer. The default bound on strain is 1.0 (100%).
   * @param strainMax the bound, a value less than or equal to one.
   */
void DynamicWarping::setStrainMax(double strainMax)
{
  _bstrain1 = (int) ceil (1.0/strainMax);
  updateSmoothingFilter();
}

 /**
   * Sets the method used to extrapolate alignment errors.
   * Extrapolation is necessary when the sum i+l of sample index
   * i and lag l is out of bounds. The default method is to use 
   * the error computed for the nearest index i and the same lag l.
   * @param ee the error extrapolation method.
   */
void DynamicWarping::setErrorExtrapolation(ErrorExtrapolation ee)
{
  _extrap = ee;
}

/**
   * Sets the exponent used to compute alignment errors |f-g|^e.
   * The default exponent is 2.
   * @param e the exponent.
   */
void DynamicWarping::setErrorExponent(double e) 
{
  _epow = (float)e;
}


/**
  * Sets the number of nonlinear smoothings of alignment errors.
  * In dynamic warping, alignment errors are smoothed the specified 
  * number of times, along all dimensions (in order 1, 2, ...), 
  * before estimating shifts by accumulating and backtracking along 
  * only the 1st dimension. 
  * <p> 
  * The default number of smoothings is zero, which is best for 1D
  * sequences. For 2D and 3D images, two smoothings are recommended.
  * @param esmooth number of nonlinear smoothings.
  */
void DynamicWarping::setErrorSmoothing(int esmooth) {
  _esmooth = esmooth;
}

/**
  * Sets extent of smoothing filters used to smooth shifts.
  * Half-widths of smoothing filters are inversely proportional to
  * strain limits, and are scaled by the specified factor. Default 
  * factor is zero, for no smoothing.
  * @param usmooth extent of smoothing filter in all dimensions.
  */
void DynamicWarping::setShiftSmoothing(double usmooth) {
  _usmooth1 = usmooth;
  updateSmoothingFilter();
}

/**
  * Computes and returns shifts for specified sequences.
  * @param f array for the sequence f.
  * @param g array for the sequence g.
  * @return array of shifts u.
  */
vector<float> DynamicWarping::findShifts(const vector<float>& f, const vector<float>& g) {
  vector<float> u;
  u.resize(f.size());
  findShifts(f,g,u);
  return u;
}

/**
  * Computes shifts for specified sequences.
  * @param f input array for the sequence f.
  * @param g input array for the sequence g.
  * @param u output array of shifts u.
  */
void DynamicWarping::findShifts(const vector<float>& f, const vector<float>& g, vector<float>& u) {
  vector< vector<float> > e;
  computeErrorsAllocMem(f,g,e);
  for (int is=0; is<_esmooth; ++is)
    smoothErrors(e,e);
  vector< vector<float> > d;
  accumulateForwardAllocMem(e, d);
  backtrackReverse(d,e,u);
  smoothShifts(u,u);
}

/**
  * Returns normalized alignment errors for all samples and lags.
  * The number of lags nl = 1+shiftMax-shiftMin. Lag indices 
  * il = 0, 1, 2, ..., nl-1 correspond to integer shifts in 
  * [shiftMin,shiftMax]. Alignment errors are a monotonically
  * increasing function of |f[i1]-g[i1+il+shiftMin]|.
  * @param f array[n1] for the sequence f[i1].
  * @param g array[n1] for the sequence g[i1].
  * @param e output array[n1][nl] of alignment errors.
  */
void DynamicWarping::computeErrorsAllocMem(const vector<float>& f, const vector<float>& g, vector <vector<float > > & e) 
{
  int n1 = f.size();
  e.resize(n1);
  for (int i = 0; i < n1; i++)
    e[i].resize(_nl);
  computeErrors(f,g,e);
  normalizeErrors(e);
}

/**
  * Smooths (and normalizes) alignment errors.
  * Input and output arrays can be the same array.
  * @param e input array[n1][nl] of alignment errors.
  * @param es output array[n1][nl] of smoothed errors.
  */
void DynamicWarping::smoothErrors(const vector< vector<float> >&e, vector< vector<float> >& es) 
{
  smoothErrors1(_bstrain1,e,es);
  normalizeErrors(es);
}

/**
  * Returns smoothed shifts.
  * @param u array of shifts to be smoothed.
  * @return array of smoothed shifts
  */
vector<float> DynamicWarping::smoothShifts(const vector<float>& u) 
{
  vector<float> us;
  us.resize(u.size());
  smoothShifts(u,us);
  return us;
}

/**
  * Smooths the specified shifts. Smoothing can be performed 
  * in place; input and output arrays can be the same array.
  * @param u input array of shifts to be smoothed.
  * @param us output array of smoothed shifts.
  */
void DynamicWarping::smoothShifts(const vector<float>& u, vector<float>& us) 
{
  if (_ref1) 
  {
    _ref1->apply(u,us); 
  } else 
  {
    us = u;
  }
}

/**
  * Returns errors accumulated in forward direction.
  * @param e array of alignment errors.
  * @return array of accumulated errors.
  */
void DynamicWarping::accumulateForwardAllocMem(const vector< vector<float> >& e, vector< vector<float> >& d) {
  d.resize(e.size());
  for (int i = 0 ; i < e.size(); i++)
    d[i].resize(e[i].size());
  accumulateForward(e,d);
}

/**
  * Accumulates alignment errors in forward direction.
  * @param e input array of alignment errors.
  * @param d output array of accumulated errors.
  */
void DynamicWarping::accumulateForward(const vector< vector<float> >&e, vector< vector<float> >& d) {
  accumulate( 1,_bstrain1,e,d);
}

/**
  * Computes shifts by backtracking in reverse direction.
  * @param d input array of accumulated errors.
  * @param e input array of alignment errors.
  * @param u output array of shifts.
  */
void DynamicWarping::backtrackReverse(const vector< vector<float> >& d, 
                                         const vector< vector<float> >& e,
                                         vector<float> & u) 
{
  backtrack(-1,_bstrain1,_lmin,d,e,u);
}

/**
  * Normalizes alignment errors to be in range [0,1].
  * @param e input/output array of alignment errors.
  */
void DynamicWarping::normalizeErrors(vector< vector<float> >& e) {
  int nl = e[0].size();
  int n1 = e.size();
  float emin = e[0][0];
  float emax = e[0][0];
  for (int i1=0; i1<n1; ++i1) {
    for (int il=0; il<nl; ++il) {
      float ei = e[i1][il];
      if (ei<emin) emin = ei;
      if (ei>emax) emax = ei;
    }
  }
  shiftAndScale(emin,emax,e);
}

float DynamicWarping::error(float f, float g) 
{
  return pow(abs(f-g),_epow);
}

/**
  * Computes alignment errors, not normalized.
  * @param f input array[ni] for sequence f.
  * @param g input array[ni] for sequence g.
  * @param e output array[ni][nl] of alignment errors.
  */
void DynamicWarping::computeErrors(const vector<float>& f, const vector<float>& g, vector< vector<float> >& e) 
{
  int n1 = f.size();
  int nl = _nl;
  int n1m = n1-1;
  bool average = (_extrap==DynamicWarping::DTW_AVERAGE);
  bool nearest = (_extrap==DynamicWarping::DTW_NEAREST);
  bool reflect = (_extrap==DynamicWarping::DTW_REFLECT);
  vector<float> eavg;
  vector<float> navg;
  if (average)
  {
    eavg.resize(nl);
    navg.resize(nl);
  }
  else
  {
    eavg.resize(0);
    navg.resize(0);
  }

  float emax = 0.0f;

  // Notes for indexing:
  // 0 <= il < nl, where il is index for lag
  // 0 <= i1 < n1, where i1 is index for sequence f
  // 0 <= j1 < n1, where j1 is index for sequence g
  // j1 = i1+il+lmin, where il+lmin = lag
  // 0 <= i1+il+lmin < n1, so that j1 is in bounds
  // max(0,-lmin-i1) <= il < min(nl,n1-lmin-i1)
  // max(0,-lmin-il) <= i1 < min(n1,n1-lmin-il)
  // j1 = 0    => i1 =     -lmin-il
  // j1 = n1-1 => i1 = n1-1-lmin-il

  // Compute errors where indices are in bounds for both f and g.
  for (int i1=0; i1<n1; ++i1) {
    int illo = max(0,   -_lmin-i1); // see notes
    int ilhi = min(nl,n1-_lmin-i1); // above
    for (int il=illo,j1=i1+il+_lmin; il<ilhi; ++il,++j1) {
      float ei = error(f[i1],g[j1]);
      e[i1][il] = ei;
      if (average) {
        eavg[il] += ei;
        navg[il] += 1;
      }
      if (ei>emax) 
        emax = ei;
    }
  }

  // If necessary, complete computation of average errors for each lag.
  if (average) {
    for (int il=0; il<nl; ++il) {
      if (navg[il]>0)
        eavg[il] /= navg[il];
    }
  }

  // For indices where errors have not yet been computed, extrapolate.
  for (int i1=0; i1<n1; ++i1) {
    int illo = max(0,   -_lmin-i1); // same as
    int ilhi = min(nl,n1-_lmin-i1); // above
    for (int il=0; il<nl; ++il) {
      if (il<illo || il>=ilhi) {
        if (average) {
          if (navg[il]>0) {
            e[i1][il] = eavg[il];
          } else {
            e[i1][il] = emax;
          }
        } else if (nearest || reflect) {
          int k1 = (il<illo)?-_lmin-il:n1m-_lmin-il;
          if (reflect)
            k1 += k1-i1;
          if (0<=k1 && k1<n1) {
            e[i1][il] = e[k1][il];
          } else {
            e[i1][il] = emax;
          }
        } else {
          e[i1][il] = emax;
        }
      }
    }
  }
}

/**
  * Non-linear accumulation of alignment errors.
  * @param dir accumulation direction, positive or negative.
  * @param b sample offset used to constrain changes in lag.
  * @param e input array[ni][nl] of alignment errors.
  * @param d output array[ni][nl] of accumulated errors.
  */
void DynamicWarping::accumulate(int dir, int b, const vector< vector<float> >& e, vector< vector<float> >& d) {
  int nl = e[0].size();
  int ni = e.size();
  int nlm1 = nl-1;
  int nim1 = ni-1;
  int ib = (dir>0)?0:nim1;
  int ie = (dir>0)?ni:-1;
  int is = (dir>0)?1:-1;
  for (int il=0; il<nl; ++il)
    d[ib][il] = 0.0f;
  for (int ii=ib; ii!=ie; ii+=is) {
    int ji = max(0,min(nim1,ii-is));
    int jb = max(0,min(nim1,ii-is*b));
    for (int il=0; il<nl; ++il) {
      int ilm1 = il-1; if (ilm1==-1) ilm1 = 0;
      int ilp1 = il+1; if (ilp1==nl) ilp1 = nlm1;
      float dm = d[jb][ilm1];
      float di = d[ji][il  ];
      float dp = d[jb][ilp1];
      for (int kb=ji; kb!=jb; kb-=is) {
        dm += e[kb][ilm1];
        dp += e[kb][ilp1];
      }
      d[ii][il] = min(min(dm,di),dp)+e[ii][il];
    }
  }
}

/**
  * Finds shifts by backtracking in accumulated alignment errors.
  * Backtracking must be performed in the direction opposite to
  * that for which accumulation was performed.
  * @param dir backtrack direction, positive or negative.
  * @param b sample offset used to constrain changes in lag.
  * @param lmin minimum lag corresponding to lag index zero.
  * @param d input array[ni][nl] of accumulated errors.
  * @param e input array[ni][nl] of alignment errors.
  * @param u output array[ni] of computed shifts.
  */
void DynamicWarping::backtrack(
  int dir, int b, int lmin, const vector< vector<float> >& d, const vector < vector<float> >& e, vector <float>& u) 
{
  float ob = 1.0f/b;
  int nl = d[0].size();
  int ni = d.size();
  int nlm1 = nl-1;
  int nim1 = ni-1;
  int ib = (dir>0)?0:nim1;
  int ie = (dir>0)?nim1:0;
  int is = (dir>0)?1:-1;
  int ii = ib;
  int il = max(0,min(nlm1,-lmin));
  float dl = d[ii][il];
  for (int jl=1; jl<nl; ++jl) {
    if (d[ii][jl]<dl) {
      dl = d[ii][jl];
      il = jl;
    }
  }
  u[ii] = il+lmin;
  while (ii!=ie) {
    int ji = max(0,min(nim1,ii+is));
    int jb = max(0,min(nim1,ii+is*b));
    int ilm1 = il-1; if (ilm1==-1) ilm1 = 0;
    int ilp1 = il+1; if (ilp1==nl) ilp1 = nlm1;
    float dm = d[jb][ilm1];
    float di = d[ji][il  ];
    float dp = d[jb][ilp1];
    for (int kb=ji; kb!=jb; kb+=is) {
      dm += e[kb][ilm1];
      dp += e[kb][ilp1];
    }
    dl = min(min(dm,di),dp);
    if (dl!=di) {
      if (dl==dm) {
        il = ilm1;
      } else {
        il = ilp1;
      }
    }
    ii += is;
    u[ii] = il+lmin;
    if (il==ilm1 || il==ilp1) {
      float du = (u[ii]-u[ii-is])*ob;
      u[ii] = u[ii-is]+du;
      for (int kb=ji; kb!=jb; kb+=is) {
        ii += is;
        u[ii] = u[ii-is]+du;
      }
    }
  }
}

/**
  * Shifts and scales alignment errors to be in range [0,1].
  * @param emin minimum alignment error before normalizing.
  * @param emax maximum alignment error before normalizing.
  * @param e input/output array of alignment errors.
  */
void DynamicWarping::shiftAndScale(float emin, float emax, vector< vector<float> >& e) 
{
  int nl = e[0].size();
  int n1 = e.size();
  float eshift = emin;
  float escale = (emax>emin)?1.0f/(emax-emin):1.0f;
  for (int i1=0; i1<n1; ++i1) {
    for (int il=0; il<nl; ++il) {
      e[i1][il] = (e[i1][il]-eshift)*escale;
    }
  }
}

/**
  * Does not normalize errors after smoothing.
  * @param b strain parameter in 1st dimension.
  * @param e input array of alignment errors to be smooothed.
  * @param es output array of smoothed alignment errors.
  */
void DynamicWarping::smoothErrors1(int b, const vector <vector<float> >& e, vector<vector<float> >& es) {
  int nl = e[0].size();
  int n1 = e.size();
  vector<vector<float> > ef;
  vector<vector<float> > er;
  ef.resize(n1);
  er.resize(n1);
  for (int i = 0; i < n1; i++)
  {
    ef[i].resize(nl);
    er[i].resize(nl);
  }
  accumulate( 1,b,e,ef);
  accumulate(-1,b,e,er);
  for (int i1=0; i1<n1; ++i1)
    for (int il=0; il<nl; ++il)
      es[i1][il] = ef[i1][il]+er[i1][il]-e[i1][il];
}

void DynamicWarping::updateSmoothingFilter()
{
  if (_ref1) delete _ref1;
  if (_usmooth1 > 0.0)
    _ref1 = new RecursiveExponentialFilter(_usmooth1*_bstrain1);
}

DynamicWarping::ErrorExtrapolation 
  DynamicWarping::mapStrToErrorExtrapolationMethod (const string& inStr)
{
  DynamicWarping::ErrorExtrapolation method = 
    DynamicWarping::DTW_NEAREST;

  if (inStr == DynamicWarping::AVERAGE_STR)
    method = DynamicWarping::DTW_AVERAGE;
  else if (inStr == DynamicWarping::REFLECT_STR)
    method = DynamicWarping::DTW_REFLECT;

  return method;
}

string DynamicWarping::mapErrorExtrapolationMethodToStr 
    (DynamicWarping::ErrorExtrapolation method)
{
  string optStr = DynamicWarping::NEAREST_STR;
  
  switch (method)
  {
  case DynamicWarping::DTW_AVERAGE:
    optStr = DynamicWarping::AVERAGE_STR;
    break;

  case DynamicWarping::DTW_REFLECT:
    optStr = DynamicWarping::REFLECT_STR;
    break;

  default:
    break;
  }

  return optStr;
}

/**
   * Constructs a filter with specified half-width.
   * The same half-width is used when applying the filter for all 
   * dimensions of multidimensional arrays.
   * @param sigma filter half-width.
   */
RecursiveExponentialFilter::RecursiveExponentialFilter(double sigma)
{
  _sigma1 = (float) sigma;
  _a1     = aFromSigma(sigma);
}

RecursiveExponentialFilter::~RecursiveExponentialFilter()
{
}

 /**
   * Applies this filter.
   * @param x input array.
   * @param y output array.
   */
void RecursiveExponentialFilter::apply (const vector<float>& x, vector<float>& y)
{
  apply1(x, y);
}

/**
  * Applies this filter along the 1st (only) array dimension.
  * Input and output arrays can be the same array.
  * @param x input array.
  * @param y output array.
  */
void RecursiveExponentialFilter::apply1(const vector<float>& x, vector<float>& y) 
{
  smooth1(_ei,_zs,_a1,x,y);
}

float RecursiveExponentialFilter::aFromSigma(double sigma) 
{
  if (sigma<=0.0f)
    return 0.0f;
  double ss = sigma*sigma;
  return (float)((1.0+ss-sqrt(1.0+2.0*ss))/ss);
}

// Smooth a 1D array.
void RecursiveExponentialFilter::smooth1
  (bool ei, bool zs, float a, const vector<float>& x, vector<float>& y) 
{
  if (a==0.0f) {
    y = x;
  } else if (ei) {
    smooth1Ei(zs,a,x,y);
  } else {
    smooth1Eo(zs,a,x,y);
  }
}

// Smooth a 1D array for input boundary conditions.
void RecursiveExponentialFilter::smooth1Ei(bool zs, float a, const vector<float>& x, vector<float>& y)
{
  int n1 = x.size();
  float b = 1.0f-a;
  float sx = zs?1.0f:b;
  float sy = a;
  float yi = y[0] = sx*x[0];
  for (int i1=1; i1<n1-1; ++i1)
    y[i1] = yi = a*yi+b*x[i1];
  sx /= 1.0f+a;
  sy /= 1.0f+a;
  y[n1-1] = yi = sy*yi+sx*x[n1-1];
  for (int i1=n1-2; i1>=0; --i1)
    y[i1] = yi = a*yi+b*y[i1];
}

// Smooth a 1D array for output boundary conditions.
// Adapted from Algorithm 4.1 in Boisvert, R.F., Algorithms for
// special tridiagonal systems: SIAM J. Sci. Stat. Comput., v. 12,
// no. 2, pp. 423-442.
void RecursiveExponentialFilter::smooth1Eo(bool zs, float a, const vector<float>& x, vector<float>& y)
{
  int n1 = x.size();
  float aa = a*a;
  float ss = zs?1.0f-a:1.0f;
  float gg = zs?aa-a:aa;
  float c = (1.0f-aa-ss)/ss;
  float d = 1.0f/(1.0f-aa+gg*(1.0f+c*pow(aa,n1-1)));
  float e = (1.0f-a)*(1.0f-a)*FLT_EPSILON/4.0f;

  // copy scaled input to output
  //mul((1.0f-a)*(1.0f-a),x,y);
  y.resize(x.size());
  for (int i = 0; i < x.size(); i++)
  {
    y[i] = x[i] * (1.0f-a)*(1.0f-a);
  }

  // reversed triangular factorization
  int k1 = min((int)ceil(log(e)/log(a)),2*n1-2); // 1 <= k1 <= 2*n1-2
  float ynm1 = 0.0f;
  int m1 = k1-n1+1; // 2-n1 <= m1 <= n1-1
  for (int i1=m1; i1>0; --i1)
    ynm1 = a*ynm1+y[i1];
  ynm1 *= c;
  if (n1-k1<1)
    ynm1 = a*ynm1+(1.0f+c)*y[0];
  m1 = max(n1-k1,1); // 1 <= m1 <= n1-1
  for (int i1=m1; i1<n1; ++i1)
    ynm1 = a*ynm1+y[i1];
  ynm1 *= d;

  // reverse substitution
  y[n1-1] -= gg*ynm1;
  for (int i1=n1-2; i1>=0; --i1)
    y[i1] += a*y[i1+1];
  y[0] /= ss;

  // forward substitution
  for (int i1=1; i1<n1-1; ++i1)
    y[i1] += a*y[i1-1];
  y[n1-1] = ynm1;
}


