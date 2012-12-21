/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;
import static java.lang.Math.*;

import static edu.mines.jtk.ogl.Gl.*;
import edu.mines.jtk.util.Stopwatch;

/**
 * A view of a world, as if in orbit around that world.
 * By default, the view camera is aimed towards the world, from a point 
 * outside the world's sphere. The camera's up axis is always aligned 
 * with lines of constant longitude. An orbit view is designed to draw
 * on a single view canvas.
 * <p>
 * All views maintain a world-to-view transform. In an orbit view, that 
 * world-to-view transform is comprised of a world-to-unit-sphere 
 * transform and a unit-sphere-to-view transform.
 * <p>
 * The world-to-unit-sphere transform centers and normalizes the world. 
 * A world drawn by an orbit view has a world sphere that, by default, is 
 * the bounding sphere of the world when first viewed. The world-to-unit-
 * sphere transform first translates the world sphere's center to the origin, 
 * and then scales the world sphere to have unit radius. The purpose of this 
 * first transform is to make other orbit view parameters independent 
 * of world coordinates. To modify the world-to-unit-sphere transform, 
 * set the world sphere.
 * <p>
 * The second unit-sphere-to-view transform applies a translate, scale, 
 * and rotate to the unit sphere, and then applies a final translate 
 * down the z-axis to push the transformed sphere into the view frustum.
 * The orbit view applies the first translate, scale, and rotate in that
 * order, so that the scale and rotate occurs about the center of
 * the view.
 * <p>
 * The rotate part of the unit-sphere-to-view transform is comprised of
 * two rotations, because an orbit view camera has both azimuth and 
 * elevation angles. Imagine a line from the center of the unit sphere 
 * to the camera. The point where that line intersects the sphere has a 
 * latitude and longitude. The azimuth angle is the longitude, positive 
 * for degrees East, negative for degrees West. The elevation angle is 
 * the latitude, positive for degrees North, negative for degrees South.
 * <p>
 * An orbit view supports both perspective and orthographic projections.
 * For perspective projections, the field of view is computed by assuming 
 * that the distance from the eye to the default screen is approximately 
 * equal to the size of that screen.
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
    init();
  }

  /**
   * Constructs an orbit view of the specified world.
   * @param world the world.
   */
  public OrbitView(World world) {
    super(world);
    init();
    updateTransforms();
  }

  /**
   * Resets this view to its state when constructed.
   */
  public void reset() {
    init();
    updateView();
  }

  /**
   * Sets the world sphere used to parameterize this view.
   * If null (the default), this view uses the bounding sphere of the
   * the world when first viewed.
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
    return new BoundingSphere(_worldSphere);
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
   * Sets the scale factor for this view.
   * @param scale the scale factor.
   */
  public void setScale(double scale) {
    if (_scale==scale)
      return;
    _scale = scale;
    updateView();
  }

  /**
   * Gets the scale factor for this view.
   * @return the scale factor.
   */
  public double getScale() {
    return _scale;
  }

  /**
   * Sets the translate vector for this view.
   * @param translate the translate vector.
   */
  public void setTranslate(Vector3 translate) {
    if (_translate.equals(translate))
      return;
    _translate = new Vector3(translate);
    updateView();
  }

  /**
   * Gets the translate vector for this view.
   * @return the translate vector.
   */
  public Vector3 getTranslate() {
    return new Vector3(_translate);
  }

  /**
   * Gets the world-to-unit-sphere transform for this view.
   * @return the world-to-unit-sphere transform.
   */
  public Matrix44 getWorldToUnitSphere() {
    return new Matrix44(_worldToUnitSphere);
  }

  /**
   * Gets the unit-sphere-to-view transform for this view.
   * @return the unit-sphere-to-view transform.
   */
  public Matrix44 getUnitSphereToView() {
    return new Matrix44(_unitSphereToView);
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
    cubeToPixel.timesEquals(Matrix44.translate(0.5*w,0.5*h,0.5));
    cubeToPixel.timesEquals(Matrix44.scale(0.5*w,-0.5*h,0.5));
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
      double d = r/sin(a/2); // distance from eye to center of frustum
      double fovy = 2*atan(h/(2*e))*180.0/PI;
      double aspect = (double)w/(double)h;
      double znear = max(d-maxscale*r,0.1);
      double zfar  = max(d+maxscale*r,100.0*znear);
      viewToCube = Matrix44.perspective(fovy,aspect,znear,zfar);
      distance = d;
    } else {
      double r = 1.0; // normalized radius of world sphere
      double m = min(w,h); // minimum of viewport width and height
      double d = maxscale*r; // distance from eye to center of frustum
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

    // Unit sphere to view.
    _unitSphereToView = Matrix44.identity();
    _unitSphereToView.timesEquals(Matrix44.translate(0,0,-distance));
    _unitSphereToView.timesEquals(Matrix44.rotateX(_elevation));
    _unitSphereToView.timesEquals(Matrix44.rotateY(-_azimuth));
    _unitSphereToView.timesEquals(Matrix44.scale(_scale,_scale,_scale));
    _unitSphereToView.timesEquals(Matrix44.translate(_translate));

    //  World to unit sphere.
    Tuple3 as = getAxesScale();
    AxesOrientation ao = getAxesOrientation();
    _worldToUnitSphere = Matrix44.identity();
    World world = getWorld();
    if (world!=null) {
      if (_worldSphere==null)
        _worldSphere = world.getBoundingSphere(true);
      BoundingSphere ws = _worldSphere;
      Point3 c = (!ws.isEmpty())?ws.getCenter():new Point3();
      double tx = -c.x;
      double ty = -c.y;
      double tz = -c.z;
      double r = (!ws.isEmpty())?ws.getRadius():1.0;
      double s = (r>0.0)?1.0/r:1.0;
      double sx = s*as.x;
      double sy = s*as.y;
      double sz = s*as.z;
      if (ao==AxesOrientation.XRIGHT_YUP_ZOUT) {
        _worldToUnitSphere.timesEquals(Matrix44.identity());
      } else if (ao==AxesOrientation.XRIGHT_YOUT_ZDOWN) {
        _worldToUnitSphere.timesEquals(Matrix44.rotateX(90.0));
      } else if (ao==AxesOrientation.XRIGHT_YIN_ZDOWN) {
        _worldToUnitSphere.timesEquals(Matrix44.rotateX(90.0));
        sy = -sy;
      } else if (ao==AxesOrientation.XOUT_YRIGHT_ZUP) {
        _worldToUnitSphere.timesEquals(Matrix44.rotateY(-90.0));
        _worldToUnitSphere.timesEquals(Matrix44.rotateX(-90.0));
      } else if (ao==AxesOrientation.XDOWN_YRIGHT_ZOUT) {
        _worldToUnitSphere.timesEquals(Matrix44.rotateZ(-90.0));
      } else if (ao==AxesOrientation.XUP_YLEFT_ZOUT) {
        _worldToUnitSphere.timesEquals(Matrix44.rotateZ(90.0));
      } else if (ao==AxesOrientation.XUP_YRIGHT_ZOUT) {
        _worldToUnitSphere.timesEquals(Matrix44.rotateZ(90.0));
        sy = -sy;
      }

      _worldToUnitSphere.timesEquals(Matrix44.scale(sx,sy,sz));
      _worldToUnitSphere.timesEquals(Matrix44.translate(tx,ty,tz));
    }

    // World to view.
    setWorldToView(_unitSphereToView.times(_worldToUnitSphere));
  }

  /**
   * Draws this view on the specified canvas.
   * @param canvas the canvas.
   */
  protected void draw(ViewCanvas canvas) {

    // Clear.
    Color c = canvas.getBackground();
    float r = c.getRed()/255.0f;
    float g = c.getGreen()/255.0f;
    float b = c.getBlue()/255.0f;
    glClearColor(r,g,b,0.0f);
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

    // Anti-aliasing for points and lines.
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    glEnable(GL_LINE_SMOOTH);
    glEnable(GL_POINT_SMOOTH);
    glHint(GL_LINE_SMOOTH_HINT,GL_NICEST);
    glHint(GL_POINT_SMOOTH_HINT,GL_NICEST);

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
    glLoadMatrixd(viewToCube.m,0);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    // Light.
    // TODO: make lighting customizable.
    float[] lightPosition = {-0.1f,-0.1f,1.0f,0.0f};
    glLightfv(GL_LIGHT0,GL_POSITION,lightPosition,0);
    glEnable(GL_LIGHT0);

    // View (world-to-view) transform.
    Matrix44 worldToView = this.getWorldToView();
    glLoadMatrixd(worldToView.m,0);

    // Cull and draw the world.
    CullContext cc = new CullContext(canvas);
    world.cullApply(cc);
    DrawList dl = cc.getDrawList();
    DrawContext dc = new DrawContext(canvas);
    dl.draw(dc);

    // Statistics:
    ++_ndraw;
    if (_stopwatch.time()>2.0) {
      _stopwatch.stop();
      //int rate = (int)(_ndraw/_stopwatch.time());
      //trace("OrbitView: draw frame/s = "+rate);
      _ndraw = 0;
      _stopwatch.restart();
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _scale;
  private Vector3 _translate;
  private double _azimuth;
  private double _elevation;
  private Projection _projection = Projection.PERSPECTIVE;
  private BoundingSphere _worldSphere = null;
  private Matrix44 _worldToUnitSphere;
  private Matrix44 _unitSphereToView;
  private Stopwatch _stopwatch;
  private int _ndraw;

  private void init() {
    _scale = 1.0;
    _translate = new Vector3(0.0,0.0,0.0);
    _azimuth = 40.0;
    _elevation = 25.0;
    _projection = Projection.PERSPECTIVE;
    _ndraw = 0;
    _stopwatch = new Stopwatch();
    _stopwatch.start();
  }

  private void updateView() {
    updateTransforms();
    repaint();
  }

  private static void trace(String s) {
    System.out.println(s);
  }
}
