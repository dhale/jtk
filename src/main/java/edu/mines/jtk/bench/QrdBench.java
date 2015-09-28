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
