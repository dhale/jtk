/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.demo;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;
import static edu.mines.jtk.mosaic.Mosaic.*;

/**
 * A plot of a sampled sequence x(t) and its amplitude and phase spectra.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.25
 */
public class SpectrumPlot extends JFrame {

    public static void main(String[] args) {
      //Real1 x = readData("data/bob1.txt");
      Real1 x = readData("data/dylan1.txt");
      SpectrumPlot plot = new SpectrumPlot(x);
      /*
      int nt = 101;
      double dt = 1.0f;
      double ft = 0.0f;
      float[] xt = new float[nt];
      xt[0] = xt[1] = xt[2] = xt[3] = xt[4] = xt[5] = 1.0f;
      Real1 x = new Real1(nt,dt,ft,xt);
      SpectrumPlot plot = new SpectrumPlot(x);
      */
    }

    public static Real1 readData(String fileName) {
      try {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        int nt = 0;
        double dt = 0.0;
        double ft = 0.0;
        for (String line=br.readLine(); line!=null; line=br.readLine()) {
          if (line.indexOf("nt =")>=0) {
            Scanner s = new Scanner(line.substring(5));
            nt = s.nextInt();
            s = new Scanner(br.readLine().substring(5));
            dt = s.nextDouble();
            s = new Scanner(br.readLine().substring(5));
            ft = s.nextDouble();
            //System.out.println("nt="+nt+" dt="+dt+" ft="+ft);
            br.readLine();
            break;
          }
        }
        float[] xt = new float[nt];
        for (int it=0; it<nt; ++it) {
          String line = br.readLine();
          xt[it] = Float.parseFloat(line);
          //System.out.println("xt["+it+"]="+xt[it]);
        }
        br.close();
        return new Real1(nt,dt,ft,xt);
      } catch (IOException ioe) {
        throw new RuntimeException("Error reading data from file: "+fileName);
      }
    }

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
      Check.argument(x.getSampling().isUniform(),"sampling of x is uniform");

    // Amplitude and phase spectra.
    Real1[] ap = computeSpectra(x);
    Real1 a = ap[0];
    Real1 p = ap[1];

    // Mosaic for sampled sequence x(t).
    Mosaic mosaicX = new Mosaic(1,1,AXES_PLACEMENT,BORDER_STYLE);
    mosaicX.setBackground(BACKGROUND_COLOR);
    mosaicX.setFont(FONT);
    mosaicX.setPreferredSize(new Dimension(WIDTH,1*HEIGHT/3));
    Tile tileX = mosaicX.getTile(0,0);
    LollipopView xv = new LollipopView(x.getSampling(),x.getF());
    tileX.addTiledView(xv);
    TileAxis axisXT = mosaicX.getTileAxisBottom(0);
    TileAxis axisXA = mosaicX.getTileAxisLeft(0);
    axisXA.setLabel("Amplitude");
    axisXT.setLabel("Time (s)");
    axisXT.setFormat("%1.6G");
    
    // Mosaic for amplitude A(f) and phase P(f).
    Mosaic mosaicS = new Mosaic(2,1,AXES_PLACEMENT,BORDER_STYLE);
    mosaicS.setBackground(BACKGROUND_COLOR);
    mosaicS.setFont(FONT);
    mosaicS.setPreferredSize(new Dimension(WIDTH,2*HEIGHT/3));
    Tile tileA = mosaicS.getTile(0,0);
    Tile tileP = mosaicS.getTile(1,0);
    MarkLineView av = new MarkLineView(a.getSampling(),a.getF());
    MarkLineView pv = new MarkLineView(p.getSampling(),p.getF());
    tileA.addTiledView(av);
    tileP.addTiledView(pv);
    tileP.setBestVerticalProjector(new Projector(0.5,-0.5));
    TileAxis axisSA = mosaicS.getTileAxisLeft(0);
    TileAxis axisSP = mosaicS.getTileAxisLeft(1);
    TileAxis axisSF = mosaicS.getTileAxisBottom(0);
    axisSA.setLabel("Amplitude");
    axisSP.setLabel("Phase (cycles)");
    axisSF.setLabel("Frequency (Hz)");

    // Modes.
    ModeManager modeManager = new ModeManager();
    mosaicX.setModeManager(modeManager);
    mosaicS.setModeManager(modeManager);
    TileZoomMode zoomMode = new TileZoomMode(modeManager);
    zoomMode.setActive(true);

    /*
    // Menu.
    JMenuBar menuBar = new JMenuBar();
    JMenu modeMenu = new JMenu("Mode");
    modeMenu.setMnemonic(KeyEvent.VK_M);
    JMenuItem zoomItem = new ModeMenuItem(zoomMode);
    modeMenu.add(zoomItem);
    menuBar.add(modeMenu);

    // ToolBar.
    JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
    JToggleButton zoomButton = new ModeToggleButton(zoomMode);
    toolBar.add(zoomButton);
    */

    JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,mosaicS,mosaicX);
    jsp.setOneTouchExpandable(true);
    jsp.setResizeWeight(0.7);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //this.setJMenuBar(menuBar);
    //this.add(toolBar,BorderLayout.WEST);
    this.add(jsp,BorderLayout.CENTER);
    this.pack();
    this.setVisible(true);
  }

  private static MarkLineView makeMarkLineView(
    int nx, double dx, double fx, float[] f) 
  {
    Sampling sx = new Sampling(nx,dx,fx);
    MarkLineView mlv = new MarkLineView(sx,f);
    return mlv;
  }

  private static LollipopView makeLollipopView(
    int nx, double dx, double fx, float[] f) 
  {
    Sampling sx = new Sampling(nx,dx,fx);
    LollipopView lv = new LollipopView(sx,f);
    return lv;
  }

  private Real1[] computeSpectra(Real1 x) {
    Sampling st = x.getSampling();
    int nt = st.getCount();
    double dt = st.getDelta();
    double ft = st.getFirst();
    float[] xt = x.getF();
    int nfft = FftReal.nfftSmall(4*nt);
    FftReal fft = new FftReal(nfft);
    int nf = nfft/2+1;
    double df = 1.0/(dt*nfft);
    double ff = 0.0;
    float[] cf = new float[2*nf];
    Rap.copy(nt,xt,cf);
    fft.realToComplex(-1,cf,cf);
    float[] wft = Rap.ramp(0.0f,-2.0f*FLT_PI*(float)(df*ft),nf);
    cf = Cap.mul(cf,Cap.complex(Rap.cos(wft),Rap.sin(wft)));
    float[] af = Cap.abs(cf);
    float[] pf = Cap.arg(cf);
    pf = Rap.mul(0.5f/FLT_PI,pf);
    Sampling sf = new Sampling(nf,df,ff);
    Real1 a = new Real1(sf,af);
    Real1 p = new Real1(sf,pf);
    return new Real1[]{a,p};
  }
}
