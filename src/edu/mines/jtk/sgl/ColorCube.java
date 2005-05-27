/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import static edu.mines.jtk.opengl.Gl.*;

/**
 * A simple cube with colored sides for testing.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.27
 */
public class ColorCube extends Node {

  protected BoundingSphere computeBoundingSphere() {
    Point3 c = new Point3(0.5,0.5,0.5);
    double r = 0.5;
    return new BoundingSphere(c,r);
  }

  protected void draw(DrawContext dc) {
    System.out.println("ColorCube.draw");
    glBegin(GL_QUADS); {
      glColor3d(1,0,0);
      glVertex3d(0,0,0);
      glVertex3d(1,0,0);
      glVertex3d(1,1,0);
      glVertex3d(0,1,0);
      glColor3d(0,1,0);
      glVertex3d(0,0,1);
      glVertex3d(1,0,1);
      glVertex3d(1,1,1);
      glVertex3d(0,1,1);
    } glEnd();
  }

  public static void main(String[] args) {
    ColorCube cube = new ColorCube();
    World world = new World();
    world.addChild(cube);
    OrbitView view = new OrbitView(world);
    view.setWorld(world);
    ViewCanvas canvas = new ViewCanvas(view);
    canvas.setView(view);
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(SIZE,SIZE));
    frame.getContentPane().add(canvas,BorderLayout.CENTER);
    frame.setVisible(true);
  }
  private static final int SIZE = 600;
}
