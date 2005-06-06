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

import edu.mines.jtk.gui.*;

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
    //setIcon(loadIcon(OrbitViewMode.class,"resources/View24.gif"));
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
      } else {
        component.removeMouseListener(_ml);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ViewCanvas _canvas; // the canvas
  private OrbitView _view; // the view
  private int _xmouse; // mouse x coordinate
  private int _ymouse; // mouse y coordinate
  private double _scale; // view scale factor at beginning of scale
  private double _azimuth; // view azimuth at beginning of rotate
  private double _elevation; // view elevation at beginning of rotate
  private Vector3 _translate; // view translate at beginning of translate
  private Point3 _translateP; // used in translate (see below)
  private Matrix44 _translateM; // used in translate (see below)
  private double _translateZ; // used in translate (see below)
  private boolean _rotating;
  private boolean _scaling;
  private boolean _translating;

  private MouseListener _ml = new MouseAdapter() {;
    public void mousePressed(MouseEvent e) {
      if (e.isControlDown()) {
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
        endScale(e);
        _scaling = false;
      } else if (_translating) {
        endTranslate(e);
        _translating = false;
      } else if (_rotating) {
        endRotate(e);
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

  private void endScale(MouseEvent e) {
    _canvas.removeMouseMotionListener(_mml);
  }

  private void beginTranslate(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    _canvas = (ViewCanvas)e.getSource();
    _canvas.addMouseMotionListener(_mml);
    _view = (OrbitView)_canvas.getView();

    // Compute the cube-to-unit-sphere transform.
    Matrix44 viewToCube = _canvas.getViewToCube();
    Matrix44 unitSphereToView = _view.getUnitSphereToView();
    Matrix44 unitSphereToCube = viewToCube.times(unitSphereToView);
    Matrix44 cubeToUnitSphere = unitSphereToCube.inverse();

    // Compute 3-D cube coordinates. If the cube z coordinate is 1.0,
    // then the mouse is not over any object that painted the depth
    // buffer, and we set the cube z coordinate to zero, which is in
    // the middle of the unit cube.
    Point3 pc = _canvas.transformPixelToCube(x,y);
    if (pc.z==1.0)
      pc.z = 0.0;

    // Remember everything we need during translate below.
    _translate = _view.getTranslate();
    _translateZ = pc.z;
    _translateM = Matrix44.translate(_translate).times(cubeToUnitSphere);
    _translateP = _translateM.times(pc);
  }

  private void duringTranslate(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    Point3 pc = _canvas.transformPixelToCube(x,y,_translateZ);
    Vector3 t = _translate.plus(_translateM.times(pc).minus(_translateP));
    _view.setTranslate(t);
  }

  private void endTranslate(MouseEvent e) {
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

  private void endRotate(MouseEvent e) {
    _canvas.removeMouseMotionListener(_mml);
  }
}

