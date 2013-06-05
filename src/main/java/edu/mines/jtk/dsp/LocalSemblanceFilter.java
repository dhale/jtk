/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Computes local semblance images using local smoothing filters.
 * Local semblance (Hale, 2009) is defined to be a squared smoothed-image
 * divided by a smoothed squared-image, where smoothing is performed by local
 * smoothing filters along the eigenvectors of a structure tensor field.
 * <p>
 * Reference: 
 * <a
 * href="http://www.mines.edu/~dhale/papers/Hale09StructureOrientedSmoothingAndSemblance.pdf">
 * Hale, D., 2009, Structure-oriented smoothing and semblance, CWP-635</a>
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.24
 */
public class LocalSemblanceFilter {

  /**
   * 2D smoothing directions correspond to eigenvectors of tensors.
   * The direction U corresponds to the largest eigenvalue.
   * The direction V corresponds to the smallest eigenvalue.
   */
  public enum Direction2 {
    U,V,UV
  }

  /**
   * 3D smoothing directions correspond to eigenvectors of tensors.
   * The direction U corresponds to the largest eigenvalue.
   * The direction W corresponds to the smallest eigenvalue.
   */
  public enum Direction3 {
    U,V,W,UV,UW,VW,UVW
  }

  /**
   * Constructs a local semblance filter.
   * @param halfWidth1 half-width of 1st smoothing filter.
   * @param halfWidth2 half-width of 2nd smoothing filter.
   */
  public LocalSemblanceFilter(int halfWidth1, int halfWidth2) {
    _smoother1 = new LaplacianSmoother(halfWidth1);
    _smoother2 = new LaplacianSmoother(halfWidth2);
  }

  /**
   * Computes local semblance for a 1D array.
   * @param f the array of input values.
   * @param s the array of output semblance values.
   */
  public void semblance(float[] f, float[] s) {
    int n1 = f.length;
    float[] sn,sd;
    sn = smooth1(f);
    sn = mul(sn,sn);
    sn = smooth2(sn);
    sd = mul(f,f);
    sd = smooth1(sd);
    sd = smooth2(sd);
    for (int i1=0; i1<n1; ++i1) {
      float sni = sn[i1];
      float sdi = sd[i1];
      if (sdi<=0.0f || sni<0.0f) {
        s[i1] = 0.0f;
      } else if (sdi<sni) {
        s[i1] = 1.0f;
      } else {
        s[i1] = sni/sdi;
      }
    }
  }

  /**
   * Returns local semblance for a 1D array.
   * @param f the array of input values.
   * @return the array of semblance values.
   */
  public float[] semblance(float[] f) {
    float[] s = like(f);
    semblance(f,s);
    return s;
  }

