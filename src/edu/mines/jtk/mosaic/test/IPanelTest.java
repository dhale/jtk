/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.test;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import edu.mines.jtk.gui.*;
import edu.mines.jtk.mosaic.*;
import static java.lang.Math.*;

/**
 * Test {@link edu.mines.jtk.mosaic.IPanel}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.20
 */
public class IPanelTest {

  static class Title extends IPanel {
    Title(String text) {
      super();
      _text = text;
    }
    public void paintToRect(Graphics2D g2d, Rectangle rg) {
      g2d = (Graphics2D)g2d.create();
      g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
      Font font = g2d.getFont();
      float fontSize = font.getSize();
      fontSize *= 2.0f;
      font = font.deriveFont(fontSize);
      g2d.setFont(font);
      FontMetrics fm = g2d.getFontMetrics();
      FontRenderContext frc = g2d.getFontRenderContext();
      LineMetrics lm = font.getLineMetrics("GgYy",frc);
      int fh = round(lm.getHeight());
      int fa = round(lm.getAscent());
      int fd = round(lm.getDescent());
      int fl = round(lm.getLeading());
      int w = rg.width;
      int h = rg.height;
      int wt = fm.stringWidth(_text);
      int xt = max(0,min(w-wt,(w-wt)/2));
      int yt = h/2;
      g2d.drawString(_text,xt,yt);
    }
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      Rectangle rg = new Rectangle(0,0,getWidth(),getHeight());
      paintToRect(g2d,rg);
    }
    private String _text;
  }

  static class Wave extends IPanel {
    Wave(double cycles) {
      super();
      _cycles = cycles;
    }
    public void paintToRect(Graphics2D g2d, Rectangle rg) {
      g2d = (Graphics2D)g2d.create();
      g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
      double x1u = 0.0;
      double y1u = 1.0;
      double x2u = 2.0*PI*_cycles;
      double y2u = -1.0;
      int x1d = 0;
      int y1d = 0;
      int x2d = rg.width-1;
      int y2d = rg.height-1;
      Transcaler ts = new Transcaler(x1u,y1u,x2u,y2u,x1d,y1d,x2d,y2d);
      int nx = 1000;
      double dx = (x2u-x1u)/(nx-1);
      double fx = 0.0;
      int x1 = ts.x(fx);
      int y1 = ts.y(sin(fx));
      for (int ix=1; ix<nx; ++ix) {
        double xi = fx+ix*dx;
        int x2 = ts.x(xi);
        int y2 = ts.y(sin(xi));
        g2d.drawLine(x1,y1,x2,y2);
        x1 = x2;
        y1 = y2;
      }
    }
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      Rectangle rg = new Rectangle(0,0,getWidth(),getHeight());
      paintToRect(g2d,rg);
    }
    private double _cycles;
  }

  public static void main(String[] args) {
    IPanel mosaic = new IPanel();
    mosaic.setLayout(new GridLayout(2,1));
    Title title = new Title("A Sine Wave");
    Wave wave = new Wave(5.0);
    mosaic.add(title);
    mosaic.add(wave);
    mosaic.setPreferredSize(new Dimension(800,500));

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(mosaic);
    frame.pack();
    frame.setVisible(true);
    try {
      mosaic.paintToPng(1000,3,"junk.png");
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
