/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import edu.mines.jtk.util.Stopwatch;
import edu.mines.jtk.util.Array;

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
    for (;;) {

      // Pure Java.
      edu.mines.jtk.la.DMatrixQrd qrdj = null;
      edu.mines.jtk.la.DMatrix xj = null;
      sw.restart();
      for (nqrd=0;  sw.time()<maxtime; ++nqrd) {
        qrdj = new edu.mines.jtk.la.DMatrixQrd(aj);
        xj = qrdj.solve(bj);
      }
      sw.stop();
      sum = Array.sum(xj.getArray());
      rate = nqrd/sw.time();
      System.out.println("Pure Java: rate="+rate+" sum="+sum);

      // LAPACK.
      edu.mines.jtk.lapack.DMatrixQrd qrdl = null;
      edu.mines.jtk.lapack.DMatrix xl = null;
      sw.restart();
      for (nqrd=0;  sw.time()<maxtime; ++nqrd) {
        qrdl = new edu.mines.jtk.lapack.DMatrixQrd(al);
        xl = qrdl.solve(bl);
      }
      sw.stop();
      sum = Array.sum(xl.getArray());
      rate = nqrd/sw.time();
      System.out.println("   LAPACK: rate="+rate+" sum="+sum);
    }
  }
}
