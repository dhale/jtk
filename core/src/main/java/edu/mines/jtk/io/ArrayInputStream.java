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
import java.nio.ByteOrder;

/**
 * An input stream that implements {@link ArrayInput}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.08.05
 */
public class ArrayInputStream 
  extends FilterInputStream 
  implements ArrayInput 
{

  /**
   * Constructs an array input stream for the specified stream.
   * The default byte order is BIG_ENDIAN.
   * @param is the input stream.
   */
  public ArrayInputStream(InputStream is) {
    this(is,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an array input stream for the specified file input stream.
   * The default byte order is BIG_ENDIAN.
   * @param fis the file input stream.
   */
  public ArrayInputStream(FileInputStream fis) {
    this(fis,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an array input stream for the specified file name.
   * The default byte order is BIG_ENDIAN.
   * @param name the file name.
   */
  public ArrayInputStream(String name) throws FileNotFoundException {
    this(new FileInputStream(name));
  }

  /**
   * Constructs an array input stream for the specified file.
   * The default byte order is BIG_ENDIAN.
   * @param file the file.
   */
  public ArrayInputStream(File file) throws FileNotFoundException {
    this(new FileInputStream(file));
  }

  /**
   * Constructs an array input stream for the specified stream and byte order.
   * @param is the input stream.
   * @param bo the byte order.
   */
  public ArrayInputStream(InputStream is, ByteOrder bo) {
    super(is);
    _dis = new DataInputStream(new BufferedInputStream(is));
    _ai = new ArrayInputAdapter(_dis,bo);
    _bo = bo;
  }

  /**
   * Constructs an array input stream for the specified file name
   * and byte order.
   * The default byte order is BIG_ENDIAN.
   * @param name the file name.
   * @param bo the byte order.
   */
  public ArrayInputStream(String name, ByteOrder bo) 
    throws FileNotFoundException 
  {
    this(new FileInputStream(name),bo);
  }

  /**
   * Constructs an array input stream for the specified file and byte order.
   * The default byte order is BIG_ENDIAN.
   * @param file the file.
   * @param bo the byte order.
   */
  public ArrayInputStream(File file, ByteOrder bo) 
    throws FileNotFoundException 
  {
    this(new FileInputStream(file),bo);
  }

  /**
   * Closes this array input stream.
   */
  public void close() throws IOException {
    // Anything else to do?
    super.close();
  }

  /**
   * Gets the byte order for this stream.
   * @return the byte order.
   */
  public ByteOrder getByteOrder() {
    return _bo;
  }

  public void readFully(byte[] b) throws IOException {
    _ai.readFully(b);
  }
  public void readFully(byte[] b, int off, int len) throws IOException {
    _ai.readFully(b,off,len);
  }
  public int skipBytes(int n) throws IOException {
    return _ai.skipBytes(n);
  }
  public final boolean readBoolean() throws IOException {
    return _ai.readBoolean();
  }
  public final byte readByte() throws IOException {
    return _ai.readByte();
  }
  public final int readUnsignedByte() throws IOException {
    return _ai.readUnsignedByte();
  }
  public final short readShort() throws IOException {
    return _ai.readShort();
  }
  public final int readUnsignedShort() throws IOException {
    return _ai.readUnsignedShort();
  }
  public final char readChar() throws IOException {
    return _ai.readChar();
  }
  public final int readInt() throws IOException {
    return _ai.readInt();
  }
  public final long readLong() throws IOException {
    return _ai.readLong();
  }
  public final float readFloat() throws IOException {
    return _ai.readFloat();
  }
  public final double readDouble() throws IOException {
    return _ai.readDouble();
  }
  public final String readLine() throws IOException {
    return _ai.readLine();
  }
  public final String readUTF() throws IOException {
    return _ai.readUTF();
  }

  /**
   * Reads byte elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readBytes(byte[] v, int k, int n) throws IOException {
    _ai.readBytes(v,k,n);
  }

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[] v) throws IOException {
    _ai.readBytes(v);
  }

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[][] v) throws IOException {
    _ai.readBytes(v);
  }

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[][][] v) throws IOException {
    _ai.readBytes(v);
  }

  /**
   * Reads char elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readChars(char[] v, int k, int n) throws IOException {
    _ai.readChars(v,k,n);
  }

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[] v) throws IOException {
    _ai.readChars(v);
  }

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[][] v) throws IOException {
    _ai.readChars(v);
  }

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[][][] v) throws IOException {
    _ai.readChars(v);
  }

  /**
   * Reads short elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readShorts(short[] v, int k, int n) throws IOException {
    _ai.readShorts(v,k,n);
  }

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[] v) throws IOException {
    _ai.readShorts(v);
  }

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[][] v) throws IOException {
    _ai.readShorts(v);
  }

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[][][] v) throws IOException {
    _ai.readShorts(v);
  }

  /**
   * Reads int elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readInts(int[] v, int k, int n) throws IOException {
    _ai.readInts(v,k,n);
  }

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[] v) throws IOException {
    _ai.readInts(v);
  }

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[][] v) throws IOException {
    _ai.readInts(v);
  }

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[][][] v) throws IOException {
    _ai.readInts(v);
  }

  /**
   * Reads long elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readLongs(long[] v, int k, int n) throws IOException {
    _ai.readLongs(v,k,n);
  }

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[] v) throws IOException {
    _ai.readLongs(v);
  }

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[][] v) throws IOException {
    _ai.readLongs(v);
  }

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[][][] v) throws IOException {
    _ai.readLongs(v);
  }

  /**
   * Reads float elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readFloats(float[] v, int k, int n) throws IOException {
    _ai.readFloats(v,k,n);
  }

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[] v) throws IOException {
    _ai.readFloats(v);
  }

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[][] v) throws IOException {
    _ai.readFloats(v);
  }

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[][][] v) throws IOException {
    _ai.readFloats(v);
  }

  /**
   * Reads double elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readDoubles(double[] v, int k, int n) throws IOException {
    _ai.readDoubles(v,k,n);
  }

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[] v) throws IOException {
    _ai.readDoubles(v);
  }

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[][] v) throws IOException {
    _ai.readDoubles(v);
  }

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[][][] v) throws IOException {
    _ai.readDoubles(v);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private DataInputStream _dis;
  private ArrayInput _ai;
  private ByteOrder _bo;
}
