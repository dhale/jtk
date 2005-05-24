/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

/**
 * A node in the scene graph that may contain node children.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class Group extends Node {

  /**
   * Adds the specified child node to this group's list of children. If the 
   * node is already a child of this group, then this method does nothing.
   * @param child the child node.
   */
  public void addChild(Node child) {
    if (child.addParent(this)) {
      _childList.add(child);
      dirtyBoundingSphere();
    }
  }

  /**
   * Removes the specified child node from this group's list of children. If 
   * the node is not a child of this group, then this method does nothing.
   * @param child the child node.
   */
  public void removeChild(Node child) {
    if (child.removeParent(this)) {
      _childList.remove(child);
      dirtyBoundingSphere();
    }
  }

  /**
   * Returns the number of children in this group.
   * @return the number of children.
   */
  public int countChildren() {
    return _childList.size();
  }

  /**
   * Gets an iterator for the children in this group.
   * @return the iterator.
   */
  public Iterator<Node> getChildren() {
    return _childList.iterator();
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Draws this node.
   * This implementation first calls {@link drawBegin(DrawContext)}.
   * Then, for each child node, it calls {@link drawNode(DrawContext)}.
   * Finally, it calls {@link drawEnd(DrawContext)}.
   */
  protected void drawNode(DrawContext dc) {
    drawBegin(dc);
    for (Node node : _childList)
      node.drawNode(dc);
    drawEnd(dc);
  }
  
  protected BoundingSphere computeBoundingSphere() {
    BoundingBox bb = new BoundingBox();
    for (Node child : _childList)
      bb.expandBy(child.getBoundingSphere());
    if (bb.isEmpty())
      return new BoundingSphere();
    BoundingSphere bs = new BoundingSphere(bb.getCenter(),0.0);
    for (Node child : _childList)
      bs.expandRadiusBy(child.getBoundingSphere());
    return bs;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  ArrayList<Node> _childList = new ArrayList<Node>(4);
}
