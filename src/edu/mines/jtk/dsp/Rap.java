/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.MathPlus.*;

/**
 * Real array processing. A real array is an array of floats, in which
 * each float represents one real number.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class Rap {

  ///////////////////////////////////////////////////////////////////////////
  // copy
  public static float[] copy(float[] rx) {
    return copy(rx.length,rx);
  }
  public static float[][] copy(float[][] rx) {
    int n2 = rx.length;
    float[][] ry = new float[n2][];
    for (int i2=0; i2<n2; ++i2)
      ry[i2] = copy(rx[i2]);
    return ry;
  }
  public static float[][][] copy(float[][][] rx) {
    int n3 = rx.length;
    float[][][] ry = new float[n3][][];
    for (int i3=0; i3<n3; ++i3)
      ry[i3] = copy(rx[i3]);
    return ry;
  }
  public static float[] copy(int n1, float[] rx) {
    return copy(n1,rx,new float[n1]);
  }
  public static float[][] copy(int n1, int n2, float[][] rx) {
    return copy(n1,n2,rx,new float[n2][n1]);
  }
  public static float[][][] copy(int n1, int n2, int n3, float[][][] rx) {
    return copy(n1,n2,n3,rx,new float[n3][n2][n1]);
  }
  public static float[] copy(int n1, float[] rx, float[] ry) {
    for (int i1=0; i1<n1; ++i1)
      ry[i1] = rx[i1];
    return ry;
  }
  public static float[][] copy(int n1, int n2, float[][] rx, float[][] ry) {
    for (int i2=0; i2<n2; ++i2)
      copy(n1,rx[i2],ry[i2]);
    return ry;
  }
  public static float[][][] copy(
    int n1, int n2, int n3, float[][][] rx, float[][][] ry) {
    for (int i3=0; i3<n3; ++i3)
      copy(n1,n2,rx[i3],ry[i3]);
    return ry;
  }
  public static float[] copy(
    int n1, 
    int j1x, float[] rx, 
    int j1y, float[] ry) {
    for (int i1=0,ix=j1x,iy=j1y; i1<n1; ++i1)
      ry[iy++] = rx[ix++];
    return ry;
  }
  public static float[][] copy(
    int n1, int n2, 
    int j1x, int j2x, float[][] rx, 
    int j1y, int j2y, float[][] ry) {
    for (int i2=0; i2<n2; ++i2)
      copy(n1,j1x,rx[j2x+i2],j1y,ry[j2y+i2]);
    return ry;
  }
  public static float[][][] copy(
    int n1, int n2, int n3,
    int j1x, int j2x, int j3x, float[][][] rx, 
    int j1y, int j2y, int j3y, float[][][] ry) {
    for (int i3=0; i3<n3; ++i3)
      copy(n1,n2,j1x,j2x,rx[j3x+i3],j1y,j2y,ry[j3y+i3]);
    return ry;
  }

  ///////////////////////////////////////////////////////////////////////////
  // zero
  public static float[] zero(int n1) {
    return new float[n1];
  }
  public static float[][] zero(int n1, int n2) {
    return new float[n2][n1];
  }
  public static float[][][] zero(int n1, int n2, int n3) {
    return new float[n3][n2][n1];
  }
  public static float[] zero(int n1, float[] rx) {
    for (int i1=0; i1<n1; ++i1)
      rx[i1] = 0.0f;
    return rx;
  }
  public static float[][] zero(int n1, int n2, float[][] rx) {
    for (int i2=0; i2<n2; ++i2)
      zero(n1,rx[i2]);
    return rx;
  }
  public static float[][][] zero(int n1, int n2, int n3, float[][][] rx) {
    for (int i3=0; i3<n3; ++i3)
      zero(n1,n2,rx[i3]);
    return rx;
  }

  ///////////////////////////////////////////////////////////////////////////
  // fill
  public static float[] fill(float ra, int n1) {
    return fill(ra,n1,new float[n1]);
  }
  public static float[][] fill(float ra, int n1, int n2) {
    return fill(ra,n1,n2,new float[n2][n1]);
  }
  public static float[][][] fill(float ra, int n1, int n2, int n3) {
    return fill(ra,n1,n2,n3,new float[n3][n2][n1]);
  }
  public static float[] fill(float ra, int n1, float[] rx) {
    for (int i1=0; i1<n1; ++i1)
      rx[i1] = ra;
    return rx;
  }
  public static float[][] fill(float ra, int n1, int n2, float[][] rx) {
    for (int i2=0; i2<n2; ++i2)
      fill(ra,n1,rx[i2]);
    return rx;
  }
  public static float[][][] fill(
    float ra, int n1, int n2, int n3, float[][][] rx) {
    for (int i3=0; i3<n3; ++i3)
      fill(ra,n1,n2,rx[i3]);
    return rx;
  }

  ///////////////////////////////////////////////////////////////////////////
  // ramp
  public static float[] ramp(float ra, float rb, int n1) {
    return ramp(ra,rb,n1,new float[n1]);
  }
  public static float[][] ramp(
    float ra, float rb1, float rb2, int n1, int n2) {
    return ramp(ra,rb1,rb2,n1,n2,new float[n2][n1]);
  }
  public static float[][][] ramp(
    float ra, float rb1, float rb2, float rb3, 
    int n1, int n2, int n3) {
    return ramp(ra,rb1,rb2,rb3,n1,n2,n3,new float[n3][n2][n1]);
  }
  public static float[] ramp(float ra, float rb, int n1, float[] rx) {
    for (int i1=0; i1<n1; ++i1,ra+=rb)
      rx[i1] = ra;
    return rx;
  }
  public static float[][] ramp(
    float ra, float rb1, float rb2, int n1, int n2, float[][] rx) {
    float ra2 = ra;
    for (int i2=0; i2<n2; ++i2,ra2+=rb2)
      ramp(ra2,rb1,n1,rx[i2]);
    return rx;
  }
  public static float[][][] ramp(
    float ra, float rb1, float rb2, float rb3, 
    int n1, int n2, int n3, float[][][] rx) {
    float ra3 = ra;
    for (int i3=0; i3<n3; ++i3,ra3+=rb3)
      ramp(ra3,rb1,rb2,n1,n2,rx[i3]);
    return rx;
  }

  ///////////////////////////////////////////////////////////////////////////
  // add, sub, mul, div
  public static float[] add(int n1, float[] rx, float[] ry) {
    return add(n1,rx,ry,new float[2*n1]);
  }
  public static float[][] add(int n1, int n2, float[][] rx, float[][] ry) {
    return add(n1,n2,rx,ry,new float[n2][n1]);
  }
  public static float[][][] add(
    int n1, int n2, int n3, float[][][] rx, float[][][] ry) {
    return add(n1,n2,n3,rx,ry,new float[n3][n2][n1]);
  }
  public static float[] add(int n1, float[] rx, float[] ry, float[] rz) {
    for (int i1=0; i1<n1; ++i1)
      rz[i1] = rx[i1]+ry[i1];
    return rz;
  }
  public static float[][] add(
    int n1, int n2, float[][] rx, float[][] ry, float[][] rz) {
    for (int i2=0; i2<n2; ++i2)
      add(n1,rx[i2],ry[i2],rz[i2]);
    return rz;
  }
  public static float[][][] add(
    int n1, int n2, int n3, float[][][] rx, float[][][] ry, float[][][] rz) {
    for (int i3=0; i3<n3; ++i3)
      add(n1,n2,rx[i3],ry[i3],rz[i3]);
    return rz;
  }
}
