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

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.gui.*;
import edu.mines.jtk.util.*;
import edu.mines.jtk.mosaic.*;

/**
 * Test {@link edu.mines.jtk.mosaic.SequenceView} and associates.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.01.01
 */
public class SequenceViewTest {

  public static void main(String[] args) {
    int nrow = 2;
    int ncol = 1;
    Set<Mosaic.AxesPlacement> axesPlacement = EnumSet.of(
      Mosaic.AxesPlacement.LEFT,
      Mosaic.AxesPlacement.BOTTOM
    );
    Mosaic mosaic = new Mosaic(nrow,ncol,axesPlacement);
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
    //filter1(0.8f,x,y);
    filter2(0.8f,x,y);
    //filter3(0.8f,x,y);

    tileA.addTiledView(makeView(n,d,f,x));
    tileB.addTiledView(makeView(n,d,f,y));

    ModeManager modeManager = mosaic.getModeManager();
    TileZoomMode zoomMode = new TileZoomMode(modeManager);

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

  private static SequenceView makeView(
    int nx, double dx, double fx, float[] f) 
  {
    Sampling sx = new Sampling(nx,dx,fx);
    SequenceView sv = new SequenceView(sx,f);
    sv.setSequenceColor(Color.BLUE);
    sv.setShowZero(SequenceView.ShowZero.MIDDLE);
    return sv;
  }
  private static void filter1(float a, float[] x, float[] y) {
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
  private static void filter2(float a, float[] x, float[] y) {
    int n = x.length;
    float yim1 = 0.0f;
    for (int i=0; i<n; ++i) {
      y[i] = a*yim1+(1.0f-a)*x[i];
      yim1 = y[i];
    }
    float yip1 = 0.0f;
    for (int i=n-1; i>=0; --i) {
      y[i] = a*yip1+(1.0f+a)*y[i];
      yip1 = y[i];
    }
  }
  private static void filter3(float a, float[] x, float[] y) {
    int n = x.length;
    int n2 = n*2;
    float[] t = new float[n2];
    float yim1 = 0.0f;
    for (int i=0; i<n2; ++i) {
      float xi = (i<n)?x[i]:0.0f;
      t[i] = a*yim1+(1.0f-a)*xi;
      yim1 = t[i];
    }
    float yip1 = 0.0f;
    for (int i=n2-1; i>=0; --i) {
      t[i] = a*yip1+(1.0f+a)*t[i];
      yip1 = t[i];
    }
    for (int i=0; i<n; ++i)
      y[i] = t[i];
  }
  private static void cosine(float a, int nx, double dx, double fx, float[] f) {
    for (int ix=0; ix<nx; ++ix) {
      double xi = fx+ix*dx;
      f[ix] = (float)(a*cos(2.0*PI*xi*(1.0+0.02*xi)/20));
    }
  }
}
