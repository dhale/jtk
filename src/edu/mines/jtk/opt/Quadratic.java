package edu.mines.jtk.opt;

/** Define a second-order quadratic operation on a Vector
    0.5 x'Hx + b'x
    where H is a positive semidefinite quadratic and b is a
    linear gradient.

    @author W.S. Harlan
*/
public interface Quadratic {
  /** Multiply vector by the quadratic Hessian H.
      Perform the operation in-place.
      @param x Vector to be multiplied and modified.
   */
  public void multiplyHessian(Vect x);

  /** Multiply vector by an approximate inverse of the Hessian.
      Perform the operation in-place.  This method is
      useful to speed convergence.
      An empty implementation is equivalent to an identity.
      @param x Vector to be multiplied and modified.
   */
  public void inverseHessian(Vect x);

  /** Get the linear gradient term b of the quadratic expression.
      The recipient receives a unique copy that must be disposed.
      @return The vector b where the quadratic is x'Hx + b'x
  */
  public Vect getB();

}
