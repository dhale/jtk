/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A 4-by-4 matrix.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.20
 */
public class Matrix44 implements Cloneable {

  /**
   * The matrix elements stored contiguously in an array, as in OpenGL.
   * The matrix elements are:
   * <pre><code>
   * | m00  m01  m02  m03 |     | m[ 0]  m[ 4]  m[ 8]  m[12] |
   * | m10  m11  m12  m13 |  =  | m[ 1]  m[ 5]  m[ 9]  m[13] |
   * | m20  m21  m22  m23 |     | m[ 2]  m[ 6]  m[10]  m[14] |
   * | m30  m31  m32  m03 |     | m[ 3]  m[ 7]  m[11]  m[15] |
   * </code></pre>
   */
  public double[] m = new double[16];

  /**
   * Constructs an identity matrix.
   */
  public Matrix44() {
    m[0] = m[5] = m[10] = m[15] = 1.0;
  }

  /**
   * Constructs a matrix with specified elements.
   * @param m00 the element with (row,col) indices (0,0)
   * @param m01 the element with (row,col) indices (0,1)
   * @param m02 the element with (row,col) indices (0,2)
   * @param m03 the element with (row,col) indices (0,3)
   * @param m10 the element with (row,col) indices (1,0)
   * @param m11 the element with (row,col) indices (1,1)
   * @param m12 the element with (row,col) indices (1,2)
   * @param m13 the element with (row,col) indices (1,3)
   * @param m20 the element with (row,col) indices (2,0)
   * @param m21 the element with (row,col) indices (2,1)
   * @param m22 the element with (row,col) indices (2,2)
   * @param m23 the element with (row,col) indices (2,3)
   * @param m30 the element with (row,col) indices (3,0)
   * @param m31 the element with (row,col) indices (3,1)
   * @param m32 the element with (row,col) indices (3,2)
   * @param m33 the element with (row,col) indices (3,3)
   */
  public Matrix44(double m00, double m01, double m02, double m03,
                  double m10, double m11, double m12, double m13,
                  double m20, double m21, double m22, double m23,
                  double m30, double m31, double m32, double m33)
  {
    m[ 0] = m00;  m[ 4] = m01;  m[ 8] = m02;  m[12] = m03;
    m[ 1] = m10;  m[ 5] = m11;  m[ 9] = m12;  m[13] = m13;
    m[ 2] = m20;  m[ 6] = m21;  m[10] = m22;  m[14] = m23;
    m[ 3] = m30;  m[ 7] = m31;  m[11] = m32;  m[15] = m33;
  }

  /**
   * Constructs a matrix with specified elements. The specified array of 
   * elements is referenced by (not copied into) the constructed matrix. 
   * Therefore, any changes to the matrix will be reflected in changes to 
   * the elements of the specified array.
   * @param m the array[16] of matrix elements.
   */
  public Matrix44(double[] m) {
    this.m = m;
  }

  /**
   * Returns a clone of this matrix.
   * @return the clone.
   */
  public Matrix44 clone() {
    return new Matrix44(m[ 0],m[ 4],m[ 8],m[12],
                        m[ 1],m[ 5],m[ 9],m[13],
                        m[ 2],m[ 6],m[10],m[14],
                        m[ 3],m[ 7],m[11],m[15]);
  }

  /**
   * Returns the inverse Minv of this matrix M.
   * @return the inverse Minv.
   */
  public Matrix44 inverse() {
    return new Matrix44(invert(m,new double[16]));
  }

  /**
   * Replaces this matrix M with its inverse Minv.
   * @return this matrix, inverted.
   */
  public Matrix44 inverseEquals() {
    invert(m,m);
    return this;
  }

  /**
   * Returns the transpose M' of this matrix M.
   * @return the transpose M'.
   */
  public Matrix44 transpose() {
    double[] t = {
      m[ 0],m[ 4],m[ 8],m[12],
      m[ 1],m[ 5],m[ 9],m[13],
      m[ 2],m[ 6],m[10],m[14],
      m[ 3],m[ 7],m[11],m[15]
    };
    return new Matrix44(t);
  }

