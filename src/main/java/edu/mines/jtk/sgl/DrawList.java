/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.ArrayList;

/**
 * A list of arrays of nodes (and their parents) to be drawn.
 * <p>
 * Each leaf node to be drawn is represented in this list by an array of
 * nodes. The last node in each array is the leaf node to be drawn, the 
 * next to last node is that leaf node's parent, and so on. The first node
 * in each array is the root node of the scene graph.
 * <p>
 * Conceptually, this list draws each array of nodes as follows. Starting 
 * with the first (top) node, and working from top to bottom, this list 
 * calls {@link Node#drawBegin(DrawContext)} for each node in the array. 
 * It then calls {@link Node#draw(DrawContext)} for the last (bottom) node. 
 * Finally, starting with that last node, and working from bottom to top, 
 * it calls {@link Node#drawEnd(DrawContext)} for each node in the array.
 * <p>
 * In practice, at least some parent nodes are likely to be the same from 
 * one array to the next. For example, the first (top) node is likely to 
 * be the same in all arrays of nodes. Therefore, during drawing, this 
 * list avoids any redundant calls to {@link Node#drawEnd(DrawContext)} 
 * and {@link Node#drawBegin(DrawContext)}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.26
 */
public class DrawList {
  /**
   * Appends the specified array of nodes to this draw list.
   * @param nodes the array of nodes; referenced, not copied.
   */
  public void append(Node[] nodes) {
    _list.add(nodes);
  }

  /**
   * Draws all nodes in this list.
   * @param dc the draw context.
   */
  public void draw(DrawContext dc) {

    // While drawing, need current and previous arrays of nodes.
    Node[] empty = new Node[0];
    Node[] nodes = empty;
    Node[] prevs;
    int nnode = nodes.length;
    int nprev;

    // Loop over all arrays of nodes in the list.
    int nlist = _list.size();
    for (int ilist=0; ilist<=nlist; ++ilist) {

      // Previous array of nodes.
      prevs = nodes;
      nprev = nnode;

      // Current array of nodes.
      nodes = (ilist<nlist)?_list.get(ilist):empty;
      nnode = nodes.length;

      // Index of top current node that differs from previous nodes.
      int mnode = (nnode<nprev)?nnode:nprev;
      int knode = 0;
      while (knode<mnode && nodes[knode]==prevs[knode])
        ++knode;

      // End draw (from bottom to top) for previous nodes that differ.
      for (int inode=nprev-1; inode>=knode; --inode)
        prevs[inode].drawEnd(dc);

      // Begin draw (from top to bottom) for current nodes that differ.
      for (int inode=knode; inode<nnode; ++inode)
        nodes[inode].drawBegin(dc);

      // If a current leaf node exists, draw it.
      if (nnode>0)
        nodes[nnode-1].draw(dc);
    }
  }

  private ArrayList<Node[]> _list = new ArrayList<Node[]>(32);
}
