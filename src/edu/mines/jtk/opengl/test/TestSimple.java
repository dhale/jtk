/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import java.awt.*;
import javax.swing.*;

import edu.mines.jtk.opengl.*;

/**
 * Simple OpenGL test harness. Constructs an AWT or SWT OpenGL canvas,
 * which is painted using a specified OpenGL painter.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.02
 */
public class TestSimple {
  public static void run(String[] args, GlPainter painter) {
    run(args,painter,false);
  }
  public static void run(
    String[] args, GlPainter painter, boolean autoRepaint) 
  {
    String platform = (args.length>0)?args[0]:"swt";
    if (platform.equals("awt")) {
      runAwt(painter,autoRepaint);
    } else if (platform.equals("swt")) {
      runSwt(painter,autoRepaint);
    } else {
      System.err.println("cannot recognize platform = "+platform);
    }
  }
  public static void runAwt(GlPainter painter, boolean autoRepaint) {
    GlAwtCanvas canvas = new GlAwtCanvas(painter);
    canvas.setAutoRepaint(autoRepaint);
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(SIZE,SIZE));
    frame.getContentPane().add(canvas,BorderLayout.CENTER);
    frame.setVisible(true);
  }
  public static void runSwt(GlPainter painter, boolean autoRepaint) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    shell.setSize(SIZE,SIZE);
    GlSwtCanvas canvas = new GlSwtCanvas(shell,SWT.NO_BACKGROUND,painter);
    canvas.setAutoRepaint(autoRepaint);
    shell.layout();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
    System.exit(0);
  }
  private static final int SIZE = 600;
}
