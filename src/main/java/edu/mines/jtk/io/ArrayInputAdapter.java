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
import java.nio.channels.ReadableByteChannel;

/**
 * Implements {@link ArrayInput} by wrapping {@link java.io.DataInput}.
 * This adapter wraps a specified data input to provide methods for reading 
 * values and arrays of values with an optionally specified byte order.
 * <p>
 * Byte order should rarely be specified. Most applications should simply 
 * use the default BIG_ENDIAN byte order.
 * <p>
 * When an adapter is constructed from an object that has a file channel, 
 * the channel enables more efficient reads of arrays of values.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.08.05
 */
public class ArrayInputAdapter implements ArrayInput {

  /**
   * Constructs an adapter for the specified data input.
   * The default byte order is BIG_ENDIAN.
   * @param input the data input.
   */
  public ArrayInputAdapter(DataInput input) {
    this(input,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an adapter for the specified random-access file.
   * The file channel of the random-access file enables more efficient reads.
   * @param file the random-access file.
   */
  public ArrayInputAdapter(RandomAccessFile file) {
    this(file,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an adapter for the specified file input stream and byte order.
   * The file channel of the file input stream enables more efficient reads.
   * @param stream the file input stream.
   */
  public ArrayInputAdapter(FileInputStream stream) {
    this(stream,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an adapter for the specified data input and byte order.
   * @param input the data input.
   * @param order the byte order.
   */
  public ArrayInputAdapter(DataInput input, ByteOrder order) {
    this(null,input,order);
  }

  /**
   * Constructs an adapter for the specified random-access file and byte order.
   * The file channel of the random-access file enables more efficient reads.
   * @param file the random-access file.
   * @param order the byte order.
   */
  public ArrayInputAdapter(RandomAccessFile file, ByteOrder order) {
    this(file.getChannel(),file,order);
  }

  /**
   * Constructs an adapter for the specified file input stream and byte order.
   * The file channel of the file input stream enables more efficient reads.
   * @param stream the file input stream.
   * @param order the byte order.
   */
  public ArrayInputAdapter(FileInputStream stream, ByteOrder order) {
    this(stream.getChannel(),new DataInputStream(stream),order);
  }

  /**
   * Constructs an adapter for the specified channel, input, and byte order.
   * If not null, the readable byte channel enables more efficient reads.
   * @param channel the readable byte channel; null, if none.
   * @param input the data input.
   * @param order the byte order.
   */
  public ArrayInputAdapter(
    ReadableByteChannel channel, DataInput input, ByteOrder order) 
  {
    _rbc = channel;
    _di = input;
    _bo = order;
    if (_rbc!=null) {
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

  public void readFully(byte[] b) throws IOException {
    _di.readFully(b);
  }
  public void readFully(byte[] b, int off, int len) throws IOException {
    _di.readFully(b,off,len);
  }
  public int skipBytes(int n) throws IOException {
    return _di.skipBytes(n);
  }
  public final boolean readBoolean() throws IOException {
    return _di.readBoolean();
  }
  public final byte readByte() throws IOException {
    return _di.readByte();
  }
  public final int readUnsignedByte() throws IOException {
    return _di.readUnsignedByte();
  }
  public final short readShort() throws IOException {
    int b1 = _di.readUnsignedByte();
    int b2 = _di.readUnsignedByte();
    if (_bo==ByteOrder.BIG_ENDIAN) {
      return (short)((b1<<8)+b2);
    } else {
      return (short)((b2<<8)+b1);
    }
  }
  public final int readUnsignedShort() throws IOException {
    return ((int)readShort())&0xffff;
  }
  public final char readChar() throws IOException {
    return (char)readShort();
  }
  public final int readInt() throws IOException {
    int b1 = _di.readUnsignedByte();
    int b2 = _di.readUnsignedByte();
    int b3 = _di.readUnsignedByte();
    int b4 = _di.readUnsignedByte();
    if (_bo==ByteOrder.BIG_ENDIAN) {
      return ((b1<<24)+(b2<<16)+(b3<<8)+b4);
    } else {
      return ((b4<<24)+(b3<<16)+(b2<<8)+b1);
    }
  }
  public final long readLong() throws IOException {
    int i1 = readInt();
    int i2 = readInt();
    if (_bo==ByteOrder.BIG_ENDIAN) {
      return ((long)i1<<32)+(i2&0xFFFFFFFFL);
    } else {
      return ((long)i2<<32)+(i1&0xFFFFFFFFL);
    }
  }
  public final float readFloat() throws IOException {
    return Float.intBitsToFloat(readInt());
  }
  public final double readDouble() throws IOException {
    return Double.longBitsToDouble(readLong());
  }
  public final String readLine() throws IOException {
    return _di.readLine();
  }
  public final String readUTF() throws IOException {
    return _di.readUTF();
  }

  /**
   * Reads byte elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readBytes(byte[] v, int k, int n) throws IOException {
    readFully(v,k,n);
  }

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[] v) throws IOException {
    readFully(v);
  }

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[][] v) throws IOException {
    for (byte[] vi:v)
      readBytes(vi);
  }

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[][][] v) throws IOException {
    for (byte[][] vi:v)
      readBytes(vi);
  }

  /**
   * Reads char elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readChars(char[] v, int k, int n) throws IOException {
    int m = _cb.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      if (_rbc!=null) {
        _bb.position(0).limit(l*2);
        _rbc.read(_bb);
      } else {
        _di.readFully(_buffer,0,l*2);
      }
      _cb.position(0).limit(l);
      _cb.get(v,k+j,l);
    }
  }

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[] v) throws IOException {
    readChars(v,0,v.length);
  }

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[][] v) throws IOException {
    for (char[] vi:v)
      readChars(vi);
  }

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[][][] v) throws IOException {
    for (char[][] vi:v)
      readChars(vi);
  }

  /**
   * Reads short elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readShorts(short[] v, int k, int n) throws IOException {
    int m = _sb.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      if (_rbc!=null) {
        _bb.position(0).limit(l*2);
        _rbc.read(_bb);
      } else {
        _di.readFully(_buffer,0,l*2);
      }
      _sb.position(0).limit(l);
      _sb.get(v,k+j,l);
    }
  }

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[] v) throws IOException {
    readShorts(v,0,v.length);
  }

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[][] v) throws IOException {
    for (short[] vi:v)
      readShorts(vi);
  }

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[][][] v) throws IOException {
    for (short[][] vi:v)
      readShorts(vi);
  }

  /**
   * Reads int elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readInts(int[] v, int k, int n) throws IOException {
    int m = _ib.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      if (_rbc!=null) {
        _bb.position(0).limit(l*4);
        _rbc.read(_bb);
      } else {
        _di.readFully(_buffer,0,l*4);
      }
      _ib.position(0).limit(l);
      _ib.get(v,k+j,l);
    }
  }

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[] v) throws IOException {
    readInts(v,0,v.length);
  }

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[][] v) throws IOException {
    for (int[] vi:v)
      readInts(vi);
  }

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[][][] v) throws IOException {
    for (int[][] vi:v)
      readInts(vi);
  }

  /**
   * Reads long elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readLongs(long[] v, int k, int n) throws IOException {
    int m = _lb.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      if (_rbc!=null) {
        _bb.position(0).limit(l*8);
        _rbc.read(_bb);
      } else {
        _di.readFully(_buffer,0,l*8);
      }
      _lb.position(0).limit(l);
      _lb.get(v,k+j,l);
    }
  }

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[] v) throws IOException {
    readLongs(v,0,v.length);
  }

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[][] v) throws IOException {
    for (long[] vi:v)
      readLongs(vi);
  }

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[][][] v) throws IOException {
    for (long[][] vi:v)
      readLongs(vi);
  }

  /**
   * Reads float elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readFloats(float[] v, int k, int n) throws IOException {
    int m = _fb.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      if (_rbc!=null) {
        _bb.position(0).limit(l*4);
        _rbc.read(_bb);
      } else {
        _di.readFully(_buffer,0,l*4);
      }
      _fb.position(0).limit(l);
      _fb.get(v,k+j,l);
    }
  }

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[] v) throws IOException {
    readFloats(v,0,v.length);
  }

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[][] v) throws IOException {
    for (float[] vi:v)
      readFloats(vi);
  }

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[][][] v) throws IOException {
    for (float[][] vi:v)
      readFloats(vi);
  }

  /**
   * Reads double elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readDoubles(double[] v, int k, int n) throws IOException {
    int m = _db.capacity();
    for (int j=0; j<n; j+=m) {
      int l = min(n-j,m);
      if (_rbc!=null) {
        _bb.position(0).limit(l*8);
        _rbc.read(_bb);
      } else {
        _di.readFully(_buffer,0,l*8);
      }
      _db.position(0).limit(l);
      _db.get(v,k+j,l);
    }
  }

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[] v) throws IOException {
    readDoubles(v,0,v.length);
  }

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[][] v) throws IOException {
    for (double[] vi:v)
      readDoubles(vi);
  }

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[][][] v) throws IOException {
    for (double[][] vi:v)
      readDoubles(vi);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private
  private byte[] _buffer;
  private ReadableByteChannel _rbc;
  private DataInput _di;
  private ByteOrder _bo;
  private ByteBuffer _bb;
  private CharBuffer _cb;
  private ShortBuffer _sb;
  private IntBuffer _ib;
  private LongBuffer _lb;
  private FloatBuffer _fb;
  private DoubleBuffer _db;
}
