/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
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
package edu.mines.jtk.mosaic;

import javax.swing.*;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.mosaic.PointsView}
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.01.19
 */
public class PointsViewTest {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        test1();
      }
    });
  }

  private static void test1() {
    int n = 50;
    float[] x1 = randfloat(n);
    float[] x2 = randfloat(n);
    float[] x3 = randfloat(n);

    PlotPanel panel = new PlotPanel(1,1);
    panel.setLimits(-0.1,-0.1,1.1,1.1);
    PointsView pv = panel.addPoints(x1,x2,x3);
    pv.setLineStyle(PointsView.Line.NONE);
    pv.setMarkStyle(PointsView.Mark.FILLED_CIRCLE);
    pv.setTextFormat("%4.2f");
    PlotFrame frame = new PlotFrame(panel);
    frame.setSize(800,800);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    //frame.paintToPng(300,6,"junk.png");
  }
}
