/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;

/**
 * A real-valued sampled function f(x1) of one variable x1.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.01
 */
public class Real1 {

  /**
   * Constructs an empty sampled function, one with no sampled values.
   */
  public Real1() {
    _x1 = new Sampling();
    _f = new float[0];
  }

  /**
   * Constructs a sampled function, with specified sampling and values zero.
   * @param x1 the sampling.
   */
  public Real1(Sampling x1) {
    _x1 = x1;
    _f = new float[_x1.getCount()];
  }

  /**
   * Constructs a sampled function, with specified sampling and values.
   * @param x1 the sampling of the variable x.
   * @param f array of function values f(x); referenced, not copied.
   *  The array length f.length must equal the number of samples in x.
   */
  public Real1(Sampling x1, float[] f) {
    Check.argument(x1.getCount()==f.length,
      "f.length equals the number of samples");
    _x1 = x1;
    _f = Array.copy(f);
  }

  /**
   * Constructs a sampled function, with specified sampling and values zero.
   * @param nx1 the number (count) of samples.
   * @param dx1 the sampling interval (delta).
   * @param fx1 the first sample value.
   */
  public Real1(int nx1, float dx1, float fx1) {
    this(new Sampling(nx1,dx1,fx1));
  }

  /**
   * Constructs a sampled function, with specified sampling and values.
   * @param nx1 the number (count) of samples.
   * @param dx1 the sampling interval (delta).
   * @param fx1 the first sample value.
   * @param f array of function values f(x). 
   *  The array length f.length must equal nx1.
   */
  public Real1(int nx1, float dx1, float fx1, float[] f) {
    this(new Sampling(nx1,dx1,fx1),f);
  }

  /**
   * Gets the sampling of x1 for this function.
   * @return the sampling x1.
   */
  public Sampling getSampling1() {
    return _x1;
  }

  /**
   * Gets the sampling of x1 for this function. 
   * (Same as {@link #getSampling1()}).
   * @return the sampling x1.
   */
  public Sampling getX1() {
    return _x1;
  }

  /**
   * Gets the array of values f(x1) for this function.
   * @return the array of function values; by reference, not by copy.
   */
  public float[] getValues() {
    return _f;
  }

  /**
   * Gets the array of values f(x1) for this function.
   * (Same as {@link #getValues()}).
   * @return the array of function values; by reference, not by copy.
   */
  public float[] getF() {
    return _f;
  }

  /**
   * Gets the function value with specified index.
   * @param i1 the index for sampled variable x1.
   * @return the function value.
   */
  public float getValue(int i1) {
    return _f[i1];
  }

  /**
   * Gets the function value with specified index.
   * (Same as {@link #getValue(int)}).
   * @param i1 the index for sampled variable x1.
   * @return the function value.
   */
  public float getF(int i1) {
    return _f[i1];
  }

  /**
   * Determines whether this sampled function has no values.
   * @return true, if no values; false, otherwise.
   */
  public boolean isEmpty() {
    return _x1.isEmpty();
  }

  ///////////////////////////////////////////////////////////////////////////
  // public 

  /*
  public Real1 plus(Real1 f) {
    add(this,f);
  }
  */

  ///////////////////////////////////////////////////////////////////////////
  // public static

  public static Real1 zero(int n1) {
    return new Real1(n1,1.0f,0.0f);
  }

  /*
  public static Real1 add(Real1 fa, Real1 fb) {
    return null;
  }
  */


  ///////////////////////////////////////////////////////////////////////////
  // private

  Sampling _x1; // sampling of variable x1
  float[] _f; // array of function values f(x1)

  /*
  private static Sampling merge(Sampling sa, Sampling sb) {
    if (isUniform(sa) && isUniform(sb)) {
    }
    return null;
  }
  */
}
