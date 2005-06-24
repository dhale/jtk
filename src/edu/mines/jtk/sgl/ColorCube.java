/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
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

import edu.mines.jtk.gui.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.opengl.Gl.*;

/**
 * A simple cube with colored sides for testing.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.27
 */
public class ColorCube extends Node implements Selectable {

  public ColorCube() {
    StateSet states = new StateSet();
    MaterialState ms = new MaterialState();
    ms.setColorMaterialFront(GL_AMBIENT_AND_DIFFUSE);
    ms.setSpecularFront(Color.white);
    ms.setShininessFront(100.0f);
    states.add(ms);
    setStates(states);
  }

  public void beginSelect(PickResult pr) {
    System.out.println("ColorCube.beginSelect");
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected BoundingSphere computeBoundingSphere() {
    Point3 c = new Point3(0.5,0.5,0.5);
    double r = 0.5*Math.sqrt(3.0);
    return new BoundingSphere(c,r);
  }

  protected void draw(DrawContext dc) {
    glPushClientAttrib(GL_CLIENT_VERTEX_ARRAY_BIT);
    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_NORMAL_ARRAY);
    glEnableClientState(GL_COLOR_ARRAY);
    glVertexPointer(3,GL_FLOAT,0,_vb);
    glNormalPointer(GL_FLOAT,0,_nb);
    glColorPointer(3,GL_FLOAT,0,_cb);
    glDrawArrays(GL_QUADS,0,24);
    glPopClientAttrib();
  }

  protected void pick(PickContext pc) {
    PickSegment ps = pc.getPickSegment();
    for (int iside=0; iside<6; ++iside) {
      double xa = _va[12*iside+ 0];
      double ya = _va[12*iside+ 1];
      double za = _va[12*iside+ 2];
      double xb = _va[12*iside+ 3];
      double yb = _va[12*iside+ 4];
      double zb = _va[12*iside+ 5];
      double xc = _va[12*iside+ 6];
      double yc = _va[12*iside+ 7];
      double zc = _va[12*iside+ 8];
      double xd = _va[12*iside+ 9];
      double yd = _va[12*iside+10];
      double zd = _va[12*iside+11];
      Point3 p = ps.intersectWithTriangle(xa,ya,za,xb,yb,zb,xc,yc,zc);
      Point3 q = ps.intersectWithTriangle(xa,ya,za,xc,yc,zc,xd,yd,zd);
      if (p!=null)
        pc.addResult(p);
      if (q!=null)
        pc.addResult(q);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Vertices, normals, and colors.
  private static float[] _va = {
     0.0f, 0.0f, 0.0f,  0.0f, 0.0f, 1.0f,  0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 0.0f,
     0.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,
     0.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 0.0f, 0.0f,
     1.0f, 0.0f, 0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 1.0f,  1.0f, 0.0f, 1.0f,
     0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 1.0f,  1.0f, 1.0f, 1.0f,  1.0f, 1.0f, 0.0f,
     0.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,  1.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,
  };
  private static float[] _na = {
    -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
     0.0f,-1.0f, 0.0f,  0.0f,-1.0f, 0.0f,  0.0f,-1.0f, 0.0f,  0.0f,-1.0f, 0.0f,
     0.0f, 0.0f,-1.0f,  0.0f, 0.0f,-1.0f,  0.0f, 0.0f,-1.0f,  0.0f, 0.0f,-1.0f,
     1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,
     0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,
     0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,
  };
  private static float[] _ca = {
     0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,
     1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,
     1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,
     1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,
     0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,
     0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,
  };
  private static FloatBuffer _vb = Direct.newFloatBuffer(_va);
  private static FloatBuffer _nb = Direct.newFloatBuffer(_na);
  private static FloatBuffer _cb = Direct.newFloatBuffer(_ca);

  // Simple testing.
  private static final int SIZE = 600;
  public static void main(String[] args) {
    ColorCube cc = new ColorCube();
    TransformGroup tg1 = new TransformGroup(Matrix44.translate(-2,0,0));
    TransformGroup tg2 = new TransformGroup(Matrix44.translate( 2,0,0));
    tg1.addChild(cc);
    tg2.addChild(cc);
    World world = new World();
    world.addChild(tg1);
    world.addChild(tg2);

    OrbitView view = new OrbitView(world);
    //view.setProjection(OrbitView.Projection.ORTHOGRAPHIC);
    //view.setAzimuthAndElevation(90.0,0.0);
    //view.setScale(5.0);
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
