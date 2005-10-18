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
import edu.mines.jtk.gui.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;
import static edu.mines.jtk.mosaic.Mosaic.*;

/**
 * A plot of one or more sampled sequences.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.09.19
 */
public class SequencePlot {

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
    Check.argument(as.length==av.length,"as.length==av.length");

    final JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    int nv = av.length;
    Set<AxesPlacement> axesPlacement = EnumSet.of(
      AxesPlacement.LEFT,
      AxesPlacement.BOTTOM);
    BorderStyle borderStyle = BorderStyle.FLAT;
    final Mosaic mosaic = new Mosaic(nv,1,axesPlacement,borderStyle);
    mosaic.setPreferredSize(new Dimension(850,min(nv*200,700)));
    mosaic.setFont(new Font("SansSerif",Font.PLAIN,18));
    mosaic.setBackground(Color.WHITE);
    mosaic.getTileAxisBottom(0).setLabel("sample index");
    ModeManager modeManager = new ModeManager();
    mosaic.setModeManager(modeManager);
    TileZoomMode tileZoomMode = new TileZoomMode(modeManager);
    tileZoomMode.setActive(true);
    for (int iv=0; iv<nv; ++iv) {
      Sampling s = as[iv];
      float[] v = av[iv];
      String l = al[iv];
      mosaic.getTileAxisLeft(iv).setLabel(l);
      mosaic.getTile(iv,0).addTiledView(new SequenceView(s,v));
    }
    frame.add(mosaic,BorderLayout.CENTER);

    Action saveAction = new AbstractAction("Save PNG image") {
      public void actionPerformed(ActionEvent event) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.showSaveDialog(frame);
        File file = fc.getSelectedFile();
        String filename = file.getAbsolutePath();
        try {
          mosaic.paintToPng(200,6,filename);
        } catch (IOException ioe) {
          System.out.println("Cannot write image to file: "+filename);
        }
      }
    };
    JButton saveButton = new JButton(saveAction);
    JToolBar toolBar = new JToolBar();
    toolBar.add(saveButton);
    frame.add(toolBar,BorderLayout.NORTH);

    frame.pack();
    frame.setVisible(true);
  }
}
