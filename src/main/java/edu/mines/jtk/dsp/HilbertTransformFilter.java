/****************************************************************************
Copyright 2010, Colorado School of Mines and others.
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
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Hilbert transform of band-limited uniformly-sampled functions.
 * This filter is also known as a 90-degree phase shifter.
 * <p>
 * The sign of the transform (the sign of the 90-degree phase shift)
 * is the same as that for a derivative filter, such that the Hilbert
 * transform of sin(t) is cos(t).
 * <p>
 * The ideal Hilbert transform filter is infinitely long. The
 * length of the filter used here is chosen to yield less than a
 * specified maximum error for frequencies between specified lower
 * and upper bounds.
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.06.01
 */
public class HilbertTransformFilter {
  
  /**
   * Constructs a Hilbert transform filter that yields less than 1% error
   * for frequencies between 5% and 95% of the Nyquist frequency.
   */
  public HilbertTransformFilter() {
    _filter = design(NMAX_DEFAULT,EMAX_DEFAULT,FMIN_DEFAULT,FMAX_DEFAULT);
  }
   
  /**
   * Constructs a Hilbert transform filter that yields less than a specified
   * maximum error for frequencies between specified lower and upper bounds.
   * @param nmax maximum number of coefficients in filter.
   * Must be greater than 0.
   * @param emax maximum error. 
   * Must be greater than 0.0.
   * If the computed filter has fewer than nmax coefficients, then for 
   * frequencies between fmin and fmax, the amplitude spectrum of each 
   * filter will be bounded by 1-emax and 1+emax. 
   * The length of the filter grows approximately as the logarithm of 1/emax.
   * @param fmin minimum frequency (in cycles/sample)
   * Must be greater than 0.0.
   * The length of the filter grows approximately as 1/fmin.
   * @param fmax maximum frequency (in cycles/sample).
   * Must be less than the 0.5 (the Nyquist frequency).
   * The length of the filter grows approximately as 1/(0.5-fmax).
   */
  public HilbertTransformFilter(int nmax, float emax, 
                                float fmin, float fmax) 
  {
    _filter = design(nmax,emax,fmin,fmax);
  }
  
  /**
   * Applies this Hilbert transform filter.
   * @param n number of samples in input/output arrays.
   * @param x array[n] of input samples.
   * @param y array[n] of output samples.
   */
  public void apply(int n, float[] x, float[] y) {
    Conv.conv(_filter.length,-(_filter.length-1)/2,_filter,n,0,x,n,0,y);
  }
  
  /**
   * Gets the length of this Hilbert transform filter.
   * @return filter length.
   */
  public int length() {
    return _filter.length;
  }
  
  ///////////////////////////////////////////////////////////////////////////
  // private
  
  private static final int NMAX_DEFAULT = 100000; // default max length.
  private static final float EMAX_DEFAULT = 0.010f; // default max error.
  private static final float FMIN_DEFAULT = 0.025f; // default min frequency.
  private static final float FMAX_DEFAULT = 0.475f; // default max frequency.
  private float[] _filter;

  private static float idealFilter(float x) {
    if (x==0.0f) return 0.0f;
    float y = 0.5f*FLT_PI*x;
    float s = sin(y);
    return -s*s/y;
  }
  
  /**
   * Designs a Hilbert transform filter that yields less than 1% error
   * for frequencies between 5% and 95% of the Nyquist frequency.
   * @return array of filter coefficients.
   * The length of the array will be odd, and the coefficients will have
   * odd symmetry about the sample with array index (length-1)/2.
   */
  private static float[] design() {
    return design(NMAX_DEFAULT,EMAX_DEFAULT,FMIN_DEFAULT,FMAX_DEFAULT);
  }
  
  /**
   * Designs a Hilbert transform filter that yields less than a specified
   * maximum error for frequencies between specified lower and upper bounds.
   * @param nmax maximum number of coefficients in filter.
   * Must be greater than 0.
   * @param emax maximum error. 
   * Must be greater than 0.0.
   * If the computed filter has fewer than nmax coefficients, then for 
   * frequencies between fmin and fmax, the amplitude spectrum of each 
   * filter will be bounded by 1-emax and 1+emax. 
   * The length of the filter grows approximately as the logarithm of 1/emax.
   * @param fmin minimum frequency (in cycles/sample)
   * Must be greater than 0.0.
   * The length of the filter grows approximately as 1/fmin.
   * @param fmax maximum frequency (in cycles/sample).
   * Must be less than the 0.5 (the Nyquist frequency).
   * The length of the filter grows approximately as 1/(0.5-fmax).
   * @return array of filter coefficients.
   * The length of the array will be odd, and the coefficients will have
   * odd symmetry about the sample with array index (length-1)/2.
   */
  private static float[] design(int nmax, float emax, float fmin, float fmax) {
    Check.argument(nmax>0,"nmax>0");
    Check.argument(emax>0.0f,"emax>0.0f");
    Check.argument(fmin>0.0f,"fmin>0.0f");
    Check.argument(fmax<0.5f,"fmax<0.5f");
    Check.argument(fmin<fmax,"fmin<fmax");
    if (fmin<0.5f-fmax) fmax = 0.5f-fmin;
    float width = 2.0f*(0.5f-fmax);
    float emaxWindow = 0.5f*emax;
    KaiserWindow kw = KaiserWindow.fromErrorAndWidth(emaxWindow,width);
    int n = 1+(int)ceil(kw.getLength());
    if (n%2==0) ++n;
    if (n>nmax) n = nmax;
    if (n%2==0) --n;
    float length = (float)n;
    kw = KaiserWindow.fromWidthAndLength(width,length);
    float[] f = new float[n];
    int k = (n-1)/2;
    for (int i=0,j=n-1; i<k; ++i,--j) {
      float x = i-k;
      float fideal = idealFilter(x);
      float window = (float)kw.evaluate(x);
      f[i] = fideal*window;
      f[j] = -f[i];
    }
    f[k] = 0.0f;
    return f;
  }
}