  /**
   * Replaces this matrix M with its transpose M'.
   * @return this matrix, transposed.
   */
  public Matrix44 transposeEquals() {
    double t00=m[ 0], t01=m[ 1], t02=m[ 2], t03=m[ 3],
           t10=m[ 4], t11=m[ 5], t12=m[ 6], t13=m[ 7],
           t20=m[ 8], t21=m[ 9], t22=m[10], t23=m[11],
           t30=m[12], t31=m[13], t32=m[14], t33=m[15];
    m[ 0] = t00;  m[ 4] = t01;  m[ 8] = t02;  m[12] = t03;
    m[ 1] = t10;  m[ 5] = t11;  m[ 9] = t12;  m[13] = t13;
    m[ 2] = t20;  m[ 6] = t21;  m[10] = t22;  m[14] = t23;
    m[ 3] = t30;  m[ 7] = t31;  m[11] = t32;  m[15] = t33;
    return this;
  }

  /**
   * Returns the product MA of this matrix M and a matrix A.
   * @param a the matrix A.
   * @return the product MA.
   */
  public Matrix44 times(Matrix44 a) {
    return new Matrix44(mul(m,a.m,new double[16]));
  }

  /**
   * Replaces this matrix M with the matrix product MA.
   * @param a the matrix A.
   * @return the product MA.
   */
  public Matrix44 timesEquals(Matrix44 a) {
    mul(m,a.m,m);
    return this;
  }

  /**
   * Returns the product MA' of this matrix M and the transpose of a matrix A.
   * @param a the matrix A.
   * @return the product MA'.
   */
  public Matrix44 timesTranspose(Matrix44 a) {
    return new Matrix44(mult(m,a.m,new double[16]));
  }

  /**
   * Replaces this matrix M with the matrix product MA'.
   * @param a the matrix A.
   * @return the product MA'.
   */
  public Matrix44 timesTransposeEquals(Matrix44 a) {
    mult(m,a.m,m);
    return this;
  }

  /**
   * Returns the product M'A of the transpose of this matrix M and a matrix A.
   * @param a the matrix A.
   * @return the product M'A.
   */
  public Matrix44 transposeTimes(Matrix44 a) {
    return new Matrix44(tmul(m,a.m,new double[16]));
  }

  /**
   * Replaces this matrix M with the matrix product M'A.
   * @param a the matrix A.
   * @return the product M'A.
   */
  public Matrix44 transposeTimesEquals(Matrix44 a) {
    tmul(m,a.m,m);
    return this;
  }

  /**
   * Returns the product Mp of this matrix M and a point p.
   * The coordinate w of the specified point is assumed to equal 1.0; 
   * and the returned point is homogenized, that is, scaled such that 
   * its coordinate w equals 1.0.
   * @param p the point p.
   * @return the product Mp.
   */
  public Point3 times(Point3 p) {
    double px = p.x;
    double py = p.y;
    double pz = p.z;
    double qx = m[ 0]*px + m[ 4]*py + m[ 8]*pz + m[12];
    double qy = m[ 1]*px + m[ 5]*py + m[ 9]*pz + m[13];
    double qz = m[ 2]*px + m[ 6]*py + m[10]*pz + m[14];
    double qw = m[ 3]*px + m[ 7]*py + m[11]*pz + m[15];
    if (qw!=1.0) {
      double s = 1.0/qw;
      qx *= s;
      qy *= s;
      qz *= s;
    }
    return new Point3(qx,qy,qz);
  }

  /**
   * Returns the product Mp of this matrix M and a point p.
   * @param p the point p.
   * @return the product Mp.
   */
  public Point4 times(Point4 p) {
    double px = p.x;
    double py = p.y;
    double pz = p.z;
    double pw = p.w;
    double qx = m[ 0]*px + m[ 4]*py + m[ 8]*pz + m[12]*pw;
    double qy = m[ 1]*px + m[ 5]*py + m[ 9]*pz + m[13]*pw;
    double qz = m[ 2]*px + m[ 6]*py + m[10]*pz + m[14]*pw;
    double qw = m[ 3]*px + m[ 7]*py + m[11]*pz + m[15]*pw;
    return new Point4(qx,qy,qz,qw);
  }

  /**
   * Returns the product M'p of the transpose of this matrix M and a point p. 
   * The coordinate w of the specified point is assumed to equal 1.0; and 
   * the returned point is homogenized, that is, scaled such that its 
   * coordinate w equals 1.0.
   * @param p the point p.
   * @return the product M'p.
   */
  public Point3 transposeTimes(Point3 p) {
    double px = p.x;
    double py = p.y;
    double pz = p.z;
    double qx = m[ 0]*px + m[ 1]*py + m[ 2]*pz + m[ 3];
    double qy = m[ 4]*px + m[ 5]*py + m[ 6]*pz + m[ 7];
    double qz = m[ 8]*px + m[ 9]*py + m[10]*pz + m[11];
    double qw = m[12]*px + m[13]*py + m[14]*pz + m[15];
    if (qw!=1.0) {
      double s = 1.0/qw;
      qx *= s;
      qy *= s;
      qz *= s;
    }
    return new Point3(qx,qy,qz);
  }

