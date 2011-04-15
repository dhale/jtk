/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;

import java.util.logging.Logger;

/** A VectContainer implemented as an array, with sequential indices.
    Keys will be returned in the index order.
    @author W.S. Harlan
 */
public class VectArray implements VectContainer {
  private static final long serialVersionUID = 1L;
  private static final Logger LOG = Logger.getLogger("edu.mines.jtk.opt");
  private Vect[] _vect = null;
  private int[] _keys = null;

  /** Specify the number of sequential indices.
      @param size Indices can range from 0 to size-1.
   */
  public VectArray(int size) {
    _vect = new Vect[size];
    _keys = new int[size];
    for (int i=0; i<size; ++i) {_keys[i] = i;}
  }

  // VectContainer
  @Override
  public void put(int index, Vect vect) {
    _vect[index] = vect;
  }

  // VectContainer
  @Override
  public Vect get(int index) {
    return _vect[index];
  }

  // VectContainer
  @Override
  public int size() {return _vect.length;}

  // VectContainer
  @Override
  public boolean containsKey(int index) {
    return index >= 0 && index < _vect.length && _vect[index] != null;
  }

  // VectContainer
  @Override
  public int[] getKeys() {
    return _keys;
  }

  // VectConst
  @Override
  public double dot(VectConst other) {
    VectArray otherMap = (VectArray) other;
    double result = 0.;
    for (int i=0; i<_vect.length; ++i) {
      result += _vect[i].dot(otherMap._vect[i]);
    }
    return result;
  }

  // VectConst
  @Override public VectArray clone() {
    VectArray result;
    try {
      result = (VectArray) super.clone();
      result._vect = new Vect[_vect.length];
      for (int i=0; i<_vect.length; ++i) {
        if (_vect[i] != null) {
          result._vect[i] = _vect[i].clone();
        }
      }
    } catch (CloneNotSupportedException ex) {
      IllegalStateException e = new IllegalStateException(ex.getMessage());
      e.initCause(ex);
      throw e;
    }
    return result;
  }

  // Vect
  @Override
  public void dispose() {
    _vect = null;
    _keys = null;
  }

  // Vect
  @Override
  public void multiplyInverseCovariance() {
    double scale = Almost.FLOAT.divide (1., _vect.length, 0.);
      for (Vect a_vect : _vect) {
          a_vect.multiplyInverseCovariance();
          VectUtil.scale(a_vect, scale);
      }
  }

  // Vect
  @Override
  public void constrain() {
      for (Vect a_vect : _vect) {
          a_vect.constrain();
      }
  }

  // Vect
  @Override
  public void postCondition() {
      for (Vect a_vect : _vect) {
          a_vect.postCondition();
      }
  }

  // Vect
  @Override
  public void add(double scaleThis, double scaleOther, VectConst other)  {
    addOrProject(scaleThis, scaleOther, other, false);
  }

  // Vect
  @Override
  public void project(double scaleThis, double scaleOther, VectConst other)  {
    addOrProject(scaleThis, scaleOther, other, true);
  }

  // implementation of both project and add.
  private void addOrProject
    (double scaleThis, double scaleOther, VectConst other, boolean project)  {

    VectArray otherMap = (VectArray) other;
    for (int i=0; i<_vect.length; ++i) {
      Vect vectTo = _vect[i];
      Vect vectFrom = otherMap._vect[i];
      if (vectFrom == null) {
        throw new IllegalStateException("Cannot scale missing Vect "+i);
      }
      if (project)
        vectTo.project(scaleThis, scaleOther, vectFrom);
      else
        vectTo.add(scaleThis, scaleOther, vectFrom);
    }
  }

  // Vect
  @Override
  public double magnitude() {
    double result = 0.;
      for (Vect a_vect : _vect) {
          result += a_vect.magnitude();
      }
    result = Almost.FLOAT.divide (result, _vect.length, 0.);
    return result;
  }
}
