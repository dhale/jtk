/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import static java.lang.Math.*;

/**
 * Computes the convolution or cross-correlation of two sequences.
 * Convolution of one-dimensional sequences x and y is defined generically 
 * by the following sum:
 * <pre><code>
 *   z[i] =  sum x[j]*y[i-j] ,
 *            j
 * </code></pre>
 * In practice, the sequences x, y, and z are non-zero for only finite 
 * ranges of sample indices i and j, and these ranges determine limits 
 * on the summation.
 * <p>
 * Specifically, the sequences x, y, and z are stored in arrays with 
 * zero-based indexing; e.g., x[0], x[1], x[2], ..., x[lx-1], where lx
 * denotes the length of the array x. Sequences are assumed to be zero
 * for indices outside the bounds of these arrays.
 * <p>
 * Note that an array index need not equal its corresponding sample index. 
 * For each sequence, we must specify the sample index of the first sample 
 * in the array of sample values; e.g., ifx denotes the sample index of x[0]. 
 * In terms of arrays x, y, and z, the convolution sum may be rewritten as
 * <pre><code>
 *             jhi
 *   z[i-k] =  sum  x[j]*y[i-j] ; i = k, k+1, ..., k+lz-1
 *             j=jlo
 * </code></pre>
 * where <code>k = ifz-ifx-ify</code>, <code>jlo = max(0,i-ly+1)</code>,
 * and <code>jhi = min(lx-1,i)</code>. The summation limits <code>jlo</code>
 * and <code>jhi</code> ensure that array indices are always in bounds. The
 * effect of the three first sample indices is encoded in the single shift 
 * <code>k</code>.
 * <p>
 * For example, if sequence z is to be a weighted average of the nearest 
 * five samples of sequence y, one might use 
 * <pre><code>
 *   ...
 *   x[0] = x[1] = x[2] = x[3] = x[4] = 1.0/5.0;
 *   conv(5,-2,x,ly,0,y,ly,0,z);
 *   ...
 * </code></pre>
 * In this example, the sequence x is symmetric, with index of first sample 
 * ifx = -2.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.08.15
 */
public class Conv {

