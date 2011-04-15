/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;

import java.io.IOException;
import java.util.logging.Logger;

/** Implements a Vect as a collection of ArrayVect1f's of fixed size.
    @author W.S. Harlan
*/
public class ArrayVect1fs implements Vect {
  static final long serialVersionUID = 1L;

  /** Array of wrapped data */
  protected ArrayVect1f[] _data = null;

  private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");

  /** Wrap an array of ArrayVect1f's
      @param data Wrap this array of ArrayVect1f's.
  */
  public ArrayVect1fs(ArrayVect1f[] data)  {init(data);}

  /** To be used with init() */
  public ArrayVect1fs() {}

  /** Initialize class
      @param data Wrap this collection of 1D arrays */
  private void init(ArrayVect1f[] data)  {_data = data;}

  /** Return the size of the embedded array
      @return size of embedded array
  */
  public int getSize() {return _data.length;}

  /** Get the embedded data
      @return Same array as passed to constructor.
   */
  public ArrayVect1f[] getData() {
    return _data;
  }

  // Cloneable
  @Override public ArrayVect1fs clone() {
    try {
      ArrayVect1fs result = (ArrayVect1fs) super.clone();
      if (_data != null) {
        ArrayVect1f[] data = _data.clone();
        for (int i=0; i<data.length; ++i) {
          data[i] = data[i].clone();
        }
        result.init(data);
      } // else being used by a class factory
      return result;
    } catch (CloneNotSupportedException ex) {
      IllegalStateException e = new IllegalStateException(ex.getMessage());
      e.initCause(ex);
      throw e;
    }
  }

  // Vect
  @Override
  public double dot(VectConst other) {
    double result = 0;
    ArrayVect1fs rhs = (ArrayVect1fs) other;
    for (int i=0; i<_data.length; ++i) {
      result += _data[i].dot(rhs._data[i]);
    }
    return result;
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

  // Vect
  @Override
  public void dispose() {
      for (ArrayVect1f a_data : _data) {
          a_data.dispose();
      }
    _data = null;
  }

  // Vect
  @Override
  public void multiplyInverseCovariance() {
    double scale = Almost.FLOAT.divide (1., getSize(), 0.);
      for (ArrayVect1f a_data : _data) {
          a_data.multiplyInverseCovariance();
          VectUtil.scale(a_data, scale);
      }
  }

  // Vect
  @Override
  public void constrain() {
      for (ArrayVect1f a_data : _data) {
          a_data.constrain();
      }
  }

  // Vect
  @Override
  public void postCondition() {}

  // Vect
  @Override
  public void add(double scaleThis, double scaleOther, VectConst other)  {
    ArrayVect1fs rhs = (ArrayVect1fs) other;
    for (int i=0; i<_data.length; ++i) {
      _data[i].add(scaleThis, scaleOther, rhs._data[i]);
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
    double result = 0.;
      for (ArrayVect1f a_data : _data) {
          result += a_data.magnitude();
      }
    result = Almost.FLOAT.divide (result, getSize() , 0.);
    return result;
  }

  // Serializable
  private void writeObject(java.io.ObjectOutputStream out)
    throws IOException {
    out.writeInt(_data.length);
      for (ArrayVect1f a_data : _data) {
          out.writeObject(a_data);
      }
  }

  // Serializable
  private void readObject(java.io.ObjectInputStream in)
    throws IOException, ClassNotFoundException {
    int length = in.readInt();
    _data = new ArrayVect1f[length];
    for (int i=0; i<_data.length; ++i) {
      _data[i] = (ArrayVect1f) in.readObject();
    }
  }
}

