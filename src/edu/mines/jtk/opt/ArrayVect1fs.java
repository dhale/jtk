/****************************************************************************
Copyright (c) 2004, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;
import java.util.logging.*;
import java.util.Random;
import java.io.*;

/** Implements a Vect as a collection of ArrayVect1f's,
    of fixed size.
*/
public class ArrayVect1fs implements Vect {
  static final long serialVersionUID = 1L;

  /** Array of wrapped data */
  protected ArrayVect1f[] _data = null;

  @SuppressWarnings("unused")
private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");

  /** Wrap an array of ArrayVect1f's
      @param data Wrap this array of ArrayVect1f's.
  */
  public ArrayVect1fs(ArrayVect1f[] data)  {init(data);}

  /** To be used with init() */
  public ArrayVect1fs() {}

  /** Initialize class
 * @param data Wrap this collection of 1D arrays */
  private void init(ArrayVect1f[] data)  {_data = data;}

  /** Return the size of the embedded array
 * @return size of embedded array */
  public int getSize() {return _data.length;}

  /** Get the embedded data
      @return Same array as passed to constructor.
   */
  public ArrayVect1f[] getData() {
    return _data;
  }

  @Override
public ArrayVect1fs clone() {
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

  public double dot(VectConst other) {
    double result = 0;
    ArrayVect1fs rhs = (ArrayVect1fs) other;
    for (int i=0; i<_data.length; ++i) {
      result += _data[i].dot(rhs._data[i]);
    }
    return result;
  }

  @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (int i=0; i<_data.length; ++i) {
      sb.append(""+_data[i]);
      if (i < _data.length -1) {sb.append(", ");}
    }
    sb.append(")");
    return sb.toString();
  }

  public void dispose() {
    for (int i=0; i<_data.length; ++i) {
      _data[i].dispose();
    }
    _data = null;
  }

  public void multiplyInverseCovariance() {
    double scale = Almost.FLOAT.divide (1., getSize(), 0.);
    for (int i=0; i<_data.length; ++i) {
      _data[i].multiplyInverseCovariance();
      VectUtil.scale(_data[i], scale);
    }
  }

  public void constrain() {
    for (int i=0; i<_data.length; ++i) {
      _data[i].constrain();
    }
  }

  // Vect
  public void postCondition() {}

  public void add(double scaleThis, double scaleOther, VectConst other)  {
    ArrayVect1fs rhs = (ArrayVect1fs) other;
    for (int i=0; i<_data.length; ++i) {
      _data[i].add(scaleThis, scaleOther, rhs._data[i]);
    }
  }

  public void project(double scaleThis, double scaleOther, VectConst other)  {
    add(scaleThis, scaleOther, other);
  }

  public double magnitude() {
    double result = 0.;
    for (int i=0; i<_data.length; ++i) {
      result += _data[i].magnitude();
    }
    result = Almost.FLOAT.divide (result, getSize() , 0.);
    return result;
  }

  // Serializable
  private void writeObject(java.io.ObjectOutputStream out)
    throws IOException {
    out.writeInt(_data.length);
    for (int i=0; i<_data.length; ++i) {
      out.writeObject(_data[i]);
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

  /** run tests
     @param args command line
     @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Random random = new Random(32525);
    {
      ArrayVect1f[] data = new ArrayVect1f[5];
      for (int j=0; j<data.length; ++j) {
        float[] a = new float[31];
        for (int i=0; i<a.length; ++i) {a[i] = random.nextFloat();}
        data[j] = new ArrayVect1f(a, j+3, ((j+4.)/3.));
      }
      ArrayVect1fs v = new ArrayVect1fs(data);
      VectUtil.test(v);
      v.dispose();
    }

    {
      ArrayVect1f[] data = new ArrayVect1f[5];
      for (int j=0; j<data.length; ++j) {
        float[] a = new float[31];
        for (int i=0; i<a.length; ++i) {a[i] = 1.f;}
        data[j] = new ArrayVect1f(a, 0, 3.);
      }
      ArrayVect1fs v = new ArrayVect1fs(data);
      VectUtil.test(v);
      Vect w = v.clone();
      w.multiplyInverseCovariance();
      assert Almost.FLOAT.equal(1./3., v.dot(w));
      assert Almost.FLOAT.equal(1./3., v.magnitude());
    }
  }
}

