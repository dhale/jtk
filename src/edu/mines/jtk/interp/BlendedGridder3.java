/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tensor-guided blended neighbor gridding in 3D.
 * Gridding is interpolation of a set of known sample values on a 
 * uniformly sampled grid. Here the interpolation is performed by 
 * a two-step blended neighbor process described by Hale (2009).
 * <p>
 * The first step is to compute for all samples the distance to the 
 * nearest known sample and the value of that known sample. This first
 * step produces a distance map and a nearest-neighbor interpolant.
 * <p>
 * The second step is to blend (smooth) the nearest-neighbor interpolant,
 * where the extent of smoothing varies spatially and is proportional to 
 * distances in the distance map.
 * <p>
 * In tensor-guided gridding, we replace distance with time. Time is a
 * simple term for non-Euclidean distance measured in a metric-tensor
 * field. So "nearest" now means nearest in time. In the first step we 
 * compute a time map by solving an eikonal equation with coefficients 
 * that may be both anisotropic and spatially varying. In the second 
 * step, we blend the nearest-neighbor interpolant with an anisotropic 
 * and spatially varying smoothing filter.
 * <p>
 * The default tensor field is homogeneous and isotropic. In this
 * special case, time is equivalent to distance, and tensor-guided
 * gridding is similar to gridding with Sibson's natural neighbor 
 * interpolant.
 * <p>
 * Reference: 
 * <a href="http://www.mines.edu/papers/Hale09ImageGuidedBlendedNeighborInterpolation.pdf">
 * Hale, D., 2009, Image-guided blended neighbor interpolation, CWP-634</a>
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.21
 */
public class BlendedGridder3 implements Gridder3 {

  /**
   * Constructs a gridder for default tensors.
   */
  public BlendedGridder3() {
    this(null);
  }

  /**
   * Constructs a gridder for default tensors and specified samples.
   * The specified arrays are referenced; not copied.
   * @param f array of sample values f(x1,x2,x3).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public BlendedGridder3(float[] f, float[] x1, float[] x2, float[] x3) {
    this(null);
    setScattered(f,x1,x2,x3);
  }

  /**
   * Constructs a gridder for the specified tensors.
   * @param tensors the tensors.
   */
  public BlendedGridder3(Tensors3 tensors) {
    setTensors(tensors);
  }

  /**
   * Constructs a gridder for the specified tensors and samples.
   * The specified arrays are referenced; not copied.
   * @param tensors the tensors.
   * @param f array of sample values f(x1,x2,x3).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public BlendedGridder3(
    Tensors3 tensors,
    float[] f, float[] x1, float[] x2, float[] x3) 
  {
    setTensors(tensors);
    setScattered(f,x1,x2,x3);
  }

  /**
   * Sets the tensor field used by this gridder.
   * The default is a homogeneous and isotropic tensor field.
   * @param tensors the tensors; null for default tensors.
   */
  public void setTensors(Tensors3 tensors) {
    _tensors = tensors;
    if (_tensors==null) {
      _tensors = new Tensors3() {
        public void getTensor(int i1, int i2, int i3, float[] d) {
          d[0] = 1.0f;
          d[1] = 0.0f;
          d[2] = 0.0f;
          d[3] = 1.0f;
          d[4] = 0.0f;
          d[5] = 1.0f;
        }
      };
    }
  }

  /**
   * Enables or disables blending in {@link #grid(Sampling,Sampling,Sampling)}.
   * If true (the default), that method will perform both of the two
   * steps described; that is, it will blend (smooth) after computing
   * the nearest neighbor interpolant. If false, that method perform
   * only the first step and return the nearest neighbor interpolant.
   * @param blending true, for blending; false, otherwise.
   */
  public void setBlending(boolean blending) {
    _blending = blending;
  }

