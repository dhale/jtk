/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;
import static java.lang.Math.*;

import edu.mines.jtk.opengl.*;
import static edu.mines.jtk.opengl.Gl.*;
import static edu.mines.jtk.opengl.Glu.*;

/**
 * A view of a world, as if in orbit around that world.
 * The view camera is always aimed towards the world, from a point outside 
 * the world's sphere. The camera's up axis is parallel to lines of constant 
 * longitude.
 * <p>
 * The view camera has both azimuth and elevation angles. Imagine a line 
 * from the center of the world's sphere to the camera. The point where 
 * that line intersects the sphere has a latitude and longitude. The 
 * azimuth angle is the longitude, positive for degrees East, negative 
 * for degrees West. The elevation angle is the latitude, positive for 
 * degrees North, negative for degrees South.
 * <p>
 * An orbit view supports both perspective and orthographic projections.
 * For perspective projections, the field of view is computed by assuming 
 * that the distance from the eye to the default screen is approximately 
 * equal to the size of that screen.
 * <p>
 * An orbit view is designed to draw on a single view canvas, and the
 * distance from the camera to the center of the world's sphere is 
 * computed so that the entire world is visible inside that canvas.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.29
 */
public class OrbitView extends View {

  /**
   * Perspective or orthographic projection.
   */
  public enum Projection {
    PERSPECTIVE, ORTHOGRAPHIC
  }

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
    updateTransforms();
  }

  /**
   * Sets the world sphere used to parameterize this view.
   * If null (the default), the world's bounding sphere is used.
   * @param worldSphere the world sphere; null, if none.
   */
  public void setWorldSphere(BoundingSphere worldSphere) {
    _worldSphere = worldSphere;
    updateView();
  }

  /**
   * Gets the world sphere used to parameterize this view.
   * @return the world sphere; null, if none.
   */
  public BoundingSphere getWorldSphere() {
    return _worldSphere.clone();
  }

  /**
   * Sets the projection for this view.
   * @param projection the projection.
   */
  public void setProjection(Projection projection) {
    if (_projection==projection)
      return;
    _projection = projection;
    updateView();
  }

  /**
   * Gets the projection for this view.
   * @return the projection.
   */
  public Projection getProjection() {
    return _projection;
  }

  /**
   * Sets the azimuth for this view.
   * @param azimuth the azimuth.
   */
  public void setAzimuth(double azimuth) {
    if (_azimuth==azimuth)
      return;
    _azimuth = azimuth;
    updateView();
  }

  /**
   * Gets the azimuth for this view.
   * @return the azimuth.
   */
  public double getAzimuth() {
    return _azimuth;
  }

  /**
   * Sets the elevation for this view.
   * @param elevation the elevation.
   */
  public void setElevation(double elevation) {
    if (_elevation==elevation)
      return;
    _elevation = elevation;
    updateView();
  }

  /**
   * Gets the elevation for this view.
   * @return the elevation.
   */
  public double getElevation() {
    return _elevation;
  }

  /**
   * Sets the azimuth and elevation for this view.
   * @param azimuth the azimuth.
   * @param elevation the elevation.
   */
  public void setAzimuthAndElevation(double azimuth, double elevation) {
    if (_azimuth==azimuth && _elevation==elevation)
      return;
    _azimuth = azimuth;
    _elevation = elevation;
    updateView();
  }

  /**
   * Sets the scale for this view.
   * @param scale the scale.
   */
  public void setScale(double scale) {
    if (_scale==scale)
      return;
    _scale = scale;
    updateView();
  }

  /**
   * Gets the scale for this view.
   * @return the scale.
   */
  public double getScale() {
    return _scale;
  }


  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Updates transforms for a canvas on which this view draws.
   */
  protected void updateTransforms(ViewCanvas canvas) {

    // Canvas width and height, in pixels.
    int w = canvas.getWidth();
    int h = canvas.getHeight();
    if (w==0 || h==0)
      return;

    // Screen size, in pixels.
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double xs = screenSize.width;
    double ys = screenSize.height;
    double ss = sqrt(xs*xs+ys*ys);

    // Cube to pixel.
    Matrix44 cubeToPixel = Matrix44.identity();
    cubeToPixel.timesEquals(Matrix44.translate(0.5*w,0.5*h,0));
    cubeToPixel.timesEquals(Matrix44.scale(0.5*w,0.5*h,1.0));
    canvas.setCubeToPixel(cubeToPixel);

    // View to cube.
    double maxscale = 3.0; // clipping occurs after this much scaling
    Matrix44 viewToCube;
    double distance;
    if (_projection==Projection.PERSPECTIVE) {
      double r = 1.0; // normalized radius of world sphere
      double e = ss; // distance eye-to-screen (in pixels, approximate)
      double m = min(w,h); // minimum of viewport width and height
      double a = 2*atan(m/(2*e)); // angle subtended by sphere
      double d = r/sin(a/2); // distance from eye to center of sphere
      double fovy = 2*atan(h/(2*e))*180/PI;
      double aspect = (double)w/(double)h;
      double znear = max(d-maxscale*r,0.1);
      double zfar  = max(d+maxscale*r,100*znear);
      viewToCube = Matrix44.perspective(fovy,aspect,znear,zfar);
      distance = d;
    } else {
      double r = 1.0; // normalized radius of world sphere
      double m = min(w,h); // minimum of viewport width and height
      double d = maxscale*r; // distance from eye to center of sphere
      double right = w/m;
      double left = -right;
      double top = h/m;
      double bottom = -top;
      double znear = 0.0;
      double zfar = 2.0*maxscale*r;
      viewToCube = Matrix44.ortho(left,right,bottom,top,znear,zfar);
      distance = d;
    }
    canvas.setViewToCube(viewToCube);

    // World to view.
    Matrix44 worldToView = Matrix44.identity();
    World world = getWorld();
    if (world!=null) {
      BoundingSphere ws = _worldSphere;
      if (ws==null)
        ws = world.getBoundingSphere();
      Point3 c = (!ws.isEmpty())?ws.getCenter():new Point3();
      double r = (!ws.isEmpty())?ws.getRadius():1.0;
      double s = (r>0.0)?_scale/r:_scale;
      worldToView.timesEquals(Matrix44.translate(0,0,-distance));
      worldToView.timesEquals(Matrix44.rotateX(_elevation));
      worldToView.timesEquals(Matrix44.rotateY(-_azimuth));
      worldToView.timesEquals(Matrix44.scale(s,s,s));
      worldToView.timesEquals(Matrix44.translate(-c.x,-c.y,-c.z));
    }
    setWorldToView(worldToView);
  }

  /**
   * Draws this view on the specified canvas.
   * @param canvas the canvas.
   */
  protected void draw(ViewCanvas canvas) {

    // Clear.
    glClearColor(0.0f,0.0f,0.0f,0.0f);
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

    // Other stuff.
    glEnable(GL_NORMALIZE);
    glEnable(GL_DEPTH_TEST);

    // Our world.
    World world = getWorld();
    if (world==null)
      return;

    // Viewport (cube-to-pixel) transform.
    int w = canvas.getWidth();
    int h = canvas.getHeight();
    glViewport(0,0,w,h);

    // Projection (view-to-cube) transform.
    Matrix44 viewToCube = canvas.getViewToCube();
    glMatrixMode(GL_PROJECTION);
    glLoadMatrixd(viewToCube.m);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    // Light.
    // TODO: make lighting customizable.
    float[] lightPosition = {-0.1f,-0.1f,1.0f,0.0f};
    glLightfv(GL_LIGHT0,GL_POSITION,lightPosition);
    glEnable(GL_LIGHT0);

    // View (world-to-view) transform.
    Matrix44 worldToView = this.getWorldToView();
    glLoadMatrixd(worldToView.m);

    // Cull and draw the world.
    CullContext cc = new CullContext(canvas);
    world.cullApply(cc);
    DrawList dl = cc.getDrawList();
    DrawContext dc = new DrawContext(canvas);
    dl.draw(dc);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _azimuth = 40.0;
  private double _elevation = 25.0;
  private double _scale = 1.0;
  private Projection _projection = Projection.PERSPECTIVE;
  private BoundingSphere _worldSphere = null;

  private void updateView() {
    updateTransforms();
    repaint();
  }
}
