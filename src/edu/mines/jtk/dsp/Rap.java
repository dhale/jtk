/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import java.util.Random;
import static java.lang.Math.*;

/**
 * Real array processing. A real array is an array of floats, in which
 * each float represents one real number. Methods are overloaded for 1-D
 * arrays, 2-D arrays (arrays of arrays), and 3-D arrays (arrays of arrays
 * of arrays). Multi-dimensional arrays can be regular or ragged. For
 * example, the dimensions of a regular 3-D array float[n3][n2][n1] are
 * n1, n2, and n3, where n1 is the fastest dimension, and n3 is the
 * slowest dimension. In contrast, the lengths of arrays within a ragged 
 * array of arrays (of arrays) may vary.
 * <p>
 * Some methods that create new arrays (e.g., zero, fill, ramp, and 
 * rand) have no array arguments; these methods have arguments that 
 * specify regular array dimensions n1, n2, and/or n3. All other methods, 
 * those with at least one array argument, use the dimensions of the first 
 * array argument to determine the number of array elements to process.
 * <p>
 * Most methods in this class have well-known names and functions. All
 * methods share a common naming convention for arguments. Arguments 
 * with names like rx, ry, and rz denote real arrays. Arguments with 
 * names like ra and rb denote real constants.
 * <p>
 * Method summary:
 * <pre>
 * Copy and creation operations:
 * copy - copies an array, or a specified subset of that array
 * zero - fills an array with a constant value zero
 * fill - fills an array with a specified constant value
 * ramp - fills an array with a linear values a+b*index
 * rand - fills an array with pseudo-random numbers
 * </pre><pre>
 * Binary operations:
 * add - adds one array (or constant) to another array (or constant)
 * sub - subtracts one array (or constant) from another array (or constant)
 * mul - multiplies one array (or constant) by another array (or constant)
 * div - divides one array (or constant) by another array (or constant)
 * </pre><pre>
 * Unary operations:
 * abs - absolute value
 * neg - negation
 * cos - cosine
 * sin - sin
 * exp - exponential
 * log - natural logarithm
 * log10 - logarithm base 10
 * sqrt - square-root
 * sgn - sign (1 if positive, -1 if negative, 0 if zero)
 * pow - raises to a specified power
 * </pre><pre>
 * Other operations:
 * equal - compares arrays for equality (to within an optional tolerance)
 * findMax - returns the maximum value in an array and its index
 * findMin - returns the minimum value in an array and its index
 * </pre>
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
    float[] ry = new float[n1];
    copy(n1,rx,ry);
    return ry;
  }
  public static float[][] copy(int n1, int n2, float[][] rx) {
    float[][] ry = new float[n2][n1];
    copy(n1,n2,rx,ry);
    return ry;
  }
  public static float[][][] copy(int n1, int n2, int n3, float[][][] rx) {
    float[][][] ry = new float[n3][n2][n1];
    copy(n1,n2,n3,rx,ry);
    return ry;
  }
  public static void copy(float[] rx, float[] ry) {
    copy(rx.length,rx,ry);
  }
  public static void copy(float[][] rx, float[][] ry) {
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2)
      copy(rx[i2],ry[i2]);
  }
  public static void copy(float[][][] rx, float[][][] ry) {
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3)
      copy(rx[i3],ry[i3]);
  }
  public static void copy(int n1, float[] rx, float[] ry) {
    for (int i1=0; i1<n1; ++i1)
      ry[i1] = rx[i1];
  }
  public static void copy(int n1, int n2, float[][] rx, float[][] ry) {
    for (int i2=0; i2<n2; ++i2)
      copy(n1,rx[i2],ry[i2]);
  }
  public static void copy(
    int n1, int n2, int n3, float[][][] rx, float[][][] ry) {
    for (int i3=0; i3<n3; ++i3)
      copy(n1,n2,rx[i3],ry[i3]);
  }
  public static void copy(
    int n1, 
    int j1x, float[] rx, 
    int j1y, float[] ry) {
    for (int i1=0,ix=j1x,iy=j1y; i1<n1; ++i1)
      ry[iy++] = rx[ix++];
  }
  public static void copy(
    int n1, int n2, 
    int j1x, int j2x, float[][] rx, 
    int j1y, int j2y, float[][] ry) {
    for (int i2=0; i2<n2; ++i2)
      copy(n1,j1x,rx[j2x+i2],j1y,ry[j2y+i2]);
  }
  public static void copy(
    int n1, int n2, int n3,
    int j1x, int j2x, int j3x, float[][][] rx, 
    int j1y, int j2y, int j3y, float[][][] ry) {
    for (int i3=0; i3<n3; ++i3)
      copy(n1,n2,j1x,j2x,rx[j3x+i3],j1y,j2y,ry[j3y+i3]);
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
  public static void zero(float[] rx) {
    int n1 = rx.length;
    for (int i1=0; i1<n1; ++i1)
      rx[i1] = 0.0f;
  }
  public static void zero(float[][] rx) {
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2)
      zero(rx[i2]);
  }
  public static void zero(float[][][] rx) {
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3)
      zero(rx[i3]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // fill
  public static float[] fill(float ra, int n1) {
    float[] rx = new float[n1];
    fill(ra,rx);
    return rx;
  }
  public static float[][] fill(float ra, int n1, int n2) {
    float[][] rx = new float[n2][n1];
    fill(ra,rx);
    return rx;
  }
  public static float[][][] fill(float ra, int n1, int n2, int n3) {
    float[][][] rx = new float[n3][n2][n1];
    fill(ra,rx);
    return rx;
  }
  public static void fill(float ra, float[] rx) {
    int n1 = rx.length;
    for (int i1=0; i1<n1; ++i1)
      rx[i1] = ra;
  }
  public static void fill(float ra, float[][] rx) {
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2)
      fill(ra,rx[i2]);
  }
  public static void fill(float ra, float[][][] rx) {
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3)
      fill(ra,rx[i3]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // ramp
  public static float[] ramp(float ra, float rb, int n1) {
    float[] rx = new float[n1];
    ramp(ra,rb,rx);
    return rx;
  }
  public static float[][] ramp(
    float ra, float rb1, float rb2, int n1, int n2) {
    float[][] rx = new float[n2][n1];
    ramp(ra,rb1,rb2,rx);
    return rx;
  }
  public static float[][][] ramp(
    float ra, float rb1, float rb2, float rb3, int n1, int n2, int n3) {
    float[][][] rx = new float[n3][n2][n1];
    ramp(ra,rb1,rb2,rb3,rx);
    return rx;
  }
  public static void ramp(float ra, float rb, float[] rx) {
    int n1 = rx.length;
    for (int i1=0; i1<n1; ++i1,ra+=rb)
      rx[i1] = ra;
  }
  public static void ramp(float ra, float rb1, float rb2, float[][] rx) {
    int n2 = rx.length;
    float ra2 = ra;
    for (int i2=0; i2<n2; ++i2,ra2+=rb2)
      ramp(ra2,rb1,rx[i2]);
  }
  public static void ramp(
    float ra, float rb1, float rb2, float rb3, float[][][] rx) {
    int n3 = rx.length;
    float ra3 = ra;
    for (int i3=0; i3<n3; ++i3,ra3+=rb3)
      ramp(ra3,rb1,rb2,rx[i3]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // rand
  public static float[] rand(int n1) {
    return rand(_random,n1);
  }
  public static float[][] rand(int n1, int n2) {
    return rand(_random,n1,n2);
  }
  public static float[][][] rand(int n1, int n2, int n3) {
    return rand(_random,n1,n2,n3);
  }
  public static void rand(float[] rx) {
    rand(_random,rx);
  }
  public static void rand(float[][] rx) {
    rand(_random,rx);
  }
  public static void rand(float[][][] rx) {
    rand(_random,rx);
  }
  public static float[] rand(Random random, int n1) {
    float[] rx = new float[n1];
    rand(random,rx);
    return rx;
  }
  public static float[][] rand(Random random, int n1, int n2) {
    float[][] rx = new float[n2][n1];
    rand(random,rx);
    return rx;
  }
  public static float[][][] rand(Random random, int n1, int n2, int n3) {
    float[][][] rx = new float[n3][n2][n1];
    rand(random,rx);
    return rx;
  }
  public static void rand(Random random, float[] rx) {
    int n1 = rx.length;
    for (int i1=0; i1<n1; ++i1)
      rx[i1] = random.nextFloat();
  }
  public static void rand(Random random, float[][] rx) {
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2)
      rand(random,rx[i2]);
  }
  public static void rand(Random random, float[][][] rx) {
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3)
      rand(random,rx[i3]);
  }
  private static Random _random = new Random();

  ///////////////////////////////////////////////////////////////////////////
  // add, sub, mul, div, ...
  public static float[] add(float[] rx, float[] ry) {
    return _add.apply(rx,ry);
  }
  public static float[] add(float ra, float[] ry) {
    return _add.apply(ra,ry);
  }
  public static float[] add(float[] rx, float rb) {
    return _add.apply(rx,rb);
  }
  public static float[][] add(float[][] rx, float[][] ry) {
    return _add.apply(rx,ry);
  }
  public static float[][] add(float ra, float[][] ry) {
    return _add.apply(ra,ry);
  }
  public static float[][] add(float[][] rx, float rb) {
    return _add.apply(rx,rb);
  }
  public static float[][][] add(float[][][] rx, float[][][] ry) {
    return _add.apply(rx,ry);
  }
  public static float[][][] add(float ra, float[][][] ry) {
    return _add.apply(ra,ry);
  }
  public static float[][][] add(float[][][] rx, float rb) {
    return _add.apply(rx,rb);
  }
  public static void add(float[] rx, float[] ry, float[] rz) {
    _add.apply(rx,ry,rz);
  }
  public static void add(float ra, float[] ry, float[] rz) {
    _add.apply(ra,ry,rz);
  }
  public static void add(float[] rx, float rb, float[] rz) {
    _add.apply(rx,rb,rz);
  }
  public static void add(float[][] rx, float[][] ry, float[][] rz) {
    _add.apply(rx,ry,rz);
  }
  public static void add(float ra, float[][] ry, float[][] rz) {
    _add.apply(ra,ry,rz);
  }
  public static void add(float[][] rx, float rb, float[][] rz) {
    _add.apply(rx,rb,rz);
  }
  public static void add(float[][][] rx, float[][][] ry, float[][][] rz) {
    _add.apply(rx,ry,rz);
  }
  public static void add(float ra, float[][][] ry, float[][][] rz) {
    _add.apply(ra,ry,rz);
  }
  public static void add(float[][][] rx, float rb, float[][][] rz) {
    _add.apply(rx,rb,rz);
  }
  public static float[] sub(float[] rx, float[] ry) {
    return _sub.apply(rx,ry);
  }
  public static float[] sub(float ra, float[] ry) {
    return _sub.apply(ra,ry);
  }
  public static float[] sub(float[] rx, float rb) {
    return _sub.apply(rx,rb);
  }
  public static float[][] sub(float[][] rx, float[][] ry) {
    return _sub.apply(rx,ry);
  }
  public static float[][] sub(float ra, float[][] ry) {
    return _sub.apply(ra,ry);
  }
  public static float[][] sub(float[][] rx, float rb) {
    return _sub.apply(rx,rb);
  }
  public static float[][][] sub(float[][][] rx, float[][][] ry) {
    return _sub.apply(rx,ry);
  }
  public static float[][][] sub(float ra, float[][][] ry) {
    return _sub.apply(ra,ry);
  }
  public static float[][][] sub(float[][][] rx, float rb) {
    return _sub.apply(rx,rb);
  }
  public static void sub(float[] rx, float[] ry, float[] rz) {
    _sub.apply(rx,ry,rz);
  }
  public static void sub(float ra, float[] ry, float[] rz) {
    _sub.apply(ra,ry,rz);
  }
  public static void sub(float[] rx, float rb, float[] rz) {
    _sub.apply(rx,rb,rz);
  }
  public static void sub(float[][] rx, float[][] ry, float[][] rz) {
    _sub.apply(rx,ry,rz);
  }
  public static void sub(float ra, float[][] ry, float[][] rz) {
    _sub.apply(ra,ry,rz);
  }
  public static void sub(float[][] rx, float rb, float[][] rz) {
    _sub.apply(rx,rb,rz);
  }
  public static void sub(float[][][] rx, float[][][] ry, float[][][] rz) {
    _sub.apply(rx,ry,rz);
  }
  public static void sub(float ra, float[][][] ry, float[][][] rz) {
    _sub.apply(ra,ry,rz);
  }
  public static void sub(float[][][] rx, float rb, float[][][] rz) {
    _sub.apply(rx,rb,rz);
  }
  public static float[] mul(float[] rx, float[] ry) {
    return _mul.apply(rx,ry);
  }
  public static float[] mul(float ra, float[] ry) {
    return _mul.apply(ra,ry);
  }
  public static float[] mul(float[] rx, float rb) {
    return _mul.apply(rx,rb);
  }
  public static float[][] mul(float[][] rx, float[][] ry) {
    return _mul.apply(rx,ry);
  }
  public static float[][] mul(float ra, float[][] ry) {
    return _mul.apply(ra,ry);
  }
  public static float[][] mul(float[][] rx, float rb) {
    return _mul.apply(rx,rb);
  }
  public static float[][][] mul(float[][][] rx, float[][][] ry) {
    return _mul.apply(rx,ry);
  }
  public static float[][][] mul(float ra, float[][][] ry) {
    return _mul.apply(ra,ry);
  }
  public static float[][][] mul(float[][][] rx, float rb) {
    return _mul.apply(rx,rb);
  }
  public static void mul(float[] rx, float[] ry, float[] rz) {
    _mul.apply(rx,ry,rz);
  }
  public static void mul(float ra, float[] ry, float[] rz) {
    _mul.apply(ra,ry,rz);
  }
  public static void mul(float[] rx, float rb, float[] rz) {
    _mul.apply(rx,rb,rz);
  }
  public static void mul(float[][] rx, float[][] ry, float[][] rz) {
    _mul.apply(rx,ry,rz);
  }
  public static void mul(float ra, float[][] ry, float[][] rz) {
    _mul.apply(ra,ry,rz);
  }
  public static void mul(float[][] rx, float rb, float[][] rz) {
    _mul.apply(rx,rb,rz);
  }
  public static void mul(float[][][] rx, float[][][] ry, float[][][] rz) {
    _mul.apply(rx,ry,rz);
  }
  public static void mul(float ra, float[][][] ry, float[][][] rz) {
    _mul.apply(ra,ry,rz);
  }
  public static void mul(float[][][] rx, float rb, float[][][] rz) {
    _mul.apply(rx,rb,rz);
  }
  public static float[] div(float[] rx, float[] ry) {
    return _div.apply(rx,ry);
  }
  public static float[] div(float ra, float[] ry) {
    return _div.apply(ra,ry);
  }
  public static float[] div(float[] rx, float rb) {
    return _div.apply(rx,rb);
  }
  public static float[][] div(float[][] rx, float[][] ry) {
    return _div.apply(rx,ry);
  }
  public static float[][] div(float ra, float[][] ry) {
    return _div.apply(ra,ry);
  }
  public static float[][] div(float[][] rx, float rb) {
    return _div.apply(rx,rb);
  }
  public static float[][][] div(float[][][] rx, float[][][] ry) {
    return _div.apply(rx,ry);
  }
  public static float[][][] div(float ra, float[][][] ry) {
    return _div.apply(ra,ry);
  }
  public static float[][][] div(float[][][] rx, float rb) {
    return _div.apply(rx,rb);
  }
  public static void div(float[] rx, float[] ry, float[] rz) {
    _div.apply(rx,ry,rz);
  }
  public static void div(float ra, float[] ry, float[] rz) {
    _div.apply(ra,ry,rz);
  }
  public static void div(float[] rx, float rb, float[] rz) {
    _div.apply(rx,rb,rz);
  }
  public static void div(float[][] rx, float[][] ry, float[][] rz) {
    _div.apply(rx,ry,rz);
  }
  public static void div(float ra, float[][] ry, float[][] rz) {
    _div.apply(ra,ry,rz);
  }
  public static void div(float[][] rx, float rb, float[][] rz) {
    _div.apply(rx,rb,rz);
  }
  public static void div(float[][][] rx, float[][][] ry, float[][][] rz) {
    _div.apply(rx,ry,rz);
  }
  public static void div(float ra, float[][][] ry, float[][][] rz) {
    _div.apply(ra,ry,rz);
  }
  public static void div(float[][][] rx, float rb, float[][][] rz) {
    _div.apply(rx,rb,rz);
  }
  private static abstract class Binary {
    float[] apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      float[] rz = new float[n1];
      apply(rx,ry,rz);
      return rz;
    }
    float[] apply(float ra, float[] ry) {
      int n1 = ry.length;
      float[] rz = new float[n1];
      apply(ra,ry,rz);
      return rz;
    }
    float[] apply(float[] rx, float rb) {
      int n1 = rx.length;
      float[] rz = new float[n1];
      apply(rx,rb,rz);
      return rz;
    }
    float[][] apply(float[][] rx, float[][] ry) {
      int n2 = rx.length;
      float[][] rz = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        rz[i2] = apply(rx[i2],ry[i2]);
      return rz;
    }
    float[][] apply(float ra, float[][] ry) {
      int n2 = ry.length;
      float[][] rz = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        rz[i2] = apply(ra,ry[i2]);
      return rz;
    }
    float[][] apply(float[][] rx, float rb) {
      int n2 = rx.length;
      float[][] rz = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        rz[i2] = apply(rx[i2],rb);
      return rz;
    }
    float[][][] apply(float[][][] rx, float[][][] ry) {
      int n3 = rx.length;
      float[][][] rz = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        rz[i3] = apply(rx[i3],ry[i3]);
      return rz;
    }
    float[][][] apply(float ra, float[][][] ry) {
      int n3 = ry.length;
      float[][][] rz = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        rz[i3] = apply(ra,ry[i3]);
      return rz;
    }
    float[][][] apply(float[][][] rx, float rb) {
      int n3 = rx.length;
      float[][][] rz = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        rz[i3] = apply(rx[i3],rb);
      return rz;
    }
    abstract void apply(float[] rx, float[] ry, float[] rz);
    abstract void apply(float   ra, float[] ry, float[] rz);
    abstract void apply(float[] rx, float   rb, float[] rz);
    void apply(float[][] rx, float[][] ry, float[][] rz) {
      int n2 = rx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(rx[i2],ry[i2],rz[i2]);
    }
    void apply(float ra, float[][] ry, float[][] rz) {
      int n2 = ry.length;
      for (int i2=0; i2<n2; ++i2)
        apply(ra,ry[i2],rz[i2]);
    }
    void apply(float[][] rx, float rb, float[][] rz) {
      int n2 = rx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(rx[i2],rb,rz[i2]);
    }
    void apply(float[][][] rx, float[][][] ry, float[][][] rz) {
      int n3 = rx.length;
      for (int i3=0; i3<n3; ++i3)
        apply(rx[i3],ry[i3],rz[i3]);
    }
    void apply(float ra, float[][][] ry, float[][][] rz) {
      int n3 = ry.length;
      for (int i3=0; i3<n3; ++i3)
        apply(ra,ry[i3],rz[i3]);
    }
    void apply(float[][][] rx, float rb, float[][][] rz) {
      int n3 = rx.length;
      for (int i3=0; i3<n3; ++i3)
        apply(rx[i3],rb,rz[i3]);
    }
  }
  private static Binary _add = new Binary() {
    void apply(float[] rx, float[] ry, float[] rz) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = rx[i1]+ry[i1];
    }
    void apply(float ra, float[] ry, float[] rz) {
      int n1 = ry.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = ra+ry[i1];
    }
    void apply(float[] rx, float rb, float[] rz) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = rx[i1]+rb;
    }
  };
  private static Binary _sub = new Binary() {
    void apply(float[] rx, float[] ry, float[] rz) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = rx[i1]-ry[i1];
    }
    void apply(float ra, float[] ry, float[] rz) {
      int n1 = ry.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = ra-ry[i1];
    }
    void apply(float[] rx, float rb, float[] rz) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = rx[i1]-rb;
    }
  };
  private static Binary _mul = new Binary() {
    void apply(float[] rx, float[] ry, float[] rz) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = rx[i1]*ry[i1];
    }
    void apply(float ra, float[] ry, float[] rz) {
      int n1 = ry.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = ra*ry[i1];
    }
    void apply(float[] rx, float rb, float[] rz) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = rx[i1]*rb;
    }
  };
  private static Binary _div = new Binary() {
    void apply(float[] rx, float[] ry, float[] rz) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = rx[i1]/ry[i1];
    }
    void apply(float ra, float[] ry, float[] rz) {
      int n1 = ry.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = ra/ry[i1];
    }
    void apply(float[] rx, float rb, float[] rz) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        rz[i1] = rx[i1]/rb;
    }
  };

  ///////////////////////////////////////////////////////////////////////////
  // abs, neg, ...
  public static float[] abs(float[] rx) {
    return _abs.apply(rx);
  }
  public static float[][] abs(float[][] rx) {
    return _abs.apply(rx);
  }
  public static float[][][] abs(float[][][] rx) {
    return _abs.apply(rx);
  }
  public static void abs(float[] rx, float[] ry) {
    _abs.apply(rx,ry);
  }
  public static void abs(float[][] rx, float[][] ry) {
    _abs.apply(rx,ry);
  }
  public static void abs(float[][][] rx, float[][][] ry) {
    _abs.apply(rx,ry);
  }
  public static float[] neg(float[] rx) {
    return _neg.apply(rx);
  }
  public static float[][] neg(float[][] rx) {
    return _neg.apply(rx);
  }
  public static float[][][] neg(float[][][] rx) {
    return _neg.apply(rx);
  }
  public static void neg(float[] rx, float[] ry) {
    _neg.apply(rx,ry);
  }
  public static void neg(float[][] rx, float[][] ry) {
    _neg.apply(rx,ry);
  }
  public static void neg(float[][][] rx, float[][][] ry) {
    _neg.apply(rx,ry);
  }
  public static float[] cos(float[] rx) {
    return _cos.apply(rx);
  }
  public static float[][] cos(float[][] rx) {
    return _cos.apply(rx);
  }
  public static float[][][] cos(float[][][] rx) {
    return _cos.apply(rx);
  }
  public static void cos(float[] rx, float[] ry) {
    _cos.apply(rx,ry);
  }
  public static void cos(float[][] rx, float[][] ry) {
    _cos.apply(rx,ry);
  }
  public static void cos(float[][][] rx, float[][][] ry) {
    _cos.apply(rx,ry);
  }
  public static float[] sin(float[] rx) {
    return _sin.apply(rx);
  }
  public static float[][] sin(float[][] rx) {
    return _sin.apply(rx);
  }
  public static float[][][] sin(float[][][] rx) {
    return _sin.apply(rx);
  }
  public static void sin(float[] rx, float[] ry) {
    _sin.apply(rx,ry);
  }
  public static void sin(float[][] rx, float[][] ry) {
    _sin.apply(rx,ry);
  }
  public static void sin(float[][][] rx, float[][][] ry) {
    _sin.apply(rx,ry);
  }
  public static float[] exp(float[] rx) {
    return _exp.apply(rx);
  }
  public static float[][] exp(float[][] rx) {
    return _exp.apply(rx);
  }
  public static float[][][] exp(float[][][] rx) {
    return _exp.apply(rx);
  }
  public static void exp(float[] rx, float[] ry) {
    _exp.apply(rx,ry);
  }
  public static void exp(float[][] rx, float[][] ry) {
    _exp.apply(rx,ry);
  }
  public static void exp(float[][][] rx, float[][][] ry) {
    _exp.apply(rx,ry);
  }
  public static float[] log(float[] rx) {
    return _log.apply(rx);
  }
  public static float[][] log(float[][] rx) {
    return _log.apply(rx);
  }
  public static float[][][] log(float[][][] rx) {
    return _log.apply(rx);
  }
  public static void log(float[] rx, float[] ry) {
    _log.apply(rx,ry);
  }
  public static void log(float[][] rx, float[][] ry) {
    _log.apply(rx,ry);
  }
  public static void log(float[][][] rx, float[][][] ry) {
    _log.apply(rx,ry);
  }
  public static float[] log10(float[] rx) {
    return _log10.apply(rx);
  }
  public static float[][] log10(float[][] rx) {
    return _log10.apply(rx);
  }
  public static float[][][] log10(float[][][] rx) {
    return _log10.apply(rx);
  }
  public static void log10(float[] rx, float[] ry) {
    _log10.apply(rx,ry);
  }
  public static void log10(float[][] rx, float[][] ry) {
    _log10.apply(rx,ry);
  }
  public static void log10(float[][][] rx, float[][][] ry) {
    _log10.apply(rx,ry);
  }
  public static float[] sqrt(float[] rx) {
    return _sqrt.apply(rx);
  }
  public static float[][] sqrt(float[][] rx) {
    return _sqrt.apply(rx);
  }
  public static float[][][] sqrt(float[][][] rx) {
    return _sqrt.apply(rx);
  }
  public static void sqrt(float[] rx, float[] ry) {
    _sqrt.apply(rx,ry);
  }
  public static void sqrt(float[][] rx, float[][] ry) {
    _sqrt.apply(rx,ry);
  }
  public static void sqrt(float[][][] rx, float[][][] ry) {
    _sqrt.apply(rx,ry);
  }
  public static float[] sgn(float[] rx) {
    return _sgn.apply(rx);
  }
  public static float[][] sgn(float[][] rx) {
    return _sgn.apply(rx);
  }
  public static float[][][] sgn(float[][][] rx) {
    return _sgn.apply(rx);
  }
  public static void sgn(float[] rx, float[] ry) {
    _sgn.apply(rx,ry);
  }
  public static void sgn(float[][] rx, float[][] ry) {
    _sgn.apply(rx,ry);
  }
  public static void sgn(float[][][] rx, float[][][] ry) {
    _sgn.apply(rx,ry);
  }
  private static abstract class Unary {
    float[] apply(float[] rx) {
      int n1 = rx.length;
      float[] ry = new float[n1];
      apply(rx,ry);
      return ry;
    }
    float[][] apply(float[][] rx) {
      int n2 = rx.length;
      float[][] ry = new float[n2][];
      for (int i2=0; i2<n2; ++i2)
        ry[i2] = apply(rx[i2]);
      return ry;
    }
    float[][][] apply(float[][][] rx) {
      int n3 = rx.length;
      float[][][] ry = new float[n3][][];
      for (int i3=0; i3<n3; ++i3)
        ry[i3] = apply(rx[i3]);
      return ry;
    }
    abstract void apply(float[] rx, float[] ry);
    void apply(float[][] rx, float[][] ry) {
      int n2 = rx.length;
      for (int i2=0; i2<n2; ++i2)
        apply(rx[i2],ry[i2]);
    }
    void apply(float[][][] rx, float[][][] ry) {
      int n3 = rx.length;
      for (int i3=0; i3<n3; ++i3)
        apply(rx[i3],ry[i3]);
    }
  }
  private static Unary _abs = new Unary() {
    void apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1) {
        float rxi = rx[i1];
        ry[i1] = (rxi>=0.0f)?rxi:-rxi;
      }
    }
  };
  private static Unary _neg = new Unary() {
    void apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        ry[i1] = -rx[i1];
    }
  };
  private static Unary _cos = new Unary() {
    void apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        ry[i1] = (float)Math.cos(rx[i1]);
    }
  };
  private static Unary _sin = new Unary() {
    void apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        ry[i1] = (float)Math.sin(rx[i1]);
    }
  };
  private static Unary _exp = new Unary() {
    void apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        ry[i1] = (float)Math.exp(rx[i1]);
    }
  };
  private static Unary _log = new Unary() {
    void apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        ry[i1] = (float)Math.log(rx[i1]);
    }
  };
  private static Unary _log10 = new Unary() {
    void apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        ry[i1] = (float)Math.log10(rx[i1]);
    }
  };
  private static Unary _sqrt = new Unary() {
    void apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        ry[i1] = (float)Math.sqrt(rx[i1]);
    }
  };
  private static Unary _sgn = new Unary() {
    void apply(float[] rx, float[] ry) {
      int n1 = rx.length;
      for (int i1=0; i1<n1; ++i1)
        ry[i1] = (rx[i1]>0.0f)?1.0f:(rx[i1]<0.0f)?-1.0f:0.0f;
    }
  };

  ///////////////////////////////////////////////////////////////////////////
  // pow
  public static float[] pow(float[] rx, float ra) {
    int n1 = rx.length;
    float[] ry = new float[n1];
    pow(rx,ra,ry);
    return ry;
  }
  public static float[][] pow(float[][] rx, float ra) {
    int n2 = rx.length;
    float[][] ry = new float[n2][];
    for (int i2=0; i2<n2; ++i2)
      ry[i2] = pow(rx[i2],ra);
    return ry;
  }
  public static float[][][] pow(float[][][] rx, float ra) {
    int n3 = rx.length;
    float[][][] ry = new float[n3][][];
    for (int i3=0; i3<n3; ++i3)
      ry[i3] = pow(rx[i3],ra);
    return ry;
  }
  public static void pow(float[] rx, float ra, float[] ry) {
    int n1 = rx.length;
    for (int i1=0; i1<n1; ++i1)
      ry[i1] = (float)Math.pow(rx[i1],ra);
  }
  public static void pow(float[][] rx, float ra, float[][] ry) {
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2)
      pow(rx[i2],ra,ry[i2]);
  }
  public static void pow(float[][][] rx, float ra, float[][][] ry) {
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3)
      pow(rx[i3],ra,ry[i3]);
  }

  ///////////////////////////////////////////////////////////////////////////
  // equal
  public static boolean equal(float[] rx, float[] ry) {
    int n1 = rx.length;
    for (int i1=0; i1<n1; ++i1) {
      if (rx[i1]!=ry[i1])
        return false;
    }
    return true;
  }
  public static boolean equal(float[][] rx, float[][] ry) {
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      if (!equal(rx[i2],ry[i2]))
        return false;
    }
    return true;
  }
  public static boolean equal(float[][][] rx, float[][][] ry) {
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      if (!equal(rx[i3],ry[i3]))
        return false;
    }
    return true;
  }
  public static boolean equal(float tolerance, float[] rx, float[] ry) {
    int n1 = rx.length;
    for (int i1=0; i1<n1; ++i1) {
      if (!equal(tolerance,rx[i1],ry[i1]))
        return false;
    }
    return true;
  }
  public static boolean equal(float tolerance, float[][] rx, float[][] ry) {
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      if (!equal(tolerance,rx[i2],ry[i2]))
        return false;
    }
    return true;
  }
  public static boolean equal(float tolerance, float[][][] rx, float[][][] ry) {
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      if (!equal(tolerance,rx[i3],ry[i3]))
        return false;
    }
    return true;
  }
  private static boolean equal(float tolerance, float ra, float rb) {
    return (ra<rb)?rb-ra<=tolerance:ra-rb<=tolerance;
  }

  ///////////////////////////////////////////////////////////////////////////
  // findMax, findMin
  public static float findMax(float[] rx, int[] index) {
    int i1max = 0;
    float rmax = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]>rmax) {
        rmax = rx[i1];
        i1max = i1;
      }
    }
    if (index!=null)
      index[0] = i1max;
    return rmax;
  }
  public static float findMax(float[][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    float rmax = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      float[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]>rmax) {
          rmax = rxi2[i1];
          i2max = i2;
          i1max = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
    }
    return rmax;
  }
  public static float findMax(float[][][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    int i3max = 0;
    float rmax = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      float[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        float[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]>rmax) {
            rmax = rxi3i2[i1];
            i1max = i1;
            i2max = i2;
            i3max = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
      index[2] = i3max;
    }
    return rmax;
  }
  public static float findMin(float[] rx, int[] index) {
    int i1min = 0;
    float rmin = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]<rmin) {
        rmin = rx[i1];
        i1min = i1;
      }
    }
    if (index!=null)
      index[0] = i1min;
    return rmin;
  }
  public static float findMin(float[][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    float rmin = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      float[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]<rmin) {
          rmin = rxi2[i1];
          i2min = i2;
          i1min = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
    }
    return rmin;
  }
  public static float findMin(float[][][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    int i3min = 0;
    float rmin = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      float[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        float[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]<rmin) {
            rmin = rxi3i2[i1];
            i1min = i1;
            i2min = i2;
            i3min = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
      index[2] = i3min;
    }
    return rmin;
  }
}
