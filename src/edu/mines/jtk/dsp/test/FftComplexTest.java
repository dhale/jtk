/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp.test;

import junit.framework.*;
import edu.mines.jtk.dsp.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.dsp.FftComplex}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class FftComplexTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(FftComplexTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1() {
    int nmax = 1000;
    for (int n=2; n<nmax; ++n) {
      int nfft = FftComplex.nfftSmall(n);
      FftComplex fft = new FftComplex(nfft);
      float[] c1 = Cap.zero(nfft);
      c1[2] = 1.0f;
      float[] cx = Cap.copy(c1);
      fft.complexToComplex(1,cx,cx);
      float ra = 0.0f;
      float rb = 2.0f*FLT_PI/(float)nfft;
      float[] amp = Rap.fill(1.0f,nfft);
      float[] phs = Rap.ramp(ra,rb,nfft);
      float[] cc = Cap.polar(amp,phs);
      assertEqual(cc,cx);
      fft.complexToComplex(-1,cx,cx);
      fft.scale(nfft,cx);
      assertEqual(c1,cx);
    }
  }

  public void test2() {
    int n1max = 26;
    int n2max = 26;
    for (int n2=2; n2<n2max; ++n2) {
      int n2fft = FftComplex.nfftSmall(n2);
      FftComplex fft2 = new FftComplex(n2fft);
      for (int n1=2; n1<n1max; ++n1) {
        int n1fft = FftComplex.nfftSmall(n1);
        FftComplex fft1 = new FftComplex(n1fft);
        float[][] c1 = Cap.zero(n1fft,n2fft);
        c1[1][2] = 1.0f;
        float[][] cx = Cap.copy(c1);
        fft1.complexToComplex1(1,n2fft,cx,cx);
        fft2.complexToComplex2(1,n1fft,cx,cx);
        float ra = 0.0f;
        float rb1 = 2.0f*FLT_PI/(float)n1fft;
        float rb2 = 2.0f*FLT_PI/(float)n2fft;
        float[][] amp = Rap.fill(1.0f,n1fft,n2fft);
        float[][] phs = Rap.ramp(ra,rb1,rb2,n1fft,n2fft);
        float[][] cc = Cap.polar(amp,phs);
        assertEqual(cc,cx);
        fft1.complexToComplex1(-1,n2fft,cx,cx);
        fft2.complexToComplex2(-1,n1fft,cx,cx);
        fft1.scale(n1fft,n2fft,cx);
        fft2.scale(n1fft,n2fft,cx);
        assertEqual(c1,cx);
      }
    }
  }

  public void test1Random() {
    int nmax = 1000;
    for (int n=2; n<nmax; ++n) {
      int nfft = FftComplex.nfftSmall(n);
      FftComplex fft = new FftComplex(nfft);
      float[] cr = Cap.rand(nfft);
      float[] cx = Cap.copy(cr);
      float[] cy = Cap.zero(nfft);
      fft.complexToComplex( 1,cx,cy);
      fft.complexToComplex(-1,cy,cx);
      fft.scale(nfft,cx);
      assertEqual(cr,cx);
    }
  }

  public void test2Random() {
    int n1max = 26;
    int n2max = 26;
    for (int n2=2; n2<n2max; ++n2) {
      int n2fft = FftComplex.nfftSmall(n2);
      FftComplex fft2 = new FftComplex(n2fft);
      for (int n1=2; n1<n1max; ++n1) {
        int n1fft = FftComplex.nfftSmall(n1);
        FftComplex fft1 = new FftComplex(n1fft);
        float[][] cr = Cap.rand(n1fft,n2fft);
        float[][] cx = Cap.copy(cr);
        float[][] cy = Cap.zero(n1fft,n2fft);
        fft1.complexToComplex1( 1,n2fft,cx,cy);
        fft2.complexToComplex2( 1,n1fft,cy,cy);
        fft1.complexToComplex1(-1,n2fft,cy,cx);
        fft2.complexToComplex2(-1,n1fft,cx,cx);
        fft1.scale(n1fft,n2fft,cx);
        fft2.scale(n1fft,n2fft,cx);
        assertEqual(cr,cx);
      }
    }
  }

  private void assertEqual(float[] ca, float[] cb) {
    float tolerance = (float)(ca.length/2)*FLT_EPSILON;
    assertTrue(Cap.equal(tolerance,ca,cb));
  }

  private void assertEqual(float[][] ca, float[][] cb) {
    float tolerance = (float)(ca.length+ca[0].length/2)*FLT_EPSILON;
    assertTrue(Cap.equal(tolerance,ca,cb));
  }
}
