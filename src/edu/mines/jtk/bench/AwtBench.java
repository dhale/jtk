/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

import edu.mines.jtk.util.Stopwatch;

/**
 * AWT/Swing workbench.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.14
 */
public class AwtBench {

  public static void main(String[] args) {
    testImage();
    //testNativeJFrame();
    //testNativeFrame();
    //benchPrimitives();
  }

  public static void testImage() {
    int widthPanel = 600;
    int heightPanel = 600;
    int dpi = 288;
    double widthInches = 3.0;
    double heightInches = 3.0;
    ImagePanel panel = new ImagePanel();
    panel.setPreferredSize(new Dimension(widthPanel,heightPanel));
    BufferedImage bi = panel.drawToImage(dpi,widthInches,heightInches);
    writeToPng(bi,"ImageAWT.png");
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
  }
  private static class ImagePanel extends JPanel {
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      int wpixels = getWidth();
      int hpixels = getHeight();
      draw(g,wpixels,hpixels);
    }
    public void draw(Graphics g, int width, int height) {
      Graphics2D g2d = (Graphics2D)g;
      g2d.setColor(Color.RED);
      for (int i=0; i<3; ++i)
        g2d.drawLine(0,0,i*(width-1)/2,height-1);
      for (int i=0; i<2; ++i)
        g2d.drawLine(0,0,width-1,i*(height-1)/2);
      g2d.setColor(Color.BLACK);
      Font font = new Font("SansSerif",Font.PLAIN,10);
      g2d.setFont(font);
      FontMetrics fm = g2d.getFontMetrics();
      int fontAscent = fm.getAscent();
      int fontHeight = fm.getHeight();
      int x = width/3;
      int y = height/3;
      g2d.drawLine(x,0,x,y);
      y += fontAscent;
      int sw = fm.stringWidth("Goodbye");
      g2d.drawString("Goodbye",x-sw/2,y);
      y += fontHeight;
      sw = fm.stringWidth("Hello");
      g2d.drawString("Hello",x-sw/2,y);
    }
    public BufferedImage drawToImage(
      double dpi, double widthInches, double heightInches) {
      int wimage = (int)(dpi*widthInches+0.5);
      int himage = (int)(dpi*heightInches+0.5);
      int type = BufferedImage.TYPE_INT_RGB;
      BufferedImage bi = new BufferedImage(wimage,himage,type);
      Graphics2D g2d = (Graphics2D)bi.getGraphics();
      g2d.setColor(Color.WHITE);
      g2d.fillRect(0,0,wimage,himage);
      g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
      double sr = 2+Toolkit.getDefaultToolkit().getScreenResolution();
      double scale = dpi/sr;
      int wpixels = (int)((wimage-1)/scale+1.5);
      int hpixels = (int)((himage-1)/scale+1.5);
      g2d.scale(scale,scale);
      draw(g2d,wpixels,hpixels);
      g2d.dispose();
      return bi;
    }
  }
  private static void writeToPng(BufferedImage image, String fileName) {
    try {
      File file = new File(fileName);
      ImageIO.write(image,"png",file);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private static void testNativeJFrame() {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(500,200));
    frame.getContentPane().add(new NativeCanvas());
    frame.setVisible(true);
  }

  private static void testNativeFrame() {
    Frame frame = new Frame();
    frame.setBounds(100,100,500,200);
    frame.add(new NativeCanvas());
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        System.exit(0);
      }
    });
    frame.setVisible(true);
  }

  private static class NativeCanvas extends Canvas {
    private static final long serialVersionUID = 1L;
    public void paint(Graphics g) {
      paintNative(this);
    }
    private static native void paintNative(Canvas canvas);
    static {
      System.loadLibrary("edu_mines_jtk_bench");
    }
  }

  private static void benchPrimitives() {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(new MyPanel());
    frame.setSize(new Dimension(800,800));
    frame.setVisible(true);
  }

  private static class MyPanel extends JPanel {
    private static final long serialVersionUID = 1L;
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
