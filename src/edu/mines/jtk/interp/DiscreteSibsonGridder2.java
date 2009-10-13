/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.Sampling;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * A discrete approximation of Sibson's natural neighbor interpolation.
 * Given a set of known samples of a function f(x1,x2) for scattered points 
 * x1 and x2, this approximation computes an interpolating function g(x1,x2) 
 * with specified uniform samplings of x1 and x2. The function g(x1,x2)
 * approximates the natural neighbor interpolant proposed by Sibson (1981).
 * <p>
 * In this approximation, all scattered points x1 and x2 are rounded to the 
 * nearest uniformly sampled points. If two or more known samples fall into 
 * a single uniform sample bin, their values f(x1,x2) are averaged to obtain 
 * the value g(x1,x2) for that bin. Values of g(x1,x2) for all other bins are 
 * computed to approximate natural neighbor interpolation.
 * <p>
 * The primary goal of this implementation is simplicity. Like the method 
 * of Park et al. (2006), it requires no Delaunay triangulation or Voronoi 
 * tesselation, and its cost decreases as the number of known samples 
 * increases. Moreover, unlike the method of Park et al., this method uses 
 * no auxilary data structure such as a k-d tree to find nearest known 
 * samples. Computational complexity of this method is within a constant 
 * factor of that of Park et al.
 * <p>
 * Discrete implementations of Sibson's interpolation can produce artifacts
 * (small axis-aligned ridges or valleys) caused by sampling circles on a 
 * rectangular grid. To attenuate these artifacts, this method applies some 
 * number of Gauss-Seidel iterations of bi-Laplacian smoothing to the 
 * interpolated samples, without modifying the known samples.
 * <pre>
 * References: 
 * Park, S.W., L. Linsen, O. Kreylos, J.D. Owens, B. Hamann, 2006,
 * Discrete Sibson interpolation: IEEE Transactions on Visualization 
 * and Computer Graphics,
 * v. 12, 243-253.
 * Sibson, R., 1981, A brief description of natural neighbor interpolation,
 * in V. Barnett, ed., Interpreting Multivariate Data: John Wiley and Sons,
 * 21-36.
 * </pre>
 * @author Dave Hale, Colorado School of Mines 
 * @version 2009.10.10
 */
public class DiscreteSibsonGridder2 implements Gridder2 {

  /**
   * Constructs a nearest neighbor gridder with specified known samples.
   * The specified arrays are copied; not referenced.
   * @param f array of known sample values f(x1,x2).
   * @param x1 array of known sample x1 coordinates.
   * @param x2 array of known sample x2 coordinates.
   */
  public DiscreteSibsonGridder2(float[] f, float[] x1, float[] x2) {
    setScattered(f,x1,x2);
  }

