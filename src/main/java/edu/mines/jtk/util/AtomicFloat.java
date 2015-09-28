/****************************************************************************
Copyright 2007, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A float value that may be updated atomically. An atomic float works like 
 * an atomic integer. (See {@link java.util.concurrent.atomic.AtomicInteger}.)
 * For example, an atomic float might be used for parallel computation of the 
 * dot product of two vectors of floats.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.07.27
 */
public class AtomicFloat extends Number implements java.io.Serializable {
  private static final long serialVersionUID = 6792837592345465936L;

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
    _ai = new AtomicInteger(i(value));
  }

  /**
   * Gets the current value of this float.
   * @return the current value.
   */
  public final float get() {
    return f(_ai.get());
  }

  /**
   * Sets the value of this float.
   * @param value the new value.
   */
  public final void set(float value) {
    _ai.set(i(value));
  }

  /**
   * Atomically sets the value of this float and returns its old value.
   * @param value the new value.
   * @return the old value.
   */
  public final float getAndSet(float value) {
    return f(_ai.getAndSet(i(value)));
  }

  /**
   * Atomically sets this float to the specified updated value
   * if the current value equals the specified expected value.
   * @param expect the expected value.
   * @param update the updated value.
   * @return true, if successfully set; false, if the current 
   *  value was not equal to the expected value.
   */
  public final boolean compareAndSet(float expect, float update) {
    return _ai.compareAndSet(i(expect),i(update));
  }

  /**
   * Atomically sets this float to the specified updated value
   * if the current value equals the specified expected value.
   * <p>
   * My fail spuriously, and does not provide ordering guarantees, so
   * is only rarely useful.
   * @param expect the expected value.
   * @param update the updated value.
   * @return true, if successfully set; false, if the current 
   *  value was not equal to the expected value.
   */
  public final boolean weakCompareAndSet(float expect, float update) {
    return _ai.weakCompareAndSet(i(expect),i(update));
  }

  /**
   * Atomically increments by one the value of this float.
   * @return the previous value of this float.
   */
  public final float getAndIncrement() {
    return getAndAdd(1.0f);
  }

  /**
   * Atomically decrements by one the value of this float.
   * @return the previous value of this float.
   */
  public final float getAndDecrement() {
    return getAndAdd(-1.0f);
  }

  /**
   * Atomically adds a specified value to the value of this float.
   * @param delta the value to add.
   * @return the previous value of this float.
   */
  public final float getAndAdd(float delta) {
    for (;;) {
      int iexpect = _ai.get();
      float expect = f(iexpect);
      float update = expect+delta;
      int iupdate = i(update);
      if (_ai.compareAndSet(iexpect,iupdate))
        return expect;
    }
  }

  /**
   * Atomically increments by one the value of this float.
   * @return the updated value of this float.
   */
  public final float incrementAndGet() {
    return addAndGet(1.0f);
  }

  /**
   * Atomically decrements by one the value of this float.
   * @return the updated value of this float.
   */
  public final float decrementAndGet() {
    return addAndGet(-1.0f);
  }

  /**
   * Atomically adds a specified value to the value of this float.
   * @param delta the value to add.
   * @return the updated value of this float.
   */
  public final float addAndGet(float delta) {
    for (;;) {
      int iexpect = _ai.get();
      float expect = f(iexpect);
      float update = expect+delta;
      int iupdate = i(update);
      if (_ai.compareAndSet(iexpect,iupdate))
        return update;
    }
  }

  public String toString() {
    return Float.toString(get());
  }

  public int intValue() {
    return (int)get();
  }

  public long longValue() {
    return (long)get();
  }

  public float floatValue() {
    return get();
  }

  public double doubleValue() {
    return (double)get();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private AtomicInteger _ai;

  private static int i(float f) {
    return Float.floatToIntBits(f);
  }
  private static float f(int i) {
    return Float.intBitsToFloat(i);
  }
} 
