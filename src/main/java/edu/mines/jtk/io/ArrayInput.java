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

import java.io.DataInput;
import java.io.IOException;

/**
 * An interface for reading arrays of primitive values from a binary stream.
 * This interfaces extends the standard interface {@link java.io.DataInput}.
 * It adds methods for reading arrays of primitive values.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.08.05
 */
public interface ArrayInput extends DataInput {

  // From DataInput
  public void readFully(byte[] b) throws IOException;
  public void readFully(byte[] b, int off, int len) throws IOException;
  public int skipBytes(int n) throws IOException;
  public boolean readBoolean() throws IOException;
  public byte readByte() throws IOException;
  public int readUnsignedByte() throws IOException;
  public short readShort() throws IOException;
  public int readUnsignedShort() throws IOException;
  public char readChar() throws IOException;
  public int readInt() throws IOException;
  public long readLong() throws IOException;
  public float readFloat() throws IOException;
  public double readDouble() throws IOException;
  public String readLine() throws IOException;
  public String readUTF() throws IOException;

  /**
   * Reads byte elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readBytes(byte[] v, int k, int n) throws IOException;

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[] v) throws IOException;

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[][] v) throws IOException;

  /**
   * Reads byte elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readBytes(byte[][][] v) throws IOException;

  /**
   * Reads char elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readChars(char[] v, int k, int n) throws IOException;

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[] v) throws IOException;

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[][] v) throws IOException;

  /**
   * Reads char elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readChars(char[][][] v) throws IOException;

  /**
   * Reads short elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readShorts(short[] v, int k, int n) throws IOException;

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[] v) throws IOException;

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[][] v) throws IOException;

  /**
   * Reads short elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readShorts(short[][][] v) throws IOException;

  /**
   * Reads int elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readInts(int[] v, int k, int n) throws IOException;

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[] v) throws IOException;

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[][] v) throws IOException;

  /**
   * Reads int elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readInts(int[][][] v) throws IOException;

  /**
   * Reads long elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readLongs(long[] v, int k, int n) throws IOException;

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[] v) throws IOException;

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[][] v) throws IOException;

  /**
   * Reads long elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readLongs(long[][][] v) throws IOException;

  /**
   * Reads float elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readFloats(float[] v, int k, int n) throws IOException;

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[] v) throws IOException;

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[][] v) throws IOException;

  /**
   * Reads float elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readFloats(float[][][] v) throws IOException;

  /**
   * Reads double elements into a specified array.
   * @param v the array.
   * @param k the index of the first element to read.
   * @param n the number of elements to read.
   */
  public void readDoubles(double[] v, int k, int n) throws IOException;

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[] v) throws IOException;

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[][] v) throws IOException;

  /**
   * Reads double elements into a specified array.
   * The array length equals the number of elements to read.
   * @param v the array.
   */
  public void readDoubles(double[][][] v) throws IOException;
}
