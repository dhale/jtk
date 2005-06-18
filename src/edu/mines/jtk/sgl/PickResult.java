/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A result from a pick traversal.
 * <p>
 * A pick result has a list of nodes, ordered parent to child, that
 * represents the path from the root node to the picked node. The last
 * node in the list is the picked node.
 * <p>
 * A pick result also has a point of intersection with a pick shape.
 * This point may be obtained in either the local or world coordinate 
 * system, and its depth (z) may be obtained in pixel coordinates.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.17
 */
public class PickResult implements Cloneable {

  /**
   * Constructs a new pick result in the specified context.
   * @param pc the pick context.
   * @param point the picked point, in local coordinates.
   */
  public PickResult(PickContext pc, Point3 point) {
    _nodes = pc.getNodes();
    _localToWorld = pc.getLocalToWorld();
    _pointLocal = point.clone();
    _pointWorld = _localToWorld.times(point);
    _depthPixel = pc.getLocalToPixel().times(point).z;
  }

  /**
   * Returns a clone of this pick result.
   * @return the clone.
   */
  public PickResult clone() {
    PickResult pr = new PickResult();
    pr._nodes = _nodes.clone();
    pr._pointLocal = _pointLocal.clone();
    pr._pointWorld = _pointWorld.clone();
    pr._depthPixel = _depthPixel;
    pr._localToWorld = _localToWorld.clone();
    return pr;
  }

  /**
   * Gets the array of nodes in this result. The nodes are ordered parent 
   * to child; the last node in the array is the picked node.
   * @return the array of nodes; by copy, not by reference.
   */
  public Node[] getNodes() {
    return _nodes.clone();
  }

  /**
   * Gets the picked node in this result. The picked node is the last node
   * in the list (ordered parent to child) of nodes in this pick result.
   * @return the picked node.
   */
  public Node getNode() {
    return _nodes[_nodes.length-1];
  }

  /**
   * Gets a node in this result that is an instance of the specified class.
   * The node returned is the last node in the list (ordered parent to
   * child) of nodes in this pick result that is an instance of the
   * specified class. If no such node exists, this method returns null.
   * @return the node; null, if none.
   */
  public Node getNode(Class nodeClass) {
    for (int i=_nodes.length-1; i>=0; --i) {
      Node node = _nodes[i];
      if (node.getClass().equals(nodeClass))
        return node;
    }
    return null;
  }

  /**
   * Gets the picked point in local coordinates.
   * @return the picked point in local coordinates.
   */
  public Point3 getPointLocal() {
    return _pointLocal;
  }

  /**
   * Gets the picked point in world coordinates.
   * @return the picked point in world coordinates.
   */
  public Point3 getPointWorld() {
    return _pointWorld;
  }

  /**
   * Gets the pixel z (depth) coordinate of the picked point. This depth
   * depth coordinate increases from 0.0 at the near clipping plane to 1.0 
   * at the far clipping plane.
   * @return the pixel z coordinate.
   */
  public double getPixelZ() {
    return _depthPixel;
  }

  /**
   * Gets the local-to-world coordinate transform matrix.
   * @return the local-to-world coordinate transform matrix.
   */
  public Matrix44 getLocalToWorld() {
    return _localToWorld;
  }

  private Node[] _nodes;
  private Point3 _pointLocal;
  private Point3 _pointWorld;
  private double _depthPixel;
  private Matrix44 _localToWorld;

  private PickResult() {
  }
}
