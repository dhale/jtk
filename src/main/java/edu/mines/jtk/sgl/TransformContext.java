/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A traversal context with coordinate transforms.
 * <p>
 * A transform context maintains transforms for converting coordinates 
 * among five different coordinate systems:
 * <dl>
 * <dt>local</dt><dd>
 * the local coordinate system for a node in the scene graph. Vertex 
 * coordinates for a node are specified in a local system, which may 
 * or may not be the same as the world coordinate system. Two nodes
 * with the same local vertex coordinates (or even a single node) may 
 * appear in different locations, corresponding to different world
 * coordinates.
 * </dd>
 * <dt>world</dt><dd>
 * the coordinate system for the world (root) node of the scene graph.
 * A local-to-world transform puts all nodes in the same one world
 * coordinate system. World coordinates are independent of any view 
 * of the world.
 * </dd>
 * <dt>view</dt><dd>
 * a right-handed coordinate system in which the eye or camera is located
 * at the origin, looking down the negative z axis, with the positive y 
 * axis up and the positive x axis right. A world-to-view transform
 * typically pushes the world away from the camera, down the negative z 
 * axis. As the name implies, view coordinates depend on the view.
 * </dd>
 * <dt>cube</dt><dd>
 * a left-handed, normalized coordinate system with six clipping planes.
 * In this coordinate system,
 * x = -1 and x = 1 correspond to the left and right planes,
 * y = -1 and y = 1 correspond to the bottom and top planes, and
 * z = -1 and z = 1 correspond to the near and far planes.
 * A view-to-cube transform corresponds to a view's projection;
 * e.g., perspective or orthographic.
 * </dd>
 * <dt>pixel</dt><dd>
 * typically, a right-handed coordinate system in which x and y coordinates 
 * are window coordinates. For a window with width w and height h,
 * x = 0 and x = w-1 correspond to the leftmost and rightmost pixels, and
 * y = 0 and y = h-1 correspond to the topmost and bottommost pixels.
 * The pixel z (depth) coordinate is a floating point number, where
 * z = 0.0 and z = 1.0 correspond to the near and far clipping planes.
 * A cube-to-pixel transform typically depends on the dimensions of the 
 * window in which a scene is rendered.
 * </dd>
 * </dl>
 * <p>
 * When traversing the scenegraph, the world-to-view, view-to-cube, and
 * cube-to-pixel transforms do not change. However, nodes that transform 
 * coordinates will push, modify, and pop the local-to-world transform,
 * using the methods {@link #pushLocalToWorld(Matrix44)} and
  {@link #popLocalToWorld()}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class TransformContext extends TraversalContext {

  /**
   * Constructs a transform context for the specified view canvas.
   * Gets its view-to-cube and cube-to-pixel transforms from the
   * canvas. Gets its world-to-view transform from the view drawn
   * on that canvas, and sets the local-to-world transform to the
   * identity matrix.
   * @param canvas the view canvas.
   */
  public TransformContext(ViewCanvas canvas) {
    _canvas = canvas;
    _view = _canvas.getView();
    _world = _view.getWorld();
    _localToWorld = Matrix44.identity();
    _worldToView = _view.getWorldToView();
    _viewToCube = _canvas.getViewToCube();
    _cubeToPixel = _canvas.getCubeToPixel();
  }

  /**
   * Gets the canvas for which this transform context was constructed.
   * @return the view canvas.
   */
  public ViewCanvas getViewCanvas() {
    return _canvas;
  }

  /**
   * Gets the view for which this transform context was constructed.
   * @return the view.
   */
  public View getView() {
    return _view;
  }

  /**
   * Gets the world for which this transform context was constructed.
   * @return the world.
   */
  public World getWorld() {
    return _world;
  }

  /**
   * Gets the local-to-world transform.
   * @return the local-to-world transform.
   */
  public Matrix44 getLocalToWorld() {
    return new Matrix44(_localToWorld);
  }

  /**
   * Gets the world-to-local transform.
   * @return the world-to-local transform.
   */
  public Matrix44 getWorldToLocal() {
    return getLocalToWorld().inverseEquals();
  }

  /**
   * Gets the local-to-view transform.
   * @return the local-to-view transform.
   */
  public Matrix44 getLocalToView() {
    return getWorldToView().timesEquals(_localToWorld);
  }

  /**
   * Gets the view-to-local transform.
   * @return the view-to-local transform.
   */
  public Matrix44 getViewToLocal() {
    return getLocalToView().inverseEquals();
  }

  /**
   * Gets the local-to-cube transform.
   * @return the local-to-cube transform.
   */
  public Matrix44 getLocalToCube() {
    return getWorldToCube().timesEquals(_localToWorld);
  }

  /**
   * Gets the cube-to-local transform.
   * @return the cube-to-local transform.
   */
  public Matrix44 getCubeToLocal() {
    return getLocalToCube().inverseEquals();
  }

  /**
   * Gets the local-to-pixel transform.
   * @return the local-to-pixel transform.
   */
  public Matrix44 getLocalToPixel() {
    return getWorldToPixel().timesEquals(_localToWorld);
  }

  /**
   * Gets the pixel-to-local transform.
   * @return the pixel-to-local transform.
   */
  public Matrix44 getPixelToLocal() {
    return getLocalToPixel().inverseEquals();
  }

  /**
   * Gets the world-to-view transform.
   * @return the world-to-view transform.
   */
  public Matrix44 getWorldToView() {
    return new Matrix44(_worldToView);
  }

  /**
   * Gets the view-to-world transform.
   * @return the view-to-world transform.
   */
  public Matrix44 getViewToWorld() {
    return getWorldToView().inverseEquals();
  }

  /**
   * Gets the world-to-cube transform.
   * @return the world-to-cube transform.
   */
  public Matrix44 getWorldToCube() {
    return getViewToCube().timesEquals(_worldToView);
  }

  /**
   * Gets the cube-to-world transform.
   * @return the cube-to-world transform.
   */
  public Matrix44 getCubeToWorld() {
    return getWorldToCube().inverseEquals();
  }

  /**
   * Gets the world-to-pixel transform.
   * @return the world-to-pixel transform.
   */
  public Matrix44 getWorldToPixel() {
    return getViewToPixel().timesEquals(_worldToView);
  }

  /**
   * Gets the pixel-to-world transform.
   * @return the pixel-to-world transform.
   */
  public Matrix44 getPixelToWorld() {
    return getWorldToPixel().inverseEquals();
  }

  /**
   * Gets the view-to-cube transform.
   * @return the view-to-cube transform.
   */
  public Matrix44 getViewToCube() {
    return new Matrix44(_viewToCube);
  }

  /**
   * Gets the cube-to-view transform.
   * @return the cube-to-view transform.
   */
  public Matrix44 getCubeToView() {
    return getViewToCube().inverseEquals();
  }

  /**
   * Gets the view-to-pixel transform.
   * @return the view-to-pixel transform.
   */
  public Matrix44 getViewToPixel() {
    return getCubeToPixel().timesEquals(_viewToCube);
  }

  /**
   * Gets the pixel-to-view transform.
   * @return the pixel-to-view transform.
   */
  public Matrix44 getPixelToView() {
    return getViewToPixel().inverseEquals();
  }

  /**
   * Gets the cube-to-pixel transform.
   * @return the cube-to-pixel transform.
   */
  public Matrix44 getCubeToPixel() {
    return new Matrix44(_cubeToPixel);
  }

  /**
   * Gets the pixel-to-cube transform.
   * @return the pixel-to-cube transform.
   */
  public Matrix44 getPixelToCube() {
    return getCubeToPixel().inverseEquals();
  }

  /**
   * Saves the local-to-world transform before appending a transform.
   * The specified transform matrix is post-multiplied with the current
   * local-to-world transform, such that the specified transform is applied
   * first when transforming local coordinates to world coordinates.
   * @param transform the transform to append.
   */
  public void pushLocalToWorld(Matrix44 transform) {
    _localToWorldStack.push(new Matrix44(_localToWorld));
    _localToWorld.timesEquals(transform);
  }

  /**
   * Restores the most recently saved (pushed) local-to-world transform.
   * Discards the current local-to-world transform.
   */
  public void popLocalToWorld() {
    _localToWorld = _localToWorldStack.pop();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ViewCanvas _canvas;
  private View _view;
  private World _world;
  private Matrix44 _localToWorld = new Matrix44();
  private Matrix44 _worldToView = new Matrix44();
  private Matrix44 _viewToCube = new Matrix44();
  private Matrix44 _cubeToPixel = new Matrix44();
  private ArrayStack<Matrix44> _localToWorldStack = new ArrayStack<Matrix44>();
}
