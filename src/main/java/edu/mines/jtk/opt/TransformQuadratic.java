/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;

import java.util.logging.Logger;

/** For a linearized transform, implement the Gauss-Newton
    quadratic approximation of a damped least-squares objective function.
    Specifically,
    <pre>
      [f(m+x)-data]'N[f(m+x)-data] + (m+x)'M(m+x)
    </pre>

    Linearization of
    <pre>
      f(m+x) ~= f(m) + Fx
    </pre>
    makes the function quadratic in x
    <pre>
      [f(m)+Fx-data]'N[f(m)+Fx-data] + (m+x)'M(m+x)
    </pre>

    If only perturbations are damped, then the objective function is
    <pre>
       [f(m)+Fx-data]'N[f(m)+Fx-data] + x'Mx
    </pre>

    <pre>
    m is the reference model, and x is the perturbation.
    f(m) is the nonlinear forward transform.
    F is the linearized version of f(m) with respect to the
    reference model m
    N is the inverse covariance of the data and
    M is the inverse covariance of the model.
    </pre>

    The Hessian is given by
    <pre>
      H = F'NF + M
    </pre>.

    b is determined by
    <pre>
       b = -F' N [data - f(m)] + Mm
    </pre>
    for full damping, or by
    <pre>
      b = -F' N [data - f(m)]
    </pre>
    if only perturbations are damped.

    @author W.S. Harlan
*/
public class TransformQuadratic implements Quadratic {
  private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
  private VectConst _data;
  private VectConst _referenceModel;
  private VectConst _perturbModel;
  private Transform _transform;
  private boolean _dampOnlyPerturbation = false;

  /** Wrap known data, reference mode, and transform
      as a Gauss-Newton objectiveFunction.
      @param referenceModel This is the reference model
      m in the objective function.
      @param perturbModel  If non-null, then use instances
      of this vector to perturb the reference model.
      It must be possible to project the referenceModel
      and perturbModel into each other.
      @param data The data to be fit.
      @param transform Optimize with this transform
      @param dampOnlyPerturbation If true then use objective function
      <code> [f(m)+Fx-data]'N[f(m)+Fx-data] + x'Mx </code>
   */
  public TransformQuadratic(VectConst data,
                            VectConst referenceModel,
                            VectConst perturbModel,
                            Transform transform,
                            boolean dampOnlyPerturbation) {
    _data = data;
    _referenceModel = referenceModel;
    _perturbModel = perturbModel;
    _transform = transform;
    _dampOnlyPerturbation = dampOnlyPerturbation;
  }

  /** Run a few tests to ensure that transpose satisfies definition.
      This is expensive and intended only for test code.
      @return number of sigficant digits of precision in transpose.
      Expect 5 or 6.  Unacceptable if not 3 or more.  A value
      of 3 or 4 probably indicates a subtle error.
  */
  public int getTransposePrecision() {
    VectConst d = _data; // arbitrary nonzero data
    Vect b = this.getB(); // arbitrary nonzero model
    double bb = b.dot(b);
    checkNaN(bb);
    assert ! Almost.FLOAT.zero(bb) : "Cannot test with zero-magnitude b";

    // Get forward transform of b
    Vect Fb = VectUtil.cloneZero(d);
    Vect bSave = b.clone();
    _transform.forwardLinearized(Fb, b, _referenceModel);

    // make sure didn't step on wrong vector
    assert VectUtil.areSame(b, bSave) :"model was changed by forward model";
    bSave.dispose();

    // check that forward zeros output data.
    Vect test = d.clone();
    _transform.forwardLinearized(test, b, _referenceModel);
    assert VectUtil.areSame(test, Fb): "forwardLinearized should zero data";
    test.dispose();

    // Get transpose of d
    Vect Ad = VectUtil.cloneZero(b);
    Vect dSave = d.clone();
    _transform.addTranspose(d, Ad,_referenceModel);
    double transposeMagnitude = Ad.dot(Ad);
    checkNaN(transposeMagnitude);

    // make sure didn't step on wrong vector
    assert VectUtil.areSame(d, dSave) : "data was changed by transpose";
    dSave.dispose();

    // ensure that transpose adds to existing model
    test = b.clone(); // make initial size comparable to transpose
    double scaleTest = 1.1*Math.sqrt(transposeMagnitude/bb);
    VectUtil.scale(test, scaleTest);
    _transform.addTranspose(d, test,_referenceModel);
    assert ! VectUtil.areSame(Ad, test): "Transpose should not zero model.  "+
      "Magnitude: b="+bb+ "trans="+transposeMagnitude+" test="+test.dot(test);
    test.add(1., -1., Ad);
    VectUtil.scale(test, 1./scaleTest);
    assert VectUtil.areSame(test, b) : "Transpose did not add to model vector";
    test.dispose();

    // get dot products that should be equal
    double dFb = d.dot(Fb);
    double Adb = Ad.dot(b);
    assert !Almost.FLOAT.zero(dFb) : "zero magnitude test: dFb is zero";
    assert !Almost.FLOAT.zero(Adb) : "zero magnitude test: Adb is zero";
    checkNaN(dFb);
    checkNaN(Adb);

    int significantDigits = 10;
    boolean matches = false;
    while (! matches && significantDigits > 0) {
      Almost almost = new Almost (significantDigits);
      matches = almost.equal(dFb, Adb);
      if (!matches) {
        --significantDigits;
      }
    }
    if (significantDigits < 3) {
      LOG.severe("Transpose precision is unacceptable: dFb="+dFb+" Adb="+Adb);
    }
    Ad.dispose();
    Fb.dispose();
    b.dispose();
    return significantDigits;
  }

