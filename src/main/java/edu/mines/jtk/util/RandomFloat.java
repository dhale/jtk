/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
 * Pseudo-random float generator. Compared with the standard Java class
 * Random, this class generates generates random floats more quickly and
 * perhaps more randomly (certainly with greater periodicity).
 *
 * The uniform generator was 
 * adapted from subroutine uni in "Numerical Methods and Software",
 * D. Kahaner, C. Moler, S. Nash, Prentice Hall, 1988.  This book references,
 * Marsaglia G., "Comments on the perfect uniform random number generator",
 * Unpublished notes, Wash S. U.  According to the reference, this random
 * number generator "passes all known tests and has a period that is ...
 * approximately 10^19".
 *
 * The normal (Gaussian) generator was
 * adapted from subroutine rnor in "Numerical Methods and Software", D.
 * Kahaner, C. Moler, S. Nash, Prentice Hall, 1988.  Subroutine rnor,
 * in turn, is adapted from a paper by Marsaglia G. and Tsang, W. W., 1984,
 * A fast, easily implemented method for sampling from decreasing or symmetric
 * unimodal density functions:  SIAM J. Sci. Stat. Comput., v. 5, no. 2,
 * p. 349-359.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 1997.02.21, 2006.07.13
 */
public class RandomFloat {

  /**
   * Constructs a random float generator with default seed.
   */
  public RandomFloat() {
  }

  /**
   * Constructs a random float generator.
   *@param seed the seed with which to initialize the generator.
   */
  public RandomFloat(int seed) {
    setSeed(seed);
  }

  /**
   * Gets a pseudo-random float from a uniform [0,1) distribution.
   * The float is between 0.0f (inclusive) and 1.0f (exclusive).
   *@return the pseudo-random float.
   */
  public final float uniform() {

    // Basic generator is Fibonacci.
    float uni = _u[_i]-_u[_j];
    if (uni<0.0) uni += 1.0;
    _u[_i] = uni;
    if (--_i<0) _i = 16;
    if (--_j<0) _j = 16;

    // Second generator is congruential.
    _c -= CD;
    if (_c<0.0) _c += CM;

    // Combination generator.
    uni -= _c;
    if (uni<0.0) uni += 1.0f;
    return uni;
  }

  /**
   * Gets a pseudo-random float from a normal (Gaussian) distribution.
   * The N(0,1) distribution has zero mean and unit variance.
   *@return the pseudo-random float.
   */
  public float normal() {

    // uni is uniform on [0,1).
    float uni = _u[_i]-_u[_j];
    if (uni<0.0) uni += 1.0;
    _u[_i] = uni;
    if (--_i<0) _i = 16;
    if (--_j<0) _j = 16;

    // vni is uniform on [-1,1).
    float vni = uni+uni-1.0f;

    // k is in range [0,63].
    int k = ((int)(_u[_i]*128))%64;

    // Fast path.
    float rnor = vni*_v[k+1];
    if (rnor<=_v[k] && -rnor<=_v[k]) return rnor;

    // Slow path.
    float x = (Math.abs(rnor)-_v[k])/(_v[k+1]-_v[k]);
    float y = fib();
    float s = x+y;
    if (s<=C2) {
      if (s<=C1) return rnor;
      float bmbx = B-B*x;
      if (y<=C-AA*Math.exp(-0.5f*bmbx*bmbx)) {
        if (Math.exp(-0.5f*_v[k+1]*_v[k+1])+y*PC/_v[k+1] <= 
          Math.exp(-0.5f*rnor*rnor)) return rnor;
        do {
          y = fib();
          x = OXN*(float)Math.log(y);
          y = fib();
        } while (-2.0f*Math.log(y)<=x*x);
        float xnmx = XN-x;
        return (rnor>=0.0f?Math.abs(xnmx):-Math.abs(xnmx));
      }
    }
    float bmbx = B-B*x;	
    return (rnor>=0.0?Math.abs(bmbx):-Math.abs(bmbx));
  }

