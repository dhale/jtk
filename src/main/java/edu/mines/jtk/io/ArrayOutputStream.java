/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.io;

import java.io.*;
import java.nio.ByteOrder;

/**
 * An output stream that implements {@link ArrayOutput}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.08.05
 */
public class ArrayOutputStream 
  extends FilterOutputStream 
  implements ArrayOutput 
{

  /**
   * Constructs an array output stream for the specified stream.
   * The default byte order is BIG_ENDIAN.
   * @param os the output stream.
   */
  public ArrayOutputStream(OutputStream os) {
    this(os,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an array output stream for the specified file output stream.
   * The default byte order is BIG_ENDIAN.
   * @param fos the file output stream.
   */
  public ArrayOutputStream(FileOutputStream fos) {
    this(fos,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an array output stream for the specified file name.
   * The default byte order is BIG_ENDIAN.
   * @param name the file name.
   */
  public ArrayOutputStream(String name) throws FileNotFoundException {
    this(new FileOutputStream(name));
  }

  /**
   * Constructs an array output stream for the specified file.
   * The default byte order is BIG_ENDIAN.
   * @param file the file.
   */
  public ArrayOutputStream(File file) throws FileNotFoundException {
    this(new FileOutputStream(file));
  }

  /**
   * Constructs an array output stream for the specified stream and byte order.
   * @param os the output stream.
   * @param bo the byte order.
   */
  public ArrayOutputStream(OutputStream os, ByteOrder bo) {
    super(os);
    _dos = new DataOutputStream(new BufferedOutputStream(os));
    _ao = new ArrayOutputAdapter(_dos,bo);
    _bo = bo;
  }

  /**
   * Constructs an array output stream for the specified file name
   * and byte order.
   * The default byte order is BIG_ENDIAN.
   * @param name the file name.
   * @param bo the byte order.
   */
  public ArrayOutputStream(String name, ByteOrder bo) 
    throws FileNotFoundException 
  {
    this(new FileOutputStream(name),bo);
  }

  /**
   * Constructs an array output stream for the specified file and byte order.
   * The default byte order is BIG_ENDIAN.
   * @param file the file.
   * @param bo the byte order.
   */
  public ArrayOutputStream(File file, ByteOrder bo) 
    throws FileNotFoundException 
  {
    this(new FileOutputStream(file),bo);
  }

  public void flush() throws IOException {
    _dos.flush();
    super.flush();
  }

  /**
   * Closes this array output stream.
   */
  public void close() throws IOException {
    flush();
    super.close();
  }

  /**
   * Gets the byte order for this stream.
   * @return the byte order.
   */
  public ByteOrder getByteOrder() {
    return _bo;
  }

  public void write(int b) throws IOException {
    _ao.write(b);
  }
  public void write(byte[] b) throws IOException {
    _ao.write(b);
  }
  public void write(byte[] b, int off, int len) throws IOException {
    _ao.write(b,off,len);
  }
  public void writeBoolean(boolean v) throws IOException {
    _ao.writeBoolean(v);
  }
  public void writeByte(int v) throws IOException {
    _ao.writeByte(v);
  }
  public void writeShort(int v) throws IOException {
    _ao.writeShort(v);
  }
  public void writeChar(int v) throws IOException {
    _ao.writeChar(v);
  }
  public void writeInt(int v) throws IOException {
    _ao.writeInt(v);
  }
  public void writeLong(long v) throws IOException {
    _ao.writeLong(v);
  }
  public void writeFloat(float v) throws IOException {
    _ao.writeFloat(v);
  }
  public void writeDouble(double v) throws IOException {
    _ao.writeDouble(v);
  }
  public void writeBytes(String s) throws IOException {
    _ao.writeBytes(s);
  }
  public void writeChars(String s) throws IOException {
    _ao.writeChars(s);
  }
  public void writeUTF(String s) throws IOException {
    _ao.writeUTF(s);
  }

  /**
   * Writes byte elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeBytes(byte[] v, int k, int n) throws IOException {
    _ao.writeBytes(v,k,n);
  }

  /**
   * Writes byte elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeBytes(byte[] v) throws IOException {
    _ao.writeBytes(v);
  }

  /**
   * Writes byte elements from a specified array.
   * @param v the array.
   */
  public void writeBytes(byte[][] v) throws IOException {
    _ao.writeBytes(v);
  }

  /**
   * Writes byte elements from a specified array.
   * @param v the array.
   */
  public void writeBytes(byte[][][] v) throws IOException {
    _ao.writeBytes(v);
  }

  /**
   * Writes char elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeChars(char[] v, int k, int n) throws IOException {
    _ao.writeChars(v,k,n);
  }

  /**
   * Writes char elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeChars(char[] v) throws IOException {
    _ao.writeChars(v);
  }

  /**
   * Writes char elements from a specified array.
   * @param v the array.
   */
  public void writeChars(char[][] v) throws IOException {
    _ao.writeChars(v);
  }

  /**
   * Writes char elements from a specified array.
   * @param v the array.
   */
  public void writeChars(char[][][] v) throws IOException {
    _ao.writeChars(v);
  }

  /**
   * Writes short elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeShorts(short[] v, int k, int n) throws IOException {
    _ao.writeShorts(v,k,n);
  }

  /**
   * Writes shorts elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeShorts(short[] v) throws IOException {
    _ao.writeShorts(v);
  }

  /**
   * Writes short elements from a specified array.
   * @param v the array.
   */
  public void writeShorts(short[][] v) throws IOException {
    _ao.writeShorts(v);
  }

  /**
   * Writes short elements from a specified array.
   * @param v the array.
   */
  public void writeShorts(short[][][] v) throws IOException {
    _ao.writeShorts(v);
  }

  /**
   * Writes int elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeInts(int[] v, int k, int n) throws IOException {
    _ao.writeInts(v,k,n);
  }

  /**
   * Writes int elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeInts(int[] v) throws IOException {
    _ao.writeInts(v);
  }

  /**
   * Writes int elements from a specified array.
   * @param v the array.
   */
  public void writeInts(int[][] v) throws IOException {
    _ao.writeInts(v);
  }

  /**
   * Writes int elements from a specified array.
   * @param v the array.
   */
  public void writeInts(int[][][] v) throws IOException {
    _ao.writeInts(v);
  }

  /**
   * Writes long elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeLongs(long[] v, int k, int n) throws IOException {
    _ao.writeLongs(v,k,n);
  }

  /**
   * Writes long elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeLongs(long[] v) throws IOException {
    _ao.writeLongs(v);
  }

  /**
   * Writes long elements from a specified array.
   * @param v the array.
   */
  public void writeLongs(long[][] v) throws IOException {
    _ao.writeLongs(v);
  }

  /**
   * Writes long elements from a specified array.
   * @param v the array.
   */
  public void writeLongs(long[][][] v) throws IOException {
    _ao.writeLongs(v);
  }

  /**
   * Writes float elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeFloats(float[] v, int k, int n) throws IOException {
    _ao.writeFloats(v,k,n);
  }

  /**
   * Writes float elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeFloats(float[] v) throws IOException {
    _ao.writeFloats(v);
  }

  /**
   * Writes float elements from a specified array.
   * @param v the array.
   */
  public void writeFloats(float[][] v) throws IOException {
    _ao.writeFloats(v);
  }

  /**
   * Writes float elements from a specified array.
   * @param v the array.
   */
  public void writeFloats(float[][][] v) throws IOException {
    _ao.writeFloats(v);
  }

  /**
   * Writes double elements from a specified array.
   * @param v the array.
   * @param k the index of the first element to write.
   * @param n the number of elements to write.
   */
  public void writeDoubles(double[] v, int k, int n) throws IOException {
    _ao.writeDoubles(v,k,n);
  }

  /**
   * Writes double elements from a specified array.
   * The array length equals the number of elements to write.
   * @param v the array.
   */
  public void writeDoubles(double[] v) throws IOException {
    _ao.writeDoubles(v);
  }

  /**
   * Writes double elements from a specified array.
   * @param v the array.
   */
  public void writeDoubles(double[][] v) throws IOException {
    _ao.writeDoubles(v);
  }

  /**
   * Writes double elements from a specified array.
   * @param v the array.
   */
  public void writeDoubles(double[][][] v) throws IOException {
    _ao.writeDoubles(v);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private DataOutputStream _dos;
  private ArrayOutput _ao;
  private ByteOrder _bo;
}
