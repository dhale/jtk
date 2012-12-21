/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static java.lang.Math.*;
import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.dsp.SincInterpolator}.
 * @author Dave Hale, Colorado School of Mines; Bill Harlan, Landmark Graphics
 * @version 2005.08.01
 */
@SuppressWarnings("deprecation")
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
      double fmax = si.getMaximumFrequency();
      si = SincInterpolator.fromFrequencyAndLength(fmax,lmax);
      testInterpolator(si);
    }
  }

  public void testExtrapolation() {
    SincInterpolator si = new SincInterpolator();
    Random random = new Random();
    int lmax = si.getMaximumLength();
    int nxu = 2*lmax;
    int npad = lmax;
    double dxu = 1.0;
    double fxu = npad;
    int nx = npad+nxu+npad;
    double dx = 0.999;
    double fx = npad;
    float[] yi = new float[nxu];
    float[] yz = new float[nx];
    float[] yc = new float[nx];
    float[] yo = new float[nx];
    float[] yt = new float[nx];
    for (int ixu=0; ixu<nxu; ++ixu)
      yi[ixu] = yz[ixu+npad] = yc[ixu+npad] = random.nextFloat();
    for (int ipad=0; ipad<npad; ++ipad) {
      yc[ipad] = yc[npad];
      yc[npad+nxu+ipad] = yc[npad+nxu-1];
    }
    si.setExtrapolation(SincInterpolator.Extrapolation.ZERO);
    si.setUniform(nxu,dxu,fxu,yi);
    si.interpolate(nx,dx,fx,yo);
    si.setUniform(npad+nxu+npad,dxu,0.0,yz);
    si.interpolate(nx,dx,fx,yt);
    for (int ix=0; ix<nx; ++ix)
      assertEquals(yo[ix],yt[ix],0.0);
    si.setExtrapolation(SincInterpolator.Extrapolation.CONSTANT);
    si.setUniform(nxu,dxu,fxu,yi);
    si.interpolate(nx,dx,fx,yo);
    si.setUniform(npad+nxu+npad,dxu,0.0,yc);
    si.interpolate(nx,dx,fx,yt);
    for (int ix=0; ix<nx; ++ix)
      assertEquals(yo[ix],yt[ix],0.0);
  }

  public void testComplex() {
    SincInterpolator si = new SincInterpolator();
    Random random = new Random();
    int nxu = 100;
    double dxu = 3.14159;
    double fxu = 1.23456;
    float[] yr = new float[nxu];
    float[] yi = new float[nxu];
    float[] yc = new float[2*nxu];
    for (int ixu=0; ixu<nxu; ++ixu) {
      yr[ixu] = yc[2*ixu  ] = random.nextFloat();
      yi[ixu] = yc[2*ixu+1] = random.nextFloat();
    }
    si.setUniformSampling(nxu,dxu,fxu);
    int nx = 200;
    double dx = -0.9*dxu;
    double fx = fxu+(nxu+30)*dxu;
    float[] zr = new float[nx];
    float[] zi = new float[nx];
    float[] zc = new float[2*nx];
    si.setExtrapolation(SincInterpolator.Extrapolation.ZERO);
    si.setUniformSamples(yr);
    si.interpolate(nx,dx,fx,zr);
    si.setUniformSamples(yi);
    si.interpolate(nx,dx,fx,zi);
    si.setUniformSamples(yc);
    si.interpolateComplex(nx,dx,fx,zc);
    for (int ix=0; ix<nx; ++ix) {
      assertEquals(zr[ix],zc[2*ix  ],0.0);
      assertEquals(zi[ix],zc[2*ix+1],0.0);
    }
    si.setExtrapolation(SincInterpolator.Extrapolation.CONSTANT);
    si.setUniformSamples(yr);
    si.interpolate(nx,dx,fx,zr);
    si.setUniformSamples(yi);
    si.interpolate(nx,dx,fx,zi);
    si.setUniformSamples(yc);
    si.interpolateComplex(nx,dx,fx,zc);
    for (int ix=0; ix<nx; ++ix) {
      assertEquals(zr[ix],zc[2*ix  ],0.0);
      assertEquals(zi[ix],zc[2*ix+1],0.0);
    }
  }

  public void testErrorAndFrequency() {
    for (double emax:_emaxs) {
      for (double fmax:_fmaxs) {
        SincInterpolator si =
          SincInterpolator.fromErrorAndFrequency(emax,fmax);
        testInterpolator(si);
      }
    }
  }

  public void testErrorAndLength() {
    for (double emax:_emaxs) {
      for (int lmax:_lmaxs) {
        SincInterpolator si =
          SincInterpolator.fromErrorAndLength(emax,lmax);
        testInterpolator(si);
      }
    }
  }

  public void testFrequencyAndLength() {
    for (double fmax:_fmaxs) {
      for (int lmax:_lmaxs) {
        if ((1.0-2.0*fmax)*lmax>1.0) {
          SincInterpolator si = 
            SincInterpolator.fromFrequencyAndLength(fmax,lmax);
          testInterpolator(si);
        }
      }
    }
  }

  public void testAccumulate() {
    // test that accumulate is a true transpose of interpolate
    Random random = new Random(123456); // avoid unreasonable accidents
    for (int repeat=0; repeat<5; ++repeat) {
      for (SincInterpolator.Extrapolation extrapolation:
             SincInterpolator.Extrapolation.values()) {
        int nxu = 201;
        double fxu = Math.PI;
        double dxu = Math.E;
        double exu = fxu + dxu*(nxu-1);
        float[] yu = new float[nxu];
        for (int i=0; i<nxu; ++i) {
          yu[i] = 2*random.nextFloat() - 1;
        }
        // random locations extending outside range
        int nx = 2*nxu;
        float[] x = new float[nx];
        float[] y = new float[nx];
        for (int i=0; i<nxu; ++i) {
          x[i] = (float)((1.2*random.nextFloat()-0.1)*(exu-fxu) + fxu);
          y[i] = 2*random.nextFloat() - 1;
        }

        // Same interpolator for both directions
        SincInterpolator si = new SincInterpolator();
        si.setExtrapolation(extrapolation);
        si.setUniformSampling(nxu, dxu, fxu);

        // forward interpolation
        float[] yi = new float[nx]; // interpolated
        si.setUniformSamples(yu);
        si.interpolate(nx, x, yi);

        // transpose accumuation
        float[] ya = new float[nxu]; // accumulated
        si.setUniformSamples(ya);
        si.accumulate(nx, x, y);

        // Check transpose with dot product: yu.ya = y.yi
        double yuYa = 0;
        for (int ixu=0; ixu<nxu; ++ixu) {
          yuYa += yu[ixu]*ya[ixu];
        }
        double yYi = 0;
        for (int ix=0; ix<nx; ++ix) {
          yYi += y[ix]*yi[ix];
        }
        double ratio = yuYa/yYi;
        String message =
          "yu.ya="+yuYa+" y.yi="+yYi+" ratio="+ratio;
        trace(message);
        assert ratio > 0.99999 && ratio < 1.00001 : message;
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
    
    // Uniformly-sampled signal is an up-down sweep. (See below.)
    int nmax = (int)(1000*fmax);
    double xmax = PI*nmax/fmax;
    double dxu = 1.0;
    double fxu = 0.0;
    int nxu = 1+(int)((xmax-fxu)/dxu);
    dxu = (xmax-fxu)/(nxu-1);
    float[] yu = new float[nxu];
    for (int ixu=0; ixu<nxu; ++ixu) {
      double x = fxu+ixu*dxu;
      yu[ixu] = (float)sweep(fmax,nmax,x);
    }
    si.setUniform(nxu,dxu,fxu,yu);
    si.setExtrapolation(SincInterpolator.Extrapolation.CONSTANT);
    //trace("xmax="+xmax+" nmax="+nmax+" nxu="+nxu);

    // Interpolate.
    double dx = 0.01*dxu;
    double fx = 0.0;
    int nx = 1+(int)((xmax-fx)/dx);
    dx = (xmax-fx)/(nx-1);
    float[] y = new float[nx];
    si.interpolate(nx,dx,fx,y);

    // Compute the maximum error and compare with emax.
    double error = 0.0;
    for (int ix=0; ix<nx; ++ix) {
      double x = fx+ix*dx;
      double yi = y[ix];
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
    double shift = 0.5*dxu;
    nx = nxu;
    dx = dxu;
    fx = fxu+shift;
    si.interpolate(nx,dx,fx,y);
    error = 0.0;
    for (int ix=0; ix<nx; ++ix) {
      double x = fx+ix*dx;
      double yi = y[ix];
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

  /*
  private void testInterpolatorWithSine(SincInterpolator si) {

    // Interpolator design parameters.
    int lmax = si.getMaximumLength();
    double fmax = si.getMaximumFrequency();
    double emax = si.getMaximumError();
    trace("lmax="+lmax+" fmax="+fmax+" emax="+emax);
    if (fmax==0.0)
      return;

    // Sampling and arrays for input signal.
    int nxu = 2+lmax;
    double dxu = 0.1;
    double fxu = (-lmax/2)*dxu;
    float[] yu = new float[nxu];

    // Sampling for interpolated output.
    int nx = 51;
    double dx = dxu/(nx-1);
    double fx = 0.0;
    float[] x = new float[nx];
    float[] y = new float[nx];
    for (int ix=0; ix<nx; ++ix) {
      double xi = fx+ix*dx;
      x[ix] = (float)xi;
    }

    // Loop over frequencies near maximum frequency.
    int nk = 51;
    double dk = 2.0*PI*fmax/((nk-1)*dxu);
    double fk = 0.0;
    for (int ik=0; ik<nk; ++ik) {
      double k = fk+ik*dk;

      // Uniformly-sampled sine wave.
      for (int ixu=0; ixu<nxu; ++ixu) {
        double xu = fxu+ixu*dxu;
        yu[ixu] = sine(k*xu);
      }
      si.setUniform(nxu,dxu,fxu,yu);

      // Interpolated sine wave.
      si.interpolate(nx,dx,fx,y);
      for (int ix=0; ix<nx; ++ix) {
        double xi = fx+ix*dx;
        float yi = y[ix];
        float ye = sine(k*xi);
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
 */

  // Used to test design via Kaiser windows. Shows the effect of summing 
  // multiple aliases of Kaiser window spectra into the passband of an 
  // interpolator.
  /*
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
  */
}
