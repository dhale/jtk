
/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.opengl.Gl.*;

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
    _transform = transform.clone();
  }

  /**
   * Gets the transform for this group.
   * @return the transform; by copy, not by reference.
   */
  public Matrix44 getTransform() {
    return _transform.clone();
  }

  public void drawBegin(DrawContext dc) {
    glPushMatrix();
    glMultMatrixd(_transform.m);
    dc.pushTransform(_transform);
  }

  public void draw(DrawContext dc) {
  }

  public void drawEnd(DrawContext dc) {
    dc.popTransform();
    glPopMatrix();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Matrix44 _transform;
}
