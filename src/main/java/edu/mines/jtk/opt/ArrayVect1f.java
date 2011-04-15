/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;

import java.util.Arrays;
import java.util.logging.Logger;

/** Implements a Vect by wrapping an array of floats.
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
public class ArrayVect1f implements Vect {
  private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
  private static final long serialVersionUID = 2L;
  private static final int VERSION = 1;

  /** Array of wrapped data */
  protected transient float[] _data = null;

  /** Variance of each ArrayVect1f */
  protected transient double _variance = 1.;

  /** This is the first sample to treat as non-zero.
      Earlier samples should be constrained to zero. */
  protected transient int _firstSample = 0;

  /** Construct from an array of data.
      @param data This is the data that will be manipulated.
      @param firstSample This is the first sample to treat as non-zero.
      Earlier samples should be constrained to zero.
      @param variance The method multiplyInverseCovariance()
      will divide all samples by this number.  Pass a value
      of 1 if you do not care.
  */
  public ArrayVect1f(float[] data, int firstSample, double variance) {
    init(data, firstSample, variance);
  }

  /** Constructor for derived classes that call init() */
  protected ArrayVect1f() {}

  /** Construct from an array of data.
      @param data This is the data that will be manipulated.
      @param firstSample This is the first sample to treat as non-zero.
      Earlier samples should be constrained to zero.
      @param variance The method multiplyInverseCovariance()
      will divide all samples by this number.  Pass a value
      of 1 if you do not care.
  */
  protected final void init(float[] data, int firstSample, double variance) {
    _data = data;
    _firstSample = firstSample;
    _variance = variance;
  }

  /** This is the first sample to treat as non-zero.
      @return first non-zero sample
  */
  public int getFirstSample() {
    return _firstSample;
  }

  /** Return the size of the embedded array
      @return size of embedded array*/
  public int getSize() {return _data.length;}

  /** Get the embedded data
      @return Same array as passed to constructor.
  */
  public float[] getData() {
    return _data;
  }

  /** Set the internal data array to new values.
      @param data Copy this data into the internal wrapped array.
  */
  public void setData(float[] data) {
    System.arraycopy(data,0, _data, 0, _data.length);
  }

  /** Fill a VectContainer with instances of ArrayVect1f
      from a 2D array.
      @param container Container to be filled with instances of float[]
      wrapped as ArrayVect1f.
      @param data Array of data to be wrapped.
      @param firstSamples Array of first non-zero samples in each array.
      @param variance Variance of each ArrayVect1f
  */
  public static void fillContainer(VectContainer container,
                                   int[] firstSamples,
                                   float[][] data, double variance) {
    for (int i=0; i<data.length; ++i) {
      container.put(i, new ArrayVect1f(data[i], firstSamples[i], variance));
    }
  }

  /** Extract 2D array from a VectContainer with instances of ArrayVect1f.
      @param data Array of data to be extracted.
      @param container Container of ArrayVect1f to be extracted from.
  */
  public static void extractContainer(float[][] data,
                                      VectContainer container) {
    for (int i=0; i<data.length; ++i) {
      ArrayVect1f trace = (ArrayVect1f) container.get(i);
      float[] traceData = trace.getData();
      System.arraycopy(traceData,0, data[i],0, data[i].length);
    }
  }

  // Cloneable
  @Override public ArrayVect1f clone() {
    try {
      ArrayVect1f result = (ArrayVect1f) super.clone();
      if (_data != null) {
        float[] newData = _data.clone();
        result.init(newData, _firstSample, _variance);
      } // else being used by a class factory
      return result;
    } catch (CloneNotSupportedException ex) {
      IllegalStateException e = new IllegalStateException(ex.getMessage());
      e.initCause(ex);
      throw e;
    }
  }

  // Object
  @Override public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (int i=0; i<_data.length; ++i) {
      sb.append(String.valueOf(_data[i]));
      if (i < _data.length -1) {sb.append(", ");}
    }
    sb.append(")");
    return sb.toString();
  }

  // VectConst
  @Override
  public double dot(VectConst other) {
    double result = 0;
    ArrayVect1f rhs = (ArrayVect1f) other;
    for (int i=0; /*Math.max(_firstSample,rhs._firstSample)//breaks transpose*/
         i<_data.length;
         ++i) {
      result += (double)_data[i] * rhs._data[i];
    }
    return result;
  }

  //Vect
  @Override
  public void dispose() {
    _data = null;
  }

  // Vect
  @Override
  public void multiplyInverseCovariance() {
    double scale = Almost.FLOAT.divide (1., getSize()*_variance, 0.);
    VectUtil.scale(this, scale);
  }

  // Vect
  @Override
  public void constrain() {
    Arrays.fill(_data, 0, _firstSample, 0.f); // remute
  }

  // Vect
  @Override
  public void add(double scaleThis, double scaleOther, VectConst other)  {
    float s1 = (float) scaleThis;
    float s2 = (float) scaleOther;
    ArrayVect1f rhs = (ArrayVect1f) other;
    for (int i=0; i<_data.length; ++i) {
      _data[i] = s1*_data[i] + s2*rhs._data[i];
    }
  }

  // Vect
  @Override
  public void project(double scaleThis, double scaleOther, VectConst other)  {
    add(scaleThis, scaleOther, other);
  }

  // VectConst
  @Override
  public double magnitude() {
    return Almost.FLOAT.divide (this.dot(this), getSize()*_variance, 0.);
  }

  // Vect
  @Override
  public void postCondition() {}

  // Serializable
  private void writeObject(java.io.ObjectOutputStream out)
    throws java.io.IOException {
    java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
    map.put("d", _data);
    map.put("v", _variance);
    map.put("f", _firstSample);
    map.put("V", VERSION);
    out.writeObject(map);
  }

  private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {

    @SuppressWarnings("unchecked") java.util.Map<String, Object> map =
      (java.util.Map<String, Object>) in.readObject();

    _data = (float[]) map.get("d");
    _variance = (Double) map.get("v");
    _firstSample = (Integer) map.get("f");

    int version = (Integer) map.get("V");
    if (version != VERSION) {
      java.util.logging.Logger.getLogger(this.getClass().getName()).warning
        ("Need to convert data from version "+version+" to "+VERSION);
    }
  }
}

