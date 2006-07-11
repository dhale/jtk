/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.demo;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.*;
import edu.mines.jtk.mosaic.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Impulse, amplitude and phase responses of filter with poles and zeros.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.11
 */
public class PolesAndZeros {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new PolesAndZeros();
      }
    });
  }

  public PolesAndZeros() {
    _poles = new ArrayList<Cdouble>(0);
    _zeros = new ArrayList<Cdouble>(0);
    _pzp = new PoleZeroPlot();
    _rp = new ResponsePlot(false);
  }

  public void addPole(Cdouble pole) {
    _poles.add(new Cdouble(pole));
    _pzp.updatePolesView();
  }

  public void removePole(Cdouble pole) {
    _poles.remove(pole);
    _pzp.updatePolesView();
  }

  public void movePole(Cdouble poleOld, Cdouble poleNew) {
    _poles.remove(poleOld);
    _poles.add(poleNew);
    _pzp.updatePolesView();
  }

  public ArrayList<Cdouble> getPoles() {
    return new ArrayList<Cdouble>(_poles);
  }

  public void setPoles(ArrayList<Cdouble> poles) {
    _poles = new ArrayList<Cdouble>(poles);
    _pzp.updatePolesView();
  }

  public Cdouble getPoleNearest(Cdouble z) {
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

  public void addZero(Cdouble zero) {
    _zeros.add(new Cdouble(zero));
    _pzp.updateZerosView();
  }

  public void removeZero(Cdouble zero) {
    _zeros.remove(zero);
    _pzp.updateZerosView();
  }

  public void moveZero(Cdouble zeroOld, Cdouble zeroNew) {
    _zeros.remove(zeroOld);
    _zeros.add(zeroNew);
    _pzp.updateZerosView();
  }

  public ArrayList<Cdouble> getZeros() {
    return new ArrayList<Cdouble>(_zeros);
  }

  public void setZeros(ArrayList<Cdouble> zeros) {
    _zeros = new ArrayList<Cdouble>(zeros);
    _pzp.updateZerosView();
  }

  public Cdouble getZeroNearest(Cdouble z) {
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

  private ArrayList<Cdouble> _poles;
  private ArrayList<Cdouble> _zeros;
  private PoleZeroPlot _pzp;
  private ResponsePlot _rp;

  ///////////////////////////////////////////////////////////////////////////

  private class PoleZeroPlot {
    PoleZeroPlot() {
      _plotPanel = new PlotPanel();
      _plotPanel.setTitle("poles and zeros");
      _plotPanel.setHLabel("real");
      _plotPanel.setVLabel("imaginary");
      _plotPanel.setHLimits(-3.0,3.0);
      _plotPanel.setVLimits(-3.0,3.0);

      _gridView = _plotPanel.addGrid("H0-V0-");
      float[][] circlePoints = makeCirclePoints();
      _circleView = _plotPanel.addPoints(circlePoints[0],circlePoints[1]);
      _circleView.setLineColor(Color.RED);

      updatePolesView();
      updateZerosView();

      _plotFrame = new PlotFrame(_plotPanel);
      ModeManager mm = _plotFrame.getModeManager();
      TileZoomMode tzm = _plotFrame.getTileZoomMode();
      PoleZeroMode pm = new PoleZeroMode(mm,false); // for poles
      PoleZeroMode zm = new PoleZeroMode(mm,true);  // for zeros

      JMenu fileMenu = new JMenu("File");
      fileMenu.setMnemonic('F');
      Action exitAction = new AbstractAction("Exit") {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent event) {
          System.exit(0);
        }
      };
      fileMenu.add(exitAction).setMnemonic('x');
      JMenu modeMenu = new JMenu("Mode");
      modeMenu.setMnemonic('M');
      modeMenu.add(new ModeMenuItem(tzm));
      modeMenu.add(new ModeMenuItem(pm));
      modeMenu.add(new ModeMenuItem(zm));
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenu);
      menuBar.add(modeMenu);

      JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
      toolBar.setRollover(true);
      toolBar.add(new ModeToggleButton(tzm));
      toolBar.add(new ModeToggleButton(pm));
      toolBar.add(new ModeToggleButton(zm));

      pm.setActive(true);

      _plotFrame.setJMenuBar(menuBar);
      _plotFrame.add(toolBar,BorderLayout.WEST);
      _plotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      _plotFrame.setSize(400,440);
      _plotFrame.setVisible(true);
    }

    public void updatePolesView() {
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

    public void updateZerosView() {
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

    private PlotFrame _plotFrame;
    private PlotPanel _plotPanel;
    private PointsView _polesView;
    private PointsView _zerosView;
    private PointsView _circleView;
    private GridView _gridView;

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

  private class ResponsePlot {

    public ResponsePlot(boolean db) {
      _db = db;

      _plotPanelX = new PlotPanel();
      _plotPanelX.setHLabel("sample index");
      _plotPanelX.setVLabel("amplitude");

      _plotPanelAP = new PlotPanel(2,1);
      _plotPanelAP.setVLimits(1,-0.5,0.5);
      if (db) {
        _plotPanelAP.setVLabel(0,"amplitude (dB)");
      } else {
        _plotPanelAP.setVLabel(0,"amplitude");
      }
      _plotPanelAP.setVLabel(1,"phase (cycles)");
      _plotPanelAP.setHLabel("frequency (cycles/sample)");

      _plotFrame = new PlotFrame(
        _plotPanelX,_plotPanelAP,PlotFrame.Split.VERTICAL);

      JMenu fileMenu = new JMenu("File");
      fileMenu.setMnemonic('F');
      Action exitAction = new AbstractAction("Exit") {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent event) {
          System.exit(0);
        }
      };
      fileMenu.add(exitAction).setMnemonic('x');
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenu);

      _plotFrame.setJMenuBar(menuBar);
      _plotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      _plotFrame.setSize(600,600);
      _plotFrame.setVisible(true);
    }

    public void updateViews() {
      int np = _poles.size();
      int nz = _zeros.size();
      Cdouble[] poles = new Cdouble[np];
      Cdouble[] zeros = new Cdouble[nz];
      _poles.toArray(poles);
      _zeros.toArray(zeros);
      float[] x = new float[1001];
      float[] y = new float[1001];
      x[0] = 1.0f;
      RecursiveCascadeFilter f = new RecursiveCascadeFilter(poles,zeros,1.0);
      f.applyForward(x,y);
      Real1[] ap = computeSpectra(x,_db);
      Real1 a = ap[0];
      Real1 p = ap[1];
      if (_xView==null) {
        _xView = _plotPanelX.addSequence(y);
        _aView = _plotPanelAP.addPoints(0,0,a.getSampling(),a.getValues());
        _pView = _plotPanelAP.addPoints(1,0,p.getSampling(),p.getValues());
      } else {
        _xView.set(y);
        _aView.set(a.getSampling(),a.getValues());
        _pView.set(p.getSampling(),p.getValues());
      }
    }

    private boolean _db;
    private PlotPanel _plotPanelX;
    private PlotPanel _plotPanelAP;
    private PlotFrame _plotFrame;
    private SequenceView _xView;
    private PointsView _aView;
    private PointsView _pView;

    // TODO
    private void addButtons() {
      Action saveToPngAction = new AbstractAction("Save to PNG") {
        public void actionPerformed(ActionEvent event) {
          JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
          fc.showSaveDialog(_plotFrame);
          File file = fc.getSelectedFile();
          if (file!=null) {
            String filename = file.getAbsolutePath();
            _plotFrame.paintToPng(300,6,filename);
          }
        }
      };
      JButton saveToPngButton = new JButton(saveToPngAction);
      JToolBar toolBar = new JToolBar();
      toolBar.add(saveToPngButton);
      _plotFrame.add(toolBar,BorderLayout.NORTH);
    }

    private Real1[] computeSpectra(float[] x, boolean db) {
      Sampling st = new Sampling(x.length,1.0,0.0);
      int nt = st.getCount();
      double dt = st.getDelta();
      double ft = st.getFirst();
      int nfft = FftReal.nfftSmall(5*nt);
      FftReal fft = new FftReal(nfft);
      int nf = nfft/2+1;
      double df = 1.0/(dt*nfft);
      double ff = 0.0;
      float[] cf = new float[2*nf];
      Array.copy(nt,x,cf);
      fft.realToComplex(-1,cf,cf);
      float[] wft = Array.rampfloat(0.0f,-2.0f*FLT_PI*(float)(df*ft),nf);
      cf = Array.cmul(cf,Array.cmplx(Array.cos(wft),Array.sin(wft)));
      float[] af = Array.cabs(cf);
      if (db) {
        float amax = Array.max(af);
        af = Array.mul(1.0f/amax,af);
        af = Array.log10(af);
        af = Array.mul(20.0f,af);
      }
      float[] pf = Array.carg(cf);
      pf = Array.mul(0.5f/FLT_PI,pf);
      Sampling sf = new Sampling(nf,df,ff);
      Real1 a = new Real1(sf,af);
      Real1 p = new Real1(sf,pf);
      return new Real1[]{a,p};
    }
  }

  ///////////////////////////////////////////////////////////////////////////

  private class PoleZeroMode extends Mode {
    private static final long serialVersionUID = 1L;

    public PoleZeroMode(ModeManager modeManager, boolean zeros) {
      super(modeManager);
      if (zeros) {
        _zeros = true;
        setName("Zeros");
        setIcon(loadIcon(PolesAndZeros.class,"resources/Zeros.gif"));
        setMnemonicKey(KeyEvent.VK_0);
        setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_0,0));
        setShortDescription("Add (Shift), remove (Ctrl), or drag zeros");
      } else {
        _poles = true;
        setName("Poles");
        setIcon(loadIcon(PolesAndZeros.class,"resources/Poles.gif"));
        setMnemonicKey(KeyEvent.VK_X);
        setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_X,0));
        setShortDescription("Add (Shift), remove (Ctrl), or drag poles");
      }
    }
    
    protected void setActive(Component component, boolean active) {
      if (component instanceof Tile) {
        if (active) {
          component.addMouseListener(_ml);
        } else {
          component.removeMouseListener(_ml);
        }
      }
    }

    private boolean _zeros; // true, if this mode is for zeros
    private boolean _poles; // true, if this mode is for poles
    private Cdouble _zedit; // if editing, last location in complex z plane
    private boolean _editing; // true, if currently editing
    private Tile _tile; // tile in which editing began

    private MouseListener _ml = new MouseAdapter() {;
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

    private MouseMotionListener _mml = new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        if (_editing)
          duringEdit(e);
      }
    };

    private Cdouble pointToComplex(int x, int y) {
      Transcaler ts = _tile.getTranscaler();
      Projector hp = _tile.getHorizontalProjector();
      Projector vp = _tile.getVerticalProjector();
      double xu = ts.x(x);
      double yu = ts.y(y);
      double xv = hp.v(xu);
      double yv = vp.v(yu);
      return new Cdouble(xv,yv);
    }

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

    private boolean closeEnough(int x, int y, Cdouble c) {
      Point p = complexToPoint(c); 
      return abs(p.x-x)<6 && abs(p.y-y)<6;
    }

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
    private void endEdit(MouseEvent e) {
      duringEdit(e);
      _editing = false;
    }
  }
}

