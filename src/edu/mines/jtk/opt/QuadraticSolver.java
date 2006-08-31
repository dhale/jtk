package edu.mines.jtk.opt;

import edu.mines.jtk.opt.Almost;
import edu.mines.jtk.opt.LogMonitor;
import edu.mines.jtk.opt.Monitor;
import java.util.logging.*;
import java.util.*;

/** Minimize a simple quadratic objective function.
    Finds the x that minimizes the quadratic function 0.5 x'Hx + b'x .
    Solves Hx + b = 0  for x (the Normal Equations of a least-squares problem)
    b is the gradient for x=0, and H is the hessian.
    The algorithm is a conjugate gradient.
    A is an approximate inverse Hessian, making AH more diagonal
    and improving convergence.
    A may also include a model-dependent conditioning to converge
    earlier on eigenfunction of most importance.
    The algorithm assumes that the application of H dominates the cost.
<pre>
     x = p = u = 0;
     beta = 0;
     g = b;
     a = A g;
     q = H a;
     do {
       p = -a + beta p
       u = Hp = -q + beta u
       scalar = -p'g/p'u
       x = x + scalar p
       if (done) return x
       g = H x + b = g + scalar u
       a = A g
       q = H a
       beta = p'H A g / p'H p = p'q/p'u
    }
</pre>

@author W.S. Harlan, Landmark Graphics
*/
public class QuadraticSolver {
  private Quadratic _quadratic = null;
  private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");

  /** Implement the Quadratic interface and pass to this constructor.
    Pass a vector b to the constructor (a copy is cloned internally).
    This solver calls Vect and Quadratic methods.
    Does not call Vect.multiplyInverseCovariance().
    @param quadratic Defines the Hessian quadratic term.
  */
  public QuadraticSolver( Quadratic quadratic) {
    _quadratic = quadratic;
  }

  /** Return a new solution after the number of conjugate gradient
      iterations.
      @param numberIterations is the number of iterations to perform.
 * @param monitor If non-null, then track all progress.
 * @return Optimized solution
  */
  public Vect solve(int numberIterations, Monitor monitor) {
    int iter;
    if (monitor == null)
      monitor = new LogMonitor(null, null);
    monitor.report(0.);
    VectConst b = _quadratic.getB(); // instance 1
    monitor.report(1./(numberIterations+2.));

    double bb = b.dot(b); checkNaN(bb);
    if (Almost.FLOAT.zero(bb)) {
      LOG.fine("Gradient of quadratic is negligible.  Not solving");
      Vect result = VectUtil.cloneZero(b);
      monitor.report(1.);
      return result;
    }

    Vect g = (Vect) b;  b = null;   // reuse b
    Vect x = VectUtil.cloneZero(g); // instance 2
    Vect p = x.clone();      // instance 3
    Vect u = x.clone();      // instance 4
    double pu = 0.;

  solve:
    for (iter=0; iter<numberIterations; iter++) {
      double beta = 0.;
      {
        Vect q = g.clone();     // instance 5
        _quadratic.inverseHessian(q);
        q.postCondition();         // a = A g
        _quadratic.multiplyHessian(q); // expensive
        monitor.report((iter+2.)/(numberIterations+2.));
        if (iter > 0) {
          double pq = p.dot(q); checkNaN(pq);
          beta = Almost.FLOAT.divide(pq,pu,0);
          if (beta < -5.) beta = -5.;
          if (beta > 5.) beta = 5.;
        }
        u.add(beta, -1., q);
        q.dispose();
      }
      { // Did not save "a" before calculating beta, to avoid extra instance.
        Vect a = g.clone();    // instance 5
        _quadratic.inverseHessian(a);
        a.postCondition();
        p.add(beta, -1., a);
        a.dispose();
      }
      double pg = p.dot(g); checkNaN(pg);
      pu = p.dot(u); checkNaN(pu);
      if (Almost.FLOAT.zero(pg)|| Almost.FLOAT.zero(pu)) {
        break solve;
      }
      double scalar = -pg/pu;
      x.add(1., scalar, p);
      if (iter == numberIterations-1) {
        break solve;
      }
      g.add(1., scalar, u);
    }
    p.dispose();
    u.dispose();
    g.dispose();
    monitor.report(1.);
    return x;
  }

