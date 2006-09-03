/****************************************************************************
Copyright (c) 2004, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;

import java.util.*;
import java.util.logging.*;

/** A VectContainer implemented as an array, with sequential indices.
    Keys will be returned in the index order.
    @author W.S. Harlan
 */
public class VectArray implements VectContainer {
  private static final long serialVersionUID = 1L;
  @SuppressWarnings("unused") private static final Logger LOG
    = Logger.getLogger("edu.mines.jtk.opt");
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
  public void put(int index, Vect vect) {
    _vect[index] = vect;
  }

  // VectContainer
  public Vect get(int index) {
    return _vect[index];
  }

  // VectContainer
  public int size() {return _vect.length;}

  // VectContainer
  public boolean containsKey(int index) {
    if (index >=0 && index <_vect.length) {
      return _vect[index] != null;
    }
        return false;
  }

  // VectContainer
  public int[] getKeys() {
    return _keys;
  }

  // VectConst
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
    VectArray result = null;
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
  public void dispose() {
    _vect = null;
    _keys = null;
  }

  // Vect
  public void multiplyInverseCovariance() {
    double scale = Almost.FLOAT.divide (1., _vect.length, 0.);
    for (int i=0; i<_vect.length; ++i) {
      _vect[i].multiplyInverseCovariance();
      VectUtil.scale(_vect[i], scale);
    }
  }

  // Vect
  public void constrain() {
    for (int i=0; i<_vect.length; ++i) {
      _vect[i].constrain();
    }
  }

  // Vect
  public void postCondition() {
    for (int i=0; i<_vect.length; ++i) {
      _vect[i].postCondition();
    }
  }

  // Vect
  public void add(double scaleThis, double scaleOther, VectConst other)  {
    addOrProject(scaleThis, scaleOther, other, false);
  }

  // Vect
  public void project(double scaleThis, double scaleOther, VectConst other)  {
    addOrProject(scaleThis, scaleOther, other, true);
  }

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
  public double magnitude() {
    double result = 0.;
    for (int i=0; i<_vect.length; ++i) {
      result += _vect[i].magnitude();
    }
    result = Almost.FLOAT.divide (result, _vect.length, 0.);
    return result;
  }

  /** Run all tests
     @param args command line arguments
     @throws Exception test errors
   */
  public static void main(String[] args) throws Exception {
    Random random = new Random(32525);
    VectArray vm = new VectArray(5);
    for (int index=0; index<5; ++index) {
      double[] a = new double[7*index];
      for (int i=0; i<a.length; ++i) {a[i] = random.nextDouble();}
      Vect v = new ArrayVect1(a, 2.);
      vm.put(index, v);
      assert vm.containsKey(index);
    }
    assert !vm.containsKey(99);
    VectUtil.test(vm);
    int[] keys = vm.getKeys();
    assert keys.length == 5 : "keys.length = "+keys.length;
    for (int i=0; i<5; ++i) { assert keys[i] == i;}
    for (int index=0; index<5; ++index) {
      ArrayVect1 value = (ArrayVect1) vm.get(index);
      assert (value != null) : "index="+index;
      assert (value.getData() != null) : "index="+index;
      assert (value.getSize() == 7*index) : "index="+index;
      assert (value.getData().length == 7*index) : "index="+index;
    }
    // test inverse covariance
    vm = new VectArray(5);
    for (int index=0; index<5; ++index) {
      double[] a = new double[7*index+1];
      for (int i=0; i<a.length; ++i) {a[i] = 1;}
      Vect v = new ArrayVect1(a, 1.);
      vm.put(index, v);
    }
    Vect wm = vm.clone();
    wm.multiplyInverseCovariance();
    assert Almost.FLOAT.equal(1., wm.dot(vm)) : wm.dot(vm);
  }
}
