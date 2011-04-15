/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

import edu.mines.jtk.util.Almost;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Logger;

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
  @Override
  public void put(int index, Vect vect) {
    _map.put(index, (_cloneContents) ? vect.clone() : vect);
  }

  // VectContainer
  @Override
  public Vect get(int index) {
    Vect result = getPrivate(index);
    if (result != null && _cloneContents) {
      result = result.clone();
    }
    return result;
  }

  // Get private instance
  private Vect getPrivate(int index) {
    return _map.get(index);
  }

  // VectContainer
  @Override
  public int size() {return _map.size();}

  // VectContainer
  @Override
  public boolean containsKey(int index) {
    return _map.containsKey(index);
  }

  // VectContainer
  @Override
  public int[] getKeys() {
    Set<Integer> keys = _map.keySet();
    int[] result = new int[keys.size()];
    int i=0;
    for (Integer j: keys) {
      result[i++] = j;
    }
    return result;
  }

  // VectConst
  @Override
  public double dot(VectConst other) {
    VectMap otherMap = (VectMap) other;
    int[] keys = getKeys();
    double result = 0.;
      for (int key : keys) {
          Vect lhs = getPrivate(key);
          Vect rhs = otherMap.getPrivate(key);
          result += lhs.dot(rhs);
      }
    return result;
  }

  // VectConst
  @Override public VectMap clone() {
    VectMap result;
    try {
      result = (VectMap) super.clone();
      result._map = new LinkedHashMap<Integer,Vect>();
      int[] keys = getKeys();
        for (int key : keys) {
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
  @Override
  public void dispose() {
    int[] keys = getKeys();
      for (int key : keys) {
          Vect vect = getPrivate(key);
          vect.dispose();
      }
    _map = null;
  }

  // Vect
  @Override
  public void multiplyInverseCovariance() {
    int[] keys = getKeys();
    double scale = Almost.FLOAT.divide (1., keys.length, 0.);
      for (int key : keys) {
          Vect vect = getPrivate(key);
          vect.multiplyInverseCovariance();
          VectUtil.scale(vect, scale);
      }
  }

  // Vect
  @Override
  public void constrain() {
    int[] keys = getKeys();
      for (int key : keys) {
          Vect vect = getPrivate(key);
          vect.constrain();
      }
  }

  // Vect
  @Override
  public void postCondition() {
    int[] keys = getKeys();
      for (int key : keys) {
          Vect vect = getPrivate(key);
          vect.postCondition();
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

  // implementation of both add and project
  private void addOrProject
    (double scaleThis, double scaleOther, VectConst other, boolean project)  {

    VectMap otherMap = (VectMap) other;
    int[] keys = getKeys();
      for (int key : keys) {
          Vect vectTo = getPrivate(key);
          Vect vectFrom = otherMap.getPrivate(key);
          if (vectFrom == null) {
              throw new IllegalStateException("Cannot scale a vector missing key " +
                      key);
          }
          if (project) {
              vectTo.project(scaleThis, scaleOther, vectFrom);
          } else {
              vectTo.add(scaleThis, scaleOther, vectFrom);
          }
      }
  }

  // Vect
  @Override
  public double magnitude() {
    int[] keys = getKeys();
    double result = 0.;
      for (int key : keys) {
          Vect vect = getPrivate(key);
          result += vect.magnitude();
      }
    result = Almost.FLOAT.divide (result, keys.length, 0.);
    return result;
  }
}
