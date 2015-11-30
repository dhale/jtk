/****************************************************************************
Copyright 2008, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
