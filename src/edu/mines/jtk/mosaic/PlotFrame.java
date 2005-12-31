/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import edu.mines.jtk.gui.*;
import static java.lang.Math.*;

/**
 * A plot frame is a window containing one or two plot panels. 
 * A plot frame may also contain menu and tool bars.
 * <p>
 * Plot frames that contain two plot panels also contain a split pane
 * with either a horizontal (size-by-side) or vertical (above-and-below)
 * orientation. The split pane enables interactive resizing of the plot 
 * panels.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.25
 */
public class PlotFrame extends JFrame {
  private static final long serialVersionUID = 1L;

  /**
   * Orientation of the split pane (if any) containing two plot panels.
   */
  public enum Split {
    HORIZONTAL,
    VERTICAL
  }

  /**
   * Constructs a plot frame for the specified plot panel.
   * @param panel
   */
  public PlotFrame(PlotPanel panel) {
    _panelTL = panel;
    _panelMain = new MainPanel();
    _panelMain.setLayout(new BorderLayout());
    _panelMain.add(_panelTL,BorderLayout.CENTER);
    this.setLayout(new BorderLayout());
    this.add(_panelMain,BorderLayout.CENTER);
    Mosaic mosaic = _panelTL.getMosaic();
    int nrow = mosaic.countRows();
    int ncol = mosaic.countColumns();
    int width = 200+300*ncol;
    int height = 100+300*nrow;
    this.setSize(width,height);
  }

  /**
   * Constructs a plot frame with two plot panels in a split pane.
   * @param panelTL the top-left panel.
   * @param panelBR the bottom-right panel.
   * @param split the split pane orientation.
   */
  public PlotFrame(PlotPanel panelTL, PlotPanel panelBR, Split split) {
    _panelTL = panelTL;
    _panelBR = panelBR;
    _split = split;
    _panelMain = new MainPanel();
    _panelMain.setLayout(new BorderLayout());
    if (_split==Split.HORIZONTAL) {
      _splitPane = new JSplitPane(
        JSplitPane.HORIZONTAL_SPLIT,_panelTL,_panelBR);
    } else {
      _splitPane = new JSplitPane(
        JSplitPane.VERTICAL_SPLIT,_panelTL,_panelBR);
    }
    _splitPane.setOneTouchExpandable(true);
    _splitPane.setResizeWeight(0.5);
    _panelMain.add(_splitPane,BorderLayout.CENTER);
    this.add(_panelMain,BorderLayout.CENTER);
    Mosaic mosaicTL = _panelTL.getMosaic();
    Mosaic mosaicBR = _panelBR.getMosaic();
    int nrowTL = mosaicTL.countRows();
    int ncolTL = mosaicTL.countColumns();
    int nrowBR = mosaicBR.countRows();
    int ncolBR = mosaicBR.countColumns();
    int width = 0;
    int height = 0;
    if (_split==Split.HORIZONTAL) {
      width += 400+300*(ncolTL+ncolBR);
      height += 200+300*max(nrowTL,nrowBR);
    } else {
      width += 400+300*max(ncolTL,ncolBR);
      height += 200+300*(nrowTL+nrowBR);
    }
    this.setSize(width,height);
  }

  /**
   * Gets the plot panel in this frame. If this frame contains more 
   * than one plot panel, this method returns the top-left panel.
   * @return the plot panel.
   */
  public PlotPanel getPlotPanel() {
    return _panelTL;
  }

  /**
   * Gets the top-left plot panel in this frame. If this panel contains only 
   * one panel, then the top-left and bottom-right panels are the same.
   * @return the top-left plot panel.
   */
  public PlotPanel getPlotPanelTopLeft() {
    return _panelTL;
  }

  /**
   * Gets the bottom-right plot panel in this frame. If this panel contains 
   * only one panel, then the top-left and bottom-right panels are the same.
   * @return the bottom-right plot panel.
   */
  public PlotPanel getPlotPanelBottomRight() {
    return (_panelBR!=null)?_panelBR:_panelTL;
  }

  /**
   * Paints this panel to a PNG image with specified resolution and width.
   * The image height is computed so that the image has the same aspect 
   * ratio as this panel.
   * @param dpi the image resolution, in dots per inch.
   * @param win the image width, in inches.
   * @param fileName the name of the file to contain the PNG image.  
   */
  public void paintToPng(double dpi, double win, String fileName) 
    throws IOException 
  {
    _panelMain.paintToPng(dpi,win,fileName);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Split _split;
  private PlotPanel _panelTL;
  private PlotPanel _panelBR;
  private JSplitPane _splitPane;
  private MainPanel _panelMain;
  private ModeManager _modeManager;

  /**
   * A main panel contains either a plot panel or a split pane that
   * contains two plot panels. Note that a JSplitPane is not an IPanel.
   * This class exists to override the method paintToRect of IPanel, so 
   * that the IPanel children of any JSplitPane are painted.
   */
  private class MainPanel extends IPanel {
    public void paintToRect(Graphics2D g2d, int x, int y, int w, int h) {
      if (_split==null) {
        _panelTL.paintToRect(g2d,x,y,w,h);
      } else {
        double ws = (double)w/(double)_splitPane.getWidth();
        double hs = (double)h/(double)_splitPane.getHeight();
        int nc = _splitPane.getComponentCount();
        for (int ic=0; ic<nc; ++ic) {
          Component c = _splitPane.getComponent(ic);
          int xc = c.getX();
          int yc = c.getY();
          int wc = c.getWidth();
          int hc = c.getHeight();
          xc = (int)round(xc*ws);
          yc = (int)round(yc*hs);
          wc = (int)round(wc*ws);
          hc = (int)round(hc*hs);
          if (c instanceof IPanel) {
            IPanel ip = (IPanel)c;
            ip.paintToRect(g2d,xc,yc,wc,hc);
          }
        }
      }
    }
  }
}
