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
package edu.mines.jtk.util;

/**
 * A generic interface for a 3-D array of floats. This interface enables
 * getting and setting 1-D, 2-D, and 3-D subarrays (slices) of elements
 * from any data structure that can be indexed like a 3-D array. Unlike
 * a 3-D array, that data structure need not reside entirely in memory.
 * <p>
 * Logically, the 3-D array can be considered as float a[n3][n2][n1], 
 * where n1 is the fastest array dimension, and n3 is the slowest.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.05.24
 */
public interface Float3 {

  /**
   * Gets the number of elements in 1st dimension of this array.
   * @return the number of elements in 1st dimension.
   */
  public int getN1();

  /**
   * Gets the number of elements in 2nd dimension of this array.
   * @return the number of elements in 2nd dimension.
   */
  public int getN2();

  /**
   * Gets the number of elements in 3rd dimension of this array.
   * @return the number of elements in 3rd dimension.
   */
  public int getN3();

  /**
   * Gets the specified subarray of elements into the specified 1-D array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m1] of elements of the specified subarray.
   */
  public void get1(int m1, int j1, int j2, int j3, float[] s);

  /**
   * Gets the specified subarray of elements into the specified 1-D array.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m2] of elements of the specified subarray.
   */
  public void get2(int m2, int j1, int j2, int j3, float[] s);

  /**
   * Gets the specified subarray of elements into the specified 1-D array.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3] of elements of the specified subarray.
   */
  public void get3(int m3, int j1, int j2, int j3, float[] s);

  /**
   * Gets the specified subarray of elements into the specified 2-D array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m2][m1] of elements of the specified subarray.
   */
  public void get12(int m1, int m2, int j1, int j2, int j3, float[][] s);

  /**
   * Gets the specified subarray of elements into the specified 2-D array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3][m1] of elements of the specified subarray.
   */
  public void get13(int m1, int m3, int j1, int j2, int j3, float[][] s);

  /**
   * Gets the specified subarray of elements into the specified 2-D array.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3][m2] of elements of the specified subarray.
   */
  public void get23(int m2, int m3, int j1, int j2, int j3, float[][] s);

  /**
   * Gets the specified subarray of elements into the specified 3-D array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3][m2][m1] of elements of the specified subarray.
   */
  public void get123(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[][][] s);

  /**
   * Gets the specified subarray of elements into the specified 1-D array.
   * The length of the array must be at least m1*m2*m3.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m1*m2*m3] of elements of the specified subarray.
   */
  public void get123(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[] s);

  /**
   * Sets the specified subarray of elements from the specified 1-D array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m1] of elements of the specified subarray.
   */
  public void set1(int m1, int j1, int j2, int j3, float[] s);

  /**
   * Sets the specified subarray of elements from the specified 1-D array.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m2] of elements of the specified subarray.
   */
  public void set2(int m2, int j1, int j2, int j3, float[] s);

  /**
   * Sets the specified subarray of elements from the specified 1-D array.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3] of elements of the specified subarray.
   */
  public void set3(int m3, int j1, int j2, int j3, float[] s);

  /**
   * Sets the specified subarray of elements from the specified 2-D array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m2][m1] of elements of the specified subarray.
   */
  public void set12(int m1, int m2, int j1, int j2, int j3, float[][] s);

  /**
   * Sets the specified subarray of elements from the specified 2-D array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3][m1] of elements of the specified subarray.
   */
  public void set13(int m1, int m3, int j1, int j2, int j3, float[][] s);

  /**
   * Sets the specified subarray of elements from the specified 2-D array.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3][m2] of elements of the specified subarray.
   */
  public void set23(int m2, int m3, int j1, int j2, int j3, float[][] s);

  /**
   * Sets the specified subarray of elements from the specified 3-D array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3][m2][m1] of elements of the specified subarray.
   */
  public void set123(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[][][] s);

  /**
   * Sets the specified subarray of elements from the specified 1-D array.
   * The length of the array must be at least m1*m2*m3.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m1*m2*m3] of elements of the specified subarray.
   */
  public void set123(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[] s);
}
