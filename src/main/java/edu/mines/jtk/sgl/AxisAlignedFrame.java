/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static java.lang.Math.*;

import static edu.mines.jtk.ogl.Gl.*;
import edu.mines.jtk.util.Check;

/**
 * An axis-aligned frame is a group of axis-aligned panels. A frame's 
 * geometry is an axis-aligned quadrilateral defined by an axis and two 
 * corner points. The axis is perpendicular to the plane containing the 
 * quadrilateral. The corner points are a pair of opposite vertices of 
 * the quadrilateral. Each corner point has three (X,Y,Z) coordinate 
 * values. For the axis that is orthogonal to the plane of this frame, 
 * the corresponding coordinate values of the two corner points are equal.
 * <p>
 * By convention, axis-aligned panel children of axis-aligned frames
 * honor their parent frame's geometry when drawing, picking, etc. This
 * convention keeps multiple panel children consistent as their frame is
 * moved and resized.
 * <p>
 * Panels do not typically control the geometry of their frame, but
 * they may set a box constraint on the corner points of that frame. 
 * For example, a panel may constrain those corner points to lie on a 
 * sampling grid. Only one constraint, the last one set, is applied. A
 * frame does not attempt to reconcile inconsistent constraints.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.06.01
 */
public class AxisAlignedFrame extends Group {

  /**
   * Constructs a frame with specified axis and corner points.
   * @param axis the axis.
   * @param pa a corner point.
   * @param pb a corner point.
   */
  public AxisAlignedFrame(Axis axis, Point3 pa, Point3 pb) {
    _axis = axis;
    setCorners(pa,pb);
    addChild(new Wire());
  }

  /**
   * Gets the axis for this frame.
   * @return the axis.
   */
  public Axis getAxis() {
    return _axis;
  }

  /**
   * Gets the minimum corner point for this frame.
   * @return the minimum corner point.
   */
  public Point3 getCornerMin() {
    return new Point3(_p[0]);
  }

  /**
   * Gets the maximum corner point for this frame.
   * @return the maximum corner point.
   */
  public Point3 getCornerMax() {
    return new Point3(_p[3]);
  }

  /**
   * Gets the corner point with specified index for this frame.
   * @param index the index in [0,3].
   * @return the corner point.
   */
  public Point3 getCorner(int index) {
    return new Point3(_p[index]);
  }

  /**
   * Sets the corner points of (moves and/or resizes) this frame.
   * If this frame has a non-null box constraint, then this method
   * first applies the constraint before settig the corner points.
   * <p>
   * The specified corner points must be opposite vertices of the
   * quadrilateral that defines this frame. For the axis that is 
   * orthogonal to the plane of this frame, the corresponding (X, 
   * Y, or Z) coordinate values of the two points should be equal; 
   * this method moves this frame to the average of those two 
   * coordinate values.
   * @param pa a corner point
   * @param pb a corner point
   */
  public void setCorners(Point3 pa, Point3 pb) {
    Point3 pmin = new Point3(min(pa.x,pb.x),min(pa.y,pb.y),min(pa.z,pb.z));
    Point3 pmax = new Point3(max(pa.x,pb.x),max(pa.y,pb.y),max(pa.z,pb.z));
    if (_constraint!=null)
      _constraint.constrainBox(pmin,pmax);
    if (_axis==Axis.X) {
      double x = 0.5*(pmin.x+pmax.x);
      _p[0] = new Point3(x,pmin.y,pmin.z);
      _p[1] = new Point3(x,pmax.y,pmin.z);
      _p[2] = new Point3(x,pmin.y,pmax.z);
      _p[3] = new Point3(x,pmax.y,pmax.z);
    } else if (_axis==Axis.Y) {
      double y = 0.5*(pmin.y+pmax.y);
      _p[0] = new Point3(pmin.x,y,pmin.z);
      _p[1] = new Point3(pmin.x,y,pmax.z);
      _p[2] = new Point3(pmax.x,y,pmin.z);
      _p[3] = new Point3(pmax.x,y,pmax.z);
    } else {
      double z = 0.5*(pmin.z+pmax.z);
      _p[0] = new Point3(pmin.x,pmin.y,z);
      _p[1] = new Point3(pmax.x,pmin.y,z);
      _p[2] = new Point3(pmin.x,pmax.y,z);
      _p[3] = new Point3(pmax.x,pmax.y,z);
    }
    dirtyBoundingSphere();
    dirtyDraw();
  }


