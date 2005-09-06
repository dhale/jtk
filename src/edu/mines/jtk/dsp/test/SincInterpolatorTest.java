/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;


import java.util.Random;
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

  //private double[] _emaxs = {0.1,0.01,0.001,0.0001}; // takes too long
  private double[] _emaxs = {0.1,0.01,0.001};
  private double[] _fmaxs = {0.10,0.30,0.40,0.45};
  private int[] _lmaxs = {8,10,12,14,16};

  public void testKenLarner() {
    for (int lmax=8; lmax<=16; lmax+=2) {
      trace("testKenLarner: lmax="+lmax);
      SincInterpolator si = SincInterpolator.fromKenLarner(lmax);
      testInterpolator(si);
    }
  }

  public void testExtrapolation() {
    SincInterpolator si = new SincInterpolator();
    Random random = new Random();
    int lmax = si.getMaximumLength();
    int nxin = 2*lmax;
    int npad = lmax;
    double dxin = 1.0;
    double fxin = npad;
    int nxout = npad+nxin+npad;
    double dxout = 0.999;
    double fxout = npad;
    float[] yi = new float[nxin];
    float[] yz = new float[nxout];
    float[] yc = new float[nxout];
    float[] yo = new float[nxout];
    float[] yt = new float[nxout];
    for (int ixin=0; ixin<nxin; ++ixin)
      yi[ixin] = yz[ixin+npad] = yc[ixin+npad] = random.nextFloat();
    for (int ipad=0; ipad<npad; ++ipad) {
      yc[ipad] = yc[npad];
      yc[npad+nxin+ipad] = yc[npad+nxin-1];
    }
    si.setExtrapolation(SincInterpolator.Extrapolation.ZERO);
    si.setInput(nxin,dxin,fxin,yi);
    si.interpolate(nxout,dxout,fxout,yo);
    si.setInput(npad+nxin+npad,dxin,0.0,yz);
    si.interpolate(nxout,dxout,fxout,yt);
    for (int ixout=0; ixout<nxout; ++ixout)
      assertEquals(yo[ixout],yt[ixout],0.0);
    si.setExtrapolation(SincInterpolator.Extrapolation.CONSTANT);
    si.setInput(nxin,dxin,fxin,yi);
    si.interpolate(nxout,dxout,fxout,yo);
    si.setInput(npad+nxin+npad,dxin,0.0,yc);
    si.interpolate(nxout,dxout,fxout,yt);
    for (int ixout=0; ixout<nxout; ++ixout)
      assertEquals(yo[ixout],yt[ixout],0.0);
  }

  public void testComplex() {
    SincInterpolator si = new SincInterpolator();
    Random random = new Random();
    int nxin = 100;
    double dxin = 3.14159;
    double fxin = 1.23456;
    float[] yr = new float[nxin];
    float[] yi = new float[nxin];
    float[] yc = new float[2*nxin];
    for (int ixin=0; ixin<nxin; ++ixin) {
      yr[ixin] = yc[2*ixin  ] = random.nextFloat();
      yi[ixin] = yc[2*ixin+1] = random.nextFloat();
    }
    si.setInputSampling(nxin,dxin,fxin);
    int nxout = 200;
    double dxout = -0.9*dxin;
    double fxout = fxin+(nxin+30)*dxin;
    float[] zr = new float[nxout];
    float[] zi = new float[nxout];
    float[] zc = new float[2*nxout];
    si.setExtrapolation(SincInterpolator.Extrapolation.ZERO);
    si.setInputSamples(yr);
    si.interpolate(nxout,dxout,fxout,zr);
    si.setInputSamples(yi);
    si.interpolate(nxout,dxout,fxout,zi);
    si.setInputSamples(yc);
    si.interpolateComplex(nxout,dxout,fxout,zc);
    for (int ixout=0; ixout<nxout; ++ixout) {
      assertEquals(zr[ixout],zc[2*ixout  ],0.0);
      assertEquals(zi[ixout],zc[2*ixout+1],0.0);
    }
    si.setExtrapolation(SincInterpolator.Extrapolation.CONSTANT);
    si.setInputSamples(yr);
    si.interpolate(nxout,dxout,fxout,zr);
    si.setInputSamples(yi);
    si.interpolate(nxout,dxout,fxout,zi);
    si.setInputSamples(yc);
    si.interpolateComplex(nxout,dxout,fxout,zc);
    for (int ixout=0; ixout<nxout; ++ixout) {
      assertEquals(zr[ixout],zc[2*ixout  ],0.0);
      assertEquals(zi[ixout],zc[2*ixout+1],0.0);
    }
  }

  public void testErrorAndFrequency() {
    for (int iemax=0; iemax<_emaxs.length; ++iemax) {
      double emax = _emaxs[iemax];
      for (int ifmax=0; ifmax<_fmaxs.length; ++ifmax) {
        double fmax = _fmaxs[ifmax];
        SincInterpolator si = 
          SincInterpolator.fromErrorAndFrequency(emax,fmax);
        testInterpolator(si);
      }
    }
  }

  public void testErrorAndLength() {
    for (int iemax=0; iemax<_emaxs.length; ++iemax) {
      double emax = _emaxs[iemax];
      for (int ilmax=0; ilmax<_lmaxs.length; ++ilmax) {
        int lmax = _lmaxs[ilmax];
        SincInterpolator si = 
          SincInterpolator.fromErrorAndLength(emax,lmax);
        testInterpolator(si);
      }
    }
  }

  public void testFrequencyAndLength() {
    for (int ifmax=0; ifmax<_fmaxs.length; ++ifmax) {
      double fmax = _fmaxs[ifmax];
      for (int ilmax=0; ilmax<_lmaxs.length; ++ilmax) {
        int lmax = _lmaxs[ilmax];
        if ((1.0-2.0*fmax)*lmax>1.0) {
          SincInterpolator si = 
            SincInterpolator.fromFrequencyAndLength(fmax,lmax);
          testInterpolator(si);
        }
      }
    }
  }

  private void testInterpolator(SincInterpolator si) {
    testInterpolatorWithSweep(si);
  }

  private void testInterpolatorWithSweep(SincInterpolator si) {
    double emax = si.getMaximumError();
    double fmax = si.getMaximumFrequency();
    int lmax = si.getMaximumLength();
    long nbytes = si.getTableBytes();
    trace("lmax="+lmax+" fmax="+fmax+" emax="+emax+" nbytes="+nbytes);
    
    // Input signal is an up-down sweep. (See below.)
    int nmax = (int)(1000*fmax);
    double xmax = PI*nmax/fmax;
    double dxin = 1.0;
    double fxin = 0.0;
    int nxin = 1+(int)((xmax-fxin)/dxin);
    dxin = (xmax-fxin)/(nxin-1);
    float[] yin = new float[nxin];
    for (int ixin=0; ixin<nxin; ++ixin) {
      double x = fxin+ixin*dxin;
      yin[ixin] = (float)sweep(fmax,nmax,x);
    }
    si.setInput(nxin,dxin,fxin,yin);
    si.setExtrapolation(SincInterpolator.Extrapolation.CONSTANT);
    trace("xmax="+xmax+" nmax="+nmax+" nxin="+nxin);

    // Interpolate.
    double dxout = 0.01*dxin;
    double fxout = 0.0;
    int nxout = 1+(int)((xmax-fxout)/dxout);
    dxout = (xmax-fxout)/(nxout-1);
    float[] yout = new float[nxout];
    si.interpolate(nxout,dxout,fxout,yout);

    // Compute the maximum error and compare with emax.
    double error = 0.0;
    for (int ixout=0; ixout<nxout; ++ixout) {
      double x = fxout+ixout*dxout;
      double yi = yout[ixout];
      double ys = sweep(fmax,nmax,x);
      double ei = abs(yi-ys);
      if (ei>emax)
        trace("    x="+x+" ys="+ys+" yi="+yi);
      error = max(error,ei);
      assertEquals(ys,yi,emax);
    }
    trace("  error="+error);
    if (error>emax)
      trace("  WARNING: error = "+error+" > emax = "+emax);

    // Repeat for a simple shift of 1/2 the input sampling interval.
    double shift = 0.5*dxin;
    nxout = nxin;
    dxout = dxin;
    fxout = fxin+shift;
    si.interpolate(nxout,dxout,fxout,yout);
    error = 0.0;
    for (int ixout=0; ixout<nxout; ++ixout) {
      double x = fxout+ixout*dxout;
      double yi = yout[ixout];
      double ys = sweep(fmax,nmax,x);
      double ei = abs(yi-ys);
      if (ei>emax)
        trace("    x="+x+" ys="+ys+" yi="+yi);
      error = max(error,ei);
      assertEquals(ys,yi,emax);
    }
    trace("  error="+error);
  }

  // An up-down sweep signal that begins with zero frequency, increases to
  // frequency fmax (in cycles/sample) at x = xmax/2, then decreases to zero 
  // frequency again at x = xmax, where xmax = PI*nmax/fmax. The frequency
  // changes continuously and changes most slowly near x = xmax/2, where 
  // frequencies are highest, and where interpolation errors may be largest.
  private double sweep(double fmax, int nmax, double x) {
    return cos(2.0*PI*nmax*cos(x*fmax/nmax));
  }

  private static void trace(String s) {
    //System.out.println(s);
  }


  ///////////////////////////////////////////////////////////////////////////
  // more test code that was useful in debugging, and might be again

  private void testInterpolatorWithSine(SincInterpolator si) {

    // Interpolator design parameters.
    int lmax = si.getMaximumLength();
    double fmax = si.getMaximumFrequency();
    double emax = si.getMaximumError();
    trace("lmax="+lmax+" fmax="+fmax+" emax="+emax);
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
    double dk = 2.0*PI*fmax/((nk-1)*dxin);
    double fk = 0.0;
    for (int ik=0; ik<nk; ++ik) {
      double k = fk+ik*dk;

      // Input sine wave.
      for (int ixin=0; ixin<nxin; ++ixin) {
        double x = fxin+ixin*dxin;
        yin[ixin] = sine(k*x);
      }
      si.setInput(nxin,dxin,fxin,yin);

      // Interpolated sine wave.
      si.interpolate(nxout,dxout,fxout,yout);
      for (int ixout=0; ixout<nxout; ++ixout) {
        double x = fxout+ixout*dxout;
        float yi = yout[ixout];
        float ye = sine(k*x);
        if (abs(ye-yi)>emax)
          trace("k="+k+" x="+x+" ye="+ye+" yi="+yi);
        assertEquals(ye,yi,emax);
      }
    }
  }

  // A simple sine wave, with an arbitrary but hardwired shift.
  private static float sine(double x) {
    return (float)sin(1+x);
  }

  // Used to test design via Kaiser windows. Shows the effect of summing 
  // multiple aliases of Kaiser window spectra into the passband of an 
  // interpolator.
  private void xtestDesign() {
    int m = 100;
    int lsinc = 8;
    double xmax = lsinc/2;
    double kmax = 0.325;
    double wwin = 1.0-2.0*kmax;
    double lwin = lsinc;
    KaiserWindow kwin = KaiserWindow.fromWidthAndLength(wwin,lwin);
    double ewin = kwin.getError();
    trace("wwin="+wwin+" lwin="+lwin+" ewin="+ewin);

    int nx = m*lsinc;
    double dx = 1.0/m;
    double fx = 0.0;
    int nxfft = FftReal.nfftFast(10*nx);
    int nk = nxfft/2+1;
    double dk = 2.0*PI/(nxfft*dx);
    double fk = 0.0;
    FftReal fft = new FftReal(nxfft);
    trace("nxfft="+nxfft);

    float[] sx = new float[nxfft];
    sx[0] = 1.0f;
    for (int ix=1,jx=nxfft-1; ix<nx/2; ++ix,--jx) {
      double x = fx+ix*dx;
      sx[ix] = (float)(x<=xmax?sinc(PI*x)*kwin.evaluate(x):0.0);
      sx[jx] = sx[ix];
      trace("s("+x+") = "+sx[ix]);
    }

    float[] sk = new float[2*nk];
    fft.realToComplex(-1,sx,sk);
    float[] ak = cabs(sk);
    ak = mul(ak,(float)(1.0/m));
    for (int ik=0; ik<=nk/10; ++ik) {
      double k = (fk+ik*dk)/(2.0*PI);
      if (k<=kmax && abs(ak[ik]-1.0)>ewin)
        trace("a("+k+") = "+ak[ik]);
    }

    float[] tx = new float[nxfft];
    for (int ix=0,jx=m/2; ix<lsinc/2; ++ix,jx+=m) {
      tx[        ix] = sx[jx];
      tx[nxfft-1-ix] = sx[nxfft-jx];
      trace("tx["+ix+"] = "+          tx[ix]);
      trace("tx["+(nxfft-1-ix)+"] = "+tx[nxfft-1-ix]);
    }

    float[] tk = new float[2*nk];
    fft.realToComplex(-1,tx,tk);
    ak = cabs(tk);
    for (int ik=0; ik<nk; ++ik) {
      double k = (fk+ik*dk)/(2.0*PI*m);
      if (k<=kmax && abs(ak[ik]-1.0)>2.0*ewin)
        trace("a("+k+") = "+ak[ik]);
    }
  }

  private double sinc(double x) {
    return (x==0.0)?1.0:sin(x)/x;
  }
}
