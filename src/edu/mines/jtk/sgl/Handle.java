/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.opengl.Gl.*;

/**
 * A handle for manipulating objects. A handle is a special type of node.
 * Its size in pixel coordinates is view-independent, which makes its size
 * in local coordinates view-dependent. In other words, no matter where a 
 * handle appears in a view, its size in pixels will appear the same. This 
 * property makes handles easily pickable with a mouse or other pointer. A 
 * handle is always large enough to be picked easily, but no larger, so that 
 * it does not obscure the objects to which it is attached.
 * <p>
 * One unfortunate effect of this constant-size-in-pixels property is that
 * a handle's bounding sphere (in local coordinates) varies among the views 
 * in which the handle appears. Therefore, the bounding sphere is often
 * infinite, and a handle with an infinite bounding sphere can never be 
 * culled during a drawing or picking traversal. Fortunately, handles should
 * be used sparingly - too many of them would clutter most views - so the
 * decrease in performance due to infinite bounding spheres is negligible.
 * <p>
 * The geometry of all handles is centered at the point (0,0,0) and 
 * normalized so that its bounding sphere in local coordinates has
 * approximately unit radius. This handle geometry is first scaled in a 
 * view-dependent manner to obtain a specified size in pixels. Then, a 
 * specified transform is applied to position and orient the handle in 
 * its local coordinate system.
 * <p>
 * A handle is a group node, because most handle subclasses have a single 
 * child leaf node that is shared by all instances of that subclass. Any
 * changes to the shared child node's appearance or size (in pixels) are 
 * then conveniently reflected in all handles of that class.
 * <p>
 * A handle is much like a transform group, one that augments its transform 
 * with a view-dependent scaling that maintains its constant size in pixels.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.30
 */
public abstract class Handle extends Group {

  /**
   * Gets the view-independent transform matrix for this handle. 
   * This transform does not include the view-dependent scaling that is 
   * applied to the handle's geometry <em>before</em> the transform.
   * @return the transform.
   */
  public Matrix44 getTransform() {
    return _transform.clone();
  }

  /**
   * Sets the view-independent transform matrix for this handle.
   * This transform does not include the view-dependent scaling that is 
   * applied to the handle's geometry <em>before</em> the transform.
   * @param transform the transform.
   */
  public void setTransform(Matrix44 transform) {
    _transform = transform.clone();
    dirtyBoundingSphere();
    dirtyDraw();
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
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Gets the size in pixels of this handle. Classes that extend this
   * abstract base class must implement this method to return the handle
   * size in pixels.
   * @return the size.
   */
  protected abstract double getSize();

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
    glMultMatrixd(transform.m);
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

  // The transform applied after view-dependent scaling.
  private Matrix44 _transform = Matrix44.identity();

  /**
   * Computes the transform matrix for this handle in the specified context.
   * The context is view-dependent, as is the computed transform matrix.
   * This matrix includes the view-dependent scaling required for the
   * handle to have the correct size in pixels.
   * @param tc the transform context.
   * @return the transform matrix for this handle.
   */
  private Matrix44 computeTransform(TransformContext tc) {
    Matrix44 localToPixel = tc.getLocalToPixel().times(_transform);
    Matrix44 pixelToLocal = localToPixel.inverse();
    Point3 p = new Point3(0.0,0.0,0.0);
    Point3 q = localToPixel.times(p);
    q.x += getSize();
    q = pixelToLocal.times(q);
    double d = p.distanceTo(q);
    return _transform.times(Matrix44.scale(d,d,d));
  }
}
