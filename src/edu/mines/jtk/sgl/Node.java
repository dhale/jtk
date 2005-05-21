/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

/**
 * A node in the scene graph.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class Node {

  public int countParents() {
    return _parentList.size();
  }

  public Group getParent(int i) {
    return _parentList.get(i);
  }

  public void cullBegin(CullContext cc) {
  }
  public void cull(CullContext cc) {
  }
  public void cullEnd(CullContext cc) {
  }

  public void drawBegin(DrawContext dc) {
  }
  public void draw(DrawContext dc) {
  }
  public void drawEnd(DrawContext dc) {
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

  void cullNode(CullContext cc) {
    cullBegin(cc);
    cull(cc);
    cullEnd(cc);
  }

  void drawNode(DrawContext dc) {
    drawBegin(dc);
    draw(dc);
    drawEnd(dc);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  ArrayList<Group> _parentList = new ArrayList<Group>(2);
}
