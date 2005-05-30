/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A traversal context with coordinate transforms.
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
    return _localToWorld.clone();
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
    return _worldToView.clone();
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
    return _viewToCube.clone();
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
    return _cubeToPixel.clone();
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
    _localToWorldStack.push(_localToWorld.clone());
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
