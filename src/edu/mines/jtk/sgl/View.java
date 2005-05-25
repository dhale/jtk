/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

import edu.mines.jtk.opengl.*;
import static edu.mines.jtk.opengl.Gl.*;

/**
 * An abstract view of a world.
 * <p>
 * A view paints its world on one or more view canvases. The simplest 
 * and typical scenario is that a view paints on only one canvas.
 * <p>
 * A view manages a world-to-view transform, which is that part of the 
 * OpenGL modelview transform that depends on the view. A view also 
 * manages the view-to-cube and cube-to-pixel transforms of each of its 
 * view canvases, as necessary.
 * <p>
 * All three transforms - world-to-view, view-to-cube, and cube-to-pixel -
 * are view-dependent. Because the latter two transforms may vary among
 * the multiple view canvases on which a view paints, those transforms 
 * are stored with each view canvas. Nevertheless, the view is responsible 
 * for updating the view-to-cube and cube-to-pixel transforms for each view 
 * canvas on which it paints.
 * <p>
 * When changes to its world might invalidate one or more of these three 
 * transforms, the abstract method {@link #updateTransforms(World)} for a 
 * view is called. Likewise, when changes to one of its view canvases might 
 * invalidate one or more of these three transforms, the abstract method 
 * {@link #updateTransforms(ViewCanvas)} is called. Classes that extend
 * this abstract base class must implement these two methods.
 * <p>
 * A view paints its world on a canvas by simply calling its methods
 * (1) {@link #paintCanvas(ViewCanvas)}, 
 * (2) {@link #paintView(ViewCanvas)}, and
 * (3) {@link #paintWorld(ViewCanvas)}, 
 * in that order, in its method {@link #paintAll(ViewCanvas)}.
 * This base class provides useful implementations of these methods,
 * which may be overridden, to customize the view.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.24
 */
public abstract class View {

  /**
   * Constructs a view of no world.
   */
  public View() {
  }

  /**
   * Constructs a view of the specified world.
   * @param world the world.
   */
  public View(World world) {
    setWorld(world);
  }

  /**
   * Sets the world painted in this view.
   * Then repaints this view.
   * @param world the world.
   */
  public void setWorld(World world) {
    if (_world!=null)
      _world.removeView(this);
    _world = world;
    if (_world!=null)
      _world.addView(this);
    repaint();
  }

  /**
   * Gets the world painted in this view.
   */
  public World getWorld() {
    return _world;
  }

  /**
   * Sets the world-to-view transform managed by this view.
   * Then repaints this view.
   * @param worldToView the world-to-view transform; copied, not referenced.
   */
  public void setWorldToView(Matrix44 worldToView) {
    _worldToView = worldToView.clone();
    repaint();
  }

  /**
   * Gets the world-to-view transform managed by this view.
   * @return the world-to-view transform; by copy, not by reference.
   */
  public Matrix44 getWorldToView() {
    return _worldToView.clone();
  }

  /**
   * Returns the number of canvases on which this view paints.
   * @return the number of canvases.
   */
  public int countCanvases() {
    return _canvasList.size();
  }

  /**
   * Gets an iterator for the canvases on which this view paints.
   * @return the iterator.
   */
  public Iterator<ViewCanvas> getCanvases() {
    return _canvasList.iterator();
  }

  /**
   * Repaints all canvases on which this view paints.
   */
  public void repaint() {
    for (ViewCanvas canvas : _canvasList)
      canvas.repaint();
  }


  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Updates transforms for the world painted by this view.
   * Classes that extend this base class must implement this method.
   * <p>
   * This method is called when the world-to-view transform of this 
   * view might require updating; e.g., when the bounds of the specified 
   * world have changed. Furthermore, this method might also update the 
   * view-to-cube and cube-to-pixel transforms of its view canvases.
   * @param world the world.
   */
  protected abstract void updateTransforms(World world);

  /**
   * Updates the transforms for a canvas on which this view paints.
   * Classes that extend this base class must implement this method.
   * <p>
   * This method is called when the view-to-cube and cube-to-pixel
   * transforms of the specified view canvas might require updating; 
   * e.g., when the canvas has been resized.
   * @param canvas the view canvas.
   */
  protected abstract void updateTransforms(ViewCanvas canvas);

  /**
   * Paints the canvas-specific part of this view on the specified canvas.
   * The canvas-specific parts of a view include the OpenGL viewport and 
   * projection matrix, which this method sets using the cube-to-pixel and
   * view-to-cube transforms of the specified canvas. This method also 
   * clears the color and depth buffers.
   * @param canvas the canvas on which this view is being painted.
   */
  protected void paintCanvas(ViewCanvas canvas) {
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

    // Viewport.
    Matrix44 cubeToPixel = canvas.getCubeToPixel();
    Point4 p = new Point4(-1.0,-1.0, 0.0, 1.0);
    Point4 q = new Point4( 1.0, 1.0, 0.0, 1.0);
    p = cubeToPixel.times(p);
    q = cubeToPixel.times(q);
    int x = Math.max(0,(int)(p.x+0.5));
    int y = Math.max(0,(int)(p.y+0.5));
    int w = Math.min(canvas.getWidth(),(int)(q.x-p.x+0.5));
    int h = Math.min(canvas.getHeight(),(int)(q.y-p.y+0.5));
    glViewport(x,y,w,h);

    // Projection.
    Matrix44 viewToCube = canvas.getViewToCube();
    glMatrixMode(GL_PROJECTION);
    glLoadMatrixd(viewToCube.m);
    glMatrixMode(GL_MODELVIEW);
  }

  /**
   * Paints the view-specific part of this view on the specified canvas.
   * The view-specific part of a view includes the world-to-view transform,
   * which this method loads into the OpenGL modelview matrix stack.
   * @param canvas the canvas on which this view is being painted.
   */
  protected void paintView(ViewCanvas canvas) {
    glMatrixMode(GL_MODELVIEW);
    glLoadMatrixd(_worldToView.m);
  }

  /**
   * Paints the world-specific part of this view on the specified canvas.
   * The world-specific part of a view is its world, which this method
   * simply draws, if it has one. After drawing its world, this method
   * flushes the OpenGL context.
   * @param canvas the canvas on which this view is being painted.
   */
  protected void paintWorld(ViewCanvas canvas) {
    if (_world!=null) {
      DrawContext dc = new DrawContext(_world);
      _world.drawNode(dc);
    }
    glFlush();
  }

  /**
   * Paints everything for this view on the specified view canvas.
   * This implementation simply calls the methods
   * (1) {@link #paintCanvas(ViewCanvas)},
   * (2) {@link #paintView(ViewCanvas)}, and
   * (3) {@link #paintWorld(ViewCanvas)},
   * in that order.
   */
  protected void paintAll(ViewCanvas canvas) {
    paintCanvas(canvas);
    paintView(canvas);
    paintWorld(canvas);
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  /**
   * Called by ViewCanvas.setView(View).
   */
  boolean addCanvas(ViewCanvas canvas) {
    if (!_canvasList.contains(canvas)) {
      _canvasList.add(canvas);
      updateTransforms(canvas);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Called by ViewCanvas.setView(View).
   */
  boolean removeCanvas(ViewCanvas canvas) {
    return _canvasList.remove(canvas);
  }


  ///////////////////////////////////////////////////////////////////////////
  // private

  private World _world;
  private Matrix44 _worldToView;
  private ArrayList<ViewCanvas> _canvasList = new ArrayList<ViewCanvas>(1);
}