  /**
   * Computes gridded values using nearest neighbors.
   * Gridded values in the array p are computed for only unknown 
   * samples denoted by corresponding non-zero times in the array t. 
   * This method does not change known values in p, which correspond
   * to zero times in t.
   * @param t array of times to nearest known samples.
   * @param p array of nearest-neighbor gridded values.
   */
  public void gridNearest(float[][][] t, float[][][] p) {
    int n1 = t[0][0].length;
    int n2 = t[0].length;
    int n3 = t.length;

    // Count the known samples, the number of marks we need.
    int nmark = 0;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          if (t[i3][i2][i1]==0.0f)
            ++nmark;
        }
      }
    }

    // Make an array for marks, while storing values of known samples
    // in an array of values indexed by the mark.
    float[] pmark = new float[nmark];
    int[][][] m = new int[n3][n2][n1];
    int mark = 0;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          if (t[i3][i2][i1]==0.0f) {
            pmark[mark] = p[i3][i2][i1];
            m[i3][i2][i1] = mark;
            ++mark;
          }
        }
      }
    }

    // Use the time marker to compute both times and marks.
    TimeMarker3 tm = new TimeMarker3(n1,n2,n3,_tensors);
    tm.apply(t,m);

    // Use the marks to compute the nearest-neighbor interpolant.
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          if (t[i3][i2][i1]!=0.0f)
            p[i3][i2][i1] = pmark[m[i3][i2][i1]];
        }
      }
    }
  }

  /**
   * Computes gridded values using blended neighbors. 
   * Note that blended-neighbor gridding can be performed only 
   * after nearest-neighbor gridding. Blending does not change
   * the values of known samples for which times are zero.
   * @param t array of times to nearest known samples.
   * @param p array of nearest-neighbor gridded values.
   * @param q array of blended-neighbor gridded values.
   */
  public void gridBlended(float[][][] t, float[][][] p, float[][][] q) {
    int n1 = t[0][0].length;
    int n2 = t[0].length;
    int n3 = t.length;

    // Compute time squared, shifted to account for the shift in the
    // finite-difference stencil usd in the local smoothing filter.
    float[][][] s = mul(t,t);
    for (int i3=n3-1; i3>0; --i3) {
      for (int i2=n2-1; i2>0; --i2) {
        for (int i1=n1-1; i1>0; --i1) {
          s[i3][i2][i1] = 0.125f*(s[i3  ][i2  ][i1  ] +
                                  s[i3  ][i2  ][i1-1] +
                                  s[i3  ][i2-1][i1  ] +
                                  s[i3  ][i2-1][i1-1] +
                                  s[i3-1][i2  ][i1  ] +
                                  s[i3-1][i2  ][i1-1] +
                                  s[i3-1][i2-1][i1  ] +
                                  s[i3-1][i2-1][i1-1]);
        }
      }
    }

    // Construct and apply a local smoothing filter.
    float c = 0.5f; // constant for linear precision
    LocalSmoothingFilter lsf = new LocalSmoothingFilter(0.01,10000);
    lsf.apply(_tensors,c,s,p,q);

    // Restore the known sample values. Due to errors in finite-difference
    // approximations, these values may have changed during smoothing.
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          if (t[i3][i2][i1]==0.0f) {
            q[i3][i2][i1] = p[i3][i2][i1];
          }
        }
      }
    }
  }


  ///////////////////////////////////////////////////////////////////////////
  // interface Gridder3

  public void setScattered(float[] f, float[] x1, float[] x2, float[] x3) {
    _f = f;
    _x1 = x1;
    _x2 = x2;
    _x3 = x3;
  }

  public float[][][] grid(Sampling s1, Sampling s2, Sampling s3) {
    Check.argument(s1.isUniform(),"s1 is uniform");
    Check.argument(s2.isUniform(),"s2 is uniform");
    Check.argument(s3.isUniform(),"s3 is uniform");
    Check.state(_f!=null,"scattered samples have been set");
    Check.state(_x1!=null,"scattered samples have been set"); 
    Check.state(_x2!=null,"scattered samples have been set");
    Check.state(_x3!=null,"scattered samples have been set");
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s3.getCount();
    SimpleGridder3 sg = new SimpleGridder3(_f,_x1,_x2,_x3);
    float pnull = -FLT_MAX;
    float tnull = -FLT_MAX;
    sg.setNullValue(pnull);
    float[][][] p = sg.grid(s1,s2,s3);
    sg = null;
    float[][][] t = new float[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          t[i3][i2][i1] = (p[i3][i2][i1]!=pnull)?0.0f:tnull;
        }
      }
    }
    gridNearest(t,p);
    float[][][] q = p;
    if (_blending) {
      q = new float[n3][n2][n1];
      gridBlended(t,p,q);
    }
    return q;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Tensors3 _tensors;
  private float[] _f,_x1,_x2,_x3;
  private boolean _blending = true;
}
