/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Steerable pyramid for 2D and 3D images. Includes creation of the steerable
 * pyramid, estimation of local orientation and dimensionality attributes, 
 * application of steering weights and scaling to enhance locally linear
 * features, and construction of the filtered image.
 * <p>
 * This implementation of the steerable pyramid transform performs a 
 * multi-scale, multi-orientation decomposition of an input image through
 * application of radial and directional filters in wavenumber domain.
 * The basis steerable filter amplitudes are proportional to cos^2(theta).
 * Three basis orientations are used for 2D, and six orientations are used
 * for 3D images.  Radial filters are used to partition the data into
 * 1-octave bands, with a cosine taper.  Images are subsampled for each
 * pyramid level which greatly reduces processing effort for lower
 * wavenumbers.
 * <p>
 * Directionally-filtered basis images are used to estimate local orientation
 * and dimensionality.  Preprocessing, which includes averaging in space
 * and scale domains, is applied for these estimates.  Steering weights can be
 * calculated and applied.  Scaling and thresholding can also be applied,
 * based on a local dimensionality attribute.  For 3D images, processing can
 * be applied to enhance either locally-linear or locally-planar features.
 * <p>
 * The number of pyramid levels to use is calculated from the size of the
 * input image, assuming a minimum basis image dimension of 9 samples in x,y
 * or z.  The input image is padded before it is transformed to wavenumber
 * domain, where the filters are applied.  The main reason for this padding is
 * to avoid losing first or last samples when subsampling.  We like the number
 * of samples to be such that, for number of samples n in x, y, or z,
 * (n-1)/2+1 will always yield an integer.
 * <p>
 * The format of the steerable pyramid is a 4-dimensional array for 2D, and a
 * 5-dimensional array for 3D.  The first dimension is level number and second
 * dimension is basis filter orientation.  Below these are either 2D or 3D
 * arrays.
 * To illustrate for 2D:
 * <p>
 * [0][0][0][0] to [0][0][n2][n1] = level 0, theta 0
 * <p>
 * [0][1][0][0] to [0][1][n2][n1] = level 0, theta PI/3
 * <p>
 * [0][2][0][0] to [0][2][n2][n1] = level 0, theta 2*PI/3
 * <p>
 * [1][0][0][0] to [1][0][(n2-1)/2+1][(n1-1)/2+1] = level 1, theta 0
 * <p>
 * [1][1][0][0] to [1][1][(n2-1)/2+1][(n1-1)/2+1] = level 1, theta PI/3
 * <p>
 * [1][2][0][0] to [1][2][(n2-1)/2+1][(n1-1)/2+1] = level 1, theta 2*PI/3
 * <p>
 * ...
 * <p>
 * [NLEVEL-1][2][0][0] to 
 * [NLEVEL-1][2][(n2-1)/(2^(NLEVEL-1))+1][(n1-1)/(2^(NLEVEL-1))+1]
 * = level N-1, theta 2*PI/3
 * <p>
 * [NLEVEL][0][0][0] to [NLEVEL][0][(n2-1)/(2^NLEVEL)+1][(n1-1)/(2^NLEVEL)+1]
 * = residual low-wavenumber image
 * <p>
 * The 3D steerable pyramid array is the same except that it is arrays of 
 * arrays of 3D, rather than 2D arrays.
 *
 * @author John Mathewson, Colorado School of Mines
 * @version 2008.12.01
 */
public class SteerablePyramid {

  /**
   * Construct a steerable pyramid with default cutoff wavenumbers
   * used in the radial low-pass filters.  Default values are:
   * ka=0.60 and kb=1.00, where ka and kb are wavenumbers at start and
   * end of taper (Amp(ka)=1.0, Amp(kb)=0.0).
   */
  public SteerablePyramid() {
    ka = 0.60;
    kb = 1.00;
  }
  
  /**
   * Construct a steerable pyramid with specified cutoff wavenumbers
   * used in the radial low-pass filters.
   * @param ka wavenumber at start of taper.  Amp(ka)=1.
   * @param kb wavenumber at end of taper.  Amp(ka)=0.
   */
  public SteerablePyramid(double ka,double kb) {
    this.ka = ka;
    this.kb = kb;
  }
  
  /**
   * Creates a steerable pyramid representation of an input 2D image.
   * @param x input 2D image.
   * @return array containing steerable pyramid representation of the input
   * 2D image.
   */
  public float[][][][] makePyramid(float[][] x) {
    nx2 = x.length;
    nx1 = x[0].length;
    // Compute number of levels in pyramid from size of input image.  Also
    // determine dimensions n1 and n2 for the finest-sampled pyramid level
    // that will allow us to subsample each pyramid level without losing the
    // last sample.  In our pyramid images we will carry this number of 
    // samples and copy the original number of samples only for final output.
    nlev = 1;
    int nlev2 = 1;
    n1 = 9;
    n2 = 9;
    while (nx1>n1) {
      n1 = (n1-1)*2+1;
      nlev += 1;
    }
    while (nx2>n2) {
      n2 = (n2-1)*2+1;
      nlev2 += 1;
    }
    if (nlev>nlev2) {
      nlev = nlev2;
    }
    /**
     * Create output 4-dimensional array, consisting of:
     * Basis images: nlev*ndir 2D sub-arrays; for each pyramid level images
     * are subsampled, so they are 25% size of images in preceding level.
     * Low-wavenumber image: Single image, residual low-wavenumber energy.
     */
    float[][][][] spyr = new float[nlev+1][1][1][1];
    for (int lev=0; lev<nlev; ++lev) {
      int lfactor = (int)pow(2.0,(double)lev);
      int nl2 = (n2-1)/lfactor+1;
      int nl1 = (n1-1)/lfactor+1;
      spyr[lev] = zerofloat(nl1,nl2,NDIR2);
    }
    int lfactor = (int)pow(2.0,(double)nlev);
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    spyr[nlev] = zerofloat(nl1,nl2,1);
    float[][] cf = ftForward(0,x);
    applyRadial(ka,kb,cf);
    for (int lev=0; lev<nlev; ++lev) {
      if (lev>0) {
        cf = ftForward(lev,spyr[lev][0]);
      }
      makePyramidLevel(lev,cf,spyr);
    }
    return spyr;
  }
  
  /**
   * Creates a steerable pyramid representation of an input 3D image.
   * @param x input 3D image.
   * @return array containing steerable pyramid representation of the input
   * 3D image.
   */
  public float[][][][][] makePyramid(float[][][] x) {
    nx3 = x.length;
    nx2 = x[0].length;
    nx1 = x[0][0].length;
    // Compute number of levels in pyramid from size of input image.  Also
    // determine dimensions n1,n2,n3 for the finest-sampled pyramid level
    // that will allow us to subsample each pyramid level without losing the
    // last sample.  In our pyramid images we will carry this number of 
    // samples and copy the original number of samples only for final output.
    nlev = 1;
    int nlev2 = 1;
    int nlev3 = 1;
    n1 = 9;
    n2 = 9;
    n3 = 9;
    while (nx1>n1) {
      n1 = (n1-1)*2+1;
      nlev += 1;
    }
    while (nx2>n2) {
      n2 = (n2-1)*2+1;
      nlev2 += 1;
    }
    while (nx3>n3) {
      n3 = (n3-1)*2+1;
      nlev3 += 1;
    }
    if (nlev>nlev2) {
      nlev = nlev2;
    }
    if (nlev>nlev3) {
      nlev = nlev3;
    }
    /**
     * Create output 5-dimensional array, consisting of:
     * Basis images: nlev*ndir 3D sub-arrays; for each pyramid level images
     * are subsampled, so they are 12.5% size of images in preceding level.
     * Low-wavenumber image: Single image, residual low-wavenumber energy.
     */
    float[][][][][] spyr = new float[nlev+1][1][1][1][1];
    for (int lev=0; lev<nlev; ++lev) {
      int lfactor = (int)pow(2.0,(double)lev);
      int nl3 = (n3-1)/lfactor+1;
      int nl2 = (n2-1)/lfactor+1;
      int nl1 = (n1-1)/lfactor+1;
      spyr[lev] = new float[NDIR3][1][1][1];
      for (int dir=0; dir<NDIR3; ++dir) {
        spyr[lev][dir] = zerofloat(nl1,nl2,nl3);
      }
    }
    int lfactor = (int)pow(2.0,(double)nlev);
    int nl3 = (n3-1)/lfactor+1;
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    spyr[nlev][0] = zerofloat(nl1,nl2,nl3);
    float[][][] cf = ftForward(0,x);
    applyRadial(ka,kb,cf);
    for (int lev=0; lev<nlev; ++lev) {
      if (lev>0) {
        cf = ftForward(lev,spyr[lev][0]);
      }
      makePyramidLevel(lev,cf,spyr);
    }
    return spyr;
  }
  
