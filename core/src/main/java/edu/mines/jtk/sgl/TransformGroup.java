
/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * A group node that transforms the coordinates for its children.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class TransformGroup extends Group {

  /**
   * Constructs a new transform group with specified transform.
   * @param transform the transform; copied, not referenced.
   */
  public TransformGroup(Matrix44 transform) {
    _transform = new Matrix44(transform);
  }

  /**
   * Gets the transform for this group.
   * @return the transform; by copy, not by reference.
   */
  public Matrix44 getTransform() {
    return new Matrix44(_transform);
  }

  /**
   * Sets the transform for this group.
   * @param transform the transform; by copy, not by reference.
   */
  public void setTransform(Matrix44 transform) {
    _transform = new Matrix44(transform);
    dirtyBoundingSphere();
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Pushes the transform matrix onto the specified cull context.
   * @param cc the cull context.
   */
  protected void cullBegin(CullContext cc) {
    super.cullBegin(cc);
    cc.pushLocalToWorld(_transform);
  }

  /**
   * Pops the transform matrix from the specified cull context.
   * @param cc the cull context.
   */
  protected void cullEnd(CullContext cc) {
    cc.popLocalToWorld();
    super.cullEnd(cc);
  }

  /**
   * Pushes the transform matrix onto the specified draw context.
   * @param dc the draw context.
   */
  protected void drawBegin(DrawContext dc) {
    super.drawBegin(dc);
    dc.pushLocalToWorld(_transform);
    glPushMatrix();
    glMultMatrixd(_transform.m,0);
  }

  /**
   * Pops the transform matrix from the specified draw context.
   * @param dc the draw context.
   */
  protected void drawEnd(DrawContext dc) {
    dc.popLocalToWorld();
    glPopMatrix();
    super.drawEnd(dc);
  }

  /**
   * Pushes the transform matrix onto the specified pick context.
   * @param pc the pick context.
   */
  protected void pickBegin(PickContext pc) {
    super.pickBegin(pc);
    pc.pushLocalToWorld(_transform);
  }

  /**
   * Pops the transform matrix from the specified pick context.
   * @param pc the pick context.
   */
  protected void pickEnd(PickContext pc) {
    pc.popLocalToWorld();
    super.pickEnd(pc);
  }
  
  /**
   * Computes the bounding sphere for this transform group. A transform 
   * group computes its bounding sphere in its untransformed coordinate 
   * system, not the transformed system of its children.
   * @param finite true, to force bounding sphere to be finite.
   * @return the computed bounding sphere.
   */
  protected BoundingSphere computeBoundingSphere(boolean finite) {
    BoundingSphere bs = super.computeBoundingSphere(finite);
    if (!bs.isEmpty() && !bs.isInfinite()) {
      double r = bs.getRadius();
      Point3 c = bs.getCenter();
      Point3 x = new Point3(c.x+r,c.y,c.z);
      Point3 y = new Point3(c.x,c.y+r,c.z);
      Point3 z = new Point3(c.x,c.y,c.z+r);
      c = _transform.times(c);
      x = _transform.times(x);
      y = _transform.times(y);
      z = _transform.times(z);
      Vector3 cx = c.minus(x);
      Vector3 cy = c.minus(y);
      Vector3 cz = c.minus(z);
      double lx = cx.length();
      double ly = cy.length();
      double lz = cz.length();
      r = lx;
      if (r<ly) r = ly;
      if (r<lz) r = lz;
      bs = new BoundingSphere(c,r);
    }
    return bs;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Matrix44 _transform;
}
