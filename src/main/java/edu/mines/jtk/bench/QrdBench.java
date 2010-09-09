/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import static edu.mines.jtk.util.ArrayMath.sum;
import edu.mines.jtk.util.Stopwatch;

/**
 * Benchmark Pure Java and LAPACK's QR decompositions.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.14
 */
public class QrdBench {

  public static void main(String[] args) {
    double maxtime = 5;
    int m = 100;
    int n = 5;
    int nrhs = 5;

    // Pure Java.
    edu.mines.jtk.la.DMatrix aj = 
      edu.mines.jtk.la.DMatrix.random(m,n);
    edu.mines.jtk.la.DMatrix bj = 
      edu.mines.jtk.la.DMatrix.random(m,nrhs);

    // LAPACK.
    edu.mines.jtk.lapack.DMatrix al = 
      new edu.mines.jtk.lapack.DMatrix(aj.get());
    edu.mines.jtk.lapack.DMatrix bl = 
      new edu.mines.jtk.lapack.DMatrix(bj.get());

    double rate,sum;
    int nqrd;
    Stopwatch sw = new Stopwatch();
    for (int niter=0; niter<5; ++niter) {

      // Pure Java.
      edu.mines.jtk.la.DMatrixQrd qrdj;
      edu.mines.jtk.la.DMatrix xj = new edu.mines.jtk.la.DMatrix(1,1);
      sw.restart();
      for (nqrd=0;  sw.time()<maxtime; ++nqrd) {
        qrdj = new edu.mines.jtk.la.DMatrixQrd(aj);
        xj = qrdj.solve(bj);
      }
      sw.stop();
      sum = sum(xj.getArray());
      rate = nqrd/sw.time();
      System.out.println("Pure Java: rate="+rate+" sum="+sum);

      // LAPACK.
      edu.mines.jtk.lapack.DMatrixQrd qrdl;
      edu.mines.jtk.lapack.DMatrix xl = new edu.mines.jtk.lapack.DMatrix(1,1);
      sw.restart();
      for (nqrd=0;  sw.time()<maxtime; ++nqrd) {
        qrdl = new edu.mines.jtk.lapack.DMatrixQrd(al);
        xl = qrdl.solve(bl);
      }
      sw.stop();
      sum = sum(xl.getArray());
      rate = nqrd/sw.time();
      System.out.println("   LAPACK: rate="+rate+" sum="+sum);
    }
  }
}
