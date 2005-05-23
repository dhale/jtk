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
 * more than time during a graph traversal, each time with a different 
 * list of parent nodes.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class TraversalContext {

  /**
   * Constructs a traversal context for the specified root node.
   * @param root the root node.
   */
  public TraversalContext(Node root) {
    _node = root;
  }

  /**
   * Returns the number of current and parent nodes in this traversal.
   * @return the number of nodes.
   */
  public int countNodes() {
    return 1+_nodeStack.size();
  }

  /**
   * Gets the current node in the traversal.
   * @return the current node.
   */
  public Node getNode() {
    return _node;
  }

  /**
   * Gets the node in the traversal with specified index.
   * The index of the current node is 0. The index of its parent node is 1.
   * The index of the parent of that parent node is 2. And so on. 
   * @param index the node index.
   * @return the node.
   */
  public Node getNode(int index) {
    return (index==0)?_node:_nodeStack.get(_nodeStack.size()-index);
  }

  /**
   * Gets an iterator for the current and parent nodes in this traversal.
   * @return the node iterator.
   */
  public Iterator<Node> getNodes() {
    return new Iterator<Node>() {
      public boolean hasNext() {
        return _index<countNodes();
      }
      public Node next() {
        if (_index>=countNodes())
          throw new NoSuchElementException();
        return getNode(_index++);
      }
      public void remove() {
        throw new UnsupportedOperationException();
      }
      private int _index;
    };
  }

  /**
   * Saves the current node, and then makes the specified node current.
   * @param node the node to be made current.
   */
  public void pushNode(Node node) {
    _nodeStack.push(node);
  }

  /**
   * Restores the most recently saved (pushed) node.
   * Discards the current node.
   */
  public void popNode() {
    _node = _nodeStack.pop();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Node _node;
  private ArrayStack<Node> _nodeStack = new ArrayStack<Node>();
}
