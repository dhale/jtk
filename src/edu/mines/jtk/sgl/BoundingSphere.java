/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static java.lang.Math.sqrt;

import edu.mines.jtk.util.Check;

/**
 * A bounding sphere.
 * <p>
 * A bounding sphere may be empty. An empty sphere contains no points. A 
 * non-empty sphere contains at least one point. Some attributes, such as 
 * the sphere center and radius, are defined only for spheres that are not 
 * empty.
 * <p>
 * A bounding sphere may be infinite. An infinite sphere contains all points. 
 * Its radius is Double.POSITIVE_INFINITY, and its center is undefined.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.28
 */
public class BoundingSphere {

  /**
   * Constructs an empty bounding sphere.
   */
  public BoundingSphere() {
    setEmpty();
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
   * Constructs a bounding sphere that contains the specified bounding box.
   * @param bb the bounding box.
   */
  public BoundingSphere(BoundingBox bb) {
    this();
    expandBy(bb);
  }

  /**
   * Constructs a copy of the specified bounding sphere.
   * @param bs the bounding sphere.
   */
  public BoundingSphere(BoundingSphere bs) {
    _x = bs._x;
    _y = bs._y;
    _z = bs._z;
    _r = bs._r;
  }

  /**
   * Determines whether this sphere is empty.
   * @return true, if empty; false, otherwise.
   */
  public boolean isEmpty() {
    return _r<0.0;
  }

  /**
   * Determines whether this sphere is infinite.
   * @return true, if infinite; false, otherwise.
   */
  public boolean isInfinite() {
    return _r==Double.POSITIVE_INFINITY;
  }

  /**
   * Gets the sphere center.
   * @return the center.
   */
  public Point3 getCenter() {
    Check.state(!isEmpty(),"bounding sphere is not empty");
    Check.state(!isInfinite(),"bounding sphere is not infinite");
    return new Point3(_x,_y,_z);
  }

  /**
   * Gets the sphere radius.
   * @return the radius.
   */
  public double getRadius() {
    Check.state(!isEmpty(),"bounding sphere is not empty");
    return _r;
  }

  /**
   * Gets the sphere radius-squared.
   * @return the radius-squared.
   */
  public double getRadiusSquared() {
    Check.state(!isEmpty(),"bounding sphere is not empty");
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
    if (!isInfinite()) {
      if (!isEmpty()) {
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
  }

  /**
   * Expands this sphere to include the point with specified coordinates.
   * Changes only the radius, if necessary, not the center of this sphere.
   * @param x the x coordinate of the point.
   * @param y the y coordinate of the point.
   * @param z the z coordinate of the point.
   */
  public void expandRadiusBy(double x, double y, double z) {
    if (!isInfinite()) {
      if (!isEmpty()) {
        double dx = x-_x;
        double dy = y-_y;
        double dz = z-_z;
        double d = sqrt(dx*dx+dy*dy+dz*dz);
        if (d>_r)
          _r = d;
      } else {
        _x = x;
        _y = y;
        _z = z;
        _r = 0.0;
      }
    }
  }

  /**
   * Expands this sphere to include the specified point.
   * Adjusts the sphere center to minimize any increase in radius.
   * @param p the point.
   */
  public void expandBy(Point3 p) {
    expandBy(p.x,p.y,p.z);
  }

  /**
   * Expands this sphere to include the specified point.
   * Changes only the radius, if necessary, not the center of this sphere.
   * @param p the point.
   */
  public void expandRadiusBy(Point3 p) {
    expandRadiusBy(p.x,p.y,p.z);
  }

  /**
   * Expands this sphere to include the specified bounding sphere.
   * Adjusts the sphere center to minimize any increase in radius.
   * @param bs the bounding sphere.
   */
  public void expandBy(BoundingSphere bs) {
    if (!isInfinite()) {
      if (!bs.isInfinite()) {
        if (!bs.isEmpty()) {
          if (!isEmpty()) {
            double dx = bs._x-_x;
            double dy = bs._y-_y;
            double dz = bs._z-_z;
            double d = sqrt(dx*dx+dy*dy+dz*dz);
            if (d==0.0 && bs._r>_r) {
              _r = bs._r;
            } else if (d+bs._r>_r) {
              double da = _r/d;
              double xa = _x-dx*da;
              double ya = _y-dy*da;
              double za = _z-dz*da;
              double db = bs._r/d;
              double xb = bs._x+dx*db;
              double yb = bs._y+dy*db;
              double zb = bs._z+dz*db;
              dx = xb-_x;
              dy = yb-_y;
              dz = zb-_z;
              _r = sqrt(dx*dx+dy*dy+dz*dz);
              _x = 0.5*(xa+xb);
              _y = 0.5*(ya+yb);
              _z = 0.5*(za+zb);
            }
          } else {
            _r = bs._r;
            _x = bs._x;
            _y = bs._y;
            _z = bs._z;
          }
        }
      } else {
        setInfinite();
      }
    }
  }

  /**
   * Expands this sphere to include the specified bounding sphere.
   * Changes only the radius, if necessary, not the center of this sphere.
   * @param bs the bounding sphere.
   */
  public void expandRadiusBy(BoundingSphere bs) {
    if (!isInfinite()) {
      if (!bs.isInfinite()) {
        if (!bs.isEmpty()) {
          if (!isEmpty()) {
            double dx = bs._x-_x;
            double dy = bs._y-_y;
            double dz = bs._z-_z;
            double d = sqrt(dx*dx+dy*dy+dz*dz);
            double r = d+bs._r;
            if (r>_r)
              _r = r;
          } else {
            _r = bs._r;
            _x = bs._x;
            _y = bs._y;
            _z = bs._z;
          }
        }
      } else {
        setInfinite();
      }
    }
  }

  /**
   * Expands this sphere to include the specified bounding box.
   * Adjusts the sphere center to minimize any increase in radius.
   * @param bb the bounding box.
   */
  public void expandBy(BoundingBox bb) {
    if (!isInfinite()) {
      if (!bb.isInfinite()) {
        if (!bb.isEmpty()) {
          Point3 pmin = bb.getMin();
          Point3 pmax = bb.getMax();
          double xmin = pmin.x;
          double ymin = pmin.y;
          double zmin = pmin.z;
          double xmax = pmax.x;
          double ymax = pmax.y;
          double zmax = pmax.z;
          if (!isEmpty()) {
            for (int i=0; i<8; ++i) {
              double x = ((i&1)==0)?xmin:xmax;
              double y = ((i&2)==0)?ymin:ymax;
              double z = ((i&4)==0)?zmin:zmax;
              double dx = x-_x;
              double dy = y-_y;
              double dz = z-_z;
              double d = sqrt(dx*dx+dy*dy+dz*dz);
              double ds = (d>0.0)?_r/d:_r;
              x = _x-dx*ds;
              y = _y-dy*ds;
              z = _z-dz*ds;
              if (x<xmin) xmin = x;
              if (y<ymin) ymin = y;
              if (z<zmin) zmin = z;
              if (x>xmax) xmax = x;
              if (y>ymax) ymax = y;
              if (z>zmax) zmax = z;
            }
          }
          double dx = xmax-xmin;
          double dy = ymax-ymin;
          double dz = zmax-zmin;
          _r = 0.5*sqrt(dx*dx+dy*dy+dz*dz);
          _x = 0.5*(xmin+xmax);
          _y = 0.5*(ymin+ymax);
          _z = 0.5*(zmin+zmax);
        }
      } else {
        setInfinite();
      }
    }
  }

  /**
   * Expands this sphere to include the specified bounding box.
   * Changes only the radius, if necessary, not the center of this sphere.
   * @param bb the bounding box.
   */
  public void expandRadiusBy(BoundingBox bb) {
    if (!isInfinite()) {
      if (!bb.isInfinite()) {
        if (!bb.isEmpty()) {
          Point3 pmin = bb.getMin();
          Point3 pmax = bb.getMax();
          double xmin = pmin.x;
          double ymin = pmin.y;
          double zmin = pmin.z;
          double xmax = pmax.x;
          double ymax = pmax.y;
          double zmax = pmax.z;
          if (!isEmpty()) {
            for (int i=0; i<8; ++i) {
              double x = ((i&1)==0)?xmin:xmax;
              double y = ((i&2)==0)?ymin:ymax;
              double z = ((i&4)==0)?zmin:zmax;
              expandRadiusBy(x,y,z);
            }
          } else {
            double dx = xmax-xmin;
            double dy = ymax-ymin;
            double dz = zmax-zmin;
            _r = 0.5*sqrt(dx*dx+dy*dy+dz*dz);
            _x = 0.5*(xmin+xmax);
            _y = 0.5*(ymin+ymax);
            _z = 0.5*(zmin+zmax);
          }
        }
      } else {
        setInfinite();
      }
    }
  }

  /**
   * Determines whether this sphere contains a point with specified coordinates.
   * @param x the point x coordinate.
   * @param y the point y coordinate.
   * @param z the point z coordinate.
   * @return true, if this sphere contains the point; false, otherwise.
   */
  public boolean contains(double x, double y, double z) {
    if (isEmpty())
      return false;
    if (isInfinite())
      return true;
    double dx = _x-x;
    double dy = _y-y;
    double dz = _z-z;
    double rs = _r*_r;
    return dx*dx+dy*dy+dz*dz<=rs;
  }

  /**
   * Determines whether this sphere contains the specified point.
   * @param p the point.
   * @return true, if this sphere contains the point; false, otherwise.
   */
  public boolean contains(Point3 p) {
    return contains(p.x,p.y,p.z);
  }

  /**
   * Returns a new empty bounding sphere.
   * @return a new empty bounding sphere.
   */
  public static BoundingSphere empty() {
    return new BoundingSphere();
  }

  /**
   * Returns a new infinite bounding sphere.
   * @return a new infinite bounding sphere.
   */
  public static BoundingSphere infinite() {
    BoundingSphere bs = new BoundingSphere();
    bs.setInfinite();
    return bs;
  }

  public String toString() {
    return "{"+getCenter()+":"+getRadius()+"}";
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _x =  0.0; // center x coordinate
  private double _y =  0.0; // center y coordinate
  private double _z =  0.0; // center z coordinate
  private double _r = -1.0; // radius

  /**
   * Sets this sphere to the empty sphere.
   */
  private void setEmpty() {
    _x = 0.0;
    _y = 0.0;
    _z = 0.0;
    _r = -1.0;
  }

  /**
   * Sets this sphere to the infinite sphere.
   */
  private void setInfinite() {
    _x = 0.0;
    _y = 0.0;
    _z = 0.0;
    _r = Double.POSITIVE_INFINITY;
  }
}
