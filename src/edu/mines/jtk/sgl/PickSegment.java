/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.util.MathPlus.*;

/**
 * A line segment for picking.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.17
 */
public class PickSegment implements Cloneable {

  /**
   * Constructs a pick segment with the specified near and far endpoints.
   * @param n the near endpoint.
   * @param f the far endpoint.
   */
  public PickSegment(Point3 n, Point3 f) {
    _n = n.clone();
    _f = f.clone();
    _d = _f.minus(_n);
  }

  /**
   * Clones this pick segment.
   */
  public PickSegment clone() {
    return new PickSegment(_n,_f);
  }

  /**
   * Gets the near endpoint of this pick segment.
   * @return the near endpoint.
   */
  public Point3 getNearPoint() {
    return _n.clone();
  }

  /**
   * Gets the far endpoint of this pick segment.
   * @return the far endpoint.
   */
  public Point3 getFarPoint() {
    return _f.clone();
  }

  /**
   * Transforms this pick segment, given the specified transform matrix.
   * @param m the transform matrix.
   */
  public void transform(Matrix44 m) {
    _n = m.times(_n);
    _f = m.times(_f);
    _d = _f.minus(_n);
  }

  /**
   * Tests this pick segment for intersection with the specified triangle. If 
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
    double xna = _n.x-xa;
    double yna = _n.y-ya;
    double zna = _n.z-za;
    double u =  f*(xna*xp+yna*yp+zna*zp);
    if (u<0.0 || u>1.0)
      return null;
    double xq = yna*zba-zna*yba;
    double yq = zna*xba-xna*zba;
    double zq = xna*yba-yna*xba;
    double v = f*(xd*xq+yd*yq+zd*zq);
    if (v<0.0 || u+v>1.0)
      return null;
    double t = f*(xca*xq+yca*yq+zca*zq);
    if (t>1.0)
      return null;
    double w = 1.0-u-v;
    double xi = w*xa+u*xb+v*xc;
    double yi = w*ya+u*yb+v*yc;
    double zi = w*za+u*zb+v*zc;
    return new Point3(xi,yi,zi);
  }
  private static final double TINY = 1000.0*DBL_EPSILON;

  private Point3 _n; // near point
  private Point3 _f; // far point
  private Vector3 _d; // vector from near point to far point
}
