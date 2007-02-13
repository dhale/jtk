/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.MathPlus.*;

import edu.mines.jtk.util.Check;

/**
 * Local estimates of orientations of features in images.
 * Methods of this class can compute for each image sample numerous
 * parameters related to orientation. All orientation information 
 * is derived from eigenvectors and eigenvalues of the structure tensor. 
 * This tensor is equivalent to a matrix of 2nd partial derivatives of an
 * autocorrelation evaluated at zero lag. In other words, orientation is 
 * here determined by the (2-D) ellipse or (3-D) ellipsoid that best fits 
 * the peak of the autocorrelation of image samples in a local window.
 * <p>
 * The coordinate system for a 2-D image has two orthogonal axes 1 and 2, 
 * which correspond to the 1st and 2nd indices of the array containing 
 * image samples. For 2-D images, the eigenvectors are the unit vectors 
 * u = (u1,u2) and v = (v1,v2). The 1st eigenvector u is perpendicular 
 * to the best fitting line, and the 1st component u1 of u is always 
 * non-negative. The 2nd eigenvector v is perpendicular to u such that 
 * the cross product u1*v2-u2*v1 = 1; that is, v1 = -u2 and v2 = u1. 
 * The angle theta = asin(u2) is the angle between the 1st eigenvector 
 * u and axis 1; -pi/2 &lt;= theta &lt;= pi/2.
 * <p>
 * The coordinate system for a 3-D image has three orthogonal axes 1, 2 
 * and 3, which correspond to the 1st, 2nd and 3rd indices of the array 
 * containing image samples. For 3-D images, the eigenvectors are unit 
 * vectors u = (u1,u2,u3), v = (v1,v2,v3), and dw = (w1,w2,w3). The 1st 
 * eigenvector u is orthogonal to the best fitting plane, and the 1st 
 * component u1 of u is always non-negative. The 2nd eigenvector v is 
 * orthogonal to the best fitting line within the best fitting plane.
 * The 3rd eigenvector w is the cross product of u and v. The dip angle 
 * theta = acos(u1) is the angle between the 1st eigenvector u and axis 1; 
 * 0 &lt;= theta &lt;= pi/2. The azimuthal angle phi = atan2(u3,u2)
 * is well-defined for only non-zero theta; -pi &lt;= phi &lt;= pi.
 * <p>
 * The local linearity or planarity of features is determined by the
 * eigenvalues. For 2-D images with eigenvalues eu and ev corresponding 
 * to the eigenvectors u and v, linearity is (eu-ev)/(eu+ev). For 3-D
 * images with eigenvalues eu, ev, and ew, planarity is (eu-ev)/(eu+ev)
 * and linearity is (ev-ew)/(ev+ew). Both linearity and planarity are
 * in the range [0,1].
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.02.12
 */
public class LocalOrientFilter {

  public LocalOrientFilter(double sigma) {
    _sigma = sigma;
    _rgfGradient = new RecursiveGaussianFilter(1.0);
    _rgfSmoother = new RecursiveGaussianFilter(sigma);
  }

