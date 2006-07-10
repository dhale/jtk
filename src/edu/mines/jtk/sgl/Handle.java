/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * A handle for manipulating other nodes. A handle is a special type of node, 
 * because its size in pixel coordinates is constant. A handle has the same 
 * pixel dimensions in all views (all contexts) in which it is rendered. This 
 * property makes handles suitable for selecting and dragging with a mouse or 
 * other pointing device.
 * <p>
 * A handle maintains its constant pixel size by applying a context-dependent 
 * scaling to its children. A handle is much like a transform group, one that 
 * augments a specified transform with a context-dependent scaling.
 * <p>
 * A consequence of this constant-size-in-pixels property is that a handle's 
 * bounding sphere in local coordinates must be infinite. A bounding sphere 
 * cannot be context-dependent, and the only sphere that is guaranteed to 
 * bound a handle in any context in which it appears is the infinite sphere.
 * <p>
 * By convention, the geometry of a handle's node children is centered at the 
 * point (0,0,0). A handle first scales the bounding sphere of its children 
 * so that the radius of that sphere transforms to the handle pixel size. The 
 * handle then applies a specified transform to position and orient its 
 * children within its local coordinate system.
 * <p>
 * A handle is a group node, because the child leaf nodes of each handle 
 * subclass are typically shared by instances of that subclass. Then, any 
 * changes to the shared node children are conveniently reflected in all 
 * handles of that class. Often a handle subclass has a single leaf node 
 * child.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.30
 */
public abstract class Handle extends Group {

  /**
   * Gets the size in pixels of all handles.
   * @return the size.
   */
  public static double getSize() {
    return _size;
  }

  /**
   * Sets the size in pixels of all handles. Because handle size is not
   * associated with any particular handle or world, this method does
   * force a repaint of any view canvases in which handles appear.
   * @param size the size.
   */
  public static void setSize(double size) {
    _size = size;
  }

  /**
   * Gets the view-independent transform matrix for this handle. 
   * This transform does not include the view-dependent scaling that is 
   * applied to the handle's geometry <em>before</em> the transform.
   * @return the transform.
   */
  public Matrix44 getTransform() {
    return new Matrix44(_transform);
  }

  /**
   * Sets the view-independent transform matrix for this handle.
   * This transform does not include the view-dependent scaling that is 
   * applied to the handle's geometry <em>before</em> the transform.
   * @param transform the transform.
   */
  public void setTransform(Matrix44 transform) {
    _transform = new Matrix44(transform);
    dirtyBoundingSphere();
    dirtyDraw();
  }

  /**
   * Gets the view-independent location of the center of this handle.
   * @return the center point.
   */
  public Point3 getLocation() {
    return _transform.times(new Point3());
  }

  /**
   * Sets the view-independent location of the center of this handle.
   * This method conveniently sets the handle transform to a pure
   * translation to the specified center point.
   * @param p the center point.
   */
  public void setLocation(Point3 p) {
    setLocation(p.x,p.y,p.z);
  }

  /**
   * Sets the view-independent location of the center of this handle.
   * This method conveniently sets the handle transform to a pure
   * translation to the center point with specified coordinates.
   * @param x the center x coordinate.
   * @param y the center y coordinate.
   * @param z the center z coordinate.
   */
  public void setLocation(double x, double y, double z) {
    _transform = Matrix44.translate(x,y,z);
    dirtyBoundingSphere();
    dirtyDraw();
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Computes the bounding sphere for this handle.
   * @param finite true, to force bounding sphere to be finite.
   * @return the bounding sphere.
   */
  protected BoundingSphere computeBoundingSphere(boolean finite) {
    _boundingSphereChildren = super.computeBoundingSphere(true);
    return (finite)?BoundingSphere.empty():BoundingSphere.infinite();
  }

  /**
   * Constructs a handle with specified transform matrix.
   * @param transform the transform matrix.
   */
  protected Handle(Matrix44 transform) {
    setTransform(transform);
  }

  /**
   * Constructs a handle with specified center location.
   * @param p the center point.
   */
  protected Handle(Point3 p) {
    setLocation(p.x,p.y,p.z);
  }

  /**
   * Constructs a handle with specified center coordinates.
   * @param x the center x coordinate.
   * @param y the center y coordinate.
   * @param z the center z coordinate.
   */
  protected Handle(double x, double y, double z) {
    setLocation(x,y,z);
  }

  /**
   * Pushes the view-dependent transform onto the specified cull context.
   * @param cc the cull context.
   */
  protected void cullBegin(CullContext cc) {
    super.cullBegin(cc);
    Matrix44 transform = computeTransform(cc);
    cc.pushLocalToWorld(transform);
  }

  /**
   * Pops the view-dependent transform from the specified cull context.
   * @param cc the cull context.
   */
  protected void cullEnd(CullContext cc) {
    cc.popLocalToWorld();
    super.cullEnd(cc);
  }

  /**
   * Pushes the view-dependent transform onto the specified draw context.
   * @param dc the draw context.
   */
  protected void drawBegin(DrawContext dc) {
    super.drawBegin(dc);
    Matrix44 transform = computeTransform(dc);
    dc.pushLocalToWorld(transform);
    glPushMatrix();
    glMultMatrixd(transform.m,0);
  }

  /**
   * Pops the view-dependent transform from the specified draw context.
   * @param dc the draw context.
   */
  protected void drawEnd(DrawContext dc) {
    dc.popLocalToWorld();
    glPopMatrix();
    super.drawEnd(dc);
  }

  /**
   * Pushes the view-dependent transform onto the specified pick context.
   * @param pc the pick context.
   */
  protected void pickBegin(PickContext pc) {
    super.pickBegin(pc);
    Matrix44 transform = computeTransform(pc);
    pc.pushLocalToWorld(transform);
  }

  /**
   * Pops the view-dependent transform from the specified pick context.
   * @param pc the pick context.
   */
  protected void pickEnd(PickContext pc) {
    pc.popLocalToWorld();
    super.pickEnd(pc);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Size (radius in pixels) of handles.
  private static double _size = 10;

  // The transform applied after view-dependent scaling.
  private Matrix44 _transform = Matrix44.identity();

  // Bounding sphere of our children.
  private BoundingSphere _boundingSphereChildren;

  /**
   * Computes the transform matrix for this handle in the specified context.
   * The context is view-dependent, as is the computed transform matrix.
   * This matrix includes the view-dependent scaling required for the
   * handle to have the correct size in pixels.
   * @param tc the transform context.
   * @return the transform matrix for this handle.
   */
  private Matrix44 computeTransform(TransformContext tc) {
    View view = tc.getView();
    Tuple3 as = view.getAxesScale();
    Matrix44 localToPixel = tc.getLocalToPixel().times(_transform);
    Matrix44 pixelToLocal = localToPixel.inverse();
    Point3 p = new Point3(0.0,0.0,0.0);
    Point3 q = localToPixel.times(p);
    q.x += getSize();
    q = pixelToLocal.times(q);
    double dx = (q.x-p.x)*as.x;
    double dy = (q.y-p.y)*as.y;
    double dz = (q.z-p.z)*as.z;
    double d = Math.sqrt(dx*dx+dy*dy+dz*dz);
    double r = _boundingSphereChildren.getRadius();
    double s = d/r;
    double sx = s/as.x;
    double sy = s/as.y;
    double sz = s/as.z;
    return _transform.times(Matrix44.scale(sx,sy,sz));
  }
}
