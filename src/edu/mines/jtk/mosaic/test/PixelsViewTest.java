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
 * Tests {@link edu.mines.jtk.mosaic.PixelsView}
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.10.04
 */
public class PixelsViewTest {

  public static void main(String[] args) {
    Set<Mosaic.AxesPlacement> axesPlacement = EnumSet.of(
      Mosaic.AxesPlacement.LEFT,
      Mosaic.AxesPlacement.BOTTOM
    );
    Mosaic.BorderStyle borderStyle = Mosaic.BorderStyle.FLAT;
    Mosaic mosaic = new Mosaic(1,2,axesPlacement,borderStyle);
    mosaic.setBackground(Color.WHITE);
    mosaic.setFont(new Font("SansSerif",Font.PLAIN,12));
    mosaic.setPreferredSize(new Dimension(950,400));

    int n1 = 1;
    int n2 = 5;
    float d1 = 1.0f/(float)max(1,n1-1);
    float d2 = 1.0f/(float)max(1,n2-1);
    float[][] f = Array.rampfloat(0.0f,d1,d2,n1,n2);

    PixelsView pv0 = new PixelsView(f);
    pv0.setInterpolation(PixelsView.Interpolation.NEAREST);
    pv0.setColorModel(ByteIndexColorModel.linearHue(0.0,1.0));
    pv0.setClips(0.0f,2.0f);

    PixelsView pv1 = new PixelsView(f);
    pv1.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv1.setColorModel(ByteIndexColorModel.linearGray(0.0,1.0));
    pv1.setPercentiles(5.0f,95.0f);

    Tile tile0 = mosaic.getTile(0,0);
    Tile tile1 = mosaic.getTile(0,1);

    tile0.addTiledView(pv0);
    tile1.addTiledView(pv1);

    ModeManager modeManager = mosaic.getModeManager();
    TileZoomMode zoomMode = new TileZoomMode(modeManager);
    zoomMode.setActive(true);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(mosaic,BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }
}
