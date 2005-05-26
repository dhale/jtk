/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

/**
 * A context for scene graph traversal. A traversal context maintains 
 * a current node and a list of its parent nodes. Because nodes in the 
 * scene graph may have multiple parents, a node may become current at 
 * more than time during a traversal, each time with a different list 
 * of parent nodes.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.26
 */
public class TraversalContext {

  /**
   * Returns the number of current and parent nodes in this traversal.
   * @return the number of nodes.
   */
  public int countNodes() {
    return _nodeStack.size();
  }

  /**
   * Gets the current node in the traversal.
   * @return the current node.
   */
  public Node getNode() {
    return _nodeStack.peek();
  }

  /**
   * Gets an array of nodes representing the state of this traversal. 
   * Nodes in the array are ordered from top to bottom. The last node 
   * in the array is the current node, the next to last node is the 
   * current node's parent, and so on.
   * @return the array of nodes.
   */
  public Node[] getNodes() {
    int nnode = _nodeStack.size();
    Node[] nodes = new Node[nnode];
    for (int inode=0; inode<nnode; ++inode)
      nodes[inode] = _nodeStack.get(inode);
    return nodes;
  }

  /**
   * Saves the current node, and then makes the specified node current.
   * @param node the new current node.
   */
  public void pushNode(Node node) {
    _nodeStack.push(node);
  }

  /**
   * Restores the most recently saved (pushed) node.
   * Discards the current node.
   */
  public void popNode() {
    _nodeStack.pop();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ArrayStack<Node> _nodeStack = new ArrayStack<Node>();
}
