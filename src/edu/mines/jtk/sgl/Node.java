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
   * Marks dirty the drawing of this node. Calling this method forces
   * a drawing traversal of the scene graph containing this node.
   */
  public void dirtyDraw() {
    _drawDirty = true;
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


  ///////////////////////////////////////////////////////////////////////////
  // protected
  
  /**
   * Computes the bounding sphere for this node, including its children.
   * This method is called by {@link #getBoundingSphere()} whenever this 
   * node's bounding sphere is dirty. This method is assumed to return
   * a clean (valid) bounding sphere.
   * @return the computed bounding sphere.
   */
  protected BoundingSphere computeBoundingSphere() {
    return new BoundingSphere();
  }

  protected void cullBegin(CullContext cc) {
  }

  protected void cull(CullContext cc) {
  }

  protected void cullEnd(CullContext cc) {
  }

  protected void cullNode(CullContext cc) {
    cullBegin(cc);
    cull(cc);
    cullEnd(cc);
  }

  /**
   * Begins drawing for this node.
   * This implementation simply pushes all OpenGL attributes.
   * @param dc the draw context.
   */
  protected void drawBegin(DrawContext dc) {
    glPushAttrib(GL_ALL_ATTRIB_BITS);
  }

  /**
   * Draws this node. 
   * Leaf nodes typically override this implementation, which does nothing.
   * @param dc the draw context.
   */
  protected void draw(DrawContext dc) {
  }

  /**
   * Ends drawing for this node.
   * This implementation simply pops all OpenGL attributes.
   * @param dc the draw context.
   */
  protected void drawEnd(DrawContext dc) {
    glPopAttrib();
  }

  /**
   * Draws this node.
   * This implementation calls 
   * (1) {@link drawBegin(DrawContext)},
   * (2) {@link draw(DrawContext)}, and
   * (3) {@link drawEnd(DrawContext)}.
   * @param dc the draw context.
   */
  protected void drawNode(DrawContext dc) {
    drawBegin(dc);
    draw(dc);
    drawEnd(dc);
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

  private boolean _drawDirty = true;
  private boolean _boundingSphereDirty = true;
  private BoundingSphere _boundingSphere = null;
  private ArrayList<Group> _parentList = new ArrayList<Group>(2);
}
