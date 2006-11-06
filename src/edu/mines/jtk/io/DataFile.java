/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.io;

import static java.lang.Math.min;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;

/**
 * A data file has the capabilities of a {@link java.io.RandomAccessFile},
 * and more. For example, a data file has methods for efficiently reading 
 * and writing arrays of primitive values, such as ints and floats. Also, 
 * data files may be read or written with BIG_ENDIAN or LITTLE_ENDIAN byte 
 * orders. In other words, a data file is a better random access file.
 * <p>
 * A data file implements the interfaces {@link java.io.DataInput} and
 * {@link java.io.DataOutput}. Those interfaces specify methods for reading
 * single primitive values; e.g., {@link java.io.DataInput#readFloat()}.
 * Similar methods are specified for writing single primitive values. When 
 * reading or writing arrays of primitive values, calling those methods 
 * for each array element can be inefficient. Therefore, this class also
 * provides methods that read and write arrays of primitive values in one
 * call.
 * <p>
 * For efficiency, some software systems read and write primitive values 
 * using the native byte order of the hardware. That byte order may be 
 * BIG_ENDIAN or LITTLE_ENDIAN, and may be specified when constructing a 
 * data file.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.14
 */
public class DataFile implements DataInput, DataOutput, Closeable {

  /**
   * The byte order of the file. The default is BIG_ENDIAN, the byte order 
   * for a {@link java.io.RandomAccessFile}.
   */
  public enum ByteOrder {
    BIG_ENDIAN,
    LITTLE_ENDIAN;
  }

  /**
   * Returns the native byte order for the platform with this file.
   * @return the byte order.
   */
  public static ByteOrder nativeByteOrder() {
    if (java.nio.ByteOrder.nativeOrder()==java.nio.ByteOrder.BIG_ENDIAN) {
      return ByteOrder.BIG_ENDIAN;
    } else {
      return ByteOrder.LITTLE_ENDIAN;
    }
  }

