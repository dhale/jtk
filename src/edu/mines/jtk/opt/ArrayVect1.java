/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;
import java.util.logging.Logger;

/** Implements a Vect by wrapping an array of doubles.
    The embedded data are exposed by a getData method.  For all practical
    purposes this member is public, except that this class must always
    point to the same array.  The implementation as an array
    is the point of this class, to avoid duplicate implementations
    elsewhere.  Multiple inheritance is prohibited and
    prevents the mixin pattern, but you can share the wrapped array
    as a private member of your own class,
    and easily delegate all implemented methods.
    @author W.S. Harlan
 */
public class ArrayVect1 implements Vect {
  /** wrapped data */
  private double[] _data = null;
  /** variance */
  private double _variance = 1.;
  @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
  private static final long serialVersionUID = 1L;

  /** Construct from an array of data.
      @param data This is the data that will be manipulated.
      @param variance The method multiplyInverseCovariance()
      will divide all samples by this number.  Pass a value
      of 1 if you do not care.
  */
  public ArrayVect1(double[] data, double variance) {
    init(data,variance);
  }

  /** To be used with init() */
  protected ArrayVect1() {}

  /** Construct from an array of data.
      @param data This is the data that will be manipulated.
      @param variance The method multiplyInverseCovariance()
      will divide all samples by this number.  Pass a value
      of 1 if you do not care.
  */
  protected void init(double[] data, double variance) {
    _data = data;
    _variance = variance;
  }

  /** Return the size of the embedded array
 * @return size of the embedded array */
  public int getSize() {return _data.length;}

  /** Get the embedded data
      @return Same array as passed to constructore.
   */
  public double[] getData() {
    return _data;
  }

  // Cloneable
  @Override public ArrayVect1 clone() {
    try {
      ArrayVect1 result = (ArrayVect1) super.clone();
      result._data = result._data.clone();
      return result;
    } catch (CloneNotSupportedException ex) {
      IllegalStateException e = new IllegalStateException(ex.getMessage());
      e.initCause(ex);
      throw e;
    }
  }

  // VectConst
  public double dot(VectConst other) {
    double result = 0;
    ArrayVect1 rhs = (ArrayVect1) other;
    for (int i=0; i<_data.length; ++i) {
      result += _data[i] * rhs._data[i];
    }
    return result;
  }

  // Object
  @Override public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (int i=0; i<_data.length; ++i) {
      sb.append(""+_data[i]);
      if (i < _data.length -1) {sb.append(", ");}
    }
    sb.append(")");
    return sb.toString();
  }

  // Vect
  public void dispose() {
    _data = null;
  }

  // Vect
  public void multiplyInverseCovariance() {
    double scale = Almost.FLOAT.divide (1., getSize()*_variance, 0.);
    VectUtil.scale(this, scale);
  }

  // Vect
  public void constrain() {}

  // Vect
  public void postCondition() {}

  // Vect
  public void add(double scaleThis, double scaleOther, VectConst other)  {
    ArrayVect1 rhs = (ArrayVect1) other;
    for (int i=0; i<_data.length; ++i) {
      _data[i] = scaleThis*_data[i] + scaleOther*rhs._data[i];
    }
  }

  // Vect
  public void project(double scaleThis, double scaleOther, VectConst other)  {
    add(scaleThis, scaleOther, other);
  }

  // VectConst
  public double magnitude() {
    return Almost.FLOAT.divide (this.dot(this), getSize()*_variance, 0.);
  }
}

