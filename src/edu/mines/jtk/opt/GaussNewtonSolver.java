/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;
import edu.mines.jtk.util.LogMonitor;
import edu.mines.jtk.util.Monitor;
import edu.mines.jtk.util.PartialMonitor;
import java.util.logging.Logger;

/** Solve least-squares inverse of a Transform.
    @author W.S. Harlan
*/
public class GaussNewtonSolver {
  private static boolean s_expensiveDebug = false;

  @SuppressWarnings("unused")
private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
  /**
    Solve nonquadratic objective function with Gauss Newton iterations.
    Minimizes
    <pre>
      [f(m+x)-data]'N[f(m+x)-data] + (m+x)'M(m+x)
    </pre>
    if dampOnlyPerturbation is true and
    <pre>
      [f(m+x)-data]'N[f(m+x)-data] + (x)'M(x)
    </pre>
    if dampOnlyPerturbation is false.

    m is the reference model and x is the perturbation of that model,
    Returns full solution m+x.

    <p>
    Iterative linearization of f(m+x) ~= f(m) + Fx makes the objective
    function quadratic in x: [f(m)+Fx-data]'N[f(m)+Fx-data] + (m+x)'M(m+x)
    x is solved with the specified number of conjugate gradient iterations.
    This perturbation is then scaled after searching the nonquadratic
    objective function with the specified number of line search iterations.
    The scaled perturbation x is added to the previous reference model m
    to update the new reference model m.  Relinearization is repeated for
    the specified number of linearization iterations. Cost is proprotional to
    <pre>
    linearizationIterations*( 2* conjugateGradIterations + lineSearchIterations );
    </pre>
    Hard constraints, if any, will be applied during line searches, and
    to the final result.
    <p>
    "Line search error" is an acceptable fraction of imprecision
    in the scale factor for the line search.  A very small value
    will cause the maximum number of line seach iterations to be used.
    @param data The data to be fit.
    @param referenceModel This is the starting velocity model.
    The optimized model will be a revised instance of this class.
    @param perturbModel If non-null, then use instances of this
    model to perturb the reference model.  It must be possible
    to project between the perturbed and reference model.
    The initial state of this vector is ignored.
    @param transform Describes the linear or nonlinear transform.
    @param dampOnlyPerturbation If true then, only damp perturbations
    to model. If false, then damp the reference model plus
    the perturbation.
    @param linearizationIterations Number of times to relinearize
    the non-linear transform. Set to 1 if transform is already linear.
    (Anything less than 1 will be set to 1)
    @param lineSearchIterations Number of iterations for a a line
    search to scale a pertubation before adding to reference model.
    Recommend 20 or greater.  Use 0 if you want to disable the
    line search altogether and add the perturbation with a scale
    factor of 1.
    @param conjugateGradIterations The specified number of conjugate
    gradient iterations.
    @param lineSearchError is an acceptable fraction of imprecision
    in the scale factor for the line search. Recommend 0.001 or smaller.
    @param monitor Report progress here, if non-null.
    @return Result of optimization
  */
  public static Vect solve (VectConst data,
                            VectConst referenceModel,
                            VectConst perturbModel,
                            Transform transform,
                            boolean dampOnlyPerturbation,
                            int conjugateGradIterations,
                            int lineSearchIterations,
                            int linearizationIterations,
                            double lineSearchError,
                            Monitor monitor) {
    if (s_expensiveDebug) {
      VectUtil.test(data);
      VectUtil.test(referenceModel);
      TransformQuadratic tq = new TransformQuadratic
        (data, referenceModel, perturbModel, transform, dampOnlyPerturbation);
      int precision = tq.getTransposePrecision();
      if (precision < 6) {
        throw new IllegalStateException("Bad transpose precision = "+precision);
      }
      tq.dispose();
    }
    if (monitor == null) monitor = new LogMonitor(null, null);
    monitor.report(0.);
    // Make copy of reference model that can be constrained and updated
    Vect m0 = referenceModel.clone();
    referenceModel = null;
    m0.constrain();
    if (linearizationIterations < 1) linearizationIterations = 1;

    // iteratively linearize transform
  LINEARIZE:
    for (int iter=0; iter < linearizationIterations; ++iter) {
      double frac = (3.*conjugateGradIterations)
        /(3.*conjugateGradIterations + lineSearchIterations);
      double begin = ((double)iter)/linearizationIterations;
      double mid = (iter+frac)/linearizationIterations;
      double end = (iter+1.0)/linearizationIterations;
      monitor.report(begin);
      // best fitting quadratic for current reference model m0
      TransformQuadratic transformQuadratic =
        new TransformQuadratic(data, m0, perturbModel, transform,
                               dampOnlyPerturbation);

      // get perturbation
      QuadraticSolver quadraticSolver
        = new QuadraticSolver(transformQuadratic);
      Vect perturbation = quadraticSolver.solve
        (conjugateGradIterations, new PartialMonitor(monitor, begin, mid));

      // terminate if perturbation is negligible
      double pp = perturbation.dot(perturbation);
      if (Almost.FLOAT.zero(pp)) {
        perturbation.dispose();
        transformQuadratic.dispose();
        break LINEARIZE;
      }

      // find best scale factor if line search is enabled
      double scalar = 1.;
      if (lineSearchIterations > 0) {
        TransformFunction transformFunction =
          new TransformFunction(transform, data, m0,
                                perturbation, dampOnlyPerturbation);
        ScalarSolver scalarSolver = new ScalarSolver(transformFunction);

        double scalarMin=0., scalarMax= 1.1,
          okError = lineSearchError, okFraction = lineSearchError;
        scalar = scalarSolver.solve
          (scalarMin, scalarMax, okError, okFraction, lineSearchIterations,
           new PartialMonitor(monitor, mid, end));
        transformFunction.dispose();
      }

      // add scaled perturbation to reference model
      m0.project(1., scalar, perturbation);

      // apply constraints to reference model
      m0.constrain();

      perturbation.dispose();
      transformQuadratic.dispose();

      monitor.report(end);
    }
    monitor.report(1.);
    return m0;
  }

