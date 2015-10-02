/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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

import edu.mines.jtk.util.Cdouble;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Butterworth filter.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.10
 */
public class ButterworthFilter extends RecursiveCascadeFilter {

  /**
   * Filter type.
   */
  public enum Type {LOW_PASS, HIGH_PASS}

  /**
   * Construct a Butterworth filter with specified parameters.
   * The filter is specified by amplitudes at two frequencies. The
   * frequencies are in normalized units of cycles/sample. Either a 
   * low-pass or high-pass filter is constructed, depending on which 
   * of the corresponding two amplitudes is smaller. The filter is 
   * designed to match the larger (pass band) amplitude exactly, but 
   * may have amplitude lower than the smaller (reject band) amplitude.
   * @param fl the low frequency at which the amplitude al is specified.
   *  The low frequency fl must be greater than 0.0 and less than fh.
   * @param al the amplitude at the specified low frequency fl.
   *  The amplitude al must be greater than 0.0, less than 1.0, and
   *  not equal to the amplitude ah.
   * @param fh the high frequency at which the amplitude ah is is specified.
   *  The high frequency fh must be less than 0.5 and greater than fl.
   * @param ah the amplitude at the specified high frequency fh.
   *  The amplitude ah must be greater than 0.0, less than 1.0, and
   *  not equal to the amplitude al.
   */
  public ButterworthFilter(double fl, double al, double fh, double ah) {
    Check.argument(0.0<fl,"0.0<fl");
    Check.argument(fl<fh,"fl<fh");
    Check.argument(fh<0.5,"fh<0.5");
    Check.argument(0.0<al,"0.0<al");
    Check.argument(al<1.0,"al<1.0");
    Check.argument(al!=ah,"al!=ah");
    Check.argument(0.0<ah,"0.0<ah");
    Check.argument(ah<1.0,"ah<1.0");
    double wl = 2.0*DBL_PI*fl;
    double wh = 2.0*DBL_PI*fh;
    double xl = 2.0*tan(wl/2.0);
    double xh = 2.0*tan(wh/2.0);
    double pl = al*al;
    double ph = ah*ah;
    if (al>=ah) {
      int np = (int)ceil(0.5*log((pl*(1-ph))/(ph*(1-pl)))/log(xh/xl));
      double xc = xl*pow(pl/(1-pl),0.5/np);
      double wc = 2.0*atan(xc/2.0);
      double fc = 0.5*wc/DBL_PI;
      makePolesZerosGain(fc,np,Type.LOW_PASS);
    } else {
      int np = (int)ceil(0.5*log((ph*(1-pl))/(pl*(1-ph)))/log(xh/xl));
      double xc = xh*pow((1-ph)/ph,0.5/np);
      double wc = 2.0*atan(xc/2.0);
      double fc = 0.5*wc/DBL_PI;
      makePolesZerosGain(fc,np,Type.HIGH_PASS);
    }
    init(_poles,_zeros,_gain);
  }

  /**
   * Construct Butterworth filter with specified parameters.
   * @param fc the cutoff (half-power) frequency, in cycles per sample.
   *  At this cutuff frequency, the filter amplitude squared equals 0.5.
   *  The cutoff frequency must be greater than 0.0 and less than 0.5.
   * @param np the number of poles in the recursive filter.
   * @param type the filter type.
   */
  public ButterworthFilter(double fc, int np, Type type) {
    Check.argument(0.0<fc,"0.0<fc");
    Check.argument(fc<0.5,"fc<0.5");
    Check.argument(np>0,"np>0");
    makePolesZerosGain(fc,np,type);
    init(_poles,_zeros,_gain);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Cdouble[] _poles;
  private Cdouble[] _zeros;
  private double _gain;

  private void makePolesZerosGain(double fc, int np, Type type) {
    boolean lowpass = type==Type.LOW_PASS;
    double omegac = 2.0*tan(DBL_PI*fc);
    double dtheta = DBL_PI/np;
    double ftheta = 0.5*dtheta*(np+1);
    _poles = new Cdouble[np];
    _zeros = new Cdouble[np];
    Cdouble c1 = new Cdouble(1.0,0.0);
    Cdouble c2 = new Cdouble(2.0,0.0);
    Cdouble zj = (lowpass)?c1.neg():c1;
    Cdouble gain = new Cdouble(c1);
    for (int j=0,k=np-1; j<np; ++j,--k) {
      double theta = ftheta+j*dtheta;
      Cdouble sj = Cdouble.polar(omegac,theta);
      _zeros[j] = zj;
      if (j==k) {
        _poles[j] = (c2.plus(sj)).over(c2.minus(sj));
        _poles[j].i = 0.0;
      } else if (j<k) {
        _poles[j] = (c2.plus(sj)).over(c2.minus(sj));
        _poles[k] = _poles[j].conj();
      }
      if (lowpass) {
        gain.timesEquals(sj.over(sj.minus(c2)));
      } else {
        gain.timesEquals(c2.over(c2.minus(sj)));
      }
    }
    _gain = gain.r;
  }
}
