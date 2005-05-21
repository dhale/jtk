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
    if (child.addParent(this))
      _childList.add(child);
  }

  /**
   * Removes the specified child node from this group's list of children. If 
   * the node is not a child of this group, then this method does nothing.
   * @param child the child node.
   */
  public void removeChild(Node child) {
    if (child.removeParent(this))
      _childList.remove(child);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  ArrayList<Node> _childList = new ArrayList<Node>(4);
}
