/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A mouse constrained by a line. Constrained points (in local coordinates)
 * lie on a line through a specified origin point and parallel to a specified 
 * vector.
 * <p>
 * Given a mouse event with pixel (x,y) coordinates, a mouse-on-line computes 
 * a constrained point. When the mouse pixel coordinates equal those used to 
 * construct the mouse-on-line, the constrained point equals the specified 
 * origin point.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.05
 */
public class MouseOnLine extends MouseConstrained {

  /**
   * Constructs a mouse constrained by a line. The line of constraint
   * contains the specified origin point and is parallel to the specified 
   * vector.
   * @param event the mouse event.
   * @param origin the origin point, in local coordinates.
   * @param vector the vector parallel to the line of constraint.
   * @param localToPixel the transform from local to pixel coordinates.
   */
  public MouseOnLine(
    MouseEvent event, Point3 origin, Vector3 vector, Matrix44 localToPixel) 
  {
    super(localToPixel);
    //_xmouse = event.getX();
    _ymouse = event.getY();
    _origin = new Point3(origin);
    _vector = vector.normalize();

    // The mode of operation is either nearest or push-pull, depending on 
    // how parallel the initial mouse segment is to the line of constraint.
    Segment mouseSegment = getMouseSegment(event);
    Point3 mouseNear = mouseSegment.getA();
    Point3 mouseFar = mouseSegment.getB();
    Vector3 mouseVector = mouseFar.minus(mouseNear).normalize();
    double d = mouseVector.dot(_vector);
    if (d<0.0) {
      d = -d;
      _vector.negateEquals();
    }
    _mode = (d<0.867)?Mode.NEAREST:Mode.PUSH_PULL;
    _length = mouseSegment.length();
    _delta = origin.minus(getPointOnLine(event));
  }

  /**
   * Gets the point in local coordinates for the specified event.
   * @param event the mouse event.
   * @return the point, in local coordinates.
   */
  public Point3 getPoint(MouseEvent event) {
    return getPointOnLine(event).plusEquals(_delta);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private enum Mode {
    NEAREST,
    PUSH_PULL
  }

  //private double _xmouse; // initial mouse pixel x coordinate
  private double _ymouse; // initial mouse pixel y coordinate
  private Point3 _origin; // the specified origin point
  private Vector3 _vector; // vector parallel to line
  private Vector3 _delta; // specified point minus initial intersection point
  private double _length; // length of initial mouse segment
  private Mode _mode; // either nearest or push-pull

  private Point3 getPointOnLine(MouseEvent event) {
    Point3 point = null;

    // If mode is nearest, ...
    if (_mode==Mode.NEAREST) {

      // Ray 1 from near point A through far point B of mouse segment.
      Segment segment = getMouseSegment(event);
      Point3 p1 = segment.getA();
      Vector3 v1 = segment.getB().minus(p1);

      // Ray 2 from specified origin is parallel to specified vector.
      Point3 p2 = _origin; 
      Vector3 v2 = _vector;

      // Point on ray 2 that is nearest to ray 1.
      Vector3 a = p2.minus(p1);
      Vector3 b = v1;
      Vector3 c = v1.cross(v2);
      double cc = c.lengthSquared();
      double t = a.cross(b).dot(c)/cc;
      point = p2.plus(v2.times(t));
    } 
    
    // else, if mode is push-pull, ...
    else if (_mode==Mode.PUSH_PULL) {

      // Use mouse y coordinate only to determine distance along line.
      Component component = (Component)event.getSource();
      double height = component.getHeight();
      double ymouse = event.getY();
      double scale = 0.05*(_ymouse-ymouse)/height;
      point = _origin.plus(_vector.times(scale*_length));
    }

    return point;
  }
}
