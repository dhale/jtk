/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

/**
 * An abstract view of a world.
 * <p>
 * A view is painted on one or more view canvases. A simple and common
 * scenario is that a view is painted on only one canvas.
 * <p>
 * A view manages a world-to-view transform, which is that part of the 
 * OpenGL modelview transform that depends on the view. A view also 
 * manages the view-to-cube and cube-to-pixel transforms of each of its 
 * view canvases, as necessary.
 * <p>
 * All three transforms - world-to-view, view-to-cube, and cube-to-pixel -
 * are view-dependent. Because the latter two transforms differ among the 
 * view canvases, those transforms are stored with each view canvas. But
 * the view is responsible for setting those transforms in a view-specific
 * manner.
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
    _world = world;
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
   * @param worldToView the world-to-view transform.
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
   * Returns the number of canvases on which this view is painted.
   * @return the number of canvases.
   */
  public int countCanvases() {
    return _canvasList.size();
  }

  /**
   * Gets an iterator for the canvases on which this view is painted.
   * @return the iterator.
   */
  public Iterator<ViewCanvas> getCanvases() {
    return _canvasList.iterator();
  }

  /**
   * Repaints all canvases on which this view is painted.
   */
  public void repaint() {
    for (ViewCanvas canvas : _canvasList)
      canvas.repaint();
  }


  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Updates the transforms managed by this view.
   * Classes that extend this base class must implement this method.
   * This method is called when the world painted in a view has changed 
   * so that a new world-to-view transform may be required.
   */
  protected abstract void updateTransforms();

  /**
   * Updates the transforms for a canvas on which this view is painted.
   * Classes that extend this base class must implement this method.
   * This method is called when the view-to-cube and/or cube-to-pixel
   * transforms of the specified view canvas must be recomputed; e.g.,
   * when the canvas has been resized. 
   * @param canvas the view canvas.
   */
  protected abstract void updateTransforms(ViewCanvas canvas);

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
