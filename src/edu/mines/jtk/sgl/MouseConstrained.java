/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.event.MouseEvent;

/**
 * A constrained mouse. Classes that extend this abstract base class are
 * used to convert mouse events, with mouse pixel coordinates, to points 
 * in a local coordinate system. The constraint is necessary, because 
 * mouse pixel coordinates are two-dimensional, whereas local coordinates 
 * are three-dimensional.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.05
 */
public abstract class MouseConstrained {

  /**
   * Constructs a constrained mouse.
   * @param localToPixel the transform from local to pixel coordinates.
   */
  public MouseConstrained(Matrix44 localToPixel) {
    //_localToPixel = new Matrix44(localToPixel);
    _pixelToLocal = localToPixel.inverse();
  }

  /**
   * Gets the point in local coordinates corresponding to the specified event.
   * @param event the mouse event.
   * @return the point, in local coordinates.
   */
  public abstract Point3 getPoint(MouseEvent event);

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Gets a line segment in local coordinates for the specified event.
   * The endpoints A and B of the line segment lie on the near and far
   * clipping planes, respectively.
   * @param event the mouse event.
   * @return the line segment.
   */
  protected Segment getMouseSegment(MouseEvent event) {
    int x = event.getX();
    int y = event.getY();
    Point3 near = new Point3(x,y,0);
    Point3 far = new Point3(x,y,1);
    near = _pixelToLocal.times(near);
    far = _pixelToLocal.times(far);
    return new Segment(near,far);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  //private Matrix44 _localToPixel;
  private Matrix44 _pixelToLocal;
}
