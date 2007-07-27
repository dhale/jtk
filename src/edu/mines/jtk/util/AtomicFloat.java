/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A float value that may be updated atomically.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.07.27
 */
public class AtomicFloat {

  /**
   * Constructs an atomic float with initial value zero.
   */
  public AtomicFloat() {
    this(0.0f);
  }

  /**
   * Constructs an atomic float with specified initial value.
   * @param value the initial value.
   */
  public AtomicFloat(float value) {
    _ai = new AtomicInteger(getInt(value));
  }

  public float addAndGet(float delta) {
    return 0.0f;
  }

  public boolean compareAndSet(float expect, float update) {
    int iexpect = getInt(expect);
    int iupdate = getInt(update);
    return _ai.compareAndSet(iexpect,iupdate);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private AtomicInteger _ai;

  private static int getInt(float f) {
    return Float.floatToIntBits(f);
  }
  private static float getFloat(int i) {
    return Float.intBitsToFloat(i);
  }
} 
