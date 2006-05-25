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

  public AxisAlignedQuad(Constant axis, Point3 qmin, Point3 qmax) {
    Check.argument(axis==Constant.X && qmin.y<=qmax.y && qmin.z<=qmax.z ||
                   axis==Constant.Y && qmin.x<=qmax.x && qmin.z<=qmax.z ||
                   axis==Constant.Z && qmin.x<=qmax.x && qmin.y<=qmax.y,
                   "points qmin and qmax are valid");
    _axis = axis;
    if (axis==Constant.X) {
      double x = 0.5*(qmin.x+qmax.x);
      _q00 = new Point3(x,qmin.y,qmin.z);
      _q10 = new Point3(x,qmax.y,qmin.z);
      _q01 = new Point3(x,qmin.y,qmax.z);
      _q11 = new Point3(x,qmax.y,qmax.z);
    } else if (axis==Constant.Y) {
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
    _h00 = new HandleBox(_q00);
    _h10 = new HandleBox(_q10);
    _h01 = new HandleBox(_q01);
    _h11 = new HandleBox(_q11);
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

  protected BoundingSphere computeBoundingSphere(boolean finite) {
    Point3 c = _q00.affine(0.5,_q10).affine(0.5,_q01.affine(0.5,_q11));
    double r = _q00.minus(c).length();
    return new BoundingSphere(c,r);
  }

  protected void draw(DrawContext dc) {
    glColor3f(1.0f,1.0f,1.0f);
    glBegin(GL_QUADS); {
      glVertex3d(_q00.x,_q00.y,_q00.z);
      glVertex3d(_q10.x,_q10.y,_q10.z);
      glVertex3d(_q01.x,_q01.y,_q01.z);
      glVertex3d(_q11.x,_q11.y,_q11.z);
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

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Constant _axis;
  private Point3 _q00,_q10,_q01,_q11;
  private HandleBox _h00,_h10,_h01,_h11;

  private void showHandles() {
    _h00.setLocation(_q00);
    _h10.setLocation(_q10);
    _h01.setLocation(_q01);
    _h11.setLocation(_q11);
    addChild(_h00);
    addChild(_h10);
    addChild(_h01);
    addChild(_h11);
  }

  private void hideHandles() {
    removeChild(_h00);
    removeChild(_h10);
    removeChild(_h01);
    removeChild(_h11);
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
