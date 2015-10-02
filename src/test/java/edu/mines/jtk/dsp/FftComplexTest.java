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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

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
      float[] c1 = czerofloat(nfft);
      c1[2] = 1.0f;
      float[] cx = ccopy(c1);
      fft.complexToComplex(1,cx,cx);
      float ra = 0.0f;
      float rb = 2.0f*FLT_PI/(float)nfft;
      float[] amp = fillfloat(1.0f,nfft);
      float[] phs = rampfloat(ra,rb,nfft);
      float[] cc = polar(amp,phs);
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
        float[][] c1 = czerofloat(n1fft,n2fft);
        c1[1][2] = 1.0f;
        float[][] cx = ccopy(c1);
        fft1.complexToComplex1(1,n2fft,cx,cx);
        fft2.complexToComplex2(1,n1fft,cx,cx);
        float ra = 0.0f;
        float rb1 = 2.0f*FLT_PI/(float)n1fft;
        float rb2 = 2.0f*FLT_PI/(float)n2fft;
        float[][] amp = fillfloat(1.0f,n1fft,n2fft);
        float[][] phs = rampfloat(ra,rb1,rb2,n1fft,n2fft);
        float[][] cc = polar(amp,phs);
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
      float[] cr = crandfloat(nfft);
      float[] cx = ccopy(cr);
      float[] cy = czerofloat(nfft);
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
        float[][] cr = crandfloat(n1fft,n2fft);
        float[][] cx = ccopy(cr);
        float[][] cy = czerofloat(n1fft,n2fft);
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

  public void test3Random() {
    int n1 = 11;
    int n2 = 12;
    int n3 = 13;
    int n1fft = FftComplex.nfftSmall(n1);
    int n2fft = FftComplex.nfftSmall(n2);
    int n3fft = FftComplex.nfftSmall(n3);
    FftComplex fft1 = new FftComplex(n1fft);
    FftComplex fft2 = new FftComplex(n2fft);
    FftComplex fft3 = new FftComplex(n3fft);
    float[][][] cr = crandfloat(n1fft,n2fft,n3fft);
    float[][][] cx = ccopy(cr);
    fft1.complexToComplex1( 1,n2fft,n3fft,cx,cx);
    fft2.complexToComplex2( 1,n1fft,n3fft,cx,cx);
    fft3.complexToComplex3( 1,n1fft,n2fft,cx,cx);
    fft1.complexToComplex1(-1,n2fft,n3fft,cx,cx);
    fft2.complexToComplex2(-1,n1fft,n3fft,cx,cx);
    fft3.complexToComplex3(-1,n1fft,n2fft,cx,cx);
    fft1.scale(n1fft,n2fft,n3fft,cx);
    fft2.scale(n1fft,n2fft,n3fft,cx);
    fft3.scale(n1fft,n2fft,n3fft,cx);
    assertEqual(cr,cx);
  }

  private void assertEqual(float[] ca, float[] cb) {
    int n1 = ca.length/2;
    float tolerance = (float)(n1)*FLT_EPSILON;
    assertTrue(cequal(tolerance,ca,cb));
  }

  private void assertEqual(float[][] ca, float[][] cb) {
    int n1 = ca[0].length/2;
    int n2 = ca.length;
    float tolerance = (float)(n1+n2)*FLT_EPSILON;
    assertTrue(cequal(tolerance,ca,cb));
  }

  private void assertEqual(float[][][] ca, float[][][] cb) {
    int n1 = ca[0][0].length/2;
    int n2 = ca[0].length;
    int n3 = ca.length;
    float tolerance = (float)(n1+n2+n3)*FLT_EPSILON;
    assertTrue(cequal(tolerance,ca,cb));
  }
}