  /** Multiply by Hessian <code> H = F'NF + M </code>
      @param x Vector to be multiplied and modified
   */
  @Override
  public void multiplyHessian(Vect x) {
    Vect data = _data.clone();
    _transform.forwardLinearized(data, x,_referenceModel); // data is Fx
    data.multiplyInverseCovariance(); // data is NFx
    x.multiplyInverseCovariance(); // x is Mx
    _transform.addTranspose(data, x,_referenceModel);// x is (F'NF +M)x
    data.dispose();
  }

  // Quadratic
  @Override
  public void inverseHessian(Vect x) {
    _transform.inverseHessian(x, _referenceModel);
  }

  /** Return gradient term of quadratic.
      This instance will be in the vector space of the perturbModel,
      if provided.  Otherwise, uses an instance of the referenceModel.
      @return Value of <code> b = -F' N [data - f(m)] + Mm  </code>
      if dampOnlyPerturbation is false, and
      <code> b = -F' N [data - f(m)] </code> if dampOnlyPerturbation is true.
  */
  @Override
  public Vect getB() {
    Vect data = VectUtil.cloneZero(_data); // data is data with zeros
    _transform.forwardNonlinear(data, _referenceModel); // data is f(m)
    data.add(1., -1.,_data);                  // data is -e = -(d - f(m))
    _transform.adjustRobustErrors(data);      // (remove outliers from e)
    data.multiplyInverseCovariance();         // data is -Ne
    Vect b;
    if (_dampOnlyPerturbation) {
      if (_perturbModel != null) {
        b = VectUtil.cloneZero(_perturbModel);  // b is 0
      } else {
        b = VectUtil.cloneZero(_referenceModel); // b is 0
      }
    } else {
      if (_perturbModel != null) {
        b = _perturbModel.clone();
        b.project(0., 1., _referenceModel);      // b is m
      } else {
        b = _referenceModel.clone();      // b is m
      }
      b.multiplyInverseCovariance();           // b is M m
    }
    _transform.addTranspose(data, b, _referenceModel);  // b is -F'Ne [+ Mm]
    data.dispose();
    return b;
  }

  /** Evaluate the full objective function without approximation.
      Provide a model in the vector space of the referenceModel.
      perturbModel is unused.
      Useful for line-search of best scale factor.
      If dampedPerturbation, evaluates
      <pre>
        [f(m)-data]'N[f(m)-data] + (m-m0)'M(m-m0)
      </pre>
      where m0 is the reference model.
      Otherwise evaluates
      <pre>
        [f(m)-data]'N[f(m)-data] + m'M m
      </pre>
      @param m Model to be evaluated.
      @return Value of objective function.
   */
  public double evalFullObjectiveFunction(VectConst m) {

    Vect data = VectUtil.cloneZero(_data); // data is zeros

    Vect model = m.clone(); // model is m
    model.constrain();          // may have been done already

    _transform.forwardNonlinear(data, model); // data is f(m)
    data.add(1., -1., _data);   // data is e = f(m) - data
    _transform.adjustRobustErrors(data);
    double eNe = data.magnitude(); // eNe is e'Ne
    checkNaN(eNe);
    data.dispose();

    // damp perturbation if requested
    if (_dampOnlyPerturbation) {
      model.add(1., -1., _referenceModel); // model is (m-m0)
    }

    double mMm = model.magnitude(); // mMm is (m-m0)'M(m-m0)
    checkNaN(mMm);
    model.dispose();
    return (eNe + mMm);
  }

  /** Abort if NaN's appear.
      @param value Abort if this value is a NaN.
   */
  private static void checkNaN(double value) {
    if (value*0 != 0) {
      throw new IllegalStateException("Value is a NaN");
    }
  }

  /** Free up internal cached vectors */
  public void dispose() {}
}

