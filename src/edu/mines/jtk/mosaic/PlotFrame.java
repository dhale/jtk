/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import static java.lang.Math.round;
import javax.swing.*;

import edu.mines.jtk.awt.ModeManager;

/**
 * A plot frame is a window containing one or two plot panels. 
 * A plot frame (like any JFrame) has a content pane with a border layout, 
 * and it adds a panel (JPanel) containing its plot panel(s) to the center 
 * of that content pane. Menu and tool bars can be added as for any other 
 * JFrame.
 * <p>
 * Plot frames that contain two plot panels also contain a split pane
 * with either a horizontal (side by side) or vertical (above and below)
 * orientation. The split pane enables interactive resizing of the plot 
 * panels.
 * <p>
 * A plot frame has a single mode manager 
 * ({@link edu.mines.jtk.awt.ModeManager}).
 * When constructed, a plot frame adds and sets active (1) a tile zoom mode 
 * ({@link edu.mines.jtk.mosaic.TileZoomMode}) and (2) a mouse track mode
 * ({@link edu.mines.jtk.mosaic.MouseTrackMode}) to that mode manager. Of
 * course, other modes of interaction can be added as well.
 * <p>
 * The default font and background and foreground colors for a plot frame
 * are Arial (plain, 24 point), white, and black, respectively. Any of 
 * these attributes can be changed. For both simplicity and consistency, 
 * when any of these attributes are set for this frame, they are set for 
 * all components in this frame as well. For example, calling the method 
 * {@link #setFont(java.awt.Font)} will set the font for all panels and, 
 * in turn, all mosaics, tiles, tile axes, color bars, and titles in this 
 * frame.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.31
 */
public class PlotFrame extends JFrame {
  private static final long serialVersionUID = 1L;

  /**
   * Orientation of the split pane (if any) containing two plot panels.
   * If horizontal, two panels are placed side by side. If vertical,
   * two panels are place one above the other. This orientation is
   * unused for plot frames with only one plot panel.
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
    _panelBR = panel;
    _panelMain = new MainPanel();
    _panelMain.setLayout(new BorderLayout());
    _panelMain.add(_panelTL,BorderLayout.CENTER);
    this.setSize(_panelMain.getPreferredSize());
    this.add(_panelMain,BorderLayout.CENTER);
    this.setFont(DEFAULT_FONT);
    this.setBackground(DEFAULT_BACKGROUND);
    this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    addModeManager();
    addMainPanelListener();
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
    double resizeWeight;
    if (_split==Split.HORIZONTAL) {
      _splitPane = new JSplitPane(
        JSplitPane.HORIZONTAL_SPLIT,_panelTL,_panelBR);
      double colTL = _panelTL.getMosaic().countColumns();
      double colBR = _panelBR.getMosaic().countColumns();
      resizeWeight = colTL/(colTL+colBR);
    } else {
      _splitPane = new JSplitPane(
        JSplitPane.VERTICAL_SPLIT,_panelTL,_panelBR);
      double rowTL = _panelTL.getMosaic().countRows();
      double rowBR = _panelBR.getMosaic().countRows();
      resizeWeight = rowTL/(rowTL+rowBR);
    }
    _splitPane.setResizeWeight(resizeWeight);
    _splitPane.setOneTouchExpandable(true);
    _panelMain.add(_splitPane,BorderLayout.CENTER);
    this.setSize(_panelMain.getPreferredSize());
    this.add(_panelMain,BorderLayout.CENTER);
    this.setBackground(DEFAULT_BACKGROUND);
    this.setFont(DEFAULT_FONT);
    this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    addModeManager();
    addMainPanelListener();
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
    return _panelBR;
  }

  /**
   * Gets the mode manager for this plot frame.
   * @return the mode manager.
   */
  public ModeManager getModeManager() {
    return _modeManager;
  }

  /**
   * Gets the tile zoom mode for this plot frame.
   * By default, this mode is active.
   * @return the tile zoom mode.
   */
  public TileZoomMode getTileZoomMode() {
    return _tileZoomMode;
  }