  /**
   * Sets the number of bi-Laplacian smoothing iterations.
   * If non-zero, these iterations are performed at the end of discrete 
   * Sibson interpolation, and does not alter known sample values. This
   * smoothing attenuates high-frequency artifacts caused by approximating 
   * circles on a uniform sampling grid. However, smoothing iterations may 
   * also create unwanted oscillations in gridded values. The default 
   * number of smoothing iterations is zero.
   * @param nsmooth number of smoothing iterations.
   */
  public void setSmooth(int nsmooth) {
    _nsmooth = nsmooth;
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
    float fx1 = (float)s1.getFirst();
    float fx2 = (float)s2.getFirst();
    float lx1 = (float)s1.getLast();
    float lx2 = (float)s2.getLast();
    float dx1 = (float)s1.getDelta();
    float dx2 = (float)s2.getDelta();
    float od1 = 1.0f/dx1;
    float od2 = 1.0f/dx2;

    // Sample offsets, sorted by increasing distance. These offsets are
    // used in expanding-circle searches for nearest known samples.
    // We tabulate offsets for only one quadrant of a circle, because 
    // offsets for the other three quadrants can be found by symmetry.
    int nk = n1*n2;
    int[] kk = new int[nk];
    float[] ds = new float[nk];
    for (int i2=0,k=0; i2<n2; ++i2) {
      double x2 = i2*dx2;
      for (int i1=0; i1<n1; ++i1,++k) {
        double x1 = i1*dx1;
        ds[k] = (float)(x1*x1+x2*x2);
        kk[k] = k;
      }
    }
    quickIndexSort(ds,kk);
    ds = null;
    int[] k1 = new int[nk];
    int[] k2 = new int[nk];
    for (int k=0; k<nk; ++k) {
      int ik = kk[k];
      int i1 = ik%n1;
      int i2 = ik/n1;
      k1[k] = i1;
      k2[k] = i2;
    }
    kk = null;

    // Accumulate known samples into bins, counting the number in each bin.
    float[][] g = new float[n2][n1];
    float[][] c = new float[n2][n1];
    for (int i=0; i<_n; ++i) {
      float x1i = _x1[i];
      float x2i = _x2[i];
      if (x1i<fx1 || x1i>lx1) continue; // skip scattered values
      if (x2i<fx2 || x2i>lx2) continue; // that fall out of bounds
      int i1 = (int)(0.5f+(x1i-fx1)*od1);
      int i2 = (int)(0.5f+(x2i-fx2)*od2);
      c[i2][i1] += 1.0f; // count known values accumulated
      g[i2][i1] += _f[i]; // accumulate known values
    }

    // Average where more than one known sample per bin.
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        if (c[i2][i1]>0.0f) {
          g[i2][i1] /= c[i2][i1]; // normalize sum of known sample values
          c[i2][i1] = -c[i2][i1]; // negative counts denote known samples
        }
      }
    }

    // For all uniform sample bins (centers of scattering circles), ...
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {

        // Which bins are inside circle extending to nearest known sample?
        // Determine the function value for that nearest known sample.
        int kn = -1;
        float fn = 0.0f;
        for (int k=0; k<nk && kn<0; ++k) {
          int k1k = k1[k];
          int k2k = k2[k];
          for (int m2=0,j2=i2-k2k; m2<2; ++m2,j2=i2+k2k) {
            if (j2<0 || j2>=n2) continue;
            for (int m1=0,j1=i1-k1k; m1<2; ++m1,j1=i1+k1k) {
              if (j1<0 || j1>=n1) continue;
              if (c[j2][j1]<0.0f) { // if sample is known, ...
                kn = k;
                fn = g[j2][j1];
              }
            }
          }
        }

        // By symmetry, each pair of offset indices (k1,k2) actually 
        // represents four uniformly sampled points. In a rectangular 
        // sampling grid, either four or eight such points lie equidistant 
        // from the origin. If eight offsets, then four of them may 
        // immediately follow the four that we found in the search 
        // above. Here we include these next four, if necessary.
        if (kn<nk-1 && k1[kn]==k2[kn+1] && k2[kn]==k1[kn+1])
          ++kn;

        // Scatter the nearest function value into all bins inside the circle.
        for (int k=0; k<=kn; ++k) {
          int k1k = k1[k];
          int k2k = k2[k];
          for (int m2=0,j2=i2-k2k; m2<2; ++m2,j2=i2+k2k) {
            if (j2<0 || j2>=n2) continue;
            for (int m1=0,j1=i1-k1k; m1<2; ++m1,j1=i1+k1k) {
              if (j1<0 || j1>=n1) continue;
              if (c[j2][j1]>=0.0f) { // if sample is unknown, ...
                g[j2][j1] += fn;
                c[j2][j1] += 1.0f;
              }
            }
          }
        }
      }
    }

    // Normalize accumulated values by the number scattered into each bin.
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        if (c[i2][i1]>0.0f)
          g[i2][i1] /= c[i2][i1];
      }
    }

    // Optional Gauss-Seidel iterations of bi-Laplacian smoothing to
    // attenuate artifacts in discrete Sibson interpolation.
    int n1m = n1-1;
    int n2m = n2-1;
    float a1 =  8.0f/20.0f;
    float a2 = -2.0f/20.0f;
    float a3 = -1.0f/20.0f;
    for (int jsmooth=0; jsmooth<_nsmooth; ++jsmooth) {
      for (int i2=0; i2<n2; ++i2) {
        int i2m = (i2==0  )?i2:i2-1;
        int i2p = (i2==n2m)?i2:i2+1;
        int i2mm = (i2m==0  )?i2m:i2m-1;
        int i2pp = (i2p==n2m)?i2p:i2p+1;
        for (int i1=0; i1<n1; ++i1) {
          int i1m = (i1==0  )?i1:i1-1;
          int i1p = (i1==n1m)?i1:i1+1;
          int i1mm = (i1m==0  )?i1m:i1m-1;
          int i1pp = (i1p==n1m)?i1p:i1p+1;
          if (c[i2][i1]>0.0f) {
            float g1 = a1*(g[i2 ][i1m]+g[i2 ][i1p]+g[i2m][i1 ]+g[i2p][i1 ]);
            float g2 = a2*(g[i2m][i1m]+g[i2m][i1p]+g[i2p][i1m]+g[i2p][i1p]);
            float g3 = a3*(g[i2][i1mm]+g[i2][i1pp]+g[i2mm][i1]+g[i2pp][i1]);
            g[i2][i1] = g1+g2+g3;
          }
        }
      }
    }

    return g;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n;
  private float[] _f,_x1,_x2;
  private int _nsmooth;
}
