/****************************************************************************
 Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;
import edu.mines.jtk.util.Monitor;
import edu.mines.jtk.util.PartialMonitor;

import java.util.logging.Logger;

/**
 * Solve least-squares inverse of a non-linear Transform.
 * See QuadraticSolver to solve least-squares inverse of a linear Transform.
 *
 * @author W.S. Harlan
 */
public class GaussNewtonSolver {
    private static boolean s_expensiveDebug = false;

    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");

    private GaussNewtonSolver() {
    }

    /**
     * Solve nonquadratic objective function with Gauss Newton iterations.
     * Minimizes
     * <pre>
     * [f(m+x)-data]'N[f(m+x)-data] + (m+x)'M(m+x)
     * </pre>
     * if dampOnlyPerturbation is true and
     * <pre>
     * [f(m+x)-data]'N[f(m+x)-data] + (x)'M(x)
     * </pre>
     * if dampOnlyPerturbation is false.
     * <p/>
     * m is the reference model and x is the perturbation of that model,
     * Returns full solution m+x.
     * <p/>
     * <p/>
     * Iterative linearization of f(m+x) ~= f(m) + Fx makes the objective
     * function quadratic in x: [f(m)+Fx-data]'N[f(m)+Fx-data] + (m+x)'M(m+x)
     * x is solved with the specified number of conjugate gradient iterations.
     * This perturbation is then scaled after searching the nonquadratic
     * objective function with the specified number of line search iterations.
     * The scaled perturbation x is added to the previous reference model m
     * to update the new reference model m.  Relinearization is repeated for
     * the specified number of linearization iterations. Cost is proprotional to
     * <pre>
     * linearizationIterations*( 2* conjugateGradIterations + lineSearchIterations );
     * </pre>
     * Hard constraints, if any, will be applied during line searches, and
     * to the final result.
     * <p/>
     * "Line search error" is an acceptable fraction of imprecision
     * in the scale factor for the line search.  A very small value
     * will cause the maximum number of line seach iterations to be used.
     *
     * @param data                    The data to be fit.
     * @param referenceModel          This is the starting velocity model.
     *                                The optimized model will be a revised instance of this class.
     * @param perturbModel            If non-null, then use instances of this
     *                                model to perturb the reference model.  It must be possible
     *                                to project between the perturbed and reference model.
     *                                The initial state of this vector is ignored.
     * @param transform               Describes the linear or nonlinear transform.
     * @param dampOnlyPerturbation    If true then, only damp perturbations
     *                                to model. If false, then damp the reference model plus
     *                                the perturbation.
     * @param linearizationIterations Number of times to relinearize
     *                                the non-linear transform. Set to 1 if transform is already linear.
     *                                (Anything less than 1 will be set to 1)
     * @param lineSearchIterations    Number of iterations for a a line
     *                                search to scale a pertubation before adding to reference model.
     *                                Recommend 20 or greater.  Use 0 if you want to disable the
     *                                line search altogether and add the perturbation with a scale
     *                                factor of 1.
     * @param conjugateGradIterations The specified number of conjugate
     *                                gradient iterations.
     * @param lineSearchError         is an acceptable fraction of imprecision
     *                                in the scale factor for the line search. Recommend 0.001 or smaller.
     * @param monitor                 Report progress here, if non-null.
     * @return Result of optimization, using a cloned instance of referenceModel.
     */
    public static Vect solve(final VectConst data,
                             VectConst referenceModel,
                             final VectConst perturbModel,
                             final Transform transform,
                             final boolean dampOnlyPerturbation,
                             final int conjugateGradIterations,
                             final int lineSearchIterations,
                             int linearizationIterations,
                             final double lineSearchError,
                             Monitor monitor) {
        if (s_expensiveDebug) {
            VectUtil.test(data);
            VectUtil.test(referenceModel);
            final TransformQuadratic tq = new TransformQuadratic
                    (data, referenceModel, perturbModel, transform, dampOnlyPerturbation);
            final int precision = tq.getTransposePrecision();
            if (precision < 6) {
                throw new IllegalStateException("Bad transpose precision = " + precision);
            }
            tq.dispose();
        }
        if (monitor == null) {
            monitor = Monitor.NULL_MONITOR;
        }
        monitor.report(0.0);
        // Make copy of reference model that can be constrained and updated
        final Vect m0 = referenceModel.clone();
        referenceModel = null;
        m0.constrain();
        if (linearizationIterations < 1) {
            linearizationIterations = 1;
        }

        // iteratively linearize transform
        LINEARIZE:
        for (int iter = 0; iter < linearizationIterations && !monitor.isCanceled(); ++iter) {
            final double frac = (3.0 * conjugateGradIterations)
                    / (3.0 * conjugateGradIterations + lineSearchIterations);
            final double begin = ((double) iter) / linearizationIterations;
            final double mid = (iter + frac) / linearizationIterations;
            final double end = (iter + 1.0) / linearizationIterations;
            monitor.report(begin);
            // best fitting quadratic for current reference model m0
            final TransformQuadratic transformQuadratic =
                    new TransformQuadratic(data, m0, perturbModel, transform,
                            dampOnlyPerturbation);

            // get perturbation
            final QuadraticSolver quadraticSolver
                    = new QuadraticSolver(transformQuadratic);
            final Vect perturbation = quadraticSolver.solve
                    (conjugateGradIterations, new PartialMonitor(monitor, begin, mid));

            // terminate if perturbation is negligible
            final double pp = perturbation.dot(perturbation);
            if (Almost.FLOAT.zero(pp)) {
                perturbation.dispose();
                transformQuadratic.dispose();
                break LINEARIZE;
            }

            // find best scale factor if line search is enabled
            double scalar = 1.0;
            if (lineSearchIterations > 0) {
                final TransformFunction transformFunction =
                        new TransformFunction(transform, data, m0,
                                perturbation, dampOnlyPerturbation);
                final ScalarSolver scalarSolver = new ScalarSolver(transformFunction);

                final double scalarMin = 0.0;
                final double scalarMax = 1.1;
                final double okError = lineSearchError;
                final double okFraction = lineSearchError;
                scalar = scalarSolver.solve
                        (scalarMin, scalarMax, okError, okFraction, lineSearchIterations,
                                new PartialMonitor(monitor, mid, end));
                transformFunction.dispose();
            }

            // add scaled perturbation to reference model
            m0.project(1.0, scalar, perturbation);

            // apply constraints to reference model
            m0.constrain();

            perturbation.dispose();
            transformQuadratic.dispose();

            monitor.report(end);
        }
        monitor.report(1.0);
        return m0;
    }