  /**
   * Constructs a data file with specified name and access mode.
   * @param name the file name.
   * @param mode the access mode; "r", "rw", "rws", or "rwd".
   */
  public DataFile(String name, String mode)
    throws FileNotFoundException 
  {
    this(name,mode,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs a data file with specified file and access mode.
   * @param file the file.
   * @param mode the access mode; "r", "rw", "rws", or "rwd".
   */
  public DataFile(File file, String mode) 
    throws FileNotFoundException 
  {
    this(file,mode,ByteOrder.BIG_ENDIAN);
  }

  /**
   * Constructs a data file with specified name, access mode, and byte order.
   * @param name the file name.
   * @param mode the access mode; "r", "rw", "rws", or "rwd".
   * @param order the byte order.
   */
  public DataFile(String name, String mode, ByteOrder order) 
    throws FileNotFoundException 
  {
    this(name!=null?new File(name):null,mode,order);
  }

  /**
   * Constructs a data file with specified file, access mode, and byte order.
   * @param file the file.
   * @param mode the access mode; "r", "rw", "rws", or "rwd".
   * @param order the byte order.
   */
  public DataFile(File file, String mode, ByteOrder order) 
    throws FileNotFoundException 
  {
    _raf = new RandomAccessFile(file,mode);
    _fc = _raf.getChannel();
    _bo = order;
    _bb = ByteBuffer.allocateDirect(4096);
    if (order==ByteOrder.BIG_ENDIAN) {
      _bb.order(java.nio.ByteOrder.BIG_ENDIAN);
    } else {
      _bb.order(java.nio.ByteOrder.LITTLE_ENDIAN);
    }
    _cb = _bb.asCharBuffer();
    _sb = _bb.asShortBuffer();
    _ib = _bb.asIntBuffer();
    _lb = _bb.asLongBuffer();
    _fb = _bb.asFloatBuffer();
    _db = _bb.asDoubleBuffer();
  }
  
  /**
   * Gets the file descripter for this data file.
   * @return the file descriptor.
   */
  public final FileDescriptor getFD() throws IOException {
    return _raf.getFD();
  }

  /**
   * Gets the file channel for this data file.
   * @return the file channel.
   */
  public final FileChannel getChannel() {
    return _raf.getChannel();
  }

  /**
   * Gets the byte order for this data file.
   * @return the byte order.
   */
  public ByteOrder getByteOrder() {
    return _bo;
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
    _raf.readFully(b);
  }

  public void readFully(byte[] b, int off, int len) throws IOException {
    _raf.readFully(b,off,len);
  }

  public int skipBytes(int n) throws IOException {
    return _raf.skipBytes(n);
  }

  public void write(int b) throws IOException {
    _raf.write(b);
  }

  public void write(byte[] b) throws IOException {
    _raf.write(b);
  }

  public void write(byte[] b, int off, int len) throws IOException {
    _raf.write(b,off,len);
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
    _fc = null;
    _bb = null;
  }

  public final boolean readBoolean() throws IOException {
    return _raf.readBoolean();
  }

  public final byte readByte() throws IOException {
    return _raf.readByte();
  }

  public final int readUnsignedByte() throws IOException {
    return _raf.readUnsignedByte();
  }

  public final short readShort() throws IOException {
    int b1 = _raf.read();
    int b2 = _raf.read();
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
    int b1 = _raf.read();
    int b2 = _raf.read();
    int b3 = _raf.read();
    int b4 = _raf.read();
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
    return _raf.readLine();
  }

  public final String readUTF() throws IOException {
    return _raf.readUTF();
  }

  public void writeBoolean(boolean v) throws IOException {
    _raf.writeBoolean(v);
  }

  public void writeByte(int v) throws IOException {
    _raf.writeByte(v);
  }

  public void writeShort(int v) throws IOException {
    if (_bo==ByteOrder.BIG_ENDIAN) {
      _raf.write((v>>>8)&0xFF);
      _raf.write((v    )&0xFF);
    } else {
      _raf.write((v    )&0xFF);
      _raf.write((v>>>8)&0xFF);
    }
  }

  public void writeChar(int v) throws IOException {
    _raf.writeShort(v);
  }

  public void writeInt(int v) throws IOException {
    if (_bo==ByteOrder.BIG_ENDIAN) {
      _raf.write((v>>>24)&0xFF);
      _raf.write((v>>>16)&0xFF);
      _raf.write((v>>> 8)&0xFF);
      _raf.write((v     )&0xFF);
    } else {
      _raf.write((v     )&0xFF);
      _raf.write((v>>> 8)&0xFF);
      _raf.write((v>>>16)&0xFF);
      _raf.write((v>>>24)&0xFF);
    }
  }

  public void writeLong(long v) throws IOException {
    if (_bo==ByteOrder.BIG_ENDIAN) {
      _raf.write((int)(v>>>56)&0xFF);
      _raf.write((int)(v>>>48)&0xFF);
      _raf.write((int)(v>>>40)&0xFF);
      _raf.write((int)(v>>>32)&0xFF);
      _raf.write((int)(v>>>24)&0xFF);
      _raf.write((int)(v>>>16)&0xFF);
      _raf.write((int)(v>>> 8)&0xFF);
      _raf.write((int)(v     )&0xFF);
    } else {
      _raf.write((int)(v     )&0xFF);
      _raf.write((int)(v>>> 8)&0xFF);
      _raf.write((int)(v>>>16)&0xFF);
      _raf.write((int)(v>>>24)&0xFF);
      _raf.write((int)(v>>>32)&0xFF);
      _raf.write((int)(v>>>40)&0xFF);
      _raf.write((int)(v>>>48)&0xFF);
      _raf.write((int)(v>>>56)&0xFF);
    }
  }

  public void writeFloat(float v) throws IOException {
    writeInt(Float.floatToIntBits(v));
  }

  public void writeDouble(double v) throws IOException {
    writeLong(Double.doubleToLongBits(v));
  }

  public void writeBytes(String s) throws IOException {
    _raf.writeBytes(s);
  }

  public void writeChars(String s) throws IOException {
    int n = s.length();
    for (int i=0; i<n; ++i) {
      writeChar(s.charAt(i));
    }
  }

  public void writeUTF(String s) throws IOException {
    _raf.writeUTF(s);
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
    for (int i=0; i<v.length; ++i)
      readBytes(v[i]);
  }

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      readBytes(v[i]);
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
      _bb.position(0).limit(l*2);
      _fc.read(_bb);
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
    for (int i=0; i<v.length; ++i)
      readChars(v[i]);
  }

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      readChars(v[i]);
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
      _bb.position(0).limit(l*2);
      _fc.read(_bb);
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
    for (int i=0; i<v.length; ++i)
      readShorts(v[i]);
  }

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      readShorts(v[i]);
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
      _bb.position(0).limit(l*4);
      _fc.read(_bb);
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
    for (int i=0; i<v.length; ++i)
      readInts(v[i]);
  }

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      readInts(v[i]);
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
      _bb.position(0).limit(l*8);
      _fc.read(_bb);
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
    for (int i=0; i<v.length; ++i)
      readLongs(v[i]);
  }

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      readLongs(v[i]);
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
      _bb.position(0).limit(l*4);
      _fc.read(_bb);
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
    for (int i=0; i<v.length; ++i)
      readFloats(v[i]);
  }

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      readFloats(v[i]);
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
      _bb.position(0).limit(l*8);
      _fc.read(_bb);
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
    for (int i=0; i<v.length; ++i)
      readDoubles(v[i]);
  }

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      readDoubles(v[i]);
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
    for (int i=0; i<v.length; ++i)
      writeBytes(v[i]);
  }

  /**
   * Writes byte elements from a specified array.
   * @param v the array.
   */
  public void writeBytes(byte[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      writeBytes(v[i]);
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
      _bb.position(0).limit(l*2);
      _fc.write(_bb);
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
    for (int i=0; i<v.length; ++i)
      writeChars(v[i]);
  }

  /**
   * Writes char elements from a specified array.
   * @param v the array.
   */
  public void writeChars(char[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      writeChars(v[i]);
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
      _bb.position(0).limit(l*2);
      _fc.write(_bb);
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
    for (int i=0; i<v.length; ++i)
      writeShorts(v[i]);
  }

  /**
   * Writes short elements from a specified array.
   * @param v the array.
   */
  public void writeShorts(short[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      writeShorts(v[i]);
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
      _bb.position(0).limit(l*4);
      _fc.write(_bb);
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
    for (int i=0; i<v.length; ++i)
      writeInts(v[i]);
  }

  /**
   * Writes int elements from a specified array.
   * @param v the array.
   */
  public void writeInts(int[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      writeInts(v[i]);
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
      _bb.position(0).limit(l*8);
      _fc.write(_bb);
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
    for (int i=0; i<v.length; ++i)
      writeLongs(v[i]);
  }

  /**
   * Writes long elements from a specified array.
   * @param v the array.
   */
  public void writeLongs(long[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      writeLongs(v[i]);
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
      _bb.position(0).limit(l*4);
      _fc.write(_bb);
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
    for (int i=0; i<v.length; ++i)
      writeFloats(v[i]);
  }

  /**
   * Writes float elements from a specified array.
   * @param v the array.
   */
  public void writeFloats(float[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      writeFloats(v[i]);
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
      _bb.position(0).limit(l*8);
      _fc.write(_bb);
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
    for (int i=0; i<v.length; ++i)
      writeDoubles(v[i]);
  }

  /**
   * Writes double elements from a specified array.
   * @param v the array.
   */
  public void writeDoubles(double[][][] v) throws IOException {
    for (int i=0; i<v.length; ++i)
      writeDoubles(v[i]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private RandomAccessFile _raf;
  private FileChannel _fc;
  private ByteOrder _bo;
  private ByteBuffer _bb;
  private CharBuffer _cb;
  private ShortBuffer _sb;
  private IntBuffer _ib;
  private LongBuffer _lb;
  private FloatBuffer _fb;
  private DoubleBuffer _db;

  // Currently unused.
  /*
  private static char swap(char v) {
    return (char)((v<<8)|((v>>8)&0xff));
  }
  private static short swap(short v) {
    return (short)((v<<8)|((v>>8)&0xff));
  }
  private static int swap(int v) {
    return ((v    )     )<<24 |
           ((v>> 8)&0xff)<<16 |
           ((v>>16)&0xff)<< 8 |
           ((v>>24)&0xff);
  }
  private static long swap(long v) {
    return ((v    )      )<<56 |
           ((v>> 8)&0xffL)<<48 |
           ((v>>16)&0xffL)<<40 |
           ((v>>24)&0xffL)<<32 |
           ((v>>32)&0xffL)<<24 |
           ((v>>40)&0xffL)<<16 |
           ((v>>48)&0xffL)<< 8 |
           ((v>>56)&0xffL);
  }
  */
}
