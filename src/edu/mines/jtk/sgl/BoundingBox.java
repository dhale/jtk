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
 * An axis-aligned bounding box.
 * <p>
 * A bounding box may be empty or not. An empty box contains no points.
 * A non-empty box contains at least one point. Some attributes, such as 
 * the box minimum and maximum points, center, and radius, are meaningful 
 * only for boxes that are not empty.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class BoundingBox implements Cloneable {

  /**
   * Constructs an empty bounding box.
   */
  public BoundingBox() {
  }

  /**
   * Constructs the smallest bounding box that contains one point.
   * @param p a point.
   */
  public BoundingBox(Point3 p) {
    _xmin = _xmax = p.x;
    _ymin = _ymax = p.y;
    _zmin = _zmax = p.z;
  }

  /**
   * Constructs the smallest bounding box that contains two points.
   * The two points represent two of the eight corners of the box.
   * @param p a point.
   * @param q a point.
   */
  public BoundingBox(Point3 p, Point3 q) {
    _xmin = min(p.x,q.x);
    _ymin = min(p.y,q.y);
    _zmin = min(p.z,q.z);
    _xmax = max(p.x,q.x);
    _ymax = max(p.y,q.y);
    _zmax = max(p.z,q.z);
  }

  /**
   * Constructs a bounding box with specified bounds.
   * @param xmin the minimum x coordinate. 
   * @param ymin the minimum y coordinate. 
   * @param zmin the minimum z coordinate. 
   * @param xmax the maximum x coordinate. 
   * @param ymax the maximum y coordinate. 
   * @param zmax the maximum z coordinate. 
   */
  public BoundingBox(
    double xmin, double ymin, double zmin,
    double xmax, double ymax, double zmax)
  {
    Check.argument(xmin<=xmax,"xmin<=xmax");
    Check.argument(ymin<=ymax,"ymin<=ymax");
    Check.argument(zmin<=zmax,"zmin<=zmax");
    _xmin = xmin;
    _ymin = ymin;
    _zmin = zmin;
    _xmax = xmax;
    _ymax = ymax;
    _zmax = zmax;
  }

  public BoundingBox clone() {
    return new BoundingBox(_xmin,_ymin,_zmin,_xmax,_ymax,_zmax);
  }

  /**
   * Determines whether this box is empty.
   * @return true, if empty; false, otherwise.
   */
  public boolean isEmpty() {
    return _xmin>_xmax || _ymin>_ymax || _zmin>_zmax;
  }

  /**
   * Gets the point in this box with minimum coordinates.
   * @return the minimim point.
   */
  public Point3 getMin() {
    return new Point3(_xmin,_ymin,_zmin);
  }

  /**
   * Gets the point in this box with maximum coordinates.
   * @return the maximim point.
   */
  public Point3 getMax() {
    return new Point3(_xmax,_ymax,_zmax);
  }

  /**
   * Gets the point at the center of this box.
   * @return the box center.
   */
  public Point3 getCenter() {
    return new Point3(0.5*(_xmin+_xmax),0.5*(_ymin+_ymax),0.5*(_zmin+_zmax));
  }

  /**
   * Gets the box radius, the distance from the center to any corner.
   * @return the box radius.
   */
  public double getRadius() {
    return sqrt(getRadiusSquared());
  }

  /**
   * Gets the box radius-squared.
   * @return the box radius-squared.
   */
  public double getRadiusSquared() {
    double dx = _xmax-_xmin;
    double dy = _ymax-_ymin;
    double dz = _zmax-_zmin;
    return 0.25*(dx*dx+dy*dy+dz*dz);
  }

  /**
   * Gets the point at a specified corner of this box.
   * The corner is specified by index, an integer between 0 and 7. From
   * least to most significant, the three bits of this index correspond 
   * to x, y, and z coordinates of a corner point. A zero bit selects a
   * minimum coordinate; a one bit selects a maximum coordinate.
   * @param index the corner index.
   * @return the corner point.
   */
  public Point3 getCorner(int index) {
    double x = ((index&1)==0)?_xmin:_xmax;
    double y = ((index&2)==0)?_ymin:_ymax;
    double z = ((index&4)==0)?_zmin:_zmax;
    return new Point3(x,y,z);
  }

  /**
   * Expands this box to include the point with specified coordinates.
   * @param x the point x coordinate.
   * @param y the point y coordinate.
   * @param z the point z coordinate.
   */
  public void expandBy(double x, double y, double z) {
    if (_xmin>x) _xmin = x;
    if (_ymin>y) _ymin = y;
    if (_zmin>z) _zmin = z;
    if (_xmax<x) _xmax = x;
    if (_ymax<y) _ymax = y;
    if (_zmax<z) _zmax = z;
  }

  /**
   * Expands this box to include the specified point.
   * @param p the point.
   */
  public void expandBy(Point3 p) {
    expandBy(p.x,p.y,p.z);
  }

  /**
   * Expands this box to include the specified bounding box.
   * @param bb the bounding box.
   */
  public void expandBy(BoundingBox bb) {
    if (bb.isEmpty())
      return;
    if (_xmin>bb._xmin) _xmin = bb._xmin;
    if (_ymin>bb._ymin) _ymin = bb._ymin;
    if (_zmin>bb._zmin) _zmin = bb._zmin;
    if (_xmax<bb._xmax) _xmax = bb._xmax;
    if (_ymax<bb._ymax) _ymax = bb._ymax;
    if (_zmax<bb._zmax) _zmax = bb._zmax;
  }

  /**
   * Expands this box to include the specified bounding sphere.
   * @param bs the bounding sphere.
   */
  public void expandBy(BoundingSphere bs) {
    if (bs.isEmpty())
      return;
    Point3 c = bs.getCenter();
    double x = c.x;
    double y = c.y;
    double z = c.z;
    double r = bs.getRadius();
    if (x-r<_xmin) _xmin = x-r;
    if (y-r<_ymin) _ymin = y-r;
    if (z-r<_zmin) _zmin = z-r;
    if (x+r<_xmax) _xmax = x+r;
    if (y+r<_ymax) _ymax = y+r;
    if (z+r<_zmax) _zmax = z+r;
  }

  /**
   * Determines whether this box contains the point with specified coordinates.
   * @param x the point x coordinate.
   * @param y the point y coordinate.
   * @param z the point z coordinate.
   * @return true, if this box contains the point; false, otherwise.
   */
  public boolean contains(double x, double y, double z) {
    return _xmin<=x && x<=_xmax &&
           _ymin<=y && y<=_ymax &&
           _zmin<=z && z<=_zmax;
  }

  /**
   * Determines whether this box contains the specified point.
   * @param p the point.
   * @return true, if this box contains the point; false, otherwise.
   */
  public boolean contains(Point3 p) {
    return contains(p.x,p.y,p.z);
  }

  /**
   * Determines whether this box intersects the specified bounding box.
   * @param bb the bounding box.
   * @return true, if intersects; false, otherwise.
   */
  public boolean intersects(BoundingBox bb) {
    return max(_xmin,bb._xmin)<=min(_xmax,bb._xmax) &&
           max(_ymin,bb._ymin)<=min(_ymax,bb._ymax) &&
           max(_zmin,bb._zmin)<=min(_zmax,bb._zmax);
  }

  private double _xmin =  DBL_MAX;
  private double _ymin =  DBL_MAX;
  private double _zmin =  DBL_MAX;
  private double _xmax = -DBL_MAX;
  private double _ymax = -DBL_MAX;
  private double _zmax = -DBL_MAX;
}
