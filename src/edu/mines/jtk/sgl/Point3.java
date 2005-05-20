/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A point with three coordinates x, y, and z.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.20
 */
public class Point3 extends Tuple3 {

  /**
   * Constructs a point with coordinates zero.
   */
  public Point3() {
  }

  /**
   * Constructs a point with specified coordinates.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param z the z coordinate.
   */
  public Point3(double x, double y, double z) {
    super(x,y,z);
  }

  /**
   * Returns a clone of this point.
   * @return the clone.
   */
  public Point3 clone() {
    return new Point3(x,y,z);
  }

  /**
   * Moves this point by adding the specified vector v.
   * @param v the vector.
   * @return this point, moved.
   */
  public Point3 plusEquals(Vector3 v) {
    x += v.x;
    y += v.y;
    z += v.z;
    return this;
  }

  /**
   * Moves this point by subtracting the specified vector v.
   * @param v the vector.
   * @return this point, moved.
   */
  public Point3 minusEquals(Vector3 v) {
    x -= v.x;
    y -= v.y;
    z -= v.z;
    return this;
  }

  /**
   * Returns the vector q-p from the specified point p to this point q.
   * @param p the point p.
   * @return the vector.
   */
  public Vector3 minus(Point3 p) {
    return new Vector3(this.x-p.x,this.y-p.y,this.z-p.z);
  }

  /**
   * Returns an affine combination of this point q and the specified point p.
   * @param a the weight of the point p.
   * @param p the point p.
   * @return the affine combination (1-a)*q + a*p.
   */
  public Point3 affine(double a, Point3 p) {
    double b = 1.0-a;
    Point3 q = this;
    return new Point3(b*q.x+a*p.x,b*q.y+a*p.y,b*q.z+a*p.z);
  }

  /**
   * Returns the distance between this point q and the specified point p.
   * @param p the point.
   * @return the distance |q-p|.
   */
  public double distanceTo(Point3 p) {
    double dx = x-p.x;
    double dy = y-p.y;
    double dz = z-p.z;
    return Math.sqrt(dx*dx+dy*dy+dz*dz);
  }
}
