/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
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
package edu.mines.jtk.bench;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Test NIO buffers.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.19
 */
public class BufferBench {

  public static void main(String[] args) {
    testOutOfMemory();
  }

  public static void testOutOfMemory() {
    int capacity = 100000000;
    int nbuf = 100;
    byte data = 31;
    for (int ibuf=0; ibuf<nbuf; ++ibuf) {
      ByteBuffer bb = newByteBuffer(capacity);
      bb.put(capacity/2,data);
      bb.put(capacity-1,data);
      //for (int i=0; i<capacity; ++i)
      //  bb.put(i,data);
    }
  }

  private static ByteBuffer newByteBuffer(int capacity) {
    ByteBuffer bb = null;
    try {
      System.out.println("allocating "+capacity+" bytes");
      bb = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
    } catch (OutOfMemoryError e1) {
      System.gc();
      System.out.println("attempted gc after exception: "+e1);
      System.out.println("now attempting to allocate again");
      try {
        bb = ByteBuffer.allocateDirect(capacity);
      } catch (OutOfMemoryError e2) {
        System.out.println("failed allocate after gc"+e2.getMessage());
      }
    }
    return bb;
  }
}
