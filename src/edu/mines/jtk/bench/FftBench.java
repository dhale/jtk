/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import edu.mines.jtk.dsp.FftComplex;
import static edu.mines.jtk.util.ArrayMath.crandfloat;
import edu.mines.jtk.util.Stopwatch;

/**
 * Benchmark FFTs.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.24
 */
public class FftBench {
  public static void main(String[] args) {
    for (int niter=0; niter<5; ++niter) {
      for (int nfft=1; nfft<=720720;) {
        int nfftSmall = FftComplex.nfftSmall(nfft);
        int nfftFast = FftComplex.nfftFast(nfft);
        double timeSmall = time(nfftSmall);
        if (nfftFast==nfftSmall) {
          System.out.printf("nsmall=%d tsmall=%.14f\n",nfftSmall,timeSmall);
        } else {
          double timeFast = (nfftFast>nfftSmall)?time(nfftFast):timeSmall;
          System.out.printf("nsmall=%d tsmall=%.14f tfast=%.14f\n",
            nfftSmall,timeSmall,timeFast);
          if (timeSmall<timeFast)
            System.out.println("*** WARNING: tsmall<tfast! ***");
        }
        nfft = 1+nfftSmall;
      }
    }
  }

  private static double time(int nfft) {
    double maxtime = 2.0;
    FftComplex fft = new FftComplex(nfft);
    float[] cx = crandfloat(nfft);
    int count;
    Stopwatch sw = new Stopwatch();
    sw.start();
    for (count=0; sw.time()<maxtime; ++count) {
      fft.complexToComplex(-1,cx,cx);
      fft.complexToComplex( 1,cx,cx);
      fft.scale(nfft,cx);
    }
    sw.stop();
    double time = sw.time()/(float)count;
    return time;
  }
}
