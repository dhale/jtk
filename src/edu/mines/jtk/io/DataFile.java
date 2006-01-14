/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.io;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import static java.lang.Math.min;

/**
 * A data file has the capabilities of a {@link java.io.RandomAccessFile},
 * and more. For example, a data file has methods that enable reading and
 * writing files with either BIG_ENDIAN or LITTLE_ENDIAN byte orders. A
 * data file also has methods for reading and writing arrays of primitives.
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

  public DataFile(String name, String mode)
    throws FileNotFoundException 
  {
    this(name,mode,ByteOrder.BIG_ENDIAN);
  }

  public DataFile(File file, String mode) 
    throws FileNotFoundException 
  {
    this(file,mode,ByteOrder.BIG_ENDIAN);
  }

  public DataFile(String name, String mode, ByteOrder order) 
    throws FileNotFoundException 
  {
    this(name!=null?new File(name):null,mode,order);
  }

  public DataFile(File file, String mode, ByteOrder order) 
    throws FileNotFoundException 
  {
    _raf = new RandomAccessFile(file,mode);
    _fc = _raf.getChannel();
    _bo = order;
    _bb = ByteBuffer.allocate(4096);
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
  
  public final FileDescriptor getFD() throws IOException {
    return _raf.getFD();
  }

  public final FileChannel getChannel() {
    return _raf.getChannel();
  }

  public int read() throws IOException {
    return _raf.read();
  }

  public int read(byte[] b) throws IOException {
    return _raf.read(b);
  }

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

  public long getFilePointer() throws IOException {
    return _raf.getFilePointer();
  }

  public void seek(long pos) throws IOException {
    _raf.seek(pos);
  }

  public long length() throws IOException {
    return _raf.length();
  }

  public void setLength(long newLength) throws IOException {
    _raf.setLength(newLength);
  }

  public void close() throws IOException {
    _raf.close();
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

  public void readFloats(float[] v) throws IOException {
    readFloats(v,0,v.length);
  }

  public void readFloats(float[] v, int k, int n) throws IOException {
    int m = _fb.capacity();
    for (int j=k; j<n; j+=m) {
      int l = min(n-j,m);
      _bb.position(0).limit(l*4);
      _fc.read(_bb);
      _fb.position(0).limit(l);
      _fb.get(v,j,l);
    }
  }

  public void readDoubles(double[] v) throws IOException {
    readDoubles(v,0,v.length);
  }

  public void readDoubles(double[] v, int k, int n) throws IOException {
    int m = _db.capacity();
    for (int j=k; j<n; j+=m) {
      int l = min(n-j,m);
      _bb.position(0).limit(l*8);
      _fc.read(_bb);
      _db.position(0).limit(l);
      _db.get(v,j,l);
    }
  }

  public void writeFloats(float[] v) throws IOException {
    writeFloats(v,0,v.length);
  }

  public void writeFloats(float[] v, int k, int n) throws IOException {
    int m = _fb.capacity();
    for (int j=k; j<n; j+=m) {
      int l = min(n-j,m);
      _fb.position(0).limit(l);
      _fb.put(v,j,l);
      _bb.position(0).limit(l*4);
      _fc.write(_bb);
    }
  }

  public void writeDoubles(double[] v) throws IOException {
    writeDoubles(v,0,v.length);
  }

  public void writeDoubles(double[] v, int k, int n) throws IOException {
    int m = _db.capacity();
    for (int j=k; j<n; j+=m) {
      int l = min(n-j,m);
      _db.position(0).limit(l);
      _db.put(v,j,l);
      _bb.position(0).limit(l*8);
      _fc.write(_bb);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private RandomAccessFile _raf;
  private FileChannel _fc;
  private ByteOrder _bo;

  // Buffers for reading and writing arrays of primitives.
  private ByteBuffer _bb = ByteBuffer.allocate(4096);
  private CharBuffer _cb = _bb.asCharBuffer();
  private ShortBuffer _sb = _bb.asShortBuffer();
  private IntBuffer _ib = _bb.asIntBuffer();
  private LongBuffer _lb = _bb.asLongBuffer();
  private FloatBuffer _fb = _bb.asFloatBuffer();
  private DoubleBuffer _db = _bb.asDoubleBuffer();

  // Currently unused.
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
}