  /**
    Solve quadratic objective function for linear transform.
    Minimizes
    <pre>
      [F(m+x)-data]'N[F(m+x)-data] + (m+x)'M(m+x)
    </pre>
    if dampOnlyPerturbation is true and
    <pre>
      [F(m+x)-data]'N[F(m+x)-data] + (x)'M(x)
    </pre>
    if dampOnlyPerturbation is false.
    @param data The data to be fit.
    @param referenceModel Linearize with respect to this model.
    @param linearTransform Describes the linear transform.
    @param dampOnlyPerturbation If true then, only damp perturbations
    to model. If false, then damp the reference model plus
    the perturbation.
    @param conjugateGradIterations The specified number of conjugate
    gradient iterations.
    @param monitor Report progress here, if non-null.
    @return Result of optimization
  */
  public static Vect solve (VectConst data,
                            VectConst referenceModel,
                            LinearTransform linearTransform,
                            boolean dampOnlyPerturbation,
                            int conjugateGradIterations,
                            Monitor monitor) {
    final int linearizationIterations = 1;
    final int lineSearchIterations = 0;
    final double lineSearchError = 0.;
    Transform transform = new LinearTransformWrapper(linearTransform);
    return GaussNewtonSolver.solve(data,
                                   referenceModel,
                                   null,
                                   transform,
                                   dampOnlyPerturbation,
                                   conjugateGradIterations,
                                   lineSearchIterations,
                                   linearizationIterations,
                                   lineSearchError,
                                   monitor);
  }

  // Evaluates the unapproximated objective function for a given scale factor
  //  of the perturbation to the reference model.
  private static class TransformFunction implements ScalarSolver.Function {
    VectConst _data;
    VectConst _referenceModel;
    VectConst _perturbation;
    Vect _model;
    TransformQuadratic _transformQuadratic;

    /** Constructor
       @param transform
       @param data
       @param referenceModel
       @param perturbation
       @param dampOnlyPerturbation
     */
    public TransformFunction(Transform transform,
                             VectConst data,
                             VectConst referenceModel,
                             VectConst perturbation,
                             boolean dampOnlyPerturbation) {
      _data = data;
      _referenceModel = referenceModel;
      _model = _referenceModel.clone();
      _perturbation = perturbation;
      _transformQuadratic = new TransformQuadratic
        (data, referenceModel, null, transform, dampOnlyPerturbation);
    }

    public double function(double scalar) {
      VectUtil.copy(_model, _referenceModel);
      _model.project(1., scalar, _perturbation);
      double result = _transformQuadratic.evalFullObjectiveFunction(_model);
      return result;
    }

    /** Free resources */
    public void dispose() {
      _model.dispose();
    }
  }

  /** Turn on expensive checking of transform and vector properties
      during solving of equations.
      @param debug If true, then turn on expensive debugging.*/
  public static void setExpensiveDebug(boolean debug) {
    s_expensiveDebug = debug;
  }

}
