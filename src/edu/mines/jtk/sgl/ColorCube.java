/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;
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
public class ColorCube extends Node {

  ColorCube() {
    StateSet states = new StateSet();
    MaterialState ms = new MaterialState();
    ms.setColorMaterialFront(GL_AMBIENT_AND_DIFFUSE);
    ms.setSpecularFront(Color.white);
    ms.setShininessFront(100.0f);
    states.add(ms);
    setStates(states);
  }

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
    ViewCanvas canvas = new ViewCanvas(view);
    canvas.setView(view);

    ModeManager mm = new ModeManager();
    mm.add(canvas);
    OrbitViewMode ovm = new OrbitViewMode(mm);
    ovm.setActive(true);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(SIZE,SIZE));
    frame.getContentPane().add(canvas,BorderLayout.CENTER);
    frame.setVisible(true);
  }
  private static final int SIZE = 600;
}
