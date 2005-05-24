/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl.test;

import java.awt.*;
import javax.swing.*;

import edu.mines.jtk.opengl.*;

/**
 * Simple OpenGL test harness. Constructs an OpenGL canvas,
 * which is painted using a specified OpenGL painter.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.24
 */
public class TestSimple {
  public static void run(String[] args, GlPainter painter) {
    run(args,painter,false);
  }
  public static void run(
    String[] args, GlPainter painter, boolean autoRepaint) 
  {
    run(painter,autoRepaint);
  }
  public static void run(GlPainter painter, boolean autoRepaint) {
    GlCanvas canvas = new GlCanvas(painter);
    canvas.setAutoRepaint(autoRepaint);
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(SIZE,SIZE));
    frame.getContentPane().add(canvas,BorderLayout.CENTER);
    frame.setVisible(true);
  }
  private static final int SIZE = 600;
}
