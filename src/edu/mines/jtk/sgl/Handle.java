/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A handle for manipulating objects. A handle is selectable and dragable.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.30
 */
public abstract class Handle extends Group implements Selectable, Dragable {
  
  /**
   * Gets the location of the center of this handle.
   * @return the center point.
   */
  public Point3 getLocation() {
    return new Point3(_tx,_ty,_tz);
  }

  /**
   * Sets the location of the center of this handle.
   * @param p the center point.
   */
  public void setLocation(Point3 p) {
    _tx = p.x;
    _ty = p.y;
    _tz = p.z;
  }

  /**
   * Sets the location of the center of this handle.
   * @param x the center x coordinate.
   * @param y the center y coordinate.
   * @param z the center z coordinate.
   */
  public void setLocation(double x, double y, double z) {
    _tx = x;
    _ty = y;
    _tz = z;
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Gets the size of this handle, in pixels.
   * @return the size.
   */
  protected abstract double getSize();

  /**
   * Computes the transform matrix for this handle in the specified context.
   * The transform matrix yields a handle with the correct location and size.
   * @param tc the transform context.
   * @return the transform matrix for this handle.
   */
  protected Matrix44 computeTransform(TransformContext tc) {
    Matrix44 transform = Matrix44.translate(_tx,_ty,_tz);
    Matrix44 localToPixel = tc.getLocalToPixel().times(transform);
    Matrix44 pixelToLocal = localToPixel.inverse();
    Point3 p = new Point3(0.0,0.0,0.0);
    Point3 q = localToPixel.times(p);
    q.x += getSize();
    q = pixelToLocal.times(q);
    double d = p.distanceTo(q);
    transform.timesEquals(Matrix44.scale(d,d,d));
    return transform;
  }

  protected double _tx = 0.0;
  protected double _ty = 0.0;
  protected double _tz = 0.0;
}
