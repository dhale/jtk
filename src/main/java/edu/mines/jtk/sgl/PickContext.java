/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * A transform context for picking.
 * <p>
 * A pick context has a pick segment, which is a line segment in a local 
 * coordinate system. During a pick traversal of the scene graph, nodes 
 * compute points of intersection, if any, between their geometry and 
 * the pick segment. For efficiency, only nodes with bounding spheres that 
 * intersect the pick segment will perform intersection computations.
 * <p>
 * The pick segment in a pick context has two endpoints. One endpoint lies 
 * on the near clipping plane, with specified pixel (x,y) coordinates and 
 * pixel z coordinate 0.0. This endpoint is called the <em>near</em> 
 * endpoint of the pick segment. The <em>far</em> endpoint lies on the far
 * clipping plane. It has the same specified pixel (x,y) coordinates as 
 * the near endpoint, but has pixel z coordinate 1.0. With these pick
 * segment endpoints, only geometry between the near and far clipping
 * planes can be picked.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.21
 */
public class PickContext extends TransformContext {

  /**
   * Constructs a pick context for the specified mouse event.
   * @param event the mouse event.
   */
  public PickContext(MouseEvent event) {
    super((ViewCanvas)event.getSource());
    _event = event;
    int xp = event.getX();
    int yp = event.getY();

    // The near endpoint.
    Point3 near = new Point3(xp,yp,0.0);

    // The far endpoint.
    Point3 far = new Point3(xp,yp,1.0);

    // The pick segment, transformed to world coordinates.
    _pickSegment = new Segment(near,far);
    _pickSegment.transform(getPixelToWorld());
    _nearPoint = _pickSegment.getA();
    _farPoint = _pickSegment.getB();
  }

  /**
   * Gets the mouse event for which this context was constructed.
   * @return the mouse event.
   */
  public MouseEvent getMouseEvent() {
    return _event;
  }

  /**
   * Gets the pick segment for this context. Endpoint A of the segment lies 
   * on the near clipping plane; endpoint B lies on the far clipping plane.
   * @return the pick segment.
   */
  public Segment getPickSegment() {
    return new Segment(_pickSegment);
  }

  /**
   * Determines whether the pick segment intersects the bounding sphere
   * of the specified node.
   * @param node the node with a bounding sphere.
   * @return true, if the pick segment intersects the bounding sphere;
   *  false, otherwise.
   */
  public boolean segmentIntersectsSphereOf(Node node) {
    BoundingSphere bs = node.getBoundingSphere(false);
    if (bs.isEmpty())
      return false;
    if (bs.isInfinite())
      return true;
    Point3 a = _farPoint;
    Point3 b = _nearPoint;
    Point3 c = bs.getCenter();
    double r = bs.getRadius();
    double rr = r*r;
    double ax = a.x;
    double ay = a.y;
    double az = a.z;
    double bx = b.x;
    double by = b.y;
    double bz = b.z;
    double cx = c.x;
    double cy = c.y;
    double cz = c.z;
    double bax = bx-ax;
    double bay = by-ay;
    double baz = bz-az;
    double cax = cx-ax;
    double cay = cy-ay;
    double caz = cz-az;
    double caba = cax*bax+cay*bay+caz*baz;

    // Point on line through (a,b) that is closest to sphere.
    double px,py,pz;

    // If endpoint a is closest, ...
    if (caba<=0.0) {
      px = ax;
      py = ay;
      pz = az;
    }

    // Else if endpoint a is not closest, ...
    else {
      double baba = bax*bax+bay*bay+baz*baz;

      // If endpoint b is closest, ...
      if (baba<=caba) {
        px = bx;
        py = by;
        pz = bz;
      } 
      
      // Else if closest point is between endpoints a and b, ...
      else {
        double u = caba/baba;
        px = ax+u*bax;
        py = ay+u*bay;
        pz = az+u*baz;
      }
    }

    // Compare distance-to-closest-point-squared with radius-squared.
    double dx = px-cx;
    double dy = py-cy;
    double dz = pz-cz;
    return dx*dx+dy*dy+dz*dz<=rr;
  }

  /**
   * Adds a pick result with specified pick point to this context.
   * @param point the pick point, in local coordinates.
   */
  public void addResult(Point3 point) {
    if (point!=null) {
      PickResult pr = new PickResult(this,point);
      _pickResults.add(pr);
    }
  }

  /**
   * Gets the pick result closest to the origin of the pick segment.
   * @return the pick result; null, if none.
   */
  public PickResult getClosest() {
    PickResult prmin = null;
    double zpmin = Double.MAX_VALUE;
    for (PickResult pr : _pickResults) {
      double zp = pr.getPixelZ();
      if (zp<zpmin) {
        zpmin = zp;
        prmin = pr;
      }
    }
    return prmin;
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
    _pickSegmentStack.push(new Segment(_pickSegment));
    _pickSegment.transform(transform.inverse());
    _nearPoint = _pickSegment.getA();
    _farPoint = _pickSegment.getB();
  }

  /**
   * Restores the most recently saved (pushed) local-to-world transform.
   * Discards the current local-to-world transform.
   */
  public void popLocalToWorld() {
    super.popLocalToWorld();
    _pickSegment = _pickSegmentStack.pop();
    _nearPoint = _pickSegment.getA();
    _farPoint = _pickSegment.getB();
  }

  private MouseEvent _event;
  private Segment _pickSegment;
  private Point3 _nearPoint;
  private Point3 _farPoint;
  private ArrayStack<Segment> _pickSegmentStack = 
    new ArrayStack<Segment>();
  private ArrayList<PickResult> _pickResults = new ArrayList<PickResult>();
}
