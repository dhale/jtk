/****************************************************************************
Copyright 2010, Colorado School of Mines and others.
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

import java.util.NoSuchElementException;

/**
 * A first-in-first-out (FIFO) queue implemented with an array.
 * <p>
 * This array-based implementation is optimized for performance. Entries 
 * can be added/removed to/from the queue in amortized constant time.
 * The cost of adding/removing N entries to/from the queue is O(N), and 
 * the constant factor is less than that for an implementation based on
 * {@link java.util.LinkedList}.
 * <p>
 * For queues that contain at least a small number of entries, the length 
 * of the array used to implement the queue is less than twice the maximum 
 * number of entries in the queue. Furthermore, this length is always less
 * than three times the number of entries in the queue.
 * Therefore, this implementation requires less memory than one based on
 * {@link java.util.LinkedList}.
 * @author Dave Hale, Colorado School of Mines
 * @version 07/16/2008
 */
public class ArrayQueue<E> {

  /**
   * Constructs a queue with default capacity.
   */
  public ArrayQueue() {
    _length = MIN_LENGTH;
    _array = new Object[_length];
  }

  /**
   * Constructs a queue with the specified initial capacity.
   * The capacity is the number of entries that the queue can hold without 
   * increasing the length of the array used to implement the queue.
   * <p>
   * This constructor may be used to reduce the cost of adding a large 
   * number of entries to the queue, when that number of entries is known 
   * in advance.
   * @param capacity the initial capacity.
   */
  public ArrayQueue(int capacity) {
    if (capacity<MIN_LENGTH)
      capacity = MIN_LENGTH;
    _length = capacity;
    _array = new Object[_length];
  }

  /**
   * Adds the specified entry to the back of the queue.
   * Before addition, if the size of the queue equals its capacity, 
   * then the capacity of the queue is doubled.
   * @param e the entry.
   */
  public void add(E e) {

    // If no space is left in the array, double its size.
    if (_size==_length)
      resize(_size*2);

    // Index of entry to be added at the back of the queue.
    // This index is computed modulo the length of the array.
    int index = _first+_size;
    if (index>=_length)
      index -= _length;

    // Add the entry at the back of the queue.
    _array[index] = e;
    ++_size;
  }

  /**
   * Returns (but does not remove) the entry from the front of the queue.
   * @return the entry.
   */
  public E first() {
    if (_size==0)
      throw new NoSuchElementException();
    @SuppressWarnings("unchecked")
    E e = (E)_array[_first];
    return e;
  }

  /**
   * Removes and returns the entry from the front of the queue.
   * After removal, if the size of the queue is less than one third 
   * its capacity, then the capacity is set to be twice that size. 
   * (Note that the capacity shrinks more slowly than it grows.)
   * @return the entry.
   */
  public E remove() {

    // Ensure the queue contains at least one entry.
    if (_size==0)
      throw new NoSuchElementException();

    // Remove the first entry from the queue. If the entry removed
    // is at the end of the array, the next entry is at the beginning.
    @SuppressWarnings("unchecked")
    E e = (E)_array[_first];
    ++_first;
    if (_first==_length)
      _first = 0;
    --_size;

    // If two-thirds of the space in the array is unused, shrink it.
    // However, assuming that entries will again be added to the queue,
    // keep half the unused space. By shrinking the array more slowly
    // than we grow it, we reduce the likelihood of spurious grow-shrink
    // cycles.
    if (_size*3<_length)
      resize(_size*2);

    // Return the entry removed.
    return e;
  }

  /**
   * Determines whether the queue is empty.
   * @return true, if empty; false, otherwise.
   */
  public boolean isEmpty() {
    return _size==0;
  }

  /**
   * Ensures that the capacity of the queue is not less than the
   * specified value.
   * The capacity is the number of entries that the queue can hold without 
   * increasing the length of the array used to implement the queue.
   * <p>
   * This method may be used to reduce the cost of adding a large number 
   * of entries to the queue, when that number of entries is known in 
   * advance.
   * @param capacity the capacity.
   */
  public void ensureCapacity(int capacity) {
    if (capacity<_length)
      capacity = _length;
    resize(capacity);
  }

  /**
   * Returns the number of entries in the queue.
   * @return the number of entries.
   */
  public int size() {
    return _size;
  }

  /**
   * Sets the capacity of the queue equal to its current size.
   * This eliminates any wasted space in the array used to implement 
   * the queue.
   */
  public void trimToSize() {
    resize(_size);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  /**
   * Minimum array length. This number needs to be large enough to avoid
   * resizing small queues repeatedly. However, it should be small enough
   * that wasted space is typically insignificant.
   */
  private static final int MIN_LENGTH = 32;

  private int _length = 0; // array length (cached for efficiency)
  private Object[] _array; // array containing the queue
  private int _first = 0; // index of first entry in queue
  private int _size = 0; // number of entries in queue

  /**
   * Resizes the queue's array to have the specified length.
   * Copies entries from the old array to the new one, so that the 
   * first entry in the new array is the first entry in the queue.
   */
  private void resize(int length) {

    // Length must never be less than the minimum.
    if (length<MIN_LENGTH)
      length = MIN_LENGTH;

    // If the specified length is different from the current length.
    if (length!=_length) {

      // New array.
      Object[] array = new Object[length];

      // Copy the first part, if any.
      int n1 = _length-_first;
      if (n1>_size)
        n1 = _size;
      if (n1>0)
        System.arraycopy(_array,_first,array,0,n1);
      
      // Copy the second part, if any.
      int n2 = _first+_size-_length;
      if (n2>0)
        System.arraycopy(_array,0,array,n1,n2);

      // Remember the new array.
      _length = length;
      _array = array;
      _first = 0;
    }
  }
}