  /**
   * Computes the convolution of two specified sequences x and y.
   * @param lx the length of x.
   * @param ifx the sample index of x[0].
   * @param x array[lx] of x values.
   * @param ly the length of y.
   * @param ify the sample index of y[0].
   * @param y array[ly] of y values.
   * @param lz the length of z.
   * @param ifz the sample index of z[0].
   * @param z array[lz] of z values.
   */
  public static void conv(
    int lx, int ifx, float[] x,
    int ly, int ify, float[] y,
    int lz, int ifz, float[] z)
  {
    convFast(lx,ifx,x,ly,ify,y,lz,ifz,z);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void convFast(
    int lx, int ifx, float[] x,
    int ly, int ify, float[] y,
    int lz, int ifz, float[] z)
  {
    // If necessary, swap x and y so that x is the shorter sequence.
    // This simplifies the logic below.
    if (lx>ly) {
      int lt = lx;  lx = ly;  ly = lt;
      int ift = ifx;  ifx = ify;  ify = ift;
      float[] t = x;  x = y;  y = t;
    }

    // Output samples z are computed in up to five stages: (1) off left, 
    // (2) rolling on, (3) middle, (4) rolling off, and (5) off right. In 
    // stages 1 and 5, there is no overlap between the sequences x and y, 
    // and the corresponding output samples z are zero. In stage 2, only 
    // the first part of the sequence x overlaps the sequence y, and in 
    // stage 4, only the last part of the sequence x overlaps the sequence 
    // y. In the middle stage 3, the sequence x lies entirely within the 
    // sequence y. Stages 2 and 4 with partial overlap are most complex.
    // 
    // Note that not all five stages are necessary to compute the output
    // sequence z. For example, it is possible for only stage 1 to apply,
    // in which case all output samples are zero.
    // 
    // In stages 2, 3, and 4, output samples z are computed in pairs (a,b)
    // so that, in inner loops, only one load is required per multiply-add.
    // Note that iteration over indices i and j in stage 4 is backwards, as
    // this stage is like a mirror image of stage 2.
    // 
    // Here is an example, with lx = 6, ly = 7, and lz = 12. Only stages
    // 2, 3, and 4 are illustrated. Output samples are computed for a
    // contiguous range of indices i, but that range may be any subset of
    // the range [0:11] illustrated here.
    //
    //      i  y0  y1  y2  y3  y4  y5  y6
    //     --  --  --  --  --  --  --  --
    //  @   0  x0
    //  @   1  x1  x0
    //  @   2  x2  x1  x0
    //  @   3  x3  x2  x1  x0
    //  @   4  x4  x3  x2  x1  x0
    //  #   5  x5  x4  x3  x2  x1  x0
    //  #   6      x5  x4  x3  x2  x1  x0
    //  %   7          x5  x4  x3  x2  x1
    //  %   8              x5  x4  x3  x2
    //  %   9                  x5  x4  x3
    //  %  10                      x5  x4
    //  %  11                          x5
    //
    //  @ - rolling on  :  0 <= i <=  4
    //  # - middle      :  5 <= i <=  6
    //  % - rolling off :  7 <= i <= 11
    int imin = ifz-ifx-ify;
    int imax = imin+lz-1;
    int i,ilo,ihi;
    int j,jlo,jhi;
    float sa,sb,xa,xb,ya,yb;

    // OFF LEFT: imin <= i <= -1
    ilo = imin;
    ihi = min(-1,imax);
    for (i=ilo; i<=ihi; ++i)
      z[i-imin] = 0.0f;

    // ROLLING ON: 0 <= i <= lx-2 and 0 <= j <= i
    ilo = max(0,imin);
    ihi = min(lx-2,imax);
    jlo = 0;
    jhi = ilo;
    for (i=ilo; i<ihi; i+=2,jhi+=2) {
      sa = 0.0f;
      sb = 0.0f;
      yb = y[i-jlo+1];
      for (j=jlo; j<jhi; j+=2) {
        xa = x[j];
        sb += xa*yb;
        ya = y[i-j];
        sa += xa*ya;
        xb = x[j+1];
        sb += xb*ya;
        yb = y[i-j-1];
        sa += xb*yb;
      }
      xa = x[j];
      sb += xa*yb;
      if (j==jhi) {
        ya = y[i-j];
        sa += xa*ya;
        xb = x[j+1];
        sb += xb*ya;
      }
      z[i-imin  ] = sa;
      z[i-imin+1] = sb;
    }
    if (i==ihi) {
      jlo = 0;
      jhi = i;
      sa = 0.0f;
      for (j=jlo; j<=jhi; ++j)
        sa += x[j]*y[i-j];
      z[i-imin] = sa;
    }

    // MIDDLE: lx-1 <= i <= ly-1 and 0 <= j <= lx-1
    ilo = max(lx-1,imin);
    ihi = min(ly-1,imax);
    jlo = 0;
    jhi = lx-1;
    for (i=ilo; i<ihi; i+=2) {
      sa = 0.0f;
      sb = 0.0f;
      yb = y[i-jlo+1];
      for (j=jlo; j<jhi; j+=2) {
        xa = x[j];
        sb += xa*yb;
        ya = y[i-j];
        sa += xa*ya;
        xb = x[j+1];
        sb += xb*ya;
        yb = y[i-j-1];
        sa += xb*yb;
      }
      if (j==jhi) {
        xa = x[j];
        sb += xa*yb;
        ya = y[i-j];
        sa += xa*ya;
      }
      z[i-imin  ] = sa;
      z[i-imin+1] = sb;
    }
    if (i==ihi) {
      sa = 0.0f;
      for (j=jlo; j<=jhi; ++j)
        sa += x[j]*y[i-j];
      z[i-imin] = sa;
    }

    // ROLLING OFF: ly <= i <= lx+ly-2 and i-ly+1 <= j <= lx-1
    ilo = max(ly,imin);
    ihi = min(lx+ly-2,imax);
    jlo = ihi-ly+1;
    jhi = lx-1;
    for (i=ihi; i>ilo; i-=2,jlo-=2) {
      sa = 0.0f;
      sb = 0.0f;
      yb = y[i-jhi-1];
      for (j=jhi; j>jlo; j-=2) {
        xa = x[j];
        sb += xa*yb;
        ya = y[i-j];
        sa += xa*ya;
        xb = x[j-1];
        sb += xb*ya;
        yb = y[i-j+1];
        sa += xb*yb;
      }
      xa = x[j];
      sb += xa*yb;
      if (j==jlo) {
        ya = y[i-j];
        sa += xa*ya;
        xb = x[j-1];
        sb += xb*ya;
      }
      z[i-imin  ] = sa;
      z[i-imin-1] = sb;
    }
    if (i==ilo) {
      jlo = i-ly+1;
      jhi = lx-1;
      sa = 0.0f;
      for (j=jhi; j>=jlo; --j)
    	sa += x[j]*y[i-j];
      z[i-imin] = sa;
    }
	
    // OFF RIGHT: lx+ly-1 <= i <= imax
    ilo = max(lx+ly-1,imin);
    ihi = imax;
    for (i=ilo; i<=ihi; ++i)
      z[i-imin] = 0.0f;
  }
}
