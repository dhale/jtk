/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.Sampling;

/**
 * Simple griding of scattered samples of 3D functions f(x1,x2,x3).
 * Each gridded value is simply the average of the values of all known 
 * samples that lie within the corresponding grid cell. For a grid cell
 * that contains no such known samples, the gridded value is null.
 * <p>
 * Note that this simple method performs no interpolation for grid cells 
 * that do not contain at least one scattered sample. It may however be 
 * used as a first step in other more sophisticated gridding methods. In
 * particular, the averaging performed by this simple gridder provides a 
 * crude form of anti-alias filtering when grid cells contain multiple
 * scattered samples.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.22
 */
public class SimpleGridder3 implements Gridder3 {

  /**
   * Constructs a simple gridder with specified known samples.
   * The specified arrays are referenced, not copied.
   * @param f array of known sample values f(x1,x2,x3).
   * @param x1 array of known sample x1 coordinates.
   * @param x2 array of known sample x2 coordinates.
   * @param x3 array of known sample x3 coordinates.
   */
  public SimpleGridder3(float[] f, float[] x1, float[] x2, float[] x3) {
    setScattered(f,x1,x2,x3);
  }

  /**
   * Sets the null value used for grid cells that contain no known samples.
   * @param fnull the null value.
   */
  public void setNullValue(float fnull) {
    _fnull = fnull;
  }

  /**
   * Gets the non-null samples from the specified gridded sample values.
   * @param fnull the null value.
   * @param s1 sampling of x1.
   * @param s2 sampling of x2.
   * @param s3 sampling of x3.
   * @param g array of gridded sample values.
   * @return array {f,x1,x2,x3} of arrays of non-null samples.
   */
  public static float[][] getGriddedSamples(
    float fnull, Sampling s1, Sampling s2, Sampling s3, float[][][] g) 
  {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s3.getCount();
    int n = 0;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          if (g[i3][i2][i1]!=fnull)
            ++n;
        }
      }
    }
    float[] f = new float[n];
    float[] x1 = new float[n];
    float[] x2 = new float[n];
    float[] x3 = new float[n];
    for (int i3=0,i=0; i3<n3; ++i3) {
      float x3i = (float)s3.getValue(i3);
      for (int i2=0; i2<n2; ++i2) {
        float x2i = (float)s2.getValue(i2);
        for (int i1=0; i1<n1; ++i1) {
          if (g[i3][i2][i1]!=fnull) {
            float x1i = (float)s1.getValue(i1);
            x1[i] = x1i;
            x2[i] = x2i;
            x3[i] = x3i;
            f[i] = g[i3][i2][i1];
            ++i;
          }
        }
      }
    }
    return new float[][]{f,x1,x2,x3};
  }

  ///////////////////////////////////////////////////////////////////////////
  // interface Gridder3

  public void setScattered(float[] f, float[] x1, float[] x2, float[] x3) {
    _n = f.length;
    _f = f;
    _x1 = x1;
    _x2 = x2;
    _x3 = x3;
  }

  public float[][][] grid(Sampling s1, Sampling s2, Sampling s3) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s3.getCount();
    double d1 = s1.getDelta();
    double d2 = s2.getDelta();
    double d3 = s3.getDelta();
    double f1 = s1.getFirst();
    double f2 = s2.getFirst();
    double f3 = s3.getFirst();
    double l1 = s1.getLast();
    double l2 = s2.getLast();
    double l3 = s3.getLast();
    f1 -= 0.5*d1; l1 += 0.5*d1;
    f2 -= 0.5*d2; l2 += 0.5*d2;
    f3 -= 0.5*d3; l3 += 0.5*d3;
    float[][][] g = new float[n3][n2][n1];
    float[][][] c = new float[n3][n2][n1];
    for (int i=0; i<_n; ++i) {
      double x1 = _x1[i];
      double x2 = _x2[i];
      double x3 = _x3[i];
      if (f1<=x1 && x1<=l1 && f2<=x2 && x2<=l2 && f3<=x3 && x3<=l3) {
        int i1 = s1.indexOfNearest(x1);
        int i2 = s2.indexOfNearest(x2);
        int i3 = s3.indexOfNearest(x3);
        g[i3][i2][i1] += _f[i];
        c[i3][i2][i1] += 1.0f;
      }
    }
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          g[i3][i2][i1] = (c[i3][i2][i1]>0.0f) ?
            g[i3][i2][i1]/c[i3][i2][i1] :
            _fnull;
        }
      }
    }
    return g;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n;
  private float _fnull;
  private float[] _f,_x1,_x2,_x3;
}