  /**
   * Sums all basis images from an input 2D steerable pyramid to create a
   * filtered output image.  Optionally, residual low-wavenumber image can 
   * be zeroed.
   * @param keeplow if true:keep low-wavenumber energy, if false: zero it.
   * @param spyr input 2D steerable pyramid.
   * @return array containing output filtered 2D image.
   */
  public float[][] sumPyramid(boolean keeplow,float[][][][] spyr) {
    int lev;
    // Optionally zero the low-wavenumber image.
    if (!keeplow) {
      zero(spyr[nlev][0]);
    }
    // Sum basis images to create filtered image.  Sinc interpolation is
    // performed on subsampled images prior to summing adjacent levels.
    int lfactor = (int)pow(2.0,(double)(nlev-1));
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    for (int i=0; i<nlev; ++i) {
      lev = nlev-i-1;
      for (int dir=1; dir<NDIR2; ++dir) {
        add(spyr[lev][0],spyr[lev][dir],spyr[lev][0]);
      }
      int m2 = (nl2-1)/2+1;
      int m1 = (nl1-1)/2+1;
      SincInterp si = new SincInterp();
      si.setExtrapolation(SincInterp.Extrapolation.CONSTANT);
      for (int i2=0; i2<nl2; ++i2) {
        for (int i1=0; i1<nl1; ++i1) {
          spyr[lev][0][i2][i1] += si.interpolate(
            m1,2,0,m2,2,0,spyr[lev+1][0],i1,i2);
        }
      }
      nl2 = (nl2-1)*2+1;
      nl1 = (nl1-1)*2+1;
    }
    float[][] y = zerofloat(nx1,nx2);
    copy(nx1,nx2,spyr[0][0],y);
    return y;
  }
  
  /**
   * Sums all basis images from an input 3D steerable pyramid to create a 
   * filtered output image.  Optionally, residual low-wavenumber image can
   * be zeroed.
   * @param keeplow if true:keep low-wavenumber energy, if false: zero it.
   * @param spyr input 3D steerable pyramid.
   * @return array containing output filtered 3D image.
   */
  public float[][][] sumPyramid(boolean keeplow,float[][][][][] spyr) {
    int lev;
    // Optionally zero the low-wavenumber image.
    if (!keeplow) {
      zero(spyr[nlev][0]);
    }
    // Sum basis images to create filtered image.  Sinc interpolation is
    // performed on subsampled images prior to summing adjacent levels.
    int lfactor = (int)pow(2.0,(double)(nlev-1));
    int nl3 = (n3-1)/lfactor+1;
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    for (int i=0; i<nlev; ++i) {
      lev = nlev-i-1;
      for (int dir=1; dir<NDIR3; ++dir) {
        add(spyr[lev][0],spyr[lev][dir],spyr[lev][0]);
      }
      int m3 = (nl3-1)/2+1;
      int m2 = (nl2-1)/2+1;
      int m1 = (nl1-1)/2+1;
      int j1,j2,j3;
      float[] lo11 = zerofloat(m1);
      float[] lo12 = zerofloat(m2);
      float[] lo13 = zerofloat(m3);
      SincInterp si = SincInterp.fromErrorAndFrequency(0.001,0.4);
      si.setExtrapolation(SincInterp.Extrapolation.CONSTANT);
      for (int i3=0; i3<nl3; i3=i3+2) {
        j3 = i3/2;
        for (int i2=0; i2<nl2; i2=i2+2) {
          j2 = i2/2;
          for (int i1=0; i1<nl1; i1=i1+2) {
            j1 = i1/2;
            lo11[j1] = spyr[lev+1][0][j3][j2][j1];
            spyr[lev][1][i3][i2][i1] = lo11[j1];
          }
          for (int i1=1; i1<nl1; i1=i1+2) {
            spyr[lev][1][i3][i2][i1] = si.interpolate(m1,2,0,lo11,i1);
          }
        }
      }
      for (int i3=0; i3<nl3; i3=i3+2) {
        for (int i1=0; i1<nl1; ++i1) {
          for (int i2=0; i2<nl2; i2=i2+2) {
            j2 = i2/2;
            lo12[j2] = spyr[lev][1][i3][i2][i1];
          }
          for (int i2=1; i2<nl2; i2=i2+2) {
            spyr[lev][1][i3][i2][i1] = si.interpolate(m2,2,0,lo12,i2);
          }
        }
      }
      for (int i2=0; i2<nl2; ++i2) {
        for (int i1=0; i1<nl1; ++i1) {
          for (int i3=0; i3<nl3; i3=i3+2) {
            j3 = i3/2;
            lo13[j3] = spyr[lev][1][i3][i2][i1];
          }
          for (int i3=1; i3<nl3; i3=i3+2) {
            spyr[lev][1][i3][i2][i1] = si.interpolate(m3,2,0,lo13,i3);
          }
        }
      }
      add(spyr[lev][0],spyr[lev][1],spyr[lev][0]);
      nl3 = (nl3-1)*2+1;
      nl2 = (nl2-1)*2+1;
      nl1 = (nl1-1)*2+1;
    }
    float[][][] y = zerofloat(nx1,nx2,nx3);
    copy(nx1,nx2,nx3,spyr[0][0],y);
    return y;
  }
  
  /**
   * Estimation of local orientation and linearity attributes in 2D.
   * Preprocessing and 2D Gaussian filtering are applied to input
   * basis images before analysis, and averaging of data from adjacent
   * pyramid levels is performed.  Gaussian half-widths are varied
   * when averaging adjacent levels to maintain consistent smoothing
   * for the levels being averaged.
   * <p>
   * The format of the output attributes is a 4-dimensional array.
   * The first dimension is level number and second dimension is type
   * of attribute.  Below these are 2D arrays:
   * <p>
   * [0][0][0][0] to [0][0][n2][n1] = level 0, theta attribute (radians)
   * <p>
   * [0][1][0][0] to [0][1][n2][n1] = level 0, linearity attribute
   * <p>
   * [1][0][0][0] to [1][0][(n2-1)/2+1][(n1-1)/2+1] = level 1,
   * theta attribute (radians)
   * <p>
   * [1][1][0][0] to [1][1][(n2-1)/2+1][(n1-1)/2+1] = level 1,
   * linearity attribute
   * <p>
   * ...
   * <p>
   * [NLEVEL-1][1][0][0] to 
   * [NLEVEL-1][1][(n2-1)/(2^(NLEVEL-1))+1][(n1-1)/(2^(NLEVEL-1))+1]
   * = level N-1, linearity attribute
   * @param sigma half-width of 2D Gaussian smoothing filter.
   * @param spyr input 2D steerable pyramid.
   * @return array containing local orientation estimate and linearity
   * attribute for all sample locations in every pyramid level.  Array
   * consists of numlevels*2 2D sub-arrays.  For each level the first
   * sub-array contains orientation theta in radians, and the second
   * contains linearity attribute ranging from 0 to 1.
   */
  public float[][][][] estimateAttributes(double sigma,float[][][][] spyr) {
    double sigmaa = 2.0*sigma;
    double sigmac = 0.5*sigma;
    double a0,a1,a2;
    double e0,e1;
    double[][] feout;
    int i1a,i2a,i1c,i2c;
    // Allocate output 4-dimensional array.
    float[][][][] attr = new float[nlev][2][1][1];
    for (int lev=0; lev<nlev; ++lev) {
      int lfactor = (int)pow(2.0,(double)lev);
      int nl2 = (n2-1)/lfactor+1;
      int nl1 = (n1-1)/lfactor+1;
      for (int j=0; j<2; ++j) {
        attr[lev][j] = zerofloat(nl1,nl2);
      }
    }
    // Apply preprocessing to multiple levels and average adjacent scales.
    // Write over level a preprocessed basis images after finishing with
    // them to save memory.
    for (int levb=0; levb<nlev; ++levb) {
      int leva = levb-1;
      int levc = levb+1; 
      float[][][] pqjb = pqjShiftSmooth(sigma,levb,spyr);
      int nl2 = pqjb[0].length;
      int nl1 = pqjb[0][0].length;  
      if (leva>=0) {
        float[][][] pqja = pqjShiftSmooth(sigmaa,leva,spyr);
        for (int i2b=0; i2b<nl2; ++i2b) {
          for (int i1b=0; i1b<nl1; ++i1b) {
            i1a = 2*i1b;
            i2a = 2*i2b;
            for (int j=0; j<NDIR2; ++j) {
              pqjb[j][i2b][i1b] += pqja[j][i2a][i1a];
            }
          }
        }
      }
      if (levc<nlev) {
        float[][][] pqja = pqjShiftSmooth(sigmac,levc,spyr);
        for (int i2b=0; i2b<nl2; ++i2b) {
          for (int i1b=0; i1b<nl1; ++i1b) {
            i1c = (int)(round((double)i1b)*0.5);
            i2c = (int)(round((double)i2b)*0.5);
            for (int j=0; j<NDIR2; ++j) {
              pqjb[j][i2b][i1b] += pqja[j][i2c][i1c];
            }
          }
        }
      }
      // Compute steering angle theta and linearity attribute.
      for (int i2b=0; i2b<nl2; ++i2b) {
        for (int i1b=0; i1b<nl1; ++i1b) {
          a0 = (double)pqjb[0][i2b][i1b];
          a1 = (double)pqjb[1][i2b][i1b];
          a2 = (double)pqjb[2][i2b][i1b];
          feout = findExtrema(a0,a1,a2);
          attr[levb][0][i2b][i1b] = (float)(feout[0][0]);
          e0 = eval0(a0,a1,a2,feout[0][0]);
          e1 = eval0(a0,a1,a2,feout[1][0]);
          attr[levb][1][i2b][i1b] = (float)((e0-e1)/e0);
        } 
      }
    }
    return attr;
  }
  