  /**
   * Returns the product M'p of the transpose of this matrix M and a point p. 
   * @param p the point p.
   * @return the product M'p.
   */
  public Point4 transposeTimes(Point4 p) {
    double px = p.x;
    double py = p.y;
    double pz = p.z;
    double pw = p.w;
    double qx = m[ 0]*px + m[ 1]*py + m[ 2]*pz + m[ 3]*pw;
    double qy = m[ 4]*px + m[ 5]*py + m[ 6]*pz + m[ 7]*pw;
    double qz = m[ 8]*px + m[ 9]*py + m[10]*pz + m[11]*pw;
    double qw = m[12]*px + m[13]*py + m[14]*pz + m[15]*pw;
    return new Point4(qx,qy,qz,qw);
  }

  /**
   * Returns the product Mv of this matrix M and a vector v.
   * Uses only the upper-left 3-by-3 elements of this matrix.
   * @param v the vector v.
   * @return the product Mv.
   */
  public Vector3 times(Vector3 v) {
    double vx = v.x;
    double vy = v.y;
    double vz = v.z;
    double ux = m[ 0]*vx + m[ 4]*vy + m[ 8]*vz;
    double uy = m[ 1]*vx + m[ 5]*vy + m[ 9]*vz;
    double uz = m[ 2]*vx + m[ 6]*vy + m[10]*vz;
    return new Vector3(ux,uy,uz);
  }