  /**
   * Gets the box constraint for this frame.
   * @return the box constraint; null, if none.
   */
  public BoxConstraint getBoxConstraint() {
    return _constraint;
  }

  /**
   * Sets the box constraint for this frame. Typically, only panels in a 
   * frame call this method, because the constraint often depends on what
   * is drawn by those panels. This frame uses only the last constraint set.
   * @param constraint the box constraint.
   */
  public void setBoxConstraint(BoxConstraint constraint) {
    _constraint = constraint;
    setCorners(_p[0],_p[3]);
  }

  public void addChild(Node node) {
    super.addChild(node);
    if (node instanceof AxisAlignedPanel) {
      AxisAlignedPanel panel = (AxisAlignedPanel)node;
      Check.state(panel.getFrame()==null,"frame of panel equals null");
      panel.setFrame(this);
    }
  }

  public void removeChild(Node node) {
    super.removeChild(node);
    if (node instanceof AxisAlignedPanel) {
      AxisAlignedPanel panel = (AxisAlignedPanel)node;
      Check.state(panel.getFrame()==this,"frame of panel equals this");
      panel.setFrame(null);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  // Called internally and by AxisAlignedPanel.
  BoundingSphere computeBoundingSphereOfFrame(boolean finite) {
    double dx = _p[3].x-_p[0].x;
    double dy = _p[3].y-_p[0].y;
    double dz = _p[3].z-_p[0].z;
    double r = sqrt(dx*dx+dy*dy+dz*dz);
    double x = 0.5*(_p[0].x+_p[3].x);
    double y = 0.5*(_p[0].y+_p[3].y);
    double z = 0.5*(_p[0].z+_p[3].z);
    Point3 c = new Point3(x,y,z);
    return new BoundingSphere(c,r);
  }

  // Called internally and by AxisAlignedPanel.
  void pickOnFrame(PickContext pc) {
    Segment ps = pc.getPickSegment();
    Point3 pa = ps.intersectWithTriangle(
      _p[0].x,_p[0].y,_p[0].z,
      _p[1].x,_p[1].y,_p[1].z,
      _p[3].x,_p[3].y,_p[3].z);
    Point3 pb = ps.intersectWithTriangle(
      _p[0].x,_p[0].y,_p[0].z,
      _p[3].x,_p[3].y,_p[3].z,
      _p[2].x,_p[2].y,_p[2].z);
    pc.addResult(pa);
    pc.addResult(pb);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Axis _axis;
  private BoxConstraint _constraint;
  private Point3[] _p = new Point3[4];

  // Decorates the frame with a wire.
  private class Wire extends Node {
    protected BoundingSphere computeBoundingSphere(boolean finite) {
      return computeBoundingSphereOfFrame(finite);
    }
    protected void draw(DrawContext dc) {
      glColor3f(1.0f,1.0f,1.0f);
      glLineWidth(1.5f);
      glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
      //glEnable(GL_POLYGON_OFFSET_LINE);
      //glPolygonOffset(-1.0f,-1.0f);
      glBegin(GL_QUADS); {
        glVertex3d(_p[0].x,_p[0].y,_p[0].z);
        glVertex3d(_p[1].x,_p[1].y,_p[1].z);
        glVertex3d(_p[3].x,_p[3].y,_p[3].z);
        glVertex3d(_p[2].x,_p[2].y,_p[2].z);
      } glEnd();
    }
    public void pick(PickContext pc) {
      pickOnFrame(pc);
    }
  }
}
