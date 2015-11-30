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
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Computes the convolution (or cross-correlation) of two sequences.
 * <p>
 * Convolution of one-dimensional sequences x and y is defined generically 
 * by the following sum:
 * <pre><code>
 *   z[i] =  sum x[j]*y[i-j]
 *            j
 * </code></pre>
 * In practice, the sequences x, y, and z are non-zero for only finite 
 * ranges of sample indices i and j, and these ranges determine limits 
 * on the summation index j.
 * <p>
 * Specifically, the sequences x, y, and z are stored in arrays with 
 * zero-based indexing; e.g., x[0], x[1], x[2], ..., x[lx-1], where lx
 * denotes the length of the array x. Sequences are assumed to be zero
 * for indices outside the bounds of these arrays.
 * <p>
 * Note that an array index need not equal its corresponding sample index. 
 * For each sequence, we must specify the sample index of the first sample 
 * in the array of sample values; e.g., kx denotes the sample index of x[0]. 
 * With this distinction between sample and array indices in mind, in terms 
 * of arrays x, y, and z, the convolution sum may be rewritten as
 * <pre><code>
 *             jhi
 *   z[i-k] =  sum  x[j]*y[i-j] ; i = k, k+1, ..., k+lz-1
 *             j=jlo
 * </code></pre>
 * where k = kz-kx-ky, jlo = max(0,i-ly+1), and jhi = min(lx-1,i). The 
 * summation limits jlo and jhi ensure that array indices are always in 
 * bounds. The effect of the three first-sample indices is encoded in the 
 * single shift k.
 * <p>
 * For example, if sequence z is to be a weighted average of the nearest 
 * five samples of sequence y, one might use 
 * <pre><code>
 *   ...
 *   x[0] = x[1] = x[2] = x[3] = x[4] = 1.0/5.0;
 *   conv(5,-2,x,ly,0,y,ly,0,z);
 *   ...
 * </code></pre>
 * In this example, the sequence x is symmetric about the origin, with 
 * first-sample index kx = -2.
 * <p>
 * Cross-correlation is similar to convolution. (Indeed, cross-correlation
 * of x and y equals the convolution of x-reversed and y. The generic 
 * definition of cross-correlation is
 * <pre><code>
 *   z[i] =  sum x[j]*y[i+j]
 *            j
 * </code></pre>
 * Unlike convolution, cross-correlation is not commutative. In other words,
 * the cross-correlation of x and y does not equal the cross-correlation of 
 * y and x.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.08.15
 */
public class Conv {

  /**
   * Computes the 1-D convolution of specified sequences x and y.
   * @param lx the length of x.
   * @param kx the sample index of x[0].
   * @param x array[lx] of x values.
   * @param ly the length of y.
   * @param ky the sample index of y[0].
   * @param y array[ly] of y values.
   * @param lz the length of z.
   * @param kz the sample index of z[0].
   * @param z array[lz] of z values.
   */
  public static void conv(
    int lx, int kx, float[] x,
    int ly, int ky, float[] y,
    int lz, int kz, float[] z)
  {
    convFast(lx,kx,x,ly,ky,y,lz,kz,z);
  }

  /**
   * Computes the 2-D convolution of specified sequences x and y.
   * @param lx1 the length of x in 1st dimension.
   * @param lx2 the length of x in 2nd dimension.
   * @param kx1 the sample index in 1st dimension of x[0][0].
   * @param kx2 the sample index in 2nd dimension of x[0][0].
   * @param x array[lx2][lx1] of x values.
   * @param ly1 the length of y in 1st dimension.
   * @param ly2 the length of y in 2nd dimension.
   * @param ky1 the sample index in 1st dimension of y[0][0].
   * @param ky2 the sample index in 2nd dimension of y[0][0].
   * @param y array[ly2][ly1] of y values.
   * @param lz1 the length of z in 1st dimension.
   * @param lz2 the length of z in 2nd dimension.
   * @param kz1 the sample index in 1st dimension of z[0][0].
   * @param kz2 the sample index in 2nd dimension of z[0][0].
   * @param z array[lz2][lz1] of z values.
   */
  public static void conv(
    int lx1, int lx2, int kx1, int kx2, float[][] x,
    int ly1, int ly2, int ky1, int ky2, float[][] y,
    int lz1, int lz2, int kz1, int kz2, float[][] z)
  {
    zero(lz1,lz2,z);
    int ilo2 = kz2-kx2-ky2;
    int ihi2 = ilo2+lz2-1;
    for (int i2=ilo2; i2<=ihi2; ++i2) {
      int jlo2 = max(0,i2-ly2+1);
      int jhi2 = min(lx2-1,i2);
      for (int j2=jlo2; j2<=jhi2; ++j2) {
        convSum(lx1,kx1,x[j2],ly1,ky1,y[i2-j2],lz1,kz1,z[i2-ilo2]);
      }
    }
  }

  /**
   * Computes the 3-D convolution of specified sequences x and y.
   * @param lx1 the length of x in 1st dimension.
   * @param lx2 the length of x in 2nd dimension.
   * @param lx3 the length of x in 3rd dimension.
   * @param kx1 the sample index in 1st dimension of x[0][0][0].
   * @param kx2 the sample index in 2nd dimension of x[0][0][0].
   * @param kx3 the sample index in 3rd dimension of x[0][0][0].
   * @param x array[lx3][lx2][lx1] of x values.
   * @param ly1 the length of y in 1st dimension.
   * @param ly2 the length of y in 2nd dimension.
   * @param ly3 the length of y in 3rd dimension.
   * @param ky1 the sample index in 1st dimension of y[0][0][0].
   * @param ky2 the sample index in 2nd dimension of y[0][0][0].
   * @param ky3 the sample index in 3rd dimension of y[0][0][0].
   * @param y array[ly3][ly2][ly1] of y values.
   * @param lz1 the length of z in 1st dimension.
   * @param lz2 the length of z in 2nd dimension.
   * @param lz3 the length of z in 3rd dimension.
   * @param kz1 the sample index in 1st dimension of z[0][0][0].
   * @param kz2 the sample index in 2nd dimension of z[0][0][0].
   * @param kz3 the sample index in 3rd dimension of z[0][0][0].
   * @param z array[lz3][lz2][lz1] of z values.
   */
  public static void conv(
    int lx1, int lx2, int lx3, int kx1, int kx2, int kx3, float[][][] x,
    int ly1, int ly2, int ly3, int ky1, int ky2, int ky3, float[][][] y,
    int lz1, int lz2, int lz3, int kz1, int kz2, int kz3, float[][][] z)
  {
    zero(lz1,lz2,lz3,z);
    int ilo2 = kz2-kx2-ky2;
    int ilo3 = kz3-kx3-ky3;
    int ihi2 = ilo2+lz2-1;
    int ihi3 = ilo3+lz3-1;
    for (int i3=ilo3; i3<=ihi3; ++i3) {
      int jlo3 = max(0,i3-ly3+1);
      int jhi3 = min(lx3-1,i3);
      for (int j3=jlo3; j3<=jhi3; ++j3) {
        for (int i2=ilo2; i2<=ihi2; ++i2) {
          int jlo2 = max(0,i2-ly2+1);
          int jhi2 = min(lx2-1,i2);
          for (int j2=jlo2; j2<=jhi2; ++j2) {
            convSum(lx1,kx1,x[j3][j2],
                    ly1,ky1,y[i3-j3][i2-j2],
                    lz1,kz1,z[i3-ilo3][i2-ilo2]);
          }
        }
      }
    }
  }

  /**
   * Computes the 1-D cross-correlation of specified sequences x and y.
   * @param lx the length of x.
   * @param kx the sample index of x[0].
   * @param x array[lx] of x values.
   * @param ly the length of y.
   * @param ky the sample index of y[0].
   * @param y array[ly] of y values.
   * @param lz the length of z.
   * @param kz the sample index of z[0].
   * @param z array[lz] of z values.
   */
  public static void xcor(
    int lx, int kx, float[] x,
    int ly, int ky, float[] y,
    int lz, int kz, float[] z)
  {
    boolean copy = x==y;
    x = reverse(lx,x,copy);
    kx = 1-kx-lx;
    conv(lx,kx,x,ly,ky,y,lz,kz,z);
    if (!copy)
      reverse(lx,x,false);
  }

  /**
   * Computes the 2-D cross-correlation of specified sequences x and y.
   * @param lx1 the length of x in 1st dimension.
   * @param lx2 the length of x in 2nd dimension.
   * @param kx1 the sample index in 1st dimension of x[0][0].
   * @param kx2 the sample index in 2nd dimension of x[0][0].
   * @param x array[lx2][lx1] of x values.
   * @param ly1 the length of y in 1st dimension.
   * @param ly2 the length of y in 2nd dimension.
   * @param ky1 the sample index in 1st dimension of y[0][0].
   * @param ky2 the sample index in 2nd dimension of y[0][0].
   * @param y array[ly2][ly1] of y values.
   * @param lz1 the length of z in 1st dimension.
   * @param lz2 the length of z in 2nd dimension.
   * @param kz1 the sample index in 1st dimension of z[0][0].
   * @param kz2 the sample index in 2nd dimension of z[0][0].
   * @param z array[lz2][lz1] of z values.
   */
  public static void xcor(
    int lx1, int lx2, int kx1, int kx2, float[][] x,
    int ly1, int ly2, int ky1, int ky2, float[][] y,
    int lz1, int lz2, int kz1, int kz2, float[][] z)
  {
    boolean copy = x==y;
    x = reverse(lx1,lx2,x,copy);
    kx1 = 1-kx1-lx1;
    kx2 = 1-kx2-lx2;
    conv(lx1,lx2,kx1,kx2,x,ly1,ly2,ky1,ky2,y,lz1,lz2,kz1,kz2,z);
    if (!copy)
      reverse(lx1,lx2,x,false);
  }

  /**
   * Computes the 3-D cross-correlation of specified sequences x and y.
   * @param lx1 the length of x in 1st dimension.
   * @param lx2 the length of x in 2nd dimension.
   * @param lx3 the length of x in 3rd dimension.
   * @param kx1 the sample index in 1st dimension of x[0][0][0].
   * @param kx2 the sample index in 2nd dimension of x[0][0][0].
   * @param kx3 the sample index in 3rd dimension of x[0][0][0].
   * @param x array[lx3][lx2][lx1] of x values.
   * @param ly1 the length of y in 1st dimension.
   * @param ly2 the length of y in 2nd dimension.
   * @param ly3 the length of y in 3rd dimension.
   * @param ky1 the sample index in 1st dimension of y[0][0][0].
   * @param ky2 the sample index in 2nd dimension of y[0][0][0].
   * @param ky3 the sample index in 3rd dimension of y[0][0][0].
   * @param y array[ly3][ly2][ly1] of y values.
   * @param lz1 the length of z in 1st dimension.
   * @param lz2 the length of z in 2nd dimension.
   * @param lz3 the length of z in 3rd dimension.
   * @param kz1 the sample index in 1st dimension of z[0][0][0].
   * @param kz2 the sample index in 2nd dimension of z[0][0][0].
   * @param kz3 the sample index in 3rd dimension of z[0][0][0].
   * @param z array[lz3][lz2][lz1] of z values.
   */
  public static void xcor(
    int lx1, int lx2, int lx3, int kx1, int kx2, int kx3, float[][][] x,
    int ly1, int ly2, int ly3, int ky1, int ky2, int ky3, float[][][] y,
    int lz1, int lz2, int lz3, int kz1, int kz2, int kz3, float[][][] z)
  {
    boolean copy = x==y;
    x = reverse(lx1,lx2,lx3,x,copy);
    kx1 = 1-kx1-lx1;
    kx2 = 1-kx2-lx2;
    kx3 = 1-kx3-lx3;
    conv(lx1,lx2,lx3,kx1,kx2,kx3,x,
         ly1,ly2,ly3,ky1,ky2,ky3,y,
         lz1,lz2,lz3,kz1,kz2,kz3,z);
    if (!copy)
      reverse(lx1,lx2,lx3,x,false);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  ///////////////////////////////////////////////////////////////////////////
  // Convolution with only (slightly more than) one load per multiply-add. 
  // Simpler and slower alternatives to this method require at least two 
  // loads from memory per multiply-add; here is an example:
  // 
  // int ilo = kz-kx-ky;
  // int ihi = ilo+lz-1;
  // for (int i=ilo; i<=ihi; ++i) {
  //   int jlo = max(0,i-ly+1);
  //   int jhi = min(lx-1,i);
  //   float sum = 0.0f;
  //   for (int j=jlo; j<=jhi; ++j)
  //     sum += x[j]*y[i-j];
  //   z[i-ilo] = sum;
  // }
  //
  // The code fragment above performs the same operations, but in a
  // different order and with different rounding errors, as this more
  // complicated and efficient method.
  // 
  // This method computes output samples z in up to five stages: (1) off 
  // left, (2) rolling on, (3) middle, (4) rolling off, and (5) off right. 
  // In stages 1 and 5, there is no overlap between the sequences x and y, 
  // and the corresponding output samples z are zero. In stage 2, only 
  // the first part of the sequence x overlaps the sequence y, and in 
  // stage 4, only the last part of the sequence x overlaps the sequence 
  // y. In the middle stage 3, the sequence x lies entirely within the 
  // sequence y. Stages 2 and 4 with partial overlap are most complex.
  // 
  // The description above assumes that the input sequence x is shorter 
  // than the input sequence y. If this is not true, we first swap x and 
  // y so that x is shorter.
  // 
  // Note that not all five stages may be necessary to compute the output 
  // sequence z. For example, it is possible for only stage 1 to apply,
  // in which case all output samples are zero.
  // 
  // In stages 2, 3, and 4, output samples z are computed in pairs (a,b)
  // so that, in inner loops, only one load is required per multiply-add.
  // In stage 4, iteration over indices i and j is backwards; stage 4 is 
  // like a mirror image of stage 2.
  // 
  // Here is an example, for lx = 6 and ly = 7. Only stages 2, 3, and 4 
  // are illustrated. Output samples are computed for a contiguous range 
  // of indices i, but that range may be any subset of the range [0:11] 
  // illustrated here.
  //   r   s   i  y0  y1  y2  y3  y4  y5  y6
  //  --  --  --  --  --  --  --  --  --  --
  //   a   @   0  x0
  //   b   @   1  x1  x0
  //   a   @   2  x2  x1  x0
  //   b   @   3  x3  x2  x1  x0
  //   a   @   4  x4  x3  x2  x1  x0
  //   a   #   5  x5  x4  x3  x2  x1  x0
  //   b   #   6      x5  x4  x3  x2  x1  x0
  //   a   %   7          x5  x4  x3  x2  x1
  //   b   %   8              x5  x4  x3  x2
  //   a   %   9                  x5  x4  x3
  //   b   %  10                      x5  x4
  //   a   %  11                          x5
  //
  //  r - register (a or b)
  //  s - stage (2, 3, or 4):
  //    @ - rolling on  :  0 <= i <=  4
  //    # - middle      :  5 <= i <=  6
  //    % - rolling off :  7 <= i <= 11
  ///////////////////////////////////////////////////////////////////////////
  private static void convFast(
    int lx, int kx, float[] x,
    int ly, int ky, float[] y,
    int lz, int kz, float[] z)
  {
    // If necessary, swap x and y so that x is the shorter sequence.
    // This simplifies the logic below.
    if (lx>ly) {
      int lt = lx;  lx = ly;  ly = lt;
      int kt = kx;  kx = ky;  ky = kt;
      float[] t = x;  x = y;  y = t;
    }

    // Bounds for index i.
    int imin = kz-kx-ky;
    int imax = imin+lz-1;

    // Variables that we expect to reside in registers.
    int i,ilo,ihi,j,jlo,jhi,iz;
    float sa,sb,xa,xb,ya,yb;

    // Off left: imin <= i <= -1
    ilo = imin;
    ihi = min(-1,imax);
    for (i=ilo,iz=i-imin; i<=ihi; ++i,++iz)
      z[iz] = 0.0f;

    // Rolling on: 0 <= i <= lx-2 and 0 <= j <= i
    ilo = max(0,imin);
    ihi = min(lx-2,imax);
    jlo = 0;
    jhi = ilo;
    for (i=ilo,iz=i-imin; i<ihi; i+=2,iz+=2,jhi+=2) {
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
      z[iz  ] = sa;
      z[iz+1] = sb;
    }
    if (i==ihi) {
      jlo = 0;
      jhi = i;
      sa = 0.0f;
      for (j=jlo; j<=jhi; ++j)
        sa += x[j]*y[i-j];
      z[iz] = sa;
    }

    // Middle: lx-1 <= i <= ly-1 and 0 <= j <= lx-1
    ilo = max(lx-1,imin);
    ihi = min(ly-1,imax);
    jlo = 0;
    jhi = lx-1;
    for (i=ilo,iz=i-imin; i<ihi; i+=2,iz+=2) {
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
      z[iz  ] = sa;
      z[iz+1] = sb;
    }
    if (i==ihi) {
      sa = 0.0f;
      for (j=jlo; j<=jhi; ++j)
        sa += x[j]*y[i-j];
      z[iz] = sa;
    }

    // Rolling off: ly <= i <= lx+ly-2 and i-ly+1 <= j <= lx-1
    ilo = max(ly,imin);
    ihi = min(lx+ly-2,imax);
    jlo = ihi-ly+1;
    jhi = lx-1;
    for (i=ihi,iz=i-imin; i>ilo; i-=2,iz-=2,jlo-=2) {
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
      z[iz  ] = sa;
      z[iz-1] = sb;
    }
    if (i==ilo) {
      jlo = i-ly+1;
      jhi = lx-1;
      sa = 0.0f;
      for (j=jhi; j>=jlo; --j)
    	sa += x[j]*y[i-j];
      z[iz] = sa;
    }
	
    // Off right: lx+ly-1 <= i <= imax
    ilo = max(lx+ly-1,imin);
    ihi = imax;
    for (i=ilo,iz=i-imin; i<=ihi; ++i,++iz)
      z[iz] = 0.0f;
  }

  // Like convFast above, but accumulates the convolution sum, as in 
  // z += x*y, ,where "*" denotes convolution. This method is used in 
  // convolution of 2-D and 3-D sequences.
  private static void convSum(
    int lx, int kx, float[] x,
    int ly, int ky, float[] y,
    int lz, int kz, float[] z)
  {
    // If necessary, swap x and y so that x is the shorter sequence.
    // This simplifies the logic below.
    if (lx>ly) {
      int lt = lx;  lx = ly;  ly = lt;
      int kt = kx;  kx = ky;  ky = kt;
      float[] t = x;  x = y;  y = t;
    }

    // Bounds for index i.
    int imin = kz-kx-ky;
    int imax = imin+lz-1;

    // Variables that we expect to reside in registers.
    int i,ilo,ihi,j,jlo,jhi,iz;
    float sa,sb,xa,xb,ya,yb;

    // Rolling on: 0 <= i <= lx-2 and 0 <= j <= i
    ilo = max(0,imin);
    ihi = min(lx-2,imax);
    jlo = 0;
    jhi = ilo;
    for (i=ilo,iz=i-imin; i<ihi; i+=2,iz+=2,jhi+=2) {
      sa = z[iz  ];
      sb = z[iz+1];
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
      z[iz  ] = sa;
      z[iz+1] = sb;
    }
    if (i==ihi) {
      jlo = 0;
      jhi = i;
      sa = z[iz];
      for (j=jlo; j<=jhi; ++j)
        sa += x[j]*y[i-j];
      z[iz] = sa;
    }

    // Middle: lx-1 <= i <= ly-1 and 0 <= j <= lx-1
    ilo = max(lx-1,imin);
    ihi = min(ly-1,imax);
    jlo = 0;
    jhi = lx-1;
    for (i=ilo,iz=i-imin; i<ihi; i+=2,iz+=2) {
      sa = z[iz  ];
      sb = z[iz+1];
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
      z[iz  ] = sa;
      z[iz+1] = sb;
    }
    if (i==ihi) {
      sa = z[iz];
      for (j=jlo; j<=jhi; ++j)
        sa += x[j]*y[i-j];
      z[iz] = sa;
    }

    // Rolling off: ly <= i <= lx+ly-2 and i-ly+1 <= j <= lx-1
    ilo = max(ly,imin);
    ihi = min(lx+ly-2,imax);
    jlo = ihi-ly+1;
    jhi = lx-1;
    for (i=ihi,iz=i-imin; i>ilo; i-=2,iz-=2,jlo-=2) {
      sa = z[iz  ];
      sb = z[iz-1];
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
      z[iz  ] = sa;
      z[iz-1] = sb;
    }
    if (i==ilo) {
      jlo = i-ly+1;
      jhi = lx-1;
      sa = z[iz];
      for (j=jhi; j>=jlo; --j)
    	sa += x[j]*y[i-j];
      z[iz] = sa;
    }
  }

  private static void zero(int n1, float[] z) {
    for (int i1=0; i1<n1; ++i1)
      z[i1] = 0.0f;
  }

  private static void zero(int n1, int n2, float[][] z) {
    for (int i2=0; i2<n2; ++i2)
      zero(n1,z[i2]);
  }

  private static void zero(int n1, int n2, int n3, float[][][] z) {
    for (int i3=0; i3<n3; ++i3)
      zero(n1,n2,z[i3]);
  }

  private static float[] reverse(int n1, float[] z, boolean copy) {
    if (copy) 
      z = copy(n1,z);
    for (int i1=0,j1=n1-1; i1<j1; ++i1,--j1) {
      float zt = z[i1];
      z[i1] = z[j1];
      z[j1] = zt;
    }
    return z;
  }

  private static float[][] reverse(int n1, int n2, float[][] z, boolean copy) {
    if (copy) 
      z = copy(n1,n2,z);
    for (int i2=0,j2=n2-1; i2<j2; ++i2,--j2) {
      float[] zt = z[i2];
      z[i2] = z[j2];
      z[j2] = zt;
    }
    for (int i2=0; i2<n2; ++i2)
      reverse(n1,z[i2],false);
    return z;
  }

  private static float[][][] reverse(
    int n1, int n2, int n3, float[][][] z, boolean copy) 
  {
    if (copy) 
      z = copy(n1,n2,n3,z);
    for (int i3=0,j3=n3-1; i3<j3; ++i3,--j3) {
      float[][] zt = z[i3];
      z[i3] = z[j3];
      z[j3] = zt;
    }
    for (int i3=0; i3<n3; ++i3)
      reverse(n1,n2,z[i3],false);
    return z;
  }
}
