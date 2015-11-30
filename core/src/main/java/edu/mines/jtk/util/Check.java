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
package edu.mines.jtk.util;

/**
 * Facilitates checks for common conditions. Methods in this class throw
 * appropriate exceptions when specified conditions are not satisfied.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public class Check {

  /**
   * Ensures that the specified condition for an argument is true.
   * @param condition the condition.
   * @param message a description of the condition.
   * @exception IllegalArgumentException if the condition is false.
   */
  public static void argument(boolean condition, String message) {
    if (!condition)
      throw new IllegalArgumentException("required condition: "+message);
  }

  /**
   * Ensures that the specified condition of state is true.
   * @param condition the condition.
   * @param message a description of the condition.
   * @exception IllegalStateException if the condition is false.
   */
  public static void state(boolean condition, String message) {
    if (!condition)
      throw new IllegalStateException("required condition: "+message);
  }

  /**
   * Ensures that the specified zero-based index is in bounds.
   * @param n the smallest positive number that is not in bounds.
   * @param i the index.
   * @exception IndexOutOfBoundsException if index is out of bounds.
   */
  public static void index(int n, int i) {
    if (i<0)
      throw new IndexOutOfBoundsException("index i="+i+" < 0");
    if (n<=i)
      throw new IndexOutOfBoundsException("index i="+i+" >= n="+n);
  }

  /**
   * Ensures that the specified array index is in bounds.
   * @param a the array.
   * @param i the index.
   * @exception ArrayIndexOutOfBoundsException if index is out of bounds.
   */
  public static void index(byte[] a, int i) {
    _b = a[i];
  }

  /**
   * Ensures that the specified array index is in bounds.
   * @param a the array.
   * @param i the index.
   * @exception ArrayIndexOutOfBoundsException if index is out of bounds.
   */
  public static void index(short[] a, int i) {
    _s = a[i];
  }

  /**
   * Ensures that the specified array index is in bounds.
   * @param a the array.
   * @param i the index.
   * @exception ArrayIndexOutOfBoundsException if index is out of bounds.
   */
  public static void index(int[] a, int i) {
    _i = a[i];
  }

  /**
   * Ensures that the specified array index is in bounds.
   * @param a the array.
   * @param i the index.
   * @exception ArrayIndexOutOfBoundsException if index is out of bounds.
   */
  public static void index(long[] a, int i) {
    _l = a[i];
  }

  /**
   * Ensures that the specified array index is in bounds.
   * @param a the array.
   * @param i the index.
   * @exception ArrayIndexOutOfBoundsException if index is out of bounds.
   */
  public static void index(float[] a, int i) {
    _f = a[i];
  }

  /**
   * Ensures that the specified array index is in bounds.
   * @param a the array.
   * @param i the index.
   * @exception ArrayIndexOutOfBoundsException if index is out of bounds.
   */
  public static void index(double[] a, int i) {
    _d = a[i];
  }

  private static byte _b;
  private static short _s;
  private static int _i;
  private static long _l;
  private static float _f;
  private static double _d;

  // Static methods only.
  private Check() {
	System.out.println(_b);
	System.out.println(_s);
	System.out.println(_i);
	System.out.println(_l);
	System.out.println(_f);
	System.out.println(_d);
  }
}