  /**
   * Estimation of local orientation and linearity attributes in 3D.
   * Preprocessing and 3D Gaussian filtering are applied to input
   * basis images before analysis, and averaging of data from adjacent
   * pyramid levels is performed.  Gaussian half-widths are varied
   * when averaging adjacent levels to maintain consistent smoothing
   * for the levels being averaged.
   * <p>
   * In 3D we have a choice of filtering to enhance locally-planar or
   * locally-linear image features.  There is a parameter in this
   * method to select one of these choices.  If enhancement of planar
   * features is selected the output orientation attributes define the
   * normal to locally-planar features, and the dimensionality attribute
   * is a measure of planarity.  If enhancement of locally-linear
   * features is selected the output orientation attributes define the
   * orientation of a locally-linear feature, and the dimensionality
   * attribute is a local measure of linearity.
   * <p>
   * The format of the output attributes is a 5-dimensional array.
   * The first dimension is level number and second dimension is type
   * of attribute.  Below these are 3D arrays:
   * <p>
   * [0][0][0][0][0] to [0][0][n3][n2][n1] = level 0, direction cosine a
   * <p>
   * [0][0][0][0][0] to [0][0][n3][n2][n1] = level 0, direction cosine b
   * <p>
   * [0][0][0][0][0] to [0][0][n3][n2][n1] = level 0, direction cosine c
   * <p>
   * [0][1][0][0][0] to [0][1][n3][n2][n1] = level 0, dimensionality attribute
   * <p>
   * These are repeated for all levels, subsampled for every successive level.
   * @param forlinear true: apply to enhance locally linear, false: apply for 
   * planar.
   * @param sigma half-width of 3D Gaussian smoothing filter.
   * @param spyr input 3D steerable pyramid.
   * @return array containing local orientation estimate and dimensionality
   * attribute for all sample locations in every pyramid level.  Array
   * consists of numlevels*4 3D sub-arrays.  For each level the first three
   * sub-arrays contain direction cosines, and the fourth contains 
   * dimensionality attribute ranging from 0 to 1.
   */
  public float[][][][][] estimateAttributes(boolean forlinear,double sigma,
                                                      float[][][][][] spyr) {
    double sigmaa = 2.0*sigma;
    double sigmac = 0.5*sigma;
    double[] f = zerodouble(NDIR3);
    double[][] abcf = zerodouble(4,3);
    int i1a,i2a,i3a,i1c,i2c,i3c;
    // Parameters to select estimation for locally planar or linear features
    int abcindx = 2;
    int e0indx = 2;
    int e1indx = 1;
    statelinear = forlinear;
    if (forlinear) {
      abcindx = 0;
      e0indx = 1;
      e1indx = 0;
    }
    // Allocate output 5-dimensional array.
    float[][][][][] attr = new float[nlev][4][1][1][1];
    for (int lev=0; lev<nlev; ++lev) {
      int lfactor = (int)pow(2.0,(double)lev);
      int nl3 = (n3-1)/lfactor+1;
      int nl2 = (n2-1)/lfactor+1;
      int nl1 = (n1-1)/lfactor+1;
      for (int j=0; j<4; ++j) {
        attr[lev][j] = zerofloat(nl1,nl2,nl3);
      }
    }
    // Apply preprocessing to multiple levels and average adjacent scales.
    // Write over level a preprocessed basis images after finishing with
    // them to save memory.
    for (int levb=0; levb<nlev; ++levb) {
      int leva = levb-1;
      int levc = levb+1; 
      float[][][][] pqjb = pqjShiftSmooth(sigma,levb,spyr);
      int nl3 = pqjb[0].length;
      int nl2 = pqjb[0][0].length;
      int nl1 = pqjb[0][0][0].length;  
      if (leva>=0) {
        float[][][][] pqja = pqjShiftSmooth(sigmaa,leva,spyr);
        for (int i3b=0; i3b<nl3; ++i3b) {
          for (int i2b=0; i2b<nl2; ++i2b) {
            for (int i1b=0; i1b<nl1; ++i1b) {
              i3a = 2*i3b;
              i2a = 2*i2b;
              i1a = 2*i1b;
              for (int j=0; j<NDIR3; ++j) {
                pqjb[j][i3b][i2b][i1b] += pqja[j][i3a][i2a][i1a];
              }
            }
          }
        }
      }
      if (levc<nlev) {
        float[][][][] pqja = pqjShiftSmooth(sigmac,levc,spyr);
        for (int i3b=0; i3b<nl3; ++i3b) {
          for (int i2b=0; i2b<nl2; ++i2b) {
            for (int i1b=0; i1b<nl1; ++i1b) {
              i3c = (int)(round((double)i3b)*0.5);
              i2c = (int)(round((double)i2b)*0.5);
              i1c = (int)(round((double)i1b)*0.5);
              for (int j=0; j<NDIR3; ++j) {
                pqjb[j][i3b][i2b][i1b] += pqja[j][i3c][i2c][i1c];
              }
            }
          }
        }
      }
      // Compute steering direction cosines and dimensionality attribute.
      for (int i3b=0; i3b<nl3; ++i3b) {
        for (int i2b=0; i2b<nl2; ++i2b) {
          for (int i1b=0; i1b<nl1; ++i1b) {
            for (int j=0; j<NDIR3; ++j) {
              f[j] = pqjb[j][i3b][i2b][i1b];
            }
            findCriticalPoints(f,abcf);
            attr[levb][0][i3b][i2b][i1b] = (float)(abcf[abcindx][0]);
            attr[levb][1][i3b][i2b][i1b] = (float)(abcf[abcindx][1]);
            attr[levb][2][i3b][i2b][i1b] = (float)(abcf[abcindx][2]);
            attr[levb][3][i3b][i2b][i1b] = 
                (float)((abcf[e0indx][3]-abcf[e1indx][3])/abcf[2][3]);
          } 
        }
      }
    } 
    return attr;
  }
  