  /**
   * Computes local semblance for a 2D array.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the array of input values.
   * @param s the array of output semblance values.
   */
  public void semblance(
    Direction2 d, EigenTensors2 t, float[][] f, float[][] s) 
  {
    int n1 = f[0].length;
    int n2 = f.length;
    float[][] sn,sd;
    sn = smooth1(d,t,f);
    sn = mul(sn,sn);
    sn = smooth2(d,t,sn);
    sd = mul(f,f);
    sd = smooth1(d,t,sd);
    sd = smooth2(d,t,sd);
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        float sni = sn[i2][i1];
        float sdi = sd[i2][i1];
        if (sdi<=0.0f || sni<0.0f) {
          s[i2][i1] = 0.0f;
        } else if (sdi<sni) {
          s[i2][i1] = 1.0f;
        } else {
          s[i2][i1] = sni/sdi;
        }
      }
    }
  }

  /**
   * Returns local semblance for a 2D array.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the array of input values.
   * @return the array of semblance values.
   */
  public float[][] semblance(Direction2 d, EigenTensors2 t, float[][] f) {
    float[][] s = like(f);
    semblance(d,t,f,s);
    return s;
  }

  /**
   * Computes local semblance for a 3D array.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the array of input values.
   * @param s the array of output semblance values.
   */
  public void semblance(
    Direction3 d, EigenTensors3 t, float[][][] f, float[][][] s) 
  {
    int n1 = f[0][0].length;
    int n2 = f[0].length;
    int n3 = f.length;
    float[][][] sn,sd;
    sn = smooth1(d,t,f);
    sn = mul(sn,sn);
    sn = smooth2(d,t,sn);
    sd = mul(f,f);
    sd = smooth1(d,t,sd);
    sd = smooth2(d,t,sd);
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float sni = sn[i3][i2][i1];
          float sdi = sd[i3][i2][i1];
          if (sdi<=0.0f || sni<0.0f) {
            s[i3][i2][i1] = 0.0f;
          } else if (sdi<sni) {
            s[i3][i2][i1] = 1.0f;
          } else {
            s[i3][i2][i1] = sni/sdi;
          }
        }
      }
    }
  }

  /**
   * Returns local semblance for a 3D array.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the array of input values.
   * @return the array of semblance values.
   */
  public float[][][] semblance(Direction3 d, EigenTensors3 t, float[][][] f) {
    float[][][] s = like(f);
    semblance(d,t,f,s);
    return s;
  }

  /**
   * Applies the 1st inner smoothing of this semblance filter.
   * @param f the input array.
   * @param g the output array.
   */
  public void smooth1(float[] f, float[] g) {
    _smoother1.apply(f,g);
  }

  /**
   * Applies the 1st inner smoothing of this semblance filter.
   * @param f the input array.
   * @return the output array.
   */
  public float[] smooth1(float[] f) {
    float[] g = like(f);
    smooth1(f,g);
    return g;
  }

  /**
   * Applies the 1st inner smoothing of this semblance filter.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the input array.
   * @param g the output array.
   */
  public void smooth1(
    Direction2 d, EigenTensors2 t, float[][] f, float[][] g) 
  {
    _smoother1.apply(d,t,f,g);
  }

  /**
   * Applies the 1st inner smoothing of this semblance filter.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the input array.
   * @return the output array.
   */
  public float[][] smooth1(Direction2 d, EigenTensors2 t, float[][] f) {
    float[][] g = like(f);
    smooth1(d,t,f,g);
    return g;
  }

  /**
   * Applies the 1st inner smoothing of this semblance filter.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the input array.
   * @param g the output array.
   */
  public void smooth1(
    Direction3 d, EigenTensors3 t, float[][][] f, float[][][] g) 
  {
    _smoother1.apply(d,t,f,g);
  }

  /**
   * Applies the 1st inner smoothing of this semblance filter.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the input array.
   * @return the output array.
   */
  public float[][][] smooth1(Direction3 d, EigenTensors3 t, float[][][] f) {
    float[][][] g = like(f);
    smooth1(d,t,f,g);
    return g;
  }

  /**
   * Applies the 2nd outer smoothing of this semblance filter.
   * @param f the input array.
   * @param g the output array.
   */
  public void smooth2(float[] f, float[] g) {
    _smoother2.apply(f,g);
  }

  /**
   * Applies the 2nd outer smoothing of this semblance filter.
   * @param f the input array.
   * @return the output array.
   */
  public float[] smooth2(float[] f) {
    float[] g = like(f);
    smooth2(f,g);
    return g;
  }

  /**
   * Applies the 2nd outer smoothing of this semblance filter.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the input array.
   * @param g the output array.
   */
  public void smooth2(
    Direction2 d, EigenTensors2 t, float[][] f, float[][] g) 
  {
    _smoother2.apply(d,t,f,g);
  }

  /**
   * Applies the 2nd outer smoothing of this semblance filter.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the input array.
   * @return the output array.
   */
  public float[][] smooth2(Direction2 d, EigenTensors2 t, float[][] f) {
    float[][] g = like(f);
    smooth2(orthogonal(d),t,f,g);
    return g;
  }

  /**
   * Applies the 2nd outer smoothing of this semblance filter.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the input array.
   * @param g the output array.
   */
  public void smooth2(
    Direction3 d, EigenTensors3 t, float[][][] f, float[][][] g) 
  {
    _smoother2.apply(d,t,f,g);
  }

  /**
   * Applies the 2nd outer smoothing of this semblance filter.
   * @param d direction(s) for the first inner smoothing.
   * @param t eigen-decomposition of a tensor field.
   * @param f the input array.
   * @return the output array.
   */
  public float[][][] smooth2(Direction3 d, EigenTensors3 t, float[][][] f) {
    float[][][] g = like(f);
    smooth2(orthogonal(d),t,f,g);
    return g;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static class LaplacianSmoother {
    LaplacianSmoother(int halfWidth) {
      _scale = halfWidth*(halfWidth+1)/6.0f;
    }
    public void apply(float[] f, float[] g) {
      if (_scale==0.0f) {
        copy(f,g);
      } else {
        _lsf.apply(_scale,f,g);
      }
    }
    public void apply(
      Direction2 d, EigenTensors2 t, float[][] f, float[][] g) 
    {
      if (_scale==0.0f) {
        copy(f,g);
      } else {
        int n1 = f[0].length;
        int n2 = f.length;
        float[][] au = new float[n2][n1];
        float[][] av = new float[n2][n1];
        float[][] sf = new float[n2][n1];
        t.getEigenvalues(au,av);
        setEigenvalues(d,t);
        _lsf.applySmoothL(_kmax,f,sf);
        //_lsf.applySmoothS(f,sf);
        _lsf.apply(t,_scale,sf,g);
        t.setEigenvalues(au,av);
      }
    }
    public void apply(
      Direction3 d, EigenTensors3 t, float[][][] f, float[][][] g) 
    {
      if (_scale==0.0f) {
        copy(f,g);
      } else {
        int n1 = f[0][0].length;
        int n2 = f[0].length;
        int n3 = f.length;
        float[][][] au = new float[n3][n2][n1];
        float[][][] av = new float[n3][n2][n1];
        float[][][] aw = new float[n3][n2][n1];
        float[][][] sf = new float[n3][n2][n1];
        t.getEigenvalues(au,av,aw);
        setEigenvalues(d,t);
        _lsf.applySmoothL(_kmax,f,sf);
        //_lsf.applySmoothS(f,sf);
        _lsf.apply(t,_scale,sf,g);
        t.setEigenvalues(au,av,aw);
      }
    }
    private float _scale;
    private static final double _small = 0.001;
    private static final int _niter = 1000;
    private static final LocalDiffusionKernel _ldk = 
      //new LocalDiffusionKernel(LocalDiffusionKernel.Stencil.D22);
      new LocalDiffusionKernel(LocalDiffusionKernel.Stencil.D71);
    private static final LocalSmoothingFilter _lsf = 
      new LocalSmoothingFilter(_small,_niter,_ldk);
    private static final double _kmax = 0.35;
  }

  private LaplacianSmoother _smoother1,_smoother2;

  private static void setEigenvalues(Direction2 d, EigenTensors2 t) {
    float au = 0.0f;
    float av = 0.0f;
    if (d==Direction2.U || d==Direction2.UV)
      au = 1.0f;
    if (d==Direction2.V || d==Direction2.UV)
      av = 1.0f;
    t.setEigenvalues(au,av);
  }
  private static void setEigenvalues(Direction3 d, EigenTensors3 t) {
    float au = 0.0f;
    float av = 0.0f;
    float aw = 0.0f;
    if (d==Direction3.U || 
        d==Direction3.UV || 
        d==Direction3.UW ||
        d==Direction3.UVW)
      au = 1.0f;
    if (d==Direction3.V || 
        d==Direction3.UV || 
        d==Direction3.VW ||
        d==Direction3.UVW)
      av = 1.0f;
    if (d==Direction3.W || 
        d==Direction3.UW || 
        d==Direction3.VW ||
        d==Direction3.UVW)
      aw = 1.0f;
    t.setEigenvalues(au,av,aw);
  }

  private static float[] like(float[] f) {
    return new float[f.length];
  }
  private static float[][] like(float[][] f) {
    return new float[f.length][f[0].length];
  }
  private static float[][][] like(float[][][] f) {
    return new float[f.length][f[0].length][f[0][0].length];
  }

  private static Direction2 orthogonal(Direction2 d) {
    if (d==Direction2.U)
      return Direction2.V;
    else
      return Direction2.U;
  }
  private static Direction3 orthogonal(Direction3 d) {
    if (d==Direction3.U)
      return Direction3.VW;
    else if (d==Direction3.V)
      return Direction3.UW;
    else if (d==Direction3.W)
      return Direction3.UV;
    else if (d==Direction3.UV)
      return Direction3.W;
    else if (d==Direction3.UW)
      return Direction3.V;
    else
      return Direction3.U;
  }
} 
