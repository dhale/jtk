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
    double[] emaxs = {0.1,0.01,0.001};
    double[] fmaxs = {0.10,0.30,0.325,0.40,0.45};
    for (int iemax=0; iemax<emaxs.length; ++iemax) {
      double emax = emaxs[iemax];
      for (int ifmax=0; ifmax<fmaxs.length; ++ifmax) {
        double fmax = fmaxs[ifmax];
        SincInterpolator si = SincInterpolator.fromErrorAndFrequency(emax,fmax);
        testInterpolator(si);
      }
    }
  }

  public void testErrorAndLength() {
    double[] emaxs = {0.1,0.01,0.001};
    int[] lmaxs = {2,4,6,8,10,12,14,16};
    for (int iemax=0; iemax<emaxs.length; ++iemax) {
      double emax = emaxs[iemax];
      for (int ilmax=0; ilmax<lmaxs.length; ++ilmax) {
        int lmax = lmaxs[ilmax];
        SincInterpolator si = SincInterpolator.fromErrorAndLength(emax,lmax);
        testInterpolator(si);
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
          testInterpolator(si);
        }
      }
    }
  }

  private void testInterpolator(SincInterpolator si) {

    // Interpolator design parameters.
    int lmax = si.getMaximumLength();
    double fmax = si.getMaximumFrequency();
    double emax = si.getMaximumError();
    //System.out.println("lmax="+lmax+" fmax="+fmax+" emax="+emax);
    if (fmax==0.0)
      return;

    // Sampling and arrays for input signal.
    int nxin = 2+lmax;
    double dxin = 0.1;
    double fxin = (-lmax/2)*dxin;
    float[] yin = new float[nxin];

    // Sampling for interpolated output.
    int nxout = 51;
    double dxout = dxin/(nxout-1);
    double fxout = 0.0;
    float[] xout = new float[nxout];
    float[] yout = new float[nxout];
    for (int ixout=0; ixout<nxout; ++ixout) {
      double x = fxout+ixout*dxout;
      xout[ixout] = (float)x;
    }

    // Loop over frequencies near maximum frequency.
    int nk = 51;
    double fk = 0.8*fmax;
    double dk = 2.0*PI*(fmax-fk)/((nk-1)*dxin);
    for (int ik=0; ik<nk; ++ik) {
      double k = fk+ik*dk;

      // Input signal.
      for (int ixin=0; ixin<nxin; ++ixin) {
        double x = fxin+ixin*dxin;
        yin[ixin] = signal(k*x);
      }

      // Interpolated signal, one output sample at a time.
      si.setInput(nxin,dxin,fxin,yin);
      for (int ixout=0; ixout<nxout; ++ixout) {
        double x = fxout+ixout*dxout;
        float yi = si.interpolate(x);
        float ye = signal(k*x);
        assertEquals(ye,yi,2.0*emax);
      }

      // Interpolated signal, all output samples at once.
      si.interpolate(nxout,xout,yout);
      for (int ixout=0; ixout<nxout; ++ixout) {
        double x = fxout+ixout*dxout;
        float yi = yout[ixout];
        float ye = signal(k*x);
        assertEquals(ye,yi,2.0*emax);
      }
    }
  }

  private static float signal(double x) {
    return (float)sin(0.25*PI+x);
  }

  ///////////////////////////////////////////////////////////////////////////
  // more test functions; may be useful for debugging

  private static void testInterpolator2(SincInterpolator si) {
    int lmax = si.getMaximumLength();
    double fmax = si.getMaximumFrequency();
    double emax = si.getMaximumError();
    System.out.println("lmax="+lmax+" fmax="+fmax+" emax="+emax);
    if (fmax==0.0)
      return;
    if (lmax<6)
      return;

    int nxin = 1;
    double dxin = 1.0;
    double fxin = 0.0;
    float[] yin = new float[nxin];
    yin[0] = 1.0f;
    si.setInput(nxin,dxin,fxin,yin);

    int nxout = lmax;
    double dxout = 1.0;
    float[] yout =  new float[nxout];

    int nxfft = FftReal.nfftFast(100*nxout);
    FftReal fft = new FftReal(nxfft);
    int nk = nxfft/2+1;
    double dk = 2.0*PI/nxfft;
    double fk = 0.0;
    double ikmax = (int)(2.0*PI*fmax/dk);
    float[] yk = new float[2*nk];

    int ntest = 11;
    double dtest = 1.0/max(1,(ntest-1));
    double ftest = 0.0;
    for (int itest=0; itest<ntest; ++itest) {
      double fxout = -lmax/2.0+itest*dtest;
      for (int ixout=0; ixout<nxout; ++ixout)
        yout[ixout] = si.interpolate(fxout+ixout*dxout);
      zero(yk);
      copy(nxout,yout,yk);
      fft.realToComplex(1,yk,yk);
      float[] ak = cabs(yk);
      for (int ik=0; ik<=ikmax; ++ik) {
        double k = fk+ik*dk;
        if (abs(1.0-ak[ik])>emax) {
          double f = k*0.5/PI;
          double x = fxout+lmax/2;
          double a = ak[ik];
          System.out.println("f="+f+" x="+x+" a="+a);
          for (int ixout=0; ixout<nxout; ++ixout)
            System.out.println("yout["+ixout+"]="+yout[ixout]);
          break;
        }
        //assertEquals(1.0,ak[ik],emax);
      }
    }
  }

  public void xtestDesign() {
    int m = 100;
    int lsinc = 8;
    double xmax = lsinc/2;
    double kmax = 0.325;
    double wwin = 1.0-2.0*kmax;
    double lwin = lsinc;
    KaiserWindow kwin = KaiserWindow.fromWidthAndLength(wwin,lwin);
    double ewin = kwin.getError();
    System.out.println("wwin="+wwin+" lwin="+lwin+" ewin="+ewin);

    int nx = m*lsinc;
    double dx = 1.0/m;
    double fx = 0.0;
    int nxfft = FftReal.nfftFast(10*nx);
    int nk = nxfft/2+1;
    double dk = 2.0*PI/(nxfft*dx);
    double fk = 0.0;
    FftReal fft = new FftReal(nxfft);
    System.out.println("nxfft="+nxfft);

    float[] sx = new float[nxfft];
    sx[0] = 1.0f;
    for (int ix=1,jx=nxfft-1; ix<nx/2; ++ix,--jx) {
      double x = fx+ix*dx;
      sx[ix] = (float)(x<=xmax?sinc(x)*kwin.evaluate(x):0.0);
      sx[jx] = sx[ix];
      //System.out.println("s("+x+") = "+sx[ix]);
    }

    float[] sk = new float[2*nk];
    fft.realToComplex(-1,sx,sk);
    float[] ak = cabs(sk);
    ak = mul(ak,(float)(1.0/m));
    for (int ik=0; ik<=nk/10; ++ik) {
      double k = (fk+ik*dk)/(2.0*PI);
      if (k<=kmax && abs(ak[ik]-1.0)>ewin)
        System.out.println("a("+k+") = "+ak[ik]);
    }

    float[] tx = new float[nxfft];
    for (int ix=0,jx=m/2; ix<lsinc/2; ++ix,jx+=m) {
      tx[        ix] = sx[jx];
      tx[nxfft-1-ix] = sx[nxfft-jx];
      System.out.println("tx["+ix+"] = "+          tx[ix]);
      System.out.println("tx["+(nxfft-1-ix)+"] = "+tx[nxfft-1-ix]);
    }

    float[] tk = new float[2*nk];
    fft.realToComplex(-1,tx,tk);
    ak = cabs(tk);
    for (int ik=0; ik<nk; ++ik) {
      double k = (fk+ik*dk)/(2.0*PI*m);
      if (k<=kmax && abs(ak[ik]-1.0)>2.0*ewin)
        System.out.println("a("+k+") = "+ak[ik]);
    }
  }

  private double sinc(double x) {
    return (x==0.0)?1.0:sin(PI*x)/(PI*x);
  }
}
