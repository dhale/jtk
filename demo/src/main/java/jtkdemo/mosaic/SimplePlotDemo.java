/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
package jtkdemo.mosaic;

import javax.swing.*;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.mosaic.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Demos {@link edu.mines.jtk.mosaic.SimplePlot}
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.12.26
 */
public class SimplePlotDemo {

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
