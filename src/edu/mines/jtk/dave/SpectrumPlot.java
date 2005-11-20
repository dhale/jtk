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
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.gui.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.Array.*;
import static edu.mines.jtk.util.MathPlus.*;
import static edu.mines.jtk.mosaic.Mosaic.*;

/**
 * A plot of a sampled sequence x(t) and its amplitude and phase spectra.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.25
 */
public class SpectrumPlot extends JFrame {

  /**
   * Plot width, in pixels.
   */
  public static int WIDTH = 950;

  /**
   * Plot height, in pixels.
   */
  public static int HEIGHT = 600;

  // Attributes shared by all mosaics in plot.
  private static Set<AxesPlacement> AXES_PLACEMENT = EnumSet.of(
    AxesPlacement.LEFT,
    AxesPlacement.BOTTOM
  );
  private static BorderStyle BORDER_STYLE = BorderStyle.FLAT;
  private static Font FONT = new Font("SansSerif",Font.PLAIN,12);
  private static Color BACKGROUND_COLOR = Color.WHITE;

  /**
   * Constructs a new spectrum plot for the specified sampled sequence.
   * @param x the sampled sequence x(t). The sampling must be uniform.
   */
  public SpectrumPlot(Real1 x) {
    this(x,false);
  }

  /**
   * Constructs a new spectrum plot for the specified sampled sequence.
   * @param x the sampled sequence x(t). The sampling must be uniform.
   * @param db true, to plot amplitude spectrum in dB.
   */
  public SpectrumPlot(Real1 x, boolean db) {
    Check.argument(x.getSampling().isUniform(),"sampling of x is uniform");
    _frame = this;

    // Amplitude and phase spectra.
    Real1[] ap = computeSpectra(x,db);
    Real1 a = ap[0];
    Real1 p = ap[1];

    // Mosaic for sampled sequence x(t).
    _mosaicX = new Mosaic(1,1,AXES_PLACEMENT,BORDER_STYLE);
    _mosaicX.setBackground(BACKGROUND_COLOR);
    _mosaicX.setFont(FONT);
    _mosaicX.setPreferredSize(new Dimension(WIDTH,1*HEIGHT/3));
    Tile tileX = _mosaicX.getTile(0,0);
    SequenceView xv = new SequenceView(x.getSampling(),x.getValues());
    tileX.addTiledView(xv);
    TileAxis axisXT = _mosaicX.getTileAxisBottom(0);
    TileAxis axisXA = _mosaicX.getTileAxisLeft(0);
    axisXA.setLabel("Amplitude");
    axisXT.setLabel("Time (s)");
    axisXT.setFormat("%1.6G");
    
    // Mosaic for amplitude A(f) and phase P(f).
    _mosaicS = new Mosaic(2,1,AXES_PLACEMENT,BORDER_STYLE);
    _mosaicS.setBackground(BACKGROUND_COLOR);
    _mosaicS.setFont(FONT);
    _mosaicS.setPreferredSize(new Dimension(WIDTH,2*HEIGHT/3));
    Tile tileA = _mosaicS.getTile(0,0);
    Tile tileP = _mosaicS.getTile(1,0);
    MarkLineView av = new MarkLineView(a.getSampling(),a.getValues());
    MarkLineView pv = new MarkLineView(p.getSampling(),p.getValues());
    tileA.addTiledView(av);
    tileP.addTiledView(pv);
    tileP.setBestVerticalProjector(new Projector(0.5,-0.5));
    TileAxis axisSA = _mosaicS.getTileAxisLeft(0);
    TileAxis axisSP = _mosaicS.getTileAxisLeft(1);
    TileAxis axisSF = _mosaicS.getTileAxisBottom(0);
    if (db) {
      axisSA.setLabel("Amplitude (dB)");
    } else {
      axisSA.setLabel("Amplitude");
    }
    axisSP.setLabel("Phase (cycles)");
    axisSF.setLabel("Frequency (Hz)");

    // Modes.
    ModeManager modeManager = new ModeManager();
    _mosaicX.setModeManager(modeManager);
    _mosaicS.setModeManager(modeManager);
    TileZoomMode zoomMode = new TileZoomMode(modeManager);
    zoomMode.setActive(true);

    JSplitPane jsp = new JSplitPane(
      JSplitPane.VERTICAL_SPLIT,_mosaicS,_mosaicX);
    jsp.setOneTouchExpandable(true);
    jsp.setResizeWeight(0.7);

    Action saveSpectraAction = new AbstractAction("Save spectra to PNG") {
      public void actionPerformed(ActionEvent event) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.showSaveDialog(_frame);
        File file = fc.getSelectedFile();
        if (file!=null) {
          String filename = file.getAbsolutePath();
          saveSpectraToPng(filename);
        }
      }
    };
    Action saveSequenceAction = new AbstractAction("Save sequence to PNG") {
      public void actionPerformed(ActionEvent event) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.showSaveDialog(_frame);
        File file = fc.getSelectedFile();
        if (file!=null) {
          String filename = file.getAbsolutePath();
          saveSequenceToPng(filename);
        }
      }
    };
    JButton saveSpectraButton = new JButton(saveSpectraAction);
    JButton saveSequenceButton = new JButton(saveSequenceAction);
    JToolBar toolBar = new JToolBar();
    toolBar.add(saveSpectraButton);
    toolBar.add(saveSequenceButton);
    this.add(toolBar,BorderLayout.NORTH);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //this.setJMenuBar(menuBar);
    //this.add(toolBar,BorderLayout.WEST);
    this.add(jsp,BorderLayout.CENTER);
    this.pack();
    this.setVisible(true);
  }

  /**
   * Save sequence as PNG image in specified file.
   * @param filename the file name.
   */
  public void saveSequenceToPng(String filename) {
    try {
      _mosaicX.paintToPng(300,6,filename);
    } catch (IOException ioe) {
      System.out.println("Cannot write image to file: "+filename);
    }
  }

  /**
   * Save spectra as PNG image in specified file.
   * @param filename the file name.
   */
  public void saveSpectraToPng(String filename) {
    try {
      _mosaicS.paintToPng(300,6,filename);
    } catch (IOException ioe) {
      System.out.println("Cannot write image to file: "+filename);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private JFrame _frame;
  private Mosaic _mosaicS;
  private Mosaic _mosaicX;

  private static MarkLineView makeMarkLineView(
    int nx, double dx, double fx, float[] f) 
  {
    Sampling sx = new Sampling(nx,dx,fx);
    MarkLineView mlv = new MarkLineView(sx,f);
    return mlv;
  }

  private static SequenceView makeSequenceView(
    int nx, double dx, double fx, float[] f) 
  {
    Sampling sx = new Sampling(nx,dx,fx);
    SequenceView sv = new SequenceView(sx,f);
    return sv;
  }

  private Real1[] computeSpectra(Real1 x, boolean db) {
    Sampling st = x.getSampling();
    int nt = st.getCount();
    double dt = st.getDelta();
    double ft = st.getFirst();
    float[] xt = x.getValues();
    int nfft = FftReal.nfftSmall(5*nt);
    FftReal fft = new FftReal(nfft);
    int nf = nfft/2+1;
    double df = 1.0/(dt*nfft);
    double ff = 0.0;
    float[] cf = new float[2*nf];
    copy(nt,xt,cf);
    fft.realToComplex(-1,cf,cf);
    float[] wft = rampfloat(0.0f,-2.0f*FLT_PI*(float)(df*ft),nf);
    cf = cmul(cf,cmplx(cos(wft),sin(wft)));
    float[] af = cabs(cf);
    if (db) {
      float amax = max(af);
      af = mul(1.0f/amax,af);
      af = log10(af);
      af = mul(20.0f,af);
    }
    float[] pf = carg(cf);
    pf = mul(0.5f/FLT_PI,pf);
    Sampling sf = new Sampling(nf,df,ff);
    Real1 a = new Real1(sf,af);
    Real1 p = new Real1(sf,pf);
    return new Real1[]{a,p};
  }
}
