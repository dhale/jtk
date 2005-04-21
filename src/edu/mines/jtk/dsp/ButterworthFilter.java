/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Butterworth filter.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.10
 */
public class ButterworthFilter extends RecursiveCascadeFilter {

  /**
   * Construct a Butterworth filter with specified parameters.
   * The filter is specified amplitudes at two frequencies. Either a 
   * low-pass of high-pass filter is constructed, depending on which 
   * of the two amplitudes is smaller. The filter is designed to match
   * the larger (pass band) amplitude exactly, but may have amplitude
   * lower than the smaller (reject band) amplitude.
   * @param fl the low frequency at which the amplitude al is specified.
   *  The low frequency fl must be greater than zero and less than fh.
   * @param al the amplitude at the specified low frequency fl.
   *  The amplitude al must not equal the amplitude ah.
   * @param fh the high frequency at which the amplitude ah is is specified.
   *  The high frequency fh must be less than 0.5 and greater than fl.
   * @param ah the amplitude at the specified high frequency fh.
   *  The amplitude ah must not equal the amplitude al.
   */
  public ButterworthFilter(float fl, float al, float fh, float ah) {
    Check.argument(0.0f<fl,"0.0<fl");
    Check.argument(fl<fh,"fl<fh");
    Check.argument(fh<0.5f,"fh<0.5");
    Check.argument(al!=ah,"al!=ah");
    if (al>=ah) {
      double wl = 2.0*DBL_PI*fl;
      double wh = 2.0*DBL_PI*fh;
      double xl = 2.0*tan(wl/2.0);
      double xh = 2.0*tan(wh/2.0);
      double pl = al*al;
      double ph = ah*ah;
      int np = (int)ceil(0.5*log((pl*(1-ph))/(ph*(1-pl)))/log(xh/xl));
      double xc = xl*pow(pl/(1-pl),0.5/np);
      double wc = 2.0*atan(xc/2.0);
      double fc = 0.5*wc/DBL_PI;
      designLowPass(fc,np);
    } else {
      // TODO:
    }
  }

  /**
   * Construct Butterworth filter with specified parameters.
   * @param fc the cutoff frequency, in cycles per sample.
   *  The power spectrum at the cutoff frequency equals 1/2.
   * @param np the number of poles in the recursive filter.
   * @param lowpass true, for low-pass filter; false, for high-pass filter.
   */
  public ButterworthFilter(double fc, int np, boolean lowpass) {
    designLowPass(fc,np);
    System.out.println("npoles="+_poles.length);
    System.out.println("nzeros="+_zeros.length);
    System.out.println("gain="+_gain);
    for (int ip=0; ip<np; ++ip)
      System.out.println("pole["+ip+"] = "+_poles[ip]);
    for (int ip=0; ip<np; ++ip)
      System.out.println("zero["+ip+"] = "+_zeros[ip]);
    init(_poles,_zeros,_gain);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Complex[] _poles;
  private Complex[] _zeros;
  private float _gain;

  private void designLowPass(double fc, int np) {
    Check.argument(fc>=0.0f,"fc>=0.0");
    Check.argument(fc<=0.5f,"fc<=0.5");
    Check.argument(np>0,"np>0");
    float omegac = 2.0f*tan(FLT_PI*(float)fc);
    System.out.println("omegac="+omegac);
    float dtheta = FLT_PI/(float)np;
    float ftheta = 0.5f*dtheta*(float)(np+1);
    _poles = new Complex[np];
    _zeros = new Complex[np];
    Complex c1 = new Complex(1.0f,0.0f);
    Complex c2 = new Complex(2.0f,0.0f);
    Complex zj = new Complex(-1.0f,0.0f);
    Complex gain = new Complex(c1);
    for (int j=0,k=np-1; j<np; ++j,--k) {
      float theta = ftheta+(float)j*dtheta;
      Complex sj = Complex.polar(omegac,theta);
      System.out.println("j="+j+" sj="+sj);
      _zeros[j] = zj;
      _zeros[k] = zj;
      if (j==k) {
        _poles[j] = (c2.plus(sj)).over(c2.minus(sj));
        _poles[j].i = 0.0f;
      } else if (j<k) {
        _poles[j] = (c2.plus(sj)).over(c2.minus(sj));
        _poles[k] = _poles[j].conj();
      }
      gain.timesEquals(sj.over(sj.minus(c2)));
    }
    _gain = gain.r;
  }
}
