/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static java.lang.Math.sqrt;

/**
 * A plane. A plane divides a 3-dimensional space into points above it,
 * points below it, and points within it. The signed distance s from a 
 * point (x,y,z) to a plane is s = a*x + b*y + c*z + d, where (a,b,c,d)
 * are coefficients that define the plane. Points within the plane satisfy 
 * the equation s = 0.
 * <p>
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.30
 */
public class Plane {

  /**
   * Constructs a plane with specified coefficients.
   * @param a the coefficient a.
   * @param b the coefficient b.
   * @param c the coefficient c.
   * @param d the coefficient d.
   */
  public Plane(double a, double b, double c, double d) {
    set(a,b,c,d);
  }

  /**
   * Constructs a plane. The plane will contain the specified point p 
   * and be orthogonal to the specified normal vector n, which points
   * toward the space above the plane.
   * @param p the point in the plane.
   * @param n the normal vector.
   */
  public Plane(Point3 p, Vector3 n) {
    set(n.x,n.y,n.z,-(n.x*p.x+n.y*p.y+n.z*p.z));
  }

  /**
   * Constructs a copy of the specified plane.
   * @param p the plane.
   */
  public Plane(Plane p) {
    _a = p._a;
    _b = p._b;
    _c = p._c;
    _d = p._d;
  }

  /**
   * Sets the coefficients of this plane.
   * @param a the coefficient a.
   * @param b the coefficient b.
   * @param c the coefficient c.
   * @param d the coefficient d.
   */
  public void set(double a, double b, double c, double d) {
    _a = a;
    _b = b;
    _c = c;
    _d = d;
    normalize();
  }

  /**
   * Gets the plane coefficient a.
   * @return the plane coefficient a.
   */
  public double getA() {
    return _a;
  }

  /**
   * Gets the plane coefficient b.
   * @return the plane coefficient b.
   */
  public double getB() {
    return _b;
  }

  /**
   * Gets the plane coefficient c.
   * @return the plane coefficient c.
   */
  public double getC() {
    return _c;
  }

  /**
   * Gets the plane coefficient d.
   * @return the plane coefficient d.
   */
  public double getD() {
    return _d;
  }

  /**
   * Gets the unit-vector normal to this plane. The vector points toward
   * the space above the plane.
   * @return the unit-vector normal.
   */
  public Vector3 getNormal() {
    return new Vector3(_a,_b,_c);
  }

  /**
   * Returns the signed distance from this plane to a specified point.
   * Distance is negative for points below the plane, zero for points 
   * within the plane, and positive for points above the plane.
   * @param x the x coordinate of the point.
   * @param y the y coordinate of the point.
   * @param z the z coordinate of the point.
   * @return the signed distance.
   */
  public double distanceTo(double x, double y, double z) {
    return _a*x+_b*y+_c*z+_d;
  }

  /**
   * Returns the signed distance from this plane to a specified point.
   * Distance is negative for points below the plane, zero for points 
   * within the plane, and positive for points above the plane.
   * @param p the point.
   * @return the signed distance.
   */
  public double distanceTo(Point3 p) {
    return distanceTo(p.x,p.y,p.z);
  }

  /**
   * Transforms this plane, given the specified transform matrix.
   * If the inverse of the transform matrix is known, the method
   * {@link #transformWithInverse(Matrix44)} is more efficient.
   * <p>
   * Let M denote the matrix that transforms points p from old to new
   * coordinates; i.e., p' = M*p, where p' denotes a transformed point.
   * In old coordinates, the plane P = (a,b,c,d) satisfies the equation
   * a*x + b*y + c*z + d = 0, for all points p = (x,y,z) within the plane.
   * This method returns a new transformed plane P' = (a',b',c',d') that
   * satisfies the equation a'*x' + b'*y' + c'*z' + d' = 0 for all 
   * transformed points p' = (x',y',z') within the transformed plane.
   * @param m the transform matrix.
   */
  public void transform(Matrix44 m) {
    transformWithInverse(m.inverse());
  }

  /**
   * Transforms this plane, given the inverse of the transform matrix.
   * If the inverse of the transform matrix is known, this method is
   * more efficient than the method {@link #transform(Matrix44)}.
   * @param mi the inverse of the transform matrix.
   */
  public void transformWithInverse(Matrix44 mi) {
    // (transpose of inverse matrix) times (plane coefficient vector)
    double[] m = mi.m;
    double a = m[ 0]*_a + m[ 1]*_b + m[ 2]*_c + m[ 3]*_d;
    double b = m[ 4]*_a + m[ 5]*_b + m[ 6]*_c + m[ 7]*_d;
    double c = m[ 8]*_a + m[ 9]*_b + m[10]*_c + m[11]*_d;
    double d = m[12]*_a + m[13]*_b + m[14]*_c + m[15]*_d;
    set(a,b,c,d);
  }

  public String toString() {
    return "("+_a+","+_b+","+_c+","+_d+")";
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _a,_b,_c,_d;

  private void normalize() {
    double s = 1.0/sqrt(_a*_a+_b*_b+_c*_c);
    _a *= s;
    _b *= s;
    _c *= s;
    _d *= s;
  }
}
