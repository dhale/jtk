/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
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
  public static ByteBuffer byteBuffer(int capacity) {
    ByteBuffer b = null;
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
  public static ByteBuffer byteBuffer(byte[] a) {
    ByteBuffer b = byteBuffer(a.length);
    b.put(a);
    b.flip();
    return b;
  }

  /**
   * Returns a new direct float buffer.
   * @param capacity the buffer capacity.
   * @return the new buffer.
   */
  public static FloatBuffer floatBuffer(int capacity) {
    return byteBuffer(4*capacity).asFloatBuffer();
  }

  /**
   * Returns a new direct float buffer. Allocates the buffer with capacity 
   * equal to the length of the specified array, and copies all array 
   * elements to the buffer.
   * @param a the array.
   * @return the new buffer.
   */
  public static FloatBuffer floatBuffer(float[] a) {
    FloatBuffer b = floatBuffer(a.length);
    b.put(a);
    b.flip();
    return b;
  }
}
