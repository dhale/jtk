/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.*;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.FftReal}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class FftRealTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(FftRealTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1() {
    int nmax = 1000;
    for (int n=2; n<nmax; ++n) {
      int nfft = FftReal.nfftSmall(n);
      FftReal fft = new FftReal(nfft);
      int nw = nfft/2+1;
      float[] c1 = czerofloat(nw);
      float[] r1 = c1;
      r1[1] = 1.0f;
      float[] rx = ccopy(r1);
      float[] cx = rx;
      fft.realToComplex(1,rx,cx);
      float ra = 0.0f;
      float rb = 2.0f*FLT_PI/(float)nfft;
      float[] amp = fillfloat(1.0f,nw);
      float[] phs = rampfloat(ra,rb,nw);
      float[] cc = polar(amp,phs);
      assertComplexEqual(nw,cc,cx);
      fft.complexToReal(-1,cx,rx);
      fft.scale(nfft,rx);
      assertRealEqual(nfft,r1,rx);
    }
  }

  public void test12() {
    int n1max = 26;
    int n2max = 26;
    for (int n2=2; n2<n2max; ++n2) {
      int n2fft = FftComplex.nfftSmall(n2);
      FftComplex fft2 = new FftComplex(n2fft);
      for (int n1=2; n1<n1max; ++n1) {
        int n1fft = FftReal.nfftSmall(n1);
        FftReal fft1 = new FftReal(n1fft);
        int nw = n1fft/2+1;
        float[][] c1 = czerofloat(nw,n2fft);
        float[][] r1 = c1;
        r1[1][1] = 1.0f;
        float[][] rx = ccopy(r1);
        float[][] cx = rx;
        fft1.realToComplex1(1,n2,rx,cx);
        fft2.complexToComplex2(1,nw,cx,cx);
        float ra = 0.0f;
        float rb1 = 2.0f*FLT_PI/(float)n1fft;
        float rb2 = 2.0f*FLT_PI/(float)n2fft;
        float[][] amp = fillfloat(1.0f,nw,n2fft);
        float[][] phs = rampfloat(ra,rb1,rb2,nw,n2fft);
        float[][] cc = polar(amp,phs);
        assertComplexEqual(nw,n2fft,cc,cx);
        fft2.complexToComplex2(-1,nw,cx,cx);
        fft2.scale(nw,n2,cx);
        fft1.complexToReal1(-1,n2,cx,rx);
        fft1.scale(n1,n2,rx);
        assertRealEqual(n1,n2,r1,rx);
      }
    }
  }

  public void test21() {
    int n1max = 26;
    int n2max = 26;
    for (int n2=2; n2<n2max; ++n2) {
      int n2fft = FftReal.nfftSmall(n2);
      FftReal fft2 = new FftReal(n2fft);
      int nw = n2fft/2+1;
      for (int n1=2; n1<n1max; ++n1) {
        int n1fft = FftComplex.nfftSmall(n1);
        FftComplex fft1 = new FftComplex(n1fft);
        float[][] c1 = czerofloat(n1fft,n2fft);
        float[][] r1 = c1;
        r1[1][1] = 1.0f;
        float[][] rx = ccopy(r1);
        float[][] cx = rx;
        fft2.realToComplex2(1,n1,rx,cx);
        fft1.complexToComplex1(1,nw,cx,cx);
        float ra = 0.0f;
        float rb1 = 2.0f*FLT_PI/(float)n1fft;
        float rb2 = 2.0f*FLT_PI/(float)n2fft;
        float[][] amp = fillfloat(1.0f,n1fft,nw);
        float[][] phs = rampfloat(ra,rb1,rb2,n1fft,nw);
        float[][] cc = polar(amp,phs);
        assertComplexEqual(n1fft,nw,cc,cx);
        fft1.complexToComplex1(-1,nw,cx,cx);
        fft1.scale(n1,nw,cx);
        fft2.complexToReal2(-1,n1,cx,rx);
        fft2.scale(n1,n2,rx);
        assertRealEqual(n1,n2,r1,rx);
      }
    }
  }

  public void test1Random() {
    int nmax = 1000;
    for (int n=2; n<nmax; ++n) {
      int nfft = FftReal.nfftSmall(n);
      FftReal fft = new FftReal(nfft);
      int nw = nfft/2+1;
      float[] rr = randfloat(nfft);
      float[] rx = copy(rr);
      float[] cy = czerofloat(nw);
      fft.realToComplex( 1,rx,cy);
      fft.complexToReal(-1,cy,rx);
      fft.scale(nfft,rx);
      assertRealEqual(nfft,rr,rx);
    }
  }

  public void test12Random() {
    int n1max = 26;
    int n2max = 26;
    for (int n2=2; n2<n2max; ++n2) {
      int n2fft = FftComplex.nfftSmall(n2);
      FftComplex fft2 = new FftComplex(n2fft);
      for (int n1=2; n1<n1max; ++n1) {
        int n1fft = FftReal.nfftSmall(n1);
        FftReal fft1 = new FftReal(n1fft);
        int nw = n1fft/2+1;
        float[][] rr = randfloat(n1fft,n2);
        float[][] rx = copy(rr);
        float[][] cy = czerofloat(nw,n2fft);
        fft1.realToComplex1( 1,n2,rx,cy);
        fft2.complexToComplex2( 1,nw,cy,cy);
        fft2.complexToComplex2(-1,nw,cy,cy);
        fft2.scale(nw,n2,cy);
        fft1.complexToReal1(-1,n2,cy,rx);
        fft1.scale(n1,n2,rx);
        assertRealEqual(n1,n2,rr,rx);
      }
    }
  }

  public void test21Random() {
    int n1max = 26;
    int n2max = 26;
    for (int n2=2; n2<n2max; ++n2) {
      int n2fft = FftReal.nfftSmall(n2);
      FftReal fft2 = new FftReal(n2fft);
      int nw = n2fft/2+1;
      for (int n1=2; n1<n1max; ++n1) {
        int n1fft = FftComplex.nfftSmall(n1);
        FftComplex fft1 = new FftComplex(n1fft);
        float[][] rr = randfloat(n1,n2fft);
        float[][] rx = copy(rr);
        float[][] cy = czerofloat(n1fft,nw);
        fft2.realToComplex2( 1,n1,rx,cy);
        fft1.complexToComplex1( 1,nw,cy,cy);
        fft1.complexToComplex1(-1,nw,cy,cy);
        fft1.scale(n1,nw,cy);
        fft2.complexToReal2(-1,n1,cy,rx);
        fft2.scale(n1,n2,rx);
        assertRealEqual(n1,n2,rr,rx);
      }
    }
  }

  private void assertRealEqual(int n1, float[] re, float[] ra) {
    float tolerance = (float)(n1)*FLT_EPSILON;
    for (int i1=0; i1<n1; ++i1)
      assertEquals(re[i1],ra[i1],tolerance);
  }

  private void assertRealEqual(int n1, int n2, float[][] re, float[][] ra) {
    float tolerance = (float)(n1+n2)*FLT_EPSILON;
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1)
        try {
          assertEquals(re[i2][i1],ra[i2][i1],tolerance);
        } catch (AssertionFailedError e) {
          System.out.println("index i1="+i1+" i2="+i2);
          throw e;
        }
    }
  }

  private void assertComplexEqual(int n1, float[] ce, float[] ca) {
    float tolerance = (float)(n1)*FLT_EPSILON;
    for (int i1=0; i1<n1; ++i1) {
      assertEquals(ce[2*i1  ],ca[2*i1  ],tolerance);
      assertEquals(ce[2*i1+1],ca[2*i1+1],tolerance);
    }
  }

  private void assertComplexEqual(int n1, int n2, float[][] ce, float[][] ca) {
    float tolerance = (float)(n1+n2)*FLT_EPSILON;
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        try {
          assertEquals(ce[i2][2*i1  ],ca[i2][2*i1  ],tolerance);
          assertEquals(ce[i2][2*i1+1],ca[i2][2*i1+1],tolerance);
        } catch (AssertionFailedError e) {
          System.out.println("index i1="+i1+" i2="+i2);
          throw e;
        }
      }
    }
  }
}
