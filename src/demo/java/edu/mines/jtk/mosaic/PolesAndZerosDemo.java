/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.Cdouble;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Interactive digital filter design with poles and zeros.
 * As we interactively add, remove, or move the poles and zeros, the
 * impulse, amplitude, and phase responses of the causal filter change
 * accordingly. Because the filter is constrained to be causal, it is
 * stable only when all poles (if any) lie inside the unit circle.
 * <p>
 * By running this program, we can learn something about digital filter
 * design. By looking at its source code, we can learn how to develop
 * similar interactive programs using the Mines Java Toolkit.
 * <p>
 * In particular, this program demonstrates how to write a new mode of
 * interaction. Here, we construct a mode for adding, removing, or moving 
 * the poles and zeros of our filter. 
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.11
 */
public class PolesAndZerosDemo {

  /**
   * Runs the program.
   * @param args arguments (ignored).
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new PolesAndZerosDemo();
      }
    });
  }

  // Location and size of pole-zero plot.
  private static final int PZP_X = 100;
  private static final int PZP_Y = 0;
  private static final int PZP_WIDTH = 520;
  private static final int PZP_HEIGHT = 550;

  // Location and size of response plot.
  private static final int RP_X = PZP_X+PZP_WIDTH;
  private static final int RP_Y = 0;
  private static final int RP_WIDTH = 500;
  private static final int RP_HEIGHT = 700;

  // This outer class has two lists for the poles and zeros, a plot for 
  // them, and a plot for the impulse, amplitude, and phase responses. 
  // When the lists are changed, say, by adding a pole, this class tells 
  // the plots to update themselves accordingly.
  private ArrayList<Cdouble> _poles;
  private ArrayList<Cdouble> _zeros;
  private PoleZeroPlot _pzp;
  private ResponsePlot _rp;

  private PolesAndZerosDemo() {
    _poles = new ArrayList<Cdouble>(0);
    _zeros = new ArrayList<Cdouble>(0);
    _pzp = new PoleZeroPlot();
    _rp = new ResponsePlot(false);
  }

  private void addPole(Cdouble pole) {
    _poles.add(new Cdouble(pole));
    if (!pole.isReal())
      _poles.add(pole.conj());
    _pzp.updatePolesView();
    _rp.updateViews();
  }

  private void removePole(Cdouble pole) {
    _poles.remove(pole);
    if (!pole.isReal())
      _poles.remove(pole.conj());
    _pzp.updatePolesView();
    _rp.updateViews();
  }

  private void movePole(Cdouble poleOld, Cdouble poleNew) {
    _poles.remove(poleOld);
    if (!poleOld.isReal())
      _poles.remove(poleOld.conj());
    _poles.add(poleNew);
    if (!poleNew.isReal())
      _poles.add(poleNew.conj());
    _pzp.updatePolesView();
    _rp.updateViews();
  }

  private Cdouble getPoleNearest(Cdouble z) {
    Cdouble pmin = null;
    double dmin = 0.0;
    for (Cdouble p: _poles) {
      double d = p.minus(z).abs();
      if (pmin==null || d<dmin) {
        pmin = p;
        dmin = d;
      }
    }
    return pmin;
  }

  private void addZero(Cdouble zero) {
    _zeros.add(new Cdouble(zero));
    if (!zero.isReal())
      _zeros.add(zero.conj());
    _pzp.updateZerosView();
    _rp.updateViews();
  }

  private void removeZero(Cdouble zero) {
    _zeros.remove(zero);
    if (!zero.isReal())
      _zeros.remove(zero.conj());
    _pzp.updateZerosView();
    _rp.updateViews();
  }

  private void moveZero(Cdouble zeroOld, Cdouble zeroNew) {
    _zeros.remove(zeroOld);
    if (!zeroOld.isReal())
      _zeros.remove(zeroOld.conj());
    _zeros.add(zeroNew);
    if (!zeroNew.isReal())
      _zeros.add(zeroNew.conj());
    _pzp.updateZerosView();
    _rp.updateViews();
  }

  private Cdouble getZeroNearest(Cdouble z) {
    Cdouble pmin = null;
    double dmin = 0.0;
    for (Cdouble p: _zeros) {
      double d = p.minus(z).abs();
      if (pmin==null || d<dmin) {
        pmin = p;
        dmin = d;
      }
    }
    return pmin;
  }

  ///////////////////////////////////////////////////////////////////////////

  // A plot for the poles and zeros. This inner plot class has access to 
  // the lists of poles and zeros in the outer poles-and-zeros class.
  private class PoleZeroPlot {

    private PlotFrame _plotFrame;
    private PlotPanel _plotPanel;
    private PointsView _polesView;
    private PointsView _zerosView;
    private PointsView _circleView;

    private PoleZeroPlot() {

      // The plot panel.
      _plotPanel = new PlotPanel();
      _plotPanel.setTitle("poles and zeros");
      _plotPanel.setHLabel("real");
      _plotPanel.setVLabel("imaginary");
      _plotPanel.setHLimits(-2.0,2.0);
      _plotPanel.setVLimits(-2.0,2.0);

      // A grid view for horizontal and vertical lines (axes).
      _plotPanel.addGrid("H0-V0-");

      // A points view for the unit circle.
      float[][] circlePoints = makeCirclePoints();
      _circleView = _plotPanel.addPoints(circlePoints[0],circlePoints[1]);
      _circleView.setLineColor(Color.RED);

      // These first updates construct points views for poles and zeros.
      // Subsequent updates simply modify the data displayed in the views.
      updatePolesView();
      updateZerosView();

      // A plot frame has a mode for zooming in tiles or tile axes.
      _plotFrame = new PlotFrame(_plotPanel);
      TileZoomMode tzm = _plotFrame.getTileZoomMode();

      // We add two more modes for editing poles and zeros.
      ModeManager mm = _plotFrame.getModeManager();
      PoleZeroMode pm = new PoleZeroMode(mm,true); // for poles
      PoleZeroMode zm = new PoleZeroMode(mm,false);  // for zeros

      // The menu bar includes a mode menu for selecting a mode.
      JMenu fileMenu = new JMenu("File");
      fileMenu.setMnemonic('F');
      fileMenu.add(new SaveAsPngAction(_plotFrame)).setMnemonic('a');
      fileMenu.add(new ExitAction()).setMnemonic('x');
      JMenu modeMenu = new JMenu("Mode");
      modeMenu.setMnemonic('M');
      modeMenu.add(new ModeMenuItem(tzm));
      modeMenu.add(new ModeMenuItem(pm));
      modeMenu.add(new ModeMenuItem(zm));
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenu);
      menuBar.add(modeMenu);
      _plotFrame.setJMenuBar(menuBar);

      // The tool bar includes toggle buttons for selecting a mode.
      JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
      toolBar.setRollover(true);
      toolBar.add(new ModeToggleButton(tzm));
      toolBar.add(new ModeToggleButton(pm));
      toolBar.add(new ModeToggleButton(zm));
      _plotFrame.add(toolBar,BorderLayout.WEST);

      // Initially, enable editing of poles.
      pm.setActive(true);

      // Make the plot frame visible.
      _plotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      _plotFrame.setLocation(PZP_X,PZP_Y);
      _plotFrame.setSize(PZP_WIDTH,PZP_HEIGHT);
      _plotFrame.setFontSizeForPrint(8,240);
      _plotFrame.setVisible(true);
    }

    // Makes poles view consistent with the list of poles.
    private void updatePolesView() {
      int np = _poles.size();
      float[] xp = new float[np];
      float[] yp = new float[np];
      for (int ip=0; ip<np; ++ip) {
        Cdouble p = _poles.get(ip);
        xp[ip] = (float)p.r;
        yp[ip] = (float)p.i;
      }
      if (_polesView==null) {
        _polesView = _plotPanel.addPoints(xp,yp);
        _polesView.setMarkStyle(PointsView.Mark.CROSS);
        _polesView.setLineStyle(PointsView.Line.NONE);
      } else {
        _polesView.set(xp,yp);
      }
    }

    // Makes zeros view consistent with the list of zeros.
    private void updateZerosView() {
      int nz = _zeros.size();
      float[] xz = new float[nz];
      float[] yz = new float[nz];
      for (int iz=0; iz<nz; ++iz) {
        Cdouble z = _zeros.get(iz);
        xz[iz] = (float)z.r;
        yz[iz] = (float)z.i;
      }
      if (_zerosView==null) {
        _zerosView = _plotPanel.addPoints(xz,yz);
        _zerosView.setMarkStyle(PointsView.Mark.HOLLOW_CIRCLE);
        _zerosView.setLineStyle(PointsView.Line.NONE);
      } else {
        _zerosView.set(xz,yz);
      }
    }

    // Makes array of points on the unit circle in the complex z-plane.
    private float[][] makeCirclePoints() {
      int nt = 1000;
      double dt = 2.0*DBL_PI/(nt-1);
      float[] x = new float[nt];
      float[] y = new float[nt];
      for (int it=0; it<nt; ++it) {
        float t = (float)(it*dt);
        x[it] = cos(t);
        y[it] = sin(t);
      }
      return new float[][]{x,y};
    }
  }

  ///////////////////////////////////////////////////////////////////////////

  // A plot for filter impulse, amplitude, and phase responses. This inner 
  // plot class has access to the lists of poles and zeros in the outer 
  // poles-and-zeros class.
  private class ResponsePlot {

    private boolean _db;
    private PlotPanel _plotPanelH;
    private PlotPanel _plotPanelAP;
    private PlotFrame _plotFrame;
    private SequenceView _hView;
    private PointsView _aView;
    private PointsView _pView;

    // The amplitude response can be in decibels (db).
    private ResponsePlot(boolean db) {
      _db = db;

      // One plot panel for the impulse response.
      _plotPanelH = new PlotPanel();
      _plotPanelH.setHLabel("sample index");
      _plotPanelH.setVLabel("amplitude");
      _plotPanelH.setTitle("impulse response");

      // Another plot panel for the amplitude and phase responses.
      // Amplitude and phase are both functions of frequency, so they
      // can share a horizontal axis.
      // The amplitude response is in tile (0,0) (on top).
      // The phase response is in tile (0,1) (on bottom).
      _plotPanelAP = new PlotPanel(2,1);
      _plotPanelAP.setTitle("amplitude and phase response");
      if (_db) {
        //_plotPanelAP.setVLimits(0,-100.0,0.0);
        _plotPanelAP.setVLabel(0,"amplitude (dB)");
      } else {
        //_plotPanelAP.setVLimits(0,0.0,1.0);
        _plotPanelAP.setVLabel(0,"amplitude");
      }
      _plotPanelAP.setVLimits(1,-0.5,0.5);
      _plotPanelAP.setVLabel(1,"phase (cycles)");
      _plotPanelAP.setHLabel("frequency (cycles/sample)");

      // This first update constructs a sequence view for the impulse 
      // response, and a points view for amplitude and phase responses.
      updateViews();

      // Both plot panels share a common frame, with the impulse response 
      // on top and the amplitude and phase responses on the bottom.
      _plotFrame = new PlotFrame(
        _plotPanelH,_plotPanelAP,PlotFrame.Split.VERTICAL);

      // The menu bar.
      JMenu fileMenu = new JMenu("File");
      fileMenu.setMnemonic('F');
      fileMenu.add(new SaveAsPngAction(_plotFrame)).setMnemonic('a');
      fileMenu.add(new ExitAction()).setMnemonic('x');
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenu);
      _plotFrame.setJMenuBar(menuBar);

      // Make the plot frame visible.
      _plotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      _plotFrame.setLocation(RP_X,RP_Y);
      _plotFrame.setSize(RP_WIDTH,RP_HEIGHT);
      _plotFrame.setFontSizeForPrint(8,240);
      _plotFrame.setVisible(true);
    }

    // Makes the views of the impulse, amplitude, and phase responses
    // consistent with the lists of poles and zeros.
    private void updateViews() {
      Real1 h = computeImpulseResponse();
      Real1[] ap = computeAmplitudeAndPhaseResponses();
      Real1 a = ap[0];
      Real1 p = ap[1];
      if (_hView==null) {
        _hView = _plotPanelH.addSequence(h.getSampling(),h.getValues());
        _aView = _plotPanelAP.addPoints(0,0,a.getSampling(),a.getValues());
        _pView = _plotPanelAP.addPoints(1,0,p.getSampling(),p.getValues());
      } else {
        _hView.set(h.getSampling(),h.getValues());
        _aView.set(a.getSampling(),a.getValues());
        _pView.set(p.getSampling(),p.getValues());
      }
    }

    // Computes the impulse response from poles and zeros.
    private Real1 computeImpulseResponse() {

      // Arrays of poles and zeros.
      int np = _poles.size();
      int nz = _zeros.size();
      Cdouble[] poles = new Cdouble[np];
      Cdouble[] zeros = new Cdouble[nz];
      _poles.toArray(poles);
      _zeros.toArray(zeros);

      // Array for impulse response h.
      int n = 101;
      float[] h = new float[n];

      // If at least one pole or zero, then construct and apply the filter.
      // Otherwise, the impulse response is simply an impulse.
      if (np>0 || nz>0) {
        float[] impulse = new float[n];
        impulse[0] = 1.0f;
        RecursiveCascadeFilter f = new RecursiveCascadeFilter(poles,zeros,1.0);
        f.applyForward(impulse,h);
      } else {
        h[0] = 1.0f;
      }

      Sampling s = new Sampling(n);
      return new Real1(s,h);
    }

    // Computes the amplitude and phase responses from poles and zeros.
    private Real1[] computeAmplitudeAndPhaseResponses() {

      // Frequency sampling from zero to one-half cycles/sample.
      int nf = 501;
      double df = 0.5/(nf-1);
      double ff = 0.0;
      Sampling sf = new Sampling(nf,df,ff);

      // Arrays for amplitude and phase functions of frequency.
      float[] af = new float[nf];
      float[] pf = new float[nf];

      // For all frequencies, ...
      for (int jf=0;  jf<nf; ++jf) {
        double f = ff+jf*df;
        Cdouble cone = new Cdouble(1.0,0.0);
        Cdouble zinv = Cdouble.polar(1.0,-2.0*DBL_PI*f);
        Cdouble p = new Cdouble(1.0,0.0);
        for (Cdouble c: _zeros)
          p.timesEquals(cone.minus(c.times(zinv)));
        Cdouble q = new Cdouble(1.0,0.0);
        for (Cdouble d: _poles)
          q.timesEquals(cone.minus(d.times(zinv)));
        Cdouble h = p.over(q);
        af[jf] = (float)h.abs();
        pf[jf] = (float)h.arg();
      }

      // Amplitude response, normalized.
      //float amax = max(max(af),FLT_EPSILON);
      //af = mul(1.0f/amax,af);
      if (_db) {
        af = log10(af);
        af = mul(20.0f,af);
      }
      Real1 a = new Real1(sf,af);

      // Phase response, in cycles.
      pf = mul(0.5f/FLT_PI,pf);
      Real1 p = new Real1(sf,pf);

      return new Real1[]{a,p};
    }

    // Computes the amplitude and phase responses from impulse response.
    // This method is fast, because it uses an FFT, but it yields errors
    // for long impulse responses that are truncated.
    /*
    private Real1[] computeAmplitudeAndPhaseResponses(Real1 h) {

      // Time sampling.
      Sampling st = h.getSampling();
      int nt = st.getCount();
      double dt = st.getDelta();
      double ft = st.getFirst();

      // Frequency sampling.
      int nfft = FftReal.nfftSmall(5*nt);
      int nf = nfft/2+1;
      double df = 1.0/(nfft*dt);
      double ff = 0.0;
      Sampling sf = new Sampling(nf,df,ff);

      // Real-to-complex fast Fourier transform.
      FftReal fft = new FftReal(nfft);
      float[] cf = new float[2*nf];
      copy(nt,h.getValues(),cf);
      fft.realToComplex(-1,cf,cf);

      // Adjust phase for possibly non-zero time of first sample.
      float[] wft = rampfloat(0.0f,-2.0f*FLT_PI*(float)(df*ft),nf);
      cf = cmul(cf,cmplx(cos(wft),sin(wft)));

      // Amplitude response, normalized.
      float[] af = cabs(cf);
      float amax = max(max(af),FLT_EPSILON);
      af = mul(1.0f/amax,af);
      if (_db) {
        af = log10(af);
        af = mul(20.0f,af);
      }
      Real1 a = new Real1(sf,af);

      // Phase response, in cycles.
      float[] pf = carg(cf);
      pf = mul(0.5f/FLT_PI,pf);
      Real1 p = new Real1(sf,pf);

      return new Real1[]{a,p};
    }
    */
  }

  ///////////////////////////////////////////////////////////////////////////

  // A mode for adding, removing, or moving poles and zeros. This inner
  // class accesses the lists of poles and zeros in the outer class. It
  // does not modify those lists directly. Instead, it edits the poles and 
  // zeros by calling methods in the outer class, so that those methods can 
  // update the relevant views.
  private class PoleZeroMode extends Mode {

    // Constructs a mode for editing either poles or zeros, depending on
    // whether the flag forPoles is true or false, respectively.
    public PoleZeroMode(ModeManager modeManager, boolean forPoles) {
      super(modeManager);
      if (forPoles) {
        _poles = true;
        setName("Poles");
        setIcon(loadIcon(PolesAndZerosDemo.class,"Poles16.png"));
        setMnemonicKey(KeyEvent.VK_X);
        setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_X,0));
        setShortDescription("Add (Shift), remove (Ctrl), or drag poles");
      } else {
        //_zeros = true;
        setName("Zeros");
        setIcon(loadIcon(PolesAndZerosDemo.class,"Zeros16.png"));
        setMnemonicKey(KeyEvent.VK_0);
        setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_0,0));
        setShortDescription("Add (Shift), remove (Ctrl), or drag zeros");
      }
    }
    
    // When this mode is activated (or deactivated) for a tile, it simply 
    // adds (or removes) its mouse listener to (or from) that tile.
    protected void setActive(Component component, boolean active) {
      if (component instanceof Tile) {
        if (active) {
          component.addMouseListener(_ml);
        } else {
          component.removeMouseListener(_ml);
        }
      }
    }

    //private boolean _zeros; // true, if this mode is for zeros
    private boolean _poles; // true, if this mode is for poles
    private Cdouble _zedit; // if editing, last location in complex z plane
    private boolean _editing; // true, if currently editing
    private Tile _tile; // tile in which editing began

    // Handles mouse pressed and released events.
    private MouseListener _ml = new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.isShiftDown()) {
          add(e);
        } else if (e.isControlDown()) {
          remove(e);
        } else {
          if (beginEdit(e)) {
            _editing = true;
            _tile.addMouseMotionListener(_mml);
          }
        }
      }
      public void mouseReleased(MouseEvent e) {
        if (_editing) {
          _tile.removeMouseMotionListener(_mml);
          endEdit(e);
          _editing = false;
        }
      }
    };

    // Handles mouse dragged events.
    private MouseMotionListener _mml = new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        if (_editing)
          duringEdit(e);
      }
    };

    // Converts an point (x,y) in pixels to a complex number z.
    private Cdouble pointToComplex(int x, int y) {
      Transcaler ts = _tile.getTranscaler();
      Projector hp = _tile.getHorizontalProjector();
      Projector vp = _tile.getVerticalProjector();
      double xu = ts.x(x);
      double yu = ts.y(y);
      double xv = hp.v(xu);
      double yv = vp.v(yu);
      return roundToReal(new Cdouble(xv,yv));
    }

    // Converts  complex number z to an point (x,y) in pixels.
    private Point complexToPoint(Cdouble z) {
      Transcaler ts = _tile.getTranscaler();
      Projector hp = _tile.getHorizontalProjector();
      Projector vp = _tile.getVerticalProjector();
      double xu = hp.u(z.r);
      double yu = vp.u(z.i);
      int xp = ts.x(xu);
      int yp = ts.y(yu);
      return new Point(xp,yp);
    }

    // If the specified complex number c is nearly on the real axis 
    // (within a small fixed number of pixels), then rounds this 
    // complex number to the nearest real number by setting the 
    // imaginary part to zero.
    private Cdouble roundToReal(Cdouble c) {
      Cdouble cr = new Cdouble(c.r,0.0);
      Point pr = complexToPoint(cr);
      Point p = complexToPoint(c);
      return (abs(p.y-pr.y)<6)?cr:c;
    }

    // Determines whether a specified point (x,y) is within a small
    // fixed number of pixels to the specified complex number c.
    private boolean closeEnough(int x, int y, Cdouble c) {
      Point p = complexToPoint(c); 
      return abs(p.x-x)<6 && abs(p.y-y)<6;
    }

    // Adds a pole or zero at mouse coordinates (x,y).
    private void add(MouseEvent e) {
      _tile = (Tile)e.getSource();
      int x = e.getX();
      int y = e.getY();
      Cdouble z = pointToComplex(x,y);
      if (_poles) {
        addPole(z);
      } else {
        addZero(z);
      }
    }

    // Removes a pole or zero, if mouse (x,y) is close enough to one.
    private void remove(MouseEvent e) {
      _tile = (Tile)e.getSource();
      int x = e.getX();
      int y = e.getY();
      Cdouble z = pointToComplex(x,y);
      if (_poles) {
        Cdouble pole = getPoleNearest(z);
        if (pole!=null && closeEnough(x,y,pole))
          removePole(pole);
      } else {
        Cdouble zero = getZeroNearest(z);
        if (zero!=null && closeEnough(x,y,zero))
          removeZero(zero);
      }
    }

    // Begins editing of an existing pole or zero, if close enough.
    // Returns true, if close enough so that we have begun editing; 
    // false, otherwise.
    private boolean beginEdit(MouseEvent e) {
      _tile = (Tile)e.getSource();
      int x = e.getX();
      int y = e.getY();
      Cdouble z = pointToComplex(x,y);
      if (_poles) {
        Cdouble pole = getPoleNearest(z);
        if (pole!=null && closeEnough(x,y,pole)) {
          movePole(pole,z);
          _zedit = z;
          return true;
        }
      } else {
        Cdouble zero = getZeroNearest(z);
        if (zero!=null && closeEnough(x,y,zero)) {
          moveZero(zero,z);
          _zedit = z;
          return true;
        }
      }
      return false;
    }

    // Called while a pole or zero is being dragged during edited.
    private void duringEdit(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      Cdouble z = pointToComplex(x,y);
      if (_poles) {
        movePole(_zedit,z);
      } else {
        moveZero(_zedit,z);
      }
      _zedit = z;
    }

    // Called when done editing a pole or zero.
    private void endEdit(MouseEvent e) {
      duringEdit(e);
      _editing = false;
    }
  }

  ///////////////////////////////////////////////////////////////////////////

  // Actions common to both plot frames.
  private class ExitAction extends AbstractAction {
    private ExitAction() {
      super("Exit");
    }
    public void actionPerformed(ActionEvent event) {
      System.exit(0);
    }
  }
  private class SaveAsPngAction extends AbstractAction {
    private PlotFrame _plotFrame;
    private SaveAsPngAction(PlotFrame plotFrame) {
      super("Save as PNG");
      _plotFrame = plotFrame;
    }
    public void actionPerformed(ActionEvent event) {
      JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
      fc.showSaveDialog(_plotFrame);
      File file = fc.getSelectedFile();
      if (file!=null) {
        String filename = file.getAbsolutePath();
        _plotFrame.paintToPng(300,6,filename);
      }
    }
  }
}