  /**
   * Gets the mouse track mode for this plot frame.
   * By default, this mode is active.
   * @return the mouse track mode.
   */
  public MouseTrackMode getMouseTrackMode() {
    return _mouseTrackMode;
  }

  /**
   * Paints this frame's panel(s) to a PNG image with specified parameters.
   * The image height is computed so that the image has the same aspect 
   * ratio as the panel(s) in this frame.
   * @param dpi the image resolution, in dots per inch.
   * @param win the image width, in inches.
   * @param fileName the name of the file to contain the PNG image.  
   */
  public void paintToPng(
    final double dpi, final double win, final String fileName) 
  {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          _panelMain.paintToPng(dpi,win,fileName);
        } catch (IOException ioe) {
          throw new RuntimeException(ioe);
        }
      }
    });
  }

  /**
   * Sets the font in all components in this frame.
   * @param font the font.
   */
  public void setFont(Font font) {
    super.setFont(font);
    if (_panelMain==null) return;
    _panelMain.setFont(font);
    _panelTL.setFont(font);
    if (_panelBR!=_panelTL)
      _panelBR.setFont(font);
  }

  /**
   * Sets the font size (in points) for all panels in this frame.
   * The font size will not change as this frame is resized.
   * @param size the size.
   */
  public void setFontSize(float size) {
    setFontSizeInternal(size);
    _fontSizeForPrint = false;
    _fontSizeForSlide = false;
  }

  /**
   * Sets font size automatically for a slide in presentations.
   * This method causes the font size to change as this frame is resized,
   * so that text height will be about 1/25 of slide height if this frame
   * is saved to an image.
   * <p>
   * The width/height ratio of the slide is assumed to be 4/3. However,
   * only fractions of the slide width and height may be available if,
   * for example, more than one plot will be shown on the same slide.
   * @param fracWidth the fraction of slide width available.
   * @param fracHeight the fraction of slide height available.
   */
  public void setFontSizeForSlide(double fracWidth, double fracHeight) {
    _fracWidthSlide = fracWidth;
    _fracHeightSlide = fracHeight;
    _fontSizeForSlide = true;
    _fontSizeForPrint = false;
  }

  /**
   * Sets font size to automatically adjust for a printed manuscript.
   * This method causes the font size to change as this frame is resized,
   * so that font size will equal the specified size if this frame is saved
   * to an image and that image is then scaled to fit the specified width.
   * @param fontSize the printed font size (in points) for this plot.
   * @param plotWidth the printed width (in points) of this plot.
   */
  public void setFontSizeForPrint(double fontSize, double plotWidth) {
    _fontSizePrint = fontSize;
    _plotWidthPrint = plotWidth;
    _fontSizeForPrint = true;
    _fontSizeForSlide = false;
  }

  /**
   * Sets the foreground color in all components in this frame.
   * @param color the foreground color.
   */
  public void setForeground(Color color) {
    super.setForeground(color);
    if (_panelMain==null) return;
    _panelMain.setForeground(color);
    _panelTL.setForeground(color);
    if (_panelBR!=_panelTL)
      _panelBR.setForeground(color);
  }

  /**
   * Sets the background color in all components in this frame.
   * @param color the background color.
   */
  public void setBackground(Color color) {
    super.setBackground(color);
    if (_panelMain==null) return;
    _panelMain.setBackground(color);
    _panelTL.setBackground(color);
    if (_panelBR!=_panelTL)
      _panelBR.setBackground(color);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private
  private static void trace(String s) {System.out.println(s);}

  private static final int DEFAULT_WIDTH = 720;
  private static final int DEFAULT_HEIGHT = 550;
  private static final Font DEFAULT_FONT = new Font("Arial",Font.PLAIN,24);
  private static final Color DEFAULT_BACKGROUND = Color.WHITE;

  private PlotPanel _panelTL; // top-left panel
  private PlotPanel _panelBR; // bottom-right panel
  private Split _split; // orientation of split pane; null, if one plot panel
  private JSplitPane _splitPane; // null, if only one plot panel
  private MainPanel _panelMain; // main panel may contain split pane

  private ModeManager _modeManager; // mode manager for this plot frame
  private TileZoomMode _tileZoomMode; // tile zoom mode
  private MouseTrackMode _mouseTrackMode; // mouse track mode

  private boolean _fontSizeForPrint; // true if auto font size for print
  private boolean _fontSizeForSlide; // true if auto font size for slide
  private double _fracWidthSlide; // fraction of slide height available
  private double _fracHeightSlide; // fraction of slide width available
  private double _fontSizePrint; // font size for print
  private double _plotWidthPrint; // plot width for print

  /**
   * A main panel contains either a plot panel or a split pane that
   * contains two plot panels. Note that a JSplitPane is not an IPanel.
   * This class exists to override the method paintToRect of IPanel, so 
   * that the IPanel children of any JSplitPane are painted.
   */
  private class MainPanel extends IPanel {
    private static final long serialVersionUID = 1L;
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

  /**
   * Adds the mode manager to this frame and activates some modes.
   */
  private void addModeManager() {
    _modeManager = new ModeManager();
    _panelTL.getMosaic().setModeManager(_modeManager);
    if (_panelBR!=_panelTL)
      _panelBR.getMosaic().setModeManager(_modeManager);
    _tileZoomMode = new TileZoomMode(_modeManager);
    _mouseTrackMode = new MouseTrackMode(_modeManager);
    _tileZoomMode.setActive(true);
    _mouseTrackMode.setActive(true);
  }

  // Necessary because we have more than one way to set the font size.
  private void setFontSizeInternal(float size) {
    Font font = getFont();
    if (font==null)
      font = UIManager.getFont("Panel.font");
    if (font!=null)
      setFont(font.deriveFont(size));
  }

  // When main panel is resized, we may need to adjust its font size.
  private void addMainPanelListener() {
    _panelMain.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        if (_fontSizeForPrint) {
          adjustFontSizeForPrint();
        } else if (_fontSizeForSlide) {
          adjustFontSizeForSlide();
        }
      }
    });
  }

  // Sets font size for a slide with width/height ratio = 4/3.
  private void adjustFontSizeForSlide() {

    // Width and height of the main panel for this frame.
    double panelWidth = _panelMain.getWidth();
    double panelHeight = _panelMain.getHeight();
    if (panelWidth==0.0 || panelHeight==0.0)
      return;

    // Assume slide width = 4 and height = 3, with arbitrary units.
    double slideWidth = 4.0;
    double slideHeight = 3.0;

    // Only a specified subset of the slide is available for this panel.
    double subsetWidth = slideWidth*_fracWidthSlide;
    double subsetHeight = slideHeight*_fracHeightSlide;

    // The aspect ratio of this panel will typically not match that
    // of the subset available on the slide. Therefore, the panel
    // height may correspond to only a fraction of the subset height.
    // This scale factor is that fraction.
    double scaleHeight = 1.0;
    if (panelWidth*subsetHeight>panelHeight*subsetWidth)
      scaleHeight = (panelHeight*subsetWidth)/(panelWidth*subsetHeight);

    // Text height equals 1/25 of panel height, adjusted for any
    // scaling and our use of only a fraction of slide height.
    double textHeight = panelHeight/scaleHeight/_fracHeightSlide/25.0;

    // The font size is larger than the text height. 
    // The factor 1.25 was determined empirically.
    float fontSize = (float)(1.25*textHeight);
    setFontSizeInternal(fontSize);
  }

  // Sets font size for plots in printed manuscripts.
  private void adjustFontSizeForPrint() {
    double panelWidth = _panelMain.getWidth();
    float fontSize = (float)(_fontSizePrint*panelWidth/_plotWidthPrint);
    setFontSizeInternal(fontSize);
  }
}
