/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
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

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.FftFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.12.17
 */
public class FftFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(FftFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1Random() {
    int ntest = 1000;
    int nmin = 1;
    int nmax = 8;
    for (int itest=0; itest<ntest; ++itest) {
      int nh = nmin+_random.nextInt(1+nmax-nmin);
      int nx = nmin+_random.nextInt(1+nmax-nmin);
      int ny = nx;
      int nz = nx;
      int kh = _random.nextInt(nh);
      float[] h = randfloat(nh);
      float[] x = randfloat(nx);
      float[] y = randfloat(ny);
      float[] z = randfloat(nz);
      FftFilter ff = new FftFilter(kh,h);
      ff.apply(x,y);
      Conv.conv(nh,-kh,h,nx,0,x,nz,0,z);
      assertEquals(z,y);
    }
  }

  public void test2Random() {
    int ntest = 1000;
    int nmin = 1;
    int nmax = 8;
    for (int itest=0; itest<ntest; ++itest) {
      int nh1 = nmin+_random.nextInt(1+nmax-nmin);
      int nh2 = nmin+_random.nextInt(1+nmax-nmin);
      int nx1 = nmin+_random.nextInt(1+nmax-nmin);
      int nx2 = nmin+_random.nextInt(1+nmax-nmin);
      int ny1 = nx1;
      int ny2 = nx2;
      int nz1 = nx1;
      int nz2 = nx2;
      int kh1 = _random.nextInt(nh1);
      int kh2 = _random.nextInt(nh2);
      float[][] h = randfloat(nh1,nh2);
      float[][] x = randfloat(nx1,nx2);
      float[][] y = randfloat(ny1,ny2);
      float[][] z = randfloat(nz1,nz2);
      FftFilter ff = new FftFilter(kh1,kh2,h);
      ff.apply(x,y);
      Conv.conv(nh1,nh2,-kh1,-kh2,h,nx1,nx2,0,0,x,nz1,nz2,0,0,z);
      assertEquals(z,y);
    }
  }

  public void test3Random() {
    int ntest = 100;
    int nmin = 1;
    int nmax = 8;
    for (int itest=0; itest<ntest; ++itest) {
      int nh1 = nmin+_random.nextInt(1+nmax-nmin);
      int nh2 = nmin+_random.nextInt(1+nmax-nmin);
      int nh3 = nmin+_random.nextInt(1+nmax-nmin);
      int nx1 = nmin+_random.nextInt(1+nmax-nmin);
      int nx2 = nmin+_random.nextInt(1+nmax-nmin);
      int nx3 = nmin+_random.nextInt(1+nmax-nmin);
      int ny1 = nx1;
      int ny2 = nx2;
      int ny3 = nx3;
      int nz1 = nx1;
      int nz2 = nx2;
      int nz3 = nx3;
      int kh1 = _random.nextInt(nh1);
      int kh2 = _random.nextInt(nh2);
      int kh3 = _random.nextInt(nh3);
      float[][][] h = randfloat(nh1,nh2,nh3);
      float[][][] x = randfloat(nx1,nx2,nx3);
      float[][][] y = randfloat(ny1,ny2,ny3);
      float[][][] z = randfloat(nz1,nz2,nz3);
      FftFilter ff = new FftFilter(kh1,kh2,kh3,h);
      ff.apply(x,y);
      Conv.conv(nh1,nh2,nh3,-kh1,-kh2,-kh3,h,
                nx1,nx2,nx3,0,0,0,x,
                nz1,nz2,nz3,0,0,0,z);
      assertEquals(z,y);
    }
  }

  private Random _random = new Random();

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
