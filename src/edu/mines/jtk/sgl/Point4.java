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
   * Returns a clone of this point.
   * @return the clone.
   */
  public Point4 clone() {
    return new Point4(x,y,z,w);
  }

  /**
   * Moves this point q by adding the specified vector v.
   * @param v the vector.
   * @return this point q += v.
   */
  public Point4 plusEquals(Vector3 v) {
    x += v.x;
    y += v.y;
    z += v.z;
    return this;
  }

  /**
   * Moves this point by subtracting the specified vector.
   * @param v the vector.
   * @return this point q -= v.
   */
  public Point4 minusEquals(Vector3 v) {
    x -= v.x;
    y -= v.y;
    z -= v.z;
    return this;
  }

  /**
   * Returns an affine combination of this point q and the specified point p.
   * @param a the weight of the point p.
   * @param p the point p.
   * @return the affine combination (1-a)*q + a*p.
   */
  public Point4 affine(double a, Point4 p) {
    double b = 1.0-a;
    Point4 q = this;
    return new Point4(b*q.x+a*p.x,b*q.y+a*p.y,b*q.z+a*p.z,b*q.w+a*p.w);
  }
}
