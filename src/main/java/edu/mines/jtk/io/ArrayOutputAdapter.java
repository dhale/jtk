/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
package edu.mines.jtk.io;

import java.io.*;
import static java.lang.Math.min;
import java.nio.*;
import java.nio.channels.WritableByteChannel;

/**
 * Implements {@link ArrayOutput} by wrapping {@link java.io.DataOutput}.
 * This adapter wraps a specified data output to provide methods for writing 
 * values and arrays of values with an optionally specified byte order.
 * <p>
 * Byte order should rarely be specified. Most applications should simply 
 * use the default BIG_ENDIAN byte order.
 * <p>
 * When an adapter is constructed from an object that has a file channel, 
 * the channel enables more efficient writes of arrays of values.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.08.05
 */
public class ArrayOutputAdapter implements ArrayOutput {

  /**
   * Constructs an adapter for the specified data output.
   * The default byte order is BIG_ENDIAN.
   * @param output the data output.
   */
  public ArrayOutputAdapter(DataOutput output) {
    this(output,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an adapter for the specified random-access file.
   * The file channel of the random-access file enables more efficient writes.
   * @param file the random-access file.
   */
  public ArrayOutputAdapter(RandomAccessFile file) {
    this(file,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an adapter for the specified file output stream and byte order.
   * The file channel of the file output stream enables more efficient writes.
   * @param stream the file output stream.
   */
  public ArrayOutputAdapter(FileOutputStream stream) {
    this(stream,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an adapter for the specified data output and byte order.
   * @param output the data output.
   * @param order the byte order.
   */
  public ArrayOutputAdapter(DataOutput output, ByteOrder order) {
    this(null,output,order);
  }

  /**
   * Constructs an adapter for the specified random-access file and byte order.
   * The file channel of the random-access file enables more efficient writes.
   * @param file the random-access file.
   * @param order the byte order.
   */
  public ArrayOutputAdapter(RandomAccessFile file, ByteOrder order) {
    this(file.getChannel(),file,order);
  }

  /**
   * Constructs an adapter for the specified file output stream and byte order.
   * The file channel of the file output stream enables more efficient writes.
   * @param stream the file output stream.
   * @param order the byte order.
   */
  public ArrayOutputAdapter(FileOutputStream stream, ByteOrder order) {
    this(stream.getChannel(),new DataOutputStream(stream),order);
  }

  /**
   * Constructs an adapter for the specified channel, output, and byte order.
   * If not null, the writable byte channel enables more efficient writes.
   * @param channel the writable byte channel; null, if none.
   * @param output the data output.
   * @param order the byte order.
   */
  public ArrayOutputAdapter(
    WritableByteChannel channel, DataOutput output, ByteOrder order) 
  {
    _wbc = channel;
    _do = output;
    _bo = order;
    if (_wbc!=null) {
      _bb = ByteBuffer.allocateDirect(4096);
    } else {
      _buffer = new byte[4096];
      _bb = ByteBuffer.wrap(_buffer);
    }
    if (order==ByteOrder.BIG_ENDIAN) {
      _bb.order(ByteOrder.BIG_ENDIAN);
    } else {
      _bb.order(ByteOrder.LITTLE_ENDIAN);
    }
    _cb = _bb.asCharBuffer();
    _sb = _bb.asShortBuffer();
    _ib = _bb.asIntBuffer();
    _lb = _bb.asLongBuffer();
    _fb = _bb.asFloatBuffer();
    _db = _bb.asDoubleBuffer();
  }

  /**
   * Gets the byte order for this adapter.
   * @return the byte order.
   */
  public ByteOrder getByteOrder() {
    return _bo;
  }

  // From DataOutput.
  public void write(int b) throws IOException {
    _do.write(b);
  }
  public void write(byte[] b) throws IOException {
    _do.write(b);
  }
  public void write(byte[] b, int off, int len) throws IOException {
    _do.write(b,off,len);
  }
  public void writeBoolean(boolean v) throws IOException {
    _do.writeBoolean(v);
  }
  public void writeByte(int v) throws IOException {
    _do.writeByte(v);
  }
  public void writeShort(int v) throws IOException {
    if (_bo==ByteOrder.BIG_ENDIAN) {
      _do.write((v>>>8)&0xFF);
      _do.write((v    )&0xFF);
    } else {
      _do.write((v    )&0xFF);
      _do.write((v>>>8)&0xFF);
    }
  }
  public void writeChar(int v) throws IOException {
    _do.writeShort(v);
  }
  public void writeInt(int v) throws IOException {
    if (_bo==ByteOrder.BIG_ENDIAN) {
      _do.write((v>>>24)&0xFF);
      _do.write((v>>>16)&0xFF);
      _do.write((v>>> 8)&0xFF);
      _do.write((v     )&0xFF);
    } else {
      _do.write((v     )&0xFF);
      _do.write((v>>> 8)&0xFF);
      _do.write((v>>>16)&0xFF);
      _do.write((v>>>24)&0xFF);
    }
  }
  public void writeLong(long v) throws IOException {
    if (_bo==ByteOrder.BIG_ENDIAN) {
      _do.write((int)(v>>>56)&0xFF);
      _do.write((int)(v>>>48)&0xFF);
      _do.write((int)(v>>>40)&0xFF);
      _do.write((int)(v>>>32)&0xFF);
      _do.write((int)(v>>>24)&0xFF);
      _do.write((int)(v>>>16)&0xFF);
      _do.write((int)(v>>> 8)&0xFF);
      _do.write((int)(v     )&0xFF);
    } else {
      _do.write((int)(v     )&0xFF);
      _do.write((int)(v>>> 8)&0xFF);
      _do.write((int)(v>>>16)&0xFF);
      _do.write((int)(v>>>24)&0xFF);
      _do.write((int)(v>>>32)&0xFF);
      _do.write((int)(v>>>40)&0xFF);
      _do.write((int)(v>>>48)&0xFF);
      _do.write((int)(v>>>56)&0xFF);
    }
  }
  public void writeFloat(float v) throws IOException {
    writeInt(Float.floatToIntBits(v));
  }
  public void writeDouble(double v) throws IOException {
    writeLong(Double.doubleToLongBits(v));
  }
  public void writeBytes(String s) throws IOException {
    _do.writeBytes(s);
  }
  public void writeChars(String s) throws IOException {
    int n = s.length();
    for (int i=0; i<n; ++i) {
      writeChar(s.charAt(i));
    }
  }
  public void writeUTF(String s) throws IOException {
    _do.writeUTF(s);
  }

  /**
   * Writes byte elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeBytes(byte[] v, int k, int n) throws IOException {
    write(v,k,n);
  }

  /**
   * Writes byte elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeBytes(byte[] v) throws IOException {
    write(v);
  }

  /**
   * Writes byte elements from a specified array.
   * @param v the array.
   */
  public void writeBytes(byte[][] v) throws IOException {
    for (byte[] vi:v)
      writeBytes(vi);
  }

  /**
   * Writes byte elements from a specified array.
   * @param v the array.
   */
  public void writeBytes(byte[][][] v) throws IOException {
    for (byte[][] vi:v)
      writeBytes(vi);
  }

  /**
   * Writes char elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeChars(char[] v, int k, int n) throws IOException {
    int m = _cb.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      _cb.position(0).limit(l);
      _cb.put(v,k+j,l);
      if (_wbc!=null) {
        _bb.position(0).limit(l*2);
        _wbc.write(_bb);
      } else {
        _do.write(_buffer,0,l*2);
      }
    }
  }

  /**
   * Writes char elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeChars(char[] v) throws IOException {
    writeChars(v,0,v.length);
  }

  /**
   * Writes char elements from a specified array.
   * @param v the array.
   */
  public void writeChars(char[][] v) throws IOException {
    for (char[] vi:v)
      writeChars(vi);
  }

  /**
   * Writes char elements from a specified array.
   * @param v the array.
   */
  public void writeChars(char[][][] v) throws IOException {
    for (char[][] vi:v)
      writeChars(vi);
  }

  /**
   * Writes short elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeShorts(short[] v, int k, int n) throws IOException {
    int m = _sb.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      _sb.position(0).limit(l);
      _sb.put(v,k+j,l);
      if (_wbc!=null) {
        _bb.position(0).limit(l*2);
        _wbc.write(_bb);
      } else {
        _do.write(_buffer,0,l*2);
      }
    }
  }

  /**
   * Writes shorts elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeShorts(short[] v) throws IOException {
    writeShorts(v,0,v.length);
  }

  /**
   * Writes short elements from a specified array.
   * @param v the array.
   */
  public void writeShorts(short[][] v) throws IOException {
    for (short[] vi:v)
      writeShorts(vi);
  }

  /**
   * Writes short elements from a specified array.
   * @param v the array.
   */
  public void writeShorts(short[][][] v) throws IOException {
    for (short[][] vi:v)
      writeShorts(vi);
  }

  /**
   * Writes int elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeInts(int[] v, int k, int n) throws IOException {
    int m = _ib.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      _ib.position(0).limit(l);
      _ib.put(v,k+j,l);
      if (_wbc!=null) {
        _bb.position(0).limit(l*4);
        _wbc.write(_bb);
      } else {
        _do.write(_buffer,0,l*4);
      }
    }
  }

  /**
   * Writes int elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeInts(int[] v) throws IOException {
    writeInts(v,0,v.length);
  }

  /**
   * Writes int elements from a specified array.
   * @param v the array.
   */
  public void writeInts(int[][] v) throws IOException {
    for (int[] vi:v)
      writeInts(vi);
  }

  /**
   * Writes int elements from a specified array.
   * @param v the array.
   */
  public void writeInts(int[][][] v) throws IOException {
    for (int[][] vi:v)
      writeInts(vi);
  }

  /**
   * Writes long elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeLongs(long[] v, int k, int n) throws IOException {
    int m = _lb.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      _lb.position(0).limit(l);
      _lb.put(v,k+j,l);
      if (_wbc!=null) {
        _bb.position(0).limit(l*8);
        _wbc.write(_bb);
      } else {
        _do.write(_buffer,0,l*8);
      }
    }
  }

  /**
   * Writes long elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeLongs(long[] v) throws IOException {
    writeLongs(v,0,v.length);
  }

  /**
   * Writes long elements from a specified array.
   * @param v the array.
   */
  public void writeLongs(long[][] v) throws IOException {
    for (long[] vi:v)
      writeLongs(vi);
  }

  /**
   * Writes long elements from a specified array.
   * @param v the array.
   */
  public void writeLongs(long[][][] v) throws IOException {
    for (long[][] vi:v)
      writeLongs(vi);
  }

  /**
   * Writes float elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeFloats(float[] v, int k, int n) throws IOException {
    int m = _fb.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      _fb.position(0).limit(l);
      _fb.put(v,k+j,l);
      if (_wbc!=null) {
        _bb.position(0).limit(l*4);
        _wbc.write(_bb);
      } else {
        _do.write(_buffer,0,l*4);
      }
    }
  }

  /**
   * Writes float elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeFloats(float[] v) throws IOException {
    writeFloats(v,0,v.length);
  }

  /**
   * Writes float elements from a specified array.
   * @param v the array.
   */
  public void writeFloats(float[][] v) throws IOException {
    for (float[] vi:v)
      writeFloats(vi);
  }

  /**
   * Writes float elements from a specified array.
   * @param v the array.
   */
  public void writeFloats(float[][][] v) throws IOException {
    for (float[][] vi:v)
      writeFloats(vi);
  }

  /**
   * Writes double elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeDoubles(double[] v, int k, int n) throws IOException {
    int m = _db.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      _db.position(0).limit(l);
      _db.put(v,k+j,l);
      if (_wbc!=null) {
        _bb.position(0).limit(l*8);
        _wbc.write(_bb);
      } else {
        _do.write(_buffer,0,l*8);
      }
    }
  }

  /**
   * Writes double elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeDoubles(double[] v) throws IOException {
    writeDoubles(v,0,v.length);
  }

  /**
   * Writes double elements from a specified array.
   * @param v the array.
   */
  public void writeDoubles(double[][] v) throws IOException {
    for (double[] vi:v)
      writeDoubles(vi);
  }

  /**
   * Writes double elements from a specified array.
   * @param v the array.
   */
  public void writeDoubles(double[][][] v) throws IOException {
    for (double[][] vi:v)
      writeDoubles(vi);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private byte[] _buffer;
  private WritableByteChannel _wbc;
  private DataOutput _do;
  private ByteOrder _bo;
  private ByteBuffer _bb;
  private CharBuffer _cb;
  private ShortBuffer _sb;
  private IntBuffer _ib;
  private LongBuffer _lb;
  private FloatBuffer _fb;
  private DoubleBuffer _db;
}
