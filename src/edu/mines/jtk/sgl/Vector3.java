/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A vector with three components x, y, and z.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.20
 */
public class Vector3 extends Tuple3 {

  /**
   * Constructs a vector with components zero.
   */
  public Vector3() {
  }

  /**
   * Constructs a vector with specified components.
   * @param x the x component.
   * @param y the y component.
   * @param z the z component.
   */
  public Vector3(double x, double y, double z) {
    super(x,y,z);
  }

  /**
   * Returns a clone of this vector.
   * @return the clone.
   */
  public Vector3 clone() {
    return new Vector3(x,y,z);
  }

  /**
   * Returns the length of this vector.
   * @return the length.
   */
  public double length() {
    return Math.sqrt(x*x+y*y+z*z);
  }

  /**
   * Returns the length-squared of this vector.
   * @return the length-squared.
   */
  public double lengthSquared() {
    return x*x+y*y+z*z;
  }

  /**
   * Returns the negation -u of this vector u.
   * @return the negation -u.
   */
  public Vector3 negate() {
    return new Vector3(-x,-y,-z);
  }

  /**
   * Negates this vector.
   * @return this vector, negated.
   */
  public Vector3 negateEquals() {
    x = -x;
    y = -y;
    z = -z;
    return this;
  }

  /**
   * Returns the unit vector with the same direction as this vector.
   * @return the unit vector.
   */
  public Vector3 normalize() {
    double d = length();
    double s = (d>0.0)?1.0/d:1.0;
    return new Vector3(x*s,y*s,z*s);
  }

  /**
   * Normalizes this vector to have unit length; makes this a unit vector.
   * @return this vector, normalized.
   */
  public Vector3 normalizeEquals() {
    double d = length();
    double s = (d>0.0)?1.0/d:1.0;
    x *= s;
    y *= s;
    z *= s;
    return this;
  }

  /**
   * Returns the scaled vector s*u for this vector u.
   * @return the scaled vector.
   */
  public Vector3 times(double s) {
    return new Vector3(x*s,y*s,z*s);
  }

  /**
   * Scales this vector.
   * @return this vector, scaled.
   */
  public Vector3 timesEquals(double s) {
    x *= s;
    y *= s;
    z *= s;
    return this;
  }

  /**
   * Returns the dot product of this vector u and the specified vector v.
   * @param v the vector v.
   * @return the dot product.
   */
  public double dot(Vector3 v) {
    return x*v.x+y*v.y+z*v.z;
  }

  /**
   * Returns the cross product of this vector u and the specified vector v.
   * @param v the vector v.
   * @return the cross product.
   */
  public Vector3 cross(Vector3 v) {
    return new Vector3(y*v.z-z*v.y,z*v.x-x*v.z,x*v.y-y*v.x);
  }
}
