package jtkdemo.mosaic;

import static edu.mines.jtk.util.MathPlus.pow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu.Separator;

import edu.mines.jtk.mosaic.AxisScale;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;
import edu.mines.jtk.util.ArrayMath;

public class LogAxisPlotDemo2 {

  public static PointsView pv1, pv2, pv3, pv4;
  
  public static void main(String args[]) {

    System.out.println("LogAxisPlotDemo2");
    int n = 1000;
    float X = 300;

    float[] x = ArrayMath.rampfloat(0.0f,X / n,n);

    float[] f = new float[n];
    for (int i = 0; i < n; ++i) {
      f[i] = pow(1.5f * x[i],2);
    }

    // new plot
    PlotPanel plot = new PlotPanel(2,2);
    pv1 = plot.addPoints(0,0,x,f);
    pv2 = plot.addPoints(0,1,x,f);
    pv3 = plot.addPoints(1,0,x,f);
    pv4 = plot.addPoints(1,1,x,f);
    
    
    // frame setup
    plot.setVisible(true);
    PlotFrame frame = new PlotFrame(plot);
    frame.setSize(800,500);
    frame.add(makeScaleOptionPanel(),BorderLayout.EAST);
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  
  
  public static JPanel makeScaleOptionPanel(){
    
    // create the side panel with scale change options
    JPanel optionPanel = new JPanel();
    optionPanel.add(Box.createVerticalStrut(20));
    optionPanel.setLayout(new BoxLayout(optionPanel,BoxLayout.Y_AXIS));
    optionPanel.add(makeScalePanel(pv1,"View (0,0)"));
    optionPanel.add(Box.createVerticalStrut(20));
    optionPanel.add(new Separator());
    optionPanel.add(makeScalePanel(pv2,"View (0,1)"));
    optionPanel.add(Box.createVerticalStrut(20));
    optionPanel.add(new Separator());
    optionPanel.add(makeScalePanel(pv3,"View (1,0)"));
    optionPanel.add(Box.createVerticalStrut(20));
    optionPanel.add(new Separator());
    optionPanel.add(makeScalePanel(pv4,"View (1,1)"));
    optionPanel.add(Box.createVerticalStrut(20));
    return optionPanel;
  }
  
  
  public static JPanel makeScalePanel(final PointsView pv, String title){
    AxisScale[] values = AxisScale.values();
    JPanel scalePanel = new JPanel();
    scalePanel.setLayout(new BoxLayout(scalePanel,BoxLayout.Y_AXIS));
    JPanel titlePanel = new JPanel();
    JLabel scaleLabel = new JLabel(title);
    titlePanel.add(Box.createHorizontalGlue());
    titlePanel.add(scaleLabel);
    titlePanel.add(Box.createHorizontalGlue());
    scalePanel.add(titlePanel);
    JPanel boxPanel = new JPanel();
    boxPanel.setLayout(new BoxLayout(boxPanel,BoxLayout.X_AXIS));
    JPanel labelPanel = new JPanel(new GridLayout(1,0));
    JLabel boxLabel1 = new JLabel("Hscale");
    JLabel boxLabel2 = new JLabel("Vscale");
    labelPanel.add(Box.createHorizontalGlue());
    labelPanel.add(boxLabel1);
    labelPanel.add(Box.createHorizontalGlue());
    labelPanel.add(boxLabel2);
    labelPanel.add(Box.createHorizontalGlue());
    JComboBox<AxisScale> cbox1 = new JComboBox<AxisScale>(values);
    JComboBox<AxisScale> cbox2 = new JComboBox<AxisScale>(values);
    cbox1.setMaximumSize(new Dimension(500,100));
    cbox2.setMaximumSize(new Dimension(500,100));
    cbox1.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JComboBox<?> box = (JComboBox<?>)arg0.getSource();
        AxisScale scale = (AxisScale)box.getSelectedItem();
        pv.setHScale(scale);
      }
    });
    cbox2.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JComboBox<?> box = (JComboBox<?>)arg0.getSource();
        AxisScale scale = (AxisScale)box.getSelectedItem();
        pv.setVScale(scale);
      }
    });
    boxPanel.add(cbox1);
    boxPanel.add(cbox2);
    scalePanel.add(labelPanel);
    scalePanel.add(boxPanel);
    return scalePanel;

  }
  
}


