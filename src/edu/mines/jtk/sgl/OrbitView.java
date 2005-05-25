/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A view of a world, as if in orbit around that world.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.24
 */
public class OrbitView extends View {

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Updates the transforms managed by this view.
   * Classes that extend this base class must implement this method.
   * This method is called when the world painted in a view has changed 
   * so that a new world-to-view transform may be required.
   */
  protected void updateTransforms() {
  }

  /**
   * Updates the transforms for a canvas on which this view is painted.
   * Classes that extend this base class must implement this method.
   * This method is called when the view-to-cube and/or cube-to-pixel
   * transforms of the specified view canvas must be recomputed; e.g.,
   * when the canvas has been resized. 
   * @param canvas the view canvas.
   */
  protected void updateTransforms(ViewCanvas canvas) {
  }
}
