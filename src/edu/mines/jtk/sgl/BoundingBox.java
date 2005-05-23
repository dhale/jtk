/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A bounding box.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class BoundingBox {

  public BoundingBox(Point3 p, Point3 q) {
    _xmin = Math.min(p.x,q.x);
    _ymin = Math.min(p.y,q.y);
    _zmin = Math.min(p.z,q.z);
    _xmax = Math.max(p.x,q.x);
    _ymax = Math.max(p.y,q.y);
    _zmax = Math.max(p.z,q.z);
  }

  public boolean isValid() {
    return _xmin<=_xmax && _ymin<=_ymax && _zmin<=_zmax;
  }

  public Point3 getMin() {
    return new Point3(_xmin,_ymin,_zmin);
  }

  public Point3 getMax() {
    return new Point3(_xmax,_ymax,_zmax);
  }

  public Point3 getCenter() {
    return new Point3(0.5*(_xmin+_xmax),0.5*(_ymin+_ymax),0.5*(_zmin+_zmax));
  }

  public double getRadius() {
    return Math.sqrt(getRadiusSquared());
  }

  public double getRadiusSquared() {
    double dx = _xmax-_xmin;
    double dy = _ymax-_ymin;
    double dz = _zmax-_zmin;
    return 0.25*(dx*dx+dy*dy+dz*dz);
  }

  public void expandBy(Point3 p) {
    expandBy(p.x,p.y,p.z);
  }

  public void expandBy(double x, double y, double z) {
    if (_xmin>x) _xmin = x;
    if (_ymin>y) _ymin = y;
    if (_zmin>z) _zmin = z;
    if (_xmax<x) _xmax = x;
    if (_ymax<y) _ymax = y;
    if (_zmax<z) _zmax = z;
  }

  public void expandBy(BoundingBox bb) {
    if (!bb.isValid())
      return;
    if (_xmin>bb._xmin) _xmin = bb._xmin;
    if (_ymin>bb._ymin) _ymin = bb._ymin;
    if (_zmin>bb._zmin) _zmin = bb._zmin;
    if (_xmax<bb._xmax) _xmax = bb._xmax;
    if (_ymax<bb._ymax) _ymax = bb._ymax;
    if (_zmax<bb._zmax) _zmax = bb._zmax;
  }

  public boolean contains(Point3 p) {
    return isValid() &&
           _xmin<=p.x && p.x<=_xmax &&
           _ymin<=p.y && p.y<=_ymax &&
           _zmin<=p.z && p.z<=_zmax;
  }

  private double _xmin,_ymin,_zmin;
  private double _xmax,_ymax,_zmax;
}
