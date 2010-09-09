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
 * An array file expands the capabilities of {@link java.io.RandomAccessFile}. 
 * Specifically, an array file has methods for efficiently reading and writing 
 * arrays of primitive values, such as ints and floats. Also, array files may 
 * be read or written with either BIG_ENDIAN or LITTLE_ENDIAN byte orders.
 * <p>
 * An array file implements the interfaces {@link ArrayInput} and 
 * {@link ArrayOutput}, which extend the standard Java interfaces 
 * {@link java.io.DataInput} and {@link java.io.DataOutput}, respectively.
 * <p>
 * An array file can be constructed by specifying a file name and access mode 
 * (as for a {@link java.io.RandomAccessFile}). Alternatively, an array file 
 * can be constructed from an existing {@link java.io.RandomAccessFile}.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.08.05
 */
public class ArrayFile implements ArrayInput, ArrayOutput, Closeable {

  /**
   * Constructs an array file with specified name and access mode.
   * @param name the file name.
   * @param mode the access mode; "r", "rw", "rws", or "rwd".
   */
  public ArrayFile(String name, String mode) throws FileNotFoundException {
    this(name,mode,ByteOrder.BIG_ENDIAN,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an array file with specified file and access mode.
   * @param file the file.
   * @param mode the access mode; "r", "rw", "rws", or "rwd".
   */
  public ArrayFile(File file, String mode) throws FileNotFoundException {
    this(file,mode,ByteOrder.BIG_ENDIAN,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs an array file with specified name, access mode, and byte orders.
   * @param name the file name.
   * @param mode the access mode; "r", "rw", "rws", or "rwd".
   * @param bor the byte order for reading.
   * @param bow the byte order for writing.
   */
  public ArrayFile(String name, String mode, ByteOrder bor, ByteOrder bow) 
    throws FileNotFoundException 
  {
    this(name!=null?new File(name):null,mode,bor,bow);
  }

  /**
   * Constructs an array file with specified file, access mode, and byte orders.
   * @param file the file.
   * @param mode the access mode; "r", "rw", "rws", or "rwd".
   * @param bor the byte order for reading.
   * @param bow the byte order for writing.
   */
  public ArrayFile(File file, String mode, ByteOrder bor, ByteOrder bow) 
    throws FileNotFoundException 
  {
    this(new RandomAccessFile(file,mode),bor,bow);
  }

  /**
   * Constructs an array file for a specified random-access file 
   * and byte orders.
   * @param raf the random-access file.
   * @param bor the byte order for reading.
   * @param bow the byte order for writing.
   */
  public ArrayFile(RandomAccessFile raf, ByteOrder bor, ByteOrder bow) {
    _raf = raf;
    _bor = bor;
    _bow = bow;
    _ai = new ArrayInputAdapter(raf,bor);
    _ao = new ArrayOutputAdapter(raf,bow);
  }

  /**
   * Gets the byte order for reading data.
   * @return the byte order.
   */
  public ByteOrder getByteOrderRead() {
    return _bor;
  }

  /**
   * Gets the byte order for writing data.
   * @return the byte order.
   */
  public ByteOrder getByteOrderWrite() {
    return _bow;
  }

  /**
   * Reads a byte value from this file. 
   * The returned value will be in the range 0 to 255.
   * @return the byte value.
   */
  public int read() throws IOException {
    return _raf.read();
  }

  /**
   * Reads up to b.length bytes from this file.
   * @param b array into which to read bytes.
   * @return the number of bytes read; -1 if end of file.
   */
  public int read(byte[] b) throws IOException {
    return _raf.read(b);
  }

  /**
   * Reads up to len bytes from this file.
   * @param b array into which to read bytes.
   * @param off array index of first byte to read.
   * @param len the number of bytes to read.
   * @return the number of bytes read; -1 if end of file.
   */
  public int read(byte[] b, int off, int len) throws IOException {
    return _raf.read(b,off,len);
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

  public void write(int b) throws IOException {
    _ao.write(b);
  }

  public void write(byte[] b) throws IOException {
    _ao.write(b);
  }

  public void write(byte[] b, int off, int len) throws IOException {
    _ao.write(b,off,len);
  }

  /**
   * Gets the file pointer (byte offset) for this file. The next read or
   * write begins at this offset.
   * @return the file pointer.
   */
  public long getFilePointer() throws IOException {
    return _raf.getFilePointer();
  }

  /**
   * Sets the file pointer (byte offset) for this file. The next read or
   * or write begins at this offset. 
   * <p>
   * The offset may be set beyond the end of the file. Setting the offset
   * beyond the end of the file does not increase the file length. The file
   * length increases only by writing beyond the end of the file.
   * @param off the file pointer, the offset in bytes from the
   *  beginning of the file.
   */
  public void seek(long off) throws IOException {
    _raf.seek(off);
  }

  /**
   * Returns the length of this file.
   * @return the file length, in bytes.
   */
  public long length() throws IOException {
    return _raf.length();
  }

  /**
   * Sets the length of this file. If the current file length exceeds
   * the specified new length, then the file will be truncated. In this
   * case, if the file pointer exceeds the new length, then that pointer
   * will equal to the new length when this methods returns.
   * <p>
   * If the current file length is less than the specified new length,
   * then this file will extended. The content of the extended portion
   * is undefined.
   * @param newLength the new length.
   */
  public void setLength(long newLength) throws IOException {
    _raf.setLength(newLength);
  }

  /**
   * Closes this data file, releasing any associated system resources.
   */
  public void close() throws IOException {
    _raf.close();
    _raf = null;
    _ai = null;
    _ao = null;
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

  // Only one of these is non-null, depending on which type of
  // RandomAccessFile is wrapped by this array file.
  private RandomAccessFile _raf;
  private ByteOrder _bor;
  private ByteOrder _bow;
  private ArrayInput _ai;
  private ArrayOutput _ao;
}