  /**
   * Applies steering weights and scaling or thresholding based on linearity
   * attribute to the basis images in the input 3D steerable pyramid array.
   * If "forlinear" option was used when estimateAttributes was run, this 
   * method will convert basis images from plane-enhancing cos^2-filtered
   * to line-enhancing sin^2-filtered versions.
   * Scaling options include:
   *   No scaling (linpowr=0).
   *   Linearity raised to a power (1 &le; linpowr &le; 98).
   *   Sigmoidal thresholding (linpowr=99) Typical values for thresholding
   *     parameters are k=50.0, thresh=0.5.
   * @param forlinear true: apply to enhance locally linear, false: apply 
   * for planar.
   * @param linpowr linearity power and scaling type switch.
   * @param k sigmoidal thresholding steepness.
   * @param thresh threshold.
   * @param attr input array containing direction cosines and dimensionality.
   * @param spyr input/output 3D steerable pyramid.
   */
  public void steerScale(boolean forlinear,int linpowr,float k, float thresh,
                float[][][][][] attr, float[][][][][] spyr) {
    float ai,bi,ci,wi;
    float scal = 0.0f;
    int j0 = 0;
    int j1 = 0;
    int j2 = 0;
    float signb = 1.0f;
    for (int lev=0; lev<nlev; ++lev) {
      int lfactor = (int)pow(2.0,(double)lev);
      int nl3 = (n3-1)/lfactor+1;
      int nl2 = (n2-1)/lfactor+1;
      int nl1 = (n1-1)/lfactor+1;
      // Convert basis images to line-enhancing sin^2 if
      // attributes have been estimated for linear features.
      if (statelinear) {
        float[][][] p = add(spyr[lev][0],spyr[lev][1]);
        for (int dir=2; dir<NDIR3; ++dir) {
          add(p,spyr[lev][dir],p);
        }
        mul(p,0.5f,p);
        for (int dir=0; dir<NDIR3; ++dir) {
          sub(p,spyr[lev][dir],spyr[lev][dir]);
        }
      }
      // Compute and apply steering weights and scaling option
      for (int dir=0; dir<NDIR3; ++dir) {
        if      (dir==0) {j0=0; j1=1; j2=2; signb= 1.0f;}
        else if (dir==1) {j0=0; j1=1; j2=2; signb=-1.0f;}
        else if (dir==2) {j0=2; j1=0; j2=1; signb= 1.0f;}
        else if (dir==3) {j0=2; j1=0; j2=1; signb=-1.0f;}
        else if (dir==4) {j0=1; j1=2; j2=0; signb= 1.0f;}
        else if (dir==5) {j0=1; j1=2; j2=0; signb=-1.0f;}
        for (int i3=0; i3<nl3; ++i3) {
          for (int i2=0; i2<nl2; ++i2) {
            for (int i1=0; i1<nl1; ++i1) {
              ai = attr[lev][j0][i3][i2][i1];
              bi = attr[lev][j1][i3][i2][i1]*signb;
              ci = attr[lev][j2][i3][i2][i1];
              wi = (ai+bi)*(ai+bi)-ci*ci;
              if(linpowr==0) {
                scal = 1.0f;
              }
              else if(linpowr==1) {
                scal = attr[lev][3][i3][i2][i1];
              }
              else if(linpowr>1&&linpowr<99) {
                scal = pow(attr[lev][3][i3][i2][i1],linpowr);
              }
              else if(linpowr==99) {
                scal = 1.0f/(1.0f+exp(k*(thresh-attr[lev][3][i3][i2][i1])));
              }
              spyr[lev][dir][i3][i2][i1] *= scal*wi;
            }
          }
        }
      }
    }
  }
  
  /**
   * Applies steering weights and scaling or thresholding based on linearity
   * attribute to the basis images in the input 2D steerable pyramid.
   * Scaling options include:
   *   No scaling (linpowr=0).
   *   Linearity raised to a power (1 &le; linpowr &le; 98).
   *   Sigmoidal thresholding (linpowr=99) Typical values for thresholding
   *     parameters are k=50.0, thresh=0.5.
   * @param linpowr linearity power and scaling type switch.
   * @param k sigmoidal thresholding steepness.
   * @param thresh threshold.
   * @param attr input array containing local orientation and linearity.
   * @param spyr input/output 2D steerable pyramid.
   */
  public void steerScale(int linpowr,float k, float thresh,
                    float[][][][] attr, float[][][][] spyr) {
    float w0,w1,w2;
    float scal = 0.0f;
    double theta;
    int nl2,nl1;
    for (int lev=0; lev<nlev; ++lev) {
      nl2 = spyr[lev][0].length;
      nl1 = spyr[lev][0][0].length;
      for (int i2=0; i2<nl2; ++i2) {
        for (int i1=0; i1<nl1; ++i1) {
          theta = attr[lev][0][i2][i1];
          w0 = 0.5f*(float)(1.0+2.0*cos(2.0*(theta-THETA0)));
          w1 = 0.5f*(float)(1.0+2.0*cos(2.0*(theta-THETA1)));
          w2 = 0.5f*(float)(1.0+2.0*cos(2.0*(theta-THETA2)));
          if(linpowr==0) {
            scal = 1.0f;
          }
          else if(linpowr==1) {
            scal = attr[lev][1][i2][i1];
          }
          else if(linpowr>1&&linpowr<99) {
            scal = pow(attr[lev][1][i2][i1],linpowr);
          }
          else if(linpowr==99) {
            scal = 1.0f/(1.0f+exp(k*(thresh-attr[lev][1][i2][i1])));
          }
          spyr[lev][0][i2][i1] *= scal*w0;
          spyr[lev][1][i2][i1] *= scal*w1;
          spyr[lev][2][i2][i1] *= scal*w2;
        } 
      }
    }
  }
  
  ///////////////////////////////////////////////////////////////////////////
  // private
  private static final double THETA0 = 0.0*PI/3.0;
  private static final double THETA1 = 1.0*PI/3.0;
  private static final double THETA2 = 2.0*PI/3.0;
  private static final double ONE_THIRD = 1.0/3.0;
  private static final float  TWO_THIRDS = 2.0f/3.0f;
  private static final double SQRT3 = sqrt(3.0);
  private static final int    NDIR2=3;
  private static final int    NDIR3=6;
  // Smallest significant change to a, b, or c (for convergence test).
  private static final double ABC_SMALL = 1.0e-2;
  // Smallest significant determinant (denominator in Newton's method).
  private static final double DET_SMALL = 1.0e-40;
  // Other constants.
  private static final double COS_PIO3 = cos(PI/3.0);
  private static final double SIN_PIO3 = sin(PI/3.0);
  private int nlev,nx1,nx2,nx3,n1,n2,n3;
  private boolean statelinear;
  double ka,kb;
  
  /**
   * Make a single 2D pyramid level consisting of three directionally-filtered
   * basis images and a subsampled lower-wavenumber image.  (The low-wavenumber
   * image is the input for creation of the next level).  The output is 
   * written into the appropriate part of the 2D steerable pyramid.
   * @param lev level number.
   * @param cf input image in wavenumber domain (complex array).
   * @param spyr input/output 2D steerable pyramid.
   */
  private void makePyramidLevel(int lev,float[][] cf,float[][][][] spyr) {
    int lfactor = (int)pow(2.0,(double)lev);
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    float[][] clo1 = copy(cf);
    applyRadial(ka/2.0,kb/2.0,clo1);
    sub(cf,clo1,cf);
    ftInverse(lev,0,clo1,spyr);
    int ml2 = (nl2-1)/2+1;
    int ml1 = (nl1-1)/2+1;
    copy(ml1,ml2,0,0,2,2,spyr[lev][0],0,0,1,1,spyr[lev+1][0]);
    for (int dir=0; dir<NDIR2; ++dir) {
      applySteerableFilter(dir,cf,clo1);
      ftInverse(lev,dir,clo1,spyr); 
    }
  }
  
    /**
   * Make a single 3D pyramid level consisting of six directionally-filtered
   * basis images and a subsampled lower-wavenumber image.  (The low-wavenumber
   * image is the input for creation of the next level).  The output is 
   * written into the appropriate part of the 3D steerable pyramid.
   * @param lev level number.
   * @param cf input image in wavenumber domain (complex array).
   * @param spyr input/output 3D steerable pyramid.
   */
  private void makePyramidLevel(int lev,float[][][] cf,float[][][][][] spyr) {
    int lfactor = (int)pow(2.0,(double)lev);
    int nl3 = (n3-1)/lfactor+1;
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    float[][][] clo1 = copy(cf);
    applyRadial(ka/2.0,kb/2.0,clo1);
    sub(cf,clo1,cf);
    ftInverse(lev,0,clo1,spyr);
    int ml3 = (nl3-1)/2+1;
    int ml2 = (nl2-1)/2+1;
    int ml1 = (nl1-1)/2+1;
    copy(ml1,ml2,ml3,0,0,0,2,2,2,spyr[lev][0],
        0,0,0,1,1,1,spyr[lev+1][0]);
    for (int dir=0; dir<NDIR3; ++dir) {
      applySteerableFilter(dir,cf,clo1);
      ftInverse(lev,dir,clo1,spyr); 
    }
  }
  
  /**
   * Apply radial low-pass filter in wavenumber domain.  Filter response is
   * unity in passband, zero outside, with cosine taper.
   * @param k1 wavenumber at start of taper (Amp(k1)=1.0).
   * @param k2 wavenumber at start of taper (Amp(k2)=0.0).
   * @param cf input/output 2D image in wavenumber domain (complex array).
   */
  private void applyRadial(double k1,double k2,float[][] cf) {
    int nf2 = cf.length;
    int nf1 = cf[0].length/2;
    int ir,ii;
    double m1 = (double)(nf1-1);
    double m2 = (double)(nf2-1)/2.0;
    double w1,w2;
    double mf1 = 1.0/m1;
    double mf2 = 1.0/m2;
    double wd,a;
    double denom = 1.0/(k2-k1);
    float b;
    for (int i2=0; i2<nf2; ++i2) {
      for (int i1=0; i1<nf1; ++i1) {
        w1 = ((double)i1)*mf1;
        w2 = ((double)i2-m2)*mf2;
        wd = sqrt(w1*w1+w2*w2);
        ir = 2*i1;
        ii = ir+1;      
        if (wd>k2) {
          cf[i2][ir] = 0.0f;
          cf[i2][ii] = 0.0f;
        }
        else if (wd>k1&&wd<k2) {
          a = (wd-k1)*denom*PI;
          b = (float)(0.5*(1.0+cos(a)));
          cf[i2][ir] *= b;
          cf[i2][ii] *= b;
        }
      }      
    }      
  }
  
