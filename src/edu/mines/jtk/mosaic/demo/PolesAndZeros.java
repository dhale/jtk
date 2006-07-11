/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic.demo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.*;
import edu.mines.jtk.mosaic.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A simple demonstration of {@link edu.mines.jtk.mosaic.PlotFrame}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.03
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
    _pzp = new PoleZeroPlot();
  }
  private PoleZeroPlot _pzp;
  //private FilterPlot

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

      setPoles(new ArrayList<Cdouble>(0));

      _plotFrame = new PlotFrame(_plotPanel);
      _plotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      _plotFrame.setSize(400,440);
      _plotFrame.setVisible(true);

      addModes();
    }

    public void addPole(Cdouble pole) {
      _poles.add(new Cdouble(pole));
      updatePolesView();
    }

    public void removePole(Cdouble pole) {
      _poles.remove(pole);
      updatePolesView();
    }

    public void movePole(Cdouble poleOld, Cdouble poleNew) {
      _poles.remove(poleOld);
      _poles.add(poleNew);
      updatePolesView();
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

    public ArrayList<Cdouble> getPoles() {
      return new ArrayList<Cdouble>(_poles);
    }

    public void setPoles(ArrayList<Cdouble> poles) {
      _poles = new ArrayList<Cdouble>(poles);
      updatePolesView();
    }

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

    private ArrayList<Cdouble> _poles;
    private ArrayList<Cdouble> _zeros;
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

    private void addModes() {
      ModeManager mm = _plotPanel.getMosaic().getModeManager();
      PoleZeroMode pm = new PoleZeroMode(mm,this,false); // for poles
      PoleZeroMode zm = new PoleZeroMode(mm,this,true);  // for zeros
      pm.setActive(true);
    }
  }

  private class PoleZeroMode extends Mode {
    private static final long serialVersionUID = 1L;

    public PoleZeroMode(
      ModeManager modeManager, PoleZeroPlot pzp, boolean zeros) 
    {
      super(modeManager);
      if (zeros) {
        _zeros = true;
        setName("Zeros");
        setIcon(loadIcon(PoleZeroMode.class,"resources/Zeros.gif"));
        setMnemonicKey(KeyEvent.VK_0);
        setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_0,0));
        setShortDescription("Add (Shift), remove (Ctrl) or drag zeros");
      } else {
        _poles = true;
        setName("Poles");
        setIcon(loadIcon(PoleZeroMode.class,"resources/Poles.gif"));
        setMnemonicKey(KeyEvent.VK_X);
        setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_X,0));
        setShortDescription("Add (Shift), remove (Ctrl) or drag poles");
      }
    }

    ///////////////////////////////////////////////////////////////////////////
    // protected
    
    protected void setActive(Component component, boolean active) {
      if (component instanceof Tile) {
        if (active) {
          component.addMouseListener(_ml);
        } else {
          component.removeMouseListener(_ml);
        }
      }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private

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
          endEdit(e);
          _tile.removeMouseMotionListener(_mml);
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
      return abs(p.x-x)<4 && abs(p.y-y)<4;
    }

    private void add(MouseEvent e) {
      _tile = (Tile)e.getSource();
      int x = e.getX();
      int y = e.getY();
      Cdouble z = pointToComplex(x,y);
      if (_poles) {
        _pzp.addPole(z);
      } else {
        // TODO: add zero
      }
    }

    private void remove(MouseEvent e) {
      _tile = (Tile)e.getSource();
      int x = e.getX();
      int y = e.getY();
      Cdouble z = pointToComplex(x,y);
      if (_poles) {
        Cdouble pole = _pzp.getPoleNearest(z);
        if (pole!=null && closeEnough(x,y,pole))
          _pzp.removePole(pole);
      } else {
        // TODO: add zero
      }
    }

    private boolean beginEdit(MouseEvent e) {
      _tile = (Tile)e.getSource();
      int x = e.getX();
      int y = e.getY();
      Cdouble z = pointToComplex(x,y);
      if (_poles) {
        Cdouble pole = _pzp.getPoleNearest(z);
        if (pole!=null && closeEnough(x,y,pole)) {
          _pzp.movePole(pole,z);
          _zedit = z;
          return true;
        }
      } else {
        // TODO: move zero
      }
      return false;
    }
    private void duringEdit(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      Cdouble z = pointToComplex(x,y);
      if (_poles) {
        _pzp.movePole(_zedit,z);
      } else {
        // _pzp.moveZero(_zedit,z);
      }
      _zedit = z;
    }
    private void endEdit(MouseEvent e) {
      duringEdit(e);
      _editing = false;
    }
  }
}