  /**
   * Returns the product M'v of the transpose of this matrix M and a vector v. 
   * Uses only the upper-left 3-by-3 elements of this matrix.
   * @param v the vector v.
   * @return the product M'v.
   */
  public Vector3 transposeTimes(Vector3 v) {
    double vx = v.x;
    double vy = v.y;
    double vz = v.z;
    double ux = m[ 0]*vx + m[ 1]*vy + m[ 2]*vz;
    double uy = m[ 4]*vx + m[ 5]*vy + m[ 6]*vz;
    double uz = m[ 8]*vx + m[ 9]*vy + m[10]*vz;
    return new Vector3(ux,uy,uz);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  /**
   * Computes the matrix product C = AB. The product may be computed 
   * in place; the arrays a, b, and c may be the same array.
   * Returns the product C.
   */
  private static double[] mul(double[] a, double[] b, double[] c) {
    double a00=a[ 0],a01=a[ 4],a02=a[ 8],a03=a[12],
           a10=a[ 1],a11=a[ 5],a12=a[ 9],a13=a[13],
           a20=a[ 2],a21=a[ 6],a22=a[10],a23=a[14],
           a30=a[ 3],a31=a[ 7],a32=a[11],a33=a[15];
    double b00=b[ 0],b01=b[ 4],b02=b[ 8],b03=b[12],
           b10=b[ 1],b11=b[ 5],b12=b[ 9],b13=b[13],
           b20=b[ 2],b21=b[ 6],b22=b[10],b23=b[14],
           b30=b[ 3],b31=b[ 7],b32=b[11],b33=b[15];
    c[ 0] = a00*b00+a01*b10+a02*b20+a03*b30;
    c[ 1] = a10*b00+a11*b10+a12*b20+a13*b30;
    c[ 2] = a20*b00+a21*b10+a22*b20+a23*b30;
    c[ 3] = a30*b00+a31*b10+a32*b20+a33*b30;
    c[ 4] = a00*b01+a01*b11+a02*b21+a03*b31;
    c[ 5] = a10*b01+a11*b11+a12*b21+a13*b31;
    c[ 6] = a20*b01+a21*b11+a22*b21+a23*b31;
    c[ 7] = a30*b01+a31*b11+a32*b21+a33*b31;
    c[ 8] = a00*b02+a01*b12+a02*b22+a03*b32;
    c[ 9] = a10*b02+a11*b12+a12*b22+a13*b32;
    c[10] = a20*b02+a21*b12+a22*b22+a23*b32;
    c[11] = a30*b02+a31*b12+a32*b22+a33*b32;
    c[12] = a00*b03+a01*b13+a02*b23+a03*b33;
    c[13] = a10*b03+a11*b13+a12*b23+a13*b33;
    c[14] = a20*b03+a21*b13+a22*b23+a23*b33;
    c[15] = a30*b03+a31*b13+a32*b23+a33*b33;
    return c;
  }

  /**
   * Computes the matrix product C = AB'. The product may be computed 
   * in place; the arrays a, b, and c may be the same array.
   * Returns the product C.
   */
  private static double[] mult(double[] a, double[] b, double[] c) {
    double a00=a[ 0],a01=a[ 4],a02=a[ 8],a03=a[12],
           a10=a[ 1],a11=a[ 5],a12=a[ 9],a13=a[13],
           a20=a[ 2],a21=a[ 6],a22=a[10],a23=a[14],
           a30=a[ 3],a31=a[ 7],a32=a[11],a33=a[15];
    double b00=b[ 0],b01=b[ 4],b02=b[ 8],b03=b[12],
           b10=b[ 1],b11=b[ 5],b12=b[ 9],b13=b[13],
           b20=b[ 2],b21=b[ 6],b22=b[10],b23=b[14],
           b30=b[ 3],b31=b[ 7],b32=b[11],b33=b[15];
    c[ 0] = a00*b00+a01*b01+a02*b02+a03*b03;
    c[ 1] = a10*b00+a11*b01+a12*b02+a13*b03;
    c[ 2] = a20*b00+a21*b01+a22*b02+a23*b03;
    c[ 3] = a30*b00+a31*b01+a32*b02+a33*b03;
    c[ 4] = a00*b10+a01*b11+a02*b12+a03*b13;
    c[ 5] = a10*b10+a11*b11+a12*b12+a13*b13;
    c[ 6] = a20*b10+a21*b11+a22*b12+a23*b13;
    c[ 7] = a30*b10+a31*b11+a32*b12+a33*b13;
    c[ 8] = a00*b20+a01*b21+a02*b22+a03*b23;
    c[ 9] = a10*b20+a11*b21+a12*b22+a13*b23;
    c[10] = a20*b20+a21*b21+a22*b22+a23*b23;
    c[11] = a30*b20+a31*b21+a32*b22+a33*b23;
    c[12] = a00*b30+a01*b31+a02*b32+a03*b33;
    c[13] = a10*b30+a11*b31+a12*b32+a13*b33;
    c[14] = a20*b30+a21*b31+a22*b32+a23*b33;
    c[15] = a30*b30+a31*b31+a32*b32+a33*b33;
    return c;
  }

  /**
   * Computes the matrix product C = A'B. The product may be computed 
   * in place; the arrays a, b, and c may be the same array.
   * Returns the product C.
   */
  private static double[] tmul(double[] a, double[] b, double[] c) {
    double a00=a[ 0],a01=a[ 4],a02=a[ 8],a03=a[12],
           a10=a[ 1],a11=a[ 5],a12=a[ 9],a13=a[13],
           a20=a[ 2],a21=a[ 6],a22=a[10],a23=a[14],
           a30=a[ 3],a31=a[ 7],a32=a[11],a33=a[15];
    double b00=b[ 0],b01=b[ 4],b02=b[ 8],b03=b[12],
           b10=b[ 1],b11=b[ 5],b12=b[ 9],b13=b[13],
           b20=b[ 2],b21=b[ 6],b22=b[10],b23=b[14],
           b30=b[ 3],b31=b[ 7],b32=b[11],b33=b[15];
    c[ 0] = a00*b00+a10*b10+a20*b20+a30*b30;
    c[ 1] = a01*b00+a11*b10+a21*b20+a31*b30;
    c[ 2] = a02*b00+a12*b10+a22*b20+a32*b30;
    c[ 3] = a03*b00+a13*b10+a23*b20+a33*b30;
    c[ 4] = a00*b01+a01*b11+a20*b21+a30*b31;
    c[ 5] = a01*b01+a11*b11+a21*b21+a31*b31;
    c[ 6] = a02*b01+a12*b11+a22*b21+a32*b31;
    c[ 7] = a03*b01+a13*b11+a23*b21+a33*b31;
    c[ 8] = a00*b02+a10*b12+a20*b22+a30*b32;
    c[ 9] = a01*b02+a11*b12+a21*b22+a31*b32;
    c[10] = a02*b02+a12*b12+a22*b22+a32*b32;
    c[11] = a03*b02+a13*b12+a23*b22+a33*b32;
    c[12] = a00*b03+a10*b13+a20*b23+a30*b33;
    c[13] = a01*b03+a11*b13+a21*b23+a31*b33;
    c[14] = a02*b03+a12*b13+a22*b23+a32*b33;
    c[15] = a03*b03+a13*b13+a23*b23+a33*b33;
    return c;
  }

  /**
   * Compute the inverse B of the specified matrix A. The inverse may be 
   * computed in place; the arrays a and b may be the same array.
   * Returns the inverse B.
   * <p>
   * This method is based on Cramer's Rule for the inverse of a matrix,
   * and it's implementation follows that described in Streaming SIMD 
   * Extensions - Inverse of 4x4 Matrix, Intel Corporation, Technical 
   * Report AP-928.
   */
  private static double[] invert(double[] a, double[] b) {

    // transpose
    double t00 = a[ 0];
    double t01 = a[ 4];
    double t02 = a[ 8];
    double t03 = a[12];
    double t04 = a[ 1];
    double t08 = a[ 2];
    double t12 = a[ 3];
    double t05 = a[ 5];
    double t09 = a[ 6];
    double t13 = a[ 7];
    double t06 = a[ 9];
    double t10 = a[10];
    double t14 = a[11];
    double t07 = a[13];
    double t11 = a[14];
    double t15 = a[15];

    // pairs for first 8 elements (cofactors)
    double u00 = t10*t15;
    double u01 = t11*t14;
    double u02 = t09*t15;
    double u03 = t11*t13;
    double u04 = t09*t14;
    double u05 = t10*t13;
    double u06 = t08*t15;
    double u07 = t11*t12;
    double u08 = t08*t14;
    double u09 = t10*t12;
    double u10 = t08*t13;
    double u11 = t09*t12;

    // first 8 elements (cofactors)
    b[ 0] = u00*t05+u03*t06+u04*t07-u01*t05-u02*t06-u05*t07;
    b[ 1] = u01*t04+u06*t06+u09*t07-u00*t04-u07*t06-u08*t07;
    b[ 2] = u02*t04+u07*t05+u10*t07-u03*t04-u06*t05-u11*t07;
    b[ 3] = u05*t04+u08*t05+u11*t06-u04*t04-u09*t05-u10*t06;
    b[ 4] = u01*t01+u02*t02+u05*t03-u00*t01-u03*t02-u04*t03;
    b[ 5] = u00*t00+u07*t02+u08*t03-u01*t00-u06*t02-u09*t03;
    b[ 6] = u03*t00+u06*t01+u11*t03-u02*t00-u07*t01-u10*t03;
    b[ 7] = u04*t00+u09*t01+u10*t02-u05*t00-u08*t01-u11*t02;

    // pairs for second 8 elements (cofactors)
    u00 = t02*t07;
    u01 = t03*t06;
    u02 = t01*t07;
    u03 = t03*t05;
    u04 = t01*t06;
    u05 = t02*t05;
    u06 = t00*t07;
    u07 = t03*t04;
    u08 = t00*t06;
    u09 = t02*t04;
    u10 = t00*t05;
    u11 = t01*t04;

    // second 8 elements (cofactors)
    b[ 8] = u00*t13+u03*t14+u04*t15-u01*t13-u02*t14-u05*t15;
    b[ 9] = u01*t12+u06*t14+u09*t15-u00*t12-u07*t14-u08*t15;
    b[10] = u02*t12+u07*t13+u10*t15-u03*t12-u06*t13-u11*t15;
    b[11] = u05*t12+u08*t13+u11*t14-u04*t12-u09*t13-u10*t14;
    b[12] = u02*t10+u05*t11+u01*t09-u04*t11-u00*t09-u03*t10;
    b[13] = u08*t11+u00*t08+u07*t10-u06*t10-u09*t11-u01*t08;
    b[14] = u06*t09+u11*t11+u03*t08-u10*t11-u02*t08-u07*t09;
    b[15] = u10*t10+u04*t08+u09*t09-u08*t09-u11*t10-u05*t08;

    // determinant
    double d = t00*b[0]+t01*b[1]+t02*b[2]+t03*b[3];
    
    // inverse
    d = 1.0/d;
    b[ 0] *= d;  b[ 1] *= d;  b[ 2] *= d;  b[ 3] *= d;  
    b[ 4] *= d;  b[ 5] *= d;  b[ 6] *= d;  b[ 7] *= d;  
    b[ 8] *= d;  b[ 9] *= d;  b[10] *= d;  b[11] *= d;  
    b[12] *= d;  b[13] *= d;  b[14] *= d;  b[15] *= d;  
    return b;
  }
}