  /**
   * Apply radial low-pass filter in wavenumber domain.  Filter response is
   * unity in passband, zero outside, with cosine taper.
   * @param k1 wavenumber at start of taper (Amp(k1)=1.0).
   * @param k2 wavenumber at start of taper (Amp(k2)=0.0).
   * @param cf input/output 3D image in wavenumber domain (complex array).
   */
  private void applyRadial(double k1,double k2,float[][][] cf) {
    int nf3 = cf.length;
    int nf2 = cf[0].length;
    int nf1 = cf[0][0].length/2;
    int ir,ii;
    double m1 = (double)(nf1-1);
    double m2 = (double)(nf2-1)/2.0;
    double m3 = (double)(nf3-1)/2.0;
    double w1,w2,w3;
    double mf1 = 1.0/m1;
    double mf2 = 1.0/m2;
    double mf3 = 1.0/m3;
    double wd,a;
    double denom = 1.0/(k2-k1);
    float b;
    for (int i3=0; i3<nf3; ++i3) {
      for (int i2=0; i2<nf2; ++i2) {
        for (int i1=0; i1<nf1; ++i1) {
          w1 = ((double)i1)*mf1;
          w2 = ((double)i2-m2)*mf2;
          w3 = ((double)i3-m3)*mf3;
          wd = Math.sqrt(w1*w1+w2*w2+w3*w3);
          ir = 2*i1;
          ii = ir + 1;
          if (wd >= k2) {
            cf[i3][i2][ir] = 0.0f;
            cf[i3][i2][ii] = 0.0f;
          }
          else if (wd>k1&&wd<k2) {
            a = (wd-k1)*denom*PI;
            b = (float)(0.5*(1.0+cos(a)));
            cf[i3][i2][ir] *= b;
            cf[i3][i2][ii] *= b;
          }
        }      
      }
    }
  }
  
  /**
   * Apply directional basis filter in wavenumber domain.  Filter 
   * response is Bj=cos^2(theta-thetaj), j=0,1,2, and thetaj=j*PI/3.
   * A constant scalar of 2/3 is applied to make the filtered output
   * equal to the input when steering weights are not applied, as the
   * sum of the three filters is 3/2.
   * @param cfin input 2D image in wavenumber domain (complex array).
   * @param dir filter direction index.
   * @param cfout output 2D image in wavenumber domain (complex array).
   */
  private void applySteerableFilter(int dir,float[][] cfin,float[][] cfout) {
    int nf2 = cfin.length;
    int nf1 = cfin[0].length/2;
    int m1 = nf1-1;
    int m2 = (nf2-1)/2;
    double mf1 = 1.0/(double)m1;
    double mf2 = 1.0/(double)m2;
    int ir, ii;
    double w1,w2,theta;
    float c;
    double thetan = (double)(dir)*ONE_THIRD*PI;
    for (int i2=0; i2<nf2; ++i2) {
      for (int i1=0; i1<nf1; ++i1) {
        ir = 2*i1;
        ii = ir+1;
        w1 = (double)(i1)*mf1;
        w2 = (double)(i2-m2)*mf2;
        theta = atan2(w1,w2);
        c = (float)cos(theta-thetan);
        c = TWO_THIRDS*c*c;
        cfout[i2][ir] = c*cfin[i2][ir];
        cfout[i2][ii] = c*cfin[i2][ii];
      }      
    } 
  }
  
  /**
   * Apply directional basis filter in wavenumber domain.  Filter 
   * response is Bj=cos^2, j=0,1,2,...,5.  Filter orientations are based
   * on the cuboctahedron.
   * @param cfin input 3D image in wavenumber domain (complex array).
   * @param dir filter direction index.
   * @param cfout output 3D image in wavenumber domain (complex array).
   */
  private void applySteerableFilter(int dir,float[][][] cfin,
                                           float[][][] cfout) {
    int nf3 = cfin.length;
    int nf2 = cfin[0].length;
    int nf1 = cfin[0][0].length/2;
    int ir,ii;
    //double m1 = (double)(nf1-1); // not used
    double m2 = (double)(nf2-1)/2.0;
    double m3 = (double)(nf3-1)/2.0;
    double v1=0.0,v2=0.0,v3=0.0;
    double w1,w2,w3;
    double flt1,flt2;
    double s = 1.0;
    double s2 = 1.0 + s*s;
    if      (dir==0) {v1=0.0; v2=1.0; v3=  s;}
    else if (dir==1) {v1=0.0; v2=1.0; v3= -s;}
    else if (dir==2) {v1=1.0; v2=  s; v3=0.0;}
    else if (dir==3) {v1=1.0; v2= -s; v3=0.0;}
    else if (dir==4) {v1=  s; v2=0.0; v3=1.0;}
    else if (dir==5) {v1= -s; v2=0.0; v3=1.0;}
    for (int i3=0; i3<nf3; ++i3) {
      for (int i2=0; i2<nf2; ++i2) {
        for (int i1=0; i1<nf1; ++i1) {
          w1 = (double)i1;
          w2 = (double)i2-m2;
          w3 = (double)i3-m3;
          ir = 2*i1;
          ii = ir+1;
          flt1 = w1*v1 + w2*v2 + w3*v3;
          flt1 = flt1*flt1;
          flt2 = (w1*w1 + w2*w2 + w3*w3)*2.0*s2;
          flt1 = flt1/flt2;
          cfout[i3][i2][ir] = cfin[i3][i2][ir]*(float)flt1;
          cfout[i3][i2][ii] = cfin[i3][i2][ii]*(float)flt1;
        }      
      }
    }
    /**
     * Following section is to avoid divide-by-zero.
     * It sets amplitude to zero for zero wavenumber.
     */
    if ((int)m2*2 == nf2 && (int)m3*2 == nf3) {
      ir = 0;
      ii = ir+1;
      cfout[(int)m3][(int)m2][ir] = 0.0f;
      cfout[(int)m3][(int)m2][ii] = 0.0f;
    }
  }
  
