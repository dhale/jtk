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

  /**
   * Constructs an orbit view of no world.
   */
  public OrbitView() {
    super();
  }

  /**
   * Constructs an orbit view of the specified world.
   * @param world the world.
   */
  public OrbitView(World world) {
    super(world);
  }

  public void setAzimuth(double azimuth) {
    if (_azimuth==azimuth)
      return;
    _azimuth = azimuth;
    updateView();
  }

  public void setElevation(double elevation) {
    if (_elevation==elevation)
      return;
    _elevation = elevation;
    updateView();
  }

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
    if (world==null) 
      return;
    BoundingSphere bs = world.getBoundingSphere();
    Point3 c = (!bs.isEmpty())?bs.getCenter():new Point3();
    double r = (!bs.isEmpty())?bs.getRadius():1.0;
    double s = (r>0.0)?1.0/r:1.0;
    System.out.println("c="+c+" s="+s);
    Matrix44 center = Matrix44.translate(-c.x,-c.y,-c.z);
    Matrix44 scale = Matrix44.scale(s,s,s);
    Matrix44 azimuth = Matrix44.rotateY(-_azimuth);
    Matrix44 elevation = Matrix44.rotateX(_elevation);
    Matrix44 distance = Matrix44.translate(0,0,-_distance);
    Matrix44 worldToView = Matrix44.identity();
    worldToView.timesEquals(distance);
    System.out.println("d: worldToView=\n"+worldToView);
    worldToView.timesEquals(elevation);
    worldToView.timesEquals(azimuth);
    worldToView.timesEquals(scale);
    System.out.println("s: worldToView=\n"+worldToView);
    worldToView.timesEquals(center);
    System.out.println("c: worldToView=\n"+worldToView);
    setWorldToView(worldToView);
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

    // View to cube.
    Matrix44 viewToCube = Matrix44.perspective(45.0,1.0,0.1,10.0);
    canvas.setViewToCube(viewToCube);

    // Cube to pixel.
    int w = canvas.getWidth();
    int h = canvas.getHeight();
    double s = 0.5*Math.min(w,h);
    double x = 0.5*w;
    double y = 0.5*h;
    Matrix44 cubeToPixel = Matrix44.translate(x,y,0);
    cubeToPixel.timesEquals(Matrix44.scale(s,s,s));
    canvas.setCubeToPixel(cubeToPixel);
  }

  /**
   * Draws this view on the specified canvas.
   * @param canvas the canvas.
   */
  protected void draw(ViewCanvas canvas) {
    System.out.println("OrbitView.draw:");

    // Clear.
    glClearColor(0.0f,0.0f,0.0f,0.0f);
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

    // Our world.
    World world = getWorld();
    if (world==null)
      return;

    // Viewport (cube-to-pixel) transform.
    int[] viewport = canvas.getViewport();
    int x = viewport[0];
    int y = viewport[1];
    int w = viewport[2];
    int h = viewport[3];
    glViewport(x,y,w,h);
    System.out.println("x="+x+" y="+y+" w="+w+" h="+h);

    // Projection (view-to-cube) transform.
    Matrix44 viewToCube = canvas.getViewToCube();
    glMatrixMode(GL_PROJECTION);
    glLoadMatrixd(viewToCube.m);
    glLoadIdentity();
    glOrtho(-5.0,5.0,-5.0,5.0,-5.0,5.0);
    System.out.println("viewToCube=\n"+viewToCube);

    // View (world-to-view) transform.
    Matrix44 worldToView = this.getWorldToView();
    glMatrixMode(GL_MODELVIEW);
    glLoadMatrixd(worldToView.m);
    System.out.println("worldToView=\n"+worldToView);

    // Cull and draw the world.
    CullContext cc = new CullContext(canvas);
    world.cullApply(cc);
    DrawList dl = cc.getDrawList();
    DrawContext dc = new DrawContext(canvas);
    dl.draw(dc);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _distance = 2.0;
  private double _azimuth = Math.PI/4.4;
  private double _elevation = Math.PI/6.6;

  private void updateView() {
    World world = getWorld();
    if (world!=null) {
      updateTransforms(world);
    }
    repaint();
  }
}