  /**
   * Applies this filter for the specified image and outputs. All
   * outputs are optional and are computed for only non-null arrays.
   * @param x input array for 2-D image
   * @param theta orientation angle = asin(u2).
   * @param u1 1st component of 1st eigenvector.
   * @param u2 2nd component of 1st eigenvector.
   * @param v1 1st component of 2nd eigenvector.
   * @param v2 2nd component of 2nd eigenvector.
   * @param eu largest eigenvalue corresponding to the eigenvector u.
   * @param ev smallest eigenvalue corresponding to the eigenvector v.
   * @param el (eu-ev)/(eu+ev), a measure of linearity.
   */
  public void apply(float[][] x,
    float[][] theta,
    float[][] u1, float[][] u2, 
    float[][] v1, float[][] v2,
    float[][] eu, float[][] ev, 
    float[][] el)
  {
    // Where possible, use output arrays for workspace.
    float[][][] t = new float[8][][];
    int nt = 0;
    if (theta!=null) t[nt++] = theta;
    if (u1!=null) t[nt++] = u1;
    if (u2!=null) t[nt++] = u2;
    if (v1!=null) t[nt++] = v1;
    if (v2!=null) t[nt++] = v2;
    if (eu!=null) t[nt++] = eu;
    if (ev!=null) t[nt++] = ev;
    if (el!=null) t[nt++] = el;

    // Gradient.
    int n1 = x[0].length;
    int n2 = x.length;
    float[][] g1 = (nt>0)?t[0]:new float[n2][n1];
    float[][] g2 = (nt>1)?t[1]:new float[n2][n1];
    _rgfGradient.apply1X(x,g1);
    _rgfGradient.applyX1(x,g2);

    // Gradient products.
    float[][] g11 = (nt>2)?t[2]:new float[n2][n1];
    float[][] g12 = g1;
    float[][] g22 = g2;
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        float g1i = g1[i2][i1];
        float g2i = g2[i2][i1];
        g11[i2][i1] = g1i*g1i;
        g12[i2][i1] = g1i*g2i;
        g22[i2][i1] = g2i*g2i;
      }
    }
    
    // Smoothed gradient products comprise the structure tensor.
    float[][] gtt = (nt>3)?t[3]:new float[n2][n1];
    _rgfSmoother.apply0X(g11,gtt);
    _rgfSmoother.applyX0(gtt,g11);
    _rgfSmoother.apply0X(g12,gtt);
    _rgfSmoother.applyX0(gtt,g12);
    _rgfSmoother.apply0X(g22,gtt);
    _rgfSmoother.applyX0(gtt,g22);

    // For each sample, the eigenvector corresponding to the largest
    // eigenvalue is normal to the plane. The size of that eigenvalue
    // is a measure of planarity in the interval [0,1].
    float[][] a = new float[2][2];
    float[][] z = new float[2][2];
    float[] e = new float[2];
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        a[0][0] = g11[i2][i1];
        a[0][1] = g12[i2][i1];
        a[1][0] = g12[i2][i1];
        a[1][1] = g22[i2][i1];
        Eigen.solveSymmetric22(a,z,e);
        float u1i = z[0][0];
        float u2i = z[0][1];
        float v1i = z[1][0];
        float v2i = z[1][1];
        if (u1i<0.0f) {
          u1i = -u1i;
          u2i = -u2i;
        }
        if (v2i<0.0f) {
          v1i = -v1i;
          v2i = -v2i;
        }
        if (theta!=null) theta[i2][i1] = asin(u2i);
        if (u1!=null) u1[i2][i1] = u1i;
        if (u2!=null) u2[i2][i1] = u2i;
        if (v1!=null) v1[i2][i1] = v1i;
        if (v2!=null) v2[i2][i1] = v2i;
        if (eu!=null) eu[i2][i1] = e[0];
        if (ev!=null) ev[i2][i1] = e[1];
        if (el!=null) el[i2][i1] = (e[0]-e[1])/(e[0]+e[1]);
      }
    }
  }

  /**
   * Applies this filter for the specified image and outputs. All
   * outputs are optional and are computed for only non-null arrays.
   * @param x input array for 3-D image.
   * @param theta orientation dip angle; 0 &lt;= theta &lt;= pi/2.
   * @param phi orientation azimuthal angle; -pi &lt;= phi &lt;= pi.
   * @param u1 1st component of 1st eigenvector.
   * @param u2 2nd component of 1st eigenvector.
   * @param u3 3rd component of 1st eigenvector.
   * @param v1 1st component of 2nd eigenvector.
   * @param v2 2nd component of 2nd eigenvector.
   * @param v3 3rd component of 2nd eigenvector.
   * @param w1 1st component of 3rd eigenvector.
   * @param w2 2nd component of 3rd eigenvector.
   * @param w3 3rd component of 3rd eigenvector.
   * @param eu largest eigenvalue corresponding to the eigenvector u.
   * @param ev middle eigenvalue corresponding to the eigenvector v.
   * @param ew smallest eigenvalue corresponding to the eigenvector w.
   * @param ep (eu-ev)/(eu+ev), a measure of planarity.
   * @param el (ev-ew)/(ev+ew), a measure of linearity.
   */
  private void apply(float[][][] x,
    float[][][] theta, float[][][] phi,
    float[][][] u1, float[][][] u2, float[][][] u3, 
    float[][][] v1, float[][][] v2, float[][][] v3, 
    float[][][] w1, float[][][] w2, float[][][] w3, 
    float[][][] eu, float[][][] ev, float[][][] ew, 
    float[][][] ep, float[][][] el)
  {
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _sigma;
  private RecursiveGaussianFilter _rgfGradient;
  private RecursiveGaussianFilter _rgfSmoother;
}
