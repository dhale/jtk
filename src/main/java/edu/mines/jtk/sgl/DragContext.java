/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.event.MouseEvent;

/**
 * A context for dragging. {@link Dragable} nodes use a drag context to
 * implement dragging with a mouse.
 * <p>
 * A drag context is constructed from a pick result, which has a picked 
 * point in local coordinates, as well as transforms to world and pixel 
 * coordinates. Those local-to-world and and local-to-pixel transforms 
 * are assumed to be constant while the the mouse is being dragged. This
 * assumption implies that the view must not change while dragging.
 * <p>
 * A drag context has a current mouse event and a current point, with 
 * local, world, and pixel coordinates. A mouse motion listener (not 
 * part of this context) is responsible for updating this context as 
 * the mouse is dragged. Updates modify the current mouse event and the 
 * current point maintained by this context.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.07
 */
public class DragContext {

  /**
   * Constructs a drag context for the specified pick result.
   * @param pr the pick result.
   */
  public DragContext(PickResult pr) {
    _event = pr.getMouseEvent();
    _canvas = pr.getViewCanvas();
    _view = pr.getView();
    _world = pr.getWorld();
    _pointLocal = pr.getPointLocal();
    _pointWorld = pr.getPointWorld();
    _pointPixel = pr.getPointPixel();
    _localToWorld = pr.getLocalToWorld();
    _localToPixel = pr.getLocalToPixel();
    _worldToPixel = pr.getWorldToPixel();
    _pixelToLocal = _localToPixel.inverse();
    _pixelToWorld = _worldToPixel.inverse();
  }

  /**
   * Updates this drag context for the specified mouse event. Updates the 
   * current mouse event and the current point's local, world, and pixel 
   * coordinates. Mouse motion listeners call this method as the mouse is 
   * dragged.
   * @param event the mouse event.
   */
  public void update(MouseEvent event) {
    _event = event;
    _pointPixel.x = event.getX();
    _pointPixel.y = event.getY();
    _pointLocal = _pixelToLocal.times(_pointPixel);
    _pointWorld = _pixelToWorld.times(_pointPixel);
  }

  /**
   * Gets the canvas for which this context was constructed.
   * @return the view canvas.
   */
  public ViewCanvas getViewCanvas() {
    return _canvas;
  }

  /**
   * Gets the view for which this context was constructed.
   * @return the view.
   */
  public View getView() {
    return _view;
  }

  /**
   * Gets the world for which this context was constructed.
   * @return the world.
   */
  public World getWorld() {
    return _world;
  }

  /**
   * Gets the current mouse event.
   * @return the mouse event.
   */
  public MouseEvent getMouseEvent() {
    return _event;
  }

  /**
   * Gets the current point in local coordinates.
   * @return the current point in local coordinates.
   */
  public Point3 getPointLocal() {
    return new Point3(_pointLocal);
  }

  /**
   * Gets the current point in world coordinates.
   * @return the current point in world coordinates.
   */
  public Point3 getPointWorld() {
    return new Point3(_pointWorld);
  }

  /**
   * Gets the current point in pixel coordinates.
   * @return the current point in pixel coordinates.
   */
  public Point3 getPointPixel() {
    return new Point3(_pointPixel);
  }

  /**
   * Gets the pixel z (depth) coordinate of the current point. This depth
   * depth coordinate increases from 0.0 at the near clipping plane to 1.0 
   * at the far clipping plane.
   * @return the pixel z coordinate.
   */
  public double getPixelZ() {
    return _pointPixel.z;
  }

  /**
   * Gets the local-to-world coordinate transform matrix.
   * @return the local-to-world coordinate transform matrix.
   */
  public Matrix44 getLocalToWorld() {
    return new Matrix44(_localToWorld);
  }

  /**
   * Gets the local-to-pixel coordinate transform matrix.
   * @return the local-to-pixel coordinate transform matrix.
   */
  public Matrix44 getLocalToPixel() {
    return new Matrix44(_localToPixel);
  }

  /**
   * Gets the world-to-pixel coordinate transform matrix.
   * @return the world-to-pixel coordinate transform matrix.
   */
  public Matrix44 getWorldToPixel() {
    return new Matrix44(_worldToPixel);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private MouseEvent _event;
  private ViewCanvas _canvas;
  private View _view;
  private World _world;
  private Point3 _pointLocal;
  private Point3 _pointWorld;
  private Point3 _pointPixel;
  private Matrix44 _localToWorld;
  private Matrix44 _localToPixel;
  private Matrix44 _worldToPixel;
  private Matrix44 _pixelToLocal;
  private Matrix44 _pixelToWorld;
}
