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

  public ButterworthFilter(float fc, int np) {
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

  private void designLowPass(float fc, int np) {
    Check.argument(fc>=0.0f,"fc>=0.0");
    Check.argument(fc<=0.5f,"fc<=0.5");
    Check.argument(np>0,"np>0");
    float omegac = 2.0f*tan(FLT_PI*fc);
    System.out.println("omegac="+omegac);
    float dtheta = FLT_PI/(float)np;
    float ftheta = 0.5f*dtheta*(float)(np+1);
    _poles = new Complex[np];
    _zeros = new Complex[np];
    Complex c1 = new Complex(1.0f,0.0f);
    Complex c2 = new Complex(2.0f,0.0f);
    Complex zj = new Complex(-1.0f,0.0f);
    Complex gain = new Complex(c1);
    for (int j=0,k=np-1; j<=k; ++j,--k) {
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
