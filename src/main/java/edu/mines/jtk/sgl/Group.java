/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.ArrayList;
import java.util.Iterator;

import edu.mines.jtk.util.Check;

/**
 * A node in the scene graph that may contain node children.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class Group extends Node {

  /**
   * Adds the specified child node to this group's list of children. If the 
   * node is already a child of this group, then this method does nothing.
   * <p>
   * The child must not be a world (root) node, because a world has no 
   * parents. Also, if this group is in a world, the child must not already 
   * be in a different world. A node cannot be in more than one world at a 
   * time; it must be removed from one world before it can be added to another.
   * @param child the child node.
   */
  public void addChild(Node child) {
    Check.argument(!(child instanceof World),"child is not a world");
    World worldChild = child.getWorld();
    World worldGroup = getWorld();
    Check.argument(
      worldChild==null || worldGroup==null || worldChild==worldGroup,
      "child is not already in a different world");
    if (child.addParent(this)) {
      _childList.add(child);
      dirtyBoundingSphere();
      dirtyDraw();
      if (worldGroup!=null)
        worldGroup.updateSelectedSet(child);
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
      dirtyDraw();
      World worldGroup = getWorld();
      if (worldGroup!=null)
        worldGroup.updateSelectedSet(child);
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

  /**
   * Picks this group. This implementation simply applies the pick process
   * to its children.
   * @param pc the pick context.
   */
  public void pick(PickContext pc) {
    for (Node child : _childList)
      child.pickApply(pc);
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
   * Computes the bounding sphere for this group, including its children.
   * @param finite true, to force bounding sphere to be finite.
   * @return the computed bounding sphere.
   */
  protected BoundingSphere computeBoundingSphere(boolean finite) {
    if (countChildren()==1) {
      return _childList.get(0).getBoundingSphere(finite);
    } else {
      BoundingBox bb = new BoundingBox();
      for (Node child : _childList)
        bb.expandBy(child.getBoundingSphere(finite));
      if (bb.isEmpty())
        return BoundingSphere.empty();
      if (bb.isInfinite()) // should not happen if finite == true
        return BoundingSphere.infinite();
      BoundingSphere bs = new BoundingSphere(bb.getCenter(),0.0);
      for (Node child : _childList)
        bs.expandRadiusBy(child.getBoundingSphere(finite));
      return bs;
    }
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
