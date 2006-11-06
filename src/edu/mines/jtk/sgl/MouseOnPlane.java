/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.event.MouseEvent;

/**
 * A mouse constrained by a plane. Constrained points (in local coordinates) 
 * lie on a constraint plane that contains a specified origin point and is
 * parallel to a specified plane.
 * <p>
 * Given a mouse event with pixel (x,y) coordinates, a mouse-on-plane computes 
 * a constrained point. When the mouse pixel coordinates equal those used to 
 * construct the mouse-on-plane, the constrained point equals the specified 
 * origin point.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.05
 */
public class MouseOnPlane extends MouseConstrained {

  /**
   * Constructs a mouse constrained by a plane. The constraint plane contains 
   * the specified origin point and is parallel to the specified plane. The 
   * origin point is typically near, but not necessarily on, the specified 
   * plane.
   * @param event the initial mouse event.
   * @param origin the origin point, in local coordinates.
   * @param plane the plane, in local coordinates.
   * @param localToPixel the transform from local to pixel coordinates.
   */
  public MouseOnPlane(
    MouseEvent event, Point3 origin, Plane plane, Matrix44 localToPixel) 
  {
    super(localToPixel);
    //_origin = new Point3(origin);
    _normal = plane.getNormal();
    _plane = new Plane(plane);
    _delta = origin.minus(getPointOnPlane(event));
  }

  /**
   * Gets the constrained point in local coordinates for the specified event.
   * The constrained point lies in the constraint plane that contains the
   * origin point.
   * @param event the mouse event.
   * @return the constrained point, in local coordinates.
   */
  public Point3 getPoint(MouseEvent event) {
    return getPointOnPlane(event).plusEquals(_delta);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  //private Point3 _origin; // the specified point in the constraint plane
  private Vector3 _normal; // unit-vector normal to plane
  private Plane _plane; // the specified plane
  private Vector3 _delta; // specified origin minus initial intersection point

  /**
   * Gets point on plane corresponding to the specified mouse event.
   * The point is the intersection of the mouse segment with the plane.
   * <p>
   * Here, the plane is that specified when this mouse-on-plane was
   * constructed. That specified plane is parallel to the constraint 
   * plane that contains the origin.
   */
  private Point3 getPointOnPlane(MouseEvent event) {

    // Ray from near point A through far point B of mouse segment.
    Segment segment = getMouseSegment(event);
    Point3 a = segment.getA();
    Point3 b = segment.getB();
    Vector3 d = b.minus(a);

    // Point where the mouse segment intersects the specified plane.
    double den = d.dot(_normal);
    // TODO: handle case where denominator is tiny (ray parallel to plane)?
    double num = -_plane.distanceTo(a);
    double t = num/den;
    Point3 point;
    if (t<=0.0) {
      point = a;
    } else if (t>=1.0) {
      point = b;
    } else {
      point = a.plus(d.times(t));
    }

    return point;
  }
}
