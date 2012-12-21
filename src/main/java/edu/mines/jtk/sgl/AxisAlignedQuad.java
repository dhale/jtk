/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.event.MouseEvent;

/**
 * An axis-aligned quad has one frame that contains one or more panels.
 * The quad is constrained to lie within a box specified by two corner
 * points.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.06.01
 */
public class AxisAlignedQuad extends Group implements Selectable, Dragable {

  /**
   * Constructs an axis-aligned quad with specified axis and corner points.
   * @param axis the axis orthogonal to the plane of this quad.
   * @param qa a corner point.
   * @param qb a corner point.
   */
  public AxisAlignedQuad(Axis axis, Point3 qa, Point3 qb) {
    _frame = new AxisAlignedFrame(axis,qa,qb);
    addChild(_frame);
  }

  /**
   * Gets the frame for this quad.
   * @return the frame.
   */
  public AxisAlignedFrame getFrame() {
    return _frame;
  }

  ///////////////////////////////////////////////////////////////////////////
  // dragging

  public void dragBegin(DragContext dc) {
    _dragger = new Dragger();
    _dragger.dragBegin(dc);
  }

  public void drag(DragContext dc) {
    _dragger.drag(dc);
  }

  public void dragEnd(DragContext dc) {
    _dragger.dragEnd(dc);
    _dragger = null;
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void selectedChanged() {
    if (isSelected()) {
      updateHandles();
      showHandles();
    } else {
      hideHandles();
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private AxisAlignedFrame _frame;
  private boolean _handlesVisible;
  private Handle[] _h = new Handle[4];
  private Dragger _dragger;

  ///////////////////////////////////////////////////////////////////////////
  // dragger for quad

  private class Dragger implements Dragable {
    public void dragBegin(DragContext dc) {
      Point3 origin = dc.getPointWorld();
      Vector3 normal = null;
      Axis axis = _frame.getAxis();
      if (axis==Axis.X) {
        normal = new Vector3(1.0,0.0,0.0);
      } else if (axis==Axis.Y) {
        normal = new Vector3(0.0,1.0,0.0);
      } else if (axis==Axis.Z) {
        normal = new Vector3(0.0,0.0,1.0);
      }
      Plane plane = new Plane(origin,normal);
      MouseEvent event = dc.getMouseEvent();
      Matrix44 worldToPixel = dc.getWorldToPixel();
      if (event.isControlDown() || event.isAltDown()) { // Alt/Option for Mac
        _mouseConstrained = new MouseOnPlane(event,origin,plane,worldToPixel);
      } else {
        _mouseConstrained = new MouseOnLine(event,origin,normal,worldToPixel);
      }
      _origin = origin;
      _qa = _frame.getCornerMin();
      _qb = _frame.getCornerMax();
    }
    public void drag(DragContext dc) {
      assert _mouseConstrained!=null:"mouseConstrained!=null";
      Point3 point = _mouseConstrained.getPoint(dc.getMouseEvent());
      //BoxConstraint constraint = _frame.getBoxConstraint();
      //if (constraint!=null && !constraint.containsPoint(point))
      //  return;
      Vector3 vector = point.minus(_origin);
      Point3 qa = _qa.plus(vector);
      Point3 qb = _qb.plus(vector);
      _frame.setCorners(qa,qb);
      updateHandles();
    }
    public void dragEnd(DragContext dc) {
      _mouseConstrained = null;
    }
    private MouseConstrained _mouseConstrained; 
    private Point3 _origin;
    private Point3 _qa,_qb;
  }

  ///////////////////////////////////////////////////////////////////////////
  // handles for resizing

  private class Handle extends HandleBox implements Dragable {
    Handle(Point3 p) {
      super(p);
    }
    public void dragBegin(DragContext dc) {
      Point3 p = dc.getPointWorld();
      Vector3 n = null;
      Axis axis = _frame.getAxis();
      if (axis==Axis.X) {
        n = new Vector3(1.0,0.0,0.0);
      } else if (axis==Axis.Y) {
        n = new Vector3(0.0,1.0,0.0);
      } else if (axis==Axis.Z) {
        n = new Vector3(0.0,0.0,1.0);
      }
      MouseEvent event = dc.getMouseEvent();
      Point3 origin = getLocation();
      Plane plane = new Plane(p,n);
      Matrix44 worldToPixel = dc.getWorldToPixel();
      _mouseOnPlane = new MouseOnPlane(event,origin,plane,worldToPixel);
    }
    public void drag(DragContext dc) {
      Point3 qnew = _mouseOnPlane.getPoint(dc.getMouseEvent());
      //BoxConstraint constraint = _frame.getBoxConstraint();
      //if (constraint!=null && !constraint.containsPoint(qnew))
      //  return;
      if (this==_h[0]) {
        _frame.setCorners(qnew,_frame.getCorner(3));
      } else if (this==_h[1]) {
        _frame.setCorners(qnew,_frame.getCorner(2));
      } else if (this==_h[2]) {
        _frame.setCorners(qnew,_frame.getCorner(1));
      } else if (this==_h[3]) {
        _frame.setCorners(qnew,_frame.getCorner(0));
      }
      updateHandles();
    }
    public void dragEnd(DragContext dc) {
      _mouseOnPlane = null;
    }
    private MouseOnPlane _mouseOnPlane; // not null if dragging
  }

  private void updateHandles() {
    Point3 q0 = _frame.getCorner(0);
    Point3 q1 = _frame.getCorner(1);
    Point3 q2 = _frame.getCorner(2);
    Point3 q3 = _frame.getCorner(3);
    if (_h[0]==null) {
      _h[0] = new Handle(q0);
      _h[1] = new Handle(q1);
      _h[2] = new Handle(q2);
      _h[3] = new Handle(q3);
    } else {
      _h[0].setLocation(q0);
      _h[1].setLocation(q1);
      _h[2].setLocation(q2);
      _h[3].setLocation(q3);
    }
  }

  private void showHandles() {
    if (!_handlesVisible) {
      _handlesVisible = true;
      addChild(_h[0]);
      addChild(_h[1]);
      addChild(_h[2]);
      addChild(_h[3]);
      dirtyDraw();
    }
  }

  private void hideHandles() {
    if (_handlesVisible) {
      _handlesVisible = false;
      removeChild(_h[0]);
      removeChild(_h[1]);
      removeChild(_h[2]);
      removeChild(_h[3]);
      dirtyDraw();
    }
  }
}
