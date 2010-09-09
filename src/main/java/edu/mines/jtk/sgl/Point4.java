/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A point with four coordinates x, y, z, and w.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.19
 */
public class Point4 extends Tuple4 {

  /**
   * Constructs a point with coordinates (x,y,z) = 0 and w = 1.
   */
  public Point4() {
    super(0.0,0.0,0.0,1.0);
  }

  /**
   * Constructs a point with specified (x,y,z) coordinates and w = 1.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param z the z coordinate.
   */
  public Point4(double x, double y, double z) {
    super(x,y,z,1.0);
  }

  /**
   * Constructs a point with specified coordinates.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param z the z coordinate.
   * @param w the w coordinate.
   */
  public Point4(double x, double y, double z, double w) {
    super(x,y,z,w);
  }

  /**
   * Constructs a point from the specified 3-D point.
   * The (x,y,z,w) coordinates of the constructed point are (p.x,p.y,p.z,1).
   * @param p the 3-D point.
   */
  public Point4(Point3 p) {
    super(p.x,p.y,p.z,1.0);
  }

  /**
   * Constructs a copy of the specified point.
   * @param p the point.
   */
  public Point4(Point4 p) {
    super(p.x,p.y,p.z,p.w);
  }

  /**
   * Returns the homogenized point equivalent to this point.
   * Homogenization is division of the coordinates (x,y,z,w) by w.
   * @return the homogenized point.
   */
  public Point4 homogenize() {
    return new Point4(x/w,y/w,z/w,1.0);
  }

  /**
   * Homogenizes this point.
   * Homogenization is division of the coordinates (x,y,z,w) by w.
   * @return this homogenized point.
   */
  public Point4 homogenizeEquals() {
    x /= w;
    y /= w;
    z /= w;
    w = 1.0;
    return this;
  }

  /**
   * Returns the point q = p+v, for this point p and the specified vector v.
   * @param v the vector v.
   * @return the point q = p+v.
   */
  public Point4 plus(Vector3 v) {
    return new Point4(x+v.x,y+v.y,z+v.z,w);
  }

  /**
   * Returns the point q = p-v, for this point p and the specified vector v.
   * @param v the vector v.
   * @return the point q = p-v.
   */
  public Point4 minus(Vector3 v) {
    return new Point4(x-v.x,y-v.y,z-v.z,w);
  }

  /**
   * Moves this point p by adding the specified vector v.
   * @param v the vector v.
   * @return this point, p += v, moved.
   */
  public Point4 plusEquals(Vector3 v) {
    x += v.x;
    y += v.y;
    z += v.z;
    return this;
  }

  /**
   * Moves this point p by subtracting the specified vector v.
   * @param v the vector v.
   * @return this point, p -= v, moved.
   */
  public Point4 minusEquals(Vector3 v) {
    x -= v.x;
    y -= v.y;
    z -= v.z;
    return this;
  }

  /**
   * Returns an affine combination of this point p and the specified point q.
   * @param a the weight of the point q.
   * @param q the point q.
   * @return the affine combination (1-a)*p + a*q.
   */
  public Point4 affine(double a, Point4 q) {
    double b = 1.0-a;
    Point4 p = this;
    return new Point4(b*p.x+a*q.x,b*p.y+a*q.y,b*p.z+a*q.z,b*p.w+a*q.w);
  }
}
