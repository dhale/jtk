/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.io;

import edu.mines.jtk.util.Check;

/**
 * A simple 3-D array of floats. Implements the interface 
 * {@link edu.mines.jtk.io.Float3} for a simple Java 3-D array 
 * of floats.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.05.24
 */
public class SimpleFloat3 implements Float3 {

  /**
   * Constructs an array of elements initialized to zero.
   * @param n1 the 1st dimension of the array.
   * @param n2 the 2nd dimension of the array.
   * @param n3 the 3rd dimension of the array.
   */
  public SimpleFloat3(int n1, int n2, int n3) {
    _n1 = n1;
    _n2 = n2;
    _n3 = n3;
    _a = new float[n3][n2][n1];
  }

  /**
   * Constructs an array of elements for the specified array.
   * References (does not copy) elements from the specified array.
   * @param a array of elements to be referenced.
   */
  public SimpleFloat3(float[][][] a) {
    _n1 = a[0][0].length;
    _n2 = a[0].length;
    _n3 = a.length;
    _a = a;
  }

  /**
   * Gets the number of elements in 1st dimension of this array.
   * @return the number of elements in 1st dimension.
   */
  public int getN1() {
    return _n1;
  }

  /**
   * Gets the number of elements in 2nd dimension of this array.
   * @return the number of elements in 2nd dimension.
   */
  public int getN2() {
    return _n2;
  }

  /**
   * Gets the number of elements in 3rd dimension of this array.
   * @return the number of elements in 3rd dimension.
   */
  public int getN3() {
    return _n3;
  }

  /**
   * Gets the specified subarray of elements into the specified array.
   * The length of the array must be at least m1*m2*m3.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m1*m2*m3] of elements of the specified subarray.
   */
  public void get(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[] s)
  {
    if (m1==1) {
      if (m2==1) {
        for (int i3=0,is=0; i3<m3; ++i3) {
          s[is++] = _a[i3+j3][j2][j1];
        }
      } else {
        for (int i3=0,is=0; i3<m3; ++i3) {
          float[][] a3 = _a[i3+j3];
          for (int i2=0; i2<m2; ++i2) {
            s[is++] = a3[i2+j2][j1];
          }
        }
      }
    } else {
      for (int i3=0,is=0; i3<m3; ++i3) {
        float[][] a3 = _a[i3+j3];
        for (int i2=0; i2<m2; ++i2) {
          float[] a32 = a3[i2+j2];
          for (int i1=0; i1<m1; ++i1) {
            s[is++] = a32[i1+j1];
          }
        }
      }
    }
  }

  /**
   * Gets the specified subarray of elements into the specified array.
   * At least one of the dimensions m1, m2, or m3 must equal 1.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[ma][mb] of elements of the specified subarray.
   *  If m1==1, then ma&gt;=m3 and mb&gt;m2; 
   *  else if m2==1, then ma&gt;=m3 and mb&gt;=m1;
   *  else (m3==1), then ma&gt;=m2 and mb&gt;=m1.
   */
  public void get(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[][] s)
  {
    Check.argument(m1==1 || m2==1 || m3==1,
                  "m1==1 || m2==1 || m3==1");
    if (m1==1) {
      for (int i3=0; i3<m3; ++i3) {
        float[][] a3 = _a[i3+j3];
        float[] s3 = s[i3];
        for (int i2=0; i2<m2; ++i2) {
          s3[i2] = a3[i2+j2][j1];
        }
      }
    } else if (m2==1) {
      for (int i3=0; i3<m3; ++i3) {
        float[] a32 = _a[i3+j3][j2];
        float[] s3 = s[i3];
        for (int i1=0; i1<m1; ++i1) {
          s3[i1] = a32[i1+j1];
        }
      }
    } else {
      for (int i2=0; i2<m2; ++i2) {
        float[] a32 = _a[j3][i2+j2];
        float[] s2 = s[i2];
        for (int i1=0; i1<m1; ++i1) {
          s2[i1] = a32[i1+j1];
        }
      }
    }
  }

