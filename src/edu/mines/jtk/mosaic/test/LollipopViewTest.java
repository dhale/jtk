/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.test;

import static java.lang.Math.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jtk.util.*;
import edu.mines.jtk.mosaic.*;

/**
 * Test {@link edu.mines.jtk.mosaic.LollipopView} and associates.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.01.01
 */
public class LollipopViewTest {

  public static void main(String[] args) {
    int nrow = 2;
    int ncol = 1;
    Set<Mosaic.AxesPlacement> axesPlacement = EnumSet.of(
      Mosaic.AxesPlacement.LEFT,
      Mosaic.AxesPlacement.BOTTOM
    );
    Mosaic.BorderStyle borderStyle = Mosaic.BorderStyle.FLAT;
    Mosaic mosaic = new Mosaic(nrow,ncol,axesPlacement,borderStyle);
    mosaic.setBackground(Color.WHITE);
    mosaic.setFont(new Font("SansSerif",Font.PLAIN,12));
    mosaic.setPreferredSize(new Dimension(950,400));

    Tile tileA = mosaic.getTile(0,0);
    Tile tileB = mosaic.getTile(1,0);

    int n = 101;
    double d = 1.0;
    double f = -(n-1)/2.0;
    float[] x = new float[n];
    float[] y = new float[n];
    x[0] = x[n-1] = x[n/2] = 1.0f;
    filter(0.8f,x,y);

    tileA.addTiledView(makeView(n,d,f,x));
    tileB.addTiledView(makeView(n,d,f,y));

    TileZoomMode zoomMode = new TileZoomMode(mosaic);

    JMenuBar menuBar = new JMenuBar();
    JMenu modeMenu = new JMenu("Mode");
    modeMenu.setMnemonic(KeyEvent.VK_M);
    JMenuItem zoomItem = new ModeMenuItem(zoomMode);
    modeMenu.add(zoomItem);
    menuBar.add(modeMenu);

    JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
    JToggleButton zoomButton = new ModeToggleButton(zoomMode);
    toolBar.add(zoomButton);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setJMenuBar(menuBar);
    frame.add(toolBar,BorderLayout.WEST);
    frame.add(mosaic,BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }

  private static LollipopView makeView(
    int nx, double dx, double fx, float[] f) 
  {
    Sampling sx = new Sampling(nx,dx,fx);
    LollipopView lv = new LollipopView(sx,f);
    lv.setLollipopColor(Color.BLUE);
    lv.setShowZero(LollipopView.ShowZero.MIDDLE);
    return lv;
  }
  private static void filter(float a, float[] x, float[] y) {
    int n = x.length;
    float yim1 = 0.0f;
    for (int i=0; i<n; ++i) {
      y[i] = a*yim1+x[i];
      yim1 = y[i];
    }
    float yip1 = 0.0f;
    for (int i=n-2; i>=0; --i) {
      float yi = a*(yip1+x[i+1]);
      y[i] += yi;
      yip1 = yi;
    }
  }
  private static void cosine(float a, int nx, double dx, double fx, float[] f) {
    for (int ix=0; ix<nx; ++ix) {
      double xi = fx+ix*dx;
      f[ix] = (float)(a*cos(2.0*PI*xi*(1.0+0.02*xi)/20));
    }
  }
}