  /** Abort if NaN's appear.
      @param value Abort if this value is a NaN.
   */
  private static void checkNaN(double value) {
    if (value*0 != 0) {
      throw new IllegalStateException("Value is a NaN");
    }
  }

  /** test code
 * @param args command line
 * @throws Exception all errors*/
  public static void main(String[] args) throws Exception {
    /*
      Minimize  0.5 x'Hx + b'x = 0, where H = |2 4 | and b = (2 1)'
                                              |4 11|
      solution is x = (-3 1)'
    */
    Quadratic q = new Quadratic() {
        public void multiplyHessian(Vect x) {
          double[] data = ((ArrayVect1)x).getData();
          double[] newData = new double[data.length];
          newData[0] = 2.*data[0] + 4.*data[1];
          newData[1] = 4.*data[0] + 11.*data[1];
          data[0] = newData[0];
          data[1] = newData[1];
        }
        public void inverseHessian(Vect x) {}
        public Vect getB() {
          return new TestVect(new double[] {2.,1.}, 1.);
        }
      };
    QuadraticSolver qs = new QuadraticSolver(q);

    { // not enough iterations
      ArrayVect1 result = (ArrayVect1) qs.solve(1, null);
      assert !Almost.FLOAT.equal(-3., result.getData()[0]): "result="+result;
      assert !Almost.FLOAT.equal(1., result.getData()[1]): "result="+result;
      result.dispose();
    }
    { // just barely enough iterations
      ArrayVect1 result = (ArrayVect1) qs.solve(2, null);
      assert Almost.FLOAT.equal(-3., result.getData()[0]): "result="+result;
      assert Almost.FLOAT.equal(1., result.getData()[1]): "result="+result;
      result.dispose();
    }
    { // Does not blow up with too many iterations
      ArrayVect1 result = (ArrayVect1) qs.solve(20, null);
      assert Almost.FLOAT.equal(-3., result.getData()[0]): "result="+result;
      assert Almost.FLOAT.equal(1., result.getData()[1]): "result="+result;
      result.dispose();
    }
    assert TestVect.undisposed.size() == 0 : TestVect.getTraces();
    assert TestVect.max <= 5:
      "max number of model vectors ("+TestVect.max+") should be less than 5";

  }

  private static class TestVect extends ArrayVect1 {
    private static final long serialVersionUID = 1L;
    /** Visible only for tests */
    public static int max = 0;
    /** Visible only for tests. */
    public static Map<Object,String> undisposed =
      Collections.synchronizedMap(new HashMap<Object,String>());

    /** Constructor
       @param data
       @param variance
     */
    public TestVect(double[] data, double variance) {
      super (data,variance);
      remember(this);
    }
    @Override
        public TestVect clone() {
      TestVect result = (TestVect) super.clone();
      remember(result);
      return result;
    }
    private void remember(Object tv) { // remember where allocated
      synchronized (undisposed) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        new Exception("This vector was never disposed").printStackTrace(pw);
        pw.flush();
        undisposed.put(tv, sw.toString());
        max = Math.max(max, undisposed.size());
      }
    }
    @Override
        public void dispose() {
      synchronized (undisposed) {
        super.dispose();
        undisposed.remove(this);
      }
    }
    /** @return traces for debugging
     */
    public static String getTraces() {
      StringBuilder sb = new StringBuilder();
      for (String s : undisposed.values()) {
        sb.append(s);
        sb.append("\n");
      }
      return sb.toString();
    }
  }

}

/*  Author: William S. Harlan
Copyright (c) 2003, Landmark Graphics Corporation.  All rights reserved.
Author: William S. Harlan

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

* Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

* Neither the name of Landmark Graphics nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
