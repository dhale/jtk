/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import edu.mines.jtk.opengl.*;

/**
 * An OpenGL canvas on which a view draws its world.
 * <p>
 * The relationship between views and view canvases is one-to-many. A view 
 * canvas is managed by only one view, but that view may draw its world on
 * on one or more view canvases.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.24
 */
public class ViewCanvas extends GlCanvas {

  /**
   * Constructs a canvas with no view.
   */
  public ViewCanvas() {
  }

  /**
   * Constructs a canvas for the specified view.
   * @param view the view.
   */
  public ViewCanvas(View view) {
    setView(view);
  }

  /**
   * Sets the view that draws on this canvas.
   * @param view the view; null, if none.
   */
  public void setView(View view) {
    if (_view!=null)
      _view.removeCanvas(this);
    _view = view;
    if (_view!=null) {
      _view.addCanvas(this);
      _view.updateTransforms();
      _view.repaint();
    }
  }

  /**
   * Gets the view that draws on this canvas.
   * @return the view; null, if none.
   */
  public View getView() {
    return _view;
  }

  /**
   * Sets the view-to-cube transform for this canvas.
   * Typically, the view sets the view-to-cube transform.
   * @param viewToCube the view-to-cube transform; copied, not referenced.
   */
  public void setViewToCube(Matrix44 viewToCube) {
    _viewToCube = viewToCube.clone();
    repaint();
  }

  /**
   * Gets the view-to-cube transform for this canvas.
   * @return the view-to-cube transform; by copy, not by reference.
   */
  public Matrix44 getViewToCube() {
    return _viewToCube.clone();
  }

  /**
   * Sets the cube-to-pixel transform for this canvas.
   * Typically, the view sets the cube-to-pixel transform.
   * @param cubeToPixel the cube-to-pixel transform; copied, not referenced.
   */
  public void setCubeToPixel(Matrix44 cubeToPixel) {
    _cubeToPixel = cubeToPixel.clone();
    repaint();
  }

  /**
   * Gets the cube-to-pixel transform for this canvas.
   * @return the cube-to-pixel transform; by copy, not by reference.
   */
  public Matrix44 getCubeToPixel() {
    return _cubeToPixel.clone();
  }

  public void glPaint() {
    if (_view!=null)
      _view.draw(this);
  }

  public void glResize(
    int width, int height, int widthBefore, int heightBefore)
  {
    if (_view!=null)
      _view.updateTransforms(this);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private View _view;
  private Matrix44 _viewToCube = Matrix44.identity();
  private Matrix44 _cubeToPixel = Matrix44.identity();
}
