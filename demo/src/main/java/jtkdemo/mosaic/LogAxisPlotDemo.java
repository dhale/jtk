/****************************************************************************
Copyright 2016, Colorado School of Mines.
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;
import edu.mines.jtk.mosaic.AxisScale;
import edu.mines.jtk.mosaic.GridView;
import edu.mines.jtk.mosaic.TiledView;
import edu.mines.jtk.util.ArrayMath;
import static edu.mines.jtk.util.MathPlus.*;
/**
 * Demo {@link edu.mines.jtk.mosaic.Mosaic} and associates.
 * @author Eric Addison
 * @version 2016.1.22
 */
public class LogAxisPlotDemo {
  
  public static GridView gv = null;  
  
  public static void main(String args[]) {

    System.out.println("LogAxisPlotDemo");
    int n = 1000;
    float X = 300;

    float[] x1 = new float[n];
    float[] x2 = new float[n];
    x1 = ArrayMath.rampfloat(0.0f,X / n,n);
    x2 = ArrayMath.rampfloat(0.0f,X / n,n);

    float[] f1 = new float[n];
    float[] f2 = new float[n];
    for (int i = 0; i < n; ++i) {
      f1[i] = pow(1.5f * x1[i],1);
      f2[i] = 100 * (float) sin(0.1 * x2[i]);
    }
 // new plot
    PlotPanel plot = new PlotPanel(1,1);

    // plain old linear plots
    PointsView pv1 = plot.addPoints(0,0,x1,f1);
    pv1.setLineColor(Color.BLUE);
    pv1.setScales(AxisScale.LOG10);
    
    PointsView pv2 = plot.addPoints(0,0,x2,f2);
    pv2.setLineColor(Color.RED);
    pv2.setScales(AxisScale.LOG10);

     
    // make some buttons
    JPanel buttPanel = new JPanel();
    buttPanel.add(changeHAxisButton(pv1,"Blue"));
    buttPanel.add(changeHAxisButton(pv2,"Red"));
    buttPanel.add(limitsTestButton(plot));
    buttPanel.add(limitsDefaultButton(plot));
    buttPanel.add(gridButton(plot));

    // frame setup
    plot.setVisible(true);
    PlotFrame frame = new PlotFrame(plot);
    frame.add(buttPanel,BorderLayout.SOUTH);
    frame.setSize(600,500);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    
    
    
  }

  public static JButton changeHAxisButton(final TiledView tv, final String label) {
    final JButton b = new JButton(label + " " + tv.getHScale());
    b.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (tv.getHScale() == AxisScale.LOG10)
          tv.setScales(AxisScale.LINEAR);
        else
          tv.setScales(AxisScale.LOG10);
        b.setText(label + " " + tv.getHScale());
        System.out.println(tv + ": " + tv.getHScale());
      }
    });
    return b;
  }

  public static JButton limitsTestButton(final PlotPanel plot){
    JButton b = new JButton("setLimits Test");
    b.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
        plot.setLimits(10,20,100,1000);
      }
    });
    return b;
  }
  
  public static JButton limitsDefaultButton(final PlotPanel plot){
    JButton b = new JButton("Default Limits");
    b.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
        plot.setLimitsDefault();
      }
    });
    return b; 
  }
  
  public static JButton gridButton(final PlotPanel plot){
      final JButton b = new JButton("Add Grid");
      b.addActionListener(new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent arg0) {
          if(gv==null){
            gv = plot.addGrid(0,0);
            b.setText("Remove Grid");
          }
          else{
            plot.remove(gv);
            gv=null;
            b.setText("Add Grid");
          }
        }
      });
      return b; 
    }

  public static float f1(double x) {
    return (float) (4 * x * Math.sin(10 / x)) + 10.0f;
  }

  public static float f2(double x) {
    return (float) Math.exp(x / 20);
  }

  // generate an array of log-spaced data, i.e between 10^a and 10^b with even
  // steps in the exponent
  public static float[] logSpace(double a, double b, int n) {
    float[] y = new float[n];
    double de = (b - a) / n;
    for (int i = 0; i < n; i++)
      y[i] = (float) Math.pow(10,a + i * de);
    return y;
  }

  // generate an array of evenly spaced data
  public static float[] linSpace(double a, double b, int n) {
    float[] y = new float[n];
    double de = (b - a) / n;
    for (int i = 0; i < n; i++)
      y[i] = (float) (a + i * de);
    return y;
  }

}
