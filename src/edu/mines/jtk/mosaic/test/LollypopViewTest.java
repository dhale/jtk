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
 * Test {@link edu.mines.jtk.LollypopViewTest} and associates.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.01.01
 */
public class LollypopViewTest {

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
    mosaic.setPreferredSize(new Dimension(900,600));

    mosaic.getTile(0,0).addTiledView(makeView(101,1.0,-50.0,1.0));
    mosaic.getTile(1,0).addTiledView(makeView( 17,1.0, -5.0,2.0));

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

  private static LollypopView makeView(
    int nx, double dx, double fx, double a) 
  {
    Sampling sx = new Sampling(nx,dx,fx);
    float[] f = new float[nx];
    for (int ix=0; ix<nx; ++ix) {
      double xi = fx+ix*dx;
      f[ix] = (float)(a*cos(2.0*PI*xi/20));
    }
    return new LollypopView(sx,f);
  }
}