  /**
   * Applies preprocessing and smoothing to 2D steerable pyramid basis images
   * for input to orientation and linearity estimation.  Three input basis
   * images qj for a single pyramid level are summed to produce image p, 
   * which is multiplied with each qj.  Amplitudes are then shifted for 
   * sample locations where one of the images has a negative value.
   * After this step all samples are positive.  2D Gaussian smoothing is
   * then applied to the three preprocessed images.
   * @param sigma half-width of 2D Gaussian smoothing filter.
   * @param lev level number.
   * @param spyr input 2D steerable pyramid (3D array).
   * @return array containing preprocessed, smoothed versions of
   * directionally-filtered basis images for a single pyramid level.
   */
  private float[][][] pqjShiftSmooth(double sigma,int lev,
                                       float[][][][] spyr) {
    RecursiveGaussianFilter rcg = new RecursiveGaussianFilter(sigma);
    float [] test = zerofloat(NDIR2);
    float testmin;
    // Allocate output array
    float[][][] pq = zerofloat(1,1,NDIR2);
    // Compute pqj from qj's
    pq[0] = add(spyr[lev][0],spyr[lev][1]);
    add(pq[0],spyr[lev][2],pq[0]);
    pq[1] = mul(pq[0],spyr[lev][1]);
    pq[2] = mul(pq[0],spyr[lev][2]);
    mul(pq[0],spyr[lev][0],pq[0]);
    // Shift samples to avoid negative
    int n2 = pq[0].length;
    int n1 = pq[0][0].length;
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        test[0] = pq[0][i2][i1];
        test[1] = pq[1][i2][i1];
        test[2] = pq[2][i2][i1];
        testmin = min(test);
        if (testmin<0.0f) {
          pq[0][i2][i1] -= testmin;
          pq[1][i2][i1] -= testmin;
          pq[2][i2][i1] -= testmin;
        }
      }
    }
    // Gaussian smoothing
    rcg.apply00(pq[0],pq[0]);
    rcg.apply00(pq[1],pq[1]);
    rcg.apply00(pq[2],pq[2]);
    return pq;
  }
  
  /**
   * Applies preprocessing and smoothing to 3D steerable pyramid basis images
   * for input to orientation and dimensionality estimation.  Six input basis
   * images qj for a single pyramid level are summed to produce image p, 
   * which is multiplied with each qj.  Amplitudes are then shifted for 
   * sample locations where one of the images has a negative value.
   * After this step all samples are positive.  Gaussian smoothing is
   * then applied to the six preprocessed images.
   * @param sigma half-width of 3D Gaussian smoothing filter.
   * @param lev level number.
   * @param spyr input 3D steerable pyramid (3D array).
   * @return array containing preprocessed, smoothed versions of
   * directionally-filtered basis images for a single pyramid level.
   */
  private float[][][][] pqjShiftSmooth(double sigma,int lev,
                                       float[][][][][] spyr) {
    RecursiveGaussianFilter rcg = new RecursiveGaussianFilter(sigma);
    float [] test = zerofloat(NDIR3);
    float testmin;
    // Allocate output array
    float[][][][] pq = new float[NDIR3][1][1][1];
    // Compute pqj from qj's
    pq[0] = add(spyr[lev][0],spyr[lev][1]);
    for (int dir=2; dir<NDIR3; ++dir) {
      add(pq[0],spyr[lev][dir],pq[0]);
    }
    for (int dir=1; dir<NDIR3; ++dir) {
      pq[dir] = mul(pq[0],spyr[lev][dir]);
    }
    mul(pq[0],spyr[lev][0],pq[0]);
    // Shift samples to avoid negative
    int nl3 = pq[0].length;
    int nl2 = pq[0][0].length;
    int nl1 = pq[0][0][0].length;
    for (int i3=0; i3<nl3; ++i3) {
     for (int i2=0; i2<nl2; ++i2) {
        for (int i1=0; i1<nl1; ++i1) {
          for (int dir=0; dir<NDIR3; ++dir) {
            test[dir] = pq[dir][i3][i2][i1];
          }
          testmin = min(test);
          if (testmin<0.0f) {
            for (int dir=0; dir<NDIR3; ++dir) {
              pq[dir][i3][i2][i1] -= testmin;
            }
          }
        }
      }
    }
    // Gaussian smoothing
    for (int dir=0; dir<NDIR3; ++dir) {
      rcg.apply000(pq[dir],pq[dir]);
    }
    return pq;
  }
  
  /**
   * Finds extrema of output values for a 2nd-order steerable filter.
   * The filter is comprised of three steering filters for angles 0,
   * PI/3 and 2*PI/3.
   * <p>
   * Given three values output from the three steering filters, this method
   * computes two angles and corresponding steered output values such that
   * the derivative of steered output with respect to angle is zero.
   * Returned angles are in the range [0,PI].
   * @param f0 the value for steering filter with angle 0*PI/3.
   * @param f1 the value for steering filter with angle 1*PI/3.
   * @param f2 the value for steering filter with angle 2*PI/3.
   * @return array {{theta1,value1},{theta2,value2}}. The angle theta1
   *  corresponds to value1 with maximum absolute value; the angle theta2
   *  corresponds to value2 with minimum absolute value. Both theta1 and
   *  theta2 are in the range [0,PI] and the absolute difference in these
   *  angles is PI/2.  (This method by Dave Hale 2007.12.19.)
   */
  private static double[][] findExtrema(double f0, double f1, double f2) {
    double theta1 = 0.5*(PI+atan2(SQRT3*(f1-f2),(2.0*f0-f1-f2)));
    double value1 = eval0(f0,f1,f2,theta1);
    double theta2 = modPi(theta1+0.5*PI);
    double value2 = eval0(f0,f1,f2,theta2);
    if (abs(value1)>=abs(value2)) {
      return new double[][]{{theta1,value1},{theta2,value2}};
    } else {
      return new double[][]{{theta2,value2},{theta1,value1}};
    }
  }
  
  // Returns the specified angle modulo PI.
  // The returned angle is in the range [0,PI].
  private static double modPi(double theta) {
    return theta-floor(theta/PI)*PI;
  }

  // Steered output (zeroth derivative).
  private static double eval0(double f0, double f1, double f2, double theta) {
    double k0 = ONE_THIRD*(1.0+2.0*cos(2.0*(theta-THETA0)));
    double k1 = ONE_THIRD*(1.0+2.0*cos(2.0*(theta-THETA1)));
    double k2 = ONE_THIRD*(1.0+2.0*cos(2.0*(theta-THETA2)));
    return k0*f0+k1*f1+k2*f2;
  }

  /**
   * Finds three critical points of the 3D steering function.
   * The function is
   * <pre>
   * f(a,b,c) = ((a+b)*(a+b)-c*c)*f[0] + ((a-b)*(a-b)-c*c)*f[1] +
   *            ((a+c)*(a+c)-b*b)*f[2] + ((a-c)*(a-c)-b*b)*f[3] +
   *            ((b+c)*(b+c)-a*a)*f[4] + ((b-c)*(b-c)-a*a)*f[5]
   * </pre>
   * where (a,b,c) are components of a unit-vector.
   * @param f array[6] of function values.
   * @param abcf array of critical points; if null, a new array is returned.
   * @return array{{a0,b0,c0,f0},{a1,b1,c1,f1},{a2,b2,c2,f2}} of critical 
   *  points. If a non-null array is specified, that array will be returned
   *  with possibly modified values.  (This method by Dave Hale 2008.04.24., 
   *  revised 2008.09.23.)
   */
  private static double[][] findCriticalPoints(double[] f, double[][] abcf) {

    // Coefficients for evaluating the function f' = (f-fsum)/2.
    // The function f' can be evaluated more efficiently and has
    // the same critical points (a,b,c) as the specified function f.
    double fsum = f[0]+f[1]+f[2]+f[3]+f[4]+f[5];
    double fab = f[0]-f[1];
    double fac = f[2]-f[3];
    double fbc = f[4]-f[5];
    double faa = f[4]+f[5];
    double fbb = f[2]+f[3];
    double fcc = f[0]+f[1];

    // Initialize (a,b,c).
    double a,b,c;
    {
      double haa,hbb,hcc,hab,hac,hbc;

      // Determinant for a = 1.0, b = c = 0.0.
      hbb = 2.0*(fbb-faa);
      hcc = 2.0*(fcc-faa);
      hbc = -fbc;
      double da = hbb*hcc-hbc*hbc;
      double dda = da*da;

      // Determinant for b = 1.0, a = c = 0.0.
      haa = 2.0*(faa-fbb);
      hcc = 2.0*(fcc-fbb);
      hac = -fac;
      double db = haa*hcc-hac*hac;
      double ddb = db*db;

      // Determinant for c = 1.0, a = b = 0.0.
      haa = 2.0*(faa-fcc);
      hbb = 2.0*(fbb-fcc);
      hab = -fab;
      double dc = haa*hbb-hab*hab;
      double ddc = dc*dc;

      // Choose the point with largest curvature (determinant-squared).
      if (dda>=ddb && dda>=ddc) {
        a = 1.0; b = 0.0; c = 0.0;
      } else if (ddb>=ddc) {
        a = 0.0; b = 1.0; c = 0.0;
      } else {
        a = 0.0; b = 0.0; c = 1.0;
      }
    }

    // Search for a critical point using Newton's method.
    for (int niter=0; niter<50; ++niter) {

      // We have three cases, depending on which component of the 
      // vector (a,b,c) has largest magnitude. For example, if that 
      // component is c, we express c as c(a,b) so that f is a 
      // function of f(a,b). We then compute a Newton update (da,db), 
      // constrained so that after the update a*a+b*b <= 1.0. This 
      // last condition is necessary so that a*a+b*b+c*c = 1.0 after 
      // updating (a,b,c). We eliminate the component with largest 
      // magnitude (in this case, c), because that choice permits the 
      // largest updates (da,db).
      double da,db,dc;
      double aa = a*a;
      double bb = b*b;
      double cc = c*c;

      // If the component c has largest magnitude, ...
      if (aa<=cc && bb<=cc) {
        double aoc = a/c;
        double boc = b/c;
        double aocs = aoc*aoc;
        double bocs = boc*boc;
        double faamcc2 = (faa-fcc)*2.0;
        double fbbmcc2 = (fbb-fcc)*2.0;
        double aocsp1 = aocs+1.0;
        double bocsp1 = bocs+1.0;
        double ga = a*(faamcc2+boc*fbc)-c*(1.0-aocs)*fac-b*fab;
        double gb = b*(fbbmcc2+aoc*fac)-c*(1.0-bocs)*fbc-a*fab;
        double haa = faamcc2+boc*aocsp1*fbc+aoc*(3.0+aocs)*fac;
        double hbb = fbbmcc2+aoc*bocsp1*fac+boc*(3.0+bocs)*fbc;
        double hab = boc*aocsp1*fac+aoc*bocsp1*fbc-fab;
        double det = haa*hbb-hab*hab;
        if (det<=DET_SMALL && -det<=DET_SMALL) det = DET_SMALL;
        double odet = 1.0/det;
        da = odet*(hbb*ga-hab*gb);
        db = odet*(haa*gb-hab*ga);
        dc = 0.0;
        for (double at=a-da,bt=b-db; at*at+bt*bt>=1.0; at=a-da,bt=b-db) {
          da *= 0.5;
          db *= 0.5;
        }
        a -= da;
        b -= db;
        c = (c>=0.0)?sqrt(1.0-a*a-b*b):-sqrt(1.0-a*a-b*b);
      }

      // Else if the component b has largest magnitude, ...
      else if (aa<=bb) {
        double aob = a/b;
        double cob = c/b;
        double aobs = aob*aob;
        double cobs = cob*cob;
        double faambb2 = (faa-fbb)*2.0;
        double fccmbb2 = (fcc-fbb)*2.0;
        double aobsp1 = aobs+1.0;
        double cobsp1 = cobs+1.0;
        double ga = a*(faambb2+cob*fbc)-b*(1.0-aobs)*fab-c*fac;
        double gc = c*(fccmbb2+aob*fab)-b*(1.0-cobs)*fbc-a*fac;
        double haa = faambb2+cob*aobsp1*fbc+aob*(3.0+aobs)*fab;
        double hcc = fccmbb2+aob*cobsp1*fab+cob*(3.0+cobs)*fbc;
        double hac = cob*aobsp1*fab+aob*cobsp1*fbc-fac;
        double det = haa*hcc-hac*hac;
        if (det<=DET_SMALL && -det<=DET_SMALL) det = DET_SMALL;
        double odet = 1.0/det;
        da = odet*(hcc*ga-hac*gc);
        db = 0.0;
        dc = odet*(haa*gc-hac*ga);
        for (double at=a-da,ct=c-dc; at*at+ct*ct>=1.0; at=a-da,ct=c-dc) {
          da *= 0.5;
          dc *= 0.5;
        }
        a -= da;
        c -= dc;
        b = (b>=0.0)?sqrt(1.0-a*a-c*c):-sqrt(1.0-a*a-c*c);
      }

      // Else if the component a has largest magnitude, ...
      else {
        double boa = b/a;
        double coa = c/a;
        double boas = boa*boa;
        double coas = coa*coa;
        double fbbmaa2 = (fbb-faa)*2.0;
        double fccmaa2 = (fcc-faa)*2.0;
        double boasp1 = boas+1.0;
        double coasp1 = coas+1.0;
        double gb = b*(fbbmaa2+coa*fac)-a*(1.0-boas)*fab-c*fbc;
        double gc = c*(fccmaa2+boa*fab)-a*(1.0-coas)*fac-b*fbc;
        double hbb = fbbmaa2+coa*boasp1*fac+boa*(3.0+boas)*fab;
        double hcc = fccmaa2+boa*coasp1*fab+coa*(3.0+coas)*fac;
        double hbc = coa*boasp1*fab+boa*coasp1*fac-fbc;
        double det = hbb*hcc-hbc*hbc;
        if (det<=DET_SMALL && -det<=DET_SMALL) det = DET_SMALL;
        double odet = 1.0/det;
        da = 0.0;
        db = odet*(hcc*gb-hbc*gc);
        dc = odet*(hbb*gc-hbc*gb);
        for (double bt=b-db,ct=c-dc; bt*bt+ct*ct>=1.0; bt=b-db,ct=c-dc) {
          db *= 0.5;
          dc *= 0.5;
        }
        b -= db;
        c -= dc;
        a = (a>=0.0)?sqrt(1.0-b*b-c*c):-sqrt(1.0-b*b-c*c);
      }

      // Test for convergence.
      if (da<ABC_SMALL && -da<ABC_SMALL &&
          db<ABC_SMALL && -db<ABC_SMALL &&
          dc<ABC_SMALL && -dc<ABC_SMALL)
        break;
    }

    // Assuming convergence, we now have one critical point (a0,b0,c0).
    // We find the other two critical points by searching in the plane
    // orthogonal to the vector p0 = (a0,b0,c0).
    double a0 = a, b0 = b, c0 = c;

    // A second unit vector pr orthogonal to p0.
    double ar,br,cr;
    double aa = a*a, bb = b*b, cc = c*c;
    if (aa<=bb && aa<=cc) {
      ar = 0.0; br = c0; cr = -b0;
    } else if (bb<=cc) {
      ar = c0; br = 0.0; cr = -a0;
    } else {
      ar = b0; br = -a0; cr = 0.0;
    }
    double sr0 = ar*a0+br*b0+cr*c0; // dot product
    ar -= sr0*a0; br -= sr0*b0; cr -= sr0*c0; // Gram-Schmidt
    double sr = 1.0/sqrt(ar*ar+br*br+cr*cr);
    ar *= sr; br*= sr; cr *= sr; // unit vector

    // A third unit vector ps orthogonal to both pr and p0.
    double as = br*c0-b0*cr;
    double bs = cr*a0-c0*ar;
    double cs = ar*b0-a0*br;

    // Three points p1, p2, and p3 in plane of pr and ps,
    // with angles 0, PI/3 and -PI/3 as measured from p1.
    // These are the points used in a 2D steering function.
    double a1 = ar;
    double b1 = br;
    double c1 = cr;
    double a2 = COS_PIO3*ar+SIN_PIO3*as;
    double b2 = COS_PIO3*br+SIN_PIO3*bs;
    double c2 = COS_PIO3*cr+SIN_PIO3*cs;
    double a3 = COS_PIO3*ar-SIN_PIO3*as;
    double b3 = COS_PIO3*br-SIN_PIO3*bs;
    double c3 = COS_PIO3*cr-SIN_PIO3*cs;

    // Function values for points p0, p1, p2, and p3.
    double f0 = fab*a0*b0+fac*a0*c0+fbc*b0*c0-faa*a0*a0-fbb*b0*b0-fcc*c0*c0;
    double f1 = fab*a1*b1+fac*a1*c1+fbc*b1*c1-faa*a1*a1-fbb*b1*b1-fcc*c1*c1;
    double f2 = fab*a2*b2+fac*a2*c2+fbc*b2*c2-faa*a2*a2-fbb*b2*b2-fcc*c2*c2;
    double f3 = fab*a3*b3+fac*a3*c3+fbc*b3*c3-faa*a3*a3-fbb*b3*b3-fcc*c3*c3;

    // Critical points p1 and p2 such that p0, p1, and p2 are orthogonal.
    // We use the analytic solution available for 2D steering functions.
    double denom = 2.0*f1-f2-f3;
    if (denom<DET_SMALL && -denom<DET_SMALL)
      denom = DET_SMALL;
    double theta = 0.5*atan(SQRT3*(f2-f3)/denom);
    double ctheta = cos(theta);
    double stheta = sin(theta);
    a1 = ctheta*ar+stheta*as;
    b1 = ctheta*br+stheta*bs;
    c1 = ctheta*cr+stheta*cs;
    a2 = stheta*ar-ctheta*as;
    b2 = stheta*br-ctheta*bs;
    c2 = stheta*cr-ctheta*cs;

    // Function values for p1 and p2.
    f1 = fab*a1*b1+fac*a1*c1+fbc*b1*c1-faa*a1*a1-fbb*b1*b1-fcc*c1*c1;
    f2 = fab*a2*b2+fac*a2*c2+fbc*b2*c2-faa*a2*a2-fbb*b2*b2-fcc*c2*c2;

    // Convert to the equivalent steering function.
    f0 = fsum+2.0*f0;
    f1 = fsum+2.0*f1;
    f2 = fsum+2.0*f2;

    // Order the three points such that f0 <= f1 <= f2.
    if (f0>f1) {
      double at = a0; a0 = a1; a1 = at;
      double bt = b0; b0 = b1; b1 = bt;
      double ct = c0; c0 = c1; c1 = ct;
      double ft = f0; f0 = f1; f1 = ft;
    }
    if (f1>f2) {
      double at = a1; a1 = a2; a2 = at;
      double bt = b1; b1 = b2; b2 = bt;
      double ct = c1; c1 = c2; c2 = ct;
      double ft = f1; f1 = f2; f2 = ft;
    }
    if (f0>f1) {
      double at = a0; a0 = a1; a1 = at;
      double bt = b0; b0 = b1; b1 = bt;
      double ct = c0; c0 = c1; c1 = ct;
      double ft = f0; f0 = f1; f1 = ft;
    }

    // Results.
    if (abcf==null) abcf = new double[3][4];
    abcf[0][0] = a0; abcf[0][1] = b0; abcf[0][2] = c0; abcf[0][3] = f0;
    abcf[1][0] = a1; abcf[1][1] = b1; abcf[1][2] = c1; abcf[1][3] = f1;
    abcf[2][0] = a2; abcf[2][1] = b2; abcf[2][2] = c2; abcf[2][3] = f2;
    return abcf;
  }
  
  /**
   * Applies forward 2D Fourier transform.
   * @param level level number.
   * @param x input 2D image.
   * @return a 2D complex array, which is the forward 2D Fourier transform of
   * the input image.
   */
  private float[][] ftForward(int level, float[][] x) {
    FftReal fft1;
    FftComplex fft2;
    int ny2 = x.length;
    int ny1 = x[0].length;
    int mpad = round(20.0f/(1.0f+(float)level));
    int lfactor = (int)pow(2.0,(double)level);
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    int nf1 = FftReal.nfftSmall(nl1+mpad*2);
    int nf1c = nf1/2+1;
    int nf2 = FftComplex.nfftSmall(nl2+mpad*2);
    float[][] xr = zerofloat(nf1,nf2);
    copy(ny1,ny2,0,0,x,mpad,mpad,xr);
    float[][] cx = czerofloat(nf1c,nf2);
    fft1 = new FftReal(nf1);
    fft2 = new FftComplex(nf2);
    fft1.realToComplex1(1,nf2,xr,cx);
    flipSign(2,cx);
    fft2.complexToComplex2(1,nf1c,cx,cx);
    return cx;
  }
  
    /**
   * Applies forward 3D Fourier transform.
   * @param level level number.
   * @param x input 3D image.
   * @return a 3D complex array, which is the forward 3D Fourier transform of
   * the input image.
   */
  private float[][][] ftForward(int level,float[][][] x) {
    FftReal fft1;
    FftComplex fft2;
    FftComplex fft3;
    int ny3 = x.length;
    int ny2 = x[0].length;
    int ny1 = x[0][0].length;
    int mpad = round(20.0f/(1.0f+(float)level));
    int lfactor = (int)pow(2.0,(double)level);
    int nl3 = (n3-1)/lfactor+1;
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    int nf3 = FftComplex.nfftSmall(nl3+mpad*2);
    int nf2 = FftComplex.nfftSmall(nl2+mpad*2);
    int nf1 = FftReal.nfftSmall(nl1+mpad*2);
    int nf1c = nf1/2+1;
    float[][][] xr = zerofloat(nf1,nf2,nf3);
    copy(ny1,ny2,ny3,0,0,0,x,mpad,mpad,mpad,xr);
    float[][][] cx = czerofloat(nf1c,nf2,nf3);
    fft1 = new FftReal(nf1);
    fft2 = new FftComplex(nf2);
    fft3 = new FftComplex(nf3);
    fft1.realToComplex1(1,nf2,nf3,xr,cx);
    flipSign(2, cx);
    fft2.complexToComplex2(1,nf1c,nf3,cx,cx);
    flipSign(3, cx);
    fft3.complexToComplex3(1,nf1c,nf2,cx,cx);
    return cx;
  }
  
  /**
   * Applies inverse 2D Fourier transform to an input wavenumber-domain image.
   * The output space-domain image is written to the appropriate part of the
   * steerable pyramid array.
   * @param lev level number.
   * @param dir basis filter orientation index for the input image.
   * @param cf input image in wavenumber domain (complex array).
   * @param spyr input/output 2D steerable pyramid.
   */
  private void ftInverse(int lev,int dir,
                         float[][] cf,float spyr[][][][]) {
    FftReal fft1;
    FftComplex fft2;
    int nf2 = cf.length;
    int nf1c = cf[0].length/2;
    int nf1 = (nf1c-1)*2;
    int mpad = round(20.0f/(1.0f+(float)lev));
    int lfactor = (int)pow(2.0,(double)lev);
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    fft1 = new FftReal(nf1);
    fft2 = new FftComplex(nf2);
    fft2.complexToComplex2(-1,nf1c,cf,cf);
    flipSign(2,cf);
    fft2.scale(nf1c,nf2,cf);
    fft1.complexToReal1(-1,nf2,cf,cf);
    fft1.scale(nf1,nf2,cf);
    copy(nl1,nl2,mpad,mpad,1,1,cf,0,0,1,1,spyr[lev][dir]);
  }
  
  /**
   * Applies inverse 3D Fourier transform to an input wavenumber-domain image.
   * The output space-domain image is written to the appropriate part of the
   * steerable pyramid array.
   * @param lev level number.
   * @param dir basis filter orientation index for the input image.
   * @param cf input image in wavenumber domain (complex array).
   * @param spyr input/output 3D steerable pyramid.
   */
  private void ftInverse(int lev,int dir,
                         float[][][] cf,float spyr[][][][][]) {
    FftReal fft1;
    FftComplex fft2;
    FftComplex fft3;
    int nf3 = cf.length;
    int nf2 = cf[0].length;
    int nf1c = cf[0][0].length/2;
    int nf1 = (nf1c-1)*2;
    int mpad = round(20.0f/(1.0f+(float)lev));
    int lfactor = (int)pow(2.0,(double)lev);
    int nl3 = (n3-1)/lfactor+1;
    int nl2 = (n2-1)/lfactor+1;
    int nl1 = (n1-1)/lfactor+1;
    fft1 = new FftReal(nf1);
    fft2 = new FftComplex(nf2);
    fft3 = new FftComplex(nf3);
    fft3.complexToComplex3(-1,nf1c,nf2,cf,cf);
    flipSign(3, cf);
    fft3.scale(nf1c,nf2,nf3,cf);
    fft2.complexToComplex2(-1,nf1c,nf3,cf,cf);
    flipSign(2, cf);
    fft2.scale(nf1c,nf2,nf3,cf);
    fft1.complexToReal1(-1,nf2,nf3,cf,cf);
    fft1.scale(nf1,nf2,nf3,cf);
    copy(nl1,nl2,nl3,mpad,mpad,mpad,1,1,1,cf,
        0,0,0,1,1,1,spyr[lev][dir]);
  }
  
  /**
   * Multiplies every other sample in input space-domain image by -1. 
   * Applied before forward Fourier transform where it has the effect of
   * shifting the image by -PI in the wavenumber domain.  This is done to
   * center the image after Fourier transform.  The sign-reversal is backed
   * out after inverse Fourier transform.
   * @param a direction of Fourier transform (1=x1 direction,2=x2).
   * @param x input/output 2D array.
   */
  private void flipSign(int a, float[][] x) {
    int ir,ii;
    int nx2 = x.length;
    int nx1 = x[0].length;
    if (a==1) {
      for (int i2=0; i2<nx2; ++i2) {
        for (int i1=0; i1<nx1/4; ++i1) {
          ir = 4*i1;
          ii = ir+1;
          x[i2][ir] *= -1.0f;
          x[i2][ii] *= -1.0f;
        }
      }
    }
    else if (a == 2) {
      for (int i2=0; i2<nx2/2; ++i2) {
        for (int i1=0; i1<nx1/2; ++i1) {
          ir = 2*i1;
          ii = ir+1;
          x[2*i2][ir] *= -1.0f;
          x[2*i2][ii] *= -1.0f;
        }
      }
    }
  }
  /**
   * Multiplies every other sample in input space-domain image by -1. 
   * Applied before forward Fourier transform where it has the effect of
   * shifting the image by -PI in the wavenumber domain.  This is done to
   * center the image after Fourier transform.  The sign-reversal is backed
   * out after inverse Fourier transform.
   * @param a direction of Fourier transform (1=x1 direction,2=x2,3=x3).
   * @param x input/output 3D array.
   */
  private void flipSign(int a, float[][][] x) {
    int ir,ii,b;
    int l3 = x.length;
    int l2 = x[0].length;
    int l1 = x[0][0].length/2;
  
    if (a == 1) {
      for (int i1=0; i1<(int)Math.floor((double)l1/2.0); ++i1) {
        for (int i3=0; i3<l3; ++i3) {
          for (int i2=0; i2<l2; ++i2) {
            ir = 4*i1;
            ii = ir+1;
            x[i3][i2][ir] *= -1.0f;
            x[i3][i2][ii] *= -1.0f;
          }
        }
      }
    }
    else if (a == 2) {
      for (int i2=0; i2<(int)Math.floor((double)l2/2.0); ++i2) {
        for (int i3=0; i3<l3; ++i3) {
          for (int i1=0; i1<l1; ++i1) {
            ir = 2*i1;
            ii = ir+1;
            b = 2*i2;
            x[i3][b][ir] *= -1.0f;
            x[i3][b][ii] *= -1.0f;
          }
        }
      }
    }
    else if (a == 3) {
      for (int i3=0; i3<(int)Math.floor((double)l3/2.0); ++i3) {
        for (int i2=0; i2<l2; ++i2) {
          for (int i1=0; i1<l1; ++i1) {
            ir = 2*i1;
            ii = ir+1;
            b = 2*i3;
            x[b][i2][ir] *= -1.0f;
            x[b][i2][ii] *= -1.0f;
          }
        }
      }
    }
  }
}
