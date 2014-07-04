/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.mines.jtk.awt.Mode;
import edu.mines.jtk.awt.ModeManager;

/**
 * A mode for manipulating an orbit view.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.02
 */
public class OrbitViewMode extends Mode {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs an orbit view mode with specified manager.
   * @param modeManager the mode manager for this mode.
   */
  public OrbitViewMode(ModeManager modeManager) {
    super(modeManager);
    setName("View");
    Class<OrbitViewMode> cls = OrbitViewMode.class;
    setIcon(loadIcon(cls,"ViewHandIcon16.png"));
    setCursor(loadCursor(cls,"ViewHandCursor16.png",3,2));
    setMnemonicKey(KeyEvent.VK_SPACE);
    setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0));
    setShortDescription("Manipulate view");
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected
  
  protected void setActive(Component component, boolean active) {
    if ((component instanceof ViewCanvas)) {
      if (active) {
        component.addMouseListener(_ml);
        component.addKeyListener(_kl);
      } else {
        component.removeMouseListener(_ml);
        component.removeKeyListener(_kl);
        _rotating = false;
        _scaling = false;
        _translating = false;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ViewCanvas _canvas; // the canvas
  private OrbitView _view; // the view
  private int _xmouse; // mouse x coordinate
  private int _ymouse; // mouse y coordinate
  private double _zmouse; // mouse z coordinate
  private double _scale; // view scale factor at beginning of scale
  private double _azimuth; // view azimuth at beginning of rotate
  private double _elevation; // view elevation at beginning of rotate
  private Vector3 _translate; // view translate at beginning of translate
  private Point3 _translateP; // used in translate (see below)
  private Matrix44 _translateM; // used in translate (see below)
  private boolean _rotating;
  private boolean _scaling;
  private boolean _translating;

  private KeyListener _kl = new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      ViewCanvas canvas = (ViewCanvas)e.getSource();
      OrbitView view = (OrbitView)canvas.getView();
      int kc = e.getKeyCode();

      // Home.
      if (kc==KeyEvent.VK_HOME) {
        view.reset();

      // End.
      } else if (kc==KeyEvent.VK_END) {
        float azimuth = (float)view.getAzimuth();
        float elevation = (float)view.getElevation();
        float scale = (float)view.getScale();
        Vector3 t = view.getTranslate();
        System.out.println("OrbitView: azimuth="+azimuth);
        System.out.println("           elevation="+elevation);
        System.out.println("           scale="+scale);
        System.out.println("           translate=("+t.x+","+t.y+","+t.z+")");

      // Z shrink.
      } else if (kc==KeyEvent.VK_PAGE_DOWN) {
        Tuple3 s = view.getAxesScale();
        view.setAxesScale(s.x,s.y,s.z/1.1);

      // Z stretch.
      } else if (kc==KeyEvent.VK_PAGE_UP) {
        Tuple3 s = view.getAxesScale();
        view.setAxesScale(s.x,s.y,s.z*1.1);

      // Projection.
      } else if (kc==KeyEvent.VK_INSERT) {
        OrbitView.Projection projection = view.getProjection();
        if (projection==OrbitView.Projection.ORTHOGRAPHIC) {
          projection = OrbitView.Projection.PERSPECTIVE;
        } else if (projection==OrbitView.Projection.PERSPECTIVE) {
          projection = OrbitView.Projection.ORTHOGRAPHIC;
        }
        view.setProjection(projection);
      
      } 
      
      // Scale.
      else if (e.isControlDown() || e.isAltDown()) { // Alt/Option for Mac
        double scale = view.getScale();
        if (kc==KeyEvent.VK_UP) {
          scale *= 0.9;
        } else if (kc==KeyEvent.VK_DOWN) {
          scale *= 1.1;
        }
        view.setScale(scale);
      }
      
      // Translate.
      else if (e.isShiftDown()) {
        Matrix44 viewToCube = _canvas.getViewToCube();
        Matrix44 unitSphereToView = _view.getUnitSphereToView();
        Matrix44 unitSphereToCube = viewToCube.times(unitSphereToView);
        Matrix44 cubeToUnitSphere = unitSphereToCube.inverse();
        Vector3 translate = view.getTranslate();
        Matrix44 m = Matrix44.translate(translate).times(cubeToUnitSphere);
        double xc = 0.0;
        double yc = 0.0;
        double zc = 0.0;
        Point3 c1 = new Point3(xc,yc,zc);
        if (kc==KeyEvent.VK_LEFT) {
          xc -= 0.05;
        } else if (kc==KeyEvent.VK_RIGHT) {
          xc += 0.05;
        } else if (kc==KeyEvent.VK_UP) {
          yc += 0.05;
        } else if (kc==KeyEvent.VK_DOWN) {
          yc -= 0.05;
        }
        Point3 c2 = new Point3(xc,yc,zc);
        translate.plusEquals(m.times(c2).minus(m.times(c1)));
        view.setTranslate(translate);

      } 
      
      // Rotate.
      else {
        double azimuth = view.getAzimuth();
        double elevation = view.getElevation();
        if (kc==KeyEvent.VK_LEFT) {
          azimuth += 5.0;
        } else if (kc==KeyEvent.VK_RIGHT) {
          azimuth -= 5.0;
        } else if (kc==KeyEvent.VK_UP) {
          elevation -= 5.0;
        } else if (kc==KeyEvent.VK_DOWN) {
          elevation += 5.0;
        }
        view.setAzimuthAndElevation(azimuth,elevation);
      }
    }
  };

  private MouseListener _ml = new MouseAdapter() {
    public void mousePressed(MouseEvent e) {
      if (e.isControlDown() || e.isAltDown()) { // Alt/Option for Mac
        beginScale(e);
        _scaling = true;
      } else if (e.isShiftDown()) {
        beginTranslate(e);
        _translating = true;
      } else {
        beginRotate(e);
        _rotating = true;
      }
    }
    public void mouseReleased(MouseEvent e) {
      if (_scaling) {
        endScale();
        _scaling = false;
      } else if (_translating) {
        endTranslate();
        _translating = false;
      } else if (_rotating) {
        endRotate();
        _rotating = false;
      }
    }
  };

  private MouseMotionListener _mml = new MouseMotionAdapter() {
    public void mouseDragged(MouseEvent e) {
      if (_scaling) {
        duringScale(e);
      } else if (_translating) {
        duringTranslate(e);
      } else if (_rotating) {
        duringRotate(e);
      }
    }
  };

  private void beginScale(MouseEvent e) {
    _ymouse = e.getY();
    _canvas = (ViewCanvas)e.getSource();
    _canvas.addMouseMotionListener(_mml);
    _view = (OrbitView)_canvas.getView();
    _scale = _view.getScale();
  }

  private void duringScale(MouseEvent e) {
    int h = _canvas.getHeight();
    int y = e.getY();
    int dy = y-_ymouse;
    double ds = 2.0*(double)dy/(double)h;
    _view.setScale(_scale*Math.pow(10.0,ds));
  }

  private void endScale() {
    _canvas.removeMouseMotionListener(_mml);
  }

  private void beginTranslate(MouseEvent e) {
    _xmouse = e.getX();
    _ymouse = e.getY();
    _canvas = (ViewCanvas)e.getSource();
    _canvas.addMouseMotionListener(_mml);
    _view = (OrbitView)_canvas.getView();

    // The mouse z coordinate, read from the depth buffer. The value 1.0 
    // corresponds to the far clipping plane, which indicates that the 
    // mouse was not pressed on any object rendered into the z-buffer.
    // In this case, we use the middle value pixel z value 0.5.
    _zmouse = _canvas.getPixelZ(_xmouse,_ymouse);
    if (_zmouse==1.0)
      _zmouse = 0.5;

    // Pixel-to-unit-sphere transform.
    Matrix44 cubeToPixel = _canvas.getCubeToPixel();
    Matrix44 viewToCube = _canvas.getViewToCube();
    Matrix44 viewToPixel = cubeToPixel.times(viewToCube);
    Matrix44 unitSphereToView = _view.getUnitSphereToView();
    Matrix44 unitSphereToPixel = viewToPixel.times(unitSphereToView);
    Matrix44 pixelToUnitSphere = unitSphereToPixel.inverse();

    // The current translate vector.
    _translate = _view.getTranslate();

    // The matrix inverse of the unit-sphere-to-pixel transform, but 
    // with the current translate part removed, because that is the 
    // part that we will change during translate.
    _translateM = Matrix44.translate(_translate).times(pixelToUnitSphere);

    // The transformed 3-D pixel (mouse) coordinates.
    _translateP = _translateM.times(new Point3(_xmouse,_ymouse,_zmouse));
  }

  private void duringTranslate(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    Point3 p = new Point3(x,y,_zmouse);
    Vector3 t = _translate.plus(_translateM.times(p).minus(_translateP));
    _view.setTranslate(t);
  }

  private void endTranslate() {
    _canvas.removeMouseMotionListener(_mml);
  }

  private void beginRotate(MouseEvent e) {
    _xmouse = e.getX();
    _ymouse = e.getY();
    _canvas = (ViewCanvas)e.getSource();
    _canvas.addMouseMotionListener(_mml);
    _view = (OrbitView)_canvas.getView();
    _azimuth = _view.getAzimuth();
    _elevation = _view.getElevation();
  }

  private void duringRotate(MouseEvent e) {
    int w = _canvas.getWidth();
    int h = _canvas.getHeight();
    int x = e.getX();
    int y = e.getY();
    int dx = x-_xmouse;
    int dy = y-_ymouse;
    double da = -360.0*(double)dx/(double)w;
    double de =  360.0*(double)dy/(double)h;
    _view.setAzimuthAndElevation(_azimuth+da,_elevation+de);
  }

  private void endRotate() {
    _canvas.removeMouseMotionListener(_mml);
  }
}

