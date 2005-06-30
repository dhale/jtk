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
public abstract class Node {

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
   * Gets the bounding sphere for this node. If the bounding sphere is 
   * dirty, then this method will first call the method 
   * {@link #computeBoundingSphere()} 
   * and then mark the bounding sphere clean.
   * @return the bounding sphere.
   */
  public BoundingSphere getBoundingSphere() {
    if (_boundingSphere==null)
      _boundingSphere = computeBoundingSphere();
    return _boundingSphere;
  }
  
  /**
   * Marks dirty the bounding sphere of this node (and any parent nodes).
   * If dirty when the method {@link #getBoundingSphere(TransformContext)}
   * is next called, this node's bounding sphere will be recomputed.
   */
  public void dirtyBoundingSphere() {
    if (_boundingSphere!=null) {
      _boundingSphere = null;
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
   * This method is called by {@link #getBoundingSphere()} when this node's 
   * bounding sphere is dirty.
   * @return the computed bounding sphere.
   */
  protected abstract BoundingSphere computeBoundingSphere();

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

  ///////////////////////////////////////////////////////////////////////////
  // cull

  /**
   * Applies the cull process to this node. If the view frustum intersects
   * the bounding sphere of this node, then this method calls the three 
   * methods 
   * {@link #cullBegin(CullContext)}, 
   * {@link #cull(CullContext)}, and
   * {@link #cullEnd(CullContext)}, in that order.
   * @param cc the cull context.
   */
  protected void cullApply(CullContext cc) {
    if (cc.frustumIntersectsSphereOf(this)) {
      cullBegin(cc);
      cull(cc);
      cullEnd(cc);
    }
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
   * Culls this node. This implementation appends the node stack to
   * the draw list in the cull context.
   * @param cc the cull context.
   */
  protected void cull(CullContext cc) {
    cc.appendNodes();
  }

  /**
   * Ends the cull process for this node.
   * @param cc the cull context.
   */
  protected void cullEnd(CullContext cc) {
    cc.popNode();
  }

  ///////////////////////////////////////////////////////////////////////////
  // draw

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
  // pick

  /**
   * Applies the pick process to this node. If the pick segment intersects
   * the bounding sphere of this node, then this method calls the three 
   * methods 
   * {@link #pickBegin(PickContext)}, 
   * {@link #pick(PickContext)}, and
   * {@link #pickEnd(PickContext)}, in that order.
   * @param pc the pick context.
   */
  protected void pickApply(PickContext pc) {
    if (pc.segmentIntersectsSphereOf(this)) {
      pickBegin(pc);
      pick(pc);
      pickEnd(pc);
    }
  }

  /**
   * Begins the pick process for this node.
   * This implementation pushes this node onto the pick context,
   * @param pc the pick context.
   */
  protected void pickBegin(PickContext pc) {
    pc.pushNode(this);
  }

  /**
   * Picks this node. This implementation does nothing. Implementations
   * of this method in classes that extend this class may test the pick
   * segment for intersection with node geometry. If an intersection is
   * found, then a pick result should be added to the context.
   * @param pc the pick context.
   */
  protected void pick(PickContext pc) {
  }

  /**
   * Ends the pick process for this node.
   * @param pc the pick context.
   */
  protected void pickEnd(PickContext pc) {
    pc.popNode();
  }

  ///////////////////////////////////////////////////////////////////////////
  // select

  /**
   * Begins selection of this node for the specified pick result.
   * This implementation marks this node selected and returns false.
   * @param pr the pick result.
   */
  public boolean selectBegin(PickResult pr) {
    _selected = true;
    return true;
  }

  /**
   * Ends selection of this node.
   * This implementation simply marks this node as being not selected.
   */
  public void selectEnd() {
    _selected = false;
  }

  /**
   * Determines whether this node is currently selected.
   */
  public boolean isSelected() {
    return _selected;
  }
  private boolean _selected;

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

  private BoundingSphere _boundingSphere = null; // null, if dirty
  private ArrayList<Group> _parentList = new ArrayList<Group>(2);
  private StateSet _states;
}
