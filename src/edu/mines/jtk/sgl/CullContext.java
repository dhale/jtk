/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

/**
 * A context for view frustum culling.
 * <p>
 * A cull context has a draw list, in which it accumulates copies of
 * its node stack. Typically, a leaf node copies the node stack to the 
 * draw list when its bounding sphere intersects the view frustum of
 * the cull context.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.26
 */
public class CullContext extends TransformContext {

  /**
   * Tests the specified node for intersection with the view frustum.
   * @return true, if the view frustum intersects the bounding sphere
   *  of the node; false, otherwise.
   */
  public boolean frustumIntersects(Node node) {
    return true; // TODO: test bounding sphere
  }

  /**
   * Appends the node stack to the draw list in this context.
   */
  public void appendNodes() {
    _drawList.append(getNodes());
  }

  /**
   * Gets the draw list accumulated in this context.
   * @return the draw list.
   */
  public DrawList getDrawList() {
    return _drawList;
  }

  private DrawList _drawList = new DrawList();
}
