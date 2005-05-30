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
   * Constructs a cull context with identity transforms.
   */
  public CullContext() {
    super();
  }

  /**
   * Constructs a transform context for the specified view canvas.
   * @param canvas the view canvas.
   */
  public CullContext(ViewCanvas canvas) {
    super(canvas);
    // TODO: initialize view frustum
  }

  /**
   * Tests the specified node for intersection with the view frustum.
   * @return true, if the view frustum intersects the bounding sphere
   *  of the node; false, otherwise.
   */
  public boolean frustumIntersects(Node node) {
    BoundingSphere bs = node.getBoundingSphere();

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
  private Plane[] _frustum = new Plane[6];

  private void initFrustum(ViewCanvas canvas) {
    _frustum[0] = new Plane(-1.0, 0.0, 0.0, 1.0); // right
    _frustum[1] = new Plane( 1.0, 0.0, 0.0, 1.0); // left
    _frustum[2] = new Plane( 0.0,-1.0, 0.0, 1.0); // top
    _frustum[3] = new Plane( 0.0, 1.0, 0.0, 1.0); // bottom
    _frustum[4] = new Plane( 0.0, 0.0,-1.0, 1.0); // near
    _frustum[5] = new Plane( 0.0, 0.0, 1.0, 1.0); // far
  }
}
