/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

import edu.mines.jtk.opengl.*;
import static edu.mines.jtk.opengl.Gl.*;

/**
 * A transform context for picking.
 * <p>
 * A pick context has a pick segment, which is a line segment in a local 
 * coordinate system. During a pick traversal of the scene graph, pickable 
 * nodes compute points of intersection, if any, between their geometry and 
 * the pick segment. For efficiency, only nodes with bounding spheres that 
 * intersect the pick segment perform this computation.
 * <p>
 * The pick segment in a pick context has two endpoints. One endpoint lies
 * on the near clipping plane, with specified pixel (x,y) coordinates and z 
 * (depth) coordinate equal to 0.0. This endpoint is called the <em>near</em> 
 * endpoint of the pick segment. The <em>far</em> endpoint has the same pixel 
 * (x,y) coordinates as the near endpoint. Its pixel z coordinate equals the 
 * value read from the front z-buffer, plus dz/2, where dz is the z-buffer 
 * sampling interval. The added term dz/2 accounts for rounding of pixel 
 * depth coordinates, which are fixed-point numbers between 0.0 and 1.0.
 * Specifically, dz = 1.0/(2^m-1), where m is the constant GL_DEPTH_BITS, 
 * the number of bits in the z-buffer.
 * <p>
 * With these near and far endpoints, only the nearest pickable nodes (those 
 * with the smallest pixel z coordinates) can intersect the pick segment. The 
 * pick segment cannot, for example, intersect a pickable node that is hidden
 * behind a non-pickable node.
 * <p>
 * Care must be taken when picking nodes that rendered with polygon offset. 
 * Polygon offset modifies values in the depth buffer and thereby the far 
 * endpoint of the pick segment. A negative polygon offset may cause the 
 * pick segment to not quite intersect the geometry of a node that might 
 * otherwise be picked. A solution to this problem is to use non-negative 
 * polygon offsets.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.17
 */
public class PickContext extends TransformContext {

  /**
   * Constructs a pick context at specified pixel (x,y) coordinates.
   * @param canvas the canvas
   * @param xp the pixel x coordinate.
   * @param yp the pixel y coordinate.
   */
  public PickContext(ViewCanvas canvas, int xp, int yp) {
    super(canvas);

    // The pixel depth z from the z-buffer.
    double zp = canvas.getPixelZ(xp,yp);

    // The pixel depth z-buffer sampling interval.
    double dz = 1.0/(Math.pow(2.0,getDepthBits(canvas))-1.0);

    // The far endpoint of the pick segment.
    //Point3 far = new Point3(xp,yp,zp+0.5*dz);
    Point3 far = new Point3(xp,yp,1.0);

    // The near endpoint of the pick segment.
    Point3 near = new Point3(xp,yp,0.0);

    // The pick segment, transformed to world coordinates.
    _pickSegment = new PickSegment(near,far);
    System.out.println("near="+_pickSegment.getNearPoint());
    System.out.println(" far="+_pickSegment.getFarPoint());
    _pickSegment.transform(getPixelToWorld());
    System.out.println("near="+_pickSegment.getNearPoint());
    System.out.println(" far="+_pickSegment.getFarPoint());
  }

  /**
   * Gets the pick segment for this context.
   * @return the pick segment.
   */
  public PickSegment getPickSegment() {
    return _pickSegment.clone();
  }

  /**
   * Determines whether the pick segment intersects the bounding sphere
   * of the specified node.
   * @param node the node with a bounding sphere.
   * @return true, if the pick segment intersects the bounding sphere;
   *  false, otherwise.
   */
  public boolean segmentIntersectsSphereOf(Node node) {
    BoundingSphere bs = node.getBoundingSphere();
    Point3 a = _pickSegment.getFarPoint();
    Point3 b = _pickSegment.getNearPoint();
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
    Point3 p = new Point3(px,py,pz);
    System.out.println("p="+p+" c="+c+" r="+r);
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
    System.out.println("PickContext.getClosest: npick="+_pickResults.size());
    PickResult prmin = null;
    double zpmin = Double.MAX_VALUE;
    for (PickResult pr : _pickResults) {
      double zp = pr.getPixelZ();
      System.out.println(" zp="+zp);
      if (zp<zpmin) {
        zpmin = zp;
        prmin = pr;
      }
    }
    return (prmin!=null)?prmin.clone():null;
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
    _pickSegmentStack.push(_pickSegment.clone());
    _pickSegment.transform(transform.inverse());
  }

  /**
   * Restores the most recently saved (pushed) local-to-world transform.
   * Discards the current local-to-world transform.
   */
  public void popLocalToWorld() {
    super.popLocalToWorld();
    _pickSegment = _pickSegmentStack.pop();
  }

  private PickSegment _pickSegment;
  private ArrayStack<PickSegment> _pickSegmentStack = 
    new ArrayStack<PickSegment>();
  private ArrayList<PickResult> _pickResults = new ArrayList<PickResult>();

  private int getDepthBits(ViewCanvas canvas) {
    int[] bz = {0};
    GlContext context = canvas.getContext();
    context.lock();
    try {
      glGetIntegerv(GL_DEPTH_BITS,bz);
    } finally {
      context.unlock();
    }
    return bz[0];
  }
}
