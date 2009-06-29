/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

//import edu.mines.jtk.util.Stopwatch;

/**
 * AWT/Swing workbench.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.14
 */
public class AwtBench {

  public static void main(String[] args) {
    testImage();
    //benchPrimitives();
  }

  public static void testImage() {
    Dimension panelSize = new Dimension(600,600);
    ImagePanel panel = new ImagePanel();
    panel.setSize(panelSize);
    panel.setPreferredSize(panelSize);
    panel.setBackground(Color.WHITE);
    BufferedImage image = panel.paintToImage(300,3);
    writeToPng(image,"ImageAWT.png");
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
  }
  private static class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      int width = getWidth();
      int height = getHeight();
      g2d.setColor(Color.RED);
      for (int i=0; i<3; ++i)
        g2d.drawLine(0,0,i*(width-1)/2,height-1);
      for (int i=0; i<2; ++i)
        g2d.drawLine(0,0,width-1,i*(height-1)/2);
      g2d.setColor(Color.BLACK);
      Font font = new Font("SansSerif",Font.PLAIN,18);
      g2d.setFont(font);
      FontMetrics fm = g2d.getFontMetrics();
      int fontAscent = fm.getAscent();
      int fontHeight = fm.getHeight();
      int x = width/2;
      int y = height/2;
      g2d.drawLine(x,0,x,y);
      y += fontAscent;
      int sw = fm.stringWidth("Goodbye");
      g2d.drawString("Goodbye",x-sw/2,y);
      y += fontHeight;
      sw = fm.stringWidth("Hello");
      g2d.drawString("Hello",x-sw/2,y);
      y += fontHeight;
      String message = "abcdefghijklmnopqrstuvwxyz0123456789";
      sw = fm.stringWidth(message);
      g2d.drawString(message,x-sw/2,y);
    }
    public BufferedImage paintToImage(double dpi, double winches) {
      int wpixels = getWidth();
      int hpixels = getHeight();
      double scale = dpi*winches/wpixels;
      int wimage = (int)(scale*(wpixels-1)+1.5);
      int himage = (int)(scale*(hpixels-1)+1.5);
      int type = BufferedImage.TYPE_INT_RGB;
      BufferedImage bi = new BufferedImage(wimage,himage,type);
      Graphics2D g2d = (Graphics2D)bi.getGraphics();
      g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
      Color fg = getForeground();
      g2d.setColor(getBackground());
      g2d.fillRect(0,0,wimage,himage);
      g2d.setColor(fg);
      g2d.scale(scale,scale);
      paintComponent(g2d);
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
  /*
  private static class DGraphics {
    public DGraphics(Graphics2D g2d, double dpi) {
      _g2d = g2d;
      _xushift = 0.5;
      _yushift = 0.5;
      _xuscale = dpi/72.0;
      _yuscale = dpi/72.0;
      _xdshift = 0.0;
      _ydshift = 0.0;
      _xdscale = 72.0/dpi;
      _ydscale = 72.0/dpi;
    }
    public void translate(double x, double y) {
      _g2d.translate(x,y);
    }
    public void scale(double s) {
      _g2d.scale(s,s);
    }
    public void drawLine(double x1, double y1, double x2, double y2) {
      _g2d.drawLine(x(x1),y(y1),x(x2),y(y2));
    }
    public int x(double xu) {
      return (int)(_xushift+_xuscale*xu);
    }
    public int y(double yu) {
      return (int)(_yushift+_yuscale*yu);
    }
    public double x(int xd) {
      return _xdscale*(xd-_xdshift);
    }
    public double y(int yd) {
      return _ydscale*(yd-_ydshift);
    }
    private double _xushift,_xuscale,_yushift,_yuscale;
    private double _xdshift,_xdscale,_ydshift,_ydscale;
    Graphics2D _g2d;
  }
  */

  /*
  private static void benchPrimitives() {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(new MyPanel());
    frame.setSize(new Dimension(800,800));
    frame.setVisible(true);
  }
  */

  /*
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
  */
}
