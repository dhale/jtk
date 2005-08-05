/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;


import static java.lang.Math.*;

import junit.framework.*;
import edu.mines.jtk.dsp.*;
import static edu.mines.jtk.util.Array.*;

/**
 * Tests {@link edu.mines.jtk.dsp.SincInterpolator}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.08.01
 */
public class SincInterpolatorTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(SincInterpolatorTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testErrorAndFrequency() {
    double[] emaxs = {0.001,0.01,0.1};
    double[] fmaxs = {0.10,0.30,0.325,0.40,0.45};
    for (int iemax=0; iemax<emaxs.length; ++iemax) {
      double emax = emaxs[iemax];
      for (int ifmax=0; ifmax<fmaxs.length; ++ifmax) {
        double fmax = fmaxs[ifmax];
        SincInterpolator si = SincInterpolator.fromErrorAndFrequency(emax,fmax);
        test(si);
      }
    }
  }

  public void testErrorAndLength() {
    double[] emaxs = {0.001,0.01,0.1};
    int[] lmaxs = {2,4,6,8,10,12,14,16};
    for (int iemax=0; iemax<emaxs.length; ++iemax) {
      double emax = emaxs[iemax];
      for (int ilmax=0; ilmax<lmaxs.length; ++ilmax) {
        int lmax = lmaxs[ilmax];
        SincInterpolator si = SincInterpolator.fromErrorAndLength(emax,lmax);
        test(si);
      }
    }
  }

  public void testFrequencyAndLength() {
    double[] fmaxs = {0.10,0.30,0.325,0.40,0.45};
    int[] lmaxs = {2,4,6,8,10,12,14,16};
    for (int ifmax=0; ifmax<fmaxs.length; ++ifmax) {
      double fmax = fmaxs[ifmax];
      for (int ilmax=0; ilmax<lmaxs.length; ++ilmax) {
        int lmax = lmaxs[ilmax];
        if ((1.0-2.0*fmax)*lmax>1.0) {
          SincInterpolator si = 
            SincInterpolator.fromFrequencyAndLength(fmax,lmax);
          test(si);
        }
      }
    }
  }

  private void test(SincInterpolator si) {

    // Interpolator design parameters.
    int lmax = si.getMaximumLength();
    double fmax = si.getMaximumFrequency();
    double emax = si.getMaximumError();

    // Sampling and arrays for input signal.
    int nxin = 2+lmax;
    double dxin = 0.1;
    double fxin = (-lmax/2)*dxin;
    float[] yin = new float[nxin];

    // Sampling for interpolated output.
    int nxout = 101;
    double dxout = dxin/(nxout-1);
    double fxout = 0.0;

    // Loop over frequencies up to maximum frequency.
    int nk = 101;
    double dk = 2.0*PI*fmax/((nk-1)*dxin);
    double fk = 0.0;
    for (int ik=0; ik<nk; ++ik) {
      double k = fk+ik*dk;

      // Input sine.
      for (int ixin=0; ixin<nxin; ++ixin) {
        double x = fxin+ixin*dxin;
        yin[ixin] = (float)sin(k*x);
      }

      // Interpolated sine.
      boolean fail = false;
      si.setInput(nxin,dxin,fxin,yin);
      for (int ixout=0; ixout<nxout; ++ixout) {
        double x = fxout+ixout*dxout;
        float y = si.interpolate(x);
        if (abs(sin(k*x)-y)>emax) {
          System.out.println("sin: k="+k+" x="+x+" y="+y+" e="+abs(sin(k*x)-y));
          fail = true;
        }
        assertEquals(sin(k*x),y,emax);
      }
      //assertEquals(false,fail);
    }
  }
}
