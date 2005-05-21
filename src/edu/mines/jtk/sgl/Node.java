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

  void addParent(Group parent) {
    if (!_parentList.contains(parent))
      _parentList.add(parent);
  }

  void removeParent(Group parent) {
    _parentList.remove(parent);
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

  ArrayList<Group> _parentList = new ArrayList<Group>();
}
