/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
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

import java.util.concurrent.atomic.AtomicLong;

/**
 * A double value that may be updated atomically. An atomic double works like 
 * an atomic integer. (See {@link java.util.concurrent.atomic.AtomicInteger}.)
 * For example, an atomic double might be used for parallel computation of the 
 * dot product of two vectors of doubles.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.09.17
 */
public class AtomicDouble extends Number implements java.io.Serializable {
  private static final long serialVersionUID = 2863252398613925385L;

  /**
   * Constructs an atomic double with initial value zero.
   */
  public AtomicDouble() {
    this(0.0);
  }

  /**
   * Constructs an atomic double with specified initial value.
   * @param value the initial value.
   */
  public AtomicDouble(double value) {
    _al = new AtomicLong(l(value));
  }

  /**
   * Gets the current value of this double.
   * @return the current value.
   */
  public final double get() {
    return d(_al.get());
  }

  /**
   * Sets the value of this double.
   * @param value the new value.
   */
  public final void set(double value) {
    _al.set(l(value));
  }

  /**
   * Atomically sets the value of this double and returns its old value.
   * @param value the new value.
   * @return the old value.
   */
  public final double getAndSet(double value) {
    return d(_al.getAndSet(l(value)));
  }

  /**
   * Atomically sets this double to the specified updated value
   * if the current value equals the specified expected value.
   * @param expect the expected value.
   * @param update the updated value.
   * @return true, if successfully set; false, if the current 
   *  value was not equal to the expected value.
   */
  public final boolean compareAndSet(double expect, double update) {
    return _al.compareAndSet(l(expect),l(update));
  }

  /**
   * Atomically sets this double to the specified updated value
   * if the current value equals the specified expected value.
   * <p>
   * My fail spuriously, and does not provide ordering guarantees, so
   * is only rarely useful.
   * @param expect the expected value.
   * @param update the updated value.
   * @return true, if successfully set; false, if the current 
   *  value was not equal to the expected value.
   */
  public final boolean weakCompareAndSet(double expect, double update) {
    return _al.weakCompareAndSet(l(expect),l(update));
  }

  /**
   * Atomically increments by one the value of this double.
   * @return the previous value of this double.
   */
  public final double getAndIncrement() {
    return getAndAdd(1.0);
  }

  /**
   * Atomically decrements by one the value of this double.
   * @return the previous value of this double.
   */
  public final double getAndDecrement() {
    return getAndAdd(-1.0);
  }

  /**
   * Atomically adds a specified value to the value of this double.
   * @param delta the value to add.
   * @return the previous value of this double.
   */
  public final double getAndAdd(double delta) {
    for (;;) {
      long lexpect = _al.get();
      double expect = d(lexpect);
      double update = expect+delta;
      long lupdate = l(update);
      if (_al.compareAndSet(lexpect,lupdate))
        return expect;
    }
  }

  /**
   * Atomically increments by one the value of this double.
   * @return the updated value of this double.
   */
  public final double incrementAndGet() {
    return addAndGet(1.0);
  }

  /**
   * Atomically decrements by one the value of this double.
   * @return the updated value of this double.
   */
  public final double decrementAndGet() {
    return addAndGet(-1.0);
  }

  /**
   * Atomically adds a specified value to the value of this double.
   * @param delta the value to add.
   * @return the updated value of this double.
   */
  public final double addAndGet(double delta) {
    for (;;) {
      long lexpect = _al.get();
      double expect = d(lexpect);
      double update = expect+delta;
      long lupdate = l(update);
      if (_al.compareAndSet(lexpect,lupdate))
        return update;
    }
  }

  public String toString() {
    return Double.toString(get());
  }

  public int intValue() {
    return (int)get();
  }

  public long longValue() {
    return (long)get();
  }

  public float floatValue() {
    return (float)get();
  }

  public double doubleValue() {
    return get();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private AtomicLong _al;

  private static long l(double d) {
    return Double.doubleToLongBits(d);
  }
  private static double d(long l) {
    return Double.longBitsToDouble(l);
  }
} 
