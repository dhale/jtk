/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;


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
   * Constructs a transform context for the specified view canvas.
   * @param canvas the view canvas.
   */
  public CullContext(ViewCanvas canvas) {
    super(canvas);
    initFrustum();
  }

  /**
   * Determines whether the view frustrum intersects the bounding sphere
   * of the specified node.
   * @param node the node with a bounding sphere.
   * @return true, if the view frustum intersects the bounding sphere;
   *  false, otherwise.
   */
  public boolean frustumIntersectsSphereOf(Node node) {
    if (_active!=0) { // if at least one frustum plane is active, ...
      BoundingSphere bs = node.getBoundingSphere(false);
      if (bs.isEmpty())
        return false;
      if (bs.isInfinite())
        return true;
      Point3 c = bs.getCenter();
      double r = bs.getRadius();
      double s = -r;
      for (int i=0,plane=1; i<6; ++i,plane<<=1) { // for all six planes
        if ((_active&plane)!=0) { // if plane is active
          double d = _planes[i].distanceTo(c); // distance from center
          if (d<s) { // if sphere is entirely outside (below plane)
            return false; // no intersection
          } else if (d>r) { // else if sphere is entirely above plane
            _active ^= plane; // need not test this plane again
          }
        }
      }
    }
    return true;
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

  /**
   * Saves the current node, and then makes the specified node current.
   * @param node the new current node.
   */
  public void pushNode(Node node) {
    super.pushNode(node);
    _activeStack.push(_active);
  }

  /**
   * Restores the most recently saved (pushed) node.
   * Discards the current node.
   */
  public void popNode() {
    super.popNode();
    _active = _activeStack.pop();
  }

  /**
   * Saves the local-to-world transform before appending a transform.
   * The specified transform matrix is post-multiplied with the current
   * local-to-world transform, such that the specified transform is applied
   * first when transforming local coordinates to world coordinates.
   * @param transform the transform to append.
   */
  public void pushLocalToWorld(Matrix44 transform) {
    super.pushLocalToWorld(transform);
    for (int i=0,plane=1; i<6; ++i,plane<<=1) { // for all planes
      _planesStack.push(new Plane(_planes[i])); // save plane
      if ((_active&plane)!=0) // if plane is active
        _planes[i].transformWithInverse(transform); // transform it
    }
  }

  /**
   * Restores the most recently saved (pushed) local-to-world transform.
   * Discards the current local-to-world transform.
   */
  public void popLocalToWorld() {
    super.popLocalToWorld();
    for (int i=5; i>=0; --i) // for all planes (in reverse order!)
      _planes[i] = _planesStack.pop(); // restore plane
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // More efficient than ArrayStack<Integer>.
  private static class IntStack {
    void push(int active) {
      if (_n==_a.length) {
        int[] a = new int[2*_n];
        for (int i=0; i<_n; ++i)
          a[i] = _a[i];
        _a = a;
      }
      _a[_n++] = active;
    }
    int pop() {
      return _a[--_n];
    }
    private int _n = 0;
    private int[] _a = new int[8];
  }

  private DrawList _drawList = new DrawList();
  private Plane[] _planes = new Plane[6];
  private int _active;
  private ArrayStack<Plane> _planesStack = new ArrayStack<Plane>();
  private IntStack _activeStack = new IntStack();

  private void initFrustum() {

    // Frustum planes. Point inside lie in or above the planes.
    _planes[0] = new Plane(-1.0, 0.0, 0.0, 1.0); // right
    _planes[1] = new Plane( 1.0, 0.0, 0.0, 1.0); // left
    _planes[2] = new Plane( 0.0,-1.0, 0.0, 1.0); // top
    _planes[3] = new Plane( 0.0, 1.0, 0.0, 1.0); // bottom
    _planes[4] = new Plane( 0.0, 0.0,-1.0, 1.0); // near
    _planes[5] = new Plane( 0.0, 0.0, 1.0, 1.0); // far

    // Initially, all six planes are active; mark each with one bit.
    _active = 0x0000003F;

    Matrix44 worldToCube = getWorldToCube();
    for (int i=0; i<6; ++i) {
      _planes[i].transformWithInverse(worldToCube);
    }
  }
}
