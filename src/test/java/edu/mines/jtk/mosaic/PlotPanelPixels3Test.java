/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import static java.lang.Math.max;
import javax.swing.*;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.dsp.Sampling;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.mosaic.PlotPanelPixels3}
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.08.11
 */
public class PlotPanelPixels3Test {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        test1();
        test2();
      }
    });
  }

  private static void test1() {
    int n1 = 11;
    int n2 = 17;
    int n3 = 13;
    Sampling s1 = new Sampling(n1);
    Sampling s2 = new Sampling(n2);
    Sampling s3 = new Sampling(n3);
    float d1 = 1.0f/(float)max(1,n1-1);
    float d2 = 1.0f/(float)max(1,n2-1);
    float d3 = 1.0f/(float)max(1,n3-1);
    float[][][] f = rampfloat(0.0f,d1,d2,d3,n1,n2,n3);

    PlotPanelPixels3 plot = new PlotPanelPixels3(
      PlotPanelPixels3.Orientation.X1RIGHT,
      PlotPanelPixels3.AxesPlacement.LEFT_BOTTOM,
      s1,s2,s3,f);
    plot.addColorBar();
    plot.setLabel1("axis 1");
    plot.setLabel2("axis 2");
    plot.setLabel3("axis 3");
    //plot.setInterval1(2);
    //plot.setInterval2(3);
    //plot.setInterval3(4);
    plot.setLineColor(null);
    //plot.setSlices(1,1,1);
    //plot.setSlices(n1-1,n2-1,n3-1);
    plot.setColorModel(ColorMap.JET);
    plot.setLineColor(Color.BLACK);
    //plot.setColorModel(ColorMap.GRAY);
    //plot.setLineColor(Color.YELLOW);
    plot.setInterpolation(PixelsView.Interpolation.NEAREST);

    PlotFrame frame = new PlotFrame(plot);
    frame.setFontSize(18);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    //frame.paintToPng(300,6,"junk.png");
  }

  private static void test2() {
    int n1 = 11;
    int n2 = 17;
    int n3 = 13;
    Sampling s1 = new Sampling(n1);
    Sampling s2 = new Sampling(n2);
    Sampling s3 = new Sampling(n3);
    //float d1 = 1.0f/(float)max(1,n1-1);
    //float d2 = 1.0f/(float)max(1,n2-1);
    //float d3 = 1.0f/(float)max(1,n3-1);
    //float[][][] f0 = rampfloat(0.0f,d1,d2,d3,n1,n2,n3);
    //float[][][] f1 = fillfloat(0.0f,n1,n2,n3);
    //float[][][] f2 = fillfloat(0.0f,n1,n2,n3);
    float[][][] f0 = randfloat(n1,n2,n3);
    float[][][] f1 = randfloat(n1,n2,n3);
    float[][][] f2 = randfloat(n1,n2,n3);
    float[][][][] f = new float[][][][]{f0,f1,f2};

    PlotPanelPixels3 plot = new PlotPanelPixels3(
      //PlotPanelPixels3.Orientation.X1RIGHT,
      PlotPanelPixels3.Orientation.X1DOWN,
      PlotPanelPixels3.AxesPlacement.LEFT_BOTTOM,
      s1,s2,s3,f);
    plot.setLabel1("axis 1");
    plot.setLabel2("axis 2");
    plot.setLabel3("axis 3");
    plot.setLineColor(Color.YELLOW);
    plot.setInterpolation(PixelsView.Interpolation.NEAREST);
    plot.setClips(0.0f,1.0f);

    PlotFrame frame = new PlotFrame(plot);
    frame.setFontSize(18);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    //frame.paintToPng(300,6,"junk.png");
  }
}
