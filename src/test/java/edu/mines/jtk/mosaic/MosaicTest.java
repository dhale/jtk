/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.*;

/**
 * Test {@link edu.mines.jtk.mosaic.Mosaic} and associates.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.27
 */
public class MosaicTest {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        go();
      }
    });
  }
  private static void go() {
    int nrow = 2;
    int ncol = 3;
    Set<Mosaic.AxesPlacement> axesPlacement = EnumSet.of(
      Mosaic.AxesPlacement.TOP,
      Mosaic.AxesPlacement.LEFT,
      Mosaic.AxesPlacement.BOTTOM,
      Mosaic.AxesPlacement.RIGHT
    );
    float[] x1 = {0.0f,1.0f};
    float[] x2 = {0.0f,1.0f};
    Mosaic mosaic = new Mosaic(nrow,ncol,axesPlacement);
    mosaic.setBackground(Color.WHITE);
    mosaic.setFont(new Font("SansSerif",Font.PLAIN,12));
    mosaic.setWidthMinimum(1,200);
    mosaic.setWidthElastic(1,200);
    mosaic.setHeightElastic(0,0);
    for (int irow=0; irow<nrow; ++irow) {
      for (int icol=0; icol<ncol; ++icol) {
        mosaic.getTile(irow,icol).addTiledView(new PointsView(x1,x2));
      }
    }
    //Tile tile11 = mosaic.getTile(1,1);
    //tile11.setViewRectangle(new DRectangle(0.1,0.1,0.8,0.8));

    mosaic.getTileAxisTop(0).setLabel("axis label");
    mosaic.getTileAxisTop(1).setLabel("axis label");
    mosaic.getTileAxisTop(2).setLabel("axis label");
    mosaic.getTileAxisLeft(0).setLabel("axis label");
    mosaic.getTileAxisLeft(1).setLabel("axis label");
    mosaic.getTileAxisBottom(0).setLabel("axis label");
    mosaic.getTileAxisBottom(1).setLabel("axis label");
    mosaic.getTileAxisBottom(2).setLabel("axis label");
    mosaic.getTileAxisRight(0).setLabel("axis label");
    mosaic.getTileAxisRight(1).setLabel("axis label");

    TileZoomMode zoomMode = new TileZoomMode(mosaic.getModeManager());
    zoomMode.setActive(true);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(mosaic,BorderLayout.CENTER);
    frame.setSize(600,500);
    frame.setVisible(true);

    /*
     * This code creates a PNG image in a file named junk.png. We create the 
     * image later on the Swing thread, because it likely depends on other
     * events on that thread being processed first.
     */
    /*
    final Mosaic mosaicFinal = mosaic;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          mosaicFinal.paintToPng(300,6,"junk.png");
        } catch (IOException ioe) {
          throw new RuntimeException(ioe);
        }
      }
    });
    */
  }
}
