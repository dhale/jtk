/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.Conv}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class ConvTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ConvTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1Random() {
    int ntest = 1000;
    int kmin = -2;
    int kmax =  2;
    int lmin = 1;
    int lmax = 8;
    for (int itest=0; itest<ntest; ++itest) {
      int lx = lmin+_random.nextInt(1+lmax-lmin);
      int ly = lmin+_random.nextInt(1+lmax-lmin);
      int lz = lmin+_random.nextInt(1+lmax-lmin);
      int kx = kmin+_random.nextInt(1+kmax-kmin);
      int ky = kmin+_random.nextInt(1+kmax-kmin);
      int kz = kmin+_random.nextInt(1+kmax-kmin);
      float[] x = randfloat(lx);
      float[] y = randfloat(ly);
      float[] zs = zerofloat(lz);
      float[] zf = zerofloat(lz);
      if (_random.nextBoolean()) {
        y = x; 
        ly = lx; ky = kx;
      }

      convSimple(lx,kx,x,ly,ky,y,lz,kz,zs);
      Conv.conv(lx,kx,x,ly,ky,y,lz,kz,zf);
      assertEquals(zs,zf);

      xcorSimple(lx,kx,x,ly,ky,y,lz,kz,zs);
      Conv.xcor(lx,kx,x,ly,ky,y,lz,kz,zf);
      assertEquals(zs,zf);
    }
  }

  public void test2Random() {
    int ntest = 1000;
    int kmin = -2;
    int kmax =  2;
    int lmin = 1;
    int lmax = 8;
    for (int itest=0; itest<ntest; ++itest) {
      int lx1 = lmin+_random.nextInt(1+lmax-lmin);
      int lx2 = lmin+_random.nextInt(1+lmax-lmin);
      int ly1 = lmin+_random.nextInt(1+lmax-lmin);
      int ly2 = lmin+_random.nextInt(1+lmax-lmin);
      int lz1 = lmin+_random.nextInt(1+lmax-lmin);
      int lz2 = lmin+_random.nextInt(1+lmax-lmin);
      int kx1 = kmin+_random.nextInt(1+kmax-kmin);
      int kx2 = kmin+_random.nextInt(1+kmax-kmin);
      int ky1 = kmin+_random.nextInt(1+kmax-kmin);
      int ky2 = kmin+_random.nextInt(1+kmax-kmin);
      int kz1 = kmin+_random.nextInt(1+kmax-kmin);
      int kz2 = kmin+_random.nextInt(1+kmax-kmin);
      float[][] x = randfloat(lx1,lx2);
      float[][] y = randfloat(ly1,ly2);
      float[][] zs = zerofloat(lz1,lz2);
      float[][] zf = zerofloat(lz1,lz2);
      if (_random.nextBoolean()) {
        y = x; 
        ly1 = lx1; ky1 = kx1; 
        ly2 = lx2; ky2 = kx2;
      }

      convSimple(lx1,lx2,kx1,kx2,x,ly1,ly2,ky1,ky2,y,lz1,lz2,kz1,kz2,zs);
      Conv.conv(lx1,lx2,kx1,kx2,x,ly1,ly2,ky1,ky2,y,lz1,lz2,kz1,kz2,zf);
      assertEquals(zs,zf);

      xcorSimple(lx1,lx2,kx1,kx2,x,ly1,ly2,ky1,ky2,y,lz1,lz2,kz1,kz2,zs);
      Conv.xcor(lx1,lx2,kx1,kx2,x,ly1,ly2,ky1,ky2,y,lz1,lz2,kz1,kz2,zf);
      assertEquals(zs,zf);
    }
  }

  public void test3Random() {
    int ntest = 100;
    int kmin = -2;
    int kmax =  2;
    int lmin = 1;
    int lmax = 8;
    for (int itest=0; itest<ntest; ++itest) {
      int lx1 = lmin+_random.nextInt(1+lmax-lmin);
      int lx2 = lmin+_random.nextInt(1+lmax-lmin);
      int lx3 = lmin+_random.nextInt(1+lmax-lmin);
      int ly1 = lmin+_random.nextInt(1+lmax-lmin);
      int ly2 = lmin+_random.nextInt(1+lmax-lmin);
      int ly3 = lmin+_random.nextInt(1+lmax-lmin);
      int lz1 = lmin+_random.nextInt(1+lmax-lmin);
      int lz2 = lmin+_random.nextInt(1+lmax-lmin);
      int lz3 = lmin+_random.nextInt(1+lmax-lmin);
      int kx1 = kmin+_random.nextInt(1+kmax-kmin);
      int kx2 = kmin+_random.nextInt(1+kmax-kmin);
      int kx3 = kmin+_random.nextInt(1+kmax-kmin);
      int ky1 = kmin+_random.nextInt(1+kmax-kmin);
      int ky2 = kmin+_random.nextInt(1+kmax-kmin);
      int ky3 = kmin+_random.nextInt(1+kmax-kmin);
      int kz1 = kmin+_random.nextInt(1+kmax-kmin);
      int kz2 = kmin+_random.nextInt(1+kmax-kmin);
      int kz3 = kmin+_random.nextInt(1+kmax-kmin);
      float[][][] x = randfloat(lx1,lx2,lx3);
      float[][][] y = randfloat(ly1,ly2,ly3);
      float[][][] zs = zerofloat(lz1,lz2,lz3);
      float[][][] zf = zerofloat(lz1,lz2,lz3);
      if (_random.nextBoolean()) {
        y = x; 
        ly1 = lx1; ky1 = kx1; 
        ly2 = lx2; ky2 = kx2; 
        ly3 = lx3; ky3 = kx3;
      }

      convSimple(lx1,lx2,lx3,kx1,kx2,kx3,x,
                 ly1,ly2,ly3,ky1,ky2,ky3,y,
                 lz1,lz2,lz3,kz1,kz2,kz3,zs);
      Conv.conv(lx1,lx2,lx3,kx1,kx2,kx3,x,
                ly1,ly2,ly3,ky1,ky2,ky3,y,
                lz1,lz2,lz3,kz1,kz2,kz3,zf);
      assertEquals(zs,zf);

      xcorSimple(lx1,lx2,lx3,kx1,kx2,kx3,x,
                 ly1,ly2,ly3,ky1,ky2,ky3,y,
                 lz1,lz2,lz3,kz1,kz2,kz3,zs);
      Conv.xcor(lx1,lx2,lx3,kx1,kx2,kx3,x,
                ly1,ly2,ly3,ky1,ky2,ky3,y,
                lz1,lz2,lz3,kz1,kz2,kz3,zf);
      assertEquals(zs,zf);
    }
  }

  private Random _random = new Random();

  private static void convSimple(
    int lx, int kx, float[] x,
    int ly, int ky, float[] y,
    int lz, int kz, float[] z)
  {
    int ilo = kz-kx-ky;
    int ihi = ilo+lz-1;
    for (int i=ilo; i<=ihi; ++i) {
      int jlo = max(0,i-ly+1);
      int jhi = min(lx-1,i);
      float sum = 0.0f;
      for (int j=jlo; j<=jhi; ++j)
        sum += x[j]*y[i-j];
      z[i-ilo] = sum;
    }
  }

  private static void xcorSimple(
    int lx, int kx, float[] x,
    int ly, int ky, float[] y,
    int lz, int kz, float[] z)
  {
    int ilo = kz+kx-ky;
    int ihi = ilo+lz-1;
    for (int i=ilo; i<=ihi; ++i) {
      int jlo = max(0,-i);
      int jhi = min(lx-1,ly-1-i);
      float sum = 0.0f;
      for (int j=jlo; j<=jhi; ++j)
        sum += x[j]*y[i+j];
      z[i-ilo] = sum;
    }
  }

  private static void convSimple(
    int lx1, int lx2, int kx1, int kx2, float[][] x,
    int ly1, int ly2, int ky1, int ky2, float[][] y,
    int lz1, int lz2, int kz1, int kz2, float[][] z)
  {
    int ilo1 = kz1-kx1-ky1;
    int ihi1 = ilo1+lz1-1;
    int ilo2 = kz2-kx2-ky2;
    int ihi2 = ilo2+lz2-1;
    for (int i2=ilo2; i2<=ihi2; ++i2) {
      for (int i1=ilo1; i1<=ihi1; ++i1) {
        int jlo1 = max(0,i1-ly1+1);
        int jhi1 = min(lx1-1,i1);
        int jlo2 = max(0,i2-ly2+1);
        int jhi2 = min(lx2-1,i2);
        float sum = 0.0f;
        for (int j2=jlo2; j2<=jhi2; ++j2) {
          for (int j1=jlo1; j1<=jhi1; ++j1) {
            sum += x[j2][j1]*y[i2-j2][i1-j1];
          }
        }
        z[i2-ilo2][i1-ilo1] = sum;
      }
    }
  }

  private static void xcorSimple(
    int lx1, int lx2, int kx1, int kx2, float[][] x,
    int ly1, int ly2, int ky1, int ky2, float[][] y,
    int lz1, int lz2, int kz1, int kz2, float[][] z)
  {
    int ilo1 = kz1+kx1-ky1;
    int ihi1 = ilo1+lz1-1;
    int ilo2 = kz2+kx2-ky2;
    int ihi2 = ilo2+lz2-1;
    for (int i2=ilo2; i2<=ihi2; ++i2) {
      for (int i1=ilo1; i1<=ihi1; ++i1) {
        int jlo1 = max(0,-i1);
        int jhi1 = min(lx1-1,ly1-1-i1);
        int jlo2 = max(0,-i2);
        int jhi2 = min(lx2-1,ly2-1-i2);
        float sum = 0.0f;
        for (int j2=jlo2; j2<=jhi2; ++j2) {
          for (int j1=jlo1; j1<=jhi1; ++j1) {
            sum += x[j2][j1]*y[i2+j2][i1+j1];
          }
        }
        z[i2-ilo2][i1-ilo1] = sum;
      }
    }
  }

  private static void convSimple(
    int lx1, int lx2, int lx3, int kx1, int kx2, int kx3, float[][][] x,
    int ly1, int ly2, int ly3, int ky1, int ky2, int ky3, float[][][] y,
    int lz1, int lz2, int lz3, int kz1, int kz2, int kz3, float[][][] z)
  {
    int ilo1 = kz1-kx1-ky1;
    int ilo2 = kz2-kx2-ky2;
    int ilo3 = kz3-kx3-ky3;
    int ihi1 = ilo1+lz1-1;
    int ihi2 = ilo2+lz2-1;
    int ihi3 = ilo3+lz3-1;
    for (int i3=ilo3; i3<=ihi3; ++i3) {
      for (int i2=ilo2; i2<=ihi2; ++i2) {
        for (int i1=ilo1; i1<=ihi1; ++i1) {
          int jlo1 = max(0,i1-ly1+1);
          int jlo2 = max(0,i2-ly2+1);
          int jlo3 = max(0,i3-ly3+1);
          int jhi1 = min(lx1-1,i1);
          int jhi2 = min(lx2-1,i2);
          int jhi3 = min(lx3-1,i3);
          float sum = 0.0f;
          for (int j3=jlo3; j3<=jhi3; ++j3) {
            for (int j2=jlo2; j2<=jhi2; ++j2) {
              for (int j1=jlo1; j1<=jhi1; ++j1) {
                sum += x[j3][j2][j1]*y[i3-j3][i2-j2][i1-j1];
              }
            }
          }
          z[i3-ilo3][i2-ilo2][i1-ilo1] = sum;
        }
      }
    }
  }

  private static void xcorSimple(
    int lx1, int lx2, int lx3, int kx1, int kx2, int kx3, float[][][] x,
    int ly1, int ly2, int ly3, int ky1, int ky2, int ky3, float[][][] y,
    int lz1, int lz2, int lz3, int kz1, int kz2, int kz3, float[][][] z)
  {
    int ilo1 = kz1+kx1-ky1;
    int ilo2 = kz2+kx2-ky2;
    int ilo3 = kz3+kx3-ky3;
    int ihi1 = ilo1+lz1-1;
    int ihi2 = ilo2+lz2-1;
    int ihi3 = ilo3+lz3-1;
    for (int i3=ilo3; i3<=ihi3; ++i3) {
      for (int i2=ilo2; i2<=ihi2; ++i2) {
        for (int i1=ilo1; i1<=ihi1; ++i1) {
          int jlo1 = max(0,-i1);
          int jlo2 = max(0,-i2);
          int jlo3 = max(0,-i3);
          int jhi1 = min(lx1-1,ly1-1-i1);
          int jhi2 = min(lx2-1,ly2-1-i2);
          int jhi3 = min(lx3-1,ly3-1-i3);
          float sum = 0.0f;
          for (int j3=jlo3; j3<=jhi3; ++j3) {
            for (int j2=jlo2; j2<=jhi2; ++j2) {
              for (int j1=jlo1; j1<=jhi1; ++j1) {
                sum += x[j3][j2][j1]*y[i3+j3][i2+j2][i1+j1];
              }
            }
          }
          z[i3-ilo3][i2-ilo2][i1-ilo1] = sum;
        }
      }
    }
  }

  private static final float TOLERANCE = 1000.0f*FLT_EPSILON;
  private static void assertEquals(float[] a, float[] b) {
    int n = a.length;
    for (int i=0; i<n; ++i) {
      assertEquals(a[i],b[i],TOLERANCE);
    }
  }
  private static void assertEquals(float[][] a, float[][] b) {
    int n = a.length;
    for (int i=0; i<n; ++i) {
      assertEquals(a[i],b[i]);
    }
  }
  private static void assertEquals(float[][][] a, float[][][] b) {
    int n = a.length;
    for (int i=0; i<n; ++i) {
      assertEquals(a[i],b[i]);
    }
  }
}
