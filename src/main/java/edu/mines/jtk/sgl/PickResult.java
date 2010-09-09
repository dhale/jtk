/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.event.MouseEvent;

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
public class PickResult {

  /**
   * Constructs a new pick result in the specified context.
   * @param pc the pick context.
   * @param point the picked point, in local coordinates.
   */
  public PickResult(PickContext pc, Point3 point) {
    _event = pc.getMouseEvent();
    _nodes = pc.getNodes();
    _localToWorld = pc.getLocalToWorld();
    _localToPixel = pc.getLocalToPixel();
    _worldToPixel = pc.getWorldToPixel();
    _pointLocal = new Point3(point);
    _pointWorld = _localToWorld.times(point);
    _pointPixel = _localToPixel.times(point);
    _depthPixel = _pointPixel.z;
  }

  /**
   * Gets the mouse event for this pick result. This is the mouse event
   * of the pick context for which this pick result was constructed.
   * @return the mouse event.
   */
  public MouseEvent getMouseEvent() {
    return _event;
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
   * Gets a node in this result that is dragable.
   * The node returned is the last node in the list (ordered parent to
   * child) of nodes in this pick result that implements the interface
   * {@link Dragable}. If no such node exists, this method returns null.
   * @return the dragable node; null, if none.
   */
  public Dragable getDragableNode() {
    return (Dragable)getNode(Dragable.class);
  }

  /**
   * Gets a node in this result that is selectable.
   * The node returned is the last node in the list (ordered parent to
   * child) of nodes in this pick result that implements the interface
   * {@link Selectable}. If no such node exists, this method returns null.
   * @return the selectable node; null, if none.
   */
  public Selectable getSelectableNode() {
    return (Selectable)getNode(Selectable.class);
  }

  /**
   * Gets a node in this result that is an instance of the specified class.
   * The node returned is the last node in the list (ordered parent to
   * child) of nodes in this pick result that is an instance of the
   * specified class. If no such node exists, this method returns null.
   * @return the node; null, if none.
   */
  public Node getNode(Class<?> nodeClass) {
    for (int i=_nodes.length-1; i>=0; --i) {
      Node node = _nodes[i];
      if (nodeClass.isAssignableFrom(node.getClass()))
        return node;
    }
    return null;
  }

  /**
   * Gets the picked point in local coordinates.
   * @return the picked point in local coordinates.
   */
  public Point3 getPointLocal() {
    return new Point3(_pointLocal);
  }

  /**
   * Gets the picked point in world coordinates.
   * @return the picked point in world coordinates.
   */
  public Point3 getPointWorld() {
    return new Point3(_pointWorld);
  }

  /**
   * Gets the picked point in pixel coordinates.
   * @return the picked point in pixel coordinates.
   */
  public Point3 getPointPixel() {
    return new Point3(_pointPixel);
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
    return new Matrix44(_localToWorld);
  }

  /**
   * Gets the local-to-pixel coordinate transform matrix.
   * @return the local-to-pixel coordinate transform matrix.
   */
  public Matrix44 getLocalToPixel() {
    return new Matrix44(_localToPixel);
  }

  /**
   * Gets the world-to-pixel coordinate transform matrix.
   * @return the world-to-pixel coordinate transform matrix.
   */
  public Matrix44 getWorldToPixel() {
    return new Matrix44(_worldToPixel);
  }

  /**
   * Gets the canvas for which this pick result was constructed.
   * @return the view canvas.
   */
  public ViewCanvas getViewCanvas() {
    return (ViewCanvas)_event.getSource();
  }

  /**
   * Gets the view for which this pick result was constructed.
   * @return the view.
   */
  public View getView() {
    return getViewCanvas().getView();
  }

  /**
   * Gets the world for which this pick result was constructed.
   * @return the world.
   */
  public World getWorld() {
    return getView().getWorld();
  }

  private MouseEvent _event;
  private Node[] _nodes;
  private Point3 _pointLocal;
  private Point3 _pointWorld;
  private Point3 _pointPixel;
  private double _depthPixel;
  private Matrix44 _localToWorld;
  private Matrix44 _localToPixel;
  private Matrix44 _worldToPixel;
}
