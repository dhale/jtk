/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.test;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import edu.mines.jtk.mosaic.*;

/**
 * Test {@link edu.mines.jtk.mosaic.Mosaic} and associates.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.27
 */
public class MosaicTest {

  public static void main(String[] args) {
    int nrow = 2;
    int ncol = 3;
    Set<Mosaic.AxesPlacement> axesPlacement = EnumSet.of(
      Mosaic.AxesPlacement.TOP,
      Mosaic.AxesPlacement.LEFT,
      Mosaic.AxesPlacement.BOTTOM,
      Mosaic.AxesPlacement.RIGHT
    );
    Mosaic.BorderStyle borderStyle = Mosaic.BorderStyle.FLAT;
    Mosaic mosaic = new Mosaic(nrow,ncol,axesPlacement,borderStyle);
    mosaic.setBackground(Color.WHITE);
    mosaic.setFont(new Font("SansSerif",Font.PLAIN,12));
    mosaic.setWidthMinimum(1,200);
    mosaic.setWidthElastic(1,200);
    mosaic.setHeightElastic(0,0);
    mosaic.setPreferredSize(new Dimension(700,600));
    Tile tile11 = mosaic.getTile(1,1);
    tile11.setViewRectangle(new DRectangle(0.1,0.1,0.8,0.8));

    TileZoomMode zoomMode = new TileZoomMode(mosaic.getModeManager());

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
}
