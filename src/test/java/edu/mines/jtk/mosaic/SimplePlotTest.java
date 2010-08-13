/****************************************************************************
 Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
 This program and accompanying materials are made available under the terms of
 the Common Public License - v1.0, which accompanies this distribution, and is
 available at http://www.eclipse.org/legal/cpl-v10.html
 ****************************************************************************/
package edu.mines.jtk.mosaic;

import javax.swing.*;

import edu.mines.jtk.dsp.Sampling;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.mosaic.SimplePlot}
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.12.26
 */
public class SimplePlotTest {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        plot0();
        plot1();
        plot2();
        plot3();
      }
    });
  }
  private static void plot0() {
    float[] f = sin(rampfloat(0.0f,0.1f,63));
    SimplePlot.asSequence(f);
  }
  private static void plot1() {
    int nx = 301;
    float dx = 0.1f;
    float fx = 0.0f;
    Sampling sx = new Sampling(nx,dx,fx);
    float[] x = rampfloat(fx,dx,nx);
    float[] f = sub(mul(x,sin(x)),1.0f);
    SimplePlot.asPoints(sx,f);
  }
  private static void plot2() {
    float[][] f = sin(rampfloat(0.0f,0.1f,0.1f,101,101));
    SimplePlot.asPixels(f).addColorBar();
  }
  private static void plot3() {
    SimplePlot plot = new SimplePlot();
    plot.addGrid("H-.V-.");
    float[] f = sin(rampfloat(0.0f,0.1f,63));
    plot.addPoints(f).setStyle("r-o");
    float[] g = cos(rampfloat(0.0f,0.1f,63));
    plot.addPoints(g).setStyle("b-x");
    plot.setTitle("A simple plot of two arrays");
    plot.setVLabel("array value");
    plot.setHLabel("array index");
  }
}
