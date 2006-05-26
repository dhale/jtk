/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;
import java.awt.event.*;
import java.nio.*;
import java.util.*;
import javax.swing.*;
import static java.lang.Math.*;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.gui.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.opengl.Gl.*;

/**
 * An axis-aligned quad on which one or more panels can be rendered.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.05.25
 */
public class AxisAlignedQuad extends Group implements Selectable {

  /**
   * The coordinate that is constant for this quad. An axis-aligned quad 
   * lies in a plane of constant X, Y, or Z.
   */
  public enum Constant {
    X,Y,Z
  };

  /**
   * Constructs an axis-aligned quad with specified axis and corner points.
   * Sets the coordinate for the specified constant axis to the average of 
   * the corresponding corner point coordinates.
   * @param axis the coordinate that is constant for this quad.
   * @param qa a corner point.
   * @param qb a corner point.
   */
  public AxisAlignedQuad(Constant axis, Point3 qa, Point3 qb) {
    _axis = axis;
    setCorners(qa,qb);
    _frame = new Frame();
    addChild(_frame);
  }

  /**
   * Sets the corner points of this quad. Sets the coordinate for the 
   * constant axis to the average of the corresponding corner point 
   * coordinates.
   * @param qa a corner point.
   * @param qb a corner point.
   */
  public void setCorners(Point3 qa, Point3 qb) {
    Point3 qmin = new Point3(min(qa.x,qb.x),min(qa.y,qb.y),min(qa.z,qb.z));
    Point3 qmax = new Point3(max(qa.x,qb.x),max(qa.y,qb.y),max(qa.z,qb.z));
    if (_axis==Constant.X) {
      double x = 0.5*(qmin.x+qmax.x);
      _q00 = new Point3(x,qmin.y,qmin.z);
      _q10 = new Point3(x,qmax.y,qmin.z);
      _q01 = new Point3(x,qmin.y,qmax.z);
      _q11 = new Point3(x,qmax.y,qmax.z);
    } else if (_axis==Constant.Y) {
      double y = 0.5*(qmin.y+qmax.y);
      _q00 = new Point3(qmin.x,y,qmin.z);
      _q10 = new Point3(qmin.x,y,qmax.z);
      _q01 = new Point3(qmax.x,y,qmin.z);
      _q11 = new Point3(qmax.x,y,qmax.z);
    } else {
      double z = 0.5*(qmin.z+qmax.z);
      _q00 = new Point3(qmin.x,qmin.y,z);
      _q10 = new Point3(qmax.x,qmin.y,z);
      _q01 = new Point3(qmin.x,qmax.y,z);
      _q11 = new Point3(qmax.x,qmax.y,z);
    }
    updateHandles();
    dirtyBoundingSphere();
    dirtyDraw();
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void selectedChanged() {
    if (isSelected()) {
      showHandles();
    } else {
      hideHandles();
    }
    dirtyDraw();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Constant _axis;
  private Point3 _q00,_q10,_q01,_q11;
  private Handle _h00,_h10,_h01,_h11;
  private boolean _handlesVisible;
  private Frame _frame;

  private class Frame extends Node {

    protected BoundingSphere computeBoundingSphere(boolean finite) {
      Point3 c = _q00.affine(0.5,_q10).affine(0.5,_q01.affine(0.5,_q11));
      double r = _q00.minus(c).length();
      return new BoundingSphere(c,r);
    }

    protected void draw(DrawContext dc) {
      glColor3f(0.0f,0.0f,1.0f);
      glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
      glBegin(GL_QUADS); {
        glVertex3d(_q00.x,_q00.y,_q00.z);
        glVertex3d(_q10.x,_q10.y,_q10.z);
        glVertex3d(_q11.x,_q11.y,_q11.z);
        glVertex3d(_q01.x,_q01.y,_q01.z);
      } glEnd();
      glFlush();
    }

    protected void pick(PickContext pc) {
      Segment ps = pc.getPickSegment();
      Point3 p = ps.intersectWithTriangle(
        _q00.x,_q00.y,_q00.z,
        _q10.x,_q10.y,_q10.z,
        _q11.x,_q11.y,_q11.z);
      Point3 q = ps.intersectWithTriangle(
        _q00.x,_q00.y,_q00.z,
        _q11.x,_q11.y,_q11.z,
        _q01.x,_q01.y,_q01.z);
      if (p!=null)
        pc.addResult(p);
      if (q!=null)
        pc.addResult(q);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // handles

  private class Handle extends HandleBox implements Dragable {
    Handle(Point3 p) {
      super(p);
    }

    public void dragBegin(DragContext dc) {
      Check.state(_mouseOnPlane==null,"not dragging");
      Point3 p = dc.getPointWorld();
      Vector3 n = null;
      if (_axis==Constant.X) {
        n = new Vector3(1.0,0.0,0.0);
      } else if (_axis==Constant.Y) {
        n = new Vector3(0.0,1.0,0.0);
      } else if (_axis==Constant.Z) {
        n = new Vector3(0.0,0.0,1.0);
      }
      MouseEvent event = dc.getMouseEvent();
      Point3 origin = getLocation();
      Plane plane = new Plane(p,n);
      Matrix44 worldToPixel = dc.getWorldToPixel();
      _mouseOnPlane = new MouseOnPlane(event,origin,plane,worldToPixel);
    }

    public void drag(DragContext dc) {
      Check.state(_mouseOnPlane!=null,"dragging");
      Point3 qnew = _mouseOnPlane.getPoint(dc.getMouseEvent());
      if (this==_h00) {
        setCorners(qnew,_q11);
      } else if (this==_h10) {
        setCorners(qnew,_q01);
      } else if (this==_h01) {
        setCorners(qnew,_q10);
      } else if (this==_h11) {
        setCorners(qnew,_q00);
      }
    }

    public void dragEnd(DragContext dc) {
      _mouseOnPlane = null;
    }

    private MouseOnPlane _mouseOnPlane;
  }

  private void updateHandles() {
    if (_h00==null) {
      _h00 = new Handle(_q00);
      _h10 = new Handle(_q10);
      _h01 = new Handle(_q01);
      _h11 = new Handle(_q11);
    } else {
      _h00.setLocation(_q00);
      _h10.setLocation(_q10);
      _h01.setLocation(_q01);
      _h11.setLocation(_q11);
    }
  }

  private void showHandles() {
    if (!_handlesVisible) {
      addChild(_h00);
      addChild(_h10);
      addChild(_h01);
      addChild(_h11);
      _handlesVisible = true;
    }
  }

  private void hideHandles() {
    if (_handlesVisible) {
      removeChild(_h00);
      removeChild(_h10);
      removeChild(_h01);
      removeChild(_h11);
      _handlesVisible = false;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // testing

  private static final int SIZE = 600;
  public static void main(String[] args) {
    AxisAlignedQuad.Constant axis = AxisAlignedQuad.Constant.Y;
    Point3 qmin = new Point3(0,0,0);
    Point3 qmax = new Point3(1,1,1);
    AxisAlignedQuad aaq = new AxisAlignedQuad(axis,qmin,qmax);
    World world = new World();
    world.addChild(aaq);

    OrbitView view = new OrbitView(world);
    //view.setProjection(OrbitView.Projection.ORTHOGRAPHIC);
    //view.setAzimuthAndElevation(90.0,0.0);
    //view.setScale(5.0);
    //view.setWorldSphere(new BoundingSphere(0.5,0.5,0.5,4));
    view.setAxesOrientation(View.AxesOrientation.XRIGHT_YOUT_ZDOWN);
    //view.setAxesScale(1.0,2.0,3.0);
    ViewCanvas canvas = new ViewCanvas(view);
    canvas.setView(view);

    ModeManager mm = new ModeManager();
    mm.add(canvas);
    OrbitViewMode ovm = new OrbitViewMode(mm);
    SelectDragMode sdm = new SelectDragMode(mm);

    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic('F');
    Action exitAction = new AbstractAction("Exit") {
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    };
    JMenuItem exitItem = fileMenu.add(exitAction);
    exitItem.setMnemonic('x');

    JMenu modeMenu = new JMenu("Mode");
    modeMenu.setMnemonic('M');
    JMenuItem ovmItem = new ModeMenuItem(ovm);
    modeMenu.add(ovmItem);
    JMenuItem sdmItem = new ModeMenuItem(sdm);
    modeMenu.add(sdmItem);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(modeMenu);

    JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
    toolBar.setRollover(true);
    JToggleButton ovmButton = new ModeToggleButton(ovm);
    toolBar.add(ovmButton);
    JToggleButton sdmButton = new ModeToggleButton(sdm);
    toolBar.add(sdmButton);

    ovm.setActive(true);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(SIZE,SIZE));
    frame.add(canvas,BorderLayout.CENTER);
    frame.add(toolBar,BorderLayout.WEST);
    frame.setJMenuBar(menuBar);
    frame.setVisible(true);
  }
}
