/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Stopwatch;

/**
 * Benchmark different methods for multi-dimensional recursive filtering.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.11.17
 */
public class RecursiveFilterBench {

  public static void main(String[] args) {
    double maxtime = 5;
    int n1 = 4000;
    int n2 = 4000;
    float b0 =  2.00f;
    float b1 = -3.20f;
    float b2 =  1.28f;
    float a1 = -1.80f;
    float a2 =  0.81f;
    float[][] x = rampfloat(0.0f,0.0f,1.0f,n1,n2);
    float[][] y = zerofloat(n1,n2);
    double mflop = 9.0*n1*n2*1.0e-6;
    double rate,sum;
    int n;
    Stopwatch sw = new Stopwatch();
    for (int niter=0; niter<5; ++niter) {
      sw.restart();
      for (n=0;  sw.time()<maxtime; ++n)
        filter1(b0,b1,b2,a1,a2,x,y);
      sw.stop();
      sum = sum(y);
      rate = n*mflop/sw.time();
      System.out.println("filter1: rate="+rate+" sum="+sum);

      sw.restart();
      for (n=0;  sw.time()<maxtime; ++n)
        filter2a(b0,b1,b2,a1,a2,x,y);
      sw.stop();
      sum = sum(y);
      rate = n*mflop/sw.time();
      System.out.println("filter2a: rate="+rate+" sum="+sum);

      sw.restart();
      for (n=0;  sw.time()<maxtime; ++n)
        filter2b(b0,b1,b2,a1,a2,x,y);
      sw.stop();
      sum = sum(y);
      rate = n*mflop/sw.time();
      System.out.println("filter2b: rate="+rate+" sum="+sum);
    }
  }

  static void filter(
    float b0, float b1, float b2, float a1, float a2,
    float[] x, float[] y) 
  {
    int n = y.length;
    float yim2 = 0.0f;
    float yim1 = 0.0f;
    float xim2 = 0.0f;
    float xim1 = 0.0f;
    for (int i=0; i<n; ++i) {
      float xi = x[i];
      float yi = b0*xi+b1*xim1+b2*xim2-a1*yim1-a2*yim2;
      y[i] = yi;
      yim2 = yim1;
      yim1 = yi;
      xim2 = xim1;
      xim1 = xi;
    }
  }

  static void filter1(
    float b0, float b1, float b2, float a1, float a2,
    float[][] x, float[][] y) 
  {
    int n2 = y.length;
    for (int i2=0; i2<n2; ++i2) {
      filter(b0,b1,b2,a1,a2,x[i2],y[i2]);
    }
  }

  static void filter2a(
    float b0, float b1, float b2, float a1, float a2,
    float[][] x, float[][] y) 
  {
    int n2 = y.length;
    int n1 = y[0].length;
    float[] xt = new float[n2];
    float[] yt = new float[n2];
    for (int i1=0; i1<n1; ++i1) {
      for (int i2=0; i2<n2; ++i2) {
        xt[i2] = x[i2][i1];
      }
      filter(b0,b1,b2,a1,a2,xt,yt);
      for (int i2=0; i2<n2; ++i2) {
        y[i2][i1] = yt[i2];
      }
    }
  }

  static void filter2b(
    float b0, float b1, float b2, float a1, float a2,
    float[][] x, float[][] y) 
  {
    int n2 = y.length;
    int n1 = y[0].length;
    float[] yim2 = new float[n1];
    float[] yim1 = new float[n1];
    float[] xim2 = new float[n1];
    float[] xim1 = new float[n1];
    float[] xi = new float[n1];
    for (int i2=0; i2<n2; ++i2) {
      float[] xi2 = x[i2];
      float[] yi  = y[i2];
      for (int i1=0; i1<n1; ++i1) {
        xi[i1] = xi2[i1];
        yi[i1] = b0*xi[i1]+b1*xim1[i1]+b2*xim2[i1] -
                           a1*yim1[i1]-a2*yim2[i1];
      }
      yim2 = yim1;
      yim1 = yi;
      float[] xt = xim2;
      xim2 = xim1;
      xim1 = xi;
      xi = xt;
    }
  }
}
