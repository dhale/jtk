/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import edu.mines.jtk.opengl.*;
import static edu.mines.jtk.opengl.Gl.*;

/**
 * A view of a world, as if in orbit around that world.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.24
 */
public class OrbitView extends View {

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Updates transforms for the world drawn by this view.
   * Classes that extend this base class must implement this method.
   * <p>
   * This method is called when the world-to-view transform of this 
   * view might require updating; e.g., when the bounds of the specified 
   * world have changed. Furthermore, this method might also update the 
   * view-to-cube and cube-to-pixel transforms of its view canvases.
   * @param world the world.
   */
  protected void updateTransforms(World world) {
  }

  /**
   * Updates the transforms for a canvas on which this view draws.
   * Classes that extend this base class must implement this method.
   * <p>
   * This method is called when the view-to-cube and cube-to-pixel
   * transforms of the specified view canvas might require updating; 
   * e.g., when the canvas has been resized.
   * @param canvas the view canvas.
   */
  protected void updateTransforms(ViewCanvas canvas) {
  }

  /**
   * Draws this view on the specified canvas.
   * @param canvas the canvas.
   */
  protected void draw(ViewCanvas canvas) {

    // Clear.
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

    // Our world.
    World world = getWorld();
    if (world==null)
      return;

    // Viewport (cube-to-pixel) transform.
    int[] xywh = canvas.getViewport();
    glViewport(xywh[0],xywh[1],xywh[2],xywh[3]);

    // Projection (view-to-cube) transform.
    Matrix44 viewToCube = canvas.getViewToCube();
    glMatrixMode(GL_PROJECTION);
    glLoadMatrixd(viewToCube.m);

    // View (world-to-view) transform.
    Matrix44 worldToView = this.getWorldToView();
    glMatrixMode(GL_MODELVIEW);
    glLoadMatrixd(worldToView.m);

    // Cull and draw the world.
    CullContext cc = new CullContext(canvas);
    world.cullApply(cc);
    DrawList dl = cc.getDrawList();
    DrawContext dc = new DrawContext(canvas);
    dl.draw(dc);
  }
}
