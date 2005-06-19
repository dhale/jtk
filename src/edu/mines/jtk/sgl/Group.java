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
   * Culls this group. This implementation simply applies the cull process
   * to its children.
   * @param cc the cull context.
   */
  protected void cull(CullContext cc) {
    for (Node child : _childList)
      child.cullApply(cc);
  }

  /**
   * Draws this group. This implementation simply applies the draw process
   * to its children.
   * <p>
   * When culling is applied before drawing, the draw list accumulated
   * in the cull context will draw the child nodes, and this method will 
   * never be called.
   * @param dc the draw context.
   */
  protected void draw(DrawContext dc) {
    for (Node child : _childList)
      child.drawApply(dc);
  }

  /**
   * Picks this group. This implementation simply applies the pick process
   * to its children.
   * @param pc the pick context.
   */
  protected void pick(PickContext pc) {
    for (Node child : _childList)
      child.pickApply(pc);
  }
  
  /**
   * Computes the bounding sphere for this group, including its children.
   * @return the computed bounding sphere.
   */
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

  /**
   * Gets the OpenGL attribute bits for this group. For efficiency, this 
   * implementation simply returns 0 (no bits). This value is correct only 
   * if the group leaves all OpenGL state unchanged in its draw process. 
   * Specifically, the OpenGL state may change <em>during</em> the draw 
   * process for a group, but the state <em>before</em> and <em>after</em> 
   * that process must be the same.
   * @return the attribute bits.
   */
  protected int getAttributeBits() {
    return 0;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  ArrayList<Node> _childList = new ArrayList<Node>(4);
}