    // Evaluates the unapproximated objective function for a given scale factor
    //  of the perturbation to the reference model.
    private static class TransformFunction implements ScalarSolver.Function {
        private final VectConst _referenceModel;
        private final VectConst _perturbation;
        private final Vect _model;
        private final TransformQuadratic _transformQuadratic;

        /* Constructor
          @param transform
          @param data
          @param referenceModel
          @param perturbation
          @param dampOnlyPerturbation
        */
        private TransformFunction(final Transform transform,
                                  final VectConst data,
                                  final VectConst referenceModel,
                                  final VectConst perturbation,
                                  final boolean dampOnlyPerturbation) {
            _referenceModel = referenceModel;
            _model = _referenceModel.clone();
            _perturbation = perturbation;
            _transformQuadratic = new TransformQuadratic
                    (data, referenceModel, null, transform, dampOnlyPerturbation);
        }

        @Override
        public double function(final double scalar) {
            VectUtil.copy(_model, _referenceModel);
            _model.project(1.0, scalar, _perturbation);
            return _transformQuadratic.evalFullObjectiveFunction(_model);
        }

        /**
         * Free resources
         */
        public void dispose() {
            _model.dispose();
        }
    }

    /**
     * Turn on expensive checking of transform and vector properties
     * during solving of equations.
     *
     * @param debug If true, then turn on expensive debugging.
     */
    public static void setExpensiveDebug(final boolean debug) {
        s_expensiveDebug = debug;
    }

}
