/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mesh;

import java.awt.*;
import javax.swing.*;

import edu.mines.jtk.mosaic.*;


/**
 * Tests {@link edu.mines.jtk.mesh.TriMeshView}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.09.11
 */
public class TriMeshViewTest {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        testSquare();
      }
    });
  }

  public static void testSquare() {
    TriMesh tm = new TriMesh();
    TriMesh.Node n0 = new TriMesh.Node(0.0f,0.0f);
    TriMesh.Node n1 = new TriMesh.Node(1.0f,0.0f);
    TriMesh.Node n2 = new TriMesh.Node(0.0f,1.0f);
    TriMesh.Node n3 = new TriMesh.Node(1.0f,1.0f);
    tm.addNode(n0);
    tm.addNode(n1);
    tm.addNode(n2);
    tm.addNode(n3);
    plot(tm);
  }

  private static void plot(TriMesh tm) {
    TriMeshView tmv = new TriMeshView(tm);
    tmv.setPolysVisible(true);
    tmv.setTriColor(Color.BLACK);
    tmv.setPolyColor(Color.BLUE);
    PlotPanel panel = new PlotPanel();
    panel.setHLabel("x");
    panel.setVLabel("y");
    Mosaic mosaic = panel.getMosaic();
    mosaic.getTile(0,0).addTiledView(tmv);
    PlotFrame frame = new PlotFrame(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(700,700);
    frame.setVisible(true);
  }
}
