
/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

/**
 * A world is a root node in the scene graph.
 * <p>
 * A world maintains a list of views in which it is drawn. When a world
 * must be redrawn, it requests a repaint of its views.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.24
 */
public class World extends Group {

  /**
   * Adds the specified view of this world.
   * @param view the view.
   */
  public void addView(View view) {
    _viewList.add(view);
  }

  /**
   * Removes the specified view of this world.
   * @param view the view.
   */
  public void removeView(View view) {
    _viewList.remove(view);
  }

  /**
   * Returns the number of views of this world.
   * @return the number of views.
   */
  public int countViews() {
    return _viewList.size();
  }

  /**
   * Gets an iterator for the views of this world.
   * @return the iterator.
   */
  public Iterator<View> getViews() {
    return _viewList.iterator();
  }

  /**
   * Marks dirty the drawing of this world. 
   * This method requests a repaint of all views of this world.
   */
  public void dirtyDraw() {
    repaint();
  }

  /**
   * Marks dirty the bounding sphere of this world.
   */
  public void dirtyBoundingSphere() {
    super.dirtyBoundingSphere();
    for (View view : _viewList)
      view.updateTransforms();
  }

  /**
   * Requests a repaint of all views of this world.
   */
  public void repaint() {
    for (View view : _viewList)
      view.repaint();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ArrayList<View> _viewList;
}
