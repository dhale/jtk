package edu.mines.jtk.opt;

import java.util.logging.Logger;

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
      @param monitor If non-null, then track all progress.
      @return Optimized solution
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

}
