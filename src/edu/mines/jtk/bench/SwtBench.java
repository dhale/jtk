/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import java.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import edu.mines.jtk.util.Stopwatch;

/**
 * SWT workbench.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.02
 */
public class SwtBench {

  public static void main(String[] args) {
    benchPrimitives();
  }

  public static void benchPrimitives() {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("Draw lines");
    shell.setLayout(new FillLayout());
    Canvas canvas = new Canvas(shell,SWT.NONE);
    Painter painter = new Painter();
    canvas.addControlListener(painter);
    canvas.addPaintListener(painter);
    shell.setBounds(100,100,800,800);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
  private static class Painter implements PaintListener,ControlListener {
    private Painter() {
      _n = 1000;
      _p = new int[2*_n];
    }
    public void controlMoved(ControlEvent e) {
    }
    public void controlResized(ControlEvent e) {
      Random random = new Random();
      Rectangle bounds = ((Control)e.widget).getBounds();
      double width = (double)bounds.width;
      double height = (double)bounds.height;
      for (int i=0,j=0; i<_n; ++i,j+=2) {
        _p[j+X] = (int)(width*random.nextDouble());
        _p[j+Y] = (int)(height*random.nextDouble());
      }
    }
    public void paintControl(PaintEvent e) {
      GC gc = e.gc;
      Stopwatch sw = new Stopwatch();
      int ndraw,rate;
      double maxtime = 1.0;

      // Lines.
      System.out.print("Drawing lines: ");
      sw.restart();
      for (ndraw=0; sw.time()<maxtime; ++ndraw) {
        for (int i=0,j=0; j<2*_n; ++i) {
          int x1 = _p[j++];
          int y1 = _p[j++];
          int x2 = _p[j++];
          int y2 = _p[j++];
          gc.drawLine(x1,y1,x2,y2);
        }
      }
      sw.stop();
      rate = (int)((double)ndraw*(_n/2)/sw.time());
      System.out.println("lines/sec = "+rate);

      // Polylines.
      System.out.print("Drawing polylines: ");
      sw.restart();
      for (ndraw=0; sw.time()<maxtime; ++ndraw) {
        gc.drawPolyline(_p);
      }
      sw.stop();
      rate = (int)((double)ndraw*(_n-1)/sw.time());
      System.out.println("lines/sec = "+rate);
    }
    private int _n;
    private int[] _p;
    private static int X = 0;
    private static int Y = 1;
  }

  public static void testNative() {
    Display display = new Display();
    Shell shell = new Shell(display);
    System.out.println("shell handle="+shell.handle);
    printHandleNative(shell.handle);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }

  public static void drawLabelWithImage() {
    Display display = new Display();
    Image image = new Image(display,16,16);
    Color color = display.getSystemColor(SWT.COLOR_RED);
    GC gc = new GC(image);
    gc.setBackground(color);
    gc.fillRectangle(image.getBounds());
    gc.dispose();
    Shell shell = new Shell(display);
    Label label = new Label(shell,SWT.BORDER);
    label.setImage(image);
    label.pack();
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    image.dispose();
    display.dispose();
  }

  private static native void printHandleNative(int handle);

  static {
    System.loadLibrary("edu_mines_jtk_bench");
  }
}
