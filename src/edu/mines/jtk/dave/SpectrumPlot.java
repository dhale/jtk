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
import static edu.mines.jtk.util.Array.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A plot of a sampled sequence x(t) and its amplitude and phase spectra.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.25
 */
public class SpectrumPlot extends PlotFrame {

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
    super(makePanelX(x),makePanelAP(x,db),PlotFrame.Split.VERTICAL);
    this.addButtons();
    this.setSize(950,600);
    this.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  private static PlotPanel makePanelX(Real1 x) {
    Check.argument(x.getSampling().isUniform(),"sampling of x is uniform");
    PlotPanel panel = new PlotPanel();
    panel.addSequence(x.getSampling(),x.getValues());
    panel.setHFormat("%1.6G");
    panel.setHLabel("time (s)");
    panel.setVLabel("amplitude");
    return panel;
  }
    
  private static PlotPanel makePanelAP(Real1 x, boolean db) {
    Real1[] ap = computeSpectra(x,db);
    Real1 a = ap[0];
    Real1 p = ap[1];
    PlotPanel panel = new PlotPanel(2,1);
    panel.addPoints(0,0,a.getSampling(),a.getValues());
    panel.addPoints(1,0,p.getSampling(),p.getValues());
    panel.setVLimits(1,-0.5,0.5);
    if (db) {
      panel.setVLabel(0,"amplitude (dB)");
    } else {
      panel.setVLabel(0,"amplitude");
    }
    panel.setVLabel(1,"phase (cycles)");
    panel.setHLabel("frequency (Hz)");
    return panel;
  }

  private void addButtons() {
    Action saveToPngAction = new AbstractAction("Save to PNG") {
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

  ///////////////////////////////////////////////////////////////////////////
  // private

  private PlotFrame _frame = this;

  private static Real1[] computeSpectra(Real1 x, boolean db) {
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
