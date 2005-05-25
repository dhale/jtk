/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import edu.mines.jtk.opengl.*;
import static edu.mines.jtk.opengl.Gl.*;

/**
 * An OpenGL canvas on which a view is painted.
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
   * Sets the view painted on this canvas. Also, adds this canvas
   * to the list of canvases on which the specified view is painted.
   * @param view the view.
   */
  public void setView(View view) {
    if (_view!=null)
      _view.removeCanvas(this);
    _view = view;
    if (_view!=null)
      _view.addCanvas(this);
    repaint();
  }

  /**
   * Gets the view painted on this canvas.
   * @return the view; null, if none.
   */
  public View getView() {
    return _view;
  }

  /**
   * Sets the view-to-cube transform for this canvas.
   * Typically, the view sets the view-to-cube transform.
   * @param viewToCube the view-to-cube transform.
   */
  public void setViewToCube(Matrix44 viewToCube) {
    _viewToCube = viewToCube.clone();
    repaint();
  }

  /**
   * Gets the view-to-cube transform for this canvas.
   * @return the view-to-cube transform.
   */
  public Matrix44 getViewToCube() {
    return _viewToCube;
  }

  /**
   * Sets the cube-to-pixel transform for this canvas.
   * Typically, the view sets the cube-to-pixel transform.
   * @param cubeToPixel the cube-to-pixel transform.
   */
  public void setCubeToPixel(Matrix44 cubeToPixel) {
    _cubeToPixel = cubeToPixel.clone();
    repaint();
  }

  /**
   * Gets the cube-to-pixel transform for this canvas.
   * @return the cube-to-pixel transform.
   */
  public Matrix44 getCubeToPixel() {
    return _cubeToPixel;
  }

  public void glPaint() {
    if (_view==null)
      return;

    // Viewport.
    Point4 p = new Point4(-1.0,-1.0, 0.0, 1.0);
    Point4 q = new Point4( 1.0, 1.0, 0.0, 1.0);
    p = _cubeToPixel.times(p);
    q = _cubeToPixel.times(q);
    int x = Math.max(0,(int)(p.x+0.5));
    int y = Math.max(0,(int)(p.y+0.5));
    int w = Math.min( getWidth(),(int)(q.x-p.x+0.5));
    int h = Math.min(getHeight(),(int)(q.y-p.y+0.5));
    glViewport(x,y,w,h);

    // Projection.
    glMatrixMode(GL_PROJECTION);
    glLoadMatrixd(_viewToCube.m);

    // View transform.
    glMatrixMode(GL_MODELVIEW);
    glLoadMatrixd(_view.getWorldToView().m);

    // Draw the world.
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    World world = _view.getWorld();
    if (world!=null) {
      DrawContext dc = new DrawContext(world);
      world.drawNode(dc);
    }
    glFlush();
  }

  public void glResize(
    int width, int height, int widthBefore, int heightBefore)
  {
    if (_view==null)
      return;
    _view.updateTransforms(this);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private View _view;
  private Matrix44 _viewToCube;
  private Matrix44 _cubeToPixel;
}
