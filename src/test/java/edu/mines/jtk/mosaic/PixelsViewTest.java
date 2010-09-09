/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.max;
import javax.swing.*;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.dsp.Sampling;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.mosaic.PixelsView}
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.10.04
 */
public class PixelsViewTest {

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
    int n2 = 11;
    float d1 = 1.0f/(float)max(1,n1-1);
    float d2 = 1.0f/(float)max(1,n2-1);
    float[][] f = rampfloat(0.0f,d1,d2,n1,n2);

    Sampling s1 = new Sampling(n1,0.5,0.25*(n1-1));
    Sampling s2 = new Sampling(n2,0.5,0.25*(n2-1));

    PlotPanel panel = new PlotPanel(1,2);
    PixelsView pv0 = panel.addPixels(0,0,f);
    pv0.setInterpolation(PixelsView.Interpolation.NEAREST);
    pv0.setColorModel(ColorMap.JET);
    pv0.setPercentiles(0.0f,100.0f);
    f = mul(10.0f,f);
    pv0.set(f);

    PixelsView pv0b = panel.addPixels(0,0,s1,s2,f);
    pv0b.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv0b.setColorModel(ColorMap.GRAY);
    pv0b.setPercentiles(0.0f,100.0f);

    PixelsView pv1 = panel.addPixels(0,1,f);
    pv1.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv1.setColorModel(ColorMap.GRAY);
    pv1.setPercentiles(0.0f,100.0f);
    f = mul(10.0f,f);
    pv1.set(f);

    PixelsView pv1b = panel.addPixels(0,1,s1,s2,f);
    pv1b.setInterpolation(PixelsView.Interpolation.NEAREST);
    pv1b.setColorModel(ColorMap.JET);
    pv1b.setPercentiles(0.0f,100.0f);

    PlotFrame frame = new PlotFrame(panel);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    //frame.paintToPng(300,6,"junk.png");
  }

  private static void test2() {
    int n1 = 11;
    int n2 = 11;
    float d1 = 1.0f/(float)max(1,n1-1);
    float d2 = 1.0f/(float)max(1,n2-1);
    float[][] f0 = rampfloat(0.0f,d1,d2,n1,n2);
    float[][] f1 = zerofloat(n1,n2);
    float[][] f2 = zerofloat(n1,n2);
    //float[][] f1 = rampfloat(0.0f,d1,d2,n1,n2);
    //float[][] f2 = rampfloat(0.0f,d1,d2,n1,n2);

    Sampling s1 = new Sampling(n1,0.5,0.25*(n1-1));
    Sampling s2 = new Sampling(n2,0.5,0.25*(n2-1));

    PlotPanel panel = new PlotPanel(1,2);
    PixelsView pv0 = panel.addPixels(0,0,new float[][][]{f0,f1,f2});
    pv0.setInterpolation(PixelsView.Interpolation.NEAREST);
    pv0.setClips(0,0.0f,2.0f);
    pv0.setClips(1,0.0f,2.0f);
    pv0.setClips(2,0.0f,2.0f);
    pv0.set(new float[][][]{f0,f1,f2}); // should have no effect!

    PixelsView pv0b = panel.addPixels(0,0,s1,s2,new float[][][]{f1,f0,f2});
    pv0b.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv0b.setClips(0,0.0f,2.0f);
    pv0b.setClips(1,0.0f,2.0f);
    pv0b.setClips(2,0.0f,2.0f);

    PixelsView pv1 = panel.addPixels(0,1,new float[][][]{f1,f2,f0});
    pv1.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv1.setClips(0,0.0f,2.0f);
    pv1.setClips(1,0.0f,2.0f);
    pv1.setClips(2,0.0f,2.0f);

    PixelsView pv1b = panel.addPixels(0,1,s1,s2,new float[][][]{f1,f0,f2});
    pv1b.setInterpolation(PixelsView.Interpolation.NEAREST);
    pv1b.setClips(0,0.0f,2.0f);
    pv1b.setClips(1,0.0f,2.0f);
    pv1b.setClips(2,0.0f,2.0f);

    PlotFrame frame = new PlotFrame(panel);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    //frame.paintToPng(300,6,"junk.png");
  }
}
