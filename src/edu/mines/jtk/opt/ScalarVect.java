/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import java.util.logging.Logger;

import edu.mines.jtk.util.Almost;

/** Implements a Vect by wrapping a single double */
public class ScalarVect implements Vect {
  /** wrapped data */
  protected double _value = 0;
  /** variance for value */
  protected double _variance = 1.;
  @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
  private static final long serialVersionUID = 1L;

  /** Specify the initial value
      @param value The initial value of the wrapped scalar
      @param variance The method multiplyInverseCovariance()
      will divide the scalar by this number.
      Pass a value of 1 if you do not care.
  */
  public ScalarVect(double value, double variance) {
    init(value, variance);
  }

  /** To be used with init() */
  protected ScalarVect() {init (0., 1.);}

  /** Initialize the Vect.
      @param value The initial value of the wrapped scalar .
      @param variance The method multiplyInverseCovariance()
      will divide the scalar by this number.
      Pass a value of 1 if you do not care.
  */
  public void init(double value, double variance) {
    _value = value;
    _variance = variance;
  }

  /** Get the value of the scalar.
      @return The wrapped scalar.
   */
  public double get() {return _value;}

  /** Set the value of the scalar.
      @param value The new value of the wrapped scalar.
   */
  public void set(double value) {_value = value;}

  // VectConst, Cloneable
  @Override public ScalarVect clone() {
    try {
      ScalarVect result = (ScalarVect) super.clone();
      return result;
    } catch (CloneNotSupportedException ex) {
      IllegalStateException e = new IllegalStateException(ex.getMessage());
      e.initCause(ex);
      throw e;
    }
  }

  // VectConst
  public double dot(VectConst other) {
    ScalarVect rhs = (ScalarVect) other;
    return _value * rhs._value;
  }

  // Object
  @Override public String toString() {
    return "ScalarVect<"+_value+">";
  }

  // Vect
  public void dispose() {
    _value = Double.NaN;
    _variance = Double.NaN;
  }

  // Vect
  public void multiplyInverseCovariance() {
    _value = Almost.FLOAT.divide (_value, _variance, 0.);
  }

  // Vect
  public void constrain() {}

  // Vect
  public void postCondition() {}

  // Vect
  public void add(double scaleThis, double scaleOther, VectConst other)  {
    ScalarVect rhs = (ScalarVect) other;
    _value = scaleThis * _value + scaleOther * rhs._value;
  }

  // Vect
  public void project(double scaleThis, double scaleOther, VectConst other)  {
    add(scaleThis, scaleOther, other);
  }

  // VectConst
  public double magnitude() {
    return Almost.FLOAT.divide (this.dot(this), _variance, 0.);
  }
}

