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

/** A VectContainer implemented as an unsynchronized Map.
    Keys will be returned in the order of insertion.
    @author W.S. Harlan
 */
public class VectMap implements VectContainer {
  private static final Logger LOG
    = Logger.getLogger("edu.mines.jtk.opt");
  private LinkedHashMap<Integer,Vect> _map = new LinkedHashMap<Integer,Vect>();
  private boolean _cloneContents = false;
  private static final long serialVersionUID = 1L;

  /** Specify whether contents are copied or not.
      @param cloneContents If true, all put and get
      methods will clone the passed Vect.
   */
  public VectMap(boolean cloneContents) {
    if (cloneContents) {
      LOG.warning("Cloning hurts performance.  " +
                  "Use only for testing a VectContainer that requires puts.");
    }
    _cloneContents = cloneContents;
  }

  // VectContainer
  public void put(int index, Vect vect) {
    _map.put(index, (_cloneContents) ? vect.clone() : vect);
  }

  // VectContainer
  public Vect get(int index) {
    Vect result = getPrivate(index);
    if (result != null && _cloneContents) {
      result = result.clone();
    }
    return result;
  }

  private Vect getPrivate(int index) {
    return _map.get(index);
  }

  // VectContainer
  public int size() {return _map.size();}

  // VectContainer
  public boolean containsKey(int index) {
    return _map.containsKey(index);
  }

  // VectContainer
  public int[] getKeys() {
    Set keys = _map.keySet();
    int[] result = new int[keys.size()];
    Iterator iterator = keys.iterator();
    for (int i=0; iterator.hasNext(); ++i) {
      result[i] = ((Integer) iterator.next()).intValue();
    }
    return result;
  }

  // VectConst
  public double dot(VectConst other) {
    VectMap otherMap = (VectMap) other;
    int[] keys = getKeys();
    double result = 0.;
    for (int i=0; i<keys.length; ++i) {
      int key = keys[i];
      Vect lhs = getPrivate(key);
      Vect rhs = otherMap.getPrivate(key);
      result += lhs.dot(rhs);
    }
    return result;
  }

  // VectConst
  @Override public VectMap clone() {
    VectMap result = null;
    try {
      result = (VectMap) super.clone();
      result._map = new LinkedHashMap<Integer,Vect>();
      int[] keys = getKeys();
      for (int i=0; i<keys.length; ++i) {
        int key = keys[i];
        Vect vect = getPrivate(key);
        result.put(key, vect.clone());
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
    int[] keys = getKeys();
    for (int i=0; i<keys.length; ++i) {
      Vect vect = getPrivate(keys[i]);
      vect.dispose();
    }
    _map = null;
  }

  // Vect
  public void multiplyInverseCovariance() {
    int[] keys = getKeys();
    double scale = Almost.FLOAT.divide (1., keys.length, 0.);
    for (int i=0; i<keys.length; ++i) {
      Vect vect = getPrivate(keys[i]);
      vect.multiplyInverseCovariance();
      VectUtil.scale(vect, scale);
    }
  }

  // Vect
  public void constrain() {
    int[] keys = getKeys();
    for (int i=0; i<keys.length; ++i) {
      Vect vect = getPrivate(keys[i]);
      vect.constrain();
    }
  }

  // Vect
  public void postCondition() {
    int[] keys = getKeys();
    for (int i=0; i<keys.length; ++i) {
      Vect vect = getPrivate(keys[i]);
      vect.postCondition();
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

    VectMap otherMap = (VectMap) other;
    int[] keys = getKeys();
    for (int i=0; i<keys.length; ++i) {
      int key = keys[i];
      Vect vectTo = getPrivate(key);
      Vect vectFrom = otherMap.getPrivate(key);
      if (vectFrom == null) {
        throw new IllegalStateException("Cannot scale a vector missing key "+
                                        key);
      }
      if (project)
        vectTo.project(scaleThis, scaleOther, vectFrom);
      else
        vectTo.add(scaleThis, scaleOther, vectFrom);
    }
  }

  // Vect
  public double magnitude() {
    int[] keys = getKeys();
    double result = 0.;
    for (int i=0; i<keys.length; ++i) {
      int key = keys[i];
      Vect vect = getPrivate(key);
      result += vect.magnitude();
    }
    result = Almost.FLOAT.divide (result, keys.length, 0.);
    return result;
  }

  /** Run all tests
     @param args command line arguments
     @throws Exception All tests errors
   */
  public static void main(String[] args) throws Exception {
    Random random = new Random(32525);
    VectMap vm = new VectMap(false);
    for (int index=0; index<5; index += 2) {
      double[] a = new double[7*index];
      for (int i=0; i<a.length; ++i) {a[i] = random.nextDouble();}
      Vect v = new ArrayVect1(a, 2.);
      vm.put(index, v);
    }
    VectUtil.test(vm);
    int[] keys = vm.getKeys();
    assert keys.length == 3 : "keys.length = "+keys.length;
    assert (keys[0] == 0);
    assert (keys[1] == 2);
    assert (keys[2] == 4);
    for (int index=0; index<5; index += 2) {
      ArrayVect1 value = (ArrayVect1) vm.get(index);
      assert (value != null) : "index="+index;
      assert (value.getData() != null) : "index="+index;
      assert (value.getSize() == 7*index) : "index="+index;
      assert (value.getData().length == 7*index) : "index="+index;
      assert (vm.get(index+1) == null) : "index="+index;
    }
    // test inverse covariance
    vm = new VectMap(false);
    for (int index=1; index<5; ++index) {
      double[] a = new double[7*index];
      for (int i=0; i<a.length; ++i) {a[i] = 1;}
      Vect v = new ArrayVect1(a, 1.);
      vm.put(index, v);
      assert vm.containsKey(index);
    }
    assert !vm.containsKey(99);
    Vect wm = vm.clone();
    wm.multiplyInverseCovariance();
    assert Almost.FLOAT.equal(1., wm.dot(vm));
  }

}
