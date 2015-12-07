/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.mosaic.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Demos {@link edu.mines.jtk.mosaic.PlotFrame}
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.25
 */
public class PlotFrameDemo2 {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        go();
      }
    });
  }
  private static void go() {
    int n1 = 101;
    double d1 = 0.03;
    double f1 = -1.3;
    Sampling s1 = new Sampling(n1,d1,f1);
    int n2 = 101;
    double d2 = 0.029;
    double f2 = 0.033;
    Sampling s2 = new Sampling(n2,d2,f2);

    float[][] f = sin(rampfloat(0.0f,0.1f,0.1f,n1,n2));
    float ax = (float)(f2+d2*n2/2.0);
    float bx = (float)(0.45*d2*(n2-1));
    float cx = (float)(0.1/d1);
    float[] x1 = rampfloat((float)f1,(float)d1,n1);
    float[] x2 = add(ax,mul(bx,sin(mul(cx,x1))));

    PlotPanel.Orientation orientation = PlotPanel.Orientation.X1DOWN_X2RIGHT;
    PlotPanel panel = new PlotPanel(1,2,orientation);

    PixelsView pxv0 = panel.addPixels(0,0,s1,s2,f);
    PixelsView pxv1 = panel.addPixels(0,1,s1,s2,f);
    pxv0.setColorModel(ColorMap.GRAY);
    pxv1.setColorModel(ColorMap.JET);

    PointsView ptv0 = panel.addPoints(0,0,x1,x2);
    PointsView ptv1 = panel.addPoints(0,1,x1,x2);
    ptv0.setStyle("r--.");
    ptv1.setStyle("k-o");

    panel.addColorBar("Amplitude");
    panel.setVLabel("Depth (km)");
    panel.setHLabel(0,"Offset (km)");
    panel.setHLabel(1,"Velocity (km/s)");

    PlotFrame frame = new PlotFrame(panel);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    int type = 2;
    if (type==0) {
      frame.setFontSize(24);
      panel.setTitle("Graphic with 24-pt font");
    } else if (type==1) {
      frame.setFontSizeForPrint(8,240);
      panel.setTitle("Graphic for print");
    } else if (type==2) {
      frame.setFontSizeForSlide(1,1);
      panel.setTitle("Graphic for slide");
    }
    frame.setVisible(true);
    frame.paintToPng(720,3.333333,"junk.png");
  }
}
