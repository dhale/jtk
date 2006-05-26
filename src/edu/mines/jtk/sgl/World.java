
/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

/**
 * A world is a root node in the scene graph. A world is a group that 
 * cannot be a child of any other group. To be viewed, a node must be 
 * part of a world.
 * <p>
 * A world maintains a list of views in which it is drawn. When a world
 * must be redrawn, it requests a repaint of its views.
 * <p>
 * A world maintains a set of selected nodes. The selected set of a world is
 * updated when (1) a node is added/removed to/from a world or any group that 
 * is part of a world, or (2) a node in a world is selected or deselected.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.07
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
   * Returns the number of selected nodes in this world.
   * @return the number of selected nodes.
   */
  public int countSelected() {
    return _selectedSet.size();
  }

  /**
   * Gets an iterator for selected nodes in this world.
   * @return the iterator.
   */
  public Iterator<Node> getSelected() {
    return _selectedSet.iterator();
  }

  /**
   * Deselects all selected nodes in this world.
   */
  public void clearSelected() {
    for (Node node : new ArrayList<Node>(_selectedSet)) {
      if (node.isSelected())
        node.setSelected(false);
    }
  }

  /**
   * Deselects all selected nodes in this world, except the specified node.
   * Typically, this method is called to deselect any other nodes that may 
   * be currently selected, before selecting the specified node.
   * @param nodeToIgnore the node to ignore. The selected state of this
   *  node will not be changed.
   */
  public void clearSelectedExcept(Selectable nodeToIgnore) {
    for (Node node : new ArrayList<Node>(_selectedSet)) {
      if (node!=nodeToIgnore && node.isSelected())
        node.setSelected(false);
    }
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
  }

  /**
   * Requests a repaint of all view canvases of all views of this world.
   */
  public void repaint() {
    for (View view : _viewList)
      view.repaint();
  }

  /**
   * Gets the world for this node. This implementation overrides that
   * in {@link Node#getWorld()} to simply return this world.
   * @return this world.
   */
  public World getWorld() {
    return this;
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  /**
   * Updates the selected set of this world for the specified node.
   * This method must be called when (1) the specified node is added/removed 
   * to/from this world or (2) the specified node is selected or deselected.
   * @param node the node for which to update the selected set of this world.
   */
  void updateSelectedSet(Node node) {

    // Update our selected set for the specified node. If the node is now 
    // (1) selected and (2) part of this world, then add it to our selected
    // set. (Ok if already there.) Otherwise, remove it from our selected 
    // set. (Ok if not there.)
    if (node instanceof Selectable) {
      if (node.isSelected() && this==node.getWorld()) {
        _selectedSet.add(node);
      } else {
        _selectedSet.remove(node);
      }
    }

    // If the specified node is a group, then update our selected set
    // for each of its children.
    if (node instanceof Group) {
      Group group = (Group)node;
      Iterator<Node> children = group.getChildren();
      while (children.hasNext()) {
        Node child = children.next();
        updateSelectedSet(child);
      }
    }
  }

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
  private HashSet<Node> _selectedSet = new HashSet<Node>();
}
