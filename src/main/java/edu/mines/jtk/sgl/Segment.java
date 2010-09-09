/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.util.MathPlus.DBL_EPSILON;

/**
 * A segment of a line.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.05
 */
public class Segment {

  /**
   * Constructs a segment with the specified endpoints.
   * @param a the endpoint A.
   * @param b the endpoint B.
   */
  public Segment(Point3 a, Point3 b) {
    _a = new Point3(a);
    _b = new Point3(b);
    _d = _b.minus(_a);
  }

  /**
   * Constructs a copy of the specified segment.
   * @param ls the segment.
   */
  public Segment(Segment ls) {
    this(ls._a,ls._b);
  }

  /**
   * Gets the endpoint A of this segment.
   * @return the endpoint A.
   */
  public Point3 getA() {
    return new Point3(_a);
  }

  /**
   * Gets the endpoint B of this segment.
   * @return the endpoint B.
   */
  public Point3 getB() {
    return new Point3(_b);
  }

  /**
   * Returns the length of this segment.
   * @return the length.
   */
  public double length() {
    return _a.distanceTo(_b);
  }

  /**
   * Transforms this segment, given the specified transform matrix.
   * @param m the transform matrix.
   */
  public void transform(Matrix44 m) {
    _a = m.times(_a);
    _b = m.times(_b);
    _d = _b.minus(_a);
  }

  /**
   * Tests this segment for intersection with the specified triangle. If 
   * such an intersection exists, this method returns the intersection point.
   * @param xa x coordinate of triangle vertex a.
   * @param ya y coordinate of triangle vertex a.
   * @param za z coordinate of triangle vertex a.
   * @param xb x coordinate of triangle vertex b.
   * @param yb y coordinate of triangle vertex b.
   * @param zb z coordinate of triangle vertex b.
   * @param xc x coordinate of triangle vertex c.
   * @param yc y coordinate of triangle vertex c.
   * @param zc z coordinate of triangle vertex c.
   * @return the point of intersection; null, if none.
   */
  public Point3 intersectWithTriangle(
    double xa, double ya, double za,
    double xb, double yb, double zb,
    double xc, double yc, double zc)
  {
    double xd = _d.x;
    double yd = _d.y;
    double zd = _d.z;
    double xba = xb-xa;
    double yba = yb-ya;
    double zba = zb-za;
    double xca = xc-xa;
    double yca = yc-ya;
    double zca = zc-za;
    double xp = yd*zca-zd*yca;
    double yp = zd*xca-xd*zca;
    double zp = xd*yca-yd*xca;
    double a = xba*xp+yba*yp+zba*zp;
    if (-TINY<a && a<TINY)
      return null;
    double f = 1.0/a;
    double xaa = _a.x-xa;
    double yaa = _a.y-ya;
    double zaa = _a.z-za;
    double u =  f*(xaa*xp+yaa*yp+zaa*zp);
    if (u<0.0 || u>1.0)
      return null;
    double xq = yaa*zba-zaa*yba;
    double yq = zaa*xba-xaa*zba;
    double zq = xaa*yba-yaa*xba;
    double v = f*(xd*xq+yd*yq+zd*zq);
    if (v<0.0 || u+v>1.0)
      return null;
    double t = f*(xca*xq+yca*yq+zca*zq);
    if (t<0.0 || 1.0<t)
      return null;
    double w = 1.0-u-v;
    double xi = w*xa+u*xb+v*xc;
    double yi = w*ya+u*yb+v*yc;
    double zi = w*za+u*zb+v*zc;
    return new Point3(xi,yi,zi);
  }
  private static final double TINY = 1000.0*DBL_EPSILON;

  private Point3 _a; // endpoint A
  private Point3 _b; // endpoint B
  private Vector3 _d; // vector from A through B
}
