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
 * A plot of one or more sampled sequences.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.09.19
 */
public class SequencePlot extends PlotFrame {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a plot for one sampled sequence.
   * Assigns a default label and sampling to the sequence.
   * @param v1 array of values for sequence 1.
   */
  public SequencePlot(float[] v1) {
    this("x1",new Sampling(v1.length,1,0),v1);
  }

  /**
   * Constructs a plot for two sampled sequences.
   * Assigns a default label and sampling to each sequence.
   * @param v1 array of values for sequence 1.
   * @param v2 array of values for sequence 2.
   */
  public SequencePlot(float[] v1, float[] v2) {
    this(
      "x1",new Sampling(v1.length,1,0),v1,
      "x2",new Sampling(v2.length,1,0),v2);
  }

  /**
   * Constructs a plot for three sampled sequences.
   * Assigns a default label and sampling to each sequence.
   * @param v1 array of values for sequence 1.
   * @param v2 array of values for sequence 2.
   * @param v3 array of values for sequence 3.
   */
  public SequencePlot(float[] v1, float[] v2, float[] v3) {
    this(
      "sequence 1",new Sampling(v1.length,1,0),v1,
      "sequence 2",new Sampling(v2.length,1,0),v2,
      "sequence 3",new Sampling(v3.length,1,0),v3);
  }

  /**
   * Constructs a plot for one sampled sequence.
   * @param l1 label for sequence 1.
   * @param s1 sampling for sequence 1.
   * @param v1 array of values for sequence 1.
   */
  public SequencePlot(String l1, Sampling s1, float[] v1) {
    this(new String[]{l1},new Sampling[]{s1},new float[][]{v1});
  }

  /**
   * Constructs a plot for two sampled sequences.
   * @param l1 label for sequence 1.
   * @param s1 sampling for sequence 1.
   * @param v1 array of values for sequence 1.
   * @param l2 label for sequence 2.
   * @param s2 sampling for sequence 2.
   * @param v2 array of values for sequence 2.
   */
  public SequencePlot(
    String l1, Sampling s1, float[] v1,
    String l2, Sampling s2, float[] v2) 
  {
    this(new String[]{l1,l2},new Sampling[]{s1,s2},new float[][]{v1,v2});
  }

  /**
   * Constructs a plot for three sampled sequences.
   * @param l1 label for sequence 1.
   * @param s1 sampling for sequence 1.
   * @param v1 array of values for sequence 1.
   * @param l2 label for sequence 2.
   * @param s2 sampling for sequence 2.
   * @param v2 array of values for sequence 2.
   * @param l3 label for sequence 3.
   * @param s3 sampling for sequence 3.
   * @param v3 array of values for sequence 3.
   */
  public SequencePlot(
    String l1, Sampling s1, float[] v1,
    String l2, Sampling s2, float[] v2,
    String l3, Sampling s3, float[] v3) 
  {
    this(new String[]{l1,l2,l3},
         new Sampling[]{s1,s2,s3},
         new float[][]{v1,v2,v3});
  }

  /**
   * Constructs a plot for multiple sampled sequences.
   * @param al array of sequence labels.
   * @param as array of sequence samplings.
   * @param av array of arrays of sequence values.
   */
  public SequencePlot(String[] al, Sampling[] as, float[][] av) {
    super(makePanel(al,as,av));
    this.addButtons();
    this.setSize(950,200*av.length);
    this.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Paints this panel to a PNG image with specified resolution and width.
   * The image height is computed so that the image has the same aspect 
   * ratio as this panel.
   * @param dpi the image resolution, in dots per inch.
   * @param win the image width, in inches.
   * @param fileName the name of the file to contain the PNG image.  
   */
  public void paintToPng(double dpi, double win, String fileName) {
    super.paintToPng(dpi,win,fileName);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private PlotFrame _frame = this;

  private static PlotPanel makePanel(
    String[] al, Sampling[] as, float[][] av) 
  {
    Check.argument(al.length==av.length,"al.length==av.length");
    Check.argument(as.length==av.length,"as.length==av.length");
    int nv = av.length;
    PlotPanel panel = new PlotPanel(nv,1);
    panel.setHLabel("sample index");
    for (int iv=0; iv<nv; ++iv) {
      Sampling s = as[iv];
      float[] v = av[iv];
      String l = al[iv];
      panel.setVLabel(iv,l);
      panel.addSequence(iv,0,s,v);
    }
    return panel;
  }

  private void addButtons() {
    Action saveToPngAction = new AbstractAction("Save to PNG") {
      private static final long serialVersionUID = 1L;
      public void actionPerformed(ActionEvent event) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.showSaveDialog(_frame);
        File file = fc.getSelectedFile();
        if (file!=null) {
          String filename = file.getAbsolutePath();
          _frame.paintToPng(300,6,filename);
        }
      }
    };
    JButton saveToPngButton = new JButton(saveToPngAction);
    JToolBar toolBar = new JToolBar();
    toolBar.add(saveToPngButton);
    this.add(toolBar,BorderLayout.NORTH);
  }
}
