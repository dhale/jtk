/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static java.lang.Math.*;

import edu.mines.jtk.util.Check;

/**
 * An axis-aligned bounding box.
 * <p>
 * A bounding box may be empty. An empty box contains no points. A non-empty 
 * box contains at least one point. Some attributes, such as the box minimum 
 * and maximum points, center, and radius, are defined only for boxes that 
 * are not empty.
 * <p>
 * A bounding box may be infinite. An infinite box contains all points. 
 * Its minimum and maximum points are at Double.NEGATIVE_INFINITY and
 * Double.POSITIVE_INFINITY, respectively, and its center is undefined.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.28
 */
public class BoundingBox {

  /**
   * Constructs an empty bounding box.
   */
  public BoundingBox() {
    setEmpty();
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

  /**
   * Constructs a bounding box for points with specified coordinates.
   * The (x,y,z) coordinates are packed into the specified array such
   * that (xyz[0],xyz[1],xyz[2]) are the (x,y,z) coordinates of the 
   * 1st point, (xyz[3],xyz[4],xyz[5]) are the (x,y,z) coordinates of 
   * the 2nd point, and so on.
   * @param xyz array of packed (x,y,z) coordinates.
   */
  public BoundingBox(float[] xyz) {
    this();
    expandBy(xyz);
  }

  /**
   * Constructs a bounding box for points with specified coordinates.
   * @param x array of x coordinates.
   * @param y array of y coordinates.
   * @param z array of z coordinates.
   */
  public BoundingBox(float[] x, float[] y, float[] z) {
    this();
    expandBy(x,y,z);
  }

  /**
   * Constructs a copy of the specified bounding box.
   * @param bb the bounding box.
   */
  public BoundingBox(BoundingBox bb) {
    _xmin = bb._xmin;
    _ymin = bb._ymin;
    _zmin = bb._zmin;
    _xmax = bb._xmax;
    _ymax = bb._ymax;
    _zmax = bb._zmax;
  }

  /**
   * Determines whether this box is empty.
   * @return true, if empty; false, otherwise.
   */
  public boolean isEmpty() {
    return _xmin>_xmax || _ymin>_ymax || _zmin>_zmax;
  }

  /**
   * Determines whether this box is infinite.
   * @return true, if infinite; false, otherwise.
   */
  public boolean isInfinite() {
    return _xmin==Double.NEGATIVE_INFINITY &&
           _ymin==Double.NEGATIVE_INFINITY &&
           _zmin==Double.NEGATIVE_INFINITY &&
           _xmax==Double.POSITIVE_INFINITY &&
           _ymax==Double.POSITIVE_INFINITY &&
           _zmax==Double.POSITIVE_INFINITY;
  }

  /**
   * Gets the point in this box with minimum coordinates.
   * @return the minimim point.
   */
  public Point3 getMin() {
    Check.state(!isEmpty(),"bounding box is not empty");
    return new Point3(_xmin,_ymin,_zmin);
  }

  /**
   * Gets the point in this box with maximum coordinates.
   * @return the maximim point.
   */
  public Point3 getMax() {
    Check.state(!isEmpty(),"bounding box is not empty");
    return new Point3(_xmax,_ymax,_zmax);
  }

  /**
   * Gets the point at the center of this box.
   * @return the box center.
   */
  public Point3 getCenter() {
    Check.state(!isEmpty(),"bounding box is not empty");
    Check.state(!isInfinite(),"bounding box is not infinite");
    return (isInfinite()) ?
           new Point3(0.0,0.0,0.0) :
           new Point3(0.5*(_xmin+_xmax),0.5*(_ymin+_ymax),0.5*(_zmin+_zmax));
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
    Check.state(!isEmpty(),"bounding box is not empty");
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
    Check.state(!isEmpty(),"bounding box is not empty");
    double x = ((index&1)==0)?_xmin:_xmax;
    double y = ((index&2)==0)?_ymin:_ymax;
    double z = ((index&4)==0)?_zmin:_zmax;
    return new Point3(x,y,z);
  }

  /**
   * Expands this box to include the specified point.
   * @param p the point.
   */
  public void expandBy(Point3 p) {
    expandBy(p.x,p.y,p.z);
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
   * Expands this box to include the points with specified coordinates.
   * The (x,y,z) coordinates are packed into the specified array, such
   * that (xyz[0],xyz[1],xyz[2]) are the (x,y,z) coordinates of the 1st
   * point, (xyz[3],xyz[4],xyz[5]) are the (x,y,z) coordinates of the 2nd
   * point, and so on.
   * @param xyz array of packed (x,y,z) coordinates.
   */
  public void expandBy(float[] xyz) {
    int n = xyz.length;
    for (int i=0; i<n; i+=3)
      expandBy(xyz[i],xyz[i+1],xyz[i+2]);
  }

  /**
   * Expands this box to include the points with specified coordinates.
   * @param x array of x coordinates.
   * @param y array of y coordinates.
   * @param z array of z coordinates.
   */
  public void expandBy(float[] x, float[] y, float[] z) {
    int n = x.length;
    for (int i=0; i<n; ++i)
      expandBy(x[i],y[i],z[i]);
  }

  /**
   * Expands this box to include the specified bounding box.
   * @param bb the bounding box.
   */
  public void expandBy(BoundingBox bb) {
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
    if (!bs.isInfinite()) {
      if (!bs.isEmpty()) {
        double r = bs.getRadius();
        Point3 c = bs.getCenter();
        double x = c.x;
        double y = c.y;
        double z = c.z;
        if (_xmin>x-r) _xmin = x-r;
        if (_ymin>y-r) _ymin = y-r;
        if (_zmin>z-r) _zmin = z-r;
        if (_xmax<x+r) _xmax = x+r;
        if (_ymax<y+r) _ymax = y+r;
        if (_zmax<z+r) _zmax = z+r;
      }
    } else {
      setInfinite();
    }
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
   * Determines whether this box contains the specified bounding box.
   * @param bb the bounding box.
   * @return true, if this box contains the specified box; false, otherwise.
   */
  public boolean contains(BoundingBox bb) {
    return contains(bb._xmin,bb._ymin,bb._zmin) &&
           contains(bb._xmax,bb._ymax,bb._zmax);
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

  /**
   * Returns a new empty bounding box.
   * @return a new empty bounding box.
   */
  public static BoundingBox empty() {
    return new BoundingBox();
  }

  /**
   * Returns a new infinite bounding box.
   * @return a new infinite bounding box.
   */
  public static BoundingBox infinite() {
    BoundingBox bb = new BoundingBox();
    bb.setInfinite();
    return bb;
  }

  public String toString() {
    return "{"+getMin()+":"+getMax()+"}";
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _xmin;
  private double _ymin;
  private double _zmin;
  private double _xmax;
  private double _ymax;
  private double _zmax;

  /**
   * Sets this box to the empty box.
   */
  private void setEmpty() {
    _xmin = Double.POSITIVE_INFINITY;
    _ymin = Double.POSITIVE_INFINITY;
    _zmin = Double.POSITIVE_INFINITY;
    _xmax = Double.NEGATIVE_INFINITY;
    _ymax = Double.NEGATIVE_INFINITY;
    _zmax = Double.NEGATIVE_INFINITY;
  }

  /**
   * Sets this box to the infinite box.
   */
  private void setInfinite() {
    _xmin = Double.NEGATIVE_INFINITY;
    _ymin = Double.NEGATIVE_INFINITY;
    _zmin = Double.NEGATIVE_INFINITY;
    _xmax = Double.POSITIVE_INFINITY;
    _ymax = Double.POSITIVE_INFINITY;
    _zmax = Double.POSITIVE_INFINITY;
  }
}
