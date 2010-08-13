/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import static java.lang.Math.*;
import java.util.Random;

import edu.mines.jtk.dsp.Sampling;

/**
 * Functions for testing interpolation methods.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.06.13
 */
abstract class TestFunction {

  public static TestFunction makeSphericalQuadratic() {
    return new TestFunction() {
      public float f(float x1) {
        return 1.0f+2.0f*x1-3.0f*x1*x1;
      }
      public float f(float x1, float x2) {
        return 1.0f+2.0f*x1+3.0f*x2-6.0f*(x1*x1+x2*x2);
      }
      public float f(float x1, float x2, float x3) {
        return 1.0f+2.0f*x1+3.0f*x2+4.0f*x3-10.0f*(x1*x1+x2*x2+x3*x3);
      }
    };
  }

  public static TestFunction makeSine() {
    return new TestFunction() {
      public float f(float x1) {
        return (float)sin(PI*x1);
      }
      public float f(float x1, float x2) {
        return (float)(sin(PI*x1)*sin(PI*x2));
      }
      public float f(float x1, float x2, float x3) {
        return (float)(sin(PI*x1)*sin(PI*x2)*sin(PI*x3));
      }
    };
  }

  public static TestFunction makeLinear() {
    return new TestFunction() {
      public float f(float x1) {
        return x1;
      }
      public float f(float x1, float x2) {
        return x1+x2;
      }
      public float f(float x1, float x2, float x3) {
        return x1+x2+x3;
      }
    };
  }

  public abstract float f(float x1);
  public abstract float f(float x1, float x2);
  public abstract float f(float x1, float x2, float x3);

  public float[][] sampleScattered2(int n) {
    return sampleScattered2(n,0.0f,1.0f,0.0f,1.0f);
  }

  public float[][] sampleScattered3(int n) {
    return sampleScattered3(n,0.0f,1.0f,0.0f,1.0f,0.0f,1.0f);
  }

  public float[][] sampleScattered2(
    int n,
    float x1min, float x1max,
    float x2min, float x2max)
  {
    float[] f = new float[n];
    float[] x1 = new float[n];
    float[] x2 = new float[n];
    for (int i=0; i<n; ++i) {
      x1[i] = randomFloat(x1min,x1max);
      x2[i] = randomFloat(x2min,x2max);
      f[i] = f(x1[i],x2[i]);
    }
    return new float[][]{f,x1,x2};
  }

  public float[][] sampleScattered3(
    int n,
    float x1min, float x1max,
    float x2min, float x2max,
    float x3min, float x3max)
  {
    float[] f = new float[n];
    float[] x1 = new float[n];
    float[] x2 = new float[n];
    float[] x3 = new float[n];
    for (int i=0; i<n; ++i) {
      x1[i] = randomFloat(x1min,x1max);
      x2[i] = randomFloat(x2min,x2max);
      x3[i] = randomFloat(x3min,x3max);
      f[i] = f(x1[i],x2[i],x3[i]);
    }
    return new float[][]{f,x1,x2,x3};
  }

  public float[][] sampleUniform2(Sampling s1, Sampling s2) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    float[][] f = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      float x2 = (float)s2.getValue(i2);
      for (int i1=0; i1<n1; ++i1) {
        float x1 = (float)s1.getValue(i1);
        f[i2][i1] = f(x1,x2);
      }
    }
    return f;
  }

  public float[][][] sampleUniform2(Sampling s1, Sampling s2, Sampling s3) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s3.getCount();
    float[][][] f = new float[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      float x3 = (float)s3.getValue(i3);
      for (int i2=0; i2<n2; ++i2) {
        float x2 = (float)s2.getValue(i2);
        for (int i1=0; i1<n1; ++i1) {
          float x1 = (float)s1.getValue(i1);
          f[i3][i2][i1] = f(x1,x2,x3);
        }
      }
    }
    return f;
  }

  public float[][] sampleUniform2(
    int n,
    float x1min, float x1max,
    float x2min, float x2max)
  {
    float[] f = new float[n];
    float[] x1 = new float[n];
    float[] x2 = new float[n];
    int n1 = (int)(sqrt(n)+0.5);
    int n2 = 1+(n-1)/n1;
    Sampling s1 = new Sampling(n1,(x1max-x1min)/(n1-1),x1min);
    Sampling s2 = new Sampling(n2,(x2max-x2min)/(n2-1),x2min);
    for (int i2=0,i=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1 && i<n; ++i1,++i) {
        x1[i] = (float)s1.getValue(i1);
        x2[i] = (float)s2.getValue(i2);
        f[i] = f(x1[i],x2[i]);
      }
    }
    return new float[][]{f,x1,x2};
  }

  public float[][] sampleUniform3(
    int n,
    float x1min, float x1max,
    float x2min, float x2max,
    float x3min, float x3max)
  {
    float[] f = new float[n];
    float[] x1 = new float[n];
    float[] x2 = new float[n];
    float[] x3 = new float[n];
    int n1 = (int)(pow(n,0.33333)+0.5);
    int n2 = 1+(n-1)/(n1*n1);
    int n3 = 1+(n-1)/(n1*n2);
    Sampling s1 = new Sampling(n1,(x1max-x1min)/(n1-1),x1min);
    Sampling s2 = new Sampling(n2,(x2max-x2min)/(n2-1),x2min);
    Sampling s3 = new Sampling(n3,(x3max-x3min)/(n3-1),x3min);
    for (int i3=0,i=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1 && i<n; ++i1,++i) {
          x1[i] = (float)s1.getValue(i1);
          x2[i] = (float)s2.getValue(i2);
          x3[i] = (float)s3.getValue(i3);
          f[i] = f(x1[i],x2[i],x3[i]);
        }
      }
    }
    return new float[][]{f,x1,x2,x3};
  }

  ///////////////////////////////////////////////////////////////////////////
  // private
  //private Random _random = new Random(314);
  private Random _random = new Random();
  private float randomFloat() {
    return _random.nextFloat();
  }
  private float randomFloat(float xmin, float xmax) {
    return xmin+(xmax-xmin)*randomFloat();
  }
}
