/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.bench;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jves.util.Stopwatch;

/**
 * AWT/Swing workbench.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.14
 */
public class AwtBench {

  public static void main(String[] args) {
    testNative();
    //benchPrimitives();
  }

  private static void testNative() {
    Frame frame = new Frame();
    frame.setBounds(100,100,500,200);
    frame.add(new NativeCanvas());
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        System.exit(0);
      }
    });
    frame.show();
  }
  private static class NativeCanvas extends Canvas {
    public void paint(Graphics g) {
      paintNative(this);
    }
    private static native void paintNative(Canvas canvas);
    static {
      System.loadLibrary("edu_mines_jves_bench");
    }
  }

  private static void benchPrimitives() {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(new MyPanel());
    frame.setSize(new Dimension(800,800));
    frame.show();
  }

  private static class MyPanel extends JPanel {
    private MyPanel() {
      _n = 1000;
      _x = new int[_n];
      _y = new int[_n];
    }
    public void setBounds(int x, int y, int width, int height) {
      super.setBounds(x,y,width,height);
      System.out.println("setBounds: width="+width+" height="+height);
      Random random = new Random();
      for (int i=0; i<_n; ++i) {
        _x[i] = (int)(width*random.nextDouble());
        _y[i] = (int)(height*random.nextDouble());
      }
    }
    public void paint(Graphics g) {
      super.paint(g);
      Stopwatch sw = new Stopwatch();
      int ndraw,rate;
      double maxtime = 1.0;

      // Lines.
      System.out.print("Drawing lines: ");
      sw.restart();
      for (ndraw=0; sw.time()<maxtime; ++ndraw) {
        for (int i=0,j=0; j<_n; ++i) {
          int x1 = _x[j];
          int y1 = _y[j++];
          int x2 = _x[j];
          int y2 = _y[j++];
          g.drawLine(x1,y1,x2,y2);
        }
      }
      sw.stop();
      rate = (int)((double)ndraw*(_n/2)/sw.time());
      System.out.println("lines/sec = "+rate);

      // Polylines.
      System.out.print("Drawing polylines: ");
      sw.restart();
      for (ndraw=0; sw.time()<maxtime; ++ndraw) {
        g.drawPolyline(_x,_y,_n);
      }
      sw.stop();
      rate = (int)((double)ndraw*(_n-1)/sw.time());
      System.out.println("lines/sec = "+rate);
    }
    private int _n;
    private int[] _x,_y;
  }
}
