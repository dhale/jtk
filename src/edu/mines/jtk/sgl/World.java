
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
   * Marks dirty the drawing of this world. Calling this method causes
   * a repaint of all view canvases in which this world may be drawn.
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
      view.updateTransforms(this);
  }

  /**
   * Requests a repaint of all view canvases of all views of this world.
   */
  public void repaint() {
    for (View view : _viewList)
      view.repaint();
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  /**
   * Called by View.setWorld(World).
   */
  boolean addView(View view) {
    if (!_viewList.contains(view)) {
      _viewList.add(view);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Called by View.setWorld(World).
   */
  boolean removeView(View view) {
    return _viewList.remove(view);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ArrayList<View> _viewList = new ArrayList<View>();
}
