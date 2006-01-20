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
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Test recursive Gaussian filter for 2-D images.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.11.25
 */
public class RgfTest {

  public static void main(String[] args) {
    double sigma = 4.0;
    if (args.length>0)
      sigma = Double.valueOf(args[0]);
    int n1 = 101;
    int n2 = 101;
    float[][] x = new float[n2][n1];
    float[][] y = new float[n2][n1];
    x[3][3] = x[3][n1-4] = x[n2-4][3] = x[n2-4][n1-4] = x[n2/2][n1/2] = 1.0f;
    plot(x);
    RecursiveGaussianFilter rgf = new RecursiveGaussianFilter(sigma);

    rgf.apply00(x,y);
    plot(y);

    rgf.apply10(x,y);
    plot(y);
    rgf.apply01(x,y);
    plot(y);

    rgf.apply11(x,y);
    plot(y);
    rgf.apply20(x,y);
    plot(y);
    rgf.apply02(x,y);
    plot(y);
  }
    

  private static void plot(float[][] f) {
    plot(Array.min(f),Array.max(f),f);
  }
  private static void plot(String filename, float[][] f) {
    plot(filename,Array.min(f),Array.max(f),f);
  }
  private static PlotFrame plot(
    float fmin, float fmax, float[][] f) 
  {
    PlotPanel panel = new PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT);
    PixelsView pv = panel.addPixels(f);
    pv.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv.setColorMap(PixelsView.ColorMap.GRAY);
    //pv.setClips(fmin,fmax);

    PlotFrame frame = new PlotFrame(panel);
    frame.setSize(550,500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    return frame;
  }
  private static void plot(
    String filename, float fmin, float fmax, float[][] f) 
  {
    PlotFrame frame = plot(fmin,fmax,f);
    frame.paintToPng(300,6,filename+".png");
  }
}
