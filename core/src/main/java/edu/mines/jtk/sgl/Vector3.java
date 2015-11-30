/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
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
   * Constructs a copy of the specified vector.
   * @param v the vector.
   */
  public Vector3(Vector3 v) {
    super(v.x,v.y,v.z);
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
   * Returns the vector sum u+v for this vector u.
   * @param v the other vector.
   * @return the vector sum u+v
   */
  public Vector3 plus(Vector3 v) {
    return new Vector3(x+v.x,y+v.y,z+v.z);
  }

  /**
   * Adds a vector v to this vector u.
   * @param v the other vector.
   * @return this vector, after adding the vector v.
   */
  public Vector3 plusEquals(Vector3 v) {
    x += v.x;
    y += v.y;
    z += v.z;
    return this;
  }

  /**
   * Returns the vector difference u-v for this vector u.
   * @param v the other vector.
   * @return the vector difference u-v
   */
  public Vector3 minus(Vector3 v) {
    return new Vector3(x-v.x,y-v.y,z-v.z);
  }

  /**
   * Subtracts a vector v from this vector u.
   * @param v the other vector.
   * @return this vector, after subtracting the vector v.
   */
  public Vector3 minusEquals(Vector3 v) {
    x -= v.x;
    y -= v.y;
    z -= v.z;
    return this;
  }

  /**
   * Returns the scaled vector s*u for this vector u.
   * @param s the scale factor.
   * @return the scaled vector.
   */
  public Vector3 times(double s) {
    return new Vector3(x*s,y*s,z*s);
  }

  /**
   * Scales this vector.
   * @param s the scale factor.
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