  /**
   * Seeds the random number generator.
   * Different seeds yield different sequences of random numbers.
   *@param seed the seed.
   */
  public void setSeed(int seed) {

    // Convert seed to four smallish positive integers.
    if (seed<0) seed = -seed;
    int i1 = (seed%177)+1;
    int j1 = (seed%167)+1;
    int k1 = (seed%157)+1;
    int l1 = (seed%147)+1;

    // Generate random bit pattern in array based on given seed.
    for (int ii=0; ii<17; ++ii) {
      float s = 0.0f;
      float t = 0.5f;

      // Loop over bits in the float mantissa.
      for (int jj=0; jj<NBITS; ++jj) {
        int m1 = (((i1*j1)%179)*k1)%179;
        i1 = j1;
        j1 = k1;
        k1 = m1;
        l1 = (53*l1+1)%169;
        if (((l1*m1)%64)>=32) s += t;
        t *= 0.5;
      }
      _u[ii] = s;
    }

    // Initialize generators.
    _i = 16;
    _j = 4;
    _c = CS;
  }

  private float fib() {
    float r = _u[_i]-_u[_j];
    if (r<0.0f) r += 1.0f;
    _u[_i] = r;
    if (--_i<0) _i = 16;
    if (--_j<0) _j = 16;
    return r;
  }

  // Constants and state for uniform generator (16777216=2^24).
  private static final float CS = 362436.0f/16777216.0f;
  private static final float CD = 7654321.0f/16777216.0f;
  private static final float CM = 16777213.0f/16777216.0f;
  private static final int NBITS = 24;
  private int _i = 16;
  private int _j = 4;
  private float _c = CS;
  private float[] _u = {
    0.8668672834288f,  0.3697986366357f,  0.8008968294805f,
    0.4173889774680f,  0.8254561579836f,  0.9640965269077f,
    0.4508667414265f,  0.6451309529668f,  0.1645456024730f,
    0.2787901807898f,  0.06761531340295f, 0.9663226330820f,
    0.01963343943798f, 0.02947398211399f, 0.1636231515294f,
    0.3976343250467f,  0.2631008574685f
  };

  // Constants and additional state for normal generator.
  // Normal generator uses state of uniform generator.
  private static final float AA = 12.37586f;
  private static final float B = 0.4878992f;
  private static final float C = 12.67706f;
  private static final float C1 = 0.9689279f;
  private static final float C2 = 1.301198f;
  private static final float PC = 0.01958303f;
  private static final float XN = 2.776994f;
  private static final float OXN = 0.3601016f;
  private static float _v[]={
    0.3409450f, 0.4573146f, 0.5397793f, 0.6062427f, 0.6631691f,
    0.7136975f, 0.7596125f, 0.8020356f, 0.8417227f, 0.8792102f, 0.9148948f,
    0.9490791f, 0.9820005f, 1.0138492f, 1.0447810f, 1.0749254f, 1.1043917f,
    1.1332738f, 1.1616530f, 1.1896010f, 1.2171815f, 1.2444516f, 1.2714635f,
    1.2982650f, 1.3249008f, 1.3514125f, 1.3778399f, 1.4042211f, 1.4305929f,
    1.4569915f, 1.4834526f, 1.5100121f, 1.5367061f, 1.5635712f, 1.5906454f,
    1.6179680f, 1.6455802f, 1.6735255f, 1.7018503f, 1.7306045f, 1.7598422f,
    1.7896223f, 1.8200099f, 1.8510770f, 1.8829044f, 1.9155830f, 1.9492166f,
    1.9839239f, 2.0198430f, 2.0571356f, 2.0959930f, 2.1366450f, 2.1793713f,
    2.2245175f, 2.2725185f, 2.3239338f, 2.3795007f, 2.4402218f, 2.5075117f,
    2.5834658f, 2.6713916f, 2.7769943f, 2.7769943f, 2.7769943f, 2.7769943f
  };
}
