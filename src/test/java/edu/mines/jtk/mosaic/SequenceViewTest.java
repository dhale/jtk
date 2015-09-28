/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
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

import java.awt.*;
import javax.swing.SwingUtilities;

import edu.mines.jtk.dsp.Sampling;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Test {@link edu.mines.jtk.mosaic.SequenceView} and associates.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.01.01
 */
public class SequenceViewTest {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        go();
      }
    });
  }
  private static void go() {
    int nx = 101;
    float dx = 0.1f;
    float fx = -0.5f*dx*(float)(nx-1);
    Sampling sx = new Sampling(nx,dx,fx);
    float[] f1 = rampfloat(fx,dx,nx);
    float[] f2 = add(0.5f,sin(f1));

    PlotPanel panel = new PlotPanel(2,1);

    SequenceView sv1 = panel.addSequence(0,0,sx,f1);
    sv1.setColor(Color.RED);

    SequenceView sv2 = panel.addSequence(1,0,sx,f2);
    sv2.setZero(SequenceView.Zero.MIDDLE);
    
    PlotFrame frame = new PlotFrame(panel);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setSize(950,500);
    frame.setVisible(true);
    frame.paintToPng(300,6,"junk.png");
  }
}
