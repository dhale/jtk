/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A bounding sphere.
 * <p>
 * A bounding sphere may be empty or not. An empty sphere contains no points.
 * A non-empty sphere contains at least one point. Some attributes, such as 
 * the sphere center and radius, are meaningful only for spheres that are not 
 * empty.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class BoundingSphere {

  /**
   * Constructs an empty bounding sphere.
   */
  public BoundingSphere() {
  }

  /**
   * Constructs a bounding sphere with specified center and radius.
   * @param x the center x coordinate.
   * @param y the center y coordinate.
   * @param z the center z coordinate.
   * @param r the radius; must be non-negative.
   */
  public BoundingSphere(double x, double y, double z, double r) {
    Check.argument(r>=0.0,"r>=0.0");
    _x = x;
    _y = y;
    _z = z;
    _r = r;
  }

  /**
   * Constructs a bounding sphere with specified center and radius.
   * @param c the center.
   * @param r the radius; must be non-negative.
   */
  public BoundingSphere(Point3 c, double r) {
    this(c.x,c.y,c.z,r);
  }

  /**
   * Determines whether this sphere is empty.
   * @return true, if empty; false, otherwise.
   */
  public boolean isEmpty() {
    return _r>=0.0;
  }

  /**
   * Gets the sphere center.
   * @return the center.
   */
  public Point3 getCenter() {
    return new Point3(_x,_y,_z);
  }

  /**
   * Gets the sphere radius.
   * @return the radius.
   */
  public double getRadius() {
    return _r;
  }

  /**
   * Gets the sphere radius-squared.
   * @return the radius-squared.
   */
  public double getRadiusSquared() {
    return _r*_r;
  }

  /**
   * Expands this sphere to include the point with specified coordinates.
   * Adjusts the sphere center to minimize any increase in radius.
   * @param x the x coordinate of the point.
   * @param y the y coordinate of the point.
   * @param z the z coordinate of the point.
   */
  public void expandBy(double x, double y, double z) {
    if (_r>=0.0) {
      double dx = x-_x;
      double dy = y-_y;
      double dz = z-_z;
      double d = sqrt(dx*dx+dy*dy+dz*dz);
      if (d>_r) {
        double dr = 0.5*(d-_r);
        double ds = dr/d;
        _x += dx*ds;
        _y += dy*ds;
        _z += dz*ds;
        _r += dr;
      }
    } else {
      _x = x;
      _y = y;
      _z = z;
      _r = 0.0;
    }
  }

  /**
   * Expands this sphere to include the point with specified coordinates.
   * Adjusts the sphere center to minimize any increase in radius.
   * @param x the x coordinate of the point.
   * @param y the y coordinate of the point.
   * @param z the z coordinate of the point.
   */
  public void expandRadiusBy(double x, double y, double z) {
  }

  public void expandBy(Point3 p) {
    expandBy(p.x,p.y,p.z);
  }

  public void expandRadiusBy(Point3 p) {
    expandRadiusBy(p.x,p.y,p.z);
  }

  public void expandBy(BoundingSphere bs) {
  }

  public void expandRadiusBy(BoundingSphere bs) {
  }

  public void expandBy(BoundingBox bb) {
  }

  public void expandRadiusBy(BoundingBox bb) {
  }

  public boolean contains(double x, double y, double z) {
    double dx = _x-x;
    double dy = _y-y;
    double dz = _z-z;
    double rs = _r*_r;
    return dx*dx+dy*dy+dz*dz<=rs;
  }

  public boolean contains(Point3 p) {
    return contains(p.x,p.y,p.z);
  }

  private double _x =  0.0; // center x coordinate
  private double _y =  0.0; // center y coordinate
  private double _z =  0.0; // center z coordinate
  private double _r = -1.0; // radius
}
