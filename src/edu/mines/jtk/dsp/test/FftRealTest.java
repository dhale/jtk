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
      FftReal fft = FftReal.small(n);
      int nfft = fft.getNfft();
      float[] ca = new float[nfft+2];
      float[] ra = ca;
      ra[1] = 1.0f;
      fft.realToComplex(1,ra,ca);
      double delta = 2.0*DBL_PI/nfft;
      double tiny = nfft*FLT_EPSILON;
      for (int i=0,j=0; i<nfft/2+1; ++i,j+=2) {
        double theta = i*delta;
        float ar = ca[j  ];
        float ai = ca[j+1];
        assertEquals(cos(theta),ar,tiny);
        assertEquals(sin(theta),ai,tiny);
      }
      fft.complexToReal(-1,ca,ra);
      fft.scale(n,ra);
      for (int i=0; i<n; ++i) {
        double ear = (i==1)?1.0:0.0;
        assertEquals(ear,ra[i],tiny);
      }
    }
  }

  /*
  public void test2() {
    int n1 = 10;
    int n2max = 100;
    for (int n2=2; n2<n2max; ++n2) {
      FftReal fft = FftReal.small(n2);
      int n2fft = fft.getNfft();
      float[][] ra = new float[n2fft][n1];
      float[][] ca = new float[n2fft/2+1][2*n1];
      for (int i1=0; i1<n1; ++i1)
        ra[1][2*i1] = 1.0f;
      fft.realToComplex2(1,n1,ra,ca);
      double delta = 2.0*DBL_PI/n2fft;
      double tiny = n2fft*FLT_EPSILON;
      for (int i2=0; i2<n2fft/2+1; ++i2) {
        double theta = i2*delta;
        for (int i1=0,j1=0; i1<n1; ++i1,j1+=2) {
          float ar = ca[i2][j1  ];
          float ai = ca[i2][j1+1];
          assertEquals(cos(theta),ar,tiny);
          assertEquals(sin(theta),ai,tiny);
        }
      }
      fft.complexToReal2(-1,n1,ca,ra);
      fft.scale(n1,n2,ra);
      for (int i2=0; i2<n2; ++i2) {
        double ear = (i2==1)?1.0:0.0;
        for (int i1=0; i1<n1; ++i1) {
          assertEquals(ear,ra[i2][i1],tiny);
        }
      }
    }
  }
  */
}
