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
 * A simple 3-D array of floats. Implements the generic interface 
 * {@link edu.mines.jtk.util.Float3} using a straightforward Java 3-D
 * array of floats.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.05.24
 */
public class SimpleFloat3 implements Float3 {

  /**
   * Constructs an array of elements initialized to zero.
   * @param n1 the 1st dimension of the array[n3][n2][n1].
   * @param n2 the 2nd dimension of the array[n3][n2][n1].
   * @param n3 the 3rd dimension of the array[n3][n2][n1].
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
   * @param a array[n3][n2][n1] of elements to be referenced.
   */
  public SimpleFloat3(float[][][] a) {
    _n1 = a[0][0].length;
    _n2 = a[0].length;
    _n3 = a.length;
    _a = a;
  }

  ///////////////////////////////////////////////////////////////////////////
  // interface Float3

  public int getN1() {
    return _n1;
  }

  public int getN2() {
    return _n2;
  }

  public int getN3() {
    return _n3;
  }

  public void get1(int m1, int j1, int j2, int j3, float[] s) {
    float[] a32 = _a[j3][j2];
    for (int i1=0; i1<m1; ++i1) {
      s[i1] = a32[i1+j1];
    }
  }

  public void get2(int m2, int j1, int j2, int j3, float[] s) {
    float[][] a3 = _a[j3];
    for (int i2=0; i2<m2; ++i2) {
      s[i2] = a3[i2+j2][j1];
    }
  }

  public void get3(int m3, int j1, int j2, int j3, float[] s) {
    for (int i3=0; i3<m3; ++i3) {
      s[i3] = _a[i3+j3][j2][j1];
    }
  }

  public void get12(int m1, int m2, int j1, int j2, int j3, float[][] s) {
    for (int i2=0; i2<m2; ++i2) {
      float[] a32 = _a[j3][i2+j2];
      float[] s2 = s[i2];
      for (int i1=0; i1<m1; ++i1) {
        s2[i1] = a32[i1+j1];
      }
    }
  }

  public void get13(int m1, int m3, int j1, int j2, int j3, float[][] s) {
    for (int i3=0; i3<m3; ++i3) {
      float[] a32 = _a[i3+j3][j2];
      float[] s3 = s[i3];
      for (int i1=0; i1<m1; ++i1) {
        s3[i1] = a32[i1+j1];
      }
    }
  }

  public void get23(int m2, int m3, int j1, int j2, int j3, float[][] s) {
    for (int i3=0; i3<m3; ++i3) {
      float[][] a3 = _a[i3+j3];
      float[] s3 = s[i3];
      for (int i2=0; i2<m2; ++i2) {
        s3[i2] = a3[i2+j2][j1];
      }
    }
  }

  public void get123(
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

  public void get123(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[] s)
  {
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

  public void set1(int m1, int j1, int j2, int j3, float[] s) {
    float[] a32 = _a[j3][j2];
    for (int i1=0; i1<m1; ++i1) {
      a32[i1+j1] = s[i1];
    }
  }

  public void set2(int m2, int j1, int j2, int j3, float[] s) {
    float[][] a3 = _a[j3];
    for (int i2=0; i2<m2; ++i2) {
      a3[i2+j2][j1] = s[i2];
    }
  }

  public void set3(int m3, int j1, int j2, int j3, float[] s) {
    for (int i3=0; i3<m3; ++i3) {
      _a[i3+j3][j2][j1] = s[i3];
    }
  }

  public void set12(int m1, int m2, int j1, int j2, int j3, float[][] s) {
    for (int i2=0; i2<m2; ++i2) {
      float[] a32 = _a[j3][i2+j2];
      float[] s2 = s[i2];
      for (int i1=0; i1<m1; ++i1) {
        a32[i1+j1] = s2[i1];
      }
    }
  }

  public void set13(int m1, int m3, int j1, int j2, int j3, float[][] s) {
    for (int i3=0; i3<m3; ++i3) {
      float[] a32 = _a[i3+j3][j2];
      float[] s3 = s[i3];
      for (int i1=0; i1<m1; ++i1) {
        a32[i1+j1] = s3[i1];
      }
    }
  }

  public void set23(int m2, int m3, int j1, int j2, int j3, float[][] s) {
    for (int i3=0; i3<m3; ++i3) {
      float[][] a3 = _a[i3+j3];
      float[] s3 = s[i3];
      for (int i2=0; i2<m2; ++i2) {
        a3[i2+j2][j1] = s3[i2];
      }
    }
  }

  public void set123(
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

  public void set123(
    int m1, int m2, int m3, 
    int j1, int j2, int j3, 
    float[] s)
  {
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
  
  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n1,_n2,_n3;
  private float[][][] _a;
}
