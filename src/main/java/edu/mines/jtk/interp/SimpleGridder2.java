/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.Sampling;

/**
 * Simple griding of scattered samples of 2D functions f(x1,x2).
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
public class SimpleGridder2 implements Gridder2 {

  /**
   * Constructs a simple gridder with specified known samples.
   * The specified arrays are referenced, not copied.
   * @param f array of known sample values f(x1,x2).
   * @param x1 array of known sample x1 coordinates.
   * @param x2 array of known sample x2 coordinates.
   */
  public SimpleGridder2(float[] f, float[] x1, float[] x2) {
    setScattered(f,x1,x2);
  }

  /**
   * Sets the null value used for grid cells that contain no known samples.
   * The default null value is zero.
   * @param fnull the null value.
   */
  public void setNullValue(float fnull) {
    _fnull = fnull;
  }

  /**
   * Returns samples adjusted to lie on a specified grid.
   * @param s1 sampling of x1.
   * @param s2 sampling of x2.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @return array {f,x1,x2} of arrays of samples after gridding.
   */
  public static float[][] samplesOnGrid(
    Sampling s1, Sampling s2, float[] f, float[] x1, float[] x2)
  {
    float[][] fxs = samplesOnGrid(s1,s2,f,x1,x2,null);
    return new float[][]{fxs[0],fxs[1],fxs[2]};
  }

  /**
   * Returns samples adjusted to lie on a specified grid.
   * Sample values may have uncertainties, specified as standard
   * deviations of errors assumed to be uncorrelated and to have
   * zero mean.
   * @param s1 sampling of x1.
   * @param s2 sampling of x2.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param sd array of std dev for sample values; null, if none.
   * @return array {f,x1,x2,sd} of arrays of samples after gridding.
   */
  public static float[][] samplesOnGrid(
    Sampling s1, Sampling s2, float[] f, float[] x1, float[] x2, float[] sd)
  {
    int n = f.length;
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    double d1 = s1.getDelta();
    double d2 = s2.getDelta();
    double f1 = s1.getFirst();
    double f2 = s2.getFirst();
    double l1 = s1.getLast();
    double l2 = s2.getLast();
    f1 -= 0.5*d1; l1 += 0.5*d1;
    f2 -= 0.5*d2; l2 += 0.5*d2;

    // If errors, determine which samples, if any, have zero error.
    boolean[][] ez = null;
    if (sd!=null) {
      ez = new boolean[n2][n1];
      for (int i=0; i<n; ++i) {
        if (sd[i]==0.0f) {
          double x1i = x1[i];
          double x2i = x2[i];
          if (f1<=x1i && x1i<=l1 && f2<=x2i && x2i<=l2) {
            int i1 = s1.indexOfNearest(x1i);
            int i2 = s2.indexOfNearest(x2i);
            ez[i2][i1] = true;
          }
        }
      }
    }

    // Accumulate weighted sample values and weights.
    float[][] g = new float[n2][n1];
    float[][] w = new float[n2][n1];
    for (int i=0; i<n; ++i) {
      double x1i = x1[i];
      double x2i = x2[i];
      if (f1<=x1i && x1i<=l1 && f2<=x2i && x2i<=l2) {
        int i1 = s1.indexOfNearest(x1i);
        int i2 = s2.indexOfNearest(x2i);
        if (sd==null || ez[i2][i1] && sd[i]==0.0f) {
          g[i2][i1] += f[i];
          w[i2][i1] += 1.0f;
        } else if (!ez[i2][i1]) {
          float wi = 1.0f/(sd[i]*sd[i]); // weight = 1/variance
          g[i2][i1] += wi*f[i];
          w[i2][i1] += wi;
        }
      }
    }

    // Count the number of samples after gridding.
    n = 0;
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        if (w[i2][i1]>0.0f)
          ++n;
      }
    }

    // Complete computation of gridded samples.
    f = new float[n];
    x1 = new float[n];
    x2 = new float[n];
    if (sd!=null)
      sd = new float[n];
    for (int i2=0,i=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        if (w[i2][i1]>0.0f) {
          f[i] = g[i2][i1]/w[i2][i1];
          x1[i] = (float)s1.getValue(i1);
          x2[i] = (float)s2.getValue(i2);
          if (sd!=null)
            sd[i] = ez[i2][i1]?0.0f:1.0f/(float)Math.sqrt(w[i2][i1]);
          ++i;
        }
      }
    }
    return new float[][]{f,x1,x2,sd};
  }

  /**
   * Gets the non-null samples from the specified gridded sample values.
   * @param fnull the null value.
   * @param s1 sampling of x1.
   * @param s2 sampling of x2.
   * @param g array of gridded sample values.
   * @return array {f,x1,x2} of arrays of non-null samples.
   */
  public static float[][] getGriddedSamples(
    float fnull, Sampling s1, Sampling s2, float[][] g) 
  {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n = 0;
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        if (g[i2][i1]!=fnull)
          ++n;
      }
    }
    float[] f = new float[n];
    float[] x1 = new float[n];
    float[] x2 = new float[n];
    for (int i2=0,i=0; i2<n2; ++i2) {
      float x2i = (float)s2.getValue(i2);
      for (int i1=0; i1<n1; ++i1) {
        if (g[i2][i1]!=fnull) {
          float x1i = (float)s1.getValue(i1);
          x1[i] = x1i;
          x2[i] = x2i;
          f[i] = g[i2][i1];
          ++i;
        }
      }
    }
    return new float[][]{f,x1,x2};
  }

  ///////////////////////////////////////////////////////////////////////////
  // interface Gridder2

  public void setScattered(float[] f, float[] x1, float[] x2) {
    _n = f.length;
    _f = f;
    _x1 = x1;
    _x2 = x2;
  }

  public float[][] grid(Sampling s1, Sampling s2) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    double d1 = s1.getDelta();
    double d2 = s2.getDelta();
    double f1 = s1.getFirst();
    double f2 = s2.getFirst();
    double l1 = s1.getLast();
    double l2 = s2.getLast();
    f1 -= 0.5*d1; l1 += 0.5*d1;
    f2 -= 0.5*d2; l2 += 0.5*d2;
    float[][] g = new float[n2][n1];
    float[][] c = new float[n2][n1];
    for (int i=0; i<_n; ++i) {
      double x1 = _x1[i];
      double x2 = _x2[i];
      if (f1<=x1 && x1<=l1 && f2<=x2 && x2<=l2) {
        int i1 = s1.indexOfNearest(x1);
        int i2 = s2.indexOfNearest(x2);
        g[i2][i1] += _f[i];
        c[i2][i1] += 1.0f;
      }
    }
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        g[i2][i1] = (c[i2][i1]>0.0f)?g[i2][i1]/c[i2][i1]:_fnull;
      }
    }
    return g;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n;
  private float _fnull;
  private float[] _f,_x1,_x2;
}
