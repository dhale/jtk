/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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

import java.nio.*;

/**
 * Utilities for direct (native) buffers. Unlike arrays and non-direct
 * buffers that are backed by arrays, the contents of direct buffers
 * cannot move during garbage-collection. This makes direct buffers
 * useful in systems (e.g., OpenGL) that require native pointers to 
 * memory.
 * <p>
 * Memory allocated for a direct buffer typically lies outside the 
 * garbage-collected Java heap. That memory is freed when the direct 
 * buffer is garbage collected. Unfortunately, garbage collection 
 * normally occurs when insufficient space is available <em>inside</em> 
 * the Java heap. If insufficient space <em>outside</em> the Java heap 
 * is available, then allocation of a new direct buffer may fail with 
 * a {@link java.lang.OutOfMemoryError}. This error may occur because
 * direct buffers that are garbage have not yet been collected, perhaps 
 * because plenty of space is available inside the Java heap.
 * <p>
 * A solution to this problem is to perform garbage collection when 
 * this error occurs. Normally, a {@link java.lang.OutOfMemoryError} 
 * is fatal. However, methods in this class that allocate direct buffers 
 * will catch this error and call {@link java.lang.System#gc()} before 
 * attempting to allocate the buffer a second time. If that second 
 * attempt fails, then no further attempts are made and the error is 
 * thrown again. There is no guarantee that this solution will work, but 
 * we have not yet seen it fail in tests that repeatedly allocate direct
 * buffers that quickly become garbage.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.02
 */
public class Direct {

  /**
   * Returns a new direct byte buffer.
   * @param capacity the buffer capacity.
   * @return the new buffer.
   */
  public static ByteBuffer newByteBuffer(int capacity) {
    ByteBuffer b;
    try {
      b = ByteBuffer.allocateDirect(capacity);
    } catch (OutOfMemoryError e1) {
      System.gc();
      try {
        b = ByteBuffer.allocateDirect(capacity);
      } catch (OutOfMemoryError e2) {
        throw new OutOfMemoryError("cannot allocate direct buffer");
      }
    }
    b.order(ByteOrder.nativeOrder());
    return b;
  }

  /**
   * Returns a new direct byte buffer. Allocates the buffer with capacity 
   * equal to the length of the specified array, and copies all array 
   * elements to the buffer.
   * @param a the array.
   * @return the new buffer.
   */
  public static ByteBuffer newByteBuffer(byte[] a) {
    ByteBuffer b = newByteBuffer(a.length);
    b.put(a);
    b.flip();
    return b;
  }

  /**
   * Returns a new direct double buffer.
   * @param capacity the buffer capacity.
   * @return the new buffer.
   */
  public static DoubleBuffer newDoubleBuffer(int capacity) {
    return newByteBuffer(8*capacity).asDoubleBuffer();
  }

  /**
   * Returns a new direct double buffer. Allocates the buffer with capacity 
   * equal to the length of the specified array, and copies all array 
   * elements to the buffer.
   * @param a the array.
   * @return the new buffer.
   */
  public static DoubleBuffer newDoubleBuffer(double[] a) {
    DoubleBuffer b = newDoubleBuffer(a.length);
    b.put(a);
    b.flip();
    return b;
  }

  /**
   * Returns a new direct float buffer.
   * @param capacity the buffer capacity.
   * @return the new buffer.
   */
  public static FloatBuffer newFloatBuffer(int capacity) {
    return newByteBuffer(4*capacity).asFloatBuffer();
  }

  /**
   * Returns a new direct float buffer. Allocates the buffer with capacity 
   * equal to the length of the specified array, and copies all array 
   * elements to the buffer.
   * @param a the array.
   * @return the new buffer.
   */
  public static FloatBuffer newFloatBuffer(float[] a) {
    FloatBuffer b = newFloatBuffer(a.length);
    b.put(a);
    b.flip();
    return b;
  }

  /**
   * Returns a new direct int buffer.
   * @param capacity the buffer capacity.
   * @return the new buffer.
   */
  public static IntBuffer newIntBuffer(int capacity) {
    return newByteBuffer(4*capacity).asIntBuffer();
  }

  /**
   * Returns a new direct int buffer. Allocates the buffer with capacity 
   * equal to the length of the specified array, and copies all array 
   * elements to the buffer.
   * @param a the array.
   * @return the new buffer.
   */
  public static IntBuffer newIntBuffer(int[] a) {
    IntBuffer b = newIntBuffer(a.length);
    b.put(a);
    b.flip();
    return b;
  }

  /**
   * Returns a new direct long buffer.
   * @param capacity the buffer capacity.
   * @return the new buffer.
   */
  public static LongBuffer newLongBuffer(int capacity) {
    return newByteBuffer(8*capacity).asLongBuffer();
  }

  /**
   * Returns a new direct long buffer. Allocates the buffer with capacity 
   * equal to the length of the specified array, and copies all array 
   * elements to the buffer.
   * @param a the array.
   * @return the new buffer.
   */
  public static LongBuffer newLongBuffer(long[] a) {
    LongBuffer b = newLongBuffer(a.length);
    b.put(a);
    b.flip();
    return b;
  }

  /**
   * Returns a new direct short buffer.
   * @param capacity the buffer capacity.
   * @return the new buffer.
   */
  public static ShortBuffer newShortBuffer(int capacity) {
    return newByteBuffer(2*capacity).asShortBuffer();
  }

  /**
   * Returns a new direct short buffer. Allocates the buffer with capacity 
   * equal to the length of the specified array, and copies all array 
   * elements to the buffer.
   * @param a the array.
   * @return the new buffer.
   */
  public static ShortBuffer newShortBuffer(short[] a) {
    ShortBuffer b = newShortBuffer(a.length);
    b.put(a);
    b.flip();
    return b;
  }
}
