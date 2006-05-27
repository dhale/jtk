/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static java.lang.Math.*;

import edu.mines.jtk.dsp.*;

/**
 * A constraint for objects that must lie inside an axis-aligned box.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.05.25
 */
public class BoxConstraint {

  /**
   * Constructs a box constraint with specified bounding box.
   * Constrains objects to lie inside the bounding box.
   * @param box bounding box.
   */
  public BoxConstraint(BoundingBox box) {
    this(box.getMin(),box.getMax());
  }

  /**
   * Constructs a box constraint with specified corner points.
   * Constrains objects to lie inside a box defined by the corner points.
   * @param p a corner point.
   * @param q a corner point.
   */
  public BoxConstraint(Point3 p, Point3 q) {
    _xmin = min(p.x,q.x);
    _ymin = min(p.y,q.y);
    _zmin = min(p.z,q.z);
    _xmax = max(p.x,q.x);
    _ymax = max(p.y,q.y);
    _zmax = max(p.z,q.z);
    _sampled = false;
  }

  /**
   * Constructs a box constraint with specified samplings.
   * Constrains objects vertices to lie on the sampling grid.
   * @param sx sampling of x coordinate
   * @param sy sampling of y coordinate
   * @param sz sampling of z coordinate
   */
  public BoxConstraint(Sampling sx, Sampling sy, Sampling sz) {
    _xmin = sx.getFirst();
    _ymin = sy.getFirst();
    _zmin = sz.getFirst();
    _xmax = sx.getLast();
    _ymax = sy.getLast();
    _zmax = sz.getLast();
    _sx = sx;
    _sy = sy;
    _sz = sz;
    _sampled = true;
  }

  /**
   * Gets the bounding box for this constraint.
   * @return the bounding box.
   */
  public BoundingBox getBoundingBox() {
    return new BoundingBox(_xmin,_ymin,_zmin,_xmax,_ymax,_zmax);
  }

  /**
   * Gets the bounding sphere for this constraint.
   * @return the bounding sphere.
   */
  public BoundingSphere getBoundingSphere() {
    BoundingSphere bs = new BoundingSphere();
    bs.expandBy(getBoundingBox());
    return bs;
  }

  /**
   * Sets minimum sizes for objects constrained by this box. The dimensions 
   * of any constrained object will not be less than the specified sizes.
   * The default minimum sizes are zero.
   * @param dxmin minimum size in x dimension.
   * @param dymin minimum size in y dimension.
   * @param dzmin minimum size in z dimension.
   */
  public void setMinimum(double dxmin, double dymin, double dzmin) {
    _dxmin = dxmin;
    _dymin = dymin;
    _dzmin = dzmin;
  }

  /**
   * Constrains a specified point.
   * @param the point.
   */
  public void constrainPoint(Point3 p) {
    p.x = max(_xmin,min(_xmax,p.x));
    p.y = max(_ymin,min(_ymax,p.y));
    p.z = max(_zmin,min(_zmax,p.z));
    if (_sampled) {
      p.x = _sx.valueOfNearest(p.x);
      p.y = _sy.valueOfNearest(p.y);
      p.z = _sz.valueOfNearest(p.z);
    }
  }

  /**
   * Constrains a box defined by two specified corner points.
   * If necessary, modifies either or both of the points to satisfy
   * this constraint. When possible, modifies the second point q
   * more than the first point p.
   * @param p a corner point
   * @param q a corner point
   */
  public void constrainBox(Point3 p, Point3 q) {
    double ax = min(p.x,q.x);
    double bx = max(p.x,q.x);
    double dx = max(bx-ax,_dxmin);
    if (p.x<=q.x) {
      ax = max(_xmin,min(_xmax,ax));
      bx = min(_xmax,ax+dx);
      ax = max(_xmin,bx-dx);
    } else {
      bx = min(_xmax,max(_xmin,bx));
      ax = max(_xmin,bx-dx);
      bx = min(_xmax,ax+dx);
    }
    if (_sampled) {
      ax = _sx.valueOfNearest(ax);
      bx = _sx.valueOfNearest(bx);
    }
    if (p.x<=q.x) {
      p.x = ax;
      q.x = bx;
    } else {
      p.x = bx;
      q.x = ax;
    }
    double ay = min(p.y,q.y);
    double by = max(p.y,q.y);
    double dy = max(by-ay,_dymin);
    if (p.y<=q.y) {
      ay = max(_ymin,min(_ymax,ay));
      by = min(_ymax,ay+dy);
      ay = max(_ymin,by-dy);
    } else {
      by = min(_ymax,max(_ymin,by));
      ay = max(_ymin,by-dy);
      by = min(_ymax,ay+dy);
    }
    if (_sampled) {
      ay = _sy.valueOfNearest(ay);
      by = _sy.valueOfNearest(by);
    }
    if (p.y<=q.y) {
      p.y = ay;
      q.y = by;
    } else {
      p.y = by;
      q.y = ay;
    }
    double az = min(p.z,q.z);
    double bz = max(p.z,q.z);
    double dz = max(bz-az,_dzmin);
    if (p.z<=q.z) {
      az = max(_zmin,min(_zmax,az));
      bz = min(_zmax,az+dz);
      az = max(_zmin,bz-dz);
    } else {
      bz = min(_zmax,max(_zmin,bz));
      az = max(_zmin,bz-dz);
      bz = min(_zmax,az+dz);
    }
    if (_sampled) {
      az = _sz.valueOfNearest(az);
      bz = _sz.valueOfNearest(bz);
    }
    if (p.z<=q.z) {
      p.z = az;
      q.z = bz;
    } else {
      p.z = bz;
      q.z = az;
    }
  }

  private double _xmin,_ymin,_zmin;
  private double _xmax,_ymax,_zmax;
  private double _dxmin,_dymin,_dzmin;
  private Sampling _sx,_sy,_sz;
  private boolean _sampled;
}