  /**
   * Gets the specified subarray of elements into the specified array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3][m2][m1] of elements of the specified subarray.
   */
  public void get(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[][][] s) 
  {
    for (int i3=0; i3<m3; ++i3) {
      float[][] a3 = _a[i3+j3];
      float[][] s3 = s[i3];
      for (int i2=0; i2<m2; ++i2) {
        float[] a32 = a3[i2+j2];
        float[] s32 = s3[i2];
        for (int i1=0; i1<m1; ++i1) {
          s32[i1] = a32[i1+j1];
        }
      }
    }
  }

  /**
   * Sets the specified subarray of elements from the specified array.
   * The length of the array must be at least m1*m2*m3.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m1*m2*m3] of elements of the specified subarray.
   */
  public void set(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[] s)
  {
    if (m1==1) {
      if (m2==1) {
        for (int i3=0,is=0; i3<m3; ++i3) {
          _a[i3+j3][j2][j1] = s[is++];
        }
      } else {
        for (int i3=0,is=0; i3<m3; ++i3) {
          float[][] a3 = _a[i3+j3];
          for (int i2=0; i2<m2; ++i2) {
            a3[i2+j2][j1] = s[is++];
          }
        }
      }
    } else {
      for (int i3=0,is=0; i3<m3; ++i3) {
        float[][] a3 = _a[i3+j3];
        for (int i2=0; i2<m2; ++i2) {
          float[] a32 = a3[i2+j2];
          for (int i1=0; i1<m1; ++i1) {
            a32[i1+j1] = s[is++];
          }
        }
      }
    }
  }

  /**
   * Sets the specified subarray of elements from the specified array.
   * At least one of the dimensions m1, m2, or m3 must equal 1.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[ma][mb] of elements of the specified subarray.
   *  If m1==1, then ma&gt;=m3 and mb&gt;m2; 
   *  else if m2==1, then ma&gt;=m3 and mb&gt;=m1;
   *  else (m3==1), then ma&gt;=m2 and mb&gt;=m1.
   */
  public void set(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[][] s)
  {
    Check.argument(m1==1 || m2==1 || m3==1,
                  "m1==1 || m2==1 || m3==1");
    if (m1==1) {
      for (int i3=0; i3<m3; ++i3) {
        float[][] a3 = _a[i3+j3];
        float[] s3 = s[i3];
        for (int i2=0; i2<m2; ++i2) {
          a3[i2+j2][j1] = s3[i2];
        }
      }
    } else if (m2==1) {
      for (int i3=0; i3<m3; ++i3) {
        float[] a32 = _a[i3+j3][j2];
        float[] s3 = s[i3];
        for (int i1=0; i1<m1; ++i1) {
          a32[i1+j1] = s3[i1];
        }
      }
    } else {
      for (int i2=0; i2<m2; ++i2) {
        float[] a32 = _a[j3][i2+j2];
        float[] s2 = s[i2];
        for (int i1=0; i1<m1; ++i1) {
          a32[i1+j1] = s2[i1];
        }
      }
    }
  }

  /**
   * Sets the specified subarray of elements from the specified array.
   * @param m1 number of elements in 1st dimension of subarray.
   * @param m2 number of elements in 2nd dimension of subarray.
   * @param m3 number of elements in 3rd dimension of subarray.
   * @param j1 index of first element in 1st dimension of subarray.
   * @param j2 index of first element in 2nd dimension of subarray.
   * @param j3 index of first element in 3rd dimension of subarray.
   * @param s array[m3][m2][m1] of elements of the specified subarray.
   */
  public void set(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[][][] s) 
  {
    for (int i3=0; i3<m3; ++i3) {
      float[][] a3 = _a[i3+j3];
      float[][] s3 = s[i3];
      for (int i2=0; i2<m2; ++i2) {
        float[] a32 = a3[i2+j2];
        float[] s32 = s3[i2];
        for (int i1=0; i1<m1; ++i1) {
          a32[i1+j1] = s32[i1];
        }
      }
    }
  }
  
  ///////////////////////////////////////////////////////////////////////////
  // private
  private int _n1,_n2,_n3;
  private float[][][] _a;
}
