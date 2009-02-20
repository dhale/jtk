/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import java.util.logging.Logger;

import edu.mines.jtk.util.Almost;

/** Implement a Vect as a three-dimensional array of floats.
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

public class ArrayVect3f implements Vect {

  /** wrapped data */
  protected float[][][] _data = null;

  /** variance for all samples */
  protected double _variance = 1.;

  @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
  private static final long serialVersionUID = 1L;

  /** Wrap an array as a Vect.
      @param data This will be assigned to the public data.
      @param variance This variance will be used to divide data in
      multiplyInverseCovariance.
  */
  public ArrayVect3f(float[][][] data, double variance) {
    init(data,variance);
  }

  /** Get the value of the variance passed to the constructor.
      @return This variance will be used to divide data in
      multiplyInverseCovariance.
  */
  public double getVariance() {return _variance;}

  /** To be used with init() */
  protected ArrayVect3f() {}

  /** Wrap an array as a Vect.
      @param data This will be assigned to the public data.
      @param variance This variance will be used to divide data in
      multiplyInverseCovariance.
  */
  protected void init(float[][][] data, double variance) {
    this._data = data;
    _variance = variance;
  }

  /** Get the embedded data.
      @return Same array as passed to constructore.
   */
  public float[][][] getData() {
    return _data;
  }

  /** Return the size of the embedded array
      @return size of embedded array */
  public int getSize() {
    return _data.length*_data[0].length*_data[0][0].length;
  }

  // Vect interface
  public void add(double scaleThis, double scaleOther, VectConst other) {
    float s1 = (float) scaleThis;
    float s2 = (float) scaleOther;
    ArrayVect3f rhs = (ArrayVect3f) other;
    for (int i=0; i<_data.length; ++i) {
      for (int j=0; j<_data[0].length; ++j) {
        for (int k=0; k<_data[0][0].length; ++k) {
          _data[i][j][k] = s1*_data[i][j][k] + s2*rhs._data[i][j][k];
        }
      }
    }
  }

  // Vect interface
  public void project(double scaleThis, double scaleOther, VectConst other) {
    add(scaleThis, scaleOther, other);
  }

  // Vect interface
  public void dispose() {
    _data = null;
  }

  // Vect interface
  public void multiplyInverseCovariance() {
    double scale = Almost.FLOAT.divide (1., getSize()*_variance, 0.);
    VectUtil.scale(this, scale);
  }

  // VectConst interface
  public double magnitude() {
    return Almost.FLOAT.divide (this.dot(this), getSize()*_variance, 0.);
  }

  // Vect interface
  public void constrain() {}

  // Vect interface
  public void postCondition() {}

  // VectConst interface
  @Override public ArrayVect3f clone() {
    try {
      float[][][] newData = new float[_data.length][_data[0].length][];
      for (int i=0 ; i<newData.length; ++i) {
        for (int j=0 ; j<newData[0].length; ++j) {
          newData[i][j] = _data[i][j].clone();
        }
      }
      ArrayVect3f result = (ArrayVect3f) super.clone();
      result.init(newData, _variance);
      return result;
    } catch (CloneNotSupportedException ex) {
      IllegalStateException e = new IllegalStateException(ex.getMessage());
      e.initCause(ex);
      throw e;
    }
  }

  // VectConst interface
  public double dot(VectConst other) {
    double result = 0.;
    ArrayVect3f rhs = (ArrayVect3f) other;
    for (int i=0; i<_data.length; ++i) {
      for (int j=0; j<_data[0].length; ++j) {
        for (int k=0; k<_data[0][0].length; ++k) {
          result += (double) _data[i][j][k] * rhs._data[i][j][k];
        }
      }
    }
    return result;
  }
}
