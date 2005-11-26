/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dave;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.gui.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;
import static edu.mines.jtk.mosaic.Mosaic.*;

/**
 * Test recursive Gaussian filter for 2-D images.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.11.25
 */
public class RgfTest {

  public static void main(String[] args) {
    double sigma = 2.0;
    if (args.length>0)
      sigma = Double.valueOf(args[0]);
    int n1 = 101;
    int n2 = 101;
    float[][] x = new float[n2][n1];
    float[][] y = new float[n2][n1];
    x[3][3] = x[3][n1-4] = x[n2-4][3] = x[n2-4][n1-4] = x[n2/2][n1/2] = 1.0f;
    plotImage(x);
    RecursiveGaussianFilter rgf = new RecursiveGaussianFilter(sigma);

    rgf.apply00(x,y);
    plotImage(y);

    rgf.apply10(x,y);
    plotImage(y);
    rgf.apply01(x,y);
    plotImage(y);

    rgf.apply11(x,y);
    plotImage(y);
    rgf.apply20(x,y);
    plotImage(y);
    rgf.apply02(x,y);
    plotImage(y);
  }
    

  private static void plotImage(float[][] f) {
    plotImage(Array.min(f),Array.max(f),f);
  }
  private static void plotImage(String filename, float[][] f) {
    plotImage(filename,Array.min(f),Array.max(f),f);
  }
  private static Mosaic plotImage(
    float fmin, float fmax, float[][] f) 
  {
    int n2 = f.length;
    int n1 = f[0].length;
    Set<Mosaic.AxesPlacement> axesPlacement = EnumSet.noneOf(
      Mosaic.AxesPlacement.class
    );
    Mosaic.BorderStyle borderStyle = Mosaic.BorderStyle.FLAT;
    Mosaic mosaic = new Mosaic(1,1,axesPlacement,borderStyle);
    mosaic.setBackground(Color.WHITE);
    mosaic.setFont(new Font("SansSerif",Font.PLAIN,18));
    mosaic.setPreferredSize(new Dimension(550,500));

    PixelsView pv = new PixelsView(f);
    pv.setOrientation(PixelsView.Orientation.X1DOWN_X2RIGHT);
    pv.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv.setColorModel(ByteIndexColorModel.linearGray(0.0,1.0));

    Tile tile = mosaic.getTile(0,0);
    tile.addTiledView(pv);

    ModeManager modeManager = mosaic.getModeManager();
    TileZoomMode zoomMode = new TileZoomMode(modeManager);
    zoomMode.setActive(true);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(mosaic,BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
    return mosaic;
  }
  private static void plotImage(
    String filename, float fmin, float fmax, float[][] f) 
  {
    Mosaic mosaic = plotImage(fmin,fmax,f);
    try {
      mosaic.paintToPng(300,6,filename+"Flat.png");
    } catch (IOException ioe) {
      System.out.println("Cannot write image to file: "+filename);
    }
  }
}
