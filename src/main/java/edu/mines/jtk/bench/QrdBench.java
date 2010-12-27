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
 * Benchmark QR decompositions in packages la and lapack.
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

      // edu.mines.jtk.la
      edu.mines.jtk.la.DMatrixQrd qrd1;
      edu.mines.jtk.la.DMatrix x1 = new edu.mines.jtk.la.DMatrix(1,1);
      sw.restart();
      for (nqrd=0;  sw.time()<maxtime; ++nqrd) {
        qrd1 = new edu.mines.jtk.la.DMatrixQrd(aj);
        x1 = qrd1.solve(bj);
      }
      sw.stop();
      sum = sum(x1.getArray());
      rate = nqrd/sw.time();
      System.out.println("edu.mines.jtk.la:     rate="+rate+" sum="+sum);

      // edu.mines.jtk.lapack
      edu.mines.jtk.lapack.DMatrixQrd qrd2;
      edu.mines.jtk.lapack.DMatrix x2 = new edu.mines.jtk.lapack.DMatrix(1,1);
      sw.restart();
      for (nqrd=0;  sw.time()<maxtime; ++nqrd) {
        qrd2 = new edu.mines.jtk.lapack.DMatrixQrd(al);
        x2 = qrd2.solve(bl);
      }
      sw.stop();
      sum = sum(x2.getArray());
      rate = nqrd/sw.time();
      System.out.println("edu.mines.jtk.lapack: rate="+rate+" sum="+sum);
    }
  }
}
