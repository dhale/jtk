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
   * Gets the local-to-world transform.
   * @return the local-to-world transform.
   */
  public Matrix44 getLocalToWorld() {
    return _localToWorld.clone();
  }

  /**
   * Gets the local-to-view transform.
   * @return the local-to-view transform.
   */
  public Matrix44 getLocalToView() {
    return getWorldToView().timesEquals(_localToWorld);
  }

  /**
   * Gets the local-to-cube transform.
   * @return the local-to-cube transform.
   */
  public Matrix44 getLocalToCube() {
    return getWorldToCube().timesEquals(_localToWorld);
  }

  /**
   * Gets the local-to-pixel transform.
   * @return the local-to-pixel transform.
   */
  public Matrix44 getLocalToPixel() {
    return getWorldToPixel().timesEquals(_localToWorld);
  }

  /**
   * Gets the world-to-view transform.
   * @return the world-to-view transform.
   */
  public Matrix44 getWorldToView() {
    return _worldToView.clone();
  }

  /**
   * Gets the world-to-cube transform.
   * @return the world-to-cube transform.
   */
  public Matrix44 getWorldToCube() {
    return getViewToCube().timesEquals(_worldToView);
  }

  /**
   * Gets the world-to-pixel transform.
   * @return the world-to-pixel transform.
   */
  public Matrix44 getWorldToPixel() {
    return getViewToPixel().timesEquals(_worldToView);
  }

  /**
   * Gets the view-to-cube transform.
   * @return the view-to-cube transform.
   */
  public Matrix44 getViewToCube() {
    return _viewToCube.clone();
  }

  /**
   * Gets the view-to-pixel transform.
   * @return the view-to-pixel transform.
   */
  public Matrix44 getViewToPixel() {
    return getCubeToPixel().timesEquals(_viewToCube);
  }

  /**
   * Gets the cube-to-pixel transform.
   * @return the cube-to-pixel transform.
   */
  public Matrix44 getCubeToPixel() {
    return _cubeToPixel.clone();
  }

  /**
   * Saves the local-to-world transform before appending a transform.
   * The specified transform matrix is post-multiplied with the current
   * local-to-world transform, such that the specified transform is applied
   * first when transforming local coordinates to world coordinates.
   * @param the transform to append.
   */
  public void pushTransform(Matrix44 transform) {
    _transformStack.push(_localToWorld.clone());
    _localToWorld.timesEquals(transform);
  }

  /**
   * Restores the most recently saved (pushed) local-to-world transform.
   * Discards the current local-to-world transform.
   */
  public void popTransform() {
    _localToWorld = _transformStack.pop();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Matrix44 _localToWorld = new Matrix44();
  private Matrix44 _worldToView = new Matrix44();
  private Matrix44 _viewToCube = new Matrix44();
  private Matrix44 _cubeToPixel = new Matrix44();
  private ArrayStack<Matrix44> _transformStack = new ArrayStack<Matrix44>();
}
