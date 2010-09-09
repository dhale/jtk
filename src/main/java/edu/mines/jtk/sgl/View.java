/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An abstract view of a world.
 * <p>
 * A view draws its world on one or more view canvases. The simplest 
 * and typical scenario is that a view draws on only one canvas.
 * <p>
 * A view manages a world-to-view transform, which is that part of the 
 * OpenGL modelview transform that depends on the view. A view also 
 * manages the view-to-cube and cube-to-pixel transforms of each of its 
 * view canvases, as necessary.
 * <p>
 * All three transforms - world-to-view, view-to-cube, and cube-to-pixel -
 * are view-dependent. Because the latter two transforms may vary among
 * the multiple view canvases on which a view draws, those transforms 
 * are stored with each view canvas. Nevertheless, the view updates the
 * view-to-cube and cube-to-pixel transforms for each view canvas on which 
 * it draws.
 * <p>
 * Some aspects of the world-to-view transform are common to all classes of
 * views. These include an initial translation, scaling, and rotation of the 
 * world coordinate system. Typically, these initial transforms are followed 
 * by other view-specific transforms, but all classes of views provide at 
 * least these aspects.
 * <p>
 * Classes that extend this abstract base class must implement two methods: 
 * {@link #updateTransforms(ViewCanvas)} and {@link #draw(ViewCanvas)}. The 
 * method {@link #updateTransforms(ViewCanvas)} is called to update the
 * three view-dependent transforms for a specified view canvas. The method
 * {@link #draw(ViewCanvas)} is called to draw the view on a specified view
 * canvas.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.24
 */
public abstract class View {

  public AxesOrientation getAxesOrientation() {
    return _axesOrientation;
  }

  public void setAxesOrientation(AxesOrientation axesOrientation) {
    _axesOrientation = axesOrientation;
    updateTransforms();
    repaint();
  }

  public Tuple3 getAxesScale() {
    return new Tuple3(_axesScale);
  }

  public void setAxesScale(Tuple3 s) {
    setAxesScale(s.x,s.y,s.z);
  }

  public void setAxesScale(double sx, double sy, double sz) {
    _axesScale  = new Tuple3(sx,sy,sz);
    updateTransforms();
    repaint();
  }

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
   * Sets the world drawn by this view.
   * Then repaints this view.
   * @param world the world.
   */
  public void setWorld(World world) {
    if (_world!=null)
      _world.removeView(this);
    _world = world;
    if (_world!=null)
      _world.addView(this);
    updateTransforms();
    repaint();
  }

  /**
   * Gets the world drawn by in this view.
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
    _worldToView = new Matrix44(worldToView);
    repaint();
  }

  /**
   * Gets the world-to-view transform managed by this view.
   * @return the world-to-view transform; by copy, not by reference.
   */
  public Matrix44 getWorldToView() {
    return new Matrix44(_worldToView);
  }

  /**
   * Returns the number of canvases on which this view draws.
   * @return the number of canvases.
   */
  public int countCanvases() {
    return _canvasList.size();
  }

  /**
   * Gets an iterator for the canvases on which this view draws.
   * @return the iterator.
   */
  public Iterator<ViewCanvas> getCanvases() {
    return _canvasList.iterator();
  }

  /**
   * Updates transforms for this view and all canvases on which it draws.
   * This method should be called when the world drawn by this view changes, 
   * when the view parameters change, and when any canvas on which this view
   * draws changes.
   */
  public void updateTransforms() {
    for (ViewCanvas canvas : _canvasList)
      updateTransforms(canvas);
  }

  /**
   * Repaints all canvases on which this view draws. This method should be 
   * called when this view must redraw its world, for example, when that 
   * world changes, or when a canvas on which this view draws changes.
   */
  public void repaint() {
    for (ViewCanvas canvas : _canvasList)
      canvas.repaint();
  }


  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Updates the transforms for a canvas on which this view draws. 
   * The view-to-cube and cube-to-pixel transforms are canvas-specific.
   * @param canvas the view canvas.
   */
  protected abstract void updateTransforms(ViewCanvas canvas);

  /**
   * Draws this view on the specified canvas.
   * @param canvas the view canvas.
   */
  protected abstract void draw(ViewCanvas canvas);

  ///////////////////////////////////////////////////////////////////////////
  // package

  /**
   * Called by ViewCanvas.setView(View).
   */
  boolean addCanvas(ViewCanvas canvas) {
    if (!_canvasList.contains(canvas)) {
      _canvasList.add(canvas);
      updateTransforms();
      repaint();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Called by ViewCanvas.setView(View).
   */
  boolean removeCanvas(ViewCanvas canvas) {
    if (_canvasList.remove(canvas)) {
      updateTransforms();
      repaint();
      return true;
    } else {
      return false;
    }
  }


  ///////////////////////////////////////////////////////////////////////////
  // private

  private World _world;
  private Matrix44 _worldToView = Matrix44.identity();
  private ArrayList<ViewCanvas> _canvasList = new ArrayList<ViewCanvas>(1);
  private Tuple3 _axesScale = new Tuple3(1.0,1.0,1.0);
  private AxesOrientation _axesOrientation = AxesOrientation.XRIGHT_YUP_ZOUT;
}
