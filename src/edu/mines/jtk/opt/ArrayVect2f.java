/****************************************************************************
Copyright (c) 2004, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;
import java.util.logging.*;
import java.util.Arrays;
import java.util.Random;

/** Implement a Vect as a two dimensional array of floats.
    The second dimension can be of variable lengths.
    The embedded data are exposed by a getData method.  For all practical
    purposes this member is public, except that this class must always
    point to the same array.  The implementation as an array
    is the point of this class, to avoid duplicate implementations
    elsewhere.  Multiple inheritance is prohibited in java and
    prevents the mixin pattern, but you can share the wrapped array
    as a private member of your own class,
    and easily delegate all implemented methods.
*/
public class ArrayVect2f implements Vect {
  @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
  private static final long serialVersionUID = 2L;

  /** wrapped data */
  protected float[][] _data = null;
  private int[] _firstSample = null;

  /** variance for each sample */
  protected double _variance = 1.;
  private int _size = -1;

  /** Wrap an array as a Vect.
      @param data This will be assigned to the public data.
      @param variance This variance will be used to divide data in
      multiplyInverseCovariance.
  */
  public ArrayVect2f(float[][] data, double variance) {
    init(data, null, variance);
  }

  /** Wrap an array as a Vect.
      @param data This will be assigned to the public data.
      @param firstSample First non-zero sample of each trace.
      @param variance This variance will be used to divide data in
      multiplyInverseCovariance.
  */
  public ArrayVect2f(float[][] data, int[] firstSample, double variance) {
    init(data, firstSample, variance);
  }

  /** To be used with init() */
  protected ArrayVect2f() {}

  /** Wrap an array as a Vect.
      @param data This will be assigned to the public data.
      @param firstSample First non-zero sample of each trace.
      @param variance This variance will be used to divide data in
      multiplyInverseCovariance.
  */
  protected void init(float[][] data, int[] firstSample, double variance) {
    this._data = data;
    _variance = variance;
    _firstSample = firstSample;
    if (_firstSample != null && _firstSample.length != data.length) {
      throw new IllegalArgumentException
        ("Data and firstSample must have same length for slow dimension.");
    }
  }

  /** Get the embedded data.
      @return Same array as passed to constructore.
  */
  public float[][] getData() {
    return _data;
  }

  /** Call this method when there has been any external change
      to data returned by getData().  Any cached information
      about the dataset will be invalidated.
  */
  public void dataChanged() {
    _size = -1;
  }

  /** Return the size of the embedded array
 * @return size of embedded array */
  public int getSize() {
    if (_size<0) {
      _size = 0;
      for (int i=0; i<_data.length && _data.length > 0; ++i) {
        _size += _data[i].length;
      }
    }
    return _size;
  }

  // Vect interface
  public void add(double scaleThis, double scaleOther, VectConst other) {
    float s1 = (float) scaleThis;
    float s2 = (float) scaleOther;
    ArrayVect2f o = (ArrayVect2f) other;
    for (int i=0; i<_data.length; ++i) {
      for (int j=0; j<_data[i].length; ++j) {
        _data[i][j] = s1*_data[i][j] + s2*o._data[i][j];
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

  public double magnitude() {
    return Almost.FLOAT.divide (this.dot(this), getSize()*_variance, 0.);
  }

  // Vect interface
  public void constrain() {
    if (_firstSample == null) {
      return;
    }
    for (int i=0; i<_data.length; ++i) {
      Arrays.fill(_data[i], 0, _firstSample[i], 0.f); // remute
    }
  }

  // Vect interface
  public void postCondition() {}

  // VectConst interface
  @Override public ArrayVect2f clone() {
    try {
      float[][] newData = new float[_data.length][];
      for (int i=0 ; i<newData.length; ++i) {
        newData[i] = _data[i].clone();
      }
      int[] newFirstSample = (_firstSample !=null)
        ? _firstSample.clone()
        : null;
      ArrayVect2f result = (ArrayVect2f) super.clone();
      result.init(newData, newFirstSample, _variance);
      return result;
    } catch (CloneNotSupportedException ex) {
      IllegalStateException e = new IllegalStateException(ex.getMessage());
      e.initCause(ex);
      throw e;
    }
  }

  // VectConst interface
  public double dot(VectConst other) {
    ArrayVect2f rhs = (ArrayVect2f) other;
    double result = 0.;
    for (int i=0; i<_data.length; ++i) {
      for (int j=0; j<_data[i].length; ++j) {
        result += this._data[i][j] * rhs._data[i][j];
      }
    }
    return result;
  }

  /** Run tests
     @param args command line
     @throws Exception
   */
  public static void main(String[] args) throws Exception {
    {
      float[][] a = new float[31][21];
      for (int i=0; i<a.length; ++i) {
        for (int j=0; j<a[i].length; ++j) {
          a[i][j] = i+2.5f*j;
        }
      }
      Vect v = new ArrayVect2f(a, 2.);
      VectUtil.test(v);

      // test inverse covariance
      for (int i=0; i<a.length; ++i) {
        for (int j=0; j<a[i].length; ++j) {
          a[i][j] = 1;
        }
      }
      v = new ArrayVect2f(a, 3.);
      Vect w = v.clone();
      w.multiplyInverseCovariance();
      assert Almost.FLOAT.equal(1./3., v.dot(w));
      assert Almost.FLOAT.equal(1./3., v.magnitude());
    }

    {
      Random random = new Random(352);
      float[][] a = new float[201][];
      boolean oneWasShort = false;
      boolean oneWasLong = false;
      for (int i=0; i<a.length; ++i) {
        a[i] = new float[random.nextInt(11)];
        if (a[i].length ==0) oneWasShort = true;
        for (int j=0; j<a[i].length; ++j) {
          oneWasLong = true;
          a[i][j] = 5*random.nextFloat()-2;
        }
      }
      assert oneWasShort;
      assert oneWasLong;
      Vect v = new ArrayVect2f(a, 2.5);
      VectUtil.test(v);
    }
  }
}
