/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;
import static edu.mines.jtk.opengl.Gl.*;

/**
 * A node in the scene graph.
 * <p>
 * A node has zero or more parents. Because the number of parents can
 * be greater than one, the scene graph forms a directed acyclic graph
 * (DAG).
 * <p>
 * Nodes are drawn in what is called the <em>draw process</em>. The draw 
 * process is applied to a node in three steps by calling the methods
 * {@link #drawBegin(DrawContext)}, 
 * {@link #draw(DrawContext)}, and
 * {@link #drawEnd(DrawContext)}, in that order. The actual drawing occurs 
 * in the method {@link #draw(DrawContext)}. Think of the other two methods
 * as like opening and closing braces. They might save and restore OpenGL
 * state, or otherwise push and pop state required for drawing. Nodes must 
 * not leak OpenGL state set while drawing.
 * <p>
 * To facilitate drawing and picking, all nodes have a bounding sphere.
 * Ideally, this bounding sphere is the smallest sphere that contains the
 * node's geometry. Nodes with bounding spheres that do not intersect a 
 * view's frustum will be culled, and not drawn or picked within that 
 * view. Bounding spheres are specified in local coordinates.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class Node {

  /**
   * Returns the number of parents of this node.
   * @return the number of parents.
   */
  public int countParents() {
    return _parentList.size();
  }

  /**
   * Gets an iterator for the parents of this node.
   * @return the iterator.
   */
  public Iterator<Group> getParents() {
    return _parentList.iterator();
  }

  /**
   * Marks dirty the drawing of this node. Calling this method causes
   * a repaint of all view canvases in which this node may be rendered.
   */
  public void dirtyDraw() {
    for (Group parent : _parentList)
      parent.dirtyDraw();
  }

  /**
   * Gets the bounding sphere for this node. If the bounding sphere 
   * is dirty, then this method will first clean it by calling the 
   * method {@link #computeBoundingSphere()}.
   * @return the bounding sphere.
   */
  public BoundingSphere getBoundingSphere() {
    if (_boundingSphereDirty) {
      _boundingSphere = computeBoundingSphere();
      _boundingSphereDirty = false;
    }
    return _boundingSphere;
  }
  
  /**
   * Marks dirty the bounding sphere of this node (and any parent nodes).
   * Subsequent calls to the method {@link #getBoundingSphere()} will
   * cause this node to recompute its bounding sphere.
   */
  public void dirtyBoundingSphere() {
    if (!_boundingSphereDirty) {
      _boundingSphereDirty = true;
      for (Group parent : _parentList)
        parent.dirtyBoundingSphere();
    }
  }

  /**
   * Sets the OpenGL states for this node.
   * @param states the OpenGL states.
   */
  public void setStates(StateSet states) {
    _states = states;
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected
  
  /**
   * Computes the bounding sphere for this node, including its children.
   * This method is called by {@link #getBoundingSphere()} whenever this 
   * node's bounding sphere is dirty. This implementation returns an
   * empty bounding sphere.
   * @return the computed bounding sphere.
   */
  protected BoundingSphere computeBoundingSphere() {
    return new BoundingSphere();
  }

  /**
   * Gets the OpenGL attribute bits for this node. These bits determine 
   * which OpenGL state attributes are pushed and popped in the methods
   * {@link #drawBegin(DrawContext)} and {@link #drawEnd(DrawContext)}.
   * <p>
   * The attribute bits returned by this method need not include any 
   * bits from this node's {@link StateSet}, if it has one. Those bits
   * will be combined with any bits returned by this method.
   * <p>
   * Classes that extend this base class may override this method to 
   * return only those bits corresponding to OpenGL state that they 
   * modify but do not restore. However, classes that do so should take 
   * care to include all relevant bits. Nodes must not leak OpenGL state!
   * <p>
   * This implementation simply returns GL_ALL_ATTRIB_BITS, which is
   * safe, but may be inefficient.
   * @return the attribute bits.
   */
  protected int getAttributeBits() {
    return GL_ALL_ATTRIB_BITS;
  }

  /**
   * Applies the cull process to this node. Calls the three methods 
   * {@link #cullBegin(CullContext)}, 
   * {@link #cull(CullContext)}, and
   * {@link #cullEnd(CullContext)}, in that order.
   * @param cc the cull context.
   */
  protected void cullApply(CullContext cc) {
    cullBegin(cc);
    cull(cc);
    cullEnd(cc);
  }

  /**
   * Begins the cull process for this node.
   * This implementation pushes this node onto the cull context,
   * @param cc the cull context.
   */
  protected void cullBegin(CullContext cc) {
    cc.pushNode(this);
  }

  /**
   * Culls this node. This implementation first tests this node for
   * intersection with the view frustum of the cull context. If that
   * frustum intersects the bounding sphere of this node, then this
   * method appends the node stack to the draw list in the cull context.
   * @param cc the cull context.
   */
  protected void cull(CullContext cc) {
    if (cc.frustumIntersects(this))
      cc.appendNodes();
  }

  /**
   * Ends the cull process for this node.
   * @param cc the cull context.
   */
  protected void cullEnd(CullContext cc) {
    cc.popNode();
  }

  /**
   * Applies the draw process to this node. Calls the three methods 
   * {@link #drawBegin(DrawContext)}, 
   * {@link #draw(DrawContext)}, and
   * {@link #drawEnd(DrawContext)}, in that order.
   * @param dc the draw context.
   */
  protected void drawApply(DrawContext dc) {
    drawBegin(dc);
    draw(dc);
    drawEnd(dc);
  }

  /**
   * Begins the draw process for this node.
   * This implementation pushes this node onto the draw context,
   * and then pushes (saves) all OpenGL attributes.
   * @param dc the draw context.
   */
  protected void drawBegin(DrawContext dc) {
    dc.pushNode(this);
    int bits = getAttributeBits();
    if (bits!=GL_ALL_ATTRIB_BITS && _states!=null)
      bits |= _states.getAttributeBits();
    glPushAttrib(bits);
    if (_states!=null)
      _states.apply();
  }

  /**
   * Draws this node. This implementation does nothing.
   * @param dc the draw context.
   */
  protected void draw(DrawContext dc) {
  }

  /**
   * Ends the draw process for this node.
   * This implementation pops (restores) any OpenGL attributes that were 
   * pushed (saved) and then pops this node from the draw context.
   * @param dc the draw context.
   */
  protected void drawEnd(DrawContext dc) {
    glPopAttrib();
    dc.popNode();
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  /**
   * Adds the specified parent to this node's list of parents.
   * If this node is already a child of the parent, this method simply 
   * returns false. Called by Group.addChild(Node).
   * @return true, if this node was not a child of parent; false, otherwise.
   */
  boolean addParent(Group parent) {
    if (!_parentList.contains(parent)) {
      _parentList.add(parent);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Removes the specified parent from this node's list of parents.
   * If this node is not a child of the parent, this method simply
   * returns false. Called by Group.removeChild(Node).
   * @return true, if this node was a child of parent; false, otherwise.
   */
  boolean removeParent(Group parent) {
    return _parentList.remove(parent);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private boolean _boundingSphereDirty = true;
  private BoundingSphere _boundingSphere = null;
  private ArrayList<Group> _parentList = new ArrayList<Group>(2);
  private StateSet _states;
}
